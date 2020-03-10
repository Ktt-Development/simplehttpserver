package ktt.lib.httpserver.handler;

/**
 * Private methods to help resolve variable slashes. Applications do not normally use this class.
 * @since 01.01.01
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings({"unused","WeakerAccess"})
@Deprecated
class __ {

    /**
     * Returns a string with forward slashes only.
     * @param s string to resolve
     * @return linear string
     * @since 01.01.01
     * @see #startAndEndSlash(String)
     * @see #startSlash(String)
     * @see #endSlash(String)
     * @see #noSlash(String)
     */
    static String fwdSlash(String s){
        return s.replace("\\","/");
    }

    /**
     * Returns a string with a single start and end slash.
     * @param s string to resolve
     * @return linear string
     * @since 01.01.01
     * @see #fwdSlash(String)
     * @see #startSlash(String)
     * @see #endSlash(String)
     * @see #noSlash(String)
     */
    static String startAndEndSlash(String s){
        return s.replace("\\","/")
                .replaceAll("^/+|/+$","") // remove start and ending
                .replaceAll("^(?!/)|$(?<!/)","/"); // add starting and ending
    }

    /**
     * Returns a string with a single start and end slash.
     * @param s string to resolve
     * @return linear string
     * @since 01.01.01
     * @see #fwdSlash(String)
     * @see #startAndEndSlash(String)
     * @see #endSlash(String)
     * @see #noSlash(String)
     */
    static String startSlash(String s){
        return s.replace("\\","/")
            .replaceAll("^/+|/+$","") // remove start and ending
            .replaceAll("^(?!/)","/"); // add starting
    }

    /**
     * Returns a string with a single start and end slash.
     * @param s string to resolve
     * @return linear string
     * @since 01.01.01
     * @see #fwdSlash(String)
     * @see #startAndEndSlash(String)
     * @see #startSlash(String)
     * @see #noSlash(String)
     */
    static String endSlash(String s){
        return s.replace("\\","/")
                .replaceAll("^/+|/+$","") // remove start and ending
                .replaceAll("$(?<!/)","/"); // add ending
    }

    /**
     * Returns a string with no start or end slash.
     * @param s string to resolve
     * @return linear string
     * @since 01.01.01
     * @see #fwdSlash(String)
     * @see #startAndEndSlash(String)
     * @see #startSlash(String)
     * @see #endSlash(String)
     */
    static String noSlash(String s){
        return s.replace("\\","/")
                .replaceAll("^/+|/+$",""); // remove start and ending
    }

}
