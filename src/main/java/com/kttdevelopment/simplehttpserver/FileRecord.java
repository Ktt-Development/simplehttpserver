package com.kttdevelopment.simplehttpserver;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileRecord extends Record {

    private final String fileName, contentType;
    private final byte[] bytes;

    @SuppressWarnings("rawtypes")
    FileRecord(final Map.Entry<String,Map> entry){
        super(entry);

        Header header = Objects.requireNonNull(getHeader("Content-Disposition"));
        fileName = Objects.requireNonNull(header.getParameter("filename"));
        header = Objects.requireNonNull(getHeader("Content-Type"));
        contentType = Objects.requireNonNull(header.getHeaderValue());
        bytes = getValue().getBytes(StandardCharsets.UTF_8);
    }

    public final String getFileName(){
        return fileName;
    }

    public final String getContentType(){
        return contentType;
    }

    public final byte[] getBytes(){
        return bytes;
    }

    @Override
    public String toString(){
        return
            "FileRecord"    + '{' +
            "name"          + '=' + '\'' + getName() + '\''                     + ", " +
            "fileName"      + '=' + '\'' + fileName + '\''                      + ", " +
            "contentType"   + '=' + '\'' + contentType + '\''                   + ", " +
            "value"         + '=' + '\'' + Arrays.toString(bytes) + '\''     + ", " +
            "headers"       + '=' + getHeaders() +
            '}';
    }

}
