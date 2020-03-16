package ktt.lib.httpserver.handler;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Represent a file in the {@link FileHandler}. Applications do not use this class.
 *
 * @see FileHandler
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
class FileEntry {

    private final boolean isPreloaded;

    private final File file;
    private byte[] preloadedBytes;

    /**
     * Creates a file entry.
     *
     * @param file file to represent
     * @param isPreloaded whether to read bytes now or at runtime
     * @param bytesAdapter how to process the bytes in {@link #getBytes()}
     * @throws FileNotFoundException if file was not found or is not a file
     *
     * @see FileBytesAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    FileEntry(final File file, final boolean isPreloaded, final FileBytesAdapter bytesAdapter) throws FileNotFoundException{
        if(!file.exists() || file.isDirectory())
            throw new FileNotFoundException("File at " + file.getAbsoluteFile() + " was not found");

        this.file = file;
        this.isPreloaded = isPreloaded;

        if(isPreloaded)
            try{
                preloadedBytes = bytesAdapter.getBytes(file,Files.readAllBytes(file.toPath()));
            }catch(final IOException ignored){
                preloadedBytes = null;
            }
    }

//

    /**
     * Returns if the file was preloaded.
     *
     * @return if file was preloaded
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public final boolean isPreloaded(){
        return isPreloaded;
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
     * Returns the file's bytes after the {@link FileBytesAdapter} was used.
     *
     * @return processed file bytes
     *
     * @see FileBytesAdapter
     * @since 02.00.00
     * @author Ktt Development
     */
    public final byte[] getBytes(){
        if(isPreloaded)
            return preloadedBytes;
        else
            try{
                return Files.readAllBytes(file.toPath());
            }catch(final IOException ignored){
                return null;
            }
    }

//


    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("FileEntry")         .append("{");
        OUT.append("isPreloaded")       .append("= ")   .append(isPreloaded)                        .append(", ");
        OUT.append("file")              .append("= ")   .append(file.toString())                    .append(", ");
        OUT.append("(preloaded) bytes") .append("= ")   .append(Arrays.toString(preloadedBytes)) .append(", ");
        OUT.append("}");
        return OUT.toString();
    }

}
