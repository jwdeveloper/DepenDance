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
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class JarScannerImpl extends ClassLoader implements JarScanner {

    @Getter
    private final List<Class<?>> classes;

    private final Map<Class<?>, List<Class<?>>> byInterfaceCache;

    private final Map<Class<?>, List<Class<?>>> byParentCache;

    private final Map<Package, List<Class<?>>> byPackageCache;
    private final Map<Class<? extends Annotation>, List<Class<?>>> byAnnotationCache;
    private final Logger logger;
    private final JarScannerOptions options;

    public JarScannerImpl(JarScannerOptions options, Logger logger) {
        this.logger = logger;
        this.options = options;
        classes = loadClasses(options.getRootPackage());
        byInterfaceCache = new IdentityHashMap<>();
        byParentCache = new IdentityHashMap<>();
        byPackageCache = new IdentityHashMap<>();
        byAnnotationCache = new HashMap<>();
    }


    public void addClasses(Collection<Class<?>> classes) {
        this.classes.addAll(classes);
    }

    protected List<Class<?>> loadClasses(Class<?> clazz) {
        var source = clazz.getProtectionDomain().getCodeSource();
        if (source == null)
            return Collections.emptyList();
        final var url = source.getLocation();
        try {
            if (url.toString().endsWith(".jar"))
                return loadClassesFromZip(clazz);
            else
                return loadClassesFromFolder(url, clazz);

        } catch (IOException | URISyntaxException e) {
            logger.log(Level.SEVERE, "Unable to load classes:", e);
            return Collections.emptyList();
        }
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
                if (!validateName(name)) {
                    continue;
                }
                try {
                    classes.add(Class.forName(name, false, clazz.getClassLoader()));
                } catch (IncompatibleClassChangeError e) {

                } catch (NoClassDefFoundError | ClassNotFoundException e) {
                    // logger.error("Unable to load class:" + name, e);
                }
            }
            return classes;
        } catch (IOException e) {
            throw e;
        }
    }

    private List<Class<?>> loadClassesFromFolder(URL url, Class<?> clazz) throws URISyntaxException, IOException {

        var path = Paths.get(url.toURI());
        var classes = new ArrayList<Class<?>>();
        Files.walk(path)
                .filter(Files::isRegularFile)
                .filter(file -> file.toString().endsWith(".class"))
                .forEach(file ->
                {
                    var name = path.relativize(file).toString();
                    name = name.replace(File.separatorChar, '.').substring(0, name.length() - 6);
                    if (!validateName(name)) {
                        return;
                    }
                    try {
                        classes.add(Class.forName(name, false, clazz.getClassLoader()));
                    } catch (IncompatibleClassChangeError | NoClassDefFoundError |
                             ClassNotFoundException ignored) {
                    }
                });
        return classes;
    }

    private boolean validateName(String name) {

        if (options.getExcludedClasses().contains(name)) {
            return false;
        }
        if (options.getExcludedPackages().contains(name)) {
            return false;
        }
        return true;
    }

    public void attacheAllClassesFromPackage(Class<?> clazz) {
        classes.addAll(loadClasses(clazz));
        byInterfaceCache.clear();
        byAnnotationCache.clear();
        byPackageCache.clear();
        byParentCache.clear();
    }


    public Collection<Class<?>> findByAnnotation(Class<? extends Annotation> annotation) {
        if (byAnnotationCache.containsKey(annotation)) {
            return byAnnotationCache.get(annotation);
        }
        var result = new ArrayList<Class<?>>();
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
        var result = new ArrayList<Class<?>>();
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
        var result = new ArrayList<Class<?>>();
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
        var result = new ArrayList<Class<?>>();
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
        return Collections.unmodifiableList(classes);
    }

    private static boolean isClassContainsType(Class<?> type, Class<?> searchType) {

        while (true) {
            if (type.isAssignableFrom(searchType)) {
                return true;
            }
            type = type.getSuperclass();

            if(type == null)
            {
                return false;
            }
            if (type.equals(Object.class)) {
                return false;
            }
        }
    }

}
