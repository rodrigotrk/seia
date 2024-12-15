package br.gov.ba.seia.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.ProgramaGoverno;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ProgramaGovernoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoAtoAmbientalService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("abaProcessoController")
@ViewScoped
public class AbaProcessoController extends BaseAbaController{
	
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativaService;

	@EJB
	private SolicitacaoAdministrativoAtoAmbientalService solicitacaoAdministrativaAtoAmbientalService;
	
	@EJB
	private ProgramaGovernoService programaGovernoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@Inject
	private AbaRenovacaoAlteracaoProrrogacaoController abaRenovacaoAlteracaoProrrogacaoController;
	
	@Inject
	private AbaOutorgaController abaOutorgaController;
	
	@Inject
	private AbaFlorestalController abaFlorestalController;
	
	@Inject
	private AbaFaunaController abaFaunaController;
	
	@Inject
	private AbaLicencaAutorizacaoController abaLicencaAutorizacaoController;
	
	private PerguntaRequerimento perguntaNRA1P1;
	private PerguntaRequerimento perguntaNRA1P2;
	private PerguntaRequerimento perguntaNRA1P3;
	
	private List<ProcessoTramite> listProcessoTramite;
	private List<SolicitacaoAdministrativo> listaSolicitacaoAdministrativo;
	private List<ProgramaGoverno> listProgramaGoverno;
	
	private SolicitacaoAdministrativo solicitacaoAdministrativoAExcluir;
	
	private ProcessoTramite processoAExcluir;
	
	private ProgramaGoverno programaGovernoSelecionado;
	
