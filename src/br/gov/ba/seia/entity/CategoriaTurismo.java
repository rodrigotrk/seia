package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.CategoriaTurismoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * Entitade criada para representar a tabela <i>categoria_turismo</i>, criada para armazenar os itens classificados como "Categoria do Empreendimento" no FCE - Turismo.
 */
@Entity
@Table(name="categoria_turismo")
public class CategoriaTurismo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATEGORIA_TURISMO_IDECATEGORIATURISMO_GENERATOR", sequenceName="CATEGORIA_TURISMO_IDE_CATEGORIA_TURISMO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATEGORIA_TURISMO_IDECATEGORIATURISMO_GENERATOR")
	@Column(name="ide_categoria_turismo")
	private Integer ideCategoriaTurismo;

	@Column(name="dsc_categoria_turismo")
	private String dscCategoriaTurismo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipologia")
	private Tipologia ideTipologia;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public CategoriaTurismo() {
	}

	public CategoriaTurismo(CategoriaTurismoEnum categoriaTurismoEnum) {
		this.ideCategoriaTurismo = categoriaTurismoEnum.getId();
	}

	public Integer getIdeCategoriaTurismo() {
		return this.ideCategoriaTurismo;
	}

	public void setIdeCategoriaTurismo(Integer ideCategoriaTurismo) {
		this.ideCategoriaTurismo = ideCategoriaTurismo;
	}

	public String getDscCategoriaTurismo() {
		return this.dscCategoriaTurismo;
	}

	public void setDscCategoriaTurismo(String dscCategoriaTurismo) {
		this.dscCategoriaTurismo = dscCategoriaTurismo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCategoriaTurismo == null) ? 0 : ideCategoriaTurismo
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		CategoriaTurismo other = (CategoriaTurismo) obj;
		if (ideCategoriaTurismo == null) {
			if (other.ideCategoriaTurismo != null) {
				return false;
			}
		} else if (!ideCategoriaTurismo.equals(other.ideCategoriaTurismo)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.ideCategoriaTurismo);
	}
}