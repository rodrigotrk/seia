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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="fce_ses_lancamento_efluentes")
public class FceSesCoordenadasLancamentoLocalizacaoGeografica {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_lancamento_efluente_seq")
	@SequenceGenerator(name = "fce_ses_lancamento_efluente_seq", sequenceName = "fce_ses_lancamento_efluente_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_lancamento_efluente", nullable = false)
	private Integer ideFceSesLancamentoLocalizacaoGeografica;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@OneToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses", referencedColumnName="ide_fce_ses")
	private FceSes ideFceSes;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_tipo_periodo_derivacao", referencedColumnName="ide_tipo_periodo_derivacao")
	private TipoPeriodoDerivacao ideTipoDerivacao;
	
	@Column(name="nom_corpo_hidrico") 
	private String nomeCoporHidrico;
	
	@Column(name="num_portaria")
	private String numPortaria; 
	
	@Column(name="val_vazao_media")
	private Double valorVazaoMedia;
	
	@Transient
	private boolean localizacaoFinal;
	
	@Transient
	private boolean desabilitarItem;

	public Integer getIdeFceSesLancamentoLocalizacaoGeografica() {
		return ideFceSesLancamentoLocalizacaoGeografica;
	}


	public void setIdeFceSesLancamentoLocalizacaoGeografica(
			Integer ideFceSesLancamentoLocalizacaoGeografica) {
		this.ideFceSesLancamentoLocalizacaoGeografica = ideFceSesLancamentoLocalizacaoGeografica;
	}


	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}


	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}


	public FceSes getIdeFceSes() {
		return ideFceSes;
	}


	public void setIdeFceSes(FceSes ideFceSes) {
		this.ideFceSes = ideFceSes;
	}


	public TipoPeriodoDerivacao getIdeTipoDerivacao() {
		return ideTipoDerivacao;
	}


	public void setIdeTipoDerivacao(TipoPeriodoDerivacao ideTipoDerivacao) {
		this.ideTipoDerivacao = ideTipoDerivacao;
	}


	public String getNomeCoporHidrico() {
		return nomeCoporHidrico;
	}


	public void setNomeCoporHidrico(String nomeCoporHidrico) {
		this.nomeCoporHidrico = nomeCoporHidrico;
	}


	public String getNumPortaria() {
		return numPortaria;
	}


	public void setNumPortaria(String numPortaria) {
		this.numPortaria = numPortaria;
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


	public boolean isDesabilitarItem() {
		return desabilitarItem;
	}


	public void setDesabilitarItem(boolean desabilitarItem) {
		this.desabilitarItem = desabilitarItem;
	}

}
