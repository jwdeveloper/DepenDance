Tutorial


## Basics

```java
    public static void main(String[] args)
    {
        /*
           - Singleton There will be only one instance of object created by container
           - Transient everytime `container.find` is used new instance is created
         */

        var container = Dependance.newContainer()
                .registerSingleton(Shop.class, ShopManager.class) //registration interface to class
                .registerSingleton(Config.class)
                .registerTransient(LocalShop.class)

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

## Object Instances

```java
        var myConfigInstance = new Config();
        var container = Dependance.newContainer()
                .registerTransient(Config.class, myConfigInstance)
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
```


## Lists


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


## Events 
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


## GenericTypes

```java

  public static void main(String[] args) {

        var shopRepository = new Repository<Shop>();

        var container = Dependance.newContainer()
                .registerTransient(ExampleWithGeneric.class)
                .registerSingleton(Repository.class, shopRepository)
                .configure(configuration ->
                {
                    /**
                     * Unfortuantelly since java not allow to define class with generic parameter
                     * like Repository<MyGenericType>.class
                     * All cases with generic types must be handled manually in onInjection event
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