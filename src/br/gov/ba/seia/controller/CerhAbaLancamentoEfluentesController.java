package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.dto.TipoFinalidadeUsoAguaOrder;
import br.gov.ba.seia.entity.CerhLancamentoAbastecimentoPublico;
import br.gov.ba.seia.entity.CerhLancamentoCaracterizacaoOrigem;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteSazonalidade;
import br.gov.ba.seia.entity.CerhLancamentoOutrosUsos;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.CerhTratamentoEfluente;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhLancamentoEfluentesFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoCaptacaoLancamentoInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public class CerhAbaLancamentoEfluentesController extends CerhCaracterizacaoCaptacaoLancamentoController {
	
	@EJB
	private CerhLancamentoEfluentesFacade facade;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	private Collection<String> parametrosCollection;
	private Collection<CerhTratamentoEfluente> cerhTratamentoEfluenteCollection;
	
	@Inject
	private LocalizacaoGeograficaGenericController localizacaoGeograficaController;
	
	@Override
	public CerhLancamentoEfluentesFacade getFacade() {
		return facade;
	}

	@Override
	public TipoUsoRecursoHidricoEnum getTipoUsoRecursoHidricoEnum() {
		return TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE;
	}
	
	@Override
	public CerhLancamentoEfluenteCaracterizacao obterCaracterizacaoMontada(CerhLocalizacaoGeografica clg) {
		return (CerhLancamentoEfluenteCaracterizacao) getFacade().carregarCerhLancamentoEfluenteCaracterizacao(clg);
	}
	
	public void loadDTO(CerhDTO cerhDTO){
		load(cerhDTO);
		
		if(Util.isNullOuVazio(dto.getListaCaracterizacaoDTO())){
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
						dto.getListaCaracterizacaoDTO().add(new CerhCaracterizacaoDTO(new CerhLocalizacaoGeografica(loc), cerhProcesso));
					}
				}
			}
		}
	}

	
	@Override
	public boolean validarCerhCaraceterizacaoFinalidade() {
		boolean valido = true;
		
		for(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua:  dto.getCaracterizacaoDTO().getTipoFinalidadeUsoAguaCollection()){
			if(tipoFinalidadeUsoAgua.equals(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.OUTRA.getId()))){
				if(getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico()==null){
					valido = false;
				}
			}
			if(tipoFinalidadeUsoAgua.equals(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO.getId()))){
				if(getCerhFinalidadeAbastecimentoPublico().getIdeCerhLancamentoOutrosUsos()==null){
					valido = false;
				}
			}
		}
		
		return valido;
	}

	@Override
	public void limparAssociativas() {
		for (CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem : getFacade().listarCerhLancamentoCaracterizacaoOrigem(getCerhCaracterizacao())) {
			if(!getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection().contains(cerhLancamentoCaracterizacaoOrigem)) {
				if(cerhLancamentoCaracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().getId()
						.equals(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO.getId())){
					getFacade().excluirCerhLancamentoAbastecimentoPublico(cerhLancamentoCaracterizacaoOrigem.getId());
				}
					
				getFacade().excluirCaracterizacaoFinalidade(cerhLancamentoCaracterizacaoOrigem);
			}
		}
	}
	
	@Override
	public void excluirCaracterizacao(CerhCaracterizacaoInterface caracterizacaoInterface) {
		CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao = (CerhLancamentoEfluenteCaracterizacao)caracterizacaoInterface;
		
		if(cerhLancamentoEfluenteCaracterizacao.getCerhLancamentoCaracterizacaoOrigemCollection()!=null){
			for (CerhFinalidadeUsoAguaInterface finalidade : cerhLancamentoEfluenteCaracterizacao.getCerhLancamentoCaracterizacaoOrigemCollection()) {
				CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem = (CerhLancamentoCaracterizacaoOrigem)finalidade;
				cerhLancamentoCaracterizacaoOrigem.setIdeCerhLancamentoEfluenteCaracterizacao(cerhLancamentoEfluenteCaracterizacao);
				if(finalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO.getId())){
					getFacade().deletarCerhLancamentoAbastecimentoPublico(new CerhLancamentoAbastecimentoPublico(cerhLancamentoCaracterizacaoOrigem));
				}
				else if(finalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.OUTRA.getId())){
					getFacade().deletarFinalidadeOutra(new CerhLancamentoOutrosUsos(cerhLancamentoCaracterizacaoOrigem));
				}
				getFacade().excluirCaracterizacaoFinalidade(cerhLancamentoCaracterizacaoOrigem);
			}
		}

		getFacade().excluirCerhLancamentoSazonalidade(cerhLancamentoEfluenteCaracterizacao);
		super.getFacade().excluirCaracterizacao(caracterizacaoInterface);
		Html.esconder("confirmDialogExcluirLancamentoEfluente");
	}
	
	@Override
	public boolean isFinalidadeSelecionada() {
		return !Util.isNullOuVazio(getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection());
	}

	@Override
	public boolean isFinalidadeNecessitaInformacoesDeUso(CerhFinalidadeUsoAguaInterface cerhFinalidadeUsoAguaInterface) {
		CerhLancamentoCaracterizacaoOrigem caracterizacaoOrigem = (CerhLancamentoCaracterizacaoOrigem) cerhFinalidadeUsoAguaInterface;
		
		return 
			caracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoPublico() || 
			caracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos();
	}

	@Override
	public void prepararCerhFinalidadeUsoAgua(CerhFinalidadeUsoAguaInterface cerhFinalidadeUsoAguaInterface) {
		if(cerhFinalidadeUsoAguaInterface.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) { 
			super.listarCerhOutrosUsos();
		}
		else if(cerhFinalidadeUsoAguaInterface.getIdeTipoFinalidadeUsoAgua().isFinalidadeEsgotamentoSanitario()) {
			super.listarTipoPrestadorServico();
		}
	}
	
	public void prepararInformacaoUsoFinalidade(CerhFinalidadeUsoAguaInterface caracterizacaoFinalidade) {
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeEsgotamentoSanitario()) {
			getCerhFinalidadeAbastecimentoPublico().setIdeCerhCaptacaoAbastecimentoPublico(new CerhLancamentoAbastecimentoPublico(getCerhFinalidadeAbastecimentoPublico()));
			super.listarTipoPrestadorServico();
		} 
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) { 
			getCerhFinalidadeOutrosUsos().setIdeCerhLancamentoOutrosUsos(new CerhLancamentoOutrosUsos(getCerhFinalidadeOutrosUsos()));
			super.listarCerhOutrosUsos();
		}
	}

	@Override
	public void obterCerhFinalidade(CerhFinalidadeUsoAguaInterface finalidadeUsoAgua) throws Exception {
		CerhLancamentoCaracterizacaoOrigem caracterizacaoFinalidade = (CerhLancamentoCaracterizacaoOrigem) finalidadeUsoAgua;
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) {
			caracterizacaoFinalidade.setIdeCerhLancamentoOutrosUsos(getFacade().obterCerhFinalidadeOutrosUsos(caracterizacaoFinalidade));
		}
		else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeEsgotamentoSanitario()) {
			caracterizacaoFinalidade.setIdeCerhCaptacaoAbastecimentoPublico(getFacade().obterCerhFinalidadeAbastecimentoPublico(caracterizacaoFinalidade));
		}
		
	}
	
	@Override
	public void prepararConsultar(CerhCaracterizacaoDTO caracterizacaoDTO){
		caracterizacaoDTO.getCerhLocalizacaoGeografica().setIdeCerhLancamentoEfluenteCaracterizacao(getFacade().carregarCerhLancamentoEfluenteCaracterizacao(caracterizacaoDTO.getCerhLocalizacaoGeografica()));
	}
	
	@Override
	public boolean isExisteOutrosUsoAlemDoProcesso() {
		return !Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta()) && cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta();
	}

	@Override
	public CerhCaracterizacaoCaptacaoLancamentoInterface getCerhCaracterizacaoVazaoSazonalidade() {
		return getCerhCaracterizacao();
	}

	@Override
	public CerhCaracterizacaoFinalidadeInterface getCerhCaracterizacaoFinalidade() {
		return getCerhCaracterizacao();
	}

	@Override
	public CerhVazaoSazonalidadeInterface getCerhVazaoSazonalidade(MesEnum mes) {
		return new CerhLancamentoEfluenteSazonalidade(getCerhCaracterizacao(), mes);
	}

	@Override
	public void abrirDialogReplicarDias() {
		Html.exibir("confirmarPreencherMes");
	}

	@Override
	public TipologiaEnum getTipologiaEnum() {
		return TipologiaEnum.LANCAMENTO_EFLUENTES;
	}


	@Override
	public CerhLancamentoEfluenteCaracterizacao getCerhCaracterizacao() {
		
		if(Util.isNull(super.cerhCaracterizacao)){
			super.cerhCaracterizacao = new CerhLancamentoEfluenteCaracterizacao(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
			super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().setIdeCerhLancamentoEfluenteCaracterizacao((CerhLancamentoEfluenteCaracterizacao) super.cerhCaracterizacao);
		}
		
		return (CerhLancamentoEfluenteCaracterizacao) super.cerhCaracterizacao;
		
	}
	
	@Override
	public CerhLancamentoEfluenteCaracterizacao getCerhCaracterizacao(CerhCaracterizacaoDTO dto) {
		return dto.getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao();
	}

	@Override
	public boolean existeOutros() {
		
		if(getCerhFinalidadeOutrosUsos()!=null &&
		   getCerhFinalidadeOutrosUsos().getIdeCerhLancamentoOutrosUsos().getIdeCerhOutrosUsos()!=null &&
		   getCerhFinalidadeOutrosUsos().getIdeCerhLancamentoOutrosUsos().getIdeCerhOutrosUsos().getIdeCerhOutrosUsos().equals(26)){
			return true;
		}
		
		return false;
	}

	@Override
	public void fecharDialogCaracterizacao() {
		Html.getCurrency().hide("dlgLancamentoEfluentes");
	}

	@Override
	public void validarAba() {
		if(!Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta()) && !cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta() && 
		(!cerhDTO.getAbaDadoGerais().existeProcessoSeiaComTipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE))) {
			
			List<String> msgsValidacao = new ArrayList<String>(); 
			msgsValidacao.add(Util.getString("MSG-003", Util.getString("cerh_aba_cap_sup_num_processo")));
			throw new SeiaValidacaoRuntimeException(msgsValidacao);
		}
	}
	public boolean validarCaracterizacao() {

		boolean valido = true;
		if(!dto.getCaracterizacaoDTO().isConfirmaNomeRio()){
			MensagemUtil.erro("Confirmar corpo hídrico");
			valido =  false;
		}
		
		if(getCerhCaracterizacao().getNomCorpoHidrico()==null){
			MensagemUtil.msg0003("Nome corpo hídrico");
			valido =  false;
		}
		
		if (getCerhCaracterizacao().getIdeTipoCorpoHidrico()==null){
			MensagemUtil.msg0003("Tipo do corpo hídrico");
			valido =  false;
		}

		if(Util.isNullOuVazio(getCerhCaracterizacao().getDtInicioLancamento())){
			MensagemUtil.msg0003("Data início lancamento");
			valido =  false;
		}
		
		
		if(getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection().size()==0){
			MensagemUtil.msg0003("Origem do lançamento");
			valido =  false;
		}
		else{
			for (CerhLancamentoCaracterizacaoOrigem finalidade : getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection()) {
				if(finalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.OUTRA.getId())){
					if(getCerhFinalidadeOutrosUsos().getIdeCerhLancamentoOutrosUsos().getIdeCerhOutrosUsos()==null){
						MensagemUtil.msg0003("Outros usos");
						valido =  false;
					}
				}
				else if(finalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO.getId())){
					if(getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico().getIdeCerhTipoPrestadorServico()==null){
						MensagemUtil.msg0003("Tipo de prestador de serviço");
						valido =  false;
					}
				}
			}
		}
		
		if(getCerhCaracterizacao().getIndEfluenteRecebeTratamento()==null){
			MensagemUtil.msg0003("Antes do lançamento, o efluente recebe tratamento");
			valido =  false;
		}
		
		else if(getCerhCaracterizacao().getIndEfluenteRecebeTratamento()){
			if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getIdeCerhTratamentoEfluente()==null){
				MensagemUtil.msg0003("Tratamento do efluente");
				valido =  false;
			}
			
			if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValDboEfluenteTratado()==null){
				MensagemUtil.msg0003("DBO (5 dias 20º C) (mg/L) - Efluente tratado");
				valido =  false;
			}
			
			if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValColiformesEfluenteTratado()==null){
				MensagemUtil.msg0003("Coliformes Termotolerantes (UFC/100ml) - Efluente tratado");
				valido =  false;
			}

			if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValDboEficienciaTratamento()==null){
				MensagemUtil.msg0003("DBO (5 dias 20º C) (mg/L) - Efluente tratado");
				valido =  false;
			}
			
			if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValColiformesEficienciaTratamento()==null){
				MensagemUtil.msg0003("Coliformes Termotolerantes (UFC/100ml) - Efluente tratado");
				valido =  false;
			}
		}
		
		if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValDboEfluenteBruto()==null){
			MensagemUtil.msg0003("Efluente bruto - DBO (5 dias 20º C) (mg/L)");
			valido =  false;
		}
		
		if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValColiformesEfluenteBruto()==null){
			MensagemUtil.msg0003("Coliformes Termotolerantes (UFC/100ml) - DBO (5 dias 20º C) (mg/L)");
			valido =  false;
		}
		
		return valido;
	}
	
	public void exibirAlertaOutros(){
		if(getCerhFinalidadeOutrosUsos().getIdeCerhLancamentoOutrosUsos().getIdeCerhOutrosUsos().getIdeCerhOutrosUsos().equals(26)){
			MensagemUtil.avisoOutros();
		}
	}
	
	public boolean isOutros(){
		return isPossuiFinalidade(TipoFinalidadeUsoAguaEnum.OUTRA);
	}
	
	public boolean isEsgotamentoSanitario(){
		return isPossuiFinalidade(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO);
	}
	
	private boolean isPossuiFinalidade(TipoFinalidadeUsoAguaEnum finalidade){
		if(super.dto.getCaracterizacaoDTO().getTipoFinalidadeUsoAguaCollection()!=null){
			for (TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : super.dto.getCaracterizacaoDTO().getTipoFinalidadeUsoAguaCollection()) {
				if(tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua().equals(finalidade.getId())){
					return true;
				}
			}
		}
		return false;
	}
	
	public void confirmarNomeRio() {
		if(!Util.isNullOuVazio(getCerhCaracterizacao().getNomCorpoHidrico()) && !Util.isNull(getCerhCaracterizacao().getIdeTipoCorpoHidrico())) {
			super.dto.getCaracterizacaoDTO().setConfirmaNomeRio(true);
		} 
		if(Util.isNullOuVazio(getCerhCaracterizacao().getNomCorpoHidrico())) {
			MensagemUtil.msg0003(Util.getString("cerh_aba_cap_sup_nom_hidrico"));
		}
		if(Util.isNullOuVazio(getCerhCaracterizacao().getIdeTipoCorpoHidrico())) {
			MensagemUtil.msg0003(Util.getString("cerh_aba_cap_sup_tipo_hidrico"));
		}
	}
	
	public void editarNomeRio() {
		super.dto.getCaracterizacaoDTO().setConfirmaNomeRio(false);
	}
	
	public void converterPontoGeografico(ActionEvent event) {
		super.converterPontoGeografico(event);
    }

	@SuppressWarnings("unchecked")
	public void changeCerhCaptacaoCaracterizacaoFinalidadeCollection(ValueChangeEvent event) {
		Collection<TipoFinalidadeUsoAgua> collNew = (Collection<TipoFinalidadeUsoAgua>) event.getNewValue();
		Collection<TipoFinalidadeUsoAgua> collOld = (Collection<TipoFinalidadeUsoAgua>) event.getOldValue();
		
		if(!Util.isNullOuVazio(collNew)) {
			if(Util.isNull(getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection())) {
				getCerhCaracterizacao().setCerhLancamentoCaracterizacaoOrigemCollection(new ArrayList<CerhLancamentoCaracterizacaoOrigem>());
			}
			if(!Util.isNull(collOld) && collOld.size() > collNew.size()) {

				List<TipoFinalidadeUsoAgua> removidos = new ArrayList<TipoFinalidadeUsoAgua>();
				
				for (TipoFinalidadeUsoAgua velho : collOld) {
					if(!collNew.contains(velho)){
						removidos.add(velho);
					}	
				}
				
				List<CerhLancamentoCaracterizacaoOrigem> lista = (List<CerhLancamentoCaracterizacaoOrigem>) getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection();
				for(int x = lista.size(); x>0 ;x--){
					for(int y = 0; y<removidos.size();y++){
						if(lista.get(x-1).getIdeTipoFinalidadeUsoAgua().getId().equals(removidos.get(y).getId())){
							lista.remove(x-1);
						}
					}
					
				}

			} 
			else {
				for(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : collNew) {
					CerhLancamentoCaracterizacaoOrigem cerhFinalidade = new CerhLancamentoCaracterizacaoOrigem(tipoFinalidadeUsoAgua, getCerhCaracterizacao());
					
					if(!isContem(cerhFinalidade,getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection() )){
						getCerhCaracterizacao().getCerhLancamentoCaracterizacaoOrigemCollection().add(cerhFinalidade);
						prepararInformacaoUsoFinalidade(cerhFinalidade);
					}
				}
			}
			carregarObservacao();
		} 
		else {
			getCerhCaracterizacao().setCerhLancamentoCaracterizacaoOrigemCollection(null);
		}
	}
	

	private boolean isContem(CerhLancamentoCaracterizacaoOrigem finalidade, Collection<CerhLancamentoCaracterizacaoOrigem> finalidades){
		for (CerhLancamentoCaracterizacaoOrigem f : finalidades) {
			if(f.getIdeTipoFinalidadeUsoAgua().getId().equals(finalidade.getIdeTipoFinalidadeUsoAgua().getId())){
				return true;
			}
		}
		
		
		return false;
	}
	
	
	public CerhLancamentoCaracterizacaoOrigem getCerhFinalidadeAbastecimentoPublico() {
		return (CerhLancamentoCaracterizacaoOrigem) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO);
	}
	
	@Override
	public CerhLancamentoCaracterizacaoOrigem getCerhFinalidadeOutrosUsos() {
		return (CerhLancamentoCaracterizacaoOrigem) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.OUTRA);
	}
	
	@Override
	public void prepararDialogIncluirCaracterizacao(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		super.prepararDialog(caracterizacaoDTO, visualizacao);
		super.listarTipoCorpoHidrico();
		
		super.getTipoFinalidadeUsoAguaCollection().remove(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getId()));
		
		if(cerhTratamentoEfluenteCollection==null){
			cerhTratamentoEfluenteCollection = getFacade().listarCerhTratamentoEfluente();
		}
		
		if(parametrosCollection==null){
			parametrosCollection = new ArrayList<String>();
			parametrosCollection.add("DBO (5 dias 20º C) (mg/L)");
			parametrosCollection.add("Coliformes Termotolerantes (UFC/100ml)");
		}
		
		if(!Util.isNull(getCerhCaracterizacao()) && !Util.isNull(getCerhCaracterizacao().getIdeCerhLancamentoEfluenteCaracterizacao())) {
			super.dto.getCaracterizacaoDTO().setConfirmaNomeRio(true);
		}
		
		if(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso()!=null &&
				super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao()!=null){
				getCerhCaracterizacao().setData(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao());
			}
	}
	
	
	public void removerCerhLocalizacaoGeograficaAbaDadosGerais(CerhLocalizacaoGeografica cerhLocalizacaoGeograficaLocal){
		for(CerhTipoUso cerhTipoUso : super.cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection()) {
			if(!Util.isNullOuVazio(cerhTipoUso.getCerhLocalizacaoGeograficaCollection())) {
				for (int i = cerhTipoUso.getCerhLocalizacaoGeograficaCollection().size() - 1; i >= 0; i--) {
					CerhLocalizacaoGeografica cerhLocalizacaoGeografica = ((List<CerhLocalizacaoGeografica>) cerhTipoUso.getCerhLocalizacaoGeograficaCollection()).get(i);
					if(cerhLocalizacaoGeografica.equals(cerhLocalizacaoGeograficaLocal)) {
						cerhTipoUso.getCerhLocalizacaoGeograficaCollection().remove(cerhLocalizacaoGeografica);
					}
				}
			} 
			else if(!Util.isNull(cerhTipoUso.getIdeCerhProcesso()) && !Util.isNullOuVazio(cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection())){
				for (int i = cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection().size() - 1; i >= 0; i--) {
					CerhLocalizacaoGeografica cerhLocalizacaoGeografica = ((List<CerhLocalizacaoGeografica>) cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection()).get(i);
					if(cerhLocalizacaoGeografica.equals(cerhLocalizacaoGeograficaLocal)) {
						cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection().remove(cerhLocalizacaoGeografica);
					}
				}
			}
		}
	}


	public boolean isDisableIncluirCoordenadas(){
		if(cerhDTO.isVisualizar()){
			return true;
		}

		if(!Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta()) && cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta()){
			return false;
		}
		
		boolean isExisteProcessoSemDadoConcedido = false;
		if(super.cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection()!=null){
			for (CerhProcesso cerhProcesso : super.cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection()) {
				if(!Util.isNullOuVazio(cerhProcesso.getCerhTipoUsoCollection())) {
					for (CerhTipoUso cerhTipoUso : cerhProcesso.getCerhTipoUsoCollection()) {
						if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId()))) {
							
							if(cerhProcesso.getIndDadosConcedidos()==null){
								cerhProcesso.setIndDadosConcedidos(false);
							}
							
							if(!cerhProcesso.getIndDadosConcedidos()){
								isExisteProcessoSemDadoConcedido = true;
								break;
							}
						}
					}
				}else{
					cerhProcesso = facade.carregarCerhProcesso(cerhProcesso);
					
					if(cerhProcesso.getIndDadosConcedidos()==null){
						cerhProcesso.setIndDadosConcedidos(false);
					}
					
					if(!cerhProcesso.getIndDadosConcedidos()){
						isExisteProcessoSemDadoConcedido = true;
						break;
					}
				}
			}
		}

		if(isExisteProcessoSemDadoConcedido){
			return false;
		}
		
		return true;
	}
	
	
	public void carregarObservacao(){
		StringBuilder order = new StringBuilder();
		List<TipoFinalidadeUsoAguaOrder> listaFinalidadesOrdenadas = new ArrayList<TipoFinalidadeUsoAguaOrder>();

		if(dto.getCaracterizacaoDTO().getTipoFinalidadeUsoAguaCollection()==null){
			dto.getCaracterizacaoDTO().setTipoFinalidadeUsoAguaCollection(new ArrayList<TipoFinalidadeUsoAgua>());
		}
		
		if(dto.getCaracterizacaoDTO().getTipoFinalidadeUsoAguaCollection().size()>1){
			for (TipoFinalidadeUsoAgua tipoFinalidade : getTipoFinalidadeUsoAguaCollection()) {
				if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.INDUSTRIAL.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(0,tipoFinalidade));
				}
				else if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.MINERACAO.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(1,tipoFinalidade));
				}
				else if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(2,tipoFinalidade));
				}
				else if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_DOMESTICO.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(4,tipoFinalidade));
				}
				else if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.AQUICULTURA_TANQUE_ESCAVADO.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(5,tipoFinalidade));
				}
				else if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.MINERACAO_EXTRACAO_AREIA.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(6,tipoFinalidade));
				}
				else if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.CRIACAO_ANIMAL.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(7,tipoFinalidade));
				}
				else if(tipoFinalidade.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.TERMOELETRICA.getId())){
					listaFinalidadesOrdenadas.add(new TipoFinalidadeUsoAguaOrder(8,tipoFinalidade));
				}
			}
			
			Collections.sort(listaFinalidadesOrdenadas);
			order.append("Junto a esta finalidade existe também :");
			for(int x=0; x<listaFinalidadesOrdenadas.size(); x++){
				if(x>0){
					order.append(" "+listaFinalidadesOrdenadas.get(x).getTipoFinalidadeUsoAgua().getNomTipoFinalidadeUsoAgua());
					if((x+1)<listaFinalidadesOrdenadas.size()){
						order.append(",");
					}
					if((x+1)==listaFinalidadesOrdenadas.size()){
						order.append(".");
					}
				}
			}
		
			getCerhCaracterizacao().setDscObservacao(order.toString());
		}else{
			getCerhCaracterizacao().setDscObservacao("");
		}
	
		Html.atualizar("formLancamentoEfluentes:inputObservacao");
	}

	public void atualizarRender(){
		Html.atualizar("formLancamentoEfluentes:panelTratamentoEfluente","formLancamentoEfluentes:panelDtTratamentoEfluentes");
	}
	
	public boolean isRenderObservacacao(){
		return super.isRenderObservacacao();
	}
	
	//gets e setters
	public Collection<CerhTratamentoEfluente> getCerhTratamentoEfluenteCollection() {
		return cerhTratamentoEfluenteCollection;
	}

	public void setCerhTratamentoEfluenteCollection(Collection<CerhTratamentoEfluente> cerhTratamentoEfluenteCollection) {
		this.cerhTratamentoEfluenteCollection = cerhTratamentoEfluenteCollection;
	}

	public Collection<String> getParametrosCollection() {
		return parametrosCollection;
	}

	public void setParametrosCollection(Collection<String> parametrosCollection) {
		this.parametrosCollection = parametrosCollection;
	}

	@Override
	public void armazenarHistorico() throws Exception {
		for(CerhCaracterizacaoDTO dto : getDto().getListaCaracterizacaoDTO()) {
			CerhLancamentoEfluenteCaracterizacao copia = (CerhLancamentoEfluenteCaracterizacao) dto.getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().clone();
			prepararDialogIncluirCaracterizacao(dto, true);
			
			CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao = (CerhLancamentoEfluenteCaracterizacao) cerhCaracterizacao;
			
			cerhLancamentoEfluenteCaracterizacao.setIdeObjetoPai(cerhLancamentoEfluenteCaracterizacao.getIdeCerhLancamentoEfluenteCaracterizacao());
			cerhLancamentoEfluenteCaracterizacao.setIdeCerhLancamentoEfluenteCaracterizacao(null);
			
			if(!Util.isNullOuVazio(cerhLancamentoEfluenteCaracterizacao.getCerhLancamentoEfluenteSazonalidadeCollection())) {
				for(CerhLancamentoEfluenteSazonalidade cles : cerhLancamentoEfluenteCaracterizacao.getCerhLancamentoEfluenteSazonalidadeCollection()) {
					cles.setIdeObjetoPai(cles.getId());
					cles.setIdeCerhLancamentoEfluenteSazonalidade(null);
					cles.setIdeCerhLancamentoEfluenteCaracterizacao(cerhLancamentoEfluenteCaracterizacao);
				}
			}
			
			if(!Util.isNullOuVazio(cerhLancamentoEfluenteCaracterizacao.getCerhLancamentoCaracterizacaoOrigemCollection())) {
				for(CerhLancamentoCaracterizacaoOrigem clco : cerhLancamentoEfluenteCaracterizacao.getCerhLancamentoCaracterizacaoOrigemCollection()) {
					clco.setIdeObjetoPai(clco.getId());
					clco.setIdeCerhLancamentoCaracterizacaoOrigem(null);
					clco.setIdeCerhLancamentoEfluenteCaracterizacao(cerhLancamentoEfluenteCaracterizacao);
					
					if(!Util.isNull(clco.getIdeCerhCaptacaoAbastecimentoPublico())) {
						clco.getIdeCerhCaptacaoAbastecimentoPublico().setIdeObjetoPai(clco.getIdeCerhCaptacaoAbastecimentoPublico().getId());
						clco.getIdeCerhCaptacaoAbastecimentoPublico().setIdeCerhLancamentoAbastecimentoPublico(null);
						clco.getIdeCerhCaptacaoAbastecimentoPublico().setIdeCerhLancamentoCaracterizacaoOrigem(clco);
					}
					
					if(!Util.isNull(clco.getIdeCerhLancamentoOutrosUsos())) {
						clco.getIdeCerhLancamentoOutrosUsos().setIdeObjetoPai(clco.getIdeCerhLancamentoOutrosUsos().getId());
						clco.getIdeCerhLancamentoOutrosUsos().setIdeCerhLancamentoOutrosUsos(null);
						clco.getIdeCerhLancamentoOutrosUsos().setIdeCerhLancamentoCaracterizacaoOrigem(clco);
					}
				}
			}
			
			CerhProcesso cerhProcessoAntigo = cerhLancamentoEfluenteCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso();
			if(!Util.isNull(cerhProcessoAntigo)) {
				for(CerhProcesso cerhProcessoNovo : cerhDTO.getAbaDadoGerais().getNovoCerh().getCerhProcessoCollection()) {
					if(cerhProcessoAntigo.equals(cerhProcessoNovo.getCerhProcessoPai())) {
						
						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhLancamentoEfluenteCaracterizacao.getIdeCerhLocalizacaoGeografica().clone();
						
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);
						cloneCerhLocalizacaoGeografica.getIdeCerhLancamentoEfluenteCaracterizacao().setIdeCerhLancamentoEfluenteCaracterizacao(copia.getIdeCerhLancamentoEfluenteCaracterizacao());
						cloneCerhLocalizacaoGeografica.setIdeCerhProcesso(cerhProcessoNovo);
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
						
						for (CerhTipoUso cerhTipoUso : cerhProcessoNovo.getCerhTipoUsoCollection()) {
							if(cerhTipoUso.getIdeCerhProcesso().getIdeCerhProcesso().equals(cerhProcessoNovo.getIdeCerhProcesso())){
								cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
							}
						}
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
						cerhLancamentoEfluenteCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
					}
				}
			}
			else {
				for(CerhTipoUso ctu : cerhDTO.getAbaDadoGerais().getNovoCerh().getCerhTipoUsoCollection()) {
					if(ctu.getCerhTipoUsoPai().equals(cerhLancamentoEfluenteCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhTipoUso())) {
						
						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhLancamentoEfluenteCaracterizacao.getIdeCerhLocalizacaoGeografica().clone();
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.getIdeCerhLancamentoEfluenteCaracterizacao().setIdeCerhLancamentoEfluenteCaracterizacao(copia.getIdeCerhLancamentoEfluenteCaracterizacao());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);
						cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(ctu);
						
						LocalizacaoGeografica loc =  cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().clone();
						
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(loc));						
						
						cerhLancamentoEfluenteCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
					}
				}
			}
			salvarCaracterizacaoHistorico();
		}
	}

	@Override
	public CerhCaracterizacaoInterface obterCaracterizacaoMontadaHistorico(CerhLocalizacaoGeografica clg)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepararDialogIncluirCaracterizacaoHistorico(CerhCaracterizacaoDTO caracterizacaoDTO,
			boolean visualizacao) throws Exception {
		
	}
	
 }