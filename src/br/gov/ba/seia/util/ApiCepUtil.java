package br.gov.ba.seia.util;

import java.io.BufferedReader;
import java.io.IOException;

public class ApiCepUtil {
	
	public static String converteJsonEmString(BufferedReader buffereReader) throws IOException {
        String resposta, jsonEmString = "";
        while ((resposta = buffereReader.readLine()) != null) {
            jsonEmString += resposta;
        }
        return jsonEmString;
    }

}
