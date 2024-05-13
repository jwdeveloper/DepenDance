package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import lombok.Getter;
import org.junit.Assert;

public class _9_Fields {
    /**
     * In case you don't want to use constructor, you can just tag fields with attribute @inject
     */

    public static void main(String[] args) {

        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_9_Fields.ExampleClass1.class)
                .registerTransient(_9_Fields.ExampleClass2.class)
                .registerTransient(_9_Fields.FieldsExample.class)
                .build();

        _9_Fields.FieldsExample example = container.find(_9_Fields.FieldsExample.class);
        Assert.assertNotNull(example);
        Assert.assertNotNull(example.getExampleClass1());
        Assert.assertNotNull(example.getExampleClass2());
    }

    @Getter
    public static class FieldsExample {

        @Inject
        private ExampleClass1 exampleClass1;

        @Inject
        private ExampleClass2 exampleClass2;
    }

    public static class ExampleClass1 {

    }

    public static class ExampleClass2 {

    }
}
