package io.github.jwdeveloper.dependance.injector.api.events.events;

import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
public record OnRegistrationEvent(Class<?> input, InjectionInfo injectionInfo, RegistrationInfo registrationInfo) {
}
