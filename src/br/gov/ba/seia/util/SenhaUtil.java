package br.gov.ba.seia.util;

import java.util.Random;

public class SenhaUtil {

	private static final char[] CARACTERES_DISPONIVEIS_SENHA = new char[62];
	private static final String CARACTERES_ESPECIAL = "!@#$%&*?/";
	private static final String CARATERES_MAIUSCULA = "QWERTYUIOPASDFGHJKLZXCVBNM";
	private static final String NUMERO = "123456789";
	private static final Random RANDOM = new Random();

	static {

		for (int lFaixaCaracteres = 48, lIndiceCaracteres = 0; lFaixaCaracteres <= 122; lFaixaCaracteres++) {

			if (Character.isLetterOrDigit(lFaixaCaracteres)) {

				CARACTERES_DISPONIVEIS_SENHA[lIndiceCaracteres] = (char) lFaixaCaracteres;
				lIndiceCaracteres++;
			}
		}
	}

	public static String gerarSenha(final int pTamanhoSenha) {

		
		final char[] diverso = new char[5];
		
		/*Gerando Diversos*/
		for (int i = 0; i < 5; i++) {

			diverso[i] = CARACTERES_DISPONIVEIS_SENHA[RANDOM.nextInt(CARACTERES_DISPONIVEIS_SENHA.length)];
		}
		

		StringBuilder sb = new StringBuilder(pTamanhoSenha);
		sb.append(CARACTERES_ESPECIAL.charAt(RANDOM.nextInt(CARACTERES_ESPECIAL.length())))
		.append(new String(diverso))
		.append(NUMERO.charAt(RANDOM.nextInt(NUMERO.length())))
		.append(CARATERES_MAIUSCULA.charAt(RANDOM.nextInt(CARATERES_MAIUSCULA.length())));

		return sb.toString();
	}
}