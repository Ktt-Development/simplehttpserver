package ktt.lib.httpserver.handler_depreciated;

import java.io.File;

/**
 * This interface determines the bytes to save when adding to the {@link FilePreHandler}.
 * @see FilePreHandler
 * @see FileNameAdapter
 * @since 01.00.00
 * @version 01.00.00
 * @author Ktt Development
 */
@SuppressWarnings("WeakerAccess")
@Deprecated
public interface FileBytesAdapter {

    /**
     * Returns the bytes when given a file.
     * @param file File to read
     * @return File bytes
     * @since 01.00.00
     */
    byte[] getBytes(File file);

}
