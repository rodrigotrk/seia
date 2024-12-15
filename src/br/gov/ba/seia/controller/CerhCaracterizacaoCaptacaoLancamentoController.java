package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.entity.CerhCaptacaoVazaoSazonalidade;
import br.gov.ba.seia.entity.CerhOutrosUsos;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhSituacaoRegularizacao;
import br.gov.ba.seia.entity.CerhTipoPrestadorServico;
import br.gov.ba.seia.entity.Mes;
import br.gov.ba.seia.enumerator.CerhSituacaoRegularizacaoEnum;
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.facade.CerhCaracterizacaoCaptacaoLancamentoFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoCaptacaoLancamentoInterface;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public abstract class CerhCaracterizacaoCaptacaoLancamentoController extends CerhFinalidadeController {

	@EJB
	private CerhCaracterizacaoCaptacaoLancamentoFacade facade;
	
	private Collection<CerhOutrosUsos> cerhOutrosUsosCollection;
	private Collection<CerhTipoPrestadorServico> cerhTipoPrestadorServicoCollection;
	
	public abstract CerhCaracterizacaoCaptacaoLancamentoInterface getCerhCaracterizacaoVazaoSazonalidade();
	public abstract CerhVazaoSazonalidadeInterface getCerhVazaoSazonalidade(MesEnum mes);
	public abstract void abrirDialogReplicarDias();

	
	public void listarCerhOutrosUsos() {
		try {
			if(Util.isNullOuVazio(cerhOutrosUsosCollection)){
				cerhOutrosUsosCollection = getFacade().listarOutrosUsos();
			}
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de Outros Usos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	public void listarTipoPrestadorServico() {
		try {
			if(Util.isNullOuVazio(cerhTipoPrestadorServicoCollection)) {
				cerhTipoPrestadorServicoCollection = getFacade().listarTipoPrestadorServicoCollection();
			}
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de " + Util.getString("cerh_finalidade_dados_abs_publico_prestador_servico") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	
	public void listarDadosDeclaradosSazonalidade(CerhCaracterizacaoCaptacaoLancamentoInterface caracterizacaoVazaoSazonalidade) {
		Collection<CerhVazaoSazonalidadeInterface> coll = caracterizacaoVazaoSazonalidade.getVazaoSazonalidadeCollection();
		if(Util.isNullOuVazio(coll)) { 
			coll = new ArrayList<CerhVazaoSazonalidadeInterface>();
			for (MesEnum mes : MesEnum.values()) {
				coll.add(getCerhVazaoSazonalidade(mes));
			}
			caracterizacaoVazaoSazonalidade.setVazaoSazonalidadeCollection(coll);
		}else {
			for (MesEnum mes : MesEnum.values()) {
				for(CerhVazaoSazonalidadeInterface sazonalidade : coll) {
					if(mes.getValue().equals(sazonalidade.getIdeMes().getIdeMes())) {
						sazonalidade.getIdeMes().setNomMes(mes.getNomMes());
					}
				}
			}
		}
	}
	
	@Override
	protected void prepararDialog(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		
		super.prepararDialog(caracterizacaoDTO, visualizacao);
		listarDadosDeclaradosSazonalidade(getCerhCaracterizacaoVazaoSazonalidade());

		for (CerhVazaoSazonalidadeInterface cerhVazaoSazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()){
			if(!Util.isNullOuVazio(cerhVazaoSazonalidade)) {
				calcularVazaoDiaria(cerhVazaoSazonalidade);
				calcularVazaoMensal(cerhVazaoSazonalidade);
			}
		}
		calcularMediaVazaoDiaria();
		calcularVazaoAnual();
		calcularVazaoMaximaInstantanea();
	}
	
	@Override
	protected void prepararDialogHistorico(CerhCaracterizacaoDTO caracterizacaoDTO, boolean visualizacao) throws Exception  {
		
		super.prepararDialogHistorico(caracterizacaoDTO, visualizacao);
		listarDadosDeclaradosSazonalidade(getCerhCaracterizacaoVazaoSazonalidade());

		for (CerhVazaoSazonalidadeInterface cerhVazaoSazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()){
			if(!Util.isNullOuVazio(cerhVazaoSazonalidade)) {
				calcularVazaoDiaria(cerhVazaoSazonalidade);
				calcularVazaoMensal(cerhVazaoSazonalidade);
			}
		}
		calcularMediaVazaoDiaria();
		calcularVazaoAnual();
		calcularVazaoMaximaInstantanea();
	}

	public void replicarVazao() {
		BigDecimal vazaoJaneiro = null;
		for (CerhVazaoSazonalidadeInterface sazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(sazonalidade.getIdeMes().equals(new Mes(MesEnum.JANEIRO.getValue()))) {
				vazaoJaneiro = sazonalidade.getValVazao(); 
			} 
			else {
				sazonalidade.setValVazao(vazaoJaneiro);
			}
			calcularVazoes(sazonalidade);
		}

	}

	public void replicarTempo() {
		Integer tempoJaneiro = null;
		for (CerhVazaoSazonalidadeInterface sazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(sazonalidade.getIdeMes().equals(new Mes(MesEnum.JANEIRO.getValue()))) {
				tempoJaneiro = sazonalidade.getValTempo(); 
			} 
			else {
				sazonalidade.setValTempo(tempoJaneiro);
			}
			calcularVazoes(sazonalidade);
		}
	}

	public void replicarDiasMes() {
		Integer diaMesJaneiro = null;
		for (CerhVazaoSazonalidadeInterface sazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(sazonalidade.getIdeMes().equals(new Mes(MesEnum.JANEIRO.getValue()))) {
				diaMesJaneiro = sazonalidade.getValDiaMes();
				if(diaMesJaneiro > 28){
					abrirDialogReplicarDias();
					return;
				}
			} 
			else {
				sazonalidade.setValDiaMes(diaMesJaneiro);
			}
			calcularVazoes(sazonalidade);
		}
	}

	public void replicarDiasMesMaximoDias(){
		for (CerhVazaoSazonalidadeInterface sazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(!sazonalidade.getIdeMes().equals(new Mes(MesEnum.JANEIRO.getValue()))) {
				sazonalidade.setValDiaMes(MesEnum.getMesEnum(sazonalidade.getIdeMes().getIdeMes()).getQtdDias());
			}
			calcularVazaoMensal(sazonalidade);
		}
		calcularVazoes();
	}

	public void calcularVazoes(AjaxBehaviorEvent event) {
		calcularVazaoDiaria(event);
		calcularVazaoMensal(event);
		calcularVazoes();
		atualizarLinhaEditada(event);
	}

	private void calcularVazoes(CerhVazaoSazonalidadeInterface captacaoVazaoSazonalidade) {
		calcularVazaoDiaria(captacaoVazaoSazonalidade);
		calcularVazaoMensal(captacaoVazaoSazonalidade);
		calcularVazoes();
	}

	private void atualizarLinhaEditada(AjaxBehaviorEvent event) {
		String id = event.getComponent().getId();
		String clientId = event.getComponent().getClientId();

		List<String> updates = new ArrayList<String>();
		//		updates.add(clientId); inputTempo inputM

		updates.add(clientId.replace(id, "vazaoDia"));
		updates.add(clientId.replace(id, "vazaoMes"));
		
		if(getTipoUsoRecursoHidricoEnum().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL)) {
			updates.add("formCaracterizacao:gridDadosDeclaradosMedia");
		} 
		
		else if(getTipoUsoRecursoHidricoEnum().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA)) {
			updates.add("formCaracterizacaoSubterranea:gridDadosDeclaradosMediaSubterranea");
		}
		else {
			updates.add("formLancamentoEfluentes:panelDadosAuxiliares");
		}
		updates.add("grlMessageTemplate");

		Html.atualizar(updates);
	}

	private void calcularVazoes() {
		calcularMediaVazaoDiaria();
		calcularVazaoMaximaInstantanea();
		calcularVazaoAnual();
	}

	protected void calcularVazaoDiaria(AjaxBehaviorEvent event) {
		CerhVazaoSazonalidadeInterface sazonalidade = (CerhVazaoSazonalidadeInterface) event.getComponent().getAttributes().get("sazonalidade");
		if(!Util.isNullOuVazio(sazonalidade.getValTempo()) && sazonalidade.getValTempo().compareTo(24) > 0) {
			MensagemUtil.erro(Util.getString("cerh_vazao_sazonalidade_validar_hora"));
			sazonalidade.setValTempo(24);
			Html.atualizar(event.getComponent().getClientId());
			Html.executarJS("$('#"+ event.getComponent().getClientId().replace(":", "\\\\:") + "').focus();");
		} 
		else {
			calcularVazaoDiaria(sazonalidade);
		}
	}

	public void calcularVazaoDiaria(CerhVazaoSazonalidadeInterface vazaoSazonalidade) {
		if(!Util.isNull(vazaoSazonalidade.getValVazao()) && !Util.isNull(vazaoSazonalidade.getValTempo())) {
			vazaoSazonalidade.setVazaoDia(vazaoSazonalidade.getValVazao().multiply(new BigDecimal(vazaoSazonalidade.getValTempo())));
		}
		else if(Util.isNullOuVazio(vazaoSazonalidade.getValTempo())) {
			vazaoSazonalidade.setVazaoDia(null);
		}
		else {
			vazaoSazonalidade.setVazaoDia(BigDecimal.valueOf(0.0));
		}
	}

	protected void calcularVazaoMensal(AjaxBehaviorEvent event) {
		CerhVazaoSazonalidadeInterface sazonalidade = (CerhVazaoSazonalidadeInterface) event.getComponent().getAttributes().get("sazonalidade");
		if(!Util.isNullOuVazio(sazonalidade.getValDiaMes()) && sazonalidade.getValDiaMes().compareTo(MesEnum.getMesEnum(sazonalidade.getIdeMes().getIdeMes()).getQtdDias()) > 0) {
			JsfUtil.addErrorMessage(Util.getString("cerh_vazao_sazonalidade_validar_dia"));
			sazonalidade.setValDiaMes(MesEnum.getMesEnum(sazonalidade.getIdeMes().getIdeMes()).getQtdDias());
			Html.atualizar(event.getComponent().getClientId());
			Html.executarJS("$('#"+ event.getComponent().getClientId().replace(":", "\\\\:") + "').focus();");
		}
		else {
			calcularVazaoMensal(sazonalidade);
		}
	}

	public void calcularVazaoMensal(CerhVazaoSazonalidadeInterface vazaoSazonalidade) {
		if(!Util.isNull(vazaoSazonalidade.getVazaoDia()) && !Util.isNull(vazaoSazonalidade.getValDiaMes())) {
			vazaoSazonalidade.setVazaoMes(vazaoSazonalidade.getVazaoDia().multiply(new BigDecimal(vazaoSazonalidade.getValDiaMes())));
		} 
		else if(Util.isNull(vazaoSazonalidade.getValDiaMes())) {
			vazaoSazonalidade.setVazaoMes(null);
		} 
		else {
			vazaoSazonalidade.setVazaoMes(BigDecimal.valueOf(0.0));
		}
	}

	public void calcularMediaVazaoDiaria() {
		BigDecimal mediaVazaoDiaria = BigDecimal.valueOf(0.0);
		Integer qtdMes = 0;
		for (CerhVazaoSazonalidadeInterface sazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(!Util.isNullOuVazio(sazonalidade.getVazaoDia())){
				mediaVazaoDiaria = mediaVazaoDiaria.add(sazonalidade.getVazaoDia());
				qtdMes++;
			}
		}
		if(!Util.isNullOuVazio(mediaVazaoDiaria)) {
			dto.getCaracterizacaoDTO().setMediaVazaoDiaria(mediaVazaoDiaria.divide(new BigDecimal(qtdMes), RoundingMode.HALF_UP));
		} 
		else {
			dto.getCaracterizacaoDTO().setMediaVazaoDiaria(null);
		}
	}

	public void calcularVazaoAnual() {
		BigDecimal mediaVazaoAnual = BigDecimal.valueOf(0.0);
		for (CerhVazaoSazonalidadeInterface sazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(!Util.isNullOuVazio(sazonalidade.getVazaoMes())){
				mediaVazaoAnual = mediaVazaoAnual.add(sazonalidade.getVazaoMes());
			}
		}
		if(!Util.isNullOuVazio(mediaVazaoAnual)) {
			dto.getCaracterizacaoDTO().setVazaoAnual(mediaVazaoAnual);
		} 
		else {
			dto.getCaracterizacaoDTO().setVazaoAnual(null);
		}
	}

	public void calcularVazaoMaximaInstantanea() {
		BigDecimal maiorVazao = BigDecimal.valueOf(0.0);
		for (CerhVazaoSazonalidadeInterface sazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(!Util.isNullOuVazio(sazonalidade.getVazaoDia()) && sazonalidade.getVazaoDia().compareTo(maiorVazao) == 1){
				maiorVazao = sazonalidade.getVazaoDia();
			}
		}
		if(!Util.isNullOuVazio(maiorVazao)) {
			getCerhCaracterizacaoVazaoSazonalidade().setValVazaoMaximaInstantanea(maiorVazao);
		} 
		else {
			getCerhCaracterizacaoVazaoSazonalidade().setValVazaoMaximaInstantanea(null);
		}
	}
	
	public boolean validarVazaoSazonalidade(){
		for (CerhVazaoSazonalidadeInterface cerhVazaoSazonalidade : getCerhCaracterizacaoVazaoSazonalidade().getVazaoSazonalidadeCollection()) {
			if(Util.isNullOuVazio(cerhVazaoSazonalidade.getValVazao()) 
				&& Util.isNullOuVazio(cerhVazaoSazonalidade.getValTempo()) 
				&& Util.isNullOuVazio(cerhVazaoSazonalidade.getValDiaMes())
					|| !Util.isNullOuVazio(cerhVazaoSazonalidade.getValVazao()) 
					&& !Util.isNullOuVazio(cerhVazaoSazonalidade.getValTempo()) 
					&& !Util.isNullOuVazio(cerhVazaoSazonalidade.getValDiaMes())){
			} 
			else {
				MensagemUtil.erro("Todas informações da sazonalidade no mês de " + cerhVazaoSazonalidade.getIdeMes().getNomMes() + " é de preenchimento obrigatório.");
				return false;
			}
		}
		if(Util.isNullOuVazio(dto.getCaracterizacaoDTO().getMediaVazaoDiaria()) 
				|| Util.isNullOuVazio(dto.getCaracterizacaoDTO().getVazaoAnual()) 
				|| Util.isNullOuVazio(getCerhCaracterizacaoVazaoSazonalidade().getValVazaoMaximaInstantanea())){
			MensagemUtil.msg0003("Os " + Util.getString("cerh_aba_cap_sup_dados_declarados") + " de ao menos um mês");
			return false;
		}
		return true;
	}
	
	protected void validarPorcentagem(AjaxBehaviorEvent event) {
		CerhCaptacaoVazaoSazonalidade sazonalidade = (CerhCaptacaoVazaoSazonalidade) event.getComponent().getAttributes().get("porcentagem");
		if(!Util.isNullOuVazio(sazonalidade.getValTempoCaptacao()) && sazonalidade.getValTempoCaptacao().compareTo(24) > 0) {
			JsfUtil.addErrorMessage(Util.getString("cerh_vazao_sazonalidade_validar_hora"));
			sazonalidade.setValTempoCaptacao(24);
			Html.atualizar(event.getComponent().getClientId());
			Html.executarJS("$('#"+ event.getComponent().getClientId().replace(":", "\\\\:") + "').focus();");
		} 
		else {
			calcularVazaoDiaria(sazonalidade);
		}
	}
	
	public boolean validarDataInicioCaptacao() {
		if(Util.isNull(getCerhCaracterizacaoVazaoSazonalidade().getData())) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_aba_cap_sup_data_incio_cap"));
			return false;
		}
		return true;
	}

	public boolean isNaoExibeData(){
		return !Util.isNull(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica()) &&
			    !Util.isNull(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso()) && 
				isNaoExibeData(getCerhCaracterizacao().getIdeCerhLocalizacaoGeografica().getIdeCerhProcesso());
	}
	
	private boolean isNaoExibeData(CerhProcesso cerhProcesso){
		return !Util.isNull(cerhProcesso)
			&& !Util.isNull(cerhProcesso.getIdeCerhSituacaoRegularizacao()) 
			&& (cerhProcesso.getIdeCerhSituacaoRegularizacao().equals(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.DISPENSADO)) 
						|| cerhProcesso.getIdeCerhSituacaoRegularizacao().equals(new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.OUTORGADO)));
	}
	
	public Collection<CerhOutrosUsos> getCerhOutrosUsosCollection() {
		return cerhOutrosUsosCollection;
	}

	public Collection<CerhTipoPrestadorServico> getCerhTipoPrestadorServicoCollection() {
		return cerhTipoPrestadorServicoCollection;
	}
	
	public boolean isRenderObservacacao(){
		return (isProcessoDispensado() || isProcessoOutorgado()) && (!ContextoUtil.getContexto().isUsuarioExterno());
	}
	
	@Override
	public CerhCaracterizacaoCaptacaoLancamentoFacade getFacade(){
		return facade;
	}
}
