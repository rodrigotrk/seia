package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CerhAbaDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhSituacaoRegularizacao;
import br.gov.ba.seia.entity.CerhSituacaoTipoUso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoCorpoHidrico;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.CerhSituacaoRegularizacaoEnum;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhAbasFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;
/**
 * @author eduardo.fernandes 
 * @since 29/04/2017
 *
 */
public abstract class CerhAbaCaracterizacaoController extends CerhAbaController implements CadastroIncompletoInterface {
	 
	private Collection<CerhSituacaoTipoUso> cerhSituacaoTipoUsoCollection;
	private Collection<TipoCorpoHidrico> tipoCorpoHidricoCollection;
	private CerhCaracterizacaoDTO clone;
	
	protected MetodoUtil metodo;
	protected CerhAbaDTO dto;
	protected CerhCaracterizacaoInterface cerhCaracterizacao; 
	
	public abstract CerhAbasFacade getFacade();
	public abstract TipoUsoRecursoHidricoEnum getTipoUsoRecursoHidricoEnum();
	public abstract CerhCaracterizacaoInterface obterCaracterizacaoMontada(CerhLocalizacaoGeografica clg) throws Exception ;
	public abstract CerhCaracterizacaoInterface obterCaracterizacaoMontadaHistorico(CerhLocalizacaoGeografica clg) throws Exception ;
	public abstract CerhCaracterizacaoInterface getCerhCaracterizacao();
	public abstract CerhCaracterizacaoInterface getCerhCaracterizacao(CerhCaracterizacaoDTO dto);
	
	public abstract void prepararDialogIncluirCaracterizacao(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao)throws Exception  ;
	public abstract void prepararDialogIncluirCaracterizacaoHistorico(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao)throws Exception  ;
	public abstract void prepararConsultar(CerhCaracterizacaoDTO caracterizacaoDTO);
	public abstract void excluirCaracterizacao(CerhCaracterizacaoInterface caracterizacaoInterface)  ;
	public abstract void limparAssociativas() ;
	public abstract void fecharDialogCaracterizacao();
	
	public abstract boolean validarCaracterizacao();
	public abstract boolean existeOutros();
	public abstract boolean isExisteOutrosUsoAlemDoProcesso();

	public void load(CerhDTO cerhDTO) {
		try {

			if(Util.isNull(dto)) {
				this.cerhDTO = cerhDTO;
				montarDTO();
				metodo = new MetodoUtil(this, "adicionarCoordenada");
			}
		} 
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + " as informações da aba.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void clean(){
		dto = null;
	}
	
	
	public void limparCaracterizacao() {
		CerhCaracterizacaoDTO dtoToRemove = null;
		Integer index = null;
		for (int i = 0; i < dto.getListaCaracterizacaoDTO().size(); i++) {
			if(dto.getListaCaracterizacaoDTO().get(i).getCerhLocalizacaoGeografica().equals(clone.getCerhLocalizacaoGeografica())){
				dtoToRemove = dto.getListaCaracterizacaoDTO().get(i); 
				index = i;
				break;
			}
		}
		if(!Util.isNull(index) && !Util.isNull(clone.getCerhLocalizacaoGeografica().getIdeCerhLocalizacaoGeografica())){
			dto.getListaCaracterizacaoDTO().remove(dtoToRemove);
			dto.getListaCaracterizacaoDTO().add(index, clone);
		} 
		else if(Util.isNull(clone.getCerhLocalizacaoGeografica())){
			if(dto.getListaCaracterizacaoDTO().get(index).getCerhLocalizacaoGeografica()!=null){
				if (Util.isNull(dto.getListaCaracterizacaoDTO().get(index).getCerhLocalizacaoGeografica().getIdeCerhCaptacaoCaracterizacao().getId())) {
					dto.getListaCaracterizacaoDTO().get(index).getCerhLocalizacaoGeografica().setIdeCerhCaptacaoCaracterizacao(null);
				}
			}
			
		} else if (Util.isNull(clone.getCerhLocalizacaoGeografica())){
			clone = null;
		}
		dto.setCaracterizacaoDTO(null);
	}

	private void montarDTO() throws Exception {
		
		if(Util.isNull(this.cerhDTO.getAbas())) {
			this.cerhDTO.setAbas(new ArrayList<CerhAbaDTO>());
		}
		
		dto = new CerhAbaDTO(getTipoUsoRecursoHidricoEnum());
		
		if(!Util.isNullOuVazio(getCerh().getCerhProcessoCollection())) {
			montarDTOporProcesso();
		}
		
		if(!this.cerhDTO.isCadastrar()) {
			if(!Util.isNullOuVazio(getCerh().getCerhTipoUsoCollection())) {
				montarDTOporRespostaTipoUso();
			}
		}
		
		if(!Util.isNullOuVazio(dto.getListaCaracterizacaoDTO())){
			for (CerhCaracterizacaoDTO caracterizacaoDTO : dto.getListaCaracterizacaoDTO()) {
				tratarPonto(caracterizacaoDTO);
				prepararConsultar(caracterizacaoDTO);
			}
		}
		
		this.cerhDTO.getAbas().add(dto);
	}

	protected void montarDTOporProcesso() {
		for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
			if(!Util.isNullOuVazio(cerhProcesso.getCerhTipoUsoCollection())) {
				for (CerhTipoUso cerhTipoUso : cerhProcesso.getCerhTipoUsoCollection()) {
					if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(getTipoUsoRecursoHidricoEnum()))) {
						if(!Util.isNullOuVazio(cerhTipoUso.getCerhLocalizacaoGeograficaCollection())) {
							
							for (CerhLocalizacaoGeografica cerhLocalizacaoGeografica : cerhTipoUso.getCerhLocalizacaoGeograficaCollection()) {
							//	cerhLocalizacaoGeografica.setIdeCerhProcesso(cerhProcesso);
								cerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
								if(!Util.isNullOuVazio(cerhLocalizacaoGeografica.getIdeCerhProcesso())) {
									if(cerhLocalizacaoGeografica.getIdeCerhProcesso().equals(cerhProcesso)) {
										dto.getListaCaracterizacaoDTO().add(new CerhCaracterizacaoDTO(cerhLocalizacaoGeografica, cerhProcesso));
									}
								}
							}
						} 
						else {
							Collection<CerhLocalizacaoGeografica> listarCerhLocalizacaoGeografica = listarCerhProcesso(cerhProcesso);
							if(!Util.isNullOuVazio(listarCerhLocalizacaoGeografica)) {
								for (CerhLocalizacaoGeografica cerhLocalizacaoGeografica : listarCerhLocalizacaoGeografica) {
									CerhCaracterizacaoDTO ccDTO = new CerhCaracterizacaoDTO(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica(), cerhTipoUso);
									if(!dto.getListaCaracterizacaoDTO().contains(ccDTO)) {
										dto.getListaCaracterizacaoDTO().add(ccDTO);
									}
								}
							} 
						}
					}
				}
			} 
		}
	}
	
	public Collection<CerhLocalizacaoGeografica> listarCerhProcesso(CerhProcesso cerhProcesso) {
		try {
			return getFacade().listarCerhLocalizacaoGeografica(cerhProcesso, getTipoUsoRecursoHidricoEnum());
		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + " as informações do processo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	protected void montarDTOporRespostaTipoUso() throws Exception {
		for (CerhTipoUso cerhTipoUso : getCerh().getCerhTipoUsoCollection()) {
			if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(getTipoUsoRecursoHidricoEnum()))) {
				if(!Util.isNullOuVazio(cerhTipoUso.getCerhLocalizacaoGeograficaCollection())) {
					for (CerhLocalizacaoGeografica cerhLocalizacaoGeografica : cerhTipoUso.getCerhLocalizacaoGeograficaCollection()) {
						cerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
						dto.getListaCaracterizacaoDTO().add(new CerhCaracterizacaoDTO(cerhLocalizacaoGeografica));
					}
				} 
			}
		}
		
		removerCoordenadasDuplicadas();
		
	}
	
	private void removerCoordenadasDuplicadas() {
		List<CerhLocalizacaoGeografica> listaNova;
		if(!Util.isNullOuVazio(dto.getListaCaracterizacaoDTO())) {
			listaNova = new ArrayList<CerhLocalizacaoGeografica>();
			
			Iterator i = dto.getListaCaracterizacaoDTO().iterator();
			
			while(i.hasNext()){
				CerhCaracterizacaoDTO elementoAtual = (CerhCaracterizacaoDTO) i.next();
				if(!Util.isNullOuVazio(listaNova)) {
					if(listaNova.contains(elementoAtual.getCerhLocalizacaoGeografica())) {
						i.remove();
					}else {
						if(!Util.isNullOuVazio(elementoAtual.getCerhLocalizacaoGeografica().getIdeCerhLocalizacaoGeografica())) {
							listaNova.add(elementoAtual.getCerhLocalizacaoGeografica());
						}else {
							i.remove();
						}
					}
				}else {
					if(!Util.isNullOuVazio(elementoAtual.getCerhLocalizacaoGeografica().getIdeCerhLocalizacaoGeografica())) {
						listaNova.add(elementoAtual.getCerhLocalizacaoGeografica());
					}else {
						i.remove();
					}
				}
				
			}
		}
		System.out.print("teste");
	}

	public void adicionarCoordenada() {
		if(!Util.isNull(dto.getLocalizacaoGeografica()) && getFacade().isTheGeomPersistido(dto.getLocalizacaoGeografica())) {
			CerhCaracterizacaoDTO cerhCapDTO = new CerhCaracterizacaoDTO(dto.getLocalizacaoGeografica(), getCerhTipoUso());
			dto.getListaCaracterizacaoDTO().add(cerhCapDTO);
		}
		dto.setLocalizacaoGeografica(new LocalizacaoGeografica());
	}

	public void changeProcesso(AjaxBehaviorEvent event) {
		CerhCaracterizacaoDTO caracterizacaoDto = ((CerhCaracterizacaoDTO) event.getComponent().getAttributes().get("caracterizacaoDto"));
		if(!Util.isNullOuVazio(caracterizacaoDto)) {
			if(!Util.isNull(caracterizacaoDto.getCerhLocalizacaoGeografica().getIdeCerhProcesso())) {
				for(CerhTipoUso cerhTipoUso : caracterizacaoDto.getCerhLocalizacaoGeografica().getIdeCerhProcesso().getCerhTipoUsoCollection()) {
					if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(getTipoUsoRecursoHidricoEnum()))) {
						caracterizacaoDto.getCerhLocalizacaoGeografica().setIdeCerhTipoUso(cerhTipoUso);
						break;
					}
				}
				
				if(caracterizacaoDto.getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao()!=null){
					if(caracterizacaoDto.getCerhLocalizacaoGeografica().getIdeCerhCaptacaoCaracterizacao()!=null){
						caracterizacaoDto.getCerhLocalizacaoGeografica().getIdeCerhCaptacaoCaracterizacao().setDtInicioCaptacao(
								caracterizacaoDto.getCerhLocalizacaoGeografica().getIdeCerhProcesso().getDtInicioAutorizacao()
								);
					}
				}
			} 
			
			else {
				if(Util.isNull(getCerhTipoUso())){
					caracterizacaoDto.getCerhLocalizacaoGeografica().setIdeCerhTipoUso(new CerhTipoUso(getCerh(), getTipoUsoRecursoHidricoEnum()));
				}
				else {
					caracterizacaoDto.getCerhLocalizacaoGeografica().setIdeCerhTipoUso(getCerhTipoUso());
				}
			}
		}
		
		Html.atualizar(event.getComponent().getClientId().replace(event.getComponent().getId(), "caracterizacao"));
	}

	public void converterPontoGeografico(ActionEvent event) {
		Collection<LocalizacaoGeografica> listaLocalizacaoGeografica = new ArrayList<LocalizacaoGeografica>();
		for (CerhCaracterizacaoDTO dto : dto.getListaCaracterizacaoDTO()) {
			listaLocalizacaoGeografica.add(dto.getCerhLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}
		super.converterPontoGeografico(listaLocalizacaoGeografica);
	}

	public void visualizarCaracterizacao(ActionEvent event) {
		try {
			CerhCaracterizacaoDTO cerhCaracterizacaoDTO = (CerhCaracterizacaoDTO) event.getComponent().getAttributes().get("caracterizacao");
			prepararDialogIncluirCaracterizacao(cerhCaracterizacaoDTO, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + Util.getString("cerh_aba_cap_sup_caracterizcao") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void informarCaracterizacao(ActionEvent event) {
		try {
			CerhCaracterizacaoDTO cerhCaracterizacaoDTO = (CerhCaracterizacaoDTO) event.getComponent().getAttributes().get("caracterizacao");
			prepararDialogIncluirCaracterizacao(cerhCaracterizacaoDTO, false);
		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + Util.getString("cerh_aba_cap_sup_caracterizcao") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void informarCaracterizacaoCaptacao(ActionEvent event) {
		try {
			CerhCaracterizacaoDTO cerhCaracterizacaoDTO = (CerhCaracterizacaoDTO) event.getComponent().getAttributes().get("caracterizacao");
			prepararDialogIncluirCaracterizacaoHistorico(cerhCaracterizacaoDTO, false);
		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + Util.getString("cerh_aba_cap_sup_caracterizcao") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	protected void prepararDialog(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		
		dto.setCaracterizacaoDTO(caracterizacaoDTO);
		dto.getCaracterizacaoDTO().setVisualizacao(visualizacao);
		
		try {
			clone = dto.getCaracterizacaoDTO().clone();
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getSimpleName(),Level.ERROR, e);
		} 
		getFacade().prepararLocalizacaoGeografica(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
		
		cerhCaracterizacao = obterCaracterizacaoMontada(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
		
		prepararCerhTipoUso();
		listarCerhSituacaoTipoUso();
		
	}
	
	protected void prepararDialogHistorico(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		
		dto.setCaracterizacaoDTO(caracterizacaoDTO);
		dto.getCaracterizacaoDTO().setVisualizacao(visualizacao);
		
		try {
			clone = dto.getCaracterizacaoDTO().clone();
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getSimpleName(),Level.ERROR, e);
		} 
		getFacade().prepararLocalizacaoGeografica(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
		
		cerhCaracterizacao = obterCaracterizacaoMontadaHistorico(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
		
		prepararCerhTipoUso();
		listarCerhSituacaoTipoUso();
		
	}

	protected void tratarPonto(CerhCaracterizacaoDTO caracterizacaoDTO) throws Exception {
		getFacade().tratarPonto(caracterizacaoDTO.getCerhLocalizacaoGeografica());
	}
	
	public void prepararCerhTipoUso() {
		if(Util.isNull(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhTipoUso().getIdeCerhTipoUso())) {
			CerhProcesso cerhProcesso = dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhProcesso();
			if(!Util.isNull(cerhProcesso)) {
				dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().setIdeCerhTipoUso(super.cerhDTO.getAbaDadoGerais().obterCerhTipoUsoDoProcesso(cerhProcesso, getTipoUsoRecursoHidricoEnum()));
			} 
			else {
				if(super.cerhDTO.getAbaDadoGerais().existeCaptacao(getTipoUsoRecursoHidricoEnum())) {
					dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().setIdeCerhTipoUso(super.cerhDTO.getAbaDadoGerais().obterCerhTipoUso(getTipoUsoRecursoHidricoEnum()));
				}
			}
		}
	}

	@Override
	public void validarAba() {
		if(!Util.isNullOuVazio(dto.getListaCaracterizacaoDTO())) {
			for (CerhCaracterizacaoDTO cerhCaracterizacaoDTO : dto.getListaCaracterizacaoDTO()) {
				if((Util.isNull(cerhCaracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhTipoUso()) 
						|| Util.isNull(cerhCaracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhTipoUso())) && !Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection())) {
					List<String> msgsValidacao = new ArrayList<String>(); 
					msgsValidacao.add(Util.getString("MSG-003", Util.getString("cerh_aba_cap_sup_num_processo")));
					throw new SeiaValidacaoRuntimeException(msgsValidacao);
				}
			}
		}
	}

	@Override
	public void salvarAba() throws Exception {
		for (CerhCaracterizacaoDTO caracterizacaoDTO : dto.getListaCaracterizacaoDTO()) {
			prepararCerhLocalizacaoGeograficaParaSalvar(getCerh(),caracterizacaoDTO.getCerhLocalizacaoGeografica(), getTipoUsoRecursoHidricoEnum());
			CerhCaracterizacaoInterface caracterizacao = obterCaracterizacaoMontada(caracterizacaoDTO.getCerhLocalizacaoGeografica());
		
			if(!Util.isNullOuVazio(caracterizacao)) {
				if(!Util.isNullOuVazio(caracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao())){
					getFacade().salvarOuAtualizarCerhCaracterizacao(caracterizacao);
					MensagemUtil.sucesso("Salvo com sucesso.");
				}else {
					if(!Util.isNull(caracterizacao.getId())){
						getFacade().salvarCerhCaracterizacao(caracterizacao);
						MensagemUtil.sucesso("Salvo com sucesso.");
					}
				}
			}
			
		}
		
		for (CerhCaracterizacaoDTO caracterizacaoDTO : dto.getListaCaracterizacaoDTO()) {
			CerhCaracterizacaoInterface caracterizacao= obterCaracterizacaoMontada(caracterizacaoDTO.getCerhLocalizacaoGeografica());
			
			if(caracterizacao != null && caracterizacao.getNomCorpoHidrico()==null){
				caracterizacaoDTO.getCerhLocalizacaoGeografica().setIdeCerhIntervencaoCaracterizacao(null);
			}
		}
		
		verificarPendenciaNoCadastro();
	}
	
	protected void listarCerhSituacaoTipoUso() {
		try {
			if(Util.isNullOuVazio(cerhSituacaoTipoUsoCollection)) {
				cerhSituacaoTipoUsoCollection = getFacade().listarCerhSituacaoTipoUso();
			}
		}
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_cap_sup_situacao_captacao") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	protected void listarTipoCorpoHidrico() {
		try {
			if(Util.isNullOuVazio(tipoCorpoHidricoCollection)) {
				tipoCorpoHidricoCollection = getFacade().listarCorpoHidrico();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_aba_cap_sup_tipo_hidrico") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public void salvarCaracterizacao() {
		try {
			if(validarCaracterizacao()) {
				
				prepararCerhLocalizacaoGeograficaParaSalvar(getCerh(), dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica(), getTipoUsoRecursoHidricoEnum());
				atualizarCaracterizacao();
				
				
				if(cerhCaracterizacao instanceof CerhLancamentoEfluenteCaracterizacao) {
					getFacade().salvarOuAtualizarCerhCaracterizacao(cerhCaracterizacao);
				}else {
					getFacade().salvarCerhCaracterizacao(cerhCaracterizacao);
				}
				
				
				atualizarTipoUso(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica(),getTipoUsoRecursoHidricoEnum());
				fecharDialogCaracterizacao();
			}
		} 
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_salvar") + Util.getString("cerh_caracterizcao") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void atualizarTipoUso(CerhLocalizacaoGeografica cerhLocalizacaoGeografica,
			TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		
		if(tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL) || tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA)) {
			adicionarCaracterizacaoCaptacaoInicialListaInicial();
		}else if(tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.BARRAGEM) || tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.INTERVENCAO)) {
			adicionarCaracterizacaoIntervencaoInicialListaInicial();
		}else if(tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE)) {
			adicionarCaracterizacaoEfluenteInicialListaInicial();
		}
		
	}
	private void adicionarCaracterizacaoCaptacaoInicialListaInicial() {
		if(!Util.isNullOuVazio(super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial())) {
			for(TipoUsoRecursoHidrico trh : super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial()) {
				if(!Util.isNullOuVazio(trh.getCerhTipoUsoCollection())) {
					for(CerhTipoUso ctu : trh.getCerhTipoUsoCollection()) {
						if(ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(getTipoUsoRecursoHidricoEnum().getId())) {
							if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
								ctu.setCerhLocalizacaoGeograficaCollection(new ArrayList<CerhLocalizacaoGeografica>());
							}
							if(!ctu.getCerhLocalizacaoGeograficaCollection().contains(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica())) {
								ctu.getCerhLocalizacaoGeograficaCollection().add(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
							}
						}
					}
				}
			}
		}
	}
	
	
	private void adicionarCaracterizacaoEfluenteInicialListaInicial() {
		for(CerhTipoUso ctu : getCerh().getCerhTipoUsoCollection()) {
			if(ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())) {
				if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
					ctu.setCerhLocalizacaoGeograficaCollection(new ArrayList<CerhLocalizacaoGeografica>());
				}
				if(!ctu.getCerhLocalizacaoGeograficaCollection().contains(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica())) {
					ctu.getCerhLocalizacaoGeograficaCollection().add(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
				}
			}
		}
	}
	
	
	private void adicionarCaracterizacaoIntervencaoInicialListaInicial() {
		if(!Util.isNullOuVazio(super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial())) {
			for(TipoUsoRecursoHidrico trh : super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial()) {
				if(!Util.isNullOuVazio(trh.getCerhTipoUsoCollection())) {
					for(CerhTipoUso ctu : trh.getCerhTipoUsoCollection()) {
						if(ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(getTipoUsoRecursoHidricoEnum().getId())) {
							if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
								ctu.setCerhLocalizacaoGeograficaCollection(new ArrayList<CerhLocalizacaoGeografica>());
							}
							if(!ctu.getCerhLocalizacaoGeograficaCollection().contains(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica())) {
								ctu.getCerhLocalizacaoGeograficaCollection().add(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Melhore isso assim que possivel
	 * */
	public void atualizarCaracterizacao(){
		if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica()!= null && dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao()!=null ){
			if(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValVazaoDiluicaoOutorgada()!=null){
				CerhLancamentoEfluenteCaracterizacao cerh = (CerhLancamentoEfluenteCaracterizacao) cerhCaracterizacao;
				cerh.setValVazaoDiluicaoOutorgada(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica().getIdeCerhLancamentoEfluenteCaracterizacao().getValVazaoDiluicaoOutorgada());
			}
		}
		
	}
	
	public void salvarCaracterizacaoHistorico() {
		try {
			getFacade().salvarCaracterizacaoHistorico(cerhCaracterizacao);
		} 
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void prepararCerhLocalizacaoGeograficaParaSalvar(Cerh cerh,CerhLocalizacaoGeografica cerhLocalizacaoGeografica, TipoUsoRecursoHidricoEnum recursoHidricoEnum){
		CerhCaracterizacaoInterface caracterizacao = obterCaraterizacaoTemporaria(cerhLocalizacaoGeografica, recursoHidricoEnum);
		getFacade().salvarCerhLocalizacaoGeografica(cerh, cerhLocalizacaoGeografica, recursoHidricoEnum);
		devolverCaraterizacaoTemporaria(cerhLocalizacaoGeografica, recursoHidricoEnum, caracterizacao);
	}
	
	private CerhCaracterizacaoInterface obterCaraterizacaoTemporaria(CerhLocalizacaoGeografica cerhLocalizacaoGeografica, TipoUsoRecursoHidricoEnum recursoHidricoEnum) {
		CerhCaracterizacaoInterface caracterizacaoInterface = null;
		if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.BARRAGEM)){
			caracterizacaoInterface = cerhLocalizacaoGeografica.getIdeCerhBarragemCaracterizacao();	
			cerhLocalizacaoGeografica.setIdeCerhBarragemCaracterizacao(null);
		} 
		else if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.INTERVENCAO)){
			caracterizacaoInterface = cerhLocalizacaoGeografica.getIdeCerhIntervencaoCaracterizacao();
			cerhLocalizacaoGeografica.setIdeCerhIntervencaoCaracterizacao(null);
		} 
		else if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL) || recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA)){
			caracterizacaoInterface = cerhLocalizacaoGeografica.getIdeCerhCaptacaoCaracterizacao();
			cerhLocalizacaoGeografica.setIdeCerhCaptacaoCaracterizacao(null);
		} 
		else if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE)){
			caracterizacaoInterface = cerhLocalizacaoGeografica.getIdeCerhLancamentoEfluenteCaracterizacao();
			cerhLocalizacaoGeografica.setIdeCerhLancamentoEfluenteCaracterizacao(null);
		}
		return caracterizacaoInterface;
	}
	
	private void devolverCaraterizacaoTemporaria(CerhLocalizacaoGeografica cerhLocalizacaoGeografica, TipoUsoRecursoHidricoEnum recursoHidricoEnum, CerhCaracterizacaoInterface caracterizacaoInterface) {
		if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.BARRAGEM)){
			cerhLocalizacaoGeografica.setIdeCerhBarragemCaracterizacao((CerhBarragemCaracterizacao) caracterizacaoInterface);	
		} 
		else if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.INTERVENCAO)){
			cerhLocalizacaoGeografica.setIdeCerhIntervencaoCaracterizacao((CerhIntervencaoCaracterizacao) caracterizacaoInterface);
		} 
		else if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL) || recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA)){
			cerhLocalizacaoGeografica.setIdeCerhCaptacaoCaracterizacao((CerhCaptacaoCaracterizacao) caracterizacaoInterface);
		} 
		else if(recursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE)){
			cerhLocalizacaoGeografica.setIdeCerhLancamentoEfluenteCaracterizacao((CerhLancamentoEfluenteCaracterizacao) caracterizacaoInterface);
		}
	}
	
	public void excluirCaracterizacao() {
		try {
			preparaCerhCaracterizacaoParaExclusao();
			excluirCaracterizacao(getCerhCaracterizacao());
			dto.getListaCaracterizacaoDTO().remove(dto.getCaracterizacaoDTO());
			MensagemUtil.sucesso("Caracterização excluída com sucesso!");
		} 
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_excluir") + " a Caracterização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	protected void preparaCerhCaracterizacaoParaExclusao() throws Exception {
		CerhCaracterizacaoInterface cerhCaracterizacao = obterCaracterizacaoMontada(dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
		if(!Util.isNull(cerhCaracterizacao)){
			this.cerhCaracterizacao = cerhCaracterizacao;
		} 
		atualizarCerh(getCerhCaracterizacao());
	}
	
	public void atualizarCerh(CerhCaracterizacaoInterface caracterizacaoInterface) {
		if(!Util.isNull(caracterizacaoInterface)){
			boolean removido = false;
			if(!Util.isNull(caracterizacaoInterface.getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso()) 
					&& !Util.isNullOuVazio(getCerh().getCerhProcessoCollection())) {
				for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
					if(!Util.isNullOuVazio(cerhProcesso.getCerhTipoUsoCollection())) {
						for (int i = cerhProcesso.getCerhTipoUsoCollection().size() - 1; i >= 0; i--) {
							CerhTipoUso cerhTipoUso = ((List<CerhTipoUso>) cerhProcesso.getCerhTipoUsoCollection()).get(i);
							if(cerhTipoUso.getIdeCerhProcesso().equals(cerhProcesso) && !Util.isNullOuVazio(cerhTipoUso.getCerhLocalizacaoGeograficaCollection())) {
								cerhTipoUso.getCerhLocalizacaoGeograficaCollection().remove(caracterizacaoInterface.getIdeCerhLocalizacaoGeografica());
								removido = true;
							}
						}
					}
				}
			}
			if(!removido && !Util.isNullOuVazio(getCerh().getCerhTipoUsoCollection())) {
				for(CerhTipoUso cerhTipoUso : getCerh().getCerhTipoUsoCollection()) {
					if(!Util.isNullOuVazio(cerhTipoUso.getCerhLocalizacaoGeograficaCollection())) {
						for (int i = cerhTipoUso.getCerhLocalizacaoGeograficaCollection().size() - 1; i >= 0; i--) {
							CerhLocalizacaoGeografica cerhLocalizacaoGeografica = ((List<CerhLocalizacaoGeografica>) cerhTipoUso.getCerhLocalizacaoGeograficaCollection()).get(i);
							if(cerhLocalizacaoGeografica.equals(caracterizacaoInterface.getIdeCerhLocalizacaoGeografica())) {
								cerhTipoUso.getCerhLocalizacaoGeograficaCollection().remove(cerhLocalizacaoGeografica);
							}
						}
					} 
					else if(!Util.isNull(cerhTipoUso.getIdeCerhProcesso()) && !Util.isNullOuVazio(cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection())){
						for (int i = cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection().size() - 1; i >= 0; i--) {
							CerhLocalizacaoGeografica cerhLocalizacaoGeografica = ((List<CerhLocalizacaoGeografica>) cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection()).get(i);
							if(cerhLocalizacaoGeografica.equals(caracterizacaoInterface.getIdeCerhLocalizacaoGeografica())) {
								cerhTipoUso.getIdeCerhProcesso().getCerhLocalizacaoGeograficaCollection().remove(cerhLocalizacaoGeografica);
							}
						}
					}
				}
			}
		}
	}
	
	public boolean verificarOutros(){
		try {
			if(!Util.isNull(dto) && !Util.isNullOuVazio(dto.getListaCaracterizacaoDTO())){
				for (CerhCaracterizacaoDTO cerhCaptacaoDto : dto.getListaCaracterizacaoDTO()) {
					if(!Util.isNull(getCerhCaracterizacao(cerhCaptacaoDto))){
						cerhCaracterizacao = obterCaracterizacaoMontada(cerhCaptacaoDto.getCerhLocalizacaoGeografica());
						if(existeOutros()){
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void verificarPendenciaNoCadastro() {
		if(!Util.isNullOuVazio(dto.getListaCaracterizacaoDTO())){
			
			boolean processoNaoSelecionado = false;
			for (CerhCaracterizacaoDTO cerhCaptacaoCaracterizacaoDTO : dto.getListaCaracterizacaoDTO()) {
				CerhCaracterizacaoInterface cerhCaracterizacao = getCerhCaracterizacao(cerhCaptacaoCaracterizacaoDTO);
				if(Util.isNull(cerhCaracterizacao) || Util.isNull(cerhCaracterizacao.getId())){
					super.cerhDTO.setPendente(true);
					break;
				}
				else if(!Util.isNullOuVazio(getCerhProcessoCollection())){
					if(Util.isNull(cerhCaptacaoCaracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhProcesso())){
						processoNaoSelecionado = true;
					} 
					else {
						processoNaoSelecionado = false;
					}
				}else {
					processoNaoSelecionado = false;
				}
			}
			if(isExisteOutrosUsoAlemDoProcesso()){
				if(processoNaoSelecionado){
					super.cerhDTO.setPendente(true);
				}
						
			} 
			else {
				if(processoNaoSelecionado){
					super.cerhDTO.setPendente(true);
				}
			}
		}
		else {
			this.cerhDTO.setPendente(true);
		}
	}

	public boolean isPodeAdicionarCoordenada() {
		return isExisteProcessoSemDadoConcedido() || 
			   isExisteTipoUsoParaRecursoHidrico() || 
			   isExisteOutrosUsoAlemDoProcesso();
	}
	
	protected boolean isExisteTipoUsoParaRecursoHidrico(){

			try {
				if(Util.isNullOuVazio(getCerh().getCerhTipoUsoCollection())){
					getCerh().setCerhTipoUsoCollection(getFacade().listarTipoUso(getCerh()));
				}
				
				if(!Util.isNullOuVazio(getCerh().getCerhTipoUsoCollection())){
					for (CerhTipoUso cerhTipoUso : getCerh().getCerhTipoUsoCollection()) {
						if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(getTipoUsoRecursoHidricoEnum().getId())) {
							return true;
						}
					}
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			} 
		
		return false;
	}

	public boolean isExisteProcessoSemDadoConcedido() {
		if(!Util.isNullOuVazio(getCerh().getCerhProcessoCollection())) {
			for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
				if(cerhProcesso.getIndDadosConcedidos()!=null && !cerhProcesso.getIndDadosConcedidos() && 
				   !Util.isNullOuVazio(cerhProcesso.getCerhTipoUsoCollection())) {

					for (CerhTipoUso cerhTipoUso : cerhProcesso.getCerhTipoUsoCollection()) {
						if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(getTipoUsoRecursoHidricoEnum().getId())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean isExisteCoordenada() {
		return !Util.isNull(dto) && !Util.isNullOuVazio(dto.getListaCaracterizacaoDTO());
	}
	
	public boolean isPossivelVisualizar(CerhCaracterizacaoInterface caracterizacao){
		return !Util.isNull(caracterizacao) && !Util.isNull(caracterizacao.getId());
	}
	
	public boolean isProcessoOutorgado(){
		return isProcessoSelecionado() && getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso().getIdeCerhSituacaoRegularizacao().equals(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.OUTORGADO));
	}
	
	public boolean isProcessoDispensado(){
		return isProcessoSelecionado() && getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso().getIdeCerhSituacaoRegularizacao().equals(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.DISPENSADO));
	}
	
	protected boolean isProcessoSelecionado() {
		return 
		   !Util.isNull(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica()) &&
		   !Util.isNull(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso());
	}
	
	public int getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}
	
	public CerhTipoUso getCerhTipoUso() {
		if(!Util.isNullOuVazio(getCerh().getCerhTipoUsoCollection())) {
			for (CerhTipoUso cerhTipoUso : getCerh().getCerhTipoUsoCollection()) {
				if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(getTipoUsoRecursoHidricoEnum()))) {
					return cerhTipoUso;
				}
			}
		}
		return null;
	}

	protected Cerh getCerh() {
		return this.cerhDTO.getAbaDadoGerais().getCerh();
	}

	public Collection<CerhProcesso> getCerhProcessoCollection(){
		Collection<CerhProcesso> coll = new HashSet<CerhProcesso>();
		if(!Util.isNullOuVazio(getCerh().getCerhProcessoCollection())){
			for (CerhProcesso cerhProcesso : getCerh().getCerhProcessoCollection()) {
				if(!Util.isNullOuVazio(cerhProcesso.getCerhTipoUsoCollection())){
					for (CerhTipoUso cerhTipoUso : cerhProcesso.getCerhTipoUsoCollection()) {
						if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(getTipoUsoRecursoHidricoEnum()))){
							coll.add(cerhProcesso);
						}
					}
				}
			}
		}
		return coll;
	}
	
	public MetodoUtil getMetodo() {
		return metodo;
	}

	public void setMetodo(MetodoUtil metodoSelecionarEmpreendimento) {
		this.metodo = metodoSelecionarEmpreendimento;
	}

	public CerhAbaDTO getDto() {
		return dto;
	}

	public void setDto(CerhAbaDTO dto) {
		this.dto = dto;
	}

	public Collection<CerhSituacaoTipoUso> getCerhSituacaoTipoUsoCollection() {
		return cerhSituacaoTipoUsoCollection;
	}

	public Collection<TipoCorpoHidrico> getTipoCorpoHidricoCollection() {
		return tipoCorpoHidricoCollection;
	}
	
}
