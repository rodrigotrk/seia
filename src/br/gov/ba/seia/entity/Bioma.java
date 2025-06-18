package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.enumerator.BiomaEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "bioma")
public class Bioma implements Serializable, BaseEntity {
    
	private static final long serialVersionUID = 3952604804474204139L;

	@Id
    @Column(name = "ide_bioma", nullable = false)
    private Integer ideBioma;
    
    @Column(name = "nom_bioma", nullable = false, length = 50)
    private String nomBioma;
    
    @Column(name = "ind_ativo", nullable = false)
    private Boolean indAtivo;
    
    @JoinColumn(name = "ide_tipo_bioma", referencedColumnName = "ide_tipo_bioma")
	@ManyToOne(optional = false)
	private TipoBioma ideTipoBioma;
    
    @Column(name = "metros_cubicos", nullable = true, precision = 12, scale = 2)
	private BigDecimal metrosCubicos;
    
    @OneToMany(mappedBy="ideBioma", fetch = FetchType.LAZY)
	private Collection<ParametroCalculo> parametroCalculoCollection;
    
    @Transient
    private Double valArea;
   
    public Bioma() {
    	
    }
    
    public Bioma(Integer ideBioma) {
    	this.ideBioma = ideBioma;
    }
    
    public Bioma(Object[] obj) {
    	String nomBioma = String.valueOf(obj[0]);
    	String valArea = String.valueOf(obj[1]);
    	this.ideBioma = BiomaEnum.getEnum(nomBioma).getId();
    	this.nomBioma = nomBioma;
    	this.valArea = Double.valueOf(valArea);
    }

	public Integer getIdeBioma() {
		return ideBioma;
	}

	public void setIdeBioma(Integer ideBioma) {
		this.ideBioma = ideBioma;
	}

	public String getNomBioma() {
		return nomBioma;
	}

	public void setNomBioma(String nomBioma) {
		this.nomBioma = nomBioma;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Collection<ParametroCalculo> getParametroCalculoCollection() {
		return parametroCalculoCollection;
	}

	public void setParametroCalculoCollection(
			Collection<ParametroCalculo> parametroCalculoCollection) {
		this.parametroCalculoCollection = parametroCalculoCollection;
	}

	public TipoBioma getIdeTipoBioma() {
		return ideTipoBioma;
	}

	public void setIdeTipoBioma(TipoBioma ideTipoBioma) {
		this.ideTipoBioma = ideTipoBioma;
	}

	public BigDecimal getMetrosCubicos() {
		return metrosCubicos;
	}

	public void setMetrosCubicos(BigDecimal metrosCubicos) {
		this.metrosCubicos = metrosCubicos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideBioma == null) ? 0 : ideBioma.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bioma other = (Bioma) obj;
		if (ideBioma == null) {
			if (other.ideBioma != null)
				return false;
		} else if (!ideBioma.equals(other.ideBioma))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideBioma);
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}
}