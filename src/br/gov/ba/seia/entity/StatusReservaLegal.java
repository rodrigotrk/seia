package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.duarte
 */
@Entity
@Table(name = "status_reserva_legal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatusReservaLegal.findAll", query = "SELECT s FROM StatusReservaLegal s"),
    @NamedQuery(name = "StatusReservaLegal.findByIdeStatusReservaLegal", query = "SELECT s FROM StatusReservaLegal s WHERE s.ideStatusReservaLegal = :ideStatusReservaLegal"),
    @NamedQuery(name = "StatusReservaLegal.findByDscStatus", query = "SELECT s FROM StatusReservaLegal s WHERE s.dscStatus = :dscStatus")})
public class StatusReservaLegal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_status_reserva_legal", nullable = false)
    private Integer ideStatusReservaLegal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_status", nullable = false, length = 100)
    private String dscStatus;
    
    public StatusReservaLegal() {
    }

    public StatusReservaLegal(Integer ideStatusReservaLegal) {
        this.ideStatusReservaLegal = ideStatusReservaLegal;
    }

    public StatusReservaLegal(Integer ideStatusReservaLegal, String dscStatus) {
        this.ideStatusReservaLegal = ideStatusReservaLegal;
        this.dscStatus = dscStatus;
    }

    public Integer getIdeStatusReservaLegal() {
        return ideStatusReservaLegal;
    }

    public void setIdeStatusReservaLegal(Integer ideStatusReservaLegal) {
        this.ideStatusReservaLegal = ideStatusReservaLegal;
    }

    public String getdscStatus() {
        return dscStatus;
    }

    public void setdscStatus(String dscStatus) {
        this.dscStatus = dscStatus;
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideStatusReservaLegal != null ? ideStatusReservaLegal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof StatusReservaLegal)) {
            return false;
        }
        StatusReservaLegal other = (StatusReservaLegal) object;
        if ((this.ideStatusReservaLegal == null && other.ideStatusReservaLegal != null) || (this.ideStatusReservaLegal != null && !this.ideStatusReservaLegal.equals(other.ideStatusReservaLegal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideStatusReservaLegal);
    }
    
}
