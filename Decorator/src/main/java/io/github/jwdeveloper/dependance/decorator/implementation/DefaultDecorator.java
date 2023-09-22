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
package io.github.jwdeveloper.dependance.decorator.implementation;

import io.github.jwdeveloper.dependance.decorator.api.Decorator;
import io.github.jwdeveloper.dependance.decorator.api.DecoratorInstanceProvider;
import io.github.jwdeveloper.dependance.decorator.api.models.DecorationDto;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;


import java.util.Map;

public class DefaultDecorator implements Decorator
{
    private final Map<Class<?>, DecorationDto> decorators;
    private final DecoratorInstanceProvider decoratorInstanceProvider;

    public DefaultDecorator(DecoratorInstanceProvider decoratorInstanceProvider,
                            Map<Class<?>, DecorationDto> decorators) {
        this.decorators = decorators;
        this.decoratorInstanceProvider = decoratorInstanceProvider;
    }

    @Override
    public boolean OnRegistration(OnRegistrationEvent event) {
        return true;
    }

    public Object OnInjection(OnInjectionEvent event){
        var decoratorDto =  decorators.get(event.input());
        if(decoratorDto == null)
        {
            return event.output();
        }
        var result = event.output();
        for(var injectionInfo : decoratorDto.implementations())
        {

            var nextDecorator = decoratorInstanceProvider.getInstance(
                    injectionInfo,
                    event.injectionInfoMap(),
                    result,
                    event.container());
            result = nextDecorator;
        }
        return result;
    }
}
