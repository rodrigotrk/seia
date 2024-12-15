
package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.ProgramaGoverno;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoProcedimentoEspecial;
import br.gov.ba.seia.entity.RequerimentoUnidadeConservacao;
import br.gov.ba.seia.entity.TipoAreaPreservacaoPermanente;
import br.gov.ba.seia.entity.TipoAreaProtegida;
import br.gov.ba.seia.entity.TipoGestao;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.UnidadeConservacao;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoAreaProtegidaEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.RequerimentoProcedimentoEspecialService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("abaLicencaAutorizacaoController")
@ViewScoped
public class AbaLicencaAutorizacaoController extends BaseAbaController{

	@EJB
	private PerguntaService pService;

	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;

	@EJB
	private PerguntaRequerimentoService pRequerimentoService;

	@EJB
	private LicencaService licencaService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private RequerimentoProcedimentoEspecialService requerimentoProcedimentoEspecialService;
	
	private PerguntaRequerimento perguntaNRA3P1;
	private PerguntaRequerimento perguntaNRA3P11;
	private PerguntaRequerimento perguntaNRA3P111;
	private PerguntaRequerimento perguntaNRA3P1111;
	private PerguntaRequerimento perguntaNRA3P11111;
	private PerguntaRequerimento perguntaNRA3P112;
	private PerguntaRequerimento perguntaNRA3P1121;
	private PerguntaRequerimento perguntaNRA3P12;
	private PerguntaRequerimento perguntaNRA3P12N;
	private PerguntaRequerimento perguntaNRA3P13;
	private PerguntaRequerimento perguntaNRA3P14;

	private Licenca licenca;
	private Requerimento requerimento;
	private RequerimentoUnidadeConservacao requerimentoUnidadeConservacao;
	private RequerimentoProcedimentoEspecial requerimentoProcedimentoEspecial;

	private Collection<TipoAreaProtegida> tiposAreaProtegida;
	private Collection<TipoAreaPreservacaoPermanente> tiposAreaPreservacaoPermanente;
	private Collection<UnidadeConservacao> unidadesConservacao;
	private Collection<TipoGestao> tiposGestao;

	private TipoGestao tipoGestao;

	private Boolean indApp;
	private Boolean indUc;
	private Boolean indSomenteTipologiaXouY = false;
	private Boolean indTipologiaEspecial;
	private Boolean hasRequerimentoProcedimentoEspecial = false;
	
	private List<Tipologia> tipologias;
	
