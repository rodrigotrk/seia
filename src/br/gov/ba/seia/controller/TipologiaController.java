package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("tipologiaController")
@ViewScoped
public class TipologiaController {

	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	private Collection<Tipologia> pais;
	
	private Tipologia pai;
	
	private TreeNode root;
	
	private TreeNode selectedNode;
	
	private boolean autorizacao;
	
	@PostConstruct
	public void init() {
		
	}

	public void load(boolean autorizacao) {
		try {
			this.pai = null;
			this.autorizacao = autorizacao;
			
			if(autorizacao) {
				this.pais = this.tipologiaService.carregarTipologiasPaiAutorizacao();
				this.root = new DefaultTreeNode("RAIZ", null);

			} else {
				this.pais = this.tipologiaService.carregarTipologiasPai();
				this.root = new DefaultTreeNode("RAIZ", null);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
        	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void gerarArvore(){
		try {
			
			if(!Util.isNullOuVazio(this.pai)) {
				this.root = this.tipologiaService.montarArvoreLicenca(this.pai, this.autorizacao);
			}else {
				this.root = null;
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Tipologia getTipologiaSelecionada() throws Exception {
		
		if(Util.isNull(this.selectedNode) || Util.isNull(this.selectedNode.getData())){
			throw new Exception("A Atividade é de preenchimento obrigatório.");
		}else{
			if(!Util.isNullOuVazio(((Tipologia) this.selectedNode.getData()).getTipologiaCollection())){
				throw new Exception("A Atividade escolhida é inválida.");	
			}
		}
		
		return (Tipologia) this.selectedNode.getData();
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

	public boolean isAutorizacao() {
		return autorizacao;
	}

	public void setAutorizacao(boolean autorizacao) {
		this.autorizacao = autorizacao;
	}

	
	
}
