package br.gov.ba.seia.util;

import java.text.Normalizer;

public class StringUtil {
	
	public static String rlPad(String str, char pad, int num) {
		return String.format("%1$-" + num + "s", str).replace(' ', pad);
	}

	public static String lPad(String str, char pad, int num) {
		return String.format("%1$" + num + "s", str).replace(' ', pad);
	}
	
	public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
