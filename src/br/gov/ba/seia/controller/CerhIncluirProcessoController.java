package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.primefaces.event.DateSelectEvent;

import br.gov.ba.seia.dto.CerhAbaDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.dto.CerhIncluirProcessoDTO;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhProcessoSuspensao;
import br.gov.ba.seia.entity.CerhSituacaoRegularizacao;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Sistema;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.CerhSituacaoRegularizacaoEnum;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.SistemaEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TelaAcaoEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhAbasFacade;
import br.gov.ba.seia.facade.CerhDadosGeraisServiceFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoCaptacaoLancamentoInterface;
import br.gov.ba.seia.service.CerhBarragemCaracterizacaoService;
import br.gov.ba.seia.service.CerhCaptacaoCaracterizacaoService;
import br.gov.ba.seia.service.CerhIntervencaoCaracterizacaoService;
import br.gov.ba.seia.service.CerhLancamentoEfluenteCaracterizacaoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

public class CerhIncluirProcessoController extends ConverterPontoGeograficoController {
	
	@EJB
	private CerhDadosGeraisServiceFacade cerhDadosGeraisServiceFacade;
	
	@EJB
	private CerhAbasFacade cerhAbasFacade;
	
	@Inject
	private CerhGerarTipoUsoController gerarTipoUsoController;
	
	@EJB
	private CerhLancamentoEfluenteCaracterizacaoService cerhLancamentoEfluenteCaracterizacaoService;
	
	@EJB
	private CerhCaptacaoCaracterizacaoService cerhCaptacaoCaracterizacaoService;
	
	@EJB
	private CerhIntervencaoCaracterizacaoService cerhIntervencaoCaracterizacaoService;
	
	@EJB
	private CerhBarragemCaracterizacaoService cerhBarragemCaracterizacaoService;
	
	@Inject
	private CerhController cerhController;
	
	private CerhDTO cerhDTO;
	private CerhIncluirProcessoDTO dto;
	private Empreendimento empreendimento;
	private boolean podeVisualizar;
	private Integer ideProcessoAnterior;
	private boolean isRecarregaTipoUso;
	private Collection<CerhTipoUso> listaCerhTipoUsoSelicionadoProcesso;
	
	public CerhIncluirProcessoController() {
	}
	
	public void incluirProcesso(ActionEvent evt) throws Exception {
		inicializarVariaveis(evt,null);
		dto.setNovoRegistro(true);
		dto.setTelaAcaoEnum(TelaAcaoEnum.CADASTRAR);
		dto.setCerhProcesso(new CerhProcesso());
		dto.getCerhProcesso().setIndDadosConcedidos(false);
		dto.getCerhProcesso().setNumProcesso("");
		dto.getCerhProcesso().setIdeCerh(cerhDTO.getAbaDadoGerais().getCerh());
		dto.setCerhTipoUsoSelecionado(new CerhTipoUso());
	}
	
	public void visualizarProcesso(ActionEvent evt) throws Exception {
		prepararDialogProcesso(evt, TelaAcaoEnum.VISUALIZAR);
	}
	
	public void editarProcesso(ActionEvent evt) throws Exception {
		prepararDialogProcesso(evt, TelaAcaoEnum.EDITAR);
		dto.setNovoRegistro(false);
	}
	
	private void prepararDialogProcesso(ActionEvent evt, TelaAcaoEnum telaAcaoEnum) throws Exception{
		CerhProcesso cerhProcesso = (CerhProcesso) evt.getComponent().getAttributes().get("cerhProcesso");
		if(!Util.isNullOuVazio(dto)) {
			dto.setCerhProcesso(cerhProcesso);
		}
		inicializarVariaveis(evt,telaAcaoEnum);
		dto.setTelaAcaoEnum(telaAcaoEnum);
		
		
		dto.setCerhProcesso(cerhProcesso);
		
		if(telaAcaoEnum == TelaAcaoEnum.VISUALIZAR){
			dto.setCerhProcesso(cerhProcesso);
		}else if(telaAcaoEnum == TelaAcaoEnum.EDITAR){
			dto.setCerhProcesso(cerhProcesso.clone());
		}
		
		calcularDataFimAutorizacao();
		carregarProcesso();
		gerarTipoDeUso();
		carregarTipoUso();
		
		if(!Util.isNull(dto.getCerhTipoUsoSelecionado())){
			atualizarListaSituacaoDaRegularizacao(dto.getCerhTipoUsoSelecionado().getIdeTipoUsoRecursoHidrico());
		}
	}
	

	private void carregarProcesso() throws Exception {
		Processo processo = cerhDadosGeraisServiceFacade.buscarProcesso(dto.getCerhProcesso().getNumProcesso().trim());
		dto.setProcesso(processo);
		if(!Util.isNull(processo)) {
			carregarDadosProcesso(processo);
			carregarDadosConcedidos(processo);
			carregarCerhLocalizacaoGeografica();
			dto.setRemoverEdicaoCerhSituacaoRegularizacao(true);
		}
	}

	private void carregarTipoUso() {
		
		if(dto.getCerhProcesso().getCerhTipoUsoCollection().size()==1) {
			for (CerhTipoUso ctu : dto.getCerhProcesso().getCerhTipoUsoCollection()) {
				dto.setCerhTipoUsoSelecionado(ctu);
			}
		}
		
		try {
			dto.setListaTipoUsoRecursoHidricoSelecionado(new ArrayList<TipoUsoRecursoHidrico>());
			for(CerhTipoUso cerhTipoUso : cerhAbasFacade.listarPorCerhProcesso(dto.getCerhProcesso())){
				dto.getListaTipoUsoRecursoHidricoSelecionado().add(cerhTipoUso.getIdeTipoUsoRecursoHidrico());
				for(TipoUsoRecursoHidrico tipo : dto.getListaTipoUsoRecursoHidrico()) {
					if(tipo.equals(cerhTipoUso.getIdeTipoUsoRecursoHidrico())){
						tipo.setValue(true);
					}
					
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void prepararParaExluirProcesso(ActionEvent evt) throws Exception {
		inicializarVariaveis(evt,null);
		dto.setCerhProcesso((CerhProcesso) evt.getComponent().getAttributes().get("cerhProcesso"));
	}
	
	public void exluirProcesso() throws Exception {
		cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection().remove(dto.getCerhProcesso());
	}
	
	private void inicializarVariaveis(ActionEvent evt, TelaAcaoEnum telaAcaoEnum) throws Exception {
		cerhDTO = (CerhDTO) evt.getComponent().getAttributes().get("cerhDTO");
		if(telaAcaoEnum != TelaAcaoEnum.EDITAR && telaAcaoEnum != TelaAcaoEnum.VISUALIZAR){
			dto = new CerhIncluirProcessoDTO(cerhDTO.getTelaAcaoEnum());
			dto.setListaSituacaoRegularizacao(cerhDadosGeraisServiceFacade.listarCerhSituacaoRegularizacao());
			dto.setListaTipoUsoRecursoHidrico(cerhDadosGeraisServiceFacade.listarTipoUsoRecursoHidrico());
		}else if(Util.isNullOuVazio(dto)) {
			dto = new CerhIncluirProcessoDTO(cerhDTO.getTelaAcaoEnum());
			dto.setListaSituacaoRegularizacao(cerhDadosGeraisServiceFacade.listarCerhSituacaoRegularizacao());
			dto.setListaTipoUsoRecursoHidrico(cerhDadosGeraisServiceFacade.listarTipoUsoRecursoHidrico());
		}else {
			carregarProcesso();
			if(!Util.isNullOuVazio(dto.getProcesso())) {
				if(!dto.getProcesso().getIdeProcesso().equals(ideProcessoAnterior)) {
					ideProcessoAnterior = dto.getProcesso().getIdeProcesso();
					isRecarregaTipoUso = true;
				}else {
					isRecarregaTipoUso = false;
				}
			}else {
				isRecarregaTipoUso = true;
			}
			
			if(isRecarregaTipoUso) {
				dto.setListaTipoUsoRecursoHidrico(cerhDadosGeraisServiceFacade.listarTipoUsoRecursoHidrico());
			}
			dto.setListaSituacaoRegularizacao(cerhDadosGeraisServiceFacade.listarCerhSituacaoRegularizacao());
		}
		
		dto.setListaCerhTipoAtoDispensa(cerhDadosGeraisServiceFacade.listarCerhTipoAtoDispensa());
		dto.setListaCerhTipoAutorizacaoOutorgado(cerhDadosGeraisServiceFacade.listarCerhTipoAutorizacaoOutorgado());
		
		if(Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection())) {
			dto.setExisteCerhTipoUsoSemProcesso(false);
		}
		else {
			dto.setExisteCerhTipoUsoSemProcesso(false);
			if(!Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection())) {
				for (CerhTipoUso ctu : cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection()) {
					if(!Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
						dto.setExisteCerhTipoUsoSemProcesso(true);
						break;
					}
				}
			}
		}
		
	}
	
	public boolean podeVisualizar() {
		if(!Util.isNullOuVazio(dto) && !Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidrico())) {
			for(TipoUsoRecursoHidrico tipo : dto.getListaTipoUsoRecursoHidrico()){
				if(tipo.isVisualizar()) {
					podeVisualizar = true;
					return true;
				}
			}
		}
		podeVisualizar = false;
		return false;
	}
	
	private boolean isProcessoOutorga(Processo processo) {
		if(!Util.isNullOuVazio(processo) && !Util.isNullOuVazio( processo.getProcessoAtoCollection())) {
			for(ProcessoAto pa : processo.getProcessoAtoCollection()) {
				if (pa.getAtoAmbiental().getIdeTipoAto().getIdeTipoAto().equals(TipoAtoEnum.OUTORGA.getId())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void validarPreenchimentoDaTela() {
		if(Util.isNullOuVazio(dto.getCerhProcesso().getNumProcesso())) {
			throw new SeiaValidacaoRuntimeException(Util.getString("cerh_aba_dados_gerais_msg_numero_processo"));
		}
		else if(isProcessoJaInformado()) {
			dto.getCerhProcesso().setNumProcesso("");
			throw new SeiaValidacaoRuntimeException(Util.getString("cerh_aba_dados_gerais_msg_numero_processo_ja_cadastrado"));
		}
	}

	private boolean isProcessoJaInformado() {
		if(Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection())) {
			return false;
		}
		else {
			for(CerhProcesso cp : cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection()) {
				if(cp.getNumProcesso().equals(dto.getCerhProcesso().getNumProcesso())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void onChangeNumProcesso(ValueChangeEvent evt) {
		dto.setValidarNumProcesso(true);
	}
	
	public void eventoConsultaProcesso() { 
		dto.getCerhProcesso().setNumProcesso(dto.getCerhProcesso().getNumProcesso().trim());
		if(dto.isValidarNumProcesso()) {
			consultarProcesso();
			dto.setValidarNumProcesso(false);
		}
	}

	public void consultarProcesso() {
		try {
			validarPreenchimentoDaTela();
			Processo processo = cerhDadosGeraisServiceFacade.buscarProcesso(dto.getCerhProcesso().getNumProcesso().trim());
			if(!Util.isNull(processo)) {
				dto.getCerhProcesso().setIdeSistema(new Sistema(SistemaEnum.SEIA.getId()));
				carregarDadosProcesso(processo);
				carregarDadosConcedidos(processo);
				carregarCerhLocalizacaoGeografica();
				gerarTipoDeUso();
				gerarSituacaoRegularizacao();
			} 
			else { 
				dto.setProcesso(null);
				dto.setCerhProcesso(new CerhProcesso(dto.getCerhProcesso().getNumProcesso()));
				dto.getCerhProcesso().setIndDadosConcedidos(false);
				dto.getCerhProcesso().setIdeCerh(cerhDTO.getAbaDadoGerais().getCerh());
				dto.setCerhTipoUsoSelecionado(new CerhTipoUso());
				dto.setRemoverEdicaoCerhSituacaoRegularizacao(false);
			}
		}
		catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDadosProcesso(Processo processo) throws Exception {
		cerhDadosGeraisServiceFacade.carregarAtosProcesso(processo);
		validarProcessoConsultado(processo);
		dto.setProcesso(processo);
		
	}

	private void carregarCerhLocalizacaoGeografica() throws Exception {
		if(!Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection())) {
			Collection<CerhLocalizacaoGeografica> listaCerhLocalizacaoGeografica = new ArrayList<CerhLocalizacaoGeografica>();
			for (CerhTipoUso ctu : cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection()) {
				if(!Util.isNull(ctu.getIdeCerhProcesso()) && !Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
					for(CerhLocalizacaoGeografica clg : ctu.getCerhLocalizacaoGeograficaCollection()) {
						listaCerhLocalizacaoGeografica.add(clg.clone());
					}
				}
			}
			dto.setListaCerhLocalizacaoGeografica(listaCerhLocalizacaoGeografica);
		}
		
	}
	
	private void carregarDadosConcedidos(Processo processo) throws Exception {
		Collection<Tipologia> listaTipologiaDadosConcedidos = cerhDadosGeraisServiceFacade.listarTipologiaDadosConcedidos(processo);
		dto.setListaTipologiaDadosConcedidos(listaTipologiaDadosConcedidos);
	}

	private void gerarTipoDeUso() throws Exception {
		gerarTipoUsoController.gerar(dto,cerhDTO);
	}
	
	public boolean possuiTipoRecursoHidricoEditavel() {
		for (TipoUsoRecursoHidrico TipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
			if(TipoUsoRecursoHidrico.isEditar()){
				return true;
			}
		}
		return false;
	}
	
	private void gerarSituacaoRegularizacao() {
		
		if(preencherSituacaoRegularizacaoAutomaticamente()) {
			CerhSituacaoRegularizacaoEnum situacaoRegulacaoEnum = null;
			for (ProcessoAto pa : dto.getProcesso().getProcessoAtoCollection()) {
				if(!pa.getAtoAmbiental().isCOUT() && !pa.getAtoAmbiental().isDispensaOutorga() && (pa.getStatusProcessoAto().isDeferido() || pa.getStatusProcessoAto().isTransferido()) ) {
					situacaoRegulacaoEnum = CerhSituacaoRegularizacaoEnum.OUTORGADO;
					break;
				}
				else if(Util.isNull(situacaoRegulacaoEnum) && !pa.getAtoAmbiental().isCOUT() && !pa.getAtoAmbiental().isDispensaOutorga() 
				&& (pa.getStatusProcessoAto().isEmAnalise() || pa.getStatusProcessoAto().isAguardandoAnalise())) {
					situacaoRegulacaoEnum = CerhSituacaoRegularizacaoEnum.EM_ANALISE;
				}
			}
			if(Util.isNull(situacaoRegulacaoEnum)) {
				for (ProcessoAto pa : dto.getProcesso().getProcessoAtoCollection()) {
					if(pa.getStatusProcessoAto().isIndeferido()) {
						situacaoRegulacaoEnum = CerhSituacaoRegularizacaoEnum.INDEFERIDO;
						break;
					}
					if(pa.getAtoAmbiental().isCOUT()) {
						situacaoRegulacaoEnum = CerhSituacaoRegularizacaoEnum.CANCELADO;
						break;
					}
					if(pa.getAtoAmbiental().isDispensaOutorga()) {
						situacaoRegulacaoEnum = CerhSituacaoRegularizacaoEnum.DISPENSADO;
						break;
					}
				}
			}
			
			dto.getCerhProcesso().setIdeCerhSituacaoRegularizacao(new CerhSituacaoRegularizacao(situacaoRegulacaoEnum));
			dto.setRemoverEdicaoCerhSituacaoRegularizacao(true);
		}
		else {
			dto.getCerhProcesso().setIdeCerhSituacaoRegularizacao(null);
			dto.setRemoverEdicaoCerhSituacaoRegularizacao(false);
		}
	}
	
	private boolean preencherSituacaoRegularizacaoAutomaticamente() {
		int count=0;
		for (ProcessoAto pa : dto.getProcesso().getProcessoAtoCollection()) {
			if(Boolean.FALSE.equals(dto.isProcessoSemTipologia()) && pa.getTipologia().isIntervencao()) {
				count++;
			}
		}
		return (count < 2); 
	}

	private void validarProcessoConsultado(Processo processo) throws Exception {
		if(!Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getIdeEmpreendimento())) {
			this.empreendimento = cerhDTO.getAbaDadoGerais().getCerh().getIdeEmpreendimento();
		}
		
		Empreendimento empreend = cerhDadosGeraisServiceFacade.buscarEmpreendimentoPor(processo, this.empreendimento);
		if(Util.isNull(empreend)) {
			dto.getCerhProcesso().setNumProcesso("");
			throw new SeiaValidacaoRuntimeException(Util.getString("cerh_aba_dados_gerais_msg_numero_processo_nao_pertence_ao_empreendimento"));
		}
		
		if(Util.isNullOuVazio(processo.getProcessoAtoCollection())) {
			dto.getCerhProcesso().setNumProcesso("");
			throw new SeiaValidacaoRuntimeException(Util.getString("msg_generica_atualizar_processo"));
		}
	}
	
	public void incluirProcessoSuspensao() {
		if(Util.isNullOuVazio(dto.getCerhProcesso().getCerhProcessoSuspensaoCollection())) {
			dto.getCerhProcesso().setCerhProcessoSuspensaoCollection(new ArrayList<CerhProcessoSuspensao>());
		}
		
		int index = (dto.getCerhProcesso().getCerhProcessoSuspensaoCollection().size()+1) * -1;
		
		CerhProcessoSuspensao cerhProcessoSuspensaoSelecionado = new CerhProcessoSuspensao(index);
		cerhProcessoSuspensaoSelecionado.setIdeCerhProcesso(dto.getCerhProcesso());
		dto.setCerhProcessoSuspensaoSelecionado(cerhProcessoSuspensaoSelecionado);
		Html.atualizar("frmIncluirPeriodoSuspensao");
		Html.exibir("dlgIncluirPeriodoSuspensao");
	}
	
	public void editarProcessoSuspensao(ActionEvent evt) {
		CerhProcessoSuspensao cerhProcessoSuspensao = (CerhProcessoSuspensao) evt.getComponent().getAttributes().get("cerhProcessoSuspensao");
		dto.setCerhProcessoSuspensaoSelecionado(cerhProcessoSuspensao);
		Html.atualizar("frmIncluirPeriodoSuspensao");
		Html.exibir("dlgIncluirPeriodoSuspensao");
	}
	
	public void removerProcessoSuspensao(ActionEvent evt) {
		CerhProcessoSuspensao cerhProcessoSuspensao = (CerhProcessoSuspensao) evt.getComponent().getAttributes().get("cerhProcessoSuspensao");
		dto.getCerhProcesso().getCerhProcessoSuspensaoCollection().remove(cerhProcessoSuspensao);
		Html.atualizar("frmDialogIncluirProcesso:pnlOutorgado");
	}
	
	public void salvarProcessoSuspensao() {
		try{
			if(validarCerhProcessoSuspensao()) {
				if(!dto.getCerhProcesso().getCerhProcessoSuspensaoCollection().contains(dto.getCerhProcessoSuspensaoSelecionado())) {
					dto.getCerhProcesso().getCerhProcessoSuspensaoCollection().add(dto.getCerhProcessoSuspensaoSelecionado());
				}
				Html.atualizar("frmDialogIncluirProcesso:pnlOutorgado");
				Html.esconder("dlgIncluirPeriodoSuspensao");
			}
			
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private boolean validarCerhProcessoSuspensao() {
		
		boolean retorno = true;
		
		if(Util.isNull(dto.getCerhProcessoSuspensaoSelecionado().getDtInicioSuspensao())) {
			JsfUtil.addWarnMessage("O campo data início é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if(!Util.isNull(dto.getCerhProcessoSuspensaoSelecionado().getDtFimSuspensao())
				&& dto.getCerhProcessoSuspensaoSelecionado().getDtInicioSuspensao().compareTo(dto.getCerhProcessoSuspensaoSelecionado().getDtFimSuspensao()) > 0) {
			JsfUtil.addWarnMessage("A data final não pode ser anterior a data inicial");
			retorno = false;
		}
		
		return retorno;
	}
	
	public void converter(ActionEvent evt) {
		Tipologia tipologia = (Tipologia) evt.getComponent().getAttributes().get("tipologia");
		Collection<LocalizacaoGeografica> listaLocalizacaoGeografica = new ArrayList<LocalizacaoGeografica>();
		for (CerhLocalizacaoGeografica clg : tipologia.getListaCerhLocalizacaoGeografica()) {
			listaLocalizacaoGeografica.add(clg.getIdeLocalizacaoGeografica());
		}
		converterPontoGeografico(listaLocalizacaoGeografica);
	}
	
	public void salvar() {
		try {
			validar();
			if(dto.isNovoRegistro()) {
				if(Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection())) {
					cerhDTO.getAbaDadoGerais().getCerh().setCerhProcessoCollection(new ArrayList<CerhProcesso>());
				}
				cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection().add(dto.getCerhProcesso());
				gravarCasoNecessario();
			}
			else{
				gravarCasoNecessario();
				for (Iterator<CerhProcesso> iterator = cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection().iterator(); iterator.hasNext();) {
					CerhProcesso cerhProcesso = (CerhProcesso) iterator.next();
					if(cerhProcesso.equals(dto.getCerhProcesso())) {
						cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection().remove(cerhProcesso);
						break;
					}
				}
				cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection().add(dto.getCerhProcesso());
				Collections.sort((List<CerhProcesso>) cerhDTO.getAbaDadoGerais().getCerh().getCerhProcessoCollection());
			}
			
			Html.getCurrency()
				.update("tabViewCerh")
				.hide("dlgIncluirProcesso");
			
			MensagemUtil.sucesso("Processo Atualizado com Sucesso");
			
			CerhController cerhController = (CerhController) SessaoUtil.recuperarManagedBean("#{cerhController}", CerhController.class);
			cerhController.prepararAba();
			Html.atualizar("tabViewCerh");
		}
		catch (SeiaValidacaoRuntimeException e) {
			List<String> messages = e.getMessages();
			if(!Util.isNullOuVazio(messages)) {
				for(String msg : messages) {
					JsfUtil.addWarnMessage(msg);
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void gravarCasoNecessario() {
		
		if(Util.isNull(cerhDTO.getAbaDadoGerais().getCerh().getIdeCerh())) {
			cerhDTO.getAbaDadoGerais().getCerh().setIdeCerhStatus(new CerhStatus(CerhStatusEnum.CADASTRO_INCOMPLETO.getId()));
			cerhDTO.getAbaDadoGerais().getCerh().setDtcCadastro(new Date());
			
			cerhDadosGeraisServiceFacade.salvar(cerhDTO.getAbaDadoGerais().getCerh());
		}
		
		if(!Util.isNull(cerhDTO.getAbaDadoGerais().getCerh().getIdeCerh())) {
			if(!Util.isNullOuVazio(dto.getCerhProcesso().getCerhProcessoSuspensaoCollection())) {
				for (CerhProcessoSuspensao cps : dto.getCerhProcesso().getCerhProcessoSuspensaoCollection()) {
					if(cps.getIdeCerhProcessoSuspensao()<0) {
						cps.setIdeCerhProcessoSuspensao(null);
					}
				}
			}
			if(Util.isNullOuVazio(dto.getCerhProcesso().getIdeCerhProcesso())){
				cerhDadosGeraisServiceFacade.salvarCerhProcesso(dto.getCerhProcesso());
			}
			listaCerhTipoUsoSelicionadoProcesso = new ArrayList<CerhTipoUso>();
			
				if(isProcessoOutorga(dto.getProcesso())) {
					dto.getCerhProcesso().setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
					for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
						CerhTipoUso cerhTipoUso = new CerhTipoUso();
						cerhTipoUso.setIdeCerh(cerhDTO.getAbaDadoGerais().getCerh());
						cerhTipoUso.setIdeCerhProcesso(dto.getCerhProcesso());
						cerhTipoUso.setIdeTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
						if(tipoUsoRecursoHidrico.isValue()) {
							
							if(!Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection())) {
								for(CerhTipoUso ctu : cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection()) {
									if(ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico())) {
										/*		if(!Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
										for(CerhLocalizacaoGeografica cerhLoc : ctu.getCerhLocalizacaoGeograficaCollection()) {
											
											if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.BARRAGEM.getId())) {
												cerhBarragemCaracterizacaoService.carregarParaHistorico(cerhLoc);
												cerhController.getAbaBarragem().excluirCaracterizacao(cerhLoc.getIdeCerhBarragemCaracterizacao());
											}else if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId())) {
												cerhCaptacaoCaracterizacaoService.carregarCaptacaoCaracterizacao(cerhLoc);
												cerhController.getAbaCaptacaoSubterranea().excluirCaracterizacao(cerhLoc.getIdeCerhCaptacaoCaracterizacao());
											}else if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId())) {
												cerhCaptacaoCaracterizacaoService.carregarCaptacaoCaracterizacao(cerhLoc);
												cerhController.getAbaCaptacaoSubterranea().excluirCaracterizacao(cerhLoc.getIdeCerhCaptacaoCaracterizacao());
											}else if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.INTERVENCAO.getId())) {
												cerhIntervencaoCaracterizacaoService.carregarParaHistorico(cerhLoc);
												cerhController.getAbaIntervencao().excluirCaracterizacao(cerhLoc.getIdeCerhIntervencaoCaracterizacao());
											}else if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())) {
												cerhLancamentoEfluenteCaracterizacaoService.carregarParaHistorico(cerhLoc);
												cerhController.getAbaLancamentoEfluentes().excluirCaracterizacao(cerhLoc.getIdeCerhLancamentoEfluenteCaracterizacao());
											}    		
										}
									}
									cerhAbasFacade.remover(cerhTipoUso);*/
										if(Util.isNullOuVazio(ctu.getIdeCerhTipoUso())) {
											cerhAbasFacade.salvarCerhTipoUso(cerhTipoUso);
										}else {
											cerhTipoUso = ctu;
										}
										
										if(!listaCerhTipoUsoSelicionadoProcesso.contains(ctu)) {
											listaCerhTipoUsoSelicionadoProcesso.add(ctu);
										}
										
										if(!dto.getCerhProcesso().getCerhTipoUsoCollection().contains(ctu)) {
											dto.getCerhProcesso().getCerhTipoUsoCollection().add(ctu);
										}
									}
								}
							}else {
								cerhAbasFacade.salvarCerhTipoUso(cerhTipoUso);
								listaCerhTipoUsoSelicionadoProcesso.add(cerhTipoUso);
								dto.getCerhProcesso().getCerhTipoUsoCollection().add(cerhTipoUso);
							}

							if(Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection())) {
								cerhDTO.getAbaDadoGerais().getCerh().setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
							}
							cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection().add(cerhTipoUso);
						}else {
							cerhAbasFacade.remover(cerhTipoUso);
						}
					}
				}else if (Util.isNullOuVazio(dto.getProcesso())) {
					for (CerhTipoUso cerhTipoUso : dto.getCerhProcesso().getCerhTipoUsoCollection()) {
						//CerhTipoUso cerhTipoUso = new CerhTipoUso();
						//cerhTipoUso.setIdeCerh(cerhDTO.getAbaDadoGerais().getCerh());
						//cerhTipoUso.setIdeCerhProcesso(dto.getCerhProcesso());
						//cerhTipoUso.setIdeTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
						cerhTipoUso.setIdeCerhTipoUso(null);	
						cerhAbasFacade.remover(cerhTipoUso);
						
						cerhAbasFacade.salvarCerhTipoUso(cerhTipoUso);
						listaCerhTipoUsoSelicionadoProcesso.add(cerhTipoUso);
						//dto.getCerhProcesso().getCerhTipoUsoCollection().add(cerhTipoUso);
						cerhTipoUso.setValue(true);
						if(Util.isNullOuVazio(cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection())) {
							cerhDTO.getAbaDadoGerais().getCerh().setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
						}
						cerhDTO.getAbaDadoGerais().getCerh().getCerhTipoUsoCollection().add(cerhTipoUso);
					}
				}
			
			if(!Util.isNullOuVazio(dto.getCerhProcesso().getIdeCerhProcesso())){
				cerhDadosGeraisServiceFacade.salvarCerhProcesso(dto.getCerhProcesso());
			}
		}
		
	}
	
	public boolean isRenderedPossuiAtoDeCaptacao() {
		return (dto.isProcessoSemTipologia() && dto.isPossuiCerhTipoUsoEditavel()) || verificaSeProcessoPossuiAtoDeCaptacao();
	}
	
	private boolean verificaSeProcessoPossuiAtoDeCaptacao() {
		if (!Util.isNullOuVazio(dto.getProcesso())) {
			for (ProcessoAto ato : dto.getProcesso().getProcessoAtoCollection()) {
				if (ato.getAtoAmbiental().getId().equals(AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_HUMANO.getId()) 
						|| ato.getAtoAmbiental().getId().equals(AtoAmbientalEnum.CAPTACAO_AQUICULTURA.getId())
						|| ato.getAtoAmbiental().getId().equals(AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_INDUSTRIAL.getId())
						|| ato.getAtoAmbiental().getId().equals(AtoAmbientalEnum.CAPTACAO_PULVERIZACAO_AGRICOLA.getId())
						|| ato.getAtoAmbiental().getId().equals(AtoAmbientalEnum.CAPTACAO_DESSEDENTACAO_E_CRIACAO_ANIMAL.getId())
						|| ato.getAtoAmbiental().getId().equals(AtoAmbientalEnum.IRRIGACAO.getId())) {
					
					return true;
				} 
			}
		}
		return false;
	}
	
	private boolean verificaSeCaptacaoFoiSelecionada() {
		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoSelecionado())) {
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidricoSelecionado()) {
				if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId()) 
						|| tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId())){
					return true;
				}
			}
		}
		return false;
	}
	
	private void validar() {
		
		List<String> validacao = new ArrayList<String>();
		
		if(Util.isNullOuVazio(dto.getCerhProcesso().getNumProcesso())) {
			validacao.add("O campo número do processo é de preenchimento obrigatório.");
		}

		if (dto.isRenderedPnlTipoUso() && Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoSelecionado()) && (Util.isNullOuVazio(dto.getCerhTipoUsoSelecionado()) && Util.isNullOuVazio(dto.getCerhTipoUsoSelecionado().getIdeTipoUsoRecursoHidrico()))) {
			validacao.add("O campo tipo uso é de preenchimento obrigatório.");
		} else {
			if (!Util.isNullOuVazio(dto.getProcesso()) && !Util.isNullOuVazio(dto.getProcesso().getProcessoAtoCollection())) {
				if (verificaSeProcessoPossuiAtoDeCaptacao()) {
					if(!verificaSeCaptacaoFoiSelecionada()){
						validacao.add("O campo tipo uso é de preenchimento obrigatório.");
					}
				}
			}
		}
		
		boolean isOutorga = false;
		if(!Util.isNullOuVazio(dto) && !Util.isNullOuVazio(dto.getProcesso())) {
			if(!Util.isNullOuVazio(dto.getProcesso().getProcessoAtoCollection())) {
				for(ProcessoAto pa : dto.getProcesso().getProcessoAtoCollection()) {
					if(pa.getAtoAmbiental().getIdeTipoAto().getIdeTipoAto() .equals( TipoAtoEnum.OUTORGA.getId())) {
						isOutorga = true;
						break;
					}
				}
			}
		}
		
		boolean tipoUsoValido = true;	
		if(isOutorga) {
			for(TipoUsoRecursoHidrico tipoUso: dto.getListaTipoUsoRecursoHidrico()) {
				if(tipoUso.isValue()) {
					tipoUsoValido = true;
					break;
				}else {
					tipoUsoValido = false;
				}
			}
		}

			if(!tipoUsoValido) {
				validacao.add("O campo Tipo uso é de preenchimento obrigatório.");
			}
		
			
		if(Util.isNull(dto.getCerhProcesso().getIdeCerhSituacaoRegularizacao()) || Util.isNull(dto.getCerhProcesso().getIdeCerhSituacaoRegularizacao().getIdeCerhSituacaoRegularizacao())) {

			if(!Util.isNullOuVazio(dto.getProcesso().getProcessoAtoCollection())) {
				for(ProcessoAto pa : dto.getProcesso().getProcessoAtoCollection()) {
					if(!pa.getStatusProcessoAto().getIdeStatusProcessoAto().equals(StatusProcessoAtoEnum.DEFERIDO.getId())) {
						validacao.add("Não é possível incluir o processo pois o ato ambiental não está deferido.");
						break;
					}else {
						validacao.add("O campo situação regularização é de preenchimento obrigatório.");
						break;
					}
				}
			}else {
				validacao.add("O campo situação regularização é de preenchimento obrigatório.");
			}
			
		}
		else if(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.DISPENSADO).equals(dto.getCerhProcesso().getIdeCerhSituacaoRegularizacao())) {
			if(Util.isNull(dto.getCerhProcesso().getIdeCerhTipoAtoDispensa())) {
				validacao.add("O campo tipo de documento é de preenchimento obrigatório.");
			}
			if(Util.isNullOuVazio(dto.getCerhProcesso().getNumPortariaDocumento())) {
				validacao.add("O campo número do documento é de preenchimento obrigatório.");
			}
			if(Util.isNull(dto.getCerhProcesso().getDtInicioAutorizacao())) {
				validacao.add("O campo data de início da autorização é de preenchimento obrigatório.");
			}
		}
		else if(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.INEXIGIVEL).equals(dto.getCerhProcesso().getIdeCerhSituacaoRegularizacao())) {
			if(Util.isNull(dto.getCerhProcesso().getIndPossuiCartaInexigibilidade())) {
				validacao.add("O campo possui carta de inexigibilidade é de preenchimento obrigatório.");
			} 
			else if (dto.getCerhProcesso().getIndPossuiCartaInexigibilidade()) {
				if(Util.isNullOuVazio(dto.getCerhProcesso().getNumPortariaDocumento())) {
					validacao.add("O campo número do documento é de preenchimento obrigatório.");
				}
				if(Util.isNull(dto.getCerhProcesso().getDtInicioAutorizacao())) {
					validacao.add("O campo data de início da autorização é de preenchimento obrigatório.");
				}
			}
		}
		else if(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.OUTORGADO).equals(dto.getCerhProcesso().getIdeCerhSituacaoRegularizacao())) {
			if(Util.isNull(dto.getCerhProcesso().getIdeCerhTipoAutorizacaoOutorgado())) {
				validacao.add("O campo tipo de autorização é de preenchimento obrigatório.");
			}
			if(Util.isNullOuVazio(dto.getCerhProcesso().getNumPortariaDocumento())) {
				validacao.add("O campo número da portaria é de preenchimento obrigatório.");
			}
			if(Util.isNull(dto.getCerhProcesso().getDtInicioAutorizacao())) {
				validacao.add("O campo data de início da autorização é de preenchimento obrigatório.");
			}
			if(Util.isNullOuVazio(dto.getCerhProcesso().getNumPrazoValidade())) {
				validacao.add("O campo prazo de validade é de preenchimento obrigatório.");
			}
		}
		else if(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.CANCELADO).equals(dto.getCerhProcesso().getIdeCerhSituacaoRegularizacao())) {
			if(Util.isNullOuVazio(dto.getCerhProcesso().getNumPortariaDocumento())) {
				validacao.add("O campo número da portaria é de preenchimento obrigatório.");
			}
			if(Util.isNull(dto.getCerhProcesso().getDtInicioAutorizacao())) {
				validacao.add("O campo data de cancelamento é de preenchimento obrigatório.");
			}
		}
		
		if(!Util.isNullOuVazio(validacao)) {
			throw new SeiaValidacaoRuntimeException(validacao);
		}
	}

	public void onChangeTipoUso() {
		if(dto.isProcessoSemTipologia()) {
			prepararTipoUsoSemTipologia();
		}
		else if(!Util.isNull(dto.getCerhTipoUsoSelecionado())) {
			dto.getCerhProcesso().setIdeCerhSituacaoRegularizacao(null);
			dto.getCerhTipoUsoSelecionado().setIdeCerh(dto.getCerhProcesso().getIdeCerh());
			dto.getCerhTipoUsoSelecionado().setIdeCerhProcesso(dto.getCerhProcesso());
			dto.getCerhProcesso().setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
			dto.getCerhProcesso().getCerhTipoUsoCollection().add(dto.getCerhTipoUsoSelecionado());
			atualizarListaSituacaoDaRegularizacao(dto.getCerhTipoUsoSelecionado().getIdeTipoUsoRecursoHidrico());
			limparCamposCerhProcesso();
		}
	}

	private void prepararTipoUsoSemTipologia() {
		if(dto.getListaTipoUsoRecursoHidricoSelecionado()==null){
			dto.setListaTipoUsoRecursoHidricoSelecionado(new ArrayList<TipoUsoRecursoHidrico>());
		}
		
		dto.getListaTipoUsoRecursoHidricoSelecionado().clear();
		for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
			if(tipoUsoRecursoHidrico.isValue()){
				dto.getListaTipoUsoRecursoHidricoSelecionado().add(tipoUsoRecursoHidrico);
			}
		}
		
		if(!dto.isEditar() && !Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoSelecionado())) {
			Collection<CerhTipoUso> cerhTipoUsoCollection = new ArrayList<CerhTipoUso>();
			for(TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidricoSelecionado()) {
				CerhTipoUso cerhTipoUso = new CerhTipoUso();
				cerhTipoUso.setIdeCerh(dto.getCerhProcesso().getIdeCerh());
				cerhTipoUso.setIdeCerhProcesso(dto.getCerhProcesso());
				cerhTipoUso.setIdeTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
				cerhTipoUsoCollection.add(cerhTipoUso);
			}
			dto.getCerhProcesso().getCerhTipoUsoCollection().clear();
			dto.getCerhProcesso().setCerhTipoUsoCollection(cerhTipoUsoCollection);
		}
	}
	
	public void onChangeSituacaoRegularizacao() {
		limparCamposCerhProcesso();
	}

	private void limparCamposCerhProcesso() {
		dto.getCerhProcesso().setDtInicioAutorizacao(null);
		dto.getCerhProcesso().setNumPortariaDocumento(null);
		dto.getCerhProcesso().setNumPrazoValidade(null);
		dto.getCerhProcesso().setIndPossuiCartaInexigibilidade(null);
		dto.getCerhProcesso().setIdeCerhTipoAtoDispensa(null);
		dto.getCerhProcesso().setIdeCerhTipoAutorizacaoOutorgado(null);
	}
	
	// RN - 00251
	private void atualizarListaSituacaoDaRegularizacao(TipoUsoRecursoHidrico tipoUsoRecursoHidrico){
		
		CerhSituacaoRegularizacao DISPENSADO = new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.DISPENSADO);
		CerhSituacaoRegularizacao INEXIGIVEL = new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.INEXIGIVEL);
		
		if(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.INTERVENCAO).equals(tipoUsoRecursoHidrico)){
			dto.getListaSituacaoRegularizacao().remove(DISPENSADO);
			if(!dto.getListaSituacaoRegularizacao().contains(INEXIGIVEL)){
				dto.getListaSituacaoRegularizacao().add(INEXIGIVEL);
			}
		} 
		else {
			dto.getListaSituacaoRegularizacao().remove(INEXIGIVEL);
			if(!dto.getListaSituacaoRegularizacao().contains(DISPENSADO)){
				dto.getListaSituacaoRegularizacao().add(DISPENSADO);
			}
		}
		Collections.sort((List<CerhSituacaoRegularizacao>) dto.getListaSituacaoRegularizacao());
	}
	
	public void calcularDataFimAutorizacao() {
		if(!Util.isNull(dto.getCerhProcesso().getDtInicioAutorizacao())) {
			Date dtFimAutorizacao = null;
			if(Util.isNull(dto.getCerhProcesso().getNumPrazoValidade())) {
				dtFimAutorizacao = Util.adicionarAnos(dto.getCerhProcesso().getDtInicioAutorizacao(), 0);
			}
			else {
				dtFimAutorizacao = Util.adicionarAnos(dto.getCerhProcesso().getDtInicioAutorizacao(), dto.getCerhProcesso().getNumPrazoValidade());
			}
			dto.setDtFimAutorizacao(dtFimAutorizacao);
		}
	}

	public void changeDataInicioAutorizacaoOutorgado(DateSelectEvent event){
		Date dtFimAutorizacao = null;
		if(!Util.isNull(dto.getCerhProcesso().getNumPrazoValidade())) {
			dtFimAutorizacao = Util.adicionarAnos(event.getDate(), dto.getCerhProcesso().getNumPrazoValidade());
		}
		dto.setDtFimAutorizacao(dtFimAutorizacao);
		atualizarDataInicioCaptacaoOuLancamento(event.getDate());
	}
	 
	public void changeDataInicioAutorizacaoDispensado(DateSelectEvent event){
		atualizarDataInicioCaptacaoOuLancamento(event.getDate());
	}
	
	public CerhCaracterizacaoCaptacaoLancamentoInterface getCaracterizacao(CerhCaracterizacaoDTO cerhCaracterizacaoDTO, TipoUsoRecursoHidrico tipoUsoRecursoHidrico){
		if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId())
				|| tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId())){
			return cerhCaracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhCaptacaoCaracterizacao();
		} 
		else if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())){
		}
		return null;
	}
	
	// 8961
	private void atualizarDataInicioCaptacaoOuLancamento(Date data) {
		if(!Util.isNullOuVazio(dto.getCerhProcesso().getCerhLocalizacaoGeograficaCollection())){
			for (CerhLocalizacaoGeografica cerhLocalizacaoGeografica : dto.getCerhProcesso().getCerhLocalizacaoGeograficaCollection()) {
				TipoUsoRecursoHidrico tipoUsoRecursoHidrico = cerhLocalizacaoGeografica.getIdeCerhTipoUso().getIdeTipoUsoRecursoHidrico();
				CerhAbaDTO abaDTO = cerhDTO.getAba(tipoUsoRecursoHidrico);
				if(!Util.isNullOuVazio(abaDTO.getListaCaracterizacaoDTO())){
					for (CerhCaracterizacaoDTO caracterizacaoDTO : abaDTO.getListaCaracterizacaoDTO()) {
						if(!Util.isNull(caracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhProcesso()) 
								&& caracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhProcesso().equals(dto.getCerhProcesso())){
							CerhCaracterizacaoCaptacaoLancamentoInterface caracterizacao = getCaracterizacao(caracterizacaoDTO, tipoUsoRecursoHidrico);
							if(!Util.isNull(caracterizacao)){
								caracterizacao.setData(data);
							}
						}
					}
				}
			}
		}
	}
	
	public void changeInexigibilidade(ValueChangeEvent event){
		if((Boolean) event.getNewValue() == false){
			dto.getCerhProcesso().setNumPortariaDocumento(null);
			dto.getCerhProcesso().setDtInicioAutorizacao(null);
		}
	}
	
	public CerhIncluirProcessoDTO getDto() {
		return dto;
	}

	public void setDto(CerhIncluirProcessoDTO dto) {
		this.dto = dto;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public boolean isPodeVisualizar() {
		return podeVisualizar;
	}

	public void setPodeVisualizar(boolean podeVisualizar) {
		this.podeVisualizar = podeVisualizar;
	}

	public Collection<CerhTipoUso> getListaCerhTipoUsoSelicionadoProcesso() {
		return listaCerhTipoUsoSelicionadoProcesso;
	}

	public void setListaCerhTipoUsoSelicionadoProcesso(Collection<CerhTipoUso> listaCerhTipoUsoSelicionadoProcesso) {
		this.listaCerhTipoUsoSelicionadoProcesso = listaCerhTipoUsoSelicionadoProcesso;
	}

}
