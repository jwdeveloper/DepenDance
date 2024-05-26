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
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ContainerTests extends ContainerTestBase {


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
                .registerTransient(ExampleCommonInterface.class, ExampleClassV3.class)
                .registerSingletonList(ExampleCommonInterface.class)
                .build();

        shouldBeNotNull(container, ExampleCommonInterface.class);

        var result = (List<ExampleCommonInterface>) container.find(List.class, ExampleCommonInterface.class);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(ExampleClass.class, result.get(0).getClass());
        Assert.assertEquals(ExampleClassV2.class, result.get(1).getClass());
        Assert.assertEquals(ExampleClassV3.class, result.get(2).getClass());
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

    @Test
    public void shouldPassListToObjectConstructor() {
        var container = builder
                .registerTransient(ExampleCommonInterface.class, ExampleClass.class)
                .registerTransient(ExampleCommonInterface.class, ExampleClassV2.class)
                .registerTransientList(ExampleCommonInterface.class)
                .registerSingleton(ExampleWithList.class)
                .build();

        var listHolder = (ExampleWithList) container.find(ExampleWithList.class);
        Assert.assertEquals(2, listHolder.getExampleInterfaces().size());
    }

    @Test
    public void shouldRegister2DifferentLists() {
        var container = builder
                .registerSingleton(ExampleInterface.class, new ExampleClass())
                .registerSingleton(ExampleInterface.class, new ExampleClass())
                .registerSingleton(ExampleInterface.class, new ExampleClass())
                .registerSingleton(ExampleInterfaceV2.class, new ExampleClassV2())
                .registerSingleton(ExampleInterfaceV2.class, new ExampleClassV2())
                .registerTransientList(ExampleInterface.class)
                .registerTransientList(ExampleInterfaceV2.class)
                .build();

        var exampleInterface = (List<ExampleInterface>) container.find(List.class, ExampleInterface.class);
        var exampleInterfaceV2 = (List<ExampleInterfaceV2>) container.find(List.class, ExampleInterfaceV2.class);
        Assert.assertEquals(3, exampleInterface.size());
        Assert.assertEquals(2, exampleInterfaceV2.size());
    }

    @Test
    public void shouldHandleCaseWhenGenericTypeIsSame() {
        var container = builder
                .registerSingleton(ExampleInterface.class, new ExampleClass())
                .build();

        var exampleInterface =  container.find(ExampleInterface.class, ExampleInterface.class);
        Assert.assertNotNull(exampleInterface);
    }
}
