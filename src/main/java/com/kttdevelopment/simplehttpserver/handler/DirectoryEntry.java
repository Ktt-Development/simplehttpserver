package com.kttdevelopment.simplehttpserver.handler;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Represents a directory in the {@link FileHandler}. Applications do not use this class.
 *
 * @see FileHandler
 * @see FileEntry
 * @since 02.00.00
 * @version 03.05.01
 * @author Ktt Development
 */
class DirectoryEntry {

    private final File directory;
    private final FileHandlerAdapter adapter;
    private final ByteLoadingOption loadingOption;
    private final boolean isWalkthrough;

    private final Map<String,FileEntry> preloadedFiles = new ConcurrentHashMap<>(); // preload/watch-load only
    private final Path directoryPath;

    /**
     * Create a directory entry.
     *
     * @param directory directory to represent
     * @param adapter how to process the bytes in {@link #getBytes(String)}
     * @param loadingOption how to handle the initial file loading
     * @param isWalkthrough whether to use sub-directories or not
     * @throws UncheckedIOException failure to walk through directory or failure to start watch service
     *
     * @see FileBytesAdapter
     * @see ByteLoadingOption
     * @since 03.05.00
     * @author Ktt Development
     */
    DirectoryEntry(final File directory, final FileHandlerAdapter adapter, final ByteLoadingOption loadingOption, final boolean isWalkthrough){
        this.directory = directory;
        this.adapter = adapter;
        this.loadingOption = loadingOption;
        this.isWalkthrough = isWalkthrough;

        directoryPath = directory.toPath();

        if(loadingOption == ByteLoadingOption.WATCHLOAD){
            /* load top level directory */ {
                final File[] listFiles = Objects.requireNonNullElse(directory.listFiles(),new File[0]);
                for(final File file : listFiles) // initial population
                    if(file.isFile())
                        try{
                            preloadedFiles.put(
                                getContext(adapter.getName(file)),
                                new FileEntry(file, adapter, ByteLoadingOption.WATCHLOAD, true)
                            );
                        }catch(final RuntimeException ignored){ }
                try{ // create watch service for top level directory
                    createWatchService(directoryPath, createWatchServiceConsumer(directoryPath)); // double enrollment here
                }catch(final IOException e){
                    throw new UncheckedIOException(e);
                }
            }
            if(isWalkthrough){ /* load sub directories */
                try{
                    Files.walk(directoryPath).filter(path -> path.toFile().isDirectory()).forEach(path -> {
                        final File p2f = path.toFile();
                        final String rel = directoryPath.relativize(path).toString();

                        final File[] listFile = Objects.requireNonNullElse(p2f.listFiles(),new File[0]);
                        for(final File file : listFile) // initial population
                            try{
                                preloadedFiles.put(
                                    (rel.isEmpty() || rel.equals("/") || rel.equals("\\") ? "" : getContext(rel)) + getContext(adapter.getName(file)),
                                    new FileEntry(file,adapter,loadingOption,true)
                                );
                            }catch(final RuntimeException ignored){ }

                        // create watch service for directory
                        try{
                            createWatchService(path, createWatchServiceConsumer(path));
                        }catch(final IOException e){
                            throw new UncheckedIOException(e);
                        }

                    });
                }catch(final IOException e){
                    throw new UncheckedIOException(e);
                }
            }
        }else if(loadingOption == ByteLoadingOption.PRELOAD){
            /* load top level directory */ {
                final File[] listFiles = Objects.requireNonNullElse(directory.listFiles(),new File[0]);
                for(final File file : listFiles) // initial population
                    if(!file.isDirectory())
                        try{
                            preloadedFiles.put(
                                getContext(adapter.getName(file)),
                                new FileEntry(file, adapter, ByteLoadingOption.PRELOAD)
                            );
                        }catch(final UncheckedIOException ignored){ }
            }
            if(isWalkthrough){ /* load sub directories */
                try{
                    Files.walk(directoryPath).filter(path -> path.toFile().isDirectory()).forEach(path -> {
                        final File p2f = path.toFile();
                        final String relative = directoryPath.relativize(path).toString();

                        final File[] listFiles = Objects.requireNonNullElse(p2f.listFiles(), new File[0]);
                        for(final File file : listFiles) // populate sub files
                            try{
                                preloadedFiles.put(
                                    (relative.isEmpty() || relative.equals("/") || relative.equals("\\") ? "" : getContext(relative)) + getContext(adapter.getName(file)),
                                    new FileEntry(file, adapter, ByteLoadingOption.PRELOAD)
                                );
                            }catch(final RuntimeException ignored){ }
                    });
                }catch(final IOException e){
                    throw new UncheckedIOException(e);
                }
            }
        }
    }


