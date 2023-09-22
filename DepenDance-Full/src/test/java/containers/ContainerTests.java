package containers;

import common.ContainerTest;
import common.classess.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ContainerTests extends ContainerTest {


    @Test
    public void shouldRegisterWithType() {
        //Arrange
        var container = builder
                .registerSingleton(ExampleClass.class)
                .registerTransient(ExampleClassV2.class)
                .build();
        shouldBeNotNull(container, ExampleClass.class);
        shouldBeEqualsInstance(container, ExampleClass.class);

        shouldBeNotNull(container, ExampleClassV2.class);
        shouldBeDifferentInstance(container, ExampleClassV2.class);
    }

    @Test
    public void shouldRegisterWithTypeAndImplementation() {
        var container = builder
                .registerSingleton(ExampleInterface.class, ExampleClass.class)
                .registerTransient(ExampleInterfaceV2.class, ExampleClassV2.class)
                .build();
        shouldBeNotNull(container, ExampleInterface.class);
        shouldBeEqualsInstance(container, ExampleInterface.class);

        shouldBeNotNull(container, ExampleInterfaceV2.class);
        shouldBeDifferentInstance(container, ExampleInterfaceV2.class);
    }

    @Test
    public void shouldRegisterWithInstance() {
        var instance = new ExampleClass();
        var container = builder
                .registerSingleton(ExampleClass.class, instance)
                .build();
        shouldBeNotNull(container, ExampleClass.class);
        shouldBeEqualsInstance(container, ExampleClass.class, instance);
    }


    @Test
    public void shouldRegisterWithProvider() {

        var instance = new ExampleClass();
        var container = builder
                .registerSingleton(ExampleClass.class, (e) -> instance)
                .registerTransient(ExampleClassV2.class, (e) -> new ExampleClassV2())
                .build();
        shouldBeNotNull(container, ExampleClass.class);
        shouldBeEqualsInstance(container, ExampleClass.class, instance);

        shouldBeNotNull(container, ExampleClassV2.class);
        shouldBeDifferentInstance(container, ExampleClassV2.class);
    }

    @Test
    public void shouldRegisterSingletonWithList() {

        var container = builder
                .registerSingleton(ExampleCommonInterface.class, ExampleClass.class)
                .registerTransient(ExampleCommonInterface.class, ExampleClassV2.class)
                .registerSingletonList(ExampleCommonInterface.class)
                .build();

        shouldBeNotNull(container, ExampleCommonInterface.class);

        var result = (List<ExampleCommonInterface>) container.find(List.class, ExampleCommonInterface.class);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void shouldRegisterTransientWithList() {

        var container = builder
                .registerSingleton(ExampleCommonInterface.class, ExampleClass.class)
                .registerTransient(ExampleCommonInterface.class, ExampleClassV2.class)
                .registerTransientList(ExampleCommonInterface.class)
                .build();

        shouldBeNotNull(container, ExampleCommonInterface.class);

        var list1 = (List<ExampleCommonInterface>) container.find(List.class, ExampleCommonInterface.class);
        var list2 = (List<ExampleCommonInterface>) container.find(List.class, ExampleCommonInterface.class);

        Assert.assertNotEquals(list1, list2);
    }
}
