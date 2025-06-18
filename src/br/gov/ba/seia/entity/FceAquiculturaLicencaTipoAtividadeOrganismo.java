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
 * The persistent class for the fce_aquicultura_licenca_tipo_atividade_organismo database table.
 *
 */
@Entity
@Table(name="fce_aquicultura_licenca_tipo_atividade_organismo")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeOrganismo.findAll", query="SELECT f FROM FceAquiculturaLicencaTipoAtividadeOrganismo f"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeOrganismo.removeByIdeFceAquiculturaLicencaTipoAtividadeTipoProducao", query = "DELETE FROM FceAquiculturaLicencaTipoAtividadeOrganismo f WHERE f.fceAquiculturaLicencaTipoAtividadeTipoProducao.ideFceAquiculturaLicencaTipoAtividadeTipoProducao = :ideFceAquiculturaLicencaTipoAtividadeTipoProducao")
})
public class FceAquiculturaLicencaTipoAtividadeOrganismo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_ORGANISMO_IDEFCEAQUICULTURALICENCATIPOATIVIDADEORGANISMO_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_ORGANISMO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_ORGANISMO_IDEFCEAQUICULTURALICENCATIPOATIVIDADEORGANISMO_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca_tipo_atividade_organismo")
	private Integer ideFceAquiculturaLicencaTipoAtividadeOrganismo;

	//bi-directional many-to-one association to FceAquiculturaLicencaTipoAtividade
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao", referencedColumnName="ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao", nullable = false)
	private FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao;

	//bi-directional many-to-one association to Organismo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_organismo", referencedColumnName="ide_organismo", nullable = false)
	private Organismo organismo;

	public FceAquiculturaLicencaTipoAtividadeOrganismo() {
	}

	public FceAquiculturaLicencaTipoAtividadeOrganismo(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao, Organismo organismo) {
		this.setFceAquiculturaLicencaTipoAtividadeTipoProducao(fceAquiculturaLicencaTipoAtividadeTipoProducao);
		this.organismo = organismo;
	}

	public Integer getIdeFceAquiculturaLicencaTipoAtividadeOrganismo() {
		return this.ideFceAquiculturaLicencaTipoAtividadeOrganismo;
	}

	public void setIdeFceAquiculturaLicencaTipoAtividadeOrganismo(Integer ideFceAquiculturaLicencaTipoAtividadeOrganismo) {
		this.ideFceAquiculturaLicencaTipoAtividadeOrganismo = ideFceAquiculturaLicencaTipoAtividadeOrganismo;
	}

	public Organismo getOrganismo() {
		return this.organismo;
	}

	public void setOrganismo(Organismo organismo) {
		this.organismo = organismo;
	}

	public FceAquiculturaLicencaTipoAtividadeTipoProducao getFceAquiculturaLicencaTipoAtividadeTipoProducao() {
		return fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

	public void setFceAquiculturaLicencaTipoAtividadeTipoProducao(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		this.fceAquiculturaLicencaTipoAtividadeTipoProducao = fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

}