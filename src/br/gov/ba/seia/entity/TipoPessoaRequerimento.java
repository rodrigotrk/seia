package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author alex.santos
 */
@Entity
@Table(name = "tipo_pessoa_requerimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPessoaRequerimento.findAll", query = "SELECT t FROM TipoPessoaRequerimento t"),
    @NamedQuery(name = "TipoPessoaRequerimento.findByIdeTipoPessoaRequerimento", query = "SELECT t FROM TipoPessoaRequerimento t WHERE t.ideTipoPessoaRequerimento = :ideTipoPessoaRequerimento"),
    @NamedQuery(name = "TipoPessoaRequerimento.findByNomTipoPessoaRequerimento", query = "SELECT t FROM TipoPessoaRequerimento t WHERE t.nomTipoPessoaRequerimento = :nomTipoPessoaRequerimento")})
public class TipoPessoaRequerimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="TIPO_PESSOA_REQUERIMENTO_IDETIPOPESSOAREQUERIMENTO_GENERATOR", sequenceName="TIPO_PESSOA_REQUERIMENTO_IDE_TIPO_PESSOA_REQUERIMENTO_SEQ")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_PESSOA_REQUERIMENTO_IDETIPOPESSOAREQUERIMENTO_GENERATOR")
    @Column(name="ide_tipo_pessoa_requerimento", unique=true, nullable=false)
    private Integer ideTipoPessoaRequerimento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_tipo_pessoa_requerimento")
    private String nomTipoPessoaRequerimento;
    @Transient
    private Collection<RequerimentoPessoa> requerimentoPessoaCollection;

    public TipoPessoaRequerimento() {
    }

    public TipoPessoaRequerimento(Integer ideTipoPessoaRequerimento) {
        this.ideTipoPessoaRequerimento = ideTipoPessoaRequerimento;
    }

    public TipoPessoaRequerimento(Integer ideTipoPessoaRequerimento, String nomTipoPessoaRequerimento) {
        this.ideTipoPessoaRequerimento = ideTipoPessoaRequerimento;
        this.nomTipoPessoaRequerimento = nomTipoPessoaRequerimento;
    }

    public Integer getIdeTipoPessoaRequerimento() {
        return ideTipoPessoaRequerimento;
    }

    public void setIdeTipoPessoaRequerimento(Integer ideTipoPessoaRequerimento) {
        this.ideTipoPessoaRequerimento = ideTipoPessoaRequerimento;
    }

    public String getNomTipoPessoaRequerimento() {
        return nomTipoPessoaRequerimento;
    }

    public void setNomTipoPessoaRequerimento(String nomTipoPessoaRequerimento) {
        this.nomTipoPessoaRequerimento = nomTipoPessoaRequerimento;
    }

    @XmlTransient
    public Collection<RequerimentoPessoa> getRequerimentoPessoaCollection() {
        return requerimentoPessoaCollection;
    }

    public void setRequerimentoPessoaCollection(Collection<RequerimentoPessoa> requerimentoPessoaCollection) {
        this.requerimentoPessoaCollection = requerimentoPessoaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoPessoaRequerimento != null ? ideTipoPessoaRequerimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoPessoaRequerimento)) {
            return false;
        }
        TipoPessoaRequerimento other = (TipoPessoaRequerimento) object;
        if ((this.ideTipoPessoaRequerimento == null && other.ideTipoPessoaRequerimento != null) || (this.ideTipoPessoaRequerimento != null && !this.ideTipoPessoaRequerimento.equals(other.ideTipoPessoaRequerimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoPessoaRequerimento[ ideTipoPessoaRequerimento=" + ideTipoPessoaRequerimento + " ]";
    }
    
}
