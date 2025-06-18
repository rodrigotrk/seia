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

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela associativa para vincular os {@link TipoLocalizacaoCultivo} que foram selecionados na <b>ABA - Tanque Rede</b> do FCE - Licenciamento para Aquicultura.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 01/06/2015
 */
@Entity
@Table(name="fce_aquicultura_licenca_tipo_localizacao_cultivo")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaLicencaTipoLocalizacaoCultivo.findAll", query="SELECT f FROM FceAquiculturaLicencaTipoLocalizacaoCultivo f"),
	@NamedQuery(name="FceAquiculturaLicencaTipoLocalizacaoCultivo.removeByIdeFceAquiculturaLicenca", query = "DELETE FROM FceAquiculturaLicencaTipoLocalizacaoCultivo f WHERE f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca = :ideFceAquiculturaLicenca"),
	@NamedQuery(name="FceAquiculturaLicencaTipoLocalizacaoCultivo.removeByIdeFceAquiculturaLicencaAndTipoAtividade", query = "DELETE FROM FceAquiculturaLicencaTipoLocalizacaoCultivo f WHERE f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca = :ideFceAquiculturaLicenca AND f.ideAquiculturaTipoAtividade.ideAquiculturaTipoAtividade = :ideAquiculturaTipoAtividade")
})
public class FceAquiculturaLicencaTipoLocalizacaoCultivo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_TIPO_LOCALIZACAO_CULTIVO_IDEFCEAQUICULTURALICENCATIPOLOCALIZACAOCULTIVO_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_TIPO_LOCALIZACAO_CULTIVO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_TIPO_LOCALIZACAO_CULTIVO_IDEFCEAQUICULTURALICENCATIPOLOCALIZACAOCULTIVO_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca_tipo_localizacao_cultivo")
	private Integer ideFceAquiculturaLicencaTipoLocalizacaoCultivo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura_licenca", referencedColumnName="ide_fce_aquicultura_licenca", nullable = false)
	private FceAquiculturaLicenca ideFceAquiculturaLicenca;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_localizacao_cultivo", referencedColumnName="ide_tipo_localizacao_cultivo", nullable = false)
	private TipoLocalizacaoCultivo ideTipoLocalizacaoCultivo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_aquicultura_tipo_atividade", referencedColumnName="ide_aquicultura_tipo_atividade", nullable = false)
	private AquiculturaTipoAtividade ideAquiculturaTipoAtividade;

	public FceAquiculturaLicencaTipoLocalizacaoCultivo() {
	}

	public FceAquiculturaLicencaTipoLocalizacaoCultivo(FceAquiculturaLicenca ideFceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade, TipoLocalizacaoCultivo ideTipoLocalizacaoCultivo) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
		this.ideAquiculturaTipoAtividade = aquiculturaTipoAtividade;
		this.ideTipoLocalizacaoCultivo = ideTipoLocalizacaoCultivo;
	}

	public Integer getIdeFceAquiculturaLicencaTipoLocalizacaoCultivo() {
		return this.ideFceAquiculturaLicencaTipoLocalizacaoCultivo;
	}

	public void setIdeFceAquiculturaLicencaTipoLocalizacaoCultivo(Integer ideFceAquiculturaLicencaTipoLocalizacaoCultivo) {
		this.ideFceAquiculturaLicencaTipoLocalizacaoCultivo = ideFceAquiculturaLicencaTipoLocalizacaoCultivo;
	}

	public FceAquiculturaLicenca getIdeFceAquiculturaLicenca() {
		return ideFceAquiculturaLicenca;
	}

	public void setIdeFceAquiculturaLicenca(FceAquiculturaLicenca ideFceAquiculturaLicenca) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
	}

	public TipoLocalizacaoCultivo getIdeTipoLocalizacaoCultivo() {
		return ideTipoLocalizacaoCultivo;
	}

	public void setIdeTipoLocalizacaoCultivo(TipoLocalizacaoCultivo ideTipoLocalizacaoCultivo) {
		this.ideTipoLocalizacaoCultivo = ideTipoLocalizacaoCultivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideFceAquiculturaLicencaTipoLocalizacaoCultivo);
	}

	public AquiculturaTipoAtividade getIdeAquiculturaTipoAtividade() {
		return ideAquiculturaTipoAtividade;
	}

	public void setIdeAquiculturaTipoAtividade(
			AquiculturaTipoAtividade ideAquiculturaTipoAtividade) {
		this.ideAquiculturaTipoAtividade = ideAquiculturaTipoAtividade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideAquiculturaTipoAtividade == null) ? 0
						: ideAquiculturaTipoAtividade.hashCode());
		result = prime
				* result
				+ ((ideFceAquiculturaLicenca == null) ? 0
						: ideFceAquiculturaLicenca.hashCode());
		result = prime
				* result
				+ ((ideFceAquiculturaLicencaTipoLocalizacaoCultivo == null) ? 0
						: ideFceAquiculturaLicencaTipoLocalizacaoCultivo
								.hashCode());
		result = prime
				* result
				+ ((ideTipoLocalizacaoCultivo == null) ? 0
						: ideTipoLocalizacaoCultivo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FceAquiculturaLicencaTipoLocalizacaoCultivo other = (FceAquiculturaLicencaTipoLocalizacaoCultivo) obj;
		if (ideAquiculturaTipoAtividade == null) {
			if (other.ideAquiculturaTipoAtividade != null)
				return false;
		} else if (!ideAquiculturaTipoAtividade
				.equals(other.ideAquiculturaTipoAtividade))
			return false;
		if (ideFceAquiculturaLicenca == null) {
			if (other.ideFceAquiculturaLicenca != null)
				return false;
		} else if (!ideFceAquiculturaLicenca
				.equals(other.ideFceAquiculturaLicenca))
			return false;
		if (ideFceAquiculturaLicencaTipoLocalizacaoCultivo == null) {
			if (other.ideFceAquiculturaLicencaTipoLocalizacaoCultivo != null)
				return false;
		} else if (!ideFceAquiculturaLicencaTipoLocalizacaoCultivo
				.equals(other.ideFceAquiculturaLicencaTipoLocalizacaoCultivo))
			return false;
		if (ideTipoLocalizacaoCultivo == null) {
			if (other.ideTipoLocalizacaoCultivo != null)
				return false;
		} else if (!ideTipoLocalizacaoCultivo
				.equals(other.ideTipoLocalizacaoCultivo))
			return false;
		return true;
	}

}