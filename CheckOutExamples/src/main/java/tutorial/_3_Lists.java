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
