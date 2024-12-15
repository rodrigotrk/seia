package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ModalidadeOutorga;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovelPK;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public abstract class BaseDialogOutorgaController extends BaseDialogController{

	protected OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica;
	
	protected Outorga outorga;
	
	@Inject
	protected AbaOutorgaController abaOutorgaController;
	
	@Inject
	protected RenovarAlterarOutorgaController renovarAlterarOutorgaController;
	
	@Inject
	protected NovoRequerimentoController novoRequerimentoController;
	
	@EJB
	protected OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	private OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidadeService;
	
	@EJB
	protected OutorgaService outorgaService;

	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoFacade;

	protected Collection<Imovel> imoveis;

	private Collection<OutorgaLocalizacaoGeograficaImovelPK> listaOutorgaLocGeoImovelPKCadastradas;
	
	@Override
	void limpar() {
		this.outorga = new Outorga(super.getRequerimento());
		this.outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(this.outorga);
	}
	
	protected void carregarRespostas(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		try {
			this.perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(this.listaPerguntasRequerimento, this.getRequerimento(), outorgaLocalizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	protected void carregarImoveisParaOutorga() {
		this.imoveis = this.novoRequerimentoController.listarImovel();
		if(!novoRequerimentoController.isImovelUrbano() && !novoRequerimentoController.isCessionario() && !novoRequerimentoController.isConversaoTcraLac()){
			if(!Util.isNullOuVazio(this.imoveis) && this.imoveis.size() == 1)
				this.outorgaLocalizacaoGeografica.setListaImovel(new ArrayList<Imovel>(this.imoveis));
		}
		else{
			this.outorgaLocalizacaoGeografica.setListaImovel(this.novoRequerimentoController.getListaImovel());
		}
	}

	protected void carregarOutorgaLocalizacaoGeograficaImovel()  {
		this.carregarImoveis(this.outorgaLocalizacaoGeografica);
	}

	private void carregarImoveis(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		setListaOutorgaLocGeoImovelPKCadastradas(new ArrayList<OutorgaLocalizacaoGeograficaImovelPK>());
		for (OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel : outorgaLocalizacaoGeograficaService.buscarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica)) {
			outorgaLocalizacaoGeografica.getListaImovel().add(outorgaLocalizacaoGeograficaImovel.getIdeImovel());
			getListaOutorgaLocGeoImovelPKCadastradas().add(outorgaLocalizacaoGeograficaImovel.getOutorgaLocalizacaoGeograficaImovelPK());
		}
	}
	
	protected void carregarFinalidadesUsoAgua() {
		this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAgua().clear();
		this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAguaCaptacao().clear();
		List<OutorgaLocalizacaoGeograficaFinalidade> finalidades = 
				outorgaLocalizacaoGeograficaFinalidadeService.obterListaOutorgaLocalizacaoGeograficaFinalidades(this.outorgaLocalizacaoGeografica);
		for(OutorgaLocalizacaoGeograficaFinalidade geograficaFinalidade : finalidades) {
			
			TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua = geograficaFinalidade.getIdeTipoFinalidadeUsoAgua();
			tipoFinalidadeUsoAgua.setIndCaptacao(Util.isNullOuVazio(geograficaFinalidade.getIndCaptacao()) ? false : geograficaFinalidade.getIndCaptacao() );
			
			if(geograficaFinalidade.getIndCaptacao()){
				this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAguaCaptacao().add(tipoFinalidadeUsoAgua);
			}else{
				this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAgua().add(tipoFinalidadeUsoAgua);
			}
		}
	}
	
	protected OutorgaLocalizacaoGeografica carregarFinalidadesUsoAgua(OutorgaLocalizacaoGeografica olg) {
		olg.getListaFinalidadeUsoAgua().clear();
		olg.getListaFinalidadeUsoAguaCaptacao().clear();
		
		List<OutorgaLocalizacaoGeograficaFinalidade> finalidades = outorgaLocalizacaoGeograficaFinalidadeService.obterListaOutorgaLocalizacaoGeograficaFinalidades(olg);
		
		for(OutorgaLocalizacaoGeograficaFinalidade geograficaFinalidade : finalidades) {
			
			TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua = geograficaFinalidade.getIdeTipoFinalidadeUsoAgua();
			tipoFinalidadeUsoAgua.setIndCaptacao(Util.isNullOuVazio(geograficaFinalidade.getIndCaptacao()) ? false : geograficaFinalidade.getIndCaptacao() );
			
			if(geograficaFinalidade.getIndCaptacao()){
				olg.getListaFinalidadeUsoAguaCaptacao().add(tipoFinalidadeUsoAgua);
			}else{
				olg.getListaFinalidadeUsoAgua().add(tipoFinalidadeUsoAgua);
			}
		}
		
		return olg;
	}
	

	protected void salvarOutorga(ModalidadeOutorgaEnum modalidade) {
	
		if (Util.isNullOuVazio(this.outorga.getIdeModalidadeOutorga())) {
			this.outorga.setIdeModalidadeOutorga(new ModalidadeOutorga(modalidade.getIdModalidade()));
		}
		
		if (Util.isNullOuVazio(this.outorga.getIdeTipoSolicitacao())) {
			this.outorga.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_OUTORGA.getId()));
		}
		
		this.outorga.setValorVolumeCaminhaoPipa(this.abaOutorgaController.getValorVolumeCaminhaoPipa());
		
		this.outorga.setQtdTransporteCaminhaoPipa(this.abaOutorgaController.getQtdTransporteCaminhaoPipa());
		
		this.outorgaService.salvarAtualizarOutorga(this.outorga);
	}

	protected void salvarPerguntasRequerimento() {
		LocalizacaoGeografica ideLocalizacaoGeografica = this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica();
		Imovel imovel = this.outorgaLocalizacaoGeografica.getImovel();
		super.perguntaRequerimentoService.removerPerguntaReqByIdLocalizacaoGeografica(ideLocalizacaoGeografica);
		for (PerguntaRequerimento pergReq : super.listaPerguntasRequerimento) {
			pergReq.setIdeOutorgaLocalizacaoGeografica(this.outorgaLocalizacaoGeografica);
		}
		super.perguntaRequerimentoService.salvaListPerguntaRequerimento((List<PerguntaRequerimento>) super.listaPerguntasRequerimento, super.getRequerimento(),imovel,ideLocalizacaoGeografica,outorgaLocalizacaoGeografica);
	}
	
	public void salvarOutorgaLocalizacaoGeograficaFinalidade(OutorgaLocalizacaoGeografica outorgalocalizacaoGeografica){
		try{
			this.excluirOutorgaLocalizacaoGeograficaFinalidade(outorgalocalizacaoGeografica);
			if(!Util.isNullOuVazio(outorgalocalizacaoGeografica.getListaFinalidadeUsoAgua())){
				for(TipoFinalidadeUsoAgua agua : outorgalocalizacaoGeografica.getListaFinalidadeUsoAgua()){
					this.salvarOutorgaLocalizacaoGeograficaFinalidade(outorgalocalizacaoGeografica, agua);
				}
				
			}
			
			if(!Util.isNullOuVazio(outorgalocalizacaoGeografica.getListaFinalidadeUsoAguaCaptacao())){
				for(TipoFinalidadeUsoAgua agua : outorgalocalizacaoGeografica.getListaFinalidadeUsoAguaCaptacao()){
					this.salvarOutorgaLocalizacaoGeograficaFinalidade(outorgalocalizacaoGeografica, agua);
				}
				
			}
		}catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar a Outorga Localização Geográfica Finalidade Uso água");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirOutorgaLocalizacaoGeograficaFinalidade(OutorgaLocalizacaoGeografica localizacaoGeografica){
		try{
			List<OutorgaLocalizacaoGeograficaFinalidade> tempList = outorgaLocalizacaoGeograficaFinalidadeService.obterListaOutorgaLocalizacaoGeograficaFinalidades(localizacaoGeografica);
			if(!Util.isNullOuVazio(tempList)){
				for(OutorgaLocalizacaoGeograficaFinalidade geograficaFinalidade : tempList) {
					outorgaLocalizacaoGeograficaFinalidadeService.excluirOutorgaLocalizacaoGeograficaFinalidadesByOLG(geograficaFinalidade.getIdeOutorgaLocalizacaoGeografica());
				}
			}
		} catch (Exception e){
			JsfUtil.addErrorMessage("Ocorreu um erro ao excluir a finalidade" + e.getMessage() );
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarOutorgaLocalizacaoGeograficaFinalidade(OutorgaLocalizacaoGeografica localizacaoGeografica, TipoFinalidadeUsoAgua agua){
		try {
			OutorgaLocalizacaoGeograficaFinalidade outorgaLocalizacaoGeograficaFinalidade = new OutorgaLocalizacaoGeograficaFinalidade(localizacaoGeografica, agua);
			outorgaLocalizacaoGeograficaFinalidade.setIdeOutorgaLocalizacaoGeografica(localizacaoGeografica);
			outorgaLocalizacaoGeograficaFinalidade.setIdeTipoFinalidadeUsoAgua(agua);
			outorgaLocalizacaoGeograficaFinalidade.setIndCaptacao(agua.getIndCaptacao());
			this.outorgaLocalizacaoGeograficaFinalidadeService.salvarOutorgaLocGeoFinalidade(outorgaLocalizacaoGeograficaFinalidade);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	public void salvarOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		try {
			OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel = new OutorgaLocalizacaoGeograficaImovel();
			OutorgaLocalizacaoGeograficaImovelPK outorgaLocalizacaoGeograficaImovelPK = new OutorgaLocalizacaoGeograficaImovelPK();
			for (Imovel imovel : this.outorgaLocalizacaoGeografica.getListaImovel()) {
				outorgaLocalizacaoGeograficaImovelPK.setIdeImovel(imovel.getIdeImovel());
				outorgaLocalizacaoGeograficaImovelPK.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
				outorgaLocalizacaoGeograficaImovel.setIdeImovel(imovel);
				outorgaLocalizacaoGeograficaImovel.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
				outorgaLocalizacaoGeograficaImovel.setOutorgaLocalizacaoGeograficaImovelPK(outorgaLocalizacaoGeograficaImovelPK);
				boolean registroNovo = true;
				for(OutorgaLocalizacaoGeograficaImovelPK outorgaPK:getListaOutorgaLocGeoImovelPKCadastradas()) {
					if((outorgaPK.getIdeImovel() == outorgaLocalizacaoGeograficaImovelPK.getIdeImovel()) && 
							(outorgaPK.getIdeOutorgaLocalizacaoGeografica() == outorgaLocalizacaoGeograficaImovelPK.getIdeOutorgaLocalizacaoGeografica())) {
						registroNovo = false;
					}
				}
				if(registroNovo) {
					this.outorgaLocalizacaoGeograficaService.salvarAtualizarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaImovel);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public boolean isAfter2009(){
		if(Util.isNullOuVazio(outorgaLocalizacaoGeografica)) return false;
		return validarData(this.outorgaLocalizacaoGeografica.getDtcPerfuracaoPoco());
	} 
	
	protected boolean validarData(Date data) {
		if(Util.isNull(data)) return false;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2009,8,8); // 08/09/2009
		return novoRequerimentoController.after(data, calendar.getTime());
	}

	public boolean isTheGeomPersistido(LocalizacaoGeografica localizacaoGeografica) {
		return abaOutorgaController.isTheGeomPersistido(localizacaoGeografica);
	}

	public OutorgaLocalizacaoGeografica getOutorgaLocalizacaoGeografica() {
		return outorgaLocalizacaoGeografica;
	}

	public void setOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
	}

	public Outorga getOutorga() {
		return outorga;
	}

	public void setOutorga(Outorga outorga) {
		this.outorga = outorga;
	}

	public AbaOutorgaController getAbaOutorgaController() {
		return abaOutorgaController;
	}

	public void setAbaOutorgaController(AbaOutorgaController abaOutorgaController) {
		this.abaOutorgaController = abaOutorgaController;
	}

	public boolean isTodosImoveisCadastrados(Collection<OutorgaLocalizacaoGeografica> lista) {
		this.removerImoveisCadastrados(lista);
		return this.novoRequerimentoController.getListaImovel().isEmpty();
	}

	public void removerImoveisCadastrados(Collection<OutorgaLocalizacaoGeografica> lista) {
		this.imoveis = this.novoRequerimentoController.listarImovel();
		List<Imovel> listaImovel = new ArrayList<Imovel>(this.novoRequerimentoController.getListaImovel());
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : lista) {
			this.carregarImoveis(outorgaLocalizacaoGeografica);
			listaImovel.removeAll(outorgaLocalizacaoGeografica.getListaImovel());
		}
		this.imoveis = listaImovel;
	}

	public Collection<Imovel> getImoveis() {
		return imoveis;
	}

	public void setImoveis(Collection<Imovel> imoveis) {
		this.imoveis = imoveis;
	}

	public Collection<OutorgaLocalizacaoGeograficaImovelPK> getListaOutorgaLocGeoImovelPKCadastradas() {
		return listaOutorgaLocGeoImovelPKCadastradas;
	}

	public void setListaOutorgaLocGeoImovelPKCadastradas(Collection<OutorgaLocalizacaoGeograficaImovelPK> listaOutorgaLocGeoImovelPKCadastradas) {
		this.listaOutorgaLocGeoImovelPKCadastradas = listaOutorgaLocGeoImovelPKCadastradas;
	}
	
	
	
}
