package io.github.jwdeveloper.dependance.injector.implementation.containers;

import io.github.jwdeveloper.dependance.injector.api.containers.ContainerConfiguration;
import io.github.jwdeveloper.dependance.injector.api.events.ContainerEvents;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


@Getter
public class ContainerConfigurationImpl implements ContainerConfiguration {
   private final List<RegistrationInfo> registrations = new ArrayList<>();

   private final Set<Class<?>> registerdTypes = new HashSet<>();
   private final List<ContainerEvents> events = new ArrayList<>();

   private final List<RegistrationInfo> registerAsList = new ArrayList<>();
   public void addRegistration(RegistrationInfo info)
   {
      registrations.add(info);
   }

   public void addRegistration(List<RegistrationInfo> info)
   {
      registrations.addAll(info);
   }

   public void onInjection(Function<OnInjectionEvent,Object> event)
   {
      events.add(new ContainerEvents() {
         @Override
         public boolean OnRegistration(OnRegistrationEvent e) {
            return true;
         }

         @Override
         public Object OnInjection(OnInjectionEvent e)  {
            return event.apply(e);
         }
      });
   }

   public void onRegistration(Function<OnRegistrationEvent,Boolean> event)
   {
      events.add(new ContainerEvents() {
         @Override
         public boolean OnRegistration(OnRegistrationEvent e) {
            return event.apply(e);
         }
         @Override
         public Object OnInjection(OnInjectionEvent e) {
            return e.output();
         }
      });
   }

   @Override
   public void onEvent(ContainerEvents event) {
         events.add(event);
   }
}
