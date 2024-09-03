package decorator;

import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;
import org.junit.Test;

public class DecoratorProxyTest {

    public static class SpigotValidationServiceImpl implements SpigotValidationService {

    }

    public static class DefaultValidationService implements ValidationService {

    }

    public interface ValidationService {

        default boolean validate() {
            return true;
        }
    }

    public interface SpigotValidationService extends ValidationService {


    }


    public static class PlayerImpl implements Player {

        @Override
        public String name() {
            return "Mark";
        }

        public ValidationService validate() {
            return new DefaultValidationService();
        }

        @Override
        public boolean kick() {
            return true;
        }
    }

    public interface Player {
        String name();

        ValidationService validate();

        default String helloPlayer() {
            return "Hello player";
        }

        boolean kick();

    }

    public interface SpigotPlayer extends Player {
        default String spigotPlayerHello() {
            return "Hello spigot player";
        }

        default String name() {
            return "Adam";
        }

        SpigotValidationService validate();
    }

    public interface VirtualPlayer extends SpigotPlayer {
        default String name() {
            return "virtual";
        }
    }

    @Test
    public void should_create_proxy() {
        var dependance = Dependance.newContainer();

        dependance.registerSingleton(ValidationService.class, DefaultValidationService.class);
        dependance.registerProxy(ValidationService.class, SpigotValidationService.class);

        dependance.registerSingleton(Player.class, PlayerImpl.class);
        dependance.registerProxy(Player.class, SpigotPlayer.class);
        dependance.registerProxy(Player.class, VirtualPlayer.class);

        var container = dependance.build();
        var proxy = container.find(SpigotPlayer.class);

        var validationService = proxy.validate();
        var validateResult = validationService.validate();

        var name = proxy.name();
        var spigotHello = proxy.spigotPlayerHello();
        var playerHello = proxy.helloPlayer();
        var kick = proxy.kick();

        Assert.assertTrue(validateResult);
        Assert.assertEquals("Adam", name);
        Assert.assertEquals("Hello spigot player", spigotHello);
        Assert.assertEquals("Hello player", playerHello);
        Assert.assertTrue(kick);
        Assert.assertNotNull(proxy);
    }

}
