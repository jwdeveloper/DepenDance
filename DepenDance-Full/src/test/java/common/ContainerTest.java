package common;


import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.DependanceContainerBuilder;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import org.junit.Assert;
import org.junit.Before;


public abstract class ContainerTest {
    public static DependanceContainerBuilder builder;

    @Before
    public  void before() {
        builder = Dependance.newContainer();
    }


    protected void shouldBeNull(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        Assert.assertNull(instance1);
    }

    protected void shouldBeNotNull(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        Assert.assertNotNull(instance1);
    }

    protected void shouldBeDifferentInstance(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        var instance2 = container.find(clazz);
        Assert.assertNotEquals(instance1, instance2);
    }

    protected void shouldBeEqualsInstance(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        var instance2 = container.find(clazz);
        Assert.assertEquals(instance1, instance2);
    }

    protected void shouldBeEqualsInstance(Container container, Class<?> clazz, Object instance) {
        var instance1 = container.find(clazz);
        Assert.assertEquals(instance, instance1);
    }

    protected void shouldThrows(Class<? extends Throwable> throwablClass, Runnable action)
    {
        try
        {
            action.run();
        }
        catch (Exception e)
        {
            if(throwablClass.equals(throwablClass.getClass()))
            {
                return;
            }

            Throwable cause = null;
            do
            {
                 cause = e.getCause();

                 if(cause.getClass().equals(throwablClass))
                 {
                     return;
                 }
            }
            while (cause != null);

            throw e;
        }

    }
}
