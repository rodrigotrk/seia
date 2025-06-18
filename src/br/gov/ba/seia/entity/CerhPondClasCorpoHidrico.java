/**
 * 
 */
package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_pond_classe_corpo_hidrico")
public class CerhPondClasCorpoHidrico{

	@Id
	@Column( name = "ide_cerh_pond_classe_corpo_hidrico")
	private Integer ideCerhPonClaCorpoHidrico;
	
	@Column(name = "ide_geo_rpga")
	private Integer geoRpga;
	
	@ManyToOne
	@JoinColumn(name="ide_tipo_uso_recurso_hidrico")
	private TipoUsoRecursoHidrico tipoUsoRecursoHidrico;
	
	@ManyToOne
	@JoinColumn(name="ide_cerh_classe_corpo_hidrico")
	private CerhClasseCorpoHidrico classeCorpoHidrico;
	
	@Column(name = "vlr_referencia")
	private Double vlrReferencia;
	
	@Column(name = "ind_consumo")
	private boolean indConsumo;
	
	@Column(name = "dt_cadastro")
	private Date dtCadastro;
	
	@Column(name = "ind_excluido")
	private boolean indExcluido;
	
	@Column(name = "dt_excluido")
	private Date dtExcluido;

	/**
	 * @return the ideCerhPonClaCorpoHidrico
	 */
	public Integer getIdeCerhPonClaCorpoHidrico() {
		return ideCerhPonClaCorpoHidrico;
	}

	/**
	 * @param ideCerhPonClaCorpoHidrico the ideCerhPonClaCorpoHidrico to set
	 */
	public void setIdeCerhPonClaCorpoHidrico(Integer ideCerhPonClaCorpoHidrico) {
		this.ideCerhPonClaCorpoHidrico = ideCerhPonClaCorpoHidrico;
	}

	/**
	 * @return the geoRpga
	 */
	public Integer getGeoRpga() {
		return geoRpga;
	}

	/**
	 * @param geoRpga the geoRpga to set
	 */
	public void setGeoRpga(Integer geoRpga) {
		this.geoRpga = geoRpga;
	}

	/**
	 * @return the tipoUsoRecursoHidrico
	 */
	public TipoUsoRecursoHidrico getTipoUsoRecursoHidrico() {
		return tipoUsoRecursoHidrico;
	}

	/**
	 * @param tipoUsoRecursoHidrico the tipoUsoRecursoHidrico to set
	 */
	public void setTipoUsoRecursoHidrico(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		this.tipoUsoRecursoHidrico = tipoUsoRecursoHidrico;
	}

	/**
	 * @return the vlrReferencia
	 */
	public Double getVlrReferencia() {
		return vlrReferencia;
	}

	/**
	 * @param vlrReferencia the vlrReferencia to set
	 */
	public void setVlrReferencia(Double vlrReferencia) {
		this.vlrReferencia = vlrReferencia;
	}

	/**
	 * @return the indConsumo
	 */
	public boolean isIndConsumo() {
		return indConsumo;
	}

	/**
	 * @param indConsumo the indConsumo to set
	 */
	public void setIndConsumo(boolean indConsumo) {
		this.indConsumo = indConsumo;
	}

	/**
	 * @return the dtCadastro
	 */
	public Date getDtCadastro() {
		return dtCadastro;
	}

	/**
	 * @param dtCadastro the dtCadastro to set
	 */
	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	/**
	 * @return the indExcluido
	 */
	public boolean isIndExcluido() {
		return indExcluido;
	}

	/**
	 * @param indExcluido the indExcluido to set
	 */
	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	/**
	 * @return the dtExcluido
	 */
	public Date getDtExcluido() {
		return dtExcluido;
	}

	/**
	 * @param dtExcluido the dtExcluido to set
	 */
	public void setDtExcluido(Date dtExcluido) {
		this.dtExcluido = dtExcluido;
	}

	/**
	 * @return the classeCorpoHidrico
	 */
	public CerhClasseCorpoHidrico getClasseCorpoHidrico() {
		return classeCorpoHidrico;
	}

	/**
	 * @param classeCorpoHidrico the classeCorpoHidrico to set
	 */
	public void setClasseCorpoHidrico(CerhClasseCorpoHidrico classeCorpoHidrico) {
		this.classeCorpoHidrico = classeCorpoHidrico;
	}
	

}
