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

import br.gov.ba.seia.enumerator.TipoProducaoEnum;


/**
 * The persistent class for the fce_aquicultura_licenca_tipo_atividade_tipo_producao database table.
 *
 */
@Entity
@Table(name="fce_aquicultura_licenca_tipo_atividade_tipo_producao")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeTipoProducao.findAll", query="SELECT f FROM FceAquiculturaLicencaTipoAtividadeTipoProducao f"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividadeTipoProducao.removeByIdeFceAquiculturaLicencaTipoAtividade", query = "DELETE FROM FceAquiculturaLicencaTipoAtividadeTipoProducao f WHERE f.fceAquiculturaLicencaTipoAtividade.ideFceAquiculturaLicencaTipoAtividade = :ideFceAquiculturaLicencaTipoAtividade")
})
public class FceAquiculturaLicencaTipoAtividadeTipoProducao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_TIPO_PRODUCAO_IDEFCEAQUICULTURALICENCATIPOATIVIDADETIPOPRODUCAO_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_TIPO_PRODUCAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_TIPO_PRODUCAO_IDEFCEAQUICULTURALICENCATIPOATIVIDADETIPOPRODUCAO_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao")
	private Integer ideFceAquiculturaLicencaTipoAtividadeTipoProducao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_producao", referencedColumnName="ide_tipo_producao", nullable = false)
	private TipoProducao ideTipoProducao;

	//bi-directional many-to-one association to FceAquiculturaLicencaTipoAtividade
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura_licenca_tipo_atividade", referencedColumnName="ide_fce_aquicultura_licenca_tipo_atividade", nullable = false )
	private FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade;

	@Column(name="num_area_cultivo")
	private BigDecimal numAreaCultivo;

	@Column(name="num_volume_cultivo")
	private BigDecimal numVolumeCultivo;

	@Column(name="num_producao")
	private BigDecimal numProducao;

	@Transient
	private boolean tipoInstalacaoRaceways;
	
	public FceAquiculturaLicencaTipoAtividadeTipoProducao() {

	}

	public FceAquiculturaLicencaTipoAtividadeTipoProducao(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade, TipoProducaoEnum ideTipoProducaoEnum) {
		this.ideTipoProducao = new TipoProducao(ideTipoProducaoEnum.getId());
		this.fceAquiculturaLicencaTipoAtividade = fceAquiculturaLicencaTipoAtividade;
	}

	public Integer getIdeFceAquiculturaLicencaTipoAtividadeTipoProducao() {
		return this.ideFceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

	public void setIdeFceAquiculturaLicencaTipoAtividadeTipoProducao(Integer ideFceAquiculturaLicencaTipoAtividadeTipoProducao) {
		this.ideFceAquiculturaLicencaTipoAtividadeTipoProducao = ideFceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

	public TipoProducao getIdeTipoProducao() {
		return this.ideTipoProducao;
	}

	public void setIdeTipoProducao(TipoProducao ideTipoProducao) {
		this.ideTipoProducao = ideTipoProducao;
	}

	public FceAquiculturaLicencaTipoAtividade getFceAquiculturaLicencaTipoAtividade() {
		return this.fceAquiculturaLicencaTipoAtividade;
	}

	public void setFceAquiculturaLicencaTipoAtividade(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		this.fceAquiculturaLicencaTipoAtividade = fceAquiculturaLicencaTipoAtividade;
	}

	public BigDecimal getNumAreaCultivo() {
		return numAreaCultivo;
	}

	public void setNumAreaCultivo(BigDecimal numAreaCultivo) {
		this.numAreaCultivo = numAreaCultivo;
	}

	public BigDecimal getNumVolumeCultivo() {
		return numVolumeCultivo;
	}

	public void setNumVolumeCultivo(BigDecimal numVolumeCultivo) {
		this.numVolumeCultivo = numVolumeCultivo;
	}

	public BigDecimal getNumProducao() {
		return numProducao;
	}

	public void setNumProducao(BigDecimal numProducao) {
		this.numProducao = numProducao;
	}

	public boolean isTipoInstalacaoRaceways() {
		return tipoInstalacaoRaceways;
	}

	public void setTipoInstalacaoRaceways(boolean tipoInstalacaoRaceways) {
		this.tipoInstalacaoRaceways = tipoInstalacaoRaceways;
	}

}