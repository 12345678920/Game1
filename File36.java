package com.game;

import java.io.*;
//import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class File36 {
	private static HashMap<String, String> stored = new HashMap<>();
	@Deprecated
	public static void write(String name, String content) {
		File file = new File(name + ".file36");
		try {
			FileOutputStream st = new FileOutputStream(file);
			String con = Mode36Cipher(content, Mode.ENCRYPT);
			st.write(con.getBytes());
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap<String, String> read(String file) {
		return readString(Reader.read(file));
	}
	public static void insert(String key, String value) {
		stored.put(key, value);
	}
	public static void clear() {
		stored.clear();
	}
	public static void writeInserted(String name) {
		File file = new File(name + ".file36");
		try {
			FileOutputStream st = new FileOutputStream(file);
			StringBuilder content = new StringBuilder();
			for(Map.Entry<String, String> e : stored.entrySet()) {
				String con1 = Mode36Cipher(e.getKey() + "," + e.getValue() + "\r\n", Mode.ENCRYPT);
				content.append(con1);
			}
			st.write(content.toString().getBytes());
			st.close();
		} catch(Exception e) {
			
		}
	}
	public static HashMap<String, String> readString(String str) {
		HashMap<String, String> m = new HashMap<>();
		String pstr = Mode36Cipher(str, Mode.DECRYPT);
		String[] strstr = pstr.split("\\r?\\n");
		for(String inner : strstr) {
			String[] data = inner.split("\\s*,\\s*");
			m.put(data[0], data[1]);
		}
		return m;
	}
	public static HashMap<String, String> readURL(String url) {
		try {
			URL urli = new URL(url + ".png");
			InputStream strim = urli.openStream();
			InputStreamReader r = new InputStreamReader(strim);
			BufferedReader br = new BufferedReader(r);
			String total = "";
			String line;
			while((line = br.readLine()) != null) {
				total += line;
			}
			return readString(total);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String Mode36Cipher(String str, Mode mode) {
		/** This is an interesting mode of cipher */
		String rez = "";
		char[] oldarray = {',', '\r', '\n', ' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		char[] newarray = {'F', '9', 'Q', 'h', '%', '*', ')', '[', '?', '\'', '$', '/', '+', ';', '"', '@', '\\', ']', '!', '>', 'L', '.', '4', '<', '&', 't', '|', '(', '^', '_', '}', '-', '=', 'P', '7', '~', ',', ':', '#', '`', '{', ' ', 'G', 'k', 'C', 'W', '2', '5', '0', 'A', 'l', 'B', 'q', '0', 'h', 'H'};
		if(mode == Mode.ENCRYPT) {
			boolean inbounds = false;
			char[] charz = str.toCharArray();
			for(char c : charz) {
			for(int i = 0; i < oldarray.length; i++) {
				if(c == oldarray[i]) {
					rez += newarray[i];
					inbounds = true;
				}
			}
			if(!inbounds) {
				rez += c;
			}
			inbounds = false;
			}
			return rez;
		}
		else if(mode == Mode.DECRYPT) {
			boolean inbounds = false;
			char[] charz = str.toCharArray();
			for(char c : charz) {
			for(int i = 0; i < newarray.length; i++) {
				if(c == newarray[i]) {
					rez += oldarray[i];
					inbounds = true;
				}
			}
			if(!inbounds) {
				rez += c;
			}
			inbounds = false;
			}
			return rez;
		}
		return null;
	}
	public enum Mode {
		ENCRYPT, DECRYPT;
	}
	public static class Reader {
		public static String read(String file) {
			try {
			InputStream fis = new FileInputStream(file + ".file36");
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		    String total = "";
		    String line;
		    while((line = br.readLine()) != null) {
		    	total += (line);
		    }
		    br.close();
		    return total;
			} catch(Exception e) {
				return null;
			}
		}
	}
	@SuppressWarnings("serial")
	public class File36Exception extends Exception {
		public File36Exception() {
			super("There was an error in handling the file36 data");
		}
	}
}
