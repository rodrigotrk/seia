package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "imovel_rural_abastecimento", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"ide_imovel_rural_abastecimento"})})
@NamedQueries({
	@NamedQuery(name = "ImovelRuralAbastecimento.findByAll", query = "SELECT i FROM ImovelRuralAbastecimento i"),
	@NamedQuery(name = "ImovelRuralAbastecimento.findByIdeImovelRuralAbastecimento", query = "SELECT i FROM ImovelRuralAbastecimento i WHERE i.ideImovelRuralAbastecimento = :ideImovelRuralAbastecimento"),
	@NamedQuery(name = "ImovelRuralAbastecimento.findByIndConcessionaria", query = "SELECT i FROM ImovelRuralAbastecimento i WHERE i.indConcessionaria = :indConcessionaria"),
	@NamedQuery(name = "ImovelRuralAbastecimento.findByIndPrecipitacao", query = "SELECT i FROM ImovelRuralAbastecimento i WHERE i.indPrecipitacao = :indPrecipitacao"),
	@NamedQuery(name = "ImovelRuralAbastecimento.findByIndSuperficial", query = "SELECT i FROM ImovelRuralAbastecimento i WHERE i.indSuperficial = :indSuperficial"),
	@NamedQuery(name = "ImovelRuralAbastecimento.findByIndSubterraneo", query = "SELECT i FROM ImovelRuralAbastecimento i WHERE i.indSubterraneo = :indSubterraneo"),
	@NamedQuery(name = "ImovelRuralAbastecimento.findByVazSuperficial", query = "SELECT i FROM ImovelRuralAbastecimento i WHERE i.vazSuperficial = :volSuperficial"),
	@NamedQuery(name = "ImovelRuralAbastecimento.findByVazSubterraneo", query = "SELECT i FROM ImovelRuralAbastecimento i WHERE i.vazSubterraneo = :volSubterraneo")})
public class ImovelRuralAbastecimento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMOVEL_RURAL_ABASTECIMENTO_SEQ") 
    @SequenceGenerator(name="IMOVEL_RURAL_ABASTECIMENTO_SEQ", sequenceName="IMOVEL_RURAL_ABASTECIMENTO_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_rural_abastecimento", nullable = false)
	private Integer ideImovelRuralAbastecimento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_concessionaria")
    private Boolean indConcessionaria;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_precipitacao")
    private Boolean indPrecipitacao;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_superficial")
    private Boolean indSuperficial;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_subterraneo")
    private Boolean indSubterraneo;

    @Basic(optional = false)
    @Column(name = "vaz_superficial", precision = 14, scale = 2)
    private Double vazSuperficial;

    @Basic(optional = false)
    @Column(name = "vaz_subterraneo", precision = 14, scale = 2)
    private Double vazSubterraneo;

    @JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private ImovelRural ideImovelRural;

	public ImovelRuralAbastecimento() {
	}

	public ImovelRuralAbastecimento(Integer ideImovelRuralAbastecimento) {
		this.ideImovelRuralAbastecimento = ideImovelRuralAbastecimento;
	}

	public ImovelRuralAbastecimento(Integer ideImovelRuralAbastecimento, boolean indConcessionaria, boolean indPrecipitacao, boolean indSubterraneo, boolean indSuperficial, Double vazSuperficial, Double vazSubterraneo) {
		this.ideImovelRuralAbastecimento = ideImovelRuralAbastecimento;
		this.indConcessionaria = indConcessionaria;
		this.indPrecipitacao = indPrecipitacao;
		this.indSuperficial = indSuperficial;
		this.indSubterraneo = indSubterraneo;
		this.vazSuperficial = vazSuperficial;
		this.vazSubterraneo = vazSubterraneo;
	}

	public Integer getIdeImovelRuralAbastecimento() {
		return ideImovelRuralAbastecimento;
	}

	public void setIdeImovelRuralAbastecimento(Integer ideImovelRuralAbastecimento) {
		this.ideImovelRuralAbastecimento = ideImovelRuralAbastecimento;
	}

	public boolean isIndConcessionaria() {
		return indConcessionaria;
	}

	public void setIndConcessionaria(boolean indConcessionaria) {
		this.indConcessionaria = indConcessionaria;
	}

	public boolean isIndPrecipitacao() {
		return indPrecipitacao;
	}

	public void setIndPrecipitacao(boolean indPrecipitacao) {
		this.indPrecipitacao = indPrecipitacao;
	}

	public boolean isIndSuperficial() {
		return indSuperficial;
	}

	public void setIndSuperficial(boolean indSuperficial) {
		this.indSuperficial = indSuperficial;
	}

	public boolean isIndSubterraneo() {
		return indSubterraneo;
	}

	public void setIndSubterraneo(boolean indSubterraneo) {
		this.indSubterraneo = indSubterraneo;
	}

	public Double getVazSuperficial() {
		return vazSuperficial;
	}

	public void setVazSuperficial(Double vazSuperficial) {
		this.vazSuperficial = vazSuperficial;
	}

	public Double getVazSubterraneo() {
		return vazSubterraneo;
	}

	public void setVazSubterraneo(Double vazSubterraneo) {
		this.vazSubterraneo = vazSubterraneo;
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
        hash += (ideImovelRuralAbastecimento != null ? ideImovelRuralAbastecimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaLegal)) {
            return false;
        }
        ImovelRuralAbastecimento other = (ImovelRuralAbastecimento) object;
        if ((this.ideImovelRuralAbastecimento == null && other.ideImovelRuralAbastecimento != null) || (this.ideImovelRuralAbastecimento != null && !this.ideImovelRuralAbastecimento.equals(other.ideImovelRuralAbastecimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideImovelRuralAbastecimento);
    }
  
}
