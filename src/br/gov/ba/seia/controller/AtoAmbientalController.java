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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.AtoAmbientalTipologiaService;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@ViewScoped
@Named("atoAmbientalController")

public class AtoAmbientalController {

	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private TipoAtoService tipoAtoService;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	
	@EJB
	private DocumentoAtoAmbientalService documentoAtoAmbientalService;

	@EJB
	private AtoAmbientalTipologiaService atoAmbientalTipologiaService;
	
	private boolean cadastroRender;
	private boolean consultaRender;
	
	private boolean editar;
	private boolean salvar;
	private boolean editandoFinalidade;
	
	private Tipologia tipologiaSelecionada;
	private TipoFinalidadeUsoAgua finalidadeSelecionada;
	private DocumentoObrigatorio documentoObrigatorioSelecionado;

	private DocumentoAto documentoAtoSelecionado;
	private AtoAmbiental atoAmbientalSelecionado;
	private AtoAmbiental atoAmbientalNovo;
	private String nomeAto;

	private LazyDataModel<AtoAmbiental> atoAmbientalModel;
	private LazyDataModel<DocumentoAto> documentoAtoModel;
	
	private boolean dlsAtivo;
	private boolean recarregar;
	
	private Collection<Tipologia> tipologiaMenu;	
	private Collection<TipoFinalidadeUsoAgua> finalidadeMenu;
	private Collection<DocumentoObrigatorio> documentoObrigatorioMenu;

	private Tipologia tipologia;
	private TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua;
	private DocumentoObrigatorio documentoObrigatorio;
	
	
	private Collection<Tipologia> tipologiasDoAto;
	private Collection<TipoAto> listaTipoAto;
	private boolean novoAto;
	private boolean autorizado;
	private boolean autorizadoFinalidade;
	private boolean finalidadedEnable;
	private boolean editarFinalidade;
	private boolean renderDocumentoAto;
	
	@PostConstruct
	public void init(){
	
		incializarVariaveis();
		carregarLazyModelAtoAmbiental();
	}
	
