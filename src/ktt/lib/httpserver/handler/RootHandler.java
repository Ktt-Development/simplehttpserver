package ktt.lib.httpserver.handler;

import com.sun.net.httpserver.HttpHandler;

public class RootHandler extends PredicateHandler {

    public RootHandler(final HttpHandler rootHandler, final HttpHandler elseHandler){
        super(
            rootHandler,
            elseHandler,
            simpleHttpExchange -> simpleHttpExchange.getContext().equalsIgnoreCase("/")
        );
    }

}
