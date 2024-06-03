package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import org.junit.Assert;

public class _09_Fields {


    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_09_Fields.ExampleSerivce.class)
                .registerTransient(_09_Fields.ExampleClass.class)
                .build();

        _09_Fields.ExampleSerivce example = container.find(_09_Fields.ExampleSerivce.class);
        Assert.assertNotNull(example);
        example.exampleClass.sayIt();
    }

    public static class ExampleSerivce {
        @Inject
        ExampleClass exampleClass;
    }

    public static class ExampleClass {
        public void sayIt() {
            System.out.println("Hello world!");
        }
    }
}
