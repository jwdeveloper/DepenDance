package io.github.jwdeveloper.dependance;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRun
{
    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(ContainerBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(options).run();
    }
}