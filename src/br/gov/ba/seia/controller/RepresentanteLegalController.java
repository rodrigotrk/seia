package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaHistoricoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("representanteLegalController")
@ViewScoped
public class RepresentanteLegalController extends PessoaEndereco {
	
	private PessoaFisica pessoaFisica;
	private PessoaJuridica pessoaJuridica;
	private PessoaFisica pessoaSelecionada;
	private PessoaFisica pessoaPesquisa;
	private Boolean editMode;	
	private Boolean enableFormPessoaFisica;
	private Boolean disableTabRepLegal;
	private Boolean disableTabDependenciasRepLegal;
	private DataModel<RepresentanteLegal> modelRepresentanteLegal;	
	private RepresentanteLegal representanteLegalSelecionado;
	private Boolean emptyList;
	private int tabAtual;
	private static Integer abaMAX = 3;
	private Boolean visualizaProximo;
	private Boolean visualizaAnterior;	
	private Boolean temArquivo;
	private List<String> listaArquivo;
	private String caminhoArquivo;
	private StreamedContent arquivoBaixar; 
	private Boolean telaIdentificacaoPapel;
	private Boolean disableBotoes;
	private Boolean permitirAddRepLegal;
	private Boolean pessoaCadastrada;
	private Boolean representanteLegalDaPessoaSelecionada;
	private Boolean showExpandirFormCadastro;
	private Boolean habilitaCalendarios;
	private Boolean iniciarAbaTelefone;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB PessoaJuridicaHistoricoService pessoaJuridicaHistoricoService;
	
	@PostConstruct
	public void init(){
		
		pessoaFisica = new PessoaFisica();
		pessoaJuridica = new PessoaJuridica();
		super.pessoa = new Pessoa();
		pessoaSelecionada = new PessoaFisica();
		pessoaPesquisa = new PessoaFisica();
		editMode = false;	
		enableFormPessoaFisica = false;
		disableTabRepLegal = true;
		disableTabDependenciasRepLegal = true;
		tratarPessoaJuridicaSessao();
		carregarRepresentantesLegais();
		representanteLegalSelecionado = new RepresentanteLegal();
		setTabAtual(0);
		temArquivo = false;
		listaArquivo = new ArrayList<String>();
		telaIdentificacaoPapel = false;
		disableBotoes = false;
		permitirAddRepLegal = false;
		pessoaCadastrada = false;
		showExpandirFormCadastro = true;
		iniciarAbaTelefone = false;
	}
	
	public String salvarPessoa() {
		
		if(!editMode && existeRepresentanteNaLista()){
			JsfUtil.addErrorMessage("Representante Legal já cadastrado.");
			return null;
		}
		
		try {
			
			if(habilitaCalendarios) {
				representanteLegalSelecionado.setDtcInicioRepresentacao(null);
				representanteLegalSelecionado.setDtcFimRepresentacao(null);
			}
			
			if (Util.validaPeriodo(representanteLegalSelecionado.getDtcInicioRepresentacao(), representanteLegalSelecionado.getDtcFimRepresentacao())) {
				if (!Util.isNullOuVazio(caminhoArquivo)) {
					salvarPessoaFisica();
					representanteLegalSelecionado.setDscCaminhoRepresentacao(caminhoArquivo);
					representanteLegalSelecionado.setDtcExclusao(null);
					representanteLegalSelecionado.setIdePessoaJuridica(pessoaJuridica);
					representanteLegalSelecionado.setIndExcluido(false);
					representanteLegalSelecionado.setIdePessoaFisica(pessoaSelecionada);
					representanteLegalSelecionado.setDtcInicio(new Date());
					representanteLegalService.salvarRepresentanteLegal(representanteLegalSelecionado);
					disableTabDependenciasRepLegal = false;
					carregarRepresentantesLegais();
					pessoaJuridicaHistoricoService.salvarPessoaJuridicaHistorico(preencherHistoricoPessoaJuridica(true));
					if(editMode){
						JsfUtil.addSuccessMessage("Alteração efetuada com sucesso!");
					}else{
						JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
					}
					caminhoArquivo= null;
					this.editMode = false;
					limparObjetosPessoaFisica();
				} else {
					JsfUtil.addErrorMessage("O campo Cópia do Estatuto Social é de preenchimento obrigatório.");
				}
			} else {
				JsfUtil.addErrorMessage("Data Início da representação deve ser anterior a Data Final da representação!");
			}
 		} catch (Exception e) {
 			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro: Ao persistir os dados.");
		}
		
		return null;
	}
	
	public PessoaJuridicaHistorico preencherHistoricoPessoaJuridica(Boolean adicaoRemocao) {
		PessoaJuridicaHistorico pessoaJuridicaHistorico = new PessoaJuridicaHistorico();
		PessoaJuridicaHistorico oldPessoaJuridicaHistorico = new PessoaJuridicaHistorico();
		
		oldPessoaJuridicaHistorico = pessoaJuridicaHistoricoService.buscarHistoricoAnterior(pessoaJuridica);
		
		pessoaJuridicaHistorico.setIdePessoaJuridica(pessoaJuridica);
		pessoaJuridicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaJuridicaHistorico.setDtcHistorico(new Date());
		pessoaJuridicaHistorico.setIdeRepresentanteLegal(representanteLegalSelecionado);
		pessoaJuridicaHistorico.setStatusRepresentante(adicaoRemocao);
		
		if(!Util.isNullOuVazio(pessoaJuridica.getNumInscricaoMunicipal())) {
			pessoaJuridicaHistorico.setNumInscricaoMunicipal(pessoaJuridica.getNumInscricaoMunicipal());
		}
		
		if(!Util.isNullOuVazio(oldPessoaJuridicaHistorico)) {
			pessoaJuridicaHistorico.setDscCaminhoArquivo(oldPessoaJuridicaHistorico.getDscCaminhoArquivo());
			pessoaJuridicaHistorico.setIdeProcesso(oldPessoaJuridicaHistorico.getIdeProcesso());
			pessoaJuridicaHistorico.setNomeFantasia(oldPessoaJuridicaHistorico.getNomeFantasia());
			pessoaJuridicaHistorico.setDtcAbertura(oldPessoaJuridicaHistorico.getDtcAbertura());
			pessoaJuridicaHistorico.setNomNaturezaJuridica(oldPessoaJuridicaHistorico.getNomNaturezaJuridica());
		} 
		
		
		return pessoaJuridicaHistorico;
	}
	
