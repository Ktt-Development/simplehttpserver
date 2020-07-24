package com.kttdevelopment.simplehttpserver;

/**
 * A utility class used to generate uniform contexts. Applications do not use this class.
 *
 * @since 03.05.03
 * @version 03.05.03
 * @author Ktt Development
 */
public abstract class ContextUtil {


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
        final String linSlash = context.replace('\\','/').replaceAll("/{2,}","/");
        if(linSlash.isBlank() || linSlash.equals("/")) // handle blank or '/' contexts
            return leadingSlash || trailingSlash ? "/" : "";
        final String ltSlash = (!(linSlash.charAt(0) == '/') ? '/' : "") + linSlash + (!(linSlash.charAt(linSlash.length()-1) == '/') ? '/' : "");
        return ltSlash.substring(leadingSlash ? 0 : 1,ltSlash.length() + (trailingSlash ? 0 : -1));
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

        return getContext(OUT.toString(),leadingSlash,trailingSlash);
    }

}
