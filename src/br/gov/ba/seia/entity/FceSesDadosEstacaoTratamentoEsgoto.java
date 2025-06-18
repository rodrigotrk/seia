package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Basic;
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

@Entity
@Table(name="fce_ses_dados_estacao_tratam_esgoto")
public class FceSesDadosEstacaoTratamentoEsgoto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_dados_estacao_tratam_esgoto_seq")
	@SequenceGenerator(name = "fce_ses_dados_estacao_tratam_esgoto_seq", sequenceName = "fce_ses_dados_estacao_tratam_esgoto_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_dados_estacao_tratam_esgoto", nullable = false)
	private Integer ideFceSesDadosTramentoEsgoto;
	
	@Column(name="nom_estacao")
	private String nomeEstacao;
	
	@Column(name="val_vazao_med_tratamento")
	private Double valorVazaoMedia;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses", referencedColumnName="ide_fce_ses")
	private FceSes ideFceSes;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@OneToMany(mappedBy = "ideFceSesDadosEstacaoTipoComposicao")
	private List<FceSesDadosEstacaoTipoComposicao> dadosEstacaoTipoComposicaoCollection;

	public Integer getIdeFceSesDadosTramentoEsgoto() {
		return ideFceSesDadosTramentoEsgoto;
	}

	public void setIdeFceSesDadosTramentoEsgoto(Integer ideFceSesDadosTramentoEsgoto) {
		this.ideFceSesDadosTramentoEsgoto = ideFceSesDadosTramentoEsgoto;
	}

	public String getNomeEstacao() {
		return nomeEstacao;
	}

	public void setNomeEstacao(String nomeEstacao) {
		this.nomeEstacao = nomeEstacao;
	}

	public Double getValorVazaoMedia() {
		return valorVazaoMedia;
	}

	public void setValorVazaoMedia(Double valorVazaoMedia) {
		this.valorVazaoMedia = valorVazaoMedia;
	}

	public FceSes getIdeFceSes() {
		return ideFceSes;
	}

	public void setIdeFceSes(FceSes ideFceSes) {
		this.ideFceSes = ideFceSes;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public List<FceSesDadosEstacaoTipoComposicao> getDadosEstacaoTipoComposicaoCollection() {
		return dadosEstacaoTipoComposicaoCollection;
	}

	public void setDadosEstacaoTipoComposicaoCollection(
			List<FceSesDadosEstacaoTipoComposicao> dadosEstacaoTipoComposicaoCollection) {
		this.dadosEstacaoTipoComposicaoCollection = dadosEstacaoTipoComposicaoCollection;
	}
	
	
}
