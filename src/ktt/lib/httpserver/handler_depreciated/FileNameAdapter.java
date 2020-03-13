package ktt.lib.httpserver.handler_depreciated;

import java.io.File;

/**
 * This interface determines the file name to use when adding to the {@link FilePreHandler}.
 * @see FilePreHandler
 * @see FileBytesAdapter
 * @since 01.00.00
 * @version 01.00.01
 * @author Ktt Development
 */
@SuppressWarnings("WeakerAccess")
@Deprecated
public interface FileNameAdapter {

    /**
     * Returns the preferred name when given a file.
     * @param file File to name
     * @return preferred name
     * @since 01.00.00
     */
    String getName(File file);

}
