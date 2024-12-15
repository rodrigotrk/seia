package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
public class SelecionarAtoAmbientalService {
	
	private Tipologia tipologiaEmpreendimentoSelecionada;
	
	@EJB
	private EmpreendimentoService empreendimentoService;

	public void adicionaGrupoTipologia(boolean fecharDialog,TreeNode root,TreeNode selectedNode,
			Collection<Tipologia> listaTipologiaEmpreendimento) {
		try {
			if (!Util.isNullOuVazio(selectedNode)) {
				Tipologia lTipologiaSelecionada = (Tipologia) selectedNode.getData();
				if (lTipologiaSelecionada.getIndPossuiFilhos()) {
					JsfUtil.addWarnMessage("Esta Tipologia não é uma Atividade.");
				} else {
					tipologiaEmpreendimentoSelecionada = 		lTipologiaSelecionada;
					 
					if(listaTipologiaEmpreendimento ==null) {
						listaTipologiaEmpreendimento = new ArrayList();
					}
					listaTipologiaEmpreendimento.add(lTipologiaSelecionada);
						
					
					
				//	novaTipologia();
					JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
					if (fecharDialog) {
						RequestContext.getCurrentInstance().execute("dlgTipologiaEmpreendimento.hide();");
					}
				}
			} 
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public TreeNode montarArvoreTipologia(Tipologia tipologiaSelecionada) {
		try {
			return  empreendimentoService.montarArvoreTipologia(tipologiaSelecionada);
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	} 
	
	public Tipologia getTipologiaEmpreendimentoSelecionada() {
		return tipologiaEmpreendimentoSelecionada;
	}

	public void setTipologiaEmpreendimentoSelecionada(Tipologia tipologiaEmpreendimentoSelecionada) {
		this.tipologiaEmpreendimentoSelecionada = tipologiaEmpreendimentoSelecionada;
	}
	
}