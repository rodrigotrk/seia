package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tcca_historico_renovacao_prazo_validade")
@NamedQuery(name="TccaHistoricoRenovacaoPrazoValidade.findAll", query="SELECT t FROM TccaHistoricoRenovacaoPrazoValidade t")
public class TccaHistoricoRenovacaoPrazoValidade extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TCCA_HISTORICO_RENOVACAO_PRAZO_VALIDADE_IDETCCAHISTORICORENOVACAOPRAZOVALIDADE_GENERATOR", sequenceName="TCCA_HISTORICO_RENOVACAO_PRAZO_VALIDADE_IDE_TCCA_HISTORICO_RENOVACAO_PRAZO_VALIDADE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TCCA_HISTORICO_RENOVACAO_PRAZO_VALIDADE_IDETCCAHISTORICORENOVACAOPRAZOVALIDADE_GENERATOR")
	@Column(name="ide_tcca_historico_renovacao_prazo_validade", updatable=false, unique=true, nullable=false)
	private Integer ideTccaHistoricoRenovacaoPrazoValidade;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_renovacao", nullable=false)
	private Date dtcRenovacao;
	
	@Column(name="num_novo_prazo_validade", nullable=false)
	private Integer numPrazoValidade;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_renovacao_prazo_validade", nullable=false)
	private Date dtNovoPrazoValidade;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_fisica_renovacao", nullable=false)
	private PessoaFisica idePessoaFisicaRenovacao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca", nullable=false)
	private Tcca ideTcca;

	public TccaHistoricoRenovacaoPrazoValidade() {
	}

	public Integer getIdeTccaHistoricoRenovacaoPrazoValidade() {
		return this.ideTccaHistoricoRenovacaoPrazoValidade;
	}

	public void setIdeTccaHistoricoRenovacaoPrazoValidade(Integer ideTccaHistoricoRenovacaoPrazoValidade) {
		this.ideTccaHistoricoRenovacaoPrazoValidade = ideTccaHistoricoRenovacaoPrazoValidade;
	}

	public Date getDtNovoPrazoValidade() {
		return this.dtNovoPrazoValidade;
	}

	public void setDtNovoPrazoValidade(Date dtNovoPrazoValidade) {
		this.dtNovoPrazoValidade = dtNovoPrazoValidade;
	}

	public Date getDtcRenovacao() {
		return this.dtcRenovacao;
	}

	public void setDtcRenovacao(Date dtcRenovacao) {
		this.dtcRenovacao = dtcRenovacao;
	}

	public PessoaFisica getIdePessoaFisicaRenovacao() {
		return this.idePessoaFisicaRenovacao;
	}

	public void setIdePessoaFisicaRenovacao(PessoaFisica idePessoaFisicaRenovacao) {
		this.idePessoaFisicaRenovacao = idePessoaFisicaRenovacao;
	}

	public Integer getNumPrazoValidade() {
		return numPrazoValidade;
	}

	public void setNumPrazoValidade(Integer numPrazoValidade) {
		this.numPrazoValidade = numPrazoValidade;
	}

	public Tcca getIdeTcca() {
		return this.ideTcca;
	}

	public void setIdeTcca(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}
}