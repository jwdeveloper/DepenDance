/*
 * Copyright (c) 2023-2023 jwdeveloper  <jacekwoln@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.jwdeveloper.dependance.implementation.common;

import io.github.jwdeveloper.dependance.api.JarScanner;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class JarScannerImpl extends ClassLoader implements JarScanner {

    @Getter
    private final Set<Class<?>> classes;
    private final Map<Class<?>, Set<Class<?>>> byInterfaceCache;
    private final Map<Class<?>, Set<Class<?>>> byParentCache;
    private final Map<Package, Set<Class<?>>> byPackageCache;
    private final Map<Class<? extends Annotation>, Set<Class<?>>> byAnnotationCache;
    private final Logger logger;
    private final JarScannerOptions options;
    private PackageScanEvent packageScanEvent;

    public JarScannerImpl(JarScannerOptions options, Logger logger) {
        this.logger = logger;
        this.options = options;
        classes = new HashSet<>();
        byInterfaceCache = new IdentityHashMap<>();
        byParentCache = new IdentityHashMap<>();
        byPackageCache = new IdentityHashMap<>();
        byAnnotationCache = new HashMap<>();
        packageScanEvent = (a, b) -> {
        };
    }


    public void addClasses(Collection<Class<?>> classes) {
        this.classes.addAll(classes);
    }

    public List<Class<?>> initialize() {
        var result = new HashSet<Class<?>>(options.getIncludedClasses());
        var packagesToScan = new HashSet<>(options.getIncludedPackages());
        packagesToScan.add(options.getRootPackage());
        try {
            for (var packagee : packagesToScan) {
                var source = packagee.getProtectionDomain().getCodeSource();
                if (source == null) {
                    throw new RuntimeException("Code source not found for class " + packagee.getName());
                }
                var url = source.getLocation();
                var scannedClasses = new ArrayList<Class>();
                if (url.toString().endsWith(".jar"))
                    scannedClasses.addAll(loadClassesFromZip(packagee));
                else
                    scannedClasses.addAll(loadClassesFromFolder(url, packagee));

                packageScanEvent.onScanned(packagee, scannedClasses);
                for (var clazz : scannedClasses) {
                    result.add(clazz);
                }
            }
        } catch (IOException | URISyntaxException e) {
            logger.log(Level.SEVERE, "Unable to load classes:", e);
            return Collections.emptyList();
        }
        classes.addAll(result);
        return result.stream().toList();
    }

    public void onPackageScan(PackageScanEvent event) {
        this.packageScanEvent = event;
    }


    private List<Class<?>> loadClassesFromZip(Class<?> clazz) throws IOException {
        final var source = clazz.getProtectionDomain().getCodeSource();
        final var url = source.getLocation();
        try (final var zip = new ZipInputStream(url.openStream())) {
            final List<Class<?>> classes = new ArrayList<>();
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory())
                    continue;
                var name = entry.getName();
                if (name.startsWith("META-INF"))
                    continue;
                if (!name.endsWith(".class"))
                    continue;
                name = name.replace('/', '.').substring(0, name.length() - 6);
                var optional = handleSingleClass(name, clazz.getClassLoader());
                if (optional.isEmpty()) {
                    continue;
                }
                classes.add(optional.get());
            }
            return classes;
        } catch (IOException e) {
            throw e;
        }
    }

    private List<Class<?>> loadClassesFromFolder(URL url, Class<?> targetPackage) throws URISyntaxException, IOException {
        var packagePath = targetPackage.getPackage().getName().replace('.', File.separatorChar);
        var basePath = Paths.get(url.toURI());
        var targetPath = basePath.resolve(packagePath);
        var classes = new ArrayList<Class<?>>();
        Files.walk(targetPath)
                .filter(Files::isRegularFile)
                .filter(file -> file.toString().endsWith(".class"))
                .forEach(file -> {
                    var relativePath = basePath.relativize(file).toString();
                    var className = relativePath.replace(File.separatorChar, '.').substring(0, relativePath.length() - 6);
                    if (!className.startsWith(targetPackage.getPackage().getName())) {
                        return;
                    }
                    var optional = handleSingleClass(className, targetPackage.getClassLoader());
                    if (optional.isEmpty()) {
                        return;
                    }
                    classes.add(optional.get());
                });
        return classes;
    }


    private Optional<Class<?>> handleSingleClass(String className, ClassLoader classLoader) {

        if (options.getExcludedClasses().contains(className)) {
            return Optional.empty();
        }
        if (options.getExcludedPackages().contains(toPackageName(className))) {
            return Optional.empty();
        }
        try {
            var clazz = Class.forName(className, false, classLoader);
            return Optional.of(clazz);
        } catch (IncompatibleClassChangeError | NoClassDefFoundError | ClassNotFoundException ignored) {
            return Optional.empty();
        }
    }

    public String toPackageName(String classFullName) {
        int lastDotIndex = classFullName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // No package name
        }
        return classFullName.substring(0, lastDotIndex);
    }

    public void attacheAllClassesFromPackage(Class<?> clazz) {
        options.includePackage(clazz);
        byInterfaceCache.clear();
        byAnnotationCache.clear();
        byPackageCache.clear();
        byParentCache.clear();
        initialize();
    }


    public Collection<Class<?>> findByAnnotation(Class<? extends Annotation> annotation) {
        if (byAnnotationCache.containsKey(annotation)) {
            return byAnnotationCache.get(annotation);
        }
        var result = new HashSet<Class<?>>();
        for (var _class : classes) {
            if (_class.isAnnotationPresent(annotation)) {
                result.add(_class);
            }
        }
        byAnnotationCache.put(annotation, result);
        return result;
    }

    public Collection<Class<?>> findByInterface(Class<?> _interface) {
        if (byInterfaceCache.containsKey(_interface)) {
            return byInterfaceCache.get(_interface);
        }
        var result = new HashSet<Class<?>>();
        for (var _class : classes) {
            for (var _classInterface : _class.getInterfaces()) {
                if (_classInterface.equals(_interface)) {
                    result.add(_class);
                    break;
                }
            }
        }
        byInterfaceCache.put(_interface, result);
        return result;
    }

    public Collection<Class<?>> findBySuperClass(Class<?> superClass) {
        if (byParentCache.containsKey(superClass)) {
            return byParentCache.get(superClass);
        }
        var result = new HashSet<Class<?>>();
        for (var _class : classes) {
            if (isClassContainsType(_class, superClass)) {
                result.add(_class);
            }
        }
        byParentCache.put(superClass, result);
        return result;
    }

    public Collection<Class<?>> findByPackage(Package _package) {
        if (byPackageCache.containsKey(_package)) {
            return byPackageCache.get(_package);
        }
        var result = new HashSet<Class<?>>();
        for (var _class : classes) {
            for (var _classInterface : _class.getInterfaces()) {
                if (_classInterface.getPackage().equals(_package)) {
                    result.add(_class);
                    break;
                }
            }
        }
        byPackageCache.put(_package, result);
        return result;
    }

    @Override
    public Collection<Class<?>> findAll() {
        return classes.stream().toList();
    }

    private static boolean isClassContainsType(Class<?> type, Class<?> searchType) {

        while (true) {
            if (type.isAssignableFrom(searchType)) {
                return true;
            }
            type = type.getSuperclass();

            if (type == null) {
                return false;
            }
            if (type.equals(Object.class)) {
                return false;
            }
        }
    }

}
