package tutorial;

import tutorial.models.Config;
import tutorial.models.LocalShop;
import tutorial.models.Shop;
import tutorial.models.ShopManager;
import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;

public class _1_Basic {
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
}