    private final Map<Path,AtomicBoolean> watchService = new ConcurrentHashMap<>();

    private void createWatchService(final Path path, final Consumer<WatchEvent<?>> consumer) throws IOException{
        final WatchService service = FileSystems.getDefault().newWatchService();
        path.register(service,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);

        final AtomicBoolean stop = new AtomicBoolean(false);

        new Thread(() -> {
            System.out.println("create ws 4 " + path);
            WatchKey key;
            try{
                while((key = service.take()) != null){
                    for(WatchEvent<?> event : key.pollEvents()){
                        consumer.accept(event);
                    }
                    key.reset();
                    System.out.println("reset event");
                    if(stop.get())
                        break;
                }
            }catch(final InterruptedException ignored){ }
        }).start();

        watchService.put(path,stop);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private Consumer<WatchEvent<?>> createWatchServiceConsumer(final Path path){
        return (WatchEvent<?> event) -> {
            try{
                final Path target = directoryPath.resolve((Path) event.context());
                final File file = target.toFile();
                final WatchEvent.Kind<?> type = event.kind();

                final String parentPath = getContext(directoryPath.relativize(path).toString());
                final String context = (parentPath.isEmpty() || parentPath.equals("/") || parentPath.equals("\\") ? "" : parentPath) + getContext(adapter.getName(file));

                if(file.isFile())
                    if(type == ENTRY_CREATE)
                        preloadedFiles.put(context, new FileEntry(file,adapter,ByteLoadingOption.WATCHLOAD,true));
                    else if(type == ENTRY_DELETE)
                        preloadedFiles.remove(context);
                    else if(type == ENTRY_MODIFY)
                        Objects.requireNonNull(preloadedFiles.get(context)).reloadBytes();
                    else; // prevent ambiguous else with below
                else
                   if(type == ENTRY_CREATE)
                       try{
                           createWatchService(target, createWatchServiceConsumer(target));
                       }catch(final IOException e){
                           e.printStackTrace();
                       }
                   else if(type == ENTRY_DELETE){
                       System.out.println("ws:" + watchService);
                       System.out.println("directory deletion via" + target);
                       Objects.requireNonNull(watchService.get(target)).set(true);
                       watchService.remove(target);
                   }

            }catch(final ClassCastException | NullPointerException e){
                e.printStackTrace();
            }
        };
    }

//

    /**
     * Returns the directory being referenced
     *
     * @return referenced directory
     *
     * @see #getPreloadedFiles()
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
    public Map<String, FileEntry> getPreloadedFiles(){
        return Collections.unmodifiableMap(preloadedFiles);
    }

    /**
     * Returns the file at the associated path. <b>Preload only.</b>
     *
     * @param path context to check
     * @return file associated with that context
     *
     * @see #getDirectory()
     * @see #getPreloadedFiles()
     * @since 02.00.00
     * @author Ktt Development
     */
    @SuppressWarnings("SpellCheckingInspection")
    public final File getFile(final String path){
        final String relative = getContext(path);
        if(loadingOption != ByteLoadingOption.LIVELOAD){
            return preloadedFiles.get(relative).getFile();
        }else{ // file is a reference, existence of file does not matter
            final String dabs = directory.getAbsolutePath();
            final File parentFile = new File(dabs + relative).getParentFile();
            final String pabs = parentFile.getAbsolutePath();

            // if is in top level directory (both cases) or if is a child of the directory folder (walk case)
            // null otherwise
            return pabs.equals(dabs) || (isWalkthrough && pabs.startsWith(dabs)) ? new File(dabs + relative) : null;
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
        if(loadingOption != ByteLoadingOption.LIVELOAD ){ // find preloaded bytes
            return preloadedFiles.get(rel).getBytes(); // already adapted
        }else{
            try{
                final File file = Objects.requireNonNull(getFile(path)); // find if file allowed
                return adapter.getBytes(file,Files.readAllBytes(file.toPath())); // adapt bytes here
            }catch(final NullPointerException | IOException ignored){
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
            "preloadedFiles"    + '=' +     preloadedFiles  +
            '}';
    }

}
