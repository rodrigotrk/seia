package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.facade.FormarEquipeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("visualizarEquipeController")
@ViewScoped
public class VisualizarEquipeController {
	
	@EJB
	private FormarEquipeServiceFacade formarEquipeServiceFacade;
	
	private Collection<Equipe> listaEquipe;
	
	public void load(VwConsultaProcesso vwProcesso) {
		if(!Util.isNull(vwProcesso)) {
			try {
				listaEquipe = formarEquipeServiceFacade.listarEquipe(vwProcesso.getIdeProcesso());
			}
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		RequestContext.getCurrentInstance().addPartialUpdateTarget(":formVisualizarEquipe");
	}

	public Collection<Equipe> getListaEquipe() {
		return listaEquipe;
	}

	public void setListaEquipe(Collection<Equipe> listaEquipe) {
		this.listaEquipe = listaEquipe;
	}
}
