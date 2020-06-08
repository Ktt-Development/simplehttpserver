package com.kttdevelopment.simplehttpserver.handler;

/**
 * Determines how files will be loaded in the {@link FileHandler}. <br>
 *
 * <code>PRELOAD</code> - read file when it is added to the handler <br>
 * <code>WATCHLOAD</code> - read file when it is added and anytime it is updated <br>
 * <code>LIVELOAD</code> - read file each time an exchange happens
 *
 * @see FileHandler
 * @since 03.05.00
 * @version 03.05.01
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ByteLoadingOption {

    PRELOAD,
    WATCHLOAD,
    LIVELOAD

}
