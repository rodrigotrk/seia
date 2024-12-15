package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "subcategoria_instrumento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_subcategoria_instrumento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubcategoriaInstrumento.findAll", query = "SELECT s FROM SubcategoriaInstrumento s"),
    @NamedQuery(name = "SubcategoriaInstrumento.findByIdeSubcategoriaInstrumento", query = "SELECT s FROM SubcategoriaInstrumento s WHERE s.ideSubcategoriaInstrumento = :ideSubcategoriaInstrumento"),
    @NamedQuery(name = "SubcategoriaInstrumento.findByNomSubcategoriaInstrumento", query = "SELECT s FROM SubcategoriaInstrumento s WHERE s.nomSubcategoriaInstrumento = :nomSubcategoriaInstrumento"),
    @NamedQuery(name = "SubcategoriaInstrumento.findByDtcCriacao", query = "SELECT s FROM SubcategoriaInstrumento s WHERE s.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "SubcategoriaInstrumento.findByIndExcluido", query = "SELECT s FROM SubcategoriaInstrumento s WHERE s.indExcluido = :indExcluido"),
    @NamedQuery(name = "SubcategoriaInstrumento.findByDtcExclusao", query = "SELECT s FROM SubcategoriaInstrumento s WHERE s.dtcExclusao = :dtcExclusao")})
public class SubcategoriaInstrumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_subcategoria_instrumento", nullable = false)
    private Integer ideSubcategoriaInstrumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nom_subcategoria_instrumento", nullable = false, length = 150)
    private String nomSubcategoriaInstrumento;
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
    @JoinColumn(name = "ide_instrumento_controle", referencedColumnName = "ide_instrumento_controle", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InstrumentoControle ideInstrumentoControle;

    public SubcategoriaInstrumento() {
    }

    public SubcategoriaInstrumento(Integer ideSubcategoriaInstrumento) {
        this.ideSubcategoriaInstrumento = ideSubcategoriaInstrumento;
    }

    public SubcategoriaInstrumento(Integer ideSubcategoriaInstrumento, String nomSubcategoriaInstrumento, Date dtcCriacao, boolean indExcluido) {
        this.ideSubcategoriaInstrumento = ideSubcategoriaInstrumento;
        this.nomSubcategoriaInstrumento = nomSubcategoriaInstrumento;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeSubcategoriaInstrumento() {
        return ideSubcategoriaInstrumento;
    }

    public void setIdeSubcategoriaInstrumento(Integer ideSubcategoriaInstrumento) {
        this.ideSubcategoriaInstrumento = ideSubcategoriaInstrumento;
    }

    public String getNomSubcategoriaInstrumento() {
        return nomSubcategoriaInstrumento;
    }

    public void setNomSubcategoriaInstrumento(String nomSubcategoriaInstrumento) {
        this.nomSubcategoriaInstrumento = nomSubcategoriaInstrumento;
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

    public InstrumentoControle getIdeInstrumentoControle() {
        return ideInstrumentoControle;
    }

    public void setIdeInstrumentoControle(InstrumentoControle ideInstrumentoControle) {
        this.ideInstrumentoControle = ideInstrumentoControle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSubcategoriaInstrumento != null ? ideSubcategoriaInstrumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SubcategoriaInstrumento)) {
            return false;
        }
        SubcategoriaInstrumento other = (SubcategoriaInstrumento) object;
        if ((this.ideSubcategoriaInstrumento == null && other.ideSubcategoriaInstrumento != null) || (this.ideSubcategoriaInstrumento != null && !this.ideSubcategoriaInstrumento.equals(other.ideSubcategoriaInstrumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.SubcategoriaInstrumento[ ideSubcategoriaInstrumento=" + ideSubcategoriaInstrumento + " ]";
    }
    
}
