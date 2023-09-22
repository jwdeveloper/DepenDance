package containers;

import common.ContainerTest;
import common.classess.ExampleClass;
import common.classess.exceptionsExamples.ClassWithMoreConstructors;
import common.classess.exceptionsExamples.ClassWithParamterNotRegistered;
import common.classess.exceptionsExamples.deathcycle.A;
import common.classess.exceptionsExamples.deathcycle.B;
import common.classess.exceptionsExamples.deathcycle.C;
import common.classess.exceptionsExamples.deathcycle.CycleDependencyExample;
import io.github.jwdeveloper.dependance.injector.api.exceptions.ContainerException;
import io.github.jwdeveloper.dependance.injector.api.exceptions.DeathCycleException;
import io.github.jwdeveloper.dependance.injector.api.exceptions.InjectionNotFoundException;
import io.github.jwdeveloper.dependance.injector.implementation.utilites.Messages;
import org.junit.Assert;
import org.junit.Test;

public class ContainerExceptionsTests extends ContainerTest {
    @Test
    public void shouldReturnNullWhenNotRegistered() {
        shouldThrows(InjectionNotFoundException.class, () ->
        {
            var container = builder.build();
            container.find(ExampleClass.class);
        });
    }

    @Test
    public void shouldThrowWhenDeathCycle() {
        shouldThrows(DeathCycleException.class, () ->
        {
            builder.registerSingleton(CycleDependencyExample.class)
                    .registerSingleton(A.class)
                    .registerSingleton(B.class)
                    .registerSingleton(C.class)
                    .build();
        });
    }

    @Test
    public void shouldThrowWhenThereAreMoreConstructorsWithoutAnnotation() {
        Assert.assertThrows(Messages.INJECTION_USE_ANNOTATION_WITH_MORE_CONSTUROCTORS, ContainerException.class, () ->
        {
            builder.registerSingleton(ClassWithMoreConstructors.class)
                    .build();
        });
    }

    @Test
    public void shouldThrowsWhenClassHaveTypeInConstructorThatIsNotRegistered() {

        Assert.assertThrows(Messages.INJECTION_CANT_BE_CREATED, ContainerException.class, () ->
        {
            var container = builder.registerSingleton(ClassWithParamterNotRegistered.class)
                    .build();
            container.find(ClassWithParamterNotRegistered.class);
        });
    }
}
