package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CaracteristicaFlorestaProducao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.FlorestalAtoAmbiental;
import br.gov.ba.seia.entity.FlorestalAtoAmbientalPK;
import br.gov.ba.seia.entity.FlorestalCaracteristicaFlorestaProducao;
import br.gov.ba.seia.entity.FlorestalCaracteristicaFlorestaProducaoPK;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.TipoReservaLegal;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.ProgramaGovernoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.enumerator.TipologiaCefirEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaProdutivaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.EnderecoEmpreendimentoService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("florestalController")
@ViewScoped
public class FlorestalController {
	
	@Inject
	private NovoRequerimentoController novoRequerimentoController;
	
	@Inject
	private AbaFlorestalController abaFlorestalController;
	
	@EJB
	private PerguntaService perguntaService;

	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;

	@EJB
	private FlorestalService florestalService;

	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;
	
	@EJB
	private ImovelService imovelService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoFacade;
	
	@EJB
	private AreaProdutivaService areaProdutivaService;
	
	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;

	private Florestal florestal;
	
	private List<PerguntaRequerimento> listaPerguntasRequerimento;
	
	private PerguntaRequerimento perguntaNR_A5_DFLO_P11;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P111;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P12;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P13;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P14;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P15;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P16;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P17;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P18;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P19;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p10;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p101;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p113;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p12;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p13;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p14;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p15;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p16;
	private PerguntaRequerimento perguntaNR_A5_DFLO_P1p11;

	
	private Collection<AtoAmbiental> atosAmbientais;
	private Collection<TipoReservaLegal> tiposReservaLegal;
	private Collection<CaracteristicaFlorestaProducao> caracteristicasFlorestaProducao;
	private CaracteristicaFlorestaProducao caracteristicaFlorestaProducaoNenhumaDasAnteriores;
	private Collection<Imovel> imoveis;
	
	private boolean sistemaDeReposicaoFlorestalImplantado,editMode = false;
	
	private boolean programaGovernoASV;
	
	private BigDecimal numAreaPergunta121;
	private BigDecimal numAreaPergunta132;
	private BigDecimal numAreaPergunta142;
	private BigDecimal numAreaPergunta151;
	private BigDecimal numAreaPergunta162;
	private BigDecimal numAreaPergunta181;
	private BigDecimal numAreaPergunta192;
	
	private EmpreendimentoRequerimento empreendimentoRequerimento;
	private boolean isBloquearDQC;
	
