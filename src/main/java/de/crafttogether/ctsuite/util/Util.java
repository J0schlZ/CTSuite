package de.crafttogether.ctsuite.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {

	public static String readFile(InputStream inputStream) {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		String fileContent = null;
		int result;
		try {
			result = bis.read();
			while(result != -1) {
			    buf.write((byte) result);
			    result = bis.read();
			}
			fileContent = buf.toString("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContent;
	}
	
	public static String readFile (String filePath) {
		File file = null;
		FileInputStream inputStream = null;
		
		try {
			file = new File(filePath);			
			inputStream = new FileInputStream(file);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				inputStream.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return readFile(inputStream);
	}
}
