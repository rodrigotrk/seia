package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "reserva_legal_processo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReservaLegalProcesso.findAll", query = "SELECT r FROM ReservaLegalProcesso r"),
    @NamedQuery(name = "ReservaLegalProcesso.findByIdeReservaLegalProcesso", query = "SELECT r FROM ReservaLegalProcesso r WHERE r.ideReservaLegalProcesso = :ideReservaLegalProcesso"),
    @NamedQuery(name = "ReservaLegalProcesso.findByNumProcesso", query = "SELECT r FROM ReservaLegalProcesso r WHERE r.numProcesso = :numProcesso")})
public class ReservaLegalProcesso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_reserva_legal_processo", nullable = false)
    private Integer ideReservaLegalProcesso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_processo", nullable = false, length = 30)
    private String numProcesso;
    @JoinColumn(name = "ide_reserva_legal_tramite", referencedColumnName = "ide_reserva_legal_tramite", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ReservaLegalTramite ideReservaLegalTramite;

    public ReservaLegalProcesso() {
    }

    public ReservaLegalProcesso(Integer ideReservaLegalProcesso) {
        this.ideReservaLegalProcesso = ideReservaLegalProcesso;
    }

    public ReservaLegalProcesso(Integer ideReservaLegalProcesso, String numProcesso) {
        this.ideReservaLegalProcesso = ideReservaLegalProcesso;
        this.numProcesso = numProcesso;
    }

    public Integer getIdeReservaLegalProcesso() {
        return ideReservaLegalProcesso;
    }

    public void setIdeReservaLegalProcesso(Integer ideReservaLegalProcesso) {
        this.ideReservaLegalProcesso = ideReservaLegalProcesso;
    }

    public String getNumProcesso() {
        return numProcesso;
    }

    public void setNumProcesso(String numProcesso) {
        this.numProcesso = numProcesso;
    }

    public ReservaLegalTramite getIdeReservaLegalTramite() {
        return ideReservaLegalTramite;
    }

    public void setIdeReservaLegalTramite(ReservaLegalTramite ideReservaLegalTramite) {
        this.ideReservaLegalTramite = ideReservaLegalTramite;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideReservaLegalProcesso != null ? ideReservaLegalProcesso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaLegalProcesso)) {
            return false;
        }
        ReservaLegalProcesso other = (ReservaLegalProcesso) object;
        if ((this.ideReservaLegalProcesso == null && other.ideReservaLegalProcesso != null) || (this.ideReservaLegalProcesso != null && !this.ideReservaLegalProcesso.equals(other.ideReservaLegalProcesso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ReservaLegalProcesso[ ideReservaLegalProcesso=" + ideReservaLegalProcesso + " ]";
    }
    
}
