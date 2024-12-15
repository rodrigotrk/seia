package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.abstracts.BaseCerhCaptacaoCaracterizacao;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhNaturezaPoco;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.CerhAbaCaptacaoSubterraneaFacade;
import br.gov.ba.seia.facade.CerhAbasCaptacoesFacade;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public class CerhAbaCaptacaoSubterraneaController extends BaseCerhCaptacaoCaracterizacao {

	@EJB
	private CerhAbaCaptacaoSubterraneaFacade facade;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@Inject
	private LocalizacaoGeograficaGenericController localizacaoGeograficaController;
	
	private Collection<CerhNaturezaPoco> cerhNaturezaPocoCollection;
	
	public CerhAbaCaptacaoSubterraneaController() {
		
	}

	@Override
	public void prepararDialogIncluirCaracterizacao(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception   {
		super.prepararDialog(caracterizacaoDTO, visualizacao);
		
		listarNaturezaPoco();
		
		if(!Util.isNull(super.getCerhCaracterizacao()) ) {
			
			if(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso()!=null &&
				super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao()!=null){
				super.getCerhCaracterizacao().setData(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao());
			}
		}
		
		
	}
	
	
	@Override
	public void prepararDialogIncluirCaracterizacaoHistorico(CerhCaracterizacaoDTO caracterizacaoDTO,
			boolean visualizacao) throws Exception {
		
		
		super.prepararDialogHistorico(caracterizacaoDTO, visualizacao);
		
		listarNaturezaPoco();
		
		if(!Util.isNull(super.getCerhCaracterizacao()) ) {
			
			if(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso()!=null &&
				super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao()!=null){
				super.getCerhCaracterizacao().setData(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao());
			}
		}
		
		
	}
	
	private void listarNaturezaPoco() {
		try {
			if(Util.isNullOuVazio(cerhNaturezaPocoCollection)) {
				cerhNaturezaPocoCollection = facade.listarCerhNaturezaPoco();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_cap_sub_natureza_poco") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}

	/*
	 * ABSTRACT
	 */
	@Override
	public CerhAbasCaptacoesFacade getFacade() {
		return facade;
	}

	@Override
	public boolean validarCaracterizacao() {
		boolean valido = true;

		/*
		
		 * */
		
		
		if(!super.validarDataInicioCaptacao()) {
			valido = false;
		}

		if(Util.isNull(getCerhCaracterizacao().getIdeCerhSituacaoTipoUso())){
			MensagemUtil.msg0003("A situação da regularização");
			valido = false;
		}
		
		if(Util.isNull(getCerhCaracterizacao().getIdeCerhNaturezaPoco())) {
			MensagemUtil.msg0003("A Natureza do poço");
			valido = false;
		}
		
		if(super.isUsuarioExterno() && Util.isNullOuVazio(super.getCerhCaracterizacao().getValProfundidade())){
			valido = false;
		}
		
		if(Util.isNull(getCerhCaracterizacao().getIdeCerhNaturezaPoco())) {
			MensagemUtil.msg0003("A Natureza do poço");
			valido = false;
		}
			
		
		if(Util.isNullOuVazio(getCerhCaracterizacao().getValProfundidade())){
			MensagemUtil.msg0003("A " + Util.getString("cerh_aba_cap_sub_profundidade"));
			valido = false;
		}
		
		if(super.isUsuarioExterno() && Util.isNullOuVazio(super.getCerhCaracterizacao().getValVazaoTeste())){
			MensagemUtil.msg0003("A " + Util.getString("cerh_aba_cap_sub_vazao_teste"));
			valido = false;
		}
		
		if(super.isUsuarioExterno() && Util.isNullOuVazio(getCerhCaracterizacao().getValNivelEstatico())){
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_cap_sub_nivel_estatico"));
			valido = false;
		}

		if(super.isUsuarioExterno() && Util.isNullOuVazio(getCerhCaracterizacao().getValNivelDinamico())){
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_cap_sub_nivel_dinamico"));
			valido = false;
		}
		
		if(!super.validarDialog()) {
			valido = false;
		}

		return valido;
	}
	
	public void loadDTO(CerhDTO cerhDTO){
		super.load(cerhDTO);
		
		if(Util.isNullOuVazio(super.getDto().getListaCaracterizacaoDTO())){
			List<LocalizacaoGeografica> listlocGeo = new ArrayList<LocalizacaoGeografica>();
			
			for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
				listlocGeo.addAll(localizacaoGeograficaService.listarLocalizacaoGeograficaPorProcessoLancamentoEfluenteOuCapSubterranea(cerhProcesso.getNumProcesso()));
			}
			
			for(LocalizacaoGeografica loc : listlocGeo){
				
				for(DadoGeografico dado : loc.getDadoGeograficoCollection()){
					loc.setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
					loc.setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
				}
				
				if(!Util.isNullOuVazio(loc.getLatitudeInicial()) && !Util.isNullOuVazio(loc.getLongitudeInicial())){
				
					for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
						cerhProcesso.getIdeCerhProcesso();
						dto.getListaCaracterizacaoDTO().add(new CerhCaracterizacaoDTO(new CerhLocalizacaoGeografica(loc),cerhProcesso));
					}
				}
			}
		}
	}

	
	@Override
	public void fecharDialogCaracterizacao() {
		Html.esconder("dlgCaracterizacaoCapSubterranea");
	}
	
	@Override
	public void fecharDialogDadosMineracao() {
		Html.esconder("dialogIncluirDadosMineracaoSubterranea");
	}

	@Override
	public void fecharDialogDadosAbsIndustrial() {
		Html.esconder("dialogIncluirDadosAbsIndustrialSubterranea");
	}

	@Override
	public CerhCaptacaoCaracterizacao obterCaracterizacaoMontada(CerhLocalizacaoGeografica clg) throws Exception {
		
		CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao  = super.obterCaracterizacaoMontada(clg);
		
		if(!Util.isNull(cerhCaptacaoCaracterizacao) && !Util.isNull(cerhCaptacaoCaracterizacao.getIdeCerhNaturezaPoco())) {
			cerhCaptacaoCaracterizacao.setIdeCerhNaturezaPoco(facade.carregarCerhNaturezaPoco(cerhCaptacaoCaracterizacao.getIdeCerhNaturezaPoco().getIdeCerhNaturezaPoco()));
		}
		return cerhCaptacaoCaracterizacao;
	}
	
	@Override
	public CerhCaptacaoCaracterizacao obterCaracterizacaoMontadaHistorico(CerhLocalizacaoGeografica clg) throws Exception {
		
		CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao  = super.obterCaracterizacaoMontadaHistorico(clg);
		
		if(!Util.isNull(cerhCaptacaoCaracterizacao) && !Util.isNull(cerhCaptacaoCaracterizacao.getIdeCerhNaturezaPoco())) {
			cerhCaptacaoCaracterizacao.setIdeCerhNaturezaPoco(facade.carregarCerhNaturezaPoco(cerhCaptacaoCaracterizacao.getIdeCerhNaturezaPoco().getIdeCerhNaturezaPoco()));
		}
		return cerhCaptacaoCaracterizacao;
	}

	@Override
	public TipologiaEnum getTipologiaEnum() {
		return TipologiaEnum.CAPTACAO_SUBTERRANEA;
	}
	
	@Override
	public TipoUsoRecursoHidricoEnum getTipoUsoRecursoHidricoEnum() {
		return TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA;
	}

	@Override
	public void abrirDialogReplicarDias() {
		Html.exibir("confirmarPreencherMesCapSub");
	}
	
	public Collection<CerhNaturezaPoco> getCerhNaturezaPocoCollection() {
		return cerhNaturezaPocoCollection;
	}


}