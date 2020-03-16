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

    private final FileHandlerAdapter adapter;

    public FileHandler(final FileHandlerAdapter adapter){
        this.adapter = adapter;
    }

//

    public final void addFile(final File file){ }

    public final void addFile(final File file, final boolean preload){ }

    public final void addFile(final File file, final FileHandlerAdapter adapter){ }

    public final void addFile(final File file, final FileHandlerAdapter adapter, final boolean preload){ }

    public final void addFile(final String context, final File file){ }

    public final void addFile(final String context, final File file, final boolean preload){ }

    public final void addFile(final String context, final File file, final FileHandlerAdapter adapter){ }

    public final void addFile(final String context, final File file, final FileHandlerAdapter adapter, final boolean preload){ }

    //

    public final void addFiles(final File[] file){ }

    public final void addFiles(final File[] files, final boolean preload){ }

    public final void addFiles(final File[] files, final FileHandlerAdapter adapter){ }

    public final void addFiles(final File[] files, final FileHandlerAdapter adapter, final boolean preload){ }

    public final void addFiles(final String context, final File[] files){ }

    public final void addFiles(final String context, final File[] files, final boolean preload){ }

    public final void addFiles(final String context, final File[] files, final FileHandlerAdapter adapter){ }

    public final void addFiles(final String context, final File[] files, final FileHandlerAdapter adapter, final boolean preload){ }

    //

    public final void addDirectory(final File directory){ }

    public final void addDirectory(final File directory, final boolean preload){ }

    public final void addDirectory(final File directory, final boolean preload, final boolean walk){ }

    public final void addDirectory(final File directory, final FileHandlerAdapter adapter){ }

    public final void addDirectory(final File directory, final FileHandlerAdapter adapter, final boolean preload){ }

    public final void addDirectory(final File directory, final FileHandlerAdapter adapter, final boolean preload, final boolean walk){ }

    public final void addDirectory(final String context, final File directory){ }

    public final void addDirectory(final String context, final File directory, final boolean preload){ }

    public final void addDirectory(final String context, final File directory, final boolean preload, final boolean walk){ }

    public final void addDirectory(final String context, final File directory, final FileHandlerAdapter adapter){ }

    public final void addDirectory(final String context, final File directory, final FileHandlerAdapter adapter, final boolean preload){ }

    public final void addDirectory(final String context, final File directory, final FileHandlerAdapter adapter, final boolean preload, final boolean walk){ }

    public final void addDirectory(final File directory, final String directoryName){ }

    public final void addDirectory(final File directory, final String directoryName, final boolean preload){ }

    public final void addDirectory(final File directory, final String directoryName, final boolean preload, final boolean walk){ }

    public final void addDirectory(final File directory, final String directoryName, final FileHandlerAdapter adapter){ }

    public final void addDirectory(final File directory, final String directoryName, final FileHandlerAdapter adapter, final boolean preload){ }

    public final void addDirectory(final File directory, final String directoryName, final FileHandlerAdapter adapter, final boolean preload, final boolean walk){ }

    public final void addDirectory(final String context, final File directory, final String directoryName){ }

    public final void addDirectory(final String context, final File directory, final String directoryName, final boolean preload){ }

    public final void addDirectory(final String context, final File directory, final String directoryName, final boolean preload, final boolean walk){ }

    public final void addDirectory(final String context, final File directory, final String directoryName, final FileHandlerAdapter adapter){ }

    public final void addDirectory(final String context, final File directory, final String directoryName, final FileHandlerAdapter adapter, final boolean preload){ }

    public final void addDirectory(final String context, final File directory, final String directoryName, final FileHandlerAdapter adapter, final boolean preload, final boolean walk){ }

    //

    public final void removeFile(final File file){ }

    public final void removeFiles(final File[] files){ }

    public final void removeDirectory(final File directory){ }

//

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        final String rel = exchange.getContext().substring(exchange.getHttpContext().getPath().length());

        String match = "";
        for(final String key : files.keySet())
            if(rel.startsWith(key) && key.startsWith(match))
                match = key;

        if(!match.isEmpty() && files.containsKey(match)){
            final FileEntry entry = files.get(match);
            handle(exchange,entry.getFile(),entry.getBytes());
        }else{
            match = "";
            for(final String key : directories.keySet())
                if(rel.startsWith(key) && key.startsWith(match))
                    match = key;

            if(!match.isEmpty() && directories.containsKey(match)){
                final DirectoryEntry entry = directories.get(match);
                final String rel2;
                try{
                    rel2 = rel.substring(match.length()+1);

                    final File file;
                    if((file = entry.getFile(rel2)) != null){
                        handle(exchange, file, entry.getBytes(rel2)); return;
                    }
                }catch(final IndexOutOfBoundsException ignored){ }
            }
            handle(exchange,null,null);
        }
    }

    public void handle(final SimpleHttpExchange exchange, final File source, final byte[] bytes) throws IOException {
        exchange.send(bytes, HTTPCode.HTTP_OK);
    }

}
