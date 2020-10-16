package com.kttdevelopment.simplehttpserver;

import java.util.Collections;
import java.util.Map;

public class MultipartFormData {

    private final Map<String,Record> records;

    MultipartFormData(final Map<String,Record> records){
        this.records = Collections.unmodifiableMap(records);
    }

    public final Record get(final String key){
        return records.get(key);
    }

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
