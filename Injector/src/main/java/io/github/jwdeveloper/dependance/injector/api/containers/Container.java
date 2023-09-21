package io.github.jwdeveloper.dependance.injector.api.containers;

import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.api.search.ContainerSearch;

import java.lang.reflect.Type;

public interface Container extends Cloneable, ContainerSearch
{
    Object find(Class<?> type, Type... genericParameters);
}
