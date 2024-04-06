package io.github.jwdeveloper.dependance.implementation.common;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;


@Getter
public class JarScannerOptions {
    private Class<?> rootPackage;
    private final Set<Class<?>> includedPackages;
    private final Set<Class<?>> includedClasses;
    private final Set<String> excludedPackages;
    private final Set<String> excludedClasses;


    public JarScannerOptions() {
        this.excludedPackages = new HashSet<>();
        this.excludedClasses = new HashSet<>();
        this.includedClasses = new HashSet<>();
        this.includedPackages = new HashSet<>();
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
