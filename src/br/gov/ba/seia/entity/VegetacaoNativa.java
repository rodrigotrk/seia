package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import javax.persistence.EntityListeners;

/**
 *
 * @author micael.coutinho
 */
@EntityListeners(VegetacaoNativaListener.class)
@Entity
@Table(name = "vegetacao_nativa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VegetacaoNativa.findAll", query = "SELECT v FROM VegetacaoNativa v"),
    @NamedQuery(name = "VegetacaoNativa.findByIdeVegetacaoNativa", query = "SELECT v FROM VegetacaoNativa v WHERE v.ideVegetacaoNativa = :ideVegetacaoNativa"),
    @NamedQuery(name = "VegetacaoNativa.findByValArea", query = "SELECT v FROM VegetacaoNativa v WHERE v.valArea = :valArea")})
public class VegetacaoNativa implements Serializable,Cloneable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VEGETACAO_NATIVA_IDE_VEGETACAO_NATIVA_SEQ") 
    @SequenceGenerator(name="VEGETACAO_NATIVA_IDE_VEGETACAO_NATIVA_SEQ", sequenceName="VEGETACAO_NATIVA_IDE_VEGETACAO_NATIVA_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_vegetacao_nativa", nullable = false)
	private Integer ideVegetacaoNativa;
    
	// @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "val_area")
    private Double valArea;
    
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
    @ManyToOne
    private LocalizacaoGeografica ideLocalizacaoGeografica;
    
    @JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural")
    @ManyToOne(optional = false)
    private ImovelRural ideImovelRural;
    
    @Size(max = 500)
	@Column(name = "dsc_observacao_alteracao_shape")
	private String dscObservacaoAlteracaoShape;
    
    @OneToMany(mappedBy = "ideVegetacaoNativaFinalidade")
    @Cascade(CascadeType.ALL)
    private Collection<VegetacaoNativaFinalidade> vegetacaoNativaFinalidadeCollection;
    
    @Transient
	private Collection<TipoFinalidadeVegetacaoNativa> tipoFinalidadeVegetacaoNativaCollection;
    
    @Transient
    private GeoJsonSicar geoJsonSicar;

    public VegetacaoNativa() {
    }

    public VegetacaoNativa(Integer ideVegetacaoNativa) {
        this.ideVegetacaoNativa = ideVegetacaoNativa;
    }

    public VegetacaoNativa(Integer ideVegetacaoNativa, Double valArea) {
        this.ideVegetacaoNativa = ideVegetacaoNativa;
        this.valArea = valArea;
    }

    public Integer getIdeVegetacaoNativa() {
        return ideVegetacaoNativa;
    }

    public void setIdeVegetacaoNativa(Integer ideVegetacaoNativa) {
        this.ideVegetacaoNativa = ideVegetacaoNativa;
    }

    public Double getValArea() {
        return valArea;
    }

    public void setValArea(Double valArea) {
        this.valArea = valArea;
    }

    public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
        return ideLocalizacaoGeografica;
    }

    public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
        this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
    }

    public ImovelRural getIdeImovelRural() {
        return ideImovelRural;
    }

    public void setIdeImovelRural(ImovelRural ideImovelRural) {
        this.ideImovelRural = ideImovelRural;
    }

    public Collection<VegetacaoNativaFinalidade> getVegetacaoNativaFinalidadeCollection() {
		return vegetacaoNativaFinalidadeCollection;
	}

	public void setVegetacaoNativaFinalidadeCollection(
			Collection<VegetacaoNativaFinalidade> vegetacaoNativaFinalidadeCollection) {
		this.vegetacaoNativaFinalidadeCollection = vegetacaoNativaFinalidadeCollection;
	}
	
	public Collection<TipoFinalidadeVegetacaoNativa> getTipoFinalidadeVegetacaoNativaCollection() {
		Collection<TipoFinalidadeVegetacaoNativa> listTipoFinalidade = new ArrayList<TipoFinalidadeVegetacaoNativa>();
		
		for (VegetacaoNativaFinalidade vegetacaoNativaFinalidade : vegetacaoNativaFinalidadeCollection) {
			listTipoFinalidade.add(vegetacaoNativaFinalidade.getIdeTipoFinalidadeVegetacaoNativa());
		}
		return tipoFinalidadeVegetacaoNativaCollection;
	}

	public void setTipoFinalidadeVegetacaoNativaCollection(
			Collection<TipoFinalidadeVegetacaoNativa> tipoFinalidadeVegetacaoNativaCollection) {
		this.tipoFinalidadeVegetacaoNativaCollection = tipoFinalidadeVegetacaoNativaCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVegetacaoNativa != null ? ideVegetacaoNativa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof VegetacaoNativa)) {
            return false;
        }
        VegetacaoNativa other = (VegetacaoNativa) object;
        if ((this.ideVegetacaoNativa == null && other.ideVegetacaoNativa != null) || (this.ideVegetacaoNativa != null && !this.ideVegetacaoNativa.equals(other.ideVegetacaoNativa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VegetacaoNativa[ ideVegetacaoNativa=" + ideVegetacaoNativa + " ]";
    }
    
	public GeoJsonSicar getGeoJsonSicar() {
		return geoJsonSicar;
	}

	public void setGeoJsonSicar(GeoJsonSicar geoJsonSicar) {
		this.geoJsonSicar = geoJsonSicar;
	}

	public String getDscObservacaoAlteracaoShape() {
		return dscObservacaoAlteracaoShape;
	}

	public void setDscObservacaoAlteracaoShape(String dscObservacaoAlteracaoShape) {
		this.dscObservacaoAlteracaoShape = dscObservacaoAlteracaoShape;
	}
	
	@Override
	public VegetacaoNativa clone() throws CloneNotSupportedException {
		return (VegetacaoNativa) super.clone();
	}
    
}
