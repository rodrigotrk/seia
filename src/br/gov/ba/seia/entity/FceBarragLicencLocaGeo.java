/**
 * 
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name="fce_barrag_licenc_loca_geo")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "FceBarragLicencLocaGeo.listarByIdeFceBarragemLicenciamento", query = "SELECT f FROM FceBarragLicencLocaGeo f WHERE f.fceBarragemLicenciamento.ideFceBarragemLicenciamento = :ideFceBarragemLicenciamento"),
		@NamedQuery(name = "FceBarragLicencLocaGeo.removeByPk", query = "DELETE FROM FceBarragLicencLocaGeo f WHERE f.fceBarragLicencLocaGeoPK.ideFceBarragemLicenciamento = :ideFceBarragemLicenciamento and f.fceBarragLicencLocaGeoPK.ideLocalGeoInicioEixo = :ideLocalGeoInicioEixo and f.fceBarragLicencLocaGeoPK.ideLocalGeoFimEixo = :ideLocalGeoFimEixo")
})
public class FceBarragLicencLocaGeo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceBarragLicencLocaGeoPK fceBarragLicencLocaGeoPK;
	
	@NotNull
	@JoinColumn(name = "ide_local_geo_inicio_eixo", referencedColumnName = "ide_localizacao_geografica", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalGeoInicioEixo;
	
	@NotNull
	@JoinColumn(name = "ide_local_geo_fim_eixo", referencedColumnName = "ide_localizacao_geografica", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalGeoFimEixo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_fce_barragem_licenciamento", referencedColumnName = "ide_fce_barragem_licenciamento", nullable = false, insertable = false, updatable = false)
	private FceBarragemLicenciamento fceBarragemLicenciamento;
	
	@Column(name = "NOM_RIO")
	private String nomeRio;
	
	@Transient
	private Boolean confirmar;

	public FceBarragLicencLocaGeo(FceBarragemLicenciamento fceBarragemLicenciamento) {
		this.setFceBarragemLicenciamento(fceBarragemLicenciamento);
	}
	
	public FceBarragLicencLocaGeo(LocalizacaoGeografica ideLocalGeoInicioEixo, LocalizacaoGeografica ideLocalGeoFimEixo, FceBarragemLicenciamento fceBarragemLicenciamento) {
		this.setIdeLocalGeoInicioEixo(ideLocalGeoInicioEixo);
		this.setIdeLocalGeoFimEixo(ideLocalGeoFimEixo);
		this.setFceBarragemLicenciamento(fceBarragemLicenciamento);
		
		fceBarragLicencLocaGeoPK = new FceBarragLicencLocaGeoPK(fceBarragemLicenciamento.getIdeFceBarragemLicenciamento(), ideLocalGeoInicioEixo.getIdeLocalizacaoGeografica(), ideLocalGeoFimEixo.getIdeLocalizacaoGeografica());
	}
	
	public FceBarragLicencLocaGeo(){}
	
	public FceBarragLicencLocaGeo(boolean pontoMultiplo) {
		if (pontoMultiplo){
			ideLocalGeoInicioEixo = new LocalizacaoGeografica();
			ideLocalGeoFimEixo = new LocalizacaoGeografica();
		}
	}
	/**
	 * @return the fceBarragLicencLocaGeoPK
	 */
	public FceBarragLicencLocaGeoPK getFceBarragLicencLocaGeoPK() {
		return fceBarragLicencLocaGeoPK;
	}

	/**
	 * @param fceBarragLicencLocaGeoPK the fceBarragLicencLocaGeoPK to set
	 */
	public void setFceBarragLicencLocaGeoPK(
			FceBarragLicencLocaGeoPK fceBarragLicencLocaGeoPK) {
		this.fceBarragLicencLocaGeoPK = fceBarragLicencLocaGeoPK;
	}

	/**
	 * @return the nomeRio
	 */
	public String getNomeRio() {
		return nomeRio;
	}

	/**
	 * @param nomeRio the nomeRio to set
	 */
	public void setNomeRio(String nomeRio) {
		this.nomeRio = nomeRio;
	}

	public LocalizacaoGeografica getIdeLocalGeoInicioEixo() {
		return ideLocalGeoInicioEixo;
	}

	public void setIdeLocalGeoInicioEixo(LocalizacaoGeografica ideLocalGeoInicioEixo) {
		this.ideLocalGeoInicioEixo = ideLocalGeoInicioEixo;
	}

	public LocalizacaoGeografica getIdeLocalGeoFimEixo() {
		return ideLocalGeoFimEixo;
	}

	public void setIdeLocalGeoFimEixo(LocalizacaoGeografica ideLocalGeoFimEixo) {
		this.ideLocalGeoFimEixo = ideLocalGeoFimEixo;
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
	
	
	public Boolean getConfirmar() {
		if (Util.isNullOuVazio(confirmar)){
			if (!Util.isNullOuVazio(fceBarragLicencLocaGeoPK)) {
				confirmar = true;
			} else {
				confirmar = false;
			}
		}
		return confirmar;
	}

	public void setConfirmar(Boolean confirmar) {
		this.confirmar = confirmar;
	}
}
