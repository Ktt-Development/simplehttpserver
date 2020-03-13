package ktt.lib.httpserver.handler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class FileEntry {

    private final File file;
    private final boolean isPreloaded;
    private byte[] preloadedBytes;

    FileEntry(final File file, final boolean isPreloaded, final FileBytesAdapter bytesAdapter){
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
                return "Failed to read file".getBytes(StandardCharsets.UTF_8);
            }
    }

}
