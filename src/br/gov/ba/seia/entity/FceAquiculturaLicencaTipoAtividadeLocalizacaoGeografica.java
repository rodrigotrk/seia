package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.util.Util;


/**
 * Tabela criada para armazenar as <i>Poligonais das áreas de Cultivo</i>.
 *	<ul>
 *		<li>1 - Psicultura
 *		<li>2 - Carcinicultura
 *		<li>3 - Ranicultura
 *		<li>4 - Algicultura
 *		<li>5 - Malococultura
 *	</ul>
 *  <pre><b> #6934 </b></pre>
 *	@author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 */
@Entity
@Table(name="fce_aquicultura_licenca_tipo_atividade_localizacao_geografica")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.findAll", query="SELECT f FROM FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica f"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndTipoAquicultura", query = "DELETE FROM FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica f WHERE f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca = :ideFceAquiculturaLicenca AND f.ideTipoAquicultura.ideTipoAquicultura = :ideTipoAquicultura"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndTipoAquiculturaAndTipoAtividade", query = "DELETE FROM FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica f WHERE f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca = :ideFceAquiculturaLicenca AND f.ideTipoAquicultura.ideTipoAquicultura = :ideTipoAquicultura  AND f.ideAquiculturaTipoAtividade.ideAquiculturaTipoAtividade =:ideAquiculturaTipoAtividade"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.removeByIde", query = "DELETE FROM FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica f WHERE f.ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica =:ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica")
})
public class FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_LOCALIZACAO_GEOGRAFICA_IDEFCEAQUICULTURALICENCATIPOATIVIDADELOCALIZACAOGEOGRAFI_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_LOC_GEO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_LOCALIZACAO_GEOGRAFICA_IDEFCEAQUICULTURALICENCATIPOATIVIDADELOCALIZACAOGEOGRAFI_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca_tipo_atividade_localizacao_geografi")
	private Integer ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica", nullable = false)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura_licenca", referencedColumnName="ide_fce_aquicultura_licenca", nullable = false)
	private FceAquiculturaLicenca ideFceAquiculturaLicenca;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_aquicultura_tipo_atividade", referencedColumnName="ide_aquicultura_tipo_atividade", nullable = false)
	private AquiculturaTipoAtividade ideAquiculturaTipoAtividade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_aquicultura", referencedColumnName="ide_tipo_aquicultura", nullable = false)
	private TipoAquicultura ideTipoAquicultura;
	
	@Transient
	private BigDecimal areaPoligonal;

	public FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica() {

	}
	public FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(FceAquiculturaLicenca ideFceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade, TipoAquiculturaEnum aquiculturaEnum) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
		this.ideTipoAquicultura = new TipoAquicultura(aquiculturaEnum);
		this.ideAquiculturaTipoAtividade = aquiculturaTipoAtividade;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(FceAquiculturaLicenca ideFceAquiculturaLicenca, AquiculturaTipoAtividade ideAquiculturaTipoAtividade) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
		this.ideAquiculturaTipoAtividade = ideAquiculturaTipoAtividade;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public Integer getIdeFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica() {
		return this.ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
	}

	public void setIdeFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(Integer ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica) {
		this.ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica = ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public AquiculturaTipoAtividade getIdeAquiculturaTipoAtividade() {
		return ideAquiculturaTipoAtividade;
	}

	public void setIdeAquiculturaTipoAtividade(AquiculturaTipoAtividade ideAquiculturaTipoAtividade) {
		this.ideAquiculturaTipoAtividade = ideAquiculturaTipoAtividade;
	}

	public FceAquiculturaLicenca getIdeFceAquiculturaLicenca() {
		return ideFceAquiculturaLicenca;
	}

	public void setIdeFceAquiculturaLicenca(FceAquiculturaLicenca ideFceAquiculturaLicenca) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
	}

	public boolean isShapeSalvo() {
		return !Util.isNullOuVazio(this.ideLocalizacaoGeografica);
	}
	public BigDecimal getAreaPoligonal() {
		return areaPoligonal;
	}
	public void setAreaPoligonal(BigDecimal areaPoligonal) {
		this.areaPoligonal = areaPoligonal;
	}
	public TipoAquicultura getIdeTipoAquicultura() {
		return ideTipoAquicultura;
	}
	public void setIdeTipoAquicultura(TipoAquicultura ideTipoAquicultura) {
		this.ideTipoAquicultura = ideTipoAquicultura;
	}
}