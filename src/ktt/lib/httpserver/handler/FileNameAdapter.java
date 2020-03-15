package ktt.lib.httpserver.handler;

import java.io.File;

/**
 * This interface determines the file name to use when adding to {@link FileHandler}. Not intended for directories.
 *
 * @see FileHandlerAdapter
 * @see FileHandler
 * @since 01.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
interface FileNameAdapter {

    /**
     * Returns the name when given a file.
     *
     * @param file file to name
     * @return new name
     *
     * @since 01.00.00
     * @author Ktt Development
     */
    default String getName(final File file){
        return file.getName();
    }

}
