package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "cerh_pond_gestao")
public class CerhPondGestao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_pond_gestao_seq")
	@SequenceGenerator(name = "cerh_pond_gestao_seq", sequenceName = "cerh_pond_gestao_seq", allocationSize = 1)
	@Column( name = "ide_cerh_pond_gestao")
	private Integer ideCerhPondGestao;
	
	@Column(name = "ide_geo_rpga")
	private Integer geoRpga;
	
	@Column(name = "dt_cadastro")
	private Date dtCadastro;
	
	@Column(name = "vl_referencia")
	private Double vlrReferencia;
	
	@Column(name = "ind_excluido")
	private boolean indExcluido;
	
	@Column(name = "dt_excluido")
	private Date dtExcluido;

	/**
	 * @return the ideCerhPondGestao
	 */
	public Integer getIdeCerhPondGestao() {
		return ideCerhPondGestao;
	}

	/**
	 * @param ideCerhPondGestao the ideCerhPondGestao to set
	 */
	public void setIdeCerhPondGestao(Integer ideCerhPondGestao) {
		this.ideCerhPondGestao = ideCerhPondGestao;
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
