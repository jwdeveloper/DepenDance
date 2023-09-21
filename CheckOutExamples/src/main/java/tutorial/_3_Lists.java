package tutorial;

import tutorial.models.LocalShop;
import tutorial.models.OnlineShop;
import tutorial.models.Shop;
import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;

import java.util.List;

public class _3_Lists
{
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
}
