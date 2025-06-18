package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "fce_florestal")
public class FceFlorestal extends AbstractEntity implements Serializable, Cloneable {
	
	private static final long serialVersionUID = -6674941621135429813L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_florestal_ide_fce_florestal_seq")
	@SequenceGenerator(name = "fce_florestal_ide_fce_florestal_seq", sequenceName = "fce_florestal_ide_fce_florestal_seq", allocationSize = 1)
	@Column(name = "ide_fce_florestal")
	private Integer ideFceFlorestal;
	
	@Column(name = "num_area_app")
	private Double numAreaApp;
	
	@Column(name = "num_area_supressao")
	private Double numAreaSupressao;
	
	@Column(name = "num_area_suprimida")
	private Double numAreaSuprimida;
	
	@Column(name = "val_area")
	private BigDecimal valArea;
	
	@Column(name = "val_area_und_producao")
	private BigDecimal valAreaUndProducao;
	
	@Column(name = "ind_app")
	private Boolean indApp;
	
	@Column(name = "ind_material_lenhoso")
	private Boolean indMaterialLenhoso;
	
	@Column(name = "ind_aceite")
	private Boolean indAceite;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;
	
	@OneToMany(mappedBy="ideEspecieFlorestal", fetch=FetchType.LAZY)
	private Collection<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoCollection;
	
	@Transient
	private Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoNativa;
	
	@Transient
	private Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoExotica;
	
	@Transient
	private Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoNativaRemocao;
	
	@Transient
	private Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoExoticaRemocao;
	
	public FceFlorestal() {}
	
	public FceFlorestal(Fce fce) {
		this.ideFce = fce;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Integer getIdeFceFlorestal() {
		return ideFceFlorestal;
	}
	
	public void setIdeFceFlorestal(Integer ideFceFlorestal) {
		this.ideFceFlorestal = ideFceFlorestal;
	}
	
	public Fce getIdeFce() {
		return ideFce;
	}
	
	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}
	
	public Double getNumAreaApp() {
		return numAreaApp;
	}
	
	public void setNumAreaApp(Double numAreaApp) {
		this.numAreaApp = numAreaApp;
	}
	
	public Double getNumAreaSupressao() {
		return numAreaSupressao;
	}
	
	public void setNumAreaSupressao(Double numAreaSupressao) {
		this.numAreaSupressao = numAreaSupressao;
	}
	
	public Double getNumAreaSuprimida() {
		return numAreaSuprimida;
	}
	
	public void setNumAreaSuprimida(Double numAreaSuprimida) {
		this.numAreaSuprimida = numAreaSuprimida;
	}
	
	public BigDecimal getValArea() {
		return valArea;
	}
	
	public void setValArea(BigDecimal valArea) {
		this.valArea = valArea;
	}
	
	public BigDecimal getValAreaUndProducao() {
		return valAreaUndProducao;
	}
	
	public void setValAreaUndProducao(BigDecimal valAreaUndProducao) {
		this.valAreaUndProducao = valAreaUndProducao;
	}
	
	public Boolean getIndApp() {
		return indApp;
	}
	
	public void setIndApp(Boolean indApp) {
		this.indApp = indApp;
	}
	
	public Boolean getIndMaterialLenhoso() {
		return indMaterialLenhoso;
	}
	
	public void setIndMaterialLenhoso(Boolean indMaterialLenhoso) {
		this.indMaterialLenhoso = indMaterialLenhoso;
	}
	
	public Boolean getIndAceite() {
		return indAceite;
	}
	
	public void setIndAceite(Boolean indAceite) {
		this.indAceite = indAceite;
	}

	public Collection<EspecieFlorestalAutorizacao> getEspecieFlorestalAutorizacaoCollection() {
		return especieFlorestalAutorizacaoCollection;
	}

	public void setEspecieFlorestalAutorizacaoCollection(
			Collection<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoCollection) {
		this.especieFlorestalAutorizacaoCollection = especieFlorestalAutorizacaoCollection;
	}

	public Collection<EspecieFlorestalAutorizacao> getListaEspecieFlorestalAutorizacaoNativa() {
		return listaEspecieFlorestalAutorizacaoNativa;
	}

	public void setListaEspecieFlorestalAutorizacaoNativa(
			Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoNativa) {
		this.listaEspecieFlorestalAutorizacaoNativa = listaEspecieFlorestalAutorizacaoNativa;
	}

	public Collection<EspecieFlorestalAutorizacao> getListaEspecieFlorestalAutorizacaoExotica() {
		return listaEspecieFlorestalAutorizacaoExotica;
	}

	public void setListaEspecieFlorestalAutorizacaoExotica(
			Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoExotica) {
		this.listaEspecieFlorestalAutorizacaoExotica = listaEspecieFlorestalAutorizacaoExotica;
	}

	public Collection<EspecieFlorestalAutorizacao> getListaEspecieFlorestalAutorizacaoNativaRemocao() {
		return listaEspecieFlorestalAutorizacaoNativaRemocao;
	}

	public void setListaEspecieFlorestalAutorizacaoNativaRemocao(
			Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoNativaRemocao) {
		this.listaEspecieFlorestalAutorizacaoNativaRemocao = listaEspecieFlorestalAutorizacaoNativaRemocao;
	}

	public Collection<EspecieFlorestalAutorizacao> getListaEspecieFlorestalAutorizacaoExoticaRemocao() {
		return listaEspecieFlorestalAutorizacaoExoticaRemocao;
	}

	public void setListaEspecieFlorestalAutorizacaoExoticaRemocao(
			Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacaoExoticaRemocao) {
		this.listaEspecieFlorestalAutorizacaoExoticaRemocao = listaEspecieFlorestalAutorizacaoExoticaRemocao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ideFce == null) ? 0 : ideFce.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FceFlorestal other = (FceFlorestal) obj;
		if (ideFce == null) {
			if (other.ideFce != null)
				return false;
		} else if (!ideFce.equals(other.ideFce))
			return false;
		return true;
	}
	
	@Override
	public FceFlorestal clone() throws CloneNotSupportedException  {
		return (FceFlorestal) super.clone();
	}
}