package br.gov.ba.seia.controller;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ComunicacaoRequerenteInterface;
import br.gov.ba.seia.interfaces.TramitacaoInterface;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.facade.ComunicacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("comunicacaoRequerenteController")
@ViewScoped
public class ComunicacaoRequerenteController {

	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	@EJB
	private ComunicacaoReenquadramentoProcessoServiceFacade comunicacaoReenquadramentoProcessoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	
	private Requerimento requerimento;
	private ComunicacaoRequerimento comunicacaoRequerimento;
	
	private LazyDataModel<ComunicacaoRequerenteInterface> comunicacoes;
	
	private LazyDataModel<TramitacaoInterface> tramitacoes;
	
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;

	public void carregarTela(Requerimento requerimento) {
		try {
			Integer ideRequerimento = requerimento.getIdeRequerimento();
			this.requerimento = requerimento = requerimentoService.carregarDadosBasicos(ideRequerimento);
			carregarComunicacao(ideRequerimento);
			carregarTramitacao(requerimento);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO){
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
		carregarTela(processoReenquadramentoDTO.getRequerimento());
	}
	
	public boolean isReenquadramento() {
		return !Util.isNull(processoReenquadramentoDTO);
	}

	private void carregarTramitacao(final Requerimento requerimento) throws Exception {
		tramitacoes  = new LazyDataModel<TramitacaoInterface>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<TramitacaoInterface> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<TramitacaoInterface> listaTramitacao;
				try {
					if (isReenquadramento()){
						listaTramitacao = tramitacaoReenquadramentoProcessoServiceFacade.listarTramitacaoReenquadramentoProcessoPorProcessoReenquadramento(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento(), first, pageSize);
					} else {
						listaTramitacao = (List<TramitacaoInterface>) tramitacaoRequerimentoService.listarTramitacaoRequerimentoPorRequerimento(requerimento, first, pageSize);
					}
					return listaTramitacao;
				}
				catch (Exception e) {
					Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
			
		};
		if (isReenquadramento()){
			tramitacoes.setRowCount(countTramitacaoReenquadramentoProcesso(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento()));
		} else {
			tramitacoes.setRowCount(countTramitacao(requerimento));
		}
	}

	private Integer countTramitacao(final Requerimento requerimento) throws Exception {
		return tramitacaoRequerimentoService.listarTramitacaoRequerimentoPorRequerimentoCount(requerimento);
	}
	
	private Integer countTramitacaoReenquadramentoProcesso(Integer ideProcessoReenquadramento) {
		return tramitacaoReenquadramentoProcessoServiceFacade.listarTramitacaoReenquadramentoProcessoPorProcessoReenquadramentoCount(ideProcessoReenquadramento);
	}

	private void carregarComunicacao(final Integer ideRequerimento) {
		if(!Util.isNull(ideRequerimento)) {
			comunicacoes = new LazyDataModel<ComunicacaoRequerenteInterface>() {
				private static final long serialVersionUID = 1L;
				@Override
				public List<ComunicacaoRequerenteInterface> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					try{
						List<ComunicacaoRequerenteInterface> listaComunicacao = null;
						
						if (isReenquadramento()){
							listaComunicacao = (List<ComunicacaoRequerenteInterface>) comunicacaoReenquadramentoProcessoService.carregarComunicacaoDemanda(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento(), first, pageSize);
							
						} else {
							listaComunicacao = (List<ComunicacaoRequerenteInterface>) comunicacaoRequerimentoService.carregarComunicacaoDemanda(ideRequerimento,first, pageSize);
						}	
						return listaComunicacao;
					} 
					catch (Exception e) {
						Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
						JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação.");
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
				}
			};
			if (isReenquadramento()){
				comunicacoes.setRowCount(countComunicacaoReenquadramentoProcesso(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento()));
			} else {
				comunicacoes.setRowCount(countComunicacao(ideRequerimento));
			}
		}
	}

	private Integer countComunicacao(Integer ideRequerimento) {
		return comunicacaoRequerimentoService.carregarComunicacaoCount(ideRequerimento);
	}
	
	private Integer countComunicacaoReenquadramentoProcesso(Integer ideProcessoReenquadramento) {
		return comunicacaoReenquadramentoProcessoService.carregarComunicacaoDemandaCount(ideProcessoReenquadramento);
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public ComunicacaoRequerimento getComunicacaoRequrerimento() {
		return comunicacaoRequerimento;
	}

	public void setComunicacaoRequrerimento(ComunicacaoRequerimento comunicacaoRequrerimento) {
		comunicacaoRequerimento = comunicacaoRequrerimento;
	}

	public LazyDataModel<ComunicacaoRequerenteInterface> getComunicacoes() {
		return comunicacoes;
	}

	public void setComunicacoes(LazyDataModel<ComunicacaoRequerenteInterface> comunicacoes) {
		this.comunicacoes = comunicacoes;
	}

	/**
	 * @return the tramitacoes
	 */
	public LazyDataModel<TramitacaoInterface> getTramitacoes() {
		return tramitacoes;
	}

	/**
	 * @param tramitacoes the tramitacoes to set
	 */
	public void setTramitacoes(LazyDataModel<TramitacaoInterface> tramitacoes) {
		this.tramitacoes = tramitacoes;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(
			ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}

}
