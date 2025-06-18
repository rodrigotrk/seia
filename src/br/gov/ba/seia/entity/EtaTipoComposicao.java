package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="eta_tipo_composicao")
public class EtaTipoComposicao {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eta_tipo_composicao_seq")
	@SequenceGenerator(name = "eta_tipo_composicao_seq", sequenceName = "eta_tipo_composicao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_eta_tipo_composicao")
	private Integer ideEtaTipoComposicao;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_saa_localizacao_geografica_eta", referencedColumnName="ide_fce_saa_localizacao_geografica_eta")
	private FceSaaLocalizacaoGeograficaEta ideFceSaaLocalizacaoGeograficaEta;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_saa_tipo_composicao", referencedColumnName="ide_fce_saa_tipo_composicao")
	private FceSaaTipoComposicao ideFceSaaTipoComposicao;
	
	@Column(name="val_quantidade")
	private Integer valorQuantidade;
	
	@Transient
	private boolean desabilitarItem;

	public Integer getIdeEtaTipoComposicao() {
		return ideEtaTipoComposicao;
	}

	public void setIdeEtaTipoComposicao(Integer ideEtaTipoComposicao) {
		this.ideEtaTipoComposicao = ideEtaTipoComposicao;
	}

	public FceSaaLocalizacaoGeograficaEta getIdeFceSaaLocalizacaoGeograficaEta() {
		return ideFceSaaLocalizacaoGeograficaEta;
	}

	public void setIdeFceSaaLocalizacaoGeograficaEta(
			FceSaaLocalizacaoGeograficaEta ideFceSaaLocalizacaoGeograficaEta) {
		this.ideFceSaaLocalizacaoGeograficaEta = ideFceSaaLocalizacaoGeograficaEta;
	}

	public FceSaaTipoComposicao getIdeFceSaaTipoComposicao() {
		return ideFceSaaTipoComposicao;
	}

	public void setIdeFceSaaTipoComposicao(
			FceSaaTipoComposicao ideFceSaaTipoComposicao) {
		this.ideFceSaaTipoComposicao = ideFceSaaTipoComposicao;
	}

	public Integer getValorQuantidade() {
		return valorQuantidade;
	}

	public void setValorQuantidade(Integer valorQuantidade) {
		this.valorQuantidade = valorQuantidade;
	}

	public boolean getDesabilitarItem() {
		return desabilitarItem;
	}

	public void setDesabilitarItem(boolean desabilitarItem) {
		this.desabilitarItem = desabilitarItem;
	}
	
}
