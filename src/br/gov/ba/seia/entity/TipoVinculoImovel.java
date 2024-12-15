package br.gov.ba.seia.entity;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.util.Util;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_vinculo_imovel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoVinculoImovel.findAll", query = "SELECT t FROM TipoVinculoImovel t"),
    @NamedQuery(name = "TipoVinculoImovel.findByIdeTipoVinculoImovel", query = "SELECT t FROM TipoVinculoImovel t WHERE t.ideTipoVinculoImovel = :ideTipoVinculoImovel"),
    @NamedQuery(name = "TipoVinculoImovel.findByNomTipoVinculoImovel", query = "SELECT t FROM TipoVinculoImovel t WHERE t.nomTipoVinculoImovel = :nomTipoVinculoImovel")})
public class TipoVinculoImovel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final int TIPO_VINCULO_PROPRIETARIO = 1;
	public static final int TIPO_VINCULO_JUSTO_POSSUIDOR = 2;
	public static final int TIPO_VINCULO_PROCURADOR = 5;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_vinculo_imovel", nullable = false)
    private Integer ideTipoVinculoImovel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_tipo_vinculo_imovel", nullable = false, length = 100)
    private String nomTipoVinculoImovel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoVinculoImovel", fetch = FetchType.LAZY)
    private Collection<PessoaImovel> pessoaImovelCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoVinculoImovel", fetch = FetchType.LAZY)
    private Collection<TipoDocumentoImovelRural> tipoDocumentoImovelRuralCollection;

    public TipoVinculoImovel() {
    }

    public TipoVinculoImovel(Integer ideTipoVinculoImovel) {
        this.ideTipoVinculoImovel = ideTipoVinculoImovel;
    }

    public TipoVinculoImovel(Integer ideTipoVinculoImovel, String nomTipoVinculoImovel) {
        this.ideTipoVinculoImovel = ideTipoVinculoImovel;
        this.nomTipoVinculoImovel = nomTipoVinculoImovel;
    }

    public Integer getIdeTipoVinculoImovel() {
        return ideTipoVinculoImovel;
    }

    public void setIdeTipoVinculoImovel(Integer ideTipoVinculoImovel) {
        this.ideTipoVinculoImovel = ideTipoVinculoImovel;
    }

    public String getNomTipoVinculoImovel() {
        return nomTipoVinculoImovel;
    }

    public void setNomTipoVinculoImovel(String nomTipoVinculoImovel) {
        this.nomTipoVinculoImovel = nomTipoVinculoImovel;
    }

    @XmlTransient
    public Collection<PessoaImovel> getPessoaImovelCollection() {
        return pessoaImovelCollection;
    }

    public void setPessoaImovelCollection(Collection<PessoaImovel> pessoaImovelCollection) {
        this.pessoaImovelCollection = pessoaImovelCollection;
    }
    
    @Transient
    public boolean isProprietario() {
    	if(!Util.isNull(getIdeTipoVinculoImovel()) && getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO)) {
    		return true;
    	}
    	return false;
    }

    @Transient
    public boolean isJustoPossuidor() {
    	if(!Util.isNull(getIdeTipoVinculoImovel()) && getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
    		return true;
    	}
    	return false;
    }
    
    @Transient
    public boolean isProcurador() {
    	if(!Util.isNull(getIdeTipoVinculoImovel()) && getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_PROCURADOR)) {
    		return true;
    	}
    	return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoVinculoImovel != null ? ideTipoVinculoImovel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoVinculoImovel)) {
            return false;
        }
        TipoVinculoImovel other = (TipoVinculoImovel) object;
        if ((this.ideTipoVinculoImovel == null && other.ideTipoVinculoImovel != null) || (this.ideTipoVinculoImovel != null && !this.ideTipoVinculoImovel.equals(other.ideTipoVinculoImovel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideTipoVinculoImovel);
    }
    
}
