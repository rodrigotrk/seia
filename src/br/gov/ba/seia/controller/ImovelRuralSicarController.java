package br.gov.ba.seia.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Level;
import org.primefaces.json.JSONException;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.PessoaJuridicaPct;
import br.gov.ba.seia.entity.ProcessoTramiteImovelRural;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoSeguimentoPct;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.PctImovelRuralFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.ImovelRuralSicarService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.json.JsonUtil;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.TelefoneService;

@Named("imovelRuralSicarController")
@ViewScoped
public class ImovelRuralSicarController extends SeiaControllerAb implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private ImovelRural imovelRural;
	private ImovelRural imovelRuralPesquisa;
	private LazyDataModel<ImovelRural> listaImoveis = null;
	private String nomImovelRural;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcSincronia;
	private boolean indSincronia = true;
	private String filtroNumCar;
	private String protocolo;
	private String token;
	private String numSicar;
	private String codRetorno;
	private boolean sicarGerado;
	private String mensagemRetorno;
	protected CronogramaEtapa cronogramaEtapaSelecionado;	
	private PctImovelRural pctImovelRural;
	protected List<DocumentoImovelRural> listaPradImportadosRl;
	private DualListModel<TipoSeguimentoPct> tipoSeguimentoPcts;
	
	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;
	@EJB
	private ImovelRuralSicarService imovelRuralSicarService;
	
	@EJB
	private PctImovelRuralFacade pctImovelRuralFacade;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService; 

	@PostConstruct
	public void init() {
	}
	
	public Endereco getEndereco() {
		return this.imovelRural.getImovel().getIdeEndereco();
	}
	
	private void carregarPctImovelRural(){
		try {
			if(!Util.isNullOuVazio(imovelRural)) {
				
				pctImovelRural = imovelRuralServiceFacade.buscarPctImovelRural(imovelRural);
				
				/*carregarPCTProprietarioPossuidor();
				carregarPCTRepresentanteFamilia();*/
				
				if(!Util.isNullOuVazio(pctImovelRural)) {
					imovelRural.setIdePctImovelRural(pctImovelRural);
					ContextoUtil.getContexto().setPCT(true);
				}
			}
			
			if(getShowPCT()) {
				getListarTipoSeguimentoPct();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getListarTipoSeguimentoPct(){
		try {
			tipoSeguimentoPcts = new DualListModel<TipoSeguimentoPct>(imovelRuralServiceFacade.listarTipoSeguimentoPct(), new ArrayList<TipoSeguimentoPct>());
			ajustarSegmento();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ajustarSegmento() {
		if (Util.isNullOuVazio(pctImovelRural.getTipoSeguimentoPctCollection())) {
			pctImovelRural.setTipoSeguimentoPctCollection(new ArrayList<TipoSeguimentoPct>());
		}
		
		for (Iterator iterator = pctImovelRural.getTipoSeguimentoPctCollection().iterator(); iterator.hasNext();) {
			TipoSeguimentoPct tipoSeguimentoPct = (TipoSeguimentoPct) iterator.next();
			
			tipoSeguimentoPcts.getSource().remove(tipoSeguimentoPct);
		}
	}
	
	public Boolean getShowPCT(){
		
		return ContextoUtil.getContexto().isPCT();
	}
	
	public void gerarSicar(){
		try {
			Integer qtd = 0;
			for (ImovelRural imovelRural : imovelRuralServiceFacade.listarImovelRuralSemSicar(false)) {
				this.imovelRural = imovelRural;
				System.out.println("---" + this.imovelRural.getIdeImovelRural() + "---");
				carregarImovelRural();
				imovelRuralServiceFacade.gerarCertificado(this.imovelRural);
				this.imovelRural.setImovelRuralSicar(new ImovelRuralSicar());
				this.imovelRural.getImovelRuralSicar().setDtcCriacao(new Date());
				montarImovelRuralSicar();
				imovelRuralServiceFacade.salvarImovelRuralSicar(this.imovelRural.getImovelRuralSicar());
				qtd++; 
			}
			JsfUtil.addSuccessMessage(qtd + " imóveis sincronizados com sucesso de " + imovelRuralServiceFacade.listarImovelRuralSemSicar(true).size() +"!");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro na sincronização do imóvel: " + this.imovelRural.getDesDenominacao() + " [id = " + this.imovelRural.getIdeImovelRural() + " ]");
		}
	}
	
	public void detalharImovelRuralSicar() {
		try {
			this.sicarGerado = false;
			carregarImovelRural();
			ContextoUtil.getContexto().setImovelRural(this.imovelRural);
		} catch (Exception e) {
			if (Util.isNullOuVazio(this.imovelRural)) {
				JsfUtil.addErrorMessage("Imóvel não encontrado ou excluído.");
			}else{
				JsfUtil.addErrorMessage("Erro na exibição do Json para o SICAR.");
			}
		}
	}
	
	public void gerarImovelRuralSicar() {
		try {			
			montarImovelRuralSicar();	
			this.sicarGerado = true;
			JsfUtil.addSuccessMessage("Json para o SICAR gerado com sucesso.");			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro na geração do Json para o SICAR. " + e.getMessage());
		}
	}
	
	public void salvarImovelRuralSicar() {
		try {	
			imovelRuralSicarService.atualizar(imovelRural.getImovelRuralSicar());
			this.sicarGerado = false;
			JsfUtil.addSuccessMessage("Json para o SICAR atualizado com sucesso.");			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro na atualização do Json para o SICAR. " + e.getMessage());
		}
	}	

	private void carregarImovelRural() throws Exception {		
		if (!Util.isNullOuVazio(imovelRural)) {
			imovelRural = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());
			if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica()) && !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				imovelRural.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
			}
			if (imovelRural.isImovelINCRA() && !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote()) && !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica())){
				imovelRural.setIdeLocalizacaoGeograficaLote(imovelRuralServiceFacade.carregarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica()));
			}
				
			if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
				imovelRural.setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelRural.getReservaLegal()));					
				imovelRural.setAppCollection(imovelRuralServiceFacade.listarAppByImovelRural(imovelRural));					
				imovelRural.setAreaProdutivaCollection(imovelRuralServiceFacade.listarAreaProdutivaByImovelRural(imovelRural));
				imovelRural.setVegetacaoNativa(imovelRuralServiceFacade.listarVegetacaoNativaByImovelRural(imovelRural));
				imovelRural.setIdeAreaRuralConsolidada(imovelRuralServiceFacade.listarAreaRuralConsolidadaNativaByImovelRural(imovelRural));
				imovelRural.setImovelRuralUsoAguaCollection(imovelRuralServiceFacade.obterListaUsoAguaImovelRural(imovelRural));
				imovelRural.setAssentadoIncraImovelRuralCollection(imovelRuralServiceFacade.listarAssentadoIncraImovelRuralPorImovelRural(imovelRural));
				carregarImovelRuralUsoAgua();
				carregarApps();
				carregarAreasProdutivas();
				carregarResponsaveisTecnicos();
				carregarAsentadosIncraImovelRural();
				if(!Util.isNull(imovelRural.getIndRppn()) && imovelRural.getIndRppn()){
					imovelRural.setIdeImovelRuralRppn(carregarImovelRuralRppn());
				}
									
				this.imovelRural.setProcessoTramiteImovelRuralCollection(imovelRuralServiceFacade.listarProcesTramitImovelRuralPorImovelRural(this.imovelRural));
				if(Util.isNullOuVazio(this.imovelRural.getProcessoTramiteImovelRuralCollection())) {
					this.imovelRural.setProcessoTramiteImovelRuralCollection(new ArrayList<ProcessoTramiteImovelRural>());
				}
			}
			
			if(imovelRural.isImovelPCT()) {
				carregarPctImovelRural();
			}
			carregarDocumentoImovelRuralPosse();
		}
	}
	
	private void carregarDocumentoImovelRuralPosse() {
		try {
			if(!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())) {
				imovelRural.setDocumentoImovelRuralPosse(
						imovelRuralServiceFacade.carregarTudoDocumentoImovelRuralPosse(imovelRural.getDocumentoImovelRuralPosse()));
			}
		} catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarAsentadosIncraImovelRural() throws Exception{
		if(!Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())){
			for (AssentadoIncraImovelRural assentado : imovelRural.getAssentadoIncraImovelRuralCollection()){
				assentado.setAssociacaoAssentadoImovelRuralIncraCollection(imovelRuralServiceFacade.listarAssociacaoAssentadoImovelRuralIncraPorAssentadoIncraImovelRural(assentado));
			}
		}
	}
	
	private void carregarResponsaveisTecnicos() throws Exception {
		List<ResponsavelImovelRural> resps = new ArrayList<ResponsavelImovelRural>();
		imovelRural.setResponsavelImovelRuralCollection(imovelRuralServiceFacade.filtrarResponsaveisPorImovelRural(imovelRural));
		for (ResponsavelImovelRural responsavel : imovelRural.getResponsavelImovelRuralCollection()) {
			ResponsavelImovelRural resp = new ResponsavelImovelRural();
			resp = imovelRuralServiceFacade.filtrarResponsavelImovelRuralById(responsavel.getIdeResponsavelImovelRural());
			resps.add(resp);			
		}
		imovelRural.setResponsavelImovelRuralCollection(resps);
	}

	private void carregarApps() throws Exception{
		List<App> apps = new ArrayList<App>();			
		if(!Util.isNullOuVazio(imovelRural.getAppCollection())){
			for (App app : imovelRural.getAppCollection()){
				apps.add(imovelRuralServiceFacade.carregarTudoApp(app));
			}
		}
		imovelRural.setAppCollection(new ArrayList<App>(apps));		
	}
	
	private void carregarAreasProdutivas() throws Exception{
		List<AreaProdutiva> aps = new ArrayList<AreaProdutiva>();			
		if(!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())){
			for (AreaProdutiva ap : imovelRural.getAreaProdutivaCollection()){
				ap = imovelRuralServiceFacade.carregarTudoAreaProdutiva(ap);
				switch (ap.getIdeTipologia().getIdeTipologia()) {
					case 6:				
						if(!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
							for (AreaProdutivaTipologiaAtividade ta : ap.getAreaProdutivaTipologiaAtividadeCollection()) {							
								ta.setIdeAreaProdutivaTipologiaAtividadeAgricultura(imovelRuralServiceFacade.carregarTipologiaAtividadeAgriculturaByIde(ta));					
							}
						}						
					break;
					case 8:
					case 10:
						if(!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
							for (AreaProdutivaTipologiaAtividade ta : ap.getAreaProdutivaTipologiaAtividadeCollection()) {							
								ta.setIdeAreaProdutivaTipologiaAtividadeAnimal(imovelRuralServiceFacade.carregarTipologiaAtividadeAnimalByIde(ta));					
							}
						}
					break;
					case 16:
						if(!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
							for (AreaProdutivaTipologiaAtividade ta : ap.getAreaProdutivaTipologiaAtividadeCollection()) {							
								ta.setIdeAreaProdutivaTipologiaAtividadePiscicultura(imovelRuralServiceFacade.carregarTipologiaAtividadePisciculturaByIde(ta.getIdeAreaProdutivaTipologiaAtividadePiscicultura().getIdeAreaProdutivaTipologiaAtividadePiscicultura()));					
							}
						}						
					break;					
				}
				
				aps.add(ap);
			}
		}
		imovelRural.setAreaProdutivaCollection(new ArrayList<AreaProdutiva>(aps));
	}

	private void carregarImovelRuralUsoAgua() throws Exception {
		List<ImovelRuralUsoAgua> listUsoAguaImovelRural = new ArrayList<ImovelRuralUsoAgua>();

		for (ImovelRuralUsoAgua imovelRuralUsoAgua : imovelRural.getImovelRuralUsoAguaCollection()) {
			imovelRuralUsoAgua = imovelRuralServiceFacade.obterPorId(imovelRuralUsoAgua);
			imovelRuralUsoAgua.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
			imovelRuralUsoAgua.setTipoFinalidadeCollection(this.imovelRuralServiceFacade.listarImovelRuralUsoAguaTipoFinalidadeUsoAgua(imovelRuralUsoAgua));
			if (!Util.isNullOuVazio(imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal()))
				imovelRuralUsoAgua.setIdeLocalizacaoGeograficaFinal(imovelRuralServiceFacade.carregarLocalizacaoGeografica(imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().getIdeLocalizacaoGeografica()));
			listUsoAguaImovelRural.add(imovelRuralUsoAgua);
		}
		imovelRural.setImovelRuralUsoAguaCollection(new ArrayList<ImovelRuralUsoAgua>(listUsoAguaImovelRural));
	}
	
	private ImovelRuralRppn carregarImovelRuralRppn() throws Exception{
		ImovelRuralRppn imovelRuralRppn = imovelRuralServiceFacade.carregarImovelRuralRppnByIdeImovelRural(this.imovelRural);
		return imovelRuralRppn;		
	}
	
	public void consultarImoveisSicar() {
		try {
			carregarLazyModelReqImoveis();		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	@SuppressWarnings("serial")
	public void carregarLazyModelReqImoveis() {
		imovelRuralPesquisa = new ImovelRural();
		imovelRuralPesquisa.setDesDenominacao(nomImovelRural);
		imovelRuralPesquisa.setImovelRuralSicar(new ImovelRuralSicar());
		imovelRuralPesquisa.getImovelRuralSicar().setDtcIniSicronia(dtcSincronia);
		imovelRuralPesquisa.getImovelRuralSicar().setIndSicronia(indSincronia);
		imovelRuralPesquisa.getImovelRuralSicar().setNumProtocolo(protocolo);
		imovelRuralPesquisa.getImovelRuralSicar().setToken(token);
		imovelRuralPesquisa.getImovelRuralSicar().setNumSicar(numSicar);
		imovelRuralPesquisa.getImovelRuralSicar().setCodRetornoSincronia(codRetorno);
		imovelRuralPesquisa.getImovelRuralSicar().setMsgRetornoSincronia(mensagemRetorno);
		
		listaImoveis = new LazyDataModel<ImovelRural>() {
			@Override
			public List<ImovelRural> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<ImovelRural> reqImoveis = null;
				try {
					setPageSize(pageSize);
					reqImoveis = populateListImoveis(first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage(e.getMessage());
				}
				return reqImoveis;
			}
		};
		listaImoveis.setRowCount(getRowCountImoveis());
	}
	
	protected List<ImovelRural> populateListImoveis(int first, int pageSize) {		
		List<ImovelRural> reqImoveis = new ArrayList<ImovelRural>();
		try {
			reqImoveis = imovelRuralSicarService.listarPorCriteriaDemanda(imovelRuralPesquisa, tratarFiltroNumeroCar(),  first, pageSize);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return reqImoveis;
	}
	
	private Boolean tratarFiltroNumeroCar() {
		if(filtroNumCar.equals("TODOS"))
			return null;
		else if(filtroNumCar.equals("SIM"))
			return true;
		else 
			return false;
	}

	protected int getRowCountImoveis() {
		int totalRowCount = 0;
		try {
			totalRowCount = imovelRuralSicarService.count(imovelRuralPesquisa, tratarFiltroNumeroCar());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return totalRowCount;
	}
	
	public void limparFormConsulta(){
		this.indSincronia = true;
		this.nomImovelRural = null;
		this.dtcSincronia = null;
		this.protocolo = null;
		this.token = null;
		this.numSicar = null;
		this.codRetorno = null;
		this.mensagemRetorno = null;
		this.listaImoveis = null;		
	}
	
	private void montarImovelRuralSicar() throws Exception {
		carregarDadosGeoJsonSicar();
		carregarRepresentantesLegaisAssociacao();
		String numToken = "";
		
		if (imovelRural.isTermoCompromisso()) {
			List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(
					this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId());
			numToken = "-" + lCefir.get(0).getNumToken();
			imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
		} else {
			List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(
					this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.CEFIR.getId());
			if (!Util.isNullOuVazio(lCefir)) {
				numToken = "-" + lCefir.get(0).getNumToken();
				imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
			}
		}
		
		imovelRural.getImovelRuralSicar().setIdeImovelRural(imovelRural);
		imovelRural.getImovelRuralSicar().setNumProtocolo("BA-" + imovelRural.getIdeImovelRural() + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + numToken);
		imovelRural.getImovelRuralSicar().setIndSicronia(false);
		imovelRural.getImovelRuralSicar().setDtcIniSicronia(null);
		imovelRural.getImovelRuralSicar().setDtcFimSicronia(null);
		imovelRural.getImovelRuralSicar().setToken(null);
		imovelRural.getImovelRuralSicar().setMsgRetornoSincronia(null);
		imovelRural.getImovelRuralSicar().setCodRetornoSincronia(null);
		imovelRural.getImovelRuralSicar().setUrlReciboInscricao(null);
		imovelRural.getImovelRuralSicar().setJson(JsonUtil.montarJsonImovelRuralSicar(imovelRural, this.imovelRural.getIdeImovelRuralRppn()).toString());
	}
	
	private void carregarRepresentantesLegaisAssociacao() throws Exception {
		if(!Util.isNullOuVazio(this.imovelRural.getIdePctImovelRural()) && 
				!Util.isNullOuVazio(this.imovelRural.getIdePctImovelRural().getPessoaJuridicaPctListaAssociacao())) {
			for (PessoaJuridicaPct pessoaJuridicaPct : this.imovelRural.getIdePctImovelRural().getPessoaJuridicaPctListaAssociacao()) {
				if(Util.isNullOuVazio(pessoaJuridicaPct.getIdePessoaJuridica().getRepresentanteLegalCollection())) {
					pessoaJuridicaPct.getIdePessoaJuridica().setRepresentanteLegalCollection(new ArrayList<RepresentanteLegal>());
					List<RepresentanteLegal> representantesLegais = representanteLegalService.getListaRepresentanteLegalByPessoa(pessoaJuridicaPct.getIdePessoaJuridica());
					for (RepresentanteLegal representanteLegal : representantesLegais) {
						if(Util.isNullOuVazio(representanteLegal.getIdePessoaFisica().getPessoa().getTelefoneCollection())){
							representanteLegal.getIdePessoaFisica().getPessoa().setTelefoneCollection(new ArrayList<Telefone>());
							List<Telefone> telefones = telefoneService.buscarTelefonesPorPessoa(representanteLegal.getIdePessoaFisica().getPessoa());
							representanteLegal.getIdePessoaFisica().getPessoa().getTelefoneCollection().addAll(telefones);
							EnderecoPessoa enderecoPessoa = enderecoPessoaService.buscarEnderecoPorPessoa(representanteLegal.getIdePessoaFisica().getPessoa());
							if(!Util.isNullOuVazio(enderecoPessoa)) {
								representanteLegal.getIdePessoaFisica().getPessoa().setEndereco(enderecoPessoa.getIdeEndereco());
							} else {
								enderecoPessoa = new EnderecoPessoa();
							}
						}
					}
					pessoaJuridicaPct.getIdePessoaJuridica().getRepresentanteLegalCollection().addAll(representantesLegais);
				}
			}
		}
	}
	
	private void carregarDadosGeoJsonSicar() throws Exception {		
		List<Integer> localizacoes = new ArrayList<Integer>();
		
		localizacoes.add(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		
		GeoJsonSicar geoJsonIm = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
		
		if(!Util.isNullOuVazio(geoJsonIm)) {
			imovelRural.setGeoJsonSicar(geoJsonIm);
		} else {
			throw new JSONException("Falha ao carregar objeto Geo Imovel: Geometria não disponível.");
		}
		
		localizacoes.clear();
		
		if (imovelRural.isImovelPCT()) {
			localizacoes.add(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
			
			GeoJsonSicar geoJsonPctLimiteTerritorio = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			
			if(!Util.isNullOuVazio(geoJsonPctLimiteTerritorio)) {
				imovelRural.setGeoJsonSicarPctLimiteTerritorio(geoJsonPctLimiteTerritorio);
			} else {
				throw new JSONException("Falha ao carregar objeto Geo Pct Limite Territorio: Geometria não disponível.");
			}
			
			localizacoes.clear();
			
			localizacoes.add(imovelRural.getIdeLocalizacaoGeograficaPct().getIdeLocalizacaoGeografica());
			
			GeoJsonSicar geoJsonPctSede = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			
			if(!Util.isNullOuVazio(geoJsonPctSede)) {
				imovelRural.setGeoJsonSicarPct(geoJsonPctSede);
			} else {
				throw new JSONException("Falha ao carregar objeto Geo Pct Sede: Geometria não disponível.");
			}
			
			localizacoes.clear();
        }
		
		if(!Util.isNullOuVazio(imovelRural.getReservaLegal()) && !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())){
			localizacoes.add(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			
			GeoJsonSicar geoJsonRl = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			
			if(!Util.isNullOuVazio(geoJsonRl)) {
				imovelRural.getReservaLegal().setGeoJsonSicar(geoJsonRl);
			} else {
				throw new JSONException("Falha ao carregar objeto Geo Reserva Legal: Geometria não disponível.");
			}
			
			localizacoes.clear();
		}
		
		if(!Util.isNullOuVazio(imovelRural.getIndApp()) && imovelRural.getIndApp() && !Util.isNullOuVazio(imovelRural.getAppCollection())){
			List<Integer> localizacoesApp = new ArrayList<Integer>();
			
			for (App app : imovelRural.getAppCollection()) {
				localizacoes.clear();
				localizacoes.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				
				GeoJsonSicar geoJsonApp = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
				
				if(!Util.isNullOuVazio(geoJsonApp)) {
					app.setGeoJsonSicar(geoJsonApp);
					localizacoesApp.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				} else {
					throw new JSONException("Falha ao carregar objeto Geo App: Geometria não disponível.");
				}
			}
			
			GeoJsonSicar geoJsonAppTotal = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoesApp);
			
			if(!Util.isNullOuVazio(geoJsonAppTotal)) {
				imovelRural.setGeoJsonSicarAppTotal(geoJsonAppTotal);
			} else {
				throw new JSONException("Falha ao carregar objeto Geo App Total: Geometria não disponível.");
			}
			
			localizacoes.clear();
		}
		
		if(!Util.isNullOuVazio(imovelRural.getIndVegetacaoNativa()) && imovelRural.getIndVegetacaoNativa()){
			localizacoes.add(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			
			GeoJsonSicar geoJsonVn = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			
			if(!Util.isNullOuVazio(geoJsonVn)) {
				imovelRural.getVegetacaoNativa().setGeoJsonSicar(geoJsonVn);
			} else {
				throw new JSONException("Falha ao carregar objeto Geo Vegetacao Nativa: Geometria não disponível.");
			}
			
			localizacoes.clear();
		}
		
		
		if(!Util.isNullOuVazio(imovelRural.getIndAreaRuralConsolidada()) && imovelRural.getIndAreaRuralConsolidada()) {
			localizacoes.add(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			
			GeoJsonSicar geoJsonArc = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			
			if(!Util.isNullOuVazio(geoJsonArc)) {
				imovelRural.getIdeAreaRuralConsolidada().setGeoJsonSicar(geoJsonArc);
			} else {
				throw new JSONException("Falha ao carregar objeto Geo Area Rural Consolidada: Geometria não disponível.");
			}
			
			localizacoes.clear();
		}
	}

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public ImovelRural getImovelRuralPesquisa() {
		return imovelRuralPesquisa;
	}

	public void setImovelRuralPesquisa(ImovelRural imovelRuralPesquisa) {
		this.imovelRuralPesquisa = imovelRuralPesquisa;
	}

	public LazyDataModel<ImovelRural> getListaImoveis() {
		return listaImoveis;
	}

	public void setListaImoveis(LazyDataModel<ImovelRural> listaImoveis) {
		this.listaImoveis = listaImoveis;
	}

	public String getNomImovelRural() {
		return nomImovelRural;
	}

	public void setNomImovelRural(String nomImovelRural) {
		this.nomImovelRural = nomImovelRural;
	}

	public Date getDtcSincronia() {
		return dtcSincronia;
	}

	public void setDtcSincronia(Date dtcSincronia) {
		this.dtcSincronia = dtcSincronia;
	}

	public boolean isIndSincronia() {
		return indSincronia;
	}

	public void setIndSincronia(boolean indSincronia) {
		this.indSincronia = indSincronia;
	}

	public String getFiltroNumCar() {
		return filtroNumCar;
	}

	public void setFiltroNumCar(String filtroNumCar) {
		this.filtroNumCar = filtroNumCar;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getNumSicar() {
		return numSicar;
	}

	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}

	public String getCodRetorno() {
		return codRetorno;
	}

	public void setCodRetorno(String codRetorno) {
		this.codRetorno = codRetorno;
	}

	public boolean isSicarGerado() {
		return sicarGerado;
	}

	public void setSicarGerado(boolean sicarGerado) {
		this.sicarGerado = sicarGerado;
	}

	public String getMensagemRetorno() {
		return mensagemRetorno;
	}

	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}
}