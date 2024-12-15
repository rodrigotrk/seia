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
@Table(name="fce_saa_localizacao_geografica_elevatoria_bruta")
public class FceSaaLocalizacaoGeograficaElevatoriaBruta {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_saa_localizacao_geografica_elevatoria_bruta_seq")
	@SequenceGenerator(name = "fce_saa_localizacao_geografica_elevatoria_bruta_seq", sequenceName = "fce_saa_localizacao_geografica_elevatoria_bruta_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_saa_localizacao_geografica_elevatoria_bruta")
	private Integer ideFceSaaLocalizacaoGeograficaElevatoriaBruta;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_saa", referencedColumnName="ide_fce_saa")
	private FceSaa ideFceSaa;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@Column(name="val_vazao")
	private Double valorVazao;
	
	@Transient
	private boolean localizacaoFinal;
	
	@Transient
	private boolean desabilitarVazao;

	public Integer getIdeFceSaaLocalizacaoGeograficaElevatoriaBruta() {
		return ideFceSaaLocalizacaoGeograficaElevatoriaBruta;
	}

	public void setIdeFceSaaLocalizacaoGeograficaElevatoriaBruta(
			Integer ideFceSaaLocalizacaoGeograficaElevatoriaBruta) {
		this.ideFceSaaLocalizacaoGeograficaElevatoriaBruta = ideFceSaaLocalizacaoGeograficaElevatoriaBruta;
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

	public boolean isDesabilitarVazao() {
		return desabilitarVazao;
	}

	public void setDesabilitarVazao(boolean desabilitarVazao) {
		this.desabilitarVazao = desabilitarVazao;
	}
	
}
