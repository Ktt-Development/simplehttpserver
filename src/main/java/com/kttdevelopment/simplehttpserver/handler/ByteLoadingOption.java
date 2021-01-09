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

package com.kttdevelopment.simplehttpserver.handler;

/**
 * Determines how files will be loaded in the {@link FileHandler}. <br>
 *
 * <code>PRELOAD</code> - read file when it is added to the handler <br>
 * <code>MODLOAD</code> - read file when it is added and anytime it is updated <br>
 * <code>CACHELOAD</code> - load file when requested and clear from memory when maximum time expires. Requires a {@link CacheFileAdapter}. <br>
 * <code>LIVELOAD</code> - read file each time an exchange happens
 *
 * @see FileHandler
 * @since 03.05.00
 * @version 4.0.0
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ByteLoadingOption {

    PRELOAD,
    MODLOAD,
    CACHELOAD,
    LIVELOAD

}
