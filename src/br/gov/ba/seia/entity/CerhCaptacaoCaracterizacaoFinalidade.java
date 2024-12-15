package br.gov.ba.seia.entity;

import java.util.Collection;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;


@Entity
@Table(name="cerh_captacao_caracterizacao_finalidade")
public class CerhCaptacaoCaracterizacaoFinalidade extends AbstractEntityHist implements CerhFinalidadeUsoAguaInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_caracterizacao_finalidade_seq")
	@SequenceGenerator(name = "cerh_captacao_caracterizacao_finalidade_seq", sequenceName = "cerh_captacao_caracterizacao_finalidade_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_caracterizacao_finalidade")
	private Integer ideCerhCaptacaoCaracterizacaoFinalidade;

	@Historico(nameMethod="getIdeTipoFinalidadeUsoAgua")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_finalidade_uso_agua")
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_captacao_caracterizacao")
	private CerhCaptacaoCaracterizacao ideCerhCaptacaoCaracterizacao;

	@Historico
	@OneToOne(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CerhCaptacaoTermoeletrica ideCerhCaptacaoTermoeletrica;

	@Historico
	@OneToOne(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CerhCaptacaoMineracaoExtracaoAreia ideCerhCaptacaoMineracaoExtracaoAreia;

	@Historico
	@OneToOne(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CerhCaptacaoOutrosUsos ideCerhCaptacaoOutrosUso;

	@OneToOne(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CerhCaptacaoAbastecimentoPublico ideCerhCaptacaoAbastecimentoPublico;
	
	@Historico
	@OneToMany(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhCaptacaoDadosIrrigacao> cerhCaptacaoDadosIrrigacaoCollection;

	@Historico
	@OneToMany(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhCaptacaoTransposicao> cerhCaptacaoTransposicaoCollection;

	@Historico
	@OneToMany(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhCaptacaoDadosMineracao> cerhCaptacaoDadosMineracaoCollection;
	
	@Historico
	@OneToMany(mappedBy="ideCerhCaptacaoCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhCaptacaoAbastecimentoIndustrial> cerhCaptacaoAbastecimentoIndustrialCollection;


	public CerhCaptacaoCaracterizacaoFinalidade() {
	}

	/**
	 * @param cerhCaptacaoCaracterizacao 
	 * @param tipoFinalidadeUsoAgua
	 */
	public CerhCaptacaoCaracterizacaoFinalidade(CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao, TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua) {
		this.ideCerhCaptacaoCaracterizacao = cerhCaptacaoCaracterizacao;
		this.ideTipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua;
	}

	public Integer getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(Integer ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}
	
	@Override
	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	@Override
	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public CerhCaptacaoCaracterizacao getIdeCerhCaptacaoCaracterizacao() {
		return ideCerhCaptacaoCaracterizacao;
	}

	public void setIdeCerhCaptacaoCaracterizacao(CerhCaptacaoCaracterizacao ideCerhCaptacaoCaracterizacao) {
		this.ideCerhCaptacaoCaracterizacao = ideCerhCaptacaoCaracterizacao;
	}

	public Collection<CerhCaptacaoDadosIrrigacao> getCerhCaptacaoDadosIrrigacaoCollection() {
		return cerhCaptacaoDadosIrrigacaoCollection;
	}

	public void setCerhCaptacaoDadosIrrigacaoCollection(Collection<CerhCaptacaoDadosIrrigacao> cerhCaptacaoDadosIrrigacaoCollection) {
		this.cerhCaptacaoDadosIrrigacaoCollection = cerhCaptacaoDadosIrrigacaoCollection;
	}

	public Collection<CerhCaptacaoDadosMineracao> getCerhCaptacaoDadosMineracaoCollection() {
		return cerhCaptacaoDadosMineracaoCollection;
	}

	public void setCerhCaptacaoDadosMineracaoCollection(Collection<CerhCaptacaoDadosMineracao> cerhCaptacaoDadosMineracaoCollection) {
		this.cerhCaptacaoDadosMineracaoCollection = cerhCaptacaoDadosMineracaoCollection;
	}

	public Collection<CerhCaptacaoTransposicao> getCerhCaptacaoTransposicaoCollection() {
		return cerhCaptacaoTransposicaoCollection;
	}

	public void setCerhCaptacaoTransposicaoCollection(Collection<CerhCaptacaoTransposicao> cerhCaptacaoTransposicaoCollection) {
		this.cerhCaptacaoTransposicaoCollection = cerhCaptacaoTransposicaoCollection;
	}

	public Collection<CerhCaptacaoAbastecimentoIndustrial> getCerhCaptacaoAbastecimentoIndustrialCollection() {
		return cerhCaptacaoAbastecimentoIndustrialCollection;
	}

	public void setCerhCaptacaoAbastecimentoIndustrialCollection(Collection<CerhCaptacaoAbastecimentoIndustrial> cerhCaptacaoAbastecimentoIndustrialCollection) {
		this.cerhCaptacaoAbastecimentoIndustrialCollection = cerhCaptacaoAbastecimentoIndustrialCollection;
	}

	public CerhCaptacaoMineracaoExtracaoAreia getIdeCerhCaptacaoMineracaoExtracaoAreia() {
		return ideCerhCaptacaoMineracaoExtracaoAreia;
	}

	public void setIdeCerhCaptacaoMineracaoExtracaoAreia(CerhCaptacaoMineracaoExtracaoAreia ideCerhCaptacaoMineracaoExtracaoAreia) {
		this.ideCerhCaptacaoMineracaoExtracaoAreia = ideCerhCaptacaoMineracaoExtracaoAreia;
	}

	public CerhCaptacaoOutrosUsos getIdeCerhCaptacaoOutrosUso() {
		return ideCerhCaptacaoOutrosUso;
	}

	public void setIdeCerhCaptacaoOutrosUso(CerhCaptacaoOutrosUsos ideCerhCaptacaoOutrosUso) {
		this.ideCerhCaptacaoOutrosUso = ideCerhCaptacaoOutrosUso;
	}

	public CerhCaptacaoTermoeletrica getIdeCerhCaptacaoTermoeletrica() {
		return ideCerhCaptacaoTermoeletrica;
	}

	public void setIdeCerhCaptacaoTermoeletrica(CerhCaptacaoTermoeletrica ideCerhCaptacaoTermoeletrica) {
		this.ideCerhCaptacaoTermoeletrica = ideCerhCaptacaoTermoeletrica;
	}

	public CerhCaptacaoAbastecimentoPublico getIdeCerhCaptacaoAbastecimentoPublico() {
		return ideCerhCaptacaoAbastecimentoPublico;
	}

	public void setIdeCerhCaptacaoAbastecimentoPublico(CerhCaptacaoAbastecimentoPublico ideCerhCaptacaoAbastecimentoPublico) {
		this.ideCerhCaptacaoAbastecimentoPublico = ideCerhCaptacaoAbastecimentoPublico;
	}

}