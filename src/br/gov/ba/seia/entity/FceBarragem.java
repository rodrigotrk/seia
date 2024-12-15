/**
 * 
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name="fce_barragem")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceBarragem.buscarByIdeFce", query = "SELECT f FROM FceBarragem f WHERE f.ideFce.ideFce = :ideFce and f.ideFce.ideDocumentoObrigatorio.ideDocumentoObrigatorio=:ideDocumentoObrigatorio")})
public class FceBarragem implements Serializable, BaseEntity, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ide_fce_barragem_seq", sequenceName="ide_fce_barragem_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_fce_barragem_seq")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_fce_barragem")
	private Integer ideFceBarragem;
	
	@JoinColumn(name="ide_localizacao_geografica",referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch=FetchType.LAZY)
	private LocalizacaoGeografica localizacaoGeografica;
	
	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;
	
	@Column(name = "val_vazao_maxima")
	private Double vazaoMaxima;
	
	@Column(name = "val_vazao_regularizada")
	private Double vazaoRegularizada;
	
	@Column(name = "val_descarga_fundo")
	private Double descargaFundo;
	
	@Column(name = "val_altura_maxima")
	private Double alturaMaxima;
	
	@Column(name = "val_volume_maximo_acumulado")
	private Double volumeMaximoAcumuldao;
	
	@Column(name = "val_cota_maxima")
	private Double cotaMaxima;

	@Column(name = "val_cota_minima")
	private Double cotaMinima;
	
	@Column(name = "val_cota_media_diarias")
	private Double cotaMediaDiarias;

	@JoinTable(name = "fce_barragem_uso_reservatorio", joinColumns = { @JoinColumn(name = "ide_fce_barragem", referencedColumnName = "ide_fce_barragem") }, inverseJoinColumns = { @JoinColumn(name = "ide_uso_reservatorio", referencedColumnName = "ide_uso_reservatorio") })
	@ManyToMany
	private List<UsoReservatorio> usosReservatorio;
	
	@OneToMany(mappedBy = "fceBarragem")
	private List<FceIntervencaoBarragem> fceIntervencaoBarragems;
	
	@OneToOne(mappedBy = "fceBarragem")
	private FceBarragemLicenciamento fceBarragemLicenciamento;
	
	public FceBarragem() {
		this.setUsosReservatorio(new ArrayList<UsoReservatorio>());
	}
	
	/**
	 * @return the ideFceBarragem
	 */
	public Integer getIdeFceBarragem() {
		return ideFceBarragem;
	}

	/**
	 * @param ideFceBarragem the ideFceBarragem to set
	 */
	public void setIdeFceBarragem(Integer ideFceBarragem) {
		this.ideFceBarragem = ideFceBarragem;
	}

	/**
	 * @return the localizacaoGeografica
	 */
	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	/**
	 * @param localizacaoGeografica the localizacaoGeografica to set
	 */
	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	/**
	 * @return the ideFce
	 */
	public Fce getIdeFce() {
		return ideFce;
	}

	/**
	 * @param ideFce the ideFce to set
	 */
	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	/**
	 * @return the vazaoMaxima
	 */
	public Double getVazaoMaxima() {
		return vazaoMaxima;
	}

	/**
	 * @param vazaoMaxima the vazaoMaxima to set
	 */
	public void setVazaoMaxima(Double vazaoMaxima) {
		this.vazaoMaxima = vazaoMaxima;
	}

	/**
	 * @return the vazaoRegularizada
	 */
	public Double getVazaoRegularizada() {
		return vazaoRegularizada;
	}

	/**
	 * @param vazaoRegularizada the vazaoRegularizada to set
	 */
	public void setVazaoRegularizada(Double vazaoRegularizada) {
		this.vazaoRegularizada = vazaoRegularizada;
	}

	/**
	 * @return the descargaFundo
	 */
	public Double getDescargaFundo() {
		return descargaFundo;
	}

	/**
	 * @param descargaFundo the descargaFundo to set
	 */
	public void setDescargaFundo(Double descargaFundo) {
		this.descargaFundo = descargaFundo;
	}

	/**
	 * @return the alturaMaxima
	 */
	public Double getAlturaMaxima() {
		return alturaMaxima;
	}

	/**
	 * @param alturaMaxima the alturaMaxima to set
	 */
	public void setAlturaMaxima(Double alturaMaxima) {
		this.alturaMaxima = alturaMaxima;
	}

	/**
	 * @return the volumeMaximoAcumuldao
	 */
	public Double getVolumeMaximoAcumuldao() {
		return volumeMaximoAcumuldao;
	}

	/**
	 * @param volumeMaximoAcumuldao the volumeMaximoAcumuldao to set
	 */
	public void setVolumeMaximoAcumuldao(Double volumeMaximoAcumuldao) {
		this.volumeMaximoAcumuldao = volumeMaximoAcumuldao;
	}

	/**
	 * @return the cotaMaxima
	 */
	public Double getCotaMaxima() {
		return cotaMaxima;
	}

	/**
	 * @param cotaMaxima the cotaMaxima to set
	 */
	public void setCotaMaxima(Double cotaMaxima) {
		this.cotaMaxima = cotaMaxima;
	}

	/**
	 * @return the cotaMinima
	 */
	public Double getCotaMinima() {
		return cotaMinima;
	}

	/**
	 * @param cotaMinima the cotaMinima to set
	 */
	public void setCotaMinima(Double cotaMinima) {
		this.cotaMinima = cotaMinima;
	}

	/**
	 * @return the cotaMediaDiarias
	 */
	public Double getCotaMediaDiarias() {
		return cotaMediaDiarias;
	}

	/**
	 * @param cotaMediaDiarias the cotaMediaDiarias to set
	 */
	public void setCotaMediaDiarias(Double cotaMediaDiarias) {
		this.cotaMediaDiarias = cotaMediaDiarias;
	}
	
	
	/**
	 * @return the usosReservatorio
	 */
	public List<UsoReservatorio> getUsosReservatorio() {
		return usosReservatorio;
	}

	/**
	 * @param usosReservatorio the usosReservatorio to set
	 */
	public void setUsosReservatorio(List<UsoReservatorio> usosReservatorio) {
		this.usosReservatorio = usosReservatorio;
	}

	/**
	 * @return the fceBarragemLicenciamento
	 */
	public FceBarragemLicenciamento getFceBarragemLicenciamento() {
		return fceBarragemLicenciamento;
	}

	/**
	 * @param fceBarragemLicenciamento the fceBarragemLicenciamento to set
	 */
	public void setFceBarragemLicenciamento(
			FceBarragemLicenciamento fceBarragemLicenciamento) {
		this.fceBarragemLicenciamento = fceBarragemLicenciamento;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceBarragem other = (FceBarragem) obj;
		if (ideFceBarragem == null) {
			if (other.ideFceBarragem != null) {
				return false;
			}
		} else if (!ideFceBarragem.equals(other.ideFceBarragem)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceBarragem == null) ? 0 : ideFceBarragem.hashCode());
		return result;
	}
	

	/**
	 * @return the fceIntervencaoBarragems
	 */
	public List<FceIntervencaoBarragem> getFceIntervencaoBarragems() {
		return fceIntervencaoBarragems;
	}

	/**
	 * @param fceIntervencaoBarragems the fceIntervencaoBarragems to set
	 */
	public void setFceIntervencaoBarragems(
			List<FceIntervencaoBarragem> fceIntervencaoBarragems) {
		this.fceIntervencaoBarragems = fceIntervencaoBarragems;
	}

	@Override
	public Long getId() {
		return new Long(getIdeFceBarragem());
	}
	
	 @Override
     public FceBarragem clone() throws CloneNotSupportedException {
          return (FceBarragem)super.clone();
     }
}
