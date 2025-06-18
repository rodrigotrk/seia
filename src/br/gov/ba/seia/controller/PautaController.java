package br.gov.ba.seia.controller;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.OperacaoProcessoEnum;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public abstract class PautaController extends FiltroConsultaProcesso {
	
	@EJB
	protected VwConsultaProcessoService vwProcessoService;
	@EJB
	protected PautaService pautaService;
	
	protected static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	protected LazyDataModel<VwConsultaProcesso> dataModelProcessos;
	
	protected OperacaoProcessoEnum operacaoProcessoEnum;
	
	public abstract void consultarProcesso();
	
	protected boolean validate() {
		if(!Util.validaPeriodo(getPeriodoInicio(), getPeriodoFim())) {
			JsfUtil.addErrorMessage(BUNDLE.getString("geral_msg_periodo_invalido"));
			return false;
		}
		return true;
	}
	
	@Override
	protected void limpar() {
		super.limpar();
		dataModelProcessos = null;
	}
	
	protected void consultar() throws Exception {
		
		Integer idePessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
		final Pauta pauta = pautaService.obtemPautaPorIdeFuncionario(idePessoaFisica); 
		
		dataModelProcessos = new LazyDataModel<VwConsultaProcesso>() {
			
			private static final long serialVersionUID = -549249300009769836L;
			
			@Override
			public List<VwConsultaProcesso> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				try {
					return (List<VwConsultaProcesso>) vwProcessoService.listarPorCriteriaDemanda(getParametros(), operacaoProcessoEnum, pauta.getIdePauta(), first, pageSize);
				} 
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		};
		
		dataModelProcessos.setRowCount(getRowCountProcesso(pauta));
	}
	
	protected int getRowCountProcesso(Pauta pauta) throws Exception {
		return vwProcessoService.consultarProcessosCount(getParametros(), operacaoProcessoEnum, pauta.getIdePauta());
	}

	public LazyDataModel<VwConsultaProcesso> getDataModelProcessos() {
		return dataModelProcessos;
	}

	public void setDataModelProcessos(LazyDataModel<VwConsultaProcesso> dataModelProcessos) {
		this.dataModelProcessos = dataModelProcessos;
	}
}
