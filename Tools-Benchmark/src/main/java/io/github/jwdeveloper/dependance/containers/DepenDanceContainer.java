package io.github.jwdeveloper.dependance.containers;

import io.github.jwdeveloper.dependance.ContainerBenchmark;
import io.github.jwdeveloper.dependance.Dependance;

public class DepenDanceContainer
{
    public static void run()
    {
        var container = Dependance.newContainer().registerSingleton(ContainerBenchmark.class).build();
        container.find(ContainerBenchmark.class);
    }
}
