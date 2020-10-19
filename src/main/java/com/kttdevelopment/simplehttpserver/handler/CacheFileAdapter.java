package com.kttdevelopment.simplehttpserver.handler;

import java.util.concurrent.TimeUnit;

/**
 * This class caches file bytes when adding to the {@link FileHandler}.
 *
 * @see FileHandlerAdapter
 * @see FileHandler
 * @since 4.0.0
 * @version 4.0.0
 * @author Ktt Development
 */
public class CacheFileAdapter implements FileHandlerAdapter {

    private final long cacheTimeMillis;

    /**
     * Creates a CacheFileAdapter where files will expire after set milliseconds.
     *
     * @param cacheTimeMillis how long a file should exist for
     *
     * @since 4.0.0
     * @author Ktt Development
     */
    public CacheFileAdapter(final long cacheTimeMillis){
        this.cacheTimeMillis = cacheTimeMillis;
    }

    /**
     * Creates a CacheFileAdapter where files will expire after a set time.
     *
     * @param cacheTime how long a file should exist for
     * @param timeUnit the time unit
     *
     * @see TimeUnit
     * @since 4.0.0
     * @author Ktt Development
     */
    public CacheFileAdapter(final long cacheTime, final TimeUnit timeUnit){
        cacheTimeMillis = timeUnit.toMillis(cacheTime);
    }

    /**
     * Returns how long files should be cached for.
     *
     * @return file cache time
     *
     * @since 4.0.0
     * @author Ktt Development
     */
    final long getCacheTimeMillis(){
        return cacheTimeMillis;
    }

    @Override
    public String toString(){
        return
            "CacheFileAdapter"  + '{' +
            "cacheTimeMillis"   + '=' + cacheTimeMillis +
            '}';
    }

}
