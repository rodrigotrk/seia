package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * Entidade que representa a Poligonal ({@link LocalizacaoGeografica}) da APP ({@link TipoApp})  presente no {@link FceTurismo}.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 04/12/2014
 */
@Entity
@Table(name="fce_turismo_localizacao_geografica")
@NamedQuery(name = "FceTurismoLocalizacaoGeografica.removeByIdeFceTurismo", query = "DELETE FROM FceTurismoLocalizacaoGeografica f WHERE f.ideFceTurismo.ideFceTurismo = :ideFceTurismo")
public class FceTurismoLocalizacaoGeografica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_TURISMO_LOCALIZACAO_GEOGRAFICA_IDEFCETURISMOLOCALIZACAOGEOGRAFICA_GENERATOR", sequenceName="FCE_TURISMO_LOCALIZACAO_GEOGRAFICA_IDE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_TURISMO_LOCALIZACAO_GEOGRAFICA_IDEFCETURISMOLOCALIZACAOGEOGRAFICA_GENERATOR")
	@Column(name="ide_fce_turismo_localizacao_geografica")
	private Integer ideFceTurismoLocalizacaoGeografica;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_app", referencedColumnName="ide_tipo_app")
	private TipoApp ideTipoApp;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_turismo", referencedColumnName="ide_fce_turismo")
	private FceTurismo ideFceTurismo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_imovel", referencedColumnName="ide_imovel")
	private Imovel ideImovel;

	public FceTurismoLocalizacaoGeografica() {
	}

	public FceTurismoLocalizacaoGeografica(TipoApp ideTipoApp, FceTurismo ideFceTurismo, Imovel ideImovel) {
		this.ideTipoApp = ideTipoApp;
		this.ideFceTurismo = ideFceTurismo;
		this.ideImovel = ideImovel;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}
	
	public FceTurismoLocalizacaoGeografica(App app, FceTurismo ideFceTurismo, Imovel ideImovel) {
		this.ideTipoApp = app.getIdeTipoApp();
		this.ideLocalizacaoGeografica = app.getIdeLocalizacaoGeografica();
		this.ideFceTurismo = ideFceTurismo;
		this.ideImovel = ideImovel;
	}

	public Integer getIdeFceTurismoLocalizacaoGeografica() {
		return this.ideFceTurismoLocalizacaoGeografica;
	}

	public void setIdeFceTurismoLocalizacaoGeografica(Integer ideFceTurismoLocalizacaoGeografica) {
		this.ideFceTurismoLocalizacaoGeografica = ideFceTurismoLocalizacaoGeografica;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public TipoApp getIdeTipoApp() {
		return ideTipoApp;
	}

	public void setIdeTipoApp(TipoApp ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}

	public FceTurismo getIdeFceTurismo() {
		return ideFceTurismo;
	}

	public void setIdeFceTurismo(FceTurismo ideFceTurismo) {
		this.ideFceTurismo = ideFceTurismo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceTurismo == null) ? 0 : ideFceTurismo.hashCode());
		result = prime
				* result
				+ ((ideFceTurismoLocalizacaoGeografica == null) ? 0
						: ideFceTurismoLocalizacaoGeografica.hashCode());
		result = prime
				* result
				+ ((ideLocalizacaoGeografica == null) ? 0
						: ideLocalizacaoGeografica.hashCode());
		result = prime * result
				+ ((ideTipoApp == null) ? 0 : ideTipoApp.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		FceTurismoLocalizacaoGeografica other = (FceTurismoLocalizacaoGeografica) obj;
		if (ideFceTurismo == null) {
			if (other.ideFceTurismo != null) {
				return false;
			}
		} else if (!ideFceTurismo.equals(other.ideFceTurismo)) {
			return false;
		}
		if (ideFceTurismoLocalizacaoGeografica == null) {
			if (other.ideFceTurismoLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideFceTurismoLocalizacaoGeografica
				.equals(other.ideFceTurismoLocalizacaoGeografica)) {
			return false;
		}
		if (ideLocalizacaoGeografica == null) {
			if (other.ideLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideLocalizacaoGeografica
				.equals(other.ideLocalizacaoGeografica)) {
			return false;
		}
		if (ideTipoApp == null) {
			if (other.ideTipoApp != null) {
				return false;
			}
		} else if (!ideTipoApp.equals(other.ideTipoApp)) {
			return false;
		}
		return true;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

}