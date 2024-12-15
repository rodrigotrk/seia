package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Segmento;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.SegmentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: SegmentoController.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.controller
 * @autor: diegoraian em 6 de set de 2017
 * Objetivo: 	
 */
@Named(value="segmentoController")
@ViewScoped
public class SegmentoController {
	
	/**
	 * Propriedade: filtroConsulta
	 * @type: Segmento
	 */
	private Segmento filtroConsulta;
	
	
	/**
	 * Propriedade: segmentoSelecionado
	 * @type: Segmento
	 */
	private Segmento segmentoSelecionado;
	
	
	/**
	 * Propriedade: novoSegmento
	 * @type: Segmento
	 */
	private Segmento novoSegmento;
	
	/**
	 * Propriedade: segmentoService
	 * @type: SegmentoService
	 */
	@EJB
	SegmentoService segmentoService;
	
	/**
	 * Propriedade: dataTableSegmentos
	 * @type: Collection<Segmento>
	 */
	private Collection<Segmento> dataTableSegmentos;
	
	
	@PostConstruct
	public void init(){
		this.filtroConsulta = new Segmento();
		this.novoSegmento = new Segmento();
	}

	/**
	 * Inclui um novo Segmento
	 * @author diegoraian
	 */
	public void incluir(){
		setNovoSegmento(new Segmento());
	}
	
	
	/**
	 * Edita um segmentos
	 * @param segmento
	 * @author diegoraian
	 */
	public void editar(Segmento segmento){
		setNovoSegmento(segmento);
		
	}
	/**
	 * Consultar segmentos
	 * @author diegoraian
	 */
	public void consultar(){
		
		try {
			dataTableSegmentos =  segmentoService.preencherDataList(getFiltroConsulta());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	
	private Boolean validarEntrada(Segmento segmento) throws Exception{
		if (Util.isNullOuVazio(segmento.getNomSegmento())) {
			throw new Exception(Util.getString("msg_003",Util.getString("lbl_segmento")));
		}
		return false;
	}
	/**
	 * Salvar segmento
	 * @author diegoraian
	 */
	public void salvarAtualizar(){
		try {
			validarEntrada(getNovoSegmento());
			segmentoService.validarExistencia(getNovoSegmento());
			if(Util.isNullOuVazio(getNovoSegmento().getIdeSegmento())){
				segmentoService.salvar(getNovoSegmento());
				JsfUtil.addSuccessMessage(Util.getString("geral_msg_inclusao_sucesso"));
			}else{
				segmentoService.atualizar(getNovoSegmento());
				JsfUtil.addSuccessMessage(Util.getString("geral_msg_alteracao_sucesso"));
			}
			RequestContext.getCurrentInstance().execute("dialogSegmento.hide()");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}	
		consultar();
	}

	/**
	 * Excluir o segmento
	 * @author diegoraian
	 */
	public void excluir(){
		try {
			segmentoService.excluir(getNovoSegmento());
			consultar();
			JsfUtil.addSuccessMessage(Util.getString("msg_exclusao_segmento"));
		} catch(Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	/**
	 * Getter do campo dataTableSegmentos
	 *	
	 * @return the dataTableSegmentos
	 */
	public Collection<Segmento> getDataTableSegmentos() {
		return dataTableSegmentos;
	}

	/**
	 * Setter do campo  dataTableSegmentos
	 * @param dataTableSegmentos the dataTableSegmentos to set
	 */
	public void setDataTableSegmentos(Collection<Segmento> dataTableSegmentos) {
		this.dataTableSegmentos = dataTableSegmentos;
	}


	/**
	 * Getter do campo segmentoSelecionado
	 *	
	 * @return the segmentoSelecionado
	 */
	public Segmento getSegmentoSelecionado() {
		return segmentoSelecionado;
	}

	/**
	 * Setter do campo  segmentoSelecionado
	 * @param segmentoSelecionado the segmentoSelecionado to set
	 */
	public void setSegmentoSelecionado(Segmento segmentoSelecionado) {
		this.segmentoSelecionado = segmentoSelecionado;
	}

	/**
	 * Getter do campo novoSegmento
	 *	
	 * @return the novoSegmento
	 */
	public Segmento getNovoSegmento() {
		return novoSegmento;
	}

	/**
	 * Setter do campo  novoSegmento
	 * @param novoSegmento the novoSegmento to set
	 */
	public void setNovoSegmento(Segmento novoSegmento) {
		this.novoSegmento = novoSegmento;
	}


	/**
	 * Getter do campo filtroConsulta
	 *	
	 * @return the filtroConsulta
	 */
	public Segmento getFiltroConsulta() {
		return filtroConsulta;
	}

	/**
	 * Setter do campo  filtroConsulta
	 * @param filtroConsulta the filtroConsulta to set
	 */
	public void setFiltroConsulta(Segmento filtroConsulta) {
		this.filtroConsulta = filtroConsulta;
	}
	
	
}
