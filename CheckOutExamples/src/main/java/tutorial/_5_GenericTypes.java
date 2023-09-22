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

import io.github.jwdeveloper.dependance.Dependance;
import tutorial.models.*;


public class _5_GenericTypes {



    public static void main(String[] args) {

        var shopRepository = new Repository<Shop>();

        var container = Dependance.newContainer()
                .registerTransient(ExampleWithGeneric.class)
                .registerSingleton(Repository.class, shopRepository)
                .configure(configuration ->
                {
                    /**
                     * Unfortunately since java not allow to define class with generic parameter
                     * like Repository<MyGenericType>.class
                     * All cases with generic types (besides lists) must be handled manually in onInjection event
                     */

                    configuration.onInjection(injection ->
                    {
                        if(!injection.input().isAssignableFrom(Repository.class))
                        {
                            return injection.output();
                        }

                        var genericParameter =injection.inputGenericParameters()[0];
                        if(genericParameter.equals(OnlineShop.class))
                        {
                            return new Repository<OnlineShop>();
                        }
                        if(genericParameter.equals(LocalShop.class))
                        {
                            return new Repository<LocalShop>();
                        }
                        return new Repository();
                    });
                })
                .build();


        var example = container.find(ExampleWithGeneric.class);
        var onlineShopRepo = container.find(Repository.class, OnlineShop.class);
        var localShopRepo = container.find(Repository.class, LocalShop.class);
    }


}
