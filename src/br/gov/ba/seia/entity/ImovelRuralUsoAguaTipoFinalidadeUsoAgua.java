package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: ImovelRuralUsoAguaTipoFinalidadeUsoAgua
 *
 */

@Entity
@Table(name = "imovel_rural_uso_agua_tipo_finalidade_uso_agua")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImovelRuralUsoAguaTipoFinalidadeUsoAgua.findAll", query = "SELECT i FROM ImovelRuralUsoAguaTipoFinalidadeUsoAgua i"),
    @NamedQuery(name = "ImovelRuralUsoAguaTipoFinalidadeUsoAgua.findByIdeImovelRuralUsoAguaTipoFinalidadeUsoAgua", query = "SELECT i FROM ImovelRuralUsoAguaTipoFinalidadeUsoAgua i WHERE i.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua = :ideImovelRuralUsoAguaTipoFinalidadeUsoAgua"),
    @NamedQuery(name = "ImovelRuralUsoAguaTipoFinalidadeUsoAgua.findByImovelRuralUsoAgua", query = "SELECT i FROM ImovelRuralUsoAguaTipoFinalidadeUsoAgua i WHERE i.ideImovelRuralUsoAgua = :ideImovelRuralUsoAgua"),
    @NamedQuery(name = "ImovelRuralUsoAguaTipoFinalidadeUsoAgua.findTipoFinalidadeUsoAguaByImovelRuralUsoAgua", query = "SELECT i.ideTipoFinalidadeUsoAgua FROM ImovelRuralUsoAguaTipoFinalidadeUsoAgua i WHERE i.ideImovelRuralUsoAgua = :ideImovelRuralUsoAgua"),
	@NamedQuery(name = "ImovelRuralUsoAguaTipoFinalidadeUsoAgua.findByTipoFinalidadeUsoAgua", query = "SELECT i FROM ImovelRuralUsoAguaTipoFinalidadeUsoAgua i WHERE i.ideTipoFinalidadeUsoAgua = :ideTipoFinalidadeUsoAgua")})

public class ImovelRuralUsoAguaTipoFinalidadeUsoAgua implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="imovel_rural_uso_agua_tipo_finalidade_uso_agua_seq")    
    @SequenceGenerator(name="imovel_rural_uso_agua_tipo_finalidade_uso_agua_seq", sequenceName="imovel_rural_uso_agua_tipo_finalidade_uso_agua_seq", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_imovel_rural_uso_agua_tipo_finalidade_uso_agua", nullable = false)
	private Integer ideImovelRuralUsoAguaTipoFinalidadeUsoAgua;	
	
	@JoinColumn(name = "ide_imovel_rural_uso_agua", referencedColumnName = "ide_imovel_rural_uso_agua", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ImovelRuralUsoAgua ideImovelRuralUsoAgua;
	
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;		

	public ImovelRuralUsoAguaTipoFinalidadeUsoAgua() {
		super();
	}

	public ImovelRuralUsoAguaTipoFinalidadeUsoAgua(
			Integer ideImovelRuralUsoAguaTipoFinalidadeUsoAgua) {
		super();
		this.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua = ideImovelRuralUsoAguaTipoFinalidadeUsoAgua;
	}
	
	public ImovelRuralUsoAguaTipoFinalidadeUsoAgua(
			Integer ideImovelRuralUsoAguaTipoFinalidadeUsoAgua,
			ImovelRuralUsoAgua ideImovelRuralUsoAgua,
			TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		super();
		this.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua = ideImovelRuralUsoAguaTipoFinalidadeUsoAgua;
		this.ideImovelRuralUsoAgua = ideImovelRuralUsoAgua;
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public Integer getIdeImovelRuralUsoAguaTipoFinalidadeUsoAgua() {
		return ideImovelRuralUsoAguaTipoFinalidadeUsoAgua;
	}

	public void setIdeImovelRuralUsoAguaTipoFinalidadeUsoAgua(
			Integer ideImovelRuralUsoAguaTipoFinalidadeUsoAgua) {
		this.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua = ideImovelRuralUsoAguaTipoFinalidadeUsoAgua;
	}

	public ImovelRuralUsoAgua getIdeImovelRuralUsoAgua() {
		return ideImovelRuralUsoAgua;
	}

	public void setIdeImovelRuralUsoAgua(ImovelRuralUsoAgua ideImovelRuralUsoAgua) {
		this.ideImovelRuralUsoAgua = ideImovelRuralUsoAgua;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(
			TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}
	

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideImovelRuralUsoAguaTipoFinalidadeUsoAgua != null ? ideImovelRuralUsoAguaTipoFinalidadeUsoAgua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ImovelRuralUsoAguaTipoFinalidadeUsoAgua)) {
            return false;
        }
        ImovelRuralUsoAguaTipoFinalidadeUsoAgua other = (ImovelRuralUsoAguaTipoFinalidadeUsoAgua) object;
        if ((this.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua == null && other.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua != null) || (this.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua != null && !this.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua.equals(other.ideImovelRuralUsoAguaTipoFinalidadeUsoAgua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ImovelRuralUsoAguaTipoFinalidadeUsoAgua[ ideImovelRuralUsoAguaTipoFinalidadeUsoAgua=" + ideImovelRuralUsoAguaTipoFinalidadeUsoAgua + " ]";
    }
	
   
}
