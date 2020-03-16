package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.http.RequestMethod;
import ktt.lib.httpserver.server.*;

import java.io.IOException;
import java.util.*;

public class SSEHandler extends SimpleHttpHandler {

    private final List<HttpSession> listeners = new ArrayList<>(); // listeners
    private int id = -1;
    private final LinkedList<String> queue = new LinkedList<>(); // event queue

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers","Content-Type");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", exchange.getRequestHeaders().getFirst("origin"));
        exchange.getResponseHeaders().add("Access-Control-Max-Age","3600");

        if(exchange.getRequestMethod() != RequestMethod.OPTIONS){
            exchange.send(200);
            return;
        }
        exchange.getResponseHeaders().add("content-type","text/event-stream");
        final int latest = exchange.getRequestHeaders().getFirst("Last-Event-ID")
        exchange.send(200);
    }

    public final void addToEventQueue(final String s){
        id++;
        queue.add(s);
    }

}
