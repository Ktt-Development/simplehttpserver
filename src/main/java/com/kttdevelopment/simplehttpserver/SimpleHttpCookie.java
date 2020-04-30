package com.kttdevelopment.simplehttpserver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An HTTP Cookie to be sent in a response header.
 *
 * @see SimpleHttpExchange
 * @since 02.00.00
 * @version 02.00.00
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
    private final boolean
        secure,
        httpOnly;

    /**
     * Creates an HTTP cookie. All fields except for <code>name</code>, <code>secure</code>, <code>httpOnly</code>, and <code>value</code> can be set to null if unused.
     *
     * @deprecated Use {@link Builder} class instead.
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
     *
     */
    @Deprecated
    public SimpleHttpCookie(final String name, final String value, final String domain, final String path, final String sameSite, final Date expires, final Integer maxAge, final boolean secure, final boolean httpOnly){
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

    /**
     * Converts the cookie to a readable string for the cookie header.
     *
     * @deprecated Use {@link #toCookieHeaderString()} instead.
     *
     * @return cookie header
     *
     * @see SimpleHttpExchange#setCookie(SimpleHttpCookie)
     * @since 02.00.00
     * @author Ktt Development
     */
    @SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
    @Override @Deprecated
    public final String toString(){
        StringBuilder OUT = new StringBuilder();

        OUT.append(name).append("=").append(value);
        if(expires != null)
            OUT.append("; Expires=").append(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(expires)).append(" GMT");
        if(maxAge != null)
            OUT.append("; Max-Age=").append(maxAge);
        if(domain != null)
            OUT.append("; Domain=").append(domain);
        if(path != null)
            OUT.append("; Path=").append(path);
        if(secure)
            OUT.append("; Secure=").append(secure);
        if(httpOnly)
            OUT.append("; HttpOnly=").append(httpOnly);
       if(sameSite != null)
           OUT.append("; SameSite=").append(sameSite);

       return OUT.toString();
    }

    public final String toCookieHeaderString(){
        final StringBuilder OUT = new StringBuilder();

        OUT.append(name).append("=").append(value);
        if(expires != null)
            OUT.append("; Expires=").append(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(expires)).append(" GMT");
        if(maxAge != null)
            OUT.append("; Max-Age=").append(maxAge);
        if(domain != null)
            OUT.append("; Domain=").append(domain);
        if(path != null)
            OUT.append("; Path=").append(path);
        if(secure)
            OUT.append("; Secure=").append(secure);
        if(httpOnly)
            OUT.append("; HttpOnly=").append(httpOnly);
       if(sameSite != null)
           OUT.append("; SameSite=").append(sameSite);

       return OUT.toString();
    }

    public static class Builder {

        private final String name;
        private final String value;

        private String domain;
        private String path;
        private String sameSite;
        private Date expires;
        private int maxAge;
        private boolean secure      = false;
        private boolean httpOnly    = false;

        public Builder(final String name, final String value){
            if((this.name = name) == null)
                throw new NullPointerException("Cookie name can not be null");
            if((this.value = value) == null)
                throw new NullPointerException("Cookie value can not be null");
        }

        public final String getName(){
            return name;
        }

        public final String getValue(){
            return value;
        }

        public final String getDomain(){
            return domain;
        }

        public final void setDomain(final String domain){
            this.domain = domain;
        }

        public final String getPath(){
            return path;
        }

        public final void setPath(final String path){
            this.path = path;
        }

        public final String getSameSite(){
            return sameSite;
        }

        public final void setSameSite(final String sameSite){
            this.sameSite = sameSite;
        }

        public final Date getExpires(){
            return expires;
        }

        public final void setExpires(final Date expires){
            this.expires = expires;
        }

        public final int getMaxAge(){
            return maxAge;
        }

        public final void setMaxAge(final int maxAge){
            this.maxAge = maxAge;
        }

        public final boolean isSecure(){
            return secure;
        }

        public final void setSecure(final boolean secure){
            this.secure = secure;
        }

        public final boolean isHttpOnly(){
            return httpOnly;
        }

        public final void setHttpOnly(final boolean httpOnly){
            this.httpOnly = httpOnly;
        }

        public final SimpleHttpCookie build(){
            return new SimpleHttpCookie(name,value,domain,path,sameSite,expires,maxAge,secure,httpOnly);
        }

    }

}
