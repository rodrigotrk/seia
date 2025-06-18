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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "pessoa_juridica_cnae")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PessoaJuridicaCnae.findAll", query = "SELECT p FROM PessoaJuridicaCnae p"),
    @NamedQuery(name = "PessoaJuridicaCnae.findByIdePessoaJuridicaCnae", query = "SELECT p FROM PessoaJuridicaCnae p WHERE p.idePessoaJuridicaCnae = :idePessoaJuridicaCnae"),
    @NamedQuery(name = "PessoaJuridicaCnae.exists", query = "SELECT p FROM PessoaJuridicaCnae p WHERE p.ideCnae.ideCnae = :ideCnae and p.idePessoaJuridicaCnae = :idePessoaJuridicaCnae"),
    @NamedQuery(name = "PessoaJuridicaCnae.findByIndCnaePrincipal", query = "SELECT p FROM PessoaJuridicaCnae p WHERE p.indCnaePrincipal = :indCnaePrincipal"),
    @NamedQuery(name = "PessoaJuridicaCnae.findByPessoaJuridica", query = "SELECT p FROM PessoaJuridicaCnae p WHERE p.idePessoaJuridica = :idePessoaJuridica"),
    @NamedQuery(name = "PessoaJuridicaCnae.removeByIdePessoaJuridicaCnae", query = "delete from PessoaJuridicaCnae p where p.idePessoaJuridicaCnae = :idePessoaJuridicaCnae")
})
public class PessoaJuridicaCnae implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PESSOA_JURIDICA_CNAE_IDE_PESSOA_JURIDICA_CNAE_seq")    
    @SequenceGenerator(name="PESSOA_JURIDICA_CNAE_IDE_PESSOA_JURIDICA_CNAE_seq", sequenceName="PESSOA_JURIDICA_CNAE_IDE_PESSOA_JURIDICA_CNAE_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pessoa_juridica_cnae", nullable = false)
    private Integer idePessoaJuridicaCnae;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_cnae_principal", nullable = false)
    private boolean indCnaePrincipal;
    @JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaJuridica idePessoaJuridica;
    @JoinColumn(name = "ide_cnae", referencedColumnName = "ide_cnae", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Cnae ideCnae;

    public PessoaJuridicaCnae() {
    }

    public PessoaJuridicaCnae(Integer idePessoaJuridicaCnae) {
        this.idePessoaJuridicaCnae = idePessoaJuridicaCnae;
    }

    public PessoaJuridicaCnae(Integer idePessoaJuridicaCnae, boolean indCnaePrincipal) {
        this.idePessoaJuridicaCnae = idePessoaJuridicaCnae;
        this.indCnaePrincipal = indCnaePrincipal;
    }

    public Integer getIdePessoaJuridicaCnae() {
        return idePessoaJuridicaCnae;
    }

    public void setIdePessoaJuridicaCnae(Integer idePessoaJuridicaCnae) {
        this.idePessoaJuridicaCnae = idePessoaJuridicaCnae;
    }

    public boolean getIndCnaePrincipal() {
        return indCnaePrincipal;
    }

    public void setIndCnaePrincipal(boolean indCnaePrincipal) {
        this.indCnaePrincipal = indCnaePrincipal;
    }

    public PessoaJuridica getIdePessoaJuridica() {
        return idePessoaJuridica;
    }

    public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
        this.idePessoaJuridica = idePessoaJuridica;
    }

    public Cnae getIdeCnae() {
        return ideCnae;
    }

    public void setIdeCnae(Cnae ideCnae) {
        this.ideCnae = ideCnae;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePessoaJuridicaCnae != null ? idePessoaJuridicaCnae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PessoaJuridicaCnae)) {
            return false;
        }
        PessoaJuridicaCnae other = (PessoaJuridicaCnae) object;
        if ((this.idePessoaJuridicaCnae == null && other.idePessoaJuridicaCnae != null) || (this.idePessoaJuridicaCnae != null && !this.idePessoaJuridicaCnae.equals(other.idePessoaJuridicaCnae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PessoaJuridicaCnae[ idePessoaJuridicaCnae=" + idePessoaJuridicaCnae + " ]";
    }
    
    @Override
    public PessoaJuridicaCnae clone() throws CloneNotSupportedException{
    	return (PessoaJuridicaCnae) super.clone();
    }
}
