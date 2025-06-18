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
@Table(name="fce_saa_localizacao_geografica_dados_concedidos")
public class FceSaaLocalizacaoGeograficaDadosConcedidos {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_saa_localizacao_geografica_dados_concedidos_seq")
	@SequenceGenerator(name = "fce_saa_localizacao_geografica_dados_concedidos_seq", sequenceName = "fce_saa_localizacao_geografica_dados_concedidos_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_saa_localizacao_geografica_dados_concedidos")
	private Integer ideFceSaaLocalizacaoGeograficaDadosConcedidos;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_saa", referencedColumnName="ide_fce_saa")
	private FceSaa ideFceSaa;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_tipo_captacao", referencedColumnName="ide_tipo_captacao")
	private TipoCaptacao ideTipoCaptacao;
	
	@Column(name="nom_corpo_hidrico")
	private String nomeCorpoHidrico;
	
	@Column(name="val_vazao")
	private Double valorVazao;
	
	@Column(name="num_portaria")
	private String numPortaria;
	
	@Transient
	private boolean localizacaoFinal;
	
	@Transient
	private boolean desabilitarLinha;

	public Integer getIdeFceSaaLocalizacaoGeograficaDadosConcedidos() {
		return ideFceSaaLocalizacaoGeograficaDadosConcedidos;
	}

	public void setIdeFceSaaLocalizacaoGeograficaDadosConcedidos(
			Integer ideFceSaaLocalizacaoGeograficaDadosConcedidos) {
		this.ideFceSaaLocalizacaoGeograficaDadosConcedidos = ideFceSaaLocalizacaoGeograficaDadosConcedidos;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public FceSaa getIdeFceSaa() {
		return ideFceSaa;
	}

	public void setIdeFceSaa(FceSaa ideFceSaa) {
		this.ideFceSaa = ideFceSaa;
	}

	public TipoCaptacao getIdeTipoCaptacao() {
		return ideTipoCaptacao;
	}

	public void setIdeTipoCaptacao(TipoCaptacao ideTipoCaptacao) {
		this.ideTipoCaptacao = ideTipoCaptacao;
	}

	public String getNomeCorpoHidrico() {
		return nomeCorpoHidrico;
	}

	public void setNomeCorpoHidrico(String nomeCorpoHidrico) {
		this.nomeCorpoHidrico = nomeCorpoHidrico;
	}

	public Double getValorVazao() {
		return valorVazao;
	}

	public void setValorVazao(Double valorVazao) {
		this.valorVazao = valorVazao;
	}

	public boolean isLocalizacaoFinal() {
		return localizacaoFinal;
	}

	public void setLocalizacaoFinal(boolean localizacaoFinal) {
		this.localizacaoFinal = localizacaoFinal;
	}

	public boolean isDesabilitarLinha() {
		return desabilitarLinha;
	}

	public void setDesabilitarLinha(boolean desabilitarLinha) {
		this.desabilitarLinha = desabilitarLinha;
	}

	public String getNumPortaria() {
		return numPortaria;
	}

	public void setNumPortaria(String numPortaria) {
		this.numPortaria = numPortaria;
	}
	
}
