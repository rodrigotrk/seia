package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.MovimentacaoFinanceira;
import br.gov.ba.seia.entity.MovimentacaoFinanceiraTccaProduto;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProdutoExecucao;
import br.gov.ba.seia.entity.ProdutoSaldo;
import br.gov.ba.seia.entity.ProjetoAcao;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.entity.ProjetoEmpresaExecutora;
import br.gov.ba.seia.entity.ProjetoUnidadeConservacao;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.entity.TccaProjetoOperacao;
import br.gov.ba.seia.entity.TccaSaldo;
import br.gov.ba.seia.entity.TipoOrigemDestino;
import br.gov.ba.seia.entity.UnidadeConservacao;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TccaProjetoOperacaoEnum;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoOrigemDestinoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("projetoTccaController")
@ViewScoped
public class ProjetoTccaController extends BaseTccaController {
	
	private Tcca tcca;
	private TccaProjeto projeto;
	private DataTable dataTableProjeto;
	private LazyDataModel<TccaProjeto> dataModelProjeto;
	
	private List<UnidadeConservacao> listUnidadesConservacao;
	private List<UnidadeConservacao> listUnidadesConservacaoSelecionadas;
	private UnidadeConservacao unidadeConservacaoSelecionada;
	
	private ProjetoAcao acao;
	private List<ProjetoAcao> listAcoes;
	private ProjetoAcao acaoSelecionada;
	private boolean editandoAcao;
	
	private boolean exibeGridNovoProduto;
	private ProjetoAcaoProduto produto;
	private List<ProjetoAcaoProduto> listProdutos;
	private ProjetoAcaoProduto produtoSelecionado;
	
	private ProdutoExecucao produtoExecucao;
	private boolean desabilitaSalvarExecucaoProduto;
	private ProjetoEmpresaExecutora projetoEmpresaExecutora;
	private ProjetoEmpresaExecutora projetoEmpresaExecutoraSelecionada;
	private BigDecimal diffValorPrevisto = null;
	
	private List<TipoOrigemDestino> listTiposOrigemDestino;
	private TipoOrigemDestino tipoOrigemDestinoSelecionado;
	private String numResolucao;
	private BigDecimal valorMovimentacao;
	
	private boolean saldoNaoDestinado;
	private boolean saldoOutroProjeto;
	private boolean saldoOutroTcca;
	private boolean exibeDestinoRecursoMovimentacao;
	
	/****************
	/*			 	*
	//XXX: CONSULTA *
	/* 			 	*
	/****************/
	
