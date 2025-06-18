package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoAtividadeTorre;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.TipoNaturezaTorre;
import br.gov.ba.seia.entity.TipoUnidadeConservacaoTorre;
import br.gov.ba.seia.entity.TorreAnemometrica;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.TelaDestinoEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.enumerator.TipoImovelEnum;
import br.gov.ba.seia.facade.CadastroAtividadeFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.facade.TorresAnemometricasFacede;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia Classe:
 * TorresAnenometricasController.java Projeto: seia Pacote:
 * br.gov.ba.seia.controller
 * 
 * @autor: diegoraian em 19 de set de 2017 Objetivo:
 */
@Named(value = "torresAnemometricasController")
@ViewScoped
public class TorresAnemometricasController{

	/**
	 * Propriedade: facade
	 * 
	 * @type: TorresAnemometricasFacede
	 */
	@EJB
	private TorresAnemometricasFacede torresAnemometricasFacede;
	
	@EJB
	private CadastroAtividadeFacade cadastroAtividadeFacade; 
	
	@EJB
	private LocalizacaoGeograficaServiceFacade facadeLocalizacaoGeografica;
	
	private int activeTab;
	private TorreAnemometrica torreAnemometrica;
	private CadastroAtividadeNaoSujeitaLic cadastro;
	private Collection<Empreendimento> empreendimentos = new ArrayList<Empreendimento>();
	private Collection<ResponsavelEmpreendimento> responsavelEmpreendimento = new ArrayList<ResponsavelEmpreendimento>();
	private Collection<TipoUnidadeConservacaoTorre> tiposUnidadeConservacao;
	private Collection<TipoNaturezaTorre> itemsNaturezaTorre;
	private Collection<LocalizacaoAtividadeTorre> localizacaoAtividadesTorres;
	private Empreendimento empreendimento;
	private List<TorreAnemometrica> torres = new ArrayList<TorreAnemometrica>();
	private Imovel imovel;
	private String numSicar;

	private Pessoa pessoaRequerente;
	private PessoaJuridica pessoaJuridicaRequerente;
	private List<LocalizacaoGeografica> listaLocalizacao = new ArrayList<LocalizacaoGeografica>();
	private boolean visualizarTorre;
	private boolean aceiteDeclaracao;
	private Collection<Imovel> listaImoveisCadastro = new ArrayList<Imovel>();
	private Collection<Imovel> listaImoveisEmpreendimento = new ArrayList<Imovel>();
	private String campoEscondido;
	private Imovel imovelExclusao;
	private boolean naturezaPermanente;
	
	@PostConstruct
	public void init() {
		try {
			
			if(isEdicao() || isVisualizacao()){
				carregarCadastroAtividade();
				carregarRequerente();
			}else{
				carregarRequerente();
				carregarCadastroAtividade();
			}
			
			inicializarTorreModal();
			carregarNaturezaTorres();
			carregarTipoUnidadeConservacao();
			carregarAtividadesTorres();
			carregarTorresAnemometricas();
			carregarImoveis();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregarImoveis() throws Exception {
		listaImoveisCadastro = torresAnemometricasFacede.carregarImoveisCadastro(cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
		listaImoveisEmpreendimento = torresAnemometricasFacede.carregarImoveisEmpreendimento(cadastro.getIdeEmpreendimento());
	}

	public Collection<Imovel> getListaImoveis(){
		List<Imovel> retorno = new ArrayList<Imovel>();
		retorno.addAll(listaImoveisEmpreendimento);
		retorno.addAll(listaImoveisCadastro);
		return retorno;
	}
	
	public Boolean getExisteImovelRural(){
		
		if(!Util.isNullOuVazio(this.listaImoveisEmpreendimento)){
			for(Imovel imovel : this.listaImoveisEmpreendimento){
				
				if(TipoImovelEnum.RURAL.getId().equals(imovel.getIdeTipoImovel().getIdeTipoImovel())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Boolean validarTorreAnemometricaCadastrada(){
		
		boolean retorno = true;
		
		if(Util.isNullOuVazio(torreAnemometrica.getNomTorreAnemometrica())){
			JsfUtil.addErrorMessage("Identificação é de preenchimento obrigatório!");
			retorno = false;
		}
		
		if(Util.isNullOuVazio(torreAnemometrica.getValAlturaTorre())){
			JsfUtil.addErrorMessage("Altura é de preenchimento obrigatório!");
			retorno = false;
		}
		
		if(Util.isNullOuVazio(torreAnemometrica.getAreaSupressaoVegetal())){
			JsfUtil.addErrorMessage("Área de supressão vegatal é de preenchimento obrigatório!");
			retorno = false;
		}
		
		if(Util.isNullOuVazio(listaLocalizacao)){
			JsfUtil.addErrorMessage("Localização Geográfica é de preenchimento obrigatório!");
			retorno = false;
		}
		
		if(Util.isNullOuVazio(torreAnemometrica.getIdeTipoNaturezaTorre())){
			JsfUtil.addErrorMessage("Natureza Torre é de preenchimento obrigatório!");
			retorno = false;
		}
		
		if(torreAnemometrica.getIdeTipoNaturezaTorre().getIdeTipoNaturezaTorre() != 2){
			if(Util.isNullOuVazio(torreAnemometrica.getValProjecaoMonitoramento())){
				JsfUtil.addErrorMessage("Projeção de Monitoramento é de preenchimento obrigatório!");
				retorno = false;
			}
		}
		if(Util.isNullOuVazio(torreAnemometrica.getIndAtividade())){
			JsfUtil.addErrorMessage("A atividade está situada em unidade de conservação é de preenchimento obrigatório!");
			retorno = false;
		}
		
		if(!Util.isNullOuVazio(torreAnemometrica.getIndAtividade()) && torreAnemometrica.getIndAtividade()){
			if(Util.isNullOuVazio(torreAnemometrica.getListaLocalizacaoAtividadeTorres())){
				JsfUtil.addErrorMessage("Selecione a atividade situada em unidade de conservação!");
				retorno = false;
			}
		}
		
		if(getatividadeSituadaEmAreaDeConservacao()){
			
			if(Util.isNullOuVazio(torreAnemometrica.getNomUnidadeConservadora())){
				JsfUtil.addErrorMessage("Informe o nome da UC é de preenchimento obrigatório!");
				retorno = false;
			}
			
			if(Util.isNullOuVazio(torreAnemometrica.getIdeTipoUnidadeConservacaoTorre())){
				JsfUtil.addErrorMessage("Qual o tipo da Unidade de conservação é de preenchimento obrigatório!");
				retorno = false;
			}
		}
		
		if(Util.isNullOuVazio(torreAnemometrica.getIndProcessoInema())){
			JsfUtil.addErrorMessage("Possui processo em tramite ou concluído no INEMA relacionado a atividade é de preenchimento obrigatório!");
			retorno = false;
		}
		
		if(!Util.isNullOuVazio(torreAnemometrica.getIndProcessoInema()) && torreAnemometrica.getIndProcessoInema()){
			if(Util.isNullOuVazio(torreAnemometrica.getNumProcessoInema())){
				JsfUtil.addErrorMessage("Número do processo é de preenchimento obrigatório!");
				retorno = false;
			}
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public void onChangeUnidade(ValueChangeEvent event){
		
		Long IDE_LOCALIZACAO_ATIVIDADE_TORRE_UNIDADE_CONSERVACAO = 1L; 
		
		List<LocalizacaoAtividadeTorre> unidadeOld = (List<LocalizacaoAtividadeTorre>)event.getOldValue();
		
		List<LocalizacaoAtividadeTorre> unidadeNew = (List<LocalizacaoAtividadeTorre>)event.getNewValue();
		
		boolean isUnidadeOld = false;
		
		boolean isUnidadeNew = false;
		
		if(!Util.isNullOuVazio(unidadeNew)){
			
			for(LocalizacaoAtividadeTorre loc : unidadeNew){
				
				if(IDE_LOCALIZACAO_ATIVIDADE_TORRE_UNIDADE_CONSERVACAO == loc.getIdeLocalizacaoAtividadeTorre()){
					
					isUnidadeNew = true;
				}
			}
		}
		
		if(!Util.isNullOuVazio(unidadeOld)){
			
			for(LocalizacaoAtividadeTorre loc : unidadeOld){
				
				if(IDE_LOCALIZACAO_ATIVIDADE_TORRE_UNIDADE_CONSERVACAO == loc.getIdeLocalizacaoAtividadeTorre()){
					
					isUnidadeOld = true;
				}
			}
		}
		
		if(isUnidadeOld && !isUnidadeNew){
			
			torreAnemometrica.setIdeTipoUnidadeConservacaoTorre(null);
			torreAnemometrica.setNomUnidadeConservadora(null);
			
			Html.atualizar("form_dialog_cadastro_torre:panelAreaConservacao");
		}
		
		if(!isUnidadeOld && isUnidadeNew){
			
			Html.atualizar("form_dialog_cadastro_torre:panelAreaConservacao");
		}
	}
	
	private void carregarCadastroAtividade() throws Exception {
		
		if(isEdicao() || isVisualizacao()){
			cadastro = cadastroAtividadeFacade.buscarCadastroPorIdeCadastro(ContextoUtil.getContexto().getCadastro().getIdeCadastroAtividadeNaoSujeitaLic());
		}else{
			cadastro = cadastroAtividadeFacade.buscarCadastroIncompletoPorPessoaEmpreendimento(pessoaRequerente, empreendimento, TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES);
		}
		
		if(this.cadastro == null || this.cadastro.getIdeCadastroAtividadeNaoSujeitaLic() == null){
			this.cadastro = new CadastroAtividadeNaoSujeitaLic(getUsuarioRealizandoAcao(), pessoaRequerente, 
					TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES);
		}else{
			carregarResponsaveisSelecionados();
		}
		setEmpreendimento(cadastro.getIdeEmpreendimento());
		
	}

	private void carregarResponsaveisSelecionados() throws Exception {
		responsavelEmpreendimento = torresAnemometricasFacede.listarRespEmpreendimentoPorCadastroAtividadeNaoSujeitaLic(
				cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
		
		for(ResponsavelEmpreendimento responsavel : cadastro.getIdeEmpreendimento().getResponsavelEmpreendimentoCollection()){
			responsavel.setSelecionado(responsavelEmpreendimento.contains(responsavel));
		}
	}

	public void inicializarTorreModal() {
		this.torreAnemometrica = new TorreAnemometrica();
		this.torreAnemometrica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		this.torreAnemometrica.setIdeCadastroAtividadeNaoSugeitaLic(cadastro);
	}

	/**
	 * Carregar Atividades das torres
	 * 
	 * @author diegoraian
	 */
	private void carregarAtividadesTorres() {
		try {
			this.localizacaoAtividadesTorres = torresAnemometricasFacede.listarLocalizacoesAtividadeTorre();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Carregar Unidades de conservação
	 * 
	 * @author diegoraian
	 */
	private void carregarTipoUnidadeConservacao() {
		try {
			this.tiposUnidadeConservacao = torresAnemometricasFacede.listarTiposUnidadeConservacaoTorre();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Carrega as naturezas das torres
	 * 
	 * @author diegoraian
	 */
	public void carregarNaturezaTorres() {
		try {
			this.itemsNaturezaTorre = torresAnemometricasFacede.listarTiposNaturezaTorre();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Carrega os dados do requerente
	 * 
	 * @author diegoraian
	 */
	private void carregarRequerente() {
		try {
			
			if(isEdicao() || isVisualizacao()){
				pessoaRequerente = cadastro.getIdePessoaRequerente();
				pessoaJuridicaRequerente = cadastro.getIdePessoaRequerente().getPessoaJuridica();
			}else {
				this.pessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
				this.pessoaJuridicaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica();
				
			}

			torresAnemometricasFacede.obterTelefoneEnderecoPessoa(pessoaRequerente);

			if (pessoaJuridicaRequerente != null) {
				this.pessoaJuridicaRequerente = torresAnemometricasFacede.obterPessoaJuridicaMontada(pessoaJuridicaRequerente.getIdePessoaJuridica());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * Retorna o usuário logado
	 * 
	 * @return
	 * @author diegoraian
	 */
	private PessoaFisica getUsuarioRealizandoAcao() {
		return ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	}

	/**
	 * Avança para a próxima aba
	 * 
	 * @author diegoraian
	 */
	public void avancarAba() {
		try {
			if (validarAba()) {
				if(!isVisualizacao()){
					cadastroAtividadeFacade.salvarCadastroAtividadeComStatusIncompleto(cadastro, responsavelEmpreendimento, listaImoveisCadastro);
				}
				this.activeTab++;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void avancarClicandoNaAba(TabChangeEvent event){
		try {
			if(validarAba()){
				if(!isVisualizacao()){
					cadastroAtividadeFacade.salvarCadastroAtividadeComStatusIncompleto(cadastro, responsavelEmpreendimento, listaImoveisCadastro);
				}
			}else{
				this.activeTab = 0;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Voltar para a aba anterior
	 * 
	 * @author diegoraian
	 */
	public void voltarAba() {
		this.activeTab--;
	}

	/**
	 * Inclui um novo imóvel rural
	 * 
	 * @author diegoraian
	 */
	public void incluirImovelRural() {
		if (listaImoveisCadastro.contains(getImovel()) || listaImoveisEmpreendimento.contains(getImovel())) {
			JsfUtil.addErrorMessage("Imóvel já incluído");
			rc().addPartialUpdateTarget("dialogIncluirImovelRural.hide()");
		} else {
			if (!Util.isNull(getImovel()) && !Util.isNull(getImovel().getIdeImovel())) {
				listaImoveisCadastro.add(getImovel());
				rc().addPartialUpdateTarget("formRmpreendimentos:tabViewCadastroTorres:dataTableImoveisRurais");
			}
		}
	}

	/**
	 * Detalha os dados dos representantes técnicos selecionados
	 * 
	 * @param responsavel
	 * @author diegoraian
	 */
	public void detalharRepresentanteTecnico(ResponsavelEmpreendimento responsavel) {
		if (responsavel.isSelecionado()) {
			getResponsavelEmpreendimento().add(responsavel);
		} else {
			if (getResponsavelEmpreendimento().contains(responsavel)) {
				getResponsavelEmpreendimento().remove(responsavel);
			}
		}
	}

	/**
	 * Consulta usada na lupa do modal de incluir imóvel rural e a partir do
	 * código do CAR
	 * 
	 * @author diegoraian
	 */
	public void pesquisarImovelRural() {
		try {
			if (Util.isNullOuVazio(getNumSicar())) {
				JsfUtil.addErrorMessage("Insira o código do CAR");

			} else {
				Imovel imovelR = torresAnemometricasFacede.buscarImovelPorCar(getNumSicar());

				if (!Util.isNullOuVazio(imovelR)) {
					setImovel(imovelR);
				} else {
					JsfUtil.addWarnMessage("Imóvel não encontrado");
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Limpar dialog com as informações sempre que fechar o dialog
	 * 
	 * @author diegoraian
	 */
	public void limparDialogIncluirImovel() {
		setImovel(null);
		setNumSicar("");
	}

	/**
	 * Após retornar do modal de selecionar empreendimento esse método incluí no
	 * objeto de cadastro e nas tabelas que serão utilizadas no cadastro as
	 * informações de empreendimento
	 * 
	 * @param empreendimento
	 * @throws Exception
	 * @author diegoraian
	 */
	public void salvarEmpreendimento(Empreendimento empreendimento) throws Exception {
		this.cadastro.setIdeEmpreendimento(empreendimento);
        setEmpreendimento(empreendimento);
		cadastro.setIdeEmpreendimento(empreendimento);
		carregarResponsaveisTecnicos();
	}

	public void salvarTramitacao() throws Exception {
		CadastroAtividadeNaoSujeitaLic cadastro = new CadastroAtividadeNaoSujeitaLic(pessoaRequerente.getPessoaFisica(), 
				pessoaRequerente, TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES);
		cadastro.setIdeEmpreendimento(getEmpreendimento());
		torresAnemometricasFacede.tramitarCadastroIncompleto(cadastro);
	}

	public void carregarResponsaveisTecnicos() throws Exception {
		try {
			this.cadastro.getIdeEmpreendimento().setResponsavelEmpreendimentoCollection(torresAnemometricasFacede.listarResponsaveisTecnicos(this.cadastro.getIdeEmpreendimento()));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")	+ " o(s) responsável(eis) técnico(s) do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public TelaDestinoEnum getTelaDestinoEnum() {
		return TelaDestinoEnum.TORRES_ANEMOMETRICCAS;
	}

	public boolean isEmpreendimentoSelecionado() {
		return !Util.isNullOuVazio(cadastro.getIdeEmpreendimento());
	}

	/**
	 * Método que retorna o contexto JSF atual
	 * 
	 * @return
	 * @author diegoraian
	 */
	private RequestContext rc() {
		return RequestContext.getCurrentInstance();
	}

	/**
	 *
	 * @return
	 * @author diegoraian
	 * @throws Exception
	 */

	private boolean validarRN403() {
		if (Util.isNullOuVazio(getEmpreendimento())) {
			return false;
		} else {
			if (!Util.isNullOuVazio(cadastro.getIndPossuiCefir())) {
				if (cadastro.getIndPossuiCefir() && !Util.isNullOuVazio(getEmpreendimento().getImovelCollection()) 
						&& getEmpreendimento().getImovelCollection().isEmpty()) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Valida os dados da aba de dados básicos sempre que é clicado no botão
	 * avançar
	 * 
	 * @return
	 * @throws Exception
	 * @author diegoraian
	 */
	private boolean validarAba() throws Exception {
		
		if(Util.isNull(this.cadastro.getIdeEmpreendimento())){
			JsfUtil.addWarnMessage("Selecione um empreendimento");
			return false;
		}
		
		if(Util.isNullOuVazio(responsavelEmpreendimento)){
			JsfUtil.addWarnMessage("Selecione um responsável técnico");
			return false;
		}

		if (!validarRN403()) {
			MensagemUtil.msg0024();
			return false;
		}
		
		if (!Util.isNullOuVazio(cadastro.getIndPossuiCefir())){
			if(cadastro.getIndPossuiCefir() && Util.isNullOuVazio(listaImoveisCadastro) && Util.isNullOuVazio(listaImoveisEmpreendimento)){
				JsfUtil.addWarnMessage("Selecione um Imóvel Rural");
				return false;
			}
		}
		return true;
	}

	public void onChangeSelect(ValueChangeEvent event){
		
		if(!Util.isNullOuVazio(torreAnemometrica.getListaLocalizacaoAtividadeTorres())){
			
			torreAnemometrica.getListaLocalizacaoAtividadeTorres().clear();
		}
		torreAnemometrica.setNomUnidadeConservadora(null);
		torreAnemometrica.setIdeTipoUnidadeConservacaoTorre(null);
	}
	
	public void onCloseDialog(CloseEvent event){
		carregarTorresAnemometricas();
		
		Html.atualizar("dialogCadastroTorreAnemometrica");
	}
	
	public void limparNumeroProcesso(ValueChangeEvent event){
		torreAnemometrica.setNumProcessoInema(null);
	}
	
	public void salvarTorre() {
		try {
			
			if(validarTorreAnemometricaCadastrada()){
				
				boolean nova = torreAnemometrica.getIdeTorreAnemometrica() == null; 
				torresAnemometricasFacede.salvarTorreAnemometrica(torreAnemometrica);
				inicializarTorreModal();
				carregarTorresAnemometricas();
				if(nova){
					MensagemUtil.msg0001();
				}else{
					MensagemUtil.msg0002();
				}
				RequestContext.getCurrentInstance().execute("dialogCadastroTorreAnemometrica.hide()");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar torre anemométrica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public boolean getatividadeSituadaEmAreaDeConservacao() {
		if (torreAnemometrica.getListaLocalizacaoAtividadeTorres() != null) {
			
			for(LocalizacaoAtividadeTorre loc : torreAnemometrica.getListaLocalizacaoAtividadeTorres()){
				return "Unidade de Conservação".equals(loc
						.getNomLocalizacaoAtividadeTorre());
					
			}
		}
		return false;
	}
	
	public void carregarTorresAnemometricas(){
		try {
			setTorres(torresAnemometricasFacede.carregarTorresPorCadastroAtividade(cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar as torres anemométricas");
		}
	}
	
	public void excluirTorre(){
		try {
			torresAnemometricasFacede.excluirTorreAnemometrica(torreAnemometrica);
			carregarTorresAnemometricas();
			JsfUtil.addSuccessMessage(Util.getString("MSG-005"));
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao excluir a torre anemométrica");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void atualizarTorre(){
		try {
			torresAnemometricasFacede.atualizarTrorreAnemometrica(torreAnemometrica);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao atualizar a torre anemométrica");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public String abreviarStringComReticencias(String nome, int tamanho){
		if(nome == null || nome.isEmpty()){
			return "";
		}
		if(nome.length() <= tamanho){
			return nome;
		}
		return nome.substring(0, tamanho+1).concat("...");
	}
	
	public String getLatitude(LocalizacaoGeografica localizacaoGeografica) {
		String latitude = "--";
		if(!Util.isNullOuVazio(localizacaoGeografica.getDadoGeograficoCollection())){
			for(DadoGeografico dadoGeografico : localizacaoGeografica.getDadoGeograficoCollection()){
				Map<String, String> pontos = facadeLocalizacaoGeografica.getPonto(dadoGeografico);
				if (!Util.isNullOuVazio(pontos)){
					latitude = pontos.get("latitude");
					break;
				} 
			}
		}
		return latitude;
	}

	public String getLongitude(LocalizacaoGeografica localizacaoGeografica ) {
		String longitude = "--";
		if(!Util.isNullOuVazio(localizacaoGeografica.getDadoGeograficoCollection())){
			for(DadoGeografico dadoGeografico : localizacaoGeografica.getDadoGeograficoCollection()){
				Map<String, String> pontos = facadeLocalizacaoGeografica.getPonto(dadoGeografico);
				if (!Util.isNullOuVazio(pontos)){
					longitude = pontos.get("longitude");
					break;
				}
			}
		}
		return longitude;
	}
	
	public String realizarAceiteDeclaracaoRepresentante(){
		if(aceiteDeclaracao){
			try {
				cadastroAtividadeFacade.tramitarCadastroTorreParaConcluido(cadastro);
				JsfUtil.addSuccessMessage("");
				ContextoUtil.getContexto().setUpdateMessage("O cadastro nº " + cadastro.getNumCadastro() + ", foi salvo com sucesso!");
				ContextoUtil.getContexto().setSucessMessage(true);
				return "/paginas/manter-atividade-nao-sujeita-licenciamento/consulta.xhtml?faces-redirect=true";
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}else{
			JsfUtil.addWarnMessage("É necessário aceitar na declaração");
		}
		return "";
	}
	
	public boolean isEdicao(){
		return ContextoUtil.getContexto().getCadastro() != null && ContextoUtil.getContexto().getCadastro().isEdicao();
	}
	
	public boolean isVisualizacao(){
		return ContextoUtil.getContexto().getCadastro() != null && ContextoUtil.getContexto().getCadastro().isVisualizar();
	}
	
	public void removerDadosDoEmpreendimentoAnterior() throws Exception{
		cadastroAtividadeFacade.removerDadosEmpreendimentoCadastro(cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
		carregarResponsaveisSelecionados();
		carregarImoveis();
	}
	
	public void validarFinalizacao(){
		if(Util.isNullOuVazio(torres)){
			JsfUtil.addErrorMessage("É necessário adicionar pelo menos uma Torre Anemométrica para finalizar o cadastro.");
		}else{
			RequestContext.getCurrentInstance().execute("dialogDeclaracaoRepresentanteLegal.show();");
		}
	}
	
	public boolean podeExcluir(Imovel imovel){
		return listaImoveisCadastro.contains(imovel);
	}
	
	public void excluirImovel(){
		try {
			cadastroAtividadeFacade.excluirImovelCadastro(imovelExclusao.getImovelRural(), cadastro);
			listaImoveisCadastro.remove(imovelExclusao);
			JsfUtil.addSuccessMessage(Util.getString("MSG-005"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void verificarNatureza(){
		if(torreAnemometrica.getIdeTipoNaturezaTorre().getIdeTipoNaturezaTorre() == 2){
			naturezaPermanente = true;
			torreAnemometrica.setValProjecaoMonitoramento(null);
		}else{
			naturezaPermanente = false;
		}
		
		Html.atualizar("form_dialog_cadastro_torre:projecaoMonitoramento");
	}
	
	public int getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}
	
	public Pessoa getPessoaRequerente() {
		return pessoaRequerente;
	}

	public void setPessoaRequerente(Pessoa pessoaRequerente) {
		this.pessoaRequerente = pessoaRequerente;
	}

	public boolean isRequerentePessoaJuridica() {
		return pessoaJuridicaRequerente != null;
	}

	public PessoaJuridica getPessoaJuridicaRequerente() {
		return pessoaJuridicaRequerente;
	}

	public void setPessoaJuridicaRequerente(
			PessoaJuridica pessoaJuridicaRequerente) {
		this.pessoaJuridicaRequerente = pessoaJuridicaRequerente;
	}

	public List<LocalizacaoGeografica> getListaLocalizacao() {
		listaLocalizacao.clear();
		if (!Util.isNullOuVazio(torreAnemometrica.getIdeLocalizacaoGeografica())) {
			listaLocalizacao.add(torreAnemometrica.getIdeLocalizacaoGeografica());
		}else if(Util.isNull(torreAnemometrica.getIdeLocalizacaoGeografica())){
			torreAnemometrica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		return listaLocalizacao;
	}

	public void setListaLocalizacao(List<LocalizacaoGeografica> listaLocalizacao) {
		this.listaLocalizacao = listaLocalizacao;
	}

	public TorresAnemometricasFacede getFacade() {
		return torresAnemometricasFacede;
	}

	public void setFacade(TorresAnemometricasFacede facade) {
		this.torresAnemometricasFacede = facade;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public TorreAnemometrica getTorreAnemometrica() {
		return torreAnemometrica;
	}

	public void setTorreAnemometrica(TorreAnemometrica torreAnemometrica) {
		this.torreAnemometrica = torreAnemometrica;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

	public Collection<Empreendimento> getEmpreendimentos() {
		return empreendimentos;
	}

	public void setEmpreendimentos(Collection<Empreendimento> empreendimentos) {
		this.empreendimentos = empreendimentos;
	}

	public Collection<ResponsavelEmpreendimento> getResponsavelEmpreendimento() {
		return responsavelEmpreendimento;
	}

	public void setResponsavelEmpreendimento(
			Collection<ResponsavelEmpreendimento> responsavelEmpreendimento) {
		this.responsavelEmpreendimento = responsavelEmpreendimento;
	}

	public Collection<TipoUnidadeConservacaoTorre> getTiposUnidadeConservacao() {
		return tiposUnidadeConservacao;
	}

	public void setTiposUnidadeConservacao(
			Collection<TipoUnidadeConservacaoTorre> tiposUnidadeConservacao) {
		this.tiposUnidadeConservacao = tiposUnidadeConservacao;
	}

	public Collection<TipoNaturezaTorre> getItemsNaturezaTorre() {
		return itemsNaturezaTorre;
	}

	public void setItemsNaturezaTorre(
			Collection<TipoNaturezaTorre> itemsNaturezaTorre) {
		this.itemsNaturezaTorre = itemsNaturezaTorre;
	}

	public Collection<LocalizacaoAtividadeTorre> getLocalizacaoAtividadesTorres() {
		return localizacaoAtividadesTorres;
	}

	public void setLocalizacaoAtividadesTorres(
			Collection<LocalizacaoAtividadeTorre> localizacaoAtividadesTorres) {
		this.localizacaoAtividadesTorres = localizacaoAtividadesTorres;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public List<TorreAnemometrica> getTorres() {
		return torres;
	}

	public void setTorres(List<TorreAnemometrica> torres) {
		this.torres = torres;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public String getNumSicar() {
		return numSicar;
	}

	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}

	public boolean getAceiteDeclaracao() {
		return aceiteDeclaracao;
	}

	public void setAceiteDeclaracao(boolean aceiteDeclaracao) {
		this.aceiteDeclaracao = aceiteDeclaracao;
	}

	public boolean isVisualizarTorre() {
		return visualizarTorre;
	}

	public void setVisualizarTorre(boolean visualizarTorre) {
		this.visualizarTorre = visualizarTorre;
	}

	public String getCampoEscondido() {
		return campoEscondido;
	}

	public void setCampoEscondido(String campoEscondido) {
		this.campoEscondido = campoEscondido;
	}

	public Collection<Imovel> getListaImoveisCadastro() {
		return listaImoveisCadastro;
	}

	public void setListaImoveisCadastro(Collection<Imovel> listaImoveisCadastro) {
		this.listaImoveisCadastro = listaImoveisCadastro;
	}

	public Collection<Imovel> getListaImoveisEmpreendimento() {
		return listaImoveisEmpreendimento;
	}

	public void setListaImoveisEmpreendimento(
			Collection<Imovel> listaImoveisEmpreendimento) {
		this.listaImoveisEmpreendimento = listaImoveisEmpreendimento;
	}

	public Imovel getImovelExclusao() {
		return imovelExclusao;
	}

	public void setImovelExclusao(Imovel imovelExclusao) {
		this.imovelExclusao = imovelExclusao;
	}

	public boolean isNaturezaPermanente() {
		return naturezaPermanente;
	}

	public void setNaturezaPermanente(boolean naturezaPermanente) {
		this.naturezaPermanente = naturezaPermanente;
	}
}
