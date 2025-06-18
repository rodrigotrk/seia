package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "instrumento_controle", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sgl_instrumento_controle"}),
    @UniqueConstraint(columnNames = {"nom_instrumento_controle"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstrumentoControle.findAll", query = "SELECT i FROM InstrumentoControle i"),
    @NamedQuery(name = "InstrumentoControle.findByIdeInstrumentoControle", query = "SELECT i FROM InstrumentoControle i WHERE i.ideInstrumentoControle = :ideInstrumentoControle"),
    @NamedQuery(name = "InstrumentoControle.findBySglInstrumentoControle", query = "SELECT i FROM InstrumentoControle i WHERE i.sglInstrumentoControle = :sglInstrumentoControle"),
    @NamedQuery(name = "InstrumentoControle.findByNomInstrumentoControle", query = "SELECT i FROM InstrumentoControle i WHERE i.nomInstrumentoControle = :nomInstrumentoControle"),
    @NamedQuery(name = "InstrumentoControle.findByDtcCriacao", query = "SELECT i FROM InstrumentoControle i WHERE i.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "InstrumentoControle.findByIndExcluido", query = "SELECT i FROM InstrumentoControle i WHERE i.indExcluido = :indExcluido"),
    @NamedQuery(name = "InstrumentoControle.findByDtcExclusao", query = "SELECT i FROM InstrumentoControle i WHERE i.dtcExclusao = :dtcExclusao")})
public class InstrumentoControle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_instrumento_controle", nullable = false)
    private Integer ideInstrumentoControle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "sgl_instrumento_controle", nullable = false, length = 10)
    private String sglInstrumentoControle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nom_instrumento_controle", nullable = false, length = 200)
    private String nomInstrumentoControle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideInstrumentoControle", fetch = FetchType.LAZY)
    private Collection<InstrumentoPorte> instrumentoPorteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideInstrumentoControle", fetch = FetchType.LAZY)
    private Collection<SubcategoriaInstrumento> subcategoriaInstrumentoCollection;

    public InstrumentoControle() {
    }

    public InstrumentoControle(Integer ideInstrumentoControle) {
        this.ideInstrumentoControle = ideInstrumentoControle;
    }

    public InstrumentoControle(Integer ideInstrumentoControle, String sglInstrumentoControle, String nomInstrumentoControle, Date dtcCriacao, boolean indExcluido) {
        this.ideInstrumentoControle = ideInstrumentoControle;
        this.sglInstrumentoControle = sglInstrumentoControle;
        this.nomInstrumentoControle = nomInstrumentoControle;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeInstrumentoControle() {
        return ideInstrumentoControle;
    }

    public void setIdeInstrumentoControle(Integer ideInstrumentoControle) {
        this.ideInstrumentoControle = ideInstrumentoControle;
    }

    public String getSglInstrumentoControle() {
        return sglInstrumentoControle;
    }

    public void setSglInstrumentoControle(String sglInstrumentoControle) {
        this.sglInstrumentoControle = sglInstrumentoControle;
    }

    public String getNomInstrumentoControle() {
        return nomInstrumentoControle;
    }

    public void setNomInstrumentoControle(String nomInstrumentoControle) {
        this.nomInstrumentoControle = nomInstrumentoControle;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    @XmlTransient
    public Collection<InstrumentoPorte> getInstrumentoPorteCollection() {
        return instrumentoPorteCollection;
    }

    public void setInstrumentoPorteCollection(Collection<InstrumentoPorte> instrumentoPorteCollection) {
        this.instrumentoPorteCollection = instrumentoPorteCollection;
    }

    @XmlTransient
    public Collection<SubcategoriaInstrumento> getSubcategoriaInstrumentoCollection() {
        return subcategoriaInstrumentoCollection;
    }

    public void setSubcategoriaInstrumentoCollection(Collection<SubcategoriaInstrumento> subcategoriaInstrumentoCollection) {
        this.subcategoriaInstrumentoCollection = subcategoriaInstrumentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideInstrumentoControle != null ? ideInstrumentoControle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof InstrumentoControle)) {
            return false;
        }
        InstrumentoControle other = (InstrumentoControle) object;
        if ((this.ideInstrumentoControle == null && other.ideInstrumentoControle != null) || (this.ideInstrumentoControle != null && !this.ideInstrumentoControle.equals(other.ideInstrumentoControle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.InstrumentoControle[ ideInstrumentoControle=" + ideInstrumentoControle + " ]";
    }
    
}
