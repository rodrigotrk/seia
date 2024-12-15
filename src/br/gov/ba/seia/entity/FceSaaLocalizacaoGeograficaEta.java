package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="fce_saa_localizacao_geografica_eta")
public class FceSaaLocalizacaoGeograficaEta {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_saa_localizacao_geografica_eta_seq")
	@SequenceGenerator(name = "fce_saa_localizacao_geografica_eta_seq", sequenceName = "fce_saa_localizacao_geografica_eta_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_saa_localizacao_geografica_eta")
	private Integer ideFceSaaLocalizacaoGeograficaEta;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_saa", referencedColumnName="ide_fce_saa")
	private FceSaa ideFceSaa;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@Column(name="nom_identificacao")
	private String nomeIdentificacao;
	
	@Column(name="val_vazao_media")
	private Double valorVazaoMedia;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEtaTipoComposicao")
	private List<EtaTipoComposicao> etaTipoComposicaoCollection;
	
	@Transient
	private boolean localizacaoFinal;
	
	@Transient
	private boolean desabilitarVazao;
	
	public Integer getIdeFceSaaLocalizacaoGeograficaEta() {
		return ideFceSaaLocalizacaoGeograficaEta;
	}

	public void setIdeFceSaaLocalizacaoGeograficaEta(
			Integer ideFceSaaLocalizacaoGeograficaEta) {
		this.ideFceSaaLocalizacaoGeograficaEta = ideFceSaaLocalizacaoGeograficaEta;
	}

	public FceSaa getIdeFceSaa() {
		return ideFceSaa;
	}

	public void setIdeFceSaa(FceSaa ideFceSaa) {
		this.ideFceSaa = ideFceSaa;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public String getNomeIdentificacao() {
		return nomeIdentificacao;
	}

	public void setNomeIdentificacao(String nomeIdentificacao) {
		this.nomeIdentificacao = nomeIdentificacao;
	}

	public Double getValorVazaoMedia() {
		return valorVazaoMedia;
	}

	public void setValorVazaoMedia(Double valorVazaoMedia) {
		this.valorVazaoMedia = valorVazaoMedia;
	}

	public boolean isLocalizacaoFinal() {
		return localizacaoFinal;
	}

	public void setLocalizacaoFinal(boolean localizacaoFinal) {
		this.localizacaoFinal = localizacaoFinal;
	}

	public List<EtaTipoComposicao> getEtaTipoComposicaoCollection() {
		return etaTipoComposicaoCollection;
	}

	public void setEtaTipoComposicaoCollection(
			List<EtaTipoComposicao> etaTipoComposicaoCollection) {
		this.etaTipoComposicaoCollection = etaTipoComposicaoCollection;
	}

	public boolean isDesabilitarVazao() {
		return desabilitarVazao;
	}

	public void setDesabilitarVazao(boolean desabilitarVazao) {
		this.desabilitarVazao = desabilitarVazao;
	}
	
}
