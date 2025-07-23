package com.businessextractor.util;

import java.util.*;

/**
 * Provides utilitary methods for working with strings
 * @author Bogdan Vlad
 *
 */
public class StringUtils {

	// join character for the join() operation
	private static String JOIN_CHARACTER = ";";
	
	/**
	 * Joins a list of strings into a single string
	 * @param stringsList The list of strings
	 * @return The resulting string
	 */
	public static String join (List<String> stringsList) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i=0;i<stringsList.size();i++) {
			if (i!=stringsList.size()-1) {
				stringBuilder.append(stringsList.get(i)+JOIN_CHARACTER);
			} else {
				stringBuilder.append(stringsList.get(i));
			}
		}
		return stringBuilder.toString();
	}
	
}
