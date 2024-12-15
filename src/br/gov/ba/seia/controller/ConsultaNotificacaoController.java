package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.joda.time.DateTime;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.StatusNotificacao;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.facade.ConsultarNotificacaoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("consultaNotificacaoController")
@ViewScoped
public class ConsultaNotificacaoController {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	private static Integer sim = 1;
	private static Integer nao = 0;
	private static Integer nula = -1;
	private String numNotificacao;
	private String numProcesso;
	private Date periodoInicio;
	private Date periodoFim;
	private Integer respondida = -1;
	private Collection<MotivoNotificacao> listaMotivoNotificacao;
	private MotivoNotificacao motivoNotificacao;
	private Notificacao notificacao;
	private Notificacao notificacaoSelecionada;
	private Boolean usuarioExterno = Boolean.FALSE;
	private List<StatusNotificacao> listaStatusNotificacao;
	private List<StatusNotificacao> filtroStatusNotificacao;
	private LazyDataModel<Notificacao> lNotificacoes;
	private Collection<Area> listaDeAreas;
	private Area areaSelecionada;
	
	@EJB
	private ConsultarNotificacaoServiceFacade consultarNotificacaoServiceFacade;

	@PostConstruct
	public void init() {
		usuarioExterno = ContextoUtil.getContexto().isUsuarioExterno();
		listaStatusNotificacao = StatusNotificacao.listarTodos();
		listaDeAreas = consultarNotificacaoServiceFacade.listarCoordenacoes();
	}
	
	public void responderNotificacao(ActionEvent e) {
		try {
			carregarNotificacao(e);
			Html.exibir("dialogDownloadDocumentosDaNotificacao");
		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
			throw Util.capturarException(e1, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarNotificacao(ActionEvent e)  {
		
		LstDocumentosDaNotificacaoController lstDocumentosDaNotificacaoController = 
				(LstDocumentosDaNotificacaoController) SessaoUtil.recuperarManagedBean(
						"#{lstDocumentosDaNotificacaoController}", LstDocumentosDaNotificacaoController.class);
		
		ApensarDocumentoController apensarDocumentoController = 
				(ApensarDocumentoController) SessaoUtil.recuperarManagedBean(
						"#{apensarDocumentoController}", ApensarDocumentoController.class);
		
		LocalizacaoGeograficaAllController localizacaoGeograficaAllController = 
				(LocalizacaoGeograficaAllController) SessaoUtil.recuperarManagedBean(
						"#{localizacaoGeograficaAllController}", LocalizacaoGeograficaAllController.class);
		
		Notificacao notificacao = (Notificacao) e.getComponent().getAttributes().get("notificacao");
		
		localizacaoGeograficaAllController.setShape(true);
		
		lstDocumentosDaNotificacaoController.setNotificacao(notificacao);
		lstDocumentosDaNotificacaoController.carregarDocumentosDaNotificacao();
		
		apensarDocumentoController.setNotificacao(notificacao);
		apensarDocumentoController.loadSemExibirDialog(e);
		
		apensarDocumentoController.limpaArquivosPreviosNaoSalvos();
	}
	
	private Map<String,Object> getParams(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(!Util.isNullOuVazio(numNotificacao)){
			params.put("numNotificacao", numNotificacao);
		}
		if(!Util.isNullOuVazio(numProcesso)){
			params.put("numProcesso", numProcesso);
		}
		if(!Util.isNullOuVazio(periodoInicio)){
			params.put("periodoInicio", periodoInicio);
		}
		if(!Util.isNullOuVazio(periodoFim)){
			params.put("periodoFim", periodoFim);
		}
		if(!Util.isNullOuVazio(motivoNotificacao)){
			params.put("ideMotivoNotificacao", motivoNotificacao.getIdeMotivoNotificacao());
		}
		if(!Util.isNullOuVazio(areaSelecionada)){
			params.put("ideArea", areaSelecionada);
		}
		if(respondida != nula ){
			if (respondida == sim) {
				params.put("respondida", true );
			}
			else {
				params.put("respondida",  false);
			}
			
		}
		if(filtroStatusNotificacao!=null){
			params.put("filtroPesquisa", filtroStatusNotificacao);
		}
		return params;
	}
	
	
	private Integer countNotificacao(){
		try{
			Integer count = consultarNotificacaoServiceFacade.countNotificacao(getParams());
			if(count != null){
				return count;
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);			
		}
		return 0;
	}
	
	public StreamedContent getGerarDetalhesNotificacaoPdf() {
		
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_notificacao", notificacaoSelecionada.getIdeNotificacao());
			
			if(notificacaoSelecionada.getTipo()==1){
				RelatorioUtil lRelatorio = new RelatorioUtil("detalhesNotificacao.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);				
			}
			else if(notificacaoSelecionada.getTipo()==2){
				RelatorioUtil lRelatorio = new RelatorioUtil("detalhesNotificacaoComunicacao.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);	
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}

		return null;
	}

	public void limpar(){
		areaSelecionada= null;
		numNotificacao = null;
		numProcesso = null;
		periodoInicio = null;
		periodoFim = null;		
		lNotificacoes = null;
		respondida = -1;
		motivoNotificacao = null;
		filtroStatusNotificacao = new ArrayList<StatusNotificacao>();
	}
	
	public boolean isRespostaNotificacaoRendered(Notificacao notificacao) {
		return usuarioExterno && notificacao.isNotificacaoPrazo() && notificacao.getIndRetorno() == null;
		
	}

	private boolean validate() {
		if(!Util.validaPeriodo(getPeriodoInicio(), getPeriodoFim())) {
			JsfUtil.addErrorMessage(BUNDLE.getString("geral_msg_periodo_invalido"));
			return false;
		}
		return true;
	}

	public Collection<MotivoNotificacao> getMotivosNotificacao() {
		if (listaMotivoNotificacao == null) {
			consultarMotivosNotificacao();
		}
		return listaMotivoNotificacao;
	}

	private void consultarMotivosNotificacao() {
		try {
			listaMotivoNotificacao = consultarNotificacaoServiceFacade.listarMotivoNotificacao();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void consultarNotificacao() {
		
		lNotificacoes = new LazyDataModel<Notificacao>() {
			
			private static final long serialVersionUID = -8587274025801384971L;
			
			@Override
			public List<Notificacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				try{
					if(validate()){
						return (List<Notificacao>) consultarNotificacaoServiceFacade.filtrarlistaNotificacao(first, pageSize, getParams());
					} else {
						return Collections.emptyList();
					}
				} catch(Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		};
		
		lNotificacoes.setRowCount(countNotificacao());
	}
	
	public String statusNumNotificacao(Notificacao notificacao){
		return consultarNotificacaoServiceFacade.statusNumNotificacao(notificacao);
	}
	
	public MotivoNotificacao getMotivoNotificacao() {
		return motivoNotificacao;
	}

	public void setMotivoNotificacao(MotivoNotificacao motivoNotificacao) {
		this.motivoNotificacao = motivoNotificacao;
	}

	public String getNumNotificacao() {
		return numNotificacao;
	}

	public void setNumNotificacao(String numNotificacao) {
		this.numNotificacao = numNotificacao;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		if (!Util.isNullOuVazio(periodoFim)) {
			DateTime dt = new DateTime(periodoFim);
			this.periodoFim = dt.plusHours(23).plusMinutes(59).plusSeconds(59).toDate();
		}
	}

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public Notificacao getNotificacaoSelecionada() {
		return notificacaoSelecionada;
	}

	public void setNotificacaoSelecionada(Notificacao notificacaoSelecionada) {
		this.notificacaoSelecionada = notificacaoSelecionada;
	}

	public Boolean getUsuarioExterno() {
		return usuarioExterno;
	}

	public void setUsuarioExterno(Boolean usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}

	public List<StatusNotificacao> getListaStatusNotificacao() {
		return listaStatusNotificacao;
	}

	public void setListaStatusNotificacao(List<StatusNotificacao> listaStatusNotificacao) {
		this.listaStatusNotificacao = listaStatusNotificacao;
	}

	public List<StatusNotificacao> getFiltroStatusNotificacao() {
		return filtroStatusNotificacao;
	}

	public void setFiltroStatusNotificacao(List<StatusNotificacao> filtroStatusNotificacao) {
		this.filtroStatusNotificacao = filtroStatusNotificacao;
	}

	public Integer getRespondida() {
		return respondida;
	}

	public void setRespondida(Integer respondida) {
		this.respondida = respondida;
	}

	public static Integer getNAO() {
		return nao;
	}

	public static void setNAO(Integer nAO) {
		nao = nAO;
	}

	public void setlNotificacoes(LazyDataModel<Notificacao> lNotificacoes) {
		this.lNotificacoes = lNotificacoes;
	}

	public LazyDataModel<Notificacao> getlNotificacoes() {
		return lNotificacoes;
	}

	public Collection<Area> getListaDeAreas() {
		return listaDeAreas;
	}

	public void setListaDeAreas(Collection<Area> listaDeAreas) {
		this.listaDeAreas = listaDeAreas;
	}

	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

}