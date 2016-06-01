package com.hao.keylogger.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LogConverter {

	String[] specialKeyNames = new String[] { "[Tab]", "[CAP_ON]", "[CAP_OFF]", "[Left Shift]", "[Space]",
			"[Right Shift]", "[Enter]", "[Backspace]", "[Num Enter]", "[Num 0]", "[Num 1]", "[Num 2]", "[Num 3]",
			"[Num 4]", "[Num 5]", "[Num 6]", "[Num 7]", "[Num 8]", "[Num 9]" };
	Set<String> specialKeysSet = new HashSet<String>();

	Map<String, Character> dictionary = new HashMap<String, Character>();

	// private boolean isCapOn = false;
	// private boolean isShiftDown = false;

	public LogConverter() {
		initSpecialKeySet();
		initDictionary();
	}

	private void initSpecialKeySet() {
		for (String s : specialKeyNames) {
			specialKeysSet.add(s);
		}
	}

	private void initDictionary() {
		dictionary.put("[Tab]", '\t');
		dictionary.put("[Space]", ' ');
		dictionary.put("[Enter]", '\n');
		dictionary.put("[Num Enter]", '\n');
		for (int i = 0; i <= 9; i++) {
			dictionary.put("[Num " + i + "]", String.valueOf(i).charAt(0));
		}
	}

	public String convert(String log) {
		boolean isCapOn = false;
		boolean isShift = false;
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < log.length(); i++) {
			char c = log.charAt(i);
			String specialKey = "";

			// if next token is special key
			if (c == '[') {
				if (log.charAt(i + 1) == ' ') {
					sb.append(c);
				} else {
					specialKey += c;
					while (true) {
						i++;
						char sc = log.charAt(i);
						specialKey += sc;
						if (sc == ']') {
							break;
						}
					}
					if (specialKeysSet.contains(specialKey)) {
						if (dictionary.containsKey(specialKey)) {
							sb.append(dictionary.get(specialKey));
						} else {
							switch (specialKey) {
							case "[CAP_ON]":
								isCapOn = true;
								break;
							case "[CAP_OFF]":
								isCapOn = false;
								break;
							case "[Left Shift]":
							case "[Right Shift]":
								isShift = true;
								break;
							case "[Backspace]":
								if (sb.length() > 0) {
									sb.setLength(sb.length() - 1);
								}
								break;
							}
						}
					}
				}
			} else {
				if (c != ' ') {
					c = Character.toLowerCase(c);
					if (isCapOn) {
						c = Character.toUpperCase(c);
					}
					if (isShift) {
						if (Character.isDigit(c)) {

							switch (c) {
							case '1':
								c = '!';
								break;
							case '2':
								c = '@';
								break;
							case '3':
								c = '#';
								break;
							case '4':
								c = '$';
								break;
							case '5':
								c = '%';
								break;
							case '6':
								c = '^';
								break;
							case '7':
								c = '&';
								break;
							case '8':
								c = '*';
								break;
							case '9':
								c = '(';
								break;
							case '0':
								c = ')';
								break;
							}
						} else if (c == '-') {
							c = '_';
						} else if (c == '=') {
							c = '+';
						}
						
						if (Character.isUpperCase(c)) {
							c = Character.toLowerCase(c);
						} else {
							c = Character.toUpperCase(c);
						}
						isShift = false;
					}
					sb.append(c);
				}
			}

		}

		return sb.toString();
	}
}
