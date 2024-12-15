package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: LocalizacaoAtividadeTorre.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 6 de out de 2017
 * Objetivo: 	
	
 */
@Entity
@Table(name = "localizacao_atividade_torre")
@NamedQuery(name="LocalizacaoAtividadeTorre.findAll", query="SELECT l FROM LocalizacaoAtividadeTorre l")
public class LocalizacaoAtividadeTorre implements Serializable, BaseEntity{

	/**
	 * Propriedade: serialVersionUID
	 * @type: long
	 */
	private static final long serialVersionUID = -7921382174519623435L;

	/**
	 * Propriedade: ideTipoNaturezaTorre
	 * @type: Long
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "localizacao_atividade_torre_ide_localizacao_atividade_torre_seq")
	@SequenceGenerator(name = "localizacao_atividade_torre_ide_localizacao_atividade_torre_seq", sequenceName = "localizacao_atividade_torre_ide_localizacao_atividade_torre_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_localizacao_atividade_torre", nullable = false)
	private Long ideLocalizacaoAtividadeTorre;

	/**
	 * Propriedade: nomNatureza
	 * @type: String
	 */
	@Column(name = "nom_localizacao_atividade_torre")
	private String nomLocalizacaoAtividadeTorre;
	
	/**
	 * Getter do campo ideLocalizacaoAtividadeTorre
	 *	
	 * @return the ideLocalizacaoAtividadeTorre
	 */
	public Long getIdeLocalizacaoAtividadeTorre() {
		return ideLocalizacaoAtividadeTorre;
	}

	/**
	 * Setter do campo  ideLocalizacaoAtividadeTorre
	 * @param ideLocalizacaoAtividadeTorre the ideLocalizacaoAtividadeTorre to set
	 */
	public void setIdeLocalizacaoAtividadeTorre(Long ideLocalizacaoAtividadeTorre) {
		this.ideLocalizacaoAtividadeTorre = ideLocalizacaoAtividadeTorre;
	}

	/**
	 * Getter do campo nomLocalizacaoAtividadeTorre
	 *	
	 * @return the nomLocalizacaoAtividadeTorre
	 */
	public String getNomLocalizacaoAtividadeTorre() {
		return nomLocalizacaoAtividadeTorre;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		LocalizacaoAtividadeTorre other = (LocalizacaoAtividadeTorre)obj;
		return new EqualsBuilder()
			.append(this.ideLocalizacaoAtividadeTorre, other.ideLocalizacaoAtividadeTorre)
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.ideLocalizacaoAtividadeTorre)
				.toHashCode();
	}
	/**
	 * Setter do campo  nomLocalizacaoAtividadeTorre
	 * @param nomLocalizacaoAtividadeTorre the nomLocalizacaoAtividadeTorre to set
	 */
	public void setNomLocalizacaoAtividadeTorre(String nomLocalizacaoAtividadeTorre) {
		this.nomLocalizacaoAtividadeTorre = nomLocalizacaoAtividadeTorre;
	}

	@Override
	public Long getId() {
		return ideLocalizacaoAtividadeTorre;
	}

}
