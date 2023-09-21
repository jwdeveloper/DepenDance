package tutorial.models;

import lombok.Getter;

@Getter
public class LocalShop implements Shop {
    private final Config config;

    public LocalShop(Config config)
    {
        this.config = config;
    }
}
