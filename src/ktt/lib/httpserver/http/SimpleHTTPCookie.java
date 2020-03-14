package ktt.lib.httpserver.http;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public SimpleHTTPCookie(final String name, final String value, final String domain, final String path, final String sameSite, final Date expires, final int maxAge, final boolean secure, final boolean httpOnly){
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
