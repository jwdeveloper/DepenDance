package tutorial.models;

import lombok.Getter;

@Getter
public class ShopManager
{
    private final Config config;
    private final LocalShop shop;

    public ShopManager(Config config, LocalShop shop)
    {
        this.config = config;
        this.shop = shop;
    }
}
