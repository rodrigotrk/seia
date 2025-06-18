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
 * @author carlos.sousa
 */
@Entity
@Table(name = "situacao_norma_tecnica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SituacaoNormaTecnica.findAll", query = "SELECT s FROM SituacaoNormaTecnica s"),
    @NamedQuery(name = "SituacaoNormaTecnica.findByIdeSituacaoNormaTecnica", query = "SELECT s FROM SituacaoNormaTecnica s WHERE s.ideSituacaoNormaTecnica = :ideSituacaoNormaTecnica"),
    @NamedQuery(name = "SituacaoNormaTecnica.findByNomSituacaoNormaTecnica", query = "SELECT s FROM SituacaoNormaTecnica s WHERE s.nomSituacaoNormaTecnica = :nomSituacaoNormaTecnica")})
public class SituacaoNormaTecnica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_situacao_norma_tecnica", nullable = false)
    private Integer ideSituacaoNormaTecnica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_situacao_norma_tecnica", nullable = false, length = 20)
    private String nomSituacaoNormaTecnica;

    public SituacaoNormaTecnica() {
    }

    public SituacaoNormaTecnica(Integer ideSituacaoNormaTecnica) {
        this.ideSituacaoNormaTecnica = ideSituacaoNormaTecnica;
    }

    public SituacaoNormaTecnica(Integer ideSituacaoNormaTecnica, String nomSituacaoNormaTecnica) {
        this.ideSituacaoNormaTecnica = ideSituacaoNormaTecnica;
        this.nomSituacaoNormaTecnica = nomSituacaoNormaTecnica;
    }

    public Integer getIdeSituacaoNormaTecnica() {
        return ideSituacaoNormaTecnica;
    }

    public void setIdeSituacaoNormaTecnica(Integer ideSituacaoNormaTecnica) {
        this.ideSituacaoNormaTecnica = ideSituacaoNormaTecnica;
    }

    public String getNomSituacaoNormaTecnica() {
        return nomSituacaoNormaTecnica;
    }

    public void setNomSituacaoNormaTecnica(String nomSituacaoNormaTecnica) {
        this.nomSituacaoNormaTecnica = nomSituacaoNormaTecnica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSituacaoNormaTecnica != null ? ideSituacaoNormaTecnica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SituacaoNormaTecnica)) {
            return false;
        }
        SituacaoNormaTecnica other = (SituacaoNormaTecnica) object;
        if ((this.ideSituacaoNormaTecnica == null && other.ideSituacaoNormaTecnica != null) || (this.ideSituacaoNormaTecnica != null && !this.ideSituacaoNormaTecnica.equals(other.ideSituacaoNormaTecnica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.SituacaoNormaTecnica[ ideSituacaoNormaTecnica=" + ideSituacaoNormaTecnica + " ]";
    }
    
}
