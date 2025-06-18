package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.facade.ReaberturaProcessoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.OrgaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("reaberturaProcessoController")
@ViewScoped
public class ReaberturaProcessoController extends SeiaControllerAb {
	
	@EJB
	private OrgaoService orgaoService;
	
	@EJB
	private AreaService areaService;
	
	@EJB
	private ReaberturaProcessoFacade reaberturaProcessoFacade;
	
	private VwConsultaProcesso vwProcesso;
	
	private Orgao orgao;
	private Area area;
	private String observacao;
	
	private Collection<Orgao> listaOrgaos;
	private Collection<Area> listaAreas;

	@PostConstruct
	public void init() {
		limparTela();
	}
	
	public void limparTela(){
		carregarListaOrgaos();
		orgao = new Orgao();
		area = new Area();
		observacao = StringUtils.EMPTY;
		listaAreas = new ArrayList<Area>();
	}
	
	private void carregarListaOrgaos() {
		try {
			Orgao org = new Orgao();
			org.setIndExcluido(false);
			listaOrgaos = orgaoService.filtrarListaOrgaos(org);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
        	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void mudaOrgao(ValueChangeEvent event) {	
		orgao = (Orgao) event.getNewValue();
		filtrarAreasPorOrgao();
	}
	
	public void mudaArea(ValueChangeEvent event) throws Exception {
		area = areaService.carregar((Area) event.getNewValue()) ;		
	}
	
	public void filtrarAreasPorOrgao() {
		try {
			
			if(!Util.isNullOuVazio(this.orgao)) listaAreas = areaService.filtrarAreasPorOrgao(this.orgao);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean validacaoCampos() {
		if(Util.isNullOuVazio(orgao)) {
			JsfUtil.addErrorMessage("Favor escolher o orgão destindo");
			return false;
			
		} else if(Util.isNullOuVazio(area)) {
			JsfUtil.addErrorMessage("Favor escolher a área destindo");
			return false;
			
		} else if(Util.isNullOuVazio(observacao)) {
			JsfUtil.addErrorMessage("Favor preencher a observação");
			return false;
			
		} else if(observacao.length() > 500) {
			JsfUtil.addWarnMessage("O campo 'Observação' permite no máximo 500 caracteres.");
			return false;
		}
		
		return true;
	}
	
	public void salvar() {
		
		try {
			if (validacaoCampos()) {
				
				reaberturaProcessoFacade.salvarReabertura(this);
				
				RequestContext.getCurrentInstance().execute("dialogReaberturaProcesso.hide();");
				
				JsfUtil.addSuccessMessage("Processo reaberto com sucesso");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/*
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */
	
	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}
	
	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public Orgao getOrgao() {
		return orgao;
	}
	
	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}

	public Collection<Orgao> getListaOrgaos() {
		return listaOrgaos;
	}
	
	public void setListaOrgaos(Collection<Orgao> listaOrgaos) {
		this.listaOrgaos = listaOrgaos;
	}
	
	public Collection<Area> getListaAreas() {
		return listaAreas;
	}

	public void setListaAreas(Collection<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}
	
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}