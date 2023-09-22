package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import tutorial.models.*;


public class _5_GenericTypes {



    public static void main(String[] args) {

        var shopRepository = new Repository<Shop>();

        var container = Dependance.newContainer()
                .registerTransient(ExampleWithGeneric.class)
                .registerSingleton(Repository.class, shopRepository)
                .configure(configuration ->
                {
                    /**
                     * Unfortunately since java not allow to define class with generic parameter
                     * like Repository<MyGenericType>.class
                     * All cases with generic types (besides lists) must be handled manually in onInjection event
                     */

                    configuration.onInjection(injection ->
                    {
                        if(!injection.input().isAssignableFrom(Repository.class))
                        {
                            return injection.output();
                        }

                        var genericParameter =injection.inputGenericParameters()[0];
                        if(genericParameter.equals(OnlineShop.class))
                        {
                            return new Repository<OnlineShop>();
                        }
                        if(genericParameter.equals(LocalShop.class))
                        {
                            return new Repository<LocalShop>();
                        }
                        return new Repository();
                    });
                })
                .build();


        var example = container.find(ExampleWithGeneric.class);
        var onlineShopRepo = container.find(Repository.class, OnlineShop.class);
        var localShopRepo = container.find(Repository.class, LocalShop.class);
    }


}
