package io.github.jwdeveloper.dependance.implementation.common;

import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;

import java.util.List;

public interface PackageScanEvent
{
    void onScanned(Class _package, List<Class> classes);
}
