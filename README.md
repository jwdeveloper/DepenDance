
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


<h1>Install</h1>


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
        <version>[Replace with current version]</version>
    </dependency>     
```

<h1>Examples</h1>


### Basic

```java
public class _1_Basic {


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
### Object Instances

```java
public class _2_Object_Instances
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
### Lists

```java
public class _3_Lists {
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
### Events

```java
public class _4_Events {
    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerSingleton(Shop.class, LocalShop.class)
                .registerSingleton(Shop.class, OnlineShop.class)
                .configure(config ->
                {
                    config.onInjection(_4_Events::onInjection);
                    config.onRegistration(_4_Events::onRegistration);
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
### Generic Types

```java
public class _5_Generic_Types {

    public static void main(String[] args) {

        var container = Dependance.newContainer()
                .configure(configuration ->
                {
                    /*
                     * Since java is not storing information about generic type after compilation
                     * we can not assign class with generic type to variable, so Repository<MyGenericType>.class is not possible
                     * Therefor all cases with generic types (besides lists) must be handled manually in onInjection event
                     */

                    configuration.onInjection(injection ->
                    {
                        if(!injection.input().isAssignableFrom(Repository.class))
                        {
                            return injection.output();
                        }

                        var genericParameter =injection.inputGenericParameters()[0];
                        if(genericParameter.equals(OnlineShop.class))
                        {
                            return new Repository<OnlineShop>();
                        }
                        if(genericParameter.equals(LocalShop.class))
                        {
                            return new Repository<LocalShop>();
                        }
                        return new Repository();
                    });
                })

                .build();


        //first parameter is class, second one is its generic parameter
        var onlineShopRepo = container.find(Repository.class, OnlineShop.class);
        var localShopRepo = container.find(Repository.class, LocalShop.class);

        Assert.assertNotNull(onlineShopRepo);
        Assert.assertNotNull(localShopRepo);
    }


} 
```
### AutoScan

```java
public class _6_AutoScan {

    /**
     *  To avoid boring manually registering Types to container
     *  use `scan` method that is looking for all Classes and Methods
     *  with annotation @Injection and register it automatically
     */

    public static void main(String[] args) {

        /*
         *  package under which code will be scanned should be scanned
         *  scanner is looking for all Method and Classes that are having @Injection annotation
         */

        Class<?> rootClass = _6_AutoScan.class;

        DependanceContainer container = Dependance.newContainer()
                .scan(rootClass)
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
     *
     *     container.registerTransient(OnlineShop.class,container1 ->
     *                 {
     *                     System.out.println("Hello from the online shop factory");
     *                     return new OnlineShop();
     *                 })
     */
    @Injection(lifeTime = LifeTime.TRANSIENT)
    private static OnlineShop onlineShopFactory() {
        System.out.println("Hello from the online shop factory");
        return new OnlineShop();
    }


    /**
     * This is equivalent of
     *
     *     container.registerSingleton(ExampleScannClass.class);
     */
    @Injection(lifeTime = LifeTime.SINGLETON)
    public static class ExampleScannClass {
        public ExampleScannClass() {
            System.out.println("Hello world!");
        }
    }
} 
```
### Overriding

```java
public class _7_Overriding {
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
        Assert.assertEquals(OnlineShop.class, shop.getClass());
        System.out.println("shop object is instance of OnlineShop Class");
    }
} 
```
### ManyConstructors

```java
public class _8_ManyConstructors
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
