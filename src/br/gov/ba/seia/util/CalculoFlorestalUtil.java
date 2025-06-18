package br.gov.ba.seia.util;

public class CalculoFlorestalUtil {

	//Constantes para conversão de medidas do volume total.
	private static final Double MDC = 0.5;
	
	private static final Double ESTEREO = 1.5;
		
	public static Double converterM3ParaEstereo(Double m3) {
		if (!Util.isNullOuVazio(m3)) {
			return (m3 * ESTEREO);
		}
		return 0.0;
	}
	
	public static Double converterM3ParaMDC(Double m3) {
		if (!Util.isNullOuVazio(m3)) {
			return (m3 * MDC);
		}
		return 0.0;
	}
	
	public static Double converterEstereoParaM3(Double estereo) {
		if (!Util.isNullOuVazio(estereo)) {
			return (estereo / ESTEREO);
		}
		return 0.0;
	}
	
	public static Double converterEstereoParaMDC(Double estereo) {
		if (!Util.isNullOuVazio(estereo)) {
			return (converterEstereoParaM3(estereo) * MDC);
		}
		return 0.0;
	}
	
	public static Double converterMDCParaM3(Double mdc) {
		if (!Util.isNullOuVazio(mdc)) {
			return (mdc / MDC);
		}
		return 0.0;
	}
	
	public static Double converterMDCParaEstereo(Double mdc) {
		if (!Util.isNullOuVazio(mdc)) {
			return (converterMDCParaM3(mdc) * ESTEREO);
		}
		return 0.0;
	}
	
	
}
