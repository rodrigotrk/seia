package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name="associacao_assentado_imovel_rural_incra")
@NamedQuery(name="AssociacaoAssentadoImovelRuralIncra.findAll", query="SELECT a FROM AssociacaoAssentadoImovelRuralIncra a")
public class AssociacaoAssentadoImovelRuralIncra implements Serializable {

	private static final long serialVersionUID = 6058816494462028001L;
	
	@Id
	@JoinColumn(name="ide_assentado_incra_imovel_rural", referencedColumnName="ide_assentado_incra_imovel_rural", nullable=false)
	@NotNull
	@ManyToOne(optional=false)
	private AssentadoIncraImovelRural ideAssentadoIncraImovelRural;
	
	@Id
	@JoinColumn(name="ide_associacao_incra_imovel_rural", referencedColumnName="ide_associacao_incra_imovel_rural", nullable=false)
	@NotNull
	@ManyToOne(optional=false)
	private AssociacaoIncraImovelRural ideAssociacaoIncraImovelRural;
	
	@Transient
	private boolean indExcluido;
	
	public AssociacaoAssentadoImovelRuralIncra() {
	}

	public AssentadoIncraImovelRural getIdeAssentadoIncraImovelRural() {
		return ideAssentadoIncraImovelRural;
	}

	public void setIdeAssentadoIncraImovelRural(AssentadoIncraImovelRural ideAssentadoIncraImovelRural) {
		this.ideAssentadoIncraImovelRural = ideAssentadoIncraImovelRural;
	}

	public AssociacaoIncraImovelRural getIdeAssociacaoIncraImovelRural() {
		return ideAssociacaoIncraImovelRural;
	}

	public void setIdeAssociacaoIncraImovelRural(AssociacaoIncraImovelRural ideAssociacaoIncraImovelRural) {
		this.ideAssociacaoIncraImovelRural = ideAssociacaoIncraImovelRural;
	}


	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AssociacaoAssentadoImovelRuralIncra)) {
			return false;
		}
		AssociacaoAssentadoImovelRuralIncra other = (AssociacaoAssentadoImovelRuralIncra) object;
		if (!this.ideAssentadoIncraImovelRural.equals(other.ideAssentadoIncraImovelRural)) {
			return false;
		}
		
		if (!this.ideAssociacaoIncraImovelRural.equals(other.ideAssociacaoIncraImovelRural)) {
			return false;
		}
		return true;
	}

}