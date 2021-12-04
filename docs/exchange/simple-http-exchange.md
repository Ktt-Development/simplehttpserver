---
title: Simple Http Exchange
description: A simple http exchange simplifies the process of reading and writing to the exchange.
---

# `GET` Request

If a user sends a `GET` request to the server, a map of keys and values of that request can be retrieved by using the `getGetMap` method on the exchange.

```java
// localhost:8080?exampleKey=exampleValue&anotherKey=anotherValue

SimpleHttpHandler handler = new SimpleHttpHandler(){

    @Override
    public void handle(SimpleHttpExchange exchange){
        Map GET = exchange.getGetMap();
        /*
        This returns a map:
        {
            "exampleKey": "exampleValue",
            "anotherKey": "anotherValue"
        }
        */
    }

};
```

# `POST` Request

If the user sends a basic `POST` request to the server, a map of keys and values of that request can be retrieved using the `getPostMap` method on the exchange.

# multipart/form-data

Not all `POST` request are simple key-value requests, this library offers experimental support for `multipart/form-data` requests.

Values can be retrieved using the `getMultipartFormData` method on the exchange as `Records` and `FileRecords`.

Files will be sent through an exchange as a [**byte array**](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Byte.html) and not a [`File`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/File.html).

```java
SimpleHttpHandler handler = new SimpleHttpHandler(){

    @Override
    public void handle(SimpleHttpExchange exchange){
        MultipartFormData form = exchange.getMultipartFormData();
        Record record = form.getRecord("record");
        FileRecord file = (FileRecord) form.getRecord("file");

        final byte[] fileContent = file.getBytes();
    }

}
```

# Response

To send response headers you would first modify the headers inside the `getResponseHeaders` method, and then send them using the `sendResponseHeaders` method. This process is simplified by using any of the [`send`](https://github.com/Ktt-Development/simplehttpserver/tree/main/docs/exchange/simple-http-exchange#sending-data.md) methods in the exchange.

A request may run multiple times if an exchange does not return any headers.

```java
SimpleHttpHandler handler = new SimpleHttpHandler(){

    @Override
    public void handle(SimpleHttpExchange exchange){
        Header headers = exchange.getResponseHeaders();

        exchange.send(200);
    }

}
```

# Sending Data

Data can be sent to the user by using any of the `send` methods. The send method can send data using [byte arrays](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Byte.html), [`Strings`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html), or [`Files`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/File.html).

An optional boolean parameter can also be used to compress files into gzip files for faster exchanges.