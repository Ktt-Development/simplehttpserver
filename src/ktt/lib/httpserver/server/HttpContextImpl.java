package ktt.lib.httpserver.server;

import ktt.lib.httpserver.handler.HttpHandler;

import java.net.Authenticator;
import java.util.*;
import java.util.logging.Filter;

@Deprecated
abstract class HttpContextImpl {

    static HttpContext createHttpContext(final String protocol, final String path, final HttpHandler handler, final HttpServer server){
        return new HttpContext() {

            private final HttpServer svr;
            private HttpHandler hdr;
            private final String
                ptcl,
                pth;

            private final Map<String,Object> attr = new HashMap<>();
            private final List<Filter> filters = new ArrayList<>();

            {
                hdr = handler;

                svr = server;

                if(path == null || protocol == null)
                    throw new IllegalArgumentException("Illegal value for path or protocol");
                final String linSlash = path.toLowerCase().replace("\\","/");
                final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
                pth = seSlash.substring(0,seSlash.length()-1);

                if(!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https"))
                    throw new IllegalArgumentException("Illegal value for protocol");
                else
                    ptcl = protocol.toLowerCase();

                // todo: impl auth
            }

        //

            @Override
            public final HttpHandler getHttpHandler(){
                return hdr;
            }

            @Override
            public synchronized final void setHttpHandler(final HttpHandler handler){
                hdr = handler;
            }

        //

            @Override
            public final HttpServer getServer(){
                return svr;
            }

            @Override
            public final String getProtocol(){
                return ptcl;
            }

            @Override
            public final String getPath(){
                return pth;
            }

        //

            @Override
            public synchronized final void setAuthenticator(final Authenticator auth){

            }

            @Override
            public final Authenticator getAuthenticator(){
                return null;
            }

        //

            @Override
            public final Map<String, Object> getAttributes(){
                return attr;
            }

            @Override
            public final List<Filter> getFilters(){
                return filters;
            }

        };
    }

}
