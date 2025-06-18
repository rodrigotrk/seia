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
 * The persistent class for the fce_aquicultura_licenca_tipo_atividade_sistema_cultivo database table.
 *
 */
@Entity
@Table(name="fce_aquicultura_licenca_tipo_atividade_sistema_cultivo")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeSistemaCultivo.findAll", query="SELECT f FROM FceAquiculturaLicencaTipoAtividadeSistemaCultivo f"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeSistemaCultivo.removeByIdeFceAquiculturaLicencaTipoAtividadeTipoProducao", query = "DELETE FROM FceAquiculturaLicencaTipoAtividadeSistemaCultivo f WHERE f.fceAquiculturaLicencaTipoAtividadeTipoProducao.ideFceAquiculturaLicencaTipoAtividadeTipoProducao = :ideFceAquiculturaLicencaTipoAtividadeTipoProducao")
})
public class FceAquiculturaLicencaTipoAtividadeSistemaCultivo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_SISTEMA_CULTIVO_IDEFCEAQUICULTURALICENCATIPOATIVIDADESISTEMACULTIVO_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_SISTEMA_CULTIVO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_SISTEMA_CULTIVO_IDEFCEAQUICULTURALICENCATIPOATIVIDADESISTEMACULTIVO_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca_tipo_atividade_sistema_cultivo")
	private Integer ideFceAquiculturaLicencaTipoAtividadeSistemaCultivo;

	//bi-directional many-to-one association to FceAquiculturaLicencaTipoAtividade
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao", referencedColumnName="ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao", nullable = false)
	private FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao;

	//bi-directional many-to-one association to SistemaCultivo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_sistema_cultivo", referencedColumnName="ide_sistema_cultivo", nullable = false)
	private SistemaCultivo sistemaCultivo;

	public FceAquiculturaLicencaTipoAtividadeSistemaCultivo() {

	}

	public FceAquiculturaLicencaTipoAtividadeSistemaCultivo(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao, SistemaCultivo sistemaCultivo) {
		this.setFceAquiculturaLicencaTipoAtividadeTipoProducao(fceAquiculturaLicencaTipoAtividadeTipoProducao);
		this.sistemaCultivo = sistemaCultivo;
	}

	public Integer getIdeFceAquiculturaLicencaTipoAtividadeSistemaCultivo() {
		return this.ideFceAquiculturaLicencaTipoAtividadeSistemaCultivo;
	}

	public void setIdeFceAquiculturaLicencaTipoAtividadeSistemaCultivo(Integer ideFceAquiculturaLicencaTipoAtividadeSistemaCultivo) {
		this.ideFceAquiculturaLicencaTipoAtividadeSistemaCultivo = ideFceAquiculturaLicencaTipoAtividadeSistemaCultivo;
	}

	public SistemaCultivo getSistemaCultivo() {
		return this.sistemaCultivo;
	}

	public void setSistemaCultivo(SistemaCultivo sistemaCultivo) {
		this.sistemaCultivo = sistemaCultivo;
	}

	public FceAquiculturaLicencaTipoAtividadeTipoProducao getFceAquiculturaLicencaTipoAtividadeTipoProducao() {
		return fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

	public void setFceAquiculturaLicencaTipoAtividadeTipoProducao(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		this.fceAquiculturaLicencaTipoAtividadeTipoProducao = fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

}