package ktt.lib.httpserver.server;

import ktt.lib.httpserver.handler.HttpHandler;

abstract class HttpContextProvider {

    protected static HttpContext createHttpContext(final String protocol, final String path, final HttpHandler handler, final HttpServer server){

    }

}
