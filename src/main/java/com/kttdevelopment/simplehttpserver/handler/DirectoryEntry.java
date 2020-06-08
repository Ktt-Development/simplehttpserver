package com.kttdevelopment.simplehttpserver.handler;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    private final Map<String,FileEntry> preloadedFiles = new ConcurrentHashMap<>(); // preload/watchload only
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
                    createWatchService(directoryPath, createWatchServiceConsumer());
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
                            createWatchService(path, createWatchServiceConsumer());
                        }catch(final IOException e){
                            throw new UncheckedIOException(e);
                        }

                    });
                }catch(final IOException e){
                    throw new RuntimeException(e);
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
                        }catch(final RuntimeException ignored){ }
            }
            if(isWalkthrough){
                final Path dirPath = directory.toPath();

                try{
                    Files.walk(dirPath).filter(path -> path.toFile().isDirectory()).forEach(path -> {
                        final File p2f = path.toFile();
                        final String rel = dirPath.relativize(path).toString();

                        final File[] listFiles = Objects.requireNonNullElse(p2f.listFiles(), new File[0]);
                        for(final File file : listFiles) // populate sub files
                            try{
                                preloadedFiles.put(
                                    (rel.isEmpty() || rel.equals("/") || rel.equals("\\") ? "" : getContext(rel)) + getContext(adapter.getName(file)),
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


    private final Map<Path,Thread> watchService = new ConcurrentHashMap<>();

    private void createWatchService(final Path path, final Consumer<WatchEvent<?>> consumer) throws IOException{
        final WatchService service = FileSystems.getDefault().newWatchService();
        path.register(service,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);

        final Thread th =
        new Thread(() -> {
            WatchKey key;
            try{
                while((key = service.take()) != null){
                    for(WatchEvent<?> event : key.pollEvents()){
                        consumer.accept(event);
                        key.reset();
                    }
                }
            }catch(final InterruptedException ignored){ }
        });

        watchService.put(path,th);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private Consumer<WatchEvent<?>> createWatchServiceConsumer(){

        return (WatchEvent<?> event) -> {
            try{
                final Path target = (Path) event.context();
                final File file = target.toFile();
                final WatchEvent.Kind<?> type = event.kind();

                final String relative = getContext(directoryPath.relativize(target).toString());
                final String context = relative + getContext(adapter.getName(file));

                if(file.isFile())
                    if(type == ENTRY_CREATE)
                        preloadedFiles.put(context, new FileEntry(file,adapter,ByteLoadingOption.WATCHLOAD,true));
                    else if(type == ENTRY_DELETE)
                        preloadedFiles.remove(context);
                    else if(type == ENTRY_MODIFY)
                        preloadedFiles.get(context).reloadBytes();
                    else; // prevent ambiguous else with below
                else
                   if(type == ENTRY_CREATE)
                       try{
                           createWatchService(target, createWatchServiceConsumer());
                       }catch(final IOException ignored){ }
                   else if(type == ENTRY_DELETE){
                       watchService.get(target).stop();
                       watchService.remove(target);
                   }

            }catch(final ClassCastException ignored){ }
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
    public final File getFile(final String path){
        final String rel = getContext(path);
        if(loadingOption != ByteLoadingOption.LIVELOAD){
            String match = "";
            for(final String key : preloadedFiles.keySet())
                if(rel.startsWith(key) && key.startsWith(match))
                    match = key;
            return !match.isEmpty() ? preloadedFiles.get(match).getFile() : null;
        }else{
            if(isWalkthrough){
                final File parent = new File(directory.getAbsolutePath() + path).getParentFile();
                if(!parent.getAbsolutePath().startsWith(directory.getAbsolutePath())) return null;
                final String name = path.substring(path.lastIndexOf('/'));

                for(final File file : Objects.requireNonNullElse(directory.listFiles(p -> !p.isDirectory()),new File[0]))
                    if(getContext(adapter.getName(file)).equals(name))
                        return file;
            }else{
                for(final File file : Objects.requireNonNullElse(directory.listFiles(p -> !p.isDirectory()),new File[0]))
                    if(getContext(adapter.getName(file)).equals(path))
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
            for(final String key : preloadedFiles.keySet())
                if(rel.startsWith(key) && key.startsWith(match))
                    match = key;
            return !match.isEmpty() ? preloadedFiles.get(match).getBytes() : null;
        }else{
            final File file = getFile(path);
            try{
                return adapter.getBytes(file,Files.readAllBytes(Objects.requireNonNull(file).toPath()));
            }catch(final Exception ignored){
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
