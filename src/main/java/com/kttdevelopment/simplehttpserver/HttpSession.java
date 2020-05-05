package com.kttdevelopment.simplehttpserver;

import java.util.HashMap;

/**
 * A session keeps track of a single client across multiple exchanges. This is typically used for login persistence.
 *
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
public abstract class HttpSession {

    static final HashMap<String,HttpSession> sessions = new HashMap<>();

    /**
     * Creates an empty {@link HttpSession}. Applications don't use this method.
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    HttpSession(){ }

//

    /**
     * Returns the session id.
     *
     * @return session id
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract String getSessionID();

//

    /**
     * Returns when the session was created.
     *
     * @return creation time
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract long getCreationTime();

    /**
     * Returns when the session was last used.
     *
     * @return last access time
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract long getLastAccessTime();

    /**
     * Updates the last access time for the session
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void updateLastAccessTime();

}
