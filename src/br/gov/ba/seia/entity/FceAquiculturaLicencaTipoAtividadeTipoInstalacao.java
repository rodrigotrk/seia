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


/**
 * The persistent class for the fce_aquicultura_licenca_tipo_atividade_tipo_instalacao database table.
 *
 */
@Entity
@Table(name="fce_aquicultura_licenca_tipo_atividade_tipo_instalacao")

@NamedQueries({
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeTipoInstalacao.findAll", query="SELECT f FROM FceAquiculturaLicencaTipoAtividadeTipoInstalacao f"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeTipoInstalacao.removeByIdeFceAquiculturaLicencaTipoAtividadeTipoProducao", query = "DELETE FROM FceAquiculturaLicencaTipoAtividadeTipoInstalacao f WHERE f.fceAquiculturaLicencaTipoAtividadeTipoProducao.ideFceAquiculturaLicencaTipoAtividadeTipoProducao = :ideFceAquiculturaLicencaTipoAtividadeTipoProducao")
})
public class FceAquiculturaLicencaTipoAtividadeTipoInstalacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_TIPO_INSTALACAO_IDEFCEAQUICULTURALICENCATIPOATIVIDADETIPOINSTALACAO_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_TIPO_INSTALACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_TIPO_INSTALACAO_IDEFCEAQUICULTURALICENCATIPOATIVIDADETIPOINSTALACAO_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca_tipo_atividade_tipo_instalacao")
	private Integer ideFceAquiculturaLicencaTipoAtividadeTipoInstalacao;

	//bi-directional many-to-one association to FceAquiculturaLicencaTipoAtividade
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao", referencedColumnName="ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao", nullable = false)
	private FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao;

	//bi-directional many-to-one association to TipoInstalacao
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_instalacao", referencedColumnName="ide_tipo_instalacao", nullable = false)
	private TipoInstalacao tipoInstalacao;

	@Column(name="num_estrutura")
	private Integer numEstrutura;

	public FceAquiculturaLicencaTipoAtividadeTipoInstalacao() {

	}

	public FceAquiculturaLicencaTipoAtividadeTipoInstalacao(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao, TipoInstalacao tipoInstalacao) {
		this.fceAquiculturaLicencaTipoAtividadeTipoProducao = fceAquiculturaLicencaTipoAtividadeTipoProducao;
		this.tipoInstalacao =tipoInstalacao;
		this.numEstrutura = tipoInstalacao.getNumEstrutura();
	}

	public Integer getIdeFceAquiculturaLicencaTipoAtividadeTipoInstalacao() {
		return this.ideFceAquiculturaLicencaTipoAtividadeTipoInstalacao;
	}

	public void setIdeFceAquiculturaLicencaTipoAtividadeTipoInstalacao(Integer ideFceAquiculturaLicencaTipoAtividadeTipoInstalacao) {
		this.ideFceAquiculturaLicencaTipoAtividadeTipoInstalacao = ideFceAquiculturaLicencaTipoAtividadeTipoInstalacao;
	}

	public TipoInstalacao getTipoInstalacao() {
		return this.tipoInstalacao;
	}

	public void setTipoInstalacao(TipoInstalacao tipoInstalacao) {
		this.tipoInstalacao = tipoInstalacao;
	}

	public Integer getNumEstrutura() {
		return numEstrutura;
	}

	public void setNumEstrutura(Integer numeroEstrutura) {
		this.numEstrutura = numeroEstrutura;
	}

	public FceAquiculturaLicencaTipoAtividadeTipoProducao getFceAquiculturaLicencaTipoAtividadeTipoProducao() {
		return fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

	public void setFceAquiculturaLicencaTipoAtividadeTipoProducao(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		this.fceAquiculturaLicencaTipoAtividadeTipoProducao = fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

}