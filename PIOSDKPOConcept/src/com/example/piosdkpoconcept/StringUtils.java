package com.example.piosdkpoconcept;


public class StringUtils {

	/**
	 * The empty String "".
	 */
	public static final String EMPTY = "";

	/**
	 * Checks if a String is empty ("") or null.
	 * 
	 * @param str
	 *            the String to check, may be null.
	 * @return {@code true} if the String is empty or null.
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * Checks if a String is not empty ("") and not null.
	 * 
	 * @param str
	 *            the String to check, may be null.
	 * @return {@code true} if the String is not empty and not null.
	 */
	public static boolean isNotEmpty(String str) {
		return !StringUtils.isEmpty(str);
	}


	/**
	 * Checks if a String is whitespace, empty ("") or null.
	 * 
	 * 
	 * @param str
	 *            the String to check, may be null.
	 * @return {@code true} if the String is null, empty or whitespace.
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * 
	 * @param str
	 *            the String to check, may be null.
	 * @return {@code true} if the String is not empty and not null and not
	 *         whitespace.
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}


	/**
	 * Converts supplied string to upper case.
	 * 
	 * @param str
	 *            The input string. May be {@code null}.
	 * @return a upper case string of {@code str}, or {@code null} if
	 *         supplied string was {@code null}.
	 */
	public static String toUpperCase(String str) {
		return str != null ? str.toUpperCase() : null;
	}

	/**
	 * Converts supplied string to lower case.
	 * 
	 * @param str
	 *            The input string. May be {@code null}.
	 * @return a lower case string of {@code str}, or {@code null} if
	 *         supplied string was {@code null}.
	 */
	public static String toLowerCase(String str) {
		return str != null ? str.toLowerCase() : null;
	}
}
