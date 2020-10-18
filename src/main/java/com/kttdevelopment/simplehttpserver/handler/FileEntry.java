package com.kttdevelopment.simplehttpserver.handler;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

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

    /**
     * Creates a file entry.
     *
     * @param file file to represent
     * @param bytesAdapter how to process the bytes in {@link #getBytes()}
     * @param loadingOption how to handle the initial file loading
     * @throws UncheckedIOException I/O failure to start watch service ({@link ByteLoadingOption#WATCHLOAD} only).
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

        if(loadingOption != ByteLoadingOption.LIVELOAD){
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
     * Reloads the file's preloaded bytes using the {@link FileBytesAdapter}.
     *
     * @since 03.05.00
     * @author Ktt Development
     */
    public synchronized final void reloadBytes(){
        if(loadingOption == ByteLoadingOption.PRELOAD || loadingOption == ByteLoadingOption.LIVELOAD)
            throw new UnsupportedOperationException();
        else
            try{
                bytes = loadingOption == ByteLoadingOption.MODLOAD ? ((CacheFileAdapter) adapter).getBytes(file) : adapter.getBytes(file, Files.readAllBytes(file.toPath()));
            }catch(final Throwable ignored){
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
                if(file.lastModified() != lastModified.get())
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
