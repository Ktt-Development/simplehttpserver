package com.kttdevelopment.simplehttpserver.handlers.file;

import com.kttdevelopment.simplehttpserver.handler.*;
import org.junit.jupiter.api.*;

import java.io.File;

public class FileHandlerCacheTest {

    @Test
    public final void testIllegalArg(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileHandler(new FileHandlerAdapter() {}).addFile(new File("null"), ByteLoadingOption.CACHELOAD));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileHandler(new CacheFileAdapter(0)).addFile(new File("null")));
        Assertions.assertDoesNotThrow(() -> new FileHandler(new CacheFileAdapter(0)).addFile(new File("null"), ByteLoadingOption.CACHELOAD));
    }

    @Test @Disabled
    public final void testCacheLoad(){
        // test read
    }

}
