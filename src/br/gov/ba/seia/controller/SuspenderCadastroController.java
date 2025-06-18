package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.dto.AgrupadorDeListPorObjectDTO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.HistoricoSuspensaoCadastro;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.MotivoSuspensaoCadastro;
import br.gov.ba.seia.enumerator.AcaoEnum;
import br.gov.ba.seia.enumerator.FuncionalidadeEnum;
import br.gov.ba.seia.enumerator.SecaoEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.facade.HistMotivoSuspensaoFacade;
import br.gov.ba.seia.facade.HistSuspensaoCadastroFacade;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.MotivoSuspensaoCadastroFacade;
import br.gov.ba.seia.facade.PessoaFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.historico.suspensao.FiltroHistoricoSuspensao;
import br.gov.ba.seia.interfaces.AgrupadorInterface;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.json.JsonUtil;
import br.gov.ba.seia.util.security.SecurityController;

/**
 *  Controller de Supensão de Imoveis Rurais- CEFIR
 */
@Named("suspenderCadastroController")
@ViewScoped
public class SuspenderCadastroController implements Serializable{
	private static final long serialVersionUID = -2389644427644671437L;
	
	/**
	 * Facades
	 */
	@EJB
	protected MotivoSuspensaoCadastroFacade motivoSuspensaoCadastroFacade;
	
	@EJB
	protected HistSuspensaoCadastroFacade histSuspensaoCadastroFacade;
	
	@EJB
	protected HistMotivoSuspensaoFacade histMotivoSuspensaoFacade;
	
	@EJB
	private AuditoriaFacade auditoria;
	
	@EJB
	protected ImovelRuralFacade imovelRuralServiceFacade;
	
	@EJB
	protected PessoaFacade pessoaFacade;
	
	private ImovelRural imovelRural = new ImovelRural();
	private int activeIndex = 0;
	private boolean existeSuspensaoCadastro;
	private String observacao;
	private String auto;
	private boolean exibirSalvar;
	private List <MotivoSuspensaoCadastro> listaMotivos;
	private List<AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao>> listaHistoricoAgrupada;
	private FiltroHistoricoSuspensao pendenciaAtiva;
	private FiltroHistoricoSuspensao filtroHistorico;
	private HistoricoSuspensaoCadastro histSuspensaoCadastro;
	private Collection<MotivoSuspensaoCadastro> motivosSelecionados;
	private Date dataAtual = new Date();
	private Date dataInicio;
	private Date dataFim;
	private Boolean retirarPedencia;
	private boolean acessoVisualizar;
	private boolean acessoEditar;
	private boolean usuarioExterno;
	
	public void iniciar(){
		try {
			activeIndex = 0;
			existeSuspensaoCadastro = false;
			filtroHistorico = new FiltroHistoricoSuspensao();
			
			verificaAcessos();
			modoEdicaoOuVisualizacao();
			limpar();
			
			listaMotivos = motivoSuspensaoCadastroFacade.listarTodosMotivosSuspensao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void limpar() {
		retirarPedencia = null;
		auto = "";
		observacao = "";
		exibirSalvar = true;
		listaMotivos = new ArrayList<MotivoSuspensaoCadastro>();
		limparFiltroHistorico();
	}

	private void modoEdicaoOuVisualizacao() {
		try {
			histSuspensaoCadastro = new HistoricoSuspensaoCadastro();
			imovelRural = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());
			
			if(imovelRural.getIndSuspensao()!= null && imovelRural.getIndSuspensao()){
				existeSuspensaoCadastro = true;
				visualizarSuspensaoCadastroAtiva();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void verificaAcessos() {
		usuarioExterno = ContextoUtil.getContexto().isUsuarioExterno();
		
		SecurityController security = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
		
		if(security != null) {
			acessoVisualizar = true;
			acessoEditar = security.temAcesso(SecaoEnum.CADASTRO, FuncionalidadeEnum.SUSPENSAO_DE_CADASTRO, AcaoEnum.ALTERAR);
		}
	}
	
	public boolean isRenderizaAbaPendenciaCadastro() {
		
		if(acessoEditar && !existeSuspensaoCadastro)
			
			return true;
		else 
			
			return false;
	}
	
	public boolean isRenderizaAbaVisualizarPendenciaCadastro() {
		if((acessoVisualizar || acessoEditar) && existeSuspensaoCadastro) return true;
		else return false;
	}
	
	public boolean isRenderizaAbaHistoricoSuspensao() {
		if(acessoVisualizar || acessoEditar) return true;
		else return false;
	}
	
	/**
	 * Chamada de dos metodos que possuem as regras de cada aba. 
	 */
	public void controlarAbas(TabChangeEvent event) {
		if(activeIndex == 1){
			exibirSalvar = false;
		}else{
			exibirSalvar = true;
		}
	}
	
	public void salvar(){
		boolean auditar = false;
		try {
			imovelRural = imovelRuralServiceFacade.carregarTudo(this.imovelRural.getIdeImovelRural());
			imovelRural.setReservaLegal(imovelRuralServiceFacade.buscaReservaLegalByImovelRural(imovelRural));
			imovelRural.setVegetacaoNativa(imovelRuralServiceFacade.listarVegetacaoNativaByImovelRural(imovelRural));
			ImovelRural objAntigoImovel = new ImovelRural();
			objAntigoImovel = imovelRural.clone();
			
			if(imovelRural.getIndSuspensao()!= null && imovelRural.getIndSuspensao() && existeSuspensaoCadastro && retirarPedencia){
				histSuspensaoCadastro = new HistoricoSuspensaoCadastro();
				histSuspensaoCadastro = histSuspensaoCadastroFacade.obterHistoricoSuspensaoCadastroPorIdeImovelRural(imovelRural);
				histSuspensaoCadastro.setDtcRetiradaSuspensao(new Date());
				histSuspensaoCadastroFacade.salvarOuAtualizar(histSuspensaoCadastro);
				imovelRural.setIndSuspensao(false);
				imovelRuralServiceFacade.atualizarImovelRural(this.imovelRural);
				MensagemUtil.msg0002();
				auditar = true;
			}else if (!existeSuspensaoCadastro){
				if(validarMotivos() & validarCampos()){
						preencherHistoricoSuspensaoCadastro();
						//Salvar ou Atualizar Histórico Suspensão Cadastro
						histSuspensaoCadastroFacade.salvarOuAtualizar(histSuspensaoCadastro);
						//Salvaro notivo da Suspensão do cadastro
						histMotivoSuspensaoFacade.salvar(histSuspensaoCadastro,motivosSelecionados);
						//Atualizar o status do cadastro para o status “Pendente” 
						imovelRural.setIndSuspensao(true);
						imovelRuralServiceFacade.atualizarImovelRural(this.imovelRural);
						//Sincronizar SICAR
						this.imovelRuralServiceFacade.gerarCertificado(this.imovelRural);
						if (!Util.isNullOuVazio(imovelRural.getImovelRuralSicar()) && !Util.isNullOuVazio(imovelRural.getImovelRuralSicar().getIdeImovelRuralSicar())) {
							montarImovelRuralSicar();
							
							imovelRuralServiceFacade.atualizarImovelRuralSicar(imovelRural.getImovelRuralSicar());
						} else {
							imovelRural.setImovelRuralSicar(new ImovelRuralSicar());
							imovelRural.getImovelRuralSicar().setDtcCriacao(new Date());
							montarImovelRuralSicar();
							imovelRuralServiceFacade.salvarImovelRuralSicar(imovelRural.getImovelRuralSicar());
						}
						MensagemUtil.msg0001(); 
						auditar = true;
						
				} else return;
			}
			if(auditar) auditoria(objAntigoImovel);
			
			Html.atualizar("filtroImoveis");
			Html.esconder("dialogSuspensaoCadastro");
		} catch (Exception e) {
			MensagemUtil.erro("Houve um erro ao salvar.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void auditoria(ImovelRural objAntigoImovel) {
		auditoria.atualizar(objAntigoImovel, this.imovelRural);
		auditarHistoricoSuspensaoCadastro();
	}
	
	private void auditarHistoricoSuspensaoCadastro(){
		try {
			if(!existeSuspensaoCadastro && (retirarPedencia == null || !retirarPedencia)) {
				auditoria.salvar(this.histSuspensaoCadastro);
			}else {
				HistoricoSuspensaoCadastro histSuspensaoCadastroAntigo = histSuspensaoCadastroFacade.carregarTudo(this.histSuspensaoCadastro);
				auditoria.atualizar(histSuspensaoCadastroAntigo, this.histSuspensaoCadastro);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void preencherHistoricoSuspensaoCadastro() {
		try {
			motivosSelecionados = new ArrayList<MotivoSuspensaoCadastro>();
			histSuspensaoCadastro.setIdeImovelRural(imovelRural);
			histSuspensaoCadastro.setDscObservacao(observacao);
			histSuspensaoCadastro.setNumNotificacaoAuto(auto);
			histSuspensaoCadastro.setIdePessoaFisica(histSuspensaoCadastroFacade.obterPessoaLogada());
			histSuspensaoCadastro.setDtcSuspensaoCadastro(new Date());
			
			for (MotivoSuspensaoCadastro motivo : listaMotivos) {
				if(motivo.isValue()){
					motivosSelecionados.add(motivo);
				}
			}
			if(motivosSelecionados.isEmpty()){
				MensagemUtil.msg0003("Motivo"); 
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private boolean validarMotivos(){
		if(!listaMotivos.isEmpty()){
			for (MotivoSuspensaoCadastro motivo : listaMotivos) {
				if(motivo.isValue()){
					return true;
				}
			}
		} 
		MensagemUtil.msg0003("O Motivo "); 
		return false;
	}
	
	private boolean validarCampos(){
		boolean retorno = true;
		
		if(Util.isNullOuVazio(auto)){
			MensagemUtil.msg0003("Número da notificação ou auto "); 
			retorno = false;
		}
		
		if(Util.isNullOuVazio(observacao)){
			MensagemUtil.msg0003("Observação "); 
			retorno = false;
		}
		
		return retorno;
	}
	
	public void limparFiltroHistorico() {
		FiltroHistoricoSuspensao filtro = new FiltroHistoricoSuspensao();
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
		filtro.setDataInicio(calendar.getTime());
		filtro.setDataFim(new Date());
		
		setFiltroHistorico(filtro);
		listaHistoricoAgrupada = null;
		filtroHistorico = new FiltroHistoricoSuspensao();
	}
	
	public void obterHistoricos() {
		if(!Util.isNullOuVazio(getFiltroHistorico().getDataInicio())) {
			try {
				List<FiltroHistoricoSuspensao> lista = new ArrayList<FiltroHistoricoSuspensao>();
				lista = histSuspensaoCadastroFacade.filtrarHistoricoSuspensaoCadastro(getFiltroHistorico().getDataInicio(), getFiltroHistorico().getDataFim(), imovelRural, 0, 0);
				listaHistoricoAgrupada = agruparHistoricoSuspensaoCadastro(lista);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}else{
			MensagemUtil.msg0009();
		}
	}
	
	@SuppressWarnings(value = { "deprecation" })
	private List<AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao>> agruparHistoricoSuspensaoCadastro(List<FiltroHistoricoSuspensao> lista){
		
		List<Date> listaDatas = new ArrayList<Date>();
		List<AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao>> listaAgrupada = new ArrayList<AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao>>();

		//Forma a lista com todas as datas do histórico
		for (FiltroHistoricoSuspensao suspensao : lista) {
			Date d = new Date(new SimpleDateFormat("MM/dd/yyyy").format(suspensao.getDataSuspensao()));
			
			if(!listaDatas.contains(d)) listaDatas.add(d);
		}
		
		//Cria a lista agrupada somente com as datas
		for (Date data : listaDatas) {
			listaAgrupada.add(
					new AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao>(data, new ArrayList<AgrupadorInterface<FiltroHistoricoSuspensao>>()));
		}
		
		//Encaixa todos os registros na sua data
		for (FiltroHistoricoSuspensao suspensao : lista) {
			for (AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao> agrupado : listaAgrupada) {
				
				Date d = new Date(new SimpleDateFormat("MM/dd/yyyy").format(suspensao.getDataSuspensao()));
				
				if(((Date) agrupado.getObjetoAgrupador()).equals(d)) {
					agrupado.getListaASerAgrupada().add(suspensao);
				}
			}
		}
		
		for (AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao> agrupado : listaAgrupada) {
			agrupado.setListaASerAgrupada(
					agruparHistoricoPorMotivo(agrupado.getListaASerAgrupada()));
		}
		
		return listaAgrupada;
	}
	
	private List<AgrupadorInterface<FiltroHistoricoSuspensao>> agruparHistoricoPorMotivo(List<AgrupadorInterface<FiltroHistoricoSuspensao>> lista){
		
		List<AgrupadorInterface<FiltroHistoricoSuspensao>> retorno = new ArrayList<AgrupadorInterface<FiltroHistoricoSuspensao>>();
		List<Date> listaDatas = new ArrayList<Date>();
		
		for (AgrupadorInterface<FiltroHistoricoSuspensao> suspensao : lista) {
			
			Date d = suspensao.getObjetoAgrupado().getDataSuspensao();
			
			if(!listaDatas.contains(d)) {
				listaDatas.add(d);
				
				suspensao.getObjetoAgrupado().setListaMotivos(new ArrayList<MotivoSuspensaoCadastro>());
				retorno.add(suspensao);
			}
		}
		
		for (AgrupadorInterface<FiltroHistoricoSuspensao> hist : lista) {
			for (AgrupadorInterface<FiltroHistoricoSuspensao> obj : retorno) {
				
				if(hist.getObjetoAgrupado().getIdeSuspensaoCadastro().equals(obj.getObjetoAgrupado().getIdeSuspensaoCadastro())) {
					obj.getObjetoAgrupado().getListaMotivos().add(
							new MotivoSuspensaoCadastro(hist.getObjetoAgrupado().getMotivo()));
				} 
			}
		}
		
		return retorno;
	}
	
	public void visualizarSuspensaoCadastroAtiva() {
		try {
			imovelRural = imovelRuralServiceFacade.carregarTudo(this.imovelRural.getIdeImovelRural());
			if(!Util.isNullOuVazio(imovelRural.getIdeImovelRural())) {
				try {
					pendenciaAtiva = new FiltroHistoricoSuspensao();
					pendenciaAtiva = histSuspensaoCadastroFacade.visualizarSuspensaoCadastro(imovelRural);
					pendenciaAtiva.setPessoa(pessoaFacade.buscarPessoaFisica(pendenciaAtiva.getPessoa().getIdePessoaFisica()));
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}else{
				MensagemUtil.msg0009();
			}
		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
		}
	}
	
	private void carregarDadosGeoJsonSicar() throws Exception {
		List<Integer> localizacoes = new ArrayList<Integer>();
		GeoJsonSicar geoJsonSicar = new GeoJsonSicar();
		localizacoes.add(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
		
		if(geoJsonSicar != null){
			imovelRural.setGeoJsonSicar(geoJsonSicar);
			geoJsonSicar = new GeoJsonSicar();
		}
		localizacoes.clear();

		if (!Util.isNullOuVazio(imovelRural.getReservaLegal()) && !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())) {
			localizacoes.add(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			if(geoJsonSicar != null){
				imovelRural.getReservaLegal().setGeoJsonSicar(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}

		localizacoes.clear();

		if (imovelRural.getIndVegetacaoNativa() && !Util.isNullOuVazio(imovelRural.getVegetacaoNativa())) {
			localizacoes.add(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			if(geoJsonSicar != null){
				imovelRural.getVegetacaoNativa().setGeoJsonSicar(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}

		localizacoes.clear();

		if (!Util.isNullOuVazio(this.imovelRural.getIndAreaRuralConsolidada())
				&& this.imovelRural.getIndAreaRuralConsolidada()) {
			localizacoes.add(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			if(geoJsonSicar != null){
				imovelRural.getIdeAreaRuralConsolidada().setGeoJsonSicar(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}

		if (imovelRural.getIndApp() && !Util.isNullOuVazio(imovelRural.getAppCollection())) {
			List<Integer> localizacoesApp = new ArrayList<Integer>();
			for (App app : imovelRural.getAppCollection()) {
				localizacoes.clear();
				localizacoes.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
				
				if(geoJsonSicar != null){
					app.setGeoJsonSicar(geoJsonSicar);
					geoJsonSicar = new GeoJsonSicar();
				}
				
				localizacoesApp.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoesApp);
			if(geoJsonSicar != null){
				imovelRural.setGeoJsonSicarAppTotal(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}
	}
	
	private void montarImovelRuralSicar() {
		try {
			carregarDadosGeoJsonSicar();
			String numToken = "";
			if (imovelRural.isTermoCompromisso()) {
				List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId());
				numToken = "-" + lCefir.get(0).getNumToken();
				imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
			} else {
				List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.CEFIR.getId());
				if (!Util.isNullOuVazio(lCefir)) {
					numToken = "-" + lCefir.get(0).getNumToken();
					imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
				}
			}
			imovelRural.getImovelRuralSicar().setIdeImovelRural(imovelRural);
			imovelRural.getImovelRuralSicar().setNumProtocolo("BA-" + imovelRural.getIdeImovelRural() + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date())+ numToken);
			imovelRural.getImovelRuralSicar().setIndSicronia(false);
			imovelRural.getImovelRuralSicar().setDtcIniSicronia(null);
			imovelRural.getImovelRuralSicar().setDtcFimSicronia(null);
			imovelRural.getImovelRuralSicar().setToken(null);
			imovelRural.getImovelRuralSicar().setMsgRetornoSincronia(null);
			imovelRural.getImovelRuralSicar().setCodRetornoSincronia(null);
			imovelRural.getImovelRuralSicar().setUrlReciboInscricao(null);
			imovelRural.getImovelRuralSicar().setJson(JsonUtil.montarJsonImovelRuralSicar(this.imovelRural, this.imovelRural.getIdeImovelRuralRppn()).toString());
			
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao montar o imóvel rural.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public String getTextoBotaoSuspensaoCadastro() {
		if(ContextoUtil.getContexto().isUsuarioExterno()) return "Consulta pendência";
		else return "Pendência";
	}
	
	/**
	 * XXX: Getts e Setts
	 */
	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}
	
	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public List <MotivoSuspensaoCadastro> getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(List <MotivoSuspensaoCadastro> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public boolean isExibirSalvar() {
		return exibirSalvar;
	}

	public void setExibirSalvar(boolean exibirSalvar) {
		this.exibirSalvar = exibirSalvar;
	}

	public FiltroHistoricoSuspensao getFiltroHistorico() {
		return filtroHistorico;
	}

	public void setFiltroHistorico(FiltroHistoricoSuspensao filtroHistorico) {
		this.filtroHistorico = filtroHistorico;
	}

	public HistoricoSuspensaoCadastro getSuspensaoCadastros() {
		return histSuspensaoCadastro;
	}

	public void setSuspensaoCadastros(HistoricoSuspensaoCadastro suspensaoCadastro) {
		this.histSuspensaoCadastro = suspensaoCadastro;
	}

	public Collection<MotivoSuspensaoCadastro> getMotivosSelecionados() {
		return motivosSelecionados;
	}

	public void setMotivosSelecionados(Collection<MotivoSuspensaoCadastro> motivosSelecionados) {
		this.motivosSelecionados = motivosSelecionados;
	}
	
	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public FiltroHistoricoSuspensao getPendenciaAtiva() {
		return pendenciaAtiva;
	}

	public void setPendenciaAtiva(FiltroHistoricoSuspensao pendenciaAtiva) {
		this.pendenciaAtiva = pendenciaAtiva;
	}

	public Boolean getRetirarPedencia() {
		return retirarPedencia;
	}

	public void setRetirarPedencia(Boolean retirarPedencia) {
		this.retirarPedencia = retirarPedencia;
	}

	public boolean isAcessoVisualizar() {
		return acessoVisualizar;
	}

	public void setAcessoVisualizar(boolean acessoVisualizar) {
		this.acessoVisualizar = acessoVisualizar;
	}

	public boolean isAcessoEditar() {
		return acessoEditar;
	}

	public void setAcessoEditar(boolean acessoEditar) {
		this.acessoEditar = acessoEditar;
	}

	public boolean isExisteSuspensaoCadastro() {
		return existeSuspensaoCadastro;
	}

	public void setExisteSuspensaoCadastro(boolean existeSuspensaoCadastro) {
		this.existeSuspensaoCadastro = existeSuspensaoCadastro;
	}

	public List<AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao>> getListaHistoricoAgrupada() {
		return listaHistoricoAgrupada;
	}

	public void setListaHistoricoAgrupada(List<AgrupadorDeListPorObjectDTO<Date, FiltroHistoricoSuspensao>> listaHistoricoAgrupada) {
		this.listaHistoricoAgrupada = listaHistoricoAgrupada;
	}

	public boolean isUsuarioExterno() {
		return usuarioExterno;
	}

	public void setUsuarioExterno(boolean usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}
}
