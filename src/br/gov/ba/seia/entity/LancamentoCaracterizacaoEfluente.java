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

/**
 * @author rodrigo
 *
 */
@Entity
@Table(name="lancamento_caracterizacao_efluentes")
public class LancamentoCaracterizacaoEfluente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lancamento_caracterizacao_efluentes_seq")
	@SequenceGenerator(name = "lancamento_caracterizacao_efluentes_seq", sequenceName = "lancamento_caracterizacao_efluentes_seq", allocationSize = 1)
	@Column(name="ide_lancamento_caracterizacao_efluente")
	private Integer ideLancamentoCaracterizacaoEfluente;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_caracterizacao_efluente", referencedColumnName="ide_caracterizacao_efluente")
	private CaracterizacaoEfluente ideCaracterizacaoEfluente;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses_lancamento_efluente", referencedColumnName="ide_fce_ses_lancamento_efluente")
	private FceSesCoordenadasLancamentoLocalizacaoGeografica ideCoordenadaFceLancamentoLocalizacaoGeografica;

	
	
	public CaracterizacaoEfluente getIdeCaracterizacaoEfluente() {
		return ideCaracterizacaoEfluente;
	}

	public void setIdeCaracterizacaoEfluente(
			CaracterizacaoEfluente ideCaracterizacaoEfluente) {
		this.ideCaracterizacaoEfluente = ideCaracterizacaoEfluente;
	}

	public FceSesCoordenadasLancamentoLocalizacaoGeografica getIdeCoordenadaFceLancamentoLocalizacaoGeografica() {
		return ideCoordenadaFceLancamentoLocalizacaoGeografica;
	}

	public void setIdeCoordenadaFceLancamentoLocalizacaoGeografica(
			FceSesCoordenadasLancamentoLocalizacaoGeografica ideCoordenadaFceLancamentoLocalizacaoGeografica) {
		this.ideCoordenadaFceLancamentoLocalizacaoGeografica = ideCoordenadaFceLancamentoLocalizacaoGeografica;
	}

	public Integer getIdeLancamentoCaracterizacaoEfluente() {
		return ideLancamentoCaracterizacaoEfluente;
	}

	public void setIdeLancamentoCaracterizacaoEfluente(
			Integer ideLancamentoCaracterizacaoEfluente) {
		this.ideLancamentoCaracterizacaoEfluente = ideLancamentoCaracterizacaoEfluente;
	}
	
}	