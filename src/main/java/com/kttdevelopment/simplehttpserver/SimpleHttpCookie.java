package com.kttdevelopment.simplehttpserver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An HTTP Cookie to be sent in a response header.
 *
 * @see SimpleHttpExchange
 * @see Builder
 * @since 02.00.00
 * @version 03.05.08
 * @author Ktt Development
 */
public class SimpleHttpCookie {

    private final String
        name,
        value,
        domain,
        path,
        sameSite;

    private final Date expires;
    private final Integer maxAge;
    private final Boolean
        secure,
        httpOnly;

    /**
     * Creates an HTTP cookie. All fields except for <code>name</code>, <code>secure</code>, <code>httpOnly</code>, and <code>value</code> can be set to null if unused.
     *
     * @param name name of the cookie
     * @param value value of the cookie
     * @param domain what domain to send the cookie to
     * @param path what path to send the cookie to
     * @param sameSite should the cookie be prevented from being sent cross-site
     * @param expires when the cookie expires
     * @param maxAge how long the cookie can live
     * @param secure is the cookie limited to an HTTPS protocol only
     * @param httpOnly should the cookie only be read by the server
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    private SimpleHttpCookie(final String name, final String value, final String domain, final String path, final String sameSite, final Date expires, final Integer maxAge, final Boolean secure, final Boolean httpOnly){
        if(name == null)
            throw new NullPointerException("Cookie name can not be null");
        else
            this.name = name;
        if(value == null)
            throw new NullPointerException("Cookie value can not be null");
        else
            this.value = value;
        this.domain = domain;
        this.path = path;
        this.sameSite = sameSite;
        this.expires = expires;
        this.maxAge = maxAge;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

    /**
     * Converts the cookie to a readable string for a response header.
     *
     * @return cookie as a header string
     *
     * @since 02.03.00
     * @author Ktt Development
     */
    public final String toCookieHeaderString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append(name).append("=").append(value);
        if(expires != null)
            OUT.append("; Expires=").append(sdf.format(expires)).append(" GMT");
        if(maxAge != null)
            OUT.append("; Max-Age=").append(maxAge);
        if(domain != null)
            OUT.append("; Domain=").append(domain);
        if(path != null)
            OUT.append("; Path=").append(path);
        if(secure != null && secure)
            OUT.append("; Secure=").append(true);
        if(httpOnly != null && httpOnly)
            OUT.append("; HttpOnly=").append(true);
       if(sameSite != null)
           OUT.append("; SameSite=").append(sameSite);

