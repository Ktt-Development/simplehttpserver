package ktt.lib.httpserver.server;

import ktt.lib.httpserver.handler.HttpHandler;

import java.net.Authenticator;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

public abstract class HttpContext {

    HttpContext(){ }

//

    static HttpContext createHttpContext(final String protocol, final String path, final HttpHandler handler, final HttpServer server){
        return HttpContextImpl.createHttpContext(protocol, path, handler, server);
    }

//

    public abstract HttpHandler getHttpHandler();

    public abstract void setHttpHandler(final HttpHandler handler);

//

    public abstract HttpServer getServer();

    public abstract String getProtocol();

    public abstract String getPath();

//

    public abstract void setAuthenticator(final Authenticator auth);

    public abstract Authenticator getAuthenticator();

//

    public abstract Map<String,Object> getAttributes();

    public abstract List<Filter> getFilters();

}
