package com.kttdevelopment.simplehttpserver.handler;

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
                final File[] listFiles = directory.listFiles();
                for(final File file : (listFiles == null) ? new File[0] : listFiles)
                    if(!file.isDirectory())
                        files.put(
                            getContext(adapter.getName(file)),
                            new FileEntry(file, true, adapter)
                        );
            }else{
                final Path dirPath = directory.toPath();

                Files.walk(dirPath).filter(path -> path.toFile().isDirectory()).forEach(path -> {
                    final File pathFile = path.toFile();
                    final String rel = dirPath.relativize(path).toString();

                    final File[] listFiles = pathFile.listFiles();
                    for(final File file : (listFiles == null) ? new File[0] : listFiles){
                        try{
                            files.put(
                                (rel.isEmpty() || rel.equals("/") || rel.equals("\\") ? "" : getContext(rel)) + getContext(adapter.getName(file)),
                                new FileEntry(file, true, adapter)
                            );
                        }catch(final FileNotFoundException ignored){
                            // #listFiles assume that all files exist, so this exception should never occur unless the user modified the directory mid-read.
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
        final String linSlash = path.replace("\\","/");
        if(linSlash.equals("/")) return "/";
        final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
        return seSlash.substring(0,seSlash.length()-1);
    }

//


    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("DirectoryEntry")    .append("{");
        OUT.append("isWalkthrough")     .append("=")   .append(isWalkthrough)      .append(", ");
        OUT.append("isFilePreloaded")   .append("=")   .append(isFilesPreloaded)   .append(", ");
        OUT.append("directory")         .append("=")   .append(directory)          .append(", ");
        OUT.append("(preloaded) files") .append("=")   .append(files)              .append(", ");
        OUT.append("adapter")           .append("=")   .append(adapter);
        OUT.append("}");
        return OUT.toString();
    }

}
