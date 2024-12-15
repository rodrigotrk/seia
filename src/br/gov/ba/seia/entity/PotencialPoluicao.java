package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "potencial_poluicao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_potencial_poluicao"}),
    @UniqueConstraint(columnNames = {"sgl_potencial_poluicao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PotencialPoluicao.findAll", query = "SELECT p FROM PotencialPoluicao p"),
    @NamedQuery(name = "PotencialPoluicao.findByIdePotencialPoluicao", query = "SELECT p FROM PotencialPoluicao p WHERE p.idePotencialPoluicao = :idePotencialPoluicao"),
    @NamedQuery(name = "PotencialPoluicao.findBySglPotencialPoluicao", query = "SELECT p FROM PotencialPoluicao p WHERE p.sglPotencialPoluicao = :sglPotencialPoluicao"),
    @NamedQuery(name = "PotencialPoluicao.findByNomPotencialPoluicao", query = "SELECT p FROM PotencialPoluicao p WHERE p.nomPotencialPoluicao = :nomPotencialPoluicao")})
public class PotencialPoluicao extends AbstractEntity implements Cloneable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_potencial_poluicao", nullable = false)
    private Integer idePotencialPoluicao;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sgl_potencial_poluicao", nullable = false, length = 1)
    private String sglPotencialPoluicao;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nom_potencial_poluicao", nullable = false, length = 25)
    private String nomPotencialPoluicao;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePotencialPoluicao", fetch = FetchType.LAZY)
    private Collection<TipologiaGrupo> tipologiaGrupoCollection;

    public PotencialPoluicao() {
    }

    public PotencialPoluicao(Integer idePotencialPoluicao) {
        this.idePotencialPoluicao = idePotencialPoluicao;
    }

    public PotencialPoluicao(Integer idePotencialPoluicao, String sglPotencialPoluicao, String nomPotencialPoluicao) {
        this.idePotencialPoluicao = idePotencialPoluicao;
        this.sglPotencialPoluicao = sglPotencialPoluicao;
        this.nomPotencialPoluicao = nomPotencialPoluicao;
    }

    public Integer getIdePotencialPoluicao() {
        return idePotencialPoluicao;
    }

    public void setIdePotencialPoluicao(Integer idePotencialPoluicao) {
        this.idePotencialPoluicao = idePotencialPoluicao;
    }

    public String getSglPotencialPoluicao() {
        return sglPotencialPoluicao;
    }

    public void setSglPotencialPoluicao(String sglPotencialPoluicao) {
        this.sglPotencialPoluicao = sglPotencialPoluicao;
    }

    public String getNomPotencialPoluicao() {
        return nomPotencialPoluicao;
    }

    public void setNomPotencialPoluicao(String nomPotencialPoluicao) {
        this.nomPotencialPoluicao = nomPotencialPoluicao;
    }

    @XmlTransient
    public Collection<TipologiaGrupo> getTipologiaGrupoCollection() {
        return tipologiaGrupoCollection;
    }

    public void setTipologiaGrupoCollection(Collection<TipologiaGrupo> tipologiaGrupoCollection) {
        this.tipologiaGrupoCollection = tipologiaGrupoCollection;
    }

	public PotencialPoluicao clone() throws CloneNotSupportedException {
		return (PotencialPoluicao) super.clone();
	}
}
