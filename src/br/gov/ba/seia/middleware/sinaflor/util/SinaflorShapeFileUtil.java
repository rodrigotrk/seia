package br.gov.ba.seia.middleware.sinaflor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.sql.DataSource;

import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.util.BancoUtil;

public class SinaflorShapeFileUtil {
	
	private static String APP_PATH = "/opt/sinaflor/makeShapeFile.sh";
	
	private static String HOST = null;
	private static String USER = null;
	private static String PASSWORD = null;
	private static String DATABASE = null;
	
	public static String makeShapeFile(LocalizacaoGeografica ... localizacaoGeografica) throws Exception {
		
		if(localizacaoGeografica.length==0) {
			throw new Exception("Nenhuma localizacao geográfica foi passada.");
		}
		
		initConnectionParams();
		
		String IDE_LOCALIZACAO_GEOGRAFICA = "";
		
		for(int i=0; i<localizacaoGeografica.length; i++) {
			IDE_LOCALIZACAO_GEOGRAFICA += localizacaoGeografica[i].getIdeLocalizacaoGeografica();
			if(i+1<localizacaoGeografica.length) {
				IDE_LOCALIZACAO_GEOGRAFICA += ",";
			}
		}
		
		String OUTPUT_PATH = "/tmp/" + IDE_LOCALIZACAO_GEOGRAFICA + "/SHAPEFILE-" + IDE_LOCALIZACAO_GEOGRAFICA;
		
		String PARAMS = HOST + " " + USER + " " + PASSWORD + " " + DATABASE + " " + OUTPUT_PATH + " " + IDE_LOCALIZACAO_GEOGRAFICA;
		
		String command = APP_PATH + " " + PARAMS;
		
		executeCommand(command);
		
		return OUTPUT_PATH;
		
	}
	
	private static void executeCommand(String command) throws IOException, InterruptedException {
		
		StringBuilder output = new StringBuilder();
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";			
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");
		}
		System.out.println(output.toString());
		
	}
	
	private static void initConnectionParams() throws Exception {
		if(HOST==null) {
			DataSource dataSource = (DataSource) BancoUtil.getDataSource();
			USER = dataSource.getConnection().getMetaData().getUserName();
			HOST = getHost(dataSource);
			DATABASE = getDatabase(dataSource);
			PASSWORD = getPassword(dataSource);
		}
	}

	private static String getHost(DataSource dataSource) throws SQLException {
		String host = dataSource.getConnection().getMetaData().getURL();
		int inicio = host.indexOf("//") + 2;
		int fim = host.lastIndexOf(":");
		return host.substring(inicio, fim);
	}
	
	private static String getDatabase(DataSource dataSource) throws SQLException {
		String database = dataSource.getConnection().getMetaData().getURL();
		int inicio = database.lastIndexOf("/") + 1;
		return database.substring(inicio);
	}

	private static String getPassword(DataSource dataSource) throws NoSuchFieldException, IllegalAccessException {
		
		Field mcfField = dataSource.getClass().getDeclaredField("mcf");
		mcfField.setAccessible(true);
		Object mcf = mcfField.get(dataSource);
		
		Field passwordField = mcf.getClass().getSuperclass().getDeclaredField("password");
		passwordField.setAccessible(true);
		
		return (String) passwordField.get(mcf);
	}
	
}
