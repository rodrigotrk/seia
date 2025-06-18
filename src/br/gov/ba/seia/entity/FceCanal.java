package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "fce_canal")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceCanal.selectByFce", query = "SELECT fceCanal FROM FceCanal fceCanal WHERE fceCanal.fce = :fce")})
public class FceCanal implements Serializable, BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_canais_seq")
	@SequenceGenerator(name = "fce_canais_seq", sequenceName = "fce_canais_seq", allocationSize = 1)
	@Column ( name = "ide_fce_canal")
	private Integer ideFceCanal;
	
	@Column( name = "val_vazao")
	private double vazao;
	
	@Column( name = "val_area_ocupada")
	private double areaOcupada;
	
	@Column( name = "nom_rio")
	private String nomeRio;
	
	@JoinColumn(name = "ide_tipo_rio", referencedColumnName = "ide_tipo_rio", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoRio tipoRio;
	
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Fce fce;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "fce_canal_caracteristica", 
		joinColumns = { @JoinColumn(name = "ide_fce_canal", referencedColumnName = "ide_fce_canal") }, 
		inverseJoinColumns = { @JoinColumn(name = "ide_caracteristica_canal", referencedColumnName = "ide_caracteristica_canal") })
	private List<CaracteristicaCanal> caracteristicasCanal;
	
	
	@OneToMany(mappedBy = "canal", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<FceCanalTrecho> canalTrechos;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "fce_canal_municipio",  
		joinColumns = { @JoinColumn(name = "ide_fce_canal", referencedColumnName = "ide_fce_canal") }, 
		inverseJoinColumns = { @JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio") })
	private List<Municipio> municipios;
	
	@OneToMany(mappedBy = "ideFceCanal", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	private List<FceCanalTipoFinalidadeUsoAgua> fceCanalTiposFinalidadesUsoAgua;
	
	public FceCanal() {
		this.setMunicipios(new ArrayList<Municipio>());
		this.setCaracteristicasCanal(new ArrayList<CaracteristicaCanal>());
		this.setCanalTrechos(new ArrayList<FceCanalTrecho>());
		this.setFceCanalTiposFinalidadesUsoAgua(new ArrayList<FceCanalTipoFinalidadeUsoAgua>());
	}
	
	
	public Integer getIdeFceCanal() {
		return ideFceCanal;
	}

	public void setIdeFceCanal(Integer ideFceCanal) {
		this.ideFceCanal = ideFceCanal;
	}

	public double getVazao() {
		return vazao;
	}

	public void setVazao(double vazao) {
		this.vazao = vazao;
	}

	public double getAreaOcupada() {
		return areaOcupada;
	}

	public void setAreaOcupada(double areaOcupada) {
		this.areaOcupada = areaOcupada;
	}

	public String getNomeRio() {
		return nomeRio;
	}

	public void setNomeRio(String nomeRio) {
		this.nomeRio = nomeRio;
	}

	public TipoRio getTipoRio() {
		return tipoRio;
	}

	public void setTipoRio(TipoRio tipoRio) {
		this.tipoRio = tipoRio;
	}

	public Fce getFce() {
		return fce;
	}

	public void setFce(Fce fce) {
		this.fce = fce;
	}

	public List<CaracteristicaCanal> getCaracteristicasCanal() {
		return caracteristicasCanal;
	}

	public void setCaracteristicasCanal(
			List<CaracteristicaCanal> caracteristicasCanal) {
		this.caracteristicasCanal = caracteristicasCanal;
	}

	public List<FceCanalTrecho> getCanalTrechos() {
		return canalTrechos;
	}

	public void setCanalTrechos(List<FceCanalTrecho> canalTrechos) {
		this.canalTrechos = canalTrechos;
	}

	@Override
	public Long getId() {
		return new Long(this.getIdeFceCanal());
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}


	/**
	 * @return the fceCanalTiposFinalidadesUsoAgua
	 */
	public List<FceCanalTipoFinalidadeUsoAgua> getFceCanalTiposFinalidadesUsoAgua() {
		return fceCanalTiposFinalidadesUsoAgua;
	}


	/**
	 * @param fceCanalTiposFinalidadesUsoAgua the fceCanalTiposFinalidadesUsoAgua to set
	 */
	public void setFceCanalTiposFinalidadesUsoAgua(
			List<FceCanalTipoFinalidadeUsoAgua> fceCanalTiposFinalidadesUsoAgua) {
		this.fceCanalTiposFinalidadesUsoAgua = fceCanalTiposFinalidadesUsoAgua;
	}

}
