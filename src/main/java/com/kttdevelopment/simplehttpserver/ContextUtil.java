package com.kttdevelopment.simplehttpserver;

import java.util.regex.Pattern;

/**
 * A utility class used to generate uniform contexts. Applications do not use this class.
 *
 * @since 03.05.03
 * @version 4.4.0
 * @author Ktt Development
 */
public abstract class ContextUtil {

    // replace consecutive slashes and back slashes with a forward slash
    private static final Pattern forwardSlashRegex = Pattern.compile("/{2,}|\\\\+");
    // remove start and end slashes as well as whitespace
    private static final Pattern stripSlashRegex = Pattern.compile("^\\s*/*|/*\\s*$");

    /**
     * Generates a uniform context with forward slashes removing any consecutive slashes.
     *
     * @param context context
     * @param leadingSlash if context should have a leading slash
     * @param trailingSlash if context should have a trailing slash
     * @return context with uniform slashes
     *
     * @see #joinContexts(boolean, boolean, String...)
     * @since 03.05.03
     * @author Ktt Development
     */
    public static String getContext(final String context, final boolean leadingSlash, final boolean trailingSlash){
        final String linSlash = forwardSlashRegex.matcher(context).replaceAll("/");
        final String strippedSlash = stripSlashRegex.matcher(linSlash).replaceAll("");
        return strippedSlash.length() == 0
            ? leadingSlash || trailingSlash ? "/" : ""
            : (leadingSlash ? "/" : "") + strippedSlash + (trailingSlash ? "/" : "");
    }

    /**
     * Generates a uniform context given a set of strings using forward slashes and removing consecutive slashes.
     *
     * @param leadingSlash if context should have a leading slash
     * @param trailingSlash if context should have a trailing slash
     * @param contexts contexts to join
     * @return context with uniform slashes
     *
     * @see #getContext(String, boolean, boolean)
     * @since 03.05.03
     * @author Ktt Development
     */
    public static String joinContexts(final boolean leadingSlash, final boolean trailingSlash, final String... contexts){
        final StringBuilder OUT = new StringBuilder();

        for(final String context : contexts)
            OUT.append(getContext(context, true, false));

        return getContext(OUT.toString(), leadingSlash, trailingSlash);
    }

}
