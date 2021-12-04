---
title: Cookies
description: |
    Cookies are used to save information for a user across multiple sessions.
---

# Retrieving Cookies

To retrieve cookies from an exchange you use the `getCookies` method on a [simple http exchange](https://github.com/Ktt-Development/simplehttpserver/blob/main/docs/exchange/simple-http-exchange.md).

```java
SimpleHttpHandler handler = new SimpleHttpHandler(){

    @Override
    public void handle(SimpleHttpExchange exchange){
        Map<String,String> cookies = exchange.getCookies();
    }

}
```

# Creating and Modifying Cookies

For simple key-value cookies, the `setCookie` method on a [simple http exchange](https://github.com/Ktt-Development/simplehttpserver/blob/main/docs/exchange/simple-http-exchange.md) can be used.

For cookies with additional parameters the `SimpleHttpCookie.Builder` can be used instead.

The exchange **must** be [sent](https://github.com/Ktt-Development/simplehttpserver/blob/main/docs/exchange/simple-http-exchange#sending-data.md) in order for the cookies to be set.

```java
SimpleHttpHandler handler = new SimpleHttpHandler(){

    @Override
    public void handle(SimpleHttpExchange exchange){
        SimpleHttpCookie cookie = new SimpleHttpCookie
            .Builder("key","value")
            .build();

        exchange.setCookie(cookie);
        exchange.setCookie("anotherKey","value");
        exchange.send(200);
        exchange.close();
    }

}
```