package io.github.jwdeveloper.dependance.exampleClasses;



public class ExampleClass {
    private final ExampleClass2 exampleClass2;

    @com.google.inject.Inject
    @io.github.jwdeveloper.dependance.injector.api.annotations.Inject
    public ExampleClass(ExampleClass2 exampleClass2) {
        this.exampleClass2 = exampleClass2;
    }

}
