---
title: Multithreaded Server
description: |
    A standard http server runs on a single thread, meaning that only one user can access the server at a time. This can be changed to allow multiple simultaneous users by setting the server up as multithreaded.
---

# Multithreaded Server

By default the server runs on a single thread. This means that only one clients exchange can be processed at a time and can lead to long queues.

For a server to be multithreaded the executor must be changed to one that process threads in parallel. The executor can be changed using the `setExecutor` method on the server.

To process a fixed amount of threads you can use [`Executors.newFixedThreadPool(int)`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html#newFixedThreadPool(int)).

```java
SimpleHttpServer server = SimpleHttpServer.create();
// allow up to 10 simultaneous requests
server.setExecutor(Executors.newFixedThreadPool(10));
```

To process an unlimited amount of simultaneous threads you can use [`Executors.newCachedThreadPool()`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html#newCachedThreadPool()).

```java
SimpleHttpServer server = SimpleHttpServer.create();
// allow unlimited simultaneous requests
server.setExecutor(Executors.newCachedThreadPool());
```

# Multithreaded server is not processing requests in parallel

Requests to the same context may not run in parallel for a user that is accessing the same page more than once. This issue is caused by the browser, where it will not send duplicate requests to the server at the same time.

This issue is better explained [here](https://stackoverflow.com/a/58676470).

If you still need to test multithreading then you must use an older browser like Internet Explorer or Microsoft Edge.