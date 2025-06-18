package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.dto.TccaRenovarPrazoDTO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.MovimentacaoFinanceira;
import br.gov.ba.seia.entity.MovimentacaoFinanceiraTccaProduto;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProdutoSaldo;
import br.gov.ba.seia.entity.ProjetoAcao;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.entity.ProjetoUnidadeConservacao;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaDocumentoApensado;
import br.gov.ba.seia.entity.TccaHistoricoReajusteValor;
import br.gov.ba.seia.entity.TccaHistoricoRenovacaoPrazoValidade;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.entity.TccaProjetoOperacao;
import br.gov.ba.seia.entity.TccaSaldo;
import br.gov.ba.seia.entity.TipoOrigemDestino;
import br.gov.ba.seia.entity.VwConsultaEmpreendimento;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TccaAcaoEnum;
import br.gov.ba.seia.enumerator.TccaProjetoOperacaoEnum;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoOrigemDestinoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("tccaController")
@ViewScoped
public class TccaController extends BaseTccaController {
	
	//TELA DE CONSULTA
	private Pessoa requerenteFiltro;
	private Empreendimento empreendimentoFiltro;
	private String numLicencaFiltro;
	private String numProcessoFiltro;
	private String numTCCAFiltro;
	private DataTable dataTableTCCA;
	private LazyDataModel<Tcca> dataModelTCCA;
	private boolean isTelaConsulta;
	
	//TELA DE CADASTRO
	private Tcca tcca;
	private boolean isTelaCadastro;
	private TccaDocumentoApensado documentoApensadoSelecionado;
	private TccaHistoricoReajusteValor tccaHistoricoReajusteValor;
	private LazyDataModel<VwConsultaEmpreendimento> empreendimentoDataModel;
	private String nomEmpreendimentoFiltro;
	private boolean isDadosObtidosDoProcesso;
	
	//RENOVAR PRAZO
	private TccaRenovarPrazoDTO tccaRenovarPrazoDTO;
	
	//REAJUSTAR VALOR
	private BigDecimal valorTccaReajustado;
	private BigDecimal valorSaldoNaoSuplementadoReajustado;
	
	//MOVIMENTAÇÃO
	private List<TipoOrigemDestino> listTiposOrigem;
	private List<TipoOrigemDestino> listTiposDestino;
	private TipoOrigemDestino tipoOrigemSelecionado;
	private TipoOrigemDestino tipoDestinoSelecionado;
	private boolean saldoNaoDestinado;
	private boolean saldoSuplementado;
	private boolean saldoOutroProjeto;
	private boolean saldoOutroTcca;	private String numResolucao;
	private BigDecimal valorMovimentacao;
	
	
	/*************
	/*			 *
	//XXX: GERAL *
	/* 			 *
	/*************/
	
	@PostConstruct
	public void init() {
	
		try{
			
			empreendimentoFiltro = new Empreendimento();
			
			tcca = ContextoUtil.getContexto().getTcca();
			enviarTccaParaSessao(null);
			
			if(Util.isNullOuVazio(tcca)) {
				tcca = new Tcca();
			} else {
				prepararDuplicacao();
				carregarTiposOrigemMovimentacaoFinanceiraTcca();
				carregarTiposDestinoMovimentacaoFinanceiraTcca();
				carregarProjetosAcoesEProdutos();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void acaoTcca(TccaAcaoEnum tccaAcaoEnum){
		
		if (tccaAcaoEnum.equals(TccaAcaoEnum.DUPLICAR)){
			tcca.setTccaDuplicado(true);
			prepararParaEditar();	
		}

		else if(tccaAcaoEnum.equals(TccaAcaoEnum.RENOVAR_PRAZO)){
			prepararParaRenovarPrazo();
			Html.exibir("dialogRenovarPrazoTcca");
		}
		
		else if(tccaAcaoEnum.equals(TccaAcaoEnum.REAJUSTAR_VALOR)){
			prepararParaReajustarValor();
			Html.exibir("dialogReajustarValorTcca");
		}
	}
	
	public void editarTcca() {
		try {
			tcca.setTccaDuplicado(false);
			prepararParaEditar();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void prepararParaEditar(){
		
		try {
			
			Empreendimento emp = tcca.getIdeEmpreendimento();
			
			selecionarRequerente(tcca.getIdePessoaRequerente());
			
			selecionarEmpreendimento(emp);

			tcca.setTccaDocumentoApensadoCollection(facade.listarDocumentosApensados(tcca));
			
			enviarTccaParaSessao(tcca);
			Html.redirecionar(PaginaEnum.CADASTRO_TCCA);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void atualizaMensagemPorPollAjax() {
		RequestContext.getCurrentInstance().execute("tccaPoll.stop();");
		
		if(tcca.isTccaCadastrado()) {
			JsfUtil.addSuccessMessage("TCCA Salvo com sucesso!");
			tcca.setTccaCadastrado(false);
		}
		
		if(tcca.isTccaDuplicado()) {
			JsfUtil.addSuccessMessage("TCCA Duplicado com sucesso!");
			tcca.setTccaDuplicado(false);
		}

		if(tcca.isTccaMovimentado()) {
			JsfUtil.addSuccessMessage("Movimentação do TCCA realizada com sucesso!");
			tcca.setTccaMovimentado(false);
		}
	}
	
	public void limparTelaConsulta() {
		requerenteFiltro = null;
		empreendimentoFiltro = new Empreendimento();
		numLicencaFiltro = null;
		numProcessoFiltro = null;
		numTCCAFiltro = null;
		dataModelTCCA = null;
	}
	
	public void selecionarRequerente(Pessoa pessoa) {
		
		if(isTelaConsulta) {
			requerenteFiltro = pessoa;
			isTelaConsulta = false;
			if(RequestContext.getCurrentInstance() != null) RequestContext.getCurrentInstance().addPartialUpdateTarget("formConsultaTCCA:requerenteFiltro");
			
		} else if(isTelaCadastro) {
			//TELEFONES
			pessoa.setTelefoneCollection(facade.buscarTelefonesPorPessoa(pessoa));
			
			//REPRESENTANTES
			if(!Util.isNullOuVazio(pessoa.getPessoaJuridica())) {
				pessoa.setRepresentanteLegalCollection(facade.buscarRepresentantesLegaisPorPessoa(pessoa.getPessoaJuridica()));
			}
			
			//ENDEREÇO
			Collection<EnderecoPessoa> ep = new ArrayList<EnderecoPessoa>();
			ep.add(facade.buscarEnderecoPorPessoa(pessoa));
			pessoa.setEnderecoPessoaCollection(ep);
			
			tcca.setIdePessoaRequerente(pessoa);
			
			tcca.setIdeEmpreendimento(null);
			empreendimentoDataModel = null;
			nomEmpreendimentoFiltro = null;
			
			isTelaCadastro = false;
			
			if(RequestContext.getCurrentInstance() != null) RequestContext.getCurrentInstance().addPartialUpdateTarget("formCadastroTCCA");
		}
		
		if(RequestContext.getCurrentInstance() != null) RequestContext.getCurrentInstance().execute("dialogRequerente.hide()");
	}
	
	public void consultarTCCA() {
		try {
			dataTableTCCA.setFirst(0);
			dataTableTCCA.setPage(1);
			
			dataModelTCCA = new LazyDataModel<Tcca>() {

				private static final long serialVersionUID = 7717411591034117580L;

				@Override
				public List<Tcca> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					
					List<Tcca> list = facade.listarTcca(requerenteFiltro, empreendimentoFiltro, numLicencaFiltro, numProcessoFiltro, numTCCAFiltro, first, pageSize);
					
					for (Tcca t : list) {
						t.setUltimoSaldo(facade.buscarUltimoTccaSaldoPorTcca(t));
					}
					
					return list; 
				}
			};
			
			dataModelTCCA.setRowCount(facade.countConsultaTcca(requerenteFiltro, empreendimentoFiltro, numLicencaFiltro, numProcessoFiltro, numTCCAFiltro));
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirTcca() {
		facade.excluirTcca(tcca);
		JsfUtil.addSuccessMessage("TCCA excluído com sucesso!");
	}
	
	public void cancelarTcca() {
		facade.salvarNovoStatusDoTcca(tcca, TccaProjetoTipoStatusEnum.CANCELADO);
		JsfUtil.addSuccessMessage("TCCA cancelado com sucesso!");
	}
	
	public void enviarTccaParaSessao(Tcca tcca) {
		ContextoUtil.getContexto().setTcca(tcca);
	}
	
	/****************
	/*				*
	//XXX: CADASTRO *
	/* 				*
	/****************/
	
	public void uploadArquivoTcca(FileUploadEvent event) {
		
		try {
			if(Util.isNullOuVazio(tcca.getTccaDocumentoApensadoCollection())) {
				TccaDocumentoApensado tda = new TccaDocumentoApensado();
				tda.setIdeTcca(tcca);
				tda.setIndExcluido(false);
				tda.setNomDocumento(event.getFile().getFileName());
				tda.setArquivoUpload(event);

				tcca.setTccaDocumentoApensadoCollection(new ArrayList<TccaDocumentoApensado>());
				tcca.getTccaDocumentoApensadoCollection().add(tda);
				
				JsfUtil.addSuccessMessage("Arquivo inserido com sucesso!");
			} else {
				JsfUtil.addWarnMessage("Favor excluir o arquivo anterior.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao carregar o arquivo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isExisteTccaDocumentoApensado() {
		if(tcca != null && !Util.isNullOuVazio(tcca.getTccaDocumentoApensadoCollection())) {
			return true;
		}
		
		return false;
	}
	
	public void excluirDocumentoApensado() {
		
		if(!Util.isNullOuVazio(tcca.getTccaDocumentoApensadoCollection())) {
			
			if(!Util.isNullOuVazio(documentoApensadoSelecionado)) {
				documentoApensadoSelecionado.setIndExcluido(true);
				facade.salvarTCCADocumentoApensado(documentoApensadoSelecionado);
			}
			
			tcca.getTccaDocumentoApensadoCollection().remove(documentoApensadoSelecionado);
			JsfUtil.addSuccessMessage("Arquivo excluído com sucesso!");
		}
	}
	
	public void buscarEmpreendimentoPorRequerente() {
		try {
			empreendimentoDataModel = new LazyDataModel<VwConsultaEmpreendimento>() {

				private static final long serialVersionUID = 7717411591034117580L;

				@Override
				public List<VwConsultaEmpreendimento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					setPageSize(pageSize);
					
					List<VwConsultaEmpreendimento> empreendimentos = 
							facade.listarEmpreendimentoPorRequerente(null, tcca.getIdePessoaRequerente(), nomEmpreendimentoFiltro, first, pageSize);
					
					return empreendimentos;
				}
			};
			
			empreendimentoDataModel.setRowCount(
					facade.countEmpreendimentoPorRequerente(null, tcca.getIdePessoaRequerente(), nomEmpreendimentoFiltro));
			
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void selecionarEmpreendimento(Empreendimento emp) {
		emp = facade.buscarEmpreendimentoPorIdComMunicipio(emp.getIdeEmpreendimento());
		emp.setEmpreendimentoTipologiaCollection(facade.listarEmpreendimentoTipologia(emp));
		
		tcca.setIdeEmpreendimento(emp);
	}
	
	public void buscarProcessoPorNumero() {
		Processo p = facade.buscarProcessoPorNumero(tcca.getNumProcessoLicenca());
		
		if(!Util.isNullOuVazio(p) && !Util.isNullOuVazio(p.getIdeRequerimento())) {
			isDadosObtidosDoProcesso = true;
			isTelaCadastro = true;
			
			selecionarRequerente(p.getIdeRequerimento().getRequerente());
			
			selecionarEmpreendimento(p.getIdeRequerimento().getUltimoEmpreendimento());
		} else {
			isDadosObtidosDoProcesso = false;
			tcca.setIdePessoaRequerente(null);
			tcca.setIdeEmpreendimento(null);
		}
	}
	
	public void salvarTCCA() {
			
		Integer ideTccaAntigo = null;
		boolean isTccaNovo = false;
		
		if(!tcca.isTccaDuplicado()){		
			tcca.setIndPossuiTccaOrigem(false);
			tcca.setIndExcluido(false);
			tcca.setIndCancelado(false);
			tcca.setIndReajustado(false);
			tcca.setIndRenovado(false);
		}
		
		if(isTccaValidado()) {
			
			if(tcca.isTccaDuplicado()){
				ideTccaAntigo = tcca.getIdeTcca();
				tcca.setIdeTcca(null);
			}
			
			if(tcca.getIdeTcca() == null) {
				isTccaNovo = true;
			}
			
			//SALVA O TCCA
			facade.salvarTCCA(tcca);
			
			//SALVA O DOCUMENTO APENSADO
			if(!Util.isNullOuVazio(tcca.getTccaDocumentoApensadoCollection())) {
				for (TccaDocumentoApensado tda : tcca.getTccaDocumentoApensadoCollection()) {
					if(tda.getArquivoUpload() != null) {
						tda.setUrlTccaDocumento(FileUploadUtil.Enviar(tda.getArquivoUpload(), DiretorioArquivoEnum.TCCA.toString() + tcca.getIdeTcca().toString() + FileUploadUtil.getFileSeparator()));
						facade.salvarTCCADocumentoApensado(tda);
					}
				}
			}
			
			//SALVA OS STATUS
			if(isTccaNovo) {
				facade.salvarNovoStatusDoTcca(tcca, TccaProjetoTipoStatusEnum.CADASTRO_INCOMPLETO);
			}
			
			//REALIZA A DUPLICAÇÃO DOS FILHOS DO TCCA
			if(tcca.isTccaDuplicado()){
				salvarDadosDuplicados(ideTccaAntigo);
			}
			
			//SALVA O SALDO E A MOVIMENTAÇÃO FINANCEIRA
			TccaSaldo ultimoSaldo = facade.buscarUltimoTccaSaldoPorTcca(tcca);
			if(Util.isNullOuVazio(ultimoSaldo)) {
				salvarPrimeiroSaldoEMovimentacaoFinanceira();
			} else {
				MovimentacaoFinanceira movF = facade.buscarMovimentacaoFinanceiraPorID(ultimoSaldo.getIdeMovimentacaoFinanceira());
				
				if(!Util.isNullOuVazio(movF) && !Util.isNullOuVazio(movF.getIdeTccaProjetoOperacao())
						&& movF.getIdeTccaProjetoOperacao().getIdeTccaProjetoOperacao().equals(TccaProjetoOperacaoEnum.DESTINACAO_INICIAL.getId())) {
					
					salvarPrimeiroSaldoEMovimentacaoFinanceira();
				}
			}
			
			//Redireciona a página e exibe a mensagem de sucesso através do boolean + contexto + pool
			if(!tcca.isTccaDuplicado()){
				tcca.setTccaCadastrado(true);
			}
			
			enviarTccaParaSessao(tcca);
			Html.redirecionar(PaginaEnum.CONSULTA_TCCA);
		}
	}
	
	private boolean isTccaValidado() {
		
		if(Util.isNullOuVazio(tcca.getNumTcca())) {
			JsfUtil.addWarnMessage("Favor preencher o número do TCCA");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getNumProcessoLicenca())) {
			JsfUtil.addWarnMessage("Favor preencher o número de processo/licença");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getNumProcessoSema())) {
			JsfUtil.addWarnMessage("Favor preencher o número do processo SEMA");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getValTcca())) {
			JsfUtil.addWarnMessage("Favor preencher o valor do TCCA");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getValGradacaoImpacto())) {
			JsfUtil.addWarnMessage("Favor preencher o percentual da GI");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getNumPrazoValidade())) {
			JsfUtil.addWarnMessage("Favor preencher o prazo de validade");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getIndModalidadeExecucaoDireta())) {
			JsfUtil.addWarnMessage("Favor preencher a forma de execução de compensação ambiental");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getDtAssinatura())) {
			JsfUtil.addWarnMessage("Favor preencher a data de assinatura");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getDtPublicacao())) {
			JsfUtil.addWarnMessage("Favor preencher a data de publicação");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getIndOrigemLicenciamentoEstadual())) {
			JsfUtil.addWarnMessage("Favor preencher o tipo do empreendimento");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getIdePessoaRequerente())) {
			JsfUtil.addWarnMessage("Favor selecionar o requerente");
			return false;
			
		} else if(Util.isNullOuVazio(tcca.getIdeEmpreendimento())) {
			JsfUtil.addWarnMessage("Favor selecionar o empreendimento");
			return false;
		}
		
		return true;
	}
	
	private void salvarPrimeiroSaldoEMovimentacaoFinanceira() {
		MovimentacaoFinanceira movF = new MovimentacaoFinanceira();
		movF.setValOperacao(tcca.getValTcca());
		movF.setDtcOperacao(new Date());
		movF.setIdeTcca(tcca);
		movF.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movF.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.DESTINACAO_INICIAL));
		
		MovimentacaoFinanceiraTccaProduto movFTP = new MovimentacaoFinanceiraTccaProduto();
		movFTP.setIdeMovimentacaoFinanceira(movF);
		movFTP.setIdeTipoDestino(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
		movFTP.setIdeTccaDestino(tcca);
		
		TccaSaldo saldo = new TccaSaldo();
		saldo.setIdeTcca(tcca);
		saldo.setIdeMovimentacaoFinanceira(movF);
		saldo.setValSaldoSuplementado(BigDecimal.ZERO);
		saldo.setValSaldoNaoDestinadoProjeto(tcca.getValTcca());
		
		facade.salvarMovimentacoesESaldos(movF, movFTP, saldo, null, null, null);
	}
	
	/*********************
	/*					 *
	//XXX: DUPLICAR TCCA *
	/* 					 *
	/*********************/

	public boolean renderizaBotaoDuplicarTcca(Tcca tcca){
		return tcca.isStatusCancelado() && !isTccaComProdutoSaldoNegativo(tcca) && !isTccaProjetoCanceladoComSaldo(tcca);
	}
	
	private boolean isTccaComProdutoSaldoNegativo(Tcca tcca){
		
		for (TccaProjeto projeto : facade.listarTccaProjetoPorTcca(tcca, null, null)){
			for(ProdutoSaldo saldo : facade.listarUltimosProdutoSaldoPorTccaProjeto(projeto)){
				if(saldo.getValSaldoProduto() != null && saldo.getValSaldoProduto().compareTo(BigDecimal.ZERO) == -1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean isTccaProjetoCanceladoComSaldo(Tcca tcca){
		List<TccaProjeto> listProjetosCancelados = new ArrayList<TccaProjeto>();
		
		for (TccaProjeto projeto : facade.listarTccaProjetoPorTcca(tcca, null, null)){
			
			projeto.setUltimoStatus(facade.buscarUltimoProjetoStatusPorTccaProjeto(projeto));
			
			if(!Util.isNullOuVazio(projeto.getUltimoStatus())) {
				if(projeto.getUltimoStatus().getIdeProjetoStatus().equals(TccaProjetoTipoStatusEnum.CANCELADO.getId())){
					listProjetosCancelados.add(projeto);
				}
			}
		}
		
		for (TccaProjeto projeto: listProjetosCancelados){
			for (ProdutoSaldo saldo : facade.listarUltimosProdutoSaldoPorTccaProjeto(projeto)) {
				if(saldo.getValSaldoProduto() != null && saldo.getValSaldoProduto().compareTo(BigDecimal.ZERO) != 0){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void prepararDuplicacao() {
		if(tcca.isTccaDuplicado()){
			tcca.setNumTccaOrigem(tcca.getNumTcca());
			tcca.setNumTcca("");
			tcca.setValTcca(BigDecimal.ZERO);
			tcca.setTccaDocumentoApensadoCollection(new ArrayList<TccaDocumentoApensado>());
		}
	}
	
	private void salvarDadosDuplicados(Integer ideTccaAntigo) {
		
		List<TccaProjeto> projetosParaduplicar = facade.listarTccaProjetoPorTcca(new Tcca(ideTccaAntigo), null, null);
		
		for(TccaProjeto tccaProjeto : projetosParaduplicar){
			tccaProjeto.setProjetoAcaoCollection(facade.listarProjetoAcaoPorTccaProjeto(tccaProjeto));
			tccaProjeto.setProjetoUnidadeConservacaoCollection(facade.listarProjetoUnidadeConservacaoPorProjeto(tccaProjeto));
			tccaProjeto.setIdeTcca(tcca);
			tccaProjeto.setIdeTccaProjeto(null);
			
			for (ProjetoAcao projetoAcao : tccaProjeto.getProjetoAcaoCollection()) {
				
				projetoAcao.setProjetoAcaoProdutoCollection(facade.listarProjetoAcaoProdutoPorProjetoAcao(projetoAcao));
				projetoAcao.setIdeTccaProjeto(tccaProjeto);
				projetoAcao.setIdeProjetoAcao(null);
				
				for (ProjetoAcaoProduto projetoAcaoProduto : projetoAcao.getProjetoAcaoProdutoCollection()) {
					
					projetoAcaoProduto.setIdeProjetoAcao(projetoAcao);
					projetoAcaoProduto.setIdeProjetoAcaoProduto(null);
					
					facade.salvarTccaProjeto(tccaProjeto);
					facade.salvarProjetoAcao(projetoAcao);
					facade.salvarProjetoAcaoProduto(projetoAcaoProduto);
					
					//SALVA AS UNIDADES DE CONSERVAÇÃO DO PROJETO
					for (ProjetoUnidadeConservacao puc : tccaProjeto.getProjetoUnidadeConservacaoCollection()) {
						puc.setIdeProjetoUnidadeConservacao(null);
						puc.setIdeTccaProjeto(tccaProjeto);
						
						facade.salvarProjetoUnidadeConservacao(puc);
					}
					
					//SALVA OS STATUS DO TCCA NOVO
					facade.salvarNovoStatusDoProjeto(tccaProjeto, TccaProjetoTipoStatusEnum.CADASTRO_INCOMPLETO);
					
					if(validarFinalizacaoProjeto(tccaProjeto, null)) {
						facade.salvarNovoStatusDoTcca(tcca, TccaProjetoTipoStatusEnum.VIGENTE);
						facade.salvarNovoStatusDoProjeto(tccaProjeto, TccaProjetoTipoStatusEnum.PREVISTO);
					}
					
					//SALVA OS STATUS DOS PROJETOS DO TCCA ANTIGO
					for (TccaProjeto tp : facade.listarTccaProjetoPorTcca(new Tcca(ideTccaAntigo), null, null)) {
						facade.salvarNovoStatusDoProjeto(tp, TccaProjetoTipoStatusEnum.REMANEJADO);
					}
				}
			}
		}
	}
	
	/************************
	/*						*
	//XXX: REAJUSTAR VALOR  *
	/* 						*
	/************************/
	
	private void prepararParaReajustarValor(){
		
		tcca.setUltimoSaldo(facade.buscarUltimoTccaSaldoPorTcca(tcca));
		
		tccaHistoricoReajusteValor = new TccaHistoricoReajusteValor(tcca);
		tccaHistoricoReajusteValor.setValTcca(tcca.getValTcca());
		tccaHistoricoReajusteValor.setValTccaAnterior(tcca.getValTcca());
		tccaHistoricoReajusteValor.setIndiceReajuste(BigDecimal.ZERO);
		tccaHistoricoReajusteValor.setDtcReajuste(new Date());
		
		valorTccaReajustado = BigDecimal.ZERO;
		valorSaldoNaoSuplementadoReajustado = BigDecimal.ZERO; 
		
		Html.atualizar("formReajustarValor");
	}
	
	public void calcularReajustarValor(){	
		
		//VALOR DE REAJUSTE QUE SERÁ ACRESCIDO AO VALOR ANTIGO DO TCCA
		tccaHistoricoReajusteValor.setValReajuste(
				tccaHistoricoReajusteValor.getValTcca().multiply(
					tccaHistoricoReajusteValor.getIndiceReajuste()
						.divide(new BigDecimal("100"))));
		
		//VALOR DO TCCA APÓS O REAJUSTE
		valorTccaReajustado = tccaHistoricoReajusteValor.getValReajuste().add(tccaHistoricoReajusteValor.getValTcca());
		
		//VALOR DO SALDO DISPONÍVEL DO TCCA
		valorSaldoNaoSuplementadoReajustado = tcca.getUltimoSaldo().getValSaldoNaoDestinadoProjeto().add(tccaHistoricoReajusteValor.getValReajuste());
		
		Html.atualizar("formReajustarValor");
	}

	public void salvarReajustarValor(){	
		
		if(tccaHistoricoReajusteValor.getIndiceReajuste().compareTo(BigDecimal.ZERO)==0) {
			MensagemUtil.msg0003("Índice de reajuste");
			
		} else {
			tcca.setIndReajustado(true);
			tcca.setValTcca(valorTccaReajustado);
			facade.salvarTCCA(tcca);
		
			tccaHistoricoReajusteValor.setValTcca(valorTccaReajustado);
			facade.salvarTccaHistoricoReajusteValor(tccaHistoricoReajusteValor);
			
			MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
			movimentacaoFinanceira.setValOperacao(tccaHistoricoReajusteValor.getValReajuste());
			movimentacaoFinanceira.setDtcOperacao(new Date());
			movimentacaoFinanceira.setIdeTcca(tcca);
			movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.REAJUSTE));
			movimentacaoFinanceira.setIdeProdutoExecucao(null);
			movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(tccaHistoricoReajusteValor);
			
			MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
			movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
			movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(null);
			movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
			movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(null);
			movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(null);
			movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(null);
			movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(tcca);
			
			TccaSaldo ultimoSaldoTccaOrigem = facade.buscarUltimoTccaSaldoPorTcca(tcca);
			
			TccaSaldo tccaSaldoOrigem = new TccaSaldo();
			tccaSaldoOrigem.setIdeTcca(tcca);
			tccaSaldoOrigem.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
			tccaSaldoOrigem.setValSaldoNaoDestinadoProjeto(valorSaldoNaoSuplementadoReajustado);
			
			if(Util.isNullOuVazio(ultimoSaldoTccaOrigem)) {
				tccaSaldoOrigem.setValSaldoSuplementado(BigDecimal.ZERO);
			} else {
				tccaSaldoOrigem.setValSaldoSuplementado(ultimoSaldoTccaOrigem.getValSaldoSuplementado());
			}
			
			facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, tccaSaldoOrigem, null, null, null);
			
			Html.esconder("dialogReajustarValorTcca");
			MensagemUtil.msg0001();
			Html.atualizar("formConsultaTCCA");
		}
	}
	
	/*********************
	/*					 *
	//XXX: RENOVAR PRAZO *
	/* 					 *
	/*********************/
	
	public boolean renderRenovarPrazo(Tcca tcca) {
		
		if(tcca.isStatusVigente() || tcca.isStatusEmExecucao()) {
			Date dataVencimentoMenosSeisMeses = tcca.getDtcVencimento();
			Calendar dtV6M = Calendar.getInstance();
			dtV6M.setTime(dataVencimentoMenosSeisMeses);		
			dtV6M.add(Calendar.MONTH, - 6);
			dataVencimentoMenosSeisMeses = dtV6M.getTime();
			
			Date dataVencimentoMaisDezDias = tcca.getDtcVencimento();
			Calendar dtV10D = Calendar.getInstance();
			dtV10D.setTime(dataVencimentoMaisDezDias);		
			dtV10D.add(Calendar.DAY_OF_MONTH, + 10);
			dataVencimentoMaisDezDias = dtV10D.getTime();
			
			if((new Date().compareTo(dataVencimentoMenosSeisMeses)==0 || new Date().after(dataVencimentoMenosSeisMeses)) 
					&& (new Date().compareTo(dataVencimentoMaisDezDias)==0 || new Date().before(dataVencimentoMaisDezDias))) {
				
				return true;
			}
		}
		
		return false;
	}
	
	private void prepararParaRenovarPrazo(){
		tccaRenovarPrazoDTO = new TccaRenovarPrazoDTO();
		tccaRenovarPrazoDTO.setNumTcca(tcca.getNumTcca());
		tccaRenovarPrazoDTO.setDtcPublicacao(tcca.getDtPublicacao());
		tccaRenovarPrazoDTO.setQtdMesesPrazoValidade(tcca.getNumPrazoValidade());
		
		Html.atualizar("formRenovarPrazo");
	}
	
	public void renovarPrazoDeValidadePorIgualPeriodo(){	
		
		Calendar c = Calendar.getInstance();
		c.setTime(tccaRenovarPrazoDTO.getDtcVencimento());
		c.add(Calendar.MONTH, + tccaRenovarPrazoDTO.getQtdMesesPrazoValidade());
		
		tccaRenovarPrazoDTO.setNewDtcVencimento(c.getTime());
		tccaRenovarPrazoDTO.setDtcRenovacao(tccaRenovarPrazoDTO.getDtcVencimento());
		tccaRenovarPrazoDTO.setQtdMesesPrazoValidadeRenovacao(tccaRenovarPrazoDTO.getQtdMesesPrazoValidade());
		
		Html.atualizar("formRenovarPrazo");
	}
	
	public void calcularNovaDataVencimento() {
		if(tccaRenovarPrazoDTO.getDtcRenovacao() != null && !Util.isNullOuVazio(tccaRenovarPrazoDTO.getQtdMesesPrazoValidadeRenovacao())) {
			Calendar c = Calendar.getInstance();
			c.setTime(tccaRenovarPrazoDTO.getDtcRenovacao());
			c.add(Calendar.MONTH, + tccaRenovarPrazoDTO.getQtdMesesPrazoValidadeRenovacao());
			
			tccaRenovarPrazoDTO.setNewDtcVencimento(c.getTime());
		}
	}
	
	private boolean isValidoRenovacao(){
		
		if(tccaRenovarPrazoDTO.getDtcRenovacao() == null) {
			MensagemUtil.msg0003("Data da renovação");
			return false;
			
		} else if(tccaRenovarPrazoDTO.getQtdMesesPrazoValidadeRenovacao().equals(0)) {
			MensagemUtil.msg0003("Prazo de validade");
			return false;
		}
		
		return true;
	}
	
	public void salvarRenovarPrazoValidade(){
		
		if(isValidoRenovacao()){
			
			TccaHistoricoRenovacaoPrazoValidade renovacao = new TccaHistoricoRenovacaoPrazoValidade();
	
			renovacao.setDtcRenovacao(tccaRenovarPrazoDTO.getDtcRenovacao());
			renovacao.setNumPrazoValidade(tccaRenovarPrazoDTO.getQtdMesesPrazoValidadeRenovacao());
			renovacao.setDtNovoPrazoValidade(tccaRenovarPrazoDTO.getNewDtcVencimento());
			
			renovacao.setIdePessoaFisicaRenovacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			renovacao.setIdeTcca(tcca);
			facade.salvarTccaRenovacao(renovacao);
			
			tcca.setIndRenovado(true);
			tcca.setDtPublicacao(tccaRenovarPrazoDTO.getDtcRenovacao());
			tcca.setNumPrazoValidade(tccaRenovarPrazoDTO.getQtdMesesPrazoValidadeRenovacao());
			facade.salvarTCCA(tcca);
			
			Html.esconder("dialogRenovarPrazoTcca");
			Html.atualizar("formConsultaTCCA");
			MensagemUtil.msg0001();
		}
	}

	/*********************
	/*					 *
	//XXX: MOVIMENTAÇÃO  *
	/* 					 *
	/*********************/
	
	public void exibirRelatorioMovimentacaoFinanceira(Tcca t){
		tcca = t;
		tcca.setListHistoricoMovimentacoes(facade.listarMovimentacaoFinanceiraTccaProdutoPorTcca(tcca));
		tcca.setListHistoricoReajustes(facade.listarTccaHistoricoReajusteValorPorTcca(tcca));
		
		Html.atualizar("formMovimentacaoFinanceira");
	}
	
	private void carregarTiposOrigemMovimentacaoFinanceiraTcca() {
		listTiposOrigem = new ArrayList<TipoOrigemDestino>();
		listTiposOrigem.add(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
		listTiposOrigem.add(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_SUPLEMENTADO));
	}
	
	private void carregarTiposDestinoMovimentacaoFinanceiraTcca() {
		listTiposDestino = new ArrayList<TipoOrigemDestino>();
		listTiposDestino.add(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_OUTRO_TCCA));
		listTiposDestino.add(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_OUTRO_PROJETO));
	}
	
	private void carregarProjetosAcoesEProdutos() {
		List<TccaProjeto> list = facade.listarTccaProjetoPorTcca(tcca, null, null);
		
		for (TccaProjeto tp : list) {
			tp = calcularSaldosDoProjeto(tp);
			tp = calcularValoresExecutadosNoProjeto(tp);
			tp.setUltimoStatus(facade.buscarUltimoProjetoStatusPorTccaProjeto(tp));
		}
		
		tcca.setTccaProjetoCollection(list);
	}
	
	public void changeTipoOrigem(AjaxBehaviorEvent event) {
		limparVariaveisChangeTipoOrigem();
		
		if(tipoOrigemSelecionado != null) {
			if(tipoOrigemSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA.getId()) {
				
				if(isSaldoNaoPositivo(tcca.getUltimoSaldo().getValSaldoNaoDestinadoProjeto())) {
					limparVariaveisChangeTipoOrigem();
					tipoOrigemSelecionado = null;
					JsfUtil.addWarnMessage("Saldo insuficiente para a movimentação desejada.");
					return;
				}
				
				saldoNaoDestinado = true;
				saldoSuplementado = false;
				
			} else if(tipoOrigemSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_SUPLEMENTADO.getId()) {
				
				if(isSaldoNaoPositivo(tcca.getUltimoSaldo().getValSaldoSuplementado())) {
					limparVariaveisChangeTipoOrigem();
					tipoOrigemSelecionado = null;
					JsfUtil.addWarnMessage("Saldo insuficiente para a movimentação desejada.");
					return;
				}
				
				saldoNaoDestinado = false;
				saldoSuplementado = true;
				
				tipoDestinoSelecionado = listTiposDestino.get(1); //Força a seleção em SALDO DE OUTRO PROJETO
				saldoOutroProjeto = true;
			}
		}
	}
	
	public boolean isSaldoNaoPositivo(BigDecimal val) {
		if(val.compareTo(BigDecimal.ZERO) == 0 || val.compareTo(BigDecimal.ZERO) == -1) return true;
		else return false;
	}
	
	public void limparVariaveisChangeTipoOrigem() {
		tccaDestinoRecurso = new Tcca();
		projetoDestinoRecurso = null;
		acaoDestinoRecurso = null;
		produtoDestinoRecurso = null;
		
		saldoNaoDestinado = false;
		saldoSuplementado = false;
		
		tipoDestinoSelecionado = null;
		saldoOutroTcca = false;
		saldoOutroProjeto = false;
	}
	
	public void changeTipoDestino(AjaxBehaviorEvent event) {
		tccaDestinoRecurso = new Tcca();
		projetoDestinoRecurso = null;
		acaoDestinoRecurso = null;
		produtoDestinoRecurso = null;

		saldoOutroTcca = false;
		saldoOutroProjeto = false;
		
		if(tipoDestinoSelecionado != null) {
			if(tipoDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_OUTRO_TCCA.getId()) {
				saldoOutroTcca = true;
				saldoOutroProjeto = false;
				
			} else if(tipoDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_OUTRO_PROJETO.getId()) {
				saldoOutroTcca = false;
				saldoOutroProjeto = true;
			}
		}
	}

	public void validarSalvarMovimentacaoFinanceiraTcca() {
		
		if(Util.isNullOuVazio(valorMovimentacao)) {
			JsfUtil.addWarnMessage("Favor preencher o valor da operação!");
			return;
			
		} else if(Util.isNullOuVazio(tcca.getUltimoSaldo())
				
			|| (tipoOrigemSelecionado.getIdeTipoOrigemDestino().equals(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA.getId())
					&& valorMovimentacao.compareTo(tcca.getUltimoSaldo().getValSaldoNaoDestinadoProjeto()) == 1)
			
			|| (tipoOrigemSelecionado.getIdeTipoOrigemDestino().equals(TipoOrigemDestinoEnum.SALDO_SUPLEMENTADO.getId())
				&& valorMovimentacao.compareTo(tcca.getUltimoSaldo().getValSaldoSuplementado()) == 1)) {
			
			Html.exibir("dialogConfirmarMovimentacaoSaldoInsuficiente");
			return;
		}
		
		salvarMovimentacaoFinanceiraTcca();
	}
	
	public void salvarMovimentacaoFinanceiraTcca() {
		try {
			if(tipoDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_OUTRO_TCCA.getId()) {
				if(Util.isNullOuVazio(tccaDestinoRecurso)) {
					JsfUtil.addWarnMessage("Favor selecionar o TCCA de destino do recurso.");
					return;
				}
					
				salvarMovimentacoesDoTccaParaSaldoOutroTcca();
				
			} else if(tipoDestinoSelecionado.getIdeTipoOrigemDestino() == TipoOrigemDestinoEnum.SALDO_OUTRO_PROJETO.getId()) {
				if(Util.isNullOuVazio(produtoDestinoRecurso)) {
					JsfUtil.addWarnMessage("Favor selecionar o produto de destino do recurso.");
					return;
				}
				
				salvarMovimentacoesDoProdutoParaSaldoOutroProduto();
				
			}
			
			tcca.setUltimoSaldo(facade.buscarUltimoTccaSaldoPorTcca(tcca));
			
			tcca.setTccaMovimentado(true);
			enviarTccaParaSessao(tcca);
			Html.redirecionar(PaginaEnum.CONSULTA_TCCA);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarMovimentacoesDoTccaParaSaldoOutroTcca() {
		MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
		movimentacaoFinanceira.setValOperacao(valorMovimentacao);
		movimentacaoFinanceira.setDtcOperacao(new Date());
		movimentacaoFinanceira.setIdeTcca(tcca);
		movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.SUPLEMENTACAO));
		movimentacaoFinanceira.setIdeProdutoExecucao(null);
		movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(null);
		
		MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
		movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA));
		movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(new TipoOrigemDestino(TipoOrigemDestinoEnum.SALDO_OUTRO_TCCA));
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(null);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(null);
		movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(tcca);
		movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(tccaDestinoRecurso);
		
		TccaSaldo ultimoSaldoTccaOrigem = facade.buscarUltimoTccaSaldoPorTcca(tcca);
		
		TccaSaldo tccaSaldoOrigem = new TccaSaldo();
		tccaSaldoOrigem.setValSaldoSuplementado(ultimoSaldoTccaOrigem.getValSaldoSuplementado());
		tccaSaldoOrigem.setValSaldoNaoDestinadoProjeto(ultimoSaldoTccaOrigem.getValSaldoNaoDestinadoProjeto().subtract(valorMovimentacao));
		tccaSaldoOrigem.setIdeTcca(tcca);
		tccaSaldoOrigem.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		TccaSaldo ultimoSaldoTccaDestino = facade.buscarUltimoTccaSaldoPorTcca(tccaDestinoRecurso);
		
		TccaSaldo tccaSaldoDestino = new TccaSaldo();
		tccaSaldoDestino.setIdeTcca(tccaDestinoRecurso);
		tccaSaldoDestino.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		if(Util.isNullOuVazio(ultimoSaldoTccaDestino)) {
			tccaSaldoDestino.setValSaldoSuplementado(valorMovimentacao);
			tccaSaldoDestino.setValSaldoNaoDestinadoProjeto(BigDecimal.ZERO);
		} else {
			tccaSaldoDestino.setValSaldoSuplementado(ultimoSaldoTccaDestino.getValSaldoSuplementado().add(valorMovimentacao));
			tccaSaldoDestino.setValSaldoNaoDestinadoProjeto(ultimoSaldoTccaDestino.getValSaldoNaoDestinadoProjeto());
		}
		
		facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, tccaSaldoOrigem, tccaSaldoDestino, null, null);
	}

	private void salvarMovimentacoesDoProdutoParaSaldoOutroProduto() throws Exception {
		MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
		movimentacaoFinanceira.setValOperacao(valorMovimentacao);
		movimentacaoFinanceira.setDtcOperacao(new Date());
		movimentacaoFinanceira.setIdeTcca(tcca);
		movimentacaoFinanceira.setIdePessoaFisicaOperacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		movimentacaoFinanceira.setIdeTccaProjetoOperacao(new TccaProjetoOperacao(TccaProjetoOperacaoEnum.SUPLEMENTACAO));
		movimentacaoFinanceira.setIdeProdutoExecucao(null);
		movimentacaoFinanceira.setIdeTccaHistoricoReajusteValor(null);
		
		MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto = new MovimentacaoFinanceiraTccaProduto();
		movimentacaoFinanceiraTccaProduto.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		movimentacaoFinanceiraTccaProduto.setIdeTipoOrigem(tipoOrigemSelecionado);
		movimentacaoFinanceiraTccaProduto.setIdeTipoDestino(tipoDestinoSelecionado);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoOrigem(null);
		movimentacaoFinanceiraTccaProduto.setIdeProjetoAcaoProdutoDestino(produtoDestinoRecurso);
		movimentacaoFinanceiraTccaProduto.setIdeTccaOrigem(tcca);
		movimentacaoFinanceiraTccaProduto.setIdeTccaDestino(tcca);
		
		TccaSaldo ultimoSaldoTccaOrigem = facade.buscarUltimoTccaSaldoPorTcca(tcca);
		
		TccaSaldo tccaSaldoOrigem = new TccaSaldo();
		tccaSaldoOrigem.setIdeTcca(tcca);
		tccaSaldoOrigem.setIdeMovimentacaoFinanceira(movimentacaoFinanceira);
		
		if(!Util.isNullOuVazio(ultimoSaldoTccaOrigem)) {
			if(tipoOrigemSelecionado.getIdeTipoOrigemDestino().equals(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA.getId())) {
				tccaSaldoOrigem.setValSaldoSuplementado(ultimoSaldoTccaOrigem.getValSaldoSuplementado());
				tccaSaldoOrigem.setValSaldoNaoDestinadoProjeto(ultimoSaldoTccaOrigem.getValSaldoNaoDestinadoProjeto().subtract(valorMovimentacao));
			} else {
				tccaSaldoOrigem.setValSaldoSuplementado(ultimoSaldoTccaOrigem.getValSaldoSuplementado().subtract(valorMovimentacao));
				tccaSaldoOrigem.setValSaldoNaoDestinadoProjeto(ultimoSaldoTccaOrigem.getValSaldoNaoDestinadoProjeto());
			}
		} else {
			if(tipoOrigemSelecionado.getIdeTipoOrigemDestino().equals(TipoOrigemDestinoEnum.SALDO_DISPONIVEL_TCCA.getId())) {
				tccaSaldoOrigem.setValSaldoSuplementado(BigDecimal.ZERO);
				tccaSaldoOrigem.setValSaldoNaoDestinadoProjeto(valorMovimentacao);
			} else {
				tccaSaldoOrigem.setValSaldoSuplementado(valorMovimentacao);
				tccaSaldoOrigem.setValSaldoNaoDestinadoProjeto(BigDecimal.ZERO);
			}
		}
		
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
		
		facade.salvarMovimentacoesESaldos(movimentacaoFinanceira, movimentacaoFinanceiraTccaProduto, tccaSaldoOrigem, null, null, produtoDestinoSaldo);
	}
	
	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/

	public Pessoa getRequerenteFiltro() {
		return requerenteFiltro;
	}

	public void setRequerenteFiltro(Pessoa requerenteFiltro) {
		this.requerenteFiltro = requerenteFiltro;
	}

	public Empreendimento getEmpreendimentoFiltro() {
		return empreendimentoFiltro;
	}

	public void setEmpreendimentoFiltro(Empreendimento empreendimentoFiltro) {
		this.empreendimentoFiltro = empreendimentoFiltro;
	}

	public String getNumLicencaFiltro() {
		return numLicencaFiltro;
	}

	public void setNumLicencaFiltro(String numLicencaFiltro) {
		this.numLicencaFiltro = numLicencaFiltro;
	}

	public String getNumProcessoFiltro() {
		return numProcessoFiltro;
	}

	public void setNumProcessoFiltro(String numProcessoFiltro) {
		this.numProcessoFiltro = numProcessoFiltro;
	}

	public String getNumTCCAFiltro() {
		return numTCCAFiltro;
	}

	public void setNumTCCAFiltro(String numTCCAFiltro) {
		this.numTCCAFiltro = numTCCAFiltro;
	}

	public DataTable getDataTableTCCA() {
		return dataTableTCCA;
	}

	public void setDataTableTCCA(DataTable dataTableTCCA) {
		this.dataTableTCCA = dataTableTCCA;
	}

	public LazyDataModel<Tcca> getDataModelTCCA() {
		return dataModelTCCA;
	}

	public void setDataModelTCCA(LazyDataModel<Tcca> dataModelTCCA) {
		this.dataModelTCCA = dataModelTCCA;
	}

	public boolean isTelaConsulta() {
		return isTelaConsulta;
	}

	public void setTelaConsulta(boolean isTelaConsulta) {
		this.isTelaConsulta = isTelaConsulta;
	}

	public Tcca getTcca() {
		return tcca;
	}

	public void setTcca(Tcca tcca) {
		this.tcca = tcca;
	}

	public boolean isTelaCadastro() {
		return isTelaCadastro;
	}

	public void setTelaCadastro(boolean isTelaCadastro) {
		this.isTelaCadastro = isTelaCadastro;
	}

	public TccaDocumentoApensado getDocumentoApensadoSelecionado() {
		return documentoApensadoSelecionado;
	}

	public void setDocumentoApensadoSelecionado(TccaDocumentoApensado documentoApensadoSelecionado) {
		this.documentoApensadoSelecionado = documentoApensadoSelecionado;
	}

	public TccaHistoricoReajusteValor getTccaHistoricoReajusteValor() {
		return tccaHistoricoReajusteValor;
	}

	public void setTccaHistoricoReajusteValor(TccaHistoricoReajusteValor tccaHistoricoReajusteValor) {
		this.tccaHistoricoReajusteValor = tccaHistoricoReajusteValor;
	}

	public LazyDataModel<VwConsultaEmpreendimento> getEmpreendimentoDataModel() {
		return empreendimentoDataModel;
	}

	public void setEmpreendimentoDataModel(LazyDataModel<VwConsultaEmpreendimento> empreendimentoDataModel) {
		this.empreendimentoDataModel = empreendimentoDataModel;
	}

	public String getNomEmpreendimentoFiltro() {
		return nomEmpreendimentoFiltro;
	}

	public void setNomEmpreendimentoFiltro(String nomEmpreendimentoFiltro) {
		this.nomEmpreendimentoFiltro = nomEmpreendimentoFiltro;
	}

	public TccaRenovarPrazoDTO getTccaRenovarPrazoDTO() {
		return tccaRenovarPrazoDTO;
	}

	public void setTccaRenovarPrazoDTO(TccaRenovarPrazoDTO tccaRenovarPrazoDTO) {
		this.tccaRenovarPrazoDTO = tccaRenovarPrazoDTO;
	}

	public BigDecimal getValorTccaReajustado() {
		return valorTccaReajustado;
	}

	public void setValorTccaReajustado(BigDecimal valorTccaReajustado) {
		this.valorTccaReajustado = valorTccaReajustado;
	}

	public BigDecimal getValorSaldoNaoSuplementadoReajustado() {
		return valorSaldoNaoSuplementadoReajustado;
	}

	public void setValorSaldoNaoSuplementadoReajustado(BigDecimal valorSaldoNaoSuplementadoReajustado) {
		this.valorSaldoNaoSuplementadoReajustado = valorSaldoNaoSuplementadoReajustado;
	}

	public List<TipoOrigemDestino> getListTiposOrigem() {
		return listTiposOrigem;
	}

	public void setListTiposOrigem(List<TipoOrigemDestino> listTiposOrigem) {
		this.listTiposOrigem = listTiposOrigem;
	}

	public List<TipoOrigemDestino> getListTiposDestino() {
		return listTiposDestino;
	}

	public void setListTiposDestino(List<TipoOrigemDestino> listTiposDestino) {
		this.listTiposDestino = listTiposDestino;
	}

	public TipoOrigemDestino getTipoOrigemSelecionado() {
		return tipoOrigemSelecionado;
	}

	public void setTipoOrigemSelecionado(TipoOrigemDestino tipoOrigemSelecionado) {
		this.tipoOrigemSelecionado = tipoOrigemSelecionado;
	}

	public TipoOrigemDestino getTipoDestinoSelecionado() {
		return tipoDestinoSelecionado;
	}

	public void setTipoDestinoSelecionado(TipoOrigemDestino tipoDestinoSelecionado) {
		this.tipoDestinoSelecionado = tipoDestinoSelecionado;
	}

	public boolean isSaldoNaoDestinado() {
		return saldoNaoDestinado;
	}

	public void setSaldoNaoDestinado(boolean saldoNaoDestinado) {
		this.saldoNaoDestinado = saldoNaoDestinado;
	}

	public boolean isSaldoSuplementado() {
		return saldoSuplementado;
	}

	public void setSaldoSuplementado(boolean saldoSuplementado) {
		this.saldoSuplementado = saldoSuplementado;
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

	public boolean isDadosObtidosDoProcesso() {
		return isDadosObtidosDoProcesso;
	}

	public void setDadosObtidosDoProcesso(boolean isDadosObtidosDoProcesso) {
		this.isDadosObtidosDoProcesso = isDadosObtidosDoProcesso;
	}
}