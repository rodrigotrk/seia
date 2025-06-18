package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.BarragemCaracterizacaoPontoInformadoDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhTipoIntervencaoDTO;
import br.gov.ba.seia.entity.CerhBarragemAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.entity.CerhIntervencaoServico;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhObrasHidraulicas;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.CerhObrasHidraulicasEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhAbaIntervencaoFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public class CerhAbaIntervencaoController extends CerhAbaCaracterizacaoController {

	@EJB
	private CerhAbaIntervencaoFacade facade;
	
	private CerhTipoIntervencaoDTO cerhTipoIntervencaoDTO;
	private Collection<SelectItem> cerhTipoIntervencaoDTOCollection;
	private Collection<CerhObrasHidraulicas> cerhObrasHidraulicasCollection; 
	private Collection<CerhIntervencaoServico> cerhIntervencaoServicoCollection;

	private BarragemCaracterizacaoPontoInformadoDTO pontoBarragemDTO;
	private List<BarragemCaracterizacaoPontoInformadoDTO> listaPontoBarragemDTO;

	private Boolean unicoPontoDeBarragem;

	public CerhAbaIntervencaoController() {
	}
	
	public void confirmarNomeRio() {
		if(!Util.isNullOuVazio(getCerhCaracterizacao().getNomCorpoHidrico()) && !Util.isNullOuVazio(getCerhCaracterizacao().getIdeTipoCorpoHidrico())) {
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
	
	public void changeTipoIntervencao(){
		if(cerhTipoIntervencaoDTO.getIdeCerhTipoIntervencao().equals(1)){
			listarCerhObrasHidraulicas();
			limparServicos();
		} 
		else if(cerhTipoIntervencaoDTO.getIdeCerhTipoIntervencao().equals(2)){
			listarCerhIntervencaoServico();
			limparObrasHidraulicas();
		}
		else {
			limparObrasHidraulicas();
			limparServicos();
		} 
	}
	
	public void changeObrasHidraulicas(ValueChangeEvent event){
		CerhObrasHidraulicas newObra = (CerhObrasHidraulicas) event.getNewValue();
		CerhObrasHidraulicas oldObra = (CerhObrasHidraulicas) event.getOldValue();
		if(!Util.isNull(newObra)){
			if(!newObra.equals(oldObra) && newObra.isOutros()){
				MensagemLacFceUtil.exibirInformacao001();
			}
		} 
		else {
			limparObrasHidraulicas();
		}
	}

	private void limparObrasHidraulicas() {
		getCerhCaracterizacao().setIdeCerhObrasHidraulicas(null);
		getCerhCaracterizacao().setIndPch(null);
		getCerhCaracterizacao().setIndPotenciaInstaladaBarragem(null);
		getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(null);
		getCerhCaracterizacao().setValPotenciaInstaladaTotal(null);
		getCerhCaracterizacao().setIndOperacao(null);
		getCerhCaracterizacao().setDtInicioOperacao(null);
		getCerhCaracterizacao().setValProducaoAnualEfetivamenteVerificada(null);
	}
	
	public void changeIntervencaoServico(ValueChangeEvent event){
		CerhIntervencaoServico newServico = (CerhIntervencaoServico) event.getNewValue();
		CerhIntervencaoServico oldServico = (CerhIntervencaoServico) event.getOldValue();
		if(!Util.isNull(newServico)){
			if(!newServico.equals(oldServico) && newServico.isOutros()){
				MensagemLacFceUtil.exibirInformacao001();
			}
		} 
		else {
			limparServicos();
		}
	}

	private void limparServicos() {
		getCerhCaracterizacao().setIdeCerhIntervencaoServico(null);
	}
	
	public void changePontoBarragem() throws Exception {
		if(!Util.isNullOuVazio(listaPontoBarragemDTO)){
			pontoBarragemDTO = null;
			for(BarragemCaracterizacaoPontoInformadoDTO dto : listaPontoBarragemDTO){
				if(!Util.isNull(dto.getIndSelecionado()) && dto.getIndSelecionado()){
					pontoBarragemDTO = dto;
					break;
				}
			}
			listaPontoBarragemDTO.clear();
			if(!Util.isNull(pontoBarragemDTO)){
				listaPontoBarragemDTO.add(pontoBarragemDTO);
				getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(pontoBarragemDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica());
				getCerhCaracterizacao().setIndPch(pontoBarragemDTO.getIndPCH());
				getCerhCaracterizacao().setIndOperacao(pontoBarragemDTO.getIndOperacao());
			} 
			else {
				listarPontosDeBarragem();
				desmarcarTodosOsPontosBarragem();
				getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(null);
				getCerhCaracterizacao().setIndPch(null);
				getCerhCaracterizacao().setIndOperacao(null);
			}
		}
	}
	private void desmarcarTodosOsPontosBarragem(){
		for (BarragemCaracterizacaoPontoInformadoDTO barragem : listaPontoBarragemDTO) {
			barragem.setIndSelecionado(false);
		}
	}
	
	public void changePCH(ValueChangeEvent event){
		if(!Util.isNull(event.getNewValue()) && !(Boolean)event.getNewValue()){
			pontoBarragemDTO = null;
			getCerhCaracterizacao().setIndPotenciaInstaladaBarragem(null);
			getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(null);
			getCerhCaracterizacao().setValPotenciaInstaladaTotal(null);
			getCerhCaracterizacao().setIndOperacao(null);
			getCerhCaracterizacao().setDtInicioOperacao(null);
			getCerhCaracterizacao().setValProducaoAnualEfetivamenteVerificada(null);
		}
	}
	
	public void changePotenciaInstalada(ValueChangeEvent event){
		pontoBarragemDTO = null;
		getCerhCaracterizacao().setValPotenciaInstaladaTotal(null);
		getCerhCaracterizacao().setIndOperacao(null);
		getCerhCaracterizacao().setDtInicioOperacao(null);
		getCerhCaracterizacao().setValProducaoAnualEfetivamenteVerificada(null);
		if(!(Boolean)event.getNewValue()){
			getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(null);
		} 
		else {
 			listarPontosDeBarragem();
			if(isPontoDeBarragemSelecionado()){
				getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(pontoBarragemDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica());
				getCerhCaracterizacao().setIndPch(pontoBarragemDTO.getIndPCH());
				getCerhCaracterizacao().setIndOperacao(pontoBarragemDTO.getIndOperacao());
			}
		}
		
	}

	public void changeOperacao(ValueChangeEvent event){
		if(!Util.isNull(event.getNewValue()) && !(Boolean)event.getNewValue()){
			getCerhCaracterizacao().setDtInicioOperacao(null);
			getCerhCaracterizacao().setValProducaoAnualEfetivamenteVerificada(null);
		}
	}

	public boolean isPodeResponderPotenciaInstaladaAnteriormente(){
		return isFazAproveitamentoHidreletrico() && existemPontosDeBarragem();
	}
	
	public boolean isFazAproveitamentoHidreletrico(){
		return !Util.isNull(getCerhCaracterizacao().getIndPch()) && getCerhCaracterizacao().getIndPch();
	}
	
	public boolean isPotenciaInstaladaInformadaAnteriormenteRespondida(){
		return !Util.isNull(getCerhCaracterizacao().getIndPotenciaInstaladaBarragem());
	}
	
	public boolean isPotenciaInstaladaInformadaAnteriormente(){
		return isPotenciaInstaladaInformadaAnteriormenteRespondida() && getCerhCaracterizacao().getIndPotenciaInstaladaBarragem();
	}
	
	public boolean isOperacaoRespondida(){
		return !Util.isNull(getCerhCaracterizacao().getIndOperacao());
	}
	
	public boolean isOperacao(){
		return isOperacaoRespondida() && getCerhCaracterizacao().getIndOperacao();
	}
	
	public boolean isTipoIntervencaoNecessitaInformacoesDeUso() {
		return isTipoIntervencaoObrasHidraulicas() 
				|| isTipoIntervencaoServicos();
	}

	private boolean isTipoIntervencaoSelecionado(){
		return !Util.isNull(cerhTipoIntervencaoDTO.getIdeCerhTipoIntervencao());
	}
	
	public boolean isTipoIntervencaoServicos() {
		return isTipoIntervencaoSelecionado() && cerhTipoIntervencaoDTO.getIdeCerhTipoIntervencao().equals(2);
	}

	public boolean isTipoIntervencaoObrasHidraulicas() {
		return isTipoIntervencaoSelecionado() && cerhTipoIntervencaoDTO.getIdeCerhTipoIntervencao().equals(1);
	}
	
	public boolean isObraHidraulicaNecessitaResposta(){
		return !Util.isNull(getCerhCaracterizacao().getIdeCerhObrasHidraulicas()) 
				&& (getCerhCaracterizacao().getIdeCerhObrasHidraulicas().getIdeCerhObrasHidraulicas().equals(CerhObrasHidraulicasEnum.RETIFICACAO_CANALIZACAO.getIde()) 
						|| getCerhCaracterizacao().getIdeCerhObrasHidraulicas().getIdeCerhObrasHidraulicas().equals(CerhObrasHidraulicasEnum.DESVIO.getIde())); 	
	}

	public boolean isPontoDeBarragemSelecionado(){
		return !Util.isNull(pontoBarragemDTO);
	}
	
	private boolean existemPontosDeBarragem() {
		return !Util.isNullOuVazio(listaPontoBarragemDTO);
	}
	
	public Boolean getUnicoPontoDeBarragem() {
		return !Util.isNull(unicoPontoDeBarragem) && unicoPontoDeBarragem;
	}
	
	public boolean isDeveResponderPotenciaInstaladaTotal(){
		return isFazAproveitamentoHidreletrico() && (!isPodeResponderPotenciaInstaladaAnteriormente() || (isPodeResponderPotenciaInstaladaAnteriormente() && isPotenciaInstaladaInformadaAnteriormenteRespondida() && !isPotenciaInstaladaInformadaAnteriormente()));
	}
	
	public String getHeader(){
		return Util.getString("cerh_informacaoes_uso", obterDescrição(getCerhTipoIntervencaoDTO().getIdeCerhTipoIntervencao()));
	}
	
	private String obterDescrição(Integer ide){
		for (SelectItem selectItem : cerhTipoIntervencaoDTOCollection) {
			if(selectItem.getValue().equals(ide)){
				return selectItem.getLabel(); 
			}
		}
		return "";
	}
	
	@Override
	public CerhAbaIntervencaoFacade getFacade() {
		return facade;
	}

	@Override
	public TipoUsoRecursoHidricoEnum getTipoUsoRecursoHidricoEnum() {
		return TipoUsoRecursoHidricoEnum.INTERVENCAO;
	}

	@Override
	public CerhIntervencaoCaracterizacao obterCaracterizacaoMontada(CerhLocalizacaoGeografica clg)  {
		
		CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao = getFacade().carregarCerhIntervencaoCaracterizacao(clg);
		
		if(!Util.isNull(cerhIntervencaoCaracterizacao)){
			cerhIntervencaoCaracterizacao.setIdeCerhLocalizacaoGeografica(clg);
			cerhIntervencaoCaracterizacao.setIdeCerhSituacaoTipoUso(getFacade().carregarCerhSituacaoTipoUso(cerhIntervencaoCaracterizacao.getIdeCerhSituacaoTipoUso().getIdeCerhSituacaoTipoUso()));
			cerhIntervencaoCaracterizacao.setIdeCerhIntervencaoServico(getFacade().carregarCerhIntervencaoServico(cerhIntervencaoCaracterizacao));
			cerhIntervencaoCaracterizacao.setIdeCerhObrasHidraulicas(getFacade().carregarCerhObrasHidraulicas(cerhIntervencaoCaracterizacao));
			
			if(!Util.isNull(cerhIntervencaoCaracterizacao.getIdeTipoCorpoHidrico())) {
				cerhIntervencaoCaracterizacao.setIdeTipoCorpoHidrico(getFacade().carregarTipoCorpoHidrico(cerhIntervencaoCaracterizacao.getIdeTipoCorpoHidrico().getIdeTipoCorpoHidrico()));
			}
			if(!Util.isNull(cerhIntervencaoCaracterizacao.getIdeCerhObrasHidraulicas())){
				cerhTipoIntervencaoDTO = new CerhTipoIntervencaoDTO(1, "Obras Hidráulicas");
				if(!Util.isNullOuVazio(listaPontoBarragemDTO) && !Util.isNull(cerhIntervencaoCaracterizacao.getIndPotenciaInstaladaBarragem()) && cerhIntervencaoCaracterizacao.getIndPotenciaInstaladaBarragem()){
					for(BarragemCaracterizacaoPontoInformadoDTO dto : listaPontoBarragemDTO){
						if(dto.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().equals(cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeograficaBarragem())){
							pontoBarragemDTO = dto;
							pontoBarragemDTO.setIndSelecionado(true);
							break;
						}
					}
					listaPontoBarragemDTO.clear();
					listaPontoBarragemDTO.add(pontoBarragemDTO);
				}
				
			}
			else if(!Util.isNull(cerhIntervencaoCaracterizacao.getIdeCerhIntervencaoServico())){
				cerhTipoIntervencaoDTO = new CerhTipoIntervencaoDTO(2, "Serviços");
			}
			else {
				cerhTipoIntervencaoDTO = new CerhTipoIntervencaoDTO(3, "Aquicultura em tanque rede");
			}
		}
		
		
		return cerhIntervencaoCaracterizacao;
	}

	@Override
	public void prepararDialogIncluirCaracterizacao(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		cerhTipoIntervencaoDTO = new CerhTipoIntervencaoDTO();
		
		super.listarTipoCorpoHidrico();
		listarTipoIntervencao();
		listarCerhObrasHidraulicas();
		listarCerhIntervencaoServico();
		
		super.prepararDialog(caracterizacaoDTO, visualizacao);
		if(!Util.isNull(getCerhCaracterizacao()) && !Util.isNull(getCerhCaracterizacao().getIdeCerhIntervencaoCaracterizacao())) {
			super.dto.getCaracterizacaoDTO().setConfirmaNomeRio(true);
		}
		
		listarPontosDeBarragem();
	}

	public void closeDialog(){
		cerhTipoIntervencaoDTO =null; 
	}
	
	private void listarTipoIntervencao() {
		if(Util.isNullOuVazio(cerhTipoIntervencaoDTOCollection)){
			cerhTipoIntervencaoDTOCollection = new ArrayList<SelectItem>();
			cerhTipoIntervencaoDTOCollection.add(new SelectItem(1, "Obras Hidráulicas"));
			cerhTipoIntervencaoDTOCollection.add(new SelectItem(2, "Serviços"));
			cerhTipoIntervencaoDTOCollection.add(new SelectItem(3, "Aquicultura em tanque rede"));
		}
	}
	
	private void listarCerhObrasHidraulicas(){
		if(Util.isNullOuVazio(cerhObrasHidraulicasCollection)){
			try {
				cerhObrasHidraulicasCollection = facade.listarObrasHidraulicas();
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_int_obras_hidraulicas") + ".");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}
	
	private void listarCerhIntervencaoServico(){
		if(Util.isNullOuVazio(cerhIntervencaoServicoCollection)){
			try {
				cerhIntervencaoServicoCollection = facade.listarServicos();
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_int_servicos") + ".");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}

	private void listarPontosDeBarragem() {
		listaPontoBarragemDTO = new ArrayList<BarragemCaracterizacaoPontoInformadoDTO>();
		unicoPontoDeBarragem = null;
		if(!Util.isNull(super.cerhDTO.getAba(TipoUsoRecursoHidricoEnum.BARRAGEM)) && 
		   !Util.isNullOuVazio(super.cerhDTO.getAba(TipoUsoRecursoHidricoEnum.BARRAGEM).getListaCaracterizacaoDTO())){
			for (CerhCaracterizacaoDTO caracterizacaoIntervencaoDTO : super.cerhDTO.getAba(TipoUsoRecursoHidricoEnum.BARRAGEM).getListaCaracterizacaoDTO()) {
				if(!Util.isNull(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao()) && !Util.isNull(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao().getId())){
					
					CerhBarragemCaracterizacao barragem = null;
					if(Util.isNullOuVazio(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection())){
						barragem = obterCerhBarragemCaracterizacao(caracterizacaoIntervencaoDTO);
					} 
					else {
						barragem = caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica().getIdeCerhBarragemCaracterizacao();
					}
					for(CerhBarragemCaracterizacaoFinalidade finalidade : barragem.getCerhBarragemCaracterizacaoFinalidadeCollection()){
						if(finalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()){
							CerhBarragemAproveitamentoHidreletrico hidreletrico = finalidade.getIdeCerhBarragemAproveitamentoHidreletrico();
							if(!Util.isNull(hidreletrico)
									&& (Util.isNull(hidreletrico.getIndPotenciaInstaladaIntervencao()) || (!Util.isNull(hidreletrico.getIndPotenciaInstaladaIntervencao()) && !hidreletrico.getIndPotenciaInstaladaIntervencao())) 
									&& !Util.isNull(hidreletrico.getIndPch())
									&& !Util.isNullOuVazio(hidreletrico.getValPotenciaInstaladaTotal())
									){
								barragem.setIdeCerhLocalizacaoGeografica(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica());
								listaPontoBarragemDTO.add(new BarragemCaracterizacaoPontoInformadoDTO(barragem));
							}
						}
					}
				}
			}
		}
		if(existemPontosDeBarragem() && listaPontoBarragemDTO.size() == 1){
			unicoPontoDeBarragem = true;
			pontoBarragemDTO = (BarragemCaracterizacaoPontoInformadoDTO) listaPontoBarragemDTO.get(0);
			pontoBarragemDTO.setIndSelecionado(true);
			getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(pontoBarragemDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica());
		}
		 else if(existemPontosDeBarragem() && !Util.isNullOuVazio(listaPontoBarragemDTO)) {
			if(!Util.isNullOuVazio(getCerhCaracterizacao().getIdeCerhLocalizacaoGeograficaBarragem())) {
				for (BarragemCaracterizacaoPontoInformadoDTO barragem : listaPontoBarragemDTO) {
					if (barragem.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getId().equals(getCerhCaracterizacao().getIdeCerhLocalizacaoGeograficaBarragem().getId())) {
						pontoBarragemDTO = barragem;
						pontoBarragemDTO.setIndSelecionado(true);
						getCerhCaracterizacao().setIdeCerhLocalizacaoGeograficaBarragem(pontoBarragemDTO.getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica());
						break;
					}
				}
			}
		}
	}

	private CerhBarragemCaracterizacao obterCerhBarragemCaracterizacao(CerhCaracterizacaoDTO caracterizacaoIntervencaoDTO) {
		try {
			getFacade().prepararLocalizacaoGeografica(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica());
			CerhBarragemCaracterizacao cerhBarragemCaracterizacao = facade.carregarCerhBarragemCaracterizacao(caracterizacaoIntervencaoDTO.getCerhLocalizacaoGeografica());
			cerhBarragemCaracterizacao.setCerhBarragemCaracterizacaoFinalidadeCollection(facade.listarCerhBarragemCaracterizacaoFinalidade(cerhBarragemCaracterizacao));
			if(!Util.isNullOuVazio(cerhBarragemCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection())){
				for (CerhBarragemCaracterizacaoFinalidade cerhBarragemCaracterizacaoFinalidade : cerhBarragemCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection()) {
					if(cerhBarragemCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()){
						obterCerhFinalidadeAproveitamentoHidreletrico(cerhBarragemCaracterizacaoFinalidade);
					}
				}
			}
			return cerhBarragemCaracterizacao;
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	private void obterCerhFinalidadeAproveitamentoHidreletrico(CerhBarragemCaracterizacaoFinalidade cerhBarragemCaracterizacaoFinalidade) throws Exception {
		cerhBarragemCaracterizacaoFinalidade.setIdeCerhBarragemAproveitamentoHidreletrico(facade.carregarCerhBarragemAproveitamentoHidreletrico(cerhBarragemCaracterizacaoFinalidade));
	}

	@Override
	public void fecharDialogCaracterizacao() {
		Html.esconder("dlgCaracterizacaoIntervencao");
	}

	@Override
	public boolean validarCaracterizacao() {
		boolean valido = true;
		if(!dto.getCaracterizacaoDTO().isConfirmaNomeRio()) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040") + " o " + Util.getString("cerh_aba_cap_sup_nom_hidrico") + ".");
			valido = false;
		}
		if(Util.isNull(getCerhTipoIntervencaoDTO()) || (!Util.isNull(getCerhTipoIntervencaoDTO()) && getCerhTipoIntervencaoDTO().getIdeCerhTipoIntervencao() == 0)){ 
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_int_tipo"));
			valido = false;
		}
		if(Util.isNull(getCerhCaracterizacao().getIdeCerhSituacaoTipoUso())){
			MensagemUtil.msg0003("O " + Util.getString("cerh_aba_int_situacao"));
			valido = false;
		}
		else if(isTipoIntervencaoObrasHidraulicas()){
			if(Util.isNull(getCerhCaracterizacao().getIdeCerhObrasHidraulicas())){
				MensagemUtil.msg0003("A " + Util.getString("cerh_aba_int_obras_hidraulicas"));
				valido = false;
			} 
			else if(isObraHidraulicaNecessitaResposta()){
				if(Util.isNull(getCerhCaracterizacao().getIndPch())){
					MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_aproveitamento_hidreletrico"));
					valido = false;
				} 
				else if(getCerhCaracterizacao().getIndPch()){
					if(isPodeResponderPotenciaInstaladaAnteriormente()){
						if(!isPotenciaInstaladaInformadaAnteriormenteRespondida()){
							MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_potencia_instalada"));
							valido = false;
						} 
						else if(!isPotenciaInstaladaInformadaAnteriormente()){
							if(Util.isNullOuVazio(getCerhCaracterizacao().getValPotenciaInstaladaTotal())){
								MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_potencia_instalada_total"));
								valido = false;
							}
							if(Util.isNull(getCerhCaracterizacao().getIndOperacao())){
								MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_em_operacao"));
								valido = false;
							}
							else if(getCerhCaracterizacao().getIndOperacao()){
								if(Util.isNullOuVazio(getCerhCaracterizacao().getDtInicioOperacao())){
									MensagemUtil.msg0003(Util.getString("cerh_aba_bar_dt_inicio_operacao"));
									valido = false;
								}
								if(!isPontoDeBarragemSelecionado() && Util.isNullOuVazio(getCerhCaracterizacao().getValProducaoAnualEfetivamenteVerificada())){
									MensagemUtil.msg0003(Util.getString("cerh_aba_int_oh_producao_anual_efetivamente_verificada"));
									valido = false;
								}
							}	
						} 
						else {
							if(!isPontoDeBarragemSelecionado()){
								MensagemUtil.msg0003("selecionar ponto");
								valido = false;
							}
						}
					} 
				}
			}
			else if(isTipoIntervencaoServicos()){
				if(Util.isNull(getCerhCaracterizacao().getIdeCerhIntervencaoServico())){
					MensagemUtil.msg0003("O Serviço");
					valido = false;
				}
			}
		}
		return valido;
	}

	@Override
	public boolean existeOutros() {
		if(!Util.isNull(getCerhCaracterizacao()) && 
				!Util.isNull(getCerhCaracterizacao().getIdeCerhObrasHidraulicas()) 
				&& getCerhCaracterizacao().getIdeCerhObrasHidraulicas().isOutros()){
			return true;
		}
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
	public CerhIntervencaoCaracterizacao getCerhCaracterizacao() {
		if(Util.isNull(super.cerhCaracterizacao)){
			super.cerhCaracterizacao = new CerhIntervencaoCaracterizacao(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
			dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().setIdeCerhIntervencaoCaracterizacao((CerhIntervencaoCaracterizacao) super.cerhCaracterizacao);
		}
		return (CerhIntervencaoCaracterizacao) super.cerhCaracterizacao;
	}
	
	@Override
	public void excluirCaracterizacao() {
		
		try {
			super.preparaCerhCaracterizacaoParaExclusao();
			if(getCerhCaracterizacao()!=null){
				excluirCaracterizacao(getCerhCaracterizacao());
			}
			
			dto.getListaCaracterizacaoDTO().remove(dto.getCaracterizacaoDTO());
			MensagemUtil.sucesso("Caracterização excluída com sucesso!");
		} 
		catch (SeiaValidacaoRuntimeException e) {
			MensagemUtil.erro(e.getMessage());	
		}
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_excluir") + " a Caracterização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@Override
	public void excluirCaracterizacao(CerhCaracterizacaoInterface caracterizacaoInterface)  {
		CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao;
		
		if(!Util.isNullOuVazio(super.dto.getCaracterizacaoDTO())) {
			cerhIntervencaoCaracterizacao = ((CerhIntervencaoCaracterizacao) getCerhCaracterizacao(super.dto.getCaracterizacaoDTO()));
		}else {
			cerhIntervencaoCaracterizacao = (CerhIntervencaoCaracterizacao) caracterizacaoInterface;
		}
		
		if(cerhIntervencaoCaracterizacao==null){
			getFacade().excluirCerhLocalizacaoGeografica(new CerhLocalizacaoGeografica(super.dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
		}else{
			
			if(cerhIntervencaoCaracterizacao.getIdeCerhIntervencaoCaracterizacao()!=null){
				cerhIntervencaoCaracterizacao = getFacade().carregarCerhIntervencaoCaracterizacao(cerhIntervencaoCaracterizacao);
			}
			List<CerhBarragemAproveitamentoHidreletrico> cerhBarragemAproveitamentoHidreletrico = new ArrayList<CerhBarragemAproveitamentoHidreletrico>();
					
			if(cerhIntervencaoCaracterizacao!=null && 
			   cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeografica()!= null &&
			   cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhLocalizacaoGeografica()!=null){
			   cerhBarragemAproveitamentoHidreletrico = (List<CerhBarragemAproveitamentoHidreletrico>) getFacade().carregarCerhBarragemAproveitamentoHidreletrico(cerhIntervencaoCaracterizacao);
			}
			
			/* Verifica se existe alguma Barragem vinculada com a Intervenção que está querendo excluir. 
			 * Se existir, não permite a exclusão da Intervenção. */
			if (!cerhBarragemAproveitamentoHidreletrico.isEmpty()) {
				throw new SeiaValidacaoRuntimeException(Util.getString("cerh_caracterizacao_msg_erro_barragem_intervencao_com_vinculo"));
			} else {
				getFacade().excluirCaracterizacao(caracterizacaoInterface);
			}
		}
	}

	@Override
	public void limparAssociativas()  {

	}

	@Override
	public void prepararConsultar(CerhCaracterizacaoDTO caracterizacaoDTO){
		caracterizacaoDTO.getCerhLocalizacaoGeografica().setIdeCerhIntervencaoCaracterizacao(getFacade().carregarIdeCerhIntervencaoCaracterizacao(caracterizacaoDTO.getCerhLocalizacaoGeografica()));
	}
	
	@Override
	public CerhIntervencaoCaracterizacao getCerhCaracterizacao(CerhCaracterizacaoDTO dto) {
		return dto.getCerhLocalizacaoGeografica().getIdeCerhIntervencaoCaracterizacao();
	}

	public Collection<CerhIntervencaoServico> getCerhIntervencaoServicoCollection() {
		return cerhIntervencaoServicoCollection;
	}

	public Collection<CerhObrasHidraulicas> getCerhObrasHidraulicasCollection() {
		return cerhObrasHidraulicasCollection;
	}
	
	public Collection<SelectItem> getCerhTipoIntervencaoDTOCollection() {
		return cerhTipoIntervencaoDTOCollection;
	}

	public CerhTipoIntervencaoDTO getCerhTipoIntervencaoDTO() {
		return cerhTipoIntervencaoDTO;
	}

	public void setCerhTipoIntervencaoDTO(CerhTipoIntervencaoDTO cerhTipoIntervencaoDTO) {
		this.cerhTipoIntervencaoDTO = cerhTipoIntervencaoDTO;
	}
	
	public BarragemCaracterizacaoPontoInformadoDTO getPontoBarragemDTO() {
		return pontoBarragemDTO;
	}

	public List<BarragemCaracterizacaoPontoInformadoDTO> getListaPontoBarragemDTO() {
		return listaPontoBarragemDTO;
	}

	@Override
	public void armazenarHistorico() throws Exception {
		
		for(CerhCaracterizacaoDTO dto : getDto().getListaCaracterizacaoDTO()) {
			CerhIntervencaoCaracterizacao copia = (CerhIntervencaoCaracterizacao) dto.getCerhLocalizacaoGeografica().getIdeCerhIntervencaoCaracterizacao().clone();
			prepararDialogIncluirCaracterizacao(dto, true);
			
			CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao = (CerhIntervencaoCaracterizacao) cerhCaracterizacao;
			
			cerhIntervencaoCaracterizacao.setIdeObjetoPai(cerhIntervencaoCaracterizacao.getIdeCerhIntervencaoCaracterizacao());
			cerhIntervencaoCaracterizacao.setIdeCerhIntervencaoCaracterizacao(null);
			
			CerhProcesso cerhProcessoAntigo = cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso();
			if(!Util.isNull(cerhProcessoAntigo)) {
				for(CerhProcesso cerhProcessoNovo : cerhDTO.getAbaDadoGerais().getNovoCerh().getCerhProcessoCollection()) {
					if(cerhProcessoAntigo.equals(cerhProcessoNovo.getCerhProcessoPai())) {
						
						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeografica().clone();
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);

						cloneCerhLocalizacaoGeografica.setIdeCerhProcesso(cerhProcessoNovo);
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
						
						for (CerhTipoUso cerhTipoUso : cerhProcessoNovo.getCerhTipoUsoCollection()) {
							if(cerhTipoUso.getIdeCerhProcesso().getIdeCerhProcesso().equals(cerhProcessoNovo.getIdeCerhProcesso())){
								cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
							}
						}
						
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
						cerhIntervencaoCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
					}
				}
			}
			else {
				for(CerhTipoUso ctu : cerhDTO.getAbaDadoGerais().getNovoCerh().getCerhTipoUsoCollection()) {
					if(ctu.getCerhTipoUsoPai().equals(cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhTipoUso())) {
						
						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeografica().clone();
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.getIdeCerhIntervencaoCaracterizacao().setIdeCerhIntervencaoCaracterizacao(copia.getIdeCerhIntervencaoCaracterizacao());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);
						cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(ctu);
						
						LocalizacaoGeografica loc =  cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().clone();
						
						cloneCerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(loc));						
						
						cerhIntervencaoCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
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