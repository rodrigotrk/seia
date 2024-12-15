package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_pond_vol_consumido")
public class CerhPondVolConsumido extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_pond_vol_consumido_seq")
	@SequenceGenerator(name = "cerh_pond_vol_consumido_seq", sequenceName = "cerh_pond_vol_consumido_seq", allocationSize = 1)
	@Column( name = "ide_cerh_pond_vol_consumido")
	private Integer ideCerhPondVolConsumido;
	
	@Column(name = "ide_geo_rpga")
	private Integer geoRpga;
	
	@Column(name = "vlr_referencia")
	private Double vlrReferencia;
	
	@Column(name = "dt_cadastro")
	private Date dtCadastro;
	
	@Column(name = "ind_excluido")
	private boolean indExcluido;
	
	@Column(name = "dt_excluido")
	private Date dtExcluido;
	
	public Integer getIdeCerhPondVolConsumido() {
		return ideCerhPondVolConsumido;
	}

	public void setIdeCerhPondVolConsumido(Integer ideCerhPondVolConsumido) {
		this.ideCerhPondVolConsumido = ideCerhPondVolConsumido;
	}

	public Double getVlrReferencia() {
		return vlrReferencia;
	}

	public Integer getGeoRpga() {
		return geoRpga;
	}

	public void setGeoRpga(Integer geoRpga) {
		this.geoRpga = geoRpga;
	}

	public void setVlrReferencia(Double vlrReferencia) {
		this.vlrReferencia = vlrReferencia;
	}

	public Date getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtExcluido() {
		return dtExcluido;
	}

	public void setDtExcluido(Date dtExcluido) {
		this.dtExcluido = dtExcluido;
	}
}
