package br.gov.ba.seia.entity;

import java.math.BigDecimal;
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
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="tcca_projeto")
@NamedQuery(name="TccaProjeto.findAll", query="SELECT t FROM TccaProjeto t")
public class TccaProjeto extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TCCA_PROJETO_IDETCCAPROJETO_GENERATOR", sequenceName="TCCA_PROJETO_IDE_TCCA_PROJETO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TCCA_PROJETO_IDETCCAPROJETO_GENERATOR")
	@Column(name="ide_tcca_projeto", updatable=false, unique=true, nullable=false)
	private Integer ideTccaProjeto;

	@Column(name="ind_cancelado", nullable=false)
	private Boolean indCancelado;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;

	@Column(name="nom_projeto", nullable=false, length=100)
	private String nomProjeto;

	@Column(name="num_projeto_resolucao", length=100)
	private String numProjetoResolucao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca", nullable=false)
	private Tcca ideTcca;
	
	@OneToMany(mappedBy="ideTccaProjeto")
	private List<ProjetoAcao> projetoAcaoCollection;

	@OneToMany(mappedBy="ideTccaProjeto")
	private List<ProjetoEmpresaExecutora> projetoEmpresaExecutoraCollection;

	@OneToMany(mappedBy="ideTccaProjeto")
	private List<ProjetoStatus> projetoStatusCollection;

	@OneToMany(mappedBy="ideTccaProjeto")
	private List<ProjetoUnidadeConservacao> projetoUnidadeConservacaoCollection;
	
	@Transient
	private ProjetoStatus ultimoStatus;
	
	@Transient
	private boolean visualizando;

	@Transient
	private boolean projetoCadastrado;
	
	@Transient
	private boolean projetoExecutado;
	
	@Transient
	private boolean projetoMovimentado;
	
	@Transient
	private BigDecimal valorPrevisto;

	@Transient
	private BigDecimal valorContratado;

	@Transient
	private BigDecimal valorExecutado;

	@Transient
	private BigDecimal valorRemanejado;
	
	@Transient
	private BigDecimal valorSuplementado;
	
	@Transient
	private BigDecimal valorSaldo;
	
	@Transient
	private ProjetoEmpresaExecutora projetoEmpresaExecutoraAtiva;
	
	@Transient
	private List<MovimentacaoFinanceiraTccaProduto> listHistoricoMovimentacoes;

	public TccaProjeto() {}

	public TccaProjeto(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}

	public TccaProjeto(Integer ideTccaProjeto) {
		this.ideTccaProjeto = ideTccaProjeto;
	}

	
	public boolean isStatusCadastroIncompleto() {
		
		if(!Util.isNullOuVazio(ultimoStatus) && !Util.isNullOuVazio(ultimoStatus.getIdeTccaProjetoTipoStatus())) {
			return ultimoStatus.getIdeTccaProjetoTipoStatus().getIdeTccaProjetoTipoStatus() == TccaProjetoTipoStatusEnum.CADASTRO_INCOMPLETO.getId();
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


	
	public Integer getIdeTccaProjeto() {
		return this.ideTccaProjeto;
	}

	public void setIdeTccaProjeto(Integer ideTccaProjeto) {
		this.ideTccaProjeto = ideTccaProjeto;
	}

	public Boolean getIndCancelado() {
		return this.indCancelado;
	}

	public void setIndCancelado(Boolean indCancelado) {
		this.indCancelado = indCancelado;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomProjeto() {
		return this.nomProjeto;
	}

	public void setNomProjeto(String nomProjeto) {
		this.nomProjeto = nomProjeto;
	}

	public String getNumProjetoResolucao() {
		return this.numProjetoResolucao;
	}

	public void setNumProjetoResolucao(String numProjetoResolucao) {
		this.numProjetoResolucao = numProjetoResolucao;
	}

	public List<ProjetoAcao> getProjetoAcaoCollection() {
		return this.projetoAcaoCollection;
	}

	public void setProjetoAcaoCollection(List<ProjetoAcao> projetoAcaoCollection) {
		this.projetoAcaoCollection = projetoAcaoCollection;
	}

	public List<ProjetoEmpresaExecutora> getProjetoEmpresaExecutoraCollection() {
		return this.projetoEmpresaExecutoraCollection;
	}

	public void setProjetoEmpresaExecutoraCollection(List<ProjetoEmpresaExecutora> projetoEmpresaExecutoraCollection) {
		this.projetoEmpresaExecutoraCollection = projetoEmpresaExecutoraCollection;
	}

	public List<ProjetoStatus> getProjetoStatusCollection() {
		return this.projetoStatusCollection;
	}

	public void setProjetoStatusCollection(List<ProjetoStatus> projetoStatusCollection) {
		this.projetoStatusCollection = projetoStatusCollection;
	}

	public List<ProjetoUnidadeConservacao> getProjetoUnidadeConservacaoCollection() {
		return this.projetoUnidadeConservacaoCollection;
	}

	public void setProjetoUnidadeConservacaoCollection(List<ProjetoUnidadeConservacao> projetoUnidadeConservacaoCollection) {
		this.projetoUnidadeConservacaoCollection = projetoUnidadeConservacaoCollection;
	}

	public Tcca getIdeTcca() {
		return this.ideTcca;
	}

	public void setIdeTcca(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}

	public ProjetoStatus getUltimoStatus() {
		return ultimoStatus;
	}

	public void setUltimoStatus(ProjetoStatus ultimoStatus) {
		this.ultimoStatus = ultimoStatus;
	}

	public boolean isVisualizando() {
		return visualizando;
	}

	public void setVisualizando(boolean visualizando) {
		this.visualizando = visualizando;
	}
	
	public boolean isProjetoCadastrado() {
		return projetoCadastrado;
	}

	public void setProjetoCadastrado(boolean projetoCadastrado) {
		this.projetoCadastrado = projetoCadastrado;
	}

	public BigDecimal getValorPrevisto() {
		return valorPrevisto;
	}

	public void setValorPrevisto(BigDecimal valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}

	public BigDecimal getValorContratado() {
		return valorContratado;
	}

	public void setValorContratado(BigDecimal valorContratado) {
		this.valorContratado = valorContratado;
	}

	public BigDecimal getValorExecutado() {
		return valorExecutado;
	}

	public void setValorExecutado(BigDecimal valorExecutado) {
		this.valorExecutado = valorExecutado;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public boolean isProjetoExecutado() {
		return projetoExecutado;
	}

	public void setProjetoExecutado(boolean projetoExecutado) {
		this.projetoExecutado = projetoExecutado;
	}

	public BigDecimal getValorRemanejado() {
		return valorRemanejado;
	}

	public void setValorRemanejado(BigDecimal valorRemanejado) {
		this.valorRemanejado = valorRemanejado;
	}

	public BigDecimal getValorSuplementado() {
		return valorSuplementado;
	}

	public void setValorSuplementado(BigDecimal valorSuplementado) {
		this.valorSuplementado = valorSuplementado;
	}

	public boolean isProjetoMovimentado() {
		return projetoMovimentado;
	}

	public void setProjetoMovimentado(boolean projetoMovimentado) {
		this.projetoMovimentado = projetoMovimentado;
	}

	public ProjetoEmpresaExecutora getProjetoEmpresaExecutoraAtiva() {
		return projetoEmpresaExecutoraAtiva;
	}

	public void setProjetoEmpresaExecutoraAtiva(ProjetoEmpresaExecutora projetoEmpresaExecutoraAtiva) {
		this.projetoEmpresaExecutoraAtiva = projetoEmpresaExecutoraAtiva;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getListHistoricoMovimentacoes() {
		return listHistoricoMovimentacoes;
	}

	public void setListHistoricoMovimentacoes(List<MovimentacaoFinanceiraTccaProduto> listHistoricoMovimentacoes) {
		this.listHistoricoMovimentacoes = listHistoricoMovimentacoes;
	}
}