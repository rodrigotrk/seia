package br.gov.ba.seia.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/**
 * Classe utilitária para ler e escrever em arquivo texto formato CSV
 */
public class CSVUtil {

	private static final char DEFAULT_SEPARATOR = ';';

	public static void writeFile(File file, List<List<String>> lines) throws IOException {
		FileWriter writer = new FileWriter(file);
		for (List<String> line : lines) {
			writeLine(writer, line);
		}
		writer.flush();
		writer.close();
	}

	public static void writeLine(Writer w, List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
		writeLine(w, values, separators, ' ');
	}

	private static String followCVSformat(String value) {

		String result = value;
		if (result.contains("\"")) {
			result = result.replace("\"", "\"\"");
		}
		return result;

	}

	public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

		boolean first = true;

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				sb.append(followCVSformat(value));
			} else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append("\n");
		w.append(sb.toString());

	}
	
	/**
	 * A partir do nome do arquivo informado, percorre todo o arquivo e preenche uma lista de Strings sendo cada linha um item 
	 * @param arquivoCSV
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> readFilePCT(String arquivoCSV) throws IOException {
		
		List<String[]> lista = new ArrayList<String[]>();
		BufferedReader br = null;
		byte[] arquivo = FileUtils.readFileToByteArray(new File(arquivoCSV));
		CharsetDetector charsetDetector = new CharsetDetector();
		charsetDetector.setText(arquivo);
		CharsetMatch match = charsetDetector.detect();
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoCSV), match.getName()));
			String linha = "";
			
			while ((linha = br.readLine()) != null) {
				String[] colunas = linha.split(String.valueOf(DEFAULT_SEPARATOR));
				lista.add(colunas);
	        }			
		} finally {
			if (br != null) br.close();
		}
		
		return lista;
	}

}
