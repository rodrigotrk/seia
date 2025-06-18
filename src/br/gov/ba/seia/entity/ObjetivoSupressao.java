package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the objetivo_supressao database table.
 * 
 */
@Entity
@Table(name="objetivo_supressao")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ObjetivoSupressao.findByIdeObjetivoSupressao", query = "SELECT os FROM ObjetivoSupressao os WHERE os.ideObjetoSupressao = :ideObjetoSupressao"),
	@NamedQuery(name = "ObjetivoSupressao.deleteByIdeObjetivoSupressao", query = "DELETE FROM ObjetivoSupressao os WHERE os.ideObjetoSupressao = :ideObjetoSupressao")})
public class ObjetivoSupressao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objetivo_supressao_ide_objeto_supressao_seq")
	@SequenceGenerator(name = "objetivo_supressao_ide_objeto_supressao_seq", sequenceName = "objetivo_supressao_ide_objeto_supressao_seq", allocationSize = 1)
	@Column(name="ide_objetivo_supressao")
	private Integer ideObjetoSupressao;

	@Column(name="dsc_objetivo_supressao")
	private String dscObjetoSupressao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Basic(optional = true)
	@Column(name="ide_objetivo_supressao_pai")
	private Integer objetivoSupressao;

	public ObjetivoSupressao() {
	}

	public ObjetivoSupressao(String dscObjetoSupressao,	Integer objetivoSupressao, Boolean indAtivo) {
		this.dscObjetoSupressao = dscObjetoSupressao;
		this.objetivoSupressao = objetivoSupressao;
		this.indAtivo = indAtivo;
	}

	public ObjetivoSupressao(Integer id) {
		this.ideObjetoSupressao = id;
	}

	public Integer getIdeObjetoSupressao() {
		return this.ideObjetoSupressao;
	}

	public void setIdeObjetoSupressao(Integer ideObjetoSupressao) {
		this.ideObjetoSupressao = ideObjetoSupressao;
	}

	public String getDscObjetoSupressao() {
		return this.dscObjetoSupressao;
	}

	public void setDscObjetoSupressao(String dscObjetoSupressao) {
		this.dscObjetoSupressao = dscObjetoSupressao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Integer getObjetivoSupressao() {
		return objetivoSupressao;
	}

	public void setObjetivoSupressao(Integer objetivoSupressao) {
		this.objetivoSupressao = objetivoSupressao;
	}

	@Override
	public Long getId() {
		return new Long(ideObjetoSupressao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideObjetoSupressao == null) ? 0 : ideObjetoSupressao
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObjetivoSupressao other = (ObjetivoSupressao) obj;
		if (ideObjetoSupressao == null) {
			if (other.ideObjetoSupressao != null) {
				return false;
			}
		} else if (!ideObjetoSupressao.equals(other.ideObjetoSupressao)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ObjetivoSupressao [" + dscObjetoSupressao + "]";
	}
}