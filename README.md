<p align="center">
    <a href="https://github.com/Ktt-Development/simplehttpserver">
        <img src="https://raw.githubusercontent.com/Ktt-Development/simplehttpserver/main/branding/Logo.png" alt="Logo" width="100" height="100">
    </a>
    <h3 align="center">SimpleHttpServer</h3>
    <p align="center">
        A simplified implementation of the <a href="https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/package-summary.html">sun http server</a> for JDK11. 
        <br />
        This library simplifies complex operations for both the server, exchange, and handlers.
        <br />
        <a href="https://docs.kttdevelopment.com/simplehttpserver/">Docs</a>
        •
        <a href="https://wiki.kttdevelopment.com/simplehttpserver/">Wiki</a>
        •
        <a href="https://github.com/Ktt-Development/simplehttpserver/issues">Issues</a>
    </p>
</p>

[![Deploy](https://github.com/Ktt-Development/simplehttpserver/workflows/Deploy/badge.svg)](https://github.com/Ktt-Development/simplehttpserver/actions?query=workflow%3ADeploy)
[![Java CI](https://github.com/Ktt-Development/simplehttpserver/workflows/Java%20CI/badge.svg)](https://github.com/Ktt-Development/simplehttpserver/actions?query=workflow%3A%22Java+CI%22)
[![Maven Central](https://img.shields.io/maven-central/v/com.kttdevelopment/simplehttpserver)](https://mvnrepository.com/artifact/com.kttdevelopment/simplehttpserver)
[![version](https://img.shields.io/github/v/release/ktt-development/simplehttpserver)](https://github.com/Ktt-Development/simplehttpserver/releases)
[![license](https://img.shields.io/github/license/Ktt-Development/simplehttpserver)](https://github.com/Ktt-Development/simplehttpserver/blob/main/LICENSE)
---


# Setup
Compiled binaries can be found on Maven Central.
[![Maven Central](https://img.shields.io/maven-central/v/com.kttdevelopment/simplehttpserver)](https://mvnrepository.com/artifact/com.kttdevelopment/simplehttpserver)

For projects built locally, compiled binaries can also be found in releases.
[![releases](https://img.shields.io/github/v/release/ktt-development/simplehttpserver?include_prereleases")](https://github.com/Ktt-Development/simplehttpserver/releases)

# Features

## 📋 Complicated tasks made easy

Simplified exchange methods for:
- Parsing HTTP `GET`/`POST` with `multipart/form-data` support.
- Output stream writing with `#send`.
- Sending gzip compressed responses.
- Sending files

```java
SimpleHttpHandler handler = new SimpleHttpHandler(){

    @Override
    public void handle(SimpleHttpExchange exchange){
        Map POST = exchange.getPostMap();
        
        MultipartFormData form = exchange.getMultipartFormData();
        Record record = form.getRecord("record");
        FileRecord file = (FileRecord) form.getRecord("file");

        exchange.send(new File("OK.png"), HttpCode.HTTP_OK, true);
    }

};
```

## ⭐ Extended Features

Out of the box support for:
- HTTP Cookies
- HTTP Sessions
- Multithreaded Servers

```java
SimpleHttpServer server = new SimpleHttpServer(8080);
server.setHttpSessionHandler(new HttpSessionHandler());

HttpHandler handler = new HttpHandler(){

    @Override
    public void handle(HttpExchange exchange){
        HttpSession session = server.getHttpSession(exchange);
        String session_id = session.getSessionID();

        Map<String,String> cookies = exchange.getCookies(); 

        exchange.close();
    }

};
```

## 💻 Simplified Handlers

Easy to use handlers:
- Redirect Handler
- Predicate Handler
- File Handler
- Server-Sent-Events Handler
- Temporary Handler
- Throttled Handler

```java
RedirectHandler redirect = new RedirectHandler("https://github.com/");

FileHandler fileHandler = new FileHandler();
fileHandler.addFile(new File("index.html"));
fileHandler.addDirectory(new File("/site"))

SSEHandler SSE = new SSEHandler();
SSE.push("Server sent events!");

ThrottledHandler throttled = new ThrottledHandler(new HttpHandler(), new ServerExchangeThrottler())
```
