/*
 * Copyright (C) 2021 Ktt Development
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.kttdevelopment.simplehttpserver.handler;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represent a file in the {@link FileHandler}. Applications do not use this class.
 *
 * @see ByteLoadingOption
 * @see FileHandler
 * @since 02.00.00
 * @version 4.0.0
 * @author Ktt Development
 */
class FileEntry {

    private final File file;
    private final FileBytesAdapter adapter;
    private final ByteLoadingOption loadingOption;

    private byte[] bytes = null;

    private final AtomicLong lastModified = new AtomicLong();
    private final AtomicLong expiry = new AtomicLong(0); // cache only
    private final long cacheTime; // cache only

    /**
     * Creates a file entry.
     *
     * @param file file to represent
     * @param bytesAdapter how to process the bytes in {@link #getBytes()}
     * @param loadingOption how to handle the initial file loading
     *
     * @see FileBytesAdapter
     * @see ByteLoadingOption
     * @since 03.05.00
     * @author Ktt Development
     */
    FileEntry(final File file, final FileBytesAdapter bytesAdapter, final ByteLoadingOption loadingOption){
        if(loadingOption == ByteLoadingOption.CACHELOAD && !(bytesAdapter instanceof CacheFileAdapter))
            throw new IllegalArgumentException("CacheLoad option must use a cache file adapter");

        this.file           = file;
        this.adapter        = bytesAdapter;
        this.loadingOption  = loadingOption;
        this.cacheTime      = bytesAdapter instanceof CacheFileAdapter ? ((CacheFileAdapter) bytesAdapter).getCacheTimeMillis() : -1;

        if(loadingOption != ByteLoadingOption.LIVELOAD && loadingOption != ByteLoadingOption.CACHELOAD){
            try{
                bytes = adapter.getBytes(file, Files.readAllBytes(file.toPath()));
            }catch(final Throwable ignored){
                bytes = null;
            }
            if(loadingOption != ByteLoadingOption.PRELOAD){
                lastModified.set(file.lastModified());
            }
        }
    }

//

    /**
     * Returns the file being referenced
     *
     * @return reference file
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public final File getFile(){
        return file;
    }

    /**
     * Reloads the file's cached bytes.
     *
     * @see #clearBytes()
     * @since 03.05.00
     * @author Ktt Development
     */
    public synchronized final void reloadBytes(){
        if(loadingOption == ByteLoadingOption.PRELOAD || loadingOption == ByteLoadingOption.LIVELOAD)
            throw new UnsupportedOperationException();
        else
            lastModified.set(file.lastModified());
            try{
                bytes = adapter.getBytes(file, Files.readAllBytes(file.toPath()));
            }catch(final Throwable ignored){
                bytes = null;
            }
    }

    /**
     * Clears the file's cached bytes.
     *
     * @see #reloadBytes()
     * @since 4.0.0
     * @author Ktt Development
     */
    public synchronized final void clearBytes(){
        if(loadingOption == ByteLoadingOption.PRELOAD || loadingOption == ByteLoadingOption.LIVELOAD)
            throw new UnsupportedOperationException();
        else{
            lastModified.set(0);
            bytes = null;
        }
    }

    /**
     * Returns the file's bytes after the {@link FileBytesAdapter} was used.
     *
     * @return processed file bytes
     *
     * @see FileBytesAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    public final byte[] getBytes(){
        switch(loadingOption){
            case MODLOAD:
            case CACHELOAD:
                final long now = System.currentTimeMillis();
                // update the file if it was modified or now exceeds the expiry time
                if((loadingOption == ByteLoadingOption.CACHELOAD && now > expiry.getAndUpdate(was -> now + cacheTime)) || file.lastModified() != lastModified.get())
                    reloadBytes();
            case PRELOAD:
                return bytes;
            default:
            case LIVELOAD:
                try{
                    return adapter.getBytes(file, Files.readAllBytes(file.toPath())); // read and adapt bytes
                }catch(final Throwable ignored){
                    return null;
                }
        }
    }

    /**
     * Returns the file's byte loading option.
     *
     * @return file loading option
     *
     * @see ByteLoadingOption
     * @since 03.05.00
     * @author Ktt Development
     */
    public final ByteLoadingOption getLoadingOption(){
        return loadingOption;
    }

    /**
     * Returns when the file should be removed from cache.
     *
     * @return when file should be removed from cache
     *
     * @since 4.0.0
     * @author Ktt Development
     */
    final long getExpiry(){
        return expiry.get();
    }

//

    @Override
    public String toString(){
        return
            "FileEntry"         + '{' +
            "file"              + '=' +     file            + ", " +
            "adapter"           + '=' +     adapter         + ", " +
            "loadingOption"     + '=' +     loadingOption   + ", " +
            "bytes"             + '=' +     Arrays.toString(bytes) +
            '}';
    }

}
