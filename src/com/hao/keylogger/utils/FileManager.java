package com.hao.keylogger.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.hao.keylogger.models.Resource;

public class FileManager {
	private String filePath;

	public FileManager(String filePath) {
		this.filePath = filePath;
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
		return stb.toString();
	}

	public void writeToFile(String content) throws IOException {
		File file = new File(filePath);
		File logsDir = new File(Resource.LOGS_CLIENT_DIRECTORY);
		if (!logsDir.exists()) {
			logsDir.mkdirs();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
		writer.write(content);
		writer.close();
	}

	public String getFileName() {
		File file = new File(filePath);
		return file.getName();
	}
}
