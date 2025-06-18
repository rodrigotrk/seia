package br.gov.ba.seia.middleware.sinaflor.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;

import sun.misc.BASE64Encoder;

public class Base64Util {
	
	public static String encoder(InputStream input) throws IOException {
		int len;
		int size = 1024;
		byte[] buf = new byte[size];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
	    while ((len = input.read(buf, 0, size)) != -1) {
	    	bos.write(buf, 0, len);
	    }
	    buf = bos.toByteArray();
			
	    String encode = new BASE64Encoder().encode(buf);
	    input.close();
	    
		return encode;
	}
	
	public static String encoder(File file) throws IOException {
		String encode = DatatypeConverter.printBase64Binary(FileUtils.readFileToByteArray(file));
		return encode;
	}

}
