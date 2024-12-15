/**
 * 
 */
package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_preco_pub_unitario")
public class CerhPrecoPubUnitario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_preco_pub_unitario_seq")
	@SequenceGenerator(name = "cerh_preco_pub_unitario_seq", sequenceName = "cerh_preco_pub_unitario_seq", allocationSize = 1)
	@Column( name = "ide_cerh_preco_pub_unitario")
	private Integer ideCerhPrecoPubUnitario;
	
	@Column(name="ide_geo_rpga")
	private Integer geoRpga;
	
	@ManyToOne
	@JoinColumn(name="ide_tipo_uso_recurso_hidrico")
	private TipoUsoRecursoHidrico tipoUsoRecursoHidrico;
	
	@Column(name = "ind_consumo")
	private boolean indConsumo;
	
	@Column(name = "vlr_referencia")
	private Double vlrReferencia;
	
	@Column(name = "dt_cadstro")
	private Date dtCadastro;
	
	@Column(name = "ind_excluido")
	private boolean indExcluido;
	
	@Column(name = "dt_excluido")
	private Date dtExcluido;

	/**
	 * @return the ideCerhPrecoPubUnitario
	 */
	public Integer getIdeCerhPrecoPubUnitario() {
		return ideCerhPrecoPubUnitario;
	}

	/**
	 * @param ideCerhPrecoPubUnitario the ideCerhPrecoPubUnitario to set
	 */
	public void setIdeCerhPrecoPubUnitario(Integer ideCerhPrecoPubUnitario) {
		this.ideCerhPrecoPubUnitario = ideCerhPrecoPubUnitario;
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

}
