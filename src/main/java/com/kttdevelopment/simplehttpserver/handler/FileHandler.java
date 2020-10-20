package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.HttpCode;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * A request handler that processes files using the {@link FileHandlerAdapter}. <br>
 * The <code>context</code> parameter determines if the relative context of the file within the handler before the name. <i>case-sensitive</i> <br>
 * The <code>fileName</code> parameter overrides the {@link FileHandlerAdapter#getName(File)} and determines the name of the file after the context (if there is one). <br>
 * The <code>directoryName</code> parameter determines the directory's name. Add the files at the top level by keeping this field empty. <br>
 * The <code>loadingOption</code> parameter determines how files should be loaded (see {@link ByteLoadingOption}). <br>
 * The <code>walk</code> parameter determines if all the inner directories should be used.
 * The handler will returns data given by the {@link FileHandlerAdapter} unless overridden.
 *
 * @see FileHandlerAdapter
 * @see ByteLoadingOption
 * @see SimpleHttpHandler
 * @see com.sun.net.httpserver.HttpHandler
 * @since 02.00.00
 * @version 4.0.0
 * @author Ktt Development
 */
public class FileHandler implements SimpleHttpHandler {

    private final FileHandlerAdapter adapter;

    private final Map<String,FileEntry> files = new ConcurrentHashMap<>();
    private final Map<String,DirectoryEntry> directories = new ConcurrentHashMap<>();

    /**
     * Creates a file handler without a {@link FileHandlerAdapter}. This will use the files name and bytes.
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public FileHandler(){
        this.adapter = new FileHandlerAdapter() {
            @Override
            public final byte[] getBytes(final File file, final byte[] bytes){
                return bytes;
            }

            @Override
            public final String getName(final File file){
                return FileHandler.this.getName(file);
            }
        };
    }

    /**
     * Creates a file handler with a {@link FileHandler}. This will use the adapted file name and bytes.
     *
     * @param adapter adapter to get name and bytes
     *
     * @see FileHandlerAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    public FileHandler(final FileHandlerAdapter adapter){
        this.adapter = adapter;
    }

//

    /**
     * Adds a file to the handler.
     *
     * @param file file to add
     *
     * @see #addFile(File, ByteLoadingOption)
     * @see #addFile(File, String)
     * @see #addFile(File, String, ByteLoadingOption)
     * @see #addFile(String, File)
     * @see #addFile(String, File, ByteLoadingOption)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, ByteLoadingOption)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final File file){
        addFile("", file, adapter.getName(file), ByteLoadingOption.LIVELOAD);
    }

    /**
     * Adds a file to the handler.
     *
     * @param file file to add
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addFile(File)
     * @see #addFile(File, String)
     * @see #addFile(File, String, ByteLoadingOption)
     * @see #addFile(String, File)
     * @see #addFile(String, File, ByteLoadingOption)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, ByteLoadingOption)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addFile(final File file, final ByteLoadingOption loadingOption){
        addFile("", file, adapter.getName(file), loadingOption);
    }

    /**
     * Adds a file to the handler with a specified name.
     *
     * @param file file to add
     * @param fileName file name to use
     *
     * @see #addFile(File)
     * @see #addFile(File, ByteLoadingOption)
     * @see #addFile(File, String, ByteLoadingOption)
     * @see #addFile(String, File)
     * @see #addFile(String, File, ByteLoadingOption)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, ByteLoadingOption)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final File file, final String fileName){
        addFile("", file, fileName, ByteLoadingOption.LIVELOAD);
    }

    /**
     * Adds a file to the handler with a specified name.
     *
     * @param file file to add
     * @param fileName file name to use
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addFile(File)
     * @see #addFile(File, ByteLoadingOption)
     * @see #addFile(File, String)
     * @see #addFile(String, File)
     * @see #addFile(String, File, ByteLoadingOption)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, ByteLoadingOption)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addFile(final File file, final String fileName, final ByteLoadingOption loadingOption){
        addFile("", file, fileName, loadingOption);
    }

    /**
     * Adds a file to the handler at a specified context.
     *
     * @param context context to use
     * @param file file to add
     *
     * @see #addFile(File)
     * @see #addFile(File, ByteLoadingOption)
     * @see #addFile(File, String)
     * @see #addFile(File, String, ByteLoadingOption)
     * @see #addFile(String, File, ByteLoadingOption)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, ByteLoadingOption)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file){
        addFile(context, file, adapter.getName(file), ByteLoadingOption.LIVELOAD);
    }

    /**
     * Adds a file to the handler at a specified context.
     *
     * @param context context to use
     * @param file file to add
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addFile(File)
     * @see #addFile(File, ByteLoadingOption)
     * @see #addFile(File, String)
     * @see #addFile(File, String, ByteLoadingOption)
     * @see #addFile(String, File)
     * @see #addFile(String, File, String)
     * @see #addFile(String, File, String, ByteLoadingOption)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file, final ByteLoadingOption loadingOption){
        addFile(context, file, adapter.getName(file), loadingOption);
    }

    /**
     * Adds a file to the handler at a specified context with a specified name.
     *
     * @param context context to use
     * @param file file to add
     * @param fileName file name to use
     *
     * @see #addFile(File)
     * @see #addFile(File, ByteLoadingOption)
     * @see #addFile(File, String)
     * @see #addFile(File, String, ByteLoadingOption)
     * @see #addFile(String, File)
     * @see #addFile(String, File, ByteLoadingOption)
     * @see #addFile(String, File, String, ByteLoadingOption)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file, final String fileName){
        addFile(context, file, fileName, ByteLoadingOption.LIVELOAD);
    }

    /**
     * Adds a file to the handler at a specified context with a specified name.
     *
     * @param context context to use
     * @param file file to add
     * @param fileName file name to use
     * @param loadingOption loading option
     *
     * @see ByteLoadingOption
     * @see #addFile(File)
     * @see #addFile(File, ByteLoadingOption)
     * @see #addFile(File, String)
     * @see #addFile(File, String, ByteLoadingOption)
     * @see #addFile(String, File)
     * @see #addFile(String, File, ByteLoadingOption)
     * @see #addFile(String, File, String)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addFile(final String context, final File file, final String fileName, final ByteLoadingOption loadingOption){
        try{
            files.put(
                ContextUtil.joinContexts(true, false, context, fileName),
                new FileEntry(file, adapter, loadingOption)
            );
        }catch(final UncheckedIOException ignored){ }
    }

    //

    /**
     * Adds multiple files to the handler.
     *
     * @param files files to add
     *
     * @see #addFiles(File[], ByteLoadingOption)
     * @see #addFiles(String, File[])
     * @see #addFiles(String, File[], ByteLoadingOption)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFiles(final File[] files){
        addFiles("", files, ByteLoadingOption.LIVELOAD);
    }

    /**
     * Adds multiple files to the handler.
     *
     * @param files files to add
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addFiles(File[])
     * @see #addFiles(String, File[])
     * @see #addFiles(String, File[], ByteLoadingOption)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addFiles(final File[] files, final ByteLoadingOption loadingOption){
        addFiles("", files, loadingOption);
    }

    /**
     * Adds multiple files to the handler at a specified context.
     *
     * @param context context to use
     * @param files files to add
     *
     * @see #addFiles(File[])
     * @see #addFiles(File[], ByteLoadingOption)
     * @see #addFiles(String, File[], ByteLoadingOption)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addFiles(final String context, final File[] files){
        addFiles(context, files, ByteLoadingOption.LIVELOAD);
    }

    /**
     * Adds multiple files to the handler at a specified context.
     *
     * @param context context to use
     * @param files files to add
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addFiles(File[])
     * @see #addFiles(File[], ByteLoadingOption)
     * @see #addFiles(String, File[])
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addFiles(final String context, final File[] files, final ByteLoadingOption loadingOption){
        for(final File file : files)
            addFile(context, file, loadingOption);
    }

    //

    /**
     * Adds a directory to the handler.
     *
     * @param directory directory to add
     *
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory){
        addDirectory("", directory, getName(directory), ByteLoadingOption.LIVELOAD, false);
    }

    /**
     * Adds a directory to the handler.
     *
     * @param directory directory to add
     * @param walk whether to use sub-directories or not
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.02
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final boolean walk){
        addDirectory("", directory, getName(directory), ByteLoadingOption.LIVELOAD, walk);
    }

    /**
     * Adds a directory to the handler.
     *
     * @param directory directory to add
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final ByteLoadingOption loadingOption){
        addDirectory("", directory, getName(directory), loadingOption, false);
    }

    /**
     * Adds a directory to the handler.
     *
     * @param directory directory to add
     * @param loadingOption file loading option
     * @param walk whether to use sub-directories or not
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final ByteLoadingOption loadingOption, final boolean walk){
        addDirectory("", directory, getName(directory), loadingOption, walk);
    }

    /**
     * Adds a directory to the handler with a specified name.
     *
     * @param directory directory to add
     * @param directoryName directory name to use
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final String directoryName){
        addDirectory("", directory, directoryName, ByteLoadingOption.LIVELOAD, false);
    }

    /**
     * Adds a directory to the handler with a specified name.
     *
     * @param directory directory to add
     * @param directoryName directory name to use
     * @param walk whether to use sub-directories or not
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.02
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final String directoryName, final boolean walk){
        addDirectory("", directory, directoryName, ByteLoadingOption.LIVELOAD, walk);
    }

    /**
     * Adds a directory to the handler with a specified name.
     *
     * @param directory directory to add
     * @param directoryName directory name to use
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final String directoryName, final ByteLoadingOption loadingOption){
        addDirectory("", directory, directoryName, loadingOption, false);
    }

    /**
     * Adds a directory to the handler with a specified name.
     *
     * @param directory directory to add
     * @param directoryName directory name to use
     * @param loadingOption file loading option
     * @param walk whether to use sub-directories or not
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final File directory, final String directoryName, final ByteLoadingOption loadingOption, final boolean walk){
        addDirectory("", directory, directoryName, loadingOption, walk);
    }

    /**
     * Adds a directory to the handler at a specified context.
     *
     * @param context context to use
     * @param directory directory to add
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory){
        addDirectory(context, directory, getName(directory), ByteLoadingOption.LIVELOAD, false);
    }

    /**
     * Adds a directory to the handler at a specified context.
     *
     * @param context context to use
     * @param directory directory to add
     * @param walk whether to use sub-directories or not
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     */
    public final void addDirectory(final String context, final File directory, final boolean walk){
        addDirectory(context, directory, getName(directory), ByteLoadingOption.LIVELOAD, walk);
    }

    /**
     * Adds a directory to the handler at a specified context.
     *
     * @param context context to use
     * @param directory directory to add
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final ByteLoadingOption loadingOption){
        addDirectory(context, directory, getName(directory), loadingOption, false);
    }

    /**
     * Adds a directory to the handler at a specified context.
     *
     * @param context context to use
     * @param directory directory to add
     * @param loadingOption file loading option
     * @param walk whether to use sub-directories or not
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final ByteLoadingOption loadingOption, final boolean walk){
        addDirectory(context, directory, getName(directory), loadingOption, walk);
    }

    /**
     * Adds a directory to the handler at a specified context with a specified name.
     *
     * @param context context to use
     * @param directory directory to add
     * @param directoryName directory name to use
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final String directoryName){
        addDirectory(context, directory, directoryName, ByteLoadingOption.LIVELOAD, false);
    }

    /**
     * Adds a directory to the handler at a specified context with a specified name.
     *
     * @param context context to use
     * @param directory directory to add
     * @param directoryName directory name to use
     * @param walk whether to use sub-directories or not
     *
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.01
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final String directoryName, final boolean walk){
        addDirectory(context, directory, directoryName, ByteLoadingOption.LIVELOAD, walk);
    }

    /**
     * Adds a directory to the handler at a specified context with a specified name.
     *
     * @param context context to use
     * @param directory directory to add
     * @param directoryName directory name to use
     * @param loadingOption file loading option
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption, boolean)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final String directoryName, final ByteLoadingOption loadingOption){
        addDirectory(context, directory, directoryName, loadingOption, false);
    }

    /**
     * Adds a directory to the handler at a specified context with a specified name.
     *
     * @param context context to use
     * @param directory directory to add
     * @param directoryName directory name to use
     * @param loadingOption file loading option
     * @param walk whether to use sub-directories or not
     *
     * @see ByteLoadingOption
     * @see #addDirectory(File)
     * @see #addDirectory(File, boolean)
     * @see #addDirectory(File, ByteLoadingOption)
     * @see #addDirectory(File, ByteLoadingOption, boolean)
     * @see #addDirectory(File, String)
     * @see #addDirectory(File, String, boolean)
     * @see #addDirectory(File, String, ByteLoadingOption)
     * @see #addDirectory(File, String, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File)
     * @see #addDirectory(String, File, boolean)
     * @see #addDirectory(String, File, ByteLoadingOption)
     * @see #addDirectory(String, File, ByteLoadingOption, boolean)
     * @see #addDirectory(String, File, String)
     * @see #addDirectory(String, File, String, boolean)
     * @see #addDirectory(String, File, String, ByteLoadingOption)
     * @since 03.05.00
     * @author Ktt Development
     */
    public final void addDirectory(final String context, final File directory, final String directoryName, final ByteLoadingOption loadingOption, final boolean walk){
        try{
            final String target = ContextUtil.joinContexts(true, false, context, directoryName);
            directories.put(
                target,
                new DirectoryEntry(directory, adapter, loadingOption, walk)
            );
        }catch(final UncheckedIOException ignored){}
    }

//

    private String getName(final File file){
        return file.getParentFile() == null ? file.getPath() : file.getName();
    }

//

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        final String context = URLDecoder.decode(ContextUtil.getContext(exchange.getURI().getPath().substring(exchange.getHttpContext().getPath().length()), true, false), StandardCharsets.UTF_8);

        if(files.containsKey(context)){ // exact file match
            final FileEntry entry = files.get(context);
            handle(exchange, entry.getFile(), entry.getBytes());
        }else{ // leading directory match
            String match = "";
            for(final String key : directories.keySet())
                if(context.startsWith(key) && key.startsWith(match))
                    match = key;

            if(match.isEmpty()){ // no match
                handle(exchange, null, null);
            }else{ // get file from matching directory
                final DirectoryEntry dir = directories.get(match);
                String rel = context.substring(match.length());

                final FileEntry entry = dir.getFileEntry(rel);

                handle(
                    exchange,
                    entry == null ? dir.getFile(rel) : entry.getFile(),
                    entry == null ? dir.getBytes(rel) : entry.getBytes()
                );
            }
        }
        exchange.close();

        // cache only
        final long now;
        if(adapter instanceof CacheFileAdapter && ((CacheFileAdapter) adapter).getCacheTimeMillis() < (now = System.currentTimeMillis())){ // if lowest cached elapsed
            final Consumer<FileEntry> update = entry -> {
                if(entry.getLoadingOption() == ByteLoadingOption.CACHELOAD && entry.getExpiry() < now) // clear bytes from files where cache time elapsed
                    entry.clearBytes();
                ((CacheFileAdapter) adapter).updateClosestExpiry(entry.getExpiry()); // check if lowest expiry needs to be changed
            };

            files.values().forEach(update);
            directories.values().forEach(dir -> dir.getFiles().values().forEach(update));
        }
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        SimpleHttpHandler.super.handle(exchange);
    }

    /**
     * Handles a file and gives a response.
     *
     * @param exchange the client information
     * @param source the file
     * @param bytes the files adapted bytes
     * @throws IOException internal failure
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public void handle(final SimpleHttpExchange exchange, final File source, final byte[] bytes) throws IOException {
        exchange.send(bytes, HttpCode.HTTP_OK);
    }

//

    @Override
    public String toString(){
        return
            "FileHandler"           + '{' +
            "adapter"               + '=' +     adapter.toString()  + ", " +
            "(loaded) files"        + '=' +     files               + ", " +
            "(loaded) directories"  + '=' +     directories         +
            '}';
    }

}
