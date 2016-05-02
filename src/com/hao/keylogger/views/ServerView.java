package com.hao.keylogger.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hao.keylogger.controllers.server.IServerView;
import com.hao.keylogger.controllers.server.ServerLogController;

public class ServerView extends JFrame implements ActionListener, IServerView {
	private static final String WINDOW_TITLE = "Keylogger Server";

	private final String BTN_START_SERVER_NAME = "btn_startServer";
	
	ServerLogController controller;
	
	JTextField tf_host;
	JTextField tf_port;
	JTextArea ta_monitor;

	boolean isServerStarted = false;
	
	public ServerView() {
		initFrame();
	}

	private void initFrame() {
		setSize(700, 400);
		setTitle(WINDOW_TITLE);

		// get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// North panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

		// connection panel
		JPanel conPanel = new JPanel();
		JLabel lb_host = new JLabel("Host");
		tf_host = new JTextField();
		tf_host.setColumns(15);

		JLabel lb_port = new JLabel("Port");
		tf_port = new JTextField();
		tf_port.setColumns(4);

		JButton btn_startServer = new JButton("Start server");
		btn_startServer.setName(BTN_START_SERVER_NAME);
		btn_startServer.setPreferredSize(new Dimension(150, 25));
		btn_startServer.addActionListener(this);

		conPanel.add(lb_host);
		conPanel.add(tf_host);
		conPanel.add(lb_port);
		conPanel.add(tf_port);
		conPanel.add(btn_startServer);
		
		northPanel.add(conPanel);
		
		// center
		ta_monitor = new JTextArea(5, 10);
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
			if (!isServerStarted) {
				if (controller.startServer()) {
					isServerStarted = true;
					source.setText("Stop server");
				}
				else {
					JOptionPane.showMessageDialog(null, "Server is already running!", "Start server error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				if (controller.stopServer()) {
					isServerStarted = false;
					source.setText("Start server");
				}
			}
			break;
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
		}
		catch (Exception e) {
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

}
