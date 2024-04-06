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
package tutorial;

import io.github.jwdeveloper.dependance.api.DependanceContainer;
import tutorial.models.Config;
import tutorial.models.LocalShop;
import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;

public class _2_Object_Instances
{
    public static void main(String[] args)
    {
        Config myConfigInstance = new Config();
        DependanceContainer container = Dependance.newContainer()
                .registerSingleton(Config.class, myConfigInstance) //in case we want to make instance manually we can put object as second argument
                .registerTransient(LocalShop.class,(di)->
                {
                    //more complex case, we want to find or put manually arguments to created instance
                    //for that we can use lamda resolver that has container as input, and object instance as output
                    var config = (Config)di.find(Config.class);
                    var shop = new LocalShop(config);
                    System.out.println("Shop has been created: "+shop);
                    return shop;
                })
                .build();

        Config config = container.find(Config.class);
        LocalShop shop1 = container.find(LocalShop.class);
        LocalShop shop2 = container.find(LocalShop.class);

        Assert.assertEquals(myConfigInstance,config);
        System.out.println("Config has same instance");

        Assert.assertNotEquals(shop1,shop2);
        System.out.println("Shops has different instances");
    }
}
