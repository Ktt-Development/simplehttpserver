package ktt.lib.httpserver.handler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class FileEntry {

    private final File file;
    private final boolean isPreloaded;
    private byte[] preloadedBytes;

    FileEntry(final File file, final boolean isPreloaded, final FileBytesAdapter bytesAdapter) throws FileNotFoundException{
        if(!file.exists() || file.isDirectory())
            throw new FileNotFoundException("File at " + file.getAbsoluteFile() + " was not found");

        this.file = file;
        this.isPreloaded = isPreloaded;

        if(isPreloaded)
            try{
                preloadedBytes = bytesAdapter.getBytes(file,Files.readAllBytes(file.toPath()));
            }catch(IOException ignored){
                preloadedBytes = "Failed to read file".getBytes(StandardCharsets.UTF_8);
            }
    }

    public final File getFile(){
        return file;
    }

    public final boolean isPreloaded(){
        return isPreloaded;
    }

    public final byte[] getBytes(){
        if(isPreloaded)
            return preloadedBytes;
        else
            try{
                return Files.readAllBytes(file.toPath());
            }catch(final IOException ignored){
                return null;
            }
    }

}
