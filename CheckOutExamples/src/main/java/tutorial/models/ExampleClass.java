package tutorial.models;

import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import io.github.jwdeveloper.dependance.injector.api.annotations.Injection;

@Injection
public class ExampleClass
{
    private final Config config;

    public ExampleClass(Config config) {
        this.config = config;
    }
}
