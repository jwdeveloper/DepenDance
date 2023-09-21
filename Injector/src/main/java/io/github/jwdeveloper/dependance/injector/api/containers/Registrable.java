package io.github.jwdeveloper.dependance.injector.api.containers;

import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;

public interface Registrable
{
    boolean register(RegistrationInfo registrationInfo) throws Exception;
}
