package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "calendario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendario.findAll", query = "SELECT c FROM Calendario c"),
    @NamedQuery(name = "Calendario.findByIdeCalendario", query = "SELECT c FROM Calendario c WHERE c.ideCalendario = :ideCalendario"),
    @NamedQuery(name = "Calendario.findByDtcFeriado", query = "SELECT c FROM Calendario c WHERE c.dtcFeriado = :dtcFeriado"),
    @NamedQuery(name = "Calendario.findByIndFeriado", query = "SELECT c FROM Calendario c WHERE c.indFeriado = :indFeriado"),
    @NamedQuery(name = "Calendario.findByIndPtFacultativo", query = "SELECT c FROM Calendario c WHERE c.indPtFacultativo = :indPtFacultativo"),
    @NamedQuery(name = "Calendario.findByDscFeriado", query = "SELECT c FROM Calendario c WHERE c.dscFeriado = :dscFeriado")})
public class Calendario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ide_calendario")
    private Integer ideCalendario;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_feriado")
    @Temporal(TemporalType.DATE)
    private Date dtcFeriado;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_feriado")
    private boolean indFeriado;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_pt_facultativo")
    private boolean indPtFacultativo;
    
    @Size(max = 60)
    @Column(name = "dsc_feriado")
    private String dscFeriado;

    public Calendario() {
    }

    public Calendario(Integer ideCalendario) {
        this.ideCalendario = ideCalendario;
    }

    public Calendario(Integer ideCalendario, Date dtcFeriado, boolean indFeriado, boolean indPtFacultativo) {
        this.ideCalendario = ideCalendario;
        this.dtcFeriado = dtcFeriado;
        this.indFeriado = indFeriado;
        this.indPtFacultativo = indPtFacultativo;
    }

    public Integer getIdeCalendario() {
        return ideCalendario;
    }

    public void setIdeCalendario(Integer ideCalendario) {
        this.ideCalendario = ideCalendario;
    }

    public Date getDtcFeriado() {
        return dtcFeriado;
    }

    public void setDtcFeriado(Date dtcFeriado) {
        this.dtcFeriado = dtcFeriado;
    }

    public boolean getIndFeriado() {
        return indFeriado;
    }

    public void setIndFeriado(boolean indFeriado) {
        this.indFeriado = indFeriado;
    }

    public boolean getIndPtFacultativo() {
        return indPtFacultativo;
    }

    public void setIndPtFacultativo(boolean indPtFacultativo) {
        this.indPtFacultativo = indPtFacultativo;
    }

    public String getDscFeriado() {
        return dscFeriado;
    }

    public void setDscFeriado(String dscFeriado) {
        this.dscFeriado = dscFeriado;
    }
    
}
