package br.gov.ba.seia.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAnimal;
import br.gov.ba.seia.entity.AssentadoIncra;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.AssociacaoIncra;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaIntervencao;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.ProcessoUsoAgua;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoFinalidadeVegetacaoNativa;
import br.gov.ba.seia.entity.TipoOrigemCertificado;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoBarragemEnum;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;
import br.gov.ba.seia.enumerator.TipoTravessiaDuto;
import br.gov.ba.seia.enumerator.TipoUsoAgua;
import br.gov.ba.seia.enumerator.TipologiaCefirEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.enumerator.UsoBarragem;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.PctImovelRuralFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.UsuarioAutorizacaoGeobahiaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

/**
 * Classe controladora da view detalharImovelRural
 * @author	Carlos Cruz	
 * @date	04/06/2013 
 */
@Named("detalharImovelRuralController")
@ViewScoped
public class DetalharImovelRuralController extends SeiaControllerAb {
	
	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	@EJB
	private PctImovelRuralFacade pctImovelRuralFacade;
	@EJB
	private UsuarioAutorizacaoGeobahiaService usuarioAutorizacaoGeobahiaService;
	
	private static final Object PROCURADOR = 5;
	/*
	 * Variáveis
	 */	
	private UIForm formularioASerLimpo;
	private int activeIndex;
	private ImovelRural imovelRural = new ImovelRural();
	private Pessoa solicitanteVisualizacao;
	private Pessoa requerenteVisualizacao = null;
	private Boolean respondeCefir = false;	
	private boolean habilitaCadastroAmbiental = false;		
	private List<String> listTipoCaptacao;
	private String caminhoArquivo;	
	private List<DocumentoImovelRural> listaArquivoDocumento;
	private List<DocumentoImovelRural> listaArquivoProcuracao;
	private String nomContato;
	private String numTelefone;
	private String desEmail;	
	private Collection<ResponsavelImovelRural> listaResponsaveis;
	private List<DadoGeografico> listaDadosGeo;
	private String coordenadaPontoxy;
	private List<SelectItem> itemsClassifSecGeometrica;
	private CronogramaRecuperacao cronogramaRecuperacaoRl;
	private List<CronogramaEtapa> listaCronogramaEtapaRl = new ArrayList<CronogramaEtapa>();
	private List<DocumentoImovelRural> listaArquivoPradRl;	
	private CronogramaRecuperacao cronogramaRecuperacaoRlSelecionada;
	private Collection<App> listaApp;
	private List<DocumentoImovelRural> listaArquivoPradOP;
	private List<CronogramaRecuperacao> listaCronogramaRecuperacaoExistenteRL;
	private List<CronogramaRecuperacao> listaCronogramaRecuperacaoExistenteApp;
	private Boolean isProcPfPjRepLegal = false;
	private List<SelectItem> collTipoCaptacao;
	private List<TipoVinculoImovel> listaTipoVinculos;
	private List<TipoOrigemCertificado> listaTipoOrigemCertificado;	
	private String caminhoDesenharGeoBahia = URLEnum.CAMINHO_GEOBAHIA.toString();
	private String caminhoNovoGeoBahia = URLEnum.CAMINHO_NOVO_GEOBAHIA.toString();
	private String caminhoGeoBahia = URLEnum.CAMINHO_GEOBAHIA_CEFIR.toString();
	private App appSelecionada;	
	private List<CronogramaEtapa> listaCronogramaEtapaApp = new ArrayList<CronogramaEtapa>();	
	private List<DocumentoImovelRural> listaArquivoPradAppSelecionada;
	private CronogramaRecuperacao cronogramaRecuperacaoAppSelecionada;
	private AreaProdutiva areaProdutivaSelecionada;
	private List<TipoFinalidadeUsoAgua> listFinalidadesSelecionadas;
	private ImovelRuralUsoAgua imovelRuralUsoAguaSelecionada;	
	private TipoBarragemEnum tipoBarragem;
	private List<UsoBarragem> listUsoBarragemEnum;
	private String tipoIntervencaoCorpoHidrico;	
	private String nomTipoIntervencaoCorpoHidrico;
	private Double volumeBarragem;
	private Double areaInundadaBarragem;
	private TipoTravessiaDuto tipoTravessia;
	private Collection<TipoFinalidadeUsoAgua> listTipoFinalidadeUsoAgua;
	private String arquivoSelecionado;
	private StreamedContent arquivoBaixar;
	private List<DocumentoImovelRural> listaArquivoReservaAprovada;	
	private List<DocumentoImovelRural> listaArquivoReservaAverbada;	
	private ReservaLegalAverbada reservalegalAverbada;
	private AssociacaoIncra associacaoIncraSelecionada;
	private AssentadoIncraImovelRural assentadoIncraImovelRuralSelecionado;	
	private ImovelRuralRppn imovelRuralRppn;
	private boolean visualizaImpressao;
	private String tituloProprietariosJustoPossuidores;
	private String lblListaProprietariosJustoPossuidores;
	private String lblDocumentoPossePropriedade;
	private boolean showPCT;
	private PctImovelRural pctImovelRural;
	
	private String token;
	
	public String getTituloProprietariosJustoPossuidores() {
		return tituloProprietariosJustoPossuidores;
	}

	public void setTituloProprietariosJustoPossuidores(
			String tituloProprietariosJustoPossuidores) {
		this.tituloProprietariosJustoPossuidores = tituloProprietariosJustoPossuidores;
	}

	public String getLblListaProprietariosJustoPossuidores() {
		return lblListaProprietariosJustoPossuidores;
	}

	public void setLblListaProprietariosJustoPossuidores(
			String lblListaProprietariosJustoPossuidores) {
		this.lblListaProprietariosJustoPossuidores = lblListaProprietariosJustoPossuidores;
	}

	public String getLblDocumentoPossePropriedade() {
		return lblDocumentoPossePropriedade;
	}

