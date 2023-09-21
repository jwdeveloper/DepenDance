package tutorial.models;

public class ExampleWithGeneric
{
    private final Repository<OnlineShop> onlineShopRepository;
    private final Repository<LocalShop> localShopRepository;

    public ExampleWithGeneric(Repository<OnlineShop> onlineShopRepository,
                              Repository<LocalShop> localShopRepository) {

        this.onlineShopRepository = onlineShopRepository;
        this.localShopRepository = localShopRepository;
    }
}
