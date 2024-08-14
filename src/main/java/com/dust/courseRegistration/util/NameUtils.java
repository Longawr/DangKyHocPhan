package com.dust.courseRegistration.util;

import java.util.Arrays;

import io.micrometer.common.util.StringUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NameUtils {

    /**
     * Split the last word into lastname and other into firstname.
     *
     * @param	fullname
     * 			String that be used to split
     *
     * @return	The array of string with 2 element, the first one is firstname and the other is lastname
     * 			firstName mean Family name in Asia and have middle name inside
     * 			and lastName mean personal name that have only one word
     */
    public static String[] splitFullName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return new String[] {"", ""};
        }

        String[] parts = fullName.strip().split("\\s+");

        if (parts.length == 1) {
            return new String[] {"", parts[0]};
        }

        String firstName = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 1));
        String lastName = parts[parts.length - 1];

        return new String[] {firstName, lastName};
    }

    /**
     * Remove all white space and combine first name and last name with a space into full name.
     *
     * @param	firstName
     *			Family name in Asia and have middle name inside
     *
     * @param	lastName
     * 			Personal name that have only one word
     *
     * @return	full name
     */
    public static String combineName(String firstName, String lastName) {
        String cleanedFirstName = StringUtils.isNotBlank(firstName) ? firstName.strip() : "";
        String cleanedLastName = StringUtils.isNotBlank(lastName) ? lastName.strip() : "";

        return String.join(" ", cleanedFirstName, cleanedLastName).strip();
    }
}
