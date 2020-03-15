package ktt.lib.httpserver.handler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a directory in the {@link FileHandler}. Applications do not use this class.
 *
 * @see FileHandler
 * @see FileEntry
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
class DirectoryEntry {

    private final boolean isWalkthrough;
    private final boolean isFilesPreloaded;

    private final File directory;
    private final HashMap<String,FileEntry> files = new HashMap<>(); // preload only

    private final FileHandlerAdapter adapter;

    /**
     * Creates a directory entry.
     *
     * @param directory directory to represent
     * @param isFilesPreloaded whether to read bytes now or at runtime
     * @param adapter how to process the file name for context and bytes for {@link #getBytes(String)}
     * @param isWalkthrough if directory should contain inner folders
     * @throws FileNotFoundException if file was not found or is not a directory
     * @throws IOException if file walk failed for initial directory
     *
     * @see FileHandlerAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    DirectoryEntry(final File directory, final boolean isFilesPreloaded, final FileHandlerAdapter adapter, final boolean isWalkthrough) throws IOException{
        if(!directory.exists() || !directory.isDirectory())
            throw new FileNotFoundException("Directory at " + directory.getAbsolutePath() + " was not found");

        this.directory = directory;
        this.isFilesPreloaded = isFilesPreloaded;
        this.isWalkthrough = isWalkthrough;
        this.adapter = adapter;

        if(isFilesPreloaded){
            if(!isWalkthrough){
                for(final File file : Objects.requireNonNull(directory.listFiles())){
                    files.put(
                        getContext(adapter.getName(file)),
                        new FileEntry(file, true, adapter)
                    );
                }
            }else{
                final Path dirPath = directory.toPath();

                Files.walk(dirPath).filter(path -> path.toFile().isDirectory()).forEach(path -> {
                    final File pathFile = path.toFile();

                    final String rel = dirPath.relativize(path).toString();
                    final File[] files = pathFile.listFiles();

                    if(files == null) return;

                    for(final File file : files){
                        try{
                            DirectoryEntry.this.files.put(
                                getContext(rel + "/" + adapter.getName(file)),
                                new FileEntry(file, true, adapter)
                            );
                        }catch(FileNotFoundException e){
                            // failed
                        }
                    }
                });
            }
        }
    }

//

    /**
     * Returns if the directory uses inner folders and files
     *
     * @return if directory uses inner folders and files
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public final boolean isWalkthrough(){
        return isWalkthrough;
    }

    /**
     * Returns if the files were preloaded.
     *
     * @return if the files were preloaded
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public final boolean isFilesPreloaded(){
        return isFilesPreloaded;
    }

//

    /**
     * Returns the directory being referenced
     *
     * @return referenced directory
     *
     * @see #getFiles()
     * @see #getFile(String)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final File getDirectory(){
        return directory;
    }

    /**
     * Returns the directories and their associated contexts. <b>Preload only.</b>
     *
     * @return preloaded files
     *
     * @see #getDirectory()
     * @see #getFile(String)
     * @since 02.00.00
     * @author Ktt Development
     */
    public HashMap<String, FileEntry> getFiles(){
        return files;
    }

    /**
     * Returns the file at the associated path. <b>Preload only.</b>
     *
     * @param path context to check
     * @return file associated with that context
     *
     * @see #getDirectory()
     * @see #getFiles()
     * @since 02.00.00
     * @author Ktt Development
     */
    public final File getFile(final String path){
        final String rel = getContext(path);
        if(isFilesPreloaded){
            String match = "";
            for(final String key : files.keySet())
                if(rel.startsWith(key) && key.startsWith(match))
                    match = key;
            if(!match.isEmpty()){
                return files.get(match).getFile();
            }else{
                return null;
            }
        }else{
            if(isWalkthrough){
                final File parent = new File(directory.getAbsolutePath() + path).getParentFile();
                final File target = new File(parent.getAbsolutePath() + path.substring(0,path.lastIndexOf('/')));
                return target.exists() ? target : null;
            }else{
                final File[] files = directory.listFiles(pathname -> !pathname.isDirectory());

                if(files == null) return null;

                for(final File file : files)
                    if(adapter.getName(file).equalsIgnoreCase(path))
                        return file;
                return null;
            }
        }
    }

    /**
     * Returns the file's bytes after the {@link FileHandlerAdapter} was used or null if it was not found or failed to read.
     *
     * @param path context to check
     * @return processed file bytes
     *
     * @see FileHandlerAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    public final byte[] getBytes(final String path){
        final String rel = getContext(path);
        if(isFilesPreloaded){
            String match = "";
            for(final String key : files.keySet())
                if(rel.startsWith(key) && key.startsWith(match))
                    match = key;
            if(!match.isEmpty()){
                return files.get(match).getBytes();
            }else{
                return null;
            }
        }else{
            final File file = getFile(path);
            try{
                return adapter.getBytes(file,Files.readAllBytes(Objects.requireNonNull(file).toPath()));
            }catch(NullPointerException | IOException ignored){
                return null;
            }
        }
    }

//

    private static String getContext(final String path){
        final String linSlash = path.toLowerCase().replace("\\","/");
        final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
        return seSlash.substring(0,seSlash.length()-1);
    }

}
