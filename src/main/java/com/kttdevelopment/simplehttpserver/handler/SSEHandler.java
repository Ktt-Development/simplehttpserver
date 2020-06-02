package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.SimpleHttpExchange;
import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;
import com.kttdevelopment.simplehttpserver.var.HttpCode;
import com.kttdevelopment.simplehttpserver.var.RequestMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A SSE handler allows server to client events by using an <code>text/event-stream</code>. Events are sent using {@link #push(String)} or {@link #push(String, int, String)}.
 *
 * @see SimpleHttpHandler
 * @since 03.01.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class SSEHandler implements SimpleHttpHandler {

    private final List<OutputStream> listeners = new ArrayList<>();
    private final AtomicInteger      eventId   = new AtomicInteger(-1);
    private final LinkedList<EventStreamRecord> queue = new LinkedList<>();

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        SimpleHttpHandler.super.handle(exchange);
    }

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        if(exchange.getRequestHeaders().getFirst("origin") != null)
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", exchange.getRequestHeaders().getFirst("origin"));
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Max-Age", String.valueOf(TimeUnit.HOURS.toSeconds(1)));
        exchange.getResponseHeaders().add("Cache-Control","no-cache");

        if(exchange.getRequestMethod() == RequestMethod.OPTIONS){
            exchange.sendResponseHeaders(HttpCode.HTTP_OK,0);
            return;
        }

        exchange.getResponseHeaders().put("content-type", Collections.singletonList("text/event-stream"));

        int latest = 0;
        try{
            latest = Integer.parseInt(exchange.getRequestHeaders().getFirst("Last_Event-ID"));
        }catch(final NumberFormatException | NullPointerException ignored){ }

        exchange.sendResponseHeaders(200,0);
        for(int index = latest; index < queue.size(); index++){
            exchange.getOutputStream().write(queue.get(index).toString(eventId.get()).getBytes(StandardCharsets.UTF_8));
            exchange.getOutputStream().flush();
        }

        listeners.add(exchange.getOutputStream());
    }

    /**
     * Pushes an event to the stream.
     *
     * @param data data to send
     *
     * @see #push(String, int, String)
     * @since 03.01.00
     * @author Ktt Development
     */
    public synchronized final void push(final String data){
        push(data,0,"");
    }

    /**
     * Pushes an event to the stream.
     *
     * @param data data to send
     * @param retry how long to retry for
     * @param event event type
     *
     * @see #push(String)
     * @since 03.01.00
     * @author Ktt Development
     */
    public synchronized final void push(final String data, final int retry, final String event){
        eventId.addAndGet(1);
        final EventStreamRecord record = new EventStreamRecord(retry,event,data);
        queue.add(record);
        listeners.forEach(stream -> {
            try{
                stream.write(record.toString(eventId.get()).getBytes(StandardCharsets.UTF_8));
                stream.flush();
            }catch(final IOException ignored){ }
        });
    }

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
                "id: " + id + '\n' +
                (retry > 0 ? "retry: " + retry + '\n' : "") +
                (!event.isBlank() ? "event: " + event + '\n' : "") +
                (!data.isBlank() ? "data: " + data + '\n' : "") +
                '\n';
        }

    }

    @Override
    public String toString(){
        return
            "SSEHandler"    + '{' +
            "listeners"     + '=' +     listeners   + ", " +
            "eventId"       + '=' +     eventId     + ", " +
            "queue"         + '=' +     queue       +
            '}';
    }

}
