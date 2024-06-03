
<div align="center" >


<a target="blank" >

<img src="https://raw.githubusercontent.com/jwdeveloper/DepenDance/master/Tools-Readme/src/main/resources/logo.svg" width="15%" >
</img>
</a>

</div>


<div align="center" >

<h1>DepenDance</h1>


*💉 Inject Simplicity, Eject Complexity 🪲*


<div align="center" >


<a href="https://jitpack.io/#jwdeveloper/DepenDance" target="blank" >

<img src="https://jitpack.io/v/jwdeveloper/DepenDance.svg" width="20%" >
</img>
</a>



<a href="https://discord.gg/2hu6fPPeF7" target="blank" >

<img src="https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white" >
</img>
</a>



<a target="blank" >

<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" >
</img>
</a>

</div>

</div>





```xml

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    <dependency>
        <groupid>com.github.jwdeveloper.DepenDance</groupid>
        <artifactid>DepenDance-Full</artifactid>
        <version>0.0.15-Release</version>
    </dependency>     
```


Lightweight dependency injection container that is both small and performance efficient
<h1>Features</h1>


- [x] Injecting object via constructor

- [x] Method object providers

- [x] Class Scanner to avoid manual registration [Scanner](#autoscan)

- [x] You need to get [List of objects](#lists) in the constructor, no problem

- [x] Create [object instance](#object-instances) by yourself and register it to container!

- [x] Object lifetimes [SINGLETON, TRANSIENT] [see](#basic)

- [x] [Generic types](#generic-types)

- [x] [Many constructors](#manyconstructors)

- [x] Highly customizable, adjust container with build in [events](#events) system


<h1>Tutorial</h1>


[01 Basic](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#01-Basic) 

[02 Object Instances](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#02-Object-Instances) 

[03 Lists](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#03-Lists) 

[04 Events](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#04-Events) 

[05 Generic Types](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#05-Generic-Types) 

[06 AutoScan](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#06-AutoScan) 

[07 Overriding](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#07-Overriding) 

[08 ManyConstructors](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#08-ManyConstructors) 

[09 Fields](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#09-Fields) 

[10 Methods](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#10-Methods) 

[11 ResolveParameters](https://github.com/jwdeveloper/DepenDance?tab=readme-ov-file#11-ResolveParameters) 



### 01 Basic

```java
public class _01_Basic {


    public static void main(String[] args) {
        /*
           - Singleton There will be only one instance of object created by container
           - Transient everytime `container.find` is used new instance of object is created
         */

        DependanceContainer container = Dependance.newContainer()
                .registerTransient(Shop.class, LocalShop.class) //registration interface to implementation
                .registerSingleton(Config.class)
                .registerSingleton(ShopManager.class)
                .build();


        ShopManager shopManager1 = container.find(ShopManager.class);
        ShopManager shopManager2 = container.find(ShopManager.class);

        Shop shop1 = container.find(Shop.class);
        Shop shop2 = container.find(Shop.class);


        Assert.assertEquals(shopManager1, shopManager2);
        System.out.println("There always same instance of shop manager");

        Assert.assertEquals(shopManager1.getConfig(), shopManager2.getConfig());
        System.out.println("There always same instance of config");

        Assert.assertNotEquals(shop1, shop2);
        System.out.println("There are different instances of shop");
    }
} 
```
### 02 Object Instances

```java
public class _02_Object_Instances
{
    public static void main(String[] args)
    {
        Config myConfigInstance = new Config();
        DependanceContainer container = Dependance.newContainer()
                .registerSingleton(Config.class, myConfigInstance) //in case we want to make instance manually we can put object as second argument
                .registerTransient(LocalShop.class,(di)->
                {
                    //more complex case, we want to find or put manually arguments to created instance
                    //for that we can use lamda resolver that has container as input, and object instance as output
                    var config = (Config)di.find(Config.class);
                    var shop = new LocalShop(config);
                    System.out.println("Shop has been created: "+shop);
                    return shop;
                })
                .build();

        Config config = container.find(Config.class);
        LocalShop shop1 = container.find(LocalShop.class);
        LocalShop shop2 = container.find(LocalShop.class);

        Assert.assertEquals(myConfigInstance,config);
        System.out.println("Config has same instance");

        Assert.assertNotEquals(shop1,shop2);
        System.out.println("Shops has different instances");
    }
} 
```
### 03 Lists

```java
public class _03_Lists {
    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerSingleton(Config.class)
                .registerTransient(Shop.class, OnlineShop.class)
                .registerTransient(Shop.class, LocalShop.class)
                .registerTransientList(Shop.class)
                .build();


        List<Shop> shops = (List<Shop>) container.find(List.class, Shop.class);


        for (var shop : shops) {
            System.out.println("Shops: " + shop.getClass().getSimpleName());
        }
        Assert.assertEquals(2, shops.size());
    }


} 
```
### 04 Events

```java
public class _04_Events {
    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerSingleton(Shop.class, LocalShop.class)
                .registerSingleton(Shop.class, OnlineShop.class)
                .configure(config ->
                {
                    config.onInjection(_04_Events::onInjection);
                    config.onRegistration(_04_Events::onRegistration);
                })
                .build();

        Object shops = container.find(Shop.class, String.class);
    }


    private static Boolean onRegistration(OnRegistrationEvent event) {
        System.out.println("onRegistration event: " + event.registrationInfo().implementation().getSimpleName());
        return true; //If false `container.find` injection is not registered to container
    }

    private static Object onInjection(OnInjectionEvent event) {
        var inputType = event.input();// searched class type provided as first parameters
        var genericTypes = event.inputGenericParameters(); //list of generic types provided as second parameter
        var outputObject = event.output(); //Target object instance type has not been found then output value is null
        var container = event.container(); //access to DI container
        var injectonMetadata = event.injectionInfo();

        System.out.println("OnInjection input class: " + inputType.getSimpleName());
        System.out.println("OnInjection output class: " + outputObject.getClass().getSimpleName());
        System.out.println("OnInjection genericTypes: " + genericTypes.length);
        System.out.println("OnInjection metadata: " + injectonMetadata.toString());
        return outputObject;
    }
} 
```
### 05 Generic Types

```java
public class _05_Generic_Types {

    public static void main(String[] args) {

        var container = Dependance.newContainer()
                .registerTransient(ExampleGenericsTypes.class)
                .configure(configuration ->
                {
                    /*
                     * Since java is not storing information about generic type after compilation
                     * we can not assign class with generic type to variable, so Repository<MyGenericType>.class is not possible
                     * Therefor all cases with generic types (besides lists) must be handled manually in onInjection event
                     */

                    configuration.onInjection(injection ->
                    {
                        if (!injection.input().isAssignableFrom(Repository.class)) {
                            return injection.output();
                        }

                        var genericParameter = injection.inputGenericParameters()[0];
                        if (genericParameter.equals(OnlineShop.class)) {
                            return new Repository<OnlineShop>();
                        }
                        if (genericParameter.equals(LocalShop.class)) {
                            return new Repository<LocalShop>();
                        }
                        return new Repository();
                    });
                })

                .build();


        //first parameter is class, second one is its generic parameter
        var exampleGenericsTypes = container.find(ExampleGenericsTypes.class);

        Assert.assertNotNull(exampleGenericsTypes);
        Assert.assertNotNull(exampleGenericsTypes.getLocalShopRepository());
        Assert.assertNotNull(exampleGenericsTypes.getOnlineShopRepository());

    }

    public static class ExampleGenericsTypes {

        @Getter
        private Repository<OnlineShop> onlineShopRepository;
        @Getter
        private Repository<LocalShop> localShopRepository;

        public ExampleGenericsTypes(Repository<OnlineShop> onlineShopRepository,
                                    Repository<LocalShop> localShopRepository) {
            this.onlineShopRepository = onlineShopRepository;
            this.localShopRepository = localShopRepository;
        }
    }


} 
```
### 06 AutoScan

```java
public class _06_AutoScan {

    /**
     * To avoid boring manually registering Types to container
     * use `scan` method that is looking for all Classes and Methods
     * with annotation @Injection and register it automatically
     */

    public static void main(String[] args) {

        /*
         *  package under which code will be scanned should be scanned
         *  scanner is looking for all Method and Classes that are having @Injection annotation
         */

        Class<?> rootClass = _06_AutoScan.class;

        DependanceContainer container = Dependance.newContainer()
                .scan(rootClass)
                .scan(options ->
                {
                    options.setRootPackage(rootClass);
                    options.excludeClass("org.example.ExampleClass");
                    options.excludePackage(String.class.getPackageName());
                })
                .scan(rootClass, (scannedClasses, containerBuilder) ->
                {
                    System.out.println("Hello from scanner those are found classes");
                    for (var clazz : scannedClasses)
                    {
                      System.out.println(clazz.getSimpleName());
                    }
                    System.out.println("============================================");
                })
                .build();

        Config config = container.find(Config.class);
        ExampleClass exampleClass = container.find(ExampleClass.class);
        OnlineShop onlineShop = container.find(OnlineShop.class);
        ExampleScannClass exampleScannClass = container.find(ExampleScannClass.class);

        Assert.assertNotNull(config);
        Assert.assertNotNull(exampleClass);
        Assert.assertNotNull(onlineShop);
        Assert.assertNotNull(exampleScannClass);
    }


    /**
     * This is equivalent of
     * <p>
     * container.registerTransient(OnlineShop.class,container1 ->
     * {
     * System.out.println("Hello from the online shop factory");
     * return new OnlineShop();
     * })
     */
    @Injection(lifeTime = LifeTime.TRANSIENT)
    private static OnlineShop onlineShopFactory() {
        System.out.println("Hello from the online shop factory");
        return new OnlineShop();
    }


    /**
     * This is equivalent of
     * <p>
     * container.registerSingleton(ExampleScannClass.class);
     */
    @Injection(lifeTime = LifeTime.SINGLETON)
    public static class ExampleScannClass {
        public ExampleScannClass() {
            System.out.println("Hello world!");
        }
    }
} 
```
### 07 Overriding

```java
public class _07_Overriding {
    public static void main(String[] args) {

        DependanceContainer container = Dependance.newContainer()
                .registerTransient(Shop.class, OnlineShop.class)
                .registerTransient(Shop.class, OfflineShop.class)
                /**
                 * By again declaring Shop but with different implementation (OnlineShop)
                 * We are telling container to Override (OfflineShop) and always returns (OnlineShop)
                 */
                .build();

        Shop shop = container.find(Shop.class);
        Assert.assertEquals(OfflineShop.class, shop.getClass());
        System.out.println("shop object is instance of OfflineShop Class");
    }
} 
```
### 08 ManyConstructors

```java
public class _08_ManyConstructors
{
    /**
     *  By the default the first constructor is always targeted for injecting parameters
     *  However, sometimes class can have more than one constructor, or we want to use
     *  specific one.
     *
     *  To do that use @Inject annotation over wanted constructor
     *
     */

    public static void main(String[] args) {

        DependanceContainer container = Dependance.newContainer()
                .registerTransient(ExampleClass.class)
                .registerTransient(ManyConstructorsExample.class)
                .build();

        ManyConstructorsExample example = container.find(ManyConstructorsExample.class);
        Assert.assertNotNull(example);
        System.out.println("It works!");
    }

    public static class ManyConstructorsExample
    {

        public ManyConstructorsExample(String a, int b, boolean c)
        {

        }

        @Inject
        public ManyConstructorsExample(ExampleClass c)
        {
            System.out.println("Hello from constructor with ExampleClass parameter");
        }
    }

    public static class ExampleClass
    {

    }
} 
```
### 09 Fields

```java
public class _09_Fields {


    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_09_Fields.ExampleSerivce.class)
                .registerTransient(_09_Fields.ExampleClass.class)
                .build();

        _09_Fields.ExampleSerivce example = container.find(_09_Fields.ExampleSerivce.class);
        Assert.assertNotNull(example);
        example.exampleClass.sayIt();
    }

    public static class ExampleSerivce {
        @Inject
        ExampleClass exampleClass;
    }

    public static class ExampleClass {
        public void sayIt() {
            System.out.println("Hello world!");
        }
    }
} 
```
### 10 Methods

```java
public class _10_Methods
{
    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_10_Methods.ExampleSerivce.class)
                .registerTransient(_10_Methods.ExampleClass.class)
                .build();

        _10_Methods.ExampleClass example = container.find(_10_Methods.ExampleClass.class);
        Assert.assertNotNull(example);
        example.sayIt();
    }

    public static class ExampleSerivce
    {
        @Injection
        _09_Fields.ExampleClass exampleClassProvider()
        {
            return new _09_Fields.ExampleClass();
        }
    }

    public static class ExampleClass {
        public void sayIt() {
            System.out.println("Hello world!");
        }
    }
} 
```
### 11 ResolveParameters

```java
public class _11_ResolveParameters {
    public static void main(String[] args) throws Exception {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_11_ResolveParameters.ExampleWithGenerics.class)
                .registerTransient(_11_ResolveParameters.ExampleClass.class)
                .configure(config ->
                {
                    config.onInjection(onInjectionEvent ->
                    {
                        if (!onInjectionEvent.input().equals(ExampleWithGenerics.class)) {
                            return onInjectionEvent.output();
                        }

                        var geneicsTyles = onInjectionEvent.inputGenericParameters()[0];
                        if (!String.class.equals(geneicsTyles)) {
                            return onInjectionEvent.output();
                        }
                        return new ExampleWithGenerics<String>();
                    });
                })
                .build();

        var method = _11_ResolveParameters.class.getDeclaredMethod(
                "sayHello",
                _11_ResolveParameters.ExampleWithGenerics.class,
                _11_ResolveParameters.ExampleClass.class);

        var parameters = container.resolveParameters(method);

        method.invoke(null, parameters);
    }


    public static void sayHello(_11_ResolveParameters.ExampleWithGenerics<String> exampleService,
                                _11_ResolveParameters.ExampleClass exampleClass) {

        System.out.println("Hello world");
    }


    public static class ExampleWithGenerics<T> {

    }

    public static class ExampleClass {

    }
} 
```
