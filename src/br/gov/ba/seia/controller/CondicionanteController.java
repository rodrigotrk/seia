/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.controller
 * @autor: diegoraian em 6 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CondicionanteAmbiental;
import br.gov.ba.seia.entity.CondicionanteAtoAmbiental;
import br.gov.ba.seia.entity.Segmento;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CondicionanteService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteController.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.controller
 * @autor: diegoraian em 6 de set de 2017
 * Objetivo: 	
	
 */
@Named("condicionanteController")
@ViewScoped
public class CondicionanteController {
	/**
	 * Propriedade: filtroConsulta
	 * @type: CondicionanteAmbiental
	 */
	private CondicionanteAmbiental filtroConsulta;
	
	/**
	 * Propriedade: itemSelecionado
	 * @type: CondicionanteAmbiental
	 */
	private CondicionanteAmbiental itemSelecionado;

	
	/**
	 * Propriedade: segmentos
	 * @type: List<Segmento>
	 */
	private List<Segmento> segmentos;
	
	/**
	 * Propriedade: atosAmbientais
	 * @type: List<AtoAmbiental>
	 */
	private List<AtoAmbiental> atosAmbientais;
	
	
	/**
	 * Propriedade: dualListAtosAmbientais
	 * @type: DualListModel<AtoAmbiental>
	 */
	protected DualListModel<AtoAmbiental> dualListAtosAmbientais;
	
	
	/**
	 * Propriedade: dataTableCondicionantes
	 * @type: Collection<CondicionanteAmbiental>
	 */
	private List<CondicionanteAmbiental> dataTableCondicionantes;
	
	/**
	 * Propriedade: detalhar
	 * @type: Boolean
	 */
	private Boolean detalhar = Boolean.FALSE;
	/**
	 * Propriedade: condicionanteService
	 * @type: CondicionanteService
	 */
	@EJB
	CondicionanteService condicionanteService;
	
