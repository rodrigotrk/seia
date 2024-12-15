package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "imovel_rural_tac")
@NamedQueries({
	@NamedQuery(name = "ImovelRuralTac.findByAll", query = "SELECT t FROM ImovelRuralTac t"),
	@NamedQuery(name = "ImovelRuralTac.findByIdeImovelRuralTac", query = "SELECT t FROM ImovelRuralTac t WHERE t.ideImovelRuralTac = :ideImovelRuralTac"),
	@NamedQuery(name = "ImovelRuralTac.findByIdeImovelRural", query = "SELECT t FROM ImovelRuralTac t WHERE t.ideImovelRural = :ideImovelRural")})
public class ImovelRuralTac implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMOVEL_RURAL_TAC_IDE_IMOVEL_RURAL_TAC_SEQ") 
    @SequenceGenerator(name="IMOVEL_RURAL_TAC_IDE_IMOVEL_RURAL_TAC_SEQ", sequenceName="IMOVEL_RURAL_TAC_IDE_IMOVEL_RURAL_TAC_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_rural_tac", nullable = false)
	private Integer ideImovelRuralTac;
	
	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural")
	@OneToOne(optional = false)
	private ImovelRural ideImovelRural;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "dsc_orgao_emissor", length = 100, nullable = false)
    private String dscOrgaoEmissor;
    
    @Basic(optional = false)
	@Column(name = "dtc_assinatura", nullable = true)
	private Date dtcAssinatura;
    
    @Basic(optional = false)
	@Column(name = "dtc_encerramento", nullable = true)
	private Date dtcEncerramento;
        

    public ImovelRuralTac(){
    	
    }
    
    public ImovelRuralTac(Integer ideImovelRuralTac){
    	this.ideImovelRuralTac = ideImovelRuralTac;
    }
    
    public ImovelRuralTac(Integer ideImovelRuralTac, String dscOrgaoEmissor, Date dtcAssinatura, Date dtcEncerramento){
    	this.ideImovelRuralTac = ideImovelRuralTac;
    	this.dscOrgaoEmissor = dscOrgaoEmissor;
    	this.dtcAssinatura = dtcAssinatura;
    	this.dtcEncerramento = dtcEncerramento;
    }
	
    public Integer getIdeImovelRuralTac() {
		return ideImovelRuralTac;
	}

	public void setIdeImovelRuralTac(Integer ideImovelRuralTac) {
		this.ideImovelRuralTac = ideImovelRuralTac;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public String getDscOrgaoEmissor() {
		return dscOrgaoEmissor;
	}

	public void setDscOrgaoEmissor(String dscOrgaoEmissor) {
		this.dscOrgaoEmissor = dscOrgaoEmissor;
	}

	public Date getDtcAssinatura() {
		return dtcAssinatura;
	}

	public void setDtcAssinatura(Date dtcAssinatura) {
		this.dtcAssinatura = dtcAssinatura;
	}

	public Date getDtcEncerramento() {
		return dtcEncerramento;
	}

	public void setDtcEncerramento(Date dtcEncerramento) {
		this.dtcEncerramento = dtcEncerramento;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideImovelRuralTac != null ? ideImovelRuralTac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ImovelRuralTac)) {
            return false;
        }
        ImovelRuralTac other = (ImovelRuralTac) object;
        if ((this.ideImovelRuralTac == null && other.ideImovelRuralTac != null) || (this.ideImovelRuralTac != null && !this.ideImovelRuralTac.equals(other.ideImovelRuralTac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return "entity.ImovelRuralTac[ ideImovelRuralTac=" + ideImovelRuralTac + " ]";
    }

}