package tutorial.models;

import lombok.Getter;

@Getter
public class ShopManager
{
    private final Config config;
    private final Shop shop;

    public ShopManager(Config config, Shop shop)
    {
        this.config = config;
        this.shop = shop;
    }
}
