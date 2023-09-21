package io.github.jwdeveloper.dependance.injector.api.models;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.enums.RegistrationType;


import java.util.function.Function;

public record RegistrationInfo(Class<?> _interface,
                               Class<?> implementation,
                               Function<Container,Object> provider,
                               LifeTime lifeTime,
                               RegistrationType registrationType)
{

}
