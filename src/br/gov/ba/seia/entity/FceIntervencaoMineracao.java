/**
 * 
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
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

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name="fce_intervencao_mineracao")
public class FceIntervencaoMineracao implements BaseEntity, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7332015759424716926L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_intervencao_mineracao_seq")
	@SequenceGenerator(name = "fce_intervencao_mineracao_seq", sequenceName = "fce_intervencao_mineracao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_intervencao_mineracao", nullable = false)
	private Integer ideFceIntervencaoMineracao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	private Fce fce;
	
	@Column(name = "dsc_tratamento_efluente")
	private String dscTratamentoEfluente;
	
	@OneToMany(mappedBy="ideFceIntervencaoMineracao")
	private List<LancamentoEfluenteLocalizacaoGeografica> lancamentoEfluenteLocalizacoesGeografica;
	
	@OneToMany(mappedBy="ideFceIntervencaoMineracao")
	private List<FceIntervencaoCaracteristicaExtracao> fceIntervencaoCaracteristicaExtracoes;
	
	public FceIntervencaoMineracao() {
		this.lancamentoEfluenteLocalizacoesGeografica = new ArrayList<LancamentoEfluenteLocalizacaoGeografica>();
		this.fceIntervencaoCaracteristicaExtracoes = new ArrayList<FceIntervencaoCaracteristicaExtracao>();
	}

	@Override
	public Long getId() {
		return new Long(ideFceIntervencaoMineracao);
	}

	/**
	 * @return the ideFceIntervencaoMineracao
	 */
	public Integer getIdeFceIntervencaoMineracao() {
		return ideFceIntervencaoMineracao;
	}

	/**
	 * @param ideFceIntervencaoMineracao the ideFceIntervencaoMineracao to set
	 */
	public void setIdeFceIntervencaoMineracao(Integer ideFceIntervencaoMineracao) {
		this.ideFceIntervencaoMineracao = ideFceIntervencaoMineracao;
	}

	/**
	 * @return the fce
	 */
	public Fce getFce() {
		return fce;
	}

	/**
	 * @param fce the fce to set
	 */
	public void setFce(Fce fce) {
		this.fce = fce;
	}

	/**
	 * @return the dscTratamentoEfluente
	 */
	public String getDscTratamentoEfluente() {
		return dscTratamentoEfluente;
	}

	/**
	 * @param dscTratamentoEfluente the dscTratamentoEfluente to set
	 */
	public void setDscTratamentoEfluente(String dscTratamentoEfluente) {
		this.dscTratamentoEfluente = dscTratamentoEfluente;
	}

	public List<LancamentoEfluenteLocalizacaoGeografica> getLancamentoEfluenteLocalizacoesGeografica() {
		return lancamentoEfluenteLocalizacoesGeografica;
	}

	public void setLancamentoEfluenteLocalizacoesGeografica(
			List<LancamentoEfluenteLocalizacaoGeografica> lancamentoEfluenteLocalizacoesGeografica) {
		this.lancamentoEfluenteLocalizacoesGeografica = lancamentoEfluenteLocalizacoesGeografica;
	}

	/**
	 * @return the fceIntervencaoCaracteristicaExtracoes
	 */
	public List<FceIntervencaoCaracteristicaExtracao> getFceIntervencaoCaracteristicaExtracoes() {
		return fceIntervencaoCaracteristicaExtracoes;
	}

	/**
	 * @param fceIntervencaoCaracteristicaExtracoes the fceIntervencaoCaracteristicaExtracoes to set
	 */
	public void setFceIntervencaoCaracteristicaExtracoes(
			List<FceIntervencaoCaracteristicaExtracao> fceIntervencaoCaracteristicaExtracoes) {
		this.fceIntervencaoCaracteristicaExtracoes = fceIntervencaoCaracteristicaExtracoes;
	}

}
