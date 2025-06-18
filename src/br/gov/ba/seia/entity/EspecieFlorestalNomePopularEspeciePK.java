package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class EspecieFlorestalNomePopularEspeciePK implements Serializable {
	private static final long serialVersionUID = -7906015279063521859L;
	
	@JoinColumn(name = "ide_especie_florestal", referencedColumnName = "ide_especie_florestal", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private EspecieFlorestal ideEspecieFlorestal;
	
	@JoinColumn(name = "ide_nome_popular_especie", referencedColumnName = "ide_nome_popular_especie", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private NomePopularEspecie ideNomePopularEspecie;
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public EspecieFlorestal getIdeEspecieFlorestal() {
		return ideEspecieFlorestal;
	}
	
	public void setIdeEspecieFlorestal(EspecieFlorestal ideEspecieFlorestal) {
		this.ideEspecieFlorestal = ideEspecieFlorestal;
	}
	
	public NomePopularEspecie getIdeNomePopularEspecie() {
		return ideNomePopularEspecie;
	}
	
	public void setIdeNomePopularEspecie(NomePopularEspecie ideNomePopularEspecie) {
		this.ideNomePopularEspecie = ideNomePopularEspecie;
	}
}
