package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.http.HTTPCode;
import ktt.lib.httpserver.server.SimpleHttpExchange;
import ktt.lib.httpserver.server.SimpleHttpHandler;

import java.io.*;
import java.util.*;

public class FileHandler extends SimpleHttpHandler {

    private final FileHandlerAdapter adapter;

    private final HashMap<String,FileEntry> files = new HashMap<>();
    private final HashMap<String,DirectoryEntry> directories = new HashMap<>();

    public FileHandler(final FileHandlerAdapter adapter){
        this.adapter = adapter;
    }

//

    public final void addFile(final File file){
        addFile("",file,adapter.getName(file),false);
    }

    public final void addFile(final File file, final boolean preload){
        addFile("",file,adapter.getName(file),preload);
    }

    public final void addFile(final String context, final File file){
        addFile(context,file,adapter.getName(file),false);
    }

    public final void addFile(final String context, final File file, final boolean preload){
        addFile(context,file,adapter.getName(file),preload);
    }

    public final void addFile(final File file, final String fileName){
        addFile("",file,fileName,false);
    }

    public final void addFile(final File file, final String fileName, final boolean preload){
        addFile("",file,fileName,preload);
    }

    public final void addFile(final String context, final File file, final String fileName){
        addFile(context,file,fileName,false);
    }

    public final void addFile(final String context, final File file, final String fileName, final boolean preload){
        try{
            files.put(getContext(context) + getContext(fileName),new FileEntry(file,preload,adapter));
        }catch(final FileNotFoundException ignored){ }
    }

    //

    public final void addFiles(final File[] files){
        for(final File file : files)
            addFile(file);
    }

    public final void addFiles(final File[] files, final boolean preload){
        for(final File file : files)
            addFile(file,preload);
    }

    public final void addFiles(final String context, final File[] files){
        for(final File file : files)
            addFile(context,file);
    }

    public final void addFiles(final String context, final File[] files, final boolean preload){
        for(final File file : files)
            addFile(context,file);
    }

    //

    public final void addDirectory(final File directory){
        addDirectory("",directory,directory.getName(),false,false);
    }

    public final void addDirectory(final File directory, final boolean preload){
        addDirectory("",directory,directory.getName(),preload,false);
    }

    public final void addDirectory(final File directory, final boolean preload, final boolean walk){
        addDirectory("",directory,directory.getName(),preload,walk);
    }

    public final void addDirectory(final String context, final File directory){
        addDirectory(context,directory,directory.getName(),false,false);
    }

    public final void addDirectory(final String context, final File directory, final boolean preload){
        addDirectory(context,directory,directory.getName(),preload,false);
    }

    public final void addDirectory(final String context, final File directory, final boolean preload, final boolean walk){
        addDirectory(context,directory,directory.getName(),preload,walk);
    }

    public final void addDirectory(final File directory, final String directoryName){
        addDirectory("",directory,directoryName,false,false);
    }

    public final void addDirectory(final File directory, final String directoryName, final boolean preload){
        addDirectory("",directory,directoryName,preload,false);
    }

    public final void addDirectory(final File directory, final String directoryName, final boolean preload, final boolean walk){
        addDirectory("",directory,directoryName,preload,walk);
    }

    public final void addDirectory(final String context, final File directory, final String directoryName){
        addDirectory(context,directory,directoryName,false,false);
    }

    public final void addDirectory(final String context, final File directory, final String directoryName, final boolean preload){
        addDirectory(context,directory,directoryName,preload,false);
    }

    public final void addDirectory(final String context, final File directory, final String directoryName, final boolean preload, final boolean walk){
        try{
            directories.put(getContext(context) + getContext(directoryName), new DirectoryEntry(directory, preload, adapter, walk));
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