	public void load() {
		try {
			this.editMode = false;
			
			prepararDialog();
			
			this.florestal = new Florestal(this.novoRequerimentoController.getRequerimentoSelecionado());
			
			if(!this.novoRequerimentoController.isImovelRural()){
				this.perguntaNR_A5_DFLO_P111.setIndResposta(false);
				this.perguntaNR_A5_DFLO_P13.setIndResposta(false);
				this.perguntaNR_A5_DFLO_P14.setIndResposta(false);
				this.perguntaNR_A5_DFLO_P1p11.setIndResposta(false);
			}
			
			for (Florestal florestal : this.abaFlorestalController.getFlorestais()) {
				this.imoveis.remove(florestal.getIdeImovel());
			}
			
			if(!novoRequerimentoController.isImovelUrbano() && !novoRequerimentoController.isCessionario() && !novoRequerimentoController.isConversaoTcraLac()){
				
			}
			else{
				this.florestal.setIdeImovel(this.novoRequerimentoController.getListaImovel().get(0));
			}
			caracteristicaFlorestaProducaoNenhumaDasAnteriores = new CaracteristicaFlorestaProducao();
			caracteristicaFlorestaProducaoNenhumaDasAnteriores.setNomCaracteristicaFlorestaProducao("Nenhuma das características anteriores");
			
			programaGovernoASV = habilitaPontoASVProgramaGoverno();
			verificarEmpreendimentoBloqueioDQC();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	private void prepararDialog() throws Exception {
		this.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();
		
		this.limparAreas();
		
		this.imoveis = this.novoRequerimentoController.listarImovel();
		
		this.carregarPerguntasAba();
		
		this.carregarListas();
	}
	
	public void editar(Florestal florestal){
		try {
			this.editMode = true;
			
			this.carregar(florestal);
			
			//corrigi o erro da nova pergunta que não existia para os registros antigos
			if(!Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p11) && Util.isNull(this.perguntaNR_A5_DFLO_P1p11.getIndResposta())) {
				for (PerguntaRequerimento perguntaRequerimento : listaPerguntasRequerimento) {
					if (perguntaRequerimento.getIdePergunta().getIdePergunta().equals(this.perguntaNR_A5_DFLO_P1p11.getIdePergunta().getIdePergunta())){
						perguntaRequerimento.setIndResposta(false);
					}
				}
				
				Requerimento requerimento = novoRequerimentoController.getRequerimentoSelecionado();
				this.perguntaRequerimentoService.salvaListPerguntaRequerimentoFlorestal(listaPerguntasRequerimento,requerimento, this.florestal.getIdeImovel());
			}
			
			programaGovernoASV = habilitaPontoASVProgramaGoverno();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void visualizar(Florestal florestal){
		try {
			this.editMode = false;
			
			this.carregar(florestal);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregar(Florestal florestal) throws Exception {
		prepararDialog();
		
		this.florestal = this.florestalService.obterFlorestal(florestal.getIdeFlorestal());
		
		this.carregarRespostas(florestal.getIdeRequerimento());
		
		this.carregarListaFlorestalAtoAmbiental(florestal);
		
		this.carregaListaCaracteristicasFlorestaProducao(florestal);
		
	}
	
	private void carregarPerguntasAba() throws Exception {
		this.perguntaNR_A5_DFLO_P11 = this.carregarPerguntaByCod("NR_A5_DFLO_P11");
		this.perguntaNR_A5_DFLO_P111 = this.carregarPerguntaByCod("NR_A5_DFLO_P111");
		this.perguntaNR_A5_DFLO_P12 = this.carregarPerguntaByCod("NR_A5_DFLO_P12");
		this.perguntaNR_A5_DFLO_P13 = this.carregarPerguntaByCod("NR_A5_DFLO_P13");
		this.perguntaNR_A5_DFLO_P14 = this.carregarPerguntaByCod("NR_A5_DFLO_P14");
		this.perguntaNR_A5_DFLO_P15 = this.carregarPerguntaByCod("NR_A5_DFLO_P15");
		this.perguntaNR_A5_DFLO_P16 = this.carregarPerguntaByCod("NR_A5_DFLO_P16");
		this.perguntaNR_A5_DFLO_P17 = this.carregarPerguntaByCod("NR_A5_DFLO_P17");
		this.perguntaNR_A5_DFLO_P18 = this.carregarPerguntaByCod("NR_A5_DFLO_P18");
		this.perguntaNR_A5_DFLO_P19 = this.carregarPerguntaByCod("NR_A5_DFLO_P19");
		this.perguntaNR_A5_DFLO_P1p10 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p10");
		this.perguntaNR_A5_DFLO_P1p101 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p101");
		this.perguntaNR_A5_DFLO_P1p113 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p1013");
		this.perguntaNR_A5_DFLO_P1p12 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p102");
		this.perguntaNR_A5_DFLO_P1p13 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p103");		
		this.perguntaNR_A5_DFLO_P1p14 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p104");
		this.perguntaNR_A5_DFLO_P1p15 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p105");
		this.perguntaNR_A5_DFLO_P1p16 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p106");
		this.perguntaNR_A5_DFLO_P1p11 = this.carregarPerguntaByCod("NR_A5_DFLO_P1p11");
		
		
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P11);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P111);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P12);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P13);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P14);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P15);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P16);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P17);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P18);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P19);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p10);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p101);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p113);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p12);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p13);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p14);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p15);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p16);
		this.listaPerguntasRequerimento.add(this.perguntaNR_A5_DFLO_P1p11);
		
	}
	
	private PerguntaRequerimento carregarPerguntaByCod(String codPergunta) throws Exception {
		Pergunta pergunta = this.perguntaService.carregarPerguntabyCodPergunta(codPergunta);
		return new PerguntaRequerimento(pergunta);
	}
	
	private void carregarListas() throws Exception {
		this.tiposReservaLegal =  this.florestalService.carregarListaFlorestalTipoReservaLegal();
		this.carregarImoveis();
		this.caracteristicasFlorestaProducao = this.florestalService.obterCaracteristicasFlorestaProducaoByIndAtivo();
		this.atosAmbientais = this.atoAmbientalService.obterAtoAmbientalVolumeFlorestal();
	}

	private void carregarImoveis() {
		this.imoveis = this.novoRequerimentoController.getListaImovel();
		if(this.isImovelUnico()){
			Imovel unicoImovelNaLista = this.imoveis.iterator().next();
			this.florestal.setIdeImovel(unicoImovelNaLista);
		}
	}

	public boolean isImovelUnico() {
		if(Util.isNull(this.imoveis))
			return false;
		return this.novoRequerimentoController.getListaImovel().size() == 0;
	}
	
	private void carregarRespostas(Requerimento requerimento) {
		try {
			this.perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(this.listaPerguntasRequerimento, requerimento,this.florestal.getIdeImovel());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	private void carregarListaFlorestalAtoAmbiental(Florestal florestal) {
		
		Exception erro = null;
		
		try {
			List<FlorestalAtoAmbiental> listaFlorestalAtoAmbiental = this.florestalService.obterListaFlorestalAtoSelecionado(florestal);
			for (FlorestalAtoAmbiental florestalAtoAmbiental : listaFlorestalAtoAmbiental) {
				for (AtoAmbiental atoAmbiental : this.atosAmbientais) {
					if (florestalAtoAmbiental.getIdeAtoAmbiental().equals(atoAmbiental)){
						atoAmbiental.setRowSelect(true);
						atoAmbiental.setNumPortariaRVFR(florestalAtoAmbiental.getNumPortaria());
						break;
					}
				}
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao carregar a lista de RVFR:" + e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void carregaListaCaracteristicasFlorestaProducao(Florestal florestal) {
		
		Exception erro = null;
		
		try {

			List<FlorestalCaracteristicaFlorestaProducao> listaCaracteristicasFloresta = this.florestalService
					.obterListaFlorestaProducaoSelecionado(florestal);
			this.florestal.setCaracteristicasFlorestaProducao(new ArrayList<CaracteristicaFlorestaProducao>());
			for (FlorestalCaracteristicaFlorestaProducao producao : listaCaracteristicasFloresta) {
				for (CaracteristicaFlorestaProducao caracteristica : this.caracteristicasFlorestaProducao) {
					if (producao.getIdeCaracteristicaFlorestaProducao().equals(caracteristica)) {
						this.florestal.getCaracteristicasFlorestaProducao().add(caracteristica);
						break;
					}
				}
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao carregar a lista de Floresta Produção: " + e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P111(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setIdeTipoReservaLegal(null);
			this.florestal.setNumAreaReservaLegal(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P12(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumAreaServidaoFlorestal(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P13(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumProcessoAprovacaoServidaoFlorestal(null);
			this.florestal.setNumAreaProcessoAprovacaoServidaoFlorestal(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P14(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumProcessoAprovacaoReservaLegal(null);
			this.florestal.setNumAreaProcessoAprovacaoReservaLegal(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P15(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumAreaSuprimida(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P16(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumAreaQueimada(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P18(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumAreaPlanoManejoFlorestalSustentavel(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P19(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumAreaProcessoAprovacaoReservaLegal(null);
			this.florestal.setNumAreaCorteUnidadeProducao(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P1p101(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.perguntaNR_A5_DFLO_P1p113.setIndResposta(null);
			this.perguntaNR_A5_DFLO_P1p15.setIndResposta(null);
			this.florestal.setNumProcessoLicenciamentoEstado(null);
			this.florestal.setNumPortariaEstado(null);
		}
	}
	

	public void valueChangePerguntaNR_A5_DFLO_P1p113(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumProcessoLicenciamentoEstado(null);
			this.florestal.setNumPortariaEstado(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P1p12(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumProcessoRegistroFlorestaPlantada(null);
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P1p13(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setCaracteristicasFlorestaProducaoCollection(new ArrayList<FlorestalCaracteristicaFlorestaProducao>());
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P1p11(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.florestal.setNumProcessoLicenciamentoEstado(null);
			this.florestal.setNumPortariaEstado(null);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void valueChangePerguntaNenhumaDasAnteriores(ValueChangeEvent event) {
		ArrayList<CaracteristicaFlorestaProducao> resposta = (ArrayList<CaracteristicaFlorestaProducao>) event.getNewValue();
		CaracteristicaFlorestaProducao nenhumaDasAnteriores = new CaracteristicaFlorestaProducao(4, "Nenhuma das características anteriores", true);
		if(Util.isNullOuVazio(florestal.getCaracteristicasFlorestaProducao()))
			florestal.setCaracteristicasFlorestaProducao(new ArrayList<CaracteristicaFlorestaProducao>());
		
		if(resposta.contains(nenhumaDasAnteriores) && !((ArrayList<CaracteristicaFlorestaProducao>) event.getOldValue()).contains(nenhumaDasAnteriores)){
			((ArrayList<CaracteristicaFlorestaProducao>) event.getNewValue()).clear();
			((ArrayList<CaracteristicaFlorestaProducao>) event.getNewValue()).add(nenhumaDasAnteriores);
		}else{
			if( !Util.isNullOuVazio(((ArrayList<CaracteristicaFlorestaProducao>) event.getOldValue())) && ((ArrayList<CaracteristicaFlorestaProducao>) event.getOldValue()).size() == 1 
					&& ((ArrayList<CaracteristicaFlorestaProducao>) event.getOldValue()).get(0).getIdeCaracteristicaFlorestaProducao().equals(4) ){
				
				((ArrayList<CaracteristicaFlorestaProducao>) event.getNewValue()).remove(nenhumaDasAnteriores);
			}
		}
	}
	
	public void valueChangePerguntaNR_A5_DFLO_P1p16(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			for (AtoAmbiental atoAmbiental : this.atosAmbientais) {
				atoAmbiental.setRowSelect(false);
			}
		}
	}
	
	public void salvar(){
		if(this.validar()){
			try{
				Requerimento requerimento = novoRequerimentoController.getRequerimentoSelecionado();
				this.perguntaRequerimentoService.salvaListPerguntaRequerimentoFlorestal(listaPerguntasRequerimento,requerimento, this.florestal.getIdeImovel());
				this.florestal.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.NOVO_FLORESTAL.getId()));
				
				this.florestalService.salvarAtualizarFlorestal(florestal);
				
				if(this.editMode){
					this.florestalService.excluirFlorestalRVFRbyIdeFlorestal(this.florestal.getIdeFlorestal());
					this.florestalService.excluirFlorestaProducao(this.florestal.getIdeFlorestal());
				}
				
				this.salvarFlorestalAtoAmbiental();
				this.salvarCaracteristicasFlorestal();
				
				if(this.editMode){
					JsfUtil.addSuccessMessage("Ato florestal atualizado com sucesso!");
				}else{
					JsfUtil.addSuccessMessage("Ato florestal salvo com sucesso!");
				}	
				RequestContext.getCurrentInstance().execute("dialogFlorestal.hide()");
				this.abaFlorestalController.adicionarOuAtualizarAtoFlorestalNaLista(this.florestal);
			} catch (Exception e) {
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}

	private void limparAreas() {
		numAreaPergunta121 = null;
		numAreaPergunta132 = null;
		numAreaPergunta142 = null;
		numAreaPergunta151 = null;
		numAreaPergunta162 = null;
		numAreaPergunta181 = null;
		numAreaPergunta192 = null;
	}

	private void salvarFlorestalAtoAmbiental() {
		for (AtoAmbiental atoAmbiental : this.atosAmbientais) {
			Exception erro = null;
			
			try {
				if(!atoAmbiental.isRowSelect()){
					continue;
				}
				FlorestalAtoAmbiental florestalAto = new FlorestalAtoAmbiental();
				florestalAto.setFlorestalAtoAmbientalPK(new FlorestalAtoAmbientalPK(atoAmbiental.getIdeAtoAmbiental(), this.florestal.getIdeFlorestal()));
				florestalAto.setIdeAtoAmbiental(atoAmbiental);
				florestalAto.setIdeFlorestal(this.florestal);
				florestalAto.setNumPortaria(atoAmbiental.getNumPortariaRVFR());
				this.florestalService.salvarAtualizarFlorestalRVFR(florestalAto);
			} catch (Exception e) {
				erro = e;
				JsfUtil.addErrorMessage("Erro ao salvar a lista de RVFR:" + e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
	}

	private void salvarCaracteristicasFlorestal() {
		if(!Util.isNullOuVazio(this.florestal.getCaracteristicasFlorestaProducao()))
			for (CaracteristicaFlorestaProducao caracteristica : this.florestal.getCaracteristicasFlorestaProducao()) {
				
				Exception erro = null;
				
				try {
					FlorestalCaracteristicaFlorestaProducao florestalCaracteristicaFlorestaProducao = new FlorestalCaracteristicaFlorestaProducao(
							new FlorestalCaracteristicaFlorestaProducaoPK(caracteristica.getIdeCaracteristicaFlorestaProducao(),this.florestal.getIdeFlorestal()));
					florestalCaracteristicaFlorestaProducao.setIdeFlorestal(this.florestal);
					florestalCaracteristicaFlorestaProducao.setIdeCaracteristicaFlorestaProducao(caracteristica);
					this.florestalService.salvarFlorestaProducao(florestalCaracteristicaFlorestaProducao);
				} catch (Exception e) {
					erro = e;
					JsfUtil.addErrorMessage("Erro ao salvar a lista de Florestal Produção: " + e.getMessage());
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
			}
	}
	
	public boolean isTheGeomPersistido(LocalizacaoGeografica localizacaoGeografica) {
		try {
			return !Util.isNullOuVazio(locGeoFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da localização geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean validar(){
		boolean valido = true;
		if (Util.isNullOuVazio(this.florestal.getIdeImovel())) {
			JsfUtil.addWarnMessage("Por favor, informe um imóvel.");
			valido = false;
		}
		Boolean indCessionario = this.novoRequerimentoController.getEmpreendimento().getIndCessionario();
		if (!indCessionario) {
				if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P111.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.1.1.");
					valido = false;
				} else if (this.perguntaNR_A5_DFLO_P111.getIndResposta()) {
					if (Util.isNullOuVazio(this.florestal.getIdeTipoReservaLegal())) {
						JsfUtil.addWarnMessage("Por favor, selecione pelo menos uma das opções do campo 1.1.1.1.");
						valido = false;
					}
					if (Util.isNullOuVazio(this.florestal.getNumAreaReservaLegal()) || (this.florestal.getNumAreaReservaLegal().compareTo(BigDecimal.ZERO) == 0)) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.1.2.");
						valido = false;
					}
			}

			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P12.getIndResposta())&& !indCessionario) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.2.");
				valido = false;
			} else if (this.perguntaNR_A5_DFLO_P12.getIndResposta()) {
				if (Util.isNullOuVazio(this.florestal.getNumAreaServidaoFlorestal())|| (this.florestal.getNumAreaServidaoFlorestal().compareTo(BigDecimal.ZERO) == 0)) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.1.");
					valido = false;
				}
				if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P12.getIdeLocalizacaoGeografica())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.2.");
					valido = false;
				} else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P12.getIdeLocalizacaoGeografica())){
					JsfUtil.addWarnMessage("A localização geográfica do campo 1.2.2 deve ser preenchida corretamente.");
					valido = false;
				}
			}

			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P13.getIndResposta()) && !indCessionario) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.3.");
				valido = false;
			} else if (this.perguntaNR_A5_DFLO_P13.getIndResposta()) {
				if (Util.isNullOuVazio(this.florestal.getNumProcessoAprovacaoServidaoFlorestal())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.1.");
					valido = false;
				} else {
					// VALIDAR PROCESSO
				}
				if (Util.isNullOuVazio(this.florestal.getNumAreaProcessoAprovacaoServidaoFlorestal())
						|| (this.florestal.getNumAreaProcessoAprovacaoServidaoFlorestal().compareTo(BigDecimal.ZERO) == 0)) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.2.");
					valido = false;
				}
				if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P13.getIdeLocalizacaoGeografica())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.3.");
					valido = false;
				} else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P13.getIdeLocalizacaoGeografica())){
					JsfUtil.addWarnMessage("A localização geográfica do campo 1.3.3 deve ser preenchida corretamente.");
					valido = false;
				}
			}

			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P14.getIndResposta())) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.4.");
				valido = false;
				
			} else if (this.perguntaNR_A5_DFLO_P14.getIndResposta()) {
				if (Util.isNullOuVazio(florestal.getNumAreaProcessoAprovacaoReservaLegal())) {
					if(isAreaValidaPergunta142()) florestal.setNumAreaProcessoAprovacaoReservaLegal(numAreaPergunta142);
					
					if (Util.isNullOuVazio(florestal.getNumAreaProcessoAprovacaoReservaLegal())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.4.2.");
						valido = false;
					}
				}
			}
		}
		
		if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P15.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.5.");
			valido = false;
			
		} else if (this.perguntaNR_A5_DFLO_P15.getIndResposta()) {
			
			if (Util.isNullOuVazio(this.florestal.getNumAreaSuprimida()) || (this.florestal.getNumAreaSuprimida().compareTo(BigDecimal.ZERO) == 0)) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.5.1.");
				valido = false;
			}
			
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P15.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.5.2.");
				valido = false;
				
			} else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P15.getIdeLocalizacaoGeografica())){
				JsfUtil.addWarnMessage("A localização geográfica do campo 1.5.2 deve ser preenchida corretamente.");
				valido = false;
			}
			
			valido = validarResponsavelTecnicoASV(valido);
		}

		if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P16.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.6.");
			valido = false;
		} else if (this.perguntaNR_A5_DFLO_P16.getIndResposta()) {
			if (Util.isNullOuVazio(this.florestal.getNumAreaQueimada())
					|| (this.florestal.getNumAreaQueimada().compareTo(BigDecimal.ZERO) == 0)) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.6.1.");
				valido = false;
			}
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P16.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.6.2.");
				valido = false;
			} else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P16.getIdeLocalizacaoGeografica())){
				JsfUtil.addWarnMessage("A localização geográfica do campo 1.6.2 deve ser preenchida corretamente.");
				valido = false;
			}
			
		}

		if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P17.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.7.");
			valido = false;
		}else if(this.perguntaNR_A5_DFLO_P17.getIndResposta()){
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P17.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.7.1.");
				valido = false;
			}
		}

		if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P18.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.8.");
			valido = false;
		} else if (this.perguntaNR_A5_DFLO_P18.getIndResposta()) {
			if (Util.isNullOuVazio(this.florestal.getNumAreaPlanoManejoFlorestalSustentavel())
					|| (this.florestal.getNumAreaPlanoManejoFlorestalSustentavel().compareTo(BigDecimal.ZERO) == 0)) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.8.1.");
				valido = false;
			}
			
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P18.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.8.2.");
				valido = false;
			} else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P18.getIdeLocalizacaoGeografica())){
				JsfUtil.addWarnMessage("A localização geográfica do campo 1.8.2 deve ser preenchida corretamente.");
				valido = false;
			}
			
		}

		if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P19.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.9.");
			valido = false;
		} else if (this.perguntaNR_A5_DFLO_P19.getIndResposta()) {
			if (!Util.isNullOuVazio(this.florestal.getNumProcessoAprovacaoPlanoManejoFlorestalSustentavel())) {
				// VALIDAR PROCESSO
			}

			if (Util.isNullOuVazio(this.florestal.getNumAreaCorteUnidadeProducao())
					|| (this.florestal.getNumAreaCorteUnidadeProducao().compareTo(BigDecimal.ZERO) == 0)) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.9.2.");
				valido = false;
			}
			
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P19.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.9.3.");
				valido = false;
			} else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P19.getIdeLocalizacaoGeografica())){
				JsfUtil.addWarnMessage("A localização geográfica do campo 1.9.3 deve ser preenchida corretamente.");
				valido = false;
			}
			
		}
		
		if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p10.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.");
			valido = false;
		} else if (this.perguntaNR_A5_DFLO_P1p10.getIndResposta()) {
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p101.getIndResposta())) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.1.");
				valido = false;
			} else if (this.perguntaNR_A5_DFLO_P1p101.getIndResposta()) {
				if (Util.isNullOuVazio(this.florestal.getNumProcessoLicenciamentoEstado())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.10.1.1.");
					valido = false;
				} else {
					// VALIDAR PROCESSO
				}
				
				if (Util.isNullOuVazio(this.florestal.getNumPortariaEstado())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.10.1.2.");
					valido = false;
				} else {
					if (!Util.validaTamanhoString(this.florestal.getNumPortariaEstado(), 50)) {
						JsfUtil.addWarnMessage("O campo 1.10.1.2. só aceita 50 caracteres");
						valido = false;
					}
				}
				
				if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p113.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.1.3.");
					valido = false;
				} else if (this.perguntaNR_A5_DFLO_P1p113.getIndResposta()) {
					if (Util.isNullOuVazio(this.florestal.getNumProcessoLicenciamentoMunicipio())
							&& Util.isNullOuVazio(this.florestal.getNumPortariaMunicipio())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.10.1.3.1. ou o campo 1.10.1.3.2.");
						valido = false;
					}
					if (!Util.isNullOuVazio(this.florestal.getNumProcessoLicenciamentoMunicipio())) {
						// Validar Processo
					}
					if (!Util.isNullOuVazio(this.florestal.getNumPortariaMunicipio())) {
						if (!Util.validaTamanhoString(this.florestal.getNumPortariaMunicipio(), 50)) {
							JsfUtil.addWarnMessage("O campo 1.10.1.3.2. só aceita 50 caracteres");
							valido = false;
						}
					}
				}
			}
			
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p12.getIndResposta())) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.2.");
				valido = false;
			} else if (this.perguntaNR_A5_DFLO_P1p12.getIndResposta()) {
				if (Util.isNullOuVazio(this.florestal.getNumProcessoRegistroFlorestaPlantada())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.10.2.");
					valido = false;
				} else {
					// VALIDAR PROCESSO
				}
			}
			
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p13.getIndResposta())) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.3.");
				valido = false;
			} else if (this.perguntaNR_A5_DFLO_P1p13.getIndResposta()) {
				if (Util.isNullOuVazio(this.florestal.getCaracteristicasFlorestaProducao())) {
					JsfUtil.addWarnMessage("Por favor, selecione pelo menos um item do campo 1.10.3.1.");
					valido = false;
				}
				if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p13.getIdeLocalizacaoGeografica())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.10.3.2.");
					valido = false;
				} else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P1p13.getIdeLocalizacaoGeografica())){
					JsfUtil.addWarnMessage("A localização geográfica do campo 1.10.3.2 deve ser preenchida corretamente.");
					valido = false;
				}
				
			}
			
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p14.getIndResposta()) && this.sistemaDeReposicaoFlorestalImplantado) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.4.");
				valido = false;
			}
			
			if (!Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p11.getIndResposta()) && this.perguntaNR_A5_DFLO_P1p11.getIndResposta()
					&& this.sistemaDeReposicaoFlorestalImplantado) {
				if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p15.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.5.");
					valido = false;
				} else if (this.perguntaNR_A5_DFLO_P1p15.getIndResposta()) {
					if (Util.isNullOuVazio(this.florestal.getNumProcessoEmissaoCreditoReposicaoFlorestal())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.10.5.1.");
						valido = false;
					} else {
						// VALIDAR PROCESSO
					}
					if (Util.isNullOuVazio(this.florestal.getNumPortariaEmissaoCreditoReposicaoFlorestal())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.10.5.2.");
						valido = false;
					} else {
						if (!Util.validaTamanhoString(this.florestal.getNumPortariaEmissaoCreditoReposicaoFlorestal(), 50)) {
							JsfUtil.addWarnMessage("O campo 1.10.5.2. só aceita 50 caracteres");
							valido = false;
						}
					}
				}
			}
			
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p16.getIndResposta())) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.10.6.");
				valido = false;
			} else if (this.perguntaNR_A5_DFLO_P1p16.getIndResposta()) {
				boolean existeAtoMarcado = false;
				for (AtoAmbiental ambiental : this.atosAmbientais) {
					
					if(ambiental.isRowSelect())
						existeAtoMarcado = true;
					
					if ((ambiental.isRowSelect()) && Util.isNullOuVazio(ambiental.getNumPortariaRVFR())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo do item selecionado no campo 1.10.6.1.");
						valido = false;
					}
					if (ambiental.isRowSelect() && !Util.isNullOuVazio(ambiental.getNumPortariaRVFR())) {
						if (!Util.validaTamanhoString(ambiental.getNumPortariaRVFR(), 50)) {
							JsfUtil.addWarnMessage("O campo 1.10.6.1. só aceita 50 caracteres");
							valido = false;
						}
					}
				}
				
				if(!existeAtoMarcado){
					JsfUtil.addWarnMessage("Por favor, selecione pelo menos um item do campo 1.10.6.1.");
					valido = false;
				}
			}
			
		}
		
		if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p11.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.11.");
			valido = false;
		}else if(this.perguntaNR_A5_DFLO_P1p11.getIndResposta()){
			if (Util.isNullOuVazio(this.perguntaNR_A5_DFLO_P1p11.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.11.1");
				valido = false;
			}else if(!isTheGeomPersistido(this.perguntaNR_A5_DFLO_P1p11.getIdeLocalizacaoGeografica())){
				JsfUtil.addWarnMessage("A localização geográfica do campo 1.11.1 deve ser preenchida corretamente.");
				valido = false;
			}else{
				try {
					Double area = locGeoFacade.retornarAreaShape(this.perguntaNR_A5_DFLO_P1p11.getIdeLocalizacaoGeografica());
					this.florestal.setNumAreaManejoCabruca( new BigDecimal(area));
					valido = validarAreaCefir(area, this.florestal.getIdeImovel(),this.perguntaNR_A5_DFLO_P1p11.getIdeLocalizacaoGeografica());
				} catch (Exception e) {
					
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}	
		}
		return valido;
	}
	
	private boolean validarResponsavelTecnicoASV(boolean valido) {
		//#10500
		try {
			if(!Util.isNullOuVazio(novoRequerimentoController.getEmpreendimento())){
				Collection<ResponsavelEmpreendimento> listaRE = 
						responsavelEmpreendimentoService.listarResponsaveisPorEmpreendimento(novoRequerimentoController.getEmpreendimento());
				
				if(Util.isNullOuVazio(listaRE)) {
					MensagemUtil.erro("Prezado(a) Requerente, favor informar o responsável técnico no Cadastro do Empreendimento.");
					return false;
					
				} else {
					for (ResponsavelEmpreendimento re : listaRE) {
						if(Util.isNullOuVazio(re.getNumART()) || Util.isNullOuVazio(re.getDscCaminhoArquivoART())) {
							MensagemUtil.erro("Prezado(a) requerente, favor informar os dados da ART do responsável técnico no Cadastro do Empreendimento.");
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return true;
	}
	
	private boolean validarAreaCefir(Double area, Imovel imovel,LocalizacaoGeografica localizacaoGeografica) {
		Boolean retorno = Boolean.FALSE;
		Boolean areaMenor = Boolean.FALSE;
		Boolean existeCacau = Boolean.FALSE;
		try {
			Collection<AreaProdutiva> listAreaProdutiva;
			listAreaProdutiva = areaProdutivaService.obterAreaSequeiroCacauSefir(imovel);
			
			for (AreaProdutiva areaProdutiva : listAreaProdutiva) {
				
				if (areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia() == TipologiaCefirEnum.AGRICULTURA_DE_SEQUEIRO.getId()) {
					Collection<AreaProdutivaTipologiaAtividade> areaPTA;
					areaPTA =	areaProdutivaService.listarAptaByAreaProdutiva(areaProdutiva);
					for (AreaProdutivaTipologiaAtividade areaProdutivaTipologiaAtividade : areaPTA) {
						if(areaProdutivaTipologiaAtividade.getIdeTipologiaAtividade().getDscTipologiaAtividade().contains("Cacau")){
							existeCacau = Boolean.TRUE;
							if(area <= areaProdutiva.getValArea()){
								areaMenor = Boolean.TRUE;
								retorno = Boolean.TRUE;
								break;
							}
						}
					}
				 }
			}
			if(!existeCacau){
			//Colocar mensagem no bundle.properties
			JsfUtil.addWarnMessage("Não foi identificada a atividade Produção de Cacau no CEFIR. "
					+ " Atualize seu cadastro para prosseguir com o requerimento");
			}else if(!areaMenor){
				//Colocar mensagem no bundle.properties
				JsfUtil.addWarnMessage("Poligonal persistida na pergunta 1.11.1 tem área maior do que o shape da atividade "
						+ "'Produção de Cacau' da agricultura de sequeiro no CEFIR");
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return retorno;
	}

	/**
	 * Habilita a insercao de ponto em ASV quando o requerente tiver selecionado os programas do governo listados
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean habilitaPontoASVProgramaGoverno() throws Exception {
		
		List<Integer> listaProgramasGoverno = new ArrayList<Integer>();
		listaProgramasGoverno.add(ProgramaGovernoEnum.LUZ.getId());
		listaProgramasGoverno.add(ProgramaGovernoEnum.UNIVERSAL.getId());
		listaProgramasGoverno.add(ProgramaGovernoEnum.ANEEL.getId());
		
		if(!Util.isNull(this.novoRequerimentoController.getRequerimentoSelecionado()) 
				&& !Util.isNull(this.novoRequerimentoController.getRequerimentoSelecionado().getIdeRequerimento())) {
			
			empreendimentoRequerimento = 
					requerimentoService.carregarEmpreendimentoRequerimento(this.novoRequerimentoController.getRequerimentoSelecionado().getIdeRequerimento());
			
			if (Util.isNull(empreendimentoRequerimento.getProgramaGoverno())) {
				empreendimentoRequerimento.setProgramaGoverno(this.novoRequerimentoController.getEmpreendimentoRequerimento().getProgramaGoverno());
			}
			
			if(!Util.isNull(empreendimentoRequerimento) && !Util.isNull(empreendimentoRequerimento.getProgramaGoverno()) 
					&& !Util.isNull(empreendimentoRequerimento.getProgramaGoverno().getId()) 
					&& listaProgramasGoverno.contains(empreendimentoRequerimento.getProgramaGoverno().getId())) {
				
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void verificarEmpreendimentoBloqueioDQC() throws Exception {
		Empreendimento ideEmpreendimento = empreendimentoRequerimento.getIdeEmpreendimento();
		isBloquearDQC = enderecoEmpreendimentoService.verificarBloqueioDQC(ideEmpreendimento.getIdeEmpreendimento());
		if(isBloquearDQC && !novoRequerimentoController.isDesabilitarTudo()) {
			perguntaNR_A5_DFLO_P16.setIndResposta(false);
		}
	}
	
	public void verificarImovelBloqueioDQC() {
		try {
			if(!isBloquearDQC) {
				isBloquearDQC = imovelService.verificarBloqueioDQC(florestal.getIdeImovel().getIdeImovel());
				if(isBloquearDQC && !novoRequerimentoController.isDesabilitarTudo()) {
					perguntaNR_A5_DFLO_P16.setIndResposta(false);
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isDisabledPerguntaNR_A5_DFLO_P16(){
		if(isBloquearDQC) {
			return true;
		}
		return novoRequerimentoController.isDesabilitarTudo();
	}
	
	public void onChangePerguntaArea121(){
		if(isAreaValidaPergunta121()){
			florestal.setNumAreaServidaoFlorestal(numAreaPergunta121);
		} 
		else {
			numAreaPergunta121 = florestal.getNumAreaServidaoFlorestal();
			JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formDialogFlorestal:formDialogFloresal121");
		}
	}

	private boolean isAreaValidaPergunta121(){
		if(isAreaServidaoFlorestalNotNull() && !Util.isNullOuVazio(perguntaNR_A5_DFLO_P12.getIdeLocalizacaoGeografica())){
			return isAreaValida(numAreaPergunta121, perguntaNR_A5_DFLO_P12.getIdeLocalizacaoGeografica());
		} 
		return true;
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 09/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7479">#7479</a>
	 */
	public boolean isAreaServidaoFlorestalNotNull() {
		return !Util.isNullOuVazio(numAreaPergunta121);
	}

	public void onChangePerguntaArea132(){
		if(isAreaValidaPergunta132()){
			florestal.setNumAreaProcessoAprovacaoServidaoFlorestal(numAreaPergunta132);
		} 
		else {
			numAreaPergunta132 = florestal.getNumAreaProcessoAprovacaoServidaoFlorestal();
			JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formDialogFlorestal:formDialogFloresal131");
		}
	}

	private boolean isAreaValidaPergunta132(){
		if(isAreaProcessoAprovacao132NotNull() && !Util.isNullOuVazio(perguntaNR_A5_DFLO_P13.getIdeLocalizacaoGeografica())){
			return isAreaValida(numAreaPergunta132, perguntaNR_A5_DFLO_P13.getIdeLocalizacaoGeografica());
		} 
		return true;
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 09/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7479">#7479</a>
	 */
	public boolean isAreaProcessoAprovacao132NotNull() {
		return !Util.isNullOuVazio(numAreaPergunta132);
	}
	
	public void onChangePerguntaArea142(){
		if(isAreaValidaPergunta142()){
			florestal.setNumAreaProcessoAprovacaoReservaLegal(numAreaPergunta142);
		} else {
			numAreaPergunta142 = florestal.getNumAreaProcessoAprovacaoReservaLegal();
			JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formDialogFlorestal:formDialogFloresal141");
		}
	}
	
	private boolean isAreaValidaPergunta142(){
		if(isAreaProcessoAprovacao142NotNull() && !Util.isNullOuVazio(perguntaNR_A5_DFLO_P14.getIdeLocalizacaoGeografica())){
			return isAreaValida(numAreaPergunta142, perguntaNR_A5_DFLO_P14.getIdeLocalizacaoGeografica());
		}
		return true;
	}
	
	public boolean isAreaProcessoAprovacao142NotNull() {
		return !Util.isNullOuVazio(numAreaPergunta142);
	}

	public void onChangePerguntaArea151(){
		if(isAreaValidaPergunta151()){
			florestal.setNumAreaSuprimida(numAreaPergunta151);
		} 
		else {
			numAreaPergunta151 = florestal.getNumAreaSuprimida();
			JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formDialogFlorestal:formDialogFlorestal152");
		}
	}

	private boolean isAreaValidaPergunta151(){
		if(isAreaSuprimidaNotNull() && !Util.isNullOuVazio(perguntaNR_A5_DFLO_P15.getIdeLocalizacaoGeografica())){
			return isAreaValida(numAreaPergunta151, perguntaNR_A5_DFLO_P15.getIdeLocalizacaoGeografica());
		} 
		return true;
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 09/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7479">#7479</a>
	 */
	public boolean isAreaSuprimidaNotNull() {
		return !Util.isNullOuVazio(numAreaPergunta151);
	}

	public void onChangePerguntaArea162(){
		if(isAreaValidaPergunta162()){
			florestal.setNumAreaQueimada(numAreaPergunta162);
		} 
		else {
			numAreaPergunta162 = florestal.getNumAreaQueimada();
			JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formDialogFlorestal:formDialogFloresal161");
		}
	}

	private boolean isAreaValidaPergunta162(){
		if(isAreaQueimadaNotNull() && !Util.isNullOuVazio(perguntaNR_A5_DFLO_P16.getIdeLocalizacaoGeografica()) 
				&& perguntaNR_A5_DFLO_P16.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao() == ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue()){
			return isAreaValida(numAreaPergunta162, perguntaNR_A5_DFLO_P16.getIdeLocalizacaoGeografica());
		} 
		return true;
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 09/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7479">#7479</a> 
	 */
	public boolean isAreaQueimadaNotNull() {
		return !Util.isNullOuVazio(numAreaPergunta162);
	}

	public void onChangePerguntaArea181(){
		if(isAreaValidaPergunta181()){
			florestal.setNumAreaPlanoManejoFlorestalSustentavel(numAreaPergunta181);
		} 
		else {
			numAreaPergunta181 = florestal.getNumAreaPlanoManejoFlorestalSustentavel();
			JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formDialogFlorestal:formDialogFloresal181");
		}
	}

	private boolean isAreaValidaPergunta181(){
		if(isAreaPlanoManejoNotNull() && !Util.isNullOuVazio(perguntaNR_A5_DFLO_P18.getIdeLocalizacaoGeografica())){
			return isAreaValida(numAreaPergunta181, perguntaNR_A5_DFLO_P18.getIdeLocalizacaoGeografica());
		} 
		return true;
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 09/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7479">#7479</a>
	 */
	public boolean isAreaPlanoManejoNotNull() {
		return !Util.isNullOuVazio(numAreaPergunta181);
	}

	public void onChangePerguntaArea192(){
		if(isAreaValidaPergunta192()){
			florestal.setNumAreaCorteUnidadeProducao(numAreaPergunta192);
		} 
		else {
			numAreaPergunta192 = florestal.getNumAreaCorteUnidadeProducao();
			JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formDialogFlorestal:formDialogFloresal191");
		}
	}

	private boolean isAreaValidaPergunta192(){
		if(isAreaCorteProducaoNotNull() && !Util.isNullOuVazio(perguntaNR_A5_DFLO_P19.getIdeLocalizacaoGeografica())){
			return isAreaValida(numAreaPergunta192, perguntaNR_A5_DFLO_P19.getIdeLocalizacaoGeografica());
		} 
		return true;
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 09/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7479">#7479</a>
	 */
	public boolean isAreaCorteProducaoNotNull() {
		return !Util.isNullOuVazio(numAreaPergunta192);
	}

	private boolean isAreaValida(BigDecimal area, LocalizacaoGeografica shape){
		try {
			return locGeoFacade.isAreaInformadaDeMesmoTamanhoDoShapeInserido(area, shape);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Shapefile.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Florestal getFlorestal() {
		return florestal;
	}

	public void setFlorestal(Florestal florestal) {
		this.florestal = florestal;
	}

	public List<PerguntaRequerimento> getListaPerguntasRequerimento() {
		return listaPerguntasRequerimento;
	}

	public void setListaPerguntasRequerimento(List<PerguntaRequerimento> listaPerguntasRequerimento) {
		this.listaPerguntasRequerimento = listaPerguntasRequerimento;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P11() {
		return perguntaNR_A5_DFLO_P11;
	}

	public void setPerguntaNR_A5_DFLO_P11(PerguntaRequerimento perguntaNR_A5_DFLO_P11) {
		this.perguntaNR_A5_DFLO_P11 = perguntaNR_A5_DFLO_P11;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P111() {
		return perguntaNR_A5_DFLO_P111;
	}

	public void setPerguntaNR_A5_DFLO_P111(PerguntaRequerimento perguntaNR_A5_DFLO_P111) {
		this.perguntaNR_A5_DFLO_P111 = perguntaNR_A5_DFLO_P111;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P12() {
		return perguntaNR_A5_DFLO_P12;
	}

	public void setPerguntaNR_A5_DFLO_P12(PerguntaRequerimento perguntaNR_A5_DFLO_P12) {
		this.perguntaNR_A5_DFLO_P12 = perguntaNR_A5_DFLO_P12;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P13() {
		return perguntaNR_A5_DFLO_P13;
	}

	public void setPerguntaNR_A5_DFLO_P13(PerguntaRequerimento perguntaNR_A5_DFLO_P13) {
		this.perguntaNR_A5_DFLO_P13 = perguntaNR_A5_DFLO_P13;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P14() {
		return perguntaNR_A5_DFLO_P14;
	}

	public void setPerguntaNR_A5_DFLO_P14(PerguntaRequerimento perguntaNR_A5_DFLO_P14) {
		this.perguntaNR_A5_DFLO_P14 = perguntaNR_A5_DFLO_P14;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P15() {
		return perguntaNR_A5_DFLO_P15;
	}

	public void setPerguntaNR_A5_DFLO_P15(PerguntaRequerimento perguntaNR_A5_DFLO_P15) {
		this.perguntaNR_A5_DFLO_P15 = perguntaNR_A5_DFLO_P15;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P16() {
		return perguntaNR_A5_DFLO_P16;
	}

	public void setPerguntaNR_A5_DFLO_P16(PerguntaRequerimento perguntaNR_A5_DFLO_P16) {
		this.perguntaNR_A5_DFLO_P16 = perguntaNR_A5_DFLO_P16;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P17() {
		return perguntaNR_A5_DFLO_P17;
	}

	public void setPerguntaNR_A5_DFLO_P17(PerguntaRequerimento perguntaNR_A5_DFLO_P17) {
		this.perguntaNR_A5_DFLO_P17 = perguntaNR_A5_DFLO_P17;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P18() {
		return perguntaNR_A5_DFLO_P18;
	}

	public void setPerguntaNR_A5_DFLO_P18(PerguntaRequerimento perguntaNR_A5_DFLO_P18) {
		this.perguntaNR_A5_DFLO_P18 = perguntaNR_A5_DFLO_P18;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P19() {
		return perguntaNR_A5_DFLO_P19;
	}

	public void setPerguntaNR_A5_DFLO_P19(PerguntaRequerimento perguntaNR_A5_DFLO_P19) {
		this.perguntaNR_A5_DFLO_P19 = perguntaNR_A5_DFLO_P19;
	}
	
	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p10() {
		return perguntaNR_A5_DFLO_P1p10;
	}

	public void setPerguntaNR_A5_DFLO_P1p10(PerguntaRequerimento perguntaNR_A5_DFLO_P1p10) {
		this.perguntaNR_A5_DFLO_P1p10 = perguntaNR_A5_DFLO_P1p10;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p11() {
		return perguntaNR_A5_DFLO_P1p11;
	}

	public void setPerguntaNR_A5_DFLO_P1p11(PerguntaRequerimento perguntaNR_A5_DFLO_P1p11) {
		this.perguntaNR_A5_DFLO_P1p11 = perguntaNR_A5_DFLO_P1p11;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p113() {
		return perguntaNR_A5_DFLO_P1p113;
	}

	public void setPerguntaNR_A5_DFLO_P1p113(PerguntaRequerimento perguntaNR_A5_DFLO_P1p113) {
		this.perguntaNR_A5_DFLO_P1p113 = perguntaNR_A5_DFLO_P1p113;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p12() {
		return perguntaNR_A5_DFLO_P1p12;
	}

	public void setPerguntaNR_A5_DFLO_P1p12(PerguntaRequerimento perguntaNR_A5_DFLO_P1p12) {
		this.perguntaNR_A5_DFLO_P1p12 = perguntaNR_A5_DFLO_P1p12;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p13() {
		return perguntaNR_A5_DFLO_P1p13;
	}

	public void setPerguntaNR_A5_DFLO_P1p13(PerguntaRequerimento perguntaNR_A5_DFLO_P1p13) {
		this.perguntaNR_A5_DFLO_P1p13 = perguntaNR_A5_DFLO_P1p13;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p14() {
		return perguntaNR_A5_DFLO_P1p14;
	}

	public void setPerguntaNR_A5_DFLO_P1p14(PerguntaRequerimento perguntaNR_A5_DFLO_P1p14) {
		this.perguntaNR_A5_DFLO_P1p14 = perguntaNR_A5_DFLO_P1p14;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p15() {
		return perguntaNR_A5_DFLO_P1p15;
	}

	public void setPerguntaNR_A5_DFLO_P1p15(PerguntaRequerimento perguntaNR_A5_DFLO_P1p15) {
		this.perguntaNR_A5_DFLO_P1p15 = perguntaNR_A5_DFLO_P1p15;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p16() {
		return perguntaNR_A5_DFLO_P1p16;
	}

	public void setPerguntaNR_A5_DFLO_P1p16(PerguntaRequerimento perguntaNR_A5_DFLO_P1p16) {
		this.perguntaNR_A5_DFLO_P1p16 = perguntaNR_A5_DFLO_P1p16;
	}

	public PerguntaRequerimento getPerguntaNR_A5_DFLO_P1p101() {
		return perguntaNR_A5_DFLO_P1p101;
	}

	public void setPerguntaNR_A5_DFLO_P1p101(
			PerguntaRequerimento perguntaNR_A5_DFLO_P1p101) {
		this.perguntaNR_A5_DFLO_P1p101 = perguntaNR_A5_DFLO_P1p101;
	}

	public Collection<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;
	}

	public void setAtosAmbientais(Collection<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}

	public Collection<TipoReservaLegal> getTiposReservaLegal() {
		return tiposReservaLegal;
	}

	public void setTiposReservaLegal(Collection<TipoReservaLegal> tiposReservaLegal) {
		this.tiposReservaLegal = tiposReservaLegal;
	}

	public Collection<CaracteristicaFlorestaProducao> getCaracteristicasFlorestaProducao() {
		return caracteristicasFlorestaProducao;
	}

	public void setCaracteristicasFlorestaProducao(
			Collection<CaracteristicaFlorestaProducao> caracteristicasFlorestaProducao) {
		this.caracteristicasFlorestaProducao = caracteristicasFlorestaProducao;
	}

	public Collection<Imovel> getImoveis() {
		return imoveis;
	}

	public void setImoveis(Collection<Imovel> imoveis) {
		this.imoveis = imoveis;
	}

	public NovoRequerimentoController getNovoRequerimentoController() {
		return novoRequerimentoController;
	}

	public void setNovoRequerimentoController(NovoRequerimentoController novoRequerimentoController) {
		this.novoRequerimentoController = novoRequerimentoController;
	}

	public boolean isSistemaDeReposicaoFlorestalImplantado() {
		return sistemaDeReposicaoFlorestalImplantado;
	}

	public void setSistemaDeReposicaoFlorestalImplantado(boolean sistemaDeReposicaoFlorestalImplantado) {
		this.sistemaDeReposicaoFlorestalImplantado = sistemaDeReposicaoFlorestalImplantado;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public CaracteristicaFlorestaProducao getCaracteristicaFlorestaProducaoNenhumaDasAnteriores() {
		return caracteristicaFlorestaProducaoNenhumaDasAnteriores;
	}

	public void setCaracteristicaFlorestaProducaoNenhumaDasAnteriores(
			CaracteristicaFlorestaProducao caracteristicaFlorestaProducaoNenhumaDasAnteriores) {
		this.caracteristicaFlorestaProducaoNenhumaDasAnteriores = caracteristicaFlorestaProducaoNenhumaDasAnteriores;
	}
	
	public boolean isProgramaGovernoASV() {
		return programaGovernoASV;
	}

	public void setProgramaGovernoASV(boolean programaGovernoASV) {
		this.programaGovernoASV = programaGovernoASV;
	}
	
	public int getClassificacaoShape(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}
	
	public int getClassificacaoPontoOuShape(){
		return ClassificacaoSecaoEnum.COM_PONTO_OU_SHAPEFILE.getId().intValue();
	}

	public BigDecimal getNumAreaPergunta151() {
		if(Util.isNull(numAreaPergunta151) && !Util.isNullOuVazio(florestal.getNumAreaSuprimida())){
			numAreaPergunta151 = florestal.getNumAreaSuprimida();
		}
		return numAreaPergunta151;
	}

	public void setNumAreaPergunta151(BigDecimal numAreaPergunta151) {
		this.numAreaPergunta151 = numAreaPergunta151;
	}

	public BigDecimal getNumAreaPergunta121() {
		if(Util.isNull(numAreaPergunta121) && !Util.isNullOuVazio(florestal.getNumAreaServidaoFlorestal())){
			numAreaPergunta121 = florestal.getNumAreaServidaoFlorestal();
		}
		return numAreaPergunta121;
	}

	public void setNumAreaPergunta121(BigDecimal numAreaPergunta121) {
		this.numAreaPergunta121 = numAreaPergunta121;
	}

	public BigDecimal getNumAreaPergunta132() {
		if(Util.isNull(numAreaPergunta132) && !Util.isNullOuVazio(florestal.getNumAreaProcessoAprovacaoServidaoFlorestal())){
			numAreaPergunta132 = florestal.getNumAreaProcessoAprovacaoServidaoFlorestal();
		}
		return numAreaPergunta132;
	}

	public void setNumAreaPergunta132(BigDecimal numAreaPergunta132) {
		this.numAreaPergunta132 = numAreaPergunta132;
	}
	
	public BigDecimal getNumAreaPergunta142() {
		if(Util.isNull(numAreaPergunta142) && !Util.isNullOuVazio(florestal.getNumAreaProcessoAprovacaoReservaLegal())){
			numAreaPergunta142 = florestal.getNumAreaProcessoAprovacaoReservaLegal();
		}
		return numAreaPergunta142;
	}
	
	public void setNumAreaPergunta142(BigDecimal numAreaPergunta142) {
		this.numAreaPergunta142 = numAreaPergunta142;
	}

	public BigDecimal getNumAreaPergunta162() {
		if(Util.isNull(numAreaPergunta162) && !Util.isNullOuVazio(florestal.getNumAreaQueimada())){
			numAreaPergunta162 = florestal.getNumAreaQueimada();
		}
		return numAreaPergunta162;
	}

	public void setNumAreaPergunta162(BigDecimal numAreaPergunta161) {
		this.numAreaPergunta162 = numAreaPergunta161;
	}

	public BigDecimal getNumAreaPergunta181() {
		if(Util.isNull(numAreaPergunta181) && !Util.isNullOuVazio(florestal.getNumAreaPlanoManejoFlorestalSustentavel())){
			numAreaPergunta181 = florestal.getNumAreaPlanoManejoFlorestalSustentavel();
		}
		return numAreaPergunta181;
	}

	public void setNumAreaPergunta181(BigDecimal numAreaPergunta181) {
		this.numAreaPergunta181 = numAreaPergunta181;
	}

	public BigDecimal getNumAreaPergunta192() {
		if(Util.isNull(numAreaPergunta192) && !Util.isNullOuVazio(florestal.getNumAreaCorteUnidadeProducao())){
			numAreaPergunta192 = florestal.getNumAreaCorteUnidadeProducao();
		}
		return numAreaPergunta192;
	}

	public void setNumAreaPergunta192(BigDecimal numAreaPergunta192) {
		this.numAreaPergunta192 = numAreaPergunta192;
	}
	
	public Empreendimento getEmpreendimento() {
		return empreendimentoRequerimento.getIdeEmpreendimento();
	}
}
