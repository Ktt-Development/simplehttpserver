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

/**
 * A list of expected HTTP method requests.
 * See <a href="https://tools.ietf.org/html/rfc7231#section-4.3" target="_blank">IANA RFC7324 Section 4.3</a>
 * @since 01.00.00
 * @version 4.3.0
 * @author Ktt Development
 */
@SuppressWarnings("unused")
public abstract class HttpRequestMethod {

    public static final String GET          = "GET";
    public static final String HEAD         = "HEAD";
    public static final String POST         = "POST";
    public static final String PUT          = "PUT";
    public static final String DELETE       = "DELETE";
    public static final String CONNECT      = "CONNECT";
    public static final String OPTIONS      = "OPTIONS";
    public static final String TRACE        = "TRACE";
    public static final String PATCH        = "PATCH";
    public static final String UNSUPPORTED  = "UNSUPPORTED";

}
