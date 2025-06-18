package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.ObjetoAlteracao;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.ObjetoAlteracaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.ObjetoAlteracaoService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import groovyjarjarantlr.Utils;

@Named("abaRenovacaoAlteracaoProrrogacaoController")
@ViewScoped
public class AbaRenovacaoAlteracaoProrrogacaoController extends BaseAbaController{

	private List<Outorga> outorgas;
	private List<Licenca> licencas;
	private List<SolicitacaoAdministrativo> condicionantes;
	private List<SolicitacaoAdministrativo> prorrogacoes;
	
	private Outorga outorgaAExcluir;
	
	private Licenca licencaAExcluir;
	
	private SolicitacaoAdministrativo prorrogacaoAExcluir; 
	
	private SolicitacaoAdministrativo condicionanteAExcluir;
	
	private PerguntaRequerimento perguntaNRA2P1;
	private PerguntaRequerimento perguntaNRA2P2;
	private PerguntaRequerimento perguntaNRA2P3;
	private PerguntaRequerimento perguntaNRA2P4;
	private PerguntaRequerimento perguntaNRA2P5;
	private PerguntaRequerimento perguntaNRA2P6;
	
	private boolean temOutroUsoRecursoHidrico = false;
	
	// Variaveis para a comparação de edição
	private List<Outorga> outorgasBkp;
	private List<Licenca> licencasBkp;
	private List<SolicitacaoAdministrativo> condicionantesBkp;
	private List<SolicitacaoAdministrativo> prorrogacoesBkp;

	private PerguntaRequerimento perguntaNRA2P1Bkp;
	private PerguntaRequerimento perguntaNRA2P2Bkp;
	private PerguntaRequerimento perguntaNRA2P3Bkp;
	private PerguntaRequerimento perguntaNRA2P4Bkp;
	private PerguntaRequerimento perguntaNRA2P5Bkp;
	private PerguntaRequerimento perguntaNRA2P6Bkp;
	
	private BigDecimal numVazao;
	
	@EJB 
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private PerguntaService pService;

	@EJB
	private NovoRequerimentoService newRequerimentoService;
	
	@EJB
	private LicencaService licencaService;
	
	@EJB
	private OutorgaService outorgaService;
	
	@EJB
	private ObjetoAlteracaoService objetoAlteracaoService;
	
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	
	@EJB
	private PerguntaRequerimentoService pRequerimentoService;
	
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@PostConstruct
	public void init() {
		try {
			
			this.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();
			this.outorgas = new ArrayList<Outorga>();
			this.licencas = new ArrayList<Licenca>();
			this.condicionantes = new ArrayList<SolicitacaoAdministrativo>();
			this.prorrogacoes = new ArrayList<SolicitacaoAdministrativo>();
			
			this.carregarPerguntasAba();
			this.carregarRespostas();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void carregarPerguntasAba()  {
		this.perguntaNRA2P1 = super.carregarPerguntaByCod("NR_A2_P1");
		this.perguntaNRA2P2 = super.carregarPerguntaByCod("NR_A2_P2");
		this.perguntaNRA2P3 = super.carregarPerguntaByCod("NR_A2_P3");
		this.perguntaNRA2P4 = super.carregarPerguntaByCod("NR_A2_P4");
		this.perguntaNRA2P5 = super.carregarPerguntaByCod("NR_A2_P5");
		this.perguntaNRA2P6 = super.carregarPerguntaByCod("NR_A2_P6");
		
		this.perguntaNRA2P1Bkp = this.perguntaNRA2P1;
		this.perguntaNRA2P2Bkp = this.perguntaNRA2P2;
		this.perguntaNRA2P3Bkp = this.perguntaNRA2P3;
		this.perguntaNRA2P4Bkp = this.perguntaNRA2P4;
		this.perguntaNRA2P5Bkp = this.perguntaNRA2P5;
		this.perguntaNRA2P6Bkp = this.perguntaNRA2P6;

		
		super.listaPerguntasRequerimento.add(this.perguntaNRA2P1);
		super.listaPerguntasRequerimento.add(this.perguntaNRA2P2);
		super.listaPerguntasRequerimento.add(this.perguntaNRA2P3);
		super.listaPerguntasRequerimento.add(this.perguntaNRA2P4);
		super.listaPerguntasRequerimento.add(this.perguntaNRA2P5);
		super.listaPerguntasRequerimento.add(this.perguntaNRA2P6);
		
	}
	
	@Override
	protected void carregarRespostas() {
		try {
			
			super.carregarRespostasDasPerguntas();
			
			Requerimento requerimento = super.getRequerimento();
			this.licencas = this.licencaService.getListaLicencaByIdeRequerimento(requerimento);
			this.outorgas = this.outorgaService.getOutorgaByIdeRequerimentoByNaoNovaOutorga(requerimento);
			this.prorrogacoes = this.solicitacaoAdministrativoService.getListaProrrogacaoPValidadeByIdeRequerimento(requerimento);
			this.condicionantes = this.solicitacaoAdministrativoService.getListaCondicionanteByIdeRequerimento(requerimento);
			
			this.licencasBkp = this.licencas;
			this.outorgasBkp = this.outorgas;
			this.prorrogacoesBkp = this.prorrogacoes;
			this.condicionantesBkp = this.condicionantes;
			
			this.numVazao = novoRequerimentoController.getEmpreendimentoRequerimento().getNumVazaoTotal();
			
			if(!Util.isNullOuVazio(novoRequerimentoController.getEmpreendimentoRequerimento()) && 
			Util.isNullOuVazio(novoRequerimentoController.getEmpreendimentoRequerimento().getNumVazaoTotal())){
				atualizarVazaoTotalOutorgas();
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private BigDecimal calcularVazaoTotalOutorgas() throws Exception{
		BigDecimal vazaoTotal = new BigDecimal(0);
		for (Outorga outorg : this.outorgas) {
			if(outorg.getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(new Integer(6)) && !outorg.getIntervencao() && !outorg.getAproveitHidrico()){//SE A OUTORGA FOR DO TIPO ALTERACAO
				if(outorg.getIdeTipoAlteracao().getIdeTipoAlteracao().equals(new Integer(4))){//se for remover ponto outorgado
					carregarOutorgaLocalizacaoGeografica(outorg);
					for(OutorgaLocalizacaoGeografica outLocGeo : outorg.getOutorgaLocalizacaoGeograficaCollection()){
						if(outLocGeo.getNumVazao() != null)
							vazaoTotal = vazaoTotal.subtract(outLocGeo.getNumVazao());
					}
				}else if(outorg.getIdeTipoAlteracao().getIdeTipoAlteracao().equals(1) || outorg.getIdeTipoAlteracao().getIdeTipoAlteracao().equals(2)
						|| outorg.getIdeTipoAlteracao().getIdeTipoAlteracao().equals(new Integer(3)) || outorg.getIdeTipoAlteracao().getIdeTipoAlteracao().equals(new Integer(5))){
					carregarOutorgaLocalizacaoGeografica(outorg);
					for(OutorgaLocalizacaoGeografica outLocGeo : outorg.getOutorgaLocalizacaoGeograficaCollection()){
						if(outorg.getIdeTipoAlteracao().getIdeTipoAlteracao().equals(1) || outorg.getIdeTipoAlteracao().getIdeTipoAlteracao().equals(2) && 
								!Util.isNullOuVazio(outLocGeo.getNumVazaoFinal())){
							vazaoTotal = vazaoTotal.add(outLocGeo.getNumVazaoFinal());
						}else{
							vazaoTotal = vazaoTotal.add(outLocGeo.getNumVazao());
						}
					}
				}
			}
		}
		
		this.numVazao = vazaoTotal; 
		return vazaoTotal;
	}

	/**
	 * @param outorg
	 * @throws Exception
	 */
	private void carregarOutorgaLocalizacaoGeografica(Outorga outorg){
		if(Util.isNullOuVazio(outorg.getOutorgaLocalizacaoGeograficaCollection()))
			outorg.setOutorgaLocalizacaoGeograficaCollection(this.outorgaLocalizacaoGeograficaService.buscarOutorgaLocalizacaoGeoByIdOutorga(outorg));
	}
	
	public void atualizarVazaoTotalOutorgas(){
		try {
			novoRequerimentoController.getEmpreendimentoRequerimento().setNumVazaoTotal(calcularVazaoTotalOutorgas());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
        	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void valueChangeDesmarcarPerguntaNRA2P5(ValueChangeEvent e) {
		Boolean resposta = (Boolean) e.getNewValue();
		if (resposta) {
			this.perguntaNRA2P5.setIndResposta(null);
		}
	}
	
	//Adicionar validação aqui.. 
	public void valueChangeDesmarcarPerguntaNRA2P6(ValueChangeEvent e) {
		Boolean resposta = (Boolean) e.getNewValue();
		if (resposta == null) {
			this.perguntaNRA2P6.setIndResposta(null);
		}else{
			this.perguntaNRA2P6.setIndResposta(resposta);
		}
	}
	
	public void valueChangeDesmarcarTodasPerguntas(ValueChangeEvent e) {
		Boolean resposta = (Boolean) e.getNewValue();
		if (resposta) {
			this.perguntaNRA2P1.setIndResposta(null);
			this.perguntaNRA2P2.setIndResposta(null);
			this.perguntaNRA2P3.setIndResposta(null);
			this.perguntaNRA2P4.setIndResposta(null);
			this.perguntaNRA2P6.setIndResposta(null);
		}
	}
	
	public void salvarAba(){
		try {
			
			if(validarAba()){
			
				this.salvar();
				
				if(Util.isNullOuVazio(perguntaNRA2P6.getIndResposta()))
					this.novoRequerimentoController.getAbaOutorgaController().setDisablePerguntaA4P1(false);
				else if(perguntaNRA2P6.getIndResposta())
					this.novoRequerimentoController.getAbaOutorgaController().setDisablePerguntaA4P1(false);
				else{
					this.novoRequerimentoController.getAbaOutorgaController().setDisablePerguntaA4P1(true);
					this.novoRequerimentoController.getAbaOutorgaController().getPerguntaNR_A4_P1().setIndResposta(false); 
				}
				
				if(isAlteracaoDados()) {
					this.novoRequerimentoController.getAbaOutorgaController().setAlteracaoDadosAba2(true);
				}
				
				JsfUtil.addSuccessMessage("Etapa 2 salva com sucesso.");
				
			}
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar aba processo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean validarAba(){
		boolean valido = true;
		if ((Util.isNullOuVazio(this.perguntaNRA2P1.getIndResposta()) || !this.perguntaNRA2P1.getIndResposta())
				&& (Util.isNull(this.perguntaNRA2P2.getIndResposta()) || !this.perguntaNRA2P2.getIndResposta())
				&& (Util.isNull(this.perguntaNRA2P3.getIndResposta()) || !this.perguntaNRA2P3.getIndResposta())
				&& (Util.isNull(this.perguntaNRA2P4.getIndResposta()) || !this.perguntaNRA2P4.getIndResposta())
				&& (Util.isNull(this.perguntaNRA2P5.getIndResposta()) || !this.perguntaNRA2P5.getIndResposta())) {
			valido = false;
			JsfUtil.addWarnMessage("Por favor, selecione ao menos uma das opções no campo 1.");
		} else {
			if (!Util.isNullOuVazio(this.perguntaNRA2P1.getIndResposta())) {
				if (this.perguntaNRA2P1.getIndResposta() && Util.isNullOuVazio(this.licencas)) {
					valido = false;
					JsfUtil.addWarnMessage("Por favor, inclua dados da Licença");
				}
			}
			if (!Util.isNullOuVazio(this.perguntaNRA2P2.getIndResposta())) {
				if (this.perguntaNRA2P2.getIndResposta() && Util.isNullOuVazio(this.outorgas)) {
					valido = false;
					JsfUtil.addWarnMessage("Por favor, inclua dados da Outorga");
				}
				if(Util.isNullOuVazio(perguntaNRA2P6.getIndResposta()) && this.perguntaNRA2P2.getIndResposta()){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.2.");
					valido = false;
				}
			}
			if (!Util.isNullOuVazio(this.perguntaNRA2P3.getIndResposta())) {
				if (this.perguntaNRA2P3.getIndResposta() && Util.isNullOuVazio(this.prorrogacoes)) {
					valido = false;
					JsfUtil.addWarnMessage("Por favor, inclua dados da Prorrogação de Prazo Validade");
				}
			}
			if (!Util.isNullOuVazio(this.perguntaNRA2P4.getIndResposta())) {
				if (this.perguntaNRA2P4.getIndResposta() && Util.isNullOuVazio(this.condicionantes)) {
					valido = false;
					JsfUtil.addWarnMessage("Por favor, inclua dados do Processo de Revisão");
				}
			}
		}
		return valido;
	}
	
	public void salvar(){
	
		this.pRequerimentoService.salvaListPerguntaRequerimento(super.listaPerguntasRequerimento,super.getRequerimento());
		
		this.excluirObjetosDesmarcados();
		EmpreendimentoRequerimento empreendReq = novoRequerimentoController.carregarEmpreendRequerimento();
		newRequerimentoService.atualizarEmpreendimentoRequerimento(empreendReq);
		
		for(EmpreendimentoRequerimento empReq: novoRequerimentoController.getRequerimentoSelecionado().getEmpreendimentoRequerimentoCollection()){
			if(empReq.getIdeEmpreendimentoRequerimento().equals(empreendReq.getIdeEmpreendimentoRequerimento())){
				empReq.setNumVazaoTotal(empreendReq.getNumVazaoTotal());
			}
		}
		
		Requerimento req= novoRequerimentoController.getRequerimentoSelecionado();
		if(!Util.isNullOuVazio(req)                              &&
				   !Util.isNullOuVazio(req.getIdeRequerimento()) && 
				   !Util.isNullOuVazio(req.getNumRequerimento())){
					
					TramitacaoRequerimento tramitacao = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(req.getIdeRequerimento());
					
					if(!Util.isNullOuVazio(tramitacao) &&
						tramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus()){
					
						this.tramitacaoRequerimentoService.tramitar(req, StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO, this.getOperador());			
					}	
		}
		
		if(isAlteracaoDados()) {
			this.novoRequerimentoController.getAbaOutorgaController().setAlteracaoDadosAba2(true);
		}
	}
	
	private void excluirObjetosDesmarcados() {
		if (Util.isNullOuVazio(this.perguntaNRA2P1.getIndResposta()) || !this.perguntaNRA2P1.getIndResposta()) {
			this.removerLicenca();
		}
		if (Util.isNullOuVazio(this.perguntaNRA2P2.getIndResposta()) || !this.perguntaNRA2P2.getIndResposta()) {
			this.removerOutorga();
		}
		if (Util.isNullOuVazio(this.perguntaNRA2P3.getIndResposta()) || !this.perguntaNRA2P3.getIndResposta()) {
			this.removerProrrogacoesDeValidade();
		}
		if (Util.isNullOuVazio(this.perguntaNRA2P4.getIndResposta()) || !this.perguntaNRA2P4.getIndResposta()) {
			this.removerCondicionantes();
		}
	}

	private void removerLicenca() {
		if (!Util.isNullOuVazio(this.licencas)) {
			try {
				for (Licenca licenca : this.licencas) {
					pRequerimentoService.removerPerguntaReqByIdLicenca(licenca);
					objetoAlteracaoService.excluirLicencaObjetoAlteracaoByLicenca(licenca);
					licencaService.excluirLicenca(licenca);
				}
				this.licencas = new ArrayList<Licenca>();
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao excluir a lista de Renovar ou Alterar Licença Ambienal:"+ e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	private void removerOutorga() {
		if (!Util.isNullOuVazio(outorgas)) {
			try {
				for (Outorga outorgaTemp : outorgas) {
					outorgaService.excluirTipoCaptacaoByIdeOutorga(outorgaTemp);
					outorgaService.excluirOutorga(outorgaTemp);
				}
				outorgas = new ArrayList<Outorga>();
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao excluir a lista de Renovar, Alterar ou Cancelar Outorga:"+ e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	private void removerProrrogacoesDeValidade() {
		if (!Util.isNullOuVazio(prorrogacoes)) {
			try {
				for (SolicitacaoAdministrativo solicitacaoAdministrativoTemp : prorrogacoes) {
					solicitacaoAdministrativoService.excluirSolicitacaoAdministrativa(solicitacaoAdministrativoTemp);
				}
				this.prorrogacoes = new ArrayList<SolicitacaoAdministrativo>();
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao excluir a lista de Prorrogar Prazo Validade:" + e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	private void removerCondicionantes() {
		if (!Util.isNullOuVazio(condicionantes)) {
			try {
				for (SolicitacaoAdministrativo solicitacaoAdministrativoTemp : condicionantes) {
					solicitacaoAdministrativoService.excluirSolicitacaoAdministrativa(solicitacaoAdministrativoTemp);
				}
				condicionantes = new ArrayList<SolicitacaoAdministrativo>();
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao excluir a lista de Revisão de Condicionantes:" + e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void adicionarOuAtualizarLicenca(Licenca licenca) {
		
		int posicao = 0;
		boolean atualizarLicenca = false;
		
		for (Licenca lic : licencas) {
			if(lic.getIdeLicenca().equals(licenca.getIdeLicenca())) {
				posicao = this.licencas.indexOf(lic);
				atualizarLicenca = true;
			}
		}
		
		if(atualizarLicenca) {
			this.licencas.set(posicao, licenca);
		} else {
			this.licencas.add(licenca);
		}
	}
	
	public void adicionarOuAtualizarOutorga(Outorga outorga) {
		if(this.outorgas.contains(outorga)){
			int posicao = this.outorgas.indexOf(outorga);
			this.outorgas.set(posicao, outorga);
		}else{
			this.outorgas.add(outorga);
		}			
	}
	
	public void adicionarOuAtualizarProrrogacaoDeValidade(SolicitacaoAdministrativo revisaoDeCondicionante) {
		if(this.prorrogacoes.contains(revisaoDeCondicionante)){
			int posicao = this.prorrogacoes.indexOf(revisaoDeCondicionante);
			this.prorrogacoes.set(posicao, revisaoDeCondicionante);
		}else{
			this.prorrogacoes.add(revisaoDeCondicionante);
		}			
	}
	
	public void adicionarOuAtualizarRevisaoDeCondicionante(SolicitacaoAdministrativo prorrogacaoPrazoValidade) {
		if(this.condicionantes.contains(prorrogacaoPrazoValidade)){
			int posicao = this.condicionantes.indexOf(prorrogacaoPrazoValidade);
			this.condicionantes.set(posicao, prorrogacaoPrazoValidade);
		}else{
			this.condicionantes.add(prorrogacaoPrazoValidade);
		}			
	}

	public void excluirLicenca(){
		try {
			this.objetoAlteracaoService.excluirLicencaObjetoAlteracaoByLicenca(this.licencaAExcluir);
			this.pRequerimentoService.removerPerguntaReqByIdLicenca(this.licencaAExcluir);
			this.licencaService.excluirLicenca(this.licencaAExcluir);
			this.licencas.remove(this.licencaAExcluir);
			JsfUtil.addSuccessMessage("Licença removida com sucesso.");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirOutorga(){
		try {
			this.outorgaService.excluirTipoCaptacaoByIdeOutorga(this.outorgaAExcluir);
			this.outorgaService.excluirOutorga(this.outorgaAExcluir);
			this.outorgas.remove(this.outorgaAExcluir);
			JsfUtil.addSuccessMessage("Outorga removida com sucesso.");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirCondicionante(){
		try {
			this.solicitacaoAdministrativoService.excluirSolicitacaoAdministrativa(this.condicionanteAExcluir);
			this.condicionantes.remove(this.condicionanteAExcluir);
			JsfUtil.addSuccessMessage("Solicitação administrativa excluída com sucesso!");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirProrrogacao(){
		try {
			this.solicitacaoAdministrativoService.excluirSolicitacaoAdministrativa(this.prorrogacaoAExcluir);
			this.prorrogacoes.remove(this.prorrogacaoAExcluir);
			JsfUtil.addSuccessMessage("Solicitação administrativa excluída com sucesso!");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isAlteracaoLicenca(){
		Requerimento requerimento = this.novoRequerimentoController.getRequerimento();

		List<Licenca> licencas = this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getLicencas();

		for (Licenca licenca : licencas) {
			if (licenca.getIdeTipoSolicitacao().isAlteracaoLicenca()) {
				
				PerguntaRequerimento perguntaNRA2DRENALTP12 = 
						this.pRequerimentoService.consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA2_1_12.getCod(),
								requerimento.getIdeRequerimento(), licenca);

				if(!Util.isNull(perguntaNRA2DRENALTP12) 
						&& !Util.isNull(perguntaNRA2DRENALTP12.getIndResposta()) 
						&& perguntaNRA2DRENALTP12.getIndResposta()) {
					
					this.novoRequerimentoController.getAbaLicencaAutorizacaoController().getPerguntaNR_A3_P1().setIndResposta(false);
					return true;
				}
				
			}
		}

		return false;
	}
	
	public boolean isAlteracaoLicencaSomenteDeSubstituicao()  {
		Requerimento requerimento = this.novoRequerimentoController.getRequerimento();
		
		Licenca lic = licencaService.getLicencaByIdeRequerimentoTipoAlteracao(requerimento);
		
		if (!Util.isNullOuVazio(lic)) {
			List<ObjetoAlteracao> loa = (List<ObjetoAlteracao>) objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(lic);
			
			if (!Util.isNullOuVazio(loa) && loa.size() == 1) {

				if (loa.get(0).getIdeObjetoAlteracao().equals(ObjetoAlteracaoEnum.SUBSTITUICAO.getId())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isImpactoAdicional()  {
		List<Licenca> licencas = this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getLicencas();

		for (Licenca licenca : licencas) {
			if (licenca.getIdeTipoSolicitacao().isAlteracaoLicenca()) {
				Collection<ObjetoAlteracao> listObjAlteracao = objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(licenca);
				//Se for igual a "Substituição ou instalação de equipamento que altere o processo produtivo, gerando impacto adicional"
				if(!Util.isNullOuVazio(listObjAlteracao) && listObjAlteracao.size() == 1 && listObjAlteracao.iterator().next().getIdeObjetoAlteracao().equals(1)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isProjetoLicenciadoComImpactoAdicional(){
		List<Licenca> licencas = this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getLicencas();

		for (Licenca licenca : licencas) {
			if (licenca.getIdeTipoSolicitacao().isAlteracaoLicenca()) {
				Collection<ObjetoAlteracao> listObjAlteracao = objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(licenca);
				//Se for igual a "Substituição ou instalação de equipamento que altere o processo produtivo, gerando impacto adicional"
				if(!Util.isNullOuVazio(listObjAlteracao) && listObjAlteracao.size() == 1 && listObjAlteracao.iterator().next().getIdeObjetoAlteracao().equals(1)){
					return true;
				}
				//Se for igual a "Alteração do projeto licenciado, não mencionada nas opções anteriores, gerando impacto adicional"
				if(!Util.isNullOuVazio(listObjAlteracao) && listObjAlteracao.size() == 1 && listObjAlteracao.iterator().next().getIdeObjetoAlteracao().equals(new Integer(5))){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isProjetoLicenciadoComImpactoAdicionalNDA() {
		List<Licenca> licencas = this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getLicencas();
		
		for (Licenca licenca : licencas) {
			if (licenca.getIdeTipoSolicitacao().isAlteracaoLicenca()) {
				Collection<ObjetoAlteracao> listObjAlteracao = objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(licenca);
				//Se for igual a "Alteração do projeto licenciado, não mencionada nas opções anteriores, gerando impacto adicional"
				if(!Util.isNullOuVazio(listObjAlteracao) && listObjAlteracao.size() == 1 && listObjAlteracao.iterator().next().getIdeObjetoAlteracao().equals(new Integer(5))){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isProjetoLicenciadoComImpactoAdicionalSubstituicaoOuInstalacao()  {
		List<Licenca> licencas = this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getLicencas();
		for (Licenca licenca : licencas) {
			if (licenca.getIdeTipoSolicitacao().isAlteracaoLicenca()) {
				Collection<ObjetoAlteracao> listObjAlteracao = objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(licenca);
				//Se for igual a "Substituição ou instalação de equipamento que altere o processo produtivo, gerando impacto adicional"
				if(!Util.isNullOuVazio(listObjAlteracao) && listObjAlteracao.size() == 1 && listObjAlteracao.iterator().next().getIdeObjetoAlteracao().equals(1)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isRenovacaoLicenca() {
		List<Licenca> licencas = this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().getLicencas();

		for (Licenca licenca : licencas) {
			if (licenca.getIdeTipoSolicitacao().isRenovacaoLicenca()) {
				this.novoRequerimentoController.getAbaLicencaAutorizacaoController().getPerguntaNR_A3_P1().setIndResposta(false);
				return true;
			}
		}

		return false;
	}
	
	public boolean isAlteracaoDados() {
		
		if (this.perguntaNRA2P1.getIndResposta() !=  this.perguntaNRA2P1Bkp.getIndResposta() ||
			this.perguntaNRA2P2.getIndResposta()!= perguntaNRA2P2Bkp.getIndResposta() ||
			this.perguntaNRA2P6.getIndResposta() != this.perguntaNRA2P6Bkp.getIndResposta() ||
			this.perguntaNRA2P3.getIndResposta()!= this.perguntaNRA2P3Bkp.getIndResposta() ||
			this.perguntaNRA2P4.getIndResposta() != this.perguntaNRA2P4Bkp.getIndResposta() ||
			this.perguntaNRA2P5.getIndResposta() != this.perguntaNRA2P5Bkp.getIndResposta()) return true;
		
		if (this.licencas.size() !=  this.licencasBkp.size() || 
			this.outorgas.size() != this.outorgasBkp.size() ||
			this.prorrogacoes.size() != this.prorrogacoesBkp.size() ||
			this.condicionantes.size() != this.condicionantesBkp.size()) return true;
		
		
		for(Licenca licenca:this.licencas){
			if (!this.licencasBkp.contains(licenca)) return true;
		}
		
		for (Outorga outorga : this.outorgas) {
			if (!this.outorgasBkp.contains(outorga)) return true;
		}
		
		EmpreendimentoRequerimento empreendReq = novoRequerimentoController.carregarEmpreendRequerimento();
		for(EmpreendimentoRequerimento empReq: novoRequerimentoController.getRequerimentoSelecionado().getEmpreendimentoRequerimentoCollection()){
			if(empReq.getIdeEmpreendimentoRequerimento().equals(empreendReq.getIdeEmpreendimentoRequerimento())){
				if(!Util.isNullOuVazio(empReq.getNumVazaoTotal())) {
					if(empReq.getNumVazaoTotal().compareTo(this.numVazao) != 0) return true;
				}
			}
		}
				
		for(SolicitacaoAdministrativo prorrogacao : this.prorrogacoes){
			if(!this.prorrogacoesBkp.contains(prorrogacao)) return true;
		}
		
		for(SolicitacaoAdministrativo condicionante : this.condicionantes){
			if(!this.condicionantesBkp.contains(condicionante)) return true;
		}
		
		return false;
	}
	
	public PerguntaRequerimento getPerguntaNR_A2_P1() {
		return perguntaNRA2P1;
	}

	public void setPerguntaNR_A2_P1(PerguntaRequerimento perguntaNRA2P1) {
		this.perguntaNRA2P1 = perguntaNRA2P1;
	}

	public PerguntaRequerimento getPerguntaNR_A2_P2() {
		return perguntaNRA2P2;
	}

	public void setPerguntaNR_A2_P2(PerguntaRequerimento perguntaNRA2P2) {
		this.perguntaNRA2P2 = perguntaNRA2P2;
	}

	public PerguntaRequerimento getPerguntaNR_A2_P3() {
		return perguntaNRA2P3;
	}

	public void setPerguntaNR_A2_P3(PerguntaRequerimento perguntaNRA2P3) {
		this.perguntaNRA2P3 = perguntaNRA2P3;
	}

	public PerguntaRequerimento getPerguntaNR_A2_P4() {
		return perguntaNRA2P4;
	}

	public void setPerguntaNR_A2_P4(PerguntaRequerimento perguntaNRA2P4) {
		this.perguntaNRA2P4 = perguntaNRA2P4;
	}

	public PerguntaRequerimento getPerguntaNR_A2_P5() {
		return perguntaNRA2P5;
	}

	public void setPerguntaNR_A2_P5(PerguntaRequerimento perguntaNRA2P5) {
		this.perguntaNRA2P5 = perguntaNRA2P5;
	}

	public List<Outorga> getOutorgas() {
		return outorgas;
	}

	public void setOutorgas(List<Outorga> outorgas) {
		this.outorgas = outorgas;
	}

	public List<Licenca> getLicencas() {
		return licencas;
	}

	public void setLicencas(List<Licenca> licencas) {
		this.licencas = licencas;
	}

	public List<SolicitacaoAdministrativo> getCondicionantes() {
		return condicionantes;
	}

	public void setCondicionantes(List<SolicitacaoAdministrativo> condicionantes) {
		this.condicionantes = condicionantes;
	}

	public List<SolicitacaoAdministrativo> getProrrogacoes() {
		return prorrogacoes;
	}

	public void setProrrogacoes(List<SolicitacaoAdministrativo> prorrogacoes) {
		this.prorrogacoes = prorrogacoes;
	}

	public Outorga getOutorgaAExcluir() {
		return outorgaAExcluir;
	}

	public void setOutorgaAExcluir(Outorga outorgaAExcluir) {
		this.outorgaAExcluir = outorgaAExcluir;
	}

	public Licenca getLicencaAExcluir() {
		return licencaAExcluir;
	}

	public void setLicencaAExcluir(Licenca licencaAExcluir) {
		this.licencaAExcluir = licencaAExcluir;
	}

	public SolicitacaoAdministrativo getProrrogacaoAExcluir() {
		return prorrogacaoAExcluir;
	}

	public void setProrrogacaoAExcluir(SolicitacaoAdministrativo prorrogacaoAExcluir) {
		this.prorrogacaoAExcluir = prorrogacaoAExcluir;
	}

	public SolicitacaoAdministrativo getCondicionanteAExcluir() {
		return condicionanteAExcluir;
	}

	public void setCondicionanteAExcluir(SolicitacaoAdministrativo condicionanteAExcluir) {
		this.condicionanteAExcluir = condicionanteAExcluir;
	}

	public boolean isTemOutroUsoRecursoHidrico() {
		return temOutroUsoRecursoHidrico;
	}

	public void setTemOutroUsoRecursoHidrico(boolean temOutroUsoRecursoHidrico) {
		this.temOutroUsoRecursoHidrico = temOutroUsoRecursoHidrico;
	}

	public PerguntaRequerimento getPerguntaNR_A2_P6() {
		return perguntaNRA2P6;
	}

	public void setPerguntaNR_A2_P6(PerguntaRequerimento perguntaNRA2P6) {
		this.perguntaNRA2P6 = perguntaNRA2P6;
	}
}