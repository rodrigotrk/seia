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

@Entity
@Table(name = "simbolo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_simbolo"}),
    @UniqueConstraint(columnNames = {"cod_simbolo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Simbolo.findAll", query = "SELECT s FROM Simbolo s"),
    @NamedQuery(name = "Simbolo.findByIdeSimbolo", query = "SELECT s FROM Simbolo s WHERE s.ideSimbolo = :ideSimbolo"),
    @NamedQuery(name = "Simbolo.findByCodSimbolo", query = "SELECT s FROM Simbolo s WHERE s.codSimbolo = :codSimbolo"),
    @NamedQuery(name = "Simbolo.findByNomSimbolo", query = "SELECT s FROM Simbolo s WHERE s.nomSimbolo = :nomSimbolo")})
public class Simbolo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_simbolo", nullable = false)
    private Integer ideSimbolo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cod_simbolo", nullable = false, length = 3)
    private String codSimbolo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_simbolo", nullable = false, length = 50)
    private String nomSimbolo;
    

    public Simbolo() {
    }

    public Simbolo(Integer ideSimbolo) {
        this.ideSimbolo = ideSimbolo;
    }

    public Simbolo(Integer ideSimbolo, String codSimbolo, String nomSimbolo) {
        this.ideSimbolo = ideSimbolo;
        this.codSimbolo = codSimbolo;
        this.nomSimbolo = nomSimbolo;
    }

    public Integer getIdeSimbolo() {
        return ideSimbolo;
    }

    public void setIdeSimbolo(Integer ideSimbolo) {
        this.ideSimbolo = ideSimbolo;
    }

    public String getCodSimbolo() {
        return codSimbolo;
    }

    public void setCodSimbolo(String codSimbolo) {
        this.codSimbolo = codSimbolo;
    }

    public String getNomSimbolo() {
        return nomSimbolo;
    }

    public void setNomSimbolo(String nomSimbolo) {
        this.nomSimbolo = nomSimbolo;
    }
   
  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSimbolo != null ? ideSimbolo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Simbolo)) {
            return false;
        }
        Simbolo other = (Simbolo) object;
        if ((this.ideSimbolo == null && other.ideSimbolo != null) || (this.ideSimbolo != null && !this.ideSimbolo.equals(other.ideSimbolo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Simbolo[ ideSimbolo=" + ideSimbolo + " ]";
    }
    
}
