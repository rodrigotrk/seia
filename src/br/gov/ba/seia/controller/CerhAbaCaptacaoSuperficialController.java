package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.abstracts.BaseCerhCaptacaoCaracterizacao;
import br.gov.ba.seia.dto.BarragemDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoMineracaoExtracaoAreia;
import br.gov.ba.seia.entity.CerhCaptacaoTermoeletrica;
import br.gov.ba.seia.entity.CerhCaptacaoTransposicao;
import br.gov.ba.seia.entity.CerhFinalidadeTransposicao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.CerhAbaCaptacaoSuperficialFacade;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public class CerhAbaCaptacaoSuperficialController extends BaseCerhCaptacaoCaracterizacao {
	
	@EJB
	private CerhAbaCaptacaoSuperficialFacade facade;
	
	private String nomBarragemPesquisa;
	private BarragemDTO barragemOutrosDTO;
	private Collection<BarragemDTO> barragemCollection;
	private Collection<CerhFinalidadeTransposicao> cerhFinalidadeTransposicaoCollection;

	public CerhAbaCaptacaoSuperficialController() {
		
	}
	
	@Override
	public void prepararDialogIncluirCaracterizacao(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		super.listarTipoCorpoHidrico();
		listarBarragem();
		
		super.prepararDialog(caracterizacaoDTO, visualizacao);
		
		if(!Util.isNull(super.getCerhCaracterizacao()) ) {
			if(!Util.isNull(super.getCerhCaracterizacao().getIdeCerhCaptacaoCaracterizacao())){
				super.dto.getCaracterizacaoDTO().setConfirmaNomeRio(true);
			}
			
			if(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso()!=null &&
				super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao()!=null){
				super.getCerhCaracterizacao().setData(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao());
			}
		}
	}
	
	@Override
	public void prepararDialogIncluirCaracterizacaoHistorico(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		super.listarTipoCorpoHidrico();
		listarBarragem();
		
		super.prepararDialogHistorico(caracterizacaoDTO, visualizacao);
		
		if(!Util.isNull(super.getCerhCaracterizacao()) ) {
			if(!Util.isNull(super.getCerhCaracterizacao().getIdeCerhCaptacaoCaracterizacao())){
				super.dto.getCaracterizacaoDTO().setConfirmaNomeRio(true);
			}
			
			if(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso()!=null &&
				super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao()!=null){
				super.getCerhCaracterizacao().setData(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao());
			}
		}
	}
	
	@Override
	public boolean validarCaracterizacao() {
		boolean valido = true;
		if(!dto.getCaracterizacaoDTO().isConfirmaNomeRio()) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040") + " o " + Util.getString("cerh_aba_cap_sup_nom_hidrico") + ".");
			valido = false;
		}
		
		if(!super.validarDataInicioCaptacao()) {
			valido = false;
		}
		
		if(Util.isNull(getCerhCaracterizacao().getIndCaptacaoReservatorio())) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_aba_cap_sup_pergunta_reservatorio_barragem"));
			valido = false;
		} 
		else if(getCerhCaracterizacao().getIndCaptacaoReservatorio() && Util.isNull(getCerhCaracterizacao().getBarragemDTO())) {
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_cap_sup_nome_barragem"));
			valido = false;
		}
		
		if(!super.validarDialog()) {
			valido = false;
		}
		
		return valido;
	}

	private boolean validarTransposicao(Collection<CerhCaptacaoTransposicao> cerhCaptacaoTransposicaoCollection) {
		if(Util.isNullOuVazio(cerhCaptacaoTransposicaoCollection)) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("cerh_finalidade_transposicao") + ".");
			return false;
		}
		return true;
	}

	private boolean validarTermoeletrica(CerhCaptacaoTermoeletrica ideCerhCaptacaoTermoeletrica) {
		boolean valido = true;
		if(Util.isNullOuVazio(ideCerhCaptacaoTermoeletrica.getNomCombustivelPrincipal())) {
			MensagemUtil.msg0003("O " + Util.getString("cerh_finalidade_dados_termoeletrica_combustivel"));
			valido = false;
		}
		if(Util.isNullOuVazio(ideCerhCaptacaoTermoeletrica.getValPotenciaInstalada())) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_termoeletrica_potencia_instalda"));
			valido = false;
		}
		if(Util.isNullOuVazio(ideCerhCaptacaoTermoeletrica.getValProducaoMensalEnergia())) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_termoeletrica_producao_mensal"));
			valido = false;
		}
		if(Util.isNullOuVazio(ideCerhCaptacaoTermoeletrica.getValEstimativaConsumoAgua())) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_termoeletrica_etimativa_agua"));
			valido = false;
		}
		return valido;
	}

	private boolean validarMineracaoAreia(CerhCaptacaoMineracaoExtracaoAreia ideCerhCaptacaoMineracaoExtracaoAreia) {
		boolean valido = true;
		if(super.isUsuarioExterno()){
			if(Util.isNullOuVazio(ideCerhCaptacaoMineracaoExtracaoAreia.getValProducaoMaximaMensal())) {
				MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_mineracao_areia_producao_media"));
				valido = false;	
			}
			if(Util.isNullOuVazio(ideCerhCaptacaoMineracaoExtracaoAreia.getValProporcaoAguaPolpa())) {
				MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_mineracao_areia_proporcao_polpa"));
				valido = false;
			}
			if(Util.isNullOuVazio(ideCerhCaptacaoMineracaoExtracaoAreia.getValVolumeMedioAgua())) {
				MensagemUtil.msg0003("O " + Util.getString("cerh_finalidade_dados_mineracao_areia_volume_medio"));
				valido = false;
			}
			if(Util.isNullOuVazio(ideCerhCaptacaoMineracaoExtracaoAreia.getValTeorUmidade())) {
				MensagemUtil.msg0003("O " + Util.getString("cerh_finalidade_dados_mineracao_areia_teor_umidade"));
				valido = false;
			} else if(ideCerhCaptacaoMineracaoExtracaoAreia.getValTeorUmidade() > 100){
				MensagemUtil.erro("O " + Util.getString("cerh_finalidade_dados_mineracao_areia_teor_umidade") + " não pode maior que 100%.");
				valido = false;
			}
			if(Util.isNullOuVazio(ideCerhCaptacaoMineracaoExtracaoAreia.getValProducaoMaximaAnual())) {
				MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_mineracao_areia_producao_maxima"));
				valido = false;
			}
		}
		return valido;
	}

	public void confirmarNomeRio() {
		if(!Util.isNullOuVazio(getCerhCaracterizacao().getNomCorpoHidrico()) && !Util.isNull(getCerhCaracterizacao().getIdeTipoCorpoHidrico())) {
			dto.getCaracterizacaoDTO().setConfirmaNomeRio(true);
		} 
		if(Util.isNullOuVazio(getCerhCaracterizacao().getNomCorpoHidrico())) {
			MensagemUtil.msg0003(Util.getString("cerh_aba_cap_sup_nom_hidrico"));
		}
		if(Util.isNullOuVazio(getCerhCaracterizacao().getIdeTipoCorpoHidrico())) {
			MensagemUtil.msg0003(Util.getString("cerh_aba_cap_sup_tipo_hidrico"));
		}
	}
	
	public void editarNomeRio() {
		dto.getCaracterizacaoDTO().setConfirmaNomeRio(false);
	}
	
	public void listarFinalidadeTransposicao() {
		try {
			if(Util.isNullOuVazio(cerhFinalidadeTransposicaoCollection)) {
				cerhFinalidadeTransposicaoCollection = facade.listarTransposicao();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_finalidade_transposicao") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	private void listarBarragem() {
		try {
			barragemOutrosDTO = getFacade().carregarBarragemOutrosDTO();
			barragemCollection = facade.listarBarragem();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_cap_sup_nome_barragem") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	public void prepararBarragem() {
		boolean selected = false;
		if(!Util.isNullOuVazio(barragemCollection)){
			BarragemDTO barragemSelected = null;
			for(BarragemDTO dto : barragemCollection){
				if(!Util.isNull(dto.getIndSelecionado()) && dto.getIndSelecionado()){
					barragemSelected = dto;
					selected = true;
					if(dto.getIdeBarragem().isOutros()) {
						MensagemLacFceUtil.exibirInformacao001();
					}
					break;
				}
			}
			barragemCollection.clear();
			if(selected){
				setNomBarragemPesquisa(null);
				barragemCollection.add(barragemSelected);
				getCerhCaracterizacao().setBarragemDTO(barragemSelected);
				getCerhCaracterizacao().setIdeBarragem(barragemSelected.getIdeBarragem());
			} 
			else {
				listarBarragem();
				getCerhCaracterizacao().setBarragemDTO(null);
			}
		}
	}
	
	public boolean isBarragemSelecionada(){
		for(BarragemDTO dto : barragemCollection){
			if(!Util.isNull(dto.getIndSelecionado()) && dto.getIndSelecionado()){
				return true;
			}
		}
		return false;
	}
	
	public void pesquisarBarragem(AjaxBehaviorEvent event){
		String nomBarragem = (String) event.getComponent().getAttributes().get("nomBarragem");
		if(!Util.isNullOuVazio(nomBarragem)){
			List<BarragemDTO> listaTemp = new ArrayList<BarragemDTO>();
			listaTemp.addAll(barragemCollection);
			barragemCollection.clear();
			for(BarragemDTO temp : listaTemp){
				if(temp.getIdeBarragem().getNomBarragem().toLowerCase().indexOf(nomBarragem.toLowerCase()) != -1){
					barragemCollection.add(temp);
				}
			}
			if(barragemCollection.isEmpty()){
				barragemCollection.add(barragemOutrosDTO);
			}
		} 
		else {
			barragemCollection.clear();
			listarBarragem();
		}
		atualizarTabelaBarragem(event);
	}

	private void atualizarTabelaBarragem(AjaxBehaviorEvent event) {
		String id = event.getComponent().getId();
		String update = event.getComponent().getClientId().replace(id, "");
		Html.atualizar(update.concat("dataTableBarragem"));
	}
	
	public void changeCaptacaoReservatorio(ValueChangeEvent event) {
		if((!(Boolean) event.getNewValue())) {
			getCerhCaracterizacao().setBarragemDTO(null);	
			getCerhCaracterizacao().setIdeBarragem(null);	
		}
	}
	
	@SuppressWarnings("unchecked")
	public void changeFinalidadeTransposicaoCollection(ValueChangeEvent event) {
		Collection<CerhFinalidadeTransposicao> collNew = (Collection<CerhFinalidadeTransposicao>) event.getNewValue();
		Collection<CerhFinalidadeTransposicao> collOld = (Collection<CerhFinalidadeTransposicao>) event.getOldValue();
		
		CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeTransposicao = (CerhCaptacaoCaracterizacaoFinalidade) getCerhFinalidade(TipoFinalidadeUsoAguaEnum.TRANSPOSICAO);
		
		if(!Util.isNullOuVazio(collNew)) {
			if(!Util.isNull(collOld) && collOld.size() > collNew.size()) {
				Collection<CerhCaptacaoTransposicao> cerhTranspoiscaoCollection = new ArrayList<CerhCaptacaoTransposicao>();
				cerhTranspoiscaoCollection.addAll(cerhFinalidadeTransposicao.getCerhCaptacaoTransposicaoCollection());
				for(CerhCaptacaoTransposicao cerhCaptacaoTransposicao : cerhTranspoiscaoCollection) {
					if(!collNew.contains(cerhCaptacaoTransposicao.getIdeCerhFinalidadeTransposicao())) {
						cerhFinalidadeTransposicao.getCerhCaptacaoTransposicaoCollection().remove(cerhCaptacaoTransposicao);
					}
				}
			} 
			else {
				for(CerhFinalidadeTransposicao transposicao : collNew) {
					CerhCaptacaoTransposicao cerhCaptacaoTransposicao = new CerhCaptacaoTransposicao(transposicao, cerhFinalidadeTransposicao);
					if(!cerhFinalidadeTransposicao.getCerhCaptacaoTransposicaoCollection().contains(cerhCaptacaoTransposicao)) {
						cerhFinalidadeTransposicao.getCerhCaptacaoTransposicaoCollection().add(cerhCaptacaoTransposicao);
						if(transposicao.isOutros()) {
							MensagemLacFceUtil.exibirInformacao001();
						}
					}
				}
			}
		} 
		else {
			cerhFinalidadeTransposicao.getCerhCaptacaoTransposicaoCollection().clear();
		}
	}

	@Override
	public void obterCerhFinalidade(CerhFinalidadeUsoAguaInterface finalidadeUsoAguaInterface) throws Exception {
		CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade = (CerhCaptacaoCaracterizacaoFinalidade) finalidadeUsoAguaInterface;
		super.obterCerhFinalidade(caracterizacaoFinalidade);
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia()) {
			caracterizacaoFinalidade.setIdeCerhCaptacaoMineracaoExtracaoAreia(facade.carregarCaptacaoExtracaoAreia(caracterizacaoFinalidade));
		} 
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTermoeletrica()) {
			caracterizacaoFinalidade.setIdeCerhCaptacaoTermoeletrica(facade.carregarCaptacaoTermoeletrica(caracterizacaoFinalidade));
		} 
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTransposicao()) {
			caracterizacaoFinalidade.setCerhCaptacaoTransposicaoCollection(facade.carregarCaptacaoTransposicao(caracterizacaoFinalidade));
		}
	}

	@Override
	public void prepararCerhFinalidadeUsoAgua(CerhFinalidadeUsoAguaInterface finalidadeIf) {
		CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade = (CerhCaptacaoCaracterizacaoFinalidade) finalidadeIf;
		super.prepararCerhFinalidadeUsoAgua(caracterizacaoFinalidade);
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia()) { 
			
		}
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTermoeletrica()) { 

		}
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTransposicao()){
			listarFinalidadeTransposicao();
			caracterizacaoFinalidade.getCerhCaptacaoTransposicaoCollection().iterator();
			super.setSelectCerhFinalidadeTransposicaoCollection(new ArrayList<CerhFinalidadeTransposicao>());
			for (CerhCaptacaoTransposicao cerhCaptacaoTransposicao : caracterizacaoFinalidade.getCerhCaptacaoTransposicaoCollection()) {
				super.getSelectCerhFinalidadeTransposicaoCollection().add(cerhCaptacaoTransposicao.getIdeCerhFinalidadeTransposicao());				
			}
		}
	}

	@Override
	public void limparAssociativas() {
		super.limparAssociativas();
		if(!Util.isNull(getCerhFinalidadeTransposicao())) {
			for (CerhCaptacaoTransposicao transposicao : facade.carregarCaptacaoTransposicao(getCerhFinalidadeTransposicao())) {
				if(!getCerhFinalidadeTransposicao().getCerhCaptacaoTransposicaoCollection().contains(transposicao)) {
					facade.excluirDadoFinalidade(transposicao);
				}
			}
		}
	}
	
	/*
	 * flags
	 */
	@Override
	public boolean isFinalidadeNecessitaInformacoesDeUso(CerhFinalidadeUsoAguaInterface cerhCaracterizacaoFinalidade) {
		CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade = (CerhCaptacaoCaracterizacaoFinalidade) cerhCaracterizacaoFinalidade;
		return super.isFinalidadeNecessitaInformacoesDeUso(cerhCaptacaoCaracterizacaoFinalidade)
				|| cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia() 
				|| cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTermoeletrica()
				|| cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTransposicao();
	}
	
	@Override
	public boolean existeOutros() {
		if(!Util.isNull(super.getCerhCaracterizacao())) {
			if(!Util.isNull(super.getCerhCaracterizacao().getIdeBarragem()) && super.getCerhCaracterizacao().getIdeBarragem().isOutros()){
				return true;
			} 
			if(!Util.isNullOuVazio(super.getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection())) {
				for (CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade : super.getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
					if(cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTransposicao()) {
						for (CerhCaptacaoTransposicao cerhCaptacaoTransposicao : cerhCaptacaoCaracterizacaoFinalidade.getCerhCaptacaoTransposicaoCollection()) {
							if(cerhCaptacaoTransposicao.getIdeCerhFinalidadeTransposicao().isOutros()) {
								return true;
							}
						}	
					}
				}
			}
		}
		return super.existeOutros();
	}
	
	/*
	 * ABSTRACT
	 */
	@Override
	public CerhAbaCaptacaoSuperficialFacade getFacade() {
		return facade;
	}

	@Override
	public void fecharDialogCaracterizacao() {
		Html.esconder("dlgCaracterizacaoCapSuperficial");
	}
	
	@Override
	public void prepararInformacaoUsoFinalidade(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade) {
		super.prepararInformacaoUsoFinalidade(caracterizacaoFinalidade);
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia()) { 
			getCerhFinalidadeMineracaoExtracaoAreia().setIdeCerhCaptacaoMineracaoExtracaoAreia(new CerhCaptacaoMineracaoExtracaoAreia(getCerhFinalidadeMineracaoExtracaoAreia()));
		}
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTermoeletrica()) { 
			getCerhFinalidadeTermoeletrica().setIdeCerhCaptacaoTermoeletrica(new CerhCaptacaoTermoeletrica(getCerhFinalidadeTermoeletrica()));
		}
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTransposicao()){
			super.setSelectCerhFinalidadeTransposicaoCollection(new ArrayList<CerhFinalidadeTransposicao>());
			getCerhFinalidadeTransposicao().setCerhCaptacaoTransposicaoCollection(new ArrayList<CerhCaptacaoTransposicao>());
			listarFinalidadeTransposicao();
		}
	}

	@Override
	public void fecharDialogDadosMineracao() {
		Html.esconder("dialogIncluirDadosMineracao");
	}

	@Override
	public void fecharDialogDadosAbsIndustrial() {
		Html.esconder("dialogIncluirDadosAbsIndustrial");
	}

	@Override
	public boolean validarCerhCaraceterizacaoFinalidade() {
		boolean valido = true;
		if(!super.validarCerhCaraceterizacaoFinalidade()) {
			valido = false;
		}
		for (CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade : super.getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
			if(cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia()) {
				if(!validarMineracaoAreia(getCerhFinalidadeMineracaoExtracaoAreia().getIdeCerhCaptacaoMineracaoExtracaoAreia())) {
					valido = false;
				}
			} 
			else if(cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTermoeletrica()) {
				if(!validarTermoeletrica(getCerhFinalidadeTermoeletrica().getIdeCerhCaptacaoTermoeletrica())) {
					valido = false;
				}
			} 
			else if(cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTransposicao()) {
				if(!validarTransposicao(getCerhFinalidadeTransposicao().getCerhCaptacaoTransposicaoCollection())) {
					valido = false;
				}
			}
		}
		return valido;
	}
	
	@Override
	public CerhCaptacaoCaracterizacao obterCaracterizacaoMontada(CerhLocalizacaoGeografica clg) throws Exception {

		CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao  = super.obterCaracterizacaoMontada(clg);
		//clg.setIdeCerhCaptacaoCaracterizacao(cerhCaptacaoCaracterizacao);
		
		if(!Util.isNull(cerhCaptacaoCaracterizacao)) {
			if(!Util.isNull(cerhCaptacaoCaracterizacao.getIdeTipoCorpoHidrico())){
				cerhCaptacaoCaracterizacao.setIdeTipoCorpoHidrico(getFacade().carregarTipoCorpoHidrico(cerhCaptacaoCaracterizacao.getIdeTipoCorpoHidrico().getIdeTipoCorpoHidrico()));
			}
			if(!Util.isNull(cerhCaptacaoCaracterizacao.getIdeBarragem())) {
				cerhCaptacaoCaracterizacao.setIdeBarragem(getFacade().carregarBarragem(cerhCaptacaoCaracterizacao.getIdeBarragem().getIdeBarragem()));
				cerhCaptacaoCaracterizacao.setBarragemDTO(getFacade().carregarBarragemDTO(cerhCaptacaoCaracterizacao.getIdeBarragem().getIdeBarragem()));
				barragemCollection = new ArrayList<BarragemDTO>();
				barragemCollection.add(cerhCaptacaoCaracterizacao.getBarragemDTO());
			}
		}
		
		//clg.setIdeCerhCaptacaoCaracterizacao(cerhCaptacaoCaracterizacao);
		//return clg.getIdeCerhCaptacaoCaracterizacao();
		
		return cerhCaptacaoCaracterizacao;
	}
	
	@Override
	public CerhCaptacaoCaracterizacao obterCaracterizacaoMontadaHistorico(CerhLocalizacaoGeografica clg) throws Exception {

		CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao  = super.obterCaracterizacaoMontadaHistorico(clg);
		
		if(!Util.isNull(cerhCaptacaoCaracterizacao)) {
			if(!Util.isNull(cerhCaptacaoCaracterizacao.getIdeTipoCorpoHidrico())){
				cerhCaptacaoCaracterizacao.setIdeTipoCorpoHidrico(getFacade().carregarTipoCorpoHidrico(cerhCaptacaoCaracterizacao.getIdeTipoCorpoHidrico().getIdeTipoCorpoHidrico()));
			}
			if(!Util.isNull(cerhCaptacaoCaracterizacao.getIdeBarragem())) {
				cerhCaptacaoCaracterizacao.setIdeBarragem(getFacade().carregarBarragem(cerhCaptacaoCaracterizacao.getIdeBarragem().getIdeBarragem()));
				cerhCaptacaoCaracterizacao.setBarragemDTO(getFacade().carregarBarragemDTO(cerhCaptacaoCaracterizacao.getIdeBarragem().getIdeBarragem()));
				barragemCollection = new ArrayList<BarragemDTO>();
				barragemCollection.add(cerhCaptacaoCaracterizacao.getBarragemDTO());
			}
		}
		return cerhCaptacaoCaracterizacao;
	}

	@Override
	public TipologiaEnum getTipologiaEnum() {
		return TipologiaEnum.CAPTACAO_SUPERFICIAL;
	}
	
	@Override
	public TipoUsoRecursoHidricoEnum getTipoUsoRecursoHidricoEnum() {
		return TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL;
	}

	@Override
	public void abrirDialogReplicarDias() {
		Html.exibir("confirmarPreencherMesCapSup");
	}
	
	/*
	 * getters & setters
	 */
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeMineracaoExtracaoAreia() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.MINERACAO_EXTRACAO_AREIA);
	}
	
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeTermoeletrica() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.TERMOELETRICA);
	}
	
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeTransposicao() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.TRANSPOSICAO);
	}

	public Collection<CerhFinalidadeTransposicao> getCerhFinalidadeTransposicaoCollection() {
		return cerhFinalidadeTransposicaoCollection;
	}

	public Collection<BarragemDTO> getBarragemCollection() {
		return barragemCollection;
	}

	public String getNomBarragemPesquisa() {
		return nomBarragemPesquisa;
	}

	public void setNomBarragemPesquisa(String nomBarragemPesquisa) {
		this.nomBarragemPesquisa = nomBarragemPesquisa;
	}

}