package br.gov.ba.seia.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TelaAcaoEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhAbasFacade;
import br.gov.ba.seia.facade.CerhFinalizarFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FormaterUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

@ViewScoped
@Named("cerhController")
public class CerhController {
	private final String[] ABAS_CERH = new String[]    {"abaDadosGerais", "abaBarragem", "abaIntervencao", "abaCaptacaoSuperficial", "abaCaptacaoSubterranea", "abaLancamentoEfluentes"};
	private final String[] ID_FORM_CERH = new String[] {"frmDadosGerais", "frmBarragem", "frmIntervencao", "frmCaptacaoSuperficial", "frmCaptacaoSubterranea", "formAbaLancamentoEfluentes"};
	
	@Inject
	private CerhAbaDadosGeraisController abaDadosGerais;
	
	@Inject
	private CerhAbaBarragemController abaBarragem;
	
	@Inject
	private CerhAbaIntervencaoController abaIntervencao;
	
	@Inject
	private CerhAbaCaptacaoSuperficialController abaCaptacaoSuperficial;
	
	@Inject
	private CerhAbaCaptacaoSubterraneaController abaCaptacaoSubterranea;
	
	@Inject
	private CerhAbaLancamentoEfluentesController abaLancamentoEfluentes;
	
	@EJB
	private CerhFinalizarFacade finalizarFacade;
	
	@EJB
	private CerhAbasFacade cerhAbasFacade;
	
	private CerhDTO cerhDTO;
	private int abaAtual;
	private String formAtual;
	
	public CerhController() {
	}
	
	@PostConstruct
	private void load() {
	
		try {
			gerarCerhDTO();
			inicializarCerh();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void load(Cerh cerh) {
		try {
			
			limparCarregamentoDoHistorico();
			cerhDTO = new CerhDTO(cerh.getIdeCerh(), TelaAcaoEnum.VISUALIZAR);
			inicializarCerh();
			System.out.println("CERH   " + cerh.getIdeCerh());
			Html.getCurrency().update("tabViewCerh").show("dlgHistoricoCerh");
		
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	
	private void inicializarCerh() throws Exception {
		if(cerhDTO.isValido()) {
			abaAtual=0;
			formAtual=ID_FORM_CERH[abaAtual];
			abaDadosGerais.load(cerhDTO);
			prepararAba();
			if(cerhDTO.isEditar() && cerhDTO.getAbaDadoGerais().getCerh().isCadastroCompleto()) {
				armazenarHistorico();
			}
			
		}
	}
	
	private ArrayList<Integer> arrayDeAbasAtivas() {
		ArrayList<Integer> array = new ArrayList<Integer>();
		array.add(0);
		if(isRenderedAbaBarragem()) {
			array.add(1);
		}
		if(isRenderedAbaIntervencao()) {
			array.add(2);
		}
		if(isRenderedAbaCaptacaoSuperficial()) {
			array.add(3);
		}
		if(isRenderedAbaCaptacaoSubterranea()) {
			array.add(4);
		}
		if(isRenderedAbaLancamentoEfluentes()) {
			array.add(5);
		}
		return array;
	}
	
	public void tramitarCadastroCompleto(Cerh cerh) throws Exception {
		cerh.setIdeCerhStatus(new CerhStatus(CerhStatusEnum.CADASTRO_COMPLETO));
	}
	
	public void prepararAba() {
		if(isRenderedAbaBarragem()) {
			abaBarragem.loadDTO(cerhDTO);
		}
		if(isRenderedAbaIntervencao()) {
			abaIntervencao.load(cerhDTO);
		}
		if(isRenderedAbaCaptacaoSuperficial()) {
			abaCaptacaoSuperficial.load(cerhDTO);
		} 
		if(isRenderedAbaCaptacaoSubterranea()) {
			abaCaptacaoSubterranea.loadDTO(cerhDTO);
		}
		if(isRenderedAbaLancamentoEfluentes()) {
			abaLancamentoEfluentes.loadDTO(cerhDTO);
		}
	}
	
	private void limparCarregamentoDoHistorico() {
			abaBarragem.clean();
			abaIntervencao.clean();
			abaCaptacaoSuperficial.clean();
			abaCaptacaoSubterranea.clean();
			abaLancamentoEfluentes.clean();
	}
	
	private void prepararAbaAtiva() {
		if(isAbaBarragemAtiva() && isRenderedAbaBarragem()) {
			abaBarragem.load(cerhDTO);
		}
		else if(isAbaIntervencaoAtiva() && isRenderedAbaIntervencao()) {
			abaIntervencao.load(cerhDTO);
		} 
		else if(isAbaCaptacaoSuperficialAtiva() && isRenderedAbaCaptacaoSuperficial()) {
			abaCaptacaoSuperficial.load(cerhDTO);
		} 
		else if(isAbaCaptacaoSubterraneaAtiva() && isRenderedAbaCaptacaoSubterranea()) {
			abaCaptacaoSubterranea.load(cerhDTO);
		}
		else if(isAbaLancamentoEfluenteAtiva() && isRenderedAbaLancamentoEfluentes()) {
			abaLancamentoEfluentes.load(cerhDTO);
		}
	}
	
	private void gerarCerhDTO() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		Integer ideCerh = null;
		String acao = null;
		
		if(requestParameterMap.get("ideCerh")!=null){
			 ideCerh = Integer.valueOf(requestParameterMap.get("ideCerh"));
		}
		System.out.println("CERH    " + ideCerh);
		if(requestParameterMap.get("acao")!=null){
			 acao = requestParameterMap.get("acao");
		}
		
		cerhDTO = new CerhDTO(ideCerh, acao);
	}
	
	public void onTabChange(TabChangeEvent event) {
		try {
			if(ABAS_CERH[abaAtual].compareTo(event.getTab().getId()) != 0){
				if(!cerhDTO.isVisualizar()){
					CerhAbaController controller = retornarControllerAbaAtual();
					controller.validarAba();
					controller.salvarAba();
				}
				ajustarTabIndex(event);
			}
		} 
		catch (SeiaValidacaoRuntimeException e) {
			Html.executarJS("tabViewCerh.select("+ abaAtual +");");
			exibirMensagem(e);
			
		}  
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);			
		}
	}

	private void ajustarTabIndex(TabChangeEvent event) {
		for(int i=0 ; i < ABAS_CERH.length; i++) {
			if(ABAS_CERH[i].equals(event.getTab().getId())) {
				setAbaAtual(i);
				formAtual=ID_FORM_CERH[abaAtual];
				break;
			}
		}
	} 
	
	public boolean renderedProcessAvancarAba(String form) {
		return form.equals(formAtual);
	}
	
	private CerhAbaController retornarControllerAbaAtual() throws Exception {
		Field field = this.getClass().getDeclaredField(ABAS_CERH[abaAtual]);
		field.setAccessible(true);
		CerhAbaController abaAtualController = (CerhAbaController) field.get(this);
		return abaAtualController;
	}

	public boolean existeOutros() throws Exception {
		CerhAbaController abaAtualController = retornarControllerAbaAtual();
		if(abaAtualController instanceof CerhAbaDadosGeraisController) {
			return false;
		} 
		else {
			return ((CerhAbaCaracterizacaoController) abaAtualController).verificarOutros();
		}
	}
	
	public void salvar(){
		try {
			
			avancarAba();
			
		} 
		catch (SeiaValidacaoRuntimeException e){
			exibirMensagem(e);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarAbaAtual() {
		try {
			CerhAbaController abaAtualController = retornarControllerAbaAtual();
			abaAtualController.validarAba();
			abaAtualController.salvarAba();
		} 
		catch (SeiaValidacaoRuntimeException e){
			if(e.getMessage()!=null){
				throw new SeiaValidacaoRuntimeException(e.getMessage());
			}else if(e.getMessages()!=null){
				throw new SeiaValidacaoRuntimeException(e.getMessages());
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void finalizar() {
		try {
			
			boolean outros = false;
			boolean tipoUsoValido = true;
			
			CerhAbaController abaAtualController = retornarControllerAbaAtual();
			abaAtualController.validarAba();
			
			for (Integer index : arrayDeAbasAtivas()) {
				abaAtual = index;
				salvarAbaAtual();
				if(existeOutros()) {
					outros = true;
				}
			}
			
			if(!outros && Util.isNull(cerhDTO.getPendente()) && tipoUsoPreenchido(tipoUsoValido)) {
				
				finalizarFacade.finalizarCERH(getCerh(), CerhStatusEnum.CADASTRO_COMPLETO);
				
				if(getCerh().getIdeCerhPai()!=null){
					Cerh cerhPai = finalizarFacade.getNumeroCadastroCerh(getCerh().getIdeCerhPai().getIdeCerh());
					ContextoUtil.getContexto().setUpdateMessage("O CERH de nº "+ cerhPai.getNumCadastro() +" foi finalizado com sucesso!");
				}
				else{
					ContextoUtil.getContexto().setUpdateMessage("O CERH de nº "+ getCerh().getNumCadastro() +" foi finalizado com sucesso!");
				}
				 
				ContextoUtil.getContexto().setSucessMessage(true);
				
				Html.redirecionarPagina(PaginaEnum.CONSULTAR_CERH);
			} 
			else {
				
				if(outros) {
					MensagemLacFceUtil.exibirInformacao001();
				}
				
				if(!tipoUsoValido) {
					MensagemUtil.alerta("É necessário informar ao menos um tipo de uso para cada processo da lista");
				}
				
				MensagemUtil.alerta("O CERH foi salvo, porém está incompleto.");
				finalizarFacade.finalizarCERH(getCerh(), CerhStatusEnum.CADASTRO_INCOMPLETO);
			}
		} 
		catch (SeiaValidacaoRuntimeException e){
			exibirMensagem(e);
		}
		catch (Exception e) {
			MensagemUtil.erro("Houve um erro ao finalizar o cadastro");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}
	
	private boolean tipoUsoPreenchido(boolean tipoUsoValido) {
		
		if(Util.isNull(abaDadosGerais.getDlgIncluirProcesso().getDto())) {
			if(!Util.isNull(abaDadosGerais.getDto().getCerh().getCerhProcessoCollection()) && 
					  !Util.isNullOuVazio(abaDadosGerais.getDlgIncluirProcesso().getDto()) && 
			          !Util.isNullOuVazio(abaDadosGerais.getDlgIncluirProcesso().getDto().getProcesso())) {
				
				for(ProcessoAto pa : abaDadosGerais.getDlgIncluirProcesso().getDto().getProcesso().getProcessoAtoCollection()) {
						if(pa.getAtoAmbiental().getIdeTipoAto().getIdeTipoAto() == TipoAtoEnum.OUTORGA.getId()) {
							tipoUsoValido = true;
						}else {
							for(TipoUsoRecursoHidrico tipoUso: abaDadosGerais.getDlgIncluirProcesso().getDto().getListaTipoUsoRecursoHidrico()) {
								if(tipoUso.isValue()) {
									tipoUsoValido = true;
									break;
								}else {
									tipoUsoValido = false;
								}
							}
						}
					}
				}
			}
		
		
		if(!Util.isNull(abaDadosGerais.getDlgIncluirProcesso().getDto()) && (abaDadosGerais.getDlgIncluirProcesso().getDto().isRenderedPnlTipoUso() || abaDadosGerais.getDlgIncluirProcesso().isRenderedPossuiAtoDeCaptacao())) {
			for(TipoUsoRecursoHidrico tipoUso: abaDadosGerais.getDlgIncluirProcesso().getDto().getListaTipoUsoRecursoHidrico()) {
				if(tipoUso.isValue()) {
					tipoUsoValido = true;
					break;
				}else {
					tipoUsoValido = false;
				}
			}
		}

		return tipoUsoValido;
	}

	private void armazenarHistorico() throws Exception {
		finalizarFacade.armazenarHistorico(this);
		limparCarregamentoDoHistorico();
		inicializarCerh();
	}

	public void prepararArmazenamentoHistorico() throws Exception {
		for (Integer index : arrayDeAbasAtivas()) {
			abaAtual = index;
			CerhAbaController abaAtualController = retornarControllerAbaAtual();
			abaAtualController.armazenarHistorico();
		}
	}
	
	public void avancarAba() {
		try {
			Boolean valido = true;
			
			if(!Util.isNull(abaDadosGerais.getDto().getCerh().getCerhProcessoCollection())) {
				if(!Util.isNullOuVazio(abaDadosGerais.getDlgIncluirProcesso().getDto()) && !Util.isNullOuVazio(abaDadosGerais.getDlgIncluirProcesso().getDto().getProcesso())) {
					for(ProcessoAto pa : abaDadosGerais.getDlgIncluirProcesso().getDto().getProcesso().getProcessoAtoCollection()) {
						if(pa.getAtoAmbiental().getIdeTipoAto().getIdeTipoAto() != TipoAtoEnum.OUTORGA.getId()) {
							valido = true;
						}else {
							for(TipoUsoRecursoHidrico tipoUso: abaDadosGerais.getDlgIncluirProcesso().getDto().getListaTipoUsoRecursoHidrico()) {
								if(tipoUso.isValue()) {
									valido = true;
									break;
								}else {
									valido = false;
								}
							}
						}
					}
				}else {
					if(!Util.isNull(abaDadosGerais.getDlgIncluirProcesso().getDto()) && (abaDadosGerais.getDlgIncluirProcesso().getDto().isRenderedPnlTipoUso() || abaDadosGerais.getDlgIncluirProcesso().isRenderedPossuiAtoDeCaptacao())) {
						for(TipoUsoRecursoHidrico tipoUso: abaDadosGerais.getDlgIncluirProcesso().getDto().getListaTipoUsoRecursoHidrico()) {
							if(tipoUso.isValue()) {
								valido = true;
								break;
							}else {
								valido = false;
							}
						}
					}
				}
			}
			
			
			salvarAbaAtual();
			if(!valido) {
				MensagemUtil.alerta("É necessário informar tipo de uso para todos os processos.");
			}
			setAbaAtual(abaAtual + 1);
			avancarAbasInativas();
			formAtual=ID_FORM_CERH[abaAtual];
		}
		catch(SeiaValidacaoRuntimeException e) {
			exibirMensagem(e);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void exibirMensagem(SeiaValidacaoRuntimeException e) {
		SeiaValidacaoRuntimeException erroFinal = (SeiaValidacaoRuntimeException) encontrarErro(e);
		
		if(erroFinal.getMessages()!=null) {
			MensagemUtil.alerta(erroFinal.getMessages());
		} 
		else if(erroFinal.getMessage()!=null){
			MensagemUtil.alerta(erroFinal.getMessage());
		}
	}
	
	private Throwable encontrarErro(Throwable erro) {
		if(erro.getCause()!=null) {
			return encontrarErro(erro.getCause());
		}
		return erro;
	}
	
	private void avancarAbasInativas() {
		
		if(isPularAbaBarragem()) setAbaAtual(abaAtual + 1);
		if(isPularAbaIntervencao()) setAbaAtual(abaAtual + 1);
		if(isPularAbaCaptacaoSuperficial()) setAbaAtual(abaAtual + 1);
		if(isPularAbaCaptacaoSubterranea()) setAbaAtual(abaAtual + 1);
		if(isPularAbaLancamentoEfluente()) setAbaAtual(abaAtual + 1);
		
		if(abaAtual == 6) {
			abaAtual = 0;
		}
	}
	
	public String voltarAba() {
	
			if(isAbaDadosGeraisAtiva()) {
				return "/paginas/manter-cerh/consulta.xhtml?faces-redirect=true";
			}
			else {
				try {
					setAbaAtual(abaAtual - 1);
					voltarAbasInativas();
					formAtual=ID_FORM_CERH[abaAtual];
				}
				catch(SeiaValidacaoRuntimeException e) {
					exibirMensagem(e);
					return "";
				}
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		return "";
	}

	public String visualizarGeobahia(LocalizacaoGeografica locGeo) {
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
	}
	
	private void voltarAbasInativas() {
		if(isPularAbaLancamentoEfluente()) {
			setAbaAtual(abaAtual - 1);
		}
		if(isPularAbaCaptacaoSubterranea()) {
			setAbaAtual(abaAtual - 1);
		}
		if(isPularAbaCaptacaoSuperficial()) {
			setAbaAtual(abaAtual - 1);
		}
		if(isPularAbaIntervencao()) {
			setAbaAtual(abaAtual - 1);
		}
		if(isPularAbaBarragem()) {
			setAbaAtual(abaAtual - 1);
		}
	}
	
	public boolean isRenderedBtnAvancar() {
		return abaAtual != ABAS_CERH.length-1 && !isRenderedBtnFinalizar();
	}
	
	public boolean isRenderedBtnFinalizar() {
		if(abaAtual == ABAS_CERH.length - 1) {
			return true;
		} 
		else {
			if(isAbaBarragemAtiva()) {
				return !isRenderedAbaIntervencao() && !isRenderedAbaCaptacaoSuperficial() && !isRenderedAbaCaptacaoSubterranea() && !isRenderedAbaLancamentoEfluentes();
			}
			else if(isAbaIntervencaoAtiva()) {
				return !isRenderedAbaCaptacaoSuperficial() && !isRenderedAbaCaptacaoSubterranea() && !isRenderedAbaLancamentoEfluentes();
			}
			else if(isAbaCaptacaoSuperficialAtiva()) {
				return !isRenderedAbaCaptacaoSubterranea() && !isRenderedAbaLancamentoEfluentes();
			} 
			else if(isAbaCaptacaoSubterraneaAtiva()) {
				return !isRenderedAbaLancamentoEfluentes();
			}
		}
		return false;
	}
	
	private boolean isRenderedAba() {
		return !Util.isNull(getCerh()); 
	}

	public boolean isRenderedQualquerAba(){
		return isRenderedAbaBarragem() || isRenderedAbaIntervencao() || isRenderedAbaCaptacaoSuperficialRegraCheck() || isRenderedAbaCaptacaoSubterraneaRegraCheck() || isRenderedAbaLancamentoEfluentes();
	}
	
	public boolean isRenderedAbaBarragem() {
		return isRenderedAba() && isRenderedIntervencao(TipoUsoRecursoHidricoEnum.BARRAGEM);
	}
	
	private boolean isRenderedIntervencao(TipoUsoRecursoHidricoEnum recursoHidricoEnum) {
		return cerhDTO.getAbaDadoGerais().existeIntervencao(recursoHidricoEnum) || cerhDTO.getAbaDadoGerais().existeProcessoSeiaComTipoUsoRecursoHidrico(recursoHidricoEnum);
	}
	
	public boolean isRenderedAbaIntervencao() {
		return isRenderedAba() && isRenderedIntervencao(TipoUsoRecursoHidricoEnum.INTERVENCAO);
	}
	
	private boolean isRenderedAbaCaptacao(TipoUsoRecursoHidricoEnum recursoHidricoEnum, CerhProcesso cerhProcessoParam) {
		return cerhDTO.getAbaDadoGerais().existeCaptacao(recursoHidricoEnum) || cerhDTO.getAbaDadoGerais().processoComTipoUsoRecursoHidrico(recursoHidricoEnum, cerhProcessoParam) || contemTipoUso(recursoHidricoEnum);
	}

	private boolean contemTipoUso(TipoUsoRecursoHidricoEnum tipoEnum) {
		if(!Util.isNullOuVazio(getAbaDadosGerais().getDlgIncluirProcesso().getListaCerhTipoUsoSelicionadoProcesso())) {
			for(CerhTipoUso selecionado : getAbaDadosGerais().getDlgIncluirProcesso().getListaCerhTipoUsoSelicionadoProcesso()) {
				if(selecionado.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(tipoEnum.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isRenderedAbaCaptacaoSuperficialRegraCheck() {
		return isRenderedAba() && isRenderedAbaCaptacao(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL,abaDadosGerais.getDlgIncluirProcesso().getDto().getCerhProcesso());
	}
	
	public boolean isRenderedAbaCaptacaoSubterraneaRegraCheck() {
		return isRenderedAba() && isRenderedAbaCaptacao(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA, abaDadosGerais.getDlgIncluirProcesso().getDto().getCerhProcesso());
	}
	
	public boolean isRenderedAbaCaptacaoSuperficial() {
		return isRenderedAba() && isRenderedAbaCaptacao(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL,null);
	}
	
	public boolean isRenderedAbaCaptacaoSubterranea() {
		return isRenderedAba() && isRenderedAbaCaptacao(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA, null);
	}
	
	public boolean isRenderedAbaLancamentoEfluentes() {
		return isRenderedAba() &&(!Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta()) && cerhDTO.getAbaDadoGerais().getResposta6().getIndResposta())|| 
                cerhDTO.getAbaDadoGerais().existeProcessoSeiaComTipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE);
    }
	
	private boolean isAbaDadosGeraisAtiva() {
		return abaAtual == 0;
	}
	
	private boolean isAbaBarragemAtiva() {
		return abaAtual == 1;
	}
	
	private boolean isPularAbaBarragem() {
		return isAbaBarragemAtiva() && !isRenderedAbaBarragem();
	}
	
	private boolean isAbaIntervencaoAtiva() {
		return abaAtual == 2;
	}
	
	private boolean isPularAbaIntervencao() {
		return isAbaIntervencaoAtiva() && !isRenderedAbaIntervencao();
	}
	
	private boolean isAbaCaptacaoSuperficialAtiva() {
		return abaAtual == 3;
	}
	
	private boolean isPularAbaCaptacaoSuperficial() {
		return isAbaCaptacaoSuperficialAtiva() && !isRenderedAbaCaptacaoSuperficial();
	}
	
	private boolean isAbaCaptacaoSubterraneaAtiva() {
		return abaAtual == 4;
	}
	
	private boolean isPularAbaCaptacaoSubterranea() {
		return isAbaCaptacaoSubterraneaAtiva() && !isRenderedAbaCaptacaoSubterranea();
	}
	
	private boolean isAbaLancamentoEfluenteAtiva() {
		return abaAtual == 5;
	}
	
	private boolean isPularAbaLancamentoEfluente() {
		return isAbaLancamentoEfluenteAtiva() && !isRenderedAbaLancamentoEfluentes();
	}
	
	public String getDataHoje() {
		return FormaterUtil.getDataFormatoPtBr(new Date());
	}
	
	public CerhAbaDadosGeraisController getAbaDadosGerais() {
		return abaDadosGerais;
	}

	public CerhAbaBarragemController getAbaBarragem() {
		return abaBarragem;
	}

	public CerhAbaIntervencaoController getAbaIntervencao() {
		return abaIntervencao;
	}

	public CerhAbaCaptacaoSuperficialController getAbaCaptacaoSuperficial() {
		return abaCaptacaoSuperficial;
	}

	public CerhAbaCaptacaoSubterraneaController getAbaCaptacaoSubterranea() {
		return abaCaptacaoSubterranea;
	}

	public CerhAbaLancamentoEfluentesController getAbaLancamentoEfluentes() {
		return abaLancamentoEfluentes;
	}

	public CerhDTO getCerhDTO() {
		return cerhDTO;
	}

	public void setCerhDTO(CerhDTO cerhDTO) {
		this.cerhDTO = cerhDTO;
	}

	public int getAbaAtual() {
		return abaAtual;
	}

	public void setAbaAtual(int abaAtual) {
		this.abaAtual = abaAtual;
		prepararAbaAtiva();
	}

	private Cerh getCerh() {
		return abaDadosGerais.getDto().getCerh();
	}
}