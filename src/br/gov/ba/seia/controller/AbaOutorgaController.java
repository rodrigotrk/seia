package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ModalidadeOutorga;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("abaOutorgaController")
@ViewScoped
public class AbaOutorgaController extends BaseAbaController{

	@Inject
	private CaptacaoSubterraneaController captacaoSubterraneaController;
	
	private boolean captacaoPrecipitacaoPluvial;
	private boolean captacaoConcessessionarioPublica;
	private boolean captacaoSuperficial;
	private boolean captacaoSubterranea;
	
	private OutorgaLocalizacaoGeografica outorgaLocalizacaoGeograficaAExcluir;
	
	private BigDecimal valorVolumeCaminhaoPipa;

	private Integer qtdTransporteCaminhaoPipa;
	
	private List<OutorgaLocalizacaoGeografica> captacoesSubterraneas;
	
	private List<OutorgaLocalizacaoGeografica> captacoesSuperficiais;
	
	private List<OutorgaLocalizacaoGeografica> lancamentosEfluentes;
	
	private List<OutorgaLocalizacaoGeografica> pocos;
	
	private List<OutorgaLocalizacaoGeografica> intervencoes;

	private PerguntaRequerimento perguntaNRA4P1;
	private PerguntaRequerimento perguntaNRA4P11;
	private PerguntaRequerimento perguntaNRA4P10;
	private PerguntaRequerimento perguntaNRA4P12;
	private PerguntaRequerimento perguntaNRA4P13;
	private PerguntaRequerimento perguntaNRA4P14;
	private PerguntaRequerimento perguntaNRA4P141;
	private PerguntaRequerimento perguntaNRA4P1411;
	private PerguntaRequerimento perguntaNRA4P142;
	private PerguntaRequerimento perguntaNRA4P143;
	private PerguntaRequerimento perguntaNRA4P15;
	private PerguntaRequerimento perguntaNRA4P151;
	
	// Variaveis para a comparação de edição
	private boolean isAlteracaoDadosAba2;
	private boolean isEnquadramento;
	
	private boolean captacaoSuperficialBkp;
	private boolean captacaoSubterraneaBkp;
	private boolean captacaoPrecipitacaoPluvialBkp;
	private boolean captacaoConcessessionarioPublicaBkp;
	
	private BigDecimal valorVolumeCaminhaoPipaBkp;
	private Integer qtdTransporteCaminhaoPipaBkp;
	
	private List<OutorgaLocalizacaoGeografica> captacoesSubterraneasBkp;
	
	private List<OutorgaLocalizacaoGeografica> captacoesSuperficiaisBkp;
	
	private List<OutorgaLocalizacaoGeografica> lancamentosEfluentesBkp;
	
	private List<OutorgaLocalizacaoGeografica> pocosBkp;
	
	private List<OutorgaLocalizacaoGeografica> intervencoesBkp;
	
	private PerguntaRequerimento perguntaNRA4P1Bkp;
	private PerguntaRequerimento perguntaNRA4P11Bkp;
	private PerguntaRequerimento perguntaNRA4P10Bkp;
	private PerguntaRequerimento perguntaNRA4P12Bkp;
	private PerguntaRequerimento perguntaNRA4P13Bkp;
	private PerguntaRequerimento perguntaNRA4P14Bkp;
	private PerguntaRequerimento perguntaNRA4P141Bkp;
	private PerguntaRequerimento perguntaNRA4P1411Bkp;
	private PerguntaRequerimento perguntaNRA4P142Bkp;
	private PerguntaRequerimento perguntaNRA4P143Bkp;
	private PerguntaRequerimento perguntaNRA4P15Bkp;
	private PerguntaRequerimento perguntaNRA4P151Bkp;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	@EJB
	private OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidadeService;
	
	@EJB
	private OutorgaService outorgaService;

	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoFacade;

	private boolean disablePerguntaA4P1;
	
	@Override
	@PostConstruct
	public void init()  {
		this.limparCaptacoes();
		
		super.init();
		this.carregarRespostasVolCaminhaoEMediaTransp();
		
		if(!this.novoRequerimentoController.isImovelRural()){
			this.perguntaNRA4P10.setIndResposta(false);
		}
		
		if(!Util.isNullOuVazio(this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P6())
				&& !Util.isNullOuVazio(this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P6().getIndResposta())){
			
			if(!Util.isNullOuVazio(this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P6().getIndResposta()) && !this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P6().getIndResposta()){
				this.perguntaNRA4P1.setIndResposta(false);
				disablePerguntaA4P1 = true;
			}
		}
		
		this.novoRequerimentoController.listarImovel();
		
	}

	private void limparCaptacoes() {
		this.captacoesSuperficiais = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.captacoesSubterraneas = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.lancamentosEfluentes = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.intervencoes = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.pocos = new ArrayList<OutorgaLocalizacaoGeografica>();
		
		this.captacoesSuperficiaisBkp = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.captacoesSubterraneasBkp = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.lancamentosEfluentesBkp = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.intervencoesBkp = new ArrayList<OutorgaLocalizacaoGeografica>();
		this.pocosBkp = new ArrayList<OutorgaLocalizacaoGeografica>();
		
		this.captacaoSubterranea  = false;
		this.captacaoSuperficial  = false;
		this.captacaoPrecipitacaoPluvial  = false;
		this.captacaoConcessessionarioPublica  = false;
		
		this.captacaoPrecipitacaoPluvialBkp = false;
		this.captacaoConcessessionarioPublicaBkp = false;
	}
	
	public void carregarRespostasVolCaminhaoEMediaTransp(){
		try{
			List<Outorga> outorgas = outorgaService.listarOutorgaByRequerimentoECaptacaoEOuTipoSolicitacao(super.getRequerimento(),new ModalidadeOutorga(ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade()), new TipoSolicitacao [] {new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_OUTORGA.getId())});
			if(!Util.isNullOuVazio(outorgas)){
				Outorga outorga = outorgas.iterator().next();
				this.qtdTransporteCaminhaoPipa = outorga.getQtdTransporteCaminhaoPipa();
				this.valorVolumeCaminhaoPipa = outorga.getValorVolumeCaminhaoPipa();
				
				this.qtdTransporteCaminhaoPipaBkp = this.qtdTransporteCaminhaoPipa;
				this.valorVolumeCaminhaoPipaBkp = this.valorVolumeCaminhaoPipa;
			}
		}catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	protected void carregarPerguntasAba()  {
		this.perguntaNRA4P1 = super.carregarPerguntaByCod("NR_A4_P1");//O empreendimento ou atividade usa ou usará água, realiza ou realizará lançamento de efluente e/ou possui ou possuirá intervenção em corpo hídrico?
		this.perguntaNRA4P10 = super.carregarPerguntaByCod("NR_A4_P10");
		this.perguntaNRA4P11 = super.carregarPerguntaByCod("NR_A4_P11");
		this.perguntaNRA4P12 = super.carregarPerguntaByCod("NR_A4_P12");
		this.perguntaNRA4P13 = super.carregarPerguntaByCod("NR_A4_P13");
		this.perguntaNRA4P14 = super.carregarPerguntaByCod("NR_A4_P14");
		this.perguntaNRA4P141 = super.carregarPerguntaByCod("NR_A4_P141");//O empreendimento faz ou fará captação de água ?
		this.perguntaNRA4P1411 = super.carregarPerguntaByCod("NR_A4_P1411");
		this.perguntaNRA4P142 = super.carregarPerguntaByCod("NR_A4_P142");
		this.perguntaNRA4P143 = super.carregarPerguntaByCod("NR_A4_P143");
		this.perguntaNRA4P15 = super.carregarPerguntaByCod("NR_A4_P15");
		this.perguntaNRA4P151 = super.carregarPerguntaByCod("NR_A4_P151");
		
		this.perguntaNRA4P1Bkp = super.carregarPerguntaByCod("NR_A4_P1");
		this.perguntaNRA4P10Bkp = super.carregarPerguntaByCod("NR_A4_P10");
		this.perguntaNRA4P11Bkp =super.carregarPerguntaByCod("NR_A4_P11");
		this.perguntaNRA4P12Bkp = super.carregarPerguntaByCod("NR_A4_P12");
		this.perguntaNRA4P13Bkp =super.carregarPerguntaByCod("NR_A4_P13");
		this.perguntaNRA4P14Bkp = super.carregarPerguntaByCod("NR_A4_P14");
		this.perguntaNRA4P141Bkp = super.carregarPerguntaByCod("NR_A4_P141");
		this.perguntaNRA4P1411Bkp = super.carregarPerguntaByCod("NR_A4_P1411");
		this.perguntaNRA4P142Bkp = super.carregarPerguntaByCod("NR_A4_P142");
		this.perguntaNRA4P143Bkp = super.carregarPerguntaByCod("NR_A4_P143");
		this.perguntaNRA4P15Bkp = super.carregarPerguntaByCod("NR_A4_P15");
		this.perguntaNRA4P151Bkp = super.carregarPerguntaByCod("NR_A4_P151");
		
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P1);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P10);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P11);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P12);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P13);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P14);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P141);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P1411);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P142);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P143);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P15);
		this.listaPerguntasRequerimento.add(this.perguntaNRA4P151);
		
	}

	@Override
	protected void carregarRespostas() {
		try {
			this.carregarRespostasDasPerguntas();
			
			try {
				List<Outorga> outorgasCaptacao = outorgaService.getOutorgaByModalidadeRequerimento(ModalidadeOutorgaEnum.CAPTACAO,super.getRequerimento());
				
				for(Outorga outorga : outorgasCaptacao){
					OutorgaTipoCaptacao outorgaTipoCaptacao = outorgaService.buscarOutorgaTipoCaptacaoByIdeOutorga(outorga);
					if(!Util.isNull(outorgaTipoCaptacao) && !Util.isNull(outorgaTipoCaptacao.getIdeTipoCaptacao())){
						if(TipoCaptacaoEnum.CAPTACAO_CONCESSIONARIA_PUBLICA.getId().intValue() == outorgaTipoCaptacao.getIdeTipoCaptacao().getIdeTipoCaptacao()){
							captacaoConcessessionarioPublica = true;
							this.captacaoConcessessionarioPublicaBkp = captacaoConcessessionarioPublica;
						}else if(TipoCaptacaoEnum.CAPTACAO_PRECIPITACAO_METEOROLOGICA_PLUVIAL.getId().intValue() == outorgaTipoCaptacao.getIdeTipoCaptacao().getIdeTipoCaptacao()){
							captacaoPrecipitacaoPluvial = true;
							this.captacaoPrecipitacaoPluvialBkp = captacaoPrecipitacaoPluvial;
						}
					}
				}
				
				this.captacoesSubterraneas = this.outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA,super.getRequerimento());
				if(!Util.isNullOuVazio(captacoesSubterraneas)){
					this.captacaoSubterranea  = true;
					this.captacaoSubterraneaBkp = captacaoSubterranea;
				}
				
				this.captacoesSuperficiais = this.outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL,super.getRequerimento());
				if(!Util.isNullOuVazio(captacoesSuperficiais)){
					this.captacaoSuperficial  = true;
					this.captacaoSuperficialBkp = captacaoSuperficial;
				}
				
				this.lancamentosEfluentes = this.outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES,super.getRequerimento());
				if(!Util.isNullOuVazio(lancamentosEfluentes))
					for (OutorgaLocalizacaoGeografica lancamentoEfluentes : lancamentosEfluentes) {
						lancamentoEfluentes.setIdeLocalizacaoGeografica( localizacaoGeograficaService.carregar(lancamentoEfluentes.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() ) );
					}
				
				this.intervencoes = this.outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(ModalidadeOutorgaEnum.INTERVENCAO,super.getRequerimento());
				
				if(!Util.isNullOuVazio(intervencoes))
					for (OutorgaLocalizacaoGeografica intervencao : intervencoes) {
						intervencao.setIdeLocalizacaoGeografica( localizacaoGeograficaService.carregar(intervencao.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() ) );
					}
				
				this.pocos = this.outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(ModalidadeOutorgaEnum.POCO,super.getRequerimento());
				
				if(!Util.isNullOuVazio(pocos))
					for (OutorgaLocalizacaoGeografica poco : pocos) {
						poco.setIdeLocalizacaoGeografica( localizacaoGeograficaService.carregar(poco.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() ) );
					}
				
				for (OutorgaLocalizacaoGeografica poco : pocos) {
					for (OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel : outorgaLocalizacaoGeograficaService.buscarOutorgaLocalizacaoGeograficaImovel(poco)) {
						poco.getListaImovel().add(outorgaLocalizacaoGeograficaImovel.getIdeImovel());
						if(!Util.isNullOuVazio(outorgaLocalizacaoGeograficaImovel.getIdeImovel()) && !Util.isNullOuVazio(outorgaLocalizacaoGeograficaImovel.getIdeImovel().getImovelRural()) )
							poco.setNomeImovel(outorgaLocalizacaoGeograficaImovel.getIdeImovel().getImovelRural().getDesDenominacao());
						else
							poco.setNomeImovel(novoRequerimentoController.getEmpreendimento().getNomEmpreendimento());
					}
				}
				
			} catch (Exception e) {
				Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
			
			this.captacoesSuperficiaisBkp = this.captacoesSuperficiais;
			this.captacoesSubterraneasBkp = this.captacoesSubterraneas;
			this.lancamentosEfluentesBkp = this.lancamentosEfluentes;
			this.intervencoesBkp = this.intervencoes;
			this.pocosBkp = this.pocos;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	
	public void valueChangePerguntaNRA4P1(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		novoRequerimentoController.alteracaoResposta();
		if(!resposta){
			this.perguntaNRA4P10.setIndResposta(null);
			this.perguntaNRA4P11.setIndResposta(null);
			this.perguntaNRA4P12.setIndResposta(null);
			this.perguntaNRA4P13.setIndResposta(null);
			this.perguntaNRA4P14.setIndResposta(null);
			this.perguntaNRA4P15.setIndResposta(null);
		}else{
			if(!this.novoRequerimentoController.isImovelRural()){
				this.perguntaNRA4P10.setIndResposta(false);
			}
		}
		
	}
	
	public void valueChangeAutorizacaoPoco(ValueChangeEvent event){
		try {
			
			Boolean resposta = (Boolean)event.getNewValue();
			if(this.perguntaNRA4P11.getIndResposta()!=resposta) {
				if(this.perguntaNRA4P11.getIndExcluido()==null) {
					this.perguntaNRA4P11.setIndExcluido(null);
				}
				this.perguntaNRA4P11.setIdeRequerimento(super.getRequerimento());
				this.perguntaNRA4P11.setDtcResposta(new Date());				
				this.perguntaNRA4P11.setIndResposta(resposta);
				this.perguntaRequerimentoService.salvarOuAtualizarPerguntaReq(this.perguntaNRA4P11);
			}			
			novoRequerimentoController.alteracaoResposta();
			if(resposta.equals(false) && this.perguntaNRA4P11.getIdeLocalizacaoGeografica()!=null) {
				LocalizacaoGeografica ideLocalizacaoGeografica = this.perguntaNRA4P11.getIdeLocalizacaoGeografica().clone();
				this.perguntaNRA4P11.setIdeLocalizacaoGeografica(null);
				this.localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(ideLocalizacaoGeografica);
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void valueChangePerguntaNR_A4_P14(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		novoRequerimentoController.alteracaoResposta();
		if(resposta){
			this.perguntaNRA4P141.setIndResposta(null);
			this.perguntaNRA4P1411.setIndResposta(null);
			this.perguntaNRA4P142.setIndResposta(null);
			this.perguntaNRA4P13.setIndResposta(null);
		}
	}
	
	public void valueChangePerguntaNRA4P141(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.perguntaNRA4P1411.setIndResposta(null);
			this.qtdTransporteCaminhaoPipa = null;
			this.valorVolumeCaminhaoPipa = null;
		}
	}
	
	public void valueChangePerguntaNRA4P12(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		this.perguntaNRA4P12.setIndResposta(resposta);
		novoRequerimentoController.alteracaoResposta();
	}
	
	public void valueChangePerguntaNRA4P1411(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.qtdTransporteCaminhaoPipa = null;
			this.valorVolumeCaminhaoPipa =null;
		}
		novoRequerimentoController.alteracaoResposta();
	}
	
	
	public void valueChangePerguntaNRA4P15(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.perguntaNRA4P151.setIndResposta(null);	
		}
		novoRequerimentoController.alteracaoResposta();
	}
	
	public void valueChangePerguntaNRA4P151(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		this.perguntaNRA4P151.setIndResposta(resposta);
		novoRequerimentoController.alteracaoResposta();
	}
	
	@Override
	public void salvarAba() {
		try {
			if(validarAba() && captacaoSubterraneaController.validar()){
				
				this.salvar();
				
				JsfUtil.addSuccessMessage("Etapa 4 salva com sucesso.");
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public boolean validarAba() {
		boolean valido = true;
		if(Util.isNullOuVazio(this.perguntaNRA4P1.getIndResposta())){
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P1.getIdePergunta().getNumeracaoPergunta());
			valido = false;
		} else if(this.perguntaNRA4P1.getIndResposta() ){

			if(Util.isNullOuVazio(this.perguntaNRA4P13.getIndResposta())){
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P13.getIdePergunta().getNumeracaoPergunta());
				valido = false;
			}

			if(Util.isNullOuVazio(this.perguntaNRA4P14.getIndResposta())){
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P14.getIdePergunta().getNumeracaoPergunta());
				valido = false;
			} else if(!this.perguntaNRA4P14.getIndResposta()){

				if(Util.isNull(perguntaNRA4P10.getIndResposta()) && (!novoRequerimentoController.isImovelUrbano() && !novoRequerimentoController.getEmpreendimento().getIndCessionario())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P10.getIdePergunta().getNumeracaoPergunta());
					valido = false;
				}
				if(Util.isNullOuVazio(this.perguntaNRA4P11.getIndResposta())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P11.getIdePergunta().getNumeracaoPergunta());
					valido = false;
				} else if(this.perguntaNRA4P11.getIndResposta() ){
					if(Util.isNullOuVazio(this.pocos)){
						JsfUtil.addWarnMessage("Por favor, insira um ponto na pergunta "+this.perguntaNRA4P11.getIdePergunta().getNumeracaoPergunta()+".1.");
						valido = false;
					} else if(!isListaOutorgaLocalizacoGeograficaValida(this.pocos)){
						JsfUtil.addWarnMessage("Por favor, inclua os pontos da perfuração de poço.");
						valido = false;
					}
				}
				if(Util.isNullOuVazio(this.perguntaNRA4P12.getIndResposta())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P12.getIdePergunta().getNumeracaoPergunta());
					valido = false;
				}
				if(Util.isNullOuVazio(this.perguntaNRA4P141.getIndResposta())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P141.getIdePergunta().getNumeracaoPergunta());
					valido = false;
				}else if (this.perguntaNRA4P141.getIndResposta()){ 

					if(Util.isNullOuVazio(this.perguntaNRA4P1411.getIndResposta())){
						JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P1411.getIdePergunta().getNumeracaoPergunta());
						valido = false;
					} else if(this.perguntaNRA4P1411.getIndResposta()){
						if(Util.isNullOuVazio(this.valorVolumeCaminhaoPipa) || this.valorVolumeCaminhaoPipa.compareTo(BigDecimal.ZERO) == 0){
							JsfUtil.addWarnMessage("Por favor, preencha o campo "+this.perguntaNRA4P1411.getIdePergunta().getNumeracaoPergunta()+".1.");
							valido = false;
						}
						if(Util.isNullOuVazio(this.qtdTransporteCaminhaoPipa)){
							JsfUtil.addWarnMessage("Por favor, preencha o campo "+this.perguntaNRA4P1411.getIdePergunta().getNumeracaoPergunta()+".2.");
							valido = false;
						}else if(String.valueOf(this.qtdTransporteCaminhaoPipa).length() > 3){
							JsfUtil.addWarnMessage("O campo "+this.perguntaNRA4P1411.getIdePergunta().getNumeracaoPergunta()+".2. não pode ter mais de 3 caracteres.");
							valido = false;
						}
					}

					if(this.captacaoConcessessionarioPublica == false && this.captacaoPrecipitacaoPluvial == false && this.captacaoSubterranea == false && this.captacaoSuperficial == false){
						JsfUtil.addWarnMessage("Por favor, selecione ao menos um item no campo "+this.perguntaNRA4P141.getIdePergunta().getNumeracaoPergunta());
						valido = false;
					}

					if(!Util.isNullOuVazio(this.captacaoSubterranea) && this.captacaoSubterranea){
						if(Util.isNullOuVazio(this.captacoesSubterraneas)){
							JsfUtil.addWarnMessage("Por favor, inclua pelo menos um ponto de Captação Subterrânea.");
							valido = false;
						}
						else if (!isListaOutorgaLocalizacoGeograficaValida(this.captacoesSubterraneas)) {
							JsfUtil.addWarnMessage("Por favor, inclua os pontos da captação subterrânea.");
							valido = false;
						}
					}

					if(!Util.isNullOuVazio(this.captacaoSuperficial) && this.captacaoSuperficial){
						if(Util.isNullOuVazio(this.captacoesSuperficiais)){
							JsfUtil.addWarnMessage("Por favor, inclua pelo menos um ponto de Captação Superficial.");
							valido = false;
						}
						else if (!isListaOutorgaLocalizacoGeograficaValida(this.captacoesSuperficiais)) {
							JsfUtil.addWarnMessage("Por favor, inclua os pontos da captação superficial.");
							valido = false;
						}
					}
					
				}

				if(Util.isNullOuVazio(this.perguntaNRA4P142.getIndResposta())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P142.getIdePergunta().getNumeracaoPergunta());
					valido = false;
				} else if(this.perguntaNRA4P142.getIndResposta()){
					if(Util.isNullOuVazio(this.lancamentosEfluentes)){
						JsfUtil.addWarnMessage("Por favor, inclua pelo menos um ponto do receptor dos efluentes em Corpo Hídrico");
						valido = false;
					}
					else if (!isListaOutorgaLocalizacoGeograficaValida(this.lancamentosEfluentes)) {
						JsfUtil.addWarnMessage("Por favor, inclua os pontos da captação superficial.");
						valido = false;
					}
				}

				if(Util.isNullOuVazio(this.perguntaNRA4P143.getIndResposta())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P143.getIdePergunta().getNumeracaoPergunta());
					valido = false;
				} else if(this.perguntaNRA4P143.getIndResposta()){
					if(Util.isNullOuVazio(this.intervencoes)){
						JsfUtil.addWarnMessage("Por favor, incluir pelo menos uma nova intervenção");
						valido = false;
					}
					else {
						for (OutorgaLocalizacaoGeografica olg : this.intervencoes) {
							if (!isTheGeomPersistido(olg.getIdeLocalizacaoGeografica())) {
								JsfUtil.addWarnMessage("Por favor, inclua o ponto/shape.");
								valido = false;
							}
							if (!olg.getTipoIntervencao().isBarragem() && !olg.getTipoIntervencao().isAquicultura() && !isTheGeomPersistido(olg.getIdeLocalizacaoGeograficaFinal())) {
								JsfUtil.addWarnMessage("Por favor, inclua o ponto final.");
								valido = false;
							}
						}
					}
				}
				if(Util.isNullOuVazio(this.perguntaNRA4P15.getIndResposta())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P15.getIdePergunta().getNumeracaoPergunta());
					valido = false;
				} else if(this.perguntaNRA4P15.getIndResposta() ){
					if(Util.isNullOuVazio(this.perguntaNRA4P151.getIndResposta())){
						JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta "+this.perguntaNRA4P151.getIdePergunta().getNumeracaoPergunta());
						valido = false;
					}
				}
			}

		}
		return valido;
	}

	@Override
	public void salvar()   {
		
		if(valid()) {
		
				this.excluirOutorgasLocalizacaoGeografica();
		
				this.limpar();
				
				this.salvarCaptacoes();
				
				if( !Util.isNullOuVazio(novoRequerimentoController.getEmpreendimentoRequerimento()) && (!Util.isNullOuVazio(novoRequerimentoController.getEmpreendimentoRequerimento().getNumPortariaAna())||
					!Util.isNullOuVazio(novoRequerimentoController.getEmpreendimentoRequerimento().getNumProcessoAna()))){
					
					EmpreendimentoRequerimento empreendReq = novoRequerimentoController.carregarEmpreendRequerimento();
					novoRequerimentoService.atualizarEmpreendimentoRequerimento(empreendReq);
				}
				
				this.perguntaRequerimentoService.salvaListPerguntaRequerimento(super.listaPerguntasRequerimento, super.getRequerimento());
				
				Requerimento req= novoRequerimentoController.getRequerimentoSelecionado();
				 if(!Util.isNullOuVazio(req) &&	!Util.isNullOuVazio(req.getIdeRequerimento()) && !Util.isNullOuVazio(req.getNumRequerimento())){
					TramitacaoRequerimento tramitacao = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(req.getIdeRequerimento());
					
					if(!Util.isNullOuVazio(tramitacao) && tramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus()){
						this.tramitacaoRequerimentoService.tramitar(req, StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO, super.getOperador());
					}
				}
		}
			
	}

	public boolean valid() {
		Boolean isEnquadramebnto = (Boolean) SessaoUtil.recuperarObjetoSessao("isEnquadramento");
		if(isEnquadramebnto != null) {
			if((isAlteracaoDados() || isAlteracaoDadosAba2) && isEnquadramebnto) return true;
		}else {
			return true;
		}
		return false;
	}
	
	public void excluirOutorgasLocalizacaoGeografica() {
		
		if(captacaoSubterranea && (!perguntaNRA4P1.getIndResposta() || !perguntaNRA4P141.getIndResposta())){
			if(!captacoesSubterraneas.isEmpty()){
				try {
					excluirCaptacao(captacoesSubterraneas);
					captacoesSubterraneas.clear();
					captacaoSubterranea = false;
				}catch (Exception e) {
					JsfUtil.addErrorMessage("Erro ao excluir a(s) Captação(ões)  Subterrânea(s).");
					Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			} 
		}
		
		if(captacaoSuperficial && (!perguntaNRA4P1.getIndResposta() || !perguntaNRA4P141.getIndResposta())){
			if(!captacoesSuperficiais.isEmpty()){
				try{
					excluirCaptacao(captacoesSuperficiais);
					captacoesSuperficiais.clear();
					captacaoSuperficial = false;
				} catch (Exception e) {
					JsfUtil.addErrorMessage("Erro ao excluir a(s) Captação(ões) Superficial(is).");
					Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}		
					
		if(((Util.isNullOuVazio(perguntaNRA4P142.getIndResposta()) || !perguntaNRA4P142.getIndResposta()) && !lancamentosEfluentes.isEmpty()) || !this.perguntaNRA4P1.getIndResposta()){
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : lancamentosEfluentes){
				if (outorgaLocalizacaoGeografica.getIdeOutorga().getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(TipoSolicitacaoEnum.NOVA_OUTORGA.getId())) {
					this.outorgaLocalizacaoGeograficaAExcluir = outorgaLocalizacaoGeografica;
					this.excluir();
				}
			}
			lancamentosEfluentes.clear();
		}
		
		if(((Util.isNullOuVazio(perguntaNRA4P143.getIndResposta()) || perguntaNRA4P143.getIndResposta() == false) && !intervencoes.isEmpty())
				|| !this.perguntaNRA4P1.getIndResposta()){
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : intervencoes){
				if (outorgaLocalizacaoGeografica.getIdeOutorga().getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(TipoSolicitacaoEnum.NOVA_OUTORGA.getId())) {
					this.outorgaLocalizacaoGeograficaAExcluir = outorgaLocalizacaoGeografica;
					this.excluir();
				}
			}
			this.intervencoes.clear();
		}
		
		if(Util.isNullOuVazio(perguntaNRA4P11.getIndResposta()) || perguntaNRA4P11.getIndResposta() == false) {
			
			for (OutorgaLocalizacaoGeografica poco : pocos) {
				if (poco.getIdeOutorga().getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(TipoSolicitacaoEnum.NOVA_OUTORGA.getId())) {
					outorgaLocalizacaoGeograficaAExcluir = poco;
					this.excluir();
				}
			}
			
			pocos.clear();
		}
	}

	private void limpar() {
	
		if(!perguntaNRA4P1.getIndResposta()){
			captacaoConcessessionarioPublica = false;
			captacaoPrecipitacaoPluvial = false;
			captacaoSubterranea = false;
			captacaoSuperficial = false;
		
			for (PerguntaRequerimento perguntaRequerimento : super.listaPerguntasRequerimento) {
				if(!perguntaRequerimento.equals(this.perguntaNRA4P1)){
					perguntaRequerimento.setIndResposta(null);
				}
			}
			
		}else{
			
			if(Util.isNull(perguntaNRA4P141.getIndResposta()) || !perguntaNRA4P141.getIndResposta()){
				captacaoConcessessionarioPublica = false;
				captacaoPrecipitacaoPluvial = false;
				captacaoSubterranea = false;
				captacaoSuperficial = false;
			}
			
		}
		
	
	}

	private void excluirCaptacao(List<OutorgaLocalizacaoGeografica> lista) {
		Boolean excluiuCaptacao = false;
		Outorga outorga = null;
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : lista) {
			if(outorgaLocalizacaoGeografica.getIdeOutorga().getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(TipoSolicitacaoEnum.NOVA_OUTORGA.getId())){
				outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica);
				outorgaLocalizacaoGeograficaFinalidadeService.excluirOutorgaLocalizacaoGeograficaFinalidadesByOLG(outorgaLocalizacaoGeografica);
				this.perguntaRequerimentoService.removerPerguntaReqByIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
				outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
				excluiuCaptacao = true;
				outorga = outorgaLocalizacaoGeografica.getIdeOutorga(); 
			}
		}
		if(excluiuCaptacao){
			outorgaService.excluirTipoCaptacaoByIdeOutorga(outorga);
			excluirOutorga(outorga);
		}
	}

	private void salvarCaptacoes()  {
		this.outorgaService.removerOutorga(super.getRequerimento(),TipoCaptacaoEnum.CAPTACAO_CONCESSIONARIA_PUBLICA.getId(),ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade());
		
		if(this.captacaoConcessessionarioPublica){
			this.salvarOutorga(ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade(),TipoCaptacaoEnum.CAPTACAO_CONCESSIONARIA_PUBLICA.getId());
		}
		
		this.outorgaService.removerOutorga(super.getRequerimento(),TipoCaptacaoEnum.CAPTACAO_PRECIPITACAO_METEOROLOGICA_PLUVIAL.getId(),ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade());

		if(this.captacaoPrecipitacaoPluvial){
			this.salvarOutorga(ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade(),TipoCaptacaoEnum.CAPTACAO_PRECIPITACAO_METEOROLOGICA_PLUVIAL.getId());
		}
		
		if(this.captacaoSubterranea){
			atualizarDadosOutorga(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA);
		}
		
		if(this.captacaoSuperficial){
			atualizarDadosOutorga(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL);
		}
	}
	
	private void atualizarDadosOutorga(TipoCaptacaoEnum tipoCaptacao) {
		Outorga outorga = this.outorgaService.buscarOutorgaByTipoCaptacao(tipoCaptacao, super.getRequerimento());
		outorga.setQtdTransporteCaminhaoPipa(this.qtdTransporteCaminhaoPipa);
		outorga.setValorVolumeCaminhaoPipa(this.valorVolumeCaminhaoPipa);
		
		this.outorgaService.salvarAtualizarOutorga(outorga);
	}

	private void salvarOutorga(Integer ideModalidade, Integer ideTipoCaptacao)  {
		Outorga outorga = new Outorga(super.getRequerimento());
		outorga.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_OUTORGA.getId()));
		outorga.setIdeModalidadeOutorga(new ModalidadeOutorga(ideModalidade));
		outorga.setQtdTransporteCaminhaoPipa(this.qtdTransporteCaminhaoPipa);
		outorga.setValorVolumeCaminhaoPipa(this.valorVolumeCaminhaoPipa);
		this.outorgaService.salvarAtualizarOutorga(outorga);
		
		OutorgaTipoCaptacao outorgaTipoCaptacao = new OutorgaTipoCaptacao(outorga, new TipoCaptacao(ideTipoCaptacao));
		this.outorgaService.salvarAtualizarOutorgaTipoCaptacao(outorgaTipoCaptacao);
	}
	
	public boolean isAlteracaoDados() {
		
		if (this.perguntaNRA4P1Bkp.getIndResposta()    != this.perguntaNRA4P1.getIndResposta()    ||
			this.perguntaNRA4P10Bkp.getIndResposta()   != this.perguntaNRA4P10.getIndResposta()	  ||
			this.perguntaNRA4P11Bkp.getIndResposta()   != this.perguntaNRA4P11.getIndResposta()   ||
			this.perguntaNRA4P12Bkp.getIndResposta()   != this.perguntaNRA4P12.getIndResposta()   ||
			this.perguntaNRA4P13Bkp.getIndResposta()   != this.perguntaNRA4P13.getIndResposta()   ||
			this.perguntaNRA4P14Bkp.getIndResposta()   != this.perguntaNRA4P14.getIndResposta()   ||
			this.perguntaNRA4P141Bkp.getIndResposta()  != this.perguntaNRA4P141.getIndResposta()  ||
			this.perguntaNRA4P1411Bkp.getIndResposta() != this.perguntaNRA4P1411.getIndResposta() ||
			this.perguntaNRA4P142Bkp.getIndResposta()  != this.perguntaNRA4P142.getIndResposta()  ||
			this.perguntaNRA4P143Bkp.getIndResposta()  != this.perguntaNRA4P143.getIndResposta()  ||
			this.perguntaNRA4P15Bkp.getIndResposta()   != this.perguntaNRA4P15.getIndResposta()   ||
			this.perguntaNRA4P151Bkp.getIndResposta()  != this.perguntaNRA4P151.getIndResposta()) return true;


		if(this.captacaoConcessessionarioPublica != this.captacaoConcessessionarioPublicaBkp) return true;
		if(this.captacaoPrecipitacaoPluvial != this.captacaoPrecipitacaoPluvialBkp) return true;
		if(this.valorVolumeCaminhaoPipa != this.valorVolumeCaminhaoPipaBkp) return true;
		if(this.qtdTransporteCaminhaoPipa != this.qtdTransporteCaminhaoPipaBkp) return true;
		
		if(this.captacoesSubterraneas.size() != this.captacoesSubterraneasBkp.size() ||
		   this.captacoesSuperficiais.size() != this.captacoesSuperficiaisBkp.size() ||	
		   this.lancamentosEfluentes.size() != this.lancamentosEfluentesBkp.size() ||
		   this.intervencoes.size() != this.intervencoesBkp.size() ||
		   this.pocos.size() != this.pocosBkp.size()) return true;
				
		for(OutorgaLocalizacaoGeografica captacoesSubterraneas :this.captacoesSubterraneas){
			if (!this.captacoesSubterraneasBkp.contains(captacoesSubterraneas)) return true;
		}
		
		for(OutorgaLocalizacaoGeografica captacoesSuperficiais :this.captacoesSuperficiais){
			if (!this.captacoesSuperficiaisBkp.contains(captacoesSuperficiais)) return true;
		}
		
		for(OutorgaLocalizacaoGeografica lancamentosEfluentes :this.lancamentosEfluentes){
			if (!this.lancamentosEfluentesBkp.contains(lancamentosEfluentes)) return true;
		}
		
		for(OutorgaLocalizacaoGeografica pocos :this.pocos){
			if (!this.pocosBkp.contains(pocos)) return true;
		}
		
		for(OutorgaLocalizacaoGeografica intervencoes :this.intervencoes){
			if (!this.intervencoesBkp.contains(intervencoes)) return true;
		}
		
		return false;
		
	}
	
	public void adicionarOuAtualizarCaptacaoSubterranea(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.adicionarOuAtualizar(captacoesSubterraneas ,outorgaLocalizacaoGeografica);	
	}
	
	public void adicionarOuAtualizarPoco(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.adicionarOuAtualizar(pocos,outorgaLocalizacaoGeografica);	
	}

	public void adicionarOuAtualizarCaptacaoSuperficial(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.adicionarOuAtualizar(captacoesSuperficiais, outorgaLocalizacaoGeografica);	
	}
	
	public void adicionarOuAtualizarLancamentoDeEfluentes(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.adicionarOuAtualizar(lancamentosEfluentes, outorgaLocalizacaoGeografica);	
	}
	
	public void adicionarOuAtualizarIntervencao(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.adicionarOuAtualizar(intervencoes, outorgaLocalizacaoGeografica);	
	}
	
	public void adicionarOuAtualizar(List<OutorgaLocalizacaoGeografica> lista, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		if(lista.contains(outorgaLocalizacaoGeografica)){
			int posicao = lista.indexOf(outorgaLocalizacaoGeografica);
			lista.set(posicao, outorgaLocalizacaoGeografica);
		}else{
			lista.add(outorgaLocalizacaoGeografica);
		}
	}
	
	public void excluirCaptacaoSuperficial(){
		try {
			this.excluir();
			this.captacoesSuperficiais.remove(outorgaLocalizacaoGeograficaAExcluir);
			JsfUtil.addSuccessMessage("Captação Superficial removida com sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao excluir captação superficial.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirCaptacaoSubterranea(){
		try{
			this.excluir();
			this.captacoesSubterraneas.remove(this.outorgaLocalizacaoGeograficaAExcluir);
			JsfUtil.addSuccessMessage("Captação Subterrânea removida com sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Erro ao Excluir a Captação Subterrânea.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}
	
	public void excluirPerfuracaoPoco(){
		try{
			this.excluir();
			this.pocos.remove(this.outorgaLocalizacaoGeograficaAExcluir);
			JsfUtil.addSuccessMessage("Perfuração de poço removida com sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Erro ao Excluir a Perfuração de poço.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}
	
	public void excluirIntervencao(){
		try{
			this.excluir();
			this.intervencoes.remove(this.outorgaLocalizacaoGeograficaAExcluir);
			JsfUtil.addSuccessMessage("Intervenção em corpo hidrico removido com sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Erro ao excluir intervenção em corpo hidrico.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	public void excluirLancamento(){
		try{
			this.excluir();
			this.lancamentosEfluentes.remove(this.outorgaLocalizacaoGeograficaAExcluir);
			JsfUtil.addSuccessMessage("Lançamento de efluentes em corpo hidrico removido com sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Erro ao excluir lançamento de efluentes em corpo hidrico: " + exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	private void excluir()  {
		this.outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeograficaImovel(this.outorgaLocalizacaoGeograficaAExcluir);
		this.outorgaLocalizacaoGeograficaFinalidadeService.excluirOutorgaLocalizacaoGeograficaFinalidadesByOLG(this.outorgaLocalizacaoGeograficaAExcluir);
		this.perguntaRequerimentoService.removerPerguntaReqByIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeograficaAExcluir);
		this.outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeografica(this.outorgaLocalizacaoGeograficaAExcluir);			
		List<OutorgaLocalizacaoGeografica> list = this.outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeoByIdOutorga(this.outorgaLocalizacaoGeograficaAExcluir.getIdeOutorga());
		if(Util.isNullOuVazio(list)){
			this.outorgaService.excluirTipoReceptorByIdeOutorga(this.outorgaLocalizacaoGeograficaAExcluir.getIdeOutorga());
			this.outorgaService.excluirTipoCaptacaoByIdeOutorga(this.outorgaLocalizacaoGeograficaAExcluir.getIdeOutorga());
			this.excluirOutorga(this.outorgaLocalizacaoGeograficaAExcluir.getIdeOutorga());
		}
	}
	

	private void excluirOutorga(Outorga outorga){
		try {
			this.outorgaService.removerOutorga(outorga);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao Excluir a Outorga: " + e.getMessage());
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que verifica se o <i>the_geom</i> do {@link DadoGeografico} foi persistido no banco.
	 * 
	 * @author eduardo.fernandes
	 * @since 29/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8194">#8194</a>
	 * @param localizacaoGeografica
	 * @return <b>true</b> caso não haja <i>the_geom</i>.
	 */
	public boolean isTheGeomPersistido(LocalizacaoGeografica localizacaoGeografica) {
		try {
			return !Util.isNullOuVazio(locGeoFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da localização geográfica.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private boolean isListaOutorgaLocalizacoGeograficaValida(List<OutorgaLocalizacaoGeografica> lista) {
		for (OutorgaLocalizacaoGeografica olg : lista) {
			if (!isTheGeomPersistido(olg.getIdeLocalizacaoGeografica())) {
				return false;
			}
		}
		return true;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P1() {
		return perguntaNRA4P1;
	}

	public void setPerguntaNR_A4_P1(PerguntaRequerimento perguntaNRA4P1) {
		this.perguntaNRA4P1 = perguntaNRA4P1;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P11() {
		return perguntaNRA4P11;
	}

	public void setPerguntaNR_A4_P11(PerguntaRequerimento perguntaNRA4P11) {
		this.perguntaNRA4P11 = perguntaNRA4P11;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P10() {
		return perguntaNRA4P10;
	}

	public void setPerguntaNR_A4_P10(PerguntaRequerimento perguntaNRA4P10) {
		this.perguntaNRA4P10 = perguntaNRA4P10;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P12() {
		return perguntaNRA4P12;
	}

	public void setPerguntaNR_A4_P12(PerguntaRequerimento perguntaNRA4P12) {
		this.perguntaNRA4P12 = perguntaNRA4P12;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P13() {
		return perguntaNRA4P13;
	}

	public void setPerguntaNR_A4_P13(PerguntaRequerimento perguntaNRA4P13) {
		this.perguntaNRA4P13 = perguntaNRA4P13;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P14() {
		return perguntaNRA4P14;
	}

	public void setPerguntaNR_A4_P14(PerguntaRequerimento perguntaNRA4P14) {
		this.perguntaNRA4P14 = perguntaNRA4P14;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P141() {
		return perguntaNRA4P141;
	}

	public void setPerguntaNR_A4_P141(PerguntaRequerimento perguntaNRA4P141) {
		this.perguntaNRA4P141 = perguntaNRA4P141;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P1411() {
		return perguntaNRA4P1411;
	}

	public void setPerguntaNR_A4_P1411(PerguntaRequerimento perguntaNRA4P1411) {
		this.perguntaNRA4P1411 = perguntaNRA4P1411;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P142() {
		return perguntaNRA4P142;
	}

	public void setPerguntaNR_A4_P142(PerguntaRequerimento perguntaNRA4P142) {
		this.perguntaNRA4P142 = perguntaNRA4P142;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P143() {
		return perguntaNRA4P143;
	}

	public void setPerguntaNR_A4_P143(PerguntaRequerimento perguntaNRA4P143) {
		this.perguntaNRA4P143 = perguntaNRA4P143;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P15() {
		return perguntaNRA4P15;
	}

	public void setPerguntaNR_A4_P15(PerguntaRequerimento perguntaNRA4P15) {
		this.perguntaNRA4P15 = perguntaNRA4P15;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P151() {
		return perguntaNRA4P151;
	}

	public void setPerguntaNR_A4_P151(PerguntaRequerimento perguntaNRA4P151) {
		this.perguntaNRA4P151 = perguntaNRA4P151;
	}

	public boolean isCaptacaoPrecipitacaoPluvial() {
		return captacaoPrecipitacaoPluvial;
	}

	public void setCaptacaoPrecipitacaoPluvial(boolean captacaoPrecipitacaoPluvial) {
		this.captacaoPrecipitacaoPluvial = captacaoPrecipitacaoPluvial;
	}

	public boolean isCaptacaoConcessessionarioPublica() {
		return captacaoConcessessionarioPublica;
	}

	public void setCaptacaoConcessessionarioPublica(boolean captacaoConcessessionarioPublica) {
		this.captacaoConcessessionarioPublica = captacaoConcessessionarioPublica;
	}

	public boolean isCaptacaoSuperficial() {
		return captacaoSuperficial;
	}

	public void setCaptacaoSuperficial(boolean captacaoSuperficial) {
		this.captacaoSuperficial = captacaoSuperficial;
	}

	public boolean isCaptacaoSubterranea() {
		return captacaoSubterranea;
	}

	public void setCaptacaoSubterranea(boolean captacaoSubterranea) {
		this.captacaoSubterranea = captacaoSubterranea;
	}

	public BigDecimal getValorVolumeCaminhaoPipa() {
		return valorVolumeCaminhaoPipa;
	}

	public void setValorVolumeCaminhaoPipa(BigDecimal valorVolumeCaminhaoPipa) {
		this.valorVolumeCaminhaoPipa = valorVolumeCaminhaoPipa;
	}

	public Integer getQtdTransporteCaminhaoPipa() {
		return qtdTransporteCaminhaoPipa;
	}

	public void setQtdTransporteCaminhaoPipa(Integer qtdTransporteCaminhaoPipa) {
		if(qtdTransporteCaminhaoPipa != null && qtdTransporteCaminhaoPipa.intValue() != 0)
			this.qtdTransporteCaminhaoPipa = qtdTransporteCaminhaoPipa;
		else
			this.qtdTransporteCaminhaoPipa = null;
	}

	public Collection<OutorgaLocalizacaoGeografica> getCaptacoesSubterraneas() {
		return captacoesSubterraneas;
	}

	public List<OutorgaLocalizacaoGeografica> getCaptacaoesSuperficiais() {
		return captacoesSuperficiais;
	}

	public void setCaptacaoesSuperficiais(List<OutorgaLocalizacaoGeografica> captacaoesSuperficiais) {
		this.captacoesSuperficiais = captacaoesSuperficiais;
	}

	public List<OutorgaLocalizacaoGeografica> getLancamentosEfluentes() {
		return lancamentosEfluentes;
	}

	public void setLancamentosEfluentes(List<OutorgaLocalizacaoGeografica> lancamentosEfluentes) {
		this.lancamentosEfluentes = lancamentosEfluentes;
	}

	public List<OutorgaLocalizacaoGeografica> getIntervencoes() {
		return intervencoes;
	}

	public void setIntervencoes(List<OutorgaLocalizacaoGeografica> intervencoes) {
		this.intervencoes = intervencoes;
	}

	public void setCaptacoesSubterraneas(List<OutorgaLocalizacaoGeografica> captacoesSubterraneas) {
		this.captacoesSubterraneas = captacoesSubterraneas;
	}

	public OutorgaLocalizacaoGeografica getOutorgaLocalizacaoGeograficaAExcluir() {
		return outorgaLocalizacaoGeograficaAExcluir;
	}

	public void setOutorgaLocalizacaoGeograficaAExcluir(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeograficaAExcluir) {
		this.outorgaLocalizacaoGeograficaAExcluir = outorgaLocalizacaoGeograficaAExcluir;
	}

	public List<OutorgaLocalizacaoGeografica> getCaptacoesSuperficiais() {
		return captacoesSuperficiais;
	}

	public void setCaptacoesSuperficiais(List<OutorgaLocalizacaoGeografica> captacoesSuperficiais) {
		this.captacoesSuperficiais = captacoesSuperficiais;
	}

	public List<OutorgaLocalizacaoGeografica> getPocos() {
		return pocos;
	}

	public void setPocos(List<OutorgaLocalizacaoGeografica> pocos) {
		this.pocos = pocos;
	}

	
	public boolean isTodosImoveisCadastrados(Collection<OutorgaLocalizacaoGeografica> lista) throws Exception{
		return this.removerImoveisCadastrados(lista).isEmpty();
	}

	private List<Imovel> removerImoveisCadastrados(Collection<OutorgaLocalizacaoGeografica> lista) throws Exception {
		this.novoRequerimentoController.listarImovel();
		List<Imovel> listaImovel = new ArrayList<Imovel>(this.novoRequerimentoController.getListaImovel());
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : lista) {
			this.carregarImoveis(outorgaLocalizacaoGeografica);
			listaImovel.removeAll(outorgaLocalizacaoGeografica.getListaImovel());
		}
		return listaImovel;
	}
	
	private void carregarImoveis(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {
		for (OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel : outorgaLocalizacaoGeograficaService.buscarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica)) {
			outorgaLocalizacaoGeografica.getListaImovel().add(outorgaLocalizacaoGeograficaImovel.getIdeImovel());
		}
	}

	public boolean isDisablePerguntaA4P1() {
		return disablePerguntaA4P1;
	}

	public void setDisablePerguntaA4P1(boolean disablePerguntaA4P1) {
		this.disablePerguntaA4P1 = disablePerguntaA4P1;
	}

	public boolean isAlteracaoDadosAba2() {
		return isAlteracaoDadosAba2;
	}

	public void setAlteracaoDadosAba2(boolean isAlteracaoDadosAba2) {
		this.isAlteracaoDadosAba2 = isAlteracaoDadosAba2;
	}

	public boolean isEnquadramento() {
		return isEnquadramento;
	}

	public void setEnquadramento(boolean isEnquadramento) {
		this.isEnquadramento = isEnquadramento;
	}

	public boolean isCaptacaoSubterraneaBkp() {
		return captacaoSubterraneaBkp;
	}

	public void setCaptacaoSubterraneaBkp(boolean captacaoSubterraneaBkp) {
		this.captacaoSubterraneaBkp = captacaoSubterraneaBkp;
	}

	public boolean isCaptacaoSuperficialBkp() {
		return captacaoSuperficialBkp;
	}

	public void setCaptacaoSuperficialBkp(boolean captacaoSuperficialBkp) {
		this.captacaoSuperficialBkp = captacaoSuperficialBkp;
	}
}
