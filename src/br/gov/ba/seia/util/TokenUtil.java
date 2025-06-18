package br.gov.ba.seia.util;

public class TokenUtil {

	public static synchronized String gerarCodigoAcesso() throws Exception {
		String stringAleatoria = Util.getStringAlfanumAleatoria(7);
		String codigoAcesso = CriptoUtil.gerarHash(stringAleatoria, "md5");
		return codigoAcesso.toUpperCase();
	}

}
