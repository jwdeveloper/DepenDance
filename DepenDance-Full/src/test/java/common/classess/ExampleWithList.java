package common.classess;


import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import io.github.jwdeveloper.dependance.injector.api.annotations.Injection;
import lombok.Getter;

import java.util.List;

@Injection
public class ExampleWithList
{
    @Getter
    private final List<ExampleInterface> exampleInterfaces;

    @Inject
    public ExampleWithList(List<ExampleInterface> exampleInterfaces) {
        this.exampleInterfaces = exampleInterfaces;
    }
}
