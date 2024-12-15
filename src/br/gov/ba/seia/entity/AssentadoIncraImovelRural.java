package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="assentado_incra_imovel_rural")
@NamedQuery(name="AssentadoIncraImovelRural.findAll", query="SELECT a FROM AssentadoIncraImovelRural a")
public class AssentadoIncraImovelRural implements Serializable {

	private static final long serialVersionUID = 4092980935560880291L;
	
	@Id
	@SequenceGenerator(name="ASSENTADO_INCRA_IMOVEL_RURAL_SEQ", sequenceName="ASSENTADO_INCRA_IMOVEL_RURAL_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ASSENTADO_INCRA_IMOVEL_RURAL_SEQ")
	@NotNull
	@Basic(optional=false)
	@Column(name="ide_assentado_incra_imovel_rural", nullable=false)
	private Integer ideAssentadoIncraImovelRural;
	
	@JoinColumn(name="ide_imovel_rural", referencedColumnName="ide_imovel_rural", nullable=false)
	@NotNull
	@ManyToOne(optional=false)
	private ImovelRural ideImovelRural;
	
	@JoinColumn(name="ide_assentado_incra", referencedColumnName="ide_assentado_incra", nullable=false)
	@NotNull
	@ManyToOne(optional=false)
	private AssentadoIncra ideAssentadoIncra;
	
	@Column(name = "gdc_latitude")
	private Double latitude;

	@Column(name = "gdc_longitude")
	private Double longitude;
	
	@OneToMany(mappedBy = "ideAssentadoIncraImovelRural", fetch = FetchType.LAZY)	
	private Collection<AssociacaoAssentadoImovelRuralIncra> associacaoAssentadoImovelRuralIncraCollection;
	
	@Transient
	private boolean indEdicao;

	@Transient
	private boolean indExcluido;
	
	@Transient
	private boolean indVisualizacao;
	
	public AssentadoIncraImovelRural() {
	}
	
	public AssentadoIncraImovelRural(AssentadoIncra ideAssentadoIncra, ImovelRural ideImovelRural) {
		this.ideAssentadoIncra = ideAssentadoIncra;
		this.ideImovelRural = ideImovelRural;
	}


	public Integer getIdeAssentadoIncraImovelRural() {
		return this.ideAssentadoIncraImovelRural;
	}

	public void setIdeAssentadoIncraImovelRural(Integer ideAssentadoIncraImovelRural) {
		this.ideAssentadoIncraImovelRural = ideAssentadoIncraImovelRural;
	}


	public ImovelRural getIdeImovelRural() {
		return this.ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}


	public AssentadoIncra getIdeAssentadoIncra() {
		return this.ideAssentadoIncra;
	}

	public void setIdeAssentadoIncra(AssentadoIncra assentadoIncra) {
		this.ideAssentadoIncra = assentadoIncra;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.ideAssentadoIncraImovelRural);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.ideAssentadoIncraImovelRural != null ? this.ideAssentadoIncraImovelRural.hashCode() : 0);
		return hash;
    }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AssentadoIncraImovelRural)) {
			return false;
		}
		AssentadoIncraImovelRural other = (AssentadoIncraImovelRural) object;
		if ((this.ideAssentadoIncraImovelRural == null && other.ideAssentadoIncraImovelRural != null)
				|| (this.ideAssentadoIncraImovelRural != null && !this.ideAssentadoIncraImovelRural.equals(other.ideAssentadoIncraImovelRural))) {
			return false;
		}
		return true;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public Collection<AssociacaoAssentadoImovelRuralIncra> getAssociacaoAssentadoImovelRuralIncraCollection() {
		return associacaoAssentadoImovelRuralIncraCollection;
	}

	public void setAssociacaoAssentadoImovelRuralIncraCollection(
			Collection<AssociacaoAssentadoImovelRuralIncra> associacaoAssentadoImovelRuralIncraCollection) {
		this.associacaoAssentadoImovelRuralIncraCollection = associacaoAssentadoImovelRuralIncraCollection;
	}
	
	public boolean isIndEdicao() {
		return indEdicao;
	}


	public void setIndEdicao(boolean indEdicao) {
		this.indEdicao = indEdicao;
	}


	public boolean isIndExcluido() {
		return indExcluido;
	}


	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}


	public boolean isIndVisualizacao() {
		return indVisualizacao;
	}


	public void setIndVisualizacao(boolean indVisualizacao) {
		this.indVisualizacao = indVisualizacao;
	}

}