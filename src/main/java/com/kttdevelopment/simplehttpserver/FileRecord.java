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

package com.kttdevelopment.simplehttpserver;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class represents on set of headers and parameters in a multipart/form-data that is expected of a file input.
 *
 * @see MultipartFormData
 * @see Record
 * @see com.kttdevelopment.simplehttpserver.Record.Header
 * @since 4.0.0
 * @version 4.0.0
 * @author Ktt Development
 */
public class FileRecord extends Record {

    private final String fileName, contentType;
    private final byte[] bytes;

    /**
     * Creates a record from a map entry. Throws a {@link NullPointerException} if the entry doesn't have all the required fields for a {@link Record#Record(Map.Entry)}; and a filename and Content-Type field.
     *
     * @param entry map entry
     *
     * @since 4.0.0
     * @author Ktt Development
     */
    @SuppressWarnings("rawtypes")
    FileRecord(final Map.Entry<String,Map> entry){
        super(entry);

        Header header = Objects.requireNonNull(getHeader("Content-Disposition"));
        fileName = Objects.requireNonNull(header.getParameter("filename"));
        header = Objects.requireNonNull(getHeader("Content-Type"));
        contentType = Objects.requireNonNull(header.getHeaderValue());
        bytes = getValue().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Returns the file name.
     *
     * @return file name
     *
     * @since 4.0.0
     * @author Ktt Development
     */
    public final String getFileName(){
        return fileName;
    }

    /**
     * Returns the content type of the file.
     *
     * @return content type
     *
     * @since 4.0.0
     * @author Ktt Development
     */
    public final String getContentType(){
        return contentType;
    }

    /**
     * Returns the file as bytes.
     *
     * @return file in bytes
     *
     * @see #getValue()
     * @since 4.0.0
     * @author Ktt Development
     */
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