       return OUT.toString();
    }

    //

    public String toString(){
        return
            "SimpleHttpCookie"  + '{' +
            "name"              + '=' +     name        + ", " +
            "value"             + '=' +     value       + ", " +
            "expires"           + '=' +     expires     + ", " +
            "maxAge"            + '=' +     maxAge      + ", " +
            "domain"            + '=' +     domain      + ", " +
            "path"              + '=' +     path        + ", " +
            "secure"            + '=' +     secure      + ", " +
            "httpOnly"          + '=' +     httpOnly    + ", " +
            "sameSite"          + '=' +     sameSite    +
            '}';
    }

    /**
     * Builder class for {@link SimpleHttpCookie}.
     *
     * @see SimpleHttpCookie
     * @since 02.03.00
     * @version 02.03.00
     * @author Ktt Development
     */
    public static class Builder {

        private final String name;
        private final String value;

        private String domain;
        private String path;
        private String sameSite;
        private Date expires;
        private Integer maxAge;
        private Boolean secure;
        private Boolean httpOnly;

        /**
         * Creates an HTTP cookie builder given a key and value.
         *
         * @param name Name of the cookie
         * @param value Value of the cookie
         *
         * @since 02.03.00
         * @author Ktt Development
         */
        public Builder(final String name, final String value){
            if((this.name = name) == null)
                throw new NullPointerException("Cookie name can not be null");
            if((this.value = value) == null)
                throw new NullPointerException("Cookie value can not be null");
        }

        /**
         * Returns the name of the cookie.
         *
         * @return cookie name
         *
         * @since 02.00.00
         * @author Ktt Development
         */
        public final String getName(){
            return name;
        }

        /**
         * Returns the value of the cookie.
         *
         * @return cookie value
         *
         * @since 02.03.00
         * @author Ktt Development
         */
        public final String getValue(){
            return value;
        }

        /**
         * Returns the domain to send the cookie to.
         *
         * @return domain to send the cookie to
         *
         * @see #setDomain(String)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final String getDomain(){
            return domain;
        }

        /**
         * Sets the domain of the cookie.
         *
         * @param domain what domain to send the cookie to
         * @return cookie builder
         *
         * @see #getDomain()
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Builder setDomain(final String domain){
            this.domain = domain;
            return this;
        }

        /**
         * Returns the path to send the cookie to.
         *
         * @return what path to send the cookie to
         *
         * @see #setPath(String)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final String getPath(){
            return path;
        }

        /**
         * Sets the path of the cookie.
         *
         * @param path what path to send the cookie to
         * @return cookie builder
         *
         * @see #getPath()
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Builder setPath(final String path){
            this.path = path;
            return this;
        }

        /**
         * Returns if the cookie should be prevented from being sent cross-site.
         *
         * @return if the cookie should be prevented from being sent cross-site.
         *
         * @see #setSameSite(String)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final String isSameSite(){
            return sameSite;
        }

        /**
         * Sets if the cookie should be prevented from being sent cross-site.
         *
         * @param sameSite if the cookie should be prevented from being sent cross-site
         * @return cookie builder
         *
         * @see #isSameSite()
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Builder setSameSite(final String sameSite){
            this.sameSite = sameSite;
            return this;
        }

        /**
         * Returns when the cookie should expire.
         *
         * @return when the cookie should expire.
         *
         * @see #setExpires(Date)
         * @see #getMaxAge()
         * @see #setMaxAge(int)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Date getExpires(){
            return expires;
        }

        /**
         * Sets when the cookie should expire.
         *
         * @param expires when the cookie should expire
         * @return cookie builder
         *
         * @see #getExpires()
         * @see #getMaxAge()
         * @see #setMaxAge(int)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Builder setExpires(final Date expires){
            this.expires = expires;
            return this;
        }

        /**
         * Returns how long the cookie should exist for.
         *
         * @return how long the cookie should exist for
         *
         * @see #getExpires()
         * @see #setExpires(Date)
         * @see #setMaxAge(int)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final int getMaxAge(){
            return maxAge;
        }

        /**
         * Sets how long the cookie should exist for.
         *
         * @param maxAge how long the cookie should exist for
         * @return cookie builder
         *
         * @see #getExpires()
         * @see #setExpires(Date)
         * @see #getMaxAge()
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Builder setMaxAge(final int maxAge){
            this.maxAge = maxAge;
            return this;
        }

        /**
         * Returns if the cookie must be sent over a secure/HTTPS protocol.
         *
         * @return if the cookie must be sent over a secure/HTTPS protocol
         *
         * @see #isSecure()
         * @since 02.03.00
         * @author Ktt Development
         */
        public final boolean isSecure(){
            return secure;
        }

        /**
         * Sets if the cookie must be sent over a secure/HTTPS protocol.
         *
         * @param secure if the cookie must be sent over a secure/HTTPS protocol.
         * @return cookie builder
         *
         * @see #setSecure(boolean)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Builder setSecure(final boolean secure){
            this.secure = secure;
            return this;
        }

        /**
         * Returns if only the server should have access to the cookies.
         *
         * @return if only the server should have access to the cookies.
         *
         * @see #setHttpOnly(boolean)
         * @since 02.03.00
         * @author Ktt Development
         */
        public final boolean isHttpOnly(){
            return httpOnly;
        }

        /**
         * Sets if only the server should have access to the cookies.
         *
         * @param httpOnly if only the server should have access to the cookies
         * @return cookie builder
         *
         * @see #isHttpOnly()
         * @since 02.03.00
         * @author Ktt Development
         */
        public final Builder setHttpOnly(final boolean httpOnly){
            this.httpOnly = httpOnly;
            return this;
        }

        /**
         * Returns the completed cookie.
         *
         * @return simple http cookie
         *
         * @since 02.03.00
         * @author Ktt Development
         */
        public final SimpleHttpCookie build(){
            return new SimpleHttpCookie(name, value, domain, path, sameSite, expires, maxAge, secure, httpOnly);
        }

    }

}