	public void carregarLazyModelAtoAmbiental() {

		try {

			atoAmbientalModel = new LazyDataModel<AtoAmbiental>() {

				private static final long serialVersionUID = -7856231926251865860L;

				@Override
				public List<AtoAmbiental> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					List<AtoAmbiental> atosAmbientais = new ArrayList<AtoAmbiental>();

					try {
						setPageSize(pageSize);
						atosAmbientais = atoAmbientalService.listarAtoAmbientalPorDemanda(nomeAto, first, pageSize);
					} catch (Exception e) {
						JsfUtil.addErrorMessage("Erro ao carregar Lista de atos");
					}

					return atosAmbientais;
				}
			};
				
			atoAmbientalModel.setRowCount(getCountAtoAmbiental());
	
		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
		}
	}
			
	public void carregarLazyModelDocumentoAto() {

		try {
			
			documentoAtoModel = new LazyDataModel<DocumentoAto>() {

				private static final long serialVersionUID = 8970196947383473262L;

				@Override
				public List<DocumentoAto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					List<DocumentoAto> documentosAtos= new ArrayList<DocumentoAto>();
					
					try {
						setPageSize(pageSize);
						documentosAtos = documentoAtoAmbientalService.listarDocumentoAtoByDemanda(atoAmbientalSelecionado.getIdeAtoAmbiental(), first, pageSize);		
					} catch (Exception e) {
						JsfUtil.addErrorMessage("Erro ao carregar a Lista de Documentos");
					}
					return documentosAtos;
				}
			};
			documentoAtoModel.setRowCount(getCountDocumentoAto());
		} catch (Exception e1) {					
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
		}
	}
	
	public Collection<TipoAto> getTiposAto() {
		
		if (listaTipoAto == null) {
			try {
				listaTipoAto = tipoAtoService.listarTiposAto();
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao carregar lista de atos");
			}
		}
		return listaTipoAto;
	}
	
	public int countAtoAmbiental (){
		
		if(atoAmbientalService.countAtoAmbiental(nomeAto) <= 0){
			return -1;
		}
		else {
			return atoAmbientalService.countAtoAmbiental(nomeAto);
		}
	}

	public void excluirAtoAmbiental(){
		try {
			atoAmbientalService.excluirAtoAmbiental(atoAmbientalSelecionado);
			nomeAto = "";
			JsfUtil.addSuccessMessage("Ato Ambiental excluido.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("O Ato Ambiental \n"+ atoAmbientalSelecionado.getNomAtoAmbiental() + " possui documento(s) obrigatório(s)\n e não pode ser excluido.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	
	public void mudaRender(){
		
		carregarLazyModelDocumentoAto();
		consultaRender = false;
		cadastroRender = true;
	
	}
	
	public void novoAto(){
	
		novoAto = true;
		atoAmbientalSelecionado = new AtoAmbiental();
		TipoAto tipoAto = new TipoAto();
		tipoAto.setNomTipoAto("");
		atoAmbientalSelecionado.setIdeTipoAto(tipoAto);
	
	
		atoAmbientalSelecionado.setIndAtivo(false);
		atoAmbientalSelecionado.setIndDeclaratorio(false);
		
		renderDocumentoAto = false;
		consultaRender = false;
		cadastroRender = true;
		
	}
	
	public void incializarVariaveis(){
		
		novoAto= true;
		nomeAto="";
		consultaRender= true;
		cadastroRender= false;
		dlsAtivo = true;
		autorizado = false;
		finalidadedEnable = false;
		recarregar = false;
		editandoFinalidade = false;
		renderDocumentoAto = true;
		editar = false;
		
		
		
	}
			
	public void adicionarVinculo() {
     	autorizado = true;
     	editar = false;
     	documentoAtoSelecionado = new DocumentoAto();
     	documentoAtoSelecionado.setIdeAtoAmbiental(atoAmbientalSelecionado);
	}
	

	public boolean isRenderDocumentoAto() {
		return renderDocumentoAto;
	}

	public void setRenderDocumentoAto(boolean renderDocumentoAto) {
		this.renderDocumentoAto = renderDocumentoAto;
	}

	public boolean isCadastroRender() {
		return cadastroRender;
	}

	public void setCadastroRender(boolean cadastroRender) {
		this.cadastroRender = cadastroRender;
	}

	public boolean isConsultaRender() {
		return consultaRender;
	}

	public void setConsultaRender(boolean consultaRender) {
		this.consultaRender = consultaRender;
	}

	public LazyDataModel<AtoAmbiental> getAtoAmbientalModel() {
		return atoAmbientalModel;
	}

	public void setAtoAmbientalModel(LazyDataModel<AtoAmbiental> atoAmbientalModel) {
		this.atoAmbientalModel = atoAmbientalModel;
	}

	public String getNomeAto() {
		return nomeAto;	
	}
	
	public void setNomeAto(String nomeAto) {
		this.nomeAto = nomeAto;	
	}

	public int getCountAtoAmbiental() {
	 return atoAmbientalService.countAtoAmbiental(nomeAto);	
	}
	
	public AtoAmbiental getAtoAmbientalSelecionado() {
		return atoAmbientalSelecionado;
	}

	public void setAtoAmbientalSelecionado(AtoAmbiental atoAmbientalSelecionado) {
		this.atoAmbientalSelecionado = atoAmbientalSelecionado;
	}
	
	public Tipologia getTipologiaSelecionada() {
		return tipologiaSelecionada;
	}

	public void setTipologiaSelecionada(Tipologia tipologiaSelecionada) {
		this.tipologiaSelecionada = tipologiaSelecionada;
	}

	public TipoFinalidadeUsoAgua getFinalidadeSelecionada() {
		if   (finalidadeSelecionada==null){return new TipoFinalidadeUsoAgua();}
		else {return finalidadeSelecionada;}
	}

	public void setFinalidadeSelecionada(TipoFinalidadeUsoAgua finalidadeSelecionada) {
		finalidadeSelecionada = finalidadeSelecionada;
	}

	public DocumentoObrigatorio getDocumentoObrigatorioSelecionado() {
		return documentoObrigatorioSelecionado;
	}

	public void setDocumentoObrigatorioSelecionado(DocumentoObrigatorio documentoObrigatorioSelecionado) {
		this.documentoObrigatorioSelecionado = documentoObrigatorioSelecionado;
	}

	public void salvarAtoAmbiental(){
		
		
		try {
			if(!renderDocumentoAto){//neste caso é um novo ato
				atoAmbientalService.salvarAtoAmbiental(atoAmbientalSelecionado);
				nomeAto = atoAmbientalSelecionado.getNomAtoAmbiental();
				JsfUtil.addSuccessMessage("Ato Ambiental Salvo");
			}else{
				atoAmbientalService.atualizarAtoAmbiental(atoAmbientalSelecionado);
				JsfUtil.addSuccessMessage("Ato Ambiental Alterado");
			}
			renderDocumentoAto = true;
			
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Ato Ambiental não pode ser salvo, pois já existe um ato ambiental Salvo com esse nome :");
		}
		
	}
		
	private boolean isDocumentoAtoValido() throws Exception{
		boolean isPodeSalvar = true;
		
		if(!Util.isNull(documentoAtoSelecionado)) {
			
			if (Util.isNull(documentoAtoSelecionado.getIdeDocumentoObrigatorio())) {
				JsfUtil.addErrorMessage("O campo *Documento Obrigatório é Obrigatório");
				isPodeSalvar = false;
			} 
			else {
			    if(!isEditar() && documentoAtoAmbientalService.isExisteDocumentoObrigatorio(documentoAtoSelecionado)){
			    	isPodeSalvar = false;
			    	
			    	if(Util.isNullOuVazio(documentoAtoSelecionado.getIdeTipologia())){
			    		JsfUtil.addErrorMessage("O documento obrigatorio escolhido já esta vinculado ao ato ambiental.");
			    	}else if(Util.isNullOuVazio(documentoAtoSelecionado.getIdeTipoFinalidadeUsoAgua())){
			    		JsfUtil.addErrorMessage("O documento obrigatório escolhido já esta vinculado ao ato ambiental para essa tipologia.");
			    	}else{
			    		JsfUtil.addErrorMessage("O documento obrigatório escolhido já esta vinculado ao ato ambiental para essa tipologia para essa finalidade.");
			    	}				    	
			    }
			}
		}
		
		return isPodeSalvar;
	}
	
	public void salvarDocumentoAto() throws Exception {
		
			try {
				if(isDocumentoAtoValido()){				
					documentoAtoAmbientalService.salvarOuAtualizarDocumentoAto(documentoAtoSelecionado);
					documentoObrigatorioSelecionado = new DocumentoObrigatorio();
					carregarLazyModelDocumentoAto();
					JsfUtil.addSuccessMessage("	Documento Vinculados com Sucesso.");
				}
			}catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao vincular documentos ao ato");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
	}

	public void excluirDocumentoAto(){
		try {
	
			documentoAtoAmbientalService.excluirDocumentoAto(documentoAtoSelecionado);
			JsfUtil.addSuccessMessage("Documento Ato Excluido com sucesso.");
			
		} catch (Exception e) {
			JsfUtil.addSuccessMessage("Documento Ato Não pode ser Excluido pois Contém Relacionamentos");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		
	}
	public void editarDocumentoAto(DocumentoAto documentoAto){
		
		autorizado = true;
		recarregar = true;
		editarFinalidade = true;
		documentoAtoSelecionado = documentoAto;
	}
	
	public boolean isSalvar() {
		return salvar;
	}

	public void setSalvar(boolean salvar) {
		this.salvar = salvar;
	}


	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public boolean isDslAtivo() {
		return dlsAtivo;
	}

	public void setDslAtivo(boolean dslAtivo) {
		this.dlsAtivo = dslAtivo;
	}

	public void setTipologiaMenu(Collection<Tipologia> tipologiaMenu) {
		this.tipologiaMenu = tipologiaMenu;
	}

	public Collection<Tipologia> getTipologiasDoAto() {
		return tipologiasDoAto;
	}

	public void setTipologiasDoAto(Collection<Tipologia> tipologiasDoAto) {
		this.tipologiasDoAto = tipologiasDoAto;
	}
	
	public Collection<TipoAto> getListaTipoAto() {
		if(listaTipoAto== null){
			try {
				listaTipoAto = tipoAtoService.listarTiposAtoOrderByAsc();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		
		return listaTipoAto;
	}
	
	public void carregaFinaliade(ValueChangeEvent pValueChangeEvent){
		finalidadeSelecionada = (TipoFinalidadeUsoAgua) pValueChangeEvent.getNewValue();
		
	}

	public void carregaTipologia(ValueChangeEvent pValueChangeEvent){
		autorizadoFinalidade=true;
		tipologiaSelecionada = (Tipologia) pValueChangeEvent.getNewValue();	
	}
	
	public void carregaDocumentoObrigatorio(ValueChangeEvent pValueChangeEvent){
		documentoObrigatorioSelecionado = (DocumentoObrigatorio) pValueChangeEvent.getNewValue();
	}

	public Collection<Tipologia> getTipologiaMenu() {
		if((tipologiaMenu == null) && (autorizado)){
			try {
				tipologiaMenu = tipologiaService.listarTodasTipologias();
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return tipologiaMenu;
	}
	
	public Collection<TipoFinalidadeUsoAgua> getFinalidadeMenu() {

		if(!editar){		
			try{
				if(!Util.isNullOuVazio(tipologiaSelecionada)){
					finalidadeMenu = atoAmbientalTipologiaService.buscarFinalidadePorTipologia(tipologiaSelecionada.getIdeTipologia());						
				}else{
					finalidadeMenu = new ArrayList<TipoFinalidadeUsoAgua>();
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		return finalidadeMenu;
	}
	
	public Collection<DocumentoObrigatorio> getDocumentoObrigatorioMenu() {
		
		if((documentoObrigatorioMenu==null)&&(autorizado)){
			try{
				documentoObrigatorioMenu = documentoObrigatorioService.listarDocumentoObrigatorioSemRelacionamentos();
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return documentoObrigatorioMenu;
	}


	public void setFinalidadeMenu(Collection<TipoFinalidadeUsoAgua> finalidadeMenu) {
		this.finalidadeMenu = finalidadeMenu;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public TipoFinalidadeUsoAgua getTipoFinalidadeUsoAgua() {
		return tipoFinalidadeUsoAgua;
	}

	public void setTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua) {
		this.tipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua;
	}

	public DocumentoObrigatorio getDocumentoObrigatorio() {
		return documentoObrigatorio;
	}

	public void setDocumentoObrigatorio(DocumentoObrigatorio documentoObrigatorio) {
		this.documentoObrigatorio = documentoObrigatorio;
	}

	public void setListaTipoAto(Collection<TipoAto> listaTipoAto) {
		this.listaTipoAto = listaTipoAto;
	}

	public void setDocumentoObrigatorioMenu(Collection<DocumentoObrigatorio> documentoObrigatorioMenu) {
		this.documentoObrigatorioMenu = documentoObrigatorioMenu;
	}

	public LazyDataModel<DocumentoAto> getDocumentoAtoModel() {
		return documentoAtoModel;
	}

	public void setDocumentoAtoModel(LazyDataModel<DocumentoAto> documentoAtoModel) {
		this.documentoAtoModel = documentoAtoModel;
	}

	public boolean isDlsAtivo() {
		return dlsAtivo;
	}

	public void setDlsAtivo(boolean dlsAtivo) {
		this.dlsAtivo = dlsAtivo;
	}

	public int getCountDocumentoAto() throws Exception {    
		return documentoAtoAmbientalService.countDocumentoAtosById(atoAmbientalSelecionado.getIdeAtoAmbiental());
	}

	public boolean isAutorizadao() {
		return autorizado;
	}

	public void setAutorizadao(boolean autorizadao) {
		this.autorizado = autorizadao;
	}

	public boolean isAutorizadoFinalidade() {
		return autorizadoFinalidade;
	}

	public void setAutorizadoFinalidade(boolean autorizadoFinalidade) {
		this.autorizadoFinalidade = autorizadoFinalidade;
	}

	public boolean isFinalidadedEnable() {
		return finalidadedEnable;
	}

	public void setFinalidadedEnable(boolean finalidadedEnable) {
		this.finalidadedEnable = finalidadedEnable;
	}

	public boolean isRecarregar() {
		return recarregar;
	}

	public void setRecarregar(boolean recarregar) {
		this.recarregar = recarregar;
	}

	public AtoAmbiental getAtoAmbientalNovo() {
		return atoAmbientalNovo;
	}

	public void setAtoAmbientalNovo(AtoAmbiental atoAmbientalNovo) {
		this.atoAmbientalNovo = atoAmbientalNovo;
	}

	public DocumentoAto getDocumentoAtoSelecionado() {
		return documentoAtoSelecionado;
	}

	public void setDocumentoAtoSelecionado(DocumentoAto documentoAtoSelecionado) {
		this.documentoAtoSelecionado = documentoAtoSelecionado;
	}
	

	public boolean isEditarFinalidade() {
		return editarFinalidade;
	}

	public void setEditarFinalidade(boolean editarFinalidade) {
		this.editarFinalidade = editarFinalidade;
	}

	public boolean isEditandoFinalidade() {
		return editandoFinalidade;
	}

	public void setEditandoFinalidade(boolean editandoFinalidade) {
		this.editandoFinalidade = editandoFinalidade;
	}

	public boolean isNovoAto() {
		return novoAto;
	}

	public void setNovoAto(boolean novoAto) {
		this.novoAto = novoAto;
	}
		
}