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
				}
				else {
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

	public static void main(String[] args) {
		String log = "R O I [Space] N G U O I [Space] T A [Space] D E N [CAP_ON] [Space] T H O E [Backspace] [Backspace] E O [Space] H O [Space] H A N G [Space] D O I [Space] B N [Backspace] E N [Enter] M O T [Space] [CAP_OFF] N G A Y [Space] N E N [Space] [Left Shift] D U Y E N [Space] M O T [Space] [Left Shift] B U O C [Space] E M [Space] [Left Shift] E N [Backspace] [Backspace] [Left Shift] N E N [Space] V O [Space] [Left Shift] H I E N [Enter] [Left Control] [Left Control] A C [Left Shift] [Left Shift] [Left Shift] [End] [Left Shift] [Left] [Left] [Left Control] V [Left Control] [F11] [Left Windows] R [Enter] C A I [Space] B E E P [Space] 1 2 3 4 ` [F1] [F2] [Space] [Right Control] [Right Control] A [Right] [Num 0] [Num 1] [Num 0] [Num 2] [Num Del] [Num 2] 2 0 [Space] 3 0 [Space] [ [Left Shift] L [Backspace] [Left Shift] B A C K S P A C E ] [Space] [ B A C [Backspace] [Backspace] [Backspace] [Backspace] [ E N T E R N [Backspace] [Backspace] [Backspace] [Backspace] [Backspace] [Backspace] [Backspace] [ [Left Shift] E N T E R ] [Space] [Left Shift] [ [Left Shift] E N T E R ] ";
		LogConverter lc = new LogConverter();
		System.out.println(lc.convert(log));
	}
}
