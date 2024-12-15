package br.gov.ba.seia.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("abaFlorestalController")
@ViewScoped
public class AbaFlorestalController extends BaseAbaController{

	private PerguntaRequerimento perguntaNRA5P1;
	
	private List<Florestal> florestais;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private PerguntaService perguntaServicee;

	@EJB
	private PerguntaRequerimentoService pRequerimentoService;
	
	@EJB
	private ImovelService imovelService;

	@EJB
	private FlorestalService florestalService;

	@EJB
	private AtoAmbientalService atoAmbientalService;

	private Florestal florestalExcluido;

	private List<Imovel> listaImovelFixa;

	@PostConstruct
	public void init(){
		
		try {
			super.init();
			
			Requerimento requerimento = this.novoRequerimentoController.getRequerimentoSelecionado();
			this.florestais = this.florestalService.carregarListaFlorestal(requerimento);
			
			listaImovelFixa = imovelService.filtrarListaImovelPorEmpreendimento(novoRequerimentoController.getEmpreendimento());
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void carregarPerguntasAba()  {
		this.perguntaNRA5P1 = this.carregarPerguntaByCod("NR_A5_P1");
		
		super.listaPerguntasRequerimento.add(this.perguntaNRA5P1);
	}
	@Override
	protected void carregarRespostas() {
		super.carregarRespostasDasPerguntas();
	}
	
	public void salvarAba(){
		if (this.validarAba()) {
			try{
				
				this.salvar();
				
				JsfUtil.addSuccessMessage("Etapa 5 salva com sucesso.");
				
			}catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			} 
			
		}
	}
	
	public boolean validarAba(){
		
		boolean valido = true;
		if (Util.isNullOuVazio(this.perguntaNRA5P1.getIndResposta())) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.1.");
			valido = false;
		} else if (this.perguntaNRA5P1.getIndResposta()) {
			if (Util.isNullOuVazio(this.florestais)) {
				JsfUtil.addWarnMessage("Por favor, inclua algum Ato Florestal.");
				valido = false;
			}
		}
		return valido;
		
	}
	
	public void salvar() {
		Requerimento requerimento = novoRequerimentoController.getRequerimentoSelecionado();
		this.pRequerimentoService.salvaListPerguntaRequerimento(super.listaPerguntasRequerimento,requerimento);
		
		Boolean resposta = this.perguntaNRA5P1.getIndResposta();
		if(!resposta){
			this.florestalService.removerByRequerimento(requerimento.getIdeRequerimento());
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

	public void excluirFlorestal() {
		try {
			florestalService.removerByIdeFlorestal(florestalExcluido.getIdeFlorestal());
			florestais.remove(florestalExcluido);
			JsfUtil.addSuccessMessage("Ato florestal excluido com sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao excluir o Ato Florestal" + exception);
		}
	}
	
	public void adicionarOuAtualizarAtoFlorestalNaLista(Florestal florestal) {
		if(this.florestais.contains(florestal)){
			int posicao = this.florestais.indexOf(florestal);
			this.florestais.set(posicao, florestal);
		}else{
			this.florestais.add(florestal);
		}	
	}
	
	public List<Florestal> getFlorestais() {
		return florestais;
	}

	public void setFlorestais(List<Florestal> florestais) {
		this.florestais = florestais;
	}

	public PerguntaRequerimento getPerguntaNR_A5_P1() {
		return perguntaNRA5P1;
	}

	public void setPerguntaNR_A5_P1(PerguntaRequerimento perguntaNR_A5_P1) {
		this.perguntaNRA5P1 = perguntaNR_A5_P1;
	}

	public Florestal getFlorestalExcluido() {
		return florestalExcluido;
	}

	public void setFlorestalExcluido(Florestal florestalExcluido) {
		this.florestalExcluido = florestalExcluido;
	}

	public boolean isTodosImoveisPreenchidos(){
		return this.florestais.size() >= this.listaImovelFixa.size();
	}

	/**
	 * @return the listaImovelFixa
	 */
	public List<Imovel> getListaImovelFixa() {
		return listaImovelFixa;
	}

	/**
	 * @param listaImovelFixa the listaImovelFixa to set
	 */
	public void setListaImovelFixa(List<Imovel> listaImovelFixa) {
		this.listaImovelFixa = listaImovelFixa;
	}
	
}