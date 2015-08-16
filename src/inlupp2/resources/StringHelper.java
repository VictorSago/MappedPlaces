
package inlupp2.resources;

import java.awt.FontMetrics;
import java.util.*;

/**
 * <code> public class StringHelper<code><br><br>
 * A helper classes for splitting long strings into short lines.
 * Adapted from Jim Menard's code for StringUtils class
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class StringHelper {
    
    /**
     * Returns an ArrayList of strings, one for each line in the string after it has
     * been wrapped to fit lines of <var>maxWidth</var>.<br><br>
     * This code assumes <var>str</var> is not <code>null</code>.
     * @param str The string to split
     * @param fm FontMetrics for calculations of string width
     * @param maxWidth The max line width in points
     * @return a non-empty list of strings
     */    
    public static List<String> wrap(String str, FontMetrics fm, int maxWidth) {
	List<String> lines = splitIntoLines(str);
	if (lines.size() == 0)
	    return lines;

	ArrayList<String> strings = new ArrayList<String>();
	Iterator<String> iter = lines.iterator();
	while (iter.hasNext())
	    wrapLineInto((String) iter.next(), strings, fm, maxWidth);
	return strings;
    }
    
    /**
     * Returns an ArrayList of strings, one for each line in the string.<br><br>
     * This code assumes <var>str</var> is not <code>null</code>.
     * @param str The string to split
     * @return A non-empty list of strings
     */
    public static List<String> splitIntoLines(String str) {
	ArrayList<String> strings = new ArrayList<String>();

	int len = str.length();
	if (len == 0) {
	    strings.add("");
	    return strings;
	}

	int lineStart = 0;

	for (int i = 0; i < len; ++i) {
	    char c = str.charAt(i);
	    if (c == '\r') {
		int newlineLength = 1;
		if ((i + 1) < len && str.charAt(i + 1) == '\n')
		    newlineLength = 2;
		strings.add(str.substring(lineStart, i));
		lineStart = i + newlineLength;
		if (newlineLength == 2) // skip \n next time through loop
		    ++i;
	    } else if (c == '\n') {
		strings.add(str.substring(lineStart, i));
		lineStart = i + 1;
	    }
	}
	if (lineStart < len)
	    strings.add(str.substring(lineStart));

	return strings;
    }
    
    /**
     * Wraps the line of text and add the new line(s) to <var>list</var>.
     * @param line A line of text to be wrapped
     * @param list The list of the wrapped lines
     * @param fm FontMetrics to measure the width of the strings
     * @param maxWidth Maximum width of the line(s)
     */
    public static void wrapLineInto(String line, List<String> list, FontMetrics fm, int maxWidth) {
	int len = line.length();
	int width;
	while (len > 0 && (width = fm.stringWidth(line)) > maxWidth) {
	    // Guess where to split the line.
	    int guess = len * maxWidth / width;
	    // Try to split it at a space character between words.
	    String before = line.substring(0, guess).trim();

	    width = fm.stringWidth(before);
	    int pos;
	    if (width > maxWidth) 			// The line is too long
		pos = findBreakBefore(line, guess);
	    else { 					// The line is either too short or the required length
		pos = findBreakAfter(line, guess);
		if (pos != -1) { 			// Make sure this doesn't make it too long
		    before = line.substring(0, pos).trim();
		    if (fm.stringWidth(before) > maxWidth)
			pos = findBreakBefore(line, guess);
		}
	    }
	    if (pos == -1)
		pos = guess; 				// Split in the middle of the word

	    list.add(line.substring(0, pos).trim());
	    line = line.substring(pos).trim();
	    len = line.length();
	}
	if (len > 0)
	    list.add(line);
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or before <var>start</var>. Returns -1 if no such character is found. 
     * @param line A string of text
     * @param start Where to begin testing
     */
    public static int findBreakBefore(String line, int start) {
	for (int i = start; i >= 0; --i) {
	    char c = line.charAt(i);
	    if (Character.isWhitespace(c) || c == '-')
		return i;
	}
	return -1;
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or after <var>start</var>. Returns -1 if no such character is found.
     * @param line A string of text
     * @param start Where to begin testing
     */
    public static int findBreakAfter(String line, int start) {
	int len = line.length();
	for (int i = start; i < len; ++i) {
	    char c = line.charAt(i);
	    if (Character.isWhitespace(c) || c == '-')
		return i;
	}
	return -1;
    }

}
