package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_rio_intervencao")
@XmlRootElement
public class TipoRioIntervencao implements Serializable, BaseEntity {

	private static final long serialVersionUID = -3574048192169037338L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_tipo_rio_intervencao", nullable=false)
	private Integer ideTipoRioIntervencao;
	
	@Column(name="des_tipo_rio_intervencao", nullable=false)
	private String desTipoRioIntervencao;
	
	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao;
	
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_ativo", nullable=false)
	private Boolean indAtivo;
	
	public TipoRioIntervencao() {
	}
	
	public TipoRioIntervencao(Integer ideTipoRioIntervencao) {
		this.ideTipoRioIntervencao = ideTipoRioIntervencao;
	}

	public Integer getIdeTipoRioIntervencao() {
		return ideTipoRioIntervencao;
	}

	public String getDesTipoRioIntervencao() {
		return desTipoRioIntervencao;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIdeTipoRioIntervencao(Integer ideTipoRioIntervencao) {
		this.ideTipoRioIntervencao = ideTipoRioIntervencao;
	}

	public void setDesTipoRioIntervencao(String desTipoRioIntervencao) {
		this.desTipoRioIntervencao = desTipoRioIntervencao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((desTipoRioIntervencao == null) ? 0 : desTipoRioIntervencao
						.hashCode());
		result = prime * result
				+ ((dtcCriacao == null) ? 0 : dtcCriacao.hashCode());
		result = prime
				* result
				+ ((ideTipoRioIntervencao == null) ? 0 : ideTipoRioIntervencao
						.hashCode());
		result = prime * result
				+ ((indAtivo == null) ? 0 : indAtivo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoRioIntervencao other = (TipoRioIntervencao) obj;
		if (desTipoRioIntervencao == null) {
			if (other.desTipoRioIntervencao != null)
				return false;
		} else if (!desTipoRioIntervencao.equals(other.desTipoRioIntervencao))
			return false;
		if (dtcCriacao == null) {
			if (other.dtcCriacao != null)
				return false;
		} else if (!dtcCriacao.equals(other.dtcCriacao))
			return false;
		if (ideTipoRioIntervencao == null) {
			if (other.ideTipoRioIntervencao != null)
				return false;
		} else if (!ideTipoRioIntervencao.equals(other.ideTipoRioIntervencao))
			return false;
		if (indAtivo == null) {
			if (other.indAtivo != null)
				return false;
		} else if (!indAtivo.equals(other.indAtivo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideTipoRioIntervencao);
	}

	@Override
	public Long getId() {
		return Long.valueOf(ideTipoRioIntervencao.longValue());
	}

}
