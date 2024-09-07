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
package io.github.jwdeveloper.dependance.injector.implementation.events;

import io.github.jwdeveloper.dependance.injector.api.events.ContainerEvents;
import io.github.jwdeveloper.dependance.injector.api.events.EventHandler;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;

import java.util.List;

public class EventHandlerImpl implements EventHandler {
    private final List<ContainerEvents> events;

    public EventHandlerImpl(List<ContainerEvents> events) {
        this.events = events;
    }

    @Override
    public boolean OnRegistration(OnRegistrationEvent event) {
        for (int i = events.size() - 1; i >= 0; i--) {
            var handler = events.get(i);
            var res = handler.OnRegistration(event);
            if (!res) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object OnInjection(OnInjectionEvent event) {
        var output = event.output();
        for (int i = events.size() - 1; i >= 0; i--) {
            var handler = events.get(i);
            output = handler.OnInjection(new OnInjectionEvent(event.input(),
                    event.inputGenericParameters(),
                    event.injectionInfo(),
                    output,
                    event.injectionInfoMap(),
                    event.container(),
                    event.source()));
        }
        return output;
    }
}
