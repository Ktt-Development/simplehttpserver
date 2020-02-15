package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.ExchangePacket;
import ktt.lib.httpserver.RequestHandler;
import ktt.lib.httpserver.http.HTTPCode;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * A Request Handler that returns a file and preloaded bytes.
 * <br>
 * Methods that use the {@link FileBytesAdapter} will preload bytes using {@link FileBytesAdapter#getBytes(File)}.
 * <br>
 * Methods that use the {@link FileNameAdapter} will name the files using {@link FileNameAdapter#getName(File)}. This will only name files, not the directories themselves.
 * @see FileBytesAdapter
 * @see FileNameAdapter
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings({"unused","WeakerAccess"})
public class FilePreHandler extends RequestHandler {

    private final Map<String,_pair<File,byte[]>> dat = new HashMap<>();

    private final FileBytesAdapter defaultBytesAdapter = file -> {
        if(file.exists() || file.isDirectory()){
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                return "Failed to read file".getBytes(StandardCharsets.UTF_8);
            }
        }else{
            return "File not found".getBytes(StandardCharsets.UTF_8);
        }
    };

    private final FileNameAdapter defaultNameAdapter = File::getName;

    // File //

        // addFile(File, ...)

            /**
             * Adds a file to <code>handler_context/file_name</code>
             * @param file File to add
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(File file){
                addFile("", file, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/file_name</code>
             * @param file File to add
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(File file, FileBytesAdapter bytesAdapter){
                addFile("", file, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/adapter_name</code>
             * @param file File to add
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(File file, FileNameAdapter nameAdapter){
                addFile("", file, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/adapter_name</code>
             * @param file File to add
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(File file, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addFile("", file, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/adapter_name</code>
             * @param file File to add
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(File file, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addFile("", file, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/name</code>
             * @param file File to add
             * @param name name to use
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(File file, String name){
                addFile(
                    "",
                    file,
                    param -> name,
                    defaultBytesAdapter
                );
            }

            /**
             * Adds a file to <code>handler_context/name</code>
             * @param file File to add
             * @param name name to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(File file, String name, FileBytesAdapter bytesAdapter){
                addFile(
                    "",
                    file,
                    param -> name,
                    bytesAdapter
                );
            }

        // addFile(String, File, ...)

            /**
             * Adds a file to <code>handler_context/context/file_name</code>
             * @param context context to use
             * @param file File to add
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(String context, File file){
                addFile(context, file, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/context/file_name</code>
             * @param context context to use
             * @param file File to add
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(String context, File file, FileBytesAdapter bytesAdapter){
                addFile(context, file, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/context/adapter_name</code>
             * @param context context to use
             * @param file File to add
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(String context, File file, FileNameAdapter nameAdapter){
                addFile(context, file, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/context/adapter_name</code>
             * @param context context to use
             * @param file File to add
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(String context, File file, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addFile(context, file, nameAdapter, bytesAdapter);
            }

            // Master FN //

            /**
             * Adds a file to <code>handler_context/context/adapter_name</code>
             * @param context context to use
             * @param file File to add
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, String)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(String context, File file, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                String key = __.startAndEndSlash(context) + __.noSlash(nameAdapter.getName(file));
                dat.put(key,new _pair<>(file, bytesAdapter.getBytes(file)));
            }

            /**
             * Adds a file to <code>handler_context/context/name</code>
             * @param context context to use
             * @param file File to add
             * @param name name to use
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFile(String context, File file, String name){
                addFile(
                    context,
                    file,
                    param -> name,
                    defaultBytesAdapter
                );
            }

            /**
             * Adds a file to <code>handler_context/context/name</code>
             * @param context context to use
             * @param file File to add
             * @param name name to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addFile(File)
             * @see #addFile(File, FileBytesAdapter)
             * @see #addFile(File, FileNameAdapter)
             * @see #addFile(File, FileBytesAdapter, FileNameAdapter)
             * @see #addFile(File, FileNameAdapter, FileBytesAdapter)
             * @see #addFile(File, String)
             * @see #addFile(File, String, FileBytesAdapter)
             * @see #addFile(String, File)
             * @see #addFile(String, File, FileNameAdapter)
             * @see #addFile(String, File, FileBytesAdapter)
             * @see #addFile(String, File, FileBytesAdapter,FileNameAdapter)
             * @see #addFile(String, File, FileNameAdapter,FileBytesAdapter)
             * @see #addFile(String, File, String)
             * @since 01.00.00
             */
            public final void addFile(String context, File file, String name, FileBytesAdapter bytesAdapter){
                addFile(
                    context,
                    file,
                    param -> name,
                    bytesAdapter
                );
            }

    // Files //

        // addFiles(File[], ...)

            /**
             * Adds a file to <code>handler_context/file_names</code>
             * @param files files to add
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(File[] files){
                addFiles("", files, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/file_names</code>
             * @param files files to add
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(File[] files, FileBytesAdapter bytesAdapter){
                addFiles("", files, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/adapter_names</code>
             * @param files files to add
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(File[] files, FileNameAdapter nameAdapter){
                addFiles("", files, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/adapter_names</code>
             * @param files files to add
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(File[] files, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addFiles("", files, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/file_names</code>
             * @param files files to add
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(File[] files, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addFiles("", files, nameAdapter, bytesAdapter);
            }

        // addFiles(String, File[], ...)

            /**
             * Adds a file to <code>handler_context/context/file_names</code>
             * @param context context to use
             * @param files files to add
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(String context, File[] files){
                addFiles(context, files, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/context/file_names</code>
             * @param context context to use
             * @param files files to add
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(String context, File[] files, FileBytesAdapter bytesAdapter){
                addFiles(context, files, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/context/adapter_names</code>
             * @param context context to use
             * @param files files to add
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(String context, File[] files, FileNameAdapter nameAdapter){
                addFiles(context, files, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a file to <code>handler_context/context/adapter_names</code>
             * @param context context to use
             * @param files files to add
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addFiles(String context, File[] files, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addFiles(context, files, nameAdapter, bytesAdapter);
            }

            // Inherit FN //

            /**
             * Adds a file to <code>handler_context/context/adapter_names</code>
             * @param context context to use
             * @param files file to add
             * @param nameAdapter file byte preloader
             * @param bytesAdapter file name adapter
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addFiles(File[])
             * @see #addFiles(File[], FileBytesAdapter)
             * @see #addFiles(File[], FileNameAdapter)
             * @see #addFiles(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addFiles(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addFiles(String, File[])
             * @see #addFiles(String, File[], FileBytesAdapter)
             * @see #addFiles(String, File[], FileNameAdapter)
             * @see #addFiles(String, File[], FileBytesAdapter, FileNameAdapter)
             * @since 01.00.00
             */
            public final void addFiles(String context, File[] files, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                for(File file : files){
                    addFile(context, file, nameAdapter, bytesAdapter);
                }
            }

    // Directory //

        // addDirectory(File, ...)

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/file_names</code>
             * @param file directory to use
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file){
                addDirectory("", file, file.getName(), false, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/file_names</code>
             * @param file directory to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, FileBytesAdapter bytesAdapter){
                addDirectory("", file, file.getName(), false, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/adapter_names</code>
             * @param file directory to use
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, FileNameAdapter nameAdapter){
                addDirectory("", file, file.getName(), false, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/adapter_names</code>
             * @param file directory to use
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory("", file, file.getName(), false, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/adapter_names</code>
             * @param file directory to use
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectory("", file, file.getName(), false, nameAdapter, bytesAdapter);
            }

        // addDirectory(File, boolean, ...)

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/file_names</code>
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, boolean walk){
                addDirectory("", file, file.getName(), walk, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/file_names</code>
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, boolean walk, FileBytesAdapter bytesAdapter){
                addDirectory("", file, file.getName(), walk, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/adapter_names</code>
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, boolean walk, FileNameAdapter nameAdapter){
                addDirectory("", file, file.getName(), walk, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/adapter_names</code>
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, boolean walk, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory("", file, file.getName(), walk, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/directory_name</code> then its contents to <code>handler_context/directory_name/adapter_names</code>
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(File file, boolean walk, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectory("", file, file.getName(), walk, nameAdapter, bytesAdapter);
            }

        // addDirectory(File, String, ...)

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/file_names</code>
             * @param file directory to use
             * @param name name to use
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name){
                addDirectory("", file, name, false, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/file_names</code>
             * @param file directory to use
             * @param name name to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, FileBytesAdapter bytesAdapter){
                addDirectory("", file, name, false, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param file directory to use
             * @param name name to use
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, FileNameAdapter nameAdapter){
                addDirectory("", file, name, false, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param file directory to use
             * @param name name to use
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory("", file, name, false, bytesAdapter, nameAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param file directory to use
             * @param name name to use
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectory("", file, name, false, defaultNameAdapter, defaultBytesAdapter);
            }

            // addDirectory(String, File, String, boolean)

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/file_names</code>
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner directories or not
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, boolean walk){
                addDirectory("", file, name, walk, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, boolean walk, FileBytesAdapter bytesAdapter){
                addDirectory("", file, name, walk, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, boolean walk, FileNameAdapter nameAdapter){
                addDirectory("", file, name, walk, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, boolean walk, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory("", file, name, false, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(File file, String name, boolean walk, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectory("", file, name, false, nameAdapter, bytesAdapter);
            }

        // addDirectory(String, File, ...)

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file){
                addDirectory(context, file, file.getName(), false, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, FileBytesAdapter bytesAdapter){
                addDirectory(context, file, file.getName(), false, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, FileNameAdapter nameAdapter){
                addDirectory(context, file, file.getName(), false, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory(context, file, file.getName(), false, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/adapter_names</code>
             * @param context context to use
             * @param file file to use
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectory(context, file, file.getName(), false, nameAdapter, bytesAdapter);
            }

        // addDirectory(String, File, boolean, ...)

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, boolean walk){
                addDirectory(context, file, file.getName(), walk, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, boolean walk, FileBytesAdapter bytesAdapter){
                addDirectory(context, file, file.getName(), walk, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, boolean walk, FileNameAdapter nameAdapter){
                addDirectory(context, file, file.getName(), walk, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, boolean walk, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory(context, file, file.getName(), walk, nameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/context/directory_name</code> then its contents to <code>handler_context/context/directory_name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param walk whether to use inner files or not
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, boolean walk, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectory(context, file, file.getName(), walk, nameAdapter, bytesAdapter);
            }

        // addDirectory(String, File, String, ...)

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name){
                addDirectory(context, file, name, false, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, FileBytesAdapter bytesAdapter){
                addDirectory(context, file, name, false, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, FileNameAdapter nameAdapter){
                addDirectory(context, file, name, false, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory(context, file, name, false, bytesAdapter, nameAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectory(context, file, name, false, defaultNameAdapter, defaultBytesAdapter);
            }

        // addDirectory(String, File, String, boolean)

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner files or not
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, boolean walk){
                addDirectory(context, file, name, walk, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/file_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner files or not
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, boolean walk, FileBytesAdapter bytesAdapter){
                addDirectory(context, file, name, walk, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner files or not
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, boolean walk, FileNameAdapter nameAdapter){
                addDirectory(context, file, name, walk, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner files or not
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, boolean walk, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectory(context, file, name, false, nameAdapter, bytesAdapter);
            }

            // Master FN //

            /**
             * Adds a directory to <code>handler_context/name</code> then its contents to <code>handler_context/name/adapter_names</code>
             * @param context context to use
             * @param file directory to use
             * @param name name to use
             * @param walk whether to use inner files or not
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectory(File)
             * @see #addDirectory(File, FileBytesAdapter)
             * @see #addDirectory(File, FileNameAdapter)
             * @see #addDirectory(File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(File, boolean, FileBytesAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String)
             * @see #addDirectory(File, String, FileBytesAdapter)
             * @see #addDirectory(File, String, FileNameAdapter)
             * @see #addDirectory(File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(File, String, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, FileBytesAdapter)
             * @see #addDirectory(String, File, FileNameAdapter)
             * @see #addDirectory(String, File, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String)
             * @see #addDirectory(String, File, String, FileBytesAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectory(String, File, String, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter)
             * @see #addDirectory(String, File, String, boolean, FileNameAdapter)
             * @see #addDirectory(String, File, String, boolean, FileBytesAdapter, FileNameAdapter)
             * @since 01.01.01
             */
            public final void addDirectory(String context, File file, String name, boolean walk, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                final String key = __.startAndEndSlash(context) + __.noSlash(name);

                if(file.isDirectory()){
                    if(!walk){
                        File[] files = file.listFiles();
                        addFiles(key, files != null ? files : new File[0], nameAdapter, bytesAdapter);
                    }else{
                        final Path parent = file.toPath();

                        try {
                            Files.walk(parent).forEach(path -> {
                                if(path.toFile().isDirectory()){
                                    String rel = __.endSlash(__.startAndEndSlash(key) + parent.relativize(path).toString());
                                    File[] files = path.toFile().listFiles();
                                    addFiles(rel, files != null ? files : new File[0], nameAdapter, bytesAdapter);
                                }
                            });
                        } catch (IOException ignored) {
                            dat.put(key, new _pair<>(file, bytesAdapter.getBytes(file)));
                        }
                    }
                }else{
                    dat.put(key, new _pair<>(file, bytesAdapter.getBytes(file)));
                }
            }

    // Directories //

        // addDirectories(File[], ...)

            /**
             * Adds multiple directories contents using the file name as their contexts.
             * @param files directories to use
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files){
                addDirectories("", files, false, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts.
             * @param files directories to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, FileBytesAdapter bytesAdapter){
                addDirectories("", files, false, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts.
             * @param files directories to use
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, FileNameAdapter nameAdapter){
                addDirectories("", files, false, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts; using the adapter name as their contexts.
             * @param files directories to use
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectories("", files, false, nameAdapter, bytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts; using the adapter name as their contexts.
             * @param files directories to use
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectories("", files, false, nameAdapter, bytesAdapter);
            }

        // addDirectories(File[], boolean, ...)

            /**
             * Adds multiple directories contents using the file name as their contexts.
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, boolean walk){
                addDirectories("", files, walk, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts.
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, boolean walk, FileBytesAdapter bytesAdapter){
                addDirectories("", files, walk, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts; using the adapter name as their contexts.
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, boolean walk, FileNameAdapter nameAdapter){
                addDirectories("", files, walk, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts; using the adapter name as their contexts.
             * @param files directories to use
             * @param walk whether to use inner files or not
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, boolean walk, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectories("", files, walk, nameAdapter, bytesAdapter);
            }

            /**
             * Adds multiple directories contents using the file name as their contexts; using the adapter name as their contexts.
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, boolean walk, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectories("", files, walk, nameAdapter, bytesAdapter);
            }

        // addDirectories(String, File[], ...)

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts.
             * @param context context to use
             * @param files directories to use
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files){
                addDirectories(context, files, false, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param bytesAdapter file byte preloader
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, FileBytesAdapter bytesAdapter){
                addDirectories(context, files, false, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, FileNameAdapter nameAdapter){
                addDirectories(context, files, false, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectories(context, files, false, nameAdapter, bytesAdapter);
            }

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                addDirectories(context, files, false, nameAdapter, bytesAdapter);
            }

        // addDirectories(String, File[], boolean, ...)

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, boolean walk){
                addDirectories(context, files, walk, defaultNameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte prelaoder
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, boolean walk, FileBytesAdapter bytesAdapter){
                addDirectories(context, files, walk, defaultNameAdapter, bytesAdapter);
            }

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, boolean walk, FileNameAdapter nameAdapter){
                addDirectories(context, files, walk, nameAdapter, defaultBytesAdapter);
            }

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @param bytesAdapter file byte preloader
             * @param nameAdapter file name adapter
             * @see FileBytesAdapter
             * @see FileNameAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, boolean walk, FileBytesAdapter bytesAdapter, FileNameAdapter nameAdapter){
                addDirectories(context, files, walk, nameAdapter, bytesAdapter);
            }

            // Inherit FN //

            /**
             * Adds multiple directories contents at a specified context using the file name as their contexts; using the name adapter as their contexts.
             * @param context context to use
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @param nameAdapter file name adapter
             * @param bytesAdapter file byte preloader
             * @see FileNameAdapter
             * @see FileBytesAdapter
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], FileBytesAdapter)
             * @see #addDirectories(File[], FileNameAdapter)
             * @see #addDirectories(File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(File[], boolean, FileBytesAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(File[], boolean, FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], FileBytesAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter)
             * @see #addDirectories(String, File[], FileBytesAdapter, FileNameAdapter)
             * @see #addDirectories(String, File[], FileNameAdapter, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter)
             * @see #addDirectories(String, File[], boolean, FileNameAdapter)
             * @see #addDirectories(String, File[], boolean, FileBytesAdapter, FileNameAdapter)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, boolean walk, FileNameAdapter nameAdapter, FileBytesAdapter bytesAdapter){
                for(File file : files){
                    addDirectory(context, file, walk, nameAdapter, bytesAdapter);
                }
            }

//

    @Override
    public final void handle(ExchangePacket packet) throws IOException{
        String rel = __.startSlash(URLDecoder.decode(packet.getRelativeContext(), StandardCharsets.UTF_8));

        final _pair<File,byte[]> out = dat.get(rel);
        if(out != null){
            handle(packet, out.a, out.b);
        }else{
            packet.send("File not found", HTTPCode.HTTP_NotFound);
        }
    }

    /**
     * Handles the given request and generates a response unless the file was not found.
     * @param packet a packet of data containing client information
     * @param source the file requested by the user
     * @param bytes the file's preloaded bytes
     * @throws IOException internal server error
     * since 01.00.00
     */
    public void handle(ExchangePacket packet, File source, byte[] bytes) throws IOException{
        packet.send(bytes);
    }

    /**
     * A pair to be used with the {@link HashMap}. Applications do not normally use this class.
     * @param <A> variable A
     * @param <B> variable B
     * @since 01.00.00
     */
    private static class _pair<A,B>{
        private final A a;
        private final B b;

        /**
         * Creates a pair.
         * @param a variable A
         * @param b variable B
         * @since 01.00.00
         */
        public _pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

}
