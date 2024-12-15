package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="tcca")
@NamedQuery(name="Tcca.findAll", query="SELECT t FROM Tcca t")
public class Tcca extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TCCA_IDETCCA_GENERATOR", sequenceName="TCCA_IDE_TCCA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TCCA_IDETCCA_GENERATOR")
	@Column(name="ide_tcca", updatable=false, unique=true, nullable=false)
	private Integer ideTcca;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_assinatura", nullable=false)
	private Date dtAssinatura;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_publicacao", nullable=false)
	private Date dtPublicacao;

	@Column(name="ind_cancelado", nullable=false)
	private Boolean indCancelado;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;

	@Column(name="ind_possui_tcca_origem", nullable=false)
	private Boolean indPossuiTccaOrigem;

	@Column(name="ind_reajustado", nullable=false)
	private Boolean indReajustado;

	@Column(name="ind_renovado", nullable=false)
	private Boolean indRenovado;

	@Column(name="ind_origem_licenciamento_estadual", nullable=false)
	private Boolean indOrigemLicenciamentoEstadual;
	
	@Column(name="ind_modalidade_execucao_direta", nullable=false)
	private Boolean indModalidadeExecucaoDireta;
	
	@Column(name="num_tcca", nullable=false, length=40)
	private String numTcca;
	
	@Column(name="num_tcca_origem", length=40)
	private String numTccaOrigem;
	
	@Column(name="num_prazo_validade", nullable=false)
	private Integer numPrazoValidade;

	@Column(name="num_processo_licenca", length=40)
	private String numProcessoLicenca;

	@Column(name="num_processo_sema", length=40)
	private String numProcessoSema;

	@Column(name="val_gradacao_impacto", nullable=false, precision=20, scale=2)
	private BigDecimal valGradacaoImpacto;

	@Column(name="val_tcca", nullable=false, precision=20, scale=2)
	private BigDecimal valTcca;
	
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento")
	@ManyToOne(fetch = FetchType.LAZY)
	private Empreendimento ideEmpreendimento;
	
	@JoinColumn(name = "ide_pessoa_requerente", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaRequerente;
	
	@OneToMany(mappedBy="ideTccaDestino")
	private List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoDestinoCollection;

	@OneToMany(mappedBy="ideTccaOrigem")
	private List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoOrigemCollection;

	@OneToMany(mappedBy="ideTcca")
	private List<TccaDocumentoApensado> tccaDocumentoApensadoCollection;

	@OneToMany(mappedBy="ideTcca")
	private List<TccaHistoricoReajusteValor> tccaHistoricoReajusteValorCollection;

	@OneToMany(mappedBy="ideTcca")
	private List<TccaHistoricoRenovacaoPrazoValidade> tccaHistoricoRenovacaoPrazoValidadeCollection;

	@OneToMany(mappedBy="ideTcca")
	private List<TccaProjeto> tccaProjetoCollection;

	@OneToMany(mappedBy="ideTcca")
	private List<TccaSaldo> tccaSaldoCollection;

	@OneToMany(mappedBy="ideTcca")
	private List<TccaStatus> tccaStatusCollection;
		
	@Transient
	private TccaStatus ultimoStatus;
	
	@Transient
	private TccaSaldo ultimoSaldo;
	
	@Transient
	private boolean tccaCadastrado;
	
	@Transient
	private boolean tccaMovimentado;
	
	@Transient
	private boolean tccaDuplicado;
	
	@Transient
	private boolean visualizando;
	
	@Transient
	private Date dtcVencimento;

	@Transient
	private List<MovimentacaoFinanceiraTccaProduto> listHistoricoMovimentacoes;
	
	@Transient
	private List<TccaHistoricoReajusteValor> listHistoricoReajustes;
	
	public Tcca() {}
	
	public Tcca(Integer ideTcca) {
		this.ideTcca = ideTcca;
	}

	
	public boolean isStatusCadastroIncompleto() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.CADASTRO_INCOMPLETO.getId();
		}
		
		return false;
	}

	public boolean isStatusVigente() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.VIGENTE.getId();
		}
		
		return false;
	}

	public boolean isStatusPrevisto() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.PREVISTO.getId();
		}
		
		return false;
	}

	public boolean isStatusEmExecucao() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.EM_EXECUCAO.getId();
		}
		
		return false;
	}

	public boolean isStatusExpirado() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.EXPIRADO.getId();
		}
		
		return false;
	}

	public boolean isStatusCancelado() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.CANCELADO.getId();
		}
		
		return false;
	}

	public boolean isStatusRemanejado() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.REMANEJADO.getId();
		}
		
		return false;
	}
	
	public boolean isStatusConcluido() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.CONCLUIDO.getId();
		}
		
		return false;
	}
	
	public Date getDtcVencimento() {
		
		if(dtPublicacao != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(dtPublicacao);		
			c.add(Calendar.MONTH, + numPrazoValidade );
			
			dtcVencimento = c.getTime();
		}
		
		return dtcVencimento;
	}
	

		
	public Integer getIdeTcca() {
		return this.ideTcca;
	}

	public void setIdeTcca(Integer ideTcca) {
		this.ideTcca = ideTcca;
	}
	
	public Date getDtAssinatura() {
		return dtAssinatura;
	}

	public void setDtAssinatura(Date dtAssinatura) {
		this.dtAssinatura = dtAssinatura;
	}

	public Date getDtPublicacao() {
		return dtPublicacao;
	}

	public void setDtPublicacao(Date dtPublicacao) {
		this.dtPublicacao = dtPublicacao;
	}

	public Boolean getIndCancelado() {
		return indCancelado;
	}

	public void setIndCancelado(Boolean indCancelado) {
		this.indCancelado = indCancelado;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Boolean getIndPossuiTccaOrigem() {
		return indPossuiTccaOrigem;
	}

	public void setIndPossuiTccaOrigem(Boolean indPossuiTccaOrigem) {
		this.indPossuiTccaOrigem = indPossuiTccaOrigem;
	}

	public Boolean getIndReajustado() {
		return indReajustado;
	}

	public void setIndReajustado(Boolean indReajustado) {
		this.indReajustado = indReajustado;
	}

	public Boolean getIndRenovado() {
		return indRenovado;
	}

	public void setIndRenovado(Boolean indRenovado) {
		this.indRenovado = indRenovado;
	}

	public Boolean getIndOrigemLicenciamentoEstadual() {
		return indOrigemLicenciamentoEstadual;
	}

	public void setIndOrigemLicenciamentoEstadual(Boolean indOrigemLicenciamentoEstadual) {
		this.indOrigemLicenciamentoEstadual = indOrigemLicenciamentoEstadual;
	}

	public Boolean getIndModalidadeExecucaoDireta() {
		return indModalidadeExecucaoDireta;
	}

	public void setIndModalidadeExecucaoDireta(Boolean indModalidadeExecucaoDireta) {
		this.indModalidadeExecucaoDireta = indModalidadeExecucaoDireta;
	}

	public String getNumTcca() {
		return numTcca;
	}

	public void setNumTcca(String numTcca) {
		this.numTcca = numTcca;
	}

	public String getNumTccaOrigem() {
		return numTccaOrigem;
	}

	public void setNumTccaOrigem(String numTccaOrigem) {
		this.numTccaOrigem = numTccaOrigem;
	}

	public Integer getNumPrazoValidade() {
		return numPrazoValidade;
	}

	public void setNumPrazoValidade(Integer numPrazoValidade) {
		this.numPrazoValidade = numPrazoValidade;
	}

	public String getNumProcessoLicenca() {
		return numProcessoLicenca;
	}

	public void setNumProcessoLicenca(String numProcessoLicenca) {
		this.numProcessoLicenca = numProcessoLicenca;
	}

	public String getNumProcessoSema() {
		return numProcessoSema;
	}

	public void setNumProcessoSema(String numProcessoSema) {
		this.numProcessoSema = numProcessoSema;
	}

	public BigDecimal getValGradacaoImpacto() {
		return valGradacaoImpacto;
	}

	public void setValGradacaoImpacto(BigDecimal valGradacaoImpacto) {
		this.valGradacaoImpacto = valGradacaoImpacto;
	}

	public BigDecimal getValTcca() {
		return valTcca;
	}

	public void setValTcca(BigDecimal valTcca) {
		this.valTcca = valTcca;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public Pessoa getIdePessoaRequerente() {
		return idePessoaRequerente;
	}

	public void setIdePessoaRequerente(Pessoa idePessoaRequerente) {
		this.idePessoaRequerente = idePessoaRequerente;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getMovimentacaoFinanceiraTccaProdutoDestinoCollection() {
		return movimentacaoFinanceiraTccaProdutoDestinoCollection;
	}

	public void setMovimentacaoFinanceiraTccaProdutoDestinoCollection(List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoDestinoCollection) {
		this.movimentacaoFinanceiraTccaProdutoDestinoCollection = movimentacaoFinanceiraTccaProdutoDestinoCollection;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getMovimentacaoFinanceiraTccaProdutoOrigemCollection() {
		return movimentacaoFinanceiraTccaProdutoOrigemCollection;
	}

	public void setMovimentacaoFinanceiraTccaProdutoOrigemCollection(List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoOrigemCollection) {
		this.movimentacaoFinanceiraTccaProdutoOrigemCollection = movimentacaoFinanceiraTccaProdutoOrigemCollection;
	}

	public List<TccaDocumentoApensado> getTccaDocumentoApensadoCollection() {
		return tccaDocumentoApensadoCollection;
	}

	public void setTccaDocumentoApensadoCollection(List<TccaDocumentoApensado> tccaDocumentoApensadoCollection) {
		this.tccaDocumentoApensadoCollection = tccaDocumentoApensadoCollection;
	}

	public List<TccaHistoricoReajusteValor> getTccaHistoricoReajusteValorCollection() {
		return tccaHistoricoReajusteValorCollection;
	}

	public void setTccaHistoricoReajusteValorCollection(List<TccaHistoricoReajusteValor> tccaHistoricoReajusteValorCollection) {
		this.tccaHistoricoReajusteValorCollection = tccaHistoricoReajusteValorCollection;
	}

	public List<TccaHistoricoRenovacaoPrazoValidade> getTccaHistoricoRenovacaoPrazoValidadeCollection() {
		return tccaHistoricoRenovacaoPrazoValidadeCollection;
	}

	public void setTccaHistoricoRenovacaoPrazoValidadeCollection(List<TccaHistoricoRenovacaoPrazoValidade> tccaHistoricoRenovacaoPrazoValidadeCollection) {
		this.tccaHistoricoRenovacaoPrazoValidadeCollection = tccaHistoricoRenovacaoPrazoValidadeCollection;
	}

	public List<TccaProjeto> getTccaProjetoCollection() {
		return tccaProjetoCollection;
	}

	public void setTccaProjetoCollection(List<TccaProjeto> tccaProjetoCollection) {
		this.tccaProjetoCollection = tccaProjetoCollection;
	}

	public List<TccaSaldo> getTccaSaldoCollection() {
		return tccaSaldoCollection;
	}

	public void setTccaSaldoCollection(List<TccaSaldo> tccaSaldoCollection) {
		this.tccaSaldoCollection = tccaSaldoCollection;
	}

	public List<TccaStatus> getTccaStatusCollection() {
		return tccaStatusCollection;
	}

	public void setTccaStatusCollection(List<TccaStatus> tccaStatusCollection) {
		this.tccaStatusCollection = tccaStatusCollection;
	}

	public TccaStatus getUltimoStatus() {
		return ultimoStatus;
	}

	public void setUltimoStatus(TccaStatus ultimoStatus) {
		this.ultimoStatus = ultimoStatus;
	}

	public TccaSaldo getUltimoSaldo() {
		return ultimoSaldo;
	}

	public void setUltimoSaldo(TccaSaldo ultimoSaldo) {
		this.ultimoSaldo = ultimoSaldo;
	}

	public boolean isTccaCadastrado() {
		return tccaCadastrado;
	}

	public void setTccaCadastrado(boolean tccaCadastrado) {
		this.tccaCadastrado = tccaCadastrado;
	}

	public boolean isTccaMovimentado() {
		return tccaMovimentado;
	}

	public void setTccaMovimentado(boolean tccaMovimentado) {
		this.tccaMovimentado = tccaMovimentado;
	}

	public boolean isVisualizando() {
		return visualizando;
	}

	public void setVisualizando(boolean visualizando) {
		this.visualizando = visualizando;
	}

	public boolean isTccaDuplicado() {
		return tccaDuplicado;
	}

	public void setTccaDuplicado(boolean tccaDuplicado) {
		this.tccaDuplicado = tccaDuplicado;
	}

	public void setDtcVencimento(Date dtcVencimento) {
		this.dtcVencimento = dtcVencimento;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getListHistoricoMovimentacoes() {
		return listHistoricoMovimentacoes;
	}

	public void setListHistoricoMovimentacoes(List<MovimentacaoFinanceiraTccaProduto> listHistoricoMovimentacoes) {
		this.listHistoricoMovimentacoes = listHistoricoMovimentacoes;
	}

	public List<TccaHistoricoReajusteValor> getListHistoricoReajustes() {
		return listHistoricoReajustes;
	}

	public void setListHistoricoReajustes(List<TccaHistoricoReajusteValor> listHistoricoReajustes) {
		this.listHistoricoReajustes = listHistoricoReajustes;
	}
}