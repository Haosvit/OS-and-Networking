/**
 * 
 */
package com.hao.keylogger.views;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.hao.keylogger.controllers.Client.ClientLogController;
import com.hao.keylogger.utils.Resources;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * @author Nguyen Phuc Hao
 *
 */
public class ClientView extends JFrame implements ActionListener, IClientView, ILookAndFeel {
	private static final long serialVersionUID = 1L;

	private static final String BTN_SAVE_ALL_LOGS = "btn_saveAllLogs";
	private static final String BTN_SAVE_LOG = "btn_saveLog";
	private static final String BTN_FETCH_LOG_NAME = "btn_fetchLog";
	private static final String BTN_FETCH_ALL_LOG_NAME = "btn_fetchAllLog";
	private static final String WINDOW_TITLE = "Key logger Client";
	private static final String BTN_SWITCH_VIEW = "btn_switchView";
	private static final String BTN_LOAD_SAVED_LOGS = "btn_loadAllLogs";

	ClientLogController controller;

	JTextField tf_host;
	JTextField tf_port;
	JTextArea ta_logView;
	JDatePickerImpl datePicker;
	ClientMenuBar menubar;
	JComboBox<String> list_logList;
	JLabel lb_logInfoContent;
	ArrayList<String> logListNames = new ArrayList<String>();

	public ClientView() {
		setLookAndFeel();
		InitFrame();
	}

	@Override
	public InetAddress getHostAddress() throws UnknownHostException {
		return InetAddress.getByName(tf_host.getText());
	}

	@Override
	public int getPort() {
		int port;
		try {
			port = Integer.parseInt(tf_port.getText());
		} catch (Exception ex) {
			port = 0;
		}
		return port;
	}

	@Override
	public String getLog() {
		return ta_logView.getText();
	}

	@Override
	public Date getDatePicked() {
		return (Date) datePicker.getModel().getValue();
	}

	@Override
	public void setHost(String host) {
		tf_host.setText(host);
	}

	@Override
	public void setPort(int port) {
		tf_port.setText(port + "");
	}

	@Override
	public void setLogContent(String logContent) {
		ta_logView.setText(logContent);
	}

	@Override
	public void setController(ClientLogController controller) {
		this.controller = controller;
	}

	private void InitFrame() {
		setSize(800, 600);
		setTitle(WINDOW_TITLE);
		// get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);
		setMinimumSize(new Dimension(800, 600));
		// menu
		menubar = new ClientMenuBar(this);

		// North panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

		JLabel lb_host = new JLabel("Host");
		tf_host = new JTextField();
		tf_host.setColumns(15);

		JLabel lb_port = new JLabel("Port");
		tf_port = new JTextField();
		tf_port.setColumns(4);

		// label choose date
		JLabel lb_chooseDate = new JLabel("Choose date");
		// Date picker
		UtilDateModel dateModel = new UtilDateModel();
		dateModel.setValue(new Date());
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
		datePicker = new JDatePickerImpl(datePanel);		
		
		// button fetch log
		JButton btn_fetchLog = new JButton("Fetch log");
		btn_fetchLog.setName(BTN_FETCH_LOG_NAME);
		btn_fetchLog.addActionListener(this);
		btn_fetchLog.setIcon(Resources.IC_FETCH);

		// button fetch all log
		JButton btn_fetchAllLog = new JButton("Fetch all logs");
		btn_fetchAllLog.setName(BTN_FETCH_ALL_LOG_NAME);
		btn_fetchAllLog.setIcon(Resources.IC_FETCH_ALL);
		btn_fetchAllLog.addActionListener(this);

		JButton btn_loadSavedLogs = new JButton("Load all save logs");
		btn_loadSavedLogs.setName(BTN_LOAD_SAVED_LOGS);
		btn_loadSavedLogs.setIcon(Resources.IC_LOAD_ALL);
		btn_loadSavedLogs.addActionListener(this);

		// label choose log to show
		JLabel lb_chooseLog = new JLabel("Choose log");

		// log list
		list_logList = new JComboBox<String>();
		list_logList.setPreferredSize(new Dimension(200, 25));
		list_logList.addActionListener(this);

		// button save log
		JButton btn_saveLog = new JButton("Save log");
		btn_saveLog.setName(BTN_SAVE_LOG);
		btn_saveLog.setIcon(Resources.IC_SAVE);
		btn_saveLog.addActionListener(this);

		// button save all logs
		JButton btn_saveAllLogs = new JButton("Save all logs");
		btn_saveAllLogs.setName(BTN_SAVE_ALL_LOGS);
		btn_saveAllLogs.setIcon(Resources.IC_SAVE_ALL);
		btn_saveAllLogs.addActionListener(this);

		// button convert log
		JButton btn_switchView = new JButton("Switch view");
		btn_switchView.setName(BTN_SWITCH_VIEW);
		btn_switchView.setIcon(Resources.IC_SWITCH_VIEW);
		btn_switchView.addActionListener(this);

		JPanel connPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		connPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		connPanel.add(lb_host);
		connPanel.add(tf_host);
		connPanel.add(lb_port);
		connPanel.add(tf_port);
		connPanel.add(Box.createRigidArea(new Dimension(2, 5)));
		connPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		connPanel.add(lb_chooseDate);
		connPanel.add(datePicker);
		connPanel.add(btn_fetchLog);
		connPanel.add(Box.createRigidArea(new Dimension(2, 5)));
		connPanel.add(btn_fetchAllLog);
		// logview toolbar
		JToolBar logView_toolbar = new JToolBar();
		logView_toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		logView_toolbar.add(btn_loadSavedLogs);
		logView_toolbar.add(lb_chooseLog);
		logView_toolbar.add(list_logList);
		logView_toolbar.add(btn_saveLog);
		logView_toolbar.add(btn_saveAllLogs);
		logView_toolbar.add(btn_switchView);
		
		JPanel logViewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		logViewPanel.add(btn_loadSavedLogs);
		logViewPanel.add(lb_chooseLog);
		logViewPanel.add(list_logList);
		logViewPanel.add(btn_saveLog);
		logViewPanel.add(btn_saveAllLogs);
		logViewPanel.add(btn_switchView);
		

		northPanel.add(connPanel);
		northPanel.add(logViewPanel);

		ta_logView = new JTextArea(5, 10);
		ta_logView.setLineWrap(true);
		ta_logView.setWrapStyleWord(true);
		ta_logView.setEditable(false);
		ta_logView.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(ta_logView);

		setJMenuBar(menubar);
		
		JLabel lb_logInfo = new JLabel("Log information: ");
		lb_logInfo.setIcon(Resources.IC_INFO);
		lb_logInfoContent = new JLabel("");
		
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoPanel.add(lb_logInfo);
		infoPanel.add(lb_logInfoContent);

		// adding panels to main container
		getContentPane().add(northPanel, "North");
		getContentPane().add(scrollPane);
		getContentPane().add(infoPanel, "South");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getSource().getClass().equals(Class.forName("javax.swing.JButton"))) {
				// button clicked
				JButton btn = (JButton) event.getSource();
				switch (btn.getName()) {
				case BTN_FETCH_LOG_NAME:
					ta_logView.setText("");
					controller.fetchLog(getDatePicked());
					break;
				case BTN_FETCH_ALL_LOG_NAME:
					controller.fetchAllLogs();
					break;
				case BTN_SAVE_LOG:
					controller.saveLog(getLogListSelectedIndex());
					break;
				case BTN_SAVE_ALL_LOGS:
					controller.saveAllLogs();
					break;
				case BTN_SWITCH_VIEW:
					controller.switchView();
					break;
				case BTN_LOAD_SAVED_LOGS:
					controller.loadAllSavedLogs();
					break;
				default:
					break;
				}
			} else if (event.getSource().getClass().equals(Class.forName("javax.swing.JComboBox"))) {
				// combobox clicked
				JComboBox<String> cb = (JComboBox<String>) event.getSource();
				int index;
				if ((index = cb.getSelectedIndex()) >= 0) {
					controller.displayLog(index);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void showErrorMessage(String caption, String msg) {
		JOptionPane.showMessageDialog(null, msg, caption, JOptionPane.ERROR_MESSAGE);
	}

	public void showInfoMessage(String caption, String msg) {
		JOptionPane.showMessageDialog(null, msg, caption, JOptionPane.INFORMATION_MESSAGE);
	}

	public void setLogList(ArrayList<String> logList) {
		this.logListNames = logList;
		list_logList.removeAllItems();
		if (logList.size() > 0) {
			for (String logName : logListNames) {
				list_logList.addItem(logName);
			}
			list_logList.setSelectedIndex(0);
		}
	}

	public int getLogListSelectedIndex() {
		return list_logList.getSelectedIndex();
	}

	public void scrollLogViewToTop() {
		ta_logView.setCaretPosition(0);

	}

	public void onMenuItemClick(String itemName) {
		switch (itemName) {
		case ClientMenuBar.MI_SAVE_LOG:
			controller.saveLog(getLogListSelectedIndex());
			break;
		case ClientMenuBar.MI_SAVE_ALL_LOGS:
			controller.saveAllLogs();
			break;
		case ClientMenuBar.MI_LOAD_ALL_LOGS:
			controller.loadAllSavedLogs();
			break;
		case ClientMenuBar.MI_TOGGLE_LOGGER:
			controller.toggleLoggerRemote();
			break;
		case ClientMenuBar.MI_DELETE_ALL_HOST_LOGS:
			int choosen = JOptionPane.showConfirmDialog(null, "Do you really want to delete all host logs",
					"Key logger - Delete all host logs", JOptionPane.YES_NO_OPTION);
			if (choosen == JOptionPane.YES_OPTION) {
				controller.deleteAllHostLogs();
			}
			break;
		case ClientMenuBar.MI_SHOW_SERVER_FORM:
			controller.showServerForm();
			break;
		case ClientMenuBar.MI_DEL_SAVED_LOGS: 
			int c = JOptionPane.showConfirmDialog(null, "Do you really want to delete all saved logs",
					"Key logger - Delete all saved logs", JOptionPane.YES_NO_OPTION);
			if (c == JOptionPane.YES_OPTION) {
				if (controller.deleteSavedLogs()) {
					showInfoMessage("Delete saved logs", "Deleted all saved logs");
				}
				else {
					showErrorMessage("Delete saved logs", "Can not delete saved logs");
				}
			}
			break;
		case ClientMenuBar.MI_MANAGE_LOGS:
			if (!controller.manageLogs()) {
				showErrorMessage("Manage logs", "Log directory not found!");
			}
			break;
		case ClientMenuBar.MI_EXIT:
			Runtime.getRuntime().exit(0);
			break;
		}
	}

	public void updateMenuItemWhenLoggerStop() {
		menubar.updateMenuItemWhenLoggerStop();
	}

	public void updateMenuItemWhenLoggerStart() {
		menubar.updateMenuItemWhenLoggerStart();
	}

	public void updateLogInfo(String info) {
		lb_logInfoContent.setText(info);
	}

	@Override
	public void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
	}
}
