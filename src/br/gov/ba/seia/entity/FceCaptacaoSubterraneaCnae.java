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

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name="fce_captacao_subterranea_cnae")
@NamedQueries({
    @NamedQuery(name = "FceCaptacaoSubterraneaCnae.findAll", query = "SELECT p FROM FceCaptacaoSubterraneaCnae p"),
    @NamedQuery(name = "FceCaptacaoSubterraneaCnae.findByIdeFceCaptacaoSubterraneaCnae", query = "SELECT p FROM FceCaptacaoSubterraneaCnae p WHERE p.ideFceCaptacaoSubterraneaCnae = :ideFceCaptacaoSubterraneaCnae"),
    @NamedQuery(name = "FceCaptacaoSubterraneaCnae.exists", query = "SELECT p FROM FceCaptacaoSubterraneaCnae p WHERE p.ideCnae.ideCnae = :ideCnae and p.ideFceCaptacaoSubterraneaCnae = :ideFceCaptacaoSubterraneaCnae"),
    @NamedQuery(name = "FceCaptacaoSubterraneaCnae.findByIndCnaePrincipal", query = "SELECT p FROM FceCaptacaoSubterraneaCnae p WHERE p.indCnaePrincipal = :indCnaePrincipal"),
    @NamedQuery(name = "FceCaptacaoSubterraneaCnae.findByFceCaptacaoSubterranea", query = "SELECT p FROM FceCaptacaoSubterraneaCnae p WHERE p.ideFceCaptacaoSubterranea = :ideFceCaptacaoSubterranea"),
    @NamedQuery(name = "FceCaptacaoSubterraneaCnae.removeByIdeFceCaptacaoSubterraneaCnae", query = "delete from FceCaptacaoSubterraneaCnae p where p.ideFceCaptacaoSubterraneaCnae = :ideFceCaptacaoSubterraneaCnae")
})
public class FceCaptacaoSubterraneaCnae implements Cloneable, BaseEntity{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_CAPTACAO_SUBTERRANEA_CNAE_seq")    
    @SequenceGenerator(name="FCE_CAPTACAO_SUBTERRANEA_CNAE_seq", sequenceName="FCE_CAPTACAO_SUBTERRANEA_CNAE_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_fce_captacao_subterranea_cnae", nullable = false)
    private Integer ideFceCaptacaoSubterraneaCnae;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_cnae_principal", nullable = false)
    
    private boolean indCnaePrincipal;
    @JoinColumn(name = "ide_fce_captacao_subterranea", referencedColumnName = "ide_fce_captacao_subterranea", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FceCaptacaoSubterranea ideFceCaptacaoSubterranea;
    
    @JoinColumn(name = "ide_cnae", referencedColumnName = "ide_cnae", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Cnae ideCnae;
    
    public FceCaptacaoSubterraneaCnae() {

    }
    
    public FceCaptacaoSubterraneaCnae(Integer ideFceCaptacaoSubterraneaCnae){
    	this.ideFceCaptacaoSubterraneaCnae = ideFceCaptacaoSubterraneaCnae;
    }
    
    public FceCaptacaoSubterraneaCnae(Integer ideFceCaptacaoSubterraneaCnae, boolean indCnaePrincipal) {
        this.ideFceCaptacaoSubterraneaCnae = ideFceCaptacaoSubterraneaCnae;
        this.indCnaePrincipal = indCnaePrincipal;
    }

	public Integer getIdeFceCaptacaoSubterraneaCnae() {
		return ideFceCaptacaoSubterraneaCnae;
	}

	public void setIdeFceCaptacaoSubterraneaCnae(
			Integer ideFceCaptacaoSubterraneaCnae) {
		this.ideFceCaptacaoSubterraneaCnae = ideFceCaptacaoSubterraneaCnae;
	}

	public boolean isIndCnaePrincipal() {
		return indCnaePrincipal;
	}

	public void setIndCnaePrincipal(boolean indCnaePrincipal) {
		this.indCnaePrincipal = indCnaePrincipal;
	}

	public FceCaptacaoSubterranea getIdeFceCaptacaoSubterranea() {
		return ideFceCaptacaoSubterranea;
	}

	public void setIdeFceCaptacaoSubterranea(
			FceCaptacaoSubterranea ideFceCaptacaoSubterranea) {
		this.ideFceCaptacaoSubterranea = ideFceCaptacaoSubterranea;
	}

	public Cnae getIdeCnae() {
		return ideCnae;
	}

	public void setIdeCnae(Cnae ideCnae) {
		this.ideCnae = ideCnae;
	}
	
    @Override
    public FceCaptacaoSubterraneaCnae clone() throws CloneNotSupportedException{
    	return (FceCaptacaoSubterraneaCnae) super.clone();
    }

	@Override
	public Long getId() {
		return new Long(ideFceCaptacaoSubterraneaCnae);
	}
    
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)){
			return true;
		}
		
		if(obj instanceof FceCaptacaoSubterraneaCnae && this.ideFceCaptacaoSubterraneaCnae != null){
			return this.ideFceCaptacaoSubterraneaCnae.equals(((FceCaptacaoSubterraneaCnae) obj).getIdeFceCaptacaoSubterraneaCnae());
		}
		
		return false;
	}
}
