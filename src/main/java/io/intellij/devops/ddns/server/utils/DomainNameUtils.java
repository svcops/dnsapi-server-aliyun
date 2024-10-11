package io.intellij.devops.ddns.server.utils;

/**
 * DomainNameUtils
 *
 * @author tech@intellij.io
 */
public class DomainNameUtils {

    private DomainNameUtils() {
    }

    /**
     * Validates that the given resource record (RR) string conforms to the expected pattern.
     * The valid pattern includes alphanumeric characters and has a length between 1 and 32.
     *
     * @param rr the resource record string to be validated
     * @return true if the resource record is valid, false otherwise
     */
    public static boolean validateRR(String rr) {
        String regex = "^[a-zA-Z0-9]{1,32}$";
        return rr != null && rr.matches(regex);
    }

}