	@PostConstruct
	public void init() {
		try{
			tcca = ContextoUtil.getContexto().getTcca();
			projeto = ContextoUtil.getContexto().getProjeto();
			enviarTccaParaSessao(null, null);
			desabilitaSalvarExecucaoProduto = true;
			
			if(Util.isNullOuVazio(tcca)) {
				tcca = new Tcca();
			}
			
			if(Util.isNullOuVazio(projeto)) {
				projeto = new TccaProjeto(tcca);
			} else {
				carregarUnidadesConservacao();
				carregarAcoesEProdutos();
				carregarEmpresasExecutoras();
				carregarTiposOrigemDestinoMovimentacaoFinanceiraProjeto();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void enviarTccaParaSessao(Tcca tcca, TccaProjeto projeto) {
		ContextoUtil.getContexto().setTcca(tcca);
		ContextoUtil.getContexto().setProjeto(projeto);
	}

	public void atualizaMensagemPorPollAjax() {
		RequestContext.getCurrentInstance().execute("projetoTccaPoll.stop();");
		
		if(projeto.isProjetoCadastrado()) {
			JsfUtil.addSuccessMessage("Projeto salvo com sucesso!");
			projeto.setProjetoCadastrado(false);
		}
		
		if(projeto.isProjetoExecutado()) {
			JsfUtil.addSuccessMessage("Execução do projeto salva com sucesso!");
			projeto.setProjetoExecutado(false);
		}
		
		if(projeto.isProjetoMovimentado()) {
			JsfUtil.addSuccessMessage("Movimentação do projeto realizada com sucesso!");
			projeto.setProjetoMovimentado(false);
		}
		
		consultarProjeto();
	}

	private void carregarUnidadesConservacao() {
		listUnidadesConservacaoSelecionadas = new ArrayList<UnidadeConservacao>();
		
		projeto.setProjetoUnidadeConservacaoCollection(facade.listarProjetoUnidadeConservacaoPorProjeto(projeto));
		
		for (ProjetoUnidadeConservacao puc : projeto.getProjetoUnidadeConservacaoCollection()) {
			listUnidadesConservacaoSelecionadas.add(puc.getIdeUnidadeConservacao());
		}
	}
	
	private void carregarAcoesEProdutos() {
		
		if(Util.isNullOuVazio(projeto.getProjetoAcaoCollection())) {
			listAcoes = facade.listarProjetoAcaoPorTccaProjeto(projeto);
			
			for (ProjetoAcao pa : listAcoes) {
				pa.setProjetoAcaoProdutoCollection(facade.listarProjetoAcaoProdutoPorProjetoAcao(pa));
			}
			
			projeto.setProjetoAcaoCollection(listAcoes);
		} else {
			listAcoes = projeto.getProjetoAcaoCollection();
		}
	}
	
	private void carregarEmpresasExecutoras() {
		projeto.setProjetoEmpresaExecutoraCollection(facade.listarProjetoEmpresaExecutoraPorProjeto(projeto));
	}
	
	private void carregarTiposOrigemDestinoMovimentacaoFinanceiraProjeto() {
		listTiposOrigemDestino = new ArrayList<TipoOrigemDestino>();
		listTiposOrigemDestino.add(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
		listTiposOrigemDestino.add(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_OUTRO_PROJETO));
//		listTiposOrigemDestino.add(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_OUTRO_TCCA));
	}
	
	public void consultarProjeto() {
		try {
			if (dataTableProjeto != null && !Util.isNullOuVazio(tcca)) {
				
				dataTableProjeto.setFirst(0);
				dataTableProjeto.setPage(1);
				
				dataModelProjeto = new LazyDataModel<TccaProjeto>() {
	
					private static final long serialVersionUID = 7717411591034117580L;
	
					@Override
					public List<TccaProjeto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
						
						List<TccaProjeto> list = facade.listarTccaProjetoPorTcca(tcca, first, pageSize);
						
						for (TccaProjeto tp : list) {
							tp = calcularSaldosDoProjeto(tp);
							tp = calcularValoresExecutadosNoProjeto(tp);
							tp.setUltimoStatus(facade.buscarUltimoProjetoStatusPorTccaProjeto(tp));
						}
						
						tcca.setTccaProjetoCollection(list);
						
						return list;
					}
				};
				
				dataModelProjeto.setRowCount(facade.countConsultaProjeto(tcca));
			} else {
				dataTableProjeto = null;
				dataModelProjeto = null;
				enviarTccaParaSessao(null, null);
				Html.redirecionar(PaginaEnum.CONSULTA_TCCA);
			}
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void excluirProjeto() {
		facade.excluirProjeto(projeto);
		JsfUtil.addSuccessMessage("Projeto excluído com sucesso!");
	}
	
	public void cancelarProjeto() {
		facade.salvarNovoStatusDoProjeto(projeto, TccaProjetoTipoStatusEnum.CANCELADO);
		JsfUtil.addSuccessMessage("Projeto cancelado com sucesso!");
	}
	
	public boolean isRenderedCancelarProjeto(TccaProjeto proj) {
		if(proj.isStatusRemanejado() || proj.isStatusPrevisto() || proj.isStatusEmExecucao()) {
			if(Util.isNullOuVazio(proj.getValorSaldo())) {
				return true;
			}
		}
		
		return false;
	}

	/****************
	/*				*
	//XXX: CADASTRO *
	/* 				*
	/****************/
	
	public void prepararDialogUnidadeConservacao() {
		
		if(Util.isNullOuVazio(listUnidadesConservacaoSelecionadas)) {
			listUnidadesConservacaoSelecionadas = new ArrayList<UnidadeConservacao>();
		}
		
		listUnidadesConservacao = new ArrayList<UnidadeConservacao>();
		
		for (UnidadeConservacao uc : facade.listarUnidadesConservacao()) {
			listUnidadesConservacao.add(uc);
		}
	}
	
	private void salvarUnidadesConservacao() {
		
		if(Util.isNullOuVazio(projeto.getProjetoUnidadeConservacaoCollection())) {
			projeto.setProjetoUnidadeConservacaoCollection(new ArrayList<ProjetoUnidadeConservacao>());
		}
		
		if(!Util.isNullOuVazio(listUnidadesConservacaoSelecionadas)) {
			for (UnidadeConservacao uc : listUnidadesConservacaoSelecionadas) {
				
				ProjetoUnidadeConservacao puc = new ProjetoUnidadeConservacao(uc, projeto);
				boolean unidadeConservacaoNova = true;
				
				for (ProjetoUnidadeConservacao unidade : projeto.getProjetoUnidadeConservacaoCollection()) {
					if(unidade.getIdeUnidadeConservacao().getIdeUnidadeConservacao().equals(uc.getIdeUnidadeConservacao())) {
						unidadeConservacaoNova = false;
					}
				}
				
				if(unidadeConservacaoNova) {
					facade.salvarProjetoUnidadeConservacao(puc);
					projeto.getProjetoUnidadeConservacaoCollection().add(puc);
				}
			}
		}
	}
	
	public void excluirUnidadeConservacao() {
		facade.excluirProjetoUnidadeConservacao(new ProjetoUnidadeConservacao(unidadeConservacaoSelecionada, projeto));
		listUnidadesConservacaoSelecionadas.remove(unidadeConservacaoSelecionada);
		JsfUtil.addSuccessMessage("Unidade de conservação excluída com sucesso!");
	}
	
	public void preparaDialogAcao() {
		acao = new ProjetoAcao();
		listProdutos = null;
		editandoAcao = false;
		exibeGridNovoProduto = false;
	}
	
	public void inserirAcao() {
		if(Util.isNullOuVazio(listAcoes)) {
			listAcoes = new ArrayList<ProjetoAcao>();
		}
		
		acao.setIdeTccaProjeto(projeto);
		acao.setIndExcluido(false);
		acao.setProjetoAcaoProdutoCollection(listProdutos);
		
		if(!editandoAcao) {
			listAcoes.add(acao);
			JsfUtil.addSuccessMessage("Atividade inserida com sucesso!");
		} else {
			editandoAcao = false;
			JsfUtil.addSuccessMessage("Atividade editada com sucesso!");
		}
	}
	
	public void excluirAcao() {
		
		List<ProjetoAcaoProduto> listPAP = facade.listarProjetoAcaoProdutoPorProjetoAcao(acaoSelecionada);
		
		if(!Util.isNullOuVazio(listPAP)) {
			for (ProjetoAcaoProduto pap : listPAP) {
				if(!Util.isNullOuVazio(facade.buscarProdutoExecucaoPorProduto(pap))) {
					JsfUtil.addErrorMessage("Não é possível excluir essa atividade pois ela já possui execuções!");
					return;
				}
				
				if(!Util.isNullOuVazio(facade.listarMovimentacaoFinanceiraTccaProdutoPorProjetoAcaoProduto(pap))) {
					JsfUtil.addErrorMessage("Não é possível excluir essa atividade pois ela já possui movimentações!");
					return;
				}
			}
		}
		
		facade.excluirProjetoAcaoProdutoPorProjetoAcao(acaoSelecionada);
		facade.excluirProjetoAcao(acaoSelecionada);
		listAcoes.remove(acaoSelecionada);
		JsfUtil.addSuccessMessage("Atividade excluída com sucesso!");
	}
	
	private void salvarAcoesEProdutos() {
		
		if(Util.isNullOuVazio(projeto.getProjetoAcaoCollection())) {
			projeto.setProjetoAcaoCollection(new ArrayList<ProjetoAcao>());
		}
		
		if(!Util.isNullOuVazio(listAcoes)) {
			
			for (ProjetoAcao pa : listAcoes) {
				
				pa.setIndExcluido(false);
				
				facade.salvarProjetoAcao(pa);
				
				salvarProdutos(pa.getProjetoAcaoProdutoCollection());
				
				if(!projeto.getProjetoAcaoCollection().contains(pa)) {
					projeto.getProjetoAcaoCollection().add(pa);
				}
			}
		}
	}
	
	public void preparaNovoProduto() {
		produto = new ProjetoAcaoProduto();
		exibeGridNovoProduto = true;
	}
	
	public void inserirProduto() {
		
		if(Util.isNullOuVazio(produto.getNomProduto())) {
			JsfUtil.addWarnMessage("Favor preencher o nome do produto!");
			return;
		}
		
		if(Util.isNullOuVazio(listProdutos)) {
			listProdutos = new ArrayList<ProjetoAcaoProduto>();
		}
		
		produto.setIdeProjetoAcao(acao);
		produto.setIndExcluido(false);
		listProdutos.add(produto);

		exibeGridNovoProduto = false;
		
		JsfUtil.addSuccessMessage("Produto inserido com sucesso!");
	}
	

	public void excluirProduto() {
		
		if(!Util.isNullOuVazio(facade.buscarProdutoExecucaoPorProduto(produtoSelecionado))) {
			JsfUtil.addErrorMessage("Não é possível excluir esse produto pois ele já possui execução!");
			return;
		}
		
		facade.excluirProjetoAcaoProduto(produtoSelecionado);
		listProdutos.remove(produtoSelecionado);
		JsfUtil.addSuccessMessage("Produto excluído com sucesso!");
	}
	
	private void salvarProdutos(List<ProjetoAcaoProduto> produtos) {
		
		if(!Util.isNullOuVazio(produtos)) {
			for (ProjetoAcaoProduto prod : produtos) {
				
				prod.setIndExcluido(false);
				facade.salvarProjetoAcaoProduto(prod);
			}
		}
	}
	
	public void salvarProjeto() {
		projeto.setIndExcluido(false);
		projeto.setIndCancelado(false);
		projeto.setIdeTcca(tcca);
		
		facade.salvarTccaProjeto(projeto);
		
		salvarUnidadesConservacao();
		
		salvarAcoesEProdutos();
		
		facade.salvarNovoStatusDoProjeto(projeto, TccaProjetoTipoStatusEnum.CADASTRO_INCOMPLETO);
		
		if(validarFinalizacaoProjeto(projeto, listUnidadesConservacaoSelecionadas)) {
			facade.salvarNovoStatusDoProjeto(projeto, TccaProjetoTipoStatusEnum.PREVISTO);
			facade.salvarNovoStatusDoTcca(tcca, TccaProjetoTipoStatusEnum.VIGENTE);
		}
		
		projeto.setProjetoCadastrado(true);
		enviarTccaParaSessao(tcca, projeto);
	}
	
	public void editarProjeto() {
		enviarTccaParaSessao(tcca, projeto);
		Html.redirecionar(PaginaEnum.CADASTRO_PROJETO);
	}
	
	/*****************************
	/*							 *
	//XXX: EXECUÇÃO DOS PRODUTOS *
	/* 							 *
	/*****************************/
	
	public boolean isDisabledValorPrevisto() {
		return (produtoSelecionado == null || desabilitaSalvarExecucaoProduto);
	}
	
	public boolean isDisabledValorContratado() {
		if(produtoSelecionado == null || produtoExecucao == null || desabilitaSalvarExecucaoProduto) {
			return true;
		} else if(Util.isNullOuVazio(produtoExecucao.getValPrevisto())) {
			produtoExecucao.setValContratado(null);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDisabledValorExecutado() {
		if(produtoSelecionado == null || produtoExecucao == null || desabilitaSalvarExecucaoProduto) {
			return true;
		} else if(Util.isNullOuVazio(produtoExecucao.getValContratado())) {
			produtoExecucao.setValExecutado(null);
			return true;
		} else {
			return false;
		}
	}
	
	public void validarSaldoNegativoProduto(ValueChangeEvent event) {
		
		ProjetoAcaoProduto pap = (ProjetoAcaoProduto) event.getNewValue();
		
		if(pap != null) {
			if(Util.isNullOuVazio(pap.getUltimoProdutoSaldo())
					|| (pap.getUltimoProdutoSaldo().getValSaldoProduto().compareTo(BigDecimal.ZERO) == 0
					|| pap.getUltimoProdutoSaldo().getValSaldoProduto().compareTo(BigDecimal.ZERO) == -1)) {
				
				exibeDestinoRecursoMovimentacao = false;
				tipoOrigemDestinoSelecionado = null;
				saldoOutroProjeto = false;
				saldoOutroTcca = false;
				saldoNaoDestinado = false;
				JsfUtil.addWarnMessage("Saldo insuficiente para a movimentação desejada.");
				return;
			}
		}
		
		changeProduto(event);
	}
	
	public void changeProduto(ValueChangeEvent event) {
		
		ProjetoAcaoProduto pap = (ProjetoAcaoProduto) event.getNewValue();
		exibeDestinoRecursoMovimentacao = true;
		
		if(pap != null) {
			
			produtoExecucao = facade.buscarProdutoExecucaoPorProduto(pap);
			
			if(produtoExecucao == null) {
				produtoExecucao = new ProdutoExecucao(pap);
				desabilitaSalvarExecucaoProduto = false;
			} else {
				desabilitaSalvarExecucaoProduto = !verificaPermiteAlterarValorPrevisto();
			}
		} else {
			tipoOrigemDestinoSelecionado = null;
			saldoOutroProjeto = false;
			saldoOutroTcca = false;
			saldoNaoDestinado = false;
		}
	}
	
	public boolean isRenderedEmpresaExecutora() {
		
		if(produtoExecucao != null && !Util.isNullOuVazio(produtoExecucao.getValPrevisto()) 
			&& !Util.isNullOuVazio(produtoExecucao.getValContratado())) {
			
			if(projetoEmpresaExecutora == null) projetoEmpresaExecutora = new ProjetoEmpresaExecutora(projeto);
			return true;
		} else {
			
			if(!Util.isNullOuVazio(projeto.getProjetoAcaoCollection())) {
				for (ProjetoAcao pa : projeto.getProjetoAcaoCollection()) {
					
					if(!Util.isNullOuVazio(pa.getProjetoAcaoProdutoCollection())) {
						for (ProjetoAcaoProduto pap : pa.getProjetoAcaoProdutoCollection()) {
							
							ProdutoExecucao pe = facade.buscarProdutoExecucaoPorProduto(pap);
							
							if(pe != null && !Util.isNullOuVazio(pe.getValPrevisto()) 
								&& !Util.isNullOuVazio(pe.getValContratado())) {
								
								if(projetoEmpresaExecutora == null) projetoEmpresaExecutora = new ProjetoEmpresaExecutora(projeto);
								return true;
							}
						}
					}
				}
			}
		}
		
		projetoEmpresaExecutora = null;
		return false;
	}
	
	public void salvarEmpresaExecutora() {
		if(validarEmpresaExecutoraParaSalvarEmpresa()) {
			projetoEmpresaExecutora.setDtcInativa(null);
			projetoEmpresaExecutora.setIdePessoaFisicaInativadora(null);
			projetoEmpresaExecutora.setIndInativa(false);
			facade.salvarProjetoEmpresaExecutora(projetoEmpresaExecutora);
			
			if(Util.isNullOuVazio(projeto.getProjetoEmpresaExecutoraCollection())) {
				projeto.setProjetoEmpresaExecutoraCollection(new ArrayList<ProjetoEmpresaExecutora>());
			}
			
			projeto.getProjetoEmpresaExecutoraCollection().add(projetoEmpresaExecutora);
			projetoEmpresaExecutora = null;
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formExecucaoProjeto:panelEmpresaExecutora");
		}
	}

	private boolean validarEmpresaExecutoraParaSalvarEmpresa() {
		
		for (ProjetoEmpresaExecutora pee : projeto.getProjetoEmpresaExecutoraCollection()) {
			if(pee.getIndInativa() == false) {
				JsfUtil.addWarnMessage("Cada projeto só poderá ter uma empresa ativa por vez, favor inativar a empresa atual antes de incluir uma nova.");
				return false;
			}
		}
		
		if(isRenderedEmpresaExecutora()) {
			if(projetoEmpresaExecutora.getIdePessoaExecutora() == null) {
				JsfUtil.addWarnMessage("Favor selecionar a empresa executora!");
				return false;
				
			} else if(Util.isNullOuVazio(projetoEmpresaExecutora.getDtVigenciaContratoInicio())
					|| Util.isNullOuVazio(projetoEmpresaExecutora.getDtVigenciaContratoFim())) {
				
				JsfUtil.addWarnMessage("Favor selecionar a data de inicial e final!");
				return false;
				
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean validarEmpresaExecutoraParaSalvarExecucaoProduto() {
		if(isRenderedEmpresaExecutora()) {
			
			if(Util.isNullOuVazio(projeto.getProjetoEmpresaExecutoraCollection())) {
				JsfUtil.addWarnMessage("Favor inserir uma empresa executora!");
				return false;
				
			} else {
				
				boolean existeEmpresaAtiva = false;
				
				for (ProjetoEmpresaExecutora pee : projeto.getProjetoEmpresaExecutoraCollection()) {
					if(pee.getIndInativa() == false) existeEmpresaAtiva = true;
				}
				
				if(!existeEmpresaAtiva) {
					JsfUtil.addWarnMessage("Favor inserir uma empresa executora ativa!");
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void salvarExecucaoProduto() {
		boolean produtoExecucaoNovo = false;
		boolean permiteAlterarValorPrevisto = false;
		
		if(!validarEmpresaExecutoraParaSalvarExecucaoProduto()) return;
		
		if(Util.isNullOuVazio(produtoExecucao)) produtoExecucaoNovo = true;
		else permiteAlterarValorPrevisto = verificaPermiteAlterarValorPrevisto();
		
		facade.salvarProdutoExecucao(produtoExecucao);
		
		if(!isDisabledValorExecutado() && !Util.isNullOuVazio(projeto.getProjetoEmpresaExecutoraCollection())) {
			if(tcca.isStatusVigente()) { 
				facade.salvarNovoStatusDoTcca(tcca, TccaProjetoTipoStatusEnum.EM_EXECUCAO);
			}
			
			if(projeto.isStatusPrevisto()) {
				facade.salvarNovoStatusDoProjeto(projeto, TccaProjetoTipoStatusEnum.EM_EXECUCAO);
			}
		}
		
		if(!Util.isNullOuVazio(produtoExecucao.getValPrevisto()) && produtoExecucaoNovo) {
			salvarMovimentacoesDoValorPrevisto();
		}
		
		if(permiteAlterarValorPrevisto) {
			salvarAlteracaoNoValorPrevisto(diffValorPrevisto);
		}
		
		if(!Util.isNullOuVazio(produtoExecucao.getValExecutado())) {
			salvarMovimentacoesDoValorExecutado();
		}
		
		tcca.setUltimoSaldo(facade.buscarUltimoTccaSaldoPorTcca(tcca));
		calcularSaldosDoProjeto(projeto);
		calcularValoresExecutadosNoProjeto(projeto);
		
		projeto.setProjetoExecutado(true);
		enviarTccaParaSessao(tcca, projeto);
		Html.redirecionar(PaginaEnum.CONSULTA_PROJETO);
	}
	
	public boolean verificaPermiteAlterarValorPrevisto() {
		diffValorPrevisto = BigDecimal.ZERO;
		ProdutoExecucao pe = facade.buscarProdutoExecucaoPorID(produtoExecucao);
		
		if(!Util.isNullOuVazio(pe) && !Util.isNullOuVazio(pe.getValPrevisto())
				&& (!Util.isNullOuVazio(pe.getValContratado()) || !Util.isNullOuVazio(pe.getValExecutado()))) {
			return false;
		}
		
		for (ProdutoSaldo ps : facade.listarProdutoSaldoPorProjetoAcaoProduto(pe.getIdeProjetoAcaoProduto())) {
			
			if(!Util.isNullOuVazio(ps.getIdeMovimentacaoFinanceira()) && !Util.isNullOuVazio(ps.getIdeMovimentacaoFinanceira().getIdeTccaProjetoOperacao())
					&& !ps.getIdeMovimentacaoFinanceira().getIdeTccaProjetoOperacao().getIdeTccaProjetoOperacao().equals(TccaProjetoOperacaoEnum.DESTINACAO_INICIAL.getId())
					&& !ps.getIdeMovimentacaoFinanceira().getIdeTccaProjetoOperacao().getIdeTccaProjetoOperacao().equals(TccaProjetoOperacaoEnum.DESTINACAO.getId())
					&& !ps.getIdeMovimentacaoFinanceira().getIdeTccaProjetoOperacao().getIdeTccaProjetoOperacao().equals(TccaProjetoOperacaoEnum.DEVOLUCAO.getId())
					&& !ps.getIdeMovimentacaoFinanceira().getIdeTccaProjetoOperacao().getIdeTccaProjetoOperacao().equals(TccaProjetoOperacaoEnum.REAJUSTE.getId())) {
				
				return false;
			}
		}
		
		if(produtoExecucao.getValPrevisto().compareTo(pe.getValPrevisto()) == 1) {
			diffValorPrevisto = produtoExecucao.getValPrevisto().subtract(pe.getValPrevisto());
		} else {
			diffValorPrevisto = pe.getValPrevisto().subtract(produtoExecucao.getValPrevisto());
		}
		
		return true;
	}

	private void salvarMovimentacoesDoValorPrevisto() {
		MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
		movimentacaoFinanceira.setValOperacao(produtoExecucao.getValPrevisto());
		movimentacaoFinanceira.setDtcOperacao(new Date());
		movimentacaoFinanceira.setIdeTcca(tcca);
		movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.DESTINACAO));
		movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(null);
		if(!Util.isNullOuVazio(produtoExecucao)) movimentacaoFinanceira.setIdeProdutoExecucao(produtoExecucao);
		
		MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
		movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
		movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_PRODUTO));
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(null);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(produtoExecucao.getIdeProjetoAcaoProduto());
		movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(tcca);
		movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(tcca);
		
		TccaSaldo ultimoTccaSaldo = facade.buscarUltimoTccaSaldoPorTcca(tcca);
		
		TccaSaldo tccaSaldo = new TccaSaldo();
		tccaSaldo.setValSaldoSuplementado(ultimoTccaSaldo.getValSaldoSuplementado());
		tccaSaldo.setValSaldoNaoDestinadoProjeto(ultimoTccaSaldo.getValSaldoNaoDestinadoProjeto().subtract(produtoExecucao.getValPrevisto()));
		tccaSaldo.setIdeTcca(tcca);
		tccaSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		ProdutoSaldo ultimoProdutoSaldo = facade.buscarUltimoProdutoSaldoPorProjetoAcaoProduto(produtoExecucao.getIdeProjetoAcaoProduto());
		
		ProdutoSaldo produtoSaldo = new ProdutoSaldo();
		produtoSaldo.setIdeProjetoAcaoProduto(produtoExecucao.getIdeProjetoAcaoProduto());
		produtoSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		if(Util.isNullOuVazio(ultimoProdutoSaldo)) {
			produtoSaldo.setValSaldoProduto(produtoExecucao.getValPrevisto());
			produtoSaldo.setValSaldoSuplementado(BigDecimal.ZERO);
			produtoSaldo.setValSaldoRemanejado(BigDecimal.ZERO);
		} else {
			produtoSaldo.setValSaldoProduto(ultimoProdutoSaldo.getValSaldoProduto().add(produtoExecucao.getValPrevisto()));
			produtoSaldo.setValSaldoSuplementado(ultimoProdutoSaldo.getValSaldoSuplementado());
			produtoSaldo.setValSaldoRemanejado(ultimoProdutoSaldo.getValSaldoRemanejado());
		}
		
		facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, tccaSaldo, null, produtoSaldo, null);
	}
	
	private void salvarAlteracaoNoValorPrevisto(BigDecimal diffValorPrevisto) {
		
		MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
		movimentacaoFinanceira.setValOperacao(diffValorPrevisto);
		movimentacaoFinanceira.setDtcOperacao(new Date());
		movimentacaoFinanceira.setIdeTcca(tcca);
		movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.DESTINACAO));
		movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(null);
		if(!Util.isNullOuVazio(produtoExecucao)) movimentacaoFinanceira.setIdeProdutoExecucao(produtoExecucao);
		
		MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
		movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
		movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_PRODUTO));
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(null);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(produtoExecucao.getIdeProjetoAcaoProduto());
		movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(tcca);
		movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(tcca);
		
		TccaSaldo ultimoTccaSaldo = facade.buscarUltimoTccaSaldoPorTcca(tcca);
		
		TccaSaldo tccaSaldo = new TccaSaldo();
		tccaSaldo.setValSaldoSuplementado(ultimoTccaSaldo.getValSaldoSuplementado());
		tccaSaldo.setValSaldoNaoDestinadoProjeto(ultimoTccaSaldo.getValSaldoNaoDestinadoProjeto().subtract(diffValorPrevisto));
		tccaSaldo.setIdeTcca(tcca);
		tccaSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		ProdutoSaldo ultimoProdutoSaldo = facade.buscarUltimoProdutoSaldoPorProjetoAcaoProduto(produtoExecucao.getIdeProjetoAcaoProduto());
		
		ProdutoSaldo produtoSaldo = new ProdutoSaldo();
		produtoSaldo.setIdeProjetoAcaoProduto(produtoExecucao.getIdeProjetoAcaoProduto());
		produtoSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		if(Util.isNullOuVazio(ultimoProdutoSaldo)) {
			produtoSaldo.setValSaldoProduto(diffValorPrevisto);
			produtoSaldo.setValSaldoSuplementado(BigDecimal.ZERO);
			produtoSaldo.setValSaldoRemanejado(BigDecimal.ZERO);
		} else {
			produtoSaldo.setValSaldoProduto(ultimoProdutoSaldo.getValSaldoProduto().add(diffValorPrevisto));
			produtoSaldo.setValSaldoSuplementado(ultimoProdutoSaldo.getValSaldoSuplementado());
			produtoSaldo.setValSaldoRemanejado(ultimoProdutoSaldo.getValSaldoRemanejado());
		}
		
		facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, tccaSaldo, null, produtoSaldo, null);
	}
	
	private void salvarMovimentacoesDoValorExecutado() {
		MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
		movimentacaoFinanceira.setValOperacao(produtoExecucao.getValExecutado());
		movimentacaoFinanceira.setDtcOperacao(new Date());
		movimentacaoFinanceira.setIdeTcca(tcca);
		movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.EXECUCAO));
		movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(null);
		if(!Util.isNullOuVazio(produtoExecucao)) movimentacaoFinanceira.setIdeProdutoExecucao(produtoExecucao);
		
		MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
		movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_PRODUTO));
		movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(null);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(produtoExecucao.getIdeProjetoAcaoProduto());
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(null);
		movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(tcca);
		movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(null);
		
		ProdutoSaldo saldo = facade.buscarUltimoProdutoSaldoPorProjetoAcaoProduto(produtoExecucao.getIdeProjetoAcaoProduto());
		
		ProdutoSaldo produtoSaldo = new ProdutoSaldo();
		produtoSaldo.setIdeProjetoAcaoProduto(produtoExecucao.getIdeProjetoAcaoProduto());
		produtoSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		if(Util.isNullOuVazio(saldo)) {
			produtoSaldo.setValSaldoProduto(produtoExecucao.getValExecutado());
			produtoSaldo.setValSaldoSuplementado(BigDecimal.ZERO);
			produtoSaldo.setValSaldoRemanejado(BigDecimal.ZERO);
		} else {
			produtoSaldo.setValSaldoProduto(saldo.getValSaldoProduto().subtract(produtoExecucao.getValExecutado()));
			produtoSaldo.setValSaldoSuplementado(saldo.getValSaldoSuplementado());
			produtoSaldo.setValSaldoRemanejado(saldo.getValSaldoRemanejado());
		}
		
		facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, null, null, produtoSaldo, null);
	}
	
	public void selecionarRequerente(Pessoa pessoa) {
		projetoEmpresaExecutora.setIdePessoaExecutora(pessoa);
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formExecucaoProjeto:panelEmpresaExecutora");
		if(RequestContext.getCurrentInstance() != null) RequestContext.getCurrentInstance().execute("dialogRequerente.hide()");
	}
	
	public void inativarEmpresaExecutora() {
		projetoEmpresaExecutoraSelecionada.setDtcInativa(new Date());
		projetoEmpresaExecutoraSelecionada.setIdePessoaFisicaInativadora(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		projetoEmpresaExecutoraSelecionada.setIndInativa(true);
		
		facade.salvarProjetoEmpresaExecutora(projetoEmpresaExecutoraSelecionada);
	}
	
	public void changeAcao(ValueChangeEvent event) {
		produtoSelecionado = null;
		tipoOrigemDestinoSelecionado = null;
		saldoOutroProjeto = false;
		saldoOutroTcca = false;
		saldoNaoDestinado = false;
	}
	
	/*********************
	/*					 *
	//XXX: MOVIMENTAÇÃO  *
	/* 					 *
	/*********************/
	
	public void exibirRelatorioMovimentacaoFinanceira(TccaProjeto proj){
		projeto = proj;
		projeto.setProjetoEmpresaExecutoraAtiva(facade.buscarProjetoEmpresaExecutoraAtivaPorProjeto(projeto));
		projeto.setListHistoricoMovimentacoes(facade.listarMovimentacaoFinanceiraTccaProdutoPorProjeto(projeto));

		Html.atualizar("formMovimentacaoFinanceira");
	}
	
	public void changeTipoDestino(AjaxBehaviorEvent event) {
		limparVariaveisChangeTipoDestino();
		
		if(tipoOrigemDestinoSelecionado != null) {
			if(tipoOrigemDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_OUTRO_PROJETO.getId()) {
				
				if(!Util.isNullOuVazio(tcca) && !Util.isNullOuVazio(tcca.getTccaProjetoCollection()) && tcca.getTccaProjetoCollection().size() < 2) {
					limparVariaveisChangeTipoDestino();
					tipoOrigemDestinoSelecionado = null;
					JsfUtil.addWarnMessage("Não é possível fazer a movimentação solicitada uma vez que só existe um único projeto cadastrado no respectivo TCCA");
					return;
				}
				
				saldoOutroProjeto = true;
				saldoOutroTcca = false;
				saldoNaoDestinado = false;
				
			} else if(tipoOrigemDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_OUTRO_TCCA.getId()) {
				saldoOutroProjeto = false;
				saldoOutroTcca = true;
				saldoNaoDestinado = false;
				
			} else {
				saldoOutroProjeto = false;
				saldoOutroTcca = false;
				saldoNaoDestinado = true;
			}
		}
	}
	
	public void limparVariaveisChangeTipoDestino() {
		tccaDestinoRecurso = new Tcca();
		projetoDestinoRecurso = null;
		acaoDestinoRecurso = null;
		produtoDestinoRecurso = null;
		
		saldoOutroProjeto = false;
		saldoOutroTcca = false;
		saldoNaoDestinado = false;
	}
	
	public void validarSalvarMovimentacaoFinanceiraProjeto() {
		
		if(Util.isNullOuVazio(valorMovimentacao)) {
			JsfUtil.addWarnMessage("Favor preencher o valor da operação!");
			return;
			
		} else if(Util.isNullOuVazio(produtoSelecionado.getUltimoProdutoSaldo())
				|| valorMovimentacao.compareTo(produtoSelecionado.getUltimoProdutoSaldo().getValSaldoProduto()) == 1) {
			
			Html.exibir("dialogConfirmarMovimentacaoSaldoInsuficiente");
			return;
			
		} else salvarMovimentacaoFinanceiraProjeto();
	}
	
	public void salvarMovimentacaoFinanceiraProjeto() {
		
		if(tipoOrigemDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA.getId()) {
			salvarMovimentacoesDoProdutoParaSaldoNaoDestinadoDoProjeto();
			
		} else if(tipoOrigemDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_OUTRO_PROJETO.getId()) {
			if(Util.isNullOuVazio(produtoDestinoRecurso)) {
				JsfUtil.addWarnMessage("Favor selecionar o produto de destino do recurso.");
				return;
			}
			
			salvarMovimentacoesDoProdutoParaSaldoOutroProduto();
		}
		
		tcca.setUltimoSaldo(facade.buscarUltimoTccaSaldoPorTcca(tcca));
		calcularSaldosDoProjeto(projeto);
		calcularValoresExecutadosNoProjeto(projeto);
		
		projeto.setProjetoMovimentado(true);
		enviarTccaParaSessao(tcca, projeto);
		Html.redirecionar(PaginaEnum.CONSULTA_PROJETO);
	}
	
	private void salvarMovimentacoesDoProdutoParaSaldoNaoDestinadoDoProjeto() {
		MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
		movimentacaoFinanceira.setValOperacao(valorMovimentacao);
		movimentacaoFinanceira.setDtcOperacao(new Date());
		movimentacaoFinanceira.setIdeTcca(tcca);
		movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.DEVOLUCAO));
		movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(null);
		if(!Util.isNullOuVazio(produtoExecucao)) movimentacaoFinanceira.setIdeProdutoExecucao(produtoExecucao);
		
		MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
		movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_PRODUTO));
		movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(produtoSelecionado);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(null);
		movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(tcca);
		movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(tcca);
		
		TccaSaldo ultimoSaldoTcca = facade.buscarUltimoTccaSaldoPorTcca(tcca);
		
		TccaSaldo tccaSaldo = new TccaSaldo();
		tccaSaldo.setValSaldoSuplementado(ultimoSaldoTcca.getValSaldoSuplementado());
		tccaSaldo.setValSaldoNaoDestinadoProjeto(ultimoSaldoTcca.getValSaldoNaoDestinadoProjeto().add(valorMovimentacao));
		tccaSaldo.setIdeTcca(tcca);
		tccaSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		ProdutoSaldo ultimoSaldoProduto = facade.buscarUltimoProdutoSaldoPorProjetoAcaoProduto(produtoSelecionado);
		
		ProdutoSaldo produtoSaldo = new ProdutoSaldo();
		produtoSaldo.setIdeProjetoAcaoProduto(produtoSelecionado);
		produtoSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		if(Util.isNullOuVazio(ultimoSaldoProduto)) {
			produtoSaldo.setValSaldoProduto(valorMovimentacao);
			produtoSaldo.setValSaldoSuplementado(BigDecimal.ZERO);
			produtoSaldo.setValSaldoRemanejado(valorMovimentacao);
			
		} else {
			produtoSaldo.setValSaldoProduto(ultimoSaldoProduto.getValSaldoProduto().subtract(valorMovimentacao));
			produtoSaldo.setValSaldoSuplementado(ultimoSaldoProduto.getValSaldoSuplementado());
			produtoSaldo.setValSaldoRemanejado(ultimoSaldoProduto.getValSaldoRemanejado().add(valorMovimentacao));
		}
		
		facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, tccaSaldo, null, produtoSaldo, null);
	}
	
	private void salvarMovimentacoesDoProdutoParaSaldoOutroProduto() {
		MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
		movimentacaoFinanceira.setValOperacao(valorMovimentacao);
		movimentacaoFinanceira.setDtcOperacao(new Date());
		movimentacaoFinanceira.setIdeTcca(tcca);
		movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.SUPLEMENTACAO));
		movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(null);
		if(!Util.isNullOuVazio(produtoExecucao)) movimentacaoFinanceira.setIdeProdutoExecucao(produtoExecucao);
		
		MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
		movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_PRODUTO));
		movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_OUTRO_PROJETO));
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(produtoSelecionado);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(produtoDestinoRecurso);
		movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(tcca);
		movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(tcca);
		
		ProdutoSaldo ultimoSaldoProduto = facade.buscarUltimoProdutoSaldoPorProjetoAcaoProduto(produtoSelecionado);
		
		ProdutoSaldo produtoSaldo = new ProdutoSaldo();
		produtoSaldo.setValSaldoProduto(ultimoSaldoProduto.getValSaldoProduto().subtract(valorMovimentacao));
		produtoSaldo.setValSaldoSuplementado(ultimoSaldoProduto.getValSaldoSuplementado());
		produtoSaldo.setValSaldoRemanejado(ultimoSaldoProduto.getValSaldoRemanejado().add(valorMovimentacao));
		produtoSaldo.setIdeProjetoAcaoProduto(produtoSelecionado);
		produtoSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		ProdutoSaldo ultimoSaldoProdutoDestino = facade.buscarUltimoProdutoSaldoPorProjetoAcaoProduto(produtoDestinoRecurso);
		
		ProdutoSaldo produtoDestinoSaldo = new ProdutoSaldo();
		produtoDestinoSaldo.setIdeProjetoAcaoProduto(produtoDestinoRecurso);
		produtoDestinoSaldo.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		if(Util.isNullOuVazio(ultimoSaldoProdutoDestino)) {
			produtoDestinoSaldo.setValSaldoProduto(valorMovimentacao);
			produtoDestinoSaldo.setValSaldoSuplementado(valorMovimentacao);
			produtoDestinoSaldo.setValSaldoRemanejado(BigDecimal.ZERO);
		} else {
			produtoDestinoSaldo.setValSaldoProduto(ultimoSaldoProdutoDestino.getValSaldoProduto().add(valorMovimentacao));
			produtoDestinoSaldo.setValSaldoSuplementado(ultimoSaldoProdutoDestino.getValSaldoSuplementado().add(valorMovimentacao));
			produtoDestinoSaldo.setValSaldoRemanejado(ultimoSaldoProdutoDestino.getValSaldoRemanejado());
		}
		
		facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, null, null, produtoSaldo, produtoDestinoSaldo);
	}
	
	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/
	
	public Tcca getTcca() {
		return tcca;
	}

	public void setTcca(Tcca tcca) {
		this.tcca = tcca;
	}

	public DataTable getDataTableProjeto() {
		return dataTableProjeto;
	}

	public void setDataTableProjeto(DataTable dataTableProjeto) {
		this.dataTableProjeto = dataTableProjeto;
	}

	public LazyDataModel<TccaProjeto> getDataModelProjeto() {
		return dataModelProjeto;
	}

	public void setDataModelProjeto(LazyDataModel<TccaProjeto> dataModelProjeto) {
		this.dataModelProjeto = dataModelProjeto;
	}

	public TccaProjeto getProjeto() {
		return projeto;
	}

	public void setProjeto(TccaProjeto projeto) {
		this.projeto = projeto;
	}

	public ProjetoAcao getAcao() {
		return acao;
	}

	public void setAcao(ProjetoAcao acao) {
		this.acao = acao;
	}

	public ProjetoAcaoProduto getProduto() {
		return produto;
	}

	public void setProduto(ProjetoAcaoProduto produto) {
		this.produto = produto;
	}

	public boolean isExibeGridNovoProduto() {
		return exibeGridNovoProduto;
	}

	public void setExibeGridNovoProduto(boolean exibeGridNovoProduto) {
		this.exibeGridNovoProduto = exibeGridNovoProduto;
	}

	public List<ProjetoAcao> getListAcoes() {
		return listAcoes;
	}

	public void setListAcoes(List<ProjetoAcao> listAcoes) {
		this.listAcoes = listAcoes;
	}

	public List<ProjetoAcaoProduto> getListProdutos() {
		return listProdutos;
	}

	public void setListProdutos(List<ProjetoAcaoProduto> listProdutos) {
		this.listProdutos = listProdutos;
	}

	public ProjetoAcao getAcaoSelecionada() {
		return acaoSelecionada;
	}

	public void setAcaoSelecionada(ProjetoAcao acaoSelecionada) {
		this.acaoSelecionada = acaoSelecionada;
	}

	public ProjetoAcaoProduto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(ProjetoAcaoProduto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public boolean isEditandoAcao() {
		return editandoAcao;
	}

	public void setEditandoAcao(boolean editandoAcao) {
		this.editandoAcao = editandoAcao;
	}

	public List<UnidadeConservacao> getListUnidadesConservacao() {
		return listUnidadesConservacao;
	}

	public void setListUnidadesConservacao(List<UnidadeConservacao> listUnidadesConservacao) {
		this.listUnidadesConservacao = listUnidadesConservacao;
	}

	public List<UnidadeConservacao> getListUnidadesConservacaoSelecionadas() {
		return listUnidadesConservacaoSelecionadas;
	}

	public void setListUnidadesConservacaoSelecionadas(List<UnidadeConservacao> listUnidadesConservacaoSelecionadas) {
		this.listUnidadesConservacaoSelecionadas = listUnidadesConservacaoSelecionadas;
	}

	public UnidadeConservacao getUnidadeConservacaoSelecionada() {
		return unidadeConservacaoSelecionada;
	}

	public void setUnidadeConservacaoSelecionada(UnidadeConservacao unidadeConservacaoSelecionada) {
		this.unidadeConservacaoSelecionada = unidadeConservacaoSelecionada;
	}

	public ProdutoExecucao getProdutoExecucao() {
		return produtoExecucao;
	}

	public void setProdutoExecucao(ProdutoExecucao produtoExecucao) {
		this.produtoExecucao = produtoExecucao;
	}

	public boolean isDesabilitaSalvarExecucaoProduto() {
		return desabilitaSalvarExecucaoProduto;
	}

	public void setDesabilitaSalvarExecucaoProduto(boolean desabilitaSalvarExecucaoProduto) {
		this.desabilitaSalvarExecucaoProduto = desabilitaSalvarExecucaoProduto;
	}

	public ProjetoEmpresaExecutora getProjetoEmpresaExecutora() {
		return projetoEmpresaExecutora;
	}

	public void setProjetoEmpresaExecutora(ProjetoEmpresaExecutora projetoEmpresaExecutora) {
		this.projetoEmpresaExecutora = projetoEmpresaExecutora;
	}

	public ProjetoEmpresaExecutora getProjetoEmpresaExecutoraSelecionada() {
		return projetoEmpresaExecutoraSelecionada;
	}

	public void setProjetoEmpresaExecutoraSelecionada(ProjetoEmpresaExecutora projetoEmpresaExecutoraSelecionada) {
		this.projetoEmpresaExecutoraSelecionada = projetoEmpresaExecutoraSelecionada;
	}

	public List<TipoOrigemDestino> getListTiposOrigemDestino() {
		return listTiposOrigemDestino;
	}

	public void setListTiposOrigemDestino(List<TipoOrigemDestino> listTiposOrigemDestino) {
		this.listTiposOrigemDestino = listTiposOrigemDestino;
	}

	public TipoOrigemDestino getTipoOrigemDestinoSelecionado() {
		return tipoOrigemDestinoSelecionado;
	}

	public void setTipoOrigemDestinoSelecionado(TipoOrigemDestino tipoOrigemDestinoSelecionado) {
		this.tipoOrigemDestinoSelecionado = tipoOrigemDestinoSelecionado;
	}

	public TccaProjeto getProjetoDestinoRecurso() {
		return projetoDestinoRecurso;
	}

	public void setProjetoDestinoRecurso(TccaProjeto projetoDestinoRecurso) {
		this.projetoDestinoRecurso = projetoDestinoRecurso;
	}

	public ProjetoAcao getAcaoDestinoRecurso() {
		return acaoDestinoRecurso;
	}

	public void setAcaoDestinoRecurso(ProjetoAcao acaoDestinoRecurso) {
		this.acaoDestinoRecurso = acaoDestinoRecurso;
	}

	public ProjetoAcaoProduto getProdutoDestinoRecurso() {
		return produtoDestinoRecurso;
	}

	public void setProdutoDestinoRecurso(ProjetoAcaoProduto produtoDestinoRecurso) {
		this.produtoDestinoRecurso = produtoDestinoRecurso;
	}

	public boolean isSaldoNaoDestinado() {
		return saldoNaoDestinado;
	}

	public void setSaldoNaoDestinado(boolean saldoNaoDestinado) {
		this.saldoNaoDestinado = saldoNaoDestinado;
	}

	public boolean isSaldoOutroProjeto() {
		return saldoOutroProjeto;
	}

	public void setSaldoOutroProjeto(boolean saldoOutroProjeto) {
		this.saldoOutroProjeto = saldoOutroProjeto;
	}

	public boolean isSaldoOutroTcca() {
		return saldoOutroTcca;
	}

	public void setSaldoOutroTcca(boolean saldoOutroTcca) {
		this.saldoOutroTcca = saldoOutroTcca;
	}

	public String getNumResolucao() {
		return numResolucao;
	}

	public void setNumResolucao(String numResolucao) {
		this.numResolucao = numResolucao;
	}

	public BigDecimal getValorMovimentacao() {
		return valorMovimentacao;
	}

	public void setValorMovimentacao(BigDecimal valorMovimentacao) {
		this.valorMovimentacao = valorMovimentacao;
	}

	public boolean isExibeDestinoRecursoMovimentacao() {
		return exibeDestinoRecursoMovimentacao;
	}

	public void setExibeDestinoRecursoMovimentacao(boolean exibeDestinoRecursoMovimentacao) {
		this.exibeDestinoRecursoMovimentacao = exibeDestinoRecursoMovimentacao;
	}
}