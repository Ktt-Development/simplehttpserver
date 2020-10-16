package com.kttdevelopment.simplehttpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.util.*;

/**
 * This class assigns {@link HttpSession} to every client.
 *
 * @since 03.03.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class HttpSessionHandler {

    private final Map<String,HttpSession> sessions = Collections.synchronizedMap(new HashMap<>());

    private final String cookie;

    /**
     * Creates a session handler using the cookie <code>__session-id</code>.
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public HttpSessionHandler(){
        cookie = "__session-id";
    }

    /**
     * Creates a session handler, storing the id at the specified cookie.
     *
     * @param cookie cookie to store session id
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public HttpSessionHandler(final String cookie){
        this.cookie = cookie;
    }

    /**
     * Assigns a session id to a client. It is important to make sure duplicate ids do not occur.
     *
     * @param exchange http exchange
     * @return session id
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public synchronized String assignSessionID(final HttpExchange exchange){
        String id;
        do id = UUID.randomUUID().toString();
            while(sessions.containsKey(id));
        return id;
    }

    private String getSetSession(final Headers headers){
        if(headers.containsKey("Set-Cookie"))
           for(final String value : headers.get("Set-Cookie"))
               if(value.startsWith(cookie + "="))
                   return value.substring(cookie.length() + 1, value.indexOf(";"));
       return null;
    }

    /**
     * Returns the session of the client or assigns one if it does not yet have one Session will only be saved client side if the exchange headers are sent using {@link HttpExchange#sendResponseHeaders(int, long)}.
     *
     * @param exchange http exchange
     * @return session associated with the exchange
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public final HttpSession getSession(final HttpExchange exchange){
        final String sessionId;
        final HttpSession session;

        @SuppressWarnings("SpellCheckingInspection")
        final String rcookies = exchange.getRequestHeaders().getFirst("Cookie");
        final Map<String,String> cookies = new HashMap<>();

        if(rcookies != null && !rcookies.isEmpty()){
            final String[] pairs = rcookies.split("; ");
            for(final String pair : pairs){
                final String[] value = pair.split("=");
                cookies.put(value[0], value[1]);
            }
        }

        final String setSession = getSetSession(exchange.getResponseHeaders());
        sessionId = setSession != null ? setSession : cookies.get(cookie);

        synchronized(this){
            if(!sessions.containsKey(sessionId)){
                session = new HttpSession() {
                    private final String sessionID;
                    private final long creationTime;
                    private long lastAccessTime;

                    {
                        sessionID = assignSessionID(exchange);
                        creationTime = System.currentTimeMillis();
                        lastAccessTime = creationTime;
                        sessions.put(sessionID, this);
                    }

                    @Override
                    public final String getSessionID(){
                        return sessionID;
                    }

                    //

                    @Override
                    public final long getCreationTime(){
                        return creationTime;
                    }

                    @Override
                    public final long getLastAccessTime(){
                        return lastAccessTime;
                    }

                    @Override
                    public synchronized final void updateLastAccessTime(){
                        lastAccessTime = System.currentTimeMillis();
                    }

                    //

                    @Override
                    public String toString(){
                        return
                            "HttpSession"       + '{' +
                            "sessionID"         + '=' +     sessionID       + ", " +
                            "creationTime"      + '=' +     creationTime    + ", " +
                            "lastAccessTime"    + '=' +     lastAccessTime  +
                            '}';
                    }

                };

                final SimpleHttpCookie out =
                    new SimpleHttpCookie.Builder(cookie, session.getSessionID())
                        .setPath("/")
                        .setHttpOnly(true)
                        .build();
                exchange.getResponseHeaders().add("Set-Cookie", out.toCookieHeaderString());
                sessions.put(session.getSessionID(), session);
            }else{
                session = sessions.get(sessionId);
            }
        }
        return session;
    }

    @Override
    public String toString(){
        return
            "HttpSessionHandler"    + '{' +
            "sessions"              + '=' +     sessions    + ", " +
            "cookie"                + '=' +     cookie      + ", " +
            '}';
    }

}
