package br.gov.ba.seia.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.dto.CerhHistoricoDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.facade.CerhHistoricoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("cerhHistoricoController")
@ViewScoped
public class CerhHistoricoController {

	@EJB
	private CerhHistoricoServiceFacade cerhHistoricoServiceFacade;

	@Inject
	private CerhExibirHistoricoController historicoController;
	
	private Cerh cerhPai;
	private CerhHistoricoDTO historico;
	private LazyDataModel<Cerh> listaHistorico;
	
	private Cerh cerhAnterior;
	private Cerh cerhAtual;
	
	@PostConstruct
	private void load() {
		try {
			inicializarVariaveis();
			carregarHistorico();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
	public void setarCerhParaHistorico(Cerh cerh){
		
		try{
			cerhAtual = cerh;
			cerhAnterior = cerhHistoricoServiceFacade.getCerhAnterior(cerh);
			historico =  historicoController.prepararHistorico(cerhAnterior, cerh);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		Html.getCurrency().update("formHistorico").show("historico");
		
	}
	
	public void carregarHistorico() throws Exception {

		listaHistorico = new LazyDataModel<Cerh>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Cerh> load(int first, int pageSize, String arg2, SortOrder arg3, Map<String, String> arg4) {
				try {
					return cerhHistoricoServiceFacade.listarHistorico(cerhPai, first, pageSize);
				}
				catch (Exception e) {
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		};
		listaHistorico.setRowCount(cerhHistoricoServiceFacade.listarHistoricoCount(cerhPai));
	}

	private void inicializarVariaveis()  {
		this.cerhPai = new Cerh(Integer.valueOf(Html.recuperarParametro("ideCerh")));
	}

	public LazyDataModel<Cerh> getListaHistorico() {
		return listaHistorico;
	}

	public void setListaHistorico(LazyDataModel<Cerh> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	public CerhHistoricoDTO getHistorico() {
		return historico;
	}

	public void setHistorico(CerhHistoricoDTO historico) {
		this.historico = historico;
	}

	public Boolean getDisableCheckBox() {
		return (cerhAtual != null && cerhAnterior != null);
	}

	public Cerh getCerhAnterior() {
		return cerhAnterior;
	}

	public void setCerhAnterior(Cerh cerhAnterior) {
		this.cerhAnterior = cerhAnterior;
	}

	public Cerh getCerhAtual() {
		return cerhAtual;
	}

	public void setCerhAtual(Cerh cerhAtual) {
		this.cerhAtual = cerhAtual;
	}
	
	public int getAbaAtiva(){
		return 0;
	}
}