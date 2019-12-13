package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.RequestHandler;

/**
 * By default the Http Server will use the most specific context for requests; This means however, creating a Request Handler using context <code>/</code> will process all requests without a handler instead of sending a 404 response. This workaround will only process requests at <code>/</code> and send all other requests to the alternative handler (typically a 404 page will be set here).
 * @see RequestHandler
 * @see PredicateHandler
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings({"WeakerAccess","unused"})
public final class RootHandler extends PredicateHandler {

    /**
     * Creates a root handler.
     * @param index Http Handler for the context <code>/</code>
     * @param alt Http Handler fot all other contexts
     * @see RequestHandler
     * @since 01.00.00
     */
    public RootHandler(RequestHandler index, RequestHandler alt) {
        super(index, alt, exchangePacket -> exchangePacket.getRequestContext().equals("/"));
    }

}
