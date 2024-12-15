package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="associacao_incra_imovel_rural")
@NamedQuery(name="AssociacaoIncraImovelRural.findAll", query="SELECT a FROM AssociacaoIncraImovelRural a")
public class AssociacaoIncraImovelRural implements Serializable {
	
	private static final long serialVersionUID = 2497991788315052688L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ASSOCIACAO_INCRA_IMOVEL_RURAL_SEQ")
	@SequenceGenerator(name="ASSOCIACAO_INCRA_IMOVEL_RURAL_SEQ", sequenceName= "ASSOCIACAO_INCRA_IMOVEL_RURAL_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_associacao_incra_imovel_rural", nullable=false)
	private Integer ideAssociacaoIncraImovelRural;

	@NotNull
	@JoinColumn(name="ide_associacao_incra", referencedColumnName = "ide_associacao_incra", nullable = false)
	@ManyToOne(optional = false)
	private AssociacaoIncra ideAssociacaoIncra;
	
	@JoinColumn(name="ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@ManyToOne(optional = false)
	private ImovelRural ideImovelRural;

	public AssociacaoIncraImovelRural() {
	}
	
	public AssociacaoIncraImovelRural(AssociacaoIncra ideAssociacaoIncra, ImovelRural ideImovelRural) {
		this.ideAssociacaoIncra = ideAssociacaoIncra;
		this.ideImovelRural = ideImovelRural;
	}

	public Integer getIdeAssociacaoIncraImovelRural() {
		return this.ideAssociacaoIncraImovelRural;
	}

	public void setIdeAssociacaoIncraImovelRural(Integer ideAssociacaoIncraImovelRural) {
		this.ideAssociacaoIncraImovelRural = ideAssociacaoIncraImovelRural;
	}


	public AssociacaoIncra getIdeAssociacaoIncra() {
		return this.ideAssociacaoIncra;
	}

	public void setIdeAssociacaoIncra(AssociacaoIncra ideAssociacaoIncra) {
		this.ideAssociacaoIncra = ideAssociacaoIncra;
	}


	public ImovelRural getIdeImovelRural() {
		return this.ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}
	
	   @Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (ideAssociacaoIncraImovelRural != null ? ideAssociacaoIncraImovelRural.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        if (!(object instanceof AssociacaoIncraImovelRural)) {
	            return false;
	        }
	        AssociacaoIncraImovelRural other = (AssociacaoIncraImovelRural) object;
	        if ((this.ideAssociacaoIncraImovelRural == null && other.ideAssociacaoIncraImovelRural != null) || (this.ideAssociacaoIncraImovelRural != null && !this.ideAssociacaoIncraImovelRural.equals(other.ideAssociacaoIncraImovelRural))) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	    	return String.valueOf(ideAssociacaoIncraImovelRural);
	    }
}