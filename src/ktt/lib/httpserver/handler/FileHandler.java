package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.http.HTTPCode;
import ktt.lib.httpserver.server.SimpleHttpExchange;
import ktt.lib.httpserver.server.SimpleHttpHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileHandler extends SimpleHttpHandler {

    private final HashMap<String,DirectoryEntry> directories = new HashMap<>();
    private final HashMap<String,FileEntry> files = new HashMap<>();

    //private final FileNameAdapter nameAdapter;   // at adding
    //private final FileBytesAdapter bytesAdapter; // pre: at adding; post: at handling;

    private final FileHandlerAdapter adapter;

//
        /*
            Hash ->
            - Path (string)
            - File (file)
            - isPreloaded (boolean)
            use instanceof
            - pre: byte[]
            - post: walkInner
         */

        /*
            Pref order ->
            - Exact File
            - File
            - Exact Directory
            - Directory
         */

//

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        // todo
    }

    public void handle(final SimpleHttpExchange exchange, final File source, final byte[] bytes) throws IOException {
        exchange.send(bytes, HTTPCode.HTTP_OK);
    }

}
