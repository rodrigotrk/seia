package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.facade.FormarEquipeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("matrizController")
@ViewScoped
public class MatrizController extends SeiaControllerAb {
	

	private ControleTramitacao controleTramitacao;
	private Date dataInicial;
	private Date dataFinal;
	private Area area;
	private boolean isAreaSelecionada;

	private UIForm formularioASerLimpo;

	@EJB
	private FormarEquipeServiceFacade formarEquipeService;
	@EJB
	private AreaService areaService;

	public StreamedContent getGerarRelatorioMatriz() {
				
		try {
			Area a = areaService.carregar(this.area);
			
			if(Util.isNull(a)) {
				throw new RuntimeException("Não foi possível carregar a área.");
			}
		
			Map<String, Object> lParametros = new HashMap<String, Object>();
			lParametros.put("IDE_STATUS_FLUXO", StatusFluxoEnum.ANALISE_TECNICA.getStatus());
			lParametros.put("IDE_AREA", a.getIdeArea());
			lParametros.put("NOM_AREA", a.getNomArea());
			
			RelatorioUtil lRelatorio = new RelatorioUtil("relatorioMatriz.jrxml", lParametros);
			
			return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
		
	}
	
	public String validarFormulario() {
		if (!Util.isNull(this.area) && !Util.isNull(this.area.getIdeArea())) {
			RequestContext.getCurrentInstance().addCallbackParam("erroMatriz", false);
			
		} else {
			JsfUtil.addErrorMessage("O campo Coordenação é de preenchimento obrigatório.");
			RequestContext.getCurrentInstance().addCallbackParam("erroMatriz", true);
			
		}
		return null;
	}

	public void novoControleTramitacao() {

		setControleTramitacao(new ControleTramitacao());
		setDataInicial(null);
		setDataFinal(null);

		limparTela();
	}

	public void verificarArea() {
		if(!Util.isNullOuVazio(this.area)){
			isAreaSelecionada = true;
		}else {
			isAreaSelecionada = false;
		}
	}
	
	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}

	public Collection<StatusFluxo> getColStatusFluxo() {

		return formarEquipeService.filtrarListaStatusFluxo(new StatusFluxo());
	}

	public Collection<Area> getColAreas() {

    	return formarEquipeService.filtrarListaAreas(new Area());
    }

	public ControleTramitacao getControleTramitacao() {

		if (Util.isNullOuVazio(controleTramitacao)) controleTramitacao = new ControleTramitacao();
		return controleTramitacao;
	}
	public void setControleTramitacao(ControleTramitacao controleTramitacao) {
		this.controleTramitacao = controleTramitacao;
	}

	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public boolean isAreaSelecionada() {
		return isAreaSelecionada;
	}

	public void setAreaSelecionada(boolean isAreaSelecionada) {
		this.isAreaSelecionada = isAreaSelecionada;
	}
	
}