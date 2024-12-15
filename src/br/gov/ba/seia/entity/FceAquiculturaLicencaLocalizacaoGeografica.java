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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

/**
 * Tabela criada para armazenar os pontos de {@link LocalizacaoGeografica}
 * quando não houver {@link Outorga} associada ao {@link Requerimento}.
 * <ul>
 * <li>302 - Captação superficial</li>
 * <li>303 - Captação subterrânea</li>
 * <li>304 - Lançamento de efluentes</li>
 * <li>305 - Intervenção</li>
 * </ul>
 * <pre><b> #6934 </b></pre>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 */
@Entity
@Table(name = "fce_aquicultura_licenca_localizacao_geografica")
@NamedQueries({
	@NamedQuery(name = "FceAquiculturaLicencaLocalizacaoGeografica.findAll", query = "SELECT f FROM FceAquiculturaLicencaLocalizacaoGeografica f"),
	@NamedQuery(name = "FceAquiculturaLicencaLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndNotIdeTipologia", query = "DELETE FROM FceAquiculturaLicencaLocalizacaoGeografica f WHERE f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca = :ideFceAquiculturaLicenca AND f.ideTipologia.ideTipologia <> :ideTipologia"),
	@NamedQuery(name = "FceAquiculturaLicencaLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndIdeTipologia", query = "DELETE FROM FceAquiculturaLicencaLocalizacaoGeografica f WHERE f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca = :ideFceAquiculturaLicenca AND f.ideTipologia.ideTipologia = :ideTipologia"),
	@NamedQuery(name = "FceAquiculturaLicencaLocalizacaoGeografica.removeByIde", query = "DELETE FROM FceAquiculturaLicencaLocalizacaoGeografica f WHERE f.ideFceAquiculturaLicencaLocalizacaoGeografica =:ideFceAquiculturaLicencaLocalizacaoGeografica") 
	})
public class FceAquiculturaLicencaLocalizacaoGeografica implements
Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FCE_AQUICULTURA_LICENCA_LOCALIZACAO_GEOGRAFICA_IDEFCEAQUICULTURALICENCALOCALIZACAOGEOGRAFICA_GENERATOR", sequenceName = "FCE_AQUICULTURA_LICENCA_LOCALIZACAO_GEOGRAFICA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FCE_AQUICULTURA_LICENCA_LOCALIZACAO_GEOGRAFICA_IDEFCEAQUICULTURALICENCALOCALIZACAOGEOGRAFICA_GENERATOR")
	@Column(name = "ide_fce_aquicultura_licenca_localizacao_geografica")
	private Integer ideFceAquiculturaLicencaLocalizacaoGeografica;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_aquicultura_licenca", referencedColumnName = "ide_fce_aquicultura_licenca")
	private FceAquiculturaLicenca ideFceAquiculturaLicenca;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
	private Tipologia ideTipologia;

	@Column(name="nom_rio")
	private String nomRio;

	@Transient
	private boolean confirmado;

	public FceAquiculturaLicencaLocalizacaoGeografica() {

	}

	public FceAquiculturaLicencaLocalizacaoGeografica(FceAquiculturaLicenca fceAquiculturaLicenca) {
		this.ideFceAquiculturaLicenca = fceAquiculturaLicenca;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public FceAquiculturaLicencaLocalizacaoGeografica(FceAquiculturaLicenca ideFceAquiculturaLicenca, Tipologia ideTipologia) {
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
		this.ideTipologia = ideTipologia;
	}

	public Integer getIdeFceAquiculturaLicencaLocalizacaoGeografica() {
		return this.ideFceAquiculturaLicencaLocalizacaoGeografica;
	}

	public void setIdeFceAquiculturaLicencaLocalizacaoGeografica(
			Integer ideFceAquiculturaLicencaLocalizacaoGeografica) {
		this.ideFceAquiculturaLicencaLocalizacaoGeografica = ideFceAquiculturaLicencaLocalizacaoGeografica;
	}

	public FceAquiculturaLicenca getIdeFceAquiculturaLicenca() {
		return ideFceAquiculturaLicenca;
	}

	public void setIdeFceAquiculturaLicenca(
			FceAquiculturaLicenca ideFceAquiculturaLicenca) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
		if(isSemCorpoHidrico()){
			setNomRio(null);
		}
	}

	@Override
	public Long getId() {
		return new Long(this.ideFceAquiculturaLicencaLocalizacaoGeografica);
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isCoordenadaSalva(){
		return this.ideLocalizacaoGeografica.getPossuiLocGeografica();
	}

	public boolean isIntervencaoSalva(){
		return !Util.isNullOuVazio(this.ideLocalizacaoGeografica);
	}

	public boolean isSemCorpoHidrico(){
		return !Util.isNullOuVazio(this.ideTipologia) && this.ideTipologia.getIdeTipologia().equals(TipologiaEnum.CAPTACAO_SUBTERRANEA.getId());
	}
	
	public String getLatitude(){
		return ideLocalizacaoGeografica.getLatitudeInicial();
	}
	
	public String getLongitude(){
		return ideLocalizacaoGeografica.getLongitudeInicial();
	}

	public String getNomSistemaCoordenada(){
		if(Util.isNullOuVazio(this.ideLocalizacaoGeografica.getNomeSistemaCoordenadas())){
			return "--";
		} else {
			return this.ideLocalizacaoGeografica.getNomeSistemaCoordenadas();
		}
	}

	public String getNomRio() {
		return nomRio;
	}

	public void setNomRio(String nomRio) {
		this.nomRio = nomRio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceAquiculturaLicenca == null) ? 0
						: ideFceAquiculturaLicenca.hashCode());
		result = prime
				* result
				+ ((ideFceAquiculturaLicencaLocalizacaoGeografica == null) ? 0
						: ideFceAquiculturaLicencaLocalizacaoGeografica
						.hashCode());
		result = prime
				* result
				+ ((ideLocalizacaoGeografica == null) ? 0
						: ideLocalizacaoGeografica.hashCode());
		result = prime * result
				+ ((ideTipologia == null) ? 0 : ideTipologia.hashCode());
		result = prime * result + ((nomRio == null) ? 0 : nomRio.hashCode());
		return result;
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
		FceAquiculturaLicencaLocalizacaoGeografica other = (FceAquiculturaLicencaLocalizacaoGeografica) obj;
		if (ideFceAquiculturaLicenca == null) {
			if (other.ideFceAquiculturaLicenca != null) {
				return false;
			}
		} else if (!ideFceAquiculturaLicenca
				.equals(other.ideFceAquiculturaLicenca)) {
			return false;
		}
		if (ideFceAquiculturaLicencaLocalizacaoGeografica == null) {
			if (other.ideFceAquiculturaLicencaLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideFceAquiculturaLicencaLocalizacaoGeografica
				.equals(other.ideFceAquiculturaLicencaLocalizacaoGeografica)) {
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
		if (ideTipologia == null) {
			if (other.ideTipologia != null) {
				return false;
			}
		} else if (!ideTipologia.equals(other.ideTipologia)) {
			return false;
		}
		if (nomRio == null) {
			if (other.nomRio != null) {
				return false;
			}
		} else if (!nomRio.equals(other.nomRio)) {
			return false;
		}
		return true;
	}
}