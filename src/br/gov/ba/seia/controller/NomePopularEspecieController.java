package br.gov.ba.seia.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.entity.EspecieFlorestal;
import br.gov.ba.seia.entity.EspecieSupressao;
import br.gov.ba.seia.entity.NomePopularEspecie;
import br.gov.ba.seia.facade.NomePopularEspecieServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

@Named("nomePopularEspecieController")
@ViewScoped
public class NomePopularEspecieController {

	@EJB
	private NomePopularEspecieServiceFacade nomePopularEspecieServiceFacade;
	
	private List<NomePopularEspecie> listaItemRemovidoNomePopularEspecie;
	
	private List<NomePopularEspecie> listarNomePopularEspecie;
	
	private NomePopularEspecie nomePopularEspecieSelecionado;
	
	private NomePopularEspecie nomePopularEspecie;
	
	private MetodoUtil metodoAtualizarExterno;
	
	private String idDoComponenteParaSerAtualizado;
	
	
	public void abrirNomePopular(EspecieSupressao especieSupressao) throws Exception{
		this.listarNomePopularEspecie = nomePopularEspecieServiceFacade.listarNomePopularEspecie(especieSupressao);
		
		//removerItem();
		
		if (!Util.isNullOuVazio(listarNomePopularEspecie)){
			Html.atualizar("formNomeVulgar");
			Html.executarJS("dlNomeVulgar.show()");
		} else {
			atualizarPagina();
		}
	}
	
	public void removerItem() {
		if (!Util.isNullOuVazio(listaItemRemovidoNomePopularEspecie)) {
			for (Iterator<NomePopularEspecie> it = listarNomePopularEspecie.iterator(); it.hasNext(); ) {
				NomePopularEspecie obj = (NomePopularEspecie) it.next();
				
				if (listaItemRemovidoNomePopularEspecie.contains(obj)){
					it.remove();
				}
			}
		}
	}
	
	public void abrirNomePopularEspecieFlorestal(EspecieFlorestal especieFlorestal) throws Exception{
		this.listarNomePopularEspecie = nomePopularEspecieServiceFacade.listarNomePopularEspecie(especieFlorestal);
		
		removerItem();
		
		if (!Util.isNullOuVazio(listarNomePopularEspecie)){
			Html.atualizar("formNomeVulgar");
			Html.executarJS("dlNomeVulgar.show()");
		} else {
			atualizarPagina();
		}
	}
	
	public void salvar() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		nomePopularEspecieSelecionado.setIdeNomePopularEspecie(nomePopularEspecie.getIdeNomePopularEspecie());
		nomePopularEspecieSelecionado.setNomPopularEspecie(nomePopularEspecie.getNomPopularEspecie());
		atualizarPagina();
		Html.executarJS("dlNomeVulgar.hide()");
	}
	
	public void atualizarPagina() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (!Util.isNull(metodoAtualizarExterno)){
			metodoAtualizarExterno.executeMethod();
		}
		
		if(!Util.isNull(idDoComponenteParaSerAtualizado)){
			Html.atualizar(idDoComponenteParaSerAtualizado);
//			Html.executarJS("atualizarGridTableEspecieSupressao();");
		}
	}

	public List<NomePopularEspecie> getListarNomePopularEspecie() {
		return listarNomePopularEspecie;
	}

	public void setListarNomePopularEspecie(
			List<NomePopularEspecie> listarNomePopularEspecie) {
		this.listarNomePopularEspecie = listarNomePopularEspecie;
	}

	public NomePopularEspecie getNomePopularEspecieSelecionado() {
		return nomePopularEspecieSelecionado;
	}

	public void setNomePopularEspecieSelecionado(
			NomePopularEspecie nomePopularEspecieSelecionado) {
		this.nomePopularEspecieSelecionado = nomePopularEspecieSelecionado;
	}

	public MetodoUtil getMetodoAtualizarExterno() {
		return metodoAtualizarExterno;
	}

	public void setMetodoAtualizarExterno(MetodoUtil metodoAtualizarExterno) {
		this.metodoAtualizarExterno = metodoAtualizarExterno;
	}


	public String getIdDoComponenteParaSerAtualizado() {
		return idDoComponenteParaSerAtualizado;
	}

	public void setIdDoComponenteParaSerAtualizado(
			String idDoComponenteParaSerAtualizado) {
		this.idDoComponenteParaSerAtualizado = idDoComponenteParaSerAtualizado;
	}

	public NomePopularEspecie getNomePopularEspecie() {
		return nomePopularEspecie;
	}

	public void setNomePopularEspecie(NomePopularEspecie nomePopularEspecie) {
		this.nomePopularEspecie = nomePopularEspecie;
	}

	public List<NomePopularEspecie> getListaItemRemovidoNomePopularEspecie() {
		return listaItemRemovidoNomePopularEspecie;
	}

	public void setListaItemRemovidoNomePopularEspecie(
			List<NomePopularEspecie> listaItemRemovidoNomePopularEspecie) {
		this.listaItemRemovidoNomePopularEspecie = listaItemRemovidoNomePopularEspecie;
	}
	
}
