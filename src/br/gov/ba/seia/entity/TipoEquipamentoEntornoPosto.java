package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.util.Util;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "tipo_equipamento_entorno_posto")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoEquipamentoEntornoPosto.findAll", query = "SELECT t FROM TipoEquipamentoEntornoPosto t"),
		@NamedQuery(name = "TipoEquipamentoEntornoPosto.findByIdeTipoEquipamentoEntornoPosto", query = "SELECT t FROM TipoEquipamentoEntornoPosto t WHERE t.ideTipoEquipamentoEntornoPosto = :ideTipoEquipamentoEntornoPosto"),		
		@NamedQuery(name = "TipoEquipamentoEntornoPosto.findByDscTipoEquipamentoEntornoPosto", query = "SELECT t FROM TipoEquipamentoEntornoPosto t WHERE t.dscTipoEquipamentoEntornoPosto = :dscTipoEquipamentoEntornoPosto"),		
		})
@NamedNativeQueries({
	@NamedNativeQuery(name = "TipoEquipamentoEntornoPosto.findByIdeLac", query = "select t.* from entorno_posto_combustivel e inner join tipo_equipamento_entorno_posto t on t.ide_tipo_equipamento_entorno_posto = e.ide_tipo_equipamento_entorno_posto where ide_posto_combustivel = :pIdeLac", resultClass = TipoEquipamentoEntornoPosto.class),
	@NamedNativeQuery(name = "TipoEquipamentoEntornoPosto.removerByIdeLac", query = "delete from entorno_posto_combustivel e inner join tipo_equipamento_entorno_posto t on t.ide_tipo_equipamento_entorno_posto = e.ide_tipo_equipamento_entorno_posto where ide_posto_combustivel = :pIdeLac and t.ide_tipo_equipamento_entorno_posto = :pIdeEquipamento", resultClass = TipoEquipamentoEntornoPosto.class)
})
public class TipoEquipamentoEntornoPosto implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_equipamento_entorno_posto")
	private Integer ideTipoEquipamentoEntornoPosto;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "dsc_tipo_equipamento_entorno_posto")
	private String dscTipoEquipamentoEntornoPosto;
	@ManyToMany(mappedBy = "tipoEquipamentoEntornoPostoCollection")
	private Collection<LacPostoCombustivel> postoCombustivelCollection;

	public TipoEquipamentoEntornoPosto() {
	}

	public TipoEquipamentoEntornoPosto(Integer ideTipoEquipamentoEntornoPosto) {
		this.ideTipoEquipamentoEntornoPosto = ideTipoEquipamentoEntornoPosto;
	}

	public TipoEquipamentoEntornoPosto(Integer ideTipoEquipamentoEntornoPosto, String dscTipoEquipamentoEntornoPosto) {
		this.ideTipoEquipamentoEntornoPosto = ideTipoEquipamentoEntornoPosto;
		this.dscTipoEquipamentoEntornoPosto = dscTipoEquipamentoEntornoPosto;
	}

	public Integer getIdeTipoEquipamentoEntornoPosto() {
		return ideTipoEquipamentoEntornoPosto;
	}

	public void setIdeTipoEquipamentoEntornoPosto(Integer ideTipoEquipamentoEntornoPosto) {
		this.ideTipoEquipamentoEntornoPosto = ideTipoEquipamentoEntornoPosto;
	}

	public String getDscTipoEquipamentoEntornoPosto() {
		return dscTipoEquipamentoEntornoPosto;
	}

	public void setDscTipoEquipamentoEntornoPosto(String dscTipoEquipamentoEntornoPosto) {
		this.dscTipoEquipamentoEntornoPosto = dscTipoEquipamentoEntornoPosto;
	}

	@XmlTransient
	public Collection<LacPostoCombustivel> getPostoCombustivelCollection() {
		return Util.isNull(postoCombustivelCollection) ? postoCombustivelCollection = new ArrayList<LacPostoCombustivel>() : postoCombustivelCollection;
	}

	public void setPostoCombustivelCollection(Collection<LacPostoCombustivel> postoCombustivelCollection) {
		this.postoCombustivelCollection = postoCombustivelCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dscTipoEquipamentoEntornoPosto == null) ? 0 : dscTipoEquipamentoEntornoPosto.hashCode());
		result = prime * result + ((ideTipoEquipamentoEntornoPosto == null) ? 0 : ideTipoEquipamentoEntornoPosto.hashCode());
		result = prime * result + ((postoCombustivelCollection == null) ? 0 : postoCombustivelCollection.hashCode());
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
		TipoEquipamentoEntornoPosto other = (TipoEquipamentoEntornoPosto) obj;
		if (dscTipoEquipamentoEntornoPosto == null) {
			if (other.dscTipoEquipamentoEntornoPosto != null)
				return false;
		} else if (!dscTipoEquipamentoEntornoPosto.equals(other.dscTipoEquipamentoEntornoPosto))
			return false;
		if (ideTipoEquipamentoEntornoPosto == null) {
			if (other.ideTipoEquipamentoEntornoPosto != null)
				return false;
		} else if (!ideTipoEquipamentoEntornoPosto.equals(other.ideTipoEquipamentoEntornoPosto))
			return false;
		if (postoCombustivelCollection == null) {
			if (other.postoCombustivelCollection != null)
				return false;
		} else if (!postoCombustivelCollection.equals(other.postoCombustivelCollection))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "entity.TipoEquipamentoEntornoPosto[ ideTipoEquipamentoEntornoPosto=" + ideTipoEquipamentoEntornoPosto + " ]";
	}

}
