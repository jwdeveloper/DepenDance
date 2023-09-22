package io.github.jwdeveloper.dependance.containers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.jwdeveloper.dependance.exampleClasses.ExampleClass;
import io.github.jwdeveloper.dependance.exampleClasses.ExampleClass2;

public class GuiceContainer extends AbstractModule
{
    public static void run()
    {
        Injector injector = Guice.createInjector(new GuiceContainer());
        injector.getInstance(ExampleClass.class);
    }


    @Override
    protected void configure() {
        bind(ExampleClass.class);
        bind(ExampleClass2.class);
    }
}
