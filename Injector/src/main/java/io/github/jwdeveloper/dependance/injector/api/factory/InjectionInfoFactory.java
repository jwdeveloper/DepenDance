package io.github.jwdeveloper.dependance.injector.api.factory;


import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.api.util.Pair;


public interface InjectionInfoFactory
{
     Pair<Class<?>, InjectionInfo> create(RegistrationInfo registrationInfo) throws Exception;
}
