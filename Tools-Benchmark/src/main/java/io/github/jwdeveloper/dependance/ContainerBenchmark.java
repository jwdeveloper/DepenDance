package io.github.jwdeveloper.dependance;

import io.github.jwdeveloper.dependance.containers.DepenDanceContainer;
import io.github.jwdeveloper.dependance.containers.GuiceContainer;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class ContainerBenchmark
{
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void GuiceContainerBenchmark() {

        GuiceContainer.run();
    }

    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void DepenDanceContainerBenchmark() {

        DepenDanceContainer.run();
    }
}
