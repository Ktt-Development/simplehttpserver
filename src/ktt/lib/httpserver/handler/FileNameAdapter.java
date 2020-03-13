package ktt.lib.httpserver.handler;

import java.io.File;

interface FileNameAdapter {

    default String getName(final File file){
            return file.getName();
        }

}
