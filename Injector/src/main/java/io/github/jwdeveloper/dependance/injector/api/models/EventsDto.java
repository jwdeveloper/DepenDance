package io.github.jwdeveloper.dependance.injector.api.models;


import io.github.jwdeveloper.dependance.injector.api.events.ContainerEvents;


import java.util.List;

public record EventsDto(List<ContainerEvents> events)

{

}
