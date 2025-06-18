package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.AcaoFluxo;
import br.gov.ba.seia.entity.ControleFluxo;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("acaoFluxoController")
@ViewScoped
public class AcaoFluxoController {

	private String paginaDialog;
	
	private Integer numeroPagina;
	
	private Collection<AcaoFluxo> listaAcaoFluxoDisponivel;
	private AcaoFluxo acaoFluxo;
	
	private boolean acaoFluxoAlternativo;
	private boolean acaoFluxoReencaminhar;
	private boolean acaoFluxoNotificar;
	private boolean acaoFluxoAprovarNotificacao;
	private boolean renderizarDialog;
	
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;

	@PostConstruct
	public void init(){
	}
	
	public void verificarAcoes(VwConsultaProcesso vwConsultaProcesso) {
		try {
			List<ControleFluxo> list = vwConsultaProcessoService.getListaControleFuxoDoProcesso(vwConsultaProcesso);
			if(!list.isEmpty()){
				for(ControleFluxo cf : list){
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 1){
						vwConsultaProcesso.setPodeEncaminhar(true);
					}
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 2){
						vwConsultaProcesso.setPodeNotificar(true);
					}
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 3){
						vwConsultaProcesso.setPodeAprovarNotificacao(true);
					}
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 4){
						vwConsultaProcesso.setPodeApensarDocumento(true);
					}
				}
			}
		} catch (Exception e) {
			  	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Boolean podeEncaminhar(VwConsultaProcesso vwConsultaProcesso){
		try {
			List<ControleFluxo> list = vwConsultaProcessoService.getListaControleFuxoDoProcesso(vwConsultaProcesso);
			if(!list.isEmpty()){
				for(ControleFluxo cf : list){
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 1){
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return false;
	}
	
	public Boolean podeNotificar(VwConsultaProcesso vwConsultaProcesso){
		try {
			List<ControleFluxo> list = vwConsultaProcessoService.getListaControleFuxoDoProcesso(vwConsultaProcesso);
			if(!list.isEmpty()){
				for(ControleFluxo cf : list){
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 2){
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		return false;
	}
	
	public Boolean podeAprovarNotificacao(VwConsultaProcesso vwConsultaProcesso){
		try {
			List<ControleFluxo> list = vwConsultaProcessoService.getListaControleFuxoDoProcesso(vwConsultaProcesso);
			if(!list.isEmpty()){
				for(ControleFluxo cf : list){
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 3){
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		return false;
	}
	
	public Boolean podeApensarDocumento(VwConsultaProcesso vwConsultaProcesso){
		try {
			List<ControleFluxo> list = vwConsultaProcessoService.getListaControleFuxoDoProcesso(vwConsultaProcesso);
			if(!list.isEmpty()){
				for(ControleFluxo cf : list){
					if(cf.getIdeAcaoFluxo().getIdeAcaoFluxo() == 4){
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		return false;
	}
	
	public void mudaAcao(ValueChangeEvent event) {
		this.acaoFluxo = (AcaoFluxo) event.getNewValue();
	}

	public boolean isAcaoFluxoAlternativo() {
		return acaoFluxoAlternativo;
	}

	public void setAcaoFluxoAlternativo(boolean acaoFluxoAlternativo) {
		this.acaoFluxoAlternativo = acaoFluxoAlternativo;
	}

	public boolean isAcaoFluxoReencaminhar() {
		return acaoFluxoReencaminhar;
	}

	public void setAcaoFluxoReencaminhar(boolean acaoFluxoReencaminhar) {
		this.acaoFluxoReencaminhar = acaoFluxoReencaminhar;
	}

	public boolean isAcaoFluxoNotificar() {
		return acaoFluxoNotificar;
	}

	public void setAcaoFluxoNotificar(boolean acaoFluxoNotificar) {
		this.acaoFluxoNotificar = acaoFluxoNotificar;
	}

	public boolean isAcaoFluxoAprovarNotificacao() {
		return acaoFluxoAprovarNotificacao;
	}

	public void setAcaoFluxoAprovarNotificacao(boolean acaoFluxoAprovarNotificacao) {
		this.acaoFluxoAprovarNotificacao = acaoFluxoAprovarNotificacao;
	}

	public Collection<AcaoFluxo> getListaAcaoFluxoDisponivel() {
		return listaAcaoFluxoDisponivel;
	}

	public void setListaAcaoFluxoDisponivel(Collection<AcaoFluxo> listaAcaoFluxoDisponivel) {
		this.listaAcaoFluxoDisponivel = listaAcaoFluxoDisponivel;
	}

	public AcaoFluxo getAcaoFluxo() {
		return acaoFluxo;
	}

	public void setAcaoFluxo(AcaoFluxo acaoFluxo) {
		this.acaoFluxo = acaoFluxo;
	}

	public boolean isRenderizarDialog() {
		return renderizarDialog;
	}
	public void setRenderizarDialog(boolean renderizarDialog) {
		this.renderizarDialog = renderizarDialog;
	}

	public String getPaginaDialog() {
		return paginaDialog;
	}

	public void setPaginaDialog(String paginaDialog) {
		this.paginaDialog = paginaDialog;
	}

	public Integer getNumeroPagina() {
		return numeroPagina;
	}

	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}
}