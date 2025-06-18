package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "especie_florestal_nome_popular_especie")
public class EspecieFlorestalNomePopularEspecie implements Serializable {
	private static final long serialVersionUID = 1213012960977365943L;
	
	@EmbeddedId
	private EspecieFlorestalNomePopularEspeciePK especieFlorestalNomePopularEspeciePK;
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public EspecieFlorestalNomePopularEspeciePK getEspecieFlorestalNomePopularEspeciePK() {
		return especieFlorestalNomePopularEspeciePK;
	}
	
	public void setEspecieFlorestalNomePopularEspeciePK(EspecieFlorestalNomePopularEspeciePK especieFlorestalNomePopularEspeciePK) {
		this.especieFlorestalNomePopularEspeciePK = especieFlorestalNomePopularEspeciePK;
	}
}