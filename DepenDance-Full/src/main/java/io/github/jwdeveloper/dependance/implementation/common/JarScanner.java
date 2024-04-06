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

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class JarScanner extends ClassLoader {

    @Getter
    private final List<Class<?>> classes;

    private final Map<Class<?>, List<Class<?>>> byInterfaceCatch;

    private final Map<Class<?>, List<Class<?>>> byParentCatch;

    private final Map<Package, List<Class<?>>> byPackageCatch;

    private final Map<Class<? extends Annotation>, List<Class<?>>> byAnnotationCatch;
    private final Logger logger;

    public JarScanner(Class<?> clazz, Logger logger) {
        this.logger = logger;
        classes = loadClasses(clazz);
        byInterfaceCatch = new IdentityHashMap<>();
        byParentCatch = new IdentityHashMap<>();
        byPackageCatch = new IdentityHashMap<>();
        byAnnotationCatch = new HashMap<>();
    }


    public void addClasses(Collection<Class<?>> classes) {
        this.classes.addAll(classes);
    }

    protected List<Class<?>> loadClasses(final Class<?> clazz) {
        var source = clazz.getProtectionDomain().getCodeSource();
        if (source == null) return Collections.emptyList();
        final var url = source.getLocation();
        try {
            final List<Class<?>> classes = new ArrayList<>();
            if (url.toString().endsWith(".jar")) {
                try (final var jar = new JarInputStream(url.openStream())) {
                    JarEntry entry;
                    while ((entry = jar.getNextJarEntry()) != null) {
                        if (entry.isDirectory() || !entry.getName().endsWith(".class")) continue;
                        var name = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                        try {
                            classes.add(Class.forName(name, false, clazz.getClassLoader()));
                        } catch (IncompatibleClassChangeError | NoClassDefFoundError | ClassNotFoundException ignored) {
                        }
                    }
                }
            } else {
                var path = Paths.get(url.toURI());
                Files.walk(path)
                        .filter(Files::isRegularFile)
                        .filter(file -> file.toString().endsWith(".class"))
                        .forEach(file -> {
                            var name = path.relativize(file).toString();
                            name = name.replace(File.separatorChar, '.').substring(0, name.length() - 6);
                            try {
                                classes.add(Class.forName(name, false, clazz.getClassLoader()));
                            } catch (IncompatibleClassChangeError | NoClassDefFoundError |
                                     ClassNotFoundException ignored) {
                            }
                        });
            }
            return classes;
        } catch (IOException | URISyntaxException e) {
            logger.log(Level.SEVERE, "Unable to load classes:", e);
            return Collections.emptyList();
        }
    }

    public void attacheAllClassesFromPackage(Class<?> clazz) {
        classes.addAll(loadClasses(clazz));
        byInterfaceCatch.clear();
        ;
        byAnnotationCatch.clear();
        byPackageCatch.clear();
        ;
        byParentCatch.clear();
        ;
    }


    public Collection<Class<?>> findByAnnotation(Class<? extends Annotation> annotation) {
        if (byAnnotationCatch.containsKey(annotation)) {
            return byAnnotationCatch.get(annotation);
        }
        var result = new ArrayList<Class<?>>();
        for (var _class : classes) {
            if (_class.isAnnotationPresent(annotation)) {
                result.add(_class);
            }
        }
        byAnnotationCatch.put(annotation, result);
        return result;
    }

    public Collection<Class<?>> findByInterface(Class<?> _interface) {
        if (byInterfaceCatch.containsKey(_interface)) {
            return byInterfaceCatch.get(_interface);
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
        byInterfaceCatch.put(_interface, result);
        return result;
    }


    public Collection<Class<?>> findByPackage(Package _package) {
        if (byPackageCatch.containsKey(_package)) {
            return byPackageCatch.get(_package);
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
        byPackageCatch.put(_package, result);
        return result;
    }

    public Collection<Class<?>> findAll() {
        return Collections.unmodifiableList(classes);
    }

}
