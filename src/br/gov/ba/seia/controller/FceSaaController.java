package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EtaTipoComposicao;
import br.gov.ba.seia.entity.FaixaDiametroAdutora;
import br.gov.ba.seia.entity.FceSaa;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaDadosConcedidos;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaBruta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaTratada;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaEta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaReservatorio;
import br.gov.ba.seia.entity.FceSaaTipoComposicao;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoMaterialUtilizado;
import br.gov.ba.seia.entity.TipoReservatorio;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.SaaTipoComposicaoEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.facade.FceSaaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

@Named("fceSaaController")
@ViewScoped
public class FceSaaController extends BaseFceSaaController{

	private FceSaa fceSaa;
	
	private FceSaaLocalizacaoGeograficaEta fceSaaLocalizacaoGeograficaEta; 
	
	private FceSaaLocalizacaoGeograficaElevatoriaBruta fceSaaLocalizacaoGeograficaElevatoriaBruta;
	
	private FceSaaLocalizacaoGeograficaElevatoriaTratada fceSaaLocalizacaoGeograficaElevatoriaTratada;
	
	private List<FceSaaLocalizacaoGeograficaElevatoriaTratada> listaFceSaaLocalizacaoGeograficaElevatoriaTratada;
	
	private FceSaaLocalizacaoGeograficaReservatorio fceSaaLocalizacaoGeograficaReservatorio;
	
	private FceSaaLocalizacaoGeograficaDadosConcedidos fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial;
	
	private boolean localizacaoSuperficial;
	
	private FceSaaLocalizacaoGeograficaDadosConcedidos fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea;
	
	private boolean localizacaoSubterranea;
	
	private List<FaixaDiametroAdutora> listFaixaAdutoraBruta;
	
	private List<FaixaDiametroAdutora> listFaixaAdutoraTratada;
	
	private List<FaixaDiametroAdutora> listFaixaAdutoraResDist;
	
	private List<TipoMaterialUtilizado> listTipoMaterialUtilizado;
	
	private List<FceSaaTipoComposicao> listTipoComposicoes;
	
	private List<EtaTipoComposicao> listTipoComposicoesSelecionadas;
	
	private List<FceSaaLocalizacaoGeograficaEta> listFceSaaLocalizacaoGeograficaEta;
	
	private List<FceSaaLocalizacaoGeograficaElevatoriaBruta> listFceSaaLocalizacaoGeograficaElevatoriaBruta;
	
	private List<FceSaaLocalizacaoGeograficaReservatorio> listFceSaaLocalizacaoGeograficaReservatorio;
	
	private List<TipoReservatorio> listTipoReservatorio;
	
	private LocalizacaoGeografica localizacaoGeograficaSelecionada;
	
	private List<TipoCaptacao> tipoCaptacao;
	
	private List<TipoCaptacao> tipoCaptacaoSelecionadas;
	
	private static final DocumentoObrigatorio DOC_FCE_SAA = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_SISTEMA_ABASTECIMENTO_AGUA.getId(),"Formulário de Caracterização do Empreendimento - Sistema de Abastecimento de Água (SAA)");
	
	private boolean isSuperficial;
	
	private boolean isSubterranea;
	
	private boolean isBtnFinalizar;
	
	private boolean isDadosGerais;
	
	private boolean isAguaBruta;
	
	private boolean isAguatratada;
	
	private boolean isDistribuicao;
	
	private MetodoUtil metodoExterno;
	
	private String msgConfirmacaoImprimirRelatorio;
	
	@Inject
	private LocalizacaoGeograficaGenericController localizacaoGeograficaController;
	
	@EJB
	private FceSaaFacade fceSaaFacade;
	

	
	@Override
	public void init() {
		verificarEdicao();
		inicializarObjetos();
		carregarDados();
		obterMsgImprimirRelatorio();
		if(!isFceSalvo()){
			iniciarFce(DOC_FCE_SAA);
			fceSaa = new FceSaa(super.fce);
		}
		else {
			carregarFceSaa();
			carregarDadosEstacaoTratamentoEsgoto();
			carregarAbas();
		}
	}
	
	


	private void carregarDados() {
		try {
			listFaixaAdutoraBruta = fceSaaFacade.carregarFaixasDiametro(0, 1, null);
			listFaixaAdutoraTratada = fceSaaFacade.carregarFaixasDiametro(null, 1, null);
			listFaixaAdutoraResDist = fceSaaFacade.carregarFaixasDiametro(null, null, 2);
			listTipoMaterialUtilizado = fceSaaFacade.carregarTipoMaterialUtilizado();
			listTipoReservatorio = fceSaaFacade.carregarTipoReservatorio();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	@Override
	public void verificarEdicao(){
		
		super.carregarFceDoRequerente(DOC_FCE_SAA);
	}
	
	private void carregarFceSaa(){
		try {
			fceSaa = fceSaaFacade.buscarFceSaaByIdeFce(super.fce);
			if(Util.isNullOuVazio(fceSaa)){
				fceSaa = new FceSaa();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public LocalizacaoGeografica getObterNovaLocalizacaoGeografica() {
		fceSaaLocalizacaoGeograficaElevatoriaTratada = new FceSaaLocalizacaoGeograficaElevatoriaTratada();
		fceSaaLocalizacaoGeograficaElevatoriaTratada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		return fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica();
	}
	
	private void carregarAbas() {
		try {
			
			if(isFceTecnico()){
				TipoCaptacao captacao = new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId(),"Superficial");
				TipoCaptacao captacaoSubterranea = new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId(), "Subterrânea");
				
				tipoCaptacao.add(0,captacao);
				tipoCaptacao.add(1,captacaoSubterranea);
				
				fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial = fceSaaFacade.buscarLocalizacaoDadosConcedidosByIdeFceSaaAndTipoCaptacao(fceSaa, TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId());
				if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial)){
					fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica().setBaciaHidrografica(fceSaaFacade.getBacia(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica()));
					
					for(DadoGeografico dado : fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
						fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
						fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
					}
					fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setDesabilitarLinha(Boolean.TRUE);
					isSuperficial = Boolean.TRUE;
					TipoCaptacao captacaoSuperficial = new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId(),"Superficial");
					tipoCaptacaoSelecionadas.add(captacaoSuperficial);
				}else{
					fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial = new FceSaaLocalizacaoGeograficaDadosConcedidos();
					fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					isSuperficial = Boolean.FALSE;
				}
				fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea =fceSaaFacade.buscarLocalizacaoDadosConcedidosByIdeFceSaaAndTipoCaptacao(fceSaa, TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId());
				if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea)){
					
					for(DadoGeografico dado : fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
						fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
						fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
					}
					fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setDesabilitarLinha(Boolean.TRUE);
					isSubterranea = Boolean.TRUE;
					TipoCaptacao captacaoLocSubterranea = new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId(),"Subterrânea");
					tipoCaptacaoSelecionadas.add(captacaoLocSubterranea);
				}else{
					fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea = new FceSaaLocalizacaoGeograficaDadosConcedidos();
					fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					isSubterranea = Boolean.FALSE;
				}
			}
			
			//Localização Elevatória Bruta
			listFceSaaLocalizacaoGeograficaElevatoriaBruta = fceSaaFacade.listarLocalizacaoBrutaByIdeFceSaa(fceSaa);
			
			if(Util.isNullOuVazio(listFceSaaLocalizacaoGeograficaElevatoriaBruta)){
				listFceSaaLocalizacaoGeograficaElevatoriaBruta = new ArrayList<FceSaaLocalizacaoGeograficaElevatoriaBruta>();
			}else{

				for(FceSaaLocalizacaoGeograficaElevatoriaBruta bruta  : listFceSaaLocalizacaoGeograficaElevatoriaBruta){
					bruta.setDesabilitarVazao(Boolean.TRUE);
					for(DadoGeografico dado : bruta.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
						bruta.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
						bruta.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
					}
				}
			
			}
			

			//Localização Elevatória Tratada
			listaFceSaaLocalizacaoGeograficaElevatoriaTratada = fceSaaFacade.buscarLocalizacaoTratadaByIdeFceSaa(fceSaa);
			
			if (!Util.isNullOuVazio(listaFceSaaLocalizacaoGeograficaElevatoriaTratada)){
				for (FceSaaLocalizacaoGeograficaElevatoriaTratada fceSaaLocalizacaoGeograficaElevatoriaTratada : listaFceSaaLocalizacaoGeograficaElevatoriaTratada) {
					if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica())){
						fceSaaLocalizacaoGeograficaElevatoriaTratada.setDesabilitarVazao(Boolean.TRUE);
						for(DadoGeografico dado : fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
							fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
							fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
						}
					}
				}
			}
			
			//Localização ETA
			listFceSaaLocalizacaoGeograficaEta = fceSaaFacade.listarLocalizacaoEtaByIdeFceSaa(fceSaa);
			
			if(Util.isNullOuVazio(listFceSaaLocalizacaoGeograficaEta)){
				listFceSaaLocalizacaoGeograficaEta = new ArrayList<FceSaaLocalizacaoGeograficaEta>();
			}else{
				
				for(FceSaaLocalizacaoGeograficaEta eta : listFceSaaLocalizacaoGeograficaEta){
					eta.setDesabilitarVazao(Boolean.TRUE);
					eta.setEtaTipoComposicaoCollection(fceSaaFacade.listaTipoComposicaoByIdeLocalizacaoEta(eta));
					for(DadoGeografico dado : eta.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
						eta.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
						eta.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
					}
				}

			}
			
			
			//Localização Reserva Distribuição
			listFceSaaLocalizacaoGeograficaReservatorio = fceSaaFacade.listarLocalizacaoReservatorioByIdeFceSaa(fceSaa);
			
			if(Util.isNullOuVazio(listFceSaaLocalizacaoGeograficaReservatorio)){
				listFceSaaLocalizacaoGeograficaReservatorio = new ArrayList<FceSaaLocalizacaoGeograficaReservatorio>();
			}else{
				
				for(FceSaaLocalizacaoGeograficaReservatorio reservatorio : listFceSaaLocalizacaoGeograficaReservatorio){
					reservatorio.setDesabilitarLinha(Boolean.TRUE);
					for(DadoGeografico dado : reservatorio.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
						reservatorio.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
						reservatorio.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
					}
				}

			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}

	}
	
	public void atualizarDados(){
		if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaBruta.getIdeLocalizacaoGeografica())){
		//	if(isPontoPreenchido(fceSaaLocalizacaoGeograficaElevatoriaBruta.getIdeLocalizacaoGeografica())){
			if(!StringUtils.isEmpty(fceSaaLocalizacaoGeograficaElevatoriaBruta.getIdeLocalizacaoGeografica().getPontoLatitude())
					&& !StringUtils.isEmpty(fceSaaLocalizacaoGeograficaElevatoriaBruta.getIdeLocalizacaoGeografica().getPontoLongitude()) || 
					(!StringUtils.isEmpty(fceSaaLocalizacaoGeograficaElevatoriaBruta.getIdeLocalizacaoGeografica().getLatitudeInicial()) && !StringUtils.isEmpty(fceSaaLocalizacaoGeograficaElevatoriaBruta.getIdeLocalizacaoGeografica().getLongitudeInicial()))){
				listFceSaaLocalizacaoGeograficaElevatoriaBruta.add(this.fceSaaLocalizacaoGeograficaElevatoriaBruta);
			}
				fceSaaLocalizacaoGeograficaElevatoriaBruta = new FceSaaLocalizacaoGeograficaElevatoriaBruta();
				fceSaaLocalizacaoGeograficaElevatoriaBruta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		//	}
		}else if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaReservatorio.getIdeLocalizacaoGeografica())){
		//	if(isPontoPreenchido(fceSaaLocalizacaoGeograficaReservatorio.getIdeLocalizacaoGeografica())){
			if(!StringUtils.isEmpty(fceSaaLocalizacaoGeograficaReservatorio.getIdeLocalizacaoGeografica().getPontoLatitude())
					&& !StringUtils.isEmpty(fceSaaLocalizacaoGeograficaReservatorio.getIdeLocalizacaoGeografica().getPontoLongitude()) ||
					(!StringUtils.isEmpty(fceSaaLocalizacaoGeograficaReservatorio.getIdeLocalizacaoGeografica().getLatitudeInicial())
							&& !StringUtils.isEmpty(fceSaaLocalizacaoGeograficaReservatorio.getIdeLocalizacaoGeografica().getLongitudeInicial()))){
				listFceSaaLocalizacaoGeograficaReservatorio.add(this.fceSaaLocalizacaoGeograficaReservatorio);
			}
				fceSaaLocalizacaoGeograficaReservatorio = new FceSaaLocalizacaoGeograficaReservatorio();
				fceSaaLocalizacaoGeograficaReservatorio.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		//	}
		}else if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica())){
			fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica().setBaciaHidrografica(fceSaaFacade.getBacia(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica()));
		}
		
		if(fceSaaLocalizacaoGeograficaEta != null && fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica() != null){
			
			boolean limparLocalizacaoPonto = false;
			
			boolean limparLocalizacaoInicial = false;
			
			if(StringUtils.isEmpty(fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica().getPontoLatitude())
					|| StringUtils.isEmpty(fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica().getPontoLongitude())){
				limparLocalizacaoPonto = true;
			}
			
			if(StringUtils.isEmpty(fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica().getLatitudeInicial())
					|| StringUtils.isEmpty(fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica().getLongitudeInicial())) {
				limparLocalizacaoInicial = true;
			}
			
			if((limparLocalizacaoPonto ^ limparLocalizacaoInicial)) {
				fceSaaLocalizacaoGeograficaEta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
		}
		
		if (!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaTratada) && !Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica().getDadoGeograficoCollection())) {
			
			for (Iterator<FceSaaLocalizacaoGeograficaElevatoriaTratada> iterator = listaFceSaaLocalizacaoGeograficaElevatoriaTratada.iterator(); iterator.hasNext();) {
				FceSaaLocalizacaoGeograficaElevatoriaTratada fceSaaLocalizacaoGeograficaElevatoriaTratada = (FceSaaLocalizacaoGeograficaElevatoriaTratada) iterator.next();
				
				if (Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica())){
					iterator.remove();
				}
			}
			
			if (!listaFceSaaLocalizacaoGeograficaElevatoriaTratada.contains(fceSaaLocalizacaoGeograficaElevatoriaTratada)){
				listaFceSaaLocalizacaoGeograficaElevatoriaTratada.add(fceSaaLocalizacaoGeograficaElevatoriaTratada);
			}
			
		}
	}
	
	private void ordernarComposicoes() {
		Collections.sort(listTipoComposicoes, new Comparator<FceSaaTipoComposicao>(){
			@Override
			public int compare(FceSaaTipoComposicao o1, FceSaaTipoComposicao o2) {
				return o1.getDescricaoFceSaaTipoComposicao().compareTo(o2.getDescricaoFceSaaTipoComposicao());
			}
		});
		
		for(FceSaaTipoComposicao saa : listTipoComposicoes){
	      if(SaaTipoComposicaoEnum.OUTROS.getDescricao().equalsIgnoreCase(saa.getDescricaoFceSaaTipoComposicao())){
	        FceSaaTipoComposicao saaOutros = saa;
	        listTipoComposicoes.remove(saa);
	        listTipoComposicoes.add(saaOutros);
	        return;
	      }
	    }
	}
	
	public boolean isPontoPreenchido(LocalizacaoGeografica localizacao){
		
		try {
			if(!Util.isNullOuVazio(fceSaaFacade.buscarGeometriaShape(localizacao.getIdeLocalizacaoGeografica()))){
				return true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return false;
		
	}
	
	public void validarTipoCaptacao(){
		isSuperficial = Boolean.FALSE;
		isSubterranea = Boolean.FALSE;
		for(TipoCaptacao captacao: tipoCaptacaoSelecionadas){
			
			if(captacao.getIdeTipoCaptacao() == TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId()){
				isSuperficial = Boolean.TRUE;
			}else if(captacao.getIdeTipoCaptacao() == TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId()){
				isSubterranea = Boolean.TRUE;
			}
		}
		
		if(!isSuperficial){ //#10092 - ao desmarcar os checkbox o sistema deve apagar as localizações
			this.localizacaoGeograficaSelecionada = fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica();
			this.localizacaoSuperficial = true;
			excluirLocGeoSelecionada();
		}
		if(!isSubterranea){
			this.localizacaoGeograficaSelecionada =  fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getIdeLocalizacaoGeografica();
			this.localizacaoSubterranea = true;
			excluirLocGeoSelecionada();
		}
		
		Html.atualizar("formSaa:tabSaa:pngAbaDadosConcedidos");
	}
	
	public void validarClose(){
		try {
			if(!validarFechar()){
				fce.setIndConcluido(Boolean.FALSE);
				JsfUtil.addWarnMessage("Existem dados não preenchidos, FCE não será concluído!");
				fceSaaFacade.salvarOuAtualizarFce(fce);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public void finalizar(){
		if(validarAba()){
			try {
				if(isFceTecnico()){
				
					if(validarDadosConcedidos(true)){
						fceSaaFacade.finalizar(this);
						obterMsgImprimirRelatorio();
						Html.exibir("confimarImpressaoRelatorioSaa");
					}else{
						MensagemUtil.erro("É necessário preencher os dados da informação da captação");
					}
				}else{
					fceSaaFacade.finalizar(this);
					Html.exibir("confimarImpressaoRelatorioSaa");
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Sistema de Abastecimento de Água");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void obterMsgImprimirRelatorio() {
		if(isFceTecnico()) {
			this.msgConfirmacaoImprimirRelatorio = Util.getString("geral_msg_imprimir_relatorio_dados_concedidos_fce");
		} else {
			this.msgConfirmacaoImprimirRelatorio = Util.getString("msg_imprimir_relatorio_fce_saa");
		}
	}
	
	@Override
	public void prepararParaFinalizar() throws Exception{
		if(verificarMarcacaoOutrosBruta() && verificarMarcacaoOutrosTratada()){
			super.concluirFce();
		}else{
			fce.setIndConcluido(false);
			super.salvarFce();
		}
		salvarFceSaa();
		salvarAbas();
		if(!isFceSalvo()){
			super.exibirMensagem001();
		}
		else {
			super.exibirMensagem002();
		}
		Html.esconder("sistemaAbastecimentoAgua");
		Html.atualizar("dlgAnaliseTecnica");
		limpar();
	}

	public void changeTipoMaterialBruta(){
		if(!verificarMarcacaoOutrosBruta()){
			MensagemUtil.alerta(Util.getString("lac_dadosGerais_info001"));
		}
	}
	
	public void changeTipoMaterialTratada(){
		if(!verificarMarcacaoOutrosTratada()){
			MensagemUtil.alerta(Util.getString("lac_dadosGerais_info001"));
		}
	}

	private boolean verificarMarcacaoOutrosBruta() {
		 if(!fceSaa.getIdeTipoMaterialUtilizadoBruta().getDescricaoTipoMaterialUtilizado().equalsIgnoreCase("outros")){
			 return true;
			 
		 }else{
			 return false;
		 }
	}
	
	private boolean verificarMarcacaoOutrosTratada() {
		 if(!fceSaa.getIdeTipoMaterialUtilizadoTratada().getDescricaoTipoMaterialUtilizado().equalsIgnoreCase("outros")){
			 return true;
			 
		 }else{
			 return false;
		 }
	}
	
	private void salvarAbas() throws Exception {
		if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica())){
			TipoCaptacao tipoCaptacao = new TipoCaptacao();
			tipoCaptacao.setIdeTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId());
			fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setIdeTipoCaptacao(tipoCaptacao);
			fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setIdeFceSaa(fceSaa);
			fceSaaFacade.salvarFceSaaLocalizacaoDadosConcedidos(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial);
		}
		
		if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getIdeLocalizacaoGeografica())){
			TipoCaptacao tipoCaptacao = new TipoCaptacao();
			tipoCaptacao.setIdeTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId());
			fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setIdeTipoCaptacao(tipoCaptacao);
			fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setIdeFceSaa(fceSaa);
			fceSaaFacade.salvarFceSaaLocalizacaoDadosConcedidos(fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea);
		}
		
		for(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta : listFceSaaLocalizacaoGeograficaElevatoriaBruta){
			
			localizacaoBruta.setIdeFceSaa(this.fceSaa);
			
			fceSaaFacade.salvarFceSaaLocalizacaoBruta(localizacaoBruta);
		}	
		
		for(FceSaaLocalizacaoGeograficaEta localizacaoEta : listFceSaaLocalizacaoGeograficaEta){
			
			localizacaoEta.setIdeFceSaa(this.fceSaa);
			
			for(EtaTipoComposicao tipoComposicao : localizacaoEta.getEtaTipoComposicaoCollection()){
				tipoComposicao.setIdeFceSaaLocalizacaoGeograficaEta(localizacaoEta);
			}
			
			fceSaaFacade.salvarFceSaaLocalizacaoEta(localizacaoEta);
		}
		
		if (!Util.isNullOuVazio(listaFceSaaLocalizacaoGeograficaElevatoriaTratada)) {
			for (FceSaaLocalizacaoGeograficaElevatoriaTratada fceSaaLocalizacaoGeograficaElevatoriaTratada : listaFceSaaLocalizacaoGeograficaElevatoriaTratada) {
				if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica())) {
					fceSaaLocalizacaoGeograficaElevatoriaTratada.setIdeFceSaa(this.fceSaa);
					fceSaaFacade.salvarFceSaaLocalizacaoTratada(fceSaaLocalizacaoGeograficaElevatoriaTratada);
				}
			}
		}
		
		for(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio : listFceSaaLocalizacaoGeograficaReservatorio){
			localizacaoReservatorio.setIdeFceSaa(this.fceSaa);
			fceSaaFacade.salvarFceSaaLocalizacaoReservatorio(localizacaoReservatorio);
		}
	}

	private void salvarFceSaa() throws Exception {
		fceSaa.setIdeFce(super.fce);
		fceSaaFacade.salvarFceSaa(fceSaa);
	}

	@Override
	protected void prepararDuplicacao() {
		limparObjetos();
	}


	private void limparObjetos() {
		fceSaa.setIdeFceSaa(null);
		fceSaa.setIdeFce(null);
		fceSaaLocalizacaoGeograficaElevatoriaBruta.setIdeFceSaaLocalizacaoGeograficaElevatoriaBruta(null);
		fceSaaLocalizacaoGeograficaElevatoriaBruta.setIdeFceSaa(null);
		fceSaaLocalizacaoGeograficaElevatoriaTratada.setIdeFceSaaLocalizacaoGeograficaElevatoriaTratada(null);
		fceSaaLocalizacaoGeograficaElevatoriaTratada.setIdeFceSaa(null);
		fceSaaLocalizacaoGeograficaEta.setIdeFceSaaLocalizacaoGeograficaEta(null);
		fceSaaLocalizacaoGeograficaEta.setIdeFceSaa(null);
		fceSaaLocalizacaoGeograficaReservatorio.setIdeFceSaaLocalizacaoGeograficaReservatorio(null);
		fceSaaLocalizacaoGeograficaReservatorio.setIdeFceSaa(null);
		
		for(FceSaaLocalizacaoGeograficaElevatoriaTratada elevatoria : listaFceSaaLocalizacaoGeograficaElevatoriaTratada){
			elevatoria.setIdeFceSaa(null);
			elevatoria.setIdeFceSaaLocalizacaoGeograficaElevatoriaTratada(null);
		}
		
		for(FceSaaLocalizacaoGeograficaElevatoriaBruta bruta : listFceSaaLocalizacaoGeograficaElevatoriaBruta){
			bruta.setIdeFceSaa(null);
			bruta.setIdeFceSaaLocalizacaoGeograficaElevatoriaBruta(null);
		}
		
		for(FceSaaLocalizacaoGeograficaEta eta : listFceSaaLocalizacaoGeograficaEta){
			eta.setIdeFceSaa(null);
			eta.setIdeFceSaaLocalizacaoGeograficaEta(null);
		}
		
		for (FceSaaLocalizacaoGeograficaReservatorio res : listFceSaaLocalizacaoGeograficaReservatorio){
			res.setIdeFceSaa(null);
			res.setIdeFceSaaLocalizacaoGeograficaReservatorio(null);
		}
		
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceSaa();
			salvarAbas();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Sistema de Abastecimento de Água.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	protected void carregarFceTecnico(){
		try {
			//Carregar Dados do FCE Técnico		

			inicializarObjetos();
			
			super.carregarFceDoTecnico(DOC_FCE_SAA);
			carregarDados();
			carregarFceSaa();
			carregarDadosEstacaoTratamentoEsgoto();
			carregarAbas();
			habilitarAbas();
			
			
			
			//carregarOutorgas();
			carregarFceOutorgas();
			
			
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Sistema de Abastecimento de Água.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	private void carregarFceOutorgas() throws Exception {
		setCaptacoesSubterraneas(fceSaaFacade.carregarCaptacoesSubterraneas(super.requerimento));
		setCaptacoesSuperficiais(fceSaaFacade.carregarCaptacoesSuperficiais(super.requerimento));
	}
	
	private void habilitarAbas() {
		isDadosGerais = false;
		isAguaBruta = false;
		isAguatratada = false;
		isDistribuicao = false;
	}


	public void carregarDadosEstacaoTratamentoEsgoto(){
		try {
			listTipoComposicoes = fceSaaFacade.carregarTipoComposicoes();
			fceSaaLocalizacaoGeograficaEta = new FceSaaLocalizacaoGeograficaEta();
			fceSaaLocalizacaoGeograficaEta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			listTipoComposicoesSelecionadas = new ArrayList<EtaTipoComposicao>();
			ordernarComposicoes();
			Html.atualizar("formSaaTratamentoEsgoto");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	} 
	

	public void desabilitarVazao(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta){
		
		if(!Util.isNullOuVazio(localizacaoBruta.getValorVazao())){
			Integer index = listFceSaaLocalizacaoGeograficaElevatoriaBruta.indexOf(localizacaoBruta);
			localizacaoBruta.setDesabilitarVazao(true);
			listFceSaaLocalizacaoGeograficaElevatoriaBruta.set(index, localizacaoBruta);
		}else{
			MensagemUtil.erro("Informe a vazão correta.");
		}
		
	}
	
	public void habilitarVazao(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta){
		
			Integer index = listFceSaaLocalizacaoGeograficaElevatoriaBruta.indexOf(localizacaoBruta);
			localizacaoBruta.setDesabilitarVazao(Boolean.FALSE);
			listFceSaaLocalizacaoGeograficaElevatoriaBruta.set(index, localizacaoBruta);
	}
	
	public void selecionarTipoComposicao(FceSaaTipoComposicao tipoComposicao){
		if(SaaTipoComposicaoEnum.OUTROS.getId().equals(tipoComposicao.getIdeFceSaaTipoComposicao())) {
			MensagemUtil.alerta(Util.getString("lac_dadosGerais_info001"));
		} else{
			EtaTipoComposicao etaComposicao = new EtaTipoComposicao();
			etaComposicao.setIdeFceSaaTipoComposicao(tipoComposicao);
			listTipoComposicoesSelecionadas.add(etaComposicao);
			listTipoComposicoes.remove(etaComposicao.getIdeFceSaaTipoComposicao());
			ordernarComposicoes();
			
			fceSaaLocalizacaoGeograficaEta.setEtaTipoComposicaoCollection(listTipoComposicoesSelecionadas);
		}
		
	}
	
	public void excluirComposicao(EtaTipoComposicao etaComposicao){
		listTipoComposicoesSelecionadas.remove(etaComposicao);
		fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection().remove(etaComposicao);
		
		listTipoComposicoes.add(etaComposicao.getIdeFceSaaTipoComposicao());
		ordernarComposicoes();
		Html.atualizar("tableComposicoesSelecionadas");
	}	
	
	
	public void salvarDadosEstaçãoTratamento(){

		if(validarDadosTratamentoEsgoto()){
			if(!listFceSaaLocalizacaoGeograficaEta.contains(fceSaaLocalizacaoGeograficaEta)){
				listFceSaaLocalizacaoGeograficaEta.add(this.fceSaaLocalizacaoGeograficaEta);
				fceSaaLocalizacaoGeograficaEta = new FceSaaLocalizacaoGeograficaEta();
				fceSaaLocalizacaoGeograficaEta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
			MensagemUtil.sucesso("Salvo os dados da estação de tratamento de esgoto");
			Html.executarJS("dlgDadosTratamentoEsgoto.hide();");
		}
	}
	
	private boolean validarDadosTratamentoEsgoto() {
		
		boolean retorno = Boolean.TRUE;
		if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaEta.getNomeIdentificacao())){
			retorno = false;
			MensagemUtil.msg0003("Identificação");
		}
		
		if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica())
				|| Util.isNullOuVazio(fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			retorno = false;
			MensagemUtil.msg0003("Coordenada");
		} else if(!Util.isNullOuVazio(fceSaaLocalizacaoGeograficaEta.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()) 
				&& (Util.isNullOuVazio(fceSaaLocalizacaoGeograficaEta.getValorVazaoMedia()) 
				|| fceSaaLocalizacaoGeograficaEta.getValorVazaoMedia() == 0)){
			retorno = false;
			MensagemUtil.msg0003("Vazão");
		} else if(!fceSaaLocalizacaoGeograficaEta.isDesabilitarVazao()){
			retorno = false;
			MensagemUtil.erro("É necessário confirmar o valor da vazão.");
		}
		
		if(CollectionUtils.isEmpty(fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection())){
			retorno = false;
			MensagemUtil.msg0003("Unidades");
		} else{
			for(EtaTipoComposicao comp : fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection()){
				if(Util.isNullOuVazio(comp.getValorQuantidade())){
					retorno = false;
					MensagemUtil.msg0003("Quantidade");
					break;
				}
				if(!comp.getDesabilitarItem()){
					retorno = false;
					MensagemUtil.erro("É necessário confirmar a quantidade de cada composição selecionada.");
					break;
				}
			}
		}
		
		return retorno;
	}

	public void editarDadosTratamentoEsgoto(FceSaaLocalizacaoGeograficaEta localizacaoEta){
		try {
			fceSaaLocalizacaoGeograficaEta = localizacaoEta;
			listTipoComposicoesSelecionadas = localizacaoEta.getEtaTipoComposicaoCollection();
			
			listTipoComposicoes = fceSaaFacade.carregarTipoComposicoes();
			
			List<FceSaaTipoComposicao> listTipoComposicoesRemovidas = new ArrayList<FceSaaTipoComposicao>();
			for(EtaTipoComposicao selecionado :listTipoComposicoesSelecionadas){
				selecionado.setDesabilitarItem(true);
			}
			
			for(EtaTipoComposicao selecionado :listTipoComposicoesSelecionadas){
								
				for(FceSaaTipoComposicao composicao : listTipoComposicoes){
					
					if(composicao.equals(selecionado.getIdeFceSaaTipoComposicao())){
						listTipoComposicoesRemovidas.add(composicao);
					}
				}
			}
			
			listTipoComposicoes.removeAll(listTipoComposicoesRemovidas);
			ordernarComposicoes();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	
	@Override
	public void avancarAba(){
		
		if(validarAba()){
			try {
				salvarFce();
				salvarFceSaa();
				salvarAbas();
				activeTab++;
				isBtnFinalizar = Boolean.FALSE;
				if(activeTab == 3 && isFceTecnico()){
					isBtnFinalizar = Boolean.FALSE;
				}else if(activeTab == 3 && !isFceTecnico()){
					isBtnFinalizar = Boolean.TRUE;
				}else if(activeTab > 3){
					isBtnFinalizar = Boolean.TRUE;
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void changeAba(){
		isBtnFinalizar = Boolean.FALSE;
		
		if(activeTab == 3 && !isFceTecnico()){
			isBtnFinalizar = Boolean.TRUE;
		}else if(activeTab > 3){
			isBtnFinalizar = Boolean.TRUE;
		}	
		
		if(activeTab == 0 && isFceTecnico()){ //#10092
			carregarTipoCaptacaoSelecionadas();
		}
		
		Html.atualizar("formSaa:pngBotoes");
	}
	
	
	public Boolean validarDadosConcedidos(boolean isExibirMsg){
		
		Boolean retorno = Boolean.TRUE;
		
		if(isSuperficial){
			if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getIdeLocalizacaoGeografica())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Coordenadas da captação superficial");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getNomeCorpoHidrico())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Corpo Hídrico");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getValorVazao())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Vazão(m3/dia)");
				}
				retorno = Boolean.FALSE;
			}
		}
		
		if(isSubterranea){
			if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getIdeLocalizacaoGeografica())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Coordenadas da captação subterrânea");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getValorVazao())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Vazão(m3/dia)");
				}
				retorno = Boolean.FALSE;
			}
		}
		
/*		if(!isSuperficial && !isSubterranea){
			if(isExibirMsg){
				MensagemUtil.msg0003("Tipo da captação");
			}
			retorno = Boolean.FALSE;
		}*/
		
		return retorno;
	}
	
	@Override
	public boolean validarAba() {
		
		Boolean retorno = Boolean.FALSE;
		
		if(activeTab == 0){
			
			if(isFceTecnico()){
				//Dados Concedidos
				retorno = validarDadosConcedidos(true);
				if(retorno){
					isDadosGerais = false;
				}
			}else{
				//Dados Gerais
				retorno = validarDadosGerais(true);
				if(retorno){
					isAguaBruta = false;
				}
				
			}
			
			
		}else if(activeTab == 1){
			if(isFceTecnico()){
				
				retorno = validarDadosGerais(true); 
				if(retorno){
					isAguaBruta = false;
				}
			}else{
				retorno = validarAguaBruta(true);
				if(retorno){
					isAguatratada = false;	
				}
			}
			
		}else if(activeTab == 2){
			
			if(isFceTecnico()){
				
				retorno = validarAguaBruta(true);
				if(retorno){
					isAguatratada = false;	
				}
				
			}else{
				retorno = validarAguaTratada(true);
				if(retorno){
					isDistribuicao = false;	
				}
			}
			
		}else if(activeTab == 3){
			
			if(isFceTecnico()){
				retorno = validarAguaTratada(true);
				if(retorno){
					isDistribuicao = false;	
				}
			}else{
				retorno = validarResDistribuicao(true);
			}
			
		}else if(activeTab == 4){
			
			if(isFceTecnico()){
				retorno = validarResDistribuicao(true);
			}
		}
		return retorno;
	}

	@Override
	public void voltarAba(){
		if(activeTab == 0){
			Html.executarJS("sistemaAbastecimentoAgua.hide()");
		}else{
			if(activeTab == 1){ //#10092
				carregarTipoCaptacaoSelecionadas();
			}
			isBtnFinalizar = Boolean.FALSE;
			activeTab--;
		}
	}
	
	public String visualizarGeobahia(LocalizacaoGeografica locGeo) {
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
	}
	
	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlSaa");
		RequestContext.getCurrentInstance().execute("sistemaAbastecimentoAgua.show();");
	}
	
	public void confirmarQtdComposicao(EtaTipoComposicao tipoComposicao){
		
		if(!Util.isNullOuVazio(tipoComposicao.getValorQuantidade())){
			
			for(EtaTipoComposicao item : fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection()){
				if(item.equals(tipoComposicao)){
					Integer index = fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection().indexOf(tipoComposicao);
					fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection().get(index).setValorQuantidade(tipoComposicao.getValorQuantidade());
					fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection().get(index).setDesabilitarItem(true);
				}
			}
		}else{
			MensagemUtil.erro("Informe a quantidade correta");
		}
	}
	
	
	public Boolean validarDadosGerais(boolean isExibirMsg){
		Boolean retorno = Boolean.TRUE;
		
		if(Util.isNullOuVazio(fceSaa.getValorHorizonteProjeto())){
			if(isExibirMsg){
				MensagemUtil.msg0003("Horizonte do projeto");
			}
			retorno = Boolean.FALSE;
		}
		if(Util.isNullOuVazio(fceSaa.getValorPopulacaoAtendida())){
			if(isExibirMsg){
				MensagemUtil.msg0003("População atendida");
			}
			retorno = Boolean.FALSE;
		}
		if(Util.isNullOuVazio(fceSaa.getValorConsumoPerCapta())){
			if(isExibirMsg){
				MensagemUtil.msg0003("Consumo per capta");
			}
			retorno = Boolean.FALSE;
		}
		if(Util.isNullOuVazio(fceSaa.getValorVazaoMedidaProjeto())){
			if(isExibirMsg){
				MensagemUtil.msg0003("Vazão média do projeto");
			}
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}
	
	public Boolean validarAguaBruta(boolean isExibirMsg){
		
		Boolean retorno = Boolean.TRUE;
		
		if(!Util.isNullOuVazio(listFceSaaLocalizacaoGeograficaElevatoriaBruta)){
			for (FceSaaLocalizacaoGeograficaElevatoriaBruta fceBruta: listFceSaaLocalizacaoGeograficaElevatoriaBruta){
				if(Util.isNullOuVazio(fceBruta.getValorVazao())){
					if(isExibirMsg){
						MensagemUtil.msg0003("Vazão(m3/dia)");
					}
					retorno = Boolean.FALSE;
					break;
				}
			}
			
			if(Util.isNullOuVazio(fceSaa.getValorExtTotalAdutoraBruta())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Extensão total da adutora");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaa.getIdeFaixaDiametroAdutoraBruta())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Faixa de diâmetro da adutora");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaa.getIdeTipoMaterialUtilizadoBruta())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Tipo de material predominante da adutora");
				}
				retorno = Boolean.FALSE;
			}else if(fceSaa.getIdeTipoMaterialUtilizadoBruta().getDescricaoTipoMaterialUtilizado().equals("Outros")) {
				MensagemUtil.avisoOutros();
				retorno = Boolean.FALSE;
			}
		} else{
			if(isExibirMsg){
				MensagemUtil.msg0003("Coordenada da elevatória");
			}
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}
	
	public Boolean validarAguaTratada(boolean isExibirMsg){
		
		Boolean retorno = Boolean.TRUE;
		
		if(!Util.isNullOuVazio(listFceSaaLocalizacaoGeograficaEta)){
			
			for (FceSaaLocalizacaoGeograficaEta fceEta: listFceSaaLocalizacaoGeograficaEta){
				if(Util.isNullOuVazio(fceEta.getValorVazaoMedia())){
					if(isExibirMsg){
						MensagemUtil.msg0003("Vazão(m3/dia)");
					}
					retorno = Boolean.FALSE;
					break;
				}
			}
			
			
			if(Util.isNullOuVazio(fceSaa.getValorExtTotalAdutoraTratada())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Extensão total da adutora");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaa.getIdeFaixaDiametroAdutoraTratada())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Faixa de diâmetro da adutora");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaa.getIdeTipoMaterialUtilizadoTratada())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Tipo de material predominante da adutora");
				}
				retorno = Boolean.FALSE;
			}else if(fceSaa.getIdeTipoMaterialUtilizadoTratada().getDescricaoTipoMaterialUtilizado().equals("Outros")) {
				if(isExibirMsg){
					MensagemUtil.avisoOutros();
				}
				retorno = Boolean.FALSE;
			}
			
			if (!Util.isNullOuVazio(listaFceSaaLocalizacaoGeograficaElevatoriaTratada)){
				for (FceSaaLocalizacaoGeograficaElevatoriaTratada fceSaaLocalizacaoGeograficaElevatoriaTratada : listaFceSaaLocalizacaoGeograficaElevatoriaTratada) {
					if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaTratada.getNomeIdentificacao())){
						if(isExibirMsg){
							MensagemUtil.msg0003("Nome Identificação");
						}
						retorno = Boolean.FALSE;
					}
					if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaElevatoriaTratada.getValorVazaoMedia())){
						if(isExibirMsg){
							MensagemUtil.msg0003("Vazão(m3/dia)");
						}
						retorno = Boolean.FALSE;
					}
				}
			} else {
				if(isExibirMsg){
					MensagemUtil.msg0003("Coordenadas da elevatória");
				}
				retorno = Boolean.FALSE;
			}
			
		
		} else{
			if(isExibirMsg){
				MensagemUtil.msg0003("Dados da estação de tratamento");
			}
			retorno = Boolean.FALSE;
		}
		
		return retorno;
		
	}
	
	public Boolean validarResDistribuicao(boolean isExibirMsg){
		
		Boolean retorno = Boolean.TRUE;
		
		if(!Util.isNullOuVazio(listFceSaaLocalizacaoGeograficaReservatorio)){
			for (FceSaaLocalizacaoGeograficaReservatorio fceReservatorio: listFceSaaLocalizacaoGeograficaReservatorio){
				if(Util.isNullOuVazio(fceReservatorio.getNomeIdentificacao()) 
						|| Util.isNullOuVazio(fceReservatorio.getValorCapacidade())
						|| Util.isNullOuVazio(fceReservatorio.getIdeTipoReservatorio())){
					if(isExibirMsg){
						if(Util.isNullOuVazio(fceReservatorio.getNomeIdentificacao())){
							MensagemUtil.msg0003("Identificação");
						}
						if(Util.isNullOuVazio(fceReservatorio.getIdeTipoReservatorio())){
							MensagemUtil.msg0003("Tipo");
						}
						if(Util.isNullOuVazio(fceReservatorio.getValorCapacidade())){
							MensagemUtil.msg0003("Capacidade (m³)");
						}
					}
					retorno = Boolean.FALSE;
					break;
				}
			}
			
			if(Util.isNullOuVazio(fceSaa.getValorReservaExtTotalRede())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Extensão total da rede");
				}
				retorno = Boolean.FALSE;
			}
			if(Util.isNullOuVazio(fceSaa.getIdeFaixaDiametroAdutoraReserva())){
				if(isExibirMsg){
					MensagemUtil.msg0003("Faixa de diâmetro da adutora predominante da tubulação");
				}
				retorno = Boolean.FALSE;
			}
		} else{
			if(isExibirMsg){
				MensagemUtil.msg0003("Coordenada do Reservatório");
			}
			retorno = Boolean.FALSE;
		}
		
		return retorno;
		
	}
	
	public void habilitarQtdComposicao(EtaTipoComposicao tipoComposicao){
		
			for(EtaTipoComposicao item : fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection()){
				if(item.equals(tipoComposicao)){
					Integer index = fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection().indexOf(tipoComposicao);
					fceSaaLocalizacaoGeograficaEta.getEtaTipoComposicaoCollection().get(index).setDesabilitarItem(Boolean.FALSE);
				}
			}
	}	
	
	public void confirmarDadosCapSuperficial(){
		if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getNomeCorpoHidrico())
				|| Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getValorVazao())
				|| Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.getNumPortaria())){
			
			MensagemUtil.erro("É necessário fazer o preenchimento correto dos dados");
		}else{
			fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setDesabilitarLinha(Boolean.TRUE);
		}
	}
	
	public void habilitarDadosCapSuperficial(){
		fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setDesabilitarLinha(Boolean.FALSE);
	}
	
	public void confirmarDadosCapSubterranea(){
		if(Util.isNullOuVazio(fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.getValorVazao())){
			
			MensagemUtil.erro("É necessário fazer o preenchimento correto dos dados");
		}else{
			fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setDesabilitarLinha(Boolean.TRUE);
		}
	}
	
	public void habilitarDadosCapSubterranea(){
		fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setDesabilitarLinha(Boolean.FALSE);
	}
	
	 public void confirmarLinhaReserva(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio){
		 
				 if(Util.isNullOuVazio(localizacaoReservatorio.getNomeIdentificacao())
						 || Util.isNullOuVazio(localizacaoReservatorio.getIdeTipoReservatorio())
						 || Util.isNullOuVazio(localizacaoReservatorio.getValorCapacidade())){
					 
					 MensagemUtil.erro("Faça o preenchimento correto dos campos.");
				 }else{
						Integer index = listFceSaaLocalizacaoGeograficaReservatorio.indexOf(localizacaoReservatorio);
						localizacaoReservatorio.setDesabilitarLinha(true);
						listFceSaaLocalizacaoGeograficaReservatorio.set(index, localizacaoReservatorio);
				 }
	 }
	 
	 public void habilitarLinhaReserva(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio){
		 
		Integer index = listFceSaaLocalizacaoGeograficaReservatorio.indexOf(localizacaoReservatorio);
		localizacaoReservatorio.setDesabilitarLinha(Boolean.FALSE);
		listFceSaaLocalizacaoGeograficaReservatorio.set(index, localizacaoReservatorio);
	 }	 
	
	public void desabilitarVazaoTratamentoEsgoto(FceSaaLocalizacaoGeograficaEta localizacaoEta){
		
		if(!Util.isNullOuVazio(localizacaoEta.getValorVazaoMedia())){
			localizacaoEta.setDesabilitarVazao(true);
			fceSaaLocalizacaoGeograficaEta = localizacaoEta;
		}else{
			MensagemUtil.erro("Informe a vazão correta.");
		}
	}
	
	public void habilitarVazaoTratamentoEsgoto(FceSaaLocalizacaoGeograficaEta localizacaoEta){
		
			localizacaoEta.setDesabilitarVazao(Boolean.FALSE);
			fceSaaLocalizacaoGeograficaEta = localizacaoEta;
	}
	
	public StreamedContent getImprimirRelatorio() {
		try{
			return super.getImprimirRelatorio(super.fce, DOC_FCE_SAA);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Sistema abastecimento de água.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirLocGeoSelecionada() {
		try {
			if(!Util.isNullOuVazio(this.localizacaoGeograficaSelecionada)) {

				if(listFceSaaLocalizacaoGeograficaElevatoriaBruta.contains(fceSaaLocalizacaoGeograficaElevatoriaBruta)){
					fceSaaFacade.excluirFceSaaLocalizacaoBruta(fceSaaLocalizacaoGeograficaElevatoriaBruta);
					listFceSaaLocalizacaoGeograficaElevatoriaBruta.remove(fceSaaLocalizacaoGeograficaElevatoriaBruta);
					fceSaaLocalizacaoGeograficaElevatoriaBruta = new FceSaaLocalizacaoGeograficaElevatoriaBruta();
					fceSaaLocalizacaoGeograficaElevatoriaBruta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}else if(listFceSaaLocalizacaoGeograficaEta.contains(fceSaaLocalizacaoGeograficaEta)) {
					//excluir primeiro os registro da tabela associativa "EtaTipoComposicao
					fceSaaFacade.excluirTipoComposicaoByIdeLocalizacaoEta(fceSaaLocalizacaoGeograficaEta);
					fceSaaFacade.excluirFceSaaLocalizacaoEta(fceSaaLocalizacaoGeograficaEta);
					listFceSaaLocalizacaoGeograficaEta.remove(fceSaaLocalizacaoGeograficaEta);
					fceSaaLocalizacaoGeograficaEta = new FceSaaLocalizacaoGeograficaEta();
					fceSaaLocalizacaoGeograficaEta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}else if(listFceSaaLocalizacaoGeograficaReservatorio.contains(fceSaaLocalizacaoGeograficaReservatorio)){
					fceSaaFacade.excluirFceSaaLocalizacaoReservatorio(fceSaaLocalizacaoGeograficaReservatorio);
					listFceSaaLocalizacaoGeograficaReservatorio.remove(fceSaaLocalizacaoGeograficaReservatorio);
					fceSaaLocalizacaoGeograficaReservatorio = new FceSaaLocalizacaoGeograficaReservatorio();
					fceSaaLocalizacaoGeograficaReservatorio.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}else if(localizacaoSuperficial){
					fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial = new FceSaaLocalizacaoGeograficaDadosConcedidos();
					fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					localizacaoSuperficial = false;
				}else if(localizacaoSubterranea){
					fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea = new FceSaaLocalizacaoGeograficaDadosConcedidos();
					fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					localizacaoSubterranea = false;
				}else {
					fceSaaFacade.excluirFceSaaLocalizacaoTratada(fceSaaLocalizacaoGeograficaElevatoriaTratada);
					listaFceSaaLocalizacaoGeograficaElevatoriaTratada.remove(fceSaaLocalizacaoGeograficaElevatoriaTratada);
					fceSaaLocalizacaoGeograficaElevatoriaTratada = new FceSaaLocalizacaoGeograficaElevatoriaTratada();
					fceSaaLocalizacaoGeograficaElevatoriaTratada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}
				
				super.localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);
				this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
				JsfUtil.addSuccessMessage("Localização excluída com sucesso!");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void confirmarVazaoAguaTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada){
		
		if(!Util.isNullOuVazio(localizacaoTratada.getValorVazaoMedia()) && !Util.isNullOuVazio(localizacaoTratada.getNomeIdentificacao())){
			localizacaoTratada.setDesabilitarVazao(true);
			fceSaaLocalizacaoGeograficaElevatoriaTratada = localizacaoTratada;
		}else{
			MensagemUtil.erro("Informe a identificação e vazão corretamente");
		}
		
	}
	
	public void habilitarVazaoAguaTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada){
		
			localizacaoTratada.setDesabilitarVazao(Boolean.FALSE);
			fceSaaLocalizacaoGeograficaElevatoriaTratada = localizacaoTratada;
	}	

	public void inicializarObjetos() {
		super.activeTab = 0;
		fceSaa = new FceSaa();
		listFaixaAdutoraBruta = new ArrayList<FaixaDiametroAdutora>();
		listFaixaAdutoraTratada = new ArrayList<FaixaDiametroAdutora>();
		listFaixaAdutoraResDist = new ArrayList<FaixaDiametroAdutora>();
		listTipoMaterialUtilizado = new ArrayList<TipoMaterialUtilizado>();
		listFceSaaLocalizacaoGeograficaElevatoriaBruta = new ArrayList<FceSaaLocalizacaoGeograficaElevatoriaBruta>();
		listFceSaaLocalizacaoGeograficaReservatorio = new ArrayList<FceSaaLocalizacaoGeograficaReservatorio>();
		listFceSaaLocalizacaoGeograficaEta = new ArrayList<FceSaaLocalizacaoGeograficaEta>();
		fceSaaLocalizacaoGeograficaEta = new FceSaaLocalizacaoGeograficaEta();
		fceSaaLocalizacaoGeograficaEta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial = new FceSaaLocalizacaoGeograficaDadosConcedidos();
		fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea = new FceSaaLocalizacaoGeograficaDadosConcedidos();
		fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		fceSaaLocalizacaoGeograficaElevatoriaBruta = new FceSaaLocalizacaoGeograficaElevatoriaBruta();
		fceSaaLocalizacaoGeograficaElevatoriaBruta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		fceSaaLocalizacaoGeograficaElevatoriaTratada = new FceSaaLocalizacaoGeograficaElevatoriaTratada();
		fceSaaLocalizacaoGeograficaElevatoriaTratada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		
		listaFceSaaLocalizacaoGeograficaElevatoriaTratada = new ArrayList<FceSaaLocalizacaoGeograficaElevatoriaTratada>();
		
		fceSaaLocalizacaoGeograficaReservatorio = new FceSaaLocalizacaoGeograficaReservatorio();
		fceSaaLocalizacaoGeograficaReservatorio.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		listTipoComposicoes = new ArrayList<FceSaaTipoComposicao>();
		listTipoComposicoesSelecionadas = new ArrayList<EtaTipoComposicao>();
		listTipoReservatorio = new ArrayList<TipoReservatorio>();
		localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		tipoCaptacao = new ArrayList<TipoCaptacao>();
		isBtnFinalizar = Boolean.FALSE;
		tipoCaptacaoSelecionadas = new ArrayList<TipoCaptacao>();
		isAguaBruta = true;
		isAguatratada = true;
		isDistribuicao = true;
		metodoExterno = new MetodoUtil(this, "atualizarDados");		
	}
	
	private void carregarTipoCaptacaoSelecionadas(){ //#10092
		if(isSuperficial){
			TipoCaptacao captacaoSuperficial = new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId(),"Superficial");
			tipoCaptacaoSelecionadas.add(captacaoSuperficial);
		}
		if(isSubterranea){
			TipoCaptacao captacaoLocSubterranea = new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId(),"Subterrânea");
			tipoCaptacaoSelecionadas.add(captacaoLocSubterranea);
		}
	}
	
	private boolean validarFechar() {
		
		Boolean retorno = Boolean.FALSE;
		
		if(activeTab == 0){
			if(isFceTecnico()){
				retorno = validarDadosConcedidos(false);
				if(retorno){
					isDadosGerais = false;
				}
			}else{
				retorno = validarDadosGerais(false);
				if(retorno){
					isAguaBruta = false;
				}
				
			}
			
		}else if(activeTab == 1){
			if(isFceTecnico()){
				retorno = validarDadosGerais(false); 
				if(retorno){
					isAguaBruta = false;
				}
			}else{
				retorno = validarAguaBruta(false);
				if(retorno){
					isAguatratada = false;	
				}
			}
			
		}else if(activeTab == 2){
			if(isFceTecnico()){
				
				retorno = validarAguaBruta(false);
				if(retorno){
					isAguatratada = false;	
				}
				
			}else{
				retorno = validarAguaTratada(false);
				if(retorno){
					isDistribuicao = false;	
				}
			}
			
		}else if(activeTab == 3){
			if(isFceTecnico()){
				retorno = validarAguaTratada(false);
				if(retorno){
					isDistribuicao = false;	
				}
			}else{
				retorno = validarResDistribuicao(false);
			}
			
		}else if(activeTab == 4){
			if(isFceTecnico()){
				retorno = validarResDistribuicao(false);
			}
		}
		return retorno;
	}
	
	public void excluirFceSaaLocalizacaoGeograficaEta() throws Exception{
		fceSaaFacade.excluirFceSaaLocalizacaoEta(fceSaaLocalizacaoGeograficaEta);
		fceSaaLocalizacaoGeograficaEta = new FceSaaLocalizacaoGeograficaEta();
		fceSaaLocalizacaoGeograficaEta.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
	}
	
	public FceSaa getFceSaa() {
		return fceSaa;
	}

	public void setFceSaa(FceSaa fceSaa) {
		this.fceSaa = fceSaa;
	}

	public int getClassificacaoPonto(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	public int getClassificacaoShape(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}
	
	
	public FceSaaLocalizacaoGeograficaEta getFceSaaLocalizacaoGeograficaEta() {
		return fceSaaLocalizacaoGeograficaEta;
	}

	public void setFceSaaLocalizacaoGeograficaEta(
			FceSaaLocalizacaoGeograficaEta fceSaaLocalizacaoGeograficaEta) {
		this.fceSaaLocalizacaoGeograficaEta = fceSaaLocalizacaoGeograficaEta;
	}

	public FceSaaLocalizacaoGeograficaElevatoriaBruta getFceSaaLocalizacaoGeograficaElevatoriaBruta() {
		return fceSaaLocalizacaoGeograficaElevatoriaBruta;
	}

	public void setFceSaaLocalizacaoGeograficaElevatoriaBruta(
			FceSaaLocalizacaoGeograficaElevatoriaBruta fceSaaLocalizacaoGeograficaElevatoriaBruta) {
		this.fceSaaLocalizacaoGeograficaElevatoriaBruta = fceSaaLocalizacaoGeograficaElevatoriaBruta;
	}

	public FceSaaLocalizacaoGeograficaElevatoriaTratada getFceSaaLocalizacaoGeograficaElevatoriaTratada() {
		return fceSaaLocalizacaoGeograficaElevatoriaTratada;
	}

	public void setFceSaaLocalizacaoGeograficaElevatoriaTratada(
			FceSaaLocalizacaoGeograficaElevatoriaTratada fceSaaLocalizacaoGeograficaElevatoriaTratada) {
		this.fceSaaLocalizacaoGeograficaElevatoriaTratada = fceSaaLocalizacaoGeograficaElevatoriaTratada;
	}

	public FceSaaLocalizacaoGeograficaReservatorio getFceSaaLocalizacaoGeograficaReservatorio() {
		return fceSaaLocalizacaoGeograficaReservatorio;
	}

	public void setFceSaaLocalizacaoGeograficaReservatorio(
			FceSaaLocalizacaoGeograficaReservatorio fceSaaLocalizacaoGeograficaReservatorio) {
		this.fceSaaLocalizacaoGeograficaReservatorio = fceSaaLocalizacaoGeograficaReservatorio;
	}


	public FceSaaLocalizacaoGeograficaDadosConcedidos getFceSaaLocalizacaoGeograficaDadosConcedidosSuperficial() {
		return fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial;
	}


	public void setFceSaaLocalizacaoGeograficaDadosConcedidosSuperficial(
			FceSaaLocalizacaoGeograficaDadosConcedidos fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial) {
		this.fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial = fceSaaLocalizacaoGeograficaDadosConcedidosSuperficial;
	}


	public FceSaaLocalizacaoGeograficaDadosConcedidos getFceSaaLocalizacaoGeograficaDadosConcedidosSubterranea() {
		return fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea;
	}


	public void setFceSaaLocalizacaoGeograficaDadosConcedidosSubterranea(
			FceSaaLocalizacaoGeograficaDadosConcedidos fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea) {
		this.fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea = fceSaaLocalizacaoGeograficaDadosConcedidosSubterranea;
	}


	public MetodoUtil getMetodoExterno() {
		return metodoExterno;
	}

	public void setMetodoExterno(MetodoUtil metodoExterno) {
		this.metodoExterno = metodoExterno;
	}

	public List<FaixaDiametroAdutora> getListFaixaAdutoraBruta() {
		return listFaixaAdutoraBruta;
	}

	public void setListFaixaAdutoraBruta(
			List<FaixaDiametroAdutora> listFaixaAdutoraBruta) {
		this.listFaixaAdutoraBruta = listFaixaAdutoraBruta;
	}

	public List<FaixaDiametroAdutora> getListFaixaAdutoraResDist() {
		return listFaixaAdutoraResDist;
	}

	public void setListFaixaAdutoraResDist(
			List<FaixaDiametroAdutora> listFaixaAdutoraResDist) {
		this.listFaixaAdutoraResDist = listFaixaAdutoraResDist;
	}

	public List<FaixaDiametroAdutora> getListFaixaAdutoraTratada() {
		return listFaixaAdutoraTratada;
	}

	public void setListFaixaAdutoraTratada(
			List<FaixaDiametroAdutora> listFaixaAdutoraTratada) {
		this.listFaixaAdutoraTratada = listFaixaAdutoraTratada;
	}

	public List<TipoMaterialUtilizado> getListTipoMaterialUtilizado() {
		return listTipoMaterialUtilizado;
	}

	public void setListTipoMaterialUtilizado(
			List<TipoMaterialUtilizado> listTipoMaterialUtilizado) {
		this.listTipoMaterialUtilizado = listTipoMaterialUtilizado;
	}

	public List<FceSaaTipoComposicao> getListTipoComposicoes() {
		return listTipoComposicoes;
	}

	public void setListTipoComposicoes(
			List<FceSaaTipoComposicao> listTipoComposicoes) {
		this.listTipoComposicoes = listTipoComposicoes;
	}

	public List<EtaTipoComposicao> getListTipoComposicoesSelecionadas() {
		return listTipoComposicoesSelecionadas;
	}

	public void setListTipoComposicoesSelecionadas(
			List<EtaTipoComposicao> listTipoComposicoesSelecionadas) {
		this.listTipoComposicoesSelecionadas = listTipoComposicoesSelecionadas;
	}

	public List<FceSaaLocalizacaoGeograficaEta> getListFceSaaLocalizacaoGeograficaEta() {
		return listFceSaaLocalizacaoGeograficaEta;
	}

	public void setListFceSaaLocalizacaoGeograficaEta(
			List<FceSaaLocalizacaoGeograficaEta> listFceSaaLocalizacaoGeograficaEta) {
		this.listFceSaaLocalizacaoGeograficaEta = listFceSaaLocalizacaoGeograficaEta;
	}

	public List<TipoReservatorio> getListTipoReservatorio() {
		return listTipoReservatorio;
	}

	public void setListTipoReservatorio(List<TipoReservatorio> listTipoReservatorio) {
		this.listTipoReservatorio = listTipoReservatorio;
	}

	public List<FceSaaLocalizacaoGeograficaElevatoriaBruta> getListFceSaaLocalizacaoGeograficaElevatoriaBruta() {
		return listFceSaaLocalizacaoGeograficaElevatoriaBruta;
	}

	public void setListFceSaaLocalizacaoGeograficaElevatoriaBruta(
			List<FceSaaLocalizacaoGeograficaElevatoriaBruta> listFceSaaLocalizacaoGeograficaElevatoriaBruta) {
		this.listFceSaaLocalizacaoGeograficaElevatoriaBruta = listFceSaaLocalizacaoGeograficaElevatoriaBruta;
	}

	public List<FceSaaLocalizacaoGeograficaReservatorio> getListFceSaaLocalizacaoGeograficaReservatorio() {
		return listFceSaaLocalizacaoGeograficaReservatorio;
	}

	public void setListFceSaaLocalizacaoGeograficaReservatorio(
			List<FceSaaLocalizacaoGeograficaReservatorio> listFceSaaLocalizacaoGeograficaReservatorio) {
		this.listFceSaaLocalizacaoGeograficaReservatorio = listFceSaaLocalizacaoGeograficaReservatorio;
	}


	public LocalizacaoGeograficaGenericController getLocalizacaoGeograficaController() {
		return localizacaoGeograficaController;
	}

	public void setLocalizacaoGeograficaSelecionada(
			LocalizacaoGeografica localizacaoGeograficaSelecionada) {
		this.localizacaoGeograficaSelecionada = localizacaoGeograficaSelecionada;
	}

	public List<TipoCaptacao> getTipoCaptacao() {
		return tipoCaptacao;
	}


	public void setTipoCaptacao(List<TipoCaptacao> tipoCaptacao) {
		this.tipoCaptacao = tipoCaptacao;
	}


	public List<TipoCaptacao> getTipoCaptacaoSelecionadas() {
		return tipoCaptacaoSelecionadas;
	}


	public void setTipoCaptacaoSelecionadas(
			List<TipoCaptacao> tipoCaptacaoSelecionadas) {
		this.tipoCaptacaoSelecionadas = tipoCaptacaoSelecionadas;
	}


	public LocalizacaoGeografica getLocalizacaoGeograficaSelecionada() {
		return localizacaoGeograficaSelecionada;
	}


	public boolean isSuperficial() {
		return isSuperficial;
	}


	public void setSuperficial(boolean isSuperficial) {
		this.isSuperficial = isSuperficial;
	}


	public boolean isSubterranea() {
		return isSubterranea;
	}


	public void setSubterranea(boolean isSubterranea) {
		this.isSubterranea = isSubterranea;
	}


	public boolean isBtnFinalizar() {
		return isBtnFinalizar;
	}


	public void isBtnFinalizar(boolean isFinalizar) {
		this.isBtnFinalizar = isFinalizar;
	}


	public boolean isDadosGerais() {
		return isDadosGerais;
	}


	public void setDadosGerais(boolean isDadosGerais) {
		this.isDadosGerais = isDadosGerais;
	}


	public boolean isAguaBruta() {
		return isAguaBruta;
	}


	public void setAguaBruta(boolean isAguaBruta) {
		this.isAguaBruta = isAguaBruta;
	}


	public boolean isAguatratada() {
		return isAguatratada;
	}


	public void setAguatratada(boolean isAguatratada) {
		this.isAguatratada = isAguatratada;
	}


	public boolean isDistribuicao() {
		return isDistribuicao;
	}


	public void setDistribuicao(boolean isDistribuicao) {
		this.isDistribuicao = isDistribuicao;
	}

	public void setBtnFinalizar(boolean isBtnFinalizar) {
		this.isBtnFinalizar = isBtnFinalizar;
	}


	public boolean isLocalizacaoSuperficial() {
		return localizacaoSuperficial;
	}


	public void setLocalizacaoSuperficial(boolean localizacaoSuperficial) {
		this.localizacaoSuperficial = localizacaoSuperficial;
	}


	public boolean isLocalizacaoSubterranea() {
		return localizacaoSubterranea;
	}


	public void setLocalizacaoSubterranea(boolean localizacaoSubterranea) {
		this.localizacaoSubterranea = localizacaoSubterranea;
	}

	public List<FceSaaLocalizacaoGeograficaElevatoriaTratada> getListaFceSaaLocalizacaoGeograficaElevatoriaTratada() {
		return listaFceSaaLocalizacaoGeograficaElevatoriaTratada;
	}

	public void setListaFceSaaLocalizacaoGeograficaElevatoriaTratada(
			List<FceSaaLocalizacaoGeograficaElevatoriaTratada> listaFceSaaLocalizacaoGeograficaElevatoriaTratada) {
		this.listaFceSaaLocalizacaoGeograficaElevatoriaTratada = listaFceSaaLocalizacaoGeograficaElevatoriaTratada;
	}

	public String getMsgConfirmacaoImprimirRelatorio() {
		return msgConfirmacaoImprimirRelatorio;
	}

	public void setMsgConfirmacaoImprimirRelatorio(
			String msgConfirmacaoImprimirRelatorio) {
		this.msgConfirmacaoImprimirRelatorio = msgConfirmacaoImprimirRelatorio;
	}

}
