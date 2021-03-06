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

import java.io.File;

/**
 * This interface determines the file bytes to use when adding to {@link FileHandler}.
 *
 * @see FileHandlerAdapter
 * @see FileHandler
 * @since 01.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
interface FileBytesAdapter {

    /**
     * Returns the bytes when given a file and its initial contents
     *
     * @param file file to name
     * @param bytes bytes from preload
     * @return new bytes
     *
     * @since 01.00.00
     * @author Ktt Development
     */
    default byte[] getBytes(final File file, final byte[] bytes){
        return bytes;
    }

}
