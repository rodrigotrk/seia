package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "area_abastecimento_posto_combustivel")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "AreaAbastecimentoPostoCombustivel.findAll", query = "SELECT a FROM AreaAbastecimentoPostoCombustivel a"),
		@NamedQuery(name = "AreaAbastecimentoPostoCombustivel.findByIdeTipoAreaAbastecimento", query = "SELECT a FROM AreaAbastecimentoPostoCombustivel a WHERE a.areaAbastecimentoPostoCombustivelPK.ideTipoAreaAbastecimento = :ideTipoAreaAbastecimento"),
		@NamedQuery(name = "AreaAbastecimentoPostoCombustivel.removerByIdePK", query = "delete FROM AreaAbastecimentoPostoCombustivel a WHERE a.areaAbastecimentoPostoCombustivelPK.ideTipoAreaAbastecimento = :ideTipoAreaAbastecimento and a.areaAbastecimentoPostoCombustivelPK.idePostoCombustivel = :idePostoCombustivel"),
		@NamedQuery(name = "AreaAbastecimentoPostoCombustivel.findByIdePostoCombustivel", query = "SELECT a FROM AreaAbastecimentoPostoCombustivel a WHERE a.areaAbastecimentoPostoCombustivelPK.idePostoCombustivel = :idePostoCombustivel")})
public class AreaAbastecimentoPostoCombustivel implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected AreaAbastecimentoPostoCombustivelPK areaAbastecimentoPostoCombustivelPK;

	@JoinColumn(name = "ide_tipo_permeabilidade_antes_reforma", referencedColumnName = "ide_tipo_permeabilidade", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private TipoPermeabilidade tipoPermeabilidadeAntesReforma;

	@JoinColumn(name = "ide_tipo_permeabilidade_depois_reforma", referencedColumnName = "ide_tipo_permeabilidade", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private TipoPermeabilidade tipoPermeabilidadeDepoisReforma;

	@JoinColumn(name = "ide_tipo_area_abastecimento", referencedColumnName = "ide_tipo_area_abastecimento", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private TipoAreaAbastecimento tipoAreaAbastecimento;

	@JoinColumn(name = "ide_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private LacPostoCombustivel postoCombustivel;

	public AreaAbastecimentoPostoCombustivel() {
	}

	public AreaAbastecimentoPostoCombustivel(AreaAbastecimentoPostoCombustivelPK areaAbastecimentoPostoCombustivelPK) {
		this.areaAbastecimentoPostoCombustivelPK = areaAbastecimentoPostoCombustivelPK;
	}

	public AreaAbastecimentoPostoCombustivelPK getAreaAbastecimentoPostoCombustivelPK() {
		return areaAbastecimentoPostoCombustivelPK;
	}

	public void setAreaAbastecimentoPostoCombustivelPK(AreaAbastecimentoPostoCombustivelPK areaAbastecimentoPostoCombustivelPK) {
		this.areaAbastecimentoPostoCombustivelPK = areaAbastecimentoPostoCombustivelPK;
	}

	public TipoPermeabilidade getTipoPermeabilidadeAntesReforma() {
		return tipoPermeabilidadeAntesReforma;
	}

	public void setTipoPermeabilidadeAntesReforma(TipoPermeabilidade tipoPermeabilidadeAntesReforma) {
		this.tipoPermeabilidadeAntesReforma = tipoPermeabilidadeAntesReforma;
	}

	public TipoPermeabilidade getTipoPermeabilidadeDepoisReforma() {
		return tipoPermeabilidadeDepoisReforma;
	}

	public void setTipoPermeabilidadeDepoisReforma(TipoPermeabilidade tipoPermeabilidadeDepoisReforma) {
		this.tipoPermeabilidadeDepoisReforma = tipoPermeabilidadeDepoisReforma;
	}

	public TipoAreaAbastecimento getTipoAreaAbastecimento() {
		return tipoAreaAbastecimento;
	}

	public void setTipoAreaAbastecimento(TipoAreaAbastecimento tipoAreaAbastecimento) {
		this.tipoAreaAbastecimento = tipoAreaAbastecimento;
	}

	public LacPostoCombustivel getPostoCombustivel() {
		return postoCombustivel;
	}

	public void setPostoCombustivel(LacPostoCombustivel postoCombustivel) {
		this.postoCombustivel = postoCombustivel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaAbastecimentoPostoCombustivelPK == null) ? 0 : areaAbastecimentoPostoCombustivelPK.hashCode());
		result = prime * result + ((postoCombustivel == null) ? 0 : postoCombustivel.hashCode());
		result = prime * result + ((tipoAreaAbastecimento == null) ? 0 : tipoAreaAbastecimento.hashCode());
		result = prime * result + ((tipoPermeabilidadeAntesReforma == null) ? 0 : tipoPermeabilidadeAntesReforma.hashCode());
		result = prime * result + ((tipoPermeabilidadeDepoisReforma == null) ? 0 : tipoPermeabilidadeDepoisReforma.hashCode());
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
		AreaAbastecimentoPostoCombustivel other = (AreaAbastecimentoPostoCombustivel) obj;
		if (areaAbastecimentoPostoCombustivelPK == null) {
			if (other.areaAbastecimentoPostoCombustivelPK != null)
				return false;
		} else if (!areaAbastecimentoPostoCombustivelPK.equals(other.areaAbastecimentoPostoCombustivelPK))
			return false;
		if (postoCombustivel == null) {
			if (other.postoCombustivel != null)
				return false;
		} else if (!postoCombustivel.equals(other.postoCombustivel))
			return false;
		if (tipoAreaAbastecimento == null) {
			if (other.tipoAreaAbastecimento != null)
				return false;
		} else if (!tipoAreaAbastecimento.equals(other.tipoAreaAbastecimento))
			return false;
		if (tipoPermeabilidadeAntesReforma == null) {
			if (other.tipoPermeabilidadeAntesReforma != null)
				return false;
		} else if (!tipoPermeabilidadeAntesReforma.equals(other.tipoPermeabilidadeAntesReforma))
			return false;
		if (tipoPermeabilidadeDepoisReforma == null) {
			if (other.tipoPermeabilidadeDepoisReforma != null)
				return false;
		} else if (!tipoPermeabilidadeDepoisReforma.equals(other.tipoPermeabilidadeDepoisReforma))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "entity.AreaAbastecimentoPostoCombustivel[ areaAbastecimentoPostoCombustivelPK=" + areaAbastecimentoPostoCombustivelPK + " ]";
	}

}
