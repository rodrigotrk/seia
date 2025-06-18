package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "turno", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_turno"}),
    @UniqueConstraint(columnNames = {"cod_turno"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Turno.findAll", query = "SELECT t FROM Turno t"),
    @NamedQuery(name = "Turno.findByIdeTurno", query = "SELECT t FROM Turno t WHERE t.ideTurno = :ideTurno"),
    @NamedQuery(name = "Turno.findByCodTurno", query = "SELECT t FROM Turno t WHERE t.codTurno = :codTurno"),
    @NamedQuery(name = "Turno.findByNomTurno", query = "SELECT t FROM Turno t WHERE t.nomTurno = :nomTurno")})
public class Turno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_turno", nullable = false)
    private Integer ideTurno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cod_turno", nullable = false, length = 3)
    private String codTurno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_turno", nullable = false, length = 15)
    private String nomTurno;

    public Turno() {
    }

    public Turno(Integer ideTurno) {
        this.ideTurno = ideTurno;
    }

    public Turno(Integer ideTurno, String codTurno, String nomTurno) {
        this.ideTurno = ideTurno;
        this.codTurno = codTurno;
        this.nomTurno = nomTurno;
    }

    public Integer getIdeTurno() {
        return ideTurno;
    }

    public void setIdeTurno(Integer ideTurno) {
        this.ideTurno = ideTurno;
    }

    public String getCodTurno() {
        return codTurno;
    }

    public void setCodTurno(String codTurno) {
        this.codTurno = codTurno;
    }

    public String getNomTurno() {
        return nomTurno;
    }

    public void setNomTurno(String nomTurno) {
        this.nomTurno = nomTurno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTurno != null ? ideTurno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Turno)) {
            return false;
        }
        Turno other = (Turno) object;
        if ((this.ideTurno == null && other.ideTurno != null) || (this.ideTurno != null && !this.ideTurno.equals(other.ideTurno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Turno[ ideTurno=" + ideTurno + " ]";
    }
    
}
