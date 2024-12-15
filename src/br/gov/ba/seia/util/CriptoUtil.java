package br.gov.ba.seia.util;

import java.security.MessageDigest;

public class CriptoUtil {

	public static String gerarHash(String plainText, String algorithm) throws Exception {

		MessageDigest mdAlgorithm;

		StringBuilder hexString = new StringBuilder();

		try {

			mdAlgorithm = MessageDigest.getInstance(algorithm);

			mdAlgorithm.update(plainText.getBytes());

			byte[] digest = mdAlgorithm.digest();

			for (int i = 0; i < digest.length; i++) {

				plainText = Integer.toHexString(0xFF & digest[i]);

				if (plainText.length() < 2) {

					plainText = "0" + plainText;
				}

				hexString.append(plainText);
			}

		} catch (Exception e) {

			throw new Exception();
		}

		return hexString.toString();
	}
}
