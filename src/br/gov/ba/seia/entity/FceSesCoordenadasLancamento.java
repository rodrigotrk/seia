package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="fce_ses_coordenadas_lancamento")
public class FceSesCoordenadasLancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_coordenadas_lancamento_seq")
	@SequenceGenerator(name = "fce_ses_coordenadas_lancamento_seq", sequenceName = "fce_ses_coordenadas_lancamento_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_coordenadas_lancamento", nullable = false)
	private Integer ideFceSesCoordenadasLancamento;
	
	@Column(name="val_vazao_media_captacao")
	private Double valorVazaoMediaCaptacao;
	
	@Column(name="val_vazao_efluente_tratado")
	private Double vazaoEfluenteTratado;
	
	@Column(name="ind_efluente_reuso")
	private Boolean indEfluenteReuso;
		
	@OneToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses", referencedColumnName="ide_fce_ses")
	private FceSes ideFceSes;


	public Integer getIdeFceSesCoordenadasLancamento() {
		return ideFceSesCoordenadasLancamento;
	}


	public void setIdeFceSesCoordenadasLancamento(
			Integer ideFceSesCoordenadasLancamento) {
		this.ideFceSesCoordenadasLancamento = ideFceSesCoordenadasLancamento;
	}


	public Double getValorVazaoMediaCaptacao() {
		return valorVazaoMediaCaptacao;
	}


	public void setValorVazaoMediaCaptacao(Double valorVazaoMediaCaptacao) {
		this.valorVazaoMediaCaptacao = valorVazaoMediaCaptacao;
	}


	public Boolean getIndEfluenteReuso() {
		return indEfluenteReuso;
	}


	public void setIndEfluenteReuso(Boolean indEfluenteReuso) {
		this.indEfluenteReuso = indEfluenteReuso;
	}

	public FceSes getIdeFceSes() {
		return ideFceSes;
	}

	public void setIdeFceSes(FceSes ideFceSes) {
		this.ideFceSes = ideFceSes;
	}


	public Double getVazaoEfluenteTratado() {
		return vazaoEfluenteTratado;
	}


	public void setVazaoEfluenteTratado(Double vazaoEfluenteTratado) {
		this.vazaoEfluenteTratado = vazaoEfluenteTratado;
	}

}
