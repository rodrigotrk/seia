package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.CaepogDocumento;
import br.gov.ba.seia.entity.CaepogDocumentoApensado;
/**
 * 
 * @author 
 *
 */
public class CaepogDocumentoApensadoDTO {
	
	private CaepogDocumento caepogDocumento;
	private CaepogDocumentoApensado caepogDocumentoApensado;
	
	/**
	 * Construtor
	 */
	public CaepogDocumentoApensadoDTO() {
	}

	public CaepogDocumento getCaepogDocumento() {
		return caepogDocumento;
	}

	public void setCaepogDocumento(CaepogDocumento caepogDocumento) {
		this.caepogDocumento = caepogDocumento;
	}

	public CaepogDocumentoApensado getCaepogDocumentoApensado() {
		return caepogDocumentoApensado;
	}
	/**
	 * 
	 * @param caepogDocumentoApensado
	 */
	public void setCaepogDocumentoApensado(
			CaepogDocumentoApensado caepogDocumentoApensado) {
		this.caepogDocumentoApensado = caepogDocumentoApensado;
	}	
}
