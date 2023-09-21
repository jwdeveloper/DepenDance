package io.github.jwdeveloper.dependacne.decorator.implementation;



import io.github.jwdeveloper.dependacne.decorator.api.Decorator;
import io.github.jwdeveloper.dependacne.decorator.api.builder.DecoratorBuilder;
import io.github.jwdeveloper.dependacne.decorator.api.models.DecorationDto;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.enums.RegistrationType;
import io.github.jwdeveloper.dependance.injector.api.factory.InjectionInfoFactory;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecoratorBuilderImpl implements DecoratorBuilder {

    private final Map<Class<?>, List<Class<?>>> registeredTypes;
    private final InjectionInfoFactory injectionInfoFactory;

    public DecoratorBuilderImpl(InjectionInfoFactory injectionInfoFactory,  Map<Class<?>, List<Class<?>>> decorators)
    {
        this.injectionInfoFactory = injectionInfoFactory;
        this.registeredTypes = decorators;
    }

    @Override
    public <T> DecoratorBuilder decorate(Class<T> _interface, Class<? extends T> implementation)
    {
        if(!registeredTypes.containsKey(_interface))
        {
            registeredTypes.put(_interface,new ArrayList<>());
        }
        var implementations = registeredTypes.get(_interface);
        implementations.add(implementation);
        return this;
    }

    @Override
    public Decorator build()  {

        var instanceProvider = new DecoratorInstanceProviderImpl();
        try
        {
            var decorators = createDecoratorDto();
            return new DefaultDecorator(instanceProvider, decorators);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to build decorator",e);
        }
    }

    private Map<Class<?>, DecorationDto> createDecoratorDto() throws Exception {
        var decorators = new HashMap<Class<?>, DecorationDto>();
        for(var pair : registeredTypes.entrySet())
        {
            var injectionsInfoList = new ArrayList<InjectionInfo>();
            for(var implementation : pair.getValue())
            {
                var registrationInfo = new RegistrationInfo(
                        pair.getKey(),
                        implementation,
                        null,
                        LifeTime.SINGLETON,
                        RegistrationType.InterfaceAndIml);
                var pari = injectionInfoFactory.create(registrationInfo);
                injectionsInfoList.add(pari.getValue());
            }
            var decorationDto = new DecorationDto(pair.getKey(), injectionsInfoList);
            decorators.put(pair.getKey(), decorationDto);
        }
        return decorators;
    }
}
