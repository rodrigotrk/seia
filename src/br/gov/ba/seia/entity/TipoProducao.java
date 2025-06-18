package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.TipoProducaoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * 1 - "Formas Jovens"
 * 2 - "Engorda / Crescimento"
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_producao", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"nom_tipo_producao"})})
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipoProducao.findAll", query = "SELECT t FROM TipoProducao t"),
	@NamedQuery(name = "TipoProducao.findByIdeTipoProducao", query = "SELECT t FROM TipoProducao t WHERE t.ideTipoProducao = :ideTipoProducao"),
	@NamedQuery(name = "TipoProducao.findByNomTipoProducao", query = "SELECT t FROM TipoProducao t WHERE t.nomTipoProducao = :nomTipoProducao")})
public class TipoProducao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_producao", nullable = false)
	private Integer ideTipoProducao;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 15)
	@Column(name = "nom_tipo_producao", nullable = false, length = 15)
	private String nomTipoProducao;

	@Column(name="ind_ativo", nullable = false)
	private Boolean indAtivo;

	public TipoProducao() {
	}

	public TipoProducao(Integer ideTipoProducao) {
		this.ideTipoProducao = ideTipoProducao;
	}

	public TipoProducao(Integer ideTipoProducao, String nomTipoProducao) {
		this.ideTipoProducao = ideTipoProducao;
		this.nomTipoProducao = nomTipoProducao;
	}

	public Integer getIdeTipoProducao() {
		return ideTipoProducao;
	}

	public void setIdeTipoProducao(Integer ideTipoProducao) {
		this.ideTipoProducao = ideTipoProducao;
	}

	public String getNomTipoProducao() {
		return nomTipoProducao;
	}

	public void setNomTipoProducao(String nomTipoProducao) {
		this.nomTipoProducao = nomTipoProducao;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoProducao != null ? ideTipoProducao.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		if (!(object instanceof TipoProducao)) {
			return false;
		}
		TipoProducao other = (TipoProducao) object;
		if ((this.ideTipoProducao == null && other.ideTipoProducao != null) || (this.ideTipoProducao != null && !this.ideTipoProducao.equals(other.ideTipoProducao))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoProducao[ ideTipoProducao=" + ideTipoProducao + " ]";
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.ideTipoProducao);
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public boolean isFormasJovenes(){
		return this.ideTipoProducao.equals(TipoProducaoEnum.FORMAS_JOVENS.getId());
	}

	public boolean isEngorda(){
		return this.ideTipoProducao.equals(TipoProducaoEnum.ENGORDA.getId());
	}
}
