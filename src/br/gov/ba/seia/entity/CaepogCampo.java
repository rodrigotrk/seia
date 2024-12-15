package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Tabela que armazena os Campos de exploração do cadastro
 */
@Entity
@Table(name = "caepog_campo")
public class CaepogCampo extends AbstractEntity implements Comparable<CaepogCampo> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Chave primária da tabela CAEPOG_CAMPO
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_campo_ide_caepog_campo_seq")
	@SequenceGenerator(name = "caepog_campo_ide_caepog_campo_seq", sequenceName = "caepog_campo_ide_caepog_campo_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_campo", nullable = false)
	private Integer ideCaepogCampo;
	
	/**
	 * Nome do Campo da atividade do CAEPOG
	 */
	@Column(name = "nom_caepog_campo", length = 80)
	private String nomCaepogCampo;
	
	public CaepogCampo() {
		super();
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogCampo() {
		return ideCaepogCampo;
	}

	public void setIdeCaepogCampo(Integer ideCaepogCampo) {
		this.ideCaepogCampo = ideCaepogCampo;
	}

	public String getNomCaepogCampo() {
		return nomCaepogCampo;
	}

	public void setNomCaepogCampo(String nomCaepogCampo) {
		this.nomCaepogCampo = nomCaepogCampo;
	}

	@Override
	public int compareTo(CaepogCampo o) {
		return getNomCaepogCampo().compareTo(o.getNomCaepogCampo());
	}
}