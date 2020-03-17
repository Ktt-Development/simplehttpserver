package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.var.RequestMethod;
import ktt.lib.httpserver.SimpleHttpExchange;
import ktt.lib.httpserver.SimpleHttpHandler;

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
    private final LinkedList<EventStreamRecord> queue = new LinkedList<>(); // event queue

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
            exchange.send(queue.get(index).toString(index));

        listeners.add(exchange);
    }

//

    /**
     * Pushes an event to the stream.
     *
     * @param retry how long to reconnect (leave 0 for no retry)
     * @param event event name (leave blank for no event)
     * @param data event data (leave blank for no data)
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public synchronized final void push(final int retry, final String event, final String data){
        id++;
        final EventStreamRecord record = new EventStreamRecord(retry,event,data);
        queue.add(record);
        listeners.forEach(exchange -> {
            try{
                exchange.send(record.toString(id));
            }catch(final IOException ignored){}
        });
    }

//

    private static class EventStreamRecord {

        private final int retry;
        private final String event;
        private final String data;

        public EventStreamRecord(final int retry, final String event, final String data){
            this.retry = retry;
            this.event = event;
            this.data = data;
        }

        public final String toString(final int id){
            return
                "id: " + id + "\n" +
                (retry > 0 ? "retry: " + retry + "\n" : "") +
                (!event.isBlank() ? "event: " + event + "\n" : "") +
                (!data.isBlank() ? "data: " + data + "\n" : "") + "\n";
        }

    }

}
