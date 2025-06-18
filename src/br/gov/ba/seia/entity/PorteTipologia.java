package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "porte_tipologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PorteTipologia.findAll", query = "SELECT p FROM PorteTipologia p"),
    @NamedQuery(name = "PorteTipologia.findByIdePorteTipologia", query = "SELECT p FROM PorteTipologia p WHERE p.idePorteTipologia = :idePorteTipologia"),
    @NamedQuery(name = "PorteTipologia.findByIdePorte", query = "SELECT p FROM PorteTipologia p WHERE p.idePorte = :idePorte"),
    @NamedQuery(name = "PorteTipologia.findByValMinimo", query = "SELECT p FROM PorteTipologia p WHERE p.valMinimo = :valMinimo"),
    @NamedQuery(name = "PorteTipologia.findByValMaximo", query = "SELECT p FROM PorteTipologia p WHERE p.valMaximo = :valMaximo"),
    @NamedQuery(name = "PorteTipologia.findByIdePorteIdePorteTipologia", query = "SELECT p FROM PorteTipologia p WHERE p.idePorte.idePorte = :idePorte and p.ideTipologiaGrupo.ideTipologiaGrupo = :ideTipologiaGrupo")})
public class PorteTipologia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="PORTE_TIPOLOGIA_IDE_PORTE_TIPOLOGIA_seq", sequenceName="PORTE_TIPOLOGIA_IDE_PORTE_TIPOLOGIA_seq", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PORTE_TIPOLOGIA_IDE_PORTE_TIPOLOGIA_seq")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_porte_tipologia", nullable = false)
    private Integer idePorteTipologia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "val_minimo", precision = 14, scale = 3)
    private BigDecimal valMinimo;
    @Column(name = "val_maximo", precision = 14, scale = 3)
    private BigDecimal valMaximo;
    @JoinColumn(name = "ide_tipologia_grupo", referencedColumnName = "ide_tipologia_grupo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipologiaGrupo ideTipologiaGrupo;    
    @JoinColumn(name = "ide_porte", referencedColumnName = "ide_porte", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Porte idePorte;

    public PorteTipologia() {
    }

    public PorteTipologia(Integer idePorteTipologia) {
        this.idePorteTipologia = idePorteTipologia;
    }

    public Integer getIdePorteTipologia() {
        return idePorteTipologia;
    }

    public void setIdePorteTipologia(Integer idePorteTipologia) {
        this.idePorteTipologia = idePorteTipologia;
    }

    public BigDecimal getValMinimo() {
        return Util.isNull(valMinimo) ? BigDecimal.ZERO:valMinimo;
    }

    public void setValMinimo(BigDecimal valMinimo) {
        this.valMinimo = valMinimo;
    }

    public BigDecimal getValMaximo() {
        return valMaximo;
    }

    public void setValMaximo(BigDecimal valMaximo) {
        this.valMaximo = valMaximo;
    }

    public TipologiaGrupo getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(TipologiaGrupo ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }
   
    public Porte getIdePorte() {
        return idePorte;
    }

    public void setIdePorte(Porte idePorte) {
        this.idePorte = idePorte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePorteTipologia != null ? idePorteTipologia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PorteTipologia)) {
            return false;
        }
        PorteTipologia other = (PorteTipologia) object;
        if ((this.idePorteTipologia == null && other.idePorteTipologia != null) || (this.idePorteTipologia != null && !this.idePorteTipologia.equals(other.idePorteTipologia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PorteTipologia[ idePorteTipologia=" + idePorteTipologia + " ]";
    }
    
}
