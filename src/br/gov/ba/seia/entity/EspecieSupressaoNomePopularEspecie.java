package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "especie_supressao_nome_popular_especie")
public class EspecieSupressaoNomePopularEspecie implements Serializable {

	private static final long serialVersionUID = 1213012960977365943L;

	@EmbeddedId
	private EspecieSupressaoNomePopularEspeciePK especieSupressaoNomePopularEspeciePK;

	public EspecieSupressaoNomePopularEspeciePK getEspecieSupressaoNomePopularEspeciePK() {
		return especieSupressaoNomePopularEspeciePK;
	}

	public void setEspecieSupressaoNomePopularEspeciePK(
			EspecieSupressaoNomePopularEspeciePK especieSupressaoNomePopularEspeciePK) {
		this.especieSupressaoNomePopularEspeciePK = especieSupressaoNomePopularEspeciePK;
	}

}