	@PostConstruct
	public void init(){
		preencheCombosConsulta();
		this.filtroConsulta = new CondicionanteAmbiental();
		this.itemSelecionado = new CondicionanteAmbiental();
		setFiltroConsulta(new CondicionanteAmbiental());
	}
	
	
	/**
	 *
	 * @author diegoraian
	 */
	private void preencheCombosConsulta() {
		//Combo de segmentos
		try {
			setSegmentos(condicionanteService.listarTodosSegmentos());
			
		} catch (Exception e) {
			setSegmentos(new ArrayList<Segmento>());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		//Combo de atos ambientais
		try {
			setAtosAmbientais(condicionanteService.listarAtosAmbientais());
			
		} catch (Exception e) {
			setAtosAmbientais(new ArrayList<AtoAmbiental>());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		//Instancia dualli de atos ambientais
		try {
			setDualListAtosAmbientais(
					new DualListModel<AtoAmbiental>(
							new ArrayList<AtoAmbiental>(), new ArrayList<AtoAmbiental>()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}


	/**
	 *
	 * @author diegoraian
	 */
	public void consultar(){
		try {
			setDataTableCondicionantes(condicionanteService.listarTodosCondicionantes(getFiltroConsulta()));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 *
	 * @author diegoraian
	 */
	public void incluir(){
		this.detalhar = Boolean.FALSE;
		setItemSelecionado(new CondicionanteAmbiental());
		try {
			dualListAtosAmbientais.setSource(condicionanteService.listarAtosAmbientais());
			dualListAtosAmbientais.setTarget(new ArrayList<AtoAmbiental>());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	
	/**
	 *
	 * @author diegoraian
	 */
	public void salvarAtualizar(){
		try {
			if(validarObrigatorios()){
				condicionanteService.verificarRegistroExistente(getItemSelecionado());
				if(Util.isNullOuVazio(getItemSelecionado().getIdeCondicionante())){
					condicionanteService.salvar(getItemSelecionado(),getDualListAtosAmbientais().getTarget());
					JsfUtil.addSuccessMessage(Util.getString("geral_msg_inclusao_sucesso"));
				} else {
					
					condicionanteService.atualizar(getItemSelecionado(),getDualListAtosAmbientais().getTarget());
					JsfUtil.addSuccessMessage(Util.getString("geral_msg_alteracao_sucesso"));
				}
			}
			RequestContext.getCurrentInstance().execute("dialogCondicionante.hide()");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		consultar();
	}
	/**
	 *
	 * @author diegoraian
	 */
	private Boolean validarObrigatorios() {
		if(Util.isNullOuVazio(getItemSelecionado().getNomCondicionante())){
			JsfUtil.addErrorMessage("O nome da condicionante é obrigatório");
			return false;
		}else if(Util.isNullOuVazio(getItemSelecionado().getIdeSegmento())){
			JsfUtil.addErrorMessage("O item segmento é obrigatório");
			return false;
		}
		return true;
	}


	/**
	 *
	 * @param condicionante
	 * @author diegoraian
	 */
	public void editar(CondicionanteAmbiental condicionante){
		this.detalhar = Boolean.FALSE;
		try {
			try {
				dualListAtosAmbientais.setSource(condicionanteService.listarAtosAmbientais());
				dualListAtosAmbientais.setTarget(new ArrayList<AtoAmbiental>());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			List<AtoAmbiental>  source = getDualListAtosAmbientais().getSource();
			List<AtoAmbiental>  target = getDualListAtosAmbientais().getTarget();
			CondicionanteAmbiental condicionanteAux = condicionanteService.carregarCondicionante(condicionante);
			for (CondicionanteAtoAmbiental condicionantes : condicionanteAux.getCondicionanteAtoAmbientalCollection()) {
				AtoAmbiental atoDoCondicionante =  condicionantes.getIdeAtoAmbiental();
				if (source.contains(atoDoCondicionante)) {
					source.remove(atoDoCondicionante);
					target.add(atoDoCondicionante);
				}
			}
			setItemSelecionado(condicionanteAux);	
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 *
	 * @author diegoraian
	 */
	public void excluir(){
		try{
			condicionanteService.excluir(getItemSelecionado());
			consultar();
			JsfUtil.addSuccessMessage(Util.getString("msg_exclusao_condicionante"));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void visualizar(CondicionanteAmbiental condicionante){
		editar(condicionante);
		this.detalhar = Boolean.TRUE;
	}

	/**
	 * Getter do campo filtroConsulta
	 *	
	 * @return the filtroConsulta
	 */
	public CondicionanteAmbiental getFiltroConsulta() {
		return filtroConsulta;
	}

	/**
	 * Setter do campo  filtroConsulta
	 * @param filtroConsulta the filtroConsulta to set
	 */
	public void setFiltroConsulta(CondicionanteAmbiental filtroConsulta) {
		this.filtroConsulta = filtroConsulta;
	}

	/**
	 * Getter do campo itemSelecionado
	 *	
	 * @return the itemSelecionado
	 */
	public CondicionanteAmbiental getItemSelecionado() {
		return itemSelecionado;
	}

	/**
	 * Setter do campo  itemSelecionado
	 * @param itemSelecionado the itemSelecionado to set
	 */
	public void setItemSelecionado(CondicionanteAmbiental itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}
	/**
	 * Getter do campo segmentos
	 *	
	 * @return the segmentos
	 */
	public List<Segmento> getSegmentos() {
		return segmentos;
	}
	/**
	 * Setter do campo  segmentos
	 * @param segmentos the segmentos to set
	 */
	public void setSegmentos(List<Segmento> segmentos) {
		this.segmentos = segmentos;
	}
	/**
	 * Getter do campo atosAmbientais
	 *	
	 * @return the atosAmbientais
	 */
	public List<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;
	}
	/**
	 * Setter do campo  atosAmbientais
	 * @param atosAmbientais the atosAmbientais to set
	 */
	public void setAtosAmbientais(List<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}
	
	/**
	 * Getter do campo dataTableCondicionantes
	 *	
	 * @return the dataTableCondicionantes
	 */
	public List<CondicionanteAmbiental> getDataTableCondicionantes() {
		return dataTableCondicionantes;
	}
	/**
	 * Setter do campo  dataTableCondicionantes
	 * @param dataTableCondicionantes the dataTableCondicionantes to set
	 */
	public void setDataTableCondicionantes(List<CondicionanteAmbiental> dataTableCondicionantes) {
		this.dataTableCondicionantes = dataTableCondicionantes;
	}
	/**
	 * Getter do campo dualListAtosAmbientais
	 *	
	 * @return the dualListAtosAmbientais
	 */
	public DualListModel<AtoAmbiental> getDualListAtosAmbientais() {
		return dualListAtosAmbientais;
	}
	/**
	 * Setter do campo  dualListAtosAmbientais
	 * @param dualListAtosAmbientais the dualListAtosAmbientais to set
	 */
	public void setDualListAtosAmbientais(DualListModel<AtoAmbiental> dualListAtosAmbientais) {
		this.dualListAtosAmbientais = dualListAtosAmbientais;
	}


	/**
	 * Getter do campo detalhar
	 *	
	 * @return the detalhar
	 */
	public Boolean getDetalhar() {
		return detalhar;
	}


	/**
	 * Setter do campo  detalhar
	 * @param detalhar the detalhar to set
	 */
	public void setDetalhar(Boolean detalhar) {
		this.detalhar = detalhar;
	}
	
}
