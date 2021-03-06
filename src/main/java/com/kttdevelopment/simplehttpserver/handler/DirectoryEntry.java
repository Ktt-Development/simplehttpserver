/*
 * Copyright (C) 2021 Ktt Development
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.ContextUtil;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a directory in the {@link FileHandler}. Applications do not use this class.
 *
 * @see FileHandler
 * @see FileEntry
 * @since 02.00.00
 * @version 4.3.0
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
class DirectoryEntry {

    private final File directory;
    private final FileHandlerAdapter adapter;
    private final ByteLoadingOption loadingOption;
    private final boolean isWalkthrough;

    private final Map<String,FileEntry> files = new ConcurrentHashMap<>(); // non liveload only
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
        this.directory     = directory;
        this.adapter       = adapter;
        this.loadingOption = loadingOption;
        this.isWalkthrough = isWalkthrough;

        directoryPath      = directory.toPath();

        if(loadingOption != ByteLoadingOption.LIVELOAD){
            if(!isWalkthrough){
                final File[] listFiles = Objects.requireNonNullElse(directory.listFiles(File::isFile), new File[0]);
                for(final File file : listFiles)
                    addFile(file);
            }else{
                try{
                    Files.walkFileTree(directoryPath, new SimpleFileVisitor<>() {
                        @Override
                        public final FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs){
                            addDirectoryFile(path.toFile());
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }catch(final IOException e){
                    throw new UncheckedIOException(e);
                }
            }
        }
    }

    // top level files
    private void addFile(final File file){
        files.put(
            ContextUtil.getContext(adapter.getName(file), true, false),
            new FileEntry(file, adapter, loadingOption)
        );
    }

    // file in sub directories
    private void addDirectoryFile(final File file){
        final String relative = directoryPath.relativize(file.toPath().getParent()).toString(); // attach the relative path (parent) to the adapted file name
        files.put(
            ContextUtil.joinContexts(true, false, relative, adapter.getName(file)),
            new FileEntry(file, adapter, loadingOption)
        );
    }

//

    /**
     * Returns the directory being referenced
     *
     * @return referenced directory
     *
     * @see #getFiles()
     * @see #getFile(String)
     * @see #getFileEntry(String)
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
     * @see #getFileEntry(String)
     * @since 02.00.00
     * @author Ktt Development
     */
    public final Map<String,FileEntry> getFiles(){
        return Collections.unmodifiableMap(files);
    }

    /**
     * Returns the file at the associated path. <b>Doesn't work with LIVELOAD.</b>
     *
     * @param path context to check
     * @return file associated with that context
     *
     * @see #getDirectory()
     * @see #getFiles()
     * @see #getFileEntry(String)
     * @since 02.00.00
     * @author Ktt Development
     */
    @SuppressWarnings("SpellCheckingInspection")
    public final File getFile(final String path){
        final String relative   = ContextUtil.getContext(path, true, false);
        final String dabs       = directory.getAbsolutePath();
        final File parentFile   = new File(dabs + relative).getParentFile();
        final String pabs       = parentFile.getAbsolutePath();

        // if not top level directory or if not child of directory folder, then return null file
        if(!pabs.equals(dabs) && (!isWalkthrough || !pabs.startsWith(dabs))) return null;

        final File targetFile = Paths.get(dabs, relative).toFile();
        final String fileName = targetFile.getParentFile() == null ? targetFile.getPath() : targetFile.getName();

        // for each file in parent directory, run adapter to find file that matches adapted name
        for(final File file : Objects.requireNonNullElse(parentFile.listFiles(), new File[0]))
            if(fileName.equals(adapter.getName(file)))
                return file;
        return null;
    }

    /**
     * Returns the file entry at the associated path. <b>Doesn't work with LIVELOAD.</b>
     *
     * @param path context to check
     * @return file entry associated with that context
     *
     * @see #getDirectory()
     * @see #getFiles()
     * @see #getFile(String)
     * @since 4.0.0
     * @author Ktt Development
     */
    public final FileEntry getFileEntry(final String path){
        final String context  = ContextUtil.getContext(path, true, false);
        final FileEntry entry = files.get(context);
        if(entry == null){ // add new entry if not already added and file exists
            final File file = getFile(path);
            return file != null && file.exists()
                ? loadingOption != ByteLoadingOption.LIVELOAD // only add to files if not liveload
                    ? files.put(context, new FileEntry(file, adapter, loadingOption))
                    : new FileEntry(file, adapter, loadingOption)
                : null;
        }else if(!entry.getFile().exists()){ // remove entry if file no longer exists
            files.remove(context);
            return null;
        }else{ // return existing if exists
            return entry;
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
        if(loadingOption != ByteLoadingOption.LIVELOAD ){ // find preloaded bytes
            final FileEntry entry = getFileEntry(path);
            return entry != null ? entry.getBytes() : null;
        }else{
            try{
                final File file = Objects.requireNonNull(getFile(path)); // check if file is allowed
                return file.isFile() ? adapter.getBytes(file, Files.readAllBytes(file.toPath())) : null; // adapt bytes here
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

    @Override
    public String toString(){
        return
            "DirectoryEntry"    + '{' +
            "directory"         + '=' +     directory       + ", " +
            "adapter"           + '=' +     adapter         + ", " +
            "loadingOption"     + '=' +     loadingOption   + ", " +
            "isWalkthrough"     + '=' +     isWalkthrough   + ", " +
            "files"             + '=' +     files  +
            '}';
    }

}
