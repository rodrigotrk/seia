package br.gov.ba.seia.controller;

import static br.gov.ba.seia.util.Html.atualizar;
import static br.gov.ba.seia.util.Html.exibir;
import static br.gov.ba.seia.util.Html.redirecionarPagina;
import static br.gov.ba.seia.util.Html.telaInvalida;
import static br.gov.ba.seia.util.Html.telaValida;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirInformacao001;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirInformacao041;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirInformacao066;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirInformacao067;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirInformacao068;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirInformacao070;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem001;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem002;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem003;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem005;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem013;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem015;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.FormacaoProfissional;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoProspeccao;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineralDocumentoCaptacao;
import br.gov.ba.seia.entity.PesquisaMineralResponsavelTecnico;
import br.gov.ba.seia.entity.PesquisaMineralSubstanciaMineral;
import br.gov.ba.seia.entity.PesquisaMineralUsoDaAgua;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProcessoDnpmProspecao;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.ProspecaoDetalhamento;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.SubstanciaMineral;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.CnaeEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TelaDestinoEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.PesquisaMineralFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * @author eduardo.fernandes
 * @since 31/10/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Named("pesquisaMineralController")
@ViewScoped
public class PesquisaMineralController {

	@EJB
	private PesquisaMineralFacade facade;

	private static final Integer ABA_DADOS_BASICOS = 0;
	private static final Integer ABA_CARACTERIZACAO_ATIVIDADES = 1;
	private static final Integer ABA_DOCUMENTOS_ESTUDOS = 2;
	
	private static final int REPRESENTANTE_LEGAL_PJ = 2;
	private static final int PROCURADOR_PJ = 4;

	private int activeTab;
	private boolean aceite;
	private boolean closeDialogAceite;
	private boolean substanciaMineralOutros;
	private boolean comprometimento;

	private PessoaJuridica requerente;
	private Endereco enderecoCorrespondencia;
	private PesquisaMineral pesquisaMineral;
	private CadastroAtividadeNaoSujeitaLic cadastro;
	private PesquisaMineralResponsavelTecnico pesquisaMineralResponsavel;
	private List<FormacaoProfissional> listaFormacaoProfissional;

	private TreeNode root;
	private TreeNode selectedNode;
	private PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral;
	private ProcessoDnpm processoDnpm;
	private ProspecaoDetalhamento prospecaoDetalhamento; 

	private CadastroAtividadeNaoSujeitaLicDocApensado docApensado;
	private CadastroAtividadeDocumentoIdentificacaoRepresentacao docRepresentacao;
	private List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaDocIdentificacao;
	private List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaDocRepresentacao;
	
	private List<LocalizacaoGeografica> listaLocalizacaoGeograficaToDelete;

	@PostConstruct
	public void init(){
		if(isNovoCadastro() || isEdicaoCadastro()){
			if(isNovoCadastro()){

				requerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica();

				pesquisaMineral = new PesquisaMineral();
				cadastro = new CadastroAtividadeNaoSujeitaLic(getUsuarioRealizandoAcao(), requerente.getPessoa(), TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL);
				pesquisaMineral.setCadastroAtividadeNaoSujeitaLic(cadastro);
				if(!Util.isNull(ContextoUtil.getContexto().getPessoa())){
					ContextoUtil.getContexto().setPessoa(null);
				}

			} 
			else if (isEdicaoCadastro()){
				// PEGA O CADASTRO DA QUE VEM DA TELA DE CONSULTA
				cadastro = ContextoUtil.getContexto().getCadastro();
				
				// RETIRA O CADASTRO DO CONTEXTO
				ContextoUtil.getContexto().setCadastro(null);
				
				carregarPesquisaMineral();
				
				// ATUALIZA O REQUERENTE DO CADASTRO
				requerente = new PessoaJuridica(cadastro.getIdePessoaRequerente().getIdePessoa());

				carregarPessoaJuridicaCNAE();
				
				if(!isPJComCNAE()){
					exibirInformacao066();
				}
			}
			carregarRequerente();
			carregarAbas();
		} 
	}

	private void carregarPessoaJuridicaCNAE() {
		try {
			requerente.setPessoaJuridicaCnaeCollection(facade.listarCnaeSecaoBy(requerente));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	private void carregarRequerente() {
		requerente = facade.obterPessoaJuridicaMontada(requerente.getIdePessoaJuridica());
	}

	/**
	 * Método para carregar a {@link PesquisaMineral} do {@link CadastroAtividadeNaoSujeitaLic}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void carregarPesquisaMineral() {
		try {
			pesquisaMineral = facade.carregarPesquisaMineral(cadastro);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da Pesquisa Mineral.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	private void carregarAbas(){
		carregarAbaDadosGerais();
		carregarAbaCaracterizacaoAtividade();
		carregarAbaDocumentosEstudos();
	}

	/**
	 * Método para carregar a aba <i>Dados Gerais</i>.
	 * 
	 * @author eduardo.fernandes
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void carregarAbaDadosGerais() {
		if (isEmpreendimentoSelecionado()) {
			carregarResponsaveisTecnicos();
			carregarEnderecoCorrespondencia();
		}
	}

	/**
	 * 
	 * Método para carregar a aba <i>Caracterização da Atividade</i>.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void carregarAbaCaracterizacaoAtividade(){
		listarSubtanciasMinerais();
		listarTipoCaptacao();
	}

	/**
	 * Método para carregar a aba <i>Documentos e Estudos</i>.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void carregarAbaDocumentosEstudos(){
		try {
			listaDocIdentificacao = new ArrayList<CadastroAtividadeDocumentoIdentificacaoRepresentacao>();
			listaDocRepresentacao = new ArrayList<CadastroAtividadeDocumentoIdentificacaoRepresentacao>();
			
			getCadastroAtividade().setCadastroAtividadeNaoSujeitaLicDocApensados(facade.listarCadastroAtividadeNaoSujeitaLicDocApensados(getCadastroAtividade()));
			getCadastroAtividade().setCadastroAtividadeDocumentoIdentificacaoRepresentacaos(facade.listarDocumentoIdentificacaoRepresentacao(getCadastroAtividade()));

			if(!Util.isNullOuVazio(getCadastroAtividade().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos())){
				listaDocIdentificacao.addAll(facade.obterListaDocumentoIdentificacaoRepresentacao(getCadastroAtividade().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos(), null));
				listaDocRepresentacao.addAll(facade.obterListaDocumentoIdentificacaoRepresentacao(getCadastroAtividade().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos(), isRepresentanteLegal()));
			}
			carregarListaDocumentoIdentificacao();
			if(Util.isNullOuVazio(listaDocRepresentacao)){
				carregarListaDocumentoRepresentacao();
			}

		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a aba de Documentos e Estudos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public void carregarListaDocumentoRepresentacao() {
		if(isRepresentanteLegal()){
			RepresentanteLegal representanteLegal = facade.buscarRepresentanteLegal(getUsuarioRealizandoAcao().getIdePessoaFisica(), requerente.getIdePessoaJuridica());
			if(!Util.isNull(representanteLegal)){
				CadastroAtividadeDocumentoIdentificacaoRepresentacao docRepresentante = new CadastroAtividadeDocumentoIdentificacaoRepresentacao(getCadastroAtividade(), representanteLegal);
				getCadastroAtividade().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos().add(docRepresentante);
				listaDocRepresentacao.add(docRepresentante);
			}
		} 
		else if (isProcuradorPJ() && isEmpreendimentoSelecionado()){
			ProcuradorRepEmpreendimento procurador = facade.buscarProcuradorRepresentanteEmpreendimento(getCadastroAtividade());
			if(!Util.isNull(procurador)){
				CadastroAtividadeDocumentoIdentificacaoRepresentacao docProcurador = new CadastroAtividadeDocumentoIdentificacaoRepresentacao(getCadastroAtividade(), procurador);
				getCadastroAtividade().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos().add(docProcurador);
				listaDocRepresentacao.add(docProcurador);
			}
		} 
	}

	private void carregarListaDocumentoIdentificacao() throws Exception {
		List<DocumentoIdentificacao> listaDocumentoIdentificacao = facade.listarDocumentoIdentificacao(cadastro.getIdePessoaFisicaCadastro());
		
		for(DocumentoIdentificacao documentoIdentificacao : listaDocumentoIdentificacao){
			if(Util.isNull(getDocumento(documentoIdentificacao))){
				CadastroAtividadeDocumentoIdentificacaoRepresentacao docIdentificacao = new CadastroAtividadeDocumentoIdentificacaoRepresentacao(getCadastroAtividade(), documentoIdentificacao);
				getCadastroAtividade().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos().add(docIdentificacao);
				listaDocIdentificacao.add(docIdentificacao);
			}
		}	
	}
	
	public DocumentoIdentificacao getDocumento(DocumentoIdentificacao doc){
		for(CadastroAtividadeDocumentoIdentificacaoRepresentacao cadastroDoc : getCadastroAtividade().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos()){
			if(!Util.isNull(cadastroDoc.getIdeDocumentoIdentificacao()) && cadastroDoc.getIdeDocumentoIdentificacao().equals(doc)){
				return cadastroDoc.getIdeDocumentoIdentificacao();
			}
		}
		return null;
	}

	/**
	 * Método para listar os {@link TipoCaptacao}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void listarTipoCaptacao() {
		try {
			List<PesquisaMineralUsoDaAgua> listaPesquisaUsoAgua = new ArrayList<PesquisaMineralUsoDaAgua>();
			for(TipoCaptacao tipoCaptacao : facade.listarTipoCaptacoes()){
				listaPesquisaUsoAgua.add(new PesquisaMineralUsoDaAgua(pesquisaMineral, tipoCaptacao));
			}
			if(Util.isNull(pesquisaMineral.getPesquisaMineralUsoDaAguas())){
				pesquisaMineral.setPesquisaMineralUsoDaAguas(new ArrayList<PesquisaMineralUsoDaAgua>());
			}
			for(PesquisaMineralUsoDaAgua usoDaAgua : listaPesquisaUsoAgua){
				if(Util.isNullOuVazio(pesquisaMineral.getPesquisaMineralUsoDaAguas()) || !pesquisaMineral.getPesquisaMineralUsoDaAguas().contains(usoDaAgua)){
					pesquisaMineral.getPesquisaMineralUsoDaAguas().add(usoDaAgua);
					if(!isTipoCaptacaoConcessionariaPublica(usoDaAgua)){
						usoDaAgua.setListaPesquisaMineralDocumentoCaptacao(facade.listarPesquisaMineralDocumentoCaptacao());
					}
				}
			}
			
			Collections.sort(pesquisaMineral.getPesquisaMineralUsoDaAguas());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipo Captação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método para listar os {@link MetodoProspeccao}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void listarMetodoProspeccao(ProcessoDnpm processoDnpm) {
		try {
			facade.prepararListaProcessoDnpmProspeccaoToSelect(processoDnpm);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Método Prospecção.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param event
	 */
	public void changeUtilizaAgua(ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			pesquisaMineral.setPesquisaMineralUsoDaAguas(null);
		} 
		else {
			listarTipoCaptacao();
		}
	}

	/**
	 * Método para listar as {@link SubstanciaMineral}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @throws Exception
	 */
	private void listarSubtanciasMinerais() {
		try {
			root = facade.montarArvoreTipologiasMineracao(new Tipologia(TipologiaEnum.MINERACAO), null);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Substâncias Minerais.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método que irá carregar os {@link ResponsavelEmpreendimento} do {@link Empreendimento}
	 * 
	 * @author eduardo.fernandes 
	 * @since 09/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	public void carregarResponsaveisTecnicos(){
		try {
			getEmpreendimento().setResponsavelEmpreendimentoCollection(facade.listarResponsaveisTecnicos(getEmpreendimento()));
			if(!isEmpreendimentoComResponsavelTecnico()) {
				exibirInformacao067();
			} 
			else {
				if(isExisteResponsavelPelaPesquisaMineral()){
					for (PesquisaMineralResponsavelTecnico pesquisaMineralResponsavelTecnico : pesquisaMineral.getPesquisaMineralResponsavelTecnicos()) {
						for (ResponsavelEmpreendimento responsavelEmpreendimento : getEmpreendimento().getResponsavelEmpreendimentoCollection()) {
							if(pesquisaMineralResponsavelTecnico.getIdePessoaFisicaResponsavelTecnico().equals(responsavelEmpreendimento.getIdePessoaFisica())){
								responsavelEmpreendimento.setSelecionado(true);
								break;
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o(s) responsável(eis) técnico(s) do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que irá carregar o {@link Endereco} de correspondência do {@link Empreendimento}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 09/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	public void carregarEnderecoCorrespondencia(){
		try {
			enderecoCorrespondencia = facade.carregarEnderecoCorrespondencia(getEmpreendimento());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o endereço de correspondência do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método que lista as {@link FormacaoProfissional}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void listarFormacaoProfissional(){
		try {
			if(Util.isNullOuVazio(listaFormacaoProfissional)){
				listaFormacaoProfissional = facade.listarFormacaoProfissional();
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de formação profissional.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes
	 * @since 28/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private List<FormacaoProfissional> getlistaFormacaoProfissional() {
		listarFormacaoProfissional();
		List<FormacaoProfissional> lista = new ArrayList<FormacaoProfissional>();
		lista.addAll(listaFormacaoProfissional);
		return lista;
	}

	/**
	 * Método para controlar a navegação das abas do cadastro de {@link PesquisaMineral};
	 * 
	 * @author eduardo.fernandes 
	 * @since 31/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @param event
	 */
	public void controlarAbas(TabChangeEvent event){
		if("tabDadosBasicos".equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if("tabCaractAtividade".equals(event.getTab().getId())){
			activeTab = 1;
		}
		else if("tabDocumentos".equals(event.getTab().getId())){
			activeTab = 2;
		}		
	}

	/**
	 * Método para avançar de aba.
	 * 
	 * @author eduardo.fernandes 
	 * @since 31/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	public void avancarAba(){
		if(validarAba()){
			salvar();
			activeTab++;
		}
	}

	/**
	 * Método que valida a aba atual.
	 * 
	 * @author eduardo.fernandes 
	 * @since 31/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	private boolean validarAba() {
		if(isAbaDadosBasicos()){
			return validarAbaDadosBasicos();
		}
		else if(isAbaCaracterizacaoDeAtividades()){
			return validarAbaCaracterizacaoDeAtividades();
		}
		else if(isAbaDocumentos()){
			return validarAbaDocumentos();
		}
		return false;
	}

	/**
	 * Método que valida a aba "Dados Básicos".
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean validarAbaDadosBasicos() {
		boolean valido = true;
		// Dados do Requerente
		if(!isPJComCNAE()){
			exibirInformacao066();
			valido = false;
		}
		if(Util.isNullOuVazio(requerente.getRepresentanteLegalCollection())){
			JsfUtil.addWarnMessage("O requerente não tem representantes legais cadastrados!");
			valido = false;
		}
		// Dados do Empreendimento
		if(!isEmpreendimentoSelecionado()){
			JsfUtil.addWarnMessage("O empreendimento " + Util.getString("messagem_003"));
			valido = false;
		} 
		else if(!isEmpreendimentoComResponsavelTecnico()){
			exibirInformacao067();
			valido = false;
		} 
		else {
			if(!isExisteResponsavelPelaPesquisaMineral()){
				JsfUtil.addWarnMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("lbl_cpm_responsavel_tecnico") + ".");
				valido = false;
			} 
			else {
				for(PesquisaMineralResponsavelTecnico responsavelTecnico : pesquisaMineral.getPesquisaMineralResponsavelTecnicos()){
					/*boolean respValido = true;*/
					if(!responsavelTecnico.isExisteCarteiraConselho()){
						/*exibirInformacao068();*/
						/*respValido = false;*/
					}
					if(Util.isNullOuVazio(responsavelTecnico.getFormacaoProfissional())){
						JsfUtil.addWarnMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("geral_lbl_formacao_profissional") + ".");
						/*respValido = false;*/
						valido =  false;
						break;
					}
					/*if(!respValido){
						valido =  false;
						break;
					}*/
				}
			}
		}
		return valido;
	}

	/**
	 * Método que valida a aba "Caracterização de Atividades".
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean validarAbaCaracterizacaoDeAtividades() {
		boolean valido = true;
		if (!isSubstanciaMineralAdicionada()) {
			exibirMensagem003("A " + Util.getString("lbl_cpm_substancia_mineral"));
			valido = false;
		} else {
			for(PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral : pesquisaMineral.getPesquisaMineralSubstanciaMinerals()){
				if(pesquisaMineralSubstanciaMineral.getIdeSubstanciaMineral().isOutros()){
					substanciaMineralOutros = true;
					break;
				}
			}
		}
		if (!isProcessoDnpmAdicionado()) {
			exibirMensagem003("O " + Util.getString("fce_lic_min_processo_dnpm"));
			valido = false;
		}
		else {
			for (ProcessoDnpm processoDnpm : pesquisaMineral.getProcessoDnpms()) {
				if (!validarAccordionProcessoDnpm(processoDnpm)) {
					valido = false;
					break;
				} else {
					for (ProcessoDnpmProspecao prospecao : processoDnpm.getListaProcessoDnpmProspecao()) {
						if(Util.isNullOuVazio(prospecao.getIdeProcessoDnpmProspecao())){
							JsfUtil.addErrorMessage("É necessário salvar o Processo DNPM " + this.processoDnpm.getNumProcessoDnpm() + ".");
							valido = false;
							break;
						}
					}
				}
			}
		}
		if (Util.isNull(pesquisaMineral.getIndUtilizaAgua())) {
			exibirMensagem003("A pergunta '" + Util.getString("lbl_cpm_atividade_utiliza_agua") + "'");
			valido = false;
		} 
		else if (pesquisaMineral.getIndUtilizaAgua()) {
			boolean temSelecionado = false;
			for (PesquisaMineralUsoDaAgua usoDaAgua : pesquisaMineral.getPesquisaMineralUsoDaAguas()) {
				if(usoDaAgua.isSelecionado()){
					temSelecionado = true;
					boolean escape = false;
					if (isTipoCaptacaoConcessionariaPublica(usoDaAgua)) {
						if ((Util.isNullOuVazio(usoDaAgua.getIndFonteEmbasa()) || !usoDaAgua.getIndFonteEmbasa())
								&&
								(Util.isNullOuVazio(usoDaAgua.getIndFonteSaae()) || !usoDaAgua.getIndFonteSaae())) {
							exibirInformacao041("uma " + Util.getString("lbl_cpm_fonte"));
							valido = false;
							escape = true;
						}
					}
					else {
						if (Util.isNullOuVazio(usoDaAgua.getPesquisaMineralDocumentoCaptacao())) {
							exibirInformacao041(" um " + Util.getString("lbl_cpm_documento"));
							valido = false;
							escape = true;
						}
						if (Util.isNullOuVazio(usoDaAgua.getNumDocumento())) {
							exibirMensagem003("O " + Util.getString("lbl_cpm_num_documento"));
							valido = false;
							escape = true;
						}
					}
					if (escape) {
						break;
					}
				}
			}
			if(!temSelecionado){
				exibirInformacao041(" uma Captação");
				valido = false;
			}
		}
		return valido;
	}

	/**
	 * Método que valida a aba "Documentos e Estudos".
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean validarAbaDocumentos() {
		boolean valido = true;
		for (CadastroAtividadeNaoSujeitaLicDocApensado docs : getListaCadastroAtividadeNaoSujeitaLicDocApensado()) {
			if (!isDocumentoEnviado(docs)) {
				JsfUtil.addErrorMessage(Util.getString("documento_obrigatorio_msg_envio_pendencia"));
				valido = false;
				break;
			}
		}
		if(Util.isNullOuVazio(listaDocIdentificacao)){
			JsfUtil.addErrorMessage(Util.getString("documento_obrigatorio_msg_envio_pendencia"));
			valido = false;
		} 
		
		if(Util.isNullOuVazio(listaDocRepresentacao)){
			JsfUtil.addErrorMessage(Util.getString("documento_obrigatorio_msg_envio_pendencia"));
			valido = false;
		} 
		
		return valido;
	}

	/**
	 * Método para voltar de aba.
	 * 
	 * @author eduardo.fernandes 
	 * @since 31/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	public void voltarAba(){
		activeTab--;
	}

	/**
	 * Método para salvar o Aceite na tela de Instruções do Cadastro.
	 * 
	 * @author eduardo.fernandes
	 * @since 31/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	public void salvarInstrucoesCadastro() {
		if (aceite) {
			closeDialogAceite = true;
			telaValida();
			if(!isPJComCNAE()){
				exibirInformacao066();
			}
		}
		else {
			telaInvalida();
			exibirMensagem003("O aceite");
		}
	}
	
	public void fecharInstrucoesCadastro() {
		if (!aceite || !closeDialogAceite) {
			redirecionarPagina(PaginaEnum.IDENTIFICACAO_ATIVIDADE_SEM_LICENCIAMENTO);
		}
	}
	
	public void salvarTermoCompromisso() {
		try {
			if (comprometimento) {
				telaValida();
				facade.finalizar(this);
				
				ContextoUtil.getContexto().setUpdateMessage("O cadastro nº " + getCadastroAtividade().getNumCadastro() + ", foi salvo com sucesso!");
				ContextoUtil.getContexto().setSucessMessage(true);
				
				redirecionarPagina(PaginaEnum.CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO);
			}
			else {
				telaInvalida();
				exibirMensagem003("O comprometimento");
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o termo de compromisso.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método para salvar a {@link SubstanciaMineral}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	public void salvarSubstanciaMineral() {
		if(isSubstanciaMineral()){
			if(!isSubstanciaMineralAdicionada()){
				pesquisaMineral.setPesquisaMineralSubstanciaMinerals(new ArrayList<PesquisaMineralSubstanciaMineral>());
			} 
			boolean podeAdicionar = true;
			for(PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral : pesquisaMineral.getPesquisaMineralSubstanciaMinerals()){
				if(getSubstanciaMineralSelecionada().equals(pesquisaMineralSubstanciaMineral.getIdeSubstanciaMineral())){
					podeAdicionar = false;
					JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_substancia_mineral") + " já foi selecionada.");
				}
			}
			if(podeAdicionar){
				pesquisaMineral.addPesquisaMineralSubstanciaMineral(new PesquisaMineralSubstanciaMineral(pesquisaMineral, getSubstanciaMineralSelecionada()));
				if(getSubstanciaMineralSelecionada().isOutros()){
					exibirInformacao001();
				}
				telaValida();
				exibirMensagem001();
			}
		}
		else {
			telaInvalida();
		}
	}

	/**
	 * Método para excluir uma {@link SubstanciaMineral}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	public void excluirSubstanciaMineral() {
		try {
			pesquisaMineral.removePesquisaMineralSubstanciaMineral(pesquisaMineralSubstanciaMineral);
			facade.excluirPesquisaMineralSubstanciaMineral(pesquisaMineralSubstanciaMineral);
			exibirMensagem005();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a substância mineral.");
		}
	}

	private void adicionarLocalizacaoGeograficaParaExclusao(LocalizacaoGeografica locGeo){
		if(Util.isNull(listaLocalizacaoGeograficaToDelete)){
			listaLocalizacaoGeograficaToDelete = new ArrayList<LocalizacaoGeografica>();
		}
		listaLocalizacaoGeograficaToDelete.add(locGeo);
	}
	
	private void excluirListaLocalizacaoGeografica(){
		try {
			facade.excluirLocalizacaoGeografica(listaLocalizacaoGeograficaToDelete);
			listaLocalizacaoGeograficaToDelete = null;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método para excluir a Poligonal ({@link LocalizacaoGeografica}) do {@link ProcessoDnpm}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	public void excluirPoligonalRequeridaDnpm(){
		adicionarLocalizacaoGeograficaParaExclusao(processoDnpm.getIdeLocalizacaoGeografica());
		processoDnpm.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		exibirMensagem005();
	}
	
	public void excluirCoordenadaDetalhamento(){
		adicionarLocalizacaoGeograficaParaExclusao(prospecaoDetalhamento.getIdeLocalizacaoGeografica());
		prospecaoDetalhamento.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		exibirMensagem005();
	}

	public void excluirProspeccaoMineral() {
		try {
			ProcessoDnpmProspecao processoDnpmProspecao = prospecaoDetalhamento.getProcessoDnpmProspecao();
			processoDnpmProspecao.removeProspecaoDetalhamento(prospecaoDetalhamento);
			facade.excluirProspeccaoDetalhamento(prospecaoDetalhamento);
			exibirMensagem005();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a Prospecção Mineral.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método para finalizar o cadastro da Pesquisa Mineral.
	 * 
	 * @author eduardo.fernandes 
	 * @since 31/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	public void finalizar() {
		activeTab = 0;
		boolean podeFinalizar = true;
		boolean docsIncompletos = false;
		for (int i = 0; i < 3; i++) {
			if (validarAba()) {
				salvar();
				if(activeTab != 2){
					activeTab++;
				}
			}
			else {
				podeFinalizar = false;
				if(isAbaDocumentos()){
					docsIncompletos = true;
				} 
				else {
					atualizar("tabCadastroPesquisaMineral");
					break;
				}
			}
		}
		if (podeFinalizar && !substanciaMineralOutros && isResponsavelTecnicoValido()) {
			exibir("dialogTermo");
		} 
		else if(!docsIncompletos){
			if(!isResponsavelTecnicoValido()){
				redirecionarPagina(PaginaEnum.CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO);
			}
			else if (substanciaMineralOutros && isAbaDocumentos() ){
				ContextoUtil.getContexto().setUpdateMessage(Util.getString("msg_generica_cadastro_outros"));
				ContextoUtil.getContexto().setSucessMessage(false);
				redirecionarPagina(PaginaEnum.CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO);
			} 
		}
	}

	public void redirecionarParaTelaConsulta() {
		if(!isNovoCadastro() && !isEdicaoCadastro()){
			redirecionarPagina(PaginaEnum.CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO);
		}
	}

	/**
	 * @author eduardo.fernandes
	 * @since 28/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	private void salvar() {
		facade.salvar(this);
	}

	/**
	 * Método que adiciona o {@link ResponsavelEmpreendimento} como {@link PesquisaMineralResponsavelTecnico} da {@link PesquisaMineral}.
	 * 
	 * @author eduardo.fernandes
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param event
	 */
	private void adicionarResponsavelTecnico(ResponsavelEmpreendimento responsavelTecncio) {
		try {
			if (!isExisteResponsavelPelaPesquisaMineral()) {
				pesquisaMineral.setPesquisaMineralResponsavelTecnicos(new ArrayList<PesquisaMineralResponsavelTecnico>());
			}
			PesquisaMineralResponsavelTecnico respTec = new PesquisaMineralResponsavelTecnico(pesquisaMineral, responsavelTecncio);
			if (!pesquisaMineral.getPesquisaMineralResponsavelTecnicos().contains(respTec)) {
				respTec.setNumeroCarteiraConselho(facade.obterNumeroCarteiraConselhoClasse(responsavelTecncio.getIdePessoaFisica().getPessoa()));
				if (Util.isNullOuVazio(respTec.getNumeroCarteiraConselho())) {
					respTec.setNumeroCarteiraConselho("Não informado");
					exibirInformacao068();
				} 
				else { 
					exibirMensagem001();
				}
				respTec.setListaFormacaoProfissional(getlistaFormacaoProfissional());
				pesquisaMineral.addPesquisaMineralResponsavelTecnico(respTec);
			}
			else {
				JsfUtil.addWarnMessage("O responsável técnico já foi adicionado.");
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o número da " + Util.getString("geral_lbl_carteira_conselho_classe") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void changeCheckResponsavelTecnico(ResponsavelEmpreendimento responsavelEmpreendimento){
		if(responsavelEmpreendimento.isSelecionado()){
			adicionarResponsavelTecnico(responsavelEmpreendimento);
		} 
		else {
			excluirResponsavelTecnico(responsavelEmpreendimento);
		}
	}
	
	private void excluirResponsavelTecnico(ResponsavelEmpreendimento responsavelEmpreendimento) {
		try {
			PesquisaMineralResponsavelTecnico resp = getResponsavelTecnicoBy(responsavelEmpreendimento.getIdePessoaFisica());
			pesquisaMineral.removePesquisaMineralResponsavelTecnico(resp);
			facade.excluirPesquisaMineralResponsavelTecnico(resp);
			exibirMensagem005();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " o " + Util.getString("lbl_cpm_responsavel_tecnico") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}
	
	private PesquisaMineralResponsavelTecnico getResponsavelTecnicoBy(PessoaFisica pf){
		for (PesquisaMineralResponsavelTecnico responsavelTecnico : pesquisaMineral.getPesquisaMineralResponsavelTecnicos()) {
			if(responsavelTecnico.getIdePessoaFisicaResponsavelTecnico().equals(pf)){
				return responsavelTecnico;
			}
		}
		return null;
	}

	/**
	 * Método que verifica se a opção "Outros" foi selecionada.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param event
	 */
	public void checarOutros(ValueChangeEvent event){
		FormacaoProfissional formacaoProfissional = (FormacaoProfissional) event.getNewValue();
		if(!Util.isNull(formacaoProfissional) && formacaoProfissional.isOutros()){
			JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros"));
		}
	}
	
	public void incluirProcessoDpnm(){
		processoDnpm = new ProcessoDnpm(pesquisaMineral);
	}

	public void incluirFuroSondagem(ProcessoDnpm processoDnpm){
		prospecaoDetalhamento = new ProspecaoDetalhamento(processoDnpm, MetodoProspeccaoEnum.SONDAGENS);
	}

	public void incluirTrincheira(ProcessoDnpm processoDnpm){
		prospecaoDetalhamento = new ProspecaoDetalhamento(processoDnpm, MetodoProspeccaoEnum.TRINCHEIRAS);
	}

	public void incluirPoco(ProcessoDnpm processoDnpm){
		prospecaoDetalhamento = new ProspecaoDetalhamento(processoDnpm, MetodoProspeccaoEnum.POCOS);
	}

	public void inserirProspeccaoDetalhamento(){
		if (isProspeccaoDetalhamentoValido()) {
			ProcessoDnpmProspecao processoDnpmProspecao = prospecaoDetalhamento.getProcessoDnpmProspecao();
			if (!processoDnpmProspecao.getProspecaoDetalhamentos().contains(prospecaoDetalhamento)) {
				processoDnpmProspecao.addProspecaoDetalhamento(prospecaoDetalhamento);
				exibirMensagem001();
			}
			else {
				exibirMensagem002();
			}
			telaValida();
		}
		else {
			telaInvalida();
		}
	}
	
	public void selecionarProspeccao(ProcessoDnpmProspecao dnpmProspeccao){
		ProcessoDnpm dnpm = dnpmProspeccao.getProcessoDnpm();
		if(dnpmProspeccao.getIdeMetodoProspeccao().isChecked()){
			if(Util.isNullOuVazio(dnpm.getListaProcessoDnpmProspecao())){
				dnpm.setListaProcessoDnpmProspecao(new ArrayList<ProcessoDnpmProspecao>());
			}
			dnpm.getListaProcessoDnpmProspecao().add(dnpmProspeccao);
		} 
		else { 
			dnpm.getListaProcessoDnpmProspecao().remove(dnpmProspeccao);
		}
	}
	
	public void controlarTabAccordion(TabChangeEvent event){
		processoDnpm = (ProcessoDnpm) event.getData();
		System.out.println(processoDnpm.getNumProcessoDnpm());
	}

	/**
	 * Método para verificar se a {@link ProspecaoDetalhamento} é válida.
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean isProspeccaoDetalhamentoValido() {
		boolean valido = true;
		if (!isCoordenadaSalva()) {
			valido = false;
			exibirMensagem003("A " + Util.getString("lbl_cpm_coordenada") + " " + getHeaderProspeccao());
		} 
		/*
		 * #8541
		 * else {
			if(!Util.isNullOuVazio(prospecaoDetalhamento.getProcessoDnpmProspecao().getProspecaoDetalhamentos())){
				for(ProspecaoDetalhamento detalhamento : prospecaoDetalhamento.getProcessoDnpmProspecao().getProspecaoDetalhamentos()){
					if(facade.isMesmaCoordenada(prospecaoDetalhamento, detalhamento)){
						valido = false;
						exibirInformacao076();
					}
				}
			}
		}*/
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNomIdentificacao())){
			valido = false;
			exibirMensagem003("A " + Util.getString("lbl_cpm_identificacao") + " " + getHeaderProspeccao());
		}
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNomImovelRural())){
			valido = false;
			exibirMensagem003("O " + Util.getString("lbl_cpm_nome_imovel_rural"));
		}
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNomIdentificacaoAlvo())){
			valido = false;
			exibirMensagem003("A " + Util.getString("lbl_cpm_identificacao_alvo"));
		}
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNumProfundidade())){
			valido = false;
			exibirMensagem013("A " + Util.getString("lbl_cpm_profundidade") + Util.getString("geral_lbl_metro"));
		}
		if (isProspeccaoFuroSondagem() && !validarProspeccaoSondagem()) {
			valido = false;
		} 
		else if (isProspeccaoTrincheira() && !validarProspeccaoTrincheiras()) {
			valido = false;
		} 
		else if (isProspeccaoPoco() && !validarProspeccaoPocos()) {
			valido = false;
		}
		return valido;
	}

	/**
	 * Método para verificar se a {@link ProspecaoDetalhamento} do tipo Poço é válida. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean validarProspeccaoPocos() {
		boolean valido = true;
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNumDiametro())){
			valido = false;
			exibirMensagem013("O " + Util.getString("lbl_cpm_diametro") + Util.getString("geral_lbl_milimetro"));
		}
		return valido;
	}

	/**
	 * Método para verificar se a {@link ProspecaoDetalhamento} do tipo Trincheira é válida.
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean validarProspeccaoTrincheiras() {
		boolean valido = true;
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNumLargura())){
			valido = false;
			exibirMensagem013("A " + Util.getString("lbl_cpm_largura") + Util.getString("geral_lbl_metro"));
		}
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNumComprimento())){
			valido = false;
			exibirMensagem013("O " + Util.getString("lbl_cpm_comprimento") + Util.getString("geral_lbl_metro"));
		}
		return valido;
	}

	/**
	 * Método para verificar se a {@link ProspecaoDetalhamento} do tipo Sondagem é válida.
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean validarProspeccaoSondagem() {
		boolean valido = true;
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNumDiametro())){
			valido = false;
			exibirMensagem013("O " + Util.getString("lbl_cpm_diametro") + Util.getString("geral_lbl_milimetro"));
		}
		if(Util.isNullOuVazio(prospecaoDetalhamento.getNumAreaPraca())){
			valido = false;
			exibirMensagem013("A " + Util.getString("lbl_cpm_area_praca") + Util.getString("geral_lbl_metro_quadrado"));
		}
		return valido;
	}

	public void editarProcessoDpnm(ActionEvent event) {
		processoDnpm = (ProcessoDnpm) event.getComponent().getAttributes().get("processoDnpm");
	}

	/**
	 * Método para salvar o {@link ProcessoDnpm}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	public void salvarProcessoDnpm(){
		try {
			if(isProcessoDnpmValido()){
				if(!isProcessoDnpmAdicionado()){
					pesquisaMineral.setProcessoDnpms(new ArrayList<ProcessoDnpm>());
				}
				processoDnpm.setAreaProcessoDnpm(facade.retornarAreaShape(processoDnpm.getIdeLocalizacaoGeografica()));
				listarMetodoProspeccao(processoDnpm);
				if(!pesquisaMineral.getProcessoDnpms().contains(processoDnpm)){
					pesquisaMineral.addProcessoDnpm(processoDnpm);
					exibirMensagem001();
				}
				else {
					exibirMensagem002();
				}
				excluirListaLocalizacaoGeografica();
				facade.salvarProcessoDnpm(processoDnpm);
				telaValida();
			} 
			else {
				telaInvalida();
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString(""));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public String visualizarLocalizacao(LocalizacaoGeografica locGeo) {
		if(isLocalizacaoGeograficaSalva(locGeo)){
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
		}
		return "";
	}

	/**
	 * Método para verificar se o {@link ProcessoDnpm} é válido.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean isProcessoDnpmValido() {
		boolean valido = true;
		if(Util.isNullOuVazio(processoDnpm.getNumProcessoDnpm())){
			exibirMensagem013("O " + Util.getString("fce_lic_min_numero_processo"));
			valido = false;
		}
		if (!isLocalizacaoGeograficaSalva(processoDnpm.getIdeLocalizacaoGeografica())) {
			exibirMensagem003("A " + Util.getString("fce_lic_min_poligonal_requerida_dnpm"));
			valido = false;
		} else {
			if(isProcessoDnpmAdicionado()){
				for(ProcessoDnpm dnpm : pesquisaMineral.getProcessoDnpms()){
					if(!dnpm.equals(processoDnpm)){
						try {
							if(!facade.naoExisteIntersecao(dnpm.getIdeLocalizacaoGeografica(), processoDnpm.getIdeLocalizacaoGeografica())){
								exibirInformacao070();
								valido = false;
								break;
							}
						} catch (Exception e) {
							Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
							valido = false;	
							break;
						}
					}
				}
			}
		}
		return valido;
	}

	public void adicionarOuRemoverPesquisaMineralUsoAgua(PesquisaMineralUsoDaAgua pesquisaMineralUsoDaAgua){
		if(pesquisaMineralUsoDaAgua.isSelecionado()){
			pesquisaMineral.addPesquisaMineralUsoDaAgua(pesquisaMineralUsoDaAgua);
			if(!isTipoCaptacaoConcessionariaPublica(pesquisaMineralUsoDaAgua)){
				pesquisaMineralUsoDaAgua.setListaPesquisaMineralDocumentoCaptacao(listarDocumentoCaptacao());
			}
		} 
		else {
			pesquisaMineral.removePesquisaMineralUsoDaAgua(pesquisaMineralUsoDaAgua);
		}
	}

	/**
	 * Método para listar os {@link PesquisaMineralDocumentoCaptacao}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisaMineralUsoAgua
	 * @throws Exception
	 */
	private List<PesquisaMineralDocumentoCaptacao> listarDocumentoCaptacao(){
		try {
			return facade.listarPesquisaMineralDocumentoCaptacao();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Documentos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void limpar() {
		
	}

	public void limparSubstanciaMineral() {
		selectedNode = null;
	}

	public void limparProcessoDnpm() {
		processoDnpm = null;
	}

	public void limparProspeccao() {
		prospecaoDetalhamento = null;
	}

	public void salvarAccordionProcessoDnpm(ProcessoDnpm processoDnpm) {
		if (validarAccordionProcessoDnpm(processoDnpm)) {
			excluirListaLocalizacaoGeografica();
			facade.salvarAccordionProcessoDnpm(processoDnpm);
			exibirMensagem015(Util.getString("fce_lic_min_processo_dnpm"));
		}
	}

	/**
	 * Método para verificar se todas as informações {@link ProcessoDnpm} é válido.
	 * 
	 * @author eduardo.fernandes
	 * @since 23/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param processoDnpm
	 * @return
	 */
	private boolean validarAccordionProcessoDnpm(ProcessoDnpm processoDnpm) {
		boolean valido = true;
		this.processoDnpm = processoDnpm;
		if (isProcessoDnpmValido()) {
			if (!Util.isNullOuVazio(processoDnpm.getListaProcessoDnpmProspecao())) {
				for (ProcessoDnpmProspecao prospecao : processoDnpm.getListaProcessoDnpmProspecao()) {
					if (Util.isNullOuVazio(prospecao.getProspecaoDetalhamentos())) {
						exibirMensagem003(getStringMetodoProspeccao(prospecao));
						valido = false;
						break;
					}
					else {
						for (ProspecaoDetalhamento prospecaoDetalhamento : prospecao.getProspecaoDetalhamentos()) {
							this.prospecaoDetalhamento = prospecaoDetalhamento;
							if (!isProspeccaoDetalhamentoValido()) {
								valido = false;
								break;
							}
						}
					}
				}
			}
			else {
				exibirInformacao041(" um " + Util.getString("lbl_cpm_metodo_prospeccao"));
				valido = false;
			}
		}
		else {
			valido = false;
		}
		return valido;
	}

	/**
	 * Método para obter uma String com o nome do {@link MetodoProspeccao}.
	 * 
	 * @author eduardo.fernandes
	 * @param prospeccao
	 * @since 29/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private String getStringMetodoProspeccao(ProcessoDnpmProspecao prospeccao) {
		String artigo = "A ";
		if (prospeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.POCOS.getId())) {
			artigo = "O ";
		}
		String nome = prospeccao.getIdeMetodoProspeccao().getNomMetodoProspeccao();
		if (nome.endsWith("s")) {
			nome = nome.substring(0, nome.length() - 1);
		}
		return artigo + nome;
	}

	public void excluirProcessoDnpm(ProcessoDnpm processoDnpm) {
		try {
			pesquisaMineral.removeProcessoDnpm(processoDnpm);
			facade.excluirAccordionProcessoDnpm(processoDnpm);
			exibirMensagem005();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " o Processo DNPM.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public void uploadArquivo(FileUploadEvent event) {
		try {
			docApensado.setDtcEnvioDocumento(new Date());
			docApensado.setIdePessoaFisicaEnvio(getUsuarioRealizandoAcao());

			String caminhoArqivoNovo = DiretorioArquivoEnum.PESQUISA_MINERAL.toString();

			if (!Util.isNullOuVazio(getCadastroAtividade().getIdeCadastroAtividadeNaoSujeitaLic())) {
				caminhoArqivoNovo += "_" + docApensado.getIdeCadastroAtividadeNaoSujeitaLicDocApensado();
			}
			else {
				caminhoArqivoNovo += "_" + docApensado.getCadastroAtividadeNaoSujeitaLic().getIdePessoaFisicaCadastro().getIdePessoaFisica() + "_"
						+ docApensado.getCadastroAtividadeNaoSujeitaLic().getIdePessoaRequerente().getIdePessoa();
			}

			facade.excluirDocumentoApensadoAnteriormente(docApensado.getUrlDocumento());
			docApensado.setUrlDocumento(FileUploadUtil.Enviar(event, caminhoArqivoNovo));
			JsfUtil.addSuccessMessage(Util.getString("msg_generica_arquivo_enviado"));
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o arquivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	public void uploadDocumentoRepresentacao(FileUploadEvent event) {
		try {
			docRepresentacao.setDtcEnvioDocumento(new Date());
			docRepresentacao.setIdePessoaFisicaEnvio(getUsuarioRealizandoAcao());
			
			String caminhoArquivoAntigo = "";
			if(docRepresentacao.isDocumentoRepresentanteLegal()){
				caminhoArquivoAntigo = docRepresentacao.getDscCaminhoContratoSocial();
				docRepresentacao.getIdeRepresentanteLegal().setDscCaminhoRepresentacao(FileUploadUtil.Enviar(event, DiretorioArquivoEnum.EMPREENDIMENTO.toString()));
			} 
			else if(docRepresentacao.isDocumentoProcuradorPessoaJuridica()){
				caminhoArquivoAntigo = docRepresentacao.getDscCaminhoProcuracaoPJ();
				docRepresentacao.getIdeProcuradorRepEmpreendimento().setDscCaminhoProcuracao(FileUploadUtil.Enviar(event, DiretorioArquivoEnum.EMPREENDIMENTO.toString()));
			} 
			else if(docRepresentacao.isDocumentoIdentificacao()){
				caminhoArquivoAntigo = docRepresentacao.getDscCaminhoDocIdentificacao();
				docRepresentacao.getIdeDocumentoIdentificacao().setDscCaminhoArquivo(FileUploadUtil.Enviar(event, DiretorioArquivoEnum.DOCUMENTOIDENTIFICACAO.toString()));
			}
			if(!Util.isNullOuVazio(caminhoArquivoAntigo)){
				facade.excluirDocumentoApensadoAnteriormente(caminhoArquivoAntigo);
			}
			facade.atulizarDocumento(docRepresentacao);
			JsfUtil.addSuccessMessage(Util.getString("msg_generica_arquivo_enviado"));
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o arquivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	public StreamedContent getArquivoBaixar(Object obj) {
		return facade.baixarArquivo(obj);
	}
	
	/**
	 * Método para gerar uma requisição AJAX.
	 * 
	 * @author eduardo.fernandes 
	 */
	public void poll() {
		RequestContext.getCurrentInstance().execute("pesquisaMineralPoll.stop();");
		if(!Util.isNullOuVazio((String) SessaoUtil.recuperarObjetoSessao("URL_EMPREENDIMENTO_ORIGEM"))){
			prepararInclusaoEmpreendimento();
		} 
		else if(!cadastro.isVisualizar() && !cadastro.isEdicao() && !aceite){
			exibir("dialogInstrucoes");
		} 
	}

	/**
	 * 
	 * Método para tratar o {@link Empreendimento} recém cadastrado pelo usuário.
	 * 
	 * @author eduardo.fernandes 
	 * @since 17/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8537">#8537</a>
	 */
	private void prepararInclusaoEmpreendimento() {
		aceite = true;
		salvarInstrucoesCadastro();
		Empreendimento empreendimentoSessao = (Empreendimento) SessaoUtil.recuperarObjetoSessao("EMPREENDIMENTO_SESSAO");
		carregarInformacoesDoEmpreendimento(empreendimentoSessao);
		getEmpreendimento().setEndereco(facade.carregarEndereco(empreendimentoSessao));
		SessaoUtil.removerObjetoSessao("URL_EMPREENDIMENTO_ORIGEM");
		SessaoUtil.removerObjetoSessao("EMPREENDIMENTO_SESSAO");
		atualizar("tabCadastroPesquisaMineral");
	}

	public void carregarInformacoesDoEmpreendimento(Empreendimento empreendimentoSelecionado) {
		getPesquisaMineral().getCadastroAtividadeNaoSujeitaLic().setIdeEmpreendimento(empreendimentoSelecionado);
		carregarResponsaveisTecnicos();
		carregarEnderecoCorrespondencia();
		getPesquisaMineral().setPesquisaMineralResponsavelTecnicos(new ArrayList<PesquisaMineralResponsavelTecnico>());
		carregarListaDocumentoRepresentacao();
	}

	/*
	 * ------------------------
	 * --------flags-----------
	 * ------------------------
	 */
	private boolean isRepresentanteLegal(){
		if(isNovoCadastro()){
			return REPRESENTANTE_LEGAL_PJ == ContextoUtil.getContexto().getTipoSolicitante();
		} 
		else {
			return facade.isRepresentante(getCadastroAtividade().getIdePessoaFisicaCadastro(), requerente);
		}
	}
	
	private boolean isProcuradorPJ(){
		if(isNovoCadastro()){
			return PROCURADOR_PJ == ContextoUtil.getContexto().getTipoSolicitante();
		} 
		else {
			return facade.isProcurador(getCadastroAtividade().getIdePessoaFisicaCadastro(), requerente);
		}
	}
	
	private boolean isNovoCadastro(){
		return Util.isNull(ContextoUtil.getContexto().getCadastro()) 
				&& !Util.isNullOuVazio(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente())
				&& !Util.isNullOuVazio(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa());
	}
	
	private boolean isEdicaoCadastro(){
		return !Util.isNull(ContextoUtil.getContexto().getCadastro());
	}
	
	/**
	 * Método que verifica se a aba "Dados Básicos" está ativa.
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 */
	private boolean isAbaDadosBasicos() {
		return activeTab == ABA_DADOS_BASICOS;
	}

	/**
	 * Método que verifica se a aba "Caracterização de Atividades" está ativa.
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 */
	private boolean isAbaCaracterizacaoDeAtividades() {
		return activeTab == ABA_CARACTERIZACAO_ATIVIDADES;
	}

	/**
	 * Método que verifica se a aba "Documentos" está ativa.
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 */
	private boolean isAbaDocumentos() {
		return activeTab == ABA_DOCUMENTOS_ESTUDOS;
	}

	public boolean isEmpreendimentoSelecionado(){
		return !Util.isNullOuVazio(getEmpreendimento());
	}
	
	public boolean isEmpreendimentoEditavel(){
		return Util.isNullOuVazio(pesquisaMineral) && Util.isNullOuVazio(cadastro);
	}

	public boolean isEmpreendimentoComEnderecoCorrespondencia(){
		return isEmpreendimentoSelecionado() && !Util.isNull(enderecoCorrespondencia);
	}

	/**
	 * Método que verifica se o usuário pode prosseguir no cadastro da {@link PesquisaMineral}.
	 * 
	 * @author eduardo.fernandes certo
	 * @since 10/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	public boolean isCadastroValido(){
		return isPJComCNAE() && isEmpreendimentoValido() && isResponsaveisAptosPesquisaMineral();
	}
	
	public boolean isPesquisaMineralSalva(){
		return !Util.isNullOuVazio(pesquisaMineral);
	}

	/**
	 * Método que verifica se há um Responsável Técnico {@link PesquisaMineralResponsavelTecnico} e se ele tem o número da Carteira de Identidade de Conselho de Classe cadastrado.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean isResponsavelTecnicoValido() {
		for(PesquisaMineralResponsavelTecnico responsavelTecnico : pesquisaMineral.getPesquisaMineralResponsavelTecnicos()){
			boolean respValido = true;
			if (!responsavelTecnico.isExisteCarteiraConselho()){
				ContextoUtil.getContexto().setUpdateMessage(Util.getString("msg_cpm_inf_068"));
				ContextoUtil.getContexto().setSucessMessage(false);
				respValido = false;
			}
			if(responsavelTecnico.getFormacaoProfissional().isOutros()) {
				ContextoUtil.getContexto().setUpdateMessage(Util.getString("msg_generica_cadastro_outros"));
				ContextoUtil.getContexto().setSucessMessage(false);
				respValido = false; 
			}
			if(!respValido){
				return false;
			}
		}
		return true;
	}

	/**
	 * Método que verifica se existe o CNAE - INDÚSTRIAS EXTRATIVAS vinculado a {@link PessoaJuridica}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	public boolean isPJComCNAE() {
		if (!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getPessoaJuridicaCnaeCollection())) {
			for(PessoaJuridicaCnae pjcn : requerente.getPessoaJuridicaCnaeCollection()) {
				if(pjcn.getIdeCnae().getIdeCnae().equals(CnaeEnum.INDUSTRIAS_EXTRATIVAS.getIdCNAE())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Método que verifica se {@link Empreendimento} é válido para cadastro.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean isEmpreendimentoValido(){
		return isEmpreendimentoSelecionado() && isEmpreendimentoComResponsavelTecnico();
	}

	/**
	 * Método que verifica se {@link Empreendimento} tem {@link ResponsavelEmpreendimento}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean isEmpreendimentoComResponsavelTecnico() {
		return !Util.isNullOuVazio(getEmpreendimento().getResponsavelEmpreendimentoCollection());
	}

	public boolean isExisteResponsavelPelaPesquisaMineral(){
		return !Util.isNullOuVazio(pesquisaMineral.getPesquisaMineralResponsavelTecnicos());
	}
	
	private boolean isResponsaveisAptosPesquisaMineral(){
		if(isExisteResponsavelPelaPesquisaMineral()){
			for (PesquisaMineralResponsavelTecnico responsavelTecnico : pesquisaMineral.getPesquisaMineralResponsavelTecnicos()) {
				if(!responsavelTecnico.isExisteCarteiraConselho()){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Método que verifica se foi selecionada uma {@link SubstanciaMineral}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private boolean isSubstanciaMineral(){
		if(Util.isNull(getSelectedNode()) || Util.isNull(getSelectedNode().getData())){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_lic_min_substancia_mineral") + ".");
			return false;
		}
		else{
			if(!getSelectedNode().getType().equals("substancia")){
				JsfUtil.addErrorMessage("A opção escolhida não é uma Substância Mineral.");
				return false;
			}
		}
		return true;
	}

	public boolean isSubstanciaMineralAdicionada(){
		return !Util.isNullOuVazio(pesquisaMineral.getPesquisaMineralSubstanciaMinerals());
	}

	public boolean isProcessoDnpmAdicionado(){
		return !Util.isNullOuVazio(pesquisaMineral.getProcessoDnpms());
	}

	public boolean existeLocalizacaoGeografica(ProspecaoDetalhamento detalhamento){
		return !Util.isNullOuVazio(detalhamento.getIdeLocalizacaoGeografica().getDadoGeograficoCollection());
	}
	
	/**
	 * Método que verifica se a {@link LocalizacaoGeografica} está persistida.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param locGeo
	 * @return
	 */
	private boolean isLocalizacaoGeograficaSalva(LocalizacaoGeografica locGeo) {
		try{
			return !Util.isNullOuVazio(locGeo) && !Util.isNull(facade.retornarGeometriaShapeByLocalizacaoGeografica(locGeo));
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isPoligonalRequeridaDnpmSalva(){
		return !Util.isNull(processoDnpm) && isLocalizacaoGeograficaSalva(processoDnpm.getIdeLocalizacaoGeografica());
	}

	public boolean isTipoCaptacaoConcessionariaPublica(PesquisaMineralUsoDaAgua pesquisaMineralUsoDaAgua){
		return TipoCaptacaoEnum.CAPTACAO_CONCESSIONARIA_PUBLICA.getId().equals(pesquisaMineralUsoDaAgua.getTipoCaptacao().getIdeTipoCaptacao());
	}

	public boolean isCoordenadaSalva() {
		return !Util.isNull(prospecaoDetalhamento) && isLocalizacaoGeograficaSalva(prospecaoDetalhamento.getIdeLocalizacaoGeografica());
	}

	/**
	 * Método que verifica se o {@link MetodoProspeccao} é Poço.
	 * 
	 * @author eduardo.fernandes
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	public boolean isProspeccaoPoco() {
		return !Util.isNull(prospecaoDetalhamento)
				&& prospecaoDetalhamento.getProcessoDnpmProspecao().getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.POCOS.getId());
	}

	/**
	 * Método que verifica se o {@link MetodoProspeccao} é Trincheira.
	 * 
	 * @author eduardo.fernandes
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	public boolean isProspeccaoTrincheira() {
		return !Util.isNull(prospecaoDetalhamento)
				&& prospecaoDetalhamento.getProcessoDnpmProspecao().getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.TRINCHEIRAS.getId());
	}

	/**
	 * Método que verifica se o {@link MetodoProspeccao} é Furo de Sondagem.
	 * 
	 * @author eduardo.fernandes
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	public boolean isProspeccaoFuroSondagem() {
		return !Util.isNull(prospecaoDetalhamento)
				&& prospecaoDetalhamento.getProcessoDnpmProspecao().getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.SONDAGENS.getId());
	}

	public boolean isDocumentoEnviado(CadastroAtividadeNaoSujeitaLicDocApensado docApensado) {
		return !Util.isNullOuVazio(docApensado.getUrlDocumento());
	}
	
	public boolean isDocumentoEnviado(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc) {
		return !Util.isNullOuVazio(doc.getFileName());
	}

	public boolean isDocumentoToDownload(CadastroAtividadeNaoSujeitaLicDocApensado docApensado) {
		return !Util.isNullOuVazio(docApensado.getIdeDocumentoObrigatorio().getDscCaminhoArquivo());
	}

	/*
	 * ------------------------
	 * ---getters && setters---
	 * ------------------------
	 */
	private PessoaFisica getUsuarioRealizandoAcao() {
		return ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	}

	private CadastroAtividadeNaoSujeitaLic getCadastroAtividade() {
		return pesquisaMineral.getCadastroAtividadeNaoSujeitaLic();
	}

	public List<CadastroAtividadeNaoSujeitaLicDocApensado> getListaCadastroAtividadeNaoSujeitaLicDocApensado() {
		return getCadastroAtividade().getCadastroAtividadeNaoSujeitaLicDocApensados();
	}

	public String getHeaderProspeccao() {
		if (isProspeccaoFuroSondagem()) {
			return "do " + Util.getString("lbl_cpm_furo");
		}
		else if (isProspeccaoTrincheira()) {
			return "da " + Util.getString("lbl_cpm_trincheira");
		}
		else if (isProspeccaoPoco()) {
			return "do " + Util.getString("lbl_cpm_poco");
		}
		else {
			return "";
		}
	}

	public TelaDestinoEnum getTelaDestionEnum(){
		return TelaDestinoEnum.PESQUISA_MINERAL_SEM_GUIA;
	}

	public String getLabelTelefone(Telefone teleone){
		if(!TipoTelefoneEnum.FAX.getId().equals(teleone.getIdeTipoTelefone().getIdeTipoTelefone())){
			return "Tel ";
		}
		return "";
	}

	public SubstanciaMineral getSubstanciaMineralSelecionada() {
		SubstanciaMineralTipologia substanciaMineralTipologia = (SubstanciaMineralTipologia) getSelectedNode().getData();
		return substanciaMineralTipologia.getIdeSubstanciaMineral();
	}

	public int getSomenteShape() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}

	public int getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public Empreendimento getEmpreendimento() {
		return pesquisaMineral.getCadastroAtividadeNaoSujeitaLic().getIdeEmpreendimento();
	}

	public PessoaJuridica getRequerente() {
		return requerente;
	}

	public void setRequerente(PessoaJuridica requerente) {
		this.requerente = requerente;
	}

	public Endereco getEnderecoCorrespondencia() {
		return enderecoCorrespondencia;
	}

	public PesquisaMineralResponsavelTecnico getPesquisaMineralResponsavel() {
		return pesquisaMineralResponsavel;
	}

	public void setPesquisaMineralResponsavel(PesquisaMineralResponsavelTecnico pesquisaMineralResponsavel) {
		this.pesquisaMineralResponsavel = pesquisaMineralResponsavel;
	}

	public PesquisaMineral getPesquisaMineral() {
		return pesquisaMineral;
	}

	public void setPesquisaMineral(PesquisaMineral pesquisaMineral) {
		this.pesquisaMineral = pesquisaMineral;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return this.selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public PesquisaMineralSubstanciaMineral getPesquisaMineralSubstanciaMineral() {
		return pesquisaMineralSubstanciaMineral;
	}

	public void setPesquisaMineralSubstanciaMineral(PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral) {
		this.pesquisaMineralSubstanciaMineral = pesquisaMineralSubstanciaMineral;
	}

	public ProcessoDnpm getProcessoDnpm() {
		return processoDnpm;
	}

	public void setProcessoDnpm(ProcessoDnpm processoDnpm) {
		this.processoDnpm = processoDnpm;
	}

	public ProspecaoDetalhamento getProspecaoDetalhamento() {
		return prospecaoDetalhamento;
	}

	public void setProspecaoDetalhamento(ProspecaoDetalhamento prospecaoDetalhamento) {
		this.prospecaoDetalhamento = prospecaoDetalhamento;
	}

	public CadastroAtividadeNaoSujeitaLicDocApensado getDocApensado() {
		return docApensado;
	}

	public void setDocApensado(CadastroAtividadeNaoSujeitaLicDocApensado docApensado) {
		this.docApensado = docApensado;
	}

	public boolean isComprometimento() {
		return comprometimento;
	}

	public void setComprometimento(boolean comprometimento) {
		this.comprometimento = comprometimento;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

	public boolean isAceite() {
		return aceite;
	}

	public void setAceite(boolean aceite) {
		this.aceite = aceite;
	}

	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocIdentificacao() {
		return listaDocIdentificacao;
	}
	
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocRepresentacao() {
		return listaDocRepresentacao;
	}
	
	public CadastroAtividadeDocumentoIdentificacaoRepresentacao getDocRepresentacao() {
		return docRepresentacao;
	}

	public void setDocRepresentacao(CadastroAtividadeDocumentoIdentificacaoRepresentacao docRepresentacao) {
		this.docRepresentacao = docRepresentacao;
	}
}
