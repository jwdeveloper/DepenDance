package io.github.jwdeveloper.dependance.implementation.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
public class JarScannerOptions {
    private Class<?> rootPackage;
    private Set<String> excludedPackages;
    private Set<String> excludedClasses;


    public JarScannerOptions() {
        this.excludedPackages = new HashSet<>();
        this.excludedClasses = new HashSet<>();
    }

    public void setRootPackage(Class<?> rootPackage) {
        this.rootPackage = rootPackage;
    }


    public void addExcludedClass(String packageName) {
        excludedClasses.add(packageName);
    }

    public void addExcludedClass(Class<?> packageName) {
        excludedClasses.add(packageName.getPackageName());
    }

    public void addExcludePackage(String packageName) {
        excludedPackages.add(packageName);
    }

    public void addExcludePackage(Class<?> packageName) {
        excludedPackages.add(packageName.getPackageName());
    }
}
