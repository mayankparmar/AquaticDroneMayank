package logger;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DataLogger {
	
	protected String fileName, message;		
	protected static  BufferedWriter writer;
	
	/*public static void main (String[] args) {
		loggerInit("first file");
		logStart("Content, hi");		
	}*/
			
	public void loggerInit(String fileName){		
		fileName = fileName + ".csv";
		try {
			writer = new BufferedWriter(new FileWriter(fileName, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void logStart (String message){		
		try {
			writer.write(message);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
}

