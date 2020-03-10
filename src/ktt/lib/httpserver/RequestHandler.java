package ktt.lib.httpserver;

import ktt.lib.httpserver.http.HTTPCode;

import java.io.IOException;
import java.io.UncheckedIOException;

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
@Deprecated
public abstract class RequestHandler implements Authenticator {

    /**
     * Handles the given request and generates a response; returns code 500 error if no response is sent. Information from the client is provided in an {@link ExchangePacket}.
     * @param packet a packet of data containing client information
     * @throws IOException internal failure
     * @see ExchangePacket
     * @since 01.00.00
     */
    public abstract void handle(ExchangePacket packet) throws IOException;

    /**
     * Encapsulates the {@link #handle(ExchangePacket)} for the authenticator. Applications do not normally use this class.
     * @param packet a packet of data containing client information
     * @since 01.00.00
     */
    final void _consume(ExchangePacket packet) {
        if(authenticate(packet)){
            try {
                handle(packet);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }else{
            try {
                packet.send(HTTPCode.HTTP_Unauthorized);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        packet.close();
    }

}
