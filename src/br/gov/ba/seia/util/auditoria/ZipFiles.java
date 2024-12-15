package br.gov.ba.seia.util.auditoria;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class ZipFiles {
	
	public static void zipIt(String zipFile, String pathDirectory) {
		 List<String> fileList = pathFilesInDirectory(pathDirectory);
	     byte[] buffer = new byte[1024];
	 
	     try{
	 
	    	FileOutputStream fos = new FileOutputStream(pathDirectory + File.separator +zipFile+".zip");
	    	ZipOutputStream zos = new ZipOutputStream(fos);
	 
	    	System.out.println("Output to Zip : " + pathDirectory + File.separator +zipFile+".zip");
	 
	    	for(String file : fileList) {
	 
	    		System.out.println("File Added : " + file);
	    		ZipEntry ze = new ZipEntry(file);
	        	zos.putNextEntry(ze);
	 
	        	FileInputStream in = 
	                       new FileInputStream(pathDirectory + File.separator + file);
	 
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	 
	        	in.close();
	    	}
	 
	    	zos.closeEntry();
	    	//remember close it
	    	zos.close();
	 
	    	System.out.println("Done");
	    }catch(IOException ex) {
	       Log4jUtil.log(ZipFiles.class.getName(),Level.ERROR, ex);   
	    }
	   }
	
	private static List<String> pathFilesInDirectory(String pathDirectory) {
		File file = new File(pathDirectory);
		File[] listFiles = file.listFiles();
		List<String> arquivos = new ArrayList<String>();
		if(!Util.isNullOuVazio(listFiles)) {
			for (File f : listFiles) {
				if(!f.getName().contains("zip")) {
					arquivos.add(f.getName());
				}
			}
		}
		return arquivos;
	}
	
	/**
	 * Método responsável por criar um {@link ByteArrayOutputStream} a partir de uma lista de {@link StreamedContent}.
	 * 
	 * @author ivanildo.souza
	 * @since 06/10/16
	 * @param streamContentList
	 * @throws IOException
	 */
	public static ByteArrayOutputStream comprimirStreamedContentListZip(List<StreamedContent> streamContentList) throws IOException {
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
		ZipOutputStream zipOutput = new ZipOutputStream(byteArrayOutput);	    	

		for (StreamedContent streamedContent : streamContentList) {
			
			String fileName = streamedContent.getName();
			InputStream inputStream = streamedContent.getStream();

			ZipEntry entry = new ZipEntry(fileName);
			zipOutput.putNextEntry(entry);

			byte[] data = new byte[1024]; 
			int count;
			while ((count = inputStream.read(data)) != -1) {
				zipOutput.write(data, 0, count);
			}
			zipOutput.closeEntry();
		}
		zipOutput.close();
		return byteArrayOutput;
	}
}
