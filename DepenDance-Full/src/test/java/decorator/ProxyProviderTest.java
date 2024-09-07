package decorator;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.decorator.api.annotations.ProxyProvider;
import org.junit.Test;

public class ProxyProviderTest {

    public interface Builder<SELF extends Builder<SELF>> {
        SELF sayHello();

        SELF self();
    }

    public static class BuilderImpl implements Builder {
        @ProxyProvider
        Builder proxyObject;

        @Override
        public Builder sayHello()
        {
            System.out.println("Saying hello!");
            return self();
        }

        @Override
        public Builder self() {
            if (proxyObject != null) {
                System.out.println("Invoking proxy");
                return (Builder) proxyObject;
            }
            System.out.println("Invoking the implementation of Builder");
            return this;
        }
    }

    public interface UserBuilder extends Builder<UserBuilder> {

        default UserBuilder sayName(String name) {
            System.out.println("The name is: " + name);
            return self();
        }
    }

    @Test
    public void testIt() {

        var builder = Dependance.newContainer();
        builder.registerSingleton(Builder.class, BuilderImpl.class);
        builder.registerProxy(Builder.class, UserBuilder.class);
        var container = builder.build();

        var userBuilder = container.find(UserBuilder.class);
        userBuilder.self();
        userBuilder.sayHello();
    }
}
