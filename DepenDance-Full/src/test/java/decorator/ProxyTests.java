/*
 * Copyright (c) 2023-2023 jwdeveloper  <jacekwoln@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package decorator;

import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;
import org.junit.Test;

public class ProxyTests {

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

    @Test
    public void should_create_proxy() {
        var dependance = Dependance.newContainer();

        dependance.registerSingleton(ValidationService.class, DefaultValidationService.class);
        dependance.registerProxy(ValidationService.class, SpigotValidationService.class);

        dependance.registerSingleton(Player.class, PlayerImpl.class);
        dependance.registerProxy(Player.class, SpigotPlayer.class);

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
