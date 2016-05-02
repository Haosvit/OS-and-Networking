package com.hao.keylogger.controllers.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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
				stb.append(buf,0, readChar);
			}
		return stb.toString();
	}
}
