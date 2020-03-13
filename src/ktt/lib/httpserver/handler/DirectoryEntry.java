package ktt.lib.httpserver.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

class DirectoryEntry {

    private final HashMap<String,FileEntry> files = new HashMap<>();

    private final File directory;

    private final boolean isFilesPreloaded;
    private final boolean isWalkthrough;

    DirectoryEntry(final File directory, final boolean isFilesPreloaded, final FileHandlerAdapter adapter, final boolean isWalkthrough) throws FileNotFoundException{
        if(!directory.exists() || !directory.isDirectory())
            throw new FileNotFoundException("Directory at " + directory.getAbsolutePath() + " was not found");

        this.directory = directory;
        this.isFilesPreloaded = isFilesPreloaded;
        this.isWalkthrough = isWalkthrough;

        if(isFilesPreloaded){
            if(!isWalkthrough){
                for(final File file : Objects.requireNonNull(directory.listFiles())){
                    files.put(
                        adapter.getName(file),
                        new FileEntry(file, true, new FileBytesAdapter() {
                            @Override
                            public final byte[] getBytes(final File file, final byte[] bytes){
                                return adapter.getBytes(file,bytes);
                            }
                        })
                    );
                }
            }else{
                Files.walk(directory.toPath()).forEach(new Consumer<Path>() {
                    @Override
                    public final void accept(final Path path){
                        // todo
                    }
                });
            }
        }
    }

    public HashMap<String, FileEntry> getFiles(){
        return files;
    }


    public final File getDirectory(){
        return directory;
    }

    public final boolean isFilesPreloaded(){
        return isFilesPreloaded;
    }

    public final boolean isWalkthrough(){
        return isWalkthrough;
    }

}
