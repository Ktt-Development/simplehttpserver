package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.http.RequestMethod;
import ktt.lib.httpserver.server.SimpleHttpExchange;
import ktt.lib.httpserver.server.SimpleHttpHandler;

import java.io.IOException;
import java.util.*;

/**
 * This handler processes event-streams.
 *
 * @see SimpleHttpHandler
 * @see com.sun.net.httpserver.HttpHandler
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
public class SSEHandler extends SimpleHttpHandler {

    private final List<SimpleHttpExchange> listeners = new ArrayList<>(); // listeners
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
        int latest = 0;
        try{
            latest = Integer.parseInt(exchange.getRequestHeaders().getFirst("Last-Event-ID"));
        }catch(final NumberFormatException | NullPointerException ignored){ }

        for(int index = latest; index < queue.size(); index++)
            exchange.send(queue.get(index));

        listeners.add(exchange);
    }

    /**
     * Pushes an event to the event stream.
     *
     * @param s event data
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void push(final String s){
        id++;
        queue.add(s);
        listeners.forEach(exchange -> {
            final StringBuilder OUT = new StringBuilder();
            OUT.append("id: ").append(id).append("\n");
            OUT.append("data: ").append(s).append("\n\n");
            try{
                exchange.send(OUT.toString());
            }catch(final IOException ignored){}
        });
    }

}
