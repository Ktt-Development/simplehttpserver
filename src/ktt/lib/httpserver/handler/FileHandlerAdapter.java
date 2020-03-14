package ktt.lib.httpserver.handler;

public interface FileHandlerAdapter extends FileNameAdapter, FileBytesAdapter {

    // handles preloading only; postloading handled in httphandlers

}
