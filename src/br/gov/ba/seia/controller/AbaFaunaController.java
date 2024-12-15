package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Fauna;
import br.gov.ba.seia.entity.ObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAtividadeFauna;
import br.gov.ba.seia.entity.TipoCriadouroFauna;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.FaunaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("abaFaunaController")
@ViewScoped
public class AbaFaunaController {
	
	@Inject
	private NovoRequerimentoController novoRequerimentoController;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private FaunaService faunaService;
	
	@EJB
	private PerguntaService perguntaService;
	
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;

	private PerguntaRequerimento perguntaNRA6P1;
	
	private Requerimento requerimento;
	
	private Fauna fauna;
	
	private List<PerguntaRequerimento> listaPerguntasRequerimento;
	
	private Collection<ObjetivoAtividadeManejo> objetivosAtividadeManejo;
	
	private Collection<TipoAtividadeFauna> tiposAtividadeFauna;
	
	private Collection<TipoCriadouroFauna> tiposCriadouroFauna;
	
	private boolean renderizaCriadouro;
	
	private boolean renderizaAtividade;

	@PostConstruct
	public void init() {
		try {

			listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();
			
			carregarPerguntasAba();
			carregarListas();
			
			requerimento = novoRequerimentoController.getRequerimentoSelecionado();
			
			carregarRespostas(requerimento);
			fauna = faunaService.getFaunaByIdeRequerimento(requerimento);
			
			if (!Util.isNullOuVazio(fauna)) {
				carregarFaunaDoRequerimento();
			} else {
				fauna = new Fauna(requerimento);
			}
			
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListas()  {
		objetivosAtividadeManejo = faunaService.carregaListaObjetivoAtividadeManejo();
		tiposCriadouroFauna = faunaService.carregaListaTipoCriadouroFauna();
		tiposAtividadeFauna = faunaService.carregaListaTipoAtividadeFauna();
	}
	
	private void carregarPerguntasAba()  {
		perguntaNRA6P1 = carregarPerguntaByCod("NR_A6_P1");
		
		listaPerguntasRequerimento.add(perguntaNRA6P1);
	}
	
	private PerguntaRequerimento carregarPerguntaByCod(String codPergunta) {
		Pergunta pergunta = perguntaService.carregarPerguntabyCodPergunta(codPergunta);
		return new PerguntaRequerimento(pergunta);
	}
	
	private void carregarRespostas(Requerimento requerimento) {
		try {
			perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(listaPerguntasRequerimento, requerimento);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarFaunaDoRequerimento() {
		fauna.setObjetivoAtividadeManejoCollection(faunaService.getListObjetivoAtividadeManejoByFauna(fauna));
		
		fauna.setTipoCriadouroFaunaCollection(faunaService.getListTipoCriadouroFaunaByFauna(fauna));
		
		if(!Util.isNullOuVazio(fauna.getTipoCriadouroFaunaCollection())) {
			renderizaCriadouro = true;
		} else {
			renderizaCriadouro = false;
		}
			
		fauna.setTipoAtividadeFaunaCollection(faunaService.getListTipoAtividadeFaunaByFauna(fauna));

		if(!Util.isNullOuVazio(fauna.getTipoAtividadeFaunaCollection())) {
			renderizaAtividade = true;
		} else {
			renderizaAtividade = false;
		}
	}
	
	public void valueChangePerguntaNRA6P1(ValueChangeEvent event) {
		fauna = new Fauna();
		renderizaAtividade = false;
		renderizaCriadouro = false;
		novoRequerimentoController.alteracaoResposta();
	}
	
	public void salvarAba(){
		try {
		
			if (validarAba()) {
				
				salvar();
				
				JsfUtil.addSuccessMessage("Etapa 6 salva com sucesso.");
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean validarAba(){
		boolean valido = true;
		
		if (perguntaNRA6P1.getIndResposta() == null) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.");
			valido = false;
			
		} else if (perguntaNRA6P1.getIndResposta()) {
			
			if (Util.isNullOuVazio(fauna.getObjetivoAtividadeManejoCollection())) {
				JsfUtil.addWarnMessage("Por favor, selecione pelo menos uma das opções do campo 1.1.");
				valido = false;
				
			} else {
				for (ObjetivoAtividadeManejo obj : fauna.getObjetivoAtividadeManejoCollection()) {
					
					if(obj.getIdeObjetivoAtividadeManejo() == 4 || obj.getIdeObjetivoAtividadeManejo() == 8 || obj.getIdeObjetivoAtividadeManejo() == 9) {
						
						if (Util.isNullOuVazio(fauna.getTipoCriadouroFaunaCollection())) {
							JsfUtil.addWarnMessage("Por favor, selecione pelo menos uma das opções do campo 1.2.");
							valido = false;
							break;
						}
						
					} else if(obj.getIdeObjetivoAtividadeManejo() == 1 || obj.getIdeObjetivoAtividadeManejo() == 2 || obj.getIdeObjetivoAtividadeManejo() == 3) {
						
						if (Util.isNullOuVazio(fauna.getTipoAtividadeFaunaCollection())) {
							JsfUtil.addWarnMessage("Por favor, selecione pelo menos uma das opções do campo 1.3.");
							valido = false;
							break;
						}
					}
				}
			}
		}
		
		return valido;
	}
	
	public void salvar() {
		
		perguntaRequerimentoService.salvaListPerguntaRequerimento(listaPerguntasRequerimento, requerimento);
		
		if(perguntaNRA6P1.getIndResposta() && fauna != null) {
			
			if(fauna.getIdeRequerimento() == null) {
				fauna.setIdeRequerimento(requerimento);
			}
			
			fauna.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_FAUNA.getId()));
			faunaService.salvarAtualizarFauna(fauna);
			
		} else if(fauna != null) {
			
			if(fauna.getIdeFauna() == null) {
				fauna = faunaService.getFaunaByIdeRequerimento(requerimento);
			}
			
			if(!Util.isNullOuVazio(fauna)) {
				faunaService.remover(fauna);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void valueChangeManejo(ValueChangeEvent event) {
		
		renderizaAtividade = false;
		renderizaCriadouro = false;
		
		List<ObjetivoAtividadeManejo> objetos = (List<ObjetivoAtividadeManejo>) event.getNewValue();
		
		if(!Util.isNullOuVazio(objetos)) {
			
			for (ObjetivoAtividadeManejo obj : objetos) {
				
				if(obj.getIdeObjetivoAtividadeManejo() == 1 || obj.getIdeObjetivoAtividadeManejo() == 2 || obj.getIdeObjetivoAtividadeManejo() == 3) {
					renderizaAtividade = true;
				} else if(obj.getIdeObjetivoAtividadeManejo() == 4 || obj.getIdeObjetivoAtividadeManejo() == 8 || obj.getIdeObjetivoAtividadeManejo() == 9) {
					renderizaCriadouro = true;
				}
			}
		}
		
		if(!renderizaAtividade) {
			fauna.setTipoAtividadeFaunaCollection(null);
		}
		
		if(!renderizaCriadouro) {
			fauna.setTipoCriadouroFaunaCollection(null);
		}
		novoRequerimentoController.alteracaoResposta();
	}
	
	/**
	 * 
	 * GET'S
	 * 
	 * AND
	 * 
	 * SET'S
	 * 
	 */

	public PerguntaRequerimento getPerguntaNR_A6_P1() {
		return perguntaNRA6P1;
	}

	public void setPerguntaNR_A6_P1(PerguntaRequerimento perguntaNRA6P1) {
		this.perguntaNRA6P1 = perguntaNRA6P1;
	}

	public Fauna getFauna() {
		return fauna;
	}

	public void setFauna(Fauna fauna) {
		this.fauna = fauna;
	}

	public List<PerguntaRequerimento> getListaPerguntasRequerimento() {
		return listaPerguntasRequerimento;
	}

	public void setListaPerguntasRequerimento(List<PerguntaRequerimento> listaPerguntasRequerimento) {
		this.listaPerguntasRequerimento = listaPerguntasRequerimento;
	}

	public Collection<ObjetivoAtividadeManejo> getObjetivosAtividadeManejo() {
		return objetivosAtividadeManejo;
	}

	public void setObjetivosAtividadeManejo(Collection<ObjetivoAtividadeManejo> objetivosAtividadeManejo) {
		this.objetivosAtividadeManejo = objetivosAtividadeManejo;
	}

	public Collection<TipoAtividadeFauna> getTiposAtividadeFauna() {
		return tiposAtividadeFauna;
	}

	public void setTiposAtividadeFauna(Collection<TipoAtividadeFauna> tiposAtividadeFauna) {
		this.tiposAtividadeFauna = tiposAtividadeFauna;
	}

	public Collection<TipoCriadouroFauna> getTiposCriadouroFauna() {
		return tiposCriadouroFauna;
	}

	public void setTiposCriadouroFauna(Collection<TipoCriadouroFauna> tiposCriadouroFauna) {
		this.tiposCriadouroFauna = tiposCriadouroFauna;
	}

	public boolean isRenderizaCriadouro() {
		return renderizaCriadouro;
	}

	public void setRenderizaCriadouro(boolean renderizaCriadouro) {
		this.renderizaCriadouro = renderizaCriadouro;
	}

	public boolean isRenderizaAtividade() {
		return renderizaAtividade;
	}

	public void setRenderizaAtividade(boolean renderizaAtividade) {
		this.renderizaAtividade = renderizaAtividade;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}
}