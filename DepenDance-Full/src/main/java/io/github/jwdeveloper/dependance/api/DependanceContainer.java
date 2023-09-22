package io.github.jwdeveloper.dependance.api;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;

public interface DependanceContainer extends Container
{
     <T> T find(Class<T> type);
}
