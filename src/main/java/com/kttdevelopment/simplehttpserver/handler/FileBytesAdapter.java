package com.kttdevelopment.simplehttpserver.handler;

import java.io.File;

/**
 * This interface determines the file bytes to use when adding to {@link FileHandler}.
 *
 * @see FileHandlerAdapter
 * @see FileHandler
 * @since 01.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
interface FileBytesAdapter {

    /**
     * Returns the bytes when given a file and its initial contents
     *
     * @param file file to name
     * @param bytes bytes from preload
     * @return new bytes
     *
     * @since 01.00.00
     * @author Ktt Development
     */
    default byte[] getBytes(final File file, final byte[] bytes){
        return bytes;
    }

}
