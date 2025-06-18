package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class EspecieSupressaoNomePopularEspeciePK implements Serializable {

	private static final long serialVersionUID = -7906015279063521859L;

	@JoinColumn(name = "ide_especie_supressao", referencedColumnName = "ide_especie_supressao", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private EspecieSupressao ideEspecieSupressao;

	@JoinColumn(name = "ide_nome_popular_especie", referencedColumnName = "ide_nome_popular_especie", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private NomePopularEspecie ideNomePopularEspecie;

	public EspecieSupressao getIdeEspecieSupressao() {
		return ideEspecieSupressao;
	}

	public void setIdeEspecieSupressao(EspecieSupressao ideEspecieSupressao) {
		this.ideEspecieSupressao = ideEspecieSupressao;
	}

	public NomePopularEspecie getIdeNomePopularEspecie() {
		return ideNomePopularEspecie;
	}

	public void setIdeNomePopularEspecie(
			NomePopularEspecie ideNomePopularEspecie) {
		this.ideNomePopularEspecie = ideNomePopularEspecie;
	}

}
