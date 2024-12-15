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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "unidade_medida", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_unidadade_medida"}),
    @UniqueConstraint(columnNames = {"cod_unidade_medida"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadeMedida.findAll", query = "SELECT u FROM UnidadeMedida u"),
    @NamedQuery(name = "UnidadeMedida.findByIdeUnidadeMedida", query = "SELECT u FROM UnidadeMedida u WHERE u.ideUnidadeMedida = :ideUnidadeMedida"),
    @NamedQuery(name = "UnidadeMedida.findByCodUnidadeMedida", query = "SELECT u FROM UnidadeMedida u WHERE u.codUnidadeMedida = :codUnidadeMedida"),
    @NamedQuery(name = "UnidadeMedida.findByNomUnidadadeMedida", query = "SELECT u FROM UnidadeMedida u WHERE u.nomUnidadadeMedida = :nomUnidadadeMedida"),
    @NamedQuery(name = "UnidadeMedida.findByTipologiaAtividade", query = "SELECT um FROM UnidadeTipologiaAtividade u INNER JOIN u.ideUnidadeMedida um WHERE u.ideTipologiaAtividade.ideTipologiaAtividade = :ideTipologiaAtividade")})

public class UnidadeMedida extends AbstractEntity {
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @Column(name = "ide_unidade_medida", nullable = false)
    private Integer ideUnidadeMedida;
    
	@Basic(optional = false)
    @Size(min = 1, max = 30)
    @Column(name = "cod_unidade_medida", nullable = false, length = 30)
    private String codUnidadeMedida;
    
	@Historico(name="Unidade")
	@Basic(optional = false)
    @Size(min = 1, max = 150)
    @Column(name = "nom_unidadade_medida", nullable = false, length = 150)
    private String nomUnidadadeMedida;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideUnidadeMedida", fetch = FetchType.LAZY)
    private Collection<UnidadeMedidaTipologiaGrupo> unidadeMedidaTipologiaGrupoCollection;
    
    public UnidadeMedida() {
    }

    public UnidadeMedida(Integer ideUnidadeMedida) {
        this.ideUnidadeMedida = ideUnidadeMedida;
    }

    public UnidadeMedida(Integer ideUnidadeMedida, String codUnidadeMedida, String nomUnidadadeMedida) {
        this.ideUnidadeMedida = ideUnidadeMedida;
        this.codUnidadeMedida = codUnidadeMedida;
        this.nomUnidadadeMedida = nomUnidadadeMedida;
    }

    public Integer getIdeUnidadeMedida() {
        return ideUnidadeMedida;
    }

    public void setIdeUnidadeMedida(Integer ideUnidadeMedida) {
        this.ideUnidadeMedida = ideUnidadeMedida;
    }

    public String getCodUnidadeMedida() {
        return codUnidadeMedida;
    }

    public void setCodUnidadeMedida(String codUnidadeMedida) {
        this.codUnidadeMedida = codUnidadeMedida;
    }

    public String getNomUnidadadeMedida() {
        return nomUnidadadeMedida;
    }

    public void setNomUnidadadeMedida(String nomUnidadadeMedida) {
        this.nomUnidadadeMedida = nomUnidadadeMedida;
    }

    @XmlTransient
    public Collection<UnidadeMedidaTipologiaGrupo> getUnidadeMedidaTipologiaGrupoCollection() {
        return unidadeMedidaTipologiaGrupoCollection;
    }

    public void setUnidadeMedidaTipologiaGrupoCollection(Collection<UnidadeMedidaTipologiaGrupo> unidadeMedidaTipologiaGrupoCollection) {
        this.unidadeMedidaTipologiaGrupoCollection = unidadeMedidaTipologiaGrupoCollection;
    }
}
