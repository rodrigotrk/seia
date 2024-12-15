package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "supressao_vegetacao")
@NamedQueries({
	@NamedQuery(name = "SupressaoVegetacao.findByAll", query = "SELECT s FROM SupressaoVegetacao s"),
	@NamedQuery(name = "SupressaoVegetacao.findByIdeSupressaoVegetacao", query = "SELECT s FROM SupressaoVegetacao s WHERE s.ideSupressaoVegetacao = :ideSupressaoVegetacao"),
	@NamedQuery(name = "SupressaoVegetacao.findByValArea", query = "SELECT s FROM SupressaoVegetacao s WHERE s.valArea = :valArea"),
	@NamedQuery(name = "SupressaoVegetacao.findByIndProcesso", query = "SELECT s FROM SupressaoVegetacao s WHERE s.indProcesso = :indProcesso"),
	@NamedQuery(name = "SupressaoVegetacao.findByNumProcesso", query = "SELECT s FROM SupressaoVegetacao s WHERE s.numProcesso = :numProcesso"),
	@NamedQuery(name = "SupressaoVegetacao.findByIdeImovelRural", query = "SELECT s FROM SupressaoVegetacao s WHERE s.ideImovelRural = :ideImovelRural")})
public class SupressaoVegetacao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SUPRESSAO_VEGETACAO_IDE_SUPRESSAO_VEGETACAO_SEQ") 
    @SequenceGenerator(name="SUPRESSAO_VEGETACAO_IDE_SUPRESSAO_VEGETACAO_SEQ", sequenceName="SUPRESSAO_VEGETACAO_IDE_SUPRESSAO_VEGETACAO_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_supressao_vegetacao", nullable = false)
	private Integer ideSupressaoVegetacao;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_area", nullable = false, precision = 14, scale = 2)
    private Double valArea;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_processo", nullable = false)
    private boolean indProcesso;

    @Column(name = "num_processo", length = 30)
    private String numProcesso;

    @JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural")
    @OneToOne(optional = false)
    private ImovelRural ideImovelRural;

    public SupressaoVegetacao(){
    	
    }
    
    public SupressaoVegetacao(Integer ideSupressaoVegetacao){
    	this.ideSupressaoVegetacao = ideSupressaoVegetacao;
    }
    
    public SupressaoVegetacao(Integer ideSupressaoVegetacao, Double valArea, boolean indProcesso, String numProcesso){
    	this.ideSupressaoVegetacao = ideSupressaoVegetacao;
    	this.valArea = valArea;
    	this.indProcesso = indProcesso;
    	this.numProcesso = numProcesso;
    }

	public Integer getIdeSupressaoVegetacao() {
		return ideSupressaoVegetacao;
	}

	public void setIdeSupressaoVegetacao(Integer ideSupressaoVegetacao) {
		this.ideSupressaoVegetacao = ideSupressaoVegetacao;
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	public boolean isIndProcesso() {
		return indProcesso;
	}

	public void setIndProcesso(boolean indProcesso) {
		this.indProcesso = indProcesso;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSupressaoVegetacao != null ? ideSupressaoVegetacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SupressaoVegetacao)) {
            return false;
        }
        SupressaoVegetacao other = (SupressaoVegetacao) object;
        if ((this.ideSupressaoVegetacao == null && other.ideSupressaoVegetacao != null) || (this.ideSupressaoVegetacao != null && !this.ideSupressaoVegetacao.equals(other.ideSupressaoVegetacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return "entity.SupressaoVegetacao[ ideSupressaoVegetacao=" + ideSupressaoVegetacao + " ]";
    }

}