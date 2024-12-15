package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoImovelEnum;


@Entity
@Table(name = "tipo_imovel", uniqueConstraints = {
@UniqueConstraint(columnNames = {"nom_tipo_imovel"})})
public class TipoImovel extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_tipo_imovel", nullable = false)
	private Integer ideTipoImovel;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "nom_tipo_imovel", nullable = false, length = 10)
	private String nomTipoImovel;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoImovel", fetch = FetchType.LAZY)
	private Collection<Imovel> imovelCollection;

    public TipoImovel() {

    }

    public TipoImovel(TipoImovelEnum tipoImovelEnum){
    	this.ideTipoImovel = tipoImovelEnum.getId();
    }

    public TipoImovel(Integer ideTipoImovel) {
        this.ideTipoImovel = ideTipoImovel;
    }

    public TipoImovel(Integer ideTipoImovel, String nomTipoImovel) {
        this.ideTipoImovel = ideTipoImovel;
        this.nomTipoImovel = nomTipoImovel;
    }

    public Integer getIdeTipoImovel() {
        return ideTipoImovel;
    }

    public void setIdeTipoImovel(Integer ideTipoImovel) {
        this.ideTipoImovel = ideTipoImovel;
    }

    public String getNomTipoImovel() {
        return nomTipoImovel;
    }

    public void setNomTipoImovel(String nomTipoImovel) {
        this.nomTipoImovel = nomTipoImovel;
    }

    @XmlTransient
    public Collection<Imovel> getImovelCollection() {
        return imovelCollection;
    }

    public void setImovelCollection(Collection<Imovel> imovelCollection) {
        this.imovelCollection = imovelCollection;
    }
}
