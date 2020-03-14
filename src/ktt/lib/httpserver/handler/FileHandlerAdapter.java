package ktt.lib.httpserver.handler;

/**
 * This interface determines the file name and bytes to use when adding to {@link FileHandler}. <br>
 * Only handles files before the exchange.
 *
 * @see FileHandler
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
public interface FileHandlerAdapter extends FileNameAdapter, FileBytesAdapter {

}
