package com.hao.keylogger.views;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public interface ILookAndFeel {
	
	static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
	}
}
