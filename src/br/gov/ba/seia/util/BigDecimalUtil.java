package br.gov.ba.seia.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public  class  BigDecimalUtil {

	/**
	 * @author alexandre queiroz
	 * @return aproxima o bigdecimal para a quantidade de casas solicitadas
	 */
	public static BigDecimal aproximar(BigDecimal bigDecimal, int qtdCasasDecimais){
		return bigDecimal.setScale(qtdCasasDecimais, BigDecimal.ROUND_UP);
	}
	
	public static BigDecimal aproximar(Double val, int qtdCasasDecimais){
		return aproximar(new BigDecimal(val), qtdCasasDecimais);
	}
	
	/**
	 * @author alexandre queiroz
	 * @return trunca o bigdecimal para a quantidade de casas solicitadas
	 * 
	 * Obs: preferencialmente use o metodo aproximar, user truncar apenas se tiver certeza da operação que esta realizando.
	 */
	public static BigDecimal truncar(BigDecimal bigDecimal,int qtdCasasDecimais){
		return bigDecimal.setScale(qtdCasasDecimais,RoundingMode.DOWN);
	}
}