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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="fase_assentamento_imovel_rural")
@NamedQuery(name="FaseAssentamentoImovelRural.findAll", query="SELECT f FROM FaseAssentamentoImovelRural f")
public class FaseAssentamentoImovelRural implements Serializable {
	private static final long serialVersionUID = 8544072556554789355L;

	@Id
	@SequenceGenerator(name="FASE_ASSENTAMENTO_IMOVEL_RURAL_SEQ", sequenceName="FASE_ASSENTAMENTO_IMOVEL_RURAL_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FASE_ASSENTAMENTO_IMOVEL_RURAL_SEQ")
	@NotNull
	@Basic(optional=false)
	@Column(name="ide_fase_assentamento_imovel_rural", nullable=false)
	private Integer ideFaseAssentamentoImovelRural;
	
	@NotNull
	@Size(min=1, max=100)
	@Basic(optional=false)
	@Column(name="dsc_fase_assentamento_imovel_rural", nullable=false, length=100)
	private String dscFaseAssentamentoImovelRural;

	public FaseAssentamentoImovelRural() {
	}


	public FaseAssentamentoImovelRural(Integer ideFaseAssentamentoImovelRural) {
		this.ideFaseAssentamentoImovelRural = ideFaseAssentamentoImovelRural;
	}


	public Integer getIdeFaseAssentamentoImovelRural() {
		return this.ideFaseAssentamentoImovelRural;
	}

	public void setIdeFaseAssentamentoImovelRural(Integer ideFaseAssentamentoImovelRural) {
		this.ideFaseAssentamentoImovelRural = ideFaseAssentamentoImovelRural;
	}


	public String getDscFaseAssentamentoImovelRural() {
		return this.dscFaseAssentamentoImovelRural;
	}

	public void setDscFaseAssentamentoImovelRural(String dscFaseAssentamentoImovelRural) {
		this.dscFaseAssentamentoImovelRural = dscFaseAssentamentoImovelRural;
	}

	@Override
	public String toString() {
		return String.valueOf(this.ideFaseAssentamentoImovelRural);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.ideFaseAssentamentoImovelRural != null ? this.ideFaseAssentamentoImovelRural.hashCode() : 0);
		return hash;
    }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof FaseAssentamentoImovelRural)) {
			return false;
		}
		FaseAssentamentoImovelRural other = (FaseAssentamentoImovelRural) object;
		if ((this.ideFaseAssentamentoImovelRural == null && other.ideFaseAssentamentoImovelRural != null)
				|| (this.ideFaseAssentamentoImovelRural != null && !this.ideFaseAssentamentoImovelRural.equals(other.ideFaseAssentamentoImovelRural))) {
			return false;
		}
		return true;
	}
}