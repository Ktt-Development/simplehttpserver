package ktt.lib.httpserver;

import ktt.lib.httpserver.http.HTTPCode;

import java.io.IOException;

/**
 * <i>This class is a simplified implementation of {@link com.sun.net.httpserver.HttpHandler}.</i>
 * <br>
 * A handler used to process HTTP exchanges.
 * @see com.sun.net.httpserver.HttpHandler
 * @see Authenticator
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
public abstract class RequestHandler implements Authenticator {

    /**
     * Handles the given request and generates a response; returns code 500 error if no response is sent. Information from the client is provided in an {@link ExchangePacket}. <b>Exceptions thrown are suppressed by the handler and will not throw a response.</b>
     * @param packet a packet of data containing client information
     * @throws IOException internal failure
     * @throws Exception any exceptions thrown within the handler
     * @see ExchangePacket
     * @since 01.00.00
     */
    public abstract void handle(ExchangePacket packet) throws Exception;

    /**
     * Encapsulates the {@link #handle(ExchangePacket)} for the authenticator. Applications do not normally use this class.
     * @param packet a packet of data containing client information
     * @since 01.00.00
     */
    final void _consume(ExchangePacket packet) {
        if(authenticate(packet)){
            try {
                handle(packet);
            } catch (Exception ignored) { /* internal failure */ }
        }else{
            try {
                packet.send(HTTPCode.HTTP_Unauthorized);
            } catch (IOException e) { /* severe failure */ }
        }
        try {
            packet.send(500);
        } catch (IOException ignored) { /* severe failure */ }
        packet.close();
    }

}
