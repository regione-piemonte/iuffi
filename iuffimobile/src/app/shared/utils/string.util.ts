export class StringUtil {

    /**
     * Removes all non numeric characters from string.
     * @param str string
     */
    public static removeNonNumerics(str: string): string {
        return str.replace(/^\d+$/g, '');
    };

}
