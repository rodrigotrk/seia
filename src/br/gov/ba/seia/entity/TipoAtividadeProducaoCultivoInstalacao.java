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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the tipo_atividade_producao_cultivo_instalacao database table.
 *
 */
@Entity
@Table(name="tipo_atividade_producao_cultivo_instalacao")
@NamedQuery(name="TipoAtividadeProducaoCultivoInstalacao.findAll", query="SELECT t FROM TipoAtividadeProducaoCultivoInstalacao t")
public class TipoAtividadeProducaoCultivoInstalacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_ATIVIDADE_PRODUCAO_CULTIVO_INSTALACAO_IDETIPOATIVIDADEPRODUCAOCULTIVOINSTALACAO_GENERATOR", sequenceName="TIPO_ATIVIDADE_PRODUCAO_CULTIVO_INSTALACAO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_ATIVIDADE_PRODUCAO_CULTIVO_INSTALACAO_IDETIPOATIVIDADEPRODUCAOCULTIVOINSTALACAO_GENERATOR")
	@Column(name="ide_tipo_atividade_producao_cultivo_instalacao")
	private Integer ideTipoAtividadeProducaoCultivoInstalacao;

	//bi-directional many-to-one association to AquiculturaTipoAtividade
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_aquicultura_tipo_atividade")
	private AquiculturaTipoAtividade ideAquiculturaTipoAtividade;

	//bi-directional many-to-one association to SistemaCultivo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_sistema_cultivo")
	private SistemaCultivo ideSistemaCultivo;

	//bi-directional many-to-one association to TipoInstalacao
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_instalacao")
	private TipoInstalacao ideTipoInstalacao;

	//bi-directional many-to-one association to TipoProducao
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_producao")
	private TipoProducao ideTipoProducao;

	public TipoAtividadeProducaoCultivoInstalacao() {
	}

	public Integer getIdeTipoAtividadeProducaoCultivoInstalacao() {
		return this.ideTipoAtividadeProducaoCultivoInstalacao;
	}

	public void setIdeTipoAtividadeProducaoCultivoInstalacao(Integer ideTipoAtividadeProducaoCultivoInstalacao) {
		this.ideTipoAtividadeProducaoCultivoInstalacao = ideTipoAtividadeProducaoCultivoInstalacao;
	}

	public AquiculturaTipoAtividade getIdeAquiculturaTipoAtividade() {
		return ideAquiculturaTipoAtividade;
	}

	public void setIdeAquiculturaTipoAtividade(AquiculturaTipoAtividade ideAquiculturaTipoAtividade) {
		this.ideAquiculturaTipoAtividade = ideAquiculturaTipoAtividade;
	}

	public SistemaCultivo getIdeSistemaCultivo() {
		return ideSistemaCultivo;
	}

	public void setIdeSistemaCultivo(SistemaCultivo ideSistemaCultivo) {
		this.ideSistemaCultivo = ideSistemaCultivo;
	}

	public TipoInstalacao getIdeTipoInstalacao() {
		return ideTipoInstalacao;
	}

	public void setIdeTipoInstalacao(TipoInstalacao ideTipoInstalacao) {
		this.ideTipoInstalacao = ideTipoInstalacao;
	}

	public TipoProducao getIdeTipoProducao() {
		return ideTipoProducao;
	}

	public void setIdeTipoProducao(TipoProducao ideTipoProducao) {
		this.ideTipoProducao = ideTipoProducao;
	}

}