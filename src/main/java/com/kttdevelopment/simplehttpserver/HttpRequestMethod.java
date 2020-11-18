package com.kttdevelopment.simplehttpserver;

/**
 * A list of expected HTTP method requests.
 * See <a href="https://tools.ietf.org/html/rfc7231#section-4.3" target="_blank">IANA RFC7324 Section 4.3</a>
 * @since 01.00.00
 * @version 4.3.0
 * @author Ktt Development
 */
@SuppressWarnings("unused")
public abstract class HttpRequestMethod {

    public static final String GET          = "GET";
    public static final String HEAD         = "HEAD";
    public static final String POST         = "POST";
    public static final String PUT          = "PUT";
    public static final String DELETE       = "DELETE";
    public static final String CONNECT      = "CONNECT";
    public static final String OPTIONS      = "OPTIONS";
    public static final String TRACE        = "TRACE";
    public static final String PATCH        = "PATCH";
    public static final String UNSUPPORTED  = "UNSUPPORTED";

}
