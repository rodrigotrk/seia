package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.CerhFinalidadeFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public abstract class CerhFinalidadeController extends CerhAbaCaracterizacaoController {

	@EJB
	private CerhFinalidadeFacade facade;
	
	private Collection<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaCollection;
	
	public abstract CerhCaracterizacaoFinalidadeInterface getCerhCaracterizacaoFinalidade();
	public abstract TipologiaEnum getTipologiaEnum();
	
	public abstract boolean isFinalidadeSelecionada();
	public abstract boolean isFinalidadeNecessitaInformacoesDeUso(CerhFinalidadeUsoAguaInterface cerhFinalidadeUsoAguaInterface);
	public abstract boolean validarCerhCaraceterizacaoFinalidade();
	
	public abstract void obterCerhFinalidade(CerhFinalidadeUsoAguaInterface finalidadeUsoAgua) throws Exception;
	public abstract void prepararCerhFinalidadeUsoAgua(CerhFinalidadeUsoAguaInterface cerhFinalidadeUsoAguaInterface);

	@Override
	protected void prepararDialog(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		super.prepararDialog(caracterizacaoDTO, visualizacao);
		listarTipoFinalidadeUsoAgua();
		if(!Util.isNullOuVazio(super.cerhCaracterizacao)){
			if(Util.isNull(super.dto.getCaracterizacaoDTO().getTipoFinalidadeUsoAguaCollection())){
				super.dto.getCaracterizacaoDTO().setTipoFinalidadeUsoAguaCollection(new ArrayList<TipoFinalidadeUsoAgua>());
			}
			if(getCerhCaracterizacaoFinalidade().getFinalidadeCollection()!=null){
				for (CerhFinalidadeUsoAguaInterface finalidadeUsoAgua : getCerhCaracterizacaoFinalidade().getFinalidadeCollection()) {
					prepararCerhFinalidadeUsoAgua(finalidadeUsoAgua);
					super.dto.getCaracterizacaoDTO().getTipoFinalidadeUsoAguaCollection().add(finalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua());
				}
			}
		}
	}
	
	protected void listarTipoFinalidadeUsoAgua() {
		try {
			if(Util.isNullOuVazio(tipoFinalidadeUsoAguaCollection)){
				tipoFinalidadeUsoAguaCollection = getFacade().listarTipoFinalidadeUsoAgua(getTipologiaEnum());
			}
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_finalidade") + "s.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	protected CerhFinalidadeUsoAguaInterface getCerhFinalidade(TipoFinalidadeUsoAguaEnum tipoFinalidadeUsoAguaEnum) {
		if(!Util.isNullOuVazio(getCerhCaracterizacaoFinalidade().getFinalidadeCollection())) {
			for(CerhFinalidadeUsoAguaInterface cerhFinalidade : getCerhCaracterizacaoFinalidade().getFinalidadeCollection()) {
				if(cerhFinalidade.getIdeTipoFinalidadeUsoAgua().equals(new TipoFinalidadeUsoAgua(tipoFinalidadeUsoAguaEnum.getId()))) {
					return cerhFinalidade;
				}
			}
		}
		return null;
	}
	
	public boolean validarFinalidades(){
		if(!isFinalidadeSelecionada()) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade"));
			return false;
		} 
		else if(isFinalidadeNecessitaInformacoesDeUso() && !validarCerhCaraceterizacaoFinalidade()){
			return false;
		}
		return true;
	}
	
	public boolean isFinalidadeNecessitaInformacoesDeUso() {
		return !Util.isNullOuVazio(getCerhCaracterizacaoFinalidade().getFinalidadeCollection()) 
				&& isFinalidadeNecessitaInformacoesDeUso(getCerhCaracterizacaoFinalidade().getFinalidadeCollection());
	}
	
	private boolean isFinalidadeNecessitaInformacoesDeUso(Collection<CerhFinalidadeUsoAguaInterface> collection) {
		for (CerhFinalidadeUsoAguaInterface cerhFinalidade : collection) {
			if(isFinalidadeNecessitaInformacoesDeUso(cerhFinalidade)) {
				return true;
			}
		}
		return false;
	}
	
	public void excluirFinalidadeUsoAgua(CerhFinalidadeUsoAguaInterface cerhFinalidadeUsoAguaInterface) throws Exception{
		facade.excluirCaracterizacaoFinalidade(cerhFinalidadeUsoAguaInterface);
	}
	
	public void excluirDadosFinalidade(CerhDadosFinalidadeInterface dadosFinalidadeInterface) throws Exception{
		facade.excluirDadoFinalidade(dadosFinalidadeInterface);
	}

	@Override
	public CerhFinalidadeFacade getFacade(){
		return facade; 
	}
	
	public CerhFinalidadeUsoAguaInterface getCerhFinalidadeOutrosUsos() {
		return getCerhFinalidade(TipoFinalidadeUsoAguaEnum.OUTROS);
	}
	
	public Collection<TipoFinalidadeUsoAgua> getTipoFinalidadeUsoAguaCollection() {
		return tipoFinalidadeUsoAguaCollection;
	}
	
}
