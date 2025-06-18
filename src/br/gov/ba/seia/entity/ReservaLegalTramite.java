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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "reserva_legal_tramite")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReservaLegalTramite.findAll", query = "SELECT r FROM ReservaLegalTramite r"),
    @NamedQuery(name = "ReservaLegalTramite.findByIdeReservaLegalTramite", query = "SELECT r FROM ReservaLegalTramite r WHERE r.ideReservaLegalTramite = :ideReservaLegalTramite"),
    @NamedQuery(name = "ReservaLegalTramite.findByIndTramite", query = "SELECT r FROM ReservaLegalTramite r WHERE r.indTramite = :indTramite"),
    @NamedQuery(name = "ReservaLegalTramite.findByNumProcesso", query = "SELECT r FROM ReservaLegalTramite r WHERE r.numProcesso = :numProcesso")})
public class ReservaLegalTramite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESERVA_LEGAL_TRAMITE_IDE_RESERVA_LEGAL_TRAMITE_seq")    
    @SequenceGenerator(name="RESERVA_LEGAL_TRAMITE_IDE_RESERVA_LEGAL_TRAMITE_seq", sequenceName="RESERVA_LEGAL_TRAMITE_IDE_RESERVA_LEGAL_TRAMITE_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_reserva_legal_tramite", nullable = false)
    private Integer ideReservaLegalTramite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_tramite", nullable = false)
    private Boolean indTramite;
    @Size(max = 30)
    @Column(name = "num_processo", length = 30)
    private String numProcesso;
    @JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel_rural", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ImovelRural ideImovel;

    public ReservaLegalTramite() {
    }

    public ReservaLegalTramite(Integer ideReservaLegalTramite) {
        this.ideReservaLegalTramite = ideReservaLegalTramite;
    }

    public ReservaLegalTramite(Integer ideReservaLegalTramite, boolean indTramite) {
        this.ideReservaLegalTramite = ideReservaLegalTramite;
        this.indTramite = indTramite;
    }

    public Integer getIdeReservaLegalTramite() {
        return ideReservaLegalTramite;
    }

    public void setIdeReservaLegalTramite(Integer ideReservaLegalTramite) {
        this.ideReservaLegalTramite = ideReservaLegalTramite;
    }

    public boolean getIndTramite() {
        return indTramite;
    }

    public void setIndTramite(Boolean indTramite) {
        this.indTramite = indTramite;
    }

    public String getNumProcesso() {
        return numProcesso;
    }

    public void setNumProcesso(String numProcesso) {
        this.numProcesso = numProcesso;
    }

    public ImovelRural getIdeImovel() {
        return ideImovel;
    }

    public void setIdeImovel(ImovelRural ideImovel) {
        this.ideImovel = ideImovel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideReservaLegalTramite != null ? ideReservaLegalTramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaLegalTramite)) {
            return false;
        }
        ReservaLegalTramite other = (ReservaLegalTramite) object;
        if ((this.ideReservaLegalTramite == null && other.ideReservaLegalTramite != null) || (this.ideReservaLegalTramite != null && !this.ideReservaLegalTramite.equals(other.ideReservaLegalTramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ReservaLegalTramite[ ideReservaLegalTramite=" + ideReservaLegalTramite + " ]";
    }
    
}