	@PostConstruct
	public void init() {
		try {
			this.requerimento = super.getRequerimento();
			super.init();

			this.carregarListas();

			carregarRequerimento();

			carregarTipologiasEmpreendimento();
			
			verificaTipologias();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	private void carregarRequerimento() throws Exception {
		this.licenca = this.licencaService.getLicencaByIdeRequerimento(super.getRequerimento());
		this.requerimentoProcedimentoEspecial = this.requerimentoProcedimentoEspecialService.getRequerimentoProcedimentoEspecialByRequerimento(super.getRequerimento()); 
		this.requerimento = super.getRequerimento();
		this.requerimento.setRequerimentoUnidadeConservacaoCollection(this.licencaService.obterRequerimentoUnidadeConservacaoByRequerimento(this.requerimento));
		this.requerimento.setTipoAreaProtegidaCollection(this.requerimentoService.obterTipoAreaProtegidaByRequerimento(this.requerimento));
		this.requerimento.setTipoAreaPreservacaoPermanenteCollection(this.requerimentoService.obterTipoAreaPreservacaoPermanenteByRequerimento(this.requerimento));
		Requerimento requerimentoOriginal = this.requerimentoService.buscarEntidadePorId(requerimento);
		this.requerimento.setIdeTipoRequerimento(requerimentoOriginal.getIdeTipoRequerimento());
		changeAreaProtegida();
		carregarListaUnidadeConservacaoEspecifica();
		if(Util.isNullOuVazio(this.requerimento.getRequerimentoUnidadeConservacaoCollection())){
			this.requerimentoUnidadeConservacao = new RequerimentoUnidadeConservacao();
		} else{
			for(RequerimentoUnidadeConservacao ruc : this.requerimento.getRequerimentoUnidadeConservacaoCollection()){
				this.requerimentoUnidadeConservacao = ruc;
				if(!Util.isNull(requerimentoUnidadeConservacao.getIdeUnidadeConservacao())){
					this.tipoGestao = requerimentoUnidadeConservacao.getIdeUnidadeConservacao().getIdeTipoGestao();
				}
			}
		}
		if(Util.isNull(licenca)){
			this.licenca = new Licenca(super.getRequerimento());
		} 
		else if(isRespostaPerguntaNRA3P1()){
			this.carregarRequerimentoTipoArea();
		}
		
		if(Util.isNullOuVazio(requerimentoProcedimentoEspecial)){
			this.requerimentoProcedimentoEspecial = new RequerimentoProcedimentoEspecial(super.getRequerimento());
		}else {
			hasRequerimentoProcedimentoEspecial = true;
		}
	}

	@Override
	protected void carregarRespostas() {
		super.carregarRespostasDasPerguntas();
	}

	private boolean isRespostaPerguntaNRA3P1() {
		return Boolean.TRUE.equals(this.perguntaNRA3P1.getIndResposta());
	}

	public void changeListenerPerguntaNRA3P11() {

		perguntaNRA3P111.setIndResposta(null);
		perguntaNRA3P1111.setIndResposta(null);
		perguntaNRA3P11111.setIndResposta(null);
		perguntaNRA3P112.setIndResposta(null);
		perguntaNRA3P1121.setIndResposta(null);

		if(!Util.isNull(perguntaNRA3P11.getIndResposta()) && perguntaNRA3P11.getIndResposta()) {
			perguntaNRA3P1.setIndResposta(false);
			perguntaNRA3P12N.setIndResposta(false);
		}

		if(!Util.isNull(perguntaNRA3P11.getIndResposta()) && !perguntaNRA3P11.getIndResposta()) {
			perguntaNRA3P12.setIndResposta(null);
			novoRequerimentoController.setDisabledAbasRequerimento(false);
			if(!Util.isNull(RequestContext.getCurrentInstance())) {
				RequestContext.getCurrentInstance().addPartialUpdateTarget("subViewAbas:tabAbasId");
			}
		}
		else if(!Util.isNull(perguntaNRA3P11.getIndResposta()) && perguntaNRA3P11.getIndResposta()) {
			PerguntaRequerimento perguntaNRA2P5 = novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P5();
			if(!Util.isNull(perguntaNRA2P5.getIndResposta()) && !perguntaNRA2P5.getIndResposta()) {
				JsfUtil.addWarnMessage("Não será possível fazer a solicitação da autorização do procedimento especial de licenciamento em conjunto com solicitações de alteração, prorrogação e/ou renovação de licença/outorga. Caso seja essa sua necessidade selecione “Nenhuma das opções anteriores” na etapa 2.");
			}
			else {
				novoRequerimentoController.setDisabledAbasRequerimento(true);
				if(!Util.isNull(RequestContext.getCurrentInstance())) {
					RequestContext.getCurrentInstance().addPartialUpdateTarget("subViewAbas:tabAbasId");
				}
			}
			perguntaNRA3P12.setIndResposta(false);
		}

	}

	public void teste() {

		if(!Util.isNull(perguntaNRA3P11.getIndResposta()) && !perguntaNRA3P11.getIndResposta()) {
			novoRequerimentoController.setDisabledAbasRequerimento(false);
			if(!Util.isNull(RequestContext.getCurrentInstance())) {
				RequestContext.getCurrentInstance().addPartialUpdateTarget("subViewAbas:tabAbasId");
			}
		}
		else if(!Util.isNull(perguntaNRA3P11.getIndResposta()) && perguntaNRA3P11.getIndResposta()) {
			PerguntaRequerimento perguntaNRA2P5 = novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P5();
			if(!Util.isNull(perguntaNRA2P5.getIndResposta()) && !perguntaNRA2P5.getIndResposta()) {
			}
			else {
				novoRequerimentoController.setDisabledAbasRequerimento(true);
				if(!Util.isNull(RequestContext.getCurrentInstance())) {
					RequestContext.getCurrentInstance().addPartialUpdateTarget("subViewAbas:tabAbasId");
				}
			}
			perguntaNRA3P12.setIndResposta(false);
		}

	}

	public void changeListenerPerguntaNRA3P111() {
		perguntaNRA3P1111.setIndResposta(null);
	}

	public void changeListenerPerguntaNRA3P1111() {
		perguntaNRA3P11111.setIndResposta(null);
		if(perguntaNRA3P1111.getIndResposta()) {
			Html.exibir("aceite");
			getRequerimentoProcedimentoEspecial().setDataAssinaturaTermoCompromisso(null);
		}
	}

	public void changeListenerPerguntaNRA3P11111() {
		if(!perguntaNRA3P11111.getIndResposta()) {
			JsfUtil.addWarnMessage("Para licenciar o empreendimento deverá ser firmado Termo de compromisso com o INEMA para regularizar questões referentes à supressão de vegetação nativa realizada irregularmente. Para isso, protocole a sua solicitação e proposta de Termo de Compromisso, com base no Art. 291 do Regulamento da Lei Estadual 10.431/2006, aprovado pelo Decreto nº 14.024/2012, em alguma unidade da Central de Atendimento e aguarde o nosso retorno.");
			getRequerimentoProcedimentoEspecial().setDataAssinaturaTermoCompromisso(null);
		}
	}
	
	public void changeListenerPerguntaNRA3P112() {
		perguntaNRA3P1121.setIndResposta(null);
	}

	public void changeListenerPerguntaNRA3P1121() {
		if(!perguntaNRA3P1121.getIndResposta()) {
			JsfUtil.addWarnMessage("A autorização por procedimento especial de licenciamento só poderá ser solicitada após a liberação da dispensa ou outorga de uso de recursos hídricos.");
		}
	}

	protected void carregarPerguntasAba() {
		try {

			perguntaNRA3P1 = super.carregarPerguntaByCod("NR_A3_P1");
			perguntaNRA3P11 = super.carregarPerguntaByCod("NR_A3_P11");
			perguntaNRA3P111 = super.carregarPerguntaByCod("NR_A3_P111");
			perguntaNRA3P1111 = super.carregarPerguntaByCod("NR_A3_P1111");
			perguntaNRA3P11111 = super.carregarPerguntaByCod("NR_A3_P11111");
			perguntaNRA3P112 = super.carregarPerguntaByCod("NR_A3_P112");
			perguntaNRA3P1121 = super.carregarPerguntaByCod("NR_A3_P1121");
			perguntaNRA3P12 = super.carregarPerguntaByCod("NR_A3_P12");
			perguntaNRA3P12N = super.carregarPerguntaByCod("NR_A3_P12_N");
			perguntaNRA3P13 = super.carregarPerguntaByCod("NR_A3_P13");
			perguntaNRA3P14 = super.carregarPerguntaByCod("NR_A3_P14");

			listaPerguntasRequerimento.add(perguntaNRA3P1);
			listaPerguntasRequerimento.add(perguntaNRA3P11);
			listaPerguntasRequerimento.add(perguntaNRA3P111);
			listaPerguntasRequerimento.add(perguntaNRA3P1111);
			listaPerguntasRequerimento.add(perguntaNRA3P11111);
			listaPerguntasRequerimento.add(perguntaNRA3P112);
			listaPerguntasRequerimento.add(perguntaNRA3P1121);
			listaPerguntasRequerimento.add(perguntaNRA3P12);
			listaPerguntasRequerimento.add(perguntaNRA3P12N);
			listaPerguntasRequerimento.add(perguntaNRA3P13);
			listaPerguntasRequerimento.add(perguntaNRA3P14);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListas()  {
		this.tiposAreaProtegida = this.licencaService.obterListaAreaProtegida();
		this.tiposAreaPreservacaoPermanente = this.licencaService.obterListaAreaPreservacaoPermanente(TipoAreaProtegidaEnum.AREA_DE_PRESERVACAO_PERMANENTE.getId());
		this.tiposGestao = this.licencaService.obterListaTipoGestao();
	}

	private void carregarRequerimentoTipoArea() throws Exception {
		List<TipoAreaPreservacaoPermanente> listaApp = this.licencaService.getListTipoAppByLicenca(requerimento); /** alterar nome do metodo*/
		this.requerimento.setTipoAreaPreservacaoPermanenteCollection(listaApp);
		List<TipoAreaProtegida> listaTipoArea = this.licencaService.getListTipoAreaProtegidaByLicenca(requerimento); /** alterar nome do metodo*/
		this.requerimento.setTipoAreaProtegidaCollection(listaTipoArea);
		this.changeAreaProtegida();
		if(!Util.isNull(requerimentoUnidadeConservacao) && !Util.isNull(requerimentoUnidadeConservacao.getIdeUnidadeConservacao())){ /** e quando não for 1x1?*/
			this.tipoGestao = requerimentoUnidadeConservacao.getIdeUnidadeConservacao().getIdeTipoGestao();
				
		}
		this.carregarListaUnidadeConservacaoEspecifica();
	}

	public void carregarTipologiasEmpreendimento() {
		tipologias = new ArrayList<Tipologia>();
		try {
			if(!Util.isNull(super.getRequerimento())) {
				Collection<EmpreendimentoRequerimento> listarEmpreendimentoRequerimento = empreendimentoRequerimentoService.listarEmpreendimentoRequerimento(super.getRequerimento());
				super.getRequerimento().setEmpreendimentoRequerimentoCollection(listarEmpreendimentoRequerimento);
				for (EmpreendimentoRequerimento er : super.getRequerimento().getEmpreendimentoRequerimentoCollection()) {
					tipologias.addAll(empreendimentoService.buscarTodasTipologiasDoEmpreendimento(er.getIdeEmpreendimento()));
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void changePerguntaNRA3P1(ValueChangeEvent vEvent) {
		perguntaNRA3P12.setIndResposta(null);
		perguntaNRA3P12N.setIndResposta(null);
		changePerguntaNRA3P12(new ValueChangeEvent(new HtmlSelectBooleanCheckbox(), null, false));
	}

	private void limparDadosLicenca() {
		this.licenca.setNumProcessoLicenca(null);
		this.licenca.setDscAtividadeProcessoLicencaMunicipal(null);
	}
	
	public void changePerguntaNRA3P12(ValueChangeEvent vEvent) {
		if (!(Boolean) vEvent.getNewValue()) {
			limparDadosLicenca();
			this.perguntaNRA3P14.setIndResposta(null);
		}
	}

	public void changePerguntaNRA3P13(ValueChangeEvent vEvent) {
		this.requerimento.setTipoAreaProtegidaCollection(new ArrayList<TipoAreaProtegida>());
		this.requerimento.setTipoAreaPreservacaoPermanenteCollection(new ArrayList<TipoAreaPreservacaoPermanente>());
		this.requerimentoUnidadeConservacao.setIdeUnidadeConservacao(null);
		this.tipoGestao = null;
		indApp = false;
		indUc = false;
	}

	public void changeAreaProtegida() {
		if (this.requerimento.getTipoAreaProtegidaCollection().contains(
				new TipoAreaProtegida(TipoAreaProtegidaEnum.AREA_DE_PRESERVACAO_PERMANENTE.getId()))) {
			this.indApp = true;
		} else {
			this.indApp = false;
			if (!Util.isNullOuVazio(this.requerimento.getTipoAreaPreservacaoPermanenteCollection())) {
				this.requerimento.setTipoAreaPreservacaoPermanenteCollection(new ArrayList<TipoAreaPreservacaoPermanente>());
			}
		}

		if (this.requerimento.getTipoAreaProtegidaCollection().contains(
				new TipoAreaProtegida(TipoAreaProtegidaEnum.UNIDADE_DE_CONSERVACAO.getId()))) {
			this.indUc = true;
		} else {
			this.tipoGestao = null;
			this.indUc = false;
		}
	}

	public void changeTipoGestao(ValueChangeEvent vEvent) {
		this.tipoGestao = (TipoGestao) vEvent.getNewValue();
		this.requerimentoUnidadeConservacao.setIdeUnidadeConservacao(null);
		this.requerimentoUnidadeConservacao.setNomUnidadeConservacaoMunicipio(null);
		this.carregarListaUnidadeConservacaoEspecifica();
	}

	public void carregarListaUnidadeConservacaoEspecifica() {
		try {
			if (!Util.isNull(this.tipoGestao)){
				this.unidadesConservacao = licencaService.obterListaUnidadeConservacaoByIdeTipoGestao(this.tipoGestao.getIdeTipoGestao());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, "Erro ao carregar a lista Especifica de Unidade Conservação." + e.getMessage());
		}
	}

	public void salvarAba(){
		try {
			if (validarAba()) {

				this.salvar();

				JsfUtil.addSuccessMessage("Etapa 3 salva com sucesso.");
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	public void salvar() {

		this.licenca.setTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_LICENCA.getId()));

		if(isLicenciada()){
			Licenca lic = null;

			if(this.licenca.getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(TipoSolicitacaoEnum.POSSUI_LICENCA_MUNICIPAL_FEDERAL.getId()))
				lic = this.licenca;
			else
				lic = new Licenca(this.novoRequerimentoController.getRequerimentoSelecionado());

			lic.setNumProcessoLicenca(licenca.getNumProcessoLicenca());
			lic.setDscAtividadeProcessoLicencaMunicipal(licenca.getDscAtividadeProcessoLicencaMunicipal());
			lic.setTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.POSSUI_LICENCA_MUNICIPAL_FEDERAL.getId()));
			licencaService.excluirSalvarLicenca(licenca, lic);
			try {
				this.licenca = lic.clone();
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getSimpleName(), Level.ERROR, e);
			}

		} else if(this.perguntaNRA3P1.getIndResposta() || isEmProcessoDeLicenciamento()){
			licencaService.salvarLicenca(this.licenca);
			
		} else {
			excluirLicenca();
		}
		
		if(!Util.isNullOuVazio(perguntaNRA3P11111) && !Util.isNullOuVazio(perguntaNRA3P11111.getIndResposta()) && perguntaNRA3P11111.getIndResposta() && !hasRequerimentoProcedimentoEspecial){
			requerimentoProcedimentoEspecialService.salvarRequerimentoProcedimentoEspecial(requerimentoProcedimentoEspecial);
		}

		Requerimento req = novoRequerimentoController.getRequerimentoSelecionado();

		this.pRequerimentoService.salvaListPerguntaRequerimento(super.listaPerguntasRequerimento,req);
		if(perguntaNRA3P13.getIndResposta()){
			this.requerimentoService.inserirRequerimento(this.requerimento);
		}

		List<RequerimentoUnidadeConservacao> requerimentosUnidades = this.licencaService.obterRequerimentoUnidadeConservacaoByRequerimento(this.requerimento);
		for(RequerimentoUnidadeConservacao ru : requerimentosUnidades){
			this.requerimentoService.excluirRequerimentoUnidadeConservacao(ru);
		}
		if(!Util.isNull(this.requerimentoUnidadeConservacao) && indUc &&
				(!Util.isNull(this.requerimentoUnidadeConservacao.getIdeUnidadeConservacao()) || !Util.isNullOuVazio(this.requerimentoUnidadeConservacao.getNomUnidadeConservacaoMunicipio()))){
			RequerimentoUnidadeConservacao ruc = new RequerimentoUnidadeConservacao();
			ruc.setIdeRequerimento(req);
			ruc.setIdeUnidadeConservacao(this.requerimentoUnidadeConservacao.getIdeUnidadeConservacao());
			ruc.setNomUnidadeConservacaoMunicipio(this.requerimentoUnidadeConservacao.getNomUnidadeConservacaoMunicipio());
			this.requerimentoService.salvarRequerimentoUnidadeConservacao(ruc);
			
		}
		if(!Util.isNullOuVazio(req) 
				&& !Util.isNullOuVazio(req.getIdeRequerimento()) 
				&& !Util.isNullOuVazio(req.getNumRequerimento())) {

			TramitacaoRequerimento tramitacao = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(req.getIdeRequerimento());

			if(!Util.isNullOuVazio(tramitacao) && tramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus()){
				this.tramitacaoRequerimentoService.tramitar(req, StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO, super.getOperador());
			}	
		}
	}

	/**
	 * 
	 * Método que retorna TRUE se a resposta da pergunta <b>* 1.2. O empreendimento ou atividade está em processo de licenciamento pelo município ou união?</b> for SIM
	 *
	 * @return perguntaNR_A3_P12.getIndResposta()
	 *
	 * @author eduardo.fernandes
	 * @since 21/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8164">#8164</a>
	 */
	public boolean isEmProcessoDeLicenciamento() {
		return !Util.isNull(this.perguntaNRA3P12.getIndResposta()) && this.perguntaNRA3P12.getIndResposta();
	}

	/**
	 * 
	 * Método que retorna TRUE se a resposta da pergunta <b>1.2. O empreendimento ou atividade foi licenciada pelo município ou união?</b> for SIM 
	 *
	 * @return perguntaNR_A3_P12_N.getIndResposta()
	 *
	 * @author eduardo.fernandes
	 * @since 21/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8164">#8164</a>
	 */
	public boolean isLicenciada() {
		return !Util.isNull(this.perguntaNRA3P12N.getIndResposta()) && this.perguntaNRA3P12N.getIndResposta();
	}

	public void excluirLicenca()  {
		licencaService.excluirLicenca(licenca);
		limparDadosLicenca();
		setFalseIndDla();
	}

	private void setFalseIndDla()  {

		EmpreendimentoRequerimento er = this.novoRequerimentoController.getEmpreendimentoRequerimento();
		
		ProgramaGoverno programaGoverno = this.novoRequerimentoController.getEmpreendimentoRequerimento().getProgramaGoverno();

		if(!Util.isNullOuVazio(er) && !Util.isNullOuVazio(er.getIdeRequerimento()) && !Util.isNullOuVazio(er.getIdeEmpreendimento())) {

			er = empreendimentoService.buscarEmpreendimentoRequerimento(er.getIdeRequerimento(), er.getIdeEmpreendimento());

			er.setIndDla(false);

			empreendimentoService.salvarEmpreendimentoRequerimento(er);

			this.novoRequerimentoController.setEmpreendimentoRequerimento(er);
			this.novoRequerimentoController.getEmpreendimentoRequerimento().setProgramaGoverno(programaGoverno);
		}
	}

	public boolean validarAba(){
		boolean valido = true;
		if (Util.isNullOuVazio(this.perguntaNRA3P1.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' no campo 1.");
			valido = false;
		} else {
			if (this.perguntaNRA3P1.getIndResposta()) {

				if (Util.isNullOuVazio(this.perguntaNRA3P12.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' no campo 1.2.");
					valido = false;
				} else if (this.perguntaNRA3P12.getIndResposta()) {
					if (Util.isNullOuVazio(this.licenca.getNumProcessoLicenca())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.1.");
						valido = false;
					}
					if (Util.isNullOuVazio(this.licenca.getDscAtividadeProcessoLicencaMunicipal())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.2.");
						valido = false;
					}
				}

				if (Util.isNullOuVazio(this.perguntaNRA3P14.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' no campo 1.3.");
					valido = false;
				}

			}
			else {
				if (Util.isNullOuVazio(this.perguntaNRA3P12N.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' no campo 1.2.");
					valido = false;
				} else if (this.perguntaNRA3P12N.getIndResposta()) {
					if (Util.isNullOuVazio(this.licenca.getNumProcessoLicenca())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.1.");
						valido = false;
					}
					if (Util.isNullOuVazio(this.licenca.getDscAtividadeProcessoLicencaMunicipal())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.2.");
						valido = false;
					}
				}
			}

			if (Util.isNull(this.perguntaNRA3P11.getIndResposta())) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' no campo 1.1.");
				valido = false;
			}
			else if(perguntaNRA3P11.getIndResposta())	{

				PerguntaRequerimento perguntaNRA2P5 = novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P5();
				if(!Util.isNull(perguntaNRA2P5.getIndResposta()) && !perguntaNRA2P5.getIndResposta()) {
					JsfUtil.addWarnMessage("Não será possível fazer a solicitação da autorização do procedimento especial de licenciamento em conjunto com solicitações de alteração, prorrogação e/ou renovação de licença/outorga. Caso seja essa sua necessidade selecione “Nenhuma das opções anteriores” na etapa 2.");
					valido = false;
				}

				if(Util.isNull(perguntaNRA3P111.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.1.");
					valido = false;
				}
				else if(!perguntaNRA3P111.getIndResposta()) {

					if(Util.isNull(perguntaNRA3P1111.getIndResposta())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.1.1.");
						valido = false;
					} else if(!perguntaNRA3P1111.getIndResposta()){
						if(Util.isNull(perguntaNRA3P11111.getIndResposta())) {
							JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.1.1.1");
							valido = false;
						} else if(!Util.isNullOuVazio(perguntaNRA3P11111) && !Util.isNullOuVazio(perguntaNRA3P11111.getIndResposta()) && !perguntaNRA3P11111.getIndResposta()) {
							JsfUtil.addWarnMessage("Para licenciar o empreendimento deverá ser firmado Termo de compromisso com o INEMA para regularizar questões referentes à supressão de vegetação nativa realizada irregularmente. Para isso, protocole a sua solicitação e proposta de Termo de Compromisso, com base no Art. 291 do Regulamento da Lei Estadual 10.431/2006, aprovado pelo Decreto nº 14.024/2012, em alguma unidade da Central de Atendimento e aguarde o nosso retorno.");
							valido = false;
						}
					}
					 else {
						if(!Util.isNullOuVazio(perguntaNRA3P11111) && !Util.isNullOuVazio(perguntaNRA3P11111.getIndResposta()) && perguntaNRA3P11111.getIndResposta() && Util.isNullOuVazio(requerimentoProcedimentoEspecial.getDataAssinaturaTermoCompromisso())){
							JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.1.1.1.1.");
							valido = false;
						}
					}
					
				}
				else if(perguntaNRA3P111.getIndResposta()){
					setPerguntaNR_A3_P1111(new PerguntaRequerimento());
				}
				
				if(!Util.isNullOuVazio(perguntaNRA3P11111) && !Util.isNullOuVazio(perguntaNRA3P11111.getIndResposta()) && perguntaNRA3P11111.getIndResposta()){
					if(!Util.isNullOuVazio(licenca) && Util.isNullOuVazio(requerimentoProcedimentoEspecial.getDataAssinaturaTermoCompromisso())){
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.1.1.1.1.");
						valido = false;
					}
				}

				if(Util.isNull(perguntaNRA3P112.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.2.");
					valido = false;
				}
				else if(perguntaNRA3P112.getIndResposta()){
					if(Util.isNull(perguntaNRA3P1121.getIndResposta())) {
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.1.2.1.");
						valido = false;
					}
					else if(!perguntaNRA3P1121.getIndResposta()) {
						JsfUtil.addWarnMessage("A autorização por procedimento especial de licenciamento só poderá ser solicitada após a liberação da dispensa ou outorga de uso de recursos hídricos.");
						valido = false;
					}
				}
			}			
		}
		
		if (Util.isNullOuVazio(this.perguntaNRA3P13.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' no campo 2.");
			valido = false;
		} else if (this.perguntaNRA3P13.getIndResposta()) {
			if (Util.isNullOuVazio(this.requerimento.getTipoAreaProtegidaCollection())) {
				JsfUtil.addWarnMessage("Por favor, selecione pelo menos um item do campo 2.1.");
				valido = false;
			} else {
				boolean contemPreservacaoPermanente = this.requerimento.getTipoAreaProtegidaCollection().contains(
						new TipoAreaProtegida(TipoAreaProtegidaEnum.AREA_DE_PRESERVACAO_PERMANENTE.getId()));
				for (TipoAreaProtegida tipoAreaProtegida : this.requerimento.getTipoAreaProtegidaCollection()) {
					if (tipoAreaProtegida.getIdeTipoAreaProtegida() == 2) {
						if (Util.isNullOuVazio(this.requerimento.getTipoAreaPreservacaoPermanenteCollection())) {
							JsfUtil.addWarnMessage("Por favor, selecione pelo menos um item do campo 2.1.1.");
							valido = false;
						}
					} else if (tipoAreaProtegida.getIdeTipoAreaProtegida() == 3) {
						String numeroPergunta = contemPreservacaoPermanente ? "2.1.2." : "2.1.1.";
						String numeroSubPergunta = contemPreservacaoPermanente ? "2.1.2.1." : "2.1.1.1.";
						if (Util.isNullOuVazio(this.tipoGestao)) {
							JsfUtil.addWarnMessage("Por favor, selecione pelo menos um item do campo "+numeroPergunta);
							valido = false;
						} else if (this.tipoGestao.getIdeTipoGestao() == 3) {
							if (Util.isNullOuVazio(this.requerimentoUnidadeConservacao.getNomUnidadeConservacaoMunicipio())) {
								JsfUtil.addWarnMessage("Por favor, preencha o campo "+numeroSubPergunta);
								valido = false;
							}
						} else if (this.tipoGestao.getIdeTipoGestao() == 1 || this.tipoGestao.getIdeTipoGestao() == 2) {
							if (Util.isNullOuVazio(this.requerimentoUnidadeConservacao.getIdeUnidadeConservacao())) {
								JsfUtil.addWarnMessage("Por favor, selecione um item do campo "+numeroSubPergunta);
								valido = false;
							}
						}
					}
				}
			}
		}
		
		Date dataHoje  = new Date();
		if (!Util.isNullOuVazio(requerimentoProcedimentoEspecial.getDataAssinaturaTermoCompromisso())){ 
			if (Util.validarDuasDatas(dataHoje, requerimentoProcedimentoEspecial.getDataAssinaturaTermoCompromisso())) {
				JsfUtil.addWarnMessage("A data assinatura do termo de compromisso não pode ser maior que hoje.");
				valido = false;
			}
		}
		return valido;
	}

	public Boolean getAutorizacaoOuLicenca(){
		if(this.perguntaNRA3P1 != null && this.perguntaNRA3P1.getIndResposta() != null)
			return this.perguntaNRA3P1.getIndResposta();
		else
			return false;
	}

	private void verificaTipologias() {
		
		if(!Util.isNullOuVazio(tipologias)) {
			int tipologiaXouY=0;
			boolean naoAltera = false;
			
			for (Tipologia t : tipologias) {
				
				if(t.isOutorga() || t.isAtoFlorestal()) naoAltera = true;
				else naoAltera = false;
				
				if(t.isTipologiaEspecial()) indTipologiaEspecial = true;
			}
			
			indSomenteTipologiaXouY = tipologiaXouY == tipologias.size();

			if(naoAltera) {
				tipologiaXouY++;
				
				if((!Util.isNullOuVazio(this.perguntaNRA3P1.getIndResposta()) && this.perguntaNRA3P1.getIndResposta() && indSomenteTipologiaXouY) || Util.isNullOuVazio(this.perguntaNRA3P1.getIndResposta())){
					this.perguntaNRA3P1.setIndResposta(false);
					changePerguntaNRA3P1(new ValueChangeEvent(new HtmlSelectBooleanCheckbox(), null, false));
				}
			}
			
			
			if(Util.isNull(indTipologiaEspecial) || !indTipologiaEspecial){
				indTipologiaEspecial = false;
				perguntaNRA3P11.setIndResposta(false);
			}
		}
	}

	public boolean isDisabledPerguntaNRA3P1() throws Exception {

		if(!Util.isNull(novoRequerimentoController) && novoRequerimentoController.isDesabilitarTudo() 
				|| !Util.isNull(perguntaNRA3P11.getIndResposta()) && perguntaNRA3P11.getIndResposta()) {
			return true;
		}

		if(isAbaAtiva()) {
			if(!Util.isNull(novoRequerimentoController) && !Util.isNull(novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController())) {
				if(novoRequerimentoController.isDesabilitarTudo() 
						|| novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isAlteracaoLicenca() 
						|| novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isRenovacaoLicenca() 
						|| indSomenteTipologiaXouY) {
					return true;
				} 
				else {
					return false;
				}
			}
		} 

		return false;

	}

	public boolean isDisabledPerguntaNR_A3_P11()  {

		if(!Util.isNull(novoRequerimentoController) && novoRequerimentoController.isDesabilitarTudo()) {
			return true;
		}
		
		if(indTipologiaEspecial != null) {
			return !indTipologiaEspecial;
		}
		
		try {
			Requerimento req = novoRequerimentoController.getRequerimentoSelecionado();
	
			if(req != null && req.getIdeRequerimento() != null) { 
	
				TramitacaoRequerimento tramitacao = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(req.getIdeRequerimento());
	
				if(!Util.isNullOuVazio(tramitacao) && 
						(!tramitacao.getIdeStatusRequerimento().isRequerimentoIncompleto() && !tramitacao.getIdeStatusRequerimento().isAguardandoEnquadramento())){
					
					if(perguntaNRA3P11 != null && perguntaNRA3P11.getIndResposta() != null && perguntaNRA3P11.getIndResposta()) {
						return true;
					}
				}	
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return true;
	}

	public boolean isDisabledPerguntaNRA3P12() throws Exception {

		if(!Util.isNull(novoRequerimentoController) && novoRequerimentoController.isDesabilitarTudo()) {
			return true;
		}

		if(!Util.isNull(perguntaNRA3P11.getIndResposta())) {
			return perguntaNRA3P11.getIndResposta();
		}

		return false;		
	}

	public boolean isAbaAtiva() {
		if(!Util.isNull(novoRequerimentoController) 
				&& !Util.isNull(novoRequerimentoController.getActiveTab()) 
				&& novoRequerimentoController.getActiveTab() == 2) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Método que verifica se existe apenas uma Área Protegida e se ela é APP.
	 * 
	 * @author eduardo.fernandes 
	 * @since 24/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @return
	 */
	protected boolean isTipoAreaProtegidaApenasApp(){
		if((!Util.isNullOuVazio(this.requerimento) && !Util.isNullOuVazio(this.requerimento.getTipoAreaProtegidaCollection()) )
				&& this.requerimento.getTipoAreaProtegidaCollection().size() == 1) {
			return ((List<TipoAreaProtegida>) this.requerimento.getTipoAreaProtegidaCollection()).get(0).equals(new TipoAreaProtegida(TipoAreaProtegidaEnum.AREA_DE_PRESERVACAO_PERMANENTE.getId()));
		}
		return false;
	}
	
	public PerguntaRequerimento getPerguntaNR_A3_P1() {
		return perguntaNRA3P1;
	}

	public void setPerguntaNR_A3_P1(PerguntaRequerimento perguntaNRA3P1) {
		this.perguntaNRA3P1 = perguntaNRA3P1;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P12() {
		return perguntaNRA3P12;
	}

	public void setPerguntaNR_A3_P12(PerguntaRequerimento perguntaNRA3P12) {
		this.perguntaNRA3P12 = perguntaNRA3P12;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P12_N() {
		return perguntaNRA3P12N;
	}

	public void setPerguntaNR_A3_P12_N(PerguntaRequerimento perguntaNRA3P12N) {
		this.perguntaNRA3P12N = perguntaNRA3P12N;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P13() {
		return perguntaNRA3P13;
	}

	public void setPerguntaNR_A3_P13(PerguntaRequerimento perguntaNRA3P13) {
		this.perguntaNRA3P13 = perguntaNRA3P13;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P14() {
		return perguntaNRA3P14;
	}

	public void setPerguntaNR_A3_P14(PerguntaRequerimento perguntaNRA3P14) {
		this.perguntaNRA3P14 = perguntaNRA3P14;
	}

	public Licenca getLicenca() {
		return licenca;
	}

	public void setLicenca(Licenca licenca) {
		this.licenca = licenca;
	}

	public Collection<TipoAreaProtegida> getTiposAreaProtegida() {
		return tiposAreaProtegida;
	}

	public void setTiposAreaProtegida(Collection<TipoAreaProtegida> tiposAreaProtegida) {
		this.tiposAreaProtegida = tiposAreaProtegida;
	}

	public Collection<TipoAreaPreservacaoPermanente> getTiposAreaPreservacaoPermanente() {
		return tiposAreaPreservacaoPermanente;
	}

	public void setTiposAreaPreservacaoPermanente(Collection<TipoAreaPreservacaoPermanente> tiposAreaPreservacaoPermanente) {
		this.tiposAreaPreservacaoPermanente = tiposAreaPreservacaoPermanente;
	}

	public Boolean getIndApp() {
		return indApp;
	}

	public void setIndApp(Boolean indApp) {
		this.indApp = indApp;
	}

	public Boolean getIndUc() {
		return indUc;
	}

	public void setIndUc(Boolean indUc) {
		this.indUc = indUc;
	}

	public TipoGestao getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(TipoGestao tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public Collection<UnidadeConservacao> getUnidadesConservacao() {
		return unidadesConservacao;
	}

	public void setUnidadesConservacao(Collection<UnidadeConservacao> unidadesConservacao) {
		this.unidadesConservacao = unidadesConservacao;
	}

	public Collection<TipoGestao> getTiposGestao() {
		return tiposGestao;
	}

	public void setTiposGestao(Collection<TipoGestao> tiposGestao) {
		this.tiposGestao = tiposGestao;
	}

	public List<Tipologia> getTipologias() {
		return tipologias;
	}

	public void setTipologias(List<Tipologia> tipologias) {
		this.tipologias = tipologias;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P11() {
		return perguntaNRA3P11;
	}

	public void setPerguntaNR_A3_P11(PerguntaRequerimento perguntaNRA3P11) {
		this.perguntaNRA3P11 = perguntaNRA3P11;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P111() {
		return perguntaNRA3P111;
	}

	public void setPerguntaNR_A3_P111(PerguntaRequerimento perguntaNRA3P111) {
		this.perguntaNRA3P111 = perguntaNRA3P111;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P1111() {
		return perguntaNRA3P1111;
	}

	public void setPerguntaNR_A3_P1111(PerguntaRequerimento perguntaNRA3P1111) {
		this.perguntaNRA3P1111 = perguntaNRA3P1111;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P11111() {
		return perguntaNRA3P11111;
	}
	
	public void setPerguntaNR_A3_P11111(PerguntaRequerimento perguntaNRA3P11111) {
		this.perguntaNRA3P11111 = perguntaNRA3P11111;
	}
	
	public PerguntaRequerimento getPerguntaNR_A3_P112() {
		return perguntaNRA3P112;
	}

	public void setPerguntaNR_A3_P112(PerguntaRequerimento perguntaNRA3P112) {
		this.perguntaNRA3P112 = perguntaNRA3P112;
	}

	public PerguntaRequerimento getPerguntaNR_A3_P1121() {
		return perguntaNRA3P1121;
	}

	public void setPerguntaNR_A3_P1121(PerguntaRequerimento perguntaNRA3P1121) {
		this.perguntaNRA3P1121 = perguntaNRA3P1121;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public RequerimentoUnidadeConservacao getRequerimentoUnidadeConservacao() {
		return requerimentoUnidadeConservacao;
	}

	public void setRequerimentoUnidadeConservacao(
			RequerimentoUnidadeConservacao requerimentoUnidadeConservacao) {
		this.requerimentoUnidadeConservacao = requerimentoUnidadeConservacao;
	}

	public RequerimentoProcedimentoEspecial getRequerimentoProcedimentoEspecial() {
		return requerimentoProcedimentoEspecial;
	}

	public void setRequerimentoProcedimentoEspecial(
			RequerimentoProcedimentoEspecial requerimentoProcedimentoEspecial) {
		this.requerimentoProcedimentoEspecial = requerimentoProcedimentoEspecial;
	}

	
}