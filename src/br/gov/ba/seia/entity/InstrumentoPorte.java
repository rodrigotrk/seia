package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "instrumento_porte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstrumentoPorte.findAll", query = "SELECT i FROM InstrumentoPorte i"),
    @NamedQuery(name = "InstrumentoPorte.findByIdeInstrumentoPorte", query = "SELECT i FROM InstrumentoPorte i WHERE i.ideInstrumentoPorte = :ideInstrumentoPorte"),
    @NamedQuery(name = "InstrumentoPorte.findByValInstrumentoPorte", query = "SELECT i FROM InstrumentoPorte i WHERE i.valInstrumentoPorte = :valInstrumentoPorte")})
public class InstrumentoPorte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_instrumento_porte", nullable = false)
    private Integer ideInstrumentoPorte;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_instrumento_porte", nullable = false, precision = 14, scale = 2)
    private BigDecimal valInstrumentoPorte;
    @JoinColumn(name = "ide_porte", referencedColumnName = "ide_porte", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Porte idePorte;
    @JoinColumn(name = "ide_instrumento_controle", referencedColumnName = "ide_instrumento_controle", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InstrumentoControle ideInstrumentoControle;

    public InstrumentoPorte() {
    }

    public InstrumentoPorte(Integer ideInstrumentoPorte) {
        this.ideInstrumentoPorte = ideInstrumentoPorte;
    }

    public InstrumentoPorte(Integer ideInstrumentoPorte, BigDecimal valInstrumentoPorte) {
        this.ideInstrumentoPorte = ideInstrumentoPorte;
        this.valInstrumentoPorte = valInstrumentoPorte;
    }

    public Integer getIdeInstrumentoPorte() {
        return ideInstrumentoPorte;
    }

    public void setIdeInstrumentoPorte(Integer ideInstrumentoPorte) {
        this.ideInstrumentoPorte = ideInstrumentoPorte;
    }

    public BigDecimal getValInstrumentoPorte() {
        return valInstrumentoPorte;
    }

    public void setValInstrumentoPorte(BigDecimal valInstrumentoPorte) {
        this.valInstrumentoPorte = valInstrumentoPorte;
    }

    public Porte getIdePorte() {
        return idePorte;
    }

    public void setIdePorte(Porte idePorte) {
        this.idePorte = idePorte;
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
        hash += (ideInstrumentoPorte != null ? ideInstrumentoPorte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof InstrumentoPorte)) {
            return false;
        }
        InstrumentoPorte other = (InstrumentoPorte) object;
        if ((this.ideInstrumentoPorte == null && other.ideInstrumentoPorte != null) || (this.ideInstrumentoPorte != null && !this.ideInstrumentoPorte.equals(other.ideInstrumentoPorte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.InstrumentoPorte[ ideInstrumentoPorte=" + ideInstrumentoPorte + " ]";
    }
    
}
