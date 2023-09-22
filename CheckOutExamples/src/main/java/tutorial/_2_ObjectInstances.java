package tutorial;

import tutorial.models.Config;
import tutorial.models.LocalShop;
import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;

public class _2_ObjectInstances
{
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
}
