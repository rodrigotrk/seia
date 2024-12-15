package br.gov.ba.seia.controller.abstracts;

import br.gov.ba.seia.enumerator.AbaIdentificarPapelEnum;
/**
 * Aba identificar papel
 * @author 
 *
 */
public class Aba {

	private AbaIdentificarPapelEnum abaIdentificarPapel;
	
	/**
	 * 
	 * @param abaIdentificarPapel
	 */
	public Aba(AbaIdentificarPapelEnum abaIdentificarPapel) {
		abaIdentificarPapel = abaIdentificarPapel;
	}
/**
 * 
 * @return aba identificar papel
 */
	public AbaIdentificarPapelEnum getAbaIdentificarPapel() {
		return abaIdentificarPapel;
	}
	
	
	
}
