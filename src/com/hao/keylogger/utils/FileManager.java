package com.hao.keylogger.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.models.Resources;

public class FileManager {
	private String filePath;

	public FileManager(String filePath) {
		this.filePath = filePath;
	}

	public FileManager() {
		filePath = ".";
	}

	public String readAll() throws IOException {
		StringBuilder stb = new StringBuilder();
		InputStream fIn = new FileInputStream(filePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fIn, Charset.forName("UTF-8")));
		int readChar = 0;
		char[] buf = new char[256];
		while ((readChar = reader.read(buf)) != -1) {
			stb.append(buf, 0, readChar);
		}
		reader.close();
		fIn.close();
		return stb.toString();
	}

	public void writeToFile(String content) throws IOException {
		File file = new File(filePath);
		File logsDir = new File(Resources.LOGS_CLIENT_DIRECTORY);
		if (!logsDir.exists()) {
			logsDir.mkdirs();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
		writer.write(content);
		writer.close();
	}
	
	/**
	 * Save serialized log to file
	 * @param log
	 * @param filePath
	 */
	public void writeLogToFile(Log log, String filePath) {
		try {
			File fol = new File(new File(filePath).getParent());
			if (!fol.exists()) {
				fol.mkdir();
			}
			FileOutputStream fout = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			
			out.writeObject(log);
			out.close();
			fout.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Deserialize log file
	 * @param filePath
	 * @return a log file if successful, an empty log file if error
	 */
	
	public Log loadLogFromFile(String filePath) {
		Log log = new Log();
			try {
				FileInputStream fin = new FileInputStream(filePath);
				ObjectInputStream in = new ObjectInputStream(fin);
				try {
					log = (Log) in.readObject();
					fin.close();
					in.close();
					return log;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new Log();			
	}

	public String getFileName() {
		File file = new File(filePath);
		return file.getName();
	}
}
