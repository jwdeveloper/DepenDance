package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import org.junit.Assert;

public class _8_ManyConstructors
{
    /**
     *  By the default the first constructor is always targeted for injecting parameters
     *  However, sometimes class can have more than one constructor, or we want to use
     *  specific one.
     *
     *  To do that use @Inject annotation over wanted constructor
     *
     */

    public static void main(String[] args) {

        DependanceContainer container = Dependance.newContainer()
                .registerTransient(ExampleClass.class)
                .registerTransient(ManyConstructorsExample.class)
                .build();

        ManyConstructorsExample example = container.find(ManyConstructorsExample.class);
        Assert.assertNotNull(example);
        System.out.println("It works!");
    }

    public static class ManyConstructorsExample
    {

        public ManyConstructorsExample(String a, int b, boolean c)
        {

        }

        @Inject
        public ManyConstructorsExample(ExampleClass c)
        {
            System.out.println("Hello from constructor with ExampleClass parameter");
        }
    }

    public static class ExampleClass
    {

    }
}