	private void salvarPessoaFisica() {
			
		if (Util.isNullOuVazio(super.pessoa.getIdePessoa() )) {
			super.pessoa.setDtcCriacao(new Date());
		}
		
		super.pessoa.setDtcExclusao(null);
		super.pessoa.setIndExcluido(false);
		pessoaSelecionada.setNumCpf(pessoaPesquisa.getNumCpf());
		pessoaSelecionada.setPessoa(super.pessoa );
		pessoaSelecionada.setTipSexo(null);
		super.pessoa.setPessoaFisica(pessoaSelecionada);
		
		try {
			
			pessoaFisicaService.salvarOuAtualizarPessoaFisica(pessoaSelecionada);
			permitirAddRepLegal = false;
	
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
        	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	//Resolve o #8465
	public void consultarPessoaFisicaPorCpf() {
	
		if(Util.isNullOuVazio(pessoaPesquisa.getNumCpf())){
			JsfUtil.addErrorMessage("O campo CPF é Obrigatório..");
			limpaFormulario(new ActionEvent(null));
		}else{
		
			try {
				pessoaSelecionada = pessoaFisicaService.consultarPessoaFisicaByNumCpf(pessoaPesquisa.getNumCpf());
				if(!Util.isNullOuVazio(pessoaSelecionada) && !Util.isNullOuVazio(pessoaSelecionada.getPessoa())){
					
					if(pessoaSelecionada.getPessoa().getIndExcluido()){
						JsfUtil.addErrorMessage("O Cpf nº: "+ pessoaSelecionada.getPessoa().getCpfCnpjFormatado()+" Está vinculado a um cadastro inativo.");
						enableFormPessoaFisica = false;
					}
					else{
						enableFormPessoaFisica = true;
						super.pessoa  = pessoaSelecionada.getPessoa();
						disableTabDependenciasRepLegal = false;
					}
				}else {
					enableFormPessoaFisica = true;
					pessoaSelecionada = new PessoaFisica();
					super.pessoa  = new Pessoa();
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Não foi possivel realizar a ação.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}			
		}	
	}
		
	public void limparObjetosPessoaFisicaAction(){
		limparObjetosPessoaFisica();
	}
	
	public void limparObjetosPessoaFisica(){
		limparObjetos();
		this.editMode = false;
		pessoaPesquisa = new PessoaFisica();
		enableFormPessoaFisica = false;
		disableTabDependenciasRepLegal = true;
		representanteLegalSelecionado = new RepresentanteLegal();
	}

	public void limpaFormulario(ActionEvent event){

		pessoaPesquisa = new PessoaFisica();
		pessoaSelecionada = new PessoaFisica();
		super.pessoa  = new Pessoa();
	
		habilitaCalendarios = false;
		caminhoArquivo = null;
		temArquivo = false;
			
	}

	public void onTabChange(TabChangeEvent event) {
		String activeTab = event.getTab().getId();  
        int activeTabIndex = 0;  
        for (UIComponent comp : event.getTab().getParent().getChildren()) {  
            if (comp.getId().equals(activeTab)) {  
            	break;  
            }  
            activeTabIndex++;  
        }  
        setTabAtual(activeTabIndex);
    }
	
	@Override
	public void enviarId() {
		super.enviarId("tabviewpj:tabviewreplegal");
	}

	@Override
	public void desabilitarParaVisualizacao() {
		super.enderecoGenericoController.setVisualizacao(false);
	}
	
	public void incrementaIndexAba() {
		setTabAtual(this.tabAtual + 1);
		disableTabDependenciasRepLegal = true;
	}

	public void decrementaIndexAba() {
		setTabAtual(this.tabAtual -1);
	}
	
	public void limparObjetos(){
		pessoaPesquisa = new PessoaFisica(); 
		pessoaSelecionada = new PessoaFisica();
		super.pessoa  = new Pessoa();
		temArquivo = false;
		listaArquivo = new ArrayList<String>();
		representanteLegalSelecionado = new RepresentanteLegal();
		setTabAtual(0);
	}
	
	public void carregarRepresentantesLegais() {
		try {
			if (!Util.isNull(pessoaJuridica.getIdePessoaJuridica())) {
				modelRepresentanteLegal = Util.castListToDataModel(representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica)) ;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirRepresentanteLegal() {
		representanteLegalSelecionado.setIndExcluido(true);
		representanteLegalSelecionado.setDtcExclusao(new Date());
		try {
			representanteLegalService.excluirRepresentanteLegal(representanteLegalSelecionado);
			carregarRepresentantesLegais();
			pessoaJuridicaHistoricoService.salvarPessoaJuridicaHistorico(preencherHistoricoPessoaJuridica(false));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void tratarPessoaJuridicaSessao() {
		if (!Util.isNull(ContextoUtil.getContexto().getPessoaJuridica())) {
			pessoaJuridica = ContextoUtil.getContexto().getPessoaJuridica();
			if (Util.isNull(pessoaJuridica)) {
				pessoaJuridica = new PessoaJuridica();
			} 
		}
	}
	
	public String prepararParaEdicao() {
		if(!Util.isNullOuVazio(representanteLegalSelecionado)) {
			Util.updateAttributeSession("representanteLegalSelecionado", representanteLegalSelecionado);
		
			this.pessoaSelecionada = representanteLegalSelecionado.getIdePessoaFisica();
			
			pessoaPesquisa.setNumCpf(pessoaSelecionada.getNumCpf());
			super.pessoa = pessoaSelecionada.getPessoa();
			
			if(!Util.isNullOuVazio(representanteLegalSelecionado.getDscCaminhoRepresentacao())){
				caminhoArquivo = representanteLegalSelecionado.getDscCaminhoRepresentacao();
				listaArquivo.add(FileUploadUtil.getFileName(representanteLegalSelecionado.getDscCaminhoRepresentacao()));
			}
			
			if(Util.isNull(representanteLegalSelecionado.getDtcInicioRepresentacao()) && Util.isNull(representanteLegalSelecionado.getDtcFimRepresentacao())) {
				habilitaCalendarios = true;
			} else {
				habilitaCalendarios = false;
			}
		
		}
		
		editMode = true;
		enableFormPessoaFisica = true;
		disableTabRepLegal = false;
		disableTabDependenciasRepLegal = false;
		temArquivo = true;
		showExpandirFormCadastro = true;
		if(iniciarAbaTelefone) {
			this.tabAtual = 2;
			iniciarAbaTelefone = false;
		}
		Html.atualizar("tabviewpj:paneltabviewreplegal");
		return "";
		
	}
	
	public void trataArquivo(FileUploadEvent event) {
		if (representanteLegalSelecionado == null) {
			representanteLegalSelecionado = new RepresentanteLegal();
		}
		
		caminhoArquivo = FileUploadUtil.Enviar(event,DiretorioArquivoEnum.EMPREENDIMENTO.toString());
		temArquivo = true;
		listaArquivo.clear();
		listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
		representanteLegalSelecionado.setDscCaminhoRepresentacao(caminhoArquivo);
		RequestContext.getCurrentInstance().addPartialUpdateTarget("tabviewpj:tabviewreplegal:formpessoafisicareplegal:arquivoHidden");
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");		
	}
	
	public Boolean existeRepresentanteNaLista(){
		for (RepresentanteLegal representante : modelRepresentanteLegal) {
			if(!Util.isNullOuVazio(pessoaSelecionada) && representante.getIdePessoaFisica().getNumCpf().equals(pessoaSelecionada.getNumCpf()))
			{ 
				return true;
			}
		}
		return false;
	}
		
	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}
	
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}
	
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}
	
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		carregarRepresentantesLegais();
	}
	
	public PessoaFisica getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(PessoaFisica pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	public PessoaFisica getPessoaPesquisa() {
		return pessoaPesquisa;
	}
	
	public Boolean getIsAtend() {
		return ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().intValue() == PerfilEnum.ATENDENTE.getId();
	}

	public void setPessoaPesquisa(PessoaFisica pessoaPesquisa) {
		this.pessoaPesquisa = pessoaPesquisa;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Boolean getEnableFormPessoaFisica() {
		return enableFormPessoaFisica;
	}

	public void setEnableFormPessoaFisica(Boolean enableFormPessoaFisica) {
		this.enableFormPessoaFisica = enableFormPessoaFisica;
	}

	public Boolean getDisableTabRepLegal() {
		return disableTabRepLegal;
	}

	public void setDisableTabRepLegal(Boolean showRepresentanteLegal) {
		this.disableTabRepLegal = showRepresentanteLegal;
	}
	
	public DataModel<RepresentanteLegal> getModelRepresentanteLegal() {
		return this.modelRepresentanteLegal;
	}
	
	public void setModelRepresentanteLegal(DataModel<RepresentanteLegal> modelRepresentanteLegal) {
		this.modelRepresentanteLegal = modelRepresentanteLegal;
	}

	public RepresentanteLegal getRepresentanteLegalSelecionado() {
		if(Util.isNullOuVazio(representanteLegalSelecionado))
			representanteLegalSelecionado = new RepresentanteLegal();
		return representanteLegalSelecionado;
	}

	public void setRepresentanteLegalSelecionado(
			RepresentanteLegal representanteLegalSelecionado) {
		this.representanteLegalSelecionado = representanteLegalSelecionado;
	}

	public Boolean getDisableTabDependenciasRepLegal() {
		return disableTabDependenciasRepLegal;
	}

	public void setDisableTabDependenciasRepLegal(
			Boolean disableTabDependenciasRepLegal) {
		this.disableTabDependenciasRepLegal = disableTabDependenciasRepLegal;
	}
	
	public Boolean getEmptyList() {
		if (Util.isNull(modelRepresentanteLegal) || modelRepresentanteLegal.getRowCount() == 0) {
			emptyList = false;
		} else {
			emptyList = true;
		}
		return emptyList;
	}

	public void setEmptyList(Boolean emptyList) {
		this.emptyList = emptyList;
	}

	public Boolean getVisualizaProximo() {
		if(getTabAtual() < abaMAX) {
			visualizaProximo = true;
		}else {
			visualizaProximo = false;
		}
		return visualizaProximo;
	}

	public void setVisualizaProximo(Boolean visualizaProximo) {
		this.visualizaProximo = visualizaProximo;
	}

	public Boolean getVisualizaAnterior() {
		if(getTabAtual() > 0) {
			visualizaAnterior = true;
		}else {
			visualizaAnterior = false;
		}
		return visualizaAnterior;
	}

	public void setVisualizaAnterior(Boolean visualizaAnterior) {
		this.visualizaAnterior = visualizaAnterior;
	}

	public int getTabAtual() {
		return tabAtual;
	}

	public void setTabAtual(int tabAtual) {
    	if(tabAtual == 3){
    		super.prepararEnderecoGenericoController();
    	}
		this.tabAtual = tabAtual;
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public StreamedContent getArquivoBaixar() {
		if(!listaArquivo.isEmpty()) {			
			try {
				arquivoBaixar = gerenciaArquivoService.getContentFile(representanteLegalSelecionado.getDscCaminhoRepresentacao());
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return arquivoBaixar;
	}

	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}

	public Boolean getTemArquivo() {
		return temArquivo;
	}

	public void setTemArquivo(Boolean temArquivo) {
		this.temArquivo = temArquivo;
	}

	public Boolean getTelaIdentificacaoPapel() {
		return telaIdentificacaoPapel;
	}

	public void setTelaIdentificacaoPapel(Boolean telaIdentificacaoPapel) {
		this.telaIdentificacaoPapel = telaIdentificacaoPapel;
	}

	public Boolean getDisableBotoes() {
		if (telaIdentificacaoPapel) {
			disableBotoes = false;
		}
		return disableBotoes;
	}

	public void setDisableBotoes(Boolean disableBotoes) {
		this.disableBotoes = disableBotoes;
	}

	public Boolean getPermitirAddRepLegal() {
		return permitirAddRepLegal;
	}

	public void setPermitirAddRepLegal(Boolean permitirAddRepLegal) {
		this.permitirAddRepLegal = permitirAddRepLegal;
	}

	public Boolean getPessoaCadastrada() {
		return pessoaCadastrada;
	}

	public void setPessoaCadastrada(Boolean pessoaCadastrada) {
		this.pessoaCadastrada = pessoaCadastrada;
	}

	public Boolean getRepresentanteLegalDaPessoaSelecionada() {
		return representanteLegalDaPessoaSelecionada;
	}

	public void setRepresentanteLegalDaPessoaSelecionada(
			Boolean representanteLegalDaPessoaSelecionada) {
		this.representanteLegalDaPessoaSelecionada = representanteLegalDaPessoaSelecionada;
	}

	public Boolean getShowExpandirFormCadastro() {
		return showExpandirFormCadastro;
	}

	public void setShowExpandirFormCadastro(Boolean showExpandirFormCadastro) {
		this.showExpandirFormCadastro = showExpandirFormCadastro;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public Boolean getHabilitaCalendarios() {
		return habilitaCalendarios;
	}

	public void setHabilitaCalendarios(Boolean habilitaCalendarios) {
		this.habilitaCalendarios = habilitaCalendarios;
	}

	public Boolean getIniciarAbaTelefone() {
		return iniciarAbaTelefone;
	}

	public void setIniciarAbaTelefone(Boolean iniciarAbaTelefone) {
		this.iniciarAbaTelefone = iniciarAbaTelefone;
	}

}