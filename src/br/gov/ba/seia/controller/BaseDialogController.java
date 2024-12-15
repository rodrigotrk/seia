package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public abstract class BaseDialogController {

	protected Collection<PerguntaRequerimento> listaPerguntasRequerimento;
	
	protected boolean editMode;
	
	@Inject
	private NovoRequerimentoController novoRequerimentoController;

	@EJB
	NovoRequerimentoService novoRequerimentoService;

	@EJB
	PerguntaService perguntaService;

	@EJB
	PerguntaRequerimentoService perguntaRequerimentoService;

	public abstract <T> void editar(T objeto);

	public abstract void load();

	protected void carregarPerguntas(){}
	
	abstract void limpar();

	public abstract void salvar();

	abstract boolean validar();

	protected PerguntaRequerimento carregarPerguntaByCod(String codPergunta)  {
		Pergunta pergunta = this.perguntaService.carregarPerguntabyCodPergunta(codPergunta);
		return new PerguntaRequerimento(pergunta);
	}
	
	protected void carregarRespostas() {
		try {
			
			this.perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(this.listaPerguntasRequerimento, this.getRequerimento());

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Requerimento getRequerimento() {
		return this.novoRequerimentoController.getRequerimento();
	}

	public Collection<PerguntaRequerimento> getListaPerguntasRequerimento() {
		return listaPerguntasRequerimento;
	}

	public void setListaPerguntasRequerimento(Collection<PerguntaRequerimento> listaPerguntasRequerimento) {
		this.listaPerguntasRequerimento = listaPerguntasRequerimento;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public int getClassificacaoPonto(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}
	
	public int getClassificacaoShape(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}
}
