package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public abstract class BaseAbaController {
	
	List<PerguntaRequerimento> listaPerguntasRequerimento;
	
	@Inject
	NovoRequerimentoController novoRequerimentoController;
	
	@EJB
	NovoRequerimentoService novoRequerimentoService;

	@EJB
	PerguntaService perguntaService;
	
	@EJB
	 PerguntaRequerimentoService perguntaRequerimentoService;
	
	@PostConstruct
	public void init(){
		this.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();
		this.carregarPerguntasAba();
		this.carregarRespostas();
	}
	
	protected abstract void carregarPerguntasAba() ;
	
	protected PerguntaRequerimento carregarPerguntaByCod(String codPergunta) {
		Pergunta pergunta = this.perguntaService.carregarPerguntabyCodPergunta(codPergunta);
		return new PerguntaRequerimento(pergunta);
	}
	
	protected abstract void carregarRespostas();

	protected void carregarRespostasDasPerguntas() {
		try {
			
			this.perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(this.listaPerguntasRequerimento, this.getRequerimento());

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public abstract void salvarAba();
	
	public abstract boolean validarAba();
	
	public abstract void salvar();
	
	public Requerimento getRequerimento(){
		return this.novoRequerimentoController.getRequerimento();
	}
	
	public List<PerguntaRequerimento> getListaPerguntasRequerimento() {
		return listaPerguntasRequerimento;
	}

	public void setListaPerguntasRequerimento(List<PerguntaRequerimento> listaPerguntasRequerimento) {
		this.listaPerguntasRequerimento = listaPerguntasRequerimento;
	}

	protected Pessoa getOperador() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}
}
