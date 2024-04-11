package io.github.jwdeveloper.dependance.implementation.common;

import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;

import java.util.List;

public interface ScannerEvent {
    void onScanned(List<Class> classes, DependanceContainerBuilder containerBuilder);
}
