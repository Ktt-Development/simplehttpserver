package com.kttdevelopment.simplehttpserver;

import java.util.*;

public class Record {

    private final Map<String,Header> headers;
    private final String name, value;

    @SuppressWarnings({"rawtypes", "unchecked"})
    Record(final Map.Entry<String,Map> entry){
        name  = Objects.requireNonNull(entry.getKey());
        value = Objects.requireNonNull(entry.getValue().get("value").toString());

        final Map<String,Header> headers = new HashMap<>();
        Objects.requireNonNull((Map<String,Map>) entry.getValue().get("headers")).forEach((k, v) -> headers.put(
            k,
            new Header(
                Objects.requireNonNull(v.get("header-name")).toString(),
                Objects.requireNonNull(v.get("header-value")).toString(),
                (Map) Objects.requireNonNull(v.get("parameters"))
            )
        ));
        this.headers = Collections.unmodifiableMap(headers);
    }

    public final String getName(){
        return name;
    }

    public final Header getHeader(final String key){
        return headers.get(key);
    }

    public final Map<String,Header> getHeaders(){
        return headers;
    }

    public final String getValue(){
        return value;
    }

    @Override
    public String toString(){
        return
            "Record"    + '{' +
            "name"      + '=' + '\'' + name + '\''  + ", " +
            "value"     + '=' + '\'' + value + '\'' + ", " +
            "headers"   + '=' + headers +
            '}';
    }

    public static class Header {

        private final String headerName, headerValue;
        private final Map<String,String> headerParams;

        public Header(final String headerName, final String headerValue, final Map<String,String> headerParams){
            this.headerName     = headerName;
            this.headerValue    = headerValue;
            this.headerParams   = Collections.unmodifiableMap(headerParams);
        }

        public final String getHeaderName(){
            return headerName;
        }

        public final String getHeaderValue(){
            return headerValue;
        }

        public final String getParameter(final String key){
            return headerParams.get(key);
        }

        public final Map<String,String> getParameters(){
            return headerParams;
        }

        @Override
        public String toString(){
            return
                "Header"        + '{' +
                "headerName"    + '=' + '\'' + headerName + '\''    + ", " +
                "headerValue"   + '=' + '\'' + headerValue + '\''   + ", " +
                "headerParams"  + '=' + headerParams +
                '}';
        }

    }

}
