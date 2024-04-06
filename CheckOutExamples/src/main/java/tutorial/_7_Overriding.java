package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import org.junit.Assert;
import tutorial.models.*;

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
