package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.BarragemDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.dto.IntervencaoCaracterizacaoPontoInformadoDTO;
import br.gov.ba.seia.entity.Barragem;
import br.gov.ba.seia.entity.CerhBarragemAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.BarragemEnum;
import br.gov.ba.seia.enumerator.CerhObrasHidraulicasEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhAbaBarragamFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public class CerhAbaBarragemController extends CerhFinalidadeController {

	@EJB
	private CerhAbaBarragamFacade facade;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@Inject
	private LocalizacaoGeograficaGenericController localizacaoGeograficaController;

	
	private String nomBarragemPesquisa;
	private BarragemDTO barragemOutrosDTO;	
	private Collection<BarragemDTO> barragemCollection;
	private Collection<TipoBarragem> tipoBarragemCollection;
	private Collection<TipoAproveitamentoHidreletrico> tipoAproveitamentoHidreletricoCollection;
	
	private IntervencaoCaracterizacaoPontoInformadoDTO intervencaoDTO;
	private List<IntervencaoCaracterizacaoPontoInformadoDTO> listaIntervencaoDTO;

	private Boolean unicoPontoDeIntervencao;
	
	public CerhAbaBarragemController() {
		
	}
	
	public void loadDTO(CerhDTO cerhDTO){
		super.load(cerhDTO);
		
		if(Util.isNullOuVazio(super.getDto().getListaCaracterizacaoDTO())){
			List<LocalizacaoGeografica> listlocGeo = new ArrayList<LocalizacaoGeografica>();
			for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
				listlocGeo.addAll(localizacaoGeograficaService.listarLocalizacaoGeograficaPorProcessoIntervencaoBarragem(cerhProcesso.getNumProcesso()));
			}
			for(LocalizacaoGeografica loc : listlocGeo){
				
				for(DadoGeografico dado : loc.getDadoGeograficoCollection()){
					loc.setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
					loc.setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
				}
				if(!Util.isNullOuVazio(loc.getLatitudeInicial()) && !Util.isNullOuVazio(loc.getLongitudeInicial())){
					for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
						cerhProcesso.getIdeCerhProcesso();
						dto.getListaCaracterizacaoDTO().add(new CerhCaracterizacaoDTO(new CerhLocalizacaoGeografica(loc,cerhProcesso),cerhProcesso));
					}
				}
			}
		}
		
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
	

	
	public void prepararBarragem() {
		
		boolean selected = false;
		if(!Util.isNullOuVazio(barragemCollection)){
			BarragemDTO barragemSelected = null;
			for(BarragemDTO dto : barragemCollection){
				if(!Util.isNull(dto.getIndSelecionado()) && dto.getIndSelecionado()){
					if(dto.getIdeBarragem().isOutros()) {
						dto.getIdeBarragem().setNomBarragem(null);
						dto.setNomMunicipio(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getNomeMunicipio());
						dto.setLatitude(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getIdeLocalizacaoGeografica().getLatitudeInicial());
						dto.setLongitude(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getIdeLocalizacaoGeografica().getLongitudeInicial());
					}
					barragemSelected = dto;
					selected = true;
					break;
				}
			}
			barragemCollection.clear();
			if(selected){
				nomBarragemPesquisa = null;
				
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
		if(barragemCollection==null){
			return true;
		}
		
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
	
	@SuppressWarnings("unchecked")
	public void changeCerhBarragemCaracterizacaoFinalidadeCollection(ValueChangeEvent event) {
		Collection<TipoFinalidadeUsoAgua> collNew = (Collection<TipoFinalidadeUsoAgua>) event.getNewValue();
		Collection<TipoFinalidadeUsoAgua> collOld = (Collection<TipoFinalidadeUsoAgua>) event.getOldValue();
	
		
		if(!Util.isNullOuVazio(collNew)) {
			if(Util.isNull(getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection())) {
				getCerhCaracterizacao().setCerhBarragemCaracterizacaoFinalidadeCollection(new ArrayList<CerhBarragemCaracterizacaoFinalidade>());
			}
			
			if(!Util.isNull(collOld) && collOld.size() > collNew.size()) {
				Collection<CerhBarragemCaracterizacaoFinalidade> finalidadeCollectionTemp = new ArrayList<CerhBarragemCaracterizacaoFinalidade>();
				finalidadeCollectionTemp.addAll(getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection());
				
				for(CerhBarragemCaracterizacaoFinalidade cerhFinalidade : finalidadeCollectionTemp) {
					if(!collNew.contains(cerhFinalidade.getIdeTipoFinalidadeUsoAgua())) {
						if(cerhFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()){
							intervencaoDTO = null;
							listaIntervencaoDTO = null;
							getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhLocalizacaoGeograficaIntervencao(null);
						}
						getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection().remove(cerhFinalidade);
					}
				}
			} 
		
			else {
				
				for(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : collNew) {
					CerhBarragemCaracterizacaoFinalidade finalidadeNaTela = new CerhBarragemCaracterizacaoFinalidade(getCerhCaracterizacao(), tipoFinalidadeUsoAgua);
					
					if(!isContem(finalidadeNaTela, getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection())){
						getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection().add(finalidadeNaTela);
						prepararInformacaoUsoFinalidade(finalidadeNaTela);
					}
					
				}
			}
		} 
		else {
			getCerhCaracterizacao().setCerhBarragemCaracterizacaoFinalidadeCollection(null);
		}
	}
	
	private boolean isContem(CerhBarragemCaracterizacaoFinalidade finalidade, Collection<CerhBarragemCaracterizacaoFinalidade> finalidadesSalvas  ){
		for (CerhBarragemCaracterizacaoFinalidade finalidadeSalva : finalidadesSalvas) {
			if(finalidadeSalva.getIdeTipoFinalidadeUsoAgua().getId().equals(finalidade.getIdeTipoFinalidadeUsoAgua().getId())){
				return true;
			}
		}
		
		return false;
	}
	
	public void prepararInformacaoUsoFinalidade(CerhBarragemCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()) {
			getCerhFinalidadeAproveitamentoHidreletrico().setIdeCerhBarragemAproveitamentoHidreletrico(new CerhBarragemAproveitamentoHidreletrico(getCerhFinalidadeAproveitamentoHidreletrico()));
			listarTipoAproveitamento();
			listarPontosDeIntervencao();
		}
	}
	
	private void listarBarragem() {
		try {
			barragemOutrosDTO = getFacade().carregarBarragemOutrosDTO();
			barragemCollection = facade.listarBarragem();
		} catch (Exception e) {
			MensagemUtil.erro((Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_cap_sup_nome_barragem") + "."));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	private void listarTipoBarragem() {
		try {
			if(Util.isNullOuVazio(tipoBarragemCollection)){
				tipoBarragemCollection = facade.listarTipoBarragem();
			}
		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_bar_tipo_barramento") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	private void listarTipoAproveitamento(){
		try {
			if(Util.isNullOuVazio(tipoAproveitamentoHidreletricoCollection)){
				tipoAproveitamentoHidreletricoCollection = facade.listarTipoAproveitamentoHidreletrico();
			}
		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_bar_tipo_barramento") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	private void listarPontosDeIntervencao() {
		listaIntervencaoDTO = new ArrayList<IntervencaoCaracterizacaoPontoInformadoDTO>();
		unicoPontoDeIntervencao = null;
		if(!Util.isNull(super.cerhDTO.getAba(TipoUsoRecursoHidricoEnum.INTERVENCAO)) 
				&& !Util.isNullOuVazio(super.cerhDTO.getAba(TipoUsoRecursoHidricoEnum.INTERVENCAO).getListaCaracterizacaoDTO())){
			for (CerhCaracterizacaoDTO caracterizacaoIntervencaoDTO : super.cerhDTO.getAba(TipoUsoRecursoHidricoEnum.INTERVENCAO).getListaCaracterizacaoDTO()) {
				if(!Util.isNull(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhIntervencaoCaracterizacao())
						&& !Util.isNull(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhIntervencaoCaracterizacao().getId())){
					
					CerhIntervencaoCaracterizacao intervencao = null;
					if(Util.isNull(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhIntervencaoCaracterizacao().getIdeCerhObrasHidraulicas())){
						intervencao = obterCerhIntervencaoCaracterizacao(caracterizacaoIntervencaoDTO);
					} 
					else {
						intervencao = caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhIntervencaoCaracterizacao();
					}
					if(!Util.isNull(intervencao.getIdeCerhObrasHidraulicas())
							&& (intervencao.getIdeCerhObrasHidraulicas().getIdeCerhObrasHidraulicas().equals(CerhObrasHidraulicasEnum.DESVIO.getIde()) || intervencao.getIdeCerhObrasHidraulicas().getIdeCerhObrasHidraulicas().equals(CerhObrasHidraulicasEnum.RETIFICACAO_CANALIZACAO.getIde()))
							&& (Util.isNull(intervencao.getIndPotenciaInstaladaBarragem()) || !Util.isNull(intervencao.getIndPotenciaInstaladaBarragem()) && !intervencao.getIndPotenciaInstaladaBarragem())
							&& !Util.isNullOuVazio(intervencao.getValPotenciaInstaladaTotal())){
						intervencao.setIdeCerhLocalizacaoGeografica(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica());
						listaIntervencaoDTO.add(new IntervencaoCaracterizacaoPontoInformadoDTO(intervencao));
					}
					
				}
			}
		}
		if(existemPontosDeIntervencao() && listaIntervencaoDTO.size() == 1){
			unicoPontoDeIntervencao = true;
			intervencaoDTO = (IntervencaoCaracterizacaoPontoInformadoDTO) listaIntervencaoDTO.get(0);
			intervencaoDTO.setIndSelecionado(true);
			getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhLocalizacaoGeograficaIntervencao(intervencaoDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica());
		} else if(existemPontosDeIntervencao() && !Util.isNullOuVazio(listaIntervencaoDTO)) {
			if(!Util.isNullOuVazio(getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection()) 
					&& getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection().size() == 1) {
				
				Integer idCerhLocalizacaoGeograficaIntervencao = ((ArrayList<CerhBarragemCaracterizacaoFinalidade>)getCerhCaracterizacao()
						.getCerhBarragemCaracterizacaoFinalidadeCollection()).get(0).getIdeCerhBarragemAproveitamentoHidreletrico()
						.getIdeCerhLocalizacaoGeograficaIntervencao().getId();
				
				for (IntervencaoCaracterizacaoPontoInformadoDTO intervencao : listaIntervencaoDTO) {
					if (intervencao.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getId().equals(idCerhLocalizacaoGeograficaIntervencao)) {
						intervencaoDTO = intervencao;
						intervencaoDTO.setIndSelecionado(true);
						getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhLocalizacaoGeograficaIntervencao(intervencaoDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica());
						break;
					}
				}
			}
		}
	}
	
	private void atualizarGrid(ValueChangeEvent event, String panelGrid){
		Html.atualizar(event.getComponent().getClientId().replace(event.getComponent().getId(), panelGrid));
	}
	
	public void changeDesvioTrecho(ValueChangeEvent event){
		if(!Util.isNull(event.getNewValue()) && !(Boolean)event.getNewValue()){
			getIdeCerhBarragemAproveitamentoHidreletrico().setValExtensao(null);
			getIdeCerhBarragemAproveitamentoHidreletrico().setValTrechoVazaoReduzida(null);
		}
		atualizarGrid(event, "gridAproveitamento");
	}
	
	public void changePotenciaInstalada(ValueChangeEvent event) {
		intervencaoDTO = null;
		getIdeCerhBarragemAproveitamentoHidreletrico().setIndPch(null);
		getIdeCerhBarragemAproveitamentoHidreletrico().setIndEmOperacao(null);
		getIdeCerhBarragemAproveitamentoHidreletrico().setValPotenciaInstaladaTotal(null);
		getIdeCerhBarragemAproveitamentoHidreletrico().setValProducaoAnualEfetivamenteVerificada(null);
		if(!(Boolean)event.getNewValue()){
			getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhLocalizacaoGeograficaIntervencao(null);
			getIdeCerhBarragemAproveitamentoHidreletrico().setDtInicioOperacao(null);
		} 
		else {
			listarPontosDeIntervencao();
			if(isPontoDeIntervencaoSelecionado()){
				getIdeCerhBarragemAproveitamentoHidreletrico().setIndPch(intervencaoDTO.getIndPCH());
				getIdeCerhBarragemAproveitamentoHidreletrico().setIndEmOperacao(intervencaoDTO.getIndOperacao());
			}
		}
		atualizarGrid(event, "gridPonteciaAnteriormente");
		atualizarGrid(event, "gridPch");
	}
	
	public void changePontoIntervencao()  {
		if(!Util.isNullOuVazio(listaIntervencaoDTO)){
			intervencaoDTO = null;
			for(IntervencaoCaracterizacaoPontoInformadoDTO dto : listaIntervencaoDTO){
				if(!Util.isNull(dto.getIndSelecionado()) && dto.getIndSelecionado()){
					intervencaoDTO = dto;
					break;
				}
			}
			listaIntervencaoDTO.clear();
			if(!Util.isNull(intervencaoDTO)){
				listaIntervencaoDTO.add(intervencaoDTO);
				getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhLocalizacaoGeograficaIntervencao(intervencaoDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica());
				getIdeCerhBarragemAproveitamentoHidreletrico().setIndPch(intervencaoDTO.getIndPCH());
				getIdeCerhBarragemAproveitamentoHidreletrico().setIndEmOperacao(intervencaoDTO.getIndOperacao());
			} 
			else {
				getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhLocalizacaoGeograficaIntervencao(null);
				getIdeCerhBarragemAproveitamentoHidreletrico().setIndPch(null);
				getIdeCerhBarragemAproveitamentoHidreletrico().setIndEmOperacao(null);
				listarPontosDeIntervencao();
			}
		}
	}
	
	public void changePCH(ValueChangeEvent event){
		if(!Util.isNull(event.getNewValue()) && !(Boolean)event.getNewValue()){
			getIdeCerhBarragemAproveitamentoHidreletrico().setIndPotenciaInstaladaIntervencao(null);
			getIdeCerhBarragemAproveitamentoHidreletrico().setValPotenciaInstaladaTotal(null);
			getIdeCerhBarragemAproveitamentoHidreletrico().setIndEmOperacao(null);
			getIdeCerhBarragemAproveitamentoHidreletrico().setDtInicioOperacao(null);
			getIdeCerhBarragemAproveitamentoHidreletrico().setValProducaoAnualEfetivamenteVerificada(null);
		}
		atualizarGrid(event, "gridPch");
	}
	
	public void changeOperacao(ValueChangeEvent event){
		if(!Util.isNull(event.getNewValue()) && !(Boolean)event.getNewValue()){
			getIdeCerhBarragemAproveitamentoHidreletrico().setDtInicioOperacao(null);
			getIdeCerhBarragemAproveitamentoHidreletrico().setValProducaoAnualEfetivamenteVerificada(null);
		}
		atualizarGrid(event, "gridPch");
	}
	
	public boolean isBarragemOutros(){
		return !Util.isNull(getCerhCaracterizacao()) && !Util.isNull(getCerhCaracterizacao().getBarragemDTO()) && getCerhCaracterizacao().getBarragemDTO().getIdeBarragem().isOutros(); 
	}
	
	public boolean isDesvioTrecho(){
		return !Util.isNull(getIdeCerhBarragemAproveitamentoHidreletrico().getIndDesvioTrecho()) && getIdeCerhBarragemAproveitamentoHidreletrico().getIndDesvioTrecho();
	}
	
	public boolean isPodeResponderPotenciaInstaladaAnteriormente(){
		return existemPontosDeIntervencao();
	}

	private boolean existemPontosDeIntervencao() {
		return !Util.isNullOuVazio(listaIntervencaoDTO);
	}
	
	public Boolean getUnicoPontoDeIntervencao() {
		return !Util.isNull(unicoPontoDeIntervencao) && unicoPontoDeIntervencao;
	}
	
	public boolean isPontoDeIntervencaoSelecionado(){
		return !Util.isNull(intervencaoDTO);
	}

	public boolean isFazAproveitamentoPch(){
		return !Util.isNull(getIdeCerhBarragemAproveitamentoHidreletrico().getIndPch()) && getIdeCerhBarragemAproveitamentoHidreletrico().getIndPch();
	}
	
	public boolean isPotenciaInstaladaInformadaAnteriormenteRespondida(){
		return !Util.isNull(getIdeCerhBarragemAproveitamentoHidreletrico().getIndPotenciaInstaladaIntervencao());
	}
	
	public boolean isPotenciaInstaladaInformadaAnteriormente(){
		return isPotenciaInstaladaInformadaAnteriormenteRespondida() && getIdeCerhBarragemAproveitamentoHidreletrico().getIndPotenciaInstaladaIntervencao();
	}
	
	public boolean isOperacaoRespondida(){
		return !Util.isNull(getIdeCerhBarragemAproveitamentoHidreletrico().getIndEmOperacao());
	}
	
	public boolean isOperacao(){
		return isOperacaoRespondida() && getIdeCerhBarragemAproveitamentoHidreletrico().getIndEmOperacao();
	}
	
	@Override
	public CerhBarragemCaracterizacao getCerhCaracterizacao() {
		if(Util.isNull(super.cerhCaracterizacao)){
			super.cerhCaracterizacao = new CerhBarragemCaracterizacao(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
			dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().setIdeCerhBarragemCaracterizacao((CerhBarragemCaracterizacao) super.cerhCaracterizacao);
		}
		return (CerhBarragemCaracterizacao) super.cerhCaracterizacao;
	}
	
	@Override
	public CerhBarragemCaracterizacao getCerhCaracterizacao(CerhCaracterizacaoDTO dto) {
		return dto.getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao();
	}
	
	@Override
	public CerhCaracterizacaoFinalidadeInterface getCerhCaracterizacaoFinalidade() {
		return getCerhCaracterizacao();
	}
	
	private CerhBarragemCaracterizacaoFinalidade getCerhFinalidadeAproveitamentoHidreletrico() {
		return (CerhBarragemCaracterizacaoFinalidade) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.APROVEITAMENTO_HIDRELETRICO);
	}

	public CerhBarragemAproveitamentoHidreletrico getIdeCerhBarragemAproveitamentoHidreletrico() {
		return getCerhFinalidadeAproveitamentoHidreletrico().getIdeCerhBarragemAproveitamentoHidreletrico();
	}
	
	@Override
	public TipologiaEnum getTipologiaEnum() {
		return TipologiaEnum.INTERVENCAO_CORPO_HIDRICO;
	}

	@Override
	public CerhAbaBarragamFacade getFacade() {
		return facade;
	}

	@Override
	public TipoUsoRecursoHidricoEnum getTipoUsoRecursoHidricoEnum() {
		return TipoUsoRecursoHidricoEnum.BARRAGEM;
	}


	@Override
	public void obterCerhFinalidade(CerhFinalidadeUsoAguaInterface finalidadeUsoAgua) {
		CerhBarragemCaracterizacaoFinalidade caracterizacaoFinalidade = (CerhBarragemCaracterizacaoFinalidade) finalidadeUsoAgua; 
		
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()) {
			caracterizacaoFinalidade.setIdeCerhBarragemAproveitamentoHidreletrico(getFacade().carregarCerhBarragemAproveitamentoHidreletrico(caracterizacaoFinalidade));
		}
	}

	private CerhIntervencaoCaracterizacao obterCerhIntervencaoCaracterizacao(CerhCaracterizacaoDTO caracterizacaoIntervencaoDTO) {
		try {
			getFacade().prepararLocalizacaoGeografica(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica());
			return facade.carregarCerhIntervencaoCaracterizacao(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void prepararDialogIncluirCaracterizacao(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		super.listarTipoCorpoHidrico();
		
		listarTipoBarragem();

		super.prepararDialog(caracterizacaoDTO, visualizacao);
		if(!Util.isNull(getCerhCaracterizacao()) && !Util.isNull(getCerhCaracterizacao().getIdeCerhBarragemCaracterizacao())) {
			super.dto.getCaracterizacaoDTO().setConfirmaNomeRio(true);
		}

		if(super.isUsuarioInterno()){
			if(!Util.isNull(getCerhCaracterizacao()) && !Util.isNull(getCerhCaracterizacao().getBarragemDTO())) {
				getCerhCaracterizacao().getBarragemDTO().setIndSelecionado(true);
				super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao().setBarragemDTO(getCerhCaracterizacao().getBarragemDTO());
				barragemCollection.clear();
				barragemCollection.add(getCerhCaracterizacao().getBarragemDTO());
			} else {
				listarBarragem();
			}
		}
		
		else if(super.isUsuarioExterno() || super.isUsuarioConsorcio()){
			if(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao().getBarragemDTO()==null){
				if(getCerhCaracterizacao().getBarragemDTO()!=null){
					if(!dto.getCaracterizacaoDTO().isVisualizacao()){
						getCerhCaracterizacao().getBarragemDTO().setIndSelecionado(true);
					}
					
					super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao().setBarragemDTO(getCerhCaracterizacao().getBarragemDTO());
				}else{
					nomBarragemPesquisa = "Outros";
				
					barragemOutrosDTO = getFacade().carregarBarragemOutrosDTO();
					barragemOutrosDTO.setIndSelecionado(true);
					barragemOutrosDTO.setIdeBarragem(new Barragem(361, "Outros"));
					if(barragemCollection ==null){
						barragemCollection = new ArrayList<BarragemDTO>();
					}
					barragemCollection.clear();
					if(!dto.getCaracterizacaoDTO().isVisualizacao()){
						barragemOutrosDTO.setIndSelecionado(true);
					}
					
					barragemCollection.add(barragemOutrosDTO);
					prepararBarragem();
					super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao().setBarragemDTO(barragemOutrosDTO);
				}
			}
		}
	}

	@Override
	public CerhBarragemCaracterizacao obterCaracterizacaoMontada(CerhLocalizacaoGeografica clg)  {
		CerhBarragemCaracterizacao cerhBarragemCaracterizacao = getFacade().carregarCerhBarragemCaracterizacao(clg);
		
		if(!Util.isNull(cerhBarragemCaracterizacao)) {
			cerhBarragemCaracterizacao.setIdeBarragem(getFacade().carregarBarragem(cerhBarragemCaracterizacao.getIdeBarragem().getIdeBarragem()));
			cerhBarragemCaracterizacao.setBarragemDTO(getFacade().carregarBarragemDTO(cerhBarragemCaracterizacao.getIdeBarragem().getIdeBarragem()));
			barragemCollection = new ArrayList<BarragemDTO>();
			barragemCollection.add(cerhBarragemCaracterizacao.getBarragemDTO());
			
			cerhBarragemCaracterizacao.setIdeCerhLocalizacaoGeografica(clg);
			
			cerhBarragemCaracterizacao.setIdeCerhSituacaoTipoUso(getFacade().carregarCerhSituacaoTipoUso(cerhBarragemCaracterizacao.getIdeCerhSituacaoTipoUso().getIdeCerhSituacaoTipoUso()));
			cerhBarragemCaracterizacao.setCerhBarragemCaracterizacaoFinalidadeCollection(getFacade().listarCerhBarragemCaracterizacaoFinalidade(cerhBarragemCaracterizacao));
			
			if(!Util.isNullOuVazio(cerhBarragemCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection())){
				for (CerhBarragemCaracterizacaoFinalidade cerhBarragemCaracterizacaoFinalidade : cerhBarragemCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection()) {
					obterCerhFinalidade(cerhBarragemCaracterizacaoFinalidade);
				}
			}
		}
		
		return cerhBarragemCaracterizacao;
	}
	
	
	public boolean isUsuarioExterno(){
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	@Override
	public void fecharDialogCaracterizacao() {
		Html.esconder("dlgCaracterizacaoBarragem");
	}

	@Override
	public boolean validarCaracterizacao() {
		boolean valido = true;
		
		if(!dto.getCaracterizacaoDTO().isConfirmaNomeRio()) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040") + " o " + Util.getString("cerh_aba_cap_sup_nom_hidrico") + ".");
			valido = false;
		}
		
		if(Util.isNull(getCerhCaracterizacao().getBarragemDTO())) {
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_cap_sup_nome_barragem"));
			valido = false;
		} 

		
		else if(getCerhCaracterizacao().getBarragemDTO().getIdeBarragem().isOutros()){
			if(Util.isNullOuVazio(getCerhCaracterizacao().getBarragemDTO().getIdeBarragem().getNomBarragem())){
				MensagemUtil.msg0003("O " + Util.getString("cerh_aba_cap_sup_nome_barragem"));
				valido = false;
			} 
			else {
				try{
					validarNovaBarragem(getCerhCaracterizacao().getBarragemDTO());
				} 
				catch(SeiaValidacaoRuntimeException exception){
					MensagemUtil.erro(exception.getMessage());
					valido = false;		
				}
			}
		}
		
		if(Util.isNull(getCerhCaracterizacao().getIdeTipoBarragem())) {
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_bar_tipo_barramento"));
			valido = false;
		}
		
		if(Util.isNull(getCerhCaracterizacao().getIdeCerhSituacaoTipoUso())) {
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_bar_situacao_barragem"));
			valido = false;
		}
		
		if(!super.validarFinalidades()){
			valido = false;
		}
		
		if(super.isProcessoOutorgado() && (Util.isNull(getCerhCaracterizacao().getValVazaoLiberadaJusante()) || getCerhCaracterizacao().getValVazaoLiberadaJusante().compareTo(BigDecimal.ZERO)==0)) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_aba_bar_vazao_jusante"));
			valido = false;
		}
		
		if(super.isProcessoOutorgado() || super.isProcessoDispensado()){
			if(Util.isNullOuVazio(getCerhCaracterizacao().getValAlturaMaximaBarragem())){
				MensagemUtil.msg0003("A " + Util.getString("cerh_aba_bar_altura_maxima_barragem"));
				valido = false;
			}
			if(Util.isNullOuVazio(getCerhCaracterizacao().getValVolumeMaximoReservatorio())){
				MensagemUtil.msg0003("O " + Util.getString("cerh_aba_bar_volume_maximo_reservatorio"));
				valido = false;
			}
			if(Util.isNullOuVazio(getCerhCaracterizacao().getValVazaoRegularizada())){
				MensagemUtil.msg0003("A " + Util.getString("cerh_aba_bar_vazao_regularizada"));
				valido = false;
			}
		}
		
		return valido;
	}

	private void validarNovaBarragem(BarragemDTO barragemDTO) {
		try {
			
			if(getCerhCaracterizacao().getBarragemDTO().getIdeBarragem().getIdeBarragem().equals(BarragemEnum.OUTROS.getId())){
				for (BarragemDTO barragemSalva : facade.listarBarragem()) {
					/*
					if(barragemSalva.getIdeBarragem().getNomBarragem().equalsIgnoreCase("rio_rio_rio")){
						barragemSalva.getIdeBarragem();
					}
					 * */
					if(compararBarragem(barragemSalva,getCerhCaracterizacao().getBarragemDTO())){
						throw new SeiaValidacaoRuntimeException("Essa barragem não pode ser cadastrada novamente.");
					}
				}
			}
		} 
		catch (SeiaValidacaoRuntimeException e) {
			throw new SeiaValidacaoRuntimeException(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean compararBarragem(BarragemDTO barragemUm, BarragemDTO barragemDois){
		if(barragemUm.getIdeBarragem().getNomBarragem().trim().equalsIgnoreCase(barragemDois.getIdeBarragem().getNomBarragem().trim()) && 
		   barragemUm.getNomMunicipio().trim().equalsIgnoreCase(barragemDois.getNomMunicipio().trim()) &&
		   compararCoordenadasDaBarragem(barragemUm, barragemDois)){
			return true;
		}
		
		return false;
	}

	private boolean compararCoordenadasDaBarragem(BarragemDTO barragemUm, BarragemDTO barragemDois) {
		CoordenadaGeografica coordenadaBarragemUm= new CoordenadaGeografica();
		CoordenadaGeografica coordenadaBarragemDois = new CoordenadaGeografica();
		
		if(barragemUm.getLatitude().contains("º")){
			coordenadaBarragemUm.setLatitude(GeoUtil.converterGMSParaDecimal(barragemUm.getLatitude()));
			coordenadaBarragemUm.setLongitude(GeoUtil.converterGMSParaDecimal(barragemUm.getLongitude()));
		}else{
			coordenadaBarragemUm.setLatitude(GeoUtil.decimalParaGMS(barragemUm.getLatitude()));
			coordenadaBarragemUm.setLongitude(GeoUtil.decimalParaGMS(barragemUm.getLongitude()));
		}

		if(barragemDois.getLatitude().contains("º")){
			coordenadaBarragemDois.setLatitude(GeoUtil.converterGMSParaDecimal(barragemDois.getLatitude()));
			coordenadaBarragemDois.setLongitude(GeoUtil.converterGMSParaDecimal(barragemDois.getLongitude()));
		}else{
			coordenadaBarragemDois.setLatitude(GeoUtil.decimalParaGMS(barragemDois.getLatitude()));
			coordenadaBarragemDois.setLongitude(GeoUtil.decimalParaGMS(barragemDois.getLongitude()));
		}
		
		return (coordenadaBarragemUm.compareTo(coordenadaBarragemDois)==0);
	}

	@Override
	public boolean existeOutros() {
		return false;
	}

	@Override
	public boolean isExisteOutrosUsoAlemDoProcesso() {
		if(!Util.isNullOuVazio(super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoIntervencaoSelecionado())){
			if(super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoIntervencaoSelecionado().contains(new TipoUsoRecursoHidrico(getTipoUsoRecursoHidricoEnum()))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void excluirCaracterizacao(CerhCaracterizacaoInterface caracterizacaoInterface) {
		CerhBarragemCaracterizacao cerhBarragemCaracterizacao;
		
		if(!Util.isNullOuVazio(super.dto.getCaracterizacaoDTO())) {
			cerhBarragemCaracterizacao = ((CerhBarragemCaracterizacao) getCerhCaracterizacao(super.dto.getCaracterizacaoDTO()));
		}else {
			cerhBarragemCaracterizacao = (CerhBarragemCaracterizacao) caracterizacaoInterface;
		}
		
		if( cerhBarragemCaracterizacao!=null && cerhBarragemCaracterizacao.getIdeCerhBarragemCaracterizacao()!=null){
			cerhBarragemCaracterizacao = getFacade().carregarCerhBarragemCaracterizacao(cerhBarragemCaracterizacao);
		}
		
		
		if(cerhBarragemCaracterizacao!=null){
			List<CerhIntervencaoCaracterizacao> cerhIntervencaoCaracterizacao = (List<CerhIntervencaoCaracterizacao>) getFacade().carregarCerhIntervencaoCaracterizacao(cerhBarragemCaracterizacao);
			
			/* Verifica se existe alguma Intervenção vinculada com a Barragem que está querendo excluir. 
			 * Se existir, não permite a exclusão da Barragem. */
			if (!Util.isNullOuVazio(cerhIntervencaoCaracterizacao)) {
				throw new SeiaValidacaoRuntimeException(Util.getString("cerh_caracterizacao_msg_erro_barragem_intervencao_com_vinculo"));
			} else {
				getFacade().excluirCaracterizacao((CerhBarragemCaracterizacao) caracterizacaoInterface);
			}
		}else{
			getFacade().excluirCerhLocalizacaoGeografica(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
		}
	}
	
	@Override
	public void excluirCaracterizacao() {
		try {
			super.preparaCerhCaracterizacaoParaExclusao();
			excluirCaracterizacao(getCerhCaracterizacao());
			dto.getListaCaracterizacaoDTO().remove(dto.getCaracterizacaoDTO());
			JsfUtil.addSuccessMessage("Caracterização excluída com sucesso!");
		} 
		catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addErrorMessage(e.getMessage());	
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a Caracterização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public boolean isFinalidadeSelecionada() {
		return !Util.isNullOuVazio(getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection());
	}

	@Override
	public boolean isFinalidadeNecessitaInformacoesDeUso(CerhFinalidadeUsoAguaInterface cerhFinalidadeUsoAguaInterface) {
		CerhBarragemCaracterizacaoFinalidade cerhBarragemCaracterizacaoFinalidade = (CerhBarragemCaracterizacaoFinalidade) cerhFinalidadeUsoAguaInterface;
		return cerhBarragemCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico(); 
	}

	@Override
	public boolean validarCerhCaraceterizacaoFinalidade() {
		boolean valido = true;
		for (CerhBarragemCaracterizacaoFinalidade cerhBarragemCaracterizacaoFinalidade : getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection()) {
			if(cerhBarragemCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()) {
				CerhBarragemAproveitamentoHidreletrico barragemAproveitamentoHidreletrico = cerhBarragemCaracterizacaoFinalidade.getIdeCerhBarragemAproveitamentoHidreletrico();
				if(Util.isNull(barragemAproveitamentoHidreletrico.getIdeTipoAproveitamentoHidreletrico())){
					MensagemUtil.msg0003("O " + Util.getString("cerh_aba_bar_tipo_aproveitamento"));
					valido = false;
				}
				if(Util.isNull(barragemAproveitamentoHidreletrico.getIndDesvioTrecho())){
					MensagemUtil.msg0003(Util.getString("cerh_aba_bar_possui_desvio_trecho"));
					valido = false;
				} 
				else if(barragemAproveitamentoHidreletrico.getIndDesvioTrecho()){
					if(Util.isNullOuVazio(barragemAproveitamentoHidreletrico.getValExtensao())){
						MensagemUtil.msg0003("A " + Util.getString("cerh_aba_bar_extensao"));
						valido = false;
					}
					if(super.isProcessoOutorgado() && Util.isNullOuVazio(getIdeCerhBarragemAproveitamentoHidreletrico().getValTrechoVazaoReduzida())){
						MensagemUtil.msg0003("A " + Util.getString("cerh_aba_bar_vazao_trecho_vazao_reduzida"));
						valido = false;
					}
				}
				if(isPodeResponderPotenciaInstaladaAnteriormente()){
					if(!isPotenciaInstaladaInformadaAnteriormenteRespondida()){
						MensagemUtil.msg0003(Util.getString("cerh_aba_bar_potencia_instalada"));
						valido = false;
					} 
					else if(isPotenciaInstaladaInformadaAnteriormente()){
						if(!isPontoDeIntervencaoSelecionado()){
							MensagemUtil.msg0003("selecionar ponto");
							valido = false;
						} 
						else {
							if(barragemAproveitamentoHidreletrico.getIndEmOperacao() && Util.isNullOuVazio(intervencaoDTO.getDataOperacao())){
								MensagemUtil.msg0003(Util.getString("cerh_aba_bar_dt_inicio_operacao"));
								valido = false;
							}
						}
					}
				} 
				else {
					if(Util.isNull(barragemAproveitamentoHidreletrico.getIndPch())){
						MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_aproveitamento_hidreletrico"));
						valido = false;
					} 
					else if (barragemAproveitamentoHidreletrico.getIndPch()) {
						if(Util.isNullOuVazio(barragemAproveitamentoHidreletrico.getValPotenciaInstaladaTotal())){
							MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_potencia_instalada_total"));
							valido = false;	
						}
						if(Util.isNull(barragemAproveitamentoHidreletrico.getIndEmOperacao())){
							MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_em_operacao"));
							valido = false;
						}
						else if(barragemAproveitamentoHidreletrico.getIndEmOperacao()){
							if(Util.isNullOuVazio(barragemAproveitamentoHidreletrico.getDtInicioOperacao())){
								MensagemUtil.msg0003(Util.getString("cerh_aba_bar_dt_inicio_operacao"));
								valido = false;
							}
							if(Util.isNullOuVazio(barragemAproveitamentoHidreletrico.getValProducaoAnualEfetivamenteVerificada())){
								MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_producao_anual_efetivamente_verificada"));
								valido = false;
							}
						}
					}
				}
			}
		}
		return valido;
	}

	@Override
	public void limparAssociativas(){
		for (CerhBarragemCaracterizacaoFinalidade cerhCaracterizacaoFinalidade : getFacade().listarCerhBarragemCaracterizacaoFinalidade(getCerhCaracterizacao())) {
			if(!getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection().contains(cerhCaracterizacaoFinalidade)) {
				getFacade().excluirCaracterizacaoFinalidade(cerhCaracterizacaoFinalidade);
			}
		}
	}

	@Override
	public void prepararCerhFinalidadeUsoAgua(CerhFinalidadeUsoAguaInterface cerhFinalidadeUsoAguaInterface) {
		CerhBarragemCaracterizacaoFinalidade caracterizacaoFinalidade = (CerhBarragemCaracterizacaoFinalidade) cerhFinalidadeUsoAguaInterface;
		if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()) {
			listarTipoAproveitamento();
			if(!Util.isNull(getIdeCerhBarragemAproveitamentoHidreletrico().getIndPotenciaInstaladaIntervencao()) 
					&& getIdeCerhBarragemAproveitamentoHidreletrico().getIndPotenciaInstaladaIntervencao()){
				listarPontosDeIntervencao();
				if(!isPontoDeIntervencaoSelecionado()){
					for(IntervencaoCaracterizacaoPontoInformadoDTO pontoDTO : listaIntervencaoDTO){
						if(pontoDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().equals(getIdeCerhBarragemAproveitamentoHidreletrico().getIdeCerhLocalizacaoGeograficaIntervencao())){
							intervencaoDTO = (IntervencaoCaracterizacaoPontoInformadoDTO) pontoDTO;
							intervencaoDTO.setIndSelecionado(true);
							break;
						}
					}
					if(!Util.isNull(intervencaoDTO)){
						listaIntervencaoDTO.clear();
						listaIntervencaoDTO.add(intervencaoDTO);
					}
				}
			}
		}
	}

	@Override
	public void prepararConsultar(CerhCaracterizacaoDTO caracterizacaoDTO){
		caracterizacaoDTO.getCerhLocalizacaoGeografica().setIdeCerhBarragemCaracterizacao(getFacade().carregarIdeCerhBarragemCaracterizacao(caracterizacaoDTO.getCerhLocalizacaoGeografica()));
	}

	public Collection<BarragemDTO> getBarragemCollection() {
		return barragemCollection;
	}

	public Collection<TipoBarragem> getTipoBarragemCollection() {
		return tipoBarragemCollection;
	}

	public Collection<TipoAproveitamentoHidreletrico> getTipoAproveitamentoHidreletricoCollection() {
		return tipoAproveitamentoHidreletricoCollection;
	}

	public String getNomBarragemPesquisa() {
		return nomBarragemPesquisa;
	}

	public void setNomBarragemPesquisa(String nomBarragemPesquisa) {
		this.nomBarragemPesquisa = nomBarragemPesquisa;
	}

	public IntervencaoCaracterizacaoPontoInformadoDTO getIntervencaoDTO() {
		return intervencaoDTO;
	}

	public List<IntervencaoCaracterizacaoPontoInformadoDTO> getListaIntervencaoDTO() {
		return listaIntervencaoDTO;
	}

	@Override
	public void armazenarHistorico() throws Exception {
		for(CerhCaracterizacaoDTO dto : getDto().getListaCaracterizacaoDTO()) {
			CerhBarragemCaracterizacao copia = (CerhBarragemCaracterizacao) dto.getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao().clone();
			prepararDialogIncluirCaracterizacao(dto, true);
			
			CerhBarragemCaracterizacao cerhBarragemCaracterizacao = (CerhBarragemCaracterizacao) cerhCaracterizacao;
			
			cerhBarragemCaracterizacao.setIdeObjetoPai(cerhBarragemCaracterizacao.getIdeCerhBarragemCaracterizacao());
			cerhBarragemCaracterizacao.setIdeCerhBarragemCaracterizacao(null);
			
			if(!Util.isNullOuVazio(cerhBarragemCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection())) {
				for(CerhBarragemCaracterizacaoFinalidade cbcf : cerhBarragemCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection()) {
					cbcf.setIdeObjetoPai(cbcf.getId());
					cbcf.setIdeCerhBarragemCaracterizacaoFinalidade(null);
					cbcf.setIdeCerhBarragemCaracterizacao(cerhBarragemCaracterizacao);
					
					if(!Util.isNull(cbcf.getIdeCerhBarragemAproveitamentoHidreletrico())) {
						cbcf.getIdeCerhBarragemAproveitamentoHidreletrico().setIdeObjetoPai(cbcf.getIdeCerhBarragemAproveitamentoHidreletrico().getId());
						
						cbcf.getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhBarragemAproveitamentoHidreletrico(null);
						cbcf.getIdeCerhBarragemAproveitamentoHidreletrico().setIdeCerhBarragemCaracterizacaoFinalidade(cbcf);
					}
				}
				
			}
			
			CerhProcesso cerhProcessoAntigo = cerhBarragemCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso();
			if(!Util.isNull(cerhProcessoAntigo)) {
				
				for(CerhProcesso cerhProcessoNovo : cerhDTO.getAbaDadoGerais().getNovoCerh().getCerhProcessoCollection()) {
					if(cerhProcessoAntigo.equals(cerhProcessoNovo.getCerhProcessoPai())) {
						
						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhBarragemCaracterizacao.getIdeCerhLocalizacaoGeografica().clone();
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);
						cloneCerhLocalizacaoGeografica.setIdeCerhProcesso(cerhProcessoNovo);
						
						for (CerhTipoUso cerhTipoUso : cerhProcessoNovo.getCerhTipoUsoCollection()) {
							if(cerhTipoUso.getIdeCerhProcesso().getIdeCerhProcesso().equals(cerhProcessoNovo.getIdeCerhProcesso())){
								cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
							}
						}
						
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
						cerhBarragemCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
					}
				}
			}
			else {
				for(CerhTipoUso ctu : cerhDTO.getAbaDadoGerais().getNovoCerh().getCerhTipoUsoCollection()) {
					if(ctu.getCerhTipoUsoPai().equals(cerhBarragemCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhTipoUso())) {
						
						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhBarragemCaracterizacao.getIdeCerhLocalizacaoGeografica().clone();
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.getIdeCerhBarragemCaracterizacao().setIdeCerhBarragemCaracterizacao(copia.getIdeCerhBarragemCaracterizacao());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);
						cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(ctu);
						
						LocalizacaoGeografica loc =  cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().clone();
						
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(loc));						
						
						cerhBarragemCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
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