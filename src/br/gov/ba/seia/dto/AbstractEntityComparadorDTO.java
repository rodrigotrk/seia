package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.generic.AbstractEntity;
/**
 * Classe de integração
 * @author 
 *
 */
public class AbstractEntityComparadorDTO {

	private AbstractEntity original;
	private AbstractEntity novo;
	/**
	 * Construtor
	 */
	public AbstractEntityComparadorDTO() {
	}
	/**
	 * 
	 * @param original
	 * @param novo
	 */
	public AbstractEntityComparadorDTO(AbstractEntity original, AbstractEntity novo) {
		this.original = original;
		this.novo = novo;
	}

	public AbstractEntity getOriginal() {
		return original;
	}

	public void setOriginal(AbstractEntity original) {
		this.original = original;
	}

	public AbstractEntity getNovo() {
		return novo;
	}

	public void setNovo(AbstractEntity novo) {
		this.novo = novo;
	}
	
}
