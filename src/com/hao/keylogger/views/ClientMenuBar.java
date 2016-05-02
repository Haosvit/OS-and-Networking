package com.hao.keylogger.views;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class ClientMenuBar extends JMenuBar implements ActionListener {
	public static final String MI_DELETE_ALL_HOST_LOGS = "mi_deleteAllHostLogs";
	public static final String MI_STOP_LOGGER = "mi_stopLogger";
	public static final String MI_EXIT = "mi_exit";
	public static final String MI_LOAD_ALL_LOGS = "mi_loadAllLogs";
	public static final String MI_SAVE_ALL_LOGS = "mi_saveAllLogs";
	public static final String MI_SAVE_LOG = "mi_saveLog";
	ClientView parent;
	public ClientMenuBar(ClientView parent) {
		this.parent = parent;
		initFrame();
	}

	private void initFrame() {
		JMenu menu_file = new JMenu("File");
		
		JMenuItem mi_saveLog = new JMenuItem("Save log");
		mi_saveLog.setName(MI_SAVE_LOG);
		mi_saveLog.addActionListener(this);
		
		
		JMenuItem mi_saveAllLogs = new JMenuItem("Save all logs");
		mi_saveAllLogs.setName(MI_SAVE_ALL_LOGS);
		mi_saveAllLogs.addActionListener(this);
		
		JMenuItem mi_loadAllLogs = new JMenuItem("Load all saved logs");
		mi_loadAllLogs.setName(MI_LOAD_ALL_LOGS);
		mi_loadAllLogs.addActionListener(this);
		
		JMenuItem mi_exit = new JMenuItem("Exit");
		mi_exit.setName(MI_EXIT);
		mi_exit.addActionListener(this);
		
		menu_file.add(mi_saveLog);
		menu_file.add(mi_saveAllLogs);
		menu_file.add(mi_loadAllLogs);
		menu_file.addSeparator();
		menu_file.add(mi_exit);

		JMenu menu_Remote = new JMenu("Remote");
		
		JMenuItem mi_stopLogger = new JMenuItem("Stop key logger");
		mi_stopLogger.setName(MI_STOP_LOGGER);
		mi_stopLogger.addActionListener(this);
		
		JMenuItem mi_deleteAllHostLogs = new JMenuItem("Delete all host logs");
		mi_deleteAllHostLogs.setName(MI_DELETE_ALL_HOST_LOGS);
		mi_deleteAllHostLogs.addActionListener(this);
		
		menu_Remote.add(mi_stopLogger);
		menu_Remote.add(mi_deleteAllHostLogs);
		
		this.add(menu_file);
		this.add(menu_Remote);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem) e.getSource();
		parent.onMenuItemClick(item.getName());
	}

}
