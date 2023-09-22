
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


<a target="blank" >

<img src="https://jitpack.io/v/jwdeveloper/DepenDance.svg" width="11%" >
</img>
</a>



<a target="blank" >

<img src="https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white" >
</img>
</a>



<a target="blank" >

<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" >
</img>
</a>

</div>

</div>

##  Basic

```java
public static void main(String[] args)
    {
        /*
           - Singleton There will be only one instance of object created by container
           - Transient everytime `container.find` is used new instance is created
         */

        var container = Dependance.newContainer()
                .registerTransient(Shop.class, LocalShop.class) //registration interface to class
                .registerSingleton(Config.class)
                .registerSingleton(ShopManager.class)

                //auto-register all classes with annotation @injection
                // that are in same package as input
                .autoRegistration(_1_Basic.class)
                .build();


        var shopManager1 = (ShopManager)container.find(ShopManager.class);
        var shopManager2 = (ShopManager)container.find(ShopManager.class);

        Assert.assertEquals(shopManager1, shopManager2);
        Assert.assertEquals(shopManager1.getConfig(), shopManager2.getConfig());
        Assert.assertNotEquals(shopManager1.getShop(), shopManager2.getShop());
    }
 
```
##  Object Instances

```java
public static void main(String[] args)
    {
        var myConfigInstance = new Config();
        var container = Dependance.newContainer()
                .registerSingleton(Config.class, myConfigInstance)
                .registerTransient(LocalShop.class,(e)->
                {
                    var config = (Config)e.find(Config.class);
                    var shop = new LocalShop(config);
                    System.out.println("Shop has been created: "+shop);
                    return shop;
                })
                .build();


        var config = (Config)container.find(Config.class);
        var shop1 = container.find(LocalShop.class);
        var shop2 = container.find(LocalShop.class);

        Assert.assertEquals(myConfigInstance, config);
        Assert.assertNotEquals(shop1, shop2);
    }
 
```
##  Lists

```java
public static void main(String[] args)
    {
        var container = Dependance.newContainer()
                .registerTransient(Shop.class, OnlineShop.class)
                .registerTransient(Shop.class, LocalShop.class)
                .registerTransientList(Shop.class)
                .build();


        var shops = (List<Shop>)container.find(List.class,Shop.class);

        Assert.assertNotEquals(2, shops.size());
        Assert.assertNotEquals(OnlineShop.class, shops.get(0).getClass());
        Assert.assertNotEquals(LocalShop.class, shops.get(1).getClass());
    }
 
```
##  Events

```java
public static void main(String[] args)
    {
        var container = Dependance.newContainer()
                .registerSingleton(Shop.class,LocalShop.class)
                .registerSingleton(Shop.class, OnlineShop.class)
                .configure(config ->
                {
                    config.onInjection(_4_Events::onInjection);
                    config.onRegistration(_4_Events::onRegistration);
                })
                .build();

        var shops = container.find(Shop.class, String.class);
    }


    private static Boolean onRegistration(OnRegistrationEvent event)
    {
        System.out.println("onRegistration event: "+event.registrationInfo().implementation().getSimpleName());
        return true; //If false `container.find` injection is not registered to container
    }

    private static Object onInjection(OnInjectionEvent event)
    {
        var inputType = event.input();
        var outputObject = event.output(); //If injection has not been found output is null
        var genericTypes = event.inputGenericParameters();
        var container = event.container();
        var injectonMetadata = event.injectionInfo();

        System.out.println("OnInjection input class: "+inputType.getSimpleName());
        System.out.println("OnInjection output class: "+outputObject.getClass().getSimpleName());
        System.out.println("OnInjection genericTypes: "+genericTypes.length);
        System.out.println("OnInjection metadata: "+injectonMetadata.toString());
        return outputObject;
    }
 
```
##  Generic Types

```java
public static void main(String[] args) {

        var shopRepository = new Repository<Shop>();

        var container = Dependance.newContainer()
                .registerTransient(ExampleWithGeneric.class)
                .registerSingleton(Repository.class, shopRepository)
                .configure(configuration ->
                {
                    /**
                     * Unfortunately since java not allow to define class with generic parameter
                     * like Repository<MyGenericType>.class
                     * All cases with generic types (besides lists) must be handled manually in onInjection event
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


        var example = container.find(ExampleWithGeneric.class);
        var onlineShopRepo = container.find(Repository.class, OnlineShop.class);
        var localShopRepo = container.find(Repository.class, LocalShop.class);
    }


 
```
##  AutoScan

```java
public static void main(String[] args) {

        var container = Dependance.newContainer()
                .autoRegistration(_6_AutoScan.class)
                .configure(config ->
                {
                    config.onAutoScan(autoScanEvent ->
                    {
                       if(autoScanEvent.getTarget().equals(ExampleClass.class))
                       {
                           return false;
                       }
                       return true;
                    });
                })
                .build();


        container.find(ExampleClass.class);
    }
 
```
