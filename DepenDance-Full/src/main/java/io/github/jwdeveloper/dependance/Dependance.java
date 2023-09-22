package io.github.jwdeveloper.dependance;

import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;

public class Dependance
{
    public static DependanceContainerBuilder newContainer()
    {
        return new DependanceContainerBuilder();
    }
}
