package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoVinculoPCT;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.facade.IdentificarPapelFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("identificarPapelController")
@ViewScoped
public class IdentificarPapelController extends SeiaControllerAb{

	@EJB
	private IdentificarPapelFacade facade;
	
	private static final int PROPRIO_REQUERENTE = 1; 
	private static final int REPRESENTANTE_LEGAL_PJ = 2; 
	private static final int PROCURADOR_PF = 3; 
	private static final int PROCURADOR_PJ = 4;
	private static final int CADASTRANTE_CONVENIO = 5;
	private int indiceTela = 1;
	
	private Integer papelSolicitante;
	private static final Integer PESSOA_SEM_CADASTRO = null;
	private static final Integer SOLICITANTE_NAO_TEM_LIGACAO = -1;
	private static final Integer SOLICITANTE_SEM_PROCURADOR = 0;
	private static final Integer SOLICITANTE_TEM_LIGACAO = 1;
	private static final Integer SOLICITANTE_CADASTRANTE_CONVENIO = 2;
	
	private static final String PESSOA_FISICA = "fisica";
	private static final String PESSOA_JURIDICA = "juridica";
	
	private Pessoa solicitante;
	private PessoaJuridica pessoaJuridica;
	private PessoaFisica pessoaFisica;
	private PessoaFisica pessoaValidar;
	private Pessoa usuarioCadastrante;

	private Boolean disableAbaDependencias;
	private Boolean disableAbaDependenciasPessoaFisica;
	private Boolean isProcuradorPessoaFisica;
	
	private TipoRequerimento tipoRequerimento;
	
	private MetodoUtil metodoSelecionarSolicitante;

	private TipoVinculoPCT tipoVinculoPCT;
	
	private int indiceTelaOld;
	
	private boolean indAceite;
	
	private boolean indPct;
	
	private boolean renderedCadastranteConvenioPCT;

	private List<ContratoConvenio> listaContratoConvenioPct;
	
	private ContratoConvenio contratoConvenioSelecionado;
	
	private String tipoPessoa;
	
	
	@PostConstruct
	public void init() {
		try {
			metodoSelecionarSolicitante = new MetodoUtil(this, this.getClass().getDeclaredMethod("receberSolicitante", Pessoa.class));
			papelSolicitante = null;
			solicitante = new Pessoa();
			tipoPessoa = PESSOA_FISICA;
			
			if(getIsCERH()) {
				if(!isUsuarioLogadoAtendente() && !getIsTecnicoCOGEC()){
					solicitante.setPessoaFisica(getUsuarioLogado().getPessoaFisica());
					solicitante.setIdePessoa(getUsuarioLogado().getIdePessoaFisica());
				}
			} else {
				if(!isUsuarioLogadoAtendente()){
					solicitante.setPessoaFisica(getUsuarioLogado().getPessoaFisica());
					solicitante.setIdePessoa(getUsuarioLogado().getIdePessoaFisica());
				}
			}
			
			selecionarTipoRequerimento();
			retirarEmpreendimentoDaSessao();
			carregarTipoVinculoPCT(false);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
//		renderedCadastranteConvenioPCT = false;
	}

	public void receberSolicitante(Pessoa pessoa){
		this.solicitante = pessoa;
		this.indiceTela = 1;
		this.renderedCadastranteConvenioPCT = false;
		
		if(getIsPCT()) {
			carregarTipoVinculoPCT(true);
		}
		Html.atualizar("filtroSolicitante:panel");
		Html.atualizar("panelidentificarpapel");
	}

	private void selecionarTipoRequerimento() {
		TipoRequerimento tipoReq = ContextoUtil.getContexto().getTipoRequerimento();
		if (!Util.isNull(tipoReq)) {
			if (tipoReq.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde())) {
				tipoRequerimento = new TipoRequerimento(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO);
			}
			else if (tipoReq.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getIde())) {
				tipoRequerimento = tipoReq;
			}
		}
	}
	
	public void changeRadioShowPessoaJuridica(ValueChangeEvent event) {
		disableAbaDependencias = true;
		renderedCadastranteConvenioPCT = false;
		if((Integer) event.getOldValue() == PROCURADOR_PJ || (Integer) event.getOldValue() == REPRESENTANTE_LEGAL_PJ){
			limparPessoaJuridica();
		} 
		else if((Integer) event.getOldValue() == PROCURADOR_PF) {
			limparPessoaFisica();
		} 
		else {
			limparPessoaFisica();
			limparPessoaJuridica();
		}

		if(getIsPCT() && (Integer) event.getNewValue() == CADASTRANTE_CONVENIO){
			
			renderedCadastranteConvenioPCT = existeCadastranteConvenioPCT();
			
			if(renderedCadastranteConvenioPCT){
//				listaContratoConvenioPct = listarContratoConvenioPct();
				this.listaContratoConvenioPct = getListarContratoConvenioPct();
//				this.contratoConvenioSelecionado = new ContratoConvenio();
			}
			
			if(!renderedCadastranteConvenioPCT){
				JsfUtil.addWarnMessage("Você não está vinculado a uma empressa conveniada para cadastro de território de povos e comunidades tradicionais!");
			}
		}
	}
	
	public void verificarPessoaValidar() {
		if(!isUsuarioLogadoAtendente()){
			pessoaValidar = getUsuarioLogado().getPessoaFisica();			
		} 
		else if (getIsCERH() && !getIsTecnicoCOGEC()) {
			pessoaValidar = getUsuarioLogado().getPessoaFisica();	
		}
		else {
			
			if(PESSOA_FISICA.equals(tipoPessoa)) {
				pessoaValidar =  solicitante.getPessoaFisica();
			}
		}
		if(isSolicitanteProprioRequerente()) {
			pessoaFisica = pessoaValidar;
			ContextoUtil.getContexto().setSolicitanteRequerimento(solicitante);
		}
		
		if(isSolicitanteCadastranteConvenio()){
			
			if(PESSOA_FISICA.equals(tipoPessoa)) {
				pessoaValidar = solicitante.getPessoaFisica();
				ContextoUtil.getContexto().setSolicitanteRequerimento(solicitante);
			}
			Html.atualizar("paneltabviewpf");
		}
	}
	
	public void verificarLigacaoSolicitanteComRequerente(){
		disableAbaDependencias = true;
		verificarPessoaValidar();
		try {
			papelSolicitante = getPapelSolicitante();
		
			if(papelSolicitante == SOLICITANTE_TEM_LIGACAO){
				disableAbaDependencias = false;
				if(isSolicitanteProcuradorPessoaFisica()){
					isProcuradorPessoaFisica = true;
				}
				disableAbaDependencias = false;
				ContextoUtil.getContexto().setIsProcPfPjOuRepLegal(true);
			} 
			else if(papelSolicitante != PESSOA_SEM_CADASTRO){
				if(papelSolicitante == SOLICITANTE_NAO_TEM_LIGACAO || papelSolicitante == SOLICITANTE_SEM_PROCURADOR || papelSolicitante == SOLICITANTE_CADASTRANTE_CONVENIO){
					if(isSolicitanteRepresentanteLegal() || isSolicitanteProcuradorPessoaJuridica()){
						limparPessoaJuridica();
					} 
					else if(isSolicitanteProcuradorPessoaFisica()){
						isProcuradorPessoaFisica = false;
						disableAbaDependencias = true;
						limparPessoaFisica();
					}else if(isSolicitanteCadastranteConvenio()){
						isProcuradorPessoaFisica = false;
						disableAbaDependencias = false;
					}
				}
				ContextoUtil.getContexto().setIsProcPfPjOuRepLegal(false);
			}
			
			if(!Util.isNull(RequestContext.getCurrentInstance())){
				Html.atualizar(":formavancar:buttonAvancar");
				Html.atualizar(":formavancar:buttonAvancarPct");
				Html.atualizar("formavancar");
			}

			if(!Util.isNullOuVazio(getWarnMessage())){
				JsfUtil.addWarnMessage(getWarnMessage());
			}
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private String getWarnMessage(){
		if(papelSolicitante == SOLICITANTE_NAO_TEM_LIGACAO || papelSolicitante == SOLICITANTE_SEM_PROCURADOR){
			String tipoPessoa = "";
			String soliciteCadastro = "";
			if(isSolicitanteProcuradorPessoaFisica()){
				tipoPessoa = Util.getString("msg_generica_pf_registrada");
				soliciteCadastro = Util.getString("msg_generica_solicite_pessoa_cadastro_procurador");
			} 
			else {
				tipoPessoa = Util.getString("msg_generica_pj_registrada");
				if(isSolicitanteRepresentanteLegal()){
					soliciteCadastro = Util.getString("msg_generica_solicite_pessoa_cadastro_representante");
				} 
				else {
					soliciteCadastro = Util.getString("msg_generica_solicite_pessoa_cadastro_procurador");
				}
			}
			return tipoPessoa + soliciteCadastro;
		} 
		else if(papelSolicitante == PESSOA_SEM_CADASTRO){
			if(isSolicitanteProcuradorPessoaFisica()){
				return "Pessoa Física informada não está cadastrada na base do SEIA. Preencha os dados das demais abas antes de clicar no botão 'Avançar'.";
			}else if(isSolicitanteCadastranteConvenio()){
				return "Pessoa Representante da comunidade informada não está cadastrada na base do SEIA. Preencha os dados das demais abas antes de clicar no botão 'Avançar'.";
			}
			else {
				return "Pessoa Jurídica informada não está cadastrada na base do SEIA. Preencha os dados das demais abas antes de clicar no botão 'Avançar'.";
			}
		}
		return "";
	}
	
	private Integer getPapelSolicitante() throws Exception{
		Integer papel = null;
		if(isSolicitanteProcuradorPessoaJuridica()){
			papel = facade.retornarLigacaoProcuradorPJ(pessoaValidar, new ProcuradorRepresentante(pessoaJuridica));
		} 
		else if(isSolicitanteRepresentanteLegal()){
			papel = facade.retornarLigacaoRepresentanteLegal(pessoaValidar, pessoaJuridica);
		}
		else if(isSolicitanteProcuradorPessoaFisica()){
			papel = facade.retornarLigacaoProcuradorPF(pessoaValidar, new ProcuradorPessoaFisica(pessoaFisica));
		}else if(isSolicitanteCadastranteConvenio()){
			
			if(PESSOA_FISICA.equals(tipoPessoa)) {
				solicitante = pessoaFisica.getPessoa();
			}else if(PESSOA_JURIDICA.equals(tipoPessoa)){
				solicitante = pessoaJuridica.getPessoa();
			}
			
			if(!Util.isNullOuVazio(solicitante)){
				papel =  SOLICITANTE_CADASTRANTE_CONVENIO;
			}
		}
		return papel;
	}
	
	
	public void limpar(){
		limparPessoaFisica();
		limparPessoaJuridica();
		disableAbaDependencias = true;
		disableAbaDependenciasPessoaFisica = true;
	}

	private void limparPessoaJuridica() {
		pessoaJuridica = null;
		limparPessoaJuridicaController();
	}

	private void limparPessoaJuridicaController() {
		PessoaJuridicaController pjController = (PessoaJuridicaController) SessaoUtil.recuperarManagedBean("#{pessoaJuridicaController}", PessoaJuridicaController.class);
		pjController.setDisableForm(true);
		pjController.limparObjetos();
	}

	private void limparPessoaFisica(){
		pessoaFisica = null;
		limparPessoaFisicaController();
	}
	
	private void limparPessoaFisicaController() {
		PessoaFisicaController pfc = (PessoaFisicaController) SessaoUtil.recuperarManagedBean("#{pessoaFisicaController}", PessoaFisicaController.class);
		pfc.setDisableFormPessoaFisica(true);
		pfc.setEnableFormPessoaFisica(false);
		pfc.limparObjetos();
		pfc.setPessoaPesquisa(new PessoaFisica());
	}
	
	public String avancar() {
		if(validarUsuarioLogado() && validarSolicitacao()){
			String url = "";
			addAtributoSessao("SOLICITANTE_REQ_IMOVEL_RURAL", solicitante);
			ContextoUtil.getContexto().setTipoSolicitante(indiceTela);
			if(isRequerimentoDeRegulacaoAmbiental()) {
				url = "/paginas/identificar-tipo-solicitacao/identificar-tipo-solicitacao.xhtml";
			} 
			else if (isRequerimentoDeCadastroDeImovel()) {
				url = "/paginas/manter-imoveis/cefir/cadastroImovelRural.xhtml";
			}
			else if (!isTipoRequerimentoNotNull()) {
				if(!Util.isNull(ContextoUtil.getContexto().getTelaParaRedirecionar())) {
					
					cadastroCerh();
					
					url = ContextoUtil.getContexto().getTelaParaRedirecionar().getUrl();
					ContextoUtil.getContexto().setTelaParaRedirecionar(null);
				} 
				else {
					url = PaginaEnum.IDENTIFICACAO_ATIVIDADE_SEM_LICENCIAMENTO.getUrl();
				}
			}
			return url + "?faces-redirect=true";
		} 
		return "";
	}

	public List<TipoVinculoPCT> getListarTipoVinculoPCT(){
		
		List<TipoVinculoPCT> tipoVinculoPCTs = null;
		
		try {
			tipoVinculoPCTs = facade.listarTipoVinculoPCT();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tipoVinculoPCTs;
	}

	private void cadastroCerh() {
		if(getIsCERH()){
			if(ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().getPessoa()== null){
				if(!isUsuarioLogadoAtendente() && !getIsTecnicoCOGEC()){
					ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setPessoa(new Pessoa(getUsuarioLogado().getIdePessoaFisica()));
				} 
				else {
					if(!Util.isNullOuVazio(solicitante.getPessoaFisica())) {
						ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setPessoa(new Pessoa(solicitante.getPessoaFisica().getIdePessoaFisica()));
					}else {
						ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setPessoa(new Pessoa(solicitante.getPessoaJuridica().getIdePessoaJuridica()));
					}
				}
			}
		}
	}
	
	private boolean isTelaDestinoCerh(){
		return !Util.isNull(ContextoUtil.getContexto().getTelaParaRedirecionar()) 
				&& ContextoUtil.getContexto().getTelaParaRedirecionar().getUrl().equals(PaginaEnum.CADASTRO_CERH.getUrl());
	}
	
	private boolean isUsuarioSemValidacao(){
		try {
			return !ContextoUtil.getContexto().isUsuarioExterno() || facade.isUsuarioConsorcioCerh();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}  
	}
	
	public boolean isNecessitaValidacao(){
		return !(isUsuarioSemValidacao() && isTelaDestinoCerh());
	}
	
	private boolean isRequerimentoDeRegulacaoAmbiental() {
		return isTipoRequerimentoNotNull() && tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde());
	}
	
	private boolean isRequerimentoDeCadastroDeImovel(){
		return isTipoRequerimentoNotNull() && tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getIde());
	}
	
	/**
	 * Quando for NULL quer dizer que veio do CADASTRO DE ATIVIDADES SEM LICENCIAMENTO.
	 * @return
	 */
	public boolean isTipoRequerimentoNotNull() {
		return !Util.isNull(tipoRequerimento);
	}

	private boolean validarUsuarioLogado() {
		if(isUsuarioLogadoAtendente()){
			return validarUsuarioInterno();
		} 
		else {
			if(getIsCERH() && getIsTecnicoCOGEC()) {
				return validarUsuarioInterno();
			}
			return validarUsuarioExterno();
		}
	}

	/**
	 * Método que valida se o SOLICITANTE é vinculado como Procurador ou Representante Legal do PJ/PF que está tentando abrir o Requerimento.
	 * @return
	 */
	private boolean validarSolicitacao(){
		try {
			if(!isSolicitanteProprioRequerente()){
				String tipoPessoa = Util.getString("msg_generica_pj_registrada");
				String soliciteCadastro = Util.getString("msg_generica_solicite_pessoa_cadastro_procurador");
				if(isSolicitanteProcuradorPessoaJuridica()){
					papelSolicitante = facade.retornarLigacaoProcuradorPJ(pessoaValidar, new ProcuradorRepresentante(pessoaJuridica));
				} 
				else if(isSolicitanteRepresentanteLegal()){
					papelSolicitante = facade.retornarLigacaoRepresentanteLegal(pessoaValidar, pessoaJuridica);
					soliciteCadastro = Util.getString("msg_generica_solicite_pessoa_cadastro_representante"); 
				}
				else if(isSolicitanteProcuradorPessoaFisica()){
					papelSolicitante = facade.retornarLigacaoProcuradorPF(pessoaValidar, new ProcuradorPessoaFisica(pessoaFisica));
					tipoPessoa = Util.getString("msg_generica_pf_registrada");
				}else if(isSolicitanteCadastranteConvenio()){
					
					if(Util.isNullOuVazio(tipoVinculoPCT)){
						JsfUtil.addWarnMessage("O campo Vinculo do cadastrante com a comunidade / território é de preenchimento obrigatório!");
						
						return false;
					}
					
					if(!Util.isNullOuVazio(tipoVinculoPCT) && "Outros".equals(tipoVinculoPCT.getDscTipoVinculoPCT()) && Util.isNullOuVazio(tipoVinculoPCT.getDscTipoVinculoPCTOutros())) {
						JsfUtil.addWarnMessage("O campo Descriçao do vinculo do cadastrante com a comunidade / território é de preenchimento obrigatório!");
						
						return false;
					}
					
					if (Util.isNullOuVazio(contratoConvenioSelecionado)) {
						JsfUtil.addWarnMessage("O campo Escolha o convênio é de preenchimento obrigatório!");

						return false;
					}
					
					if(!indAceite){
						JsfUtil.addWarnMessage("O campo Declaração é de preenchimento obrigatório!");
						
						return false;
					}
					
					return true;
				}
				if(papelSolicitante != SOLICITANTE_TEM_LIGACAO){
					JsfUtil.addWarnMessage(tipoPessoa + soliciteCadastro);
					return false;
				} 
				else {
					ContextoUtil.getContexto().setIsProcPfPjOuRepLegal(true);					
				}
			}
			return true;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}
	
	private boolean validarUsuarioInterno() {
		ContextoUtil.getContexto().getReqPapeisDTO().getAtendente().setPessoa(solicitante);
		ContextoUtil.getContexto().getReqPapeisDTO().getAtendente().setIndUsuarioLogado(true);
		ContextoUtil.getContexto().getReqPapeisDTO().getAtendente().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.ATENDENTE.getId()));
		ContextoUtil.getContexto().setSolicitanteRequerimento(solicitante);
		if(isSolicitanteProprioRequerente()) {
			ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIndSolicitante(true);
			ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(solicitante);
			ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
			try {

				if(solicitante.getPessoaFisica()!=null){
					if(!facade.isCadastroPFvalido(solicitante.getPessoaFisica(), isNecessitaValidacao())){
						MensagemUtil.erro("Seus dados de Cadastro não estão completos. Favor retornar ao Cadastro de Pessoa Física e preencher as informações necessárias");
						return false;
					}
				}
				else if(solicitante.getPessoaJuridica()!=null){
					boolean isSilos = false;
					if(!Util.isNullOuVazio(Util.getAttributeSession("isSilos"))) {
						isSilos = (Boolean) Util.getAttributeSession("isSilos");
					}
					if(isSilos){
						if(!facade.isCadastroPJvalidoSemCnae(solicitante.getPessoaJuridica())){
							MensagemUtil.erro("Seus dados de Cadastro não estão completos. Favor retornar ao Cadastro de Pessoa Física e preencher as informações necessárias");
							return false;
						}
					}else{
						if(!facade.isCadastroPJvalido(solicitante.getPessoaJuridica(), isNecessitaValidacao())){
							MensagemUtil.erro("Seus dados de Cadastro não estão completos. Favor retornar ao Cadastro de Pessoa Física e preencher as informações necessárias");
							return false;
						}
					}
				}
				return true;
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				return false;
			}
		} 
		else {
			ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setIndSolicitante(true);
			ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setPessoa(solicitante);
			if(isSolicitanteProcuradorPessoaFisica() && validarPF()) {
				montarDTOparaProcuradorPF();
				return true;
			}else if(isSolicitanteCadastranteConvenio() && validarTipoPessoa()){
				montarDTOparaCadastranteConvenio();
				return true;
			}
			else {
				if(!getIsPCT() && validarPJ()) {
					montarDTOparaPJ();
					return true;
				} 
			}
		}
		return false;
	}
	
	private boolean validarUsuarioExterno() {
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(solicitante);
		ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setPessoa(solicitante);
			
		
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIndUsuarioLogado(true);
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIndSolicitante(true);
		ContextoUtil.getContexto().setSolicitanteRequerimento(solicitante);

		if(isSolicitanteProprioRequerente()) {
			if(Util.isNullOuVazio(pessoaFisica)) {
				pessoaFisica = solicitante.getPessoaFisica();
			}
			if(validarPFProprioRequerente()) {
				ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
				addAtributoSessao("REQUERENTE",solicitante);
				return true;
			}
		} 
		else {
			if(isSolicitanteProcuradorPessoaFisica() && validarPF()) {
				montarDTOparaProcuradorPF();
				addAtributoSessao("REQUERENTE",pessoaFisica.getPessoa());
				return true;
			}else if(isSolicitanteCadastranteConvenio()){
				
				if(PESSOA_FISICA.equals(tipoPessoa)) {
					if(validarPFRepresentanteComunidade()){
						montarDTOparaCadastranteConvenio();
						addAtributoSessao("REQUERENTE",pessoaFisica.getPessoa());
						return true;
						
					}else{
						return false;
					}
				}else if(PESSOA_JURIDICA.equals(tipoPessoa)) {
					
					if(validarPJ()) {
						montarDTOparaCadastranteConvenio();
						addAtributoSessao("REQUERENTE",pessoaJuridica.getPessoa());
						return true;
					}else {
						return false;
					}
				}
				
			}else {
				if(validarPJ()){
					addAtributoSessao("REQUERENTE",pessoaJuridica.getPessoa());
					montarDTOparaPJ();
					return true;
				}
			}
				
		}
		return false;
	}
	
	private void  montarDTOparaCadastranteConvenio(){
		ContextoUtil.getContexto().setTipoVinculoPCT(tipoVinculoPCT);
		if(!Util.isNullOuVazio(tipoVinculoPCT)){
			ContextoUtil.getContexto().getTipoVinculoPCT().setIndAceite(indAceite);
		}
		
		ContextoUtil.getContexto().setContratoConvenio(contratoConvenioSelecionado);
		ContextoUtil.getContexto().setSolicitanteRequerimento(getUsuarioLogado().getPessoaFisica().getPessoa());
		
		if(PESSOA_FISICA.equals(tipoPessoa)) {
			ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(pessoaFisica.getPessoa());
		}else if(PESSOA_JURIDICA.equals(tipoPessoa)){
			ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(pessoaJuridica.getPessoa());
		}
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
		ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.CONVENIADO.getId()));
	}

	private void montarDTOparaProcuradorPF() {
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(pessoaFisica.getPessoa());
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
		ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId()));
	}

	private void montarDTOparaPJ(){
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(pessoaJuridica.getPessoa());
		ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
		if(isSolicitanteRepresentanteLegal()) {
			ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId()));
		}
		if(isSolicitanteProcuradorPessoaJuridica()) {
			ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().setIdeTipoPessoaRequerimento(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId()));
		}
	}
	
	public boolean validarPJ() {
		try {
			if(pessoaJuridicaNotNull() && !facade.isCadastroPJvalido(pessoaJuridica, isNecessitaValidacao())) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_dados_pj_incompleto"));
				return false;
			} 
			else if(!pessoaJuridicaNotNull()){
				JsfUtil.addWarnMessage("Informe a Pessoa Jurídica.");
				return false;
			}
			return true;
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e ,Util.SEIA_EXCEPTION); 
		}
	}

	private boolean pessoaJuridicaNotNull() {
		return !Util.isNull(pessoaJuridica) && !Util.isNull(pessoaJuridica.getIdePessoaJuridica());
	}

	private boolean validarPF() {
		try {
			if(pessoaFisicaNotNull() && !facade.isCadastroPFvalido(pessoaFisica, isNecessitaValidacao())) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_dados_pf_incompleto"));
				return false;
			} 
			else if(!pessoaFisicaNotNull()){
				JsfUtil.addWarnMessage("Informe a Pessoa Física.");
				return false;
			}
			return true;
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e ,Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean validarPFRepresentanteComunidade() {
		try {
			if(pessoaFisicaNotNull() && !facade.isCadastroPFvalidoRepresentanteComunidade(pessoaFisica, isNecessitaValidacao())) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_dados_pf_incompleto"));
				return false;
			} 
			else if(!pessoaFisicaNotNull()){
				JsfUtil.addWarnMessage("Informe a Pessoa Física.");
				return false;
			}
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw Util.capturarException(e ,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarTipoVinculoPCT(boolean pctValido){
		String isPct = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pct");
		if(new Boolean(isPct) || pctValido){
			this.indiceTela = 5;
			this.indiceTelaOld = 5;
			this.disableAbaDependencias = true;
			
			if(this.indiceTela == CADASTRANTE_CONVENIO){
				
				renderedCadastranteConvenioPCT = existeCadastranteConvenioPCT();
				
				if(renderedCadastranteConvenioPCT){
//					listaContratoConvenioPct = listarContratoConvenioPct();
					this.listaContratoConvenioPct = getListarContratoConvenioPct();
//					this.contratoConvenioSelecionado = new ContratoConvenio();
				}
				
				if(!renderedCadastranteConvenioPCT && !Util.isNullOuVazio(solicitante)){
					JsfUtil.addWarnMessage("Você não está vinculado a uma empressa conveniada para cadastro de território de povos e comunidades tradicionais!");
				}
			}
		}
	}
	
	private boolean validarTipoPessoa() {
		
		boolean retorno = false;
		if(PESSOA_FISICA.equals(tipoPessoa)) {
			retorno = validarPF();
		}else if(PESSOA_JURIDICA.equals(tipoPessoa)) {
			retorno = validarPJ();
		}
		
		return retorno;
	}
	
	/**
	 * Depreciei, pois o método estava vazio
	 * @deprecated
	 */
	public void atualizarSolicitante(){
	}

	private boolean pessoaFisicaNotNull() {
		return !Util.isNull(pessoaFisica) && !Util.isNull(pessoaFisica.getIdePessoaFisica());
	}
	
	private boolean validarPFProprioRequerente() {
		try {
			if(pessoaFisicaNotNull() && !facade.isCadastroPFvalido(pessoaFisica, false)) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_dados_pf_incompleto"));
				return false;
			} 
			else if(!pessoaFisicaNotNull()){
				JsfUtil.addWarnMessage("Informe a Pessoa Física.");
				return false;
			}
			return true;
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e ,Util.SEIA_EXCEPTION);
		}
	}	
	
	public void limparCampoDescricaoTerritorioOutros(){
		
		if(!Util.isNullOuVazio(tipoVinculoPCT) && !Util.isNullOuVazio(tipoVinculoPCT.getIdeTipoVinculoPCT())){
			
			if(!Util.isNullOuVazio(this.tipoVinculoPCT.getDscTipoVinculoPCTOutros()) && 
					!"Outros".equals(this.tipoVinculoPCT.getDscTipoVinculoPCTOutros())){
				
				this.tipoVinculoPCT.setDscTipoVinculoPCTOutros(null);
			}
		}
	}
	
	/**
	 * Método criado devido a remoção do <b>actionListener="#{empreendimentoController.limparTela()}</b> no <i>menu.xhtml</i>.
	 * Do jeito que estava implementado ao clicar em "Solicitar regularização ambiental do empreendimento" a aplicação carregava o init() de EmpreendimentoController e depois executava o limparTela(),
	 * para evitar esse desperdício de recurso e não ocasionar impacto em outras telas, mantive a remoção do empreendimento da sessão.
	 * @author eduardo.fernandes
	 * @since 02/06/2016
	 */
	private void retirarEmpreendimentoDaSessao(){
		addAtributoSessao("EMPREENDIMENTO_SESSAO", null);
	}

	private boolean isSolicitanteNotNull() {
		return !Util.isNull(solicitante);
	}
	
	public Boolean getShowIdentificarPapel() {
		return isSolicitanteNotNull() && !Util.isNull(solicitante.getIdePessoa());
	}
	
	public boolean isSolicitantePessoaFisisca(){
		return isSolicitanteNotNull() && !Util.isNull(solicitante.getPessoaFisica());
	}
	
	public boolean isSolicitantePessoaJuridica(){
		return isSolicitanteNotNull() && !Util.isNull(solicitante.getPessoaJuridica());
	}
	
	public boolean isSolicitanteProprioRequerente() {
		return getIndiceTela() == PROPRIO_REQUERENTE;
	}

	public boolean isSolicitanteProcuradorPessoaJuridica() {
		return getIndiceTela() == PROCURADOR_PJ;
	}

	public boolean isSolicitanteProcuradorPessoaFisica() {
		return getIndiceTela() == PROCURADOR_PF;
	}

	public boolean isSolicitanteRepresentanteLegal() {
		return getIndiceTela() == REPRESENTANTE_LEGAL_PJ;
	}
	
	public boolean isSolicitanteCadastranteConvenio() {
		return getIndiceTela() == CADASTRANTE_CONVENIO;
	}
	
	public Boolean getShowPessoaJuridica() {
		return isSolicitanteRepresentanteLegal();
	}

	public Boolean getShowProprioRequerente() {
		return isSolicitanteProprioRequerente();
	}

	public Boolean getShowProcuradorPessoaFisica() {
		return isSolicitanteProcuradorPessoaFisica();
	}

	public Boolean getShowProcuradorPessoaJuridica() {
		return isSolicitanteProcuradorPessoaJuridica();
	}
	
	public Boolean getShowCadastranteConvenio() {
		return isSolicitanteCadastranteConvenio();
	}

	public Boolean getShowSolicitante() {
		if(getIsCERH()) {
			return isUsuarioLogadoAtendente() || getIsTecnicoConveniado() || getIsTecnicoCOGEC();
		}
		return isUsuarioLogadoAtendente() || getIsTecnicoConveniado();
	}

	private Boolean isUsuarioLogadoAtendente() {
		return getUsuarioLogado().isAtende();
	}
	
	private Boolean getIsTecnicoCOGEC() {
		return getUsuarioLogado().isTecnicoCOGEC();
	}
	
	private Boolean getIsCERH() {
		if(!Util.isNullOuVazio(ContextoUtil.getContexto().getTelaParaRedirecionar())) {
			return ContextoUtil.getContexto().getTelaParaRedirecionar().equals(PaginaEnum.CADASTRO_CERH);
		}
		return false;
	}

	private Usuario getUsuarioLogado() {
		return ContextoUtil.getContexto().getUsuarioLogado();
	}
	
	public Boolean getIsTecnicoConveniado() {
		return getUsuarioLogado().isTecnicoConveniado();
	}
	
	public Boolean getIsPCT(){
		return ContextoUtil.getContexto().isPCT();
	}

	public String getNomeSolicitante(){
		if(isSolicitanteNotNull()){
			if(isSolicitantePessoaFisisca()){
				return solicitante.getPessoaFisica().getNomPessoa();
			} 
			else if(isSolicitantePessoaJuridica()){
				return solicitante.getPessoaJuridica().getNomRazaoSocial();
			}
		}
		return "";
	}

	/**
	 * @see <a href="http://10.105.17.77/redmine/issues/8943">#8943</a>
	 * @return [Label para ser exibido na tela]
	 */
	public String getLabelSolicitante(){
		// CERH
		if(!Util.isNull(ContextoUtil.getContexto().getTelaParaRedirecionar()) 
				&& !isRequerimentoDeCadastroDeImovel() 
				&& !isRequerimentoDeRegulacaoAmbiental()) {
			return "Usuário";
		} 
		else if(isRequerimentoDeCadastroDeImovel() || isRequerimentoDeRegulacaoAmbiental()){
			return "Requerente";
		}
		return "Solicitante";
	}
	
	public String getHintPR(){
		if(isRequerimentoDeRegulacaoAmbiental()){
			return Util.getString("identificar_papel_hint_pr");
		} else if(isRequerimentoDeCadastroDeImovel()){
			return Util.getString("cefir_hint_papel_propio");
		}
		return "Selecione essa opção caso queira que o cadastro seja emitido em nome da pessoa física logada no sistema";
	}
	
	public String getHintRLPJ(){
		if(isRequerimentoDeRegulacaoAmbiental()){
			return Util.getString("identificar_papel_hint_rlpj");
		} else if(isRequerimentoDeCadastroDeImovel()){
			return Util.getString("cefir_hint_papel_representante_legal");
		}
		return "Selecione essa opção caso você seja um representante legal de uma referida empresa e queira que o cadastro seja emitido em nome deste CNPJ";
	}
	
	public String getHintPPF(){
		if(isRequerimentoDeRegulacaoAmbiental()){
			return Util.getString("identificar_papel_hint_ppf");
		} else if(isRequerimentoDeCadastroDeImovel()){
			return Util.getString("cefir_hint_papel_procurador_pf");
		}
		return "Selecione essa opção caso você seja um procurador de pessoa física e queira que o cadastro seja emitido em nome desta pessoa física";
	}
	
	public String getHintPPJ(){
		if(isRequerimentoDeRegulacaoAmbiental()){
			return Util.getString("identificar_papel_hint_ppj");
		} else if(isRequerimentoDeCadastroDeImovel()){
			return Util.getString("cefir_hint_papel_procurador_pj");
		}
		return "Selecione essa opção caso você seja um procurador de um CNPJ e queira que o cadastro seja emitido em nome do referido CNPJ";
	}
	
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public Boolean getDisableAbaDependencias() {
		return disableAbaDependencias;
	}
	
	public void definiDisableAbaDependenciasFalse() {
		disableAbaDependencias = false;
	}

	public void setDisableAbaDependencias(Boolean disableAbaDependencias) {
		this.disableAbaDependencias = disableAbaDependencias;
	}

	public Boolean getIsProcuradorPessoaFisica() {
		return isProcuradorPessoaFisica;
	}

	public void setIsProcuradorPessoaFisica(Boolean isProcuradorPessoaFisica) {
		this.isProcuradorPessoaFisica = isProcuradorPessoaFisica;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public Boolean getDisableAbaDependenciasPessoaFisica() {
		return disableAbaDependenciasPessoaFisica;
	}

	public void setDisableAbaDependenciasPessoaFisica(Boolean disableAbaDependenciasPessoaFisica) {
		this.disableAbaDependenciasPessoaFisica = disableAbaDependenciasPessoaFisica;
	}

	public PessoaFisica getPessoaValidar() {
		return pessoaValidar;
	}

	public void setPessoaValidar(PessoaFisica pessoaValidar) {
		this.pessoaValidar = pessoaValidar;
	}

	public Pessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}
	
	public TipoRequerimento getTipoRequerimento() {
		return tipoRequerimento;
	}

	public void setTipoRequerimento(TipoRequerimento tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public List<SelectItem> getListaRadios() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		
		if(getIsPCT()){
			itens.add(new SelectItem(1, "O Próprio " + getLabelSolicitante()));
			itens.add(new SelectItem(2, "Representante Legal de Pessoa Jurídica"));
			itens.add(new SelectItem(3, "Procurador de Pessoa Física"));
			itens.add(new SelectItem(4, "Procurador de Pessoa Jurídica"));
			itens.add(new SelectItem(5, "Cadastrante Convênio"));
		}else{
			itens.add(new SelectItem(1, "O Próprio " + getLabelSolicitante()));
			itens.add(new SelectItem(2, "Representante Legal de Pessoa Jurídica"));
			itens.add(new SelectItem(3, "Procurador de Pessoa Física"));
			itens.add(new SelectItem(4, "Procurador de Pessoa Jurídica"));
		}
		
		return itens;
	}
	
	public List<TipoRequerimento> getListaTiposRequerimento() {
		List<TipoRequerimento> lista = new ArrayList<TipoRequerimento>();
        for (int j = 0; j < TipoRequerimentoEnum.values().length; j++) {
        	lista.add( new TipoRequerimento(TipoRequerimentoEnum.values()[j].getIde(), TipoRequerimentoEnum.values()[j].getNomeTipoRequerimento() ) );
        }
        return lista;
	}
	
	public boolean existeCadastranteConvenioPCT(){
		
		boolean retorno = false;
		
		try {
			if(!Util.isNullOuVazio(solicitante.getPessoaFisica())){
				
					retorno = facade.isPessoaComContratoConvenioPCT(solicitante.getPessoaFisica().getIdePessoaFisica());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public List<ContratoConvenio> getListarContratoConvenioPct(){
		
		List<ContratoConvenio> lista = null;
		try {
			lista = facade.listarContratoConvencioPctByPessoaFisica(solicitante.getPessoaFisica().getIdePessoaFisica());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Falha no carregamento");
		}
		
		return lista;
	}
	
	private int getIndiceTela() {
		return indiceTela;
	}

	public void setIndiceTela(int indiceTela) {
		this.indiceTela = indiceTela;
	}

	public MetodoUtil getMetodoSelecionarSolicitante() {
		return metodoSelecionarSolicitante;
	}

	public void setMetodoSelecionarSolicitante(MetodoUtil metodoSelecionarSolicitante) {
		this.metodoSelecionarSolicitante = metodoSelecionarSolicitante;
	}
	
	public TipoVinculoPCT getTipoVinculoPCT() {
		return tipoVinculoPCT;
	}

	public void setTipoVinculoPCT(TipoVinculoPCT tipoVinculoPCT) {
		this.tipoVinculoPCT = tipoVinculoPCT;
	}

	public Pessoa getUsuarioCadastrante() {
		return usuarioCadastrante;
	}

	public void setUsuarioCadastrante(Pessoa usuarioCadastrante) {
		this.usuarioCadastrante = usuarioCadastrante;
	}
	
	public int getIndiceTelaOld() {
		return indiceTelaOld;
	}

	public void setIndiceTelaOld(int indiceTelaOld) {
		this.indiceTelaOld = indiceTelaOld;
	}

	public boolean isIndAceite() {
		return indAceite;
	}

	public void setIndAceite(boolean indAceite) {
		this.indAceite = indAceite;
	}

	public boolean isIndPct() {
		return indPct;
	}

	public void setIndPct(boolean indPct) {
		this.indPct = indPct;
	}

	public boolean isRenderedCadastranteConvenioPCT() {
		return renderedCadastranteConvenioPCT;
	}

	public void setRenderedCadastranteConvenioPCT(
			boolean renderedCadastranteConvenioPCT) {
		this.renderedCadastranteConvenioPCT = renderedCadastranteConvenioPCT;
	}

	public List<ContratoConvenio> getListaContratoConvenioPct() {
		return listaContratoConvenioPct;
	}

	public void setListaContratoConvenioPct(
			List<ContratoConvenio> listaContratoConvenioPct) {
		this.listaContratoConvenioPct = listaContratoConvenioPct;
	}

	public ContratoConvenio getContratoConvenioSelecionado() {
		return contratoConvenioSelecionado;
	}

	public void setContratoConvenioSelecionado(
			ContratoConvenio contratoConvenioSelecionado) {
		this.contratoConvenioSelecionado = contratoConvenioSelecionado;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
}