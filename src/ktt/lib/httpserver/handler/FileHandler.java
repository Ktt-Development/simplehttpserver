package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.var.HTTPCode;
import ktt.lib.httpserver.SimpleHttpExchange;
import ktt.lib.httpserver.SimpleHttpHandler;

import java.io.*;
import java.util.*;

/**
 * A request handler that processes files using the {@link FileHandlerAdapter}. <br>
 * The <code>context</code> parameter determines if the relative context of the file within the handler before the name. <br>
 * The <code>fileName</code> parameter overrides the {@link FileHandlerAdapter#getName(File)} and determines the name of the file after the context (if there is one). <br>
 * The <code>directoryName</code> parameter determines the directory's name. Add the files at the top level by keeping this field empty. <br>
 * The <code>preload</code> parameter determines if the handler should read the bytes when they are added or read the file at the exchange. <br>
 * The <code>walk</code> parameter determines if all the inner directories should be used.
 * The handler will not add any null files.
 *
 * @see FileHandlerAdapter
 * @see SimpleHttpHandler
 * @see com.sun.net.httpserver.HttpHandler
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
public class FileHandler extends SimpleHttpHandler {

    private final FileHandlerAdapter adapter;

    private final HashMap<String,FileEntry> files = new HashMap<>();
    private final HashMap<String,DirectoryEntry> directories = new HashMap<>();

    /**
     * Creates a file handler without a {@link FileHandlerAdapter}. This will use the files name and bytes.
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public FileHandler(){
        this.adapter = new FileHandlerAdapter() {
            @Override
            public byte[] getBytes(final File file, final byte[] bytes){
                return bytes;
            }

            @Override
            public String getName(final File file){
                return file.getName();
            }
        };
    }

    /**
     * Creates a file handler with a {@link FileHandler}. This will use the adapted file name and bytes.
     *
     * @param adapter adapter to get name and bytes
     *
     * @see FileHandlerAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    public FileHandler(final FileHandlerAdapter adapter){
        this.adapter = adapter;
    }

//

    /**
     * Adds a file to the handler at the name given by the adapter.
     *
     * @param file file to add
     *
     * @see FileHandlerAdapter
     * @see #addFile(File, boolean)
     * @see #addFile(File, String)
     * @see #addFile(File, String, boolean)
     * @see #addFile(String, File)
     * @see #addFile(String, File, boolean)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final File file){
        addFile("",file,adapter.getName(file),false);
    }

    /**
     * Adds a file to the handler at a name given by the adapter and preloads the bytes.
     *
     * @param file file to add
     * @param preload whether to load the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addFile(File)
     * @see #addFile(File, String)
     * @see #addFile(File, String, boolean)
     * @see #addFile(String, File)
     * @see #addFile(String, File, boolean)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final File file, final boolean preload){
        addFile("",file,adapter.getName(file),preload);
    }

    /**
     * Adds a file to the handler.
     *
     * @param file file to add
     * @param fileName file name to use
     *
     * @see FileHandlerAdapter
     * @see #addFile(File)
     * @see #addFile(File, boolean)
     * @see #addFile(File, String, boolean)
     * @see #addFile(String, File)
     * @see #addFile(String, File, boolean)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final File file, final String fileName){
        addFile("",file,fileName,false);
    }

    /**
     * Adds a file to the handler and preloads the bytes.
     *
     * @param file file to add
     * @param fileName file name to use
     * @param preload whether to load the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addFile(File)
     * @see #addFile(File, boolean)
     * @see #addFile(File, String)
     * @see #addFile(String, File)
     * @see #addFile(String, File, boolean)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final File file, final String fileName, final boolean preload){
        addFile("",file,fileName,preload);
    }

    /**
     * Adds a file to the handler at a specified context at a name given by the adapter.
     *
     * @param context context to use
     * @param file file to add
     *
     * @see FileHandlerAdapter
     * @see #addFile(File)
     * @see #addFile(File, boolean)
     * @see #addFile(File, String)
     * @see #addFile(File, String, boolean)
     * @see #addFile(String, File, boolean)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file){
        addFile(context,file,adapter.getName(file),false);
    }

    /**
     * Adds a file to the handler at a specified context at a name given by the adapter and preloads the bytes.
     *
     * @param context context to use
     * @param file file to add
     * @param preload whether to load the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addFile(File)
     * @see #addFile(File, boolean)
     * @see #addFile(File, String)
     * @see #addFile(File, String, boolean)
     * @see #addFile(String, File)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file, final boolean preload){
        addFile(context,file,adapter.getName(file),preload);
    }

    /**
     * Adds a file to the handler at a specified context.
     *
     * @param context context to use
     * @param file file to add
     * @param fileName file name to use
     *
     * @see FileHandlerAdapter
     * @see #addFile(File)
     * @see #addFile(File, boolean)
     * @see #addFile(File, String)
     * @see #addFile(File, String, boolean)
     * @see #addFile(String, File)
     * @see #addFile(String, File, boolean)
     * @see #addFile(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file, final String fileName){
        addFile(context,file,fileName,false);
    }

    /**
     * Adds a file to the handler at a specified context and preloads the bytes.
     *
     * @param context context to use
     * @param file file to add
     * @param fileName file name to use
     * @param preload whether to load the bytes now or at the exchange.
     *
     * @see FileHandlerAdapter
     * @see #addFile(File)
     * @see #addFile(File, boolean)
     * @see #addFile(File, String)
     * @see #addFile(File, String, boolean)
     * @see #addFile(String, File)
     * @see #addFile(String, File, boolean)
     * @see #addFile(String, File, String)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file, final String fileName, final boolean preload){
        try{
            files.put(getContext(context) + getContext(fileName),new FileEntry(file,preload,adapter));
        }catch(final FileNotFoundException ignored){ }
    }

    //

    /**
     * Adds files to the handler at the names given by the adapter.
     *
     * @param files files to add
     *
     * @see FileHandlerAdapter
     * @see #addFiles(File[], boolean)
     * @see #addFiles(String, File[])
     * @see #addFiles(String, File[], boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFiles(final File[] files){
        for(final File file : files)
            addFile(file);
    }

    /**
     * Adds files to the handler at the names given by the adapter and preloads the bytes.
     *
     * @param files files to add
     * @param preload whether to load the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addFiles(File[])
     * @see #addFiles(String, File[])
     * @see #addFiles(String, File[], boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFiles(final File[] files, final boolean preload){
        for(final File file : files)
            addFile(file,preload);
    }

    /**
     * Adds files to the handler at the specified context at the names given by the adapter.
     *
     * @param context context to use
     * @param files files to add
     *
     * @see FileHandlerAdapter
     * @see #addFiles(File[])
     * @see #addFiles(File[], boolean)
     * @see #addFiles(String, File[], boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFiles(final String context, final File[] files){
        for(final File file : files)
            addFile(context,file);
    }

    /**
     * Adds files to the handler at the specified context at the names given by the adapter and preloads the bytes.
     *
     * @param context context to use
     * @param files files to add
     * @param preload whether to read the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addFiles(File[])
     * @see #addFiles(File[], boolean)
     * @see #addFiles(String, File[])
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFiles(final String context, final File[] files, final boolean preload){
        for(final File file : files)
            addFile(context,file,preload);
    }

    //

    /**
     * Adds a directory to the handler.
     *
     * @param directory directory to add
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory){
        addDirectory("",directory,directory.getName(),false,false);
    }

    /**
     * Adds a directory to the handler and preloads the files' bytes.
     *
     * @see FileHandlerAdapter
     * @param directory directory to add
     * @param preload whether to read the bytes now or at the exchange
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final boolean preload){
        addDirectory("",directory,directory.getName(),preload,false);
    }

    /**
     * Adds a directory and all its inner folders to the handler and preloads the files' bytes.
     *
     * @see FileHandlerAdapter
     * @param directory directory to add
     * @param preload whether to read the bytes now or at the exchange
     * @param walk whether to use the inner directories or not
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final boolean preload, final boolean walk){
        addDirectory("",directory,directory.getName(),preload,walk);
    }

    /**
     * Adds a directory with a specified name to the handler.
     *
     * @see FileHandlerAdapter
     * @param directory directory to add
     * @param directoryName directory name
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final String directoryName){
        addDirectory("",directory,directoryName,false,false);
    }

    /**
     * Adds a directory with a specified name to the handler and preloads the bytes.
     *
     * @param directory directory to add
     * @param directoryName directory name
     * @param preload whether to read the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final String directoryName, final boolean preload){
        addDirectory("",directory,directoryName,preload,false);
    }

    /**
     * Adds a directory with a specified name to the handler and all its inner folders and preloads the bytes.
     *
     * @param directory directory to add
     * @param directoryName directory name
     * @param preload whether to read the bytes now or at the exchange
     * @param walk whether to use the inner directories or not
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final String directoryName, final boolean preload, final boolean walk){
        addDirectory("",directory,directoryName,preload,walk);
    }

    /**
     * Adds a directory at a specified context to the handler.
     *
     * @param context context to use
     * @param directory directory to add
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory){
        addDirectory(context,directory,directory.getName(),false,false);
    }

    /**
     * Adds a directory at a specified context to the handler and preloads the bytes.
     *
     * @param context context to use
     * @param directory directory to add
     * @param preload whether to read the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final boolean preload){
        addDirectory(context,directory,directory.getName(),preload,false);
    }

    /**
     * Adds a directory at a specified context and its inner folders and preloads the bytes.
     *
     * @param context context to use
     * @param directory directory to add
     * @param preload whether to read the bytes now or at the exchange
     * @param walk whether to use the inner directories or not`
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final boolean preload, final boolean walk){
        addDirectory(context,directory,directory.getName(),preload,walk);
    }

    /**
     * Adds a directory at a specified context with a specified name.
     *
     * @param context context to use
     * @param directory directory to add
     * @param directoryName directory name
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final String directoryName){
        addDirectory(context,directory,directoryName,false,false);
    }

    /**
     * Adds a directory at a specified context with a specified name and preloads the bytes.
     *
     * @param context context to use
     * @param directory directory to add
     * @param directoryName directory name
     * @param preload whether to read the bytes now or at the exchange
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final String directoryName, final boolean preload){
        addDirectory(context,directory,directoryName,preload,false);
    }

    /**
     * Adds a directory at a specified context with a specified name and all its inner folders and preloads the bytes.
     *
     * @param context context to use
     * @param directory directory to add
     * @param directoryName directory name
     * @param preload whether to read the bytes now or at the exchange
     * @param walk whether to use the inner directories or not
     *
     * @see FileHandlerAdapter
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, boolean, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, boolean, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, boolean, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final String directoryName, final boolean preload, final boolean walk){
        try{
            directories.put(getContext(context) + (directoryName.isEmpty() ? "" : getContext(directoryName)), new DirectoryEntry(directory, preload, adapter, walk));
        }catch(final IOException ignored){ }
    }

//

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        final String rel = getContext(exchange.getContext().substring(exchange.getHttpContext().getPath().length()));

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

    /**
     * Handles a file and gives a response.
     *
     * @param exchange the client information
     * @param source the file
     * @param bytes the files adapted bytes
     * @throws IOException internal failure
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public void handle(final SimpleHttpExchange exchange, final File source, final byte[] bytes) throws IOException {
        exchange.send(bytes, HTTPCode.HTTP_OK);
    }

//

    private static String getContext(final String path){
        final String linSlash = path.toLowerCase().replace("\\","/");
        final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
        return seSlash.substring(0,seSlash.length()-1);
    }

//


    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("FileHandler")           .append("{");
        OUT.append("adapter")               .append("= ")   .append(adapter.toString()) .append(", ");
        OUT.append("(loaded) files")        .append("= ")   .append(files)              .append(", ");
        OUT.append("(loaded) directories")  .append("= ")   .append(directories);
        OUT.append("}");
        return OUT.toString();
    }

}