	protected void carregarPerguntasAba() {
		try {

			this.perguntaNRA1P1 = this.carregarPerguntaByCod("NR_A1_P1");
			this.perguntaNRA1P2 = this.carregarPerguntaByCod("NR_A1_P2");
			this.perguntaNRA1P3 = this.carregarPerguntaByCod("NR_A1_P3");

			this.listaPerguntasRequerimento.add(perguntaNRA1P1);
			this.listaPerguntasRequerimento.add(perguntaNRA1P2);
			this.listaPerguntasRequerimento.add(perguntaNRA1P3);
			
			listProgramaGoverno = programaGovernoService.findAll();

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	protected void carregarRespostas() {
		try {
			
			super.carregarRespostasDasPerguntas();
			
			Requerimento requerimento = super.getRequerimento();
			
			this.listProcessoTramite = this.novoRequerimentoService.getListProcessosINEMA(requerimento);
			
			this.listaSolicitacaoAdministrativo = this.solicitacaoAdministrativaService.getListaTransferenciaTitularidadeByIdeRequerimento(requerimento);
			
			if(!Util.isNull(novoRequerimentoController.getEmpreendimentoRequerimento()) 
					&& !Util.isNull(novoRequerimentoController.getEmpreendimentoRequerimento().getProgramaGoverno())
					&& !Util.isNull(novoRequerimentoController.getEmpreendimentoRequerimento().getProgramaGoverno().getId())) {
				
				for (ProgramaGoverno pg : listProgramaGoverno) {
					if(pg.getId().equals(novoRequerimentoController.getEmpreendimentoRequerimento().getProgramaGoverno().getId())) {
						programaGovernoSelecionado = pg;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void adicionarOuAtualizarListaProcessoTramite(ProcessoTramite processoTramite) {
		if(this.listProcessoTramite.contains(processoTramite)){
			int posicao = this.listProcessoTramite.indexOf(processoTramite);
			this.listProcessoTramite.set(posicao, processoTramite);
		}else{
			this.listProcessoTramite.add(processoTramite);
		}	
	}
	
	public void excluirProcessoInema() {
		try {
			this.novoRequerimentoService.removerProcessoTramite(this.processoAExcluir);
			this.listProcessoTramite.remove(this.processoAExcluir);
			JsfUtil.addSuccessMessage("Processo excluído com sucesso");
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao excluir processo tramite. Info: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void adicionarOuAtualizarListaTransferencia(SolicitacaoAdministrativo solicitacaoTransferencia) {
		try {
			
			Integer ideTransferencia = solicitacaoTransferencia.getIdeSolicitacaoAdministrativo();
			solicitacaoTransferencia = this.solicitacaoAdministrativaService .carregarSolicitacaoTransferenciaTitularidadeById(ideTransferencia);
			if(this.listaSolicitacaoAdministrativo.contains(solicitacaoTransferencia)){
				int posicao = this.listaSolicitacaoAdministrativo.indexOf(solicitacaoTransferencia);
				this.listaSolicitacaoAdministrativo.set(posicao, solicitacaoTransferencia);
			}else{
				this.listaSolicitacaoAdministrativo.add(solicitacaoTransferencia);
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirTransferenciaDeTitularidade() {
		try {
			this.solicitacaoAdministrativaAtoAmbientalService.excluirSolicitacaoAdministrativaAtoAmbientalbySolicitacaoAdm(this.solicitacaoAdministrativoAExcluir);
			this.solicitacaoAdministrativaService.excluirSolicitacaoAdministrativa(this.solicitacaoAdministrativoAExcluir);
			this.listaSolicitacaoAdministrativo.remove(this.solicitacaoAdministrativoAExcluir);
			JsfUtil.addSuccessMessage("Transferência de titularidade excluida com sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao excluir transferência. Info: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}
	

	public void salvarAba(){
		try {
			
			if(validarAba()){
		
				this.salvar();
				
				JsfUtil.addSuccessMessage("Etapa 1 salva com sucesso.");
				
			}
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar aba processo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void limparOutrasAbas()  {
		abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P5().setIndResposta(true);
		abaRenovacaoAlteracaoProrrogacaoController.valueChangeDesmarcarTodasPerguntas(new ValueChangeEvent(new HtmlSelectBooleanCheckbox(), null, true));
		abaRenovacaoAlteracaoProrrogacaoController.salvarAba();
		
		abaLicencaAutorizacaoController.changePerguntaNRA3P1(new ValueChangeEvent(new HtmlSelectBooleanCheckbox(), null, false));
		abaLicencaAutorizacaoController.changePerguntaNRA3P12(new ValueChangeEvent(new HtmlSelectBooleanCheckbox(), null, false));
		abaLicencaAutorizacaoController.salvarAba();
		
		abaOutorgaController.valueChangePerguntaNRA4P1(new ValueChangeEvent(new HtmlSelectOneRadio(), null, false));
		abaOutorgaController.salvarAba();
		
		if(!Util.isNullOuVazio(abaFlorestalController.getPerguntaNR_A5_P1())){
			abaFlorestalController.getPerguntaNR_A5_P1().setIndResposta(false);
			abaFlorestalController.salvarAba();
		}else{
			abaFlorestalController.init();
			abaFlorestalController.carregarPerguntasAba();
			abaFlorestalController.getPerguntaNR_A5_P1().setIndResposta(false);
			abaFlorestalController.salvarAba();
		}
		
		abaFaunaController.valueChangePerguntaNRA6P1(new ValueChangeEvent(new HtmlSelectOneRadio(), null, false));
		abaFaunaController.salvarAba();
	}
	
	public boolean validarAba() {
		boolean valido = true;

		if (Util.isNullOuVazio(this.perguntaNRA1P1.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.");
			valido = false;
		} else if((this.perguntaNRA1P1.getIndResposta()) && (Util.isNullOuVazio(this.listProcessoTramite))) {
			JsfUtil.addWarnMessage("Por favor, inclua os dados do(s) processo(s) concluído(s) ou em trâmite.");
			valido = false;
		}
		
		if (Util.isNullOuVazio(this.perguntaNRA1P2.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 2.");
			valido = false;
		} else if ((this.perguntaNRA1P2.getIndResposta()) && (Util.isNullOuVazio(this.listaSolicitacaoAdministrativo))) {
			JsfUtil.addWarnMessage("Por favor, inclua dados do(s) processo(s) de transferência.");
			valido = false;
		}
		
		if (isCessionario() && Util.isNullOuVazio(this.perguntaNRA1P3.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 3.");
			valido = false;
		} else if(isCessionario() && this.perguntaNRA1P3.getIndResposta() && (Util.isNull(programaGovernoSelecionado))) {
			JsfUtil.addWarnMessage("Por favor, selecione o programa do governo na pergunta 3.1");
			valido = false;
		}
		
		return valido;
	}

	public void salvar()  {
		
		this.perguntaRequerimentoService.salvaListPerguntaRequerimento(super.listaPerguntasRequerimento,novoRequerimentoController.getRequerimentoSelecionado(), null, null, null);

		if (!this.perguntaNRA1P1.getIndResposta() && !Util.isNullOuVazio(listProcessoTramite)) {
			this.excluirProcessoEmTramite();
		}
		
		if (!this.perguntaNRA1P2.getIndResposta() && !Util.isNullOuVazio(listaSolicitacaoAdministrativo)) {
			this.excluirTransferenciasTitularidade();
		}else if(this.perguntaNRA1P2.getIndResposta()){
			limparOutrasAbas();
		}
		
		if (isCessionario() && !Util.isNull(this.perguntaNRA1P3.getIndResposta())) {
			
			EmpreendimentoRequerimento empReq = novoRequerimentoController.carregarEmpreendRequerimento();
			if(Util.isNull(programaGovernoSelecionado)) {
				empReq.setProgramaGoverno(null);
			} else {
				empReq.setProgramaGoverno(programaGovernoSelecionado);
				novoRequerimentoController.setEmpreendimentoRequerimento(empReq);
			}
			requerimentoService.atualizarEmpreendRequerimento(empReq);
					
		}
		
		Requerimento req= novoRequerimentoController.getRequerimentoSelecionado();
		if(!Util.isNullOuVazio(req)                              &&
				   !Util.isNullOuVazio(req.getIdeRequerimento()) && 
				   !Util.isNullOuVazio(req.getNumRequerimento())){
					
					TramitacaoRequerimento tramitacao = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(req.getIdeRequerimento());
					
					if(!Util.isNullOuVazio(tramitacao) &&
						tramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus()){
					
						this.tramitacaoRequerimentoService.tramitar(req, StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO, super.getOperador());
						
				}	
		}

	}
	
	private void excluirTransferenciasTitularidade()  {
		for (SolicitacaoAdministrativo solicAdm : listaSolicitacaoAdministrativo) {
			this.solicitacaoAdministrativaAtoAmbientalService.excluirSolicitacaoAdministrativaAtoAmbientalbySolicitacaoAdm(solicAdm);
			this.solicitacaoAdministrativaService.excluirSolicitacaoAdministrativa(solicAdm);
		}
		this.listaSolicitacaoAdministrativo.clear();
	}

	private void excluirProcessoEmTramite()  {
		for (ProcessoTramite obj : listProcessoTramite) {
			novoRequerimentoService.removerProcessoTramite(obj);
		}
		this.listProcessoTramite.clear();
	}

	public boolean isCessionario() {
		if(!Util.isNull(novoRequerimentoController.getEmpreendimento()) && !Util.isNull(novoRequerimentoController.getEmpreendimento().getIndCessionario())) {
			return novoRequerimentoController.getEmpreendimento().getIndCessionario();
		}
		
		return false;
	}
	
	public void valueChangePerguntaNRA1P3(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			programaGovernoSelecionado = null;
		}
	}
	
	@Override
	public Requerimento getRequerimento(){
		return this.novoRequerimentoController.getRequerimento();
	}
	
	public PerguntaRequerimento getPerguntaNR_A1_P1() {
		return perguntaNRA1P1;
	}

	public void setPerguntaNR_A1_P1(PerguntaRequerimento perguntaNRA1P1) {
		this.perguntaNRA1P1 = perguntaNRA1P1;
	}

	public PerguntaRequerimento getPerguntaNR_A1_P2() {
		return perguntaNRA1P2;
	}

	public void setPerguntaNR_A1_P2(PerguntaRequerimento perguntaNRA1P2) {
		this.perguntaNRA1P2 = perguntaNRA1P2;
	}

	public List<ProcessoTramite> getListProcessoTramite() {
		return listProcessoTramite;
	}

	public void setListProcessoTramite(List<ProcessoTramite> listProcessoTramite) {
		this.listProcessoTramite = listProcessoTramite;
	}

	public List<SolicitacaoAdministrativo> getListaSolicitacaoAdministrativo() {
		return listaSolicitacaoAdministrativo;
	}

	public void setListaSolicitacaoAdministrativo(List<SolicitacaoAdministrativo> listaSolicitacaoAdministrativo) {
		this.listaSolicitacaoAdministrativo = listaSolicitacaoAdministrativo;
	}

	public SolicitacaoAdministrativo getSolicitacaoAdministrativoAExcluir() {
		return solicitacaoAdministrativoAExcluir;
	}

	public void setSolicitacaoAdministrativoAExcluir(SolicitacaoAdministrativo solicitacaoAdministrativoAExcluir) {
		this.solicitacaoAdministrativoAExcluir = solicitacaoAdministrativoAExcluir;
	}

	public ProcessoTramite getProcessoAExcluir() {
		return processoAExcluir;
	}

	public void setProcessoAExcluir(ProcessoTramite processoAExcluir) {
		this.processoAExcluir = processoAExcluir;
	}

	public PerguntaRequerimento getPerguntaNR_A1_P3() {
		return perguntaNRA1P3;
	}

	public void setPerguntaNR_A1_P3(PerguntaRequerimento perguntaNRA1P3) {
		this.perguntaNRA1P3 = perguntaNRA1P3;
	}

	public List<ProgramaGoverno> getListProgramaGoverno() {
		return listProgramaGoverno;
	}

	public void setListProgramaGoverno(List<ProgramaGoverno> listProgramaGoverno) {
		this.listProgramaGoverno = listProgramaGoverno;
	}

	public ProgramaGoverno getProgramaGovernoSelecionado() {
		return programaGovernoSelecionado;
	}
	
	public void setProgramaGovernoSelecionado(ProgramaGoverno programaGovernoSelecionado) {
		this.programaGovernoSelecionado = programaGovernoSelecionado;
	}
}