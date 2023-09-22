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
