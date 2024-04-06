package io.github.jwdeveloper.dependance.implementation.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class JarScannerOptions {
    private Class<?> rootPackage;
    private List<String> excludedPackages;
    private List<String> excludedClasses;


    public JarScannerOptions() {
        this.excludedPackages = new ArrayList<>();
        this.excludedClasses = new ArrayList<>();
    }

    public void setRootPackage(Class<?> rootPackage) {
        this.rootPackage = rootPackage;
    }


    public void excludeClass(String packageName) {
        excludedClasses.add(packageName);
    }

    public void excludeClass(Class<?> packageName) {
        excludedClasses.add(packageName.getPackageName());
    }

    public void excludePackage(String packageName) {
        excludedPackages.add(packageName);
    }

    public void excludePackage(Class<?> packageName) {
        excludedPackages.add(packageName.getPackageName());
    }
}
