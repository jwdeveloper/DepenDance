package io.github.jwdeveloper.dependacne.decorator.implementation;

import io.github.jwdeveloper.dependacne.decorator.api.Decorator;
import io.github.jwdeveloper.dependacne.decorator.api.DecoratorInstanceProvider;
import io.github.jwdeveloper.dependacne.decorator.api.models.DecorationDto;
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
