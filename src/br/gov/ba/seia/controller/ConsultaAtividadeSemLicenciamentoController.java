package br.gov.ba.seia.controller;

import static br.gov.ba.seia.util.Html.redirecionarPagina;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicTipoStatus;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.TelaDestinoEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.facade.ConsultaAtividadeSemLicenciamentoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 31/10/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
 */
@Named("consultaAtividadeSemLicenciamentoController")
@ViewScoped
public class ConsultaAtividadeSemLicenciamentoController {
	
	@EJB
	private ConsultaAtividadeSemLicenciamentoFacade facade;
	
	// INI - Parâmetros da Consulta
	private Pessoa requerente;
	private String numeroCadastro;
	private TipoAtividadeNaoSujeitaLicenciamento ativiade;
	private List<TipoAtividadeNaoSujeitaLicenciamento> listaAtividade;
	private CadastroAtividadeNaoSujeitaLicTipoStatus status;
	private List<CadastroAtividadeNaoSujeitaLicTipoStatus> listaStatus;
	private Date periodoInicioFiltro;
	private Date periodoFimFiltro;
	private Municipio localidade;
	private List<Municipio> listaLocalidade;
	private String nomeEmpreendimento;
	// FIM - Parâmetros da Consulta
	
	private CadastroAtividadeNaoSujeitaLic cadastroBusca;
	private DataTable dataTableCadastros;
	private LazyDataModel<CadastroAtividadeNaoSujeitaLic> dataModelCadastro;
	
	private MetodoUtil metodoSelecionarSolicitante;

	@PostConstruct
	public void init(){
		try {
			metodoSelecionarSolicitante = new MetodoUtil(this, this.getClass().getDeclaredMethod("receberSolicitante", Pessoa.class));
			carregarListas();

			ContextoUtil.getContexto().setTipoSolicitante(null);
			ContextoUtil.getContexto().setCadastro(null);
			ContextoUtil.getContexto().getReqPapeisDTO().setRequerente(new RequerimentoPessoa());
			ContextoUtil.getContexto().setTelaParaRedirecionar(null);

			// 8429
			if(Util.isNullOuVazio(dataModelCadastro) && isUsuarioExterno()){
				carregarPaginacao();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void receberSolicitante(Pessoa pessoa){
		this.requerente = pessoa;
		Html.atualizar("formAtividadesSemLicenciamento:requerenteFiltro");
	}
	
	/**
	 * Método que irá carregar os combos na tela de Consultar "Atividades não sujeitas a Licenciamento Ambiental".
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	private void carregarListas() {
		carregarListaAtividade();
		carregarListaStatus();
		carregarListaLocalidade();
		
		
	}

	/**
	 * Método que irá carregar <i> this.listaLocalidade </i> ( {@link Municipio} ).
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	private void carregarListaLocalidade() {
		try {
			listaLocalidade = facade.listarLocalidades();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Localidades.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	/**
	 * Método que irá carregar <i> this.listaStatus </i> ( {@link CadastroAtividadeNaoSujeitaLicTipoStatus} ).
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	private void carregarListaStatus() {
		try {
			listaStatus = facade.listarTipoStatus();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Status.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	/**
	 * Método que irá carregar <i> this.listaAtividade </i> ( {@link CadastroAtividadeNaoSujeitaLicTipoStatus} ).
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	private void carregarListaAtividade() {
		try {
			listaAtividade = facade.listarAtividadesSemLicenciamentoAmbiental();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Atividades.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	/**
	 * Regra para obrigatoriedade de seleção de ao menos um filtro
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8418">#8418</a>
	 * @return
	 */
	private boolean validar() {
		if(!isUsuarioExterno()){
			boolean valido = true;
			if(!Util.isNull(requerente) 
					|| !Util.isNullOuVazio(numeroCadastro) 
					|| !Util.isNull(ativiade) 
					|| !Util.isNull(status) 
					|| !Util.isNull(periodoInicioFiltro)
					|| !Util.isNull(localidade)
					|| !Util.isNullOuVazio(nomeEmpreendimento)
					){
				if(!validarDatas()){
					valido = false;
					MensagemLacFceUtil.exibirInformacao074();
				}
			} 
			else if(Util.isNull(periodoInicioFiltro) && !Util.isNull(periodoFimFiltro)){
				valido = false;
				JsfUtil.addErrorMessage("O período informado não é válido.");
			} else {
				valido = false;
				MensagemLacFceUtil.exibirInformacao075();
			}
			return valido;
		}
		return true;
	}

	/**
	 * Validação dos campos de Período
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8417">#8417</a>
	 * @return
	 */
	private boolean validarDatas() {
		if(!Util.isNull(periodoInicioFiltro) && !Util.isNull(periodoFimFiltro)){
			return Util.validarDuasDatas(periodoInicioFiltro, periodoFimFiltro);
		}
		return true;
	}

	public String novo() {
		ContextoUtil.getContexto().setTipoRequerimento(null);
		ContextoUtil.getContexto().setCadastro(null);
		Util.updateAttributeSession("isSilos", true);
		return TelaDestinoEnum.IDENTIFICAR_PAPEL.getUrlTela() + "?faces-redirect=true";
	}
	
	public String novaAtividadeSemLicenciamento() {
		String url = "";
		
		url = PaginaEnum.IDENTIFICACAO_PAPEL_ATIVIDADE_SEM_LICENCIAMENTO.getUrl();
		url = url + "?faces-redirect=true";
		
		return url;
	}
	
	public void consultar(){
		dataTableCadastros.setFirst(0);
		dataTableCadastros.setPage(1);
		if (validar()) {
			carregarPaginacao();
		}
	}
	
	public void visualizar(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		cadastroAtividadeNaoSujeitaLic.setVisualizar(true);
		cadastroAtividadeNaoSujeitaLic.setEdicao(false);
		ContextoUtil.getContexto().setCadastro(cadastroAtividadeNaoSujeitaLic);
		redirecionarParaPaginaDoCadastro(cadastroAtividadeNaoSujeitaLic.getTipoAtividadeNaoSujeitaLicenciamento());
	}

	public void editar(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		cadastroAtividadeNaoSujeitaLic.setVisualizar(false);
		cadastroAtividadeNaoSujeitaLic.setEdicao(true);
		ContextoUtil.getContexto().setCadastro(cadastroAtividadeNaoSujeitaLic);
		redirecionarParaPaginaDoCadastro(cadastroAtividadeNaoSujeitaLic.getTipoAtividadeNaoSujeitaLicenciamento());
	}

	/**
	 * Método que redireciona o {@link CadastroAtividadeNaoSujeitaLic} para a tela adequada de acordo com o {@link TipoAtividadeNaoSujeitaLicenciamento}.
	 * 
	 * @author eduardo.fernandes
	 * @param tipoAtividadeNaoSujeitaLicenciamento
	 * @since 12/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @throws IOException
	 */
	private void redirecionarParaPaginaDoCadastro(TipoAtividadeNaoSujeitaLicenciamento tipoAtividadeNaoSujeitaLicenciamento) {
		if (tipoAtividadeNaoSujeitaLicenciamento.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS.getIde())) {
			redirecionarPagina(TelaDestinoEnum.CADASTRO_OLEO_GAS.getUrlTela());
		}
		else if (tipoAtividadeNaoSujeitaLicenciamento.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL.getIde())) {
			redirecionarPagina(TelaDestinoEnum.PESQUISA_MINERAL_SEM_GUIA.getUrlTela());
		}
		else if(tipoAtividadeNaoSujeitaLicenciamento.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS.getIde())){
			redirecionarPagina(TelaDestinoEnum.SILOS_ARMAZENS.getUrlTela());
		}
		else if(tipoAtividadeNaoSujeitaLicenciamento.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES.getIde())){
			redirecionarPagina(TelaDestinoEnum.TORRES_ANEMOMETRICCAS.getUrlTela());
		}
	}

	public void excluir() {
		try {
			cadastroBusca.setIndExcluido(true);
			facade.salvarCadastro(cadastroBusca);
			JsfUtil.addSuccessMessage("Exclusão realizada com sucesso");
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " o cadastro selecionado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	private Map<String, Object> montarParametros() {

		Map<String, Object> params = new HashMap<String, Object>();
		
		if (ContextoUtil.getContexto().isUsuarioExterno()) {
			List<Integer> listaPessoas = facade.listarPessoasQuePodemSerConsultadasPeloUsuario();
			params.put("listaPessoas", listaPessoas);
		}
		params.put("requerente", requerente);
		params.put("numeroCadastro", numeroCadastro);
		params.put("atividade", ativiade);
		params.put("cadStatus", status);
		params.put("periodoInicio", periodoInicioFiltro);
		params.put("periodoFim", periodoFimFiltro);
		params.put("localidade", localidade);
		params.put("nomEmpreendimento", nomeEmpreendimento);
		params.put("usuarioLogado", ContextoUtil.getContexto().getUsuarioLogado());
		params.put("isUsuarioExterno", ContextoUtil.getContexto().isUsuarioExterno());
		params.put("ideUsuarioLogado", ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());

		return params;

	}

	private void carregarPaginacao() {

		dataModelCadastro = new LazyDataModel<CadastroAtividadeNaoSujeitaLic>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<CadastroAtividadeNaoSujeitaLic> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {

				List<CadastroAtividadeNaoSujeitaLic> cadastros = null;

				try {

					Map<String, Object> map = montarParametros();
					map.put("first", first);
					map.put("pageSize", pageSize);
					cadastros = facade.listarPorCriteriaDemanda(map);

				}
				catch (Exception e) {
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}

				return cadastros;
			}
		};

		dataModelCadastro.setRowCount(getCount());

	}

	/**
	 * @author eduardo.fernandes 
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private Integer getCount() {
		try {
			Map<String, Object> map = montarParametros();
			return facade.consultarCount(map);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a quantidade de registros.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public void limpar(){
		requerente = null;
		numeroCadastro = null;
		ativiade = null;
		status = null;
		periodoInicioFiltro = null;
		periodoFimFiltro = null;
		localidade = null;
		nomeEmpreendimento = null;
		dataModelCadastro = null;
		dataTableCadastros = null;
	}
	
	public StreamedContent getImprimirRelatorio(CadastroAtividadeNaoSujeitaLic cadastro) {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ide_cadastro", cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
			parametros.put("TIPO_ATIVIDADE",TipoAtividadeNaoSujeitaLicenciamentoEnum.getEnum(
					cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento()));
			
			parametros.put("NOME_RELATORIO","relatorio.jasper");

			return new RelatorioUtil(parametros).gerar();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public StreamedContent getImprimirCertificado(CadastroAtividadeNaoSujeitaLic cadastro) {

		try {
			if(!existeCertificado(cadastro)){
				gerarCertificado(cadastro);
			}
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ide_cadastro", cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
			parametros.put("TIPO_ATIVIDADE",TipoAtividadeNaoSujeitaLicenciamentoEnum.getEnum(
					cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento()));
			
			parametros.put("NOME_RELATORIO","certificado.jasper");
		
			return new RelatorioUtil(parametros).gerar();
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_certificado") + " do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
		
	}
	
	/**
	 * Método de criação de um {@link Certificado} para o {@link CadastroAtividadeNaoSujeitaLic}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 22/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8192">#8192</a>
	 * @param cadastro
	 * @throws Exception 
	 */
	private void gerarCertificado(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception {
		facade.salvarCertificado(cadastro);
	}

	private boolean existeCertificado(CadastroAtividadeNaoSujeitaLic cadastro){
		return !Util.isNullOuVazio(cadastro.getIdeCertificado());
	}

	/**
	 * Método para gerar uma requisição AJAX.
	 * 
	 * @author eduardo.fernandes 
	 */
	public void poll() {
		RequestContext.getCurrentInstance().execute("consultaCadastroPoll.stop();");

		String msg = ContextoUtil.getContexto().getUpdateMessage();
		Boolean sucesso = ContextoUtil.getContexto().getSucessMessage();
		if(!Util.isNullOuVazio(msg)){
			if(sucesso){
				MensagemUtil.sucesso(msg);
			} 
			else {
				MensagemUtil.alerta(msg);
			}
		}

		ContextoUtil.getContexto().setUpdateMessage(null);
		ContextoUtil.getContexto().setSucessMessage(null);
	}
	
	/*
	 * flags
	 */
	private boolean existeRequerente(){
		return !Util.isNull(requerente);
	}
	
	public boolean isPossivelVisualizar(CadastroAtividadeNaoSujeitaLic cadastro) {
		return !cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_INCOMPLETO.getIde());
	}

	public boolean isPossivelEditar(CadastroAtividadeNaoSujeitaLic cadastro) {
		return !cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.AGUARDANDO_VALIDACAO.getIde())
				&& !cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.SENDO_VALIDADO.getIde())
				&& !cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO.getIde());
	}

	public boolean isPossivelExcluir(CadastroAtividadeNaoSujeitaLic cadastro) {
		return !isPossivelVisualizar(cadastro);
	}

	public boolean isPossivelValidar(CadastroAtividadeNaoSujeitaLic cadastro) {
		return cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.AGUARDANDO_VALIDACAO.getIde())
				|| cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.SENDO_VALIDADO.getIde())
				|| cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.PENDENCIA_VALIDACAO.getIde());
	}

	public boolean isPossivelImprimirRelatorio(CadastroAtividadeNaoSujeitaLic cadastro) {
		return isPossivelVisualizar(cadastro);
	}

	public boolean isPossivelImprimirCertificado(CadastroAtividadeNaoSujeitaLic cadastro) {
		return cadastro.getTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO.getIde());
	}

	private boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno();
	}
	
	/**
	 * Retorna verdadeiro se o usuário logado tiver perfil de atendente
	 * @return
	 */
	private boolean isUsuarioAtendente() {
		
		return (PerfilEnum.ATENDENTE.getId().equals(ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil()));
	}
	
	/*
	 * getters && setters
	 */
	public String getNome(){
		if(existeRequerente()){
			if(requerente.isPF()){
				return requerente.getPessoaFisica().getNomPessoa();
			} 
			else if(requerente.isPJ()){
				return requerente.getPessoaJuridica().getNomRazaoSocial();
			}
		}
		return "";
	}
	
	public PaginaEnum getTelaDestionEnum(){
		return PaginaEnum.CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO;
	}
	
	public String getDataMinima(){
		return "01/11/2016";
	}
	public String getDatMaxima(){
		return Util.getDataHoje();
	}
	public Pessoa getRequerente() {
		return requerente;
	}
	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}
	public String getNumeroCadastro() {
		return numeroCadastro;
	}
	public void setNumeroCadastro(String numeroCadastro) {
		this.numeroCadastro = numeroCadastro;
	}
	public TipoAtividadeNaoSujeitaLicenciamento getAtiviade() {
		return ativiade;
	}
	public void setAtiviade(TipoAtividadeNaoSujeitaLicenciamento ativiade) {
		this.ativiade = ativiade;
	}
	public CadastroAtividadeNaoSujeitaLicTipoStatus getStatus() {
		return status;
	}
	public void setStatus(CadastroAtividadeNaoSujeitaLicTipoStatus status) {
		this.status = status;
	}
	public Date getPeriodoInicioFiltro() {
		return periodoInicioFiltro;
	}
	public void setPeriodoInicioFiltro(Date periodoInicioFiltro) {
		this.periodoInicioFiltro = periodoInicioFiltro;
	}
	public Date getPeriodoFimFiltro() {
		return periodoFimFiltro;
	}
	public void setPeriodoFimFiltro(Date periodoFimFiltro) {
		this.periodoFimFiltro = periodoFimFiltro;
	}
	public Municipio getLocalidade() {
		return localidade;
	}
	public void setLocalidade(Municipio localidade) {
		this.localidade = localidade;
	}
	public String getNomeEmpreendimento() {
		return nomeEmpreendimento;
	}
	public void setNomeEmpreendimento(String nomeEmpreendimento) {
		this.nomeEmpreendimento = nomeEmpreendimento;
	}
	public List<TipoAtividadeNaoSujeitaLicenciamento> getListaAtividade() {
		return listaAtividade;
	}
	public List<CadastroAtividadeNaoSujeitaLicTipoStatus> getListaStatus() {
		return listaStatus;
	}
	public List<Municipio> getListaLocalidade() {
		return listaLocalidade;
	}

	public DataTable getDataTableCadastros() {
		return dataTableCadastros;
	}

	public LazyDataModel<CadastroAtividadeNaoSujeitaLic> getDataModelCadastro() {
		return dataModelCadastro;
	}

	public void setDataTableCadastros(DataTable dataTableCadastros) {
		this.dataTableCadastros = dataTableCadastros;
	}

	public void setDataModelCadastro(LazyDataModel<CadastroAtividadeNaoSujeitaLic> dataModelCadastro) {
		this.dataModelCadastro = dataModelCadastro;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastroBusca() {
		return cadastroBusca;
	}

	public void setCadastroBusca(CadastroAtividadeNaoSujeitaLic cadastroBusca) {
		this.cadastroBusca = cadastroBusca;
	}

	public MetodoUtil getMetodoSelecionarSolicitante() {
		return metodoSelecionarSolicitante;
	}

	public void setMetodoSelecionarSolicitante(
			MetodoUtil metodoSelecionarSolicitante) {
		this.metodoSelecionarSolicitante = metodoSelecionarSolicitante;
	}
	
}
