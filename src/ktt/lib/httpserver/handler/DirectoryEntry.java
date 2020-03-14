package ktt.lib.httpserver.handler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;

class DirectoryEntry {

    private final HashMap<String,FileEntry> files = new HashMap<>();

    private final File directory;

    private final FileHandlerAdapter adapter;

    private final boolean isFilesPreloaded;
    private final boolean isWalkthrough;

    DirectoryEntry(final File directory, final boolean isFilesPreloaded, final FileHandlerAdapter adapter, final boolean isWalkthrough) throws IOException{
        if(!directory.exists() || !directory.isDirectory())
            throw new FileNotFoundException("Directory at " + directory.getAbsolutePath() + " was not found");

        this.directory = directory;
        this.isFilesPreloaded = isFilesPreloaded;
        this.isWalkthrough = isWalkthrough;
        this.adapter = adapter;

        if(isFilesPreloaded){
            if(!isWalkthrough){
                for(final File file : Objects.requireNonNull(directory.listFiles())){
                    files.put(
                        getContext(adapter.getName(file)),
                        new FileEntry(file, true, adapter)
                    );
                }
            }else{
                final Path dirPath = directory.toPath();

                Files.walk(dirPath).filter(path -> path.toFile().isDirectory()).forEach(path -> {
                    final File pathFile = path.toFile();

                    final String rel = dirPath.relativize(path).toString();
                    final File[] files = pathFile.listFiles();

                    if(files == null) return;

                    for(final File file : files){
                        try{
                            DirectoryEntry.this.files.put(
                                getContext(rel + "/" + adapter.getName(file)),
                                new FileEntry(file, true, adapter)
                            );
                        }catch(FileNotFoundException e){
                            // failed
                        }
                    }
                });
            }
        }
    }

    public final File getFile(final String path){
        final String rel = getContext(path);
        if(isFilesPreloaded){
            String match = "";
            for(final String key : files.keySet())
                if(rel.startsWith(key) && key.startsWith(match))
                    match = key;
            if(!match.isEmpty()){
                return files.get(match).getFile();
            }else{
                return null;
            }
        }else{
            if(isWalkthrough){
                final File parent = new File(directory.getAbsolutePath() + path).getParentFile();
                final File target = new File(parent.getAbsolutePath() + path.substring(0,path.lastIndexOf('/')));
                return target.exists() ? target : null;
            }else{
                final File[] files = directory.listFiles(pathname -> !pathname.isDirectory());

                if(files == null) return null;

                for(final File file : files)
                    if(adapter.getName(file).equalsIgnoreCase(path))
                        return file;
                return null;
            }
        }
    }

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

    public HashMap<String, FileEntry> getFiles(){
        return files;
    }

    public final File getDirectory(){
        return directory;
    }

    public final boolean isFilesPreloaded(){
        return isFilesPreloaded;
    }

    public final boolean isWalkthrough(){
        return isWalkthrough;
    }

//

    private static String getContext(final String path){
        final String linSlash = path.toLowerCase().replace("\\","/");
        final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
        return seSlash.substring(0,seSlash.length()-1);
    }

}
