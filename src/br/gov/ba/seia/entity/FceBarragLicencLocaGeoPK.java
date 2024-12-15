/**
 * 
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author lesantos
 *
 */
@Embeddable
public class FceBarragLicencLocaGeoPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_barragem_licenciamento")
	private Integer ideFceBarragemLicenciamento;
	
	@Column(name = "ide_local_geo_inicio_eixo")
	private Integer ideLocalGeoInicioEixo;
	
	@Column(name = "ide_local_geo_fim_eixo")
	private Integer ideLocalGeoFimEixo;

	public FceBarragLicencLocaGeoPK() {
		// Auto-generated constructor stub
	}
	
	public FceBarragLicencLocaGeoPK(Integer ideFceBarragemLicenciamento, Integer ideLocalGeoInicioEixo, Integer ideLocalGeoFimEixo) {
		this.ideFceBarragemLicenciamento = ideFceBarragemLicenciamento;
		this.ideLocalGeoInicioEixo = ideLocalGeoInicioEixo;
		this.ideLocalGeoFimEixo = ideLocalGeoFimEixo;
	}

	/**
	 * @return the ideFceBarragemLicenciamento
	 */
	public Integer getIdeFceBarragemLicenciamento() {
		return ideFceBarragemLicenciamento;
	}

	/**
	 * @param ideFceBarragemLicenciamento the ideFceBarragemLicenciamento to set
	 */
	public void setIdeFceBarragemLicenciamento(Integer ideFceBarragemLicenciamento) {
		this.ideFceBarragemLicenciamento = ideFceBarragemLicenciamento;
	}

	public Integer getIdeLocalGeoInicioEixo() {
		return ideLocalGeoInicioEixo;
	}

	public void setIdeLocalGeoInicioEixo(Integer ideLocalGeoInicioEixo) {
		this.ideLocalGeoInicioEixo = ideLocalGeoInicioEixo;
	}

	public Integer getIdeLocalGeoFimEixo() {
		return ideLocalGeoFimEixo;
	}

	public void setIdeLocalGeoFimEixo(Integer ideLocalGeoFimEixo) {
		this.ideLocalGeoFimEixo = ideLocalGeoFimEixo;
	}
}
