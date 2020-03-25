package com.kttdevelopment.simplehttpserver;

import java.util.UUID;

/**
 * Implementation for {@link HttpSession}. Applications do not use this class.
 *
 * @see HttpSession
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
abstract class HttpSessionImpl {

    /**
     * Creates a {@link HttpSession}
     *
     * @return an {@link HttpSession}
     *
     * @see HttpSession
     * @since 02.00.00
     * @author Ktt Development
     */
    synchronized static HttpSession createHttpSession(){
        return new HttpSession() {

            private final String sessionID;
            private final long creationTime;
            private long lastAccessTime;

            {
                sessionID = UUID.randomUUID().toString();
                creationTime = System.currentTimeMillis();
                lastAccessTime = creationTime;
                sessions.put(sessionID,this);
            }

            @Override
            public final String getSessionID(){
                return sessionID;
            }

        //

            @Override
            public final long getCreationTime(){
                return creationTime;
            }

            @Override
            public final long getLastAccessTime(){
                return lastAccessTime;
            }

            @Override
            public synchronized final void updateLastAccessTime(){
                lastAccessTime = System.currentTimeMillis();
            }

        //

            @SuppressWarnings("StringBufferReplaceableByString")
            @Override
            public final String toString(){
                final StringBuilder OUT = new StringBuilder();
                OUT.append("HttpSession")   .append("{");
                OUT.append("sessionID")     .append("=")   .append(sessionID)          .append(", ");
                OUT.append("creationTime")  .append("=")   .append(creationTime)       .append(", ");
                OUT.append("lastAccessTime").append("=")   .append(lastAccessTime);
                OUT.append("}");
                return OUT.toString();
            }

        };
    }

}