	public void setLblDocumentoPossePropriedade(String lblDocumentoPossePropriedade) {
		this.lblDocumentoPossePropriedade = lblDocumentoPossePropriedade;
	}

	
	/**
	  * Método para inicializar o controller.	  
	  * @author	Carlos Cruz	
	  * @date	04/06/2013
	  */
	@PostConstruct
	public void init() {
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural())){
			imovelRural = ContextoUtil.getContexto().getImovelRural();
			solicitanteVisualizacao = ContextoUtil.getContexto().getSolicitanteRequerimento();
			visualizaImpressao = ContextoUtil.getContexto().getImprimindoImovel();
			token = usuarioAutorizacaoGeobahiaService.carregarUsuarioAutorizacaoGeobahiaCefir();
			if(visualizaImpressao) {
				visualizarImovelRural();
				if(imovelRural.isImovelPCT()) {
					carregarPctImovelRural();
				}
			}
			ContextoUtil.getContexto().setImovelRural(imovelRural);
		}
	}
	
	/**
	  * Método para limpar formulário.	  
	  * @author	Carlos Cruz	
	  * @date	04/06/2013
	  */
	public void limparTela() {
		limparComponentesFormulario(formularioASerLimpo);
	}
	
	public void imprimirImovelRural() {
		ContextoUtil.getContexto().setImovelRural(imovelRural);
		ContextoUtil.getContexto().setDisableUploadDoc(true);
		ContextoUtil.getContexto().setSolicitanteRequerimento(solicitanteVisualizacao);
		ContextoUtil.getContexto().setImprimindoImovel(true);
	}
	
	public void detalharImovelRural(){
		visualizarImovelRural();
		if(imovelRural.isImovelPCT()) {
			carregarPctImovelRural();
		}
		ContextoUtil.getContexto().setImovelRural(this.imovelRural);
		ContextoUtil.getContexto().setDisableUploadDoc(true);
		ContextoUtil.getContexto().setSolicitanteRequerimento(this.solicitanteVisualizacao);
		ContextoUtil.getContexto().setImprimindoImovel(false);
	}	
	
	/**
	  * Método para montar os objetos da visualização dos detalhes do imóvel rural.	  
	  * @author	Carlos Cruz	
	  * @date	04/06/2013
	  */
	public void visualizarImovelRural() {
		this.activeIndex = 0;
		limparTela();	
		this.requerenteVisualizacao = new Pessoa();
		listTipoCaptacao = new ArrayList<String>();
		collTipoCaptacao = new ArrayList<SelectItem>();
		listaCronogramaEtapaApp = new ArrayList<CronogramaEtapa>();
		cronogramaRecuperacaoAppSelecionada = new CronogramaRecuperacao();
		listaCronogramaEtapaRl = new ArrayList<CronogramaEtapa>();
		cronogramaRecuperacaoRl = new CronogramaRecuperacao();
		carregarTipoCapitacao();
		carregarTipoVinculos();
		carregarTipoOrigemCertificado();
		carregarImovelRural();			
		montarRequerimentoImovelRuralVizualizacao();		
		montarDocumentosImovelRural();
		montarRequerenteSolicitante();
		montarResponsaveis();
		montarLocalizacao();		
		List<PessoaImovel> listPropietImovel;
		try {
			listPropietImovel = (List<PessoaImovel>) carregarPessoasDoImovel();
			if(existeProcuradorImovel(listPropietImovel))
				isProcPfPjRepLegal = true;
			else
				isProcPfPjRepLegal = false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);		
		}
		
	}
	
	public void visualizarRelatorioImovelRural() {
		if(!Util.isNull(imovelRural)) {
			try {
				Collection<PessoaImovel> lProprietarios = imovelRuralServiceFacade.filtrarPROPRIETARIOImovel(new Imovel(imovelRural.getIdeImovelRural()));
				if(!Util.isNullOuVazio(lProprietarios)) {
					this.solicitanteVisualizacao = lProprietarios.iterator().next().getIdePessoa(); 
					visualizarImovelRural();
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}

	public void imprimirRelatorioImovelRural() {
		if(!Util.isNull(imovelRural)) {
			try {
				Collection<PessoaImovel> lProprietarios = imovelRuralServiceFacade.filtrarPROPRIETARIOImovel(new Imovel(imovelRural.getIdeImovelRural()));
				if(!Util.isNullOuVazio(lProprietarios)) {
					this.solicitanteVisualizacao = lProprietarios.iterator().next().getIdePessoa(); 
					 imprimirImovelRural();
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	private void carregarImovelRural() {
		try {
			if (!Util.isNullOuVazio(imovelRural)){
				imovelRural = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());
				carregarDocumentoImovelRuralPosse();
				atualizarLabelsPossePropriedade();
				
				if(imovelRural.isImovelPCT()) {
					showPCT=true;
					imovelRural.setIdePctImovelRural(pctImovelRuralFacade.buscarPctImovelRural(imovelRural));
					carregarPctImovelRural();
				}
				
				if(imovelRural.isImovelCDA() && !imovelRural.getIndImovelRuralCdaEditado()) {
					this.imovelRural.getImovel().getIdeEndereco().setDesComplemento(null);
					this.imovelRural.getImovel().getIdeEndereco().setDesPontoReferencia(null);				
					this.imovelRural.getImovel().getIdeEndereco().setIdeLogradouro(new Logradouro());
					this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().setIdeMunicipio(new Municipio());
					this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().setIdeEstado(new Estado());
					imovelRural.setIndImovelRuralCdaEditado(true);
				}
				
				if(showPCT || (!Util.isNullOuVazio(imovelRural.getIndDesejaCompletarInformacoes()) && imovelRural.getIndDesejaCompletarInformacoes())){
					if(!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
						imovelRural.setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelRural.getReservaLegal()));					
						reservalegalAverbada = imovelRuralServiceFacade.carregarReservaLegalAverbada(imovelRural.getReservaLegal());
					}
					imovelRural.setAppCollection(imovelRuralServiceFacade.listarAppByImovelRural(imovelRural));
					imovelRural.setAreaProdutivaCollection(imovelRuralServiceFacade.listarAreaProdutivaByImovelRural(imovelRural));
					imovelRural.setVegetacaoNativa(imovelRuralServiceFacade.listarVegetacaoNativaByImovelRural(imovelRural));
					imovelRural.setImovelRuralUsoAguaCollection(imovelRuralServiceFacade.obterListaUsoAguaImovelRural(imovelRural));
					imovelRuralRppn = imovelRuralServiceFacade.carregarImovelRuralRppnByIdeImovelRural(imovelRural);
					imovelRural.setAssentadoIncraImovelRuralCollection(imovelRuralServiceFacade.listarAssentadoIncraImovelRuralPorImovelRural(imovelRural));
					carregarImovelRuralUsoAgua();
					carregarAsentadosIncraImovelRural();
					carregarAreasProdutivas();
				}
				
				
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}			
	}
	
	private void carregarAreasProdutivas() {
		try {
			List<AreaProdutiva> aps = new ArrayList<AreaProdutiva>();
			if (!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())) {
				for (AreaProdutiva ap : imovelRural.getAreaProdutivaCollection()) {
					ap = imovelRuralServiceFacade.carregarTudoAreaProdutiva(ap);
					
					if(ap.possuiTipologiaSubGrupoCadastrada()) {
						switch (ap.getIdeTipologiaSubgrupo().getIdeTipologia()) {
						case 6:
							if (!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
								for (AreaProdutivaTipologiaAtividade ta : ap.getAreaProdutivaTipologiaAtividadeCollection()) {
									ta.setIdeAreaProdutivaTipologiaAtividadeAgricultura(imovelRuralServiceFacade.carregarTipologiaAtividadeAgriculturaByIde(ta));
								}
							}
							break;
						case 8:
						case 10:
							if (!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
								for (AreaProdutivaTipologiaAtividade ta : ap.getAreaProdutivaTipologiaAtividadeCollection()) {
									ta.setIdeAreaProdutivaTipologiaAtividadeAnimal(imovelRuralServiceFacade.carregarTipologiaAtividadeAnimalByIde(ta));
								}
							}
							break;
						case 16:
							if (!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
								for (AreaProdutivaTipologiaAtividade ta : ap.getAreaProdutivaTipologiaAtividadeCollection()) {
									ta.setIdeAreaProdutivaTipologiaAtividadePiscicultura(imovelRuralServiceFacade.carregarTipologiaAtividadePisciculturaByIde(ta.getIdeAreaProdutivaTipologiaAtividadePiscicultura().getIdeAreaProdutivaTipologiaAtividadePiscicultura()));
								}
							}
							break;
						}
					}
					
					aps.add(ap);
				}
			}
			imovelRural.setAreaProdutivaCollection(new ArrayList<AreaProdutiva>(aps));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void atualizarLabelsPossePropriedade() {
		if(this.getRetornaTipoVinculoImovel().getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)){
			if(!imovelRuralServiceFacade.verificarExistenciaProprietarios(imovelRural.getImovel().getPessoaImovelCollection())) {
				this.tituloProprietariosJustoPossuidores = Util.getString("cefir_lbl_justos_possidores_atuais_imovel");
				this.lblListaProprietariosJustoPossuidores = Util.getString("cefir_lbl_justo_possuidores");
			} else {
				this.tituloProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_atuais_imovel");
				this.lblListaProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_imovel");
			}
			
			this.lblDocumentoPossePropriedade = Util.getString("cefir_lbl_documento_posse");
		} else {
			this.tituloProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_atuais_imovel");
			this.lblListaProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_imovel");
			this.lblDocumentoPossePropriedade = Util.getString("cefir_lbl_documento_propriedade");
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
	
	private void carregarAsentadosIncraImovelRural(){
		try {
			if(!Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())){
				for (AssentadoIncraImovelRural assentado : imovelRural.getAssentadoIncraImovelRuralCollection()){
					assentado.setAssociacaoAssentadoImovelRuralIncraCollection(imovelRuralServiceFacade.listarAssociacaoAssentadoImovelRuralIncraPorAssentadoIncraImovelRural(assentado));
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void carregarTipoOrigemCertificado() {
		try {
			listaTipoOrigemCertificado = (List<TipoOrigemCertificado>) imovelRuralServiceFacade.listarTipoOrigemCertificado();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void carregarTipoVinculos() {
		try {
			listaTipoVinculos = (List<TipoVinculoImovel>) imovelRuralServiceFacade.listarTipoVinculoImoveis();
			listaTipoVinculos.remove(4);
			listaTipoVinculos.remove(3);
			listaTipoVinculos.remove(2);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarTipoCapitacao() {
		try {
			Collection<TipoCaptacao> collTipoCapitacao = imovelRuralServiceFacade.listarTodosTipoCaptacao();
			for (TipoCaptacao tipoCaptacao : collTipoCapitacao) {
				SelectItem item = new SelectItem(tipoCaptacao.getIdeTipoCaptacao(), tipoCaptacao.getNomTipoCaptacao());
				this.collTipoCaptacao.add(item);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void montarRequerimentoImovelRuralVizualizacao() {
		if(!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())){						
			
			//REGRA PARA IMOVEIS RURAIS MAIORES QUE 4 MF
			if(!Util.isNullOuVazio(imovelRural.getIndDesejaCompletarInformacoes()) && imovelRural.getIndDesejaCompletarInformacoes() || imovelRural.isImovelPCT()){
				this.respondeCefir = true;
			}else{
				this.respondeCefir = false;
			}
			
			if (!Util.isNullOuVazio(imovelRural.getIndConcessionaria()) && imovelRural.getIndConcessionaria())
				this.listTipoCaptacao.add("1");
			if (!Util.isNullOuVazio(imovelRural.getIndPrecipitacao()) && imovelRural.getIndPrecipitacao())
				this.listTipoCaptacao.add("2");
			if (!Util.isNullOuVazio(imovelRural.getIndSubterraneo()) && imovelRural.getIndSubterraneo())
				this.listTipoCaptacao.add("4");
			if (!Util.isNullOuVazio(imovelRural.getIndSuperficial()) && imovelRural.getIndSuperficial())
				this.listTipoCaptacao.add("3");
						
			if(!Util.isNullOuVazio(imovelRural.getIndDesejaCompletarInformacoes()) && imovelRural.getIndDesejaCompletarInformacoes() || imovelRural.isImovelPCT()){
					this.respondeCefir = true;					
					habilitaCadastroAmbiental = true;
			}
			if(habilitaCadastroAmbiental){
				montarCadastroAmbiental();					
			}
			
			this.listaArquivoReservaAprovada = new ArrayList<DocumentoImovelRural>(); 
			if(!Util.isNullOuVazio(imovelRural.getReservaLegal()) && !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeDocumentoAprovacao())){
				this.listaArquivoReservaAprovada.add(imovelRural.getReservaLegal().getIdeDocumentoAprovacao());
			}
			
			this.listaArquivoReservaAverbada = new ArrayList<DocumentoImovelRural>();
			if(!Util.isNullOuVazio(reservalegalAverbada)){
				this.listaArquivoReservaAverbada.add(reservalegalAverbada.getIdeDocumentoAverbacao());
			}
		}
	}
	
	private Collection<PessoaImovel> carregarPessoasDoImovel() throws Exception {
		return imovelRuralServiceFacade.filtrarPessoasPorImovel(imovelRural.getImovel());
	}
	
	private void montarDocumentosImovelRural(){
		caminhoArquivo = null;
		listaArquivoDocumento = new ArrayList<DocumentoImovelRural>();
		
		if(!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse()) && imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural() != null) {
			listaArquivoDocumento.add(imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural());
		}
		
		listaArquivoProcuracao = new ArrayList<DocumentoImovelRural>();
		
		if(imovelRural.getIdeDocumentoProcuracao() != null)
			listaArquivoProcuracao.add(imovelRural.getIdeDocumentoProcuracao());
	}
	
	private void montarRequerenteSolicitante(){
		if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())){
			try {
				if(!Util.isNullOuVazio(this.imovelRural.getIdeRequerenteCadastro())) {
					this.requerenteVisualizacao = this.imovelRural.getIdeRequerenteCadastro();
				}				
			} catch (Exception e1) {			
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
			}
			
			if (!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento())){
				solicitanteVisualizacao = ContextoUtil.getContexto().getSolicitanteRequerimento();
			}
			
			if (solicitanteVisualizacao != null) {
				List<Telefone> listaTel = null;
				try {
					listaTel = imovelRuralServiceFacade.filtraTelefonePorPessoa(solicitanteVisualizacao);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
				if (!Util.isNullOuVazio(solicitanteVisualizacao.getPessoaFisica()) && !Util.isNullOuVazio(solicitanteVisualizacao.getPessoaFisica().getNomPessoa())) {
					nomContato = solicitanteVisualizacao.getPessoaFisica().getNomPessoa();
					if (!Util.isNullOuVazio(solicitanteVisualizacao.getPessoaFisica().getPessoa().getDesEmail()))
						desEmail = solicitanteVisualizacao.getPessoaFisica().getPessoa().getDesEmail();
				} else if (!Util.isNullOuVazio(solicitanteVisualizacao.getPessoaJuridica())) {
					nomContato = solicitanteVisualizacao.getPessoaJuridica().getNomRazaoSocial();
					List<RepresentanteLegal> listRepLegal;
					try {
							listRepLegal = imovelRuralServiceFacade.listaRepresentanteLegalByPessoa(solicitanteVisualizacao.getPessoaJuridica());
							for (RepresentanteLegal representanteLegal : listRepLegal) {
								if(!Util.isNullOuVazio(representanteLegal.getIdePessoaFisica().getPessoa().getDesEmail()))
									desEmail = representanteLegal.getIdePessoaFisica().getPessoa().getDesEmail();
								}				
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					}
				}
						
				if (!listaTel.isEmpty()) {
					try {
						if (listaTel.size() > 1) {
							numTelefone = getTelefoneParaRequerimento(listaTel).getNumTelefone();
						} else {
							numTelefone = listaTel.get(0).getNumTelefone();
						}
					} catch (Exception e) {
						numTelefone = listaTel.get(0).getNumTelefone();
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					}
				}
			}
		}
	}
	
	/**
	 * Retona o telefone segundo a regra: primeiramente o celular, se não tiver
	 * celular, retorna o comercial e por último o residencial.
	 * 
	 * @param collectionTel
	 * @return Telefone
	 */
	public Telefone getTelefoneParaRequerimento(List<Telefone> collectionTel) {
		Telefone tel = null;
		Telefone telRequerimento = null;
		Boolean isComercial = Boolean.FALSE;
		// Loop para pegar primeiramente o celular, se não tiver cel, pega o
		// comercial e por ultimo o residencial.
		for (Telefone telefone : collectionTel) {
			tel = telefone;
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.CELULAR.getId()) {
				telRequerimento = tel;
				break;
			}
			if (!isComercial && tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.RESIDENCIAL.getId()) {
				telRequerimento = tel;
			}
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.COMERCIAL.getId()) {
				telRequerimento = tel;
				isComercial = true;
			}
		}
		return telRequerimento;
	}
	
	public void montarResponsaveis() {
		if (!Util.isNullOuVazio(this.imovelRural)) {
			try {
				imovelRural.setResponsavelImovelRuralCollection(imovelRuralServiceFacade.filtrarResponsaveisPorImovelRural(this.imovelRural));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}

	}
	
	private void montarLocalizacao(){
		if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeografica())) {
			try {
				if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()) && imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(1)) {
					listaDadosGeo = imovelRuralServiceFacade.listarDadoGeografico(imovelRural.getIdeLocalizacaoGeografica(), imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao());
					if (!Util.isNullOuVazio(listaDadosGeo))
					this.coordenadaPontoxy = reorganizarCoordenadaPonto(this.listaDadosGeo.get(0).getCoordGeoNumerica());
				}
			}
			 catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		} else {
			imovelRural.setIdeLocalizacaoGeografica(new LocalizacaoGeografica()); 
		}
	}
	
	public void obterClassificacoes() {
		try {
			itemsClassifSecGeometrica = new ArrayList<SelectItem>();
			itemsClassifSecGeometrica.add(new SelectItem("", ResourceBundle.getBundle("/Bundle").getString("geral_lbl_selecione")));
			for (ClassificacaoSecaoGeometrica classifSecGeometrica : imovelRuralServiceFacade.listarClassificacaoSecaoGeometrica()) {
				itemsClassifSecGeometrica.add(new SelectItem(classifSecGeometrica, classifSecGeometrica.getNomClassificacaoSecao()));
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	private String reorganizarCoordenadaPonto(String ponto){
		String aux = ponto.substring(7,ponto.length() - 1);
		String[] c = aux.split(" ");
		aux = c[1]+" "+c[0];
		return aux;				
	}
	
	private Boolean existeProcuradorImovel(List<PessoaImovel> listPropietImovel) {
		for (PessoaImovel pessoaImovel : listPropietImovel) {
			if(pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel() == 5){
				return true;
			}
		}
		return false;
	}

	public TipoVinculoImovel getRetornaTipoVinculoImovel(){
		try {
			if(!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getImovel())){
				imovelRural.getImovel().setPessoaImovelCollection(imovelRuralServiceFacade.filtrarPROPRIETARIOImovel(imovelRural.getImovel()));
				//VERIFICA SE O REQUERENTE ESTA ENTRE OS PROPRIETARIOS
				for (PessoaImovel pImovel : imovelRural.getImovel().getPessoaImovelCollection()) {	
					if(pImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(2)) {
						return new TipoVinculoImovel(2, "Justo Possuidor");
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new TipoVinculoImovel(1, "Proprietário");
	}
		
	/**
	  * Método para montar o cadastro ambiental do imóvel.	  
	  * @author	Carlos Cruz	
	  * @date	04/06/2013 
	  */
	private void montarCadastroAmbiental() {
		if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getIdeImovelRural())) {
			this.cronogramaRecuperacaoRl = new CronogramaRecuperacao();
			this.listaArquivoPradRl = new ArrayList<DocumentoImovelRural>();
			this.listaArquivoPradAppSelecionada = new ArrayList<DocumentoImovelRural>();
			this.listaArquivoPradOP = new ArrayList<DocumentoImovelRural>();
			try {
				//Pegando dados do estado de conservação
				if (!Util.isNullOuVazio(imovelRural.getReservaLegal()) && !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoEstadoConservacao())) {
					if (imovelRural.getReservaLegal().getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId()) || imovelRural.getReservaLegal().getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.DEGRADADA.getId())) {
						this.cronogramaRecuperacaoRl = imovelRuralServiceFacade.listarCronogramaRecuperacaoByReservaLegal(imovelRural.getReservaLegal());
						this.listaCronogramaEtapaRl = imovelRuralServiceFacade.listarCronogramaEtapaByCronogramaRecuperacao(this.cronogramaRecuperacaoRl);						
						
						//Pegando o PRAD
						if(!Util.isNullOuVazio(this.cronogramaRecuperacaoRl.getIdeDocumentoObrigatorio())){							
							this.listaArquivoPradRl.add(this.cronogramaRecuperacaoRl.getIdeDocumentoObrigatorio());	
						}
					}
				}
				
				//OUTROS PASSIVOS AMBIENTAIS
				if(!Util.isNullOuVazio(imovelRural.getIndOutrosPassivos()) && this.imovelRural.getIndOutrosPassivos()){
					if(!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais())) {
						imovelRural.setOutrosPassivosAmbientais(imovelRuralServiceFacade.carregarTudoOutrosPassivosAmbientais(imovelRural.getOutrosPassivosAmbientais()));
						if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad())){
							listaArquivoPradOP.add(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad());						
						}
					}
				}
	
			} catch (Exception e1) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
			}
			
			//App
			if (!Util.isNullOuVazio(imovelRural.getIndApp()) && imovelRural.getIndApp()) {				
				try {
					if(!Util.isNullOuVazio(cronogramaRecuperacaoRl.getIndPradImportada()) && cronogramaRecuperacaoRl.getIndPradImportada()) {
						carregarCronogramaRecuperacao();
					}
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}			
		}
		
	}
	
	public void montarObjetoApp() {
		try {
			//Pegando dados do estado de conservação
			if (!Util.isNullOuVazio(appSelecionada.getIdeTipoEstadoConservacao())) {
				if (appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId()) || appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.DEGRADADA.getId())) {
					
					cronogramaRecuperacaoAppSelecionada = imovelRuralServiceFacade.listarCronogramaRecuperacaoByApp(appSelecionada);
					listaCronogramaEtapaApp = imovelRuralServiceFacade.listarCronogramaEtapaByCronogramaRecuperacao(cronogramaRecuperacaoAppSelecionada);					//Pegando o PRAD
					if(!Util.isNullOuVazio(this.cronogramaRecuperacaoAppSelecionada.getIdeDocumentoObrigatorio())){
						listaArquivoPradAppSelecionada.clear();
						listaArquivoPradAppSelecionada.add(cronogramaRecuperacaoAppSelecionada.getIdeDocumentoObrigatorio());						
						if(!Util.isNullOuVazio(cronogramaRecuperacaoAppSelecionada.getIndPradImportada()) && cronogramaRecuperacaoAppSelecionada.getIndPradImportada()) {
							carregarCronogramaRecuperacao();
						}
					}
					
				}else{
					cronogramaRecuperacaoAppSelecionada = null;
					listaCronogramaEtapaApp = null;	
				}
			}			
								
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public String getLabelAtividadesDesenvolvidas(AreaProdutiva areaProdutiva) {
		
		String label = "";			
			try {	
					
				if (!Util.isNullOuVazio(areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection()) && !Util.isNullOuVazio(areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection()) && !isExcecaoLabelAtividade(areaProdutiva)) {
					label += areaProdutiva.getIdeTipologiaSubgrupo().getDesTipologia()+": ";
					for (AreaProdutivaTipologiaAtividade areaProdutivaTipologiaAtividade : areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection()) {
						label += areaProdutivaTipologiaAtividade.getIdeTipologiaAtividade().getDscTipologiaAtividade() + " - ";					
					}
					label = label.substring(0, label.length() - 3);
				} else if (areaProdutiva.possuiTipologiaSubGrupoCadastrada()) {	
					label += areaProdutiva.getIdeTipologia().getDesTipologia()+": "+areaProdutiva.getIdeTipologiaSubgrupo().getDesTipologia();
				} else if (areaProdutiva.possuiTipologiaCadastrada()) {
					label += areaProdutiva.getIdeTipologia().getIdeTipologiaPai().getDesTipologia()+": "+areaProdutiva.getIdeTipologia().getDesTipologia();
				} else {
					label += areaProdutiva.getIdeTipologia().getIdeTipologiaPai().getDesTipologia();
				}				
			} catch (Exception e) {
			}
			return label;
	}

	private boolean isExcecaoLabelAtividade(AreaProdutiva areaProdutiva) {
		if(areaProdutiva.possuiTipologiaSubGrupoCadastrada()) {
			if(areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.AGRICULTURA_DE_SEQUEIRO.getId()) 
					|| areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.AGRICULTURA_IRRIGADA.getId())
					|| areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.PECUARIA.getId())) {
				return true;				
			}
		}
		return false;
	}
	
	public void montarObjetoAreaProdutiva() {
		try {
			areaProdutivaSelecionada.getIdeTipologia().setIdeTipologiaPai(imovelRuralServiceFacade.carregarTipologiaPaiByIdeFilho(areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia()));
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public String converteNumToDscUsoAgua(Integer pUsoAgua){
		switch (pUsoAgua) {
			case 1:
				return "Dessedentação";
			case 2:
				return "Limpeza";
			case 3:
				return "Dessedentação e Limpeza";
			default:				
				return "Nenhum uso informado";
		}
	}
	
	public String converteNumToDscMeses(String pMeses){
		String aux = "";
		
		for (String p : pMeses.split("-")) {
			if (p.contains("1")) 
				aux += "JAN, ";
			if (p.contains("2")) 
				aux += "FEV, ";
			if (p.contains("3")) 
				aux += "MAR, ";
			if (p.contains("4")) 
				aux += "ABR, ";
			if (p.contains("5")) 
				aux += "MAI, ";
			if (p.contains("6")) 
				aux += "JUN, ";
			if (p.contains("7")) 
				aux += "JUL, ";
			if (p.contains("8")) 
				aux += "AGO, ";
			if (p.contains("9")) 
				aux += "SET, ";
			if (p.contains("10")) 
				aux += "OUT, ";
			if (p.contains("11")) 
				aux += "NOV, ";
			if (p.contains("12")) 
				aux += "DEZ, ";
		}
		aux = aux.substring (0, aux.length()-2);
		return aux;
	}	
	
	public void carregarCronogramaRecuperacao() {
		try {
			
			if(cronogramaRecuperacaoRl.getIndPradImportada() != null && cronogramaRecuperacaoRl.getIndPradImportada()) {
				Map<Integer, CronogramaRecuperacao> documentosCadastradosAppReserva = obterListaDocumentosAppReservaLegal();
				listaCronogramaRecuperacaoExistenteRL = new ArrayList<CronogramaRecuperacao>();
				listaCronogramaRecuperacaoExistenteRL.addAll(documentosCadastradosAppReserva.values());
			}
			if(cronogramaRecuperacaoAppSelecionada.getIndPradImportada() != null && cronogramaRecuperacaoAppSelecionada.getIndPradImportada()) {
				Map<Integer, CronogramaRecuperacao> mapAux = obterListaDocumentosAppReservaLegal();
				listaCronogramaRecuperacaoExistenteApp = new ArrayList<CronogramaRecuperacao>();
				listaCronogramaRecuperacaoExistenteApp.addAll(mapAux.values());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private Map<Integer, CronogramaRecuperacao> obterListaDocumentosAppReservaLegal() throws Exception {
		Map<Integer, CronogramaRecuperacao> mapAux = new HashMap<Integer, CronogramaRecuperacao>();
		addApp(mapAux);
		addReservaLegal(mapAux);
		return mapAux;
	}
	
	private void addApp(Map<Integer, CronogramaRecuperacao> mapAux) throws Exception {
		if(!Util.isNullOuVazio(this.listaApp)) {
			Iterator<App> iterator = this.listaApp.iterator();
			while(iterator.hasNext()) {
				CronogramaRecuperacao lCronograma = imovelRuralServiceFacade.listarCronogramaRecuperacaoByApp(iterator.next());
				if(!Util.isNullOuVazio(lCronograma)) {
					if(!Util.isNullOuVazio(lCronograma.getIdeDocumentoObrigatorio())) {
						mapAux.put(lCronograma.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio(), lCronograma);
					}
				}
			}
		}
	}
	
	private void addReservaLegal(Map<Integer, CronogramaRecuperacao> mapAux) throws Exception {
		List<CronogramaRecuperacao> lListCronogramaRL = imovelRuralServiceFacade.listarCronogramaRecuperacaoById(cronogramaRecuperacaoRl);
		for (CronogramaRecuperacao lCronograma : lListCronogramaRL) {
			this.cronogramaRecuperacaoRlSelecionada = lCronograma;
			if(!Util.isNullOuVazio(lCronograma.getIdeDocumentoObrigatorio())) {
				mapAux.put(lCronograma.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio(), lCronograma);
			}
		}
	}
	
	public String buscarNomeTipoIntervencao(ImovelRuralUsoAgua imovelRuralUsoAgua){
		ImovelRuralUsoAguaIntervencao pIRUAguaIntervencao = imovelRuralUsoAgua.getImovelRuralUsoAguaIntervencaoCollection().iterator().next();
		return pIRUAguaIntervencao.getIdeTipoIntervencao().getNomTipoIntervencao(); 
	}
	
	public boolean existeTheGeomByIdeLocGeo(Integer pIdeLocGeo) throws Exception{
		
		return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(pIdeLocGeo);
	}
			
	/**
	  * Método que verifica se o usuário é externo.	  
	  * @author	Carlos Cruz	
	  * @date	04/06/2013
	  * @return	true/false 
	  */
	public boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	public String getLinkGeobahia(Integer ideImovel) {
		String linkGeobahia = "";
		
		try{			
			StringBuilder buffer = getURLGeoBahia(ideImovel);
			linkGeobahia = ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
		
		return linkGeobahia;
	}
	
	public String getLinkGeobahiaRL(Integer ideImovel) {
		String linkGeobahiaRL = "";
		
		try {
			StringBuilder buffer = getURLGeoBahia(ideImovel);
			ReservaLegal lReserva = imovelRuralServiceFacade.buscaReservaLegalByImovelRural(new ImovelRural(ideImovel));
			
			if(!Util.isNullOuVazio(lReserva) && !Util.isNullOuVazio(lReserva.getIdeLocalizacaoGeografica())) {
				buffer = getURLGeoBahia("idloc="+lReserva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				linkGeobahiaRL = ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
			}			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
		
		return linkGeobahiaRL;
	}
	
	private StringBuilder getURLGeoBahia(Integer ideImovel) throws IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo("idimov="+ideImovel+"&res=640%20480"));
	}
	
	private StringBuilder getURLGeoBahia(String parametros) throws IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo(parametros));
	}
	
	private String obterStringUrlGeoBahiaPorTipo(String parametros) {
		return URLEnum.CAMINHO_GEOBAHIA_CERTIFICADO + parametros;
	}
	
	private StringBuilder criarStreamComUrl(String pUrl) throws IOException {
		URL url = null;
		BufferedReader br = null;
		InputStreamReader inputStreamReader = null;
		StringBuilder buffer = new StringBuilder();
		try {
			url = new URL(pUrl);
			inputStreamReader = new InputStreamReader(url.openStream());
			br = new BufferedReader(inputStreamReader);
			String linha;
			while ((linha = br.readLine()) != null) {
				buffer.append(linha);
			}
		} catch (MalformedURLException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (IOException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally {
			if(br != null) {
				br.close();
			}
			if(inputStreamReader != null) {
				inputStreamReader.close();
			}
		}

		return buffer;
	}
	
	public void prepararExibicaoPontoCaptacao() {
		carregarTipoFinalidadeUsoAgua(imovelRuralUsoAguaSelecionada);
		if (!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada)) {
			
			if (!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getImovelRuralUsoAguaIntervencaoCollection())) {
				atribuirValoresVariaveisTelaUsoBarragem(getImovelRuralUsoAguaIntervencaoSelecionada());
				atribuirValoresVariaveisTelaTipoBarragem(getImovelRuralUsoAguaIntervencaoSelecionada());
				atribuirValoresVariaveisTelaTipoTravessiaDuto(getImovelRuralUsoAguaIntervencaoSelecionada());
			}
			tratarListaProcessoUsoAgua();			
		}
	}
	
	private void atribuirValoresVariaveisTelaTipoBarragem(ImovelRuralUsoAguaIntervencao pIRUAguaIntervencao) {
		for (TipoBarragemEnum t : TipoBarragemEnum.values()) {
			if (t.getId().toString().equals(pIRUAguaIntervencao.getTipoBarragem())) {
				tipoBarragem = t;
				break;
			}
		}
	}
	
	private void carregarImovelRuralUsoAgua() {
		try {
			List<ImovelRuralUsoAgua> listUsoAguaImovelRural = new ArrayList<ImovelRuralUsoAgua>();
			
			for(ImovelRuralUsoAgua imovelRuralUsoAgua : imovelRural.getImovelRuralUsoAguaCollection()) {
				imovelRuralUsoAgua = imovelRuralServiceFacade.obterPorId(imovelRuralUsoAgua);
				imovelRuralUsoAgua.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
				if(!Util.isNullOuVazio(imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal()))
				imovelRuralUsoAgua.setIdeLocalizacaoGeograficaFinal(imovelRuralServiceFacade.carregarLocalizacaoGeografica(imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().getIdeLocalizacaoGeografica()));
				listUsoAguaImovelRural.add(imovelRuralUsoAgua);
			}
			imovelRural.setImovelRuralUsoAguaCollection(new ArrayList<ImovelRuralUsoAgua>(listUsoAguaImovelRural));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}		
	}
	
	public List<ImovelRuralUsoAgua> filtrarListaUsoAguaImovelRuralPorTipo(String pTipo){
		List<ImovelRuralUsoAgua> listUsoAguaImovelRural = new ArrayList<ImovelRuralUsoAgua>();
		if(!Util.isNullOuVazio(imovelRural.getImovelRuralUsoAguaCollection())){
			for (ImovelRuralUsoAgua imovelRuralUsoAgua : imovelRural.getImovelRuralUsoAguaCollection()) {
				if (imovelRuralUsoAgua.getTipoUso().equals(pTipo)) {
					listUsoAguaImovelRural.add(imovelRuralUsoAgua);
				}
			}
		}
		return listUsoAguaImovelRural;
	}
	
	private void carregarTipoFinalidadeUsoAgua(ImovelRuralUsoAgua imovelRuralUsoAgua) {
		try {
			List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> listImovelRuralUsoAguaTipoFinalidadeUsoAgua = imovelRuralServiceFacade.listarImovelRuralUsoAguaTipoFinalidadeUsoAgua(imovelRuralUsoAgua);
			listFinalidadesSelecionadas = new ArrayList<TipoFinalidadeUsoAgua>();
			if(!Util.isNullOuVazio(listImovelRuralUsoAguaTipoFinalidadeUsoAgua)) {
				for (ImovelRuralUsoAguaTipoFinalidadeUsoAgua imovelRuralUsoAguaTipoFinalidadeUsoAgua : listImovelRuralUsoAguaTipoFinalidadeUsoAgua) {
					listFinalidadesSelecionadas.add(imovelRuralUsoAguaTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua());
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}	
	}
	
	private void atribuirValoresVariaveisTelaUsoBarragem(ImovelRuralUsoAguaIntervencao pIRUAguaIntervencao) {
		List<UsoBarragem> uB = new ArrayList<UsoBarragem>();
		boolean flagTodos = false;
		if (!Util.isNullOuVazio(pIRUAguaIntervencao.getUsoBarragem())) {
			for (UsoBarragem u : UsoBarragem.values()) {

				if (u.getId().toString().equals(pIRUAguaIntervencao.getUsoBarragem()) && !u.equals(UsoBarragem.TODOS)) {
					uB.add(u);
				} else if (pIRUAguaIntervencao.getUsoBarragem().trim().equals(UsoBarragem.TODOS.getId().toString())) {
					flagTodos = true;
					break;
				}
			}
			if (uB.isEmpty() && flagTodos) {
				uB.add(UsoBarragem.CAPTACAO);
				uB.add(UsoBarragem.LANCAMENTO);
			}
		}
		setListUsoBarragemEnum(uB);
	}
	
	public ImovelRuralUsoAguaIntervencao getImovelRuralUsoAguaIntervencaoSelecionada(){
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getImovelRuralUsoAguaIntervencaoCollection())){
			return imovelRuralUsoAguaSelecionada.getImovelRuralUsoAguaIntervencaoCollection().iterator().next();
		}else{
			return new ImovelRuralUsoAguaIntervencao();
		}		
	}
	
	public List<UsoBarragem> getListaUsoBarragem() {
		UsoBarragem[] values = UsoBarragem.values();
		List<UsoBarragem> list = new ArrayList<UsoBarragem>();
		for (UsoBarragem usoBarragem : values) {
			if (!usoBarragem.equals(UsoBarragem.TODOS)) {
				list.add(usoBarragem);
			}
		}
		return list;
	}
	
	private void atribuirValoresVariaveisTelaTipoTravessiaDuto(ImovelRuralUsoAguaIntervencao pIRUAguaIntervencao) {
		for (TipoTravessiaDuto t : TipoTravessiaDuto.values()) {
			if (t.getId().toString().equals(pIRUAguaIntervencao.getTipoTravessia())) {
				tipoTravessia = t;
				break;
			}
		}
	}
	
	private void tratarListaProcessoUsoAgua() {
		List<ProcessoUsoAgua> p = (List<ProcessoUsoAgua>) imovelRuralUsoAguaSelecionada.getProcessoUsoAguaCollection();
		List<ProcessoUsoAgua> processoCorrente = new ArrayList<ProcessoUsoAgua>(p);
		for (ProcessoUsoAgua processoUsoAgua : p) {
			if (processoUsoAgua.isIndExcluido()) {
				processoCorrente.remove(processoUsoAgua);
			}
		}
		imovelRuralUsoAguaSelecionada.setProcessoUsoAguaCollection(new ArrayList<ProcessoUsoAgua>(processoCorrente));		
	}
		
		
	public String obterPontoFormatado(LocalizacaoGeografica localizacaoGeografica) {
		if(!Util.isNullOuVazio(localizacaoGeografica.getDadoGeograficoCollection())){

			DadoGeografico dadoGeografico = localizacaoGeografica.getDadoGeograficoCollection().iterator().next();
			CoordenadaGeografica coordenadaGeografica =GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica());

			String nome = "";
			if(!Util.isNullOuVazio(localizacaoGeografica.getDesLocalizacaoGeografica())) {
				nome += "- "+ localizacaoGeografica.getDesLocalizacaoGeografica();
			}
			
			return coordenadaGeografica.getLatitude().getAsGD()+ "S " + coordenadaGeografica.getLongitude().getAsGD()+"W " + nome;
		}
		
		return "";
	}
	
	/**
	 * Se lat = true retorna latitude
	 * Se lat = true retorna longitude
	 * Se lat e lon = false retorna os dois concatenados 
	 * @param lat
	 * @param lon
	 * @param localizacaoGeografica
	 * @return latitude e/ou longitude
	 */
	public String obterLatLongPorLocGeo(Boolean lat, Boolean lon, LocalizacaoGeografica localizacaoGeografica) {
		if(!Util.isNullOuVazio(localizacaoGeografica.getDadoGeograficoCollection())){
			
			DadoGeografico dadoGeografico = localizacaoGeografica.getDadoGeograficoCollection().iterator().next();
			CoordenadaGeografica coordenadaGeografica =GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica());
			
			if(lat){
				return coordenadaGeografica.getLatitude().getAsGD();
			}
			else if(lon){
				return coordenadaGeografica.getLongitude().getAsGD();
			}
			else{				
				return  coordenadaGeografica.getLongitude().getAsGD() + " " + coordenadaGeografica.getLatitude().getAsGD();
			}
		}
		return"";
	}
	
	public Collection<TipoFinalidadeUsoAgua> obterTipoFinalidadeUsoAgua() {
		try {
			if (!Util.isNull(imovelRuralUsoAguaSelecionada) && !Util.isNull(imovelRuralUsoAguaSelecionada.getTipoUso())) {
				final String tipoUso = imovelRuralUsoAguaSelecionada.getTipoUso();
				ModalidadeOutorgaEnum modalidadeUsoAgua = null;
				if (tipoUso.equals(TipoUsoAgua.SUPERFICIAL.getId().toString())) {
					modalidadeUsoAgua = ModalidadeOutorgaEnum.CAPTACAO_SUPERFICIAL;
				} else if (tipoUso.equals(TipoUsoAgua.SUBTERRANEO.getId().toString())) {
					modalidadeUsoAgua = ModalidadeOutorgaEnum.CAPTACAO_SUBTERRANEA;
				}
				if (modalidadeUsoAgua != null) {
					return this.tipoFinalidadeUsoAguaService.buscarFinalidadeUsoAguaByIdeTipologia(modalidadeUsoAgua
							.getIdTipologia());
				}
			}									
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);			
		}
		return null;
	}
	
	/*
	 * Getters and Setters.
	*/
	
	public String getLatitudePonto1(){
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica())){
			
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLatitude().getAsGD() + "S";
		}else{
			return "";
		}
	}
	
	public String getLongitudePonto1(){
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica())){
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLongitude().getAsGD() + "W";
		}else{
			return "";
		}
	}
	
	public String getFracaoGrauLatitudePonto1() {
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica())){
			
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLatitude().getAsGD();
			
		}else{
			return "";
		}
	}
	
	public String getFracaoGrauLongitudePonto1() {
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica())){
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLongitude().getAsGD();
		}else{
			return "";
		}	
	}
	
	public String getLatitudePonto2(){
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeograficaFinal())){
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLatitude().getAsGD() + "S";
			
		}else{
			return "";
		}	
	}
	
	public String getLongitudePonto2(){
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeograficaFinal())){
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLongitude().getAsGD() + "W";
		}else{
			return "";
		}
	}
	
	public String getFracaoGrauLatitudePonto2() {
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeograficaFinal())){
			
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLatitude().getAsGD();
		}else{
			return "";
		}
	}
	
	public String getFracaoGrauLongitudePonto2() {
		if(!Util.isNullOuVazio(imovelRuralUsoAguaSelecionada) && !Util.isNullOuVazio(imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeograficaFinal())){
			DadoGeografico dadoGeografico= imovelRuralUsoAguaSelecionada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			return GeoUtil.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica()).getLongitude().getAsGD();
		}else{
			return "";
		}
	}
	
	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}

	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}	
	
	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}
	
	public Pessoa getSolicitanteVisualizacao() {
		return solicitanteVisualizacao;
	}

	public void setSolicitanteVisualizacao(Pessoa solicitanteVisualizacao) {
		this.solicitanteVisualizacao = solicitanteVisualizacao;
	}
	
	public Pessoa getRequerenteVisualizacao() {
		return requerenteVisualizacao;
	}

	public void setRequerenteVisualizacao(Pessoa requerenteVisualizacao) {
		this.requerenteVisualizacao = requerenteVisualizacao;		
	}
	
	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}
	
	public Boolean getRespondeCefir() {
		return respondeCefir;
	}

	public void setRespondeCefir(Boolean respondeCefir) {
		this.respondeCefir = respondeCefir;
	}	
	
	public boolean isHabilitaCadastroAmbiental() {
		return habilitaCadastroAmbiental;
	}

	public void setHabilitaCadastroAmbiental(boolean habilitaCadastroAmbiental) {
		this.habilitaCadastroAmbiental = habilitaCadastroAmbiental;
	}	
		
	public List<String> getListTipoCaptacao() {
		return listTipoCaptacao;
	}

	public void setListTipoCaptacao(List<String> listTipoCaptacao) {
		this.listTipoCaptacao = listTipoCaptacao;
	}
	
	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}
	
	public List<DocumentoImovelRural> getListaArquivoDocumento() {
		return listaArquivoDocumento;
	}	
	public Boolean getListaArquivoDocumentoIsEmpty() {
		if(Util.isNullOuVazio(listaArquivoDocumento))
			return true;
		else
			return listaArquivoDocumento.isEmpty();
	}
	public void setListaArquivoDocumento(List<DocumentoImovelRural> listaArquivoDocumento) {
		this.listaArquivoDocumento = listaArquivoDocumento;
	}
	
	public List<DocumentoImovelRural> getListaArquivoProcuracao() {
		return listaArquivoProcuracao;
	}	
	public Boolean getListaArquivoProcuracaoIsEmpty() {
		if(Util.isNullOuVazio(listaArquivoProcuracao))
			return true;
		else
			return listaArquivoProcuracao.isEmpty();
	}
	public void setListaArquivoProcuracao(List<DocumentoImovelRural> listaArquivoProcuracao) {
		this.listaArquivoProcuracao = listaArquivoProcuracao;
	}
	
	public String getNomContato() {
		return nomContato;
	}

	public void setNomContato(String nomContato) {
		this.nomContato = nomContato;
	}

	public String getNumTelefone() {
		return numTelefone;
	}

	public void setNumTelefone(String numTelefone) {
		this.numTelefone = numTelefone;
	}

	public String getDesEmail() {
		return desEmail;
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}
	
	public Collection<ResponsavelImovelRural> getListaResponsaveis() {
		return listaResponsaveis;
	}

	public void setListaResponsaveis(Collection<ResponsavelImovelRural> listaResponsaveis) {
		this.listaResponsaveis = listaResponsaveis;
	}
		
	public String getCoordenadaPontoxy() {
		return coordenadaPontoxy;
	}
	
	public void setCoordenadaPontoxy(String coordenadaPontoxy) {
		this.coordenadaPontoxy = coordenadaPontoxy;
	}
	
	public Boolean getExisteTheGeom() {
		try {
			return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}
	
	public Boolean getExisteTheGeomLote() {
		try {
			if(imovelRural.isImovelINCRA() && imovelRural.isFaseAssentamentoImovelRuralImplantado() && !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote())){
				return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			} else {
				return false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}
	
	public List<SelectItem> getItemsClassifSecGeometrica() {
		return itemsClassifSecGeometrica;
	}
	
	public void setItemsClassifSecGeometrica(List<SelectItem> itemsClassifSecGeometrica) {
		this.itemsClassifSecGeometrica = itemsClassifSecGeometrica;
	}
				
	public CronogramaRecuperacao getCronogramaRecuperacaoRl() {
		return cronogramaRecuperacaoRl;
	}

	public void setCronogramaRecuperacaoRl(
			CronogramaRecuperacao cronogramaRecuperacaoRl) {
		this.cronogramaRecuperacaoRl = cronogramaRecuperacaoRl;
	}
	
	public List<CronogramaEtapa> getListaCronogramaEtapaRl() {
		return listaCronogramaEtapaRl;
	}

	public void setListaCronogramaEtapaRl(
			List<CronogramaEtapa> listaCronogramaEtapaRl) {
		this.listaCronogramaEtapaRl = listaCronogramaEtapaRl;
	}
	
	public List<DocumentoImovelRural> getListaArquivoPradRl() {
		return listaArquivoPradRl;
	}

	public void setListaArquivoPradRl(List<DocumentoImovelRural> listaArquivoPradRl) {
		this.listaArquivoPradRl = listaArquivoPradRl;
	}
		
	public CronogramaRecuperacao getCronogramaRecuperacaoRlSelecionada() {
		return cronogramaRecuperacaoRlSelecionada;
	}
	
	public void setCronogramaRecuperacaoRlSelecionada(CronogramaRecuperacao cronogramaRecuperacaoAppSelecionada) {
		this.cronogramaRecuperacaoRlSelecionada = cronogramaRecuperacaoAppSelecionada;
	}
				
	public Collection<App> getListaApp() {
		return listaApp;
	}

	public void setListaApp(Collection<App> listaApp) {
		this.listaApp = listaApp;
	}	
			
	public List<DocumentoImovelRural> getListaArquivoPradOP() {
		return listaArquivoPradOP;
	}

	public void setListaArquivoPradOP(List<DocumentoImovelRural> listaArquivoPradOP) {
		this.listaArquivoPradOP = listaArquivoPradOP;
	}		
	
	public List<CronogramaRecuperacao> getListaCronogramaRecuperacaoExistenteRL() {
		return listaCronogramaRecuperacaoExistenteRL;
	}


	public void setListaCronogramaRecuperacaoExistenteRL(List<CronogramaRecuperacao> listaCronogramaRecuperacaoExistenteRL) {
		this.listaCronogramaRecuperacaoExistenteRL = listaCronogramaRecuperacaoExistenteRL;
	}
	
	public List<CronogramaRecuperacao> getListaCronogramaRecuperacaoExistenteApp() {
		return listaCronogramaRecuperacaoExistenteApp;
	}


	public void setListaCronogramaRecuperacaoExistenteApp(List<CronogramaRecuperacao> listaCronogramaRecuperacaoExistenteApp) {
		this.listaCronogramaRecuperacaoExistenteApp = listaCronogramaRecuperacaoExistenteApp;
	}
	
	public Boolean getIsProcPfPjRepLegal(){
		return isProcPfPjRepLegal;
	}
	
	public void setIsProcPfPjRepLegal(Boolean isProcPfPjRepLegal) {
		this.isProcPfPjRepLegal = isProcPfPjRepLegal;
	}
	
	public List<TipoVinculoImovel> getListaTipoVinculos() {
		return listaTipoVinculos;
	}

	public void setListaTipoVinculos(List<TipoVinculoImovel> listaTipoVinculos) {
		this.listaTipoVinculos = listaTipoVinculos;
	}
	
	public List<SelectItem> getCollTipoCaptacao() {
		return collTipoCaptacao;
	}

	public void setCollTipoCaptacao(List<SelectItem> collTipoCaptacao) {
		this.collTipoCaptacao = collTipoCaptacao;
	}
	
	public List<TipoOrigemCertificado> getListaTipoOrigemCertificado() {
		return listaTipoOrigemCertificado;
	}

	public void setListaTipoOrigemCertificado(
			List<TipoOrigemCertificado> listaTipoOrigemCertificado) {
		this.listaTipoOrigemCertificado = listaTipoOrigemCertificado;
	}
	
	public String getCaminhoDesenharGeoBahia() {
		return caminhoDesenharGeoBahia;
	}

	public void setCaminhoDesenharGeoBahia(String caminhoDesenharGeoBahia) {
		this.caminhoDesenharGeoBahia = caminhoDesenharGeoBahia;
	}
	
	public String getCaminhoNovoGeoBahia() {
		return caminhoNovoGeoBahia;
	}

	public void setCaminhoNovoGeoBahia(String caminhoNovoGeoBahia) {
		this.caminhoNovoGeoBahia = caminhoNovoGeoBahia;
	}
	
	public App getAppSelecionada() {
		return appSelecionada;
	}

	public void setAppSelecionada(App appSelecionada) {
		this.appSelecionada = appSelecionada;
	}
	
	public List<CronogramaEtapa> getListaCronogramaEtapaApp() {
		return listaCronogramaEtapaApp;
	}

	public void setListaCronogramaEtapaApp(List<CronogramaEtapa> listaCronogramaEtapaApp) {
		this.listaCronogramaEtapaApp = listaCronogramaEtapaApp;
	}
	
	public List<DocumentoImovelRural> getListaArquivoPradAppSelecionada() {
		return listaArquivoPradAppSelecionada;
	}

	public void setListaArquivoPradAppSelecionada(List<DocumentoImovelRural> listaArquivoPradApp) {
		this.listaArquivoPradAppSelecionada = listaArquivoPradApp;
	}
	
	public CronogramaRecuperacao getCronogramaRecuperacaoAppSelecionada() {
		return cronogramaRecuperacaoAppSelecionada;
	}

	public void setCronogramaRecuperacaoAppSelecionada(CronogramaRecuperacao cronogramaRecuperacaoAppSelecionada) {
		this.cronogramaRecuperacaoAppSelecionada = cronogramaRecuperacaoAppSelecionada;
	}
	
	public AreaProdutiva getAreaProdutivaSelecionada() {
		return areaProdutivaSelecionada;
	}

	public void setAreaProdutivaSelecionada(AreaProdutiva areaProdutivaSelecionada) {
		this.areaProdutivaSelecionada = areaProdutivaSelecionada;
	}
		
	public List<TipoFinalidadeUsoAgua> getListFinalidadesSelecionadas() {
		return listFinalidadesSelecionadas;
	}


	public void setListFinalidadesSelecionadas(
			List<TipoFinalidadeUsoAgua> listFinalidadesSelecionadas) {
		this.listFinalidadesSelecionadas = listFinalidadesSelecionadas;
	}
	
	public ImovelRuralUsoAgua getImovelRuralUsoAguaSelecionada() {
		return imovelRuralUsoAguaSelecionada;
	}

	public void setImovelRuralUsoAguaSelecionada(ImovelRuralUsoAgua imovelRuralUsoAguaSelecionada) {
		this.imovelRuralUsoAguaSelecionada = imovelRuralUsoAguaSelecionada;
	}
	
	public List<UsoBarragem> getListUsoBarragemEnum() {
		return listUsoBarragemEnum;
	}

	public void setListUsoBarragemEnum(List<UsoBarragem> listUsoBarragemEnum) {
		this.listUsoBarragemEnum = listUsoBarragemEnum;
	}
	
	public TipoBarragemEnum getTipoBarragem() {
		return tipoBarragem;
	}

	public void setTipoBarragem(TipoBarragemEnum tipoBarragem) {
		this.tipoBarragem = tipoBarragem;
	}
	
	public String getTipoIntervencaoCorpoHidrico() {
		return tipoIntervencaoCorpoHidrico;
	}

	public void setTipoIntervencaoCorpoHidrico(String tipoIntervencaoCorpoHidrico) {
		this.tipoIntervencaoCorpoHidrico = tipoIntervencaoCorpoHidrico;
	}
	
	public String getnomTipoIntervencaoCorpoHidrico() {
		return nomTipoIntervencaoCorpoHidrico;
	}

	public void setnomTipoIntervencaoCorpoHidrico(String nomTipoIntervencaoCorpoHidrico) {
		this.nomTipoIntervencaoCorpoHidrico = nomTipoIntervencaoCorpoHidrico;
	}
	
	public Double getVolumeBarragem() {
		return volumeBarragem;
	}

	public void setVolumeBarragem(Double volumeBarragem) {
		this.volumeBarragem = volumeBarragem;
	}

	public Double getAreaInundadaBarragem() {
		return areaInundadaBarragem;
	}

	public void setAreaInundadaBarragem(Double areaInundadaBarragem) {
		this.areaInundadaBarragem = areaInundadaBarragem;
	}

	public TipoTravessiaDuto getTipoTravessia() {
		return tipoTravessia;
	}

	public void setTipoTravessia(TipoTravessiaDuto tipoTravessia) {
		this.tipoTravessia = tipoTravessia;
	}
	
	public List<ImovelRuralUsoAgua> getListaPontoCaptacaoSuperficial() {
		try {			
			return filtrarListaUsoAguaImovelRuralPorTipo("2");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new ArrayList<ImovelRuralUsoAgua>();
	}

	public List<ImovelRuralUsoAgua> getListaPontoReceptorResiduos() {
		try {			 
			return filtrarListaUsoAguaImovelRuralPorTipo("3");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new ArrayList<ImovelRuralUsoAgua>();
	}

	public List<ImovelRuralUsoAgua> getListaPontoCaptacaoSubterranea() {
		try {			
			return filtrarListaUsoAguaImovelRuralPorTipo("1");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new ArrayList<ImovelRuralUsoAgua>();
	}

	public List<ImovelRuralUsoAgua> getListaPontoIntervencao() {
		try {
			return filtrarListaUsoAguaImovelRuralPorTipo("4");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new ArrayList<ImovelRuralUsoAgua>();
	}
	
	public Collection<TipoFinalidadeUsoAgua> getListTipoFinalidadeUsoAgua() {
		this.listTipoFinalidadeUsoAgua = obterTipoFinalidadeUsoAgua();
		return this.listTipoFinalidadeUsoAgua;
	}

	public void setListTipoFinalidadeUsoAgua(
			Collection<TipoFinalidadeUsoAgua> listTipoFinalidadeUsoAgua) {
		this.listTipoFinalidadeUsoAgua = listTipoFinalidadeUsoAgua;
	}
	
	public StreamedContent getArquivoBaixar() {
		if(!Util.isNullOuVazio(arquivoSelecionado)) {
			try {
				arquivoBaixar = imovelRuralServiceFacade.getContentFile(arquivoSelecionado);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
			return arquivoBaixar;
	}
	
	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}
	
	public String getArquivoSelecionado() {
		return arquivoSelecionado;
	}

	public void setArquivoSelecionado(String arquivoSelecionado) {
		this.arquivoSelecionado = arquivoSelecionado;
	}
	
	public List<DocumentoImovelRural> getListaArquivoReservaAprovada() {
		return listaArquivoReservaAprovada;
	}

	public void setListaArquivoReservaAprovada(List<DocumentoImovelRural> listaArquivoReservaAprovada) {
		this.listaArquivoReservaAprovada = listaArquivoReservaAprovada;
	}
	
	public List<DocumentoImovelRural> getListaArquivoReservaAverbada() {
		return listaArquivoReservaAverbada;
	}

	public void setListaArquivoReservaAverbada(List<DocumentoImovelRural> listaArquivoReservaAverbada) {
		this.listaArquivoReservaAverbada = listaArquivoReservaAverbada;
	}
	
	public ReservaLegalAverbada getReservalegalAverbada() {
		return reservalegalAverbada;
	}

	public void setReservalegalAverbada(ReservaLegalAverbada reservalegalAverbada) {
		this.reservalegalAverbada = reservalegalAverbada;
	}
	
	public boolean getIndLancamentoResiduosLiquidos() {
		if (!Util.isNullOuVazio(this.imovelRural.getIndLancamentoConcessionaria())
				|| !Util.isNullOuVazio(this.imovelRural.getIndLancamentoManancial())) {
			if (this.imovelRural.getIndLancamentoConcessionaria() || this.imovelRural.getIndLancamentoManancial()) {
				return true;
			}
		}
		return false;
	}
	
	// Início Imóveis INCRA	
	
	public String getRetornaLabelValorArea(){
		if(!Util.isNullOuVazio(imovelRural) && imovelRural.isImovelINCRA()){
			return "Área medida pelo INCRA (ha) ";
		} else {
			if(getRetornaTipoVinculoImovel().equals("Proprietário")) {
				return "Área registrada em cartório (ha) ";
			} else {
				return "Área do imóvel informado no documento de posse (ha)";
			}
		}
	}
	
	public List<AssociacaoIncra> getListAssociacoesIncra() {
			try {
				return imovelRuralServiceFacade.listAssociacaoIncraPorImovelRural(this.imovelRural);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			return new ArrayList<AssociacaoIncra>(); 
	}

	public AssociacaoIncra getAssociacaoIncraSelecionada() {
		return associacaoIncraSelecionada;
	}

	public void setAssociacaoIncraSelecionada(AssociacaoIncra associacaoIncraSelecionada) {
		this.associacaoIncraSelecionada = associacaoIncraSelecionada;
	}
	
	public List<AssentadoIncra> getListAssentadosIncraPorAssociacao() {
		try {
			if(!Util.isNull(getAssociacaoIncraSelecionada()) && !Util.isNull(this.getAssociacaoIncraSelecionada().getIdeAssociacaoIncra())) {
				return imovelRuralServiceFacade.listAssentadosIncraDoImovelRuralPorAssociacao(this.getAssociacaoIncraSelecionada(), this.imovelRural);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new ArrayList<AssentadoIncra>();
	}
	
	public List<AssentadoIncraImovelRural> getListAssentadoIncraImovelRural(){
		List<AssentadoIncraImovelRural> listTmp = new ArrayList<AssentadoIncraImovelRural>();
		if(!Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())){
			for(AssentadoIncraImovelRural assentado : imovelRural.getAssentadoIncraImovelRuralCollection()){
				if(!assentado.isIndExcluido()){
					listTmp.add(assentado);
				}
			}
		}
		return listTmp;
	}
	
	public AssentadoIncraImovelRural getAssentadoIncraImovelRuralSelecionado() {
		return assentadoIncraImovelRuralSelecionado;
	}

	public void setAssentadoIncraImovelRuralSelecionado(
			AssentadoIncraImovelRural assentadoIncraImovelRuralSelecionado) {
		this.assentadoIncraImovelRuralSelecionado = assentadoIncraImovelRuralSelecionado;
	}
	
	public void visualizarAssentadoIncra() {
		this.assentadoIncraImovelRuralSelecionado.setIndEdicao(false);
		this.assentadoIncraImovelRuralSelecionado.setIndVisualizacao(true);
	}
	// Fim Imóveis INCRA
	
	
	public ImovelRuralRppn getImovelRuralRppn() {
		return imovelRuralRppn;
	}

	public void setImovelRuralRppn(ImovelRuralRppn imovelRuralRppn) {
		this.imovelRuralRppn = imovelRuralRppn;
	}

	public boolean isVisualizaImpressao() {
		return visualizaImpressao;
	}

	public void setVisualizaImpressao(boolean visualizaImpressao) {
		this.visualizaImpressao = visualizaImpressao;
	}
	
	public boolean habilitaVisualizacaoGrupo(Integer numGrupo){
		
		if(!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse()) 
				&& !Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())
				&& imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getNumGrupoDocumento().equals(numGrupo)){			
			
			return true;					
		}
		
		return false;			
	}
	
	public Collection<TipoFinalidadeVegetacaoNativa> getListTipoFinalidadeVegetacaoNativa(){
		Collection<TipoFinalidadeVegetacaoNativa> listTipoFinalidadeVegetacaoNativa = new ArrayList<TipoFinalidadeVegetacaoNativa>();
		try {
			listTipoFinalidadeVegetacaoNativa = imovelRuralServiceFacade.listarTipoFinalidadeVegetacaoNativa();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return listTipoFinalidadeVegetacaoNativa;
	}
	
	public Pessoa getProcuradorCadastro() {
    	try {
			if (!Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())) {
				Collection<PessoaImovel> pessoaRequerente = imovelRuralServiceFacade
						.filtrarPessoasPorImovel(new Imovel(this.imovelRural.getIdeImovelRural()));
				for (PessoaImovel pessoaImovel : pessoaRequerente) {
					if (pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(PROCURADOR)) {
						return pessoaImovel.getIdePessoa();
					}
				}
			}
			if (ContextoUtil.getContexto().getIsProcPfPjOuRepLegal()) {
				return ContextoUtil.getContexto().getSolicitanteRequerimento();
			}
    	} catch (Exception e) {
    		Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
    	}
    	return null;
    }
	
	  public String getTelefoneProcurador() {
	    	List<Telefone> listTelefones;
	    	try {
	    		listTelefones = imovelRuralServiceFacade.filtraTelefonePorPessoa(getProcuradorCadastro());
	    		if(!Util.isNullOuVazio(listTelefones)) {
	    			return listTelefones.get(0).getNumTelefone().toString();
	    		}
	    	} catch (Exception e) {
	    		Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
	    	}
	    	return "";
	    }
	  
//	Alterado pelo ticket #10803
	public boolean isPerfilTecnico() {
		
		if(Util.isNullOuVazio(token)) {
			token = usuarioAutorizacaoGeobahiaService.carregarUsuarioAutorizacaoGeobahiaCefir();
		}
		
		return token.equals("") ? false : true;
	

	}
	
	public boolean getRenderedPerguntaLicenca() {		
		if(!Util.isNullOuVazio(areaProdutivaSelecionada)) {
			return imovelRuralServiceFacade.getRenderedPerguntaLicenca(areaProdutivaSelecionada, true, imovelRural);
		} else {
			return false;
		}
		
	}
	
	public boolean getManejoSustentavel(){
		Integer idTipologia = null;
		if(!Util.isNull(this.areaProdutivaSelecionada))
			idTipologia = this.areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia();
		
		return (!Util.isNull(idTipologia) && idTipologia.equals(TipologiaCefirEnum.MANEJO_FLORESTAL_SUSTENTAVEL.getId())) ? true : false;
	}

	public boolean getManejoCabruca(){
		Integer idTipologia = null;
		if(!Util.isNull(this.areaProdutivaSelecionada))
			idTipologia = this.areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia();
		
		return (!Util.isNull(idTipologia) && idTipologia.equals(TipologiaCefirEnum.MANEJO_CABRUCA.getId())) ? true : false;					
	}
	
	public List<DocumentoImovelRural> getListaDocumentoPlanoManejo() {
		List<DocumentoImovelRural> listaDocumentoPlanoManejo = new ArrayList<DocumentoImovelRural>();
		if(!Util.isNull(areaProdutivaSelecionada) && !Util.isNull(areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo())) {
			listaDocumentoPlanoManejo.add(areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo());
		}
		return listaDocumentoPlanoManejo;
	}
	
	public String retornaDscManejo(AreaProdutivaTipologiaAtividadeAnimal aptaa) {
		String dscManejo = "";
		if (aptaa.isIndManejoCria()) {
			dscManejo += "Cria - ";
		}
		if (aptaa.isIndManejoEngorda()) {
			dscManejo += "Engorda - ";
		}
		if (aptaa.isIndManejoRecria()) {
			dscManejo += "Recria - ";
		}
		if (aptaa.isIndManejoReproducao()) {
			dscManejo += "Reprodução - ";
		}
		
		if(!dscManejo.isEmpty() && dscManejo.length() > 2) {
			dscManejo = dscManejo.substring(0, dscManejo.length() - 3);
		}
		
		return dscManejo;
	}
	
	public String retornaDscTipoUsoAgua(Integer ideTipoUsoAgua) {
		if (ideTipoUsoAgua == 3) {
			return "Dessedentação e Limpeza";
		} else if (ideTipoUsoAgua == 1) {
			return "Dessedentação";
		} else if (ideTipoUsoAgua == 2) {
			return "Limpeza";
		} else {
			return "Nenhum uso informado";
		}
	}
	
	public String retornaDscMesesIrrigacao(String pMeses) {
		return Util.converteNumToDscMeses(pMeses);
	}
	
	public boolean isExibePerguntasRLAprovada(){
		if(!Util.isNullOuVazio(imovelRural.getIndReservaLegal()) && imovelRural.getIndReservaLegal()){
			if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeStatus()) 
					&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeStatus().getdscStatus()) 
					&& imovelRural.getReservaLegal().getIdeStatus().getdscStatus().equals("Aprovada") 
					&& Util.isNullOuVazio(imovelRural.getReservaLegal().getDtcAprovacaoDeclarada())){
				return false;
			}
			return true;
		}
		return false;
	}

	private void carregarPctImovelRural(){
		try {
			if(!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())){
				this.setPctImovelRural(imovelRuralServiceFacade.buscarPctImovelRural(this.imovelRural));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean getShowPCT(){
		return showPCT;
	}

	public PctImovelRural getPctImovelRural() {
		return pctImovelRural;
	}

	public void setPctImovelRural(PctImovelRural pctImovelRural) {
		this.pctImovelRural = pctImovelRural;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCaminhoGeoBahia() {
		return caminhoGeoBahia;
	}

	public void setCaminhoGeoBahia(String caminhoGeoBahia) {
		this.caminhoGeoBahia = caminhoGeoBahia;
	}

}
