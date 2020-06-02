package com.kttdevelopment.simplehttpserver.handler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a directory in the {@link FileHandler}. Applications do not use this class.
 *
 * @see FileHandler
 * @see FileEntry
 * @since 02.00.00
 * @version 03.05.00
 * @author Ktt Development
 */
class DirectoryEntry {

    private final File directory;
    private final FileHandlerAdapter adapter;
    private final ByteLoadingOption loadingOption;
    private final boolean isWalkthrough;

    private final Map<String,FileEntry> files = new ConcurrentHashMap<>(); // preload/watchload only


    DirectoryEntry(final File directory, final FileHandlerAdapter adapter, final ByteLoadingOption loadingOption, final boolean isWalkthrough) throws IOException{
        this.directory = directory;
        this.adapter = adapter;
        this.loadingOption = loadingOption;
        this.isWalkthrough = isWalkthrough;

        if(loadingOption == ByteLoadingOption.WATCHLOAD){

        }else if(loadingOption == ByteLoadingOption.PRELOAD){
                if(!isWalkthrough){
                    final File[] listFiles = Objects.requireNonNullElse(directory.listFiles(),new File[0]);
                    for(final File file : listFiles)
                        if(!file.isDirectory())
                            files.put(
                                getContext(adapter.getName(file)),
                                new FileEntry(file,adapter,ByteLoadingOption.PRELOAD)
                            );
                }else{
                    final Path dirPath = directory.toPath();

                    Files.walk(dirPath).filter(path -> path.toFile().isDirectory()).forEach(path -> {
                        final File p2f = path.toFile();
                        final String rel = dirPath.relativize(path).toString();

                        final File[] listFiles = Objects.requireNonNullElse(p2f.listFiles(), new File[0]);
                        for(final File file : listFiles)
                            files.put(
                                (rel.isEmpty() || rel.equals("/") || rel.equals("\\") ? "" : getContext(rel)) + getContext(adapter.getName(file)),
                                new FileEntry(file,adapter,loadingOption)
                            );
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
    @Deprecated
    public final boolean isFilesPreloaded(){
        return loadingOption != ByteLoadingOption.LIVELOAD;
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
    public Map<String, FileEntry> getFiles(){
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
        if(loadingOption != ByteLoadingOption.LIVELOAD){
            String match = "";
            for(final String key : files.keySet())
                if(rel.startsWith(key) && key.startsWith(match))
                    match = key;
            return !match.isEmpty() ? files.get(match).getFile() : null;
        }else{
            if(isWalkthrough){
                final File parent = new File(directory.getAbsolutePath() + path).getParentFile();
                if(!parent.getAbsolutePath().startsWith(directory.getAbsolutePath())) return null;
                final String name = path.substring(path.lastIndexOf('/'));
                final File[] listFiles = parent.listFiles(pathname -> !pathname.isDirectory());

                for(final File file : (listFiles == null) ? new File[0] : listFiles)
                    if(adapter.getName(file).equals(name))
                        return file;
            }else{
                final File[] listFiles = directory.listFiles(pathname -> !pathname.isDirectory());

                for(final File file : (listFiles == null) ? new File[0] : listFiles)
                    if(adapter.getName(file).equals(path))
                        return file;
            }
            return null;
        }
    }

    /**
     * Returns the file's bytes after the {@link FileHandlerAdapter} was used or null if it was not found or failed to read. <b>Preload only.</b>
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
        if(loadingOption != ByteLoadingOption.LIVELOAD){
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

    /**
     * Returns the file's byte loading option.
     *
     * @return file loading option
     *
     * @see ByteLoadingOption
     * @since 03.05.00
     * @author Ktt Development
     */
    public final ByteLoadingOption getLoadingOption(){
            return loadingOption;
        }

//

    private static String getContext(final String path){
        final String linSlash = path.replace("\\","/");
        if(linSlash.equals("/")) return "/";
        final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
        return seSlash.substring(0,seSlash.length()-1);
    }

//


    @Override
    public String toString(){
        return
            "DirectoryEntry"    + '{' +
            "directory"         + '=' +     directory       + ", " +
            "adapter"           + '=' +     adapter         + ", " +
            "loadingOption"     + '=' +     loadingOption   + ", " +
            "isWalkthrough"     + '=' +     isWalkthrough   + ", " +
            "files"             + '=' +     files +
            '}';
    }

}
