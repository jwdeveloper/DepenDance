package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;

public class _11_ResolveParameters {
    public static void main(String[] args) throws Exception {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_11_ResolveParameters.ExampleWithGenerics.class)
                .registerTransient(_11_ResolveParameters.ExampleClass.class)
                .configure(config ->
                {
                    config.onInjection(onInjectionEvent ->
                    {
                        if (!onInjectionEvent.input().equals(ExampleWithGenerics.class)) {
                            return onInjectionEvent.output();
                        }

                        var geneicsTyles = onInjectionEvent.inputGenericParameters()[0];
                        if (!String.class.equals(geneicsTyles)) {
                            return onInjectionEvent.output();
                        }
                        return new ExampleWithGenerics<String>();
                    });
                })
                .build();

        var method = _11_ResolveParameters.class.getDeclaredMethod(
                "sayHello",
                _11_ResolveParameters.ExampleWithGenerics.class,
                _11_ResolveParameters.ExampleClass.class);

        var parameters = container.resolveParameters(method);

        method.invoke(null, parameters);
    }


    public static void sayHello(_11_ResolveParameters.ExampleWithGenerics<String> exampleService,
                                _11_ResolveParameters.ExampleClass exampleClass) {

        System.out.println("Hello world");
    }


    public static class ExampleWithGenerics<T> {

    }

    public static class ExampleClass {

    }
}
