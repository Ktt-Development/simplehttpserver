package com.kttdevelopment.simplehttpserver.handler;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;

/**
 * Represent a file in the {@link FileHandler}. Applications do not use this class.
 *
 * @see ByteLoadingOption
 * @see FileHandler
 * @since 02.00.00
 * @version 03.05.00
 * @author Ktt Development
 */
class FileEntry {

    private final File file;
    @SuppressWarnings("FieldCanBeLocal")
    private final FileBytesAdapter adapter;
    private final ByteLoadingOption loadingOption;

    private byte[] preloadedBytes;

    /**
     * Creates a file entry.
     *
     * @param file file to represent
     * @param bytesAdapter how to process the bytes in {@link #getBytes()}
     * @param loadingOption how to handle the initial file loading
     * @throws RuntimeException I/O failure to start watch service ({@link ByteLoadingOption#WATCHLOAD} only).
     *
     * @see FileBytesAdapter
     * @see ByteLoadingOption
     * @since 03.05.00
     * @author Ktt Development
     */
    FileEntry(final File file, final FileBytesAdapter bytesAdapter, final ByteLoadingOption loadingOption){
        this(file,bytesAdapter,loadingOption,true);
    }

    /**
     * Creates a file entry.
     *
     * @param file file to represent
     * @param bytesAdapter how to process the bytes in {@link #getBytes()}
     * @param loadingOption how to handle the initial file loading
     * @param skipWatchService skip creating a watch service ({@link ByteLoadingOption#WATCHLOAD} only).
     * @throws RuntimeException I/O failure to start watch service ({@link ByteLoadingOption#WATCHLOAD} only).
     *
     * @see FileBytesAdapter
     * @see ByteLoadingOption
     * @since 03.05.00
     * @author Ktt Development
     */
    FileEntry(final File file, final FileBytesAdapter bytesAdapter, final ByteLoadingOption loadingOption, final boolean skipWatchService){
        this.file = file;
        this.loadingOption = loadingOption;
        this.adapter = bytesAdapter;

        switch(loadingOption){
            case WATCHLOAD:
                if(!skipWatchService)
                    try{
                        final WatchService service = FileSystems.getDefault().newWatchService();
                        final Path path = file.toPath();
                        path.register(service,StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);

                        new Thread(() -> {
                            WatchKey key;
                            try{
                                while((key = service.take()) != null){
                                    for(WatchEvent<?> event : key.pollEvents()){
                                        try{
                                            final Path target = (Path) event.context();
                                            try{
                                                if(Files.isSameFile(path, target))
                                                    preloadedBytes = bytesAdapter.getBytes(file,Files.readAllBytes(path));
                                            }catch(final IOException ignored){ } // don't overwrite if corrupt
                                        }catch(final ClassCastException ignored){ }
                                    }
                                    key.reset();
                                }
                            }catch(final InterruptedException ignored){ }
                        }).start();
                    }catch(final IOException e){
                        throw new RuntimeException(e);
                    }
            case PRELOAD:
                try{
                    preloadedBytes = bytesAdapter.getBytes(file,Files.readAllBytes(file.toPath()));
                }catch(final Exception ignored){
                    preloadedBytes = null;
                }
        }

    }

//

    /**
     * Returns the file being referenced
     *
     * @return reference file
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public final File getFile(){
        return file;
    }

    /**
     * Reloads the file's preloaded bytes using the {@link FileBytesAdapter}.
     *
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void reloadBytes(){
        if(loadingOption != ByteLoadingOption.LIVELOAD)
            try{
                preloadedBytes = adapter.getBytes(file,Files.readAllBytes(file.toPath()));
            }catch(final IOException ignored){ }
    }

    /**
     * Returns the file's bytes after the {@link FileBytesAdapter} was used.
     *
     * @return processed file bytes
     *
     * @see FileBytesAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    public final byte[] getBytes(){
        if(loadingOption != ByteLoadingOption.LIVELOAD)
            return preloadedBytes; // adapter determined preloaded bytes
        else
            try{
                return Files.readAllBytes(file.toPath()); // return literal bytes (no preload); adapt bytes in next part
            }catch(final IOException e){
                return null;
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


    @Override
    public String toString(){
        return
            "FileEntry"         + '{' +
            "file"              + '=' +     file            + ", " +
            "adapter"           + '=' +     adapter         + ", " +
            "loadingOption"     + '=' +     loadingOption   + ", " +
            "preloadedBytes"    + '=' +     Arrays.toString(preloadedBytes) +
            '}';
    }

}
