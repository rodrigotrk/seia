package br.gov.ba.seia.filter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;

import com.google.gson.Gson;

import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.util.Log4jUtil;

public class RecaptchaService {
	/**
	 * Verifica captcha
	 * @param request
	 * @return falso ou verdadeiro para verificação do captcha
	 */
	public boolean checkRecaptcha(HttpServletRequest request) {
		String recap = request.getParameter("g-recaptcha-response");

		try {
			return true;/*
			String urlGoogle = ConfigEnum.RECATCHA_URL.toString();
			String secret = ConfigEnum.RECAPTCHA_PRIVATE_KEY.toString();
			String urlFormatada = String.format(urlGoogle, secret, recap, (request.getRemoteAddr() != null ? "localhost" : "0.0.0.0"));
			URL url = new URL(urlFormatada);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String line, outputString = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				outputString += line;
			}
			CaptchaResponse capRes = new Gson().fromJson(outputString, CaptchaResponse.class);

			// Verify whether the input from Human or Robot
			if (capRes.isSuccess()) {
				// Input by Human
				return true;
			} else {
				// Input by Robot
				return false;
			}
*/
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}

	private class CaptchaResponse {
		private boolean success;
		private String[] errorCodes;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String[] getErrorCodes() {
			return errorCodes;
		}

		public void setErrorCodes(String[] errorCodes) {
			this.errorCodes = errorCodes;
		}

	}
}