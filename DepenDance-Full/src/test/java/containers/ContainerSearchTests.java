/*
 * Copyright (c) 2023-2023 jwdeveloper  <jacekwoln@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package containers;

import common.ContainerTestBase;
import common.classess.*;
import common.classess.annotations.ExampleAnnotation;
import common.classess.annotations.ExampleSuperSuperAnnotation;
import org.junit.Assert;
import org.junit.Test;

public class ContainerSearchTests extends ContainerTestBase {
    @Test
    public void ShouldFindByClassesCommonInterface()  {
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
    public void shouldFindBySuperInterface()  {
        //Arrange
        var container = builder
                .registerSingleton(ExampleCommonInterface.class, ExampleClass.class)
                .build();

        //Act
        var instances = container.findAllByInterface(SuperInterface.class);

        //Assert
        Assert.assertEquals(1, instances.size());
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
    public void ShouldFindBySuperSuperClass()  {
        //Arrange
        var container = builder
                .registerSingleton(ExampleClass.class)
                .registerSingleton(ExampleClassV2.class)
                .build();

        //Act
        var instances = container.findAllBySuperClass(ExampleSuperSuperClass.class);

        //Assert
        Assert.assertEquals(2, instances.size());
    }


    @Test
    public void ShouldFindByAnnotation()  {
        //Arrange
        var container = builder
                .registerSingleton(ExampleClass.class)
                .build();

        //Act
        var instances = container.findAllByAnnotation(ExampleAnnotation.class);
        var superSuperAnnotations = container.findAllByAnnotation(ExampleSuperSuperAnnotation.class);

        //Assert
        Assert.assertEquals(1, instances.size());
        Assert.assertEquals(1, superSuperAnnotations.size());
    }
}
