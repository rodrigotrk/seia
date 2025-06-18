package br.gov.ba.seia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.ProcessoSinaflor;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.middleware.sinaflor.facade.SinaflorServiceFacade;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("sinaflorController")
@ViewScoped
public class SinaflorController  {
	
	@EJB
	private SinaflorServiceFacade sinaflorServiceFacade;
	
	private String numProcesso;
	private ProcessoSinaflor processoSinaflor;
	private LazyDataModel<ProcessoSinaflor> listaProcessoSinaflor;
	

	public void teste() {
		sinaflorServiceFacade.jobSinaflor();
	}
	
	public void limpar() {
		numProcesso=null;
		listaProcessoSinaflor=null;
	}
	
	public void visualizarDetalhes(ProcessoSinaflor processoSinaflor) {
		this.processoSinaflor=processoSinaflor;
		Html.atualizar("frmDetalhe:pnlDetalhe");
		Html.exibir("dlgDetalhe");
	}
	
	public void sincronizar() {
		try {
			Integer ideProcesso = processoSinaflor.getIdeProcesso().getIdeProcesso();
			VwConsultaProcesso processoSEIA = sinaflorServiceFacade.buscarVwConsultaProcessoPorIdeProcesso(ideProcesso);
			boolean sincronizarCadastro = sinaflorServiceFacade.sincronizarCadastro(processoSEIA);
			
			consultar();
			
			Html.esconder("dlgDetalhe");
			
			if(sincronizarCadastro) {
				MensagemUtil.sucesso("Sincronização realizada com sucesso.");
			} else {
				MensagemUtil.erro("Falha na sincronização, verificar o log para maiores detalhes.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void consultar() {
		try {
			final HashMap<String, Object> params = getParams();
			
			listaProcessoSinaflor = new LazyDataModel<ProcessoSinaflor>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public List<ProcessoSinaflor> load(int arg0, int arg1, String arg2, SortOrder arg3, Map<String, String> arg4) {
					try {
						return sinaflorServiceFacade.listarProcessoSinaflor(params, arg0, arg1);
					} catch (Exception e) {
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
				}
			};
			
			listaProcessoSinaflor.setRowCount(sinaflorServiceFacade.listarProcessoSinaflorCount(params));
			
			Html.atualizar("frmConsultaSinaflor:pnlDataTable");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private HashMap<String, Object> getParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		if(!Util.isNullOuVazio(numProcesso)) {
			params.put("numProcesso", numProcesso);
		}
		
		return params;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/

	public LazyDataModel<ProcessoSinaflor> getListaProcessoSinaflor() {
		return listaProcessoSinaflor;
	}

	public void setListaProcessoSinaflor(LazyDataModel<ProcessoSinaflor> listaProcessoSinaflor) {
		this.listaProcessoSinaflor = listaProcessoSinaflor;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public ProcessoSinaflor getProcessoSinaflor() {
		return processoSinaflor;
	}

	public void setProcessoSinaflor(ProcessoSinaflor processoSinaflor) {
		this.processoSinaflor = processoSinaflor;
	}
}
