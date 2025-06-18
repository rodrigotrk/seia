package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.RelatorioQuantitativoDAOImpl;
import br.gov.ba.seia.dto.RelatorioQuantitativoDeProcessoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.StatusFluxoService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Named("relatorioQuantitativoController")
@ViewScoped
public class RelatorioQuantitativoController  {
	
	private Municipio municipioSelecionado;
	private StatusFluxo statusFluxoSelecionado;
	private TipoAto tipoAtoSelecionado;
	private AtoAmbiental atoAmbientalSelecionado;
	private Tipologia tipologiaSelecionada;
	private TipoFinalidadeUsoAgua finalidadeSelecionada;
	private Area diretoriaSelecionada;
	private Area areaSelecionada;	
	private String strBuscaMunicipio;
	
	private Date periodoDeFormacaoDE;
	private Date periodoDeFormacaoATE;	
	private Date periodoStatusDE;
	private Date periodoStatusATE;
	
	private List<StatusFluxo> listaStatusFluxo;
	private List<TipoAto> listaTipoAto;
	private List<AtoAmbiental> listaAtoAmbiental;
	private List<Tipologia> listaTipologia;
	private List<TipoFinalidadeUsoAgua> listaFinalidade;
	private List<Municipio> listaMunicipio;
	private List<Area> listaDiretoria;
	private List<Area> listaArea;

	private Tipologia tipologiaDivisao;
	private List<Tipologia> listaTipologiaDivisao;
	private Tipologia tipologiaAtividade;
	private List<Tipologia> listaTipologiaAtividade;
	
	@EJB
	private AreaService areaService;
	@EJB
	private TipoAtoService tipoAtoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private StatusFluxoService statusFluxoService;
	@EJB
	private MunicipioService municipioService;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	private RelatorioQuantitativoDAOImpl relatorioQuantitativoDAOImpl;
	
	public List<Area> getListaDiretoria() {
		try{
			if(listaDiretoria==null){
				listaDiretoria = (List<Area>) areaService.listaDiretoriasArea();
			}
			return listaDiretoria;
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public List<Area> getListaArea() {
		try{
			listaArea = (List<Area>) areaService.listarAreasFilhasOrderByAsc(diretoriaSelecionada);
			return listaArea;
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	public List<StatusFluxo> getListaStatusProcesso() {
		try{
			if(listaStatusFluxo==null){
				listaStatusFluxo = (List<StatusFluxo>) statusFluxoService.listarTodosStatusProcessoOrderByAsc();
			}
			return listaStatusFluxo;
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public List<TipoAto> getListaTipoAto() {
		try{
			if(listaTipoAto==null){
				listaTipoAto = (List<TipoAto>) tipoAtoService.listarTiposAtoOrderByAsc();
			}
			return listaTipoAto;
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public List<AtoAmbiental> getListaAtoAmbiental() {
		try{
			listaAtoAmbiental = (List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalOrderByAsc(tipoAtoSelecionado);
			return listaAtoAmbiental;
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void buscarMunicipio(){
		try{
			if(strBuscaMunicipio!=null && !strBuscaMunicipio.equals("")){
				Municipio m = new Municipio();
				m.setNomMunicipio(strBuscaMunicipio);
				listaMunicipio = (List<Municipio>) municipioService.filtrarListaMunicipios(m);		
			}
			else{
				JsfUtil.addWarnMessage("O campo localidade está vazio. Informe um valor.");
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public StreamedContent getRelatorioQuantitativoDeProcesso() {		
		try {
			Map<String, Object> parametrosParaExibicaoNoRelatorioQuantitativo = montarParametrosParaExibicaoNoRelatorioQuantitativo();
			
			List<RelatorioQuantitativoDeProcessoDTO> conteudoDoRelatorioQuantitativo = 
					(List<RelatorioQuantitativoDeProcessoDTO>) relatorioQuantitativoDAOImpl.listaDadosDoRelatorioQuantitativo(
							montarParametrosParaConsultaDoRelatorioQuantitativo());
			
			RelatorioUtil lRelatorio = new RelatorioUtil("relatorioQuantitativoAto.jasper", parametrosParaExibicaoNoRelatorioQuantitativo);				
			
			return lRelatorio.gerarRelatorioComDataSource(RelatorioUtil.RELATORIO_PDF,false, conteudoDoRelatorioQuantitativo);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Não foi possível gerar o relatório. Tente novamente.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}		
	}

	private Map<String, Object> montarParametrosParaExibicaoNoRelatorioQuantitativo() {
		
		try {
			Integer totalDeProcessoAnalisados = relatorioQuantitativoDAOImpl.totalDeProcessoAnalisados(montarParametrosParaConsultaDoRelatorioQuantitativo());
			Map<String,Object> parametrosDoRelatorio = new HashMap<String, Object>();		
			parametrosDoRelatorio.put("periodo_formacao", getParametroPeriodoDeFormacao());
			parametrosDoRelatorio.put("status", getParametroStatus());
			parametrosDoRelatorio.put("periodo_status", getParametroPeriodoDoStatus());
			parametrosDoRelatorio.put("diretotia", getParametroDiretoria());
			parametrosDoRelatorio.put("area", getParametroArea());
			parametrosDoRelatorio.put("municipio", getParametroMunicipio());
			parametrosDoRelatorio.put("categoria_ato", getParametroCategoriaAto());
			parametrosDoRelatorio.put("ato_ambiental", getParametroAtoAmbiental());
			parametrosDoRelatorio.put("tipologia", getParametroTipologia());
			parametrosDoRelatorio.put("finalidade", getParametroFinalidade());
			parametrosDoRelatorio.put("divisao", getParametroDivisao());
			parametrosDoRelatorio.put("atividade", getParametroAtividade());
			parametrosDoRelatorio.put("total_processos", totalDeProcessoAnalisados);
			return parametrosDoRelatorio;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private Map<String, Object> montarParametrosParaConsultaDoRelatorioQuantitativo() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("municipioSelecionado",municipioSelecionado);
		params.put("statusFluxoSelecionado",statusFluxoSelecionado);
		params.put("tipoAtoSelecionado",tipoAtoSelecionado);
		params.put("atoAmbientalSelecionado",atoAmbientalSelecionado);
		params.put("tipologiaSelecionada",tipologiaSelecionada);
		params.put("finalidadeSelecionada",finalidadeSelecionada);
		params.put("diretoriaSelecionada",diretoriaSelecionada);
		params.put("areaSelecionada",areaSelecionada);	
		params.put("periodoDeFormacaoDE",periodoDeFormacaoDE);
		params.put("periodoDeFormacaoATE",periodoDeFormacaoATE);	
		params.put("periodoStatusDE",periodoStatusDE);
		params.put("periodoStatusATE",periodoStatusATE);
		
		getParametroConsultaAtividade(params);
		
		return params;
	}
	
	private void getParametroConsultaAtividade(Map<String, Object> params) {
		if(!Util.isNull(tipologiaAtividade)) {
			params.put("atividade", tipologiaAtividade);
			
		} else if(tipologiaDivisao != null) {
			List<Integer> listaIdeTipologiaAtividade = new ArrayList<Integer>();
			
			for(Tipologia t : listaTipologiaAtividade) {
				listaIdeTipologiaAtividade.add(t.getIdeTipologia());
			}
			
			params.put("listaAtividades", listaIdeTipologiaAtividade);				
		}
	}

	private String getParametroPeriodoDeFormacao(){
		if(periodoDeFormacaoDE!=null && periodoDeFormacaoATE!=null){
			return  Util.formatData(periodoDeFormacaoDE) + " até " + Util.formatData(periodoDeFormacaoATE);
		}
		return null;
	}
	
	private String getParametroPeriodoDoStatus(){
		if(periodoStatusDE!=null && periodoStatusATE!=null){
			return  Util.formatData(periodoStatusDE) + " até " + Util.formatData(periodoStatusATE);
		}
		return null;
	}
	
	private String getParametroArea() {
		if(areaSelecionada!=null){
			Exception erro = null;
			try{
				areaSelecionada = areaService.carregar(areaSelecionada);
				return areaSelecionada.getNomArea(); 
			}
			catch(Exception e){
				erro = e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return "Todos";
	}
	
	private String getParametroMunicipio() {
		if(municipioSelecionado!=null){
			return municipioSelecionado.getNomMunicipio();
		}
		return null;
	}
	
	private String getParametroTipologia() {		
		if(tipologiaSelecionada != null){
			try{
				tipologiaSelecionada = tipologiaService.carregarTipologiaPorIde(tipologiaSelecionada.getIdeTipologia());
				return tipologiaSelecionada.getDesTipologia();
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
		}
		return "Todos";
	}
	
	private String getParametroFinalidade() {		
		if(finalidadeSelecionada != null){
			try{
				finalidadeSelecionada = tipoFinalidadeUsoAguaService.carregar(finalidadeSelecionada.getIdeTipoFinalidadeUsoAgua());
				return finalidadeSelecionada.getNomTipoFinalidadeUsoAgua();
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
		}
		return "Todos";
	}
	
	private String getParametroCategoriaAto() {		
		if(tipoAtoSelecionado != null){
			try{
				tipoAtoSelecionado = tipoAtoService.carregar(tipoAtoSelecionado.getIdeTipoAto());
				return tipoAtoSelecionado.getNomTipoAto();
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
		}
		return "Todos";
	}
	
	private String getParametroAtoAmbiental() {		
		if(atoAmbientalSelecionado != null){
			try{
				atoAmbientalSelecionado = atoAmbientalService.carregarById(atoAmbientalSelecionado.getIdeAtoAmbiental());
				return atoAmbientalSelecionado.getNomAtoAmbiental();
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}			
		}
		return "Todos";
	}

	private String getParametroStatus() {		
		if(statusFluxoSelecionado != null){
			try{
				statusFluxoSelecionado = statusFluxoService.carregar(statusFluxoSelecionado.getIdeStatusFluxo());
				return statusFluxoSelecionado.getDscStatusFluxo();
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
		}
		return "Todos";
	}
	
	private String getParametroDiretoria(){
		if(diretoriaSelecionada!=null){
			try{
				diretoriaSelecionada = areaService.carregar(diretoriaSelecionada);
				return diretoriaSelecionada.getNomArea(); 
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}			
		}
		return "Todos";
	}

	private String getParametroDivisao() {		
		if(tipologiaDivisao != null){
			try{
				tipologiaDivisao = tipologiaService.carregarTipologiaPorIde(tipologiaDivisao.getIdeTipologia());
				return tipologiaDivisao.getDesTipologia();
				
			} catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
		}
		
		return "Todos";
	}

	private String getParametroAtividade() {
		if(tipologiaAtividade != null){
			try{
				tipologiaAtividade = tipologiaService.carregarTipologiaPorIde(tipologiaAtividade.getIdeTipologia());
				return tipologiaAtividade.getDesTipologia();
				
			} catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
		}
		
		return "Todos";
	}
	
	public void exibirMensagemEscolhaDeFiltro() {
		JsfUtil.addWarnMessage("Informe pelo menos um filtro.");
	}
	
	public void exibirMensagemPeriodoDeFormacao() {
		JsfUtil.addWarnMessage("Informe o intervalo de formação completo.");
	}
	
	public void exibirMensagemPeriodoDoStatus() {
		JsfUtil.addWarnMessage("Informe o intervalo do período do status de forma completa.");
	}
	
	public void limparCampos() {
		municipioSelecionado=null;
		statusFluxoSelecionado=null;
		tipoAtoSelecionado=null;
		atoAmbientalSelecionado=null;
		diretoriaSelecionada=null;
		areaSelecionada=null;	
		strBuscaMunicipio=null;		
		periodoDeFormacaoDE=null;
		periodoDeFormacaoATE=null;	
		periodoStatusDE=null;
		periodoStatusATE=null;		
		listaStatusFluxo=null;
		listaTipoAto=null;
		listaAtoAmbiental=null;
		listaMunicipio=null;
		listaDiretoria=null;
		listaArea=null;
		tipologiaSelecionada=null;
		finalidadeSelecionada=null;
		tipologiaDivisao = null;
		listaTipologiaDivisao = null;
		tipologiaAtividade = null;
		listaTipologiaAtividade = null;
	}
	
	public void carregarDivisao() {
		try {
			if(Util.isNullOuVazio(listaTipologiaDivisao)) {
				listaTipologiaDivisao = tipologiaService.listarTipologiaDivisao();
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarAtividade() {
		try {
			listaTipologiaAtividade = tipologiaService.listarTipologiaAtividade(tipologiaDivisao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public List<Tipologia> getListaTipologiaDivisao() {
		carregarDivisao();
		return listaTipologiaDivisao;
	}

	public List<Tipologia> getListaTipologiaAtividade() {
		carregarAtividade();
		return listaTipologiaAtividade;
	}
	
	public List<Municipio> getListaMunicipio() {
		return listaMunicipio;		
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}

	public StatusFluxo getStatusFluxoSelecionado() {
		return statusFluxoSelecionado;
	}

	public void setStatusFluxoSelecionado(StatusFluxo statusFluxoSelecionado) {
		this.statusFluxoSelecionado = statusFluxoSelecionado;
	}

	public TipoAto getTipoAtoSelecionado() {
		return tipoAtoSelecionado;
	}

	public void setTipoAtoSelecionado(TipoAto tipoAtoSelecionado) {
		this.tipoAtoSelecionado = tipoAtoSelecionado;
	}

	public AtoAmbiental getAtoAmbientalSelecionado() {
		return atoAmbientalSelecionado;
	}

	public void setAtoAmbientalSelecionado(AtoAmbiental atoAmbientalSelecionado) {
		this.atoAmbientalSelecionado = atoAmbientalSelecionado;
	}

	public Area getDiretoriaSelecionada() {
		return diretoriaSelecionada;
	}

	public void setDiretoriaSelecionada(Area diretoriaSelecionada) {
		this.diretoriaSelecionada = diretoriaSelecionada;
	}

	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public String getStrBuscaMunicipio() {
		return strBuscaMunicipio;
	}

	public void setStrBuscaMunicipio(String strBuscaMunicipio) {
		this.strBuscaMunicipio = strBuscaMunicipio;
	}

	public Date getPeriodoDeFormacaoDE() {
		return periodoDeFormacaoDE;
	}

	public void setPeriodoDeFormacaoDE(Date periodoDeFormacaoDE) {
		this.periodoDeFormacaoDE = periodoDeFormacaoDE;
	}

	public Date getPeriodoDeFormacaoATE() {
		return periodoDeFormacaoATE;
	}

	public void setPeriodoDeFormacaoATE(Date periodoDeFormacaoATE) {
		this.periodoDeFormacaoATE = periodoDeFormacaoATE;
	}

	public Date getPeriodoStatusDE() {
		return periodoStatusDE;
	}

	public void setPeriodoStatusDE(Date periodoStatusDE) {
		this.periodoStatusDE = periodoStatusDE;
	}

	public Date getPeriodoStatusATE() {
		return periodoStatusATE;
	}

	public void setPeriodoStatusATE(Date periodoStatusATE) {
		this.periodoStatusATE = periodoStatusATE;
	}

	public Date getDataAtual() {
		return new Date();
	}

	public Tipologia getTipologiaSelecionada() {
		return tipologiaSelecionada;
	}

	public void setTipologiaSelecionada(Tipologia tipologiaSelecionada) {
		this.tipologiaSelecionada = tipologiaSelecionada;
	}

	public List<Tipologia> getListaTipologia() {
		try {
			if(Util.isNullOuVazio(listaTipologia)) {
				listaTipologia = (List<Tipologia>) tipologiaService.listarTipologiaAto();
			}
			return listaTipologia;
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void setListaTipologia(List<Tipologia> listaTipologia) {
		this.listaTipologia = listaTipologia;
	}

	public TipoFinalidadeUsoAgua getFinalidadeSelecionada() {
		return finalidadeSelecionada;
	}

	public void setFinalidadeSelecionada(TipoFinalidadeUsoAgua finalidadeSelecionada) {
		this.finalidadeSelecionada = finalidadeSelecionada;
	}

	public List<TipoFinalidadeUsoAgua> getListaFinalidade() {
		try {
			if(Util.isNullOuVazio(listaFinalidade)) {
				listaFinalidade = (List<TipoFinalidadeUsoAgua>) tipoFinalidadeUsoAguaService.listarTipoFinalidadeUsoAgua();
			}
			return listaFinalidade;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		} 			
	}

	public void setListaFinalidade(List<TipoFinalidadeUsoAgua> listaFinalidade) {
		this.listaFinalidade = listaFinalidade;
	}

	public Tipologia getTipologiaDivisao() {
		return tipologiaDivisao;
	}

	public void setTipologiaDivisao(Tipologia tipologiaDivisao) {
		this.tipologiaDivisao = tipologiaDivisao;
	}

	public void setListaTipologiaDivisao(List<Tipologia> listaTipologiaDivisao) {
		this.listaTipologiaDivisao = listaTipologiaDivisao;
	}
	
	public Tipologia getTipologiaAtividade() {
		return tipologiaAtividade;
	}	

	public void setTipologiaAtividade(Tipologia tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public void setListaTipologiaAtividade(List<Tipologia> listaTipologiaAtividade) {
		this.listaTipologiaAtividade = listaTipologiaAtividade;
	}
}