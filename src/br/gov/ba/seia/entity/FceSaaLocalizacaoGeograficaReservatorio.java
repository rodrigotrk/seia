package br.gov.ba.seia.entity;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="fce_saa_localizacao_geografica_reservatorio")
public class FceSaaLocalizacaoGeograficaReservatorio extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_saa_localizacao_geografica_reservatorio_seq")
	@SequenceGenerator(name = "fce_saa_localizacao_geografica_reservatorio_seq", sequenceName = "fce_saa_localizacao_geografica_reservatorio_seq", allocationSize = 1)
	@Column(name="ide_fce_saa_localizacao_geografica_reservatorio")
	private Integer ideFceSaaLocalizacaoGeograficaReservatorio;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_saa", referencedColumnName="ide_fce_saa")
	private FceSaa ideFceSaa;

	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@Column(name="nom_identificacao")
	private String nomeIdentificacao;
	
	@Column(name="val_capacidade")
	private Double valorCapacidade;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_tipo_reservatorio", referencedColumnName="ide_tipo_reservatorio")
	private TipoReservatorio ideTipoReservatorio;

	@Transient
	private boolean localizacaoFinal;
	
	@Transient
	private boolean desabilitarLinha;
	
	public FceSaaLocalizacaoGeograficaReservatorio() {
	}

	public Integer getIdeFceSaaLocalizacaoGeograficaReservatorio() {
		return ideFceSaaLocalizacaoGeograficaReservatorio;
	}

	public void setIdeFceSaaLocalizacaoGeograficaReservatorio(Integer ideFceSaaLocalizacaoGeograficaReservatorio) {
		this.ideFceSaaLocalizacaoGeograficaReservatorio = ideFceSaaLocalizacaoGeograficaReservatorio;
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

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public TipoReservatorio getIdeTipoReservatorio() {
		return ideTipoReservatorio;
	}

	public void setIdeTipoReservatorio(TipoReservatorio ideTipoReservatorio) {
		this.ideTipoReservatorio = ideTipoReservatorio;
	}

	public String getNomeIdentificacao() {
		return nomeIdentificacao;
	}

	public void setNomeIdentificacao(String nomeIdentificacao) {
		this.nomeIdentificacao = nomeIdentificacao;
	}

	public boolean isLocalizacaoFinal() {
		return localizacaoFinal;
	}

	public void setLocalizacaoFinal(boolean localizacaoFinal) {
		this.localizacaoFinal = localizacaoFinal;
	}

	public Double getValorCapacidade() {
		return valorCapacidade;
	}

	public void setValorCapacidade(Double valorCapacidade) {
		this.valorCapacidade = valorCapacidade;
	}

	public boolean isDesabilitarLinha() {
		return desabilitarLinha;
	}

	public void setDesabilitarLinha(boolean desabilitarLinha) {
		this.desabilitarLinha = desabilitarLinha;
	}

}
