package ktt.lib.httpserver;

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
public class SimpleHTTPCookie {

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
     * Creates an HTTP cookie. All fields except for <code>name</code>, <code>secure</code>, </code><code>httpOnly</code>, and <code>value</code> can be set to null if unused.
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
    public SimpleHTTPCookie(final String name, final String value, final String domain, final String path, final String sameSite, final Date expires, final Integer maxAge, final boolean secure, final boolean httpOnly){
        this.name = name;
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
     * @return cookie header
     *
     * @see SimpleHttpExchange#setCookie(SimpleHTTPCookie)
     * @since 02.00.00
     * @author Ktt Development
     */
    @SuppressWarnings("ConstantConditions")
    @Override
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
            OUT.append(secure);
        if(httpOnly)
            OUT.append(httpOnly);
       if(sameSite != null)
           OUT.append("; SameSite=").append(sameSite);

       return OUT.toString();
    }

}
