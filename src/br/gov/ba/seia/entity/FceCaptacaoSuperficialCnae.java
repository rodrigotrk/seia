package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="fce_captacao_superficial_cnae")
@NamedQueries({
    @NamedQuery(name = "FceCaptacaoSuperficialCnae.findAll", query = "SELECT p FROM FceCaptacaoSuperficialCnae p"),
    @NamedQuery(name = "FceCaptacaoSuperficialCnae.findByIdeFceCaptacaoSuperficialCnae", query = "SELECT p FROM FceCaptacaoSuperficialCnae p WHERE p.ideFceCaptacaoSuperficialCnae = :ideFceCaptacaoSuperficialCnae"),
    @NamedQuery(name = "FceCaptacaoSuperficialCnae.exists", query = "SELECT p FROM FceCaptacaoSuperficialCnae p WHERE p.ideCnae.ideCnae = :ideCnae and p.ideFceCaptacaoSuperficialCnae = :ideFceCaptacaoSuperficialCnae"),
    @NamedQuery(name = "FceCaptacaoSuperficialCnae.findByIndCnaePrincipal", query = "SELECT p FROM FceCaptacaoSuperficialCnae p WHERE p.indCnaePrincipal = :indCnaePrincipal"),
    @NamedQuery(name = "FceCaptacaoSuperficialCnae.findByFceCaptacaoSuperifical", query = "SELECT p FROM FceCaptacaoSuperficialCnae p WHERE p.ideFceCaptacaoSupercial = :ideFceCaptacaoSupercial"),
    @NamedQuery(name = "FceCaptacaoSuperficialCnae.removeByIdeFceCaptacaoSuperficialCnae", query = "delete from FceCaptacaoSuperficialCnae p where p.ideFceCaptacaoSuperficialCnae = :ideFceCaptacaoSuperficialCnae")
})
public class FceCaptacaoSuperficialCnae implements Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_CAPTACAO_SUPERFICIAL_CNAE_seq")    
    @SequenceGenerator(name="FCE_CAPTACAO_SUPERFICIAL_CNAE_seq", sequenceName="FCE_CAPTACAO_SUPERFICIAL_CNAE_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_fce_captacao_superficial_cnae", nullable = false)
    private Integer ideFceCaptacaoSuperficialCnae;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_cnae_principal", nullable = false)
    
    private boolean indCnaePrincipal;
    @JoinColumn(name = "ide_fce_captacao_superficial", referencedColumnName = "ide_fce_captacao_superficial", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FceCaptacaoSuperficial ideFceCaptacaoSupercial;
    
    @JoinColumn(name = "ide_cnae", referencedColumnName = "ide_cnae", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Cnae ideCnae;
    
    public FceCaptacaoSuperficialCnae() {

    }
    
    public FceCaptacaoSuperficialCnae(Integer ideFceCaptacaoSuperficialCnae){
    	this.ideFceCaptacaoSuperficialCnae = ideFceCaptacaoSuperficialCnae;
    }
    
    public FceCaptacaoSuperficialCnae(Integer ideFceCaptacaoSuperficialCnae, boolean indCnaePrincipal) {
        this.ideFceCaptacaoSuperficialCnae = ideFceCaptacaoSuperficialCnae;
        this.indCnaePrincipal = indCnaePrincipal;
    }

	public Integer getIdeFceCaptacaoSuperficialCnae() {
		return ideFceCaptacaoSuperficialCnae;
	}

	public void setIdeFceCaptacaoSuperficialCnae(
			Integer ideFceCaptacaoSuperficialCnae) {
		this.ideFceCaptacaoSuperficialCnae = ideFceCaptacaoSuperficialCnae;
	}

	public boolean isIndCnaePrincipal() {
		return indCnaePrincipal;
	}

	public void setIndCnaePrincipal(boolean indCnaePrincipal) {
		this.indCnaePrincipal = indCnaePrincipal;
	}

	public FceCaptacaoSuperficial getIdeFceCaptacaoSupercial() {
		return ideFceCaptacaoSupercial;
	}

	public void setIdeFceCaptacaoSupercial(
			FceCaptacaoSuperficial ideFceCaptacaoSupercial) {
		this.ideFceCaptacaoSupercial = ideFceCaptacaoSupercial;
	}

	public Cnae getIdeCnae() {
		return ideCnae;
	}

	public void setIdeCnae(Cnae ideCnae) {
		this.ideCnae = ideCnae;
	}
	
    @Override
    public FceCaptacaoSuperficialCnae clone() throws CloneNotSupportedException{
    	return (FceCaptacaoSuperficialCnae) super.clone();
    }
    
}
