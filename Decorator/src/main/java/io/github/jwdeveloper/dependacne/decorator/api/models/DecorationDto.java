package io.github.jwdeveloper.dependacne.decorator.api.models;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;

import java.util.List;

public record DecorationDto(Class<?> _interface, List<InjectionInfo> implementations)
{

}
