package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.inject.Named;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PessoaFisicaHistoricoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("procuradorPessoaFisicaController")
@ViewScoped
public class ProcuradorPessoaFisicaController {
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private PessoaFisicaHistoricoService pessoaFisicaHistoricoService;
	
	private PessoaFisica pessoaSelecionada;
	private PessoaFisica pessoaPesquisa;
	private ProcuradorPessoaFisica procuradorPessoaFisica;
	private PessoaFisica donoDoProcurador;
	private List<ProcuradorPessoaFisica> listaProcuradorPessoaFisica;
	
	private boolean showExpandirFormCadastro;
	private boolean renderedFormPessoaFisicaProcurador;
	private boolean disabledFormPessoaFisicaProcurador;
	
	private DataModel<ProcuradorPessoaFisica> modelProcuradorPessoaFisica;
	
	public void consultarPessoaFisicaPorCpf() {
		try {
			pessoaSelecionada = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaPesquisa);
			if (!Util.isNull(pessoaSelecionada)) {
				disabledFormPessoaFisicaProcurador = true;
				renderedFormPessoaFisicaProcurador = true;
			}
			else {
				MensagemUtil.alerta(BUNDLE.getString("procuradorMsgErroCpfNaoCadastrado"));
			}
			
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@PostConstruct
	public void init() {
		tratarDonoDoProcurador();
		pessoaSelecionada = new PessoaFisica();
		pessoaPesquisa = new PessoaFisica();		
		listaProcuradorPessoaFisica = new ArrayList<ProcuradorPessoaFisica>();
		procuradorPessoaFisica = new ProcuradorPessoaFisica();
		showExpandirFormCadastro = true;
	}
	
	private void tratarDonoDoProcurador() {
		Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		
		if (usuarioLogado.isUsuarioExterno() 
				|| (ContextoUtil.getContexto().getCadastroInCompleto() != null && ContextoUtil.getContexto().getCadastroInCompleto())) {
			
			if (ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().equals(ContextoUtil.getContexto().getPessoaFisica()) || Util.isNull(ContextoUtil.getContexto().getPessoaFisica())) {
				donoDoProcurador = usuarioLogado.getPessoaFisica();
			} else {
				donoDoProcurador = ContextoUtil.getContexto().getPessoaFisica();
			}
		} else {
			donoDoProcurador = ContextoUtil.getContexto().getPessoaFisica();
		}
		if (Util.isNull(donoDoProcurador)) {
			donoDoProcurador = new PessoaFisica();
		}
	}

	public void salvarPessoa() {
		try {
			procuradorPessoaFisica.setIdeProcurador(pessoaSelecionada);
			procuradorPessoaFisica.setIdePessoaFisica(donoDoProcurador);
			
			if (validarProcurador()) {
				
				pessoaSelecionada.setNumCpf(pessoaPesquisa.getNumCpf());
				pessoaSelecionada.setTipSexo(null);
				
				showExpandirFormCadastro = true;
				
				String msgSucesso = BUNDLE.getString("geral_msg_alteracao_sucesso");
				
				if (Util.isNull(procuradorPessoaFisica.getIdeProcuradorPessoaFisica())) {
					msgSucesso = BUNDLE.getString("geral_msg_inclusao_sucesso");
				}
				
				if (!procuradorAlreadyProcuradorPF()) {
					procuradorPessoaFisicaService.salvarProcuradorPessoaFisica(procuradorPessoaFisica);
					pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisica());
					loadListaProcuradorPessoaFisica();
					MensagemUtil.sucesso(msgSucesso);
				}
				else {
					MensagemUtil.alerta("Procurador(a) já cadastrado(a) para essa Pessoa Física");
				}
			}
			else {
				MensagemUtil.alerta(BUNDLE.getString("procuradorMsgErroProcuradorDeleMesmo"));
			}
			Html.atualizar("paneltabviewpf");
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisica() {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico.setIdePessoaFisica(procuradorPessoaFisica.getIdePessoaFisica());
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setIdeProcuradorPessoaFisica(procuradorPessoaFisica);
		pessoaFisicaHistorico.setStatusProcurador(true);
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		
		return pessoaFisicaHistorico;
	}

	private boolean procuradorAlreadyProcuradorPF() {
		try {
			Boolean procuradorAlreadyProcuradorPF = null;
			if(Util.isNullOuVazio(procuradorPessoaFisica.getIdeProcurador().getIdePessoaFisica())){
				return false;
			}
			procuradorAlreadyProcuradorPF = procuradorPessoaFisicaService.procuradorAlreadyProcuradorPF(procuradorPessoaFisica);
			return procuradorAlreadyProcuradorPF;
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION); 
		}
	}

	public void limparObjetos() {
		this.pessoaSelecionada = new PessoaFisica();
		this.procuradorPessoaFisica = new ProcuradorPessoaFisica();
		pessoaPesquisa = new PessoaFisica();
		renderedFormPessoaFisicaProcurador=false;
		disabledFormPessoaFisicaProcurador=false;
	}

	public String limparObjetosPessoaFisicaAction() {
		this.pessoaSelecionada = new PessoaFisica();
		this.procuradorPessoaFisica = new ProcuradorPessoaFisica();
		disabledFormPessoaFisicaProcurador = false;
		renderedFormPessoaFisicaProcurador= false;
		pessoaPesquisa = new PessoaFisica();
		return "";
	}

	public PessoaFisica getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(PessoaFisica pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	private Boolean validarProcurador() {
		Boolean retorno = Boolean.TRUE;
		
		try {
			if (!Util.isNull(procuradorPessoaFisica) 
					&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica()) 
					&& !Util.isNull(procuradorPessoaFisica.getIdeProcurador()) 
					&& !Util.isNull(procuradorPessoaFisica.getIdeProcurador().getIdePessoaFisica())
					&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica()) 
					&& procuradorPessoaFisica.getIdeProcurador().getIdePessoaFisica().intValue() == procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica().intValue()) {
				
				retorno = Boolean.FALSE;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao relacionar Procurador com Pessoa Física.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return retorno;
	}

	public void loadListaProcuradorPessoaFisica() {
		try {
			procuradorPessoaFisica.setIdePessoaFisica(donoDoProcurador);
			listaProcuradorPessoaFisica = (List<ProcuradorPessoaFisica>) procuradorPessoaFisicaService.listarProcuradorPessoaFisica(procuradorPessoaFisica);
		}
		catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void excluirProcuradorPessoaFisica() {
		try {
			procuradorPessoaFisicaService.excluirProcuradorPessoaFisica(procuradorPessoaFisica);
			pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisicaExclusaoProcurador());
		}
		catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisicaExclusaoProcurador() {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico = pessoaFisicaHistoricoService.selecionarPorIdeProcuradorPessoaFisica(procuradorPessoaFisica);
		
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setStatusProcurador(false);
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		
		return pessoaFisicaHistorico;
	}
	

	public PessoaFisica getPessoaPesquisa() {
		return pessoaPesquisa;
	}

	public void setPessoaPesquisa(PessoaFisica pessoaPesquisa) {
		this.pessoaPesquisa = pessoaPesquisa;
	}

	public List<ProcuradorPessoaFisica> getListaProcuradorPessoaFisica() {
		return listaProcuradorPessoaFisica;
	}

	public void setListaProcuradorPessoaFisica(List<ProcuradorPessoaFisica> listaProcuradorPessoaFisica) {
		this.listaProcuradorPessoaFisica = listaProcuradorPessoaFisica;
	}

	public DataModel<ProcuradorPessoaFisica> getModelProcuradorPessoaFisica() {
		loadListaProcuradorPessoaFisica();
		if (!Util.isNull(listaProcuradorPessoaFisica) && !listaProcuradorPessoaFisica.isEmpty()) {
			modelProcuradorPessoaFisica = Util.castListToDataModel(listaProcuradorPessoaFisica);
		} else {
			modelProcuradorPessoaFisica = null;
		}
		return modelProcuradorPessoaFisica;
	}

	public void setModelProcuradorPessoaFisica(DataModel<ProcuradorPessoaFisica> modelProcuradorPessoaFisica) {
		this.modelProcuradorPessoaFisica = modelProcuradorPessoaFisica;
	}

	public ProcuradorPessoaFisica getProcuradorPessoaFisica() {
		return procuradorPessoaFisica;
	}

	public void setProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica) {
		this.procuradorPessoaFisica = procuradorPessoaFisica;
	}

	public PessoaFisica getDonoDoProcurador() {
		return donoDoProcurador;
	}

	public void setDonoDoProcurador(PessoaFisica donoDoProcurador) {
		this.donoDoProcurador = donoDoProcurador;
	}

	public boolean getShowExpandirFormCadastro() {
		return showExpandirFormCadastro;
	}

	public void setShowExpandirFormCadastro(boolean showExpandirFormCadastro) {
		this.showExpandirFormCadastro = showExpandirFormCadastro;
	}

	public void setaPessoaFisicaUsuario() {
		pessoaSelecionada = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	}

	public boolean isRenderedFormPessoaFisicaProcurador() {
		return renderedFormPessoaFisicaProcurador;
	}

	public void setRenderedFormPessoaFisicaProcurador(boolean renderedFormPessoaFisicaProcurador) {
		this.renderedFormPessoaFisicaProcurador = renderedFormPessoaFisicaProcurador;
	}

	public boolean isDisabledFormPessoaFisicaProcurador() {
		return disabledFormPessoaFisicaProcurador;
	}

	public void setDisabledFormPessoaFisicaProcurador(boolean disabledFormPessoaFisicaProcurador) {
		this.disabledFormPessoaFisicaProcurador = disabledFormPessoaFisicaProcurador;
	}
}
