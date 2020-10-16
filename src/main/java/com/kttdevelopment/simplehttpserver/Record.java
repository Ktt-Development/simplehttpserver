package com.kttdevelopment.simplehttpserver;

import java.util.*;

/**
 * This class represents one set of headers and parameters in a multipart/form-data.
 *
 * @see MultipartFormData
 * @see FileRecord
 * @see Header
 * @since 4.0.0
 * @version 4.0.0
 * @author Ktt Development
 */
public class Record {

    private final Map<String,Header> headers;
    private final String name, value;

    /**
     * Creates a record from a map entry. Throws a {@link NullPointerException} if the entry doesn't have a: header-name, header-value, and parameters field.
     *
     * @param entry map entry
     *
     * @since 4.0.0
     * @author Ktt Development
     */
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

    /**
     * Returns form input name.
     *
     * @return form input name
     *
     * @since 4.0.0
     * @author Ktt Development
     */
    public final String getName(){
        return name;
    }

    /**
     * Returns a specified header.
     *
     * @param key header key
     * @return header
     *
     * @see Header
     * @see #getHeaders()
     * @since 4.0.0
     * @author Ktt Development
     */
    public final Header getHeader(final String key){
        return headers.get(key);
    }

    /**
     * Returns all the headers.
     *
     * @return headers
     *
     * @see Header
     * @see #getHeader(String)
     * @since 4.0.0
     * @author Ktt Development
     */
    public final Map<String,Header> getHeaders(){
        return headers;
    }

    /**
     * Returns the value as a string.
     *
     * @return value
     *
     * @since 4.0.0
     * @author Ktt Development
     */
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

    /**
     * Represents a header in a multipart/form-data.
     *
     * @since 4.0.0
     * @version 4.0.0
     * @author Ktt Development
     */
    public static class Header {

        private final String headerName, headerValue;
        private final Map<String,String> headerParams;

        /**
         * Creates header.
         *
         * @param headerName header name
         * @param headerValue header value
         * @param headerParams header parameters
         *
         * @since 4.0.0
         * @author Ktt Development
         */
        Header(final String headerName, final String headerValue, final Map<String,String> headerParams){
            this.headerName     = headerName;
            this.headerValue    = headerValue;
            this.headerParams   = Collections.unmodifiableMap(headerParams);
        }

        /**
         * Returns the header name
         *
         * @return header name
         *
         * @since 4.0.0
         * @author Ktt Development
         */
        public final String getHeaderName(){
            return headerName;
        }

        /**
         * Returns the header value
         *
         * @return header value
         *
         * @since 4.0.0
         * @author Ktt Development
         */
        public final String getHeaderValue(){
            return headerValue;
        }

        /**
         * Returns specific header parameter.
         *
         * @param key parameter key
         * @return parameter value
         *
         * @see #getParameters()
         * @since 4.0.0
         * @author Ktt Development
         */
        public final String getParameter(final String key){
            return headerParams.get(key);
        }

        /**
         * Returns all the header parameters.
         *
         * @return header parameters
         *
         * @see #getParameter(String)
         * @since 4.0.0
         * @author Ktt Development
         */
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
