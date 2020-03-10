package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.ExchangePacket;
import ktt.lib.httpserver.RequestHandler;
import ktt.lib.httpserver.http.HTTPCode;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * A Request Handler that returns a file.
 * @see RequestHandler
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings({"unused","WeakerAccess"})
@Deprecated
public class FilePostHandler extends RequestHandler {

    private final Map<String,_pair<File,Boolean>> dat = new HashMap<>();

    // File //

        // addFile(File, ...)

            /**
             * Adds a file.
             * @param file File to add
             * @see #addFile(File, String)
             * @see #addFile(String, File)
             * @see #addFile(String, File, String)
             * @since 01.00.00
             */
            public final void addFile(File file){
                addFile("", file, file.getName());
            }

            /**
             * Adds a file, using the desired name.
             * @param file File to add
             * @param name name to use
             * @see #addFile(File)
             * @see #addFile(String, File)
             * @see #addFile(String, File, String)
             * @since 01.00.00
             */
            public final void addFile(File file, String name){
                addFile("", file, name);
            }

        // addFile(String, File, ...)

            /**
             * Adds a file at the specified context.
             * @param context context to use
             * @param file File to add
             * @see #addFile(File)
             * @see #addFile(File, String)
             * @see #addFile(String, File, String)
             * @since 01.00.00
             */
            public final void addFile(String context, File file){
                addFile(context, file, file.getName());
            }

            // Master FN //

            /**
             * Adds a file at the specified context, using the desired name.
             * @param context context to use
             * @param file File to add
             * @param name name to use
             * @see #addFile(File)
             * @see #addFile(File, String)
             * @see #addFile(String, File)
             * @since 01.00.00
             */
            public final void addFile(String context, File file, String name){
                String key = __.startAndEndSlash(context) + __.noSlash(name);
                dat.put(key, new _pair<>(file, false));
            }

    // Files //

        // addFiles(File[], ...)

            /**
             * Adds multiple files.
             * @param files files to add
             * @see #addFiles(String, File[])
             * @since 01.00.00
             */
            public final void addFiles(File[] files){
                addFiles("", files);
            }

        // addFiles(String, File[], ...)

            // Inherit FN //

            /**
             * Adds multiple files at the specified context.
             * @param context context to use
             * @param files files to add
             * @see #addFiles(File[])
             * @since 01.00.00
             */
            public final void addFiles(String context, File[] files){
                for(File file : files){
                    addFile(context, file, file.getName());
                }
            }

    // Directory //

        // addDirectory(File, ...)

            /**
             * Adds a directories contents.
             * @param file directory to use
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, boolean)
             * @since 01.00.00
             */
            public final void addDirectory(File file){
                addDirectory("", file, false);
            }

            /**
             * Adds a directories contents.
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @see #addDirectory(File)
             * @see #addDirectory(String, File)
             * @see #addDirectory(String, File, boolean)
             * @since 01.00.00
             */
            public final void addDirectory(File file, boolean walk){
                addDirectory("", file, walk);
            }

        // addDirectory(String, File, ...)

            /**
             * Adds a directories contents at a specified context.
             * @param context context to use
             * @param file directory to use
             * @see #addDirectory(File)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(String, File, boolean)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file){
                addDirectory(context, file, false);
            }

            // Master FN //

            /**
             * Adds a directories contents at a specific context.
             * @param context context to use
             * @param file directory to use
             * @param walk whether to use inner directories or not
             * @see #addDirectory(File)
             * @see #addDirectory(File, boolean)
             * @see #addDirectory(String, File)
             * @since 01.00.00
             */
            public final void addDirectory(String context, File file, boolean walk){
                String key = __.startAndEndSlash(context) + __.noSlash(file.getName());
                dat.put(key, new _pair<>(file, walk));
            }

    // Directories //

        // addDirectories(File[], ...)

            /**
             * Adds multiple directories contents.
             * @param files directories to use
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], boolean)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files){
                addDirectories("", files, false);
            }

            /**
             * Adds multiple directories contents.
             * @param files directories to use
             * @param walk Whether to use inner directories or not
             * @see #addDirectories(File[])
             * @see #addDirectories(String, File[])
             * @see #addDirectories(String, File[], boolean)
             * @since 01.00.00
             */
            public final void addDirectories(File[] files, boolean walk){
                addDirectories("", files, walk);
            }

        // addDirectories(String, File[], ...)

            /**
             * Adds multiple directories contents at a specified context.
             * @param context context to use
             * @param files directories to use
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(String, File[], boolean)
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files){
                addDirectories(context, files, false);
            }

            // Inherit FN //

            /**
             * Adds multiple directories contents at a specified context.
             * @param context context to use
             * @param files directories to use
             * @param walk whether to use inner directories or not
             * @see #addDirectories(File[])
             * @see #addDirectories(File[], boolean)
             * @see #addDirectories(String, File[])
             * @since 01.00.00
             */
            public final void addDirectories(String context, File[] files, boolean walk){
                for(File file : files){
                    addDirectory(context, file, walk);
                }
            }

    @Override
    public final void handle(ExchangePacket packet) throws IOException{
        String rel = URLDecoder.decode(__.startSlash(packet.getRelativeContext()), StandardCharsets.UTF_8);

        String match = "";
        for(String key : dat.keySet()){
            if(rel.startsWith(key) && key.startsWith(match)){
                match = key;
            }
        }

        if(!match.isEmpty()) {
            final _pair<File, Boolean> pair = dat.get(match);
            final File mfile = pair.a;
            final String path = __.noSlash(mfile.getAbsolutePath()) + rel.substring(rel.indexOf(match) + match.length());
            final File file = new File(path);
            final boolean walk = pair.b;

            if(
                (file.exists() && !file.isDirectory()) &&
                (file.getAbsolutePath().equalsIgnoreCase(mfile.getAbsolutePath()) || walk)
            ){
                handle(packet, file);
                return;
            }
        }
        packet.send("File not found", HTTPCode.HTTP_NotFound);
    }

    /**
     * Handles the given request and generates a response unless the file was not found.
     * @param packet a packet of data containing client information
     * @param source the file requested by the user
     * @throws IOException internal failure
     * @since 01.01.01
     */
    public void handle(ExchangePacket packet, File source) throws IOException{
        try {
            packet.send(Files.readAllBytes(source.toPath()));
        } catch (IOException ignored) {
            packet.send("Failed to read file", HTTPCode.HTTP_InternalServerError);
        }
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
