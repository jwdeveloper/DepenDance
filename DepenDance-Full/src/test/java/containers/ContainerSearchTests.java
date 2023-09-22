package containers;

import common.ContainerTest;
import common.classess.*;
import org.junit.Assert;
import org.junit.Test;

public class ContainerSearchTests extends ContainerTest {
    @Test
    public void ShouldFindByClassesCommonInterface()  {
        //Arrange
        var container = builder
                .registerSingleton(ExampleInterface.class, ExampleClass.class)
                .registerSingleton(ExampleInterfaceV2.class, ExampleClassV2.class)
                .build();

        //Act
        var instances = container.findAllByInterface(ExampleCommonInterface.class);

        //Assert
        Assert.assertEquals(2, instances.size());
    }

    @Test
    public void ShouldFindByRegistrationInterfaceInterface()  {
        //Arrange
        var container = builder
                .registerSingleton(ExampleCommonInterface.class, ExampleClass.class)
                .registerSingleton(ExampleCommonInterface.class, ExampleClassV2.class)
                .build();

        //Act
        var instances = container.findAllByInterface(ExampleCommonInterface.class);

        //Assert
        Assert.assertEquals(2, instances.size());
    }


    @Test
    public void ShouldFindBySuperClass()  {
        //Arrange
        var container = builder
                .registerSingleton(ExampleClass.class)
                .registerSingleton(ExampleClassV2.class)
                .build();

        //Act
        var instances = container.findAllBySuperClass(ExampleSuperClass.class);

        //Assert
        Assert.assertEquals(2, instances.size());
    }


    @Test
    public void ShouldFindByAnnotation()  {
        //Arrange
        var container = builder
                .registerSingleton(ExampleClass.class)
                .registerSingleton(ExampleClassV2.class)
                .build();

        //Act
        var instances = container.findAllByAnnotation(ExampleAnnotation.class);

        //Assert
        Assert.assertEquals(2, instances.size());
    }
}
