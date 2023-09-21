package io.github.jwdeveloper.dependacne.decorator.api.annoatations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Decorate
{
    Class<?> target() default Object.class;
}
