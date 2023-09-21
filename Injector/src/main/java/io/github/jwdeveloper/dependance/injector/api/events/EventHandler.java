package io.github.jwdeveloper.dependance.injector.api.events;

import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;
public interface EventHandler extends ContainerEvents
{
     boolean OnRegistration(OnRegistrationEvent event);
     Object OnInjection(OnInjectionEvent event) ;
}
