---
title: File Handler
description: |
    The file handler is one of the most complex handlers offered by the simple http server library. This handler allowes easy access to files and directories; and control over the location and output of those files.
---

# File Handler

The `FileHandler` is used to deploy files or directories to the server. The response that is sent to the user can be modified using [adapters](#file-handler-adapter).

# File Handler Adapter

A `FileHandlerAdapter` can be used to determine what the server will sent to client and what context the file will be located at.

```java
FileHandlerAdapter adapter = new FileHandlerAdapter(){

    @Override
    public String getName(File file){
        // return file name
        return file.getName().substring(0, file.getName().length - 5);
    }

    @Override
    public byte[] getBytes(File file, byte[] bytes){
        // return bytes from file
        return bytes;
    }

};

File file = new File("index.html");

FileHandler handler = new FileHandler(adapter);
handler.add(file);

HttpServer server = HttpServer.create(8080);

// the file would be accessible at localhost:8080/handler/index
// and return the content of that file
server.createContext("/handler", handler);
```

# Adding Files and Directories

## Context

The context parameter determines where the directory would be located after the handler context and before the file name.

```java
File file = new File("index.html");

FileHandler handler = new FileHandler();
handler.add("/files", file);

HttpServer server = HttpServer.create(8080);

// the file would be accessible at localhost:8080/handler/files/index.html
server.createContext("/handler", handler);
```

## File Name

The file name parameter overrides the file name and adapter name.

```java
File file = new File("index.html");

FileHandler handler = new FileHandler();
handler.add(file, "home");

HttpServer server = HttpServer.create(8080);

// the file would be accessible at localhost:8080/handler/home
server.createContext("/handler", handler);
```

## Byte Loading Option

The byte loading option enum parameter determines how files will be stored on the server.

|Type|Usage|
|---|---|
|`PRELOAD`|Reads file when it is added.|
|`MODLOAD`|Reads file when it is added and any time it is modified.|
|`CACHELOAD`|Reads file when it is added and caches it for a set time. Requires a `CacheFileAdapter`.|
|`LIVELOAD`|Reads the file when it is accessed by the user.|

```java
File file = new File("index.html");

FileHandler handler = new FileHandler();
handler.add(file, ByteLoadingOption.PRELOAD);

HttpServer server = HttpServer.create(8080);

// if the file was modified later,
/// then the server would not reflect the change
server.createContext("/handler", handler);
```

## Walk

The walk parameter (directories only) determines whether to use inner directories or not.

```java
File file = new File("folder");

FileHandler handler = new FileHandler();
handler.addDirectory(file, true);

HttpServer server = HttpServer.create(8080);

// since walk is true, any files in the folder and
// any files inside folders in that folder would be accessible
server.createContext("/handler", handler);
```