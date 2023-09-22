package io.github.jwdeveloper.dependance.injector.implementation.utilites;

public class Messages
{
    public static final String INJECTION_NOT_FOUND ="Class %s has not been register to Dependency Injection container";

    public static final String INJECTION_ALREADY_EXISTS = "Class %s has been already register inside Dependency Injection container";
    public static final String INJECTION_CANT_BE_CREATED = "Can not create instance injection of type type %s";
    public static final String INJECTION_CANT_REGISTER = "Can not register injection of type type %s";

    public static final String INJECTION_LIST_WITH_INTERFACE = "Only Interface can be use as List parameter %s";
    public static final String INJECTION_USE_ANNOTATION_WITH_MORE_CONSTUROCTORS = "You need to use annotation Inject.class while there is more then one constructor";
    public static final String INJECTION_DETECTED_CYCLE_DEPENDECY = "Detected cycle dependency for type %s in class constructor %s";

    public static final String INJECTION_LIST_ALLOWED_TYPES = "Implementation must be an Interface or Abstract class";
}
