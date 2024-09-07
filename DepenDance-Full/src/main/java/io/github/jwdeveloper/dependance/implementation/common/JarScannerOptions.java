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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Getter
public class JarScannerOptions {
    private Class<?> rootPackage;
    private final Set<Class<?>> includedPackages;
    private final Set<Class<?>> includedClasses;
    private final Set<String> excludedPackages;
    private final Set<String> excludedClasses;
    private final Map<Class,ScannerEvent> scannerEvents;


    public JarScannerOptions() {
        this.excludedPackages = new HashSet<>();
        this.excludedClasses = new HashSet<>();
        this.includedClasses = new HashSet<>();
        this.includedPackages = new HashSet<>();
        this.scannerEvents = new HashMap<>();
    }

    public void setRootPackage(Class<?> rootPackage) {
        this.rootPackage = rootPackage;
    }


    public void includeClass(Class<?> clazz) {
        includedClasses.add(clazz);
    }

    public void includePackage(Class<?> packagee) {
        includedPackages.add(packagee);
    }

    public void includePackage(Class<?> packagee, ScannerEvent event) {
        includePackage(packagee);
        scannerEvents.put(packagee,event);
    }

    public void excludeClass(String packageName) {
        excludedClasses.add(packageName);
    }

    public void excludeClass(Class<?> packageName) {
        excludedClasses.add(packageName.getName());
    }

    public void excludePackage(String packageName) {
        excludedPackages.add(packageName);
    }

    public void excludePackage(Class<?> packageName) {
        excludedPackages.add(packageName.getPackageName());
    }
}
