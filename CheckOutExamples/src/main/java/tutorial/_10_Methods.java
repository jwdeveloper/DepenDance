package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.injector.api.annotations.Injection;
import org.junit.Assert;

public class _10_Methods
{
    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_10_Methods.ExampleSerivce.class)
                .registerTransient(_10_Methods.ExampleClass.class)
                .build();

        _10_Methods.ExampleClass example = container.find(_10_Methods.ExampleClass.class);
        Assert.assertNotNull(example);
        example.sayIt();
    }

    public static class ExampleSerivce
    {
        @Injection
        _09_Fields.ExampleClass exampleClassProvider()
        {
            return new _09_Fields.ExampleClass();
        }
    }

    public static class ExampleClass {
        public void sayIt() {
            System.out.println("Hello world!");
        }
    }
}
