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
@Table(name = "imovel_rural_prad")
@NamedQueries({
	@NamedQuery(name = "ImovelRuralPrad.findByAll", query = "SELECT p FROM ImovelRuralPrad p"),
	@NamedQuery(name = "ImovelRuralPrad.findByIdeImovelRuralPrad", query = "SELECT p FROM ImovelRuralPrad p WHERE p.ideImovelRuralPrad = :ideImovelRuralPrad"),
	@NamedQuery(name = "ImovelRuralPrad.findByIdeImovelRural", query = "SELECT p FROM ImovelRuralPrad p WHERE p.ideImovelRural = :ideImovelRural")})
public class ImovelRuralPrad implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMOVEL_RURAL_PRAD_IDE_IMOVEL_RURAL_PRAD_SEQ") 
    @SequenceGenerator(name="IMOVEL_RURAL_PRAD_IDE_IMOVEL_RURAL_PRAD_SEQ", sequenceName="IMOVEL_RURAL_PRAD_IDE_IMOVEL_RURAL_PRAD_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_rural_prad", nullable = false)
	private Integer ideImovelRuralPrad;
	
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
        

    public ImovelRuralPrad(){
    	
    }
    
    public ImovelRuralPrad(Integer ideImovelRuralPrad){
    	this.ideImovelRuralPrad = ideImovelRuralPrad;
    }
    
    public ImovelRuralPrad(Integer ideImovelRuralPrad, String dscOrgaoEmissor, Date dtcAssinatura, Date dtcEncerramento){
    	this.ideImovelRuralPrad = ideImovelRuralPrad;
    	this.dscOrgaoEmissor = dscOrgaoEmissor;
    	this.dtcAssinatura = dtcAssinatura;
    	this.dtcEncerramento = dtcEncerramento;
    }
	
    public Integer getIdeImovelRuralPrad() {
		return ideImovelRuralPrad;
	}

	public void setIdeImovelRuralPrad(Integer ideImovelRuralPrad) {
		this.ideImovelRuralPrad = ideImovelRuralPrad;
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
        hash += (ideImovelRuralPrad != null ? ideImovelRuralPrad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ImovelRuralPrad)) {
            return false;
        }
        ImovelRuralPrad other = (ImovelRuralPrad) object;
        if ((this.ideImovelRuralPrad == null && other.ideImovelRuralPrad != null) || (this.ideImovelRuralPrad != null && !this.ideImovelRuralPrad.equals(other.ideImovelRuralPrad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return "entity.ImovelRuralPrad[ ideImovelRuralPrad=" + ideImovelRuralPrad + " ]";
    }

}