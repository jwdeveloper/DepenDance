package tutorial;

import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;
import tutorial.models.LocalShop;
import tutorial.models.OnlineShop;
import tutorial.models.Shop;
import io.github.jwdeveloper.dependance.Dependance;

public class _4_Events
{
    public static void main(String[] args)
    {
        var container = Dependance.newContainer()
                .registerSingleton(Shop.class,LocalShop.class)
                .registerSingleton(Shop.class, OnlineShop.class)
                .configure(config ->
                {
                    config.onInjection(_4_Events::onInjection);
                    config.onRegistration(_4_Events::onRegistration);
                })
                .build();

        var shops = container.find(Shop.class, String.class);
    }


    private static Boolean onRegistration(OnRegistrationEvent event)
    {
        System.out.println("onRegistration event: "+event.registrationInfo().implementation().getSimpleName());
        return true; //If false `container.find` injection is not registered to container
    }

    private static Object onInjection(OnInjectionEvent event)
    {
        var inputType = event.input();
        var outputObject = event.output(); //If injection has not been found output is null
        var genericTypes = event.inputGenericParameters();
        var container = event.container();
        var injectonMetadata = event.injectionInfo();

        System.out.println("OnInjection input class: "+inputType.getSimpleName());
        System.out.println("OnInjection output class: "+outputObject.getClass().getSimpleName());
        System.out.println("OnInjection genericTypes: "+genericTypes.length);
        System.out.println("OnInjection metadata: "+injectonMetadata.toString());
        return outputObject;
    }
}
