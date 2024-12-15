package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

@Named("selecionarTipologiaController")
@ViewScoped
public class SelecionarTipologiaController {

	@EJB
	private TipologiaService tipologiaService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	private Collection<Tipologia> pais;

	private Tipologia pai;

	private TreeNode root;

	private TreeNode selectedNode;

	private Map<String, Object> parametros;

	@PostConstruct
	public void init() {

	}

	public void load(ActionEvent evt) {
		try {
			inicializarVariaveis(evt);
			this.pais = this.tipologiaService.carregarTipologiasPai();
			this.root = new DefaultTreeNode("RAIZ", null);
			Html.atualizar("formTipologia");
			Html.exibir("dlgTipologia");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void inicializarVariaveis(ActionEvent evt) {
		root = null;
		pai = null;
		selectedNode = null;
		parametros = evt.getComponent().getAttributes();
	}

	public void gerarArvore() {
		try {

			this.root = this.tipologiaService.montarArvoreLicenca(this.pai, false);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public Tipologia getTipologiaSelecionada() throws Exception {

		if (Util.isNull(this.selectedNode) || Util.isNull(this.selectedNode.getData())) {
			throw new Exception("A Atividade é de preenchimento obrigatório.");
		} else {
			if (!Util.isNullOuVazio(((Tipologia) this.selectedNode.getData()).getTipologiaCollection())) {
				throw new Exception("A Atividade escolhida é inválida.");
			}
		}

		return (Tipologia) this.selectedNode.getData();
	}

	public void adicionar() {
		try {
			validacao();
			metodoAdicionarTipologia((Tipologia) selectedNode.getData());
			Html.esconder("dlgTipologia");
		}
		catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void adicionarContinuar() {
		try {
			validacao();
			metodoAdicionarTipologia((Tipologia) selectedNode.getData());
		}
		catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void validacao() {
		if (!selectedNode.getChildren().isEmpty()) {
			throw new SeiaValidacaoRuntimeException("Não é possível adicionar uma tipologia pai.");
		}
	}

	private void metodoAdicionarTipologia(Tipologia tipologia) throws Exception {
		MetodoUtil metodoUtil = (MetodoUtil) parametros.get("metodoAdicionarTipologia");
		metodoUtil.executeMethod(tipologia);
	}

	public Collection<Tipologia> getPais() {
		return pais;
	}

	public void setPais(Collection<Tipologia> pais) {
		this.pais = pais;
	}

	public Tipologia getPai() {
		return pai;
	}

	public void setPai(Tipologia pai) {
		this.pai = pai;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

}
