package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "rel_orgao_municipio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelOrgaoMunicipio.findAll", query = "SELECT r FROM RelOrgaoMunicipio r"),
    @NamedQuery(name = "RelOrgaoMunicipio.findByIdeOrgao", query = "SELECT r FROM RelOrgaoMunicipio r WHERE r.relOrgaoMunicipioPK.ideOrgao = :ideOrgao"),
    @NamedQuery(name = "RelOrgaoMunicipio.findByIdeMunicipio", query = "SELECT r FROM RelOrgaoMunicipio r WHERE r.relOrgaoMunicipioPK.ideMunicipio = :ideMunicipio"),
    @NamedQuery(name = "RelOrgaoMunicipio.findByIndSede", query = "SELECT r FROM RelOrgaoMunicipio r WHERE r.indSede = :indSede")})
public class RelOrgaoMunicipio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RelOrgaoMunicipioPK relOrgaoMunicipioPK;
    @Column(name = "ind_sede")
    private boolean indSede;
    @JoinColumn(name = "ide_orgao", referencedColumnName = "ide_orgao", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Orgao orgao;
    @JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Municipio municipio;

    public RelOrgaoMunicipio() {
    }

    public RelOrgaoMunicipio(RelOrgaoMunicipioPK relOrgaoMunicipioPK) {
        this.relOrgaoMunicipioPK = relOrgaoMunicipioPK;
    }

    public RelOrgaoMunicipio(int ideOrgao, int ideMunicipio) {
        this.relOrgaoMunicipioPK = new RelOrgaoMunicipioPK(ideOrgao, ideMunicipio);
    }

    public RelOrgaoMunicipioPK getRelOrgaoMunicipioPK() {
        return relOrgaoMunicipioPK;
    }

    public void setRelOrgaoMunicipioPK(RelOrgaoMunicipioPK relOrgaoMunicipioPK) {
        this.relOrgaoMunicipioPK = relOrgaoMunicipioPK;
    }

    public boolean getIndSede() {
        return indSede;
    }

    public void setIndSede(boolean indSede) {
        this.indSede = indSede;
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public void setOrgao(Orgao orgao) {
        this.orgao = orgao;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    @Transient
    public String getSede() {

    	if (getIndSede())
    		return "Sede";
    	else
    		return "Não é Sede";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relOrgaoMunicipioPK != null ? relOrgaoMunicipioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RelOrgaoMunicipio)) {
            return false;
        }
        RelOrgaoMunicipio other = (RelOrgaoMunicipio) object;
        if ((this.relOrgaoMunicipioPK == null && other.relOrgaoMunicipioPK != null) || (this.relOrgaoMunicipioPK != null && !this.relOrgaoMunicipioPK.equals(other.relOrgaoMunicipioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RelOrgaoMunicipio[ relOrgaoMunicipioPK=" + relOrgaoMunicipioPK + " ]";
    }
}