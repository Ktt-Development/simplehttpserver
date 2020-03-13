package ktt.lib.httpserver.handler;

import java.io.File;

interface FileBytesAdapter {

    default byte[] getBytes(final File file, final byte[] bytes){
        return bytes;
    }

}
