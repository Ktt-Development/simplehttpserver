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

import java.util.Collections;
import java.util.Map;

/**
 * This class represents a POST request map as a multipart/form-data.
 *
 * @see SimpleHttpExchange
 * @see Record
 * @see FileRecord
 * @since 4.0.0
 * @version 4.0.0
 * @author Ktt Development
 */
public class MultipartFormData {

    private final Map<String,Record> records;

    /**
     * Creates a multipart/form-data.
     *
     * @param records map of records and record keys
     *
     * @see Record
     * @see FileRecord
     * @since 4.0.0
     * @author Ktt Development
     */
    MultipartFormData(final Map<String,Record> records){
        this.records = Collections.unmodifiableMap(records);
    }

    /**
     * Returns the record for key or null if none is found. If the record is supposed to be a FileRecord then cast it to {@link FileRecord}.
     *
     * @param key record key
     * @return {@link Record} or {@link FileRecord} or null if none is found.
     *
     * @see Record
     * @see FileRecord
     * @since 4.0.0
     * @author Ktt Development
     */
    public final Record getRecord(final String key){
        return records.get(key);
    }

    /**
     * Returns all the records in the multipart/form-data;
     *
     * @return map of all records
     *
     * @see Record
     * @see FileRecord
     * @since 4.0.0
     * @author Ktt Development
     */
    public final Map<String,Record> getRecords(){
        return records;
    }

    @Override
    public String toString(){
        return
            "MultipartFormData" + '{' +
            "record"            + '=' + records +
            '}';
    }

}
