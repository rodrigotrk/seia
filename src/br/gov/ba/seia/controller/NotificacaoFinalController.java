package br.gov.ba.seia.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.FinalidadeReenquadramentoProcesso;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.MotivoNotificacaoImovel;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.entity.ObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PotencialPoluicao;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoPotencialPoluicao;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAtoObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAtoTipoAtividadeFauna;
import br.gov.ba.seia.entity.ReenquadramentoTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.ReenquadramentoTipologia;
import br.gov.ba.seia.entity.ReenquadramentoTipologiaEmpreendimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.TipoAtividadeFauna;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.FinalidadeReenquadramentoEnum;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.facade.NotificacaoFinalServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

@Named("notificacaoFinalController")
@ViewScoped
public class NotificacaoFinalController {
	
	@EJB
	private NotificacaoFinalServiceFacade notificacaoFinalServiceFacade;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;

	private NotificacaoFinalDTO dto;
	
	@PostConstruct
	private void init() {
		limparTudo();
	}
	
	public void limparTudo() {
		dto = new NotificacaoFinalDTO();
	}

	public void load(VwConsultaProcesso vwp, TipoNotificacaoEnum tipoNotificacaoEnum, boolean acessoFeitoPorPermissaoConcedida) {
		try{
			dto = notificacaoFinalServiceFacade.carregarNotificacaoFinal(vwp, tipoNotificacaoEnum, acessoFeitoPorPermissaoConcedida);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void atualizarMotivoNotificacao() {
		try {
			dto.setListaNotificacaoMotivoNotificacaoSelecionado(new ArrayList<NotificacaoMotivoNotificacao>());
			if(!Util.isNull(dto.getNotificacaoMotivoNotificacaoSelecionado())) {
				dto.getListaNotificacaoMotivoNotificacaoSelecionado().add(dto.getNotificacaoMotivoNotificacaoSelecionado());
				if(isReenquadramento()) {
					dto.setNotificacaoComArquivosApensados(false);
					notificacaoFinalServiceFacade.carregarDadosReequadramentoProcesso(dto);
				}
		
			}
			Html.atualizar("notificacaoFinalForm:pnlDocumentosNotificacao","notificacaoFinalForm:botoes"
					,"notificacaoFinalForm:documentosDaNotificacao");
			
			
			if(isRenderedDlgReenquadramentoNotC()) {
				Html.exibir("dlgMensagemReenquadramentoNotC");
			}
			
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void alterarAtosAutorizativos() {
		try {
			marcarFinalidade(dto.isAlterarAtoAutorizativo(), FinalidadeReenquadramentoEnum.ALTERACAO_ATOS_AUTORIZATIVOS);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void incluirNovoAtoAutorizativo() {
		try {
			marcarFinalidade(dto.isIncluirNovoAtoAutorizativo(), FinalidadeReenquadramentoEnum.INCLUSAO_NOVOS_ATOS_AUTORIZATIVOS);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void alterarPotencialPoluidor() {
		try {
			marcarFinalidade(dto.isAlterarPotencialPoluidor(), FinalidadeReenquadramentoEnum.ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void alterarClasseEmpreendimento() {
		try {
			marcarFinalidade(dto.isAlterarClasseEmpreendimento(), FinalidadeReenquadramentoEnum.ALTERACAO_CLASSE_EMPREENDIMENTO);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void alterarTipologia() {
		try {
			marcarFinalidade(dto.isAlterarTipologia(), FinalidadeReenquadramentoEnum.ALTERACAO_TIPOLOGIA);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void corrigirPorte() {
		try {
			marcarFinalidade(dto.isCorrigirPorte(), FinalidadeReenquadramentoEnum.CORRECAO_PORTE_EMPREENDIMENTO);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void marcarFinalidade(boolean resposta, FinalidadeReenquadramentoEnum finalidadeReenquadramentoEnum) {
		FinalidadeReenquadramentoProcesso frp = new FinalidadeReenquadramentoProcesso(dto.getReenquadramentoProcesso(), finalidadeReenquadramentoEnum);
		if(resposta) {
			dto.getReenquadramentoProcesso().getFinalidadeReequadramentoProcessoCollection().add(frp);
		}
		else {
			dto.getReenquadramentoProcesso().getFinalidadeReequadramentoProcessoCollection().remove(frp);
			switch (finalidadeReenquadramentoEnum) {
			case ALTERACAO_ATOS_AUTORIZATIVOS:
				dto.setListaAlteracaoReenquadramentoProcessoAto(new ArrayList<ReenquadramentoProcessoAto>());
				dto.setListaProcessoAtoParaReenquadramento(null);
				break;
			case INCLUSAO_NOVOS_ATOS_AUTORIZATIVOS:
				dto.setListaInclusaoReenquadramentoProcessoAto(new ArrayList<ReenquadramentoProcessoAto>());
				break;
			case ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE:
				dto.setListaReenquadramentoPotencialPoluicao(new ArrayList<ReenquadramentoPotencialPoluicao>());
				dto.setListaRequerimentoTipologiaParaReenquadramento(null);
				break;
			case ALTERACAO_CLASSE_EMPREENDIMENTO:
				dto.getReenquadramentoProcesso().setIdeClasseEmpreendimentoInicial(null);
				dto.getReenquadramentoProcesso().setIdeNovaClasseEmpreendimento(null);
				break;
			case ALTERACAO_TIPOLOGIA:
				dto.setListaReenquadramentoTipologia(new ArrayList<ReenquadramentoTipologia>());
				break;
			case CORRECAO_PORTE_EMPREENDIMENTO:
				dto.getReenquadramentoProcesso().setIdePorteEmpreendimentoInicial(null);
				dto.getReenquadramentoProcesso().setIdeNovoPorteEmpreendimento(null);
				break;
			default:
				break;
			}
		}
		gerarTextoNotificacao();
	}

	public boolean isBloquearEnvio() {
		if(dto.getNotificacaoComArquivosApensados()) {
			if(verificarSeListaDocumentosVaziaOuNula()){
				return true;
			}
			for(ArquivoProcesso arq : dto.getListaDeDocumentosDaNotificacao()){
				if(arq.getIdeArquivoProcesso()==null && (arq.getDscArquivoProcesso()==null||arq.getDscArquivoProcesso().equals(""))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void onChangeClasse() {
		gerarTextoNotificacao();
	}
	
	public void onChangePotencialPoluicao() {
		gerarTextoNotificacao();
	}
	
	public boolean isNotificacaoPrazo() {
		return TipoNotificacaoEnum.NOTIFICACAO_PRAZO.equals(dto.getTipoNotificacaoEnum());
	}
	
	public boolean isNotificacaoComunicacao() {
		return TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO.equals(dto.getTipoNotificacaoEnum());
	}
	
	public boolean isRenderedColumnImovel() {
		return !Util.isNullOuVazio(dto.getListaImovel());
	}
	
	public boolean isRenderedPnlReequadramentoProcesso() {
		if(Util.isNull(dto.getNotificacaoMotivoNotificacaoSelecionado())) {
			return false;
		}
		return isReenquadramento();
	}
	
	public boolean isDisabledApensarDocumentos() {
		return isReenquadramento();
	}
	
	public boolean isRenderedApensarDocumentos() {
		return !isReenquadramento();
	}
	
	public boolean isRenderedTextoNotificacao() {
		return !isReenquadramento();
	}

	public Boolean isRenderedDlgReenquadramentoNotC() throws ParseException {
			
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		 String dataAux= "01/10/2014";
		 Date dataNOTC=sdf.parse(dataAux);
		
		if(isReenquadramento() &&  Util.validarDuasDatas(dto.getVwProcesso().getDtcFormacao(), dataNOTC)){
			return true;
			
		}
		 return false;
	}
	
	
	private boolean isReenquadramento() {

		return MotivoNotificacaoEnum.REENQUADRAMENTO_PROCESSO.equals(dto.getNotificacaoMotivoNotificacaoSelecionado());
	}
	
	public void adicionarRemoverMotivo(NotificacaoMotivoNotificacao nmn) {
		if(nmn.getIdeMotivoNotificacao().isChecked()) {
				if(dto.getListaNotificacaoMotivoNotificacaoSelecionado().isEmpty()){
					dto.getListaNotificacaoMotivoNotificacaoSelecionado().add(nmn);
					
				}else{
					if(nmn.getIdeNotificacaoMotivoNotificacao().intValue()>0){
						nmn.setIdeNotificacaoMotivoNotificacao(-1);
						dto.getListaNotificacaoMotivoNotificacaoSelecionado().add(nmn);
					}else{
						dto.getListaNotificacaoMotivoNotificacaoSelecionado().add(nmn);
					}
				}	
		}
		else{
				dto.getListaNotificacaoMotivoNotificacaoSelecionado().remove(nmn);
		}
	}
	
	public void adicionarRemoverMotivoEnvioShape(AjaxBehaviorEvent evt) {
		NotificacaoMotivoNotificacao nmn = (NotificacaoMotivoNotificacao) evt.getComponent().getAttributes().get("nmn");
		if(nmn.getIdeMotivoNotificacao().isChecked()) {
			dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado().add(nmn);
		}
		else{
			nmn.setMotivoNotificacaoImovelCollection(null);
			dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado().remove(nmn);
		}
		String clientId = evt.getComponent().getParent().getParent().getParent().getClientId() + ":imovel";
		Html.atualizar(clientId);
	}
	
	public void adicionarOuRemoverMotivoImovel(NotificacaoMotivoNotificacao nmn) {

		List<NotificacaoMotivoNotificacao> lista = (List<NotificacaoMotivoNotificacao>) dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado();
		
		if(nmn.getIdeMotivoNotificacao().isChecked()) {	

			for(int i = lista.size()-1 ; i>=0; i--) {
				
				NotificacaoMotivoNotificacao item =  (NotificacaoMotivoNotificacao) lista.get(i);
				
				if(item.equals(nmn)){
					if(!Util.isNullOuVazio(item.getMotivoNotificacaoImovelCollection())) {
						lista.set(i, nmn);
						break;
					}else {
						item.setMotivoNotificacaoImovelCollection(new ArrayList<MotivoNotificacaoImovel>());
						lista.set(i, nmn);
						break;
					}
				}
			}
			
		} else {
			for(int i = lista.size()-1 ; i>=0; i--) {
				
				NotificacaoMotivoNotificacao item =  (NotificacaoMotivoNotificacao) lista.get(i);
				
				if(item.equals(nmn)){
					lista.remove(i);
				}
			}
		}
	}

	public void salvarNotificacaoFinal() {
		NotificacaoFinalDTO bkp=null;
		try{
			bkp = dto.clone();
			dto.setSalvandoNotificacao(true);
			notificacaoFinalServiceFacade.salvarNotificacaoFinal(dto);
			notificacaoFinalServiceFacade.recarregar(dto);
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		}
		catch(SeiaValidacaoRuntimeException e) {
			dto=bkp;
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch(Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar notificação.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean verificarSeListaDocumentosVaziaOuNula() {

		return dto.getListaDeDocumentosDaNotificacao() == null || dto.getListaDeDocumentosDaNotificacao().isEmpty();
	}
	
	public void confirmacao() {
		try{
			if(dto.getUsuarioCoordenador() && dto.getNotificacaoFinal().isNotificacaoPrazo()) {
				Html.exibir("msgConfirmacao");
			}
			else{
				enviarNotificacaoFinal();
			}
		} 
		catch(SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch(Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar notificação.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void enviarNotificacaoFinal() {
		NotificacaoFinalDTO bkp=null;
		try {
			bkp = dto.clone();
			notificacaoFinalServiceFacade.enviarNotificacaoFinal(dto);

			if(dto.getUsuarioCoordenador()) {
				RequestContext.getCurrentInstance().execute("consultarPautaGestor();");
			} else {
				RequestContext.getCurrentInstance().execute("consultarPautaTecnico();");
			}
			
			RequestContext.getCurrentInstance().execute("dialogNotificacar.hide();");
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		} 
		catch (SeiaValidacaoRuntimeException e) {
			dto=bkp;
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar enviar a notificação.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void onChangeChbxApensarDocumentosNotificacao(ValueChangeEvent e){
		Boolean newValue = (Boolean) e.getNewValue();
		dto.setNotificacaoComArquivosApensados(newValue);
	}
	
	public boolean existeDocumentoSemDescricao(){
		for(ArquivoProcesso arq : dto.getListaDeDocumentosDaNotificacao()) {
			if(!arq.isDscConfirmada()){
				return true;
			}
		}
		return false;
	}
		
	public void onRowEdit(RowEditEvent event) {
	 	ArquivoProcesso arquivoEditado = ((ArquivoProcesso) event.getObject());
	 	if(!Util.isNullOuVazio(arquivoEditado.getDscArquivoProcesso())){
	 		arquivoEditado.setDscConfirmada(true);
		}
    }
	
	public void onChangeAlteracaoProcessoAto(AjaxBehaviorEvent evt) {
		
		Collection<ReenquadramentoProcessoAto> novaListaAlteracaoReenquadramentoProcessoAto = null;
		
		if(!Util.isNullOuVazio(dto.getListaProcessoAtoParaReenquadramento())) {
			novaListaAlteracaoReenquadramentoProcessoAto = new ArrayList<ReenquadramentoProcessoAto>();
			if(Util.isNullOuVazio(dto.getListaAlteracaoReenquadramentoProcessoAto())) {
				for(ProcessoAto pa : dto.getListaProcessoAtoParaReenquadramento()) {
					ReenquadramentoProcessoAto rpa = new ReenquadramentoProcessoAto();
					rpa.setIdeProcessoAto(pa);
					novaListaAlteracaoReenquadramentoProcessoAto.add(rpa);
				}
			}
			else{
				Collection<ProcessoAto> listaProcessoAtoParaReenquadramentoClone = Util.deepCloneList(dto.getListaProcessoAtoParaReenquadramento());
				for (ReenquadramentoProcessoAto rpa : dto.getListaAlteracaoReenquadramentoProcessoAto()) {
					if(listaProcessoAtoParaReenquadramentoClone.contains(rpa.getIdeProcessoAto())) {
						listaProcessoAtoParaReenquadramentoClone.remove(rpa.getIdeProcessoAto());
						novaListaAlteracaoReenquadramentoProcessoAto.add(rpa);
					}
				}
				for (ProcessoAto pa : listaProcessoAtoParaReenquadramentoClone) {
					ReenquadramentoProcessoAto rpa = new ReenquadramentoProcessoAto();
					rpa.setIdeProcessoAto(pa);
					novaListaAlteracaoReenquadramentoProcessoAto.add(rpa);
				}
			}
			dto.setListaAlteracaoReenquadramentoProcessoAto(novaListaAlteracaoReenquadramentoProcessoAto);
		}
		else{
			dto.setListaAlteracaoReenquadramentoProcessoAto(null);
			gerarTextoNotificacao();
		}
	}
	
	public void onChangeRequerimentoTipologia() {
		try{
			Collection<ReenquadramentoPotencialPoluicao> novaListaReenquadramentoPotencialPoluicao = null;
			if(!Util.isNullOuVazio(dto.getListaRequerimentoTipologiaParaReenquadramento())) {
				novaListaReenquadramentoPotencialPoluicao = new ArrayList<ReenquadramentoPotencialPoluicao>();
				if(Util.isNullOuVazio(dto.getListaReenquadramentoPotencialPoluicao())) {
					for(RequerimentoTipologia rt : dto.getListaRequerimentoTipologiaParaReenquadramento()) {
						ReenquadramentoPotencialPoluicao rpp = new ReenquadramentoPotencialPoluicao(dto.getReenquadramentoProcesso());
						rpp.setIdeRequerimentoTipologia(rt);
						PotencialPoluicao idePotencialPoluicao = rpp.getIdeRequerimentoTipologia().getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdePotencialPoluicao();
						rpp.setIdePotencialPoluicaoInicial(idePotencialPoluicao.clone());
						rpp.setIdePotencialPoluicaoNovo(idePotencialPoluicao.clone());
						novaListaReenquadramentoPotencialPoluicao.add(rpp);
					}
				}
				else{
					Collection<RequerimentoTipologia> listaRequerimentoTipologiaParaReenquadramentoClone = Util.deepCloneList(dto.getListaRequerimentoTipologiaParaReenquadramento());
					for (ReenquadramentoPotencialPoluicao rpp : dto.getListaReenquadramentoPotencialPoluicao()) {
						if(listaRequerimentoTipologiaParaReenquadramentoClone.contains(rpp.getIdeRequerimentoTipologia())) {
							listaRequerimentoTipologiaParaReenquadramentoClone.remove(rpp.getIdeRequerimentoTipologia());
							novaListaReenquadramentoPotencialPoluicao.add(rpp);
						}
					}
					for (RequerimentoTipologia rt : listaRequerimentoTipologiaParaReenquadramentoClone) {
						ReenquadramentoPotencialPoluicao rpp = new ReenquadramentoPotencialPoluicao(dto.getReenquadramentoProcesso());
						rpp.setIdeRequerimentoTipologia(rt);
						PotencialPoluicao idePotencialPoluicao = rpp.getIdeRequerimentoTipologia().getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdePotencialPoluicao();
						rpp.setIdePotencialPoluicaoInicial(idePotencialPoluicao.clone());
						rpp.setIdePotencialPoluicaoNovo(idePotencialPoluicao.clone());
						novaListaReenquadramentoPotencialPoluicao.add(rpp);
					}
				}
				dto.setListaReenquadramentoPotencialPoluicao(novaListaReenquadramentoPotencialPoluicao);
			}
			else{
				dto.setListaReenquadramentoPotencialPoluicao(null);
			}
			gerarTextoNotificacao();
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void gerarTextoNotificacao() {
		dto.getNotificacaoFinal().setDscNotificacao(notificacaoFinalServiceFacade.montarTextoNotificacao(dto));
		Html.atualizar("notificacaoFinalForm:texto_final");
	}
	
	public void selecionarReenquadramentoProcessoAto(ReenquadramentoProcessoAto reenquadramentoProcessoAto) {
		dto.setReenquadramentoProcessoAtoSelecionado(reenquadramentoProcessoAto);
	}
	
	public void adicionarReenquadramentoProcessoAto() {
		if(Util.isNullOuVazio(dto.getListaInclusaoReenquadramentoProcessoAto())) {
			dto.setListaInclusaoReenquadramentoProcessoAto(new ArrayList<ReenquadramentoProcessoAto>());
		}
		
		if(!Util.isNullOuVazio(dto.getListaInclusaoReenquadramentoProcessoAto()) && isAtoRepitido(dto.getListaInclusaoReenquadramentoProcessoAto(), dto.getReenquadramentoProcessoAtoSelecionado().getIdeNovoAtoAmbiental())){
			Html.exibir("dlgConfirmaDuplicidadeAto");
		}
		else{
			fluxoIncluirAtoAmbiental();
		}
		
	}

	public void fluxoIncluirAtoAmbiental() {
		dto.getListaInclusaoReenquadramentoProcessoAto();
		dto.getListaInclusaoReenquadramentoProcessoAto().add(dto.getReenquadramentoProcessoAtoSelecionado());
		Html.esconder("dlgSelecionarAtoAmbiental");
		gerarTextoNotificacao();
		Html.atualizar("notificacaoFinalForm:pnlReequadramentoProcesso");
	}
	
	private boolean isAtoRepitido(Collection<ReenquadramentoProcessoAto> listaTesteParam, AtoAmbiental atoAmbientalSelecionadoParam) {
		for(ReenquadramentoProcessoAto item :  listaTesteParam){
			if(item.getIdeNovoAtoAmbiental().equals(atoAmbientalSelecionadoParam)){
				return true;
			}
		}
		return false;
	}
	
	
	public void selecionarAtoAmbiental(AtoAmbiental atoAmbiental) {
		
		if(Util.isNullOuVazio(dto.getReenquadramentoProcessoAtoSelecionado()) || Boolean.FALSE.equals(atoAmbiental.getIndAlteracao())) {
			dto.setReenquadramentoProcessoAtoSelecionado(new ReenquadramentoProcessoAto());
		}
		
		dto.getReenquadramentoProcessoAtoSelecionado().setIdeReenquadramentoProcesso(dto.getReenquadramentoProcesso());
		dto.getReenquadramentoProcessoAtoSelecionado().setIdeNovoAtoAmbiental(atoAmbiental);
		dto.getReenquadramentoProcessoAtoSelecionado().setIdeNovaTipologia(atoAmbiental.getTipologia());
		dto.getReenquadramentoProcessoAtoSelecionado().setDscJustificativa(atoAmbiental.getDscJustificativa());
		insereReenquadramentoTipologiaEmpreendimento(atoAmbiental);
		insereReenquadramentoTipoFinalidadeUsoAgua(atoAmbiental);
		insereReenquadramentoProcessoAtoObjetivoAtividadeManejo(atoAmbiental);
		insereReenquadramentoProcessoAtoTipoAtividadeFauna(atoAmbiental);
		
		gerarTextoNotificacao();
		Html.atualizar("notificacaoFinalForm:pnlReequadramentoProcesso");
	}
	
	public void adicionarTipologia(Tipologia tipologia) {
		
		ReenquadramentoTipologia reenquadramentoTipologia = new ReenquadramentoTipologia();
		reenquadramentoTipologia.setIdeReenquadramentoProcesso(dto.getReenquadramentoProcesso());
		reenquadramentoTipologia.setIdeTipologia(tipologia);
		
		if(Util.isNullOuVazio(dto.getListaReenquadramentoTipologia())) {
			dto.setListaReenquadramentoTipologia(new ArrayList<ReenquadramentoTipologia>());
			dto.getListaReenquadramentoTipologia().add(reenquadramentoTipologia);
			gerarTextoNotificacao();
			Html.atualizar("notificacaoFinalForm:pnlReequadramentoProcesso");
			JsfUtil.addSuccessMessage("A tipologia foi adicionada.");
		}
		else {
			boolean adicionar = true;
			for(ReenquadramentoTipologia rt : dto.getListaReenquadramentoTipologia()) {
				if(tipologia.equals(rt.getIdeTipologia())) {
					adicionar = false;
					break;
				}
			}
			if(adicionar) {
				dto.getListaReenquadramentoTipologia().add(reenquadramentoTipologia);
				gerarTextoNotificacao();
				Html.atualizar("notificacaoFinalForm:pnlReequadramentoProcesso");
				JsfUtil.addSuccessMessage("A tipologia foi adicionada.");
			}
			else{
				JsfUtil.addWarnMessage("A tipologia selecionado já foi adicionada.");
			}
		}
	}
	
	public void removerTipologia(ReenquadramentoTipologia reenquadramentoTipologia) {
		List<ReenquadramentoTipologia> lista = (List<ReenquadramentoTipologia>) dto.getListaReenquadramentoTipologia();
		for (int index = dto.getListaReenquadramentoTipologia().size()-1; index>=0 ; index--) {
			ReenquadramentoTipologia rt = lista.get(index);
			if(rt.getIdeTipologia().equals(reenquadramentoTipologia.getIdeTipologia())) {
				lista.remove(index);
			}
		}
		gerarTextoNotificacao();
		Html.atualizar("notificacaoFinalForm:pnlReequadramentoProcesso");
	}
	
	public void removerInclusaoAtoAmbiental(ReenquadramentoProcessoAto reenquadramentoProcessoAto) {
		List<ReenquadramentoProcessoAto> lista = (List<ReenquadramentoProcessoAto>) dto.getListaInclusaoReenquadramentoProcessoAto();
		for (int index = dto.getListaInclusaoReenquadramentoProcessoAto().size()-1; index >= 0 ; index--) {
			ReenquadramentoProcessoAto rpa = lista.get(index);
			if(rpa.getIdeNovoAtoAmbiental() == reenquadramentoProcessoAto.getIdeNovoAtoAmbiental()) 
			 {
				
				lista.remove(index);
			}
		}
		gerarTextoNotificacao();
		Html.atualizar("notificacaoFinalForm:pnlReequadramentoProcesso");
	}
	
	public void onRowCancel(RowEditEvent event) {
		ArquivoProcesso arquivoEditado = ((ArquivoProcesso) event.getObject());
		arquivoEditado.setDscConfirmada(true);
	}
	
	public void marcarDocumentoComoNaoConfirmado(ArquivoProcesso arquivoProcesso){
		arquivoProcesso.setDscConfirmada(false);
	}
	
	public void trataArquivo(FileUploadEvent event) {
		String caminho = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.PROCESSO.toString());
		Pessoa pessoa = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
		ArquivoProcesso arq = new ArquivoProcesso(caminho, "", dto.getProcesso(), pessoa);
		arq.setDtcCriacao(new Date());
		arq.setIdeNotificacao(dto.getNotificacaoFinal());
		try{
			CategoriaDocumento ctgDocumento = notificacaoFinalServiceFacade.carregarCategoriaDocumentoNotificacao();
			arq.setCategoriaDocumento(ctgDocumento);
			if(dto.getListaDeDocumentosDaNotificacao() == null) {
				dto.setListaDeDocumentosDaNotificacao(new ArrayList<ArquivoProcesso>());
			}
			dto.getListaDeDocumentosDaNotificacao().add(arq);
			JsfUtil.addSuccessMessage("O documento foi adicionado a lista.");
		} 
		catch(Exception e){
			JsfUtil.addErrorMessage("Não foi possível adicionar o documento a lista.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirDocumentoNotificacao(){
		try{
			if(dto.getDocumentoNotificacaoSelecionado().getIdeArquivoProcesso()==null){
				dto.getListaDeDocumentosDaNotificacao().remove(dto.getDocumentoNotificacaoSelecionado());
			}
			else{
				notificacaoFinalServiceFacade.deletarArquivo(dto.getDocumentoNotificacaoSelecionado());
				dto.getListaDeDocumentosDaNotificacao().remove(dto.getDocumentoNotificacaoSelecionado());
			}			
			JsfUtil.addSuccessMessage("O documento foi removido com sucesso.");
		}
		catch(Exception e){
			JsfUtil.addErrorMessage("Erro ao tentar remover o documento.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public Collection<MotivoNotificacaoImovel> listarMotivoNotificacaoImovel(NotificacaoMotivoNotificacao nmn) {
		try{
			Collection<MotivoNotificacaoImovel> listarMotivoNotificacaoImovel = 
					notificacaoFinalServiceFacade.listarMotivoNotificacaoImovel(nmn.getIdeNotificacaoMotivoNotificacao());
			Collection<MotivoNotificacaoImovel> listarRetorno = new ArrayList<MotivoNotificacaoImovel>();
			Collection<Imovel> listaImovel = null;
			if(new MotivoNotificacao(MotivoNotificacaoEnum.RELOCACAO_RESERVA_LEGAL.getId()).equals(nmn.getIdeMotivoNotificacao()) 
					// verificar ASV
					|| new MotivoNotificacao(MotivoNotificacaoEnum.SHAPE_ASV.getId()).equals(nmn.getIdeMotivoNotificacao())) {
				listaImovel = dto.getListaImovelFlorestal();
			} 
			else {
				listaImovel = dto.getListaImovel();
			}

			if(Util.isNullOuVazio(listaImovel)) {
				nmn.setSemImovel(true);
			}
						
			for(Imovel i : listaImovel) {
				if(Util.isNullOuVazio(listarMotivoNotificacaoImovel)) {
					MotivoNotificacaoImovel mniNew = new MotivoNotificacaoImovel();
					mniNew.setIdeMotivoNotificacaoImovel(i.getIdeImovel());
					mniNew.setIdeNotificacaoMotivoNotificacao(nmn);
					mniNew.setIdeImovel(i);
					listarRetorno.add(mniNew);
				} else {					
					MotivoNotificacaoImovel mniADD = null;
					for(MotivoNotificacaoImovel mni : listarMotivoNotificacaoImovel) {
						if(listarRetorno.contains(mni)) {
							continue;
						} else {
							if(mni.getIdeImovel().equals(i)) {
								mniADD=mni;
								break;
							}
						}
					}					
					if(mniADD==null) {
						mniADD = new MotivoNotificacaoImovel();
						mniADD.setIdeMotivoNotificacaoImovel(i.getIdeImovel());
						mniADD.setIdeNotificacaoMotivoNotificacao(nmn);
						mniADD.setIdeImovel(i);
					}					
					listarRetorno.add(mniADD);
				}				
			}
			return listarRetorno;
		}
		catch(Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarListaDeDocumentosDaNotificacao() {
		try{
			
			if(dto.getNotificacaoComArquivosApensados()){
				dto.setListaDeDocumentosDaNotificacao(arquivoProcessoService.listarArquivosProcessoPorNotificacao(dto.getNotificacaoFinal()));
				
			}else{
				for (ArquivoProcesso arqProcss : dto.getListaDeDocumentosDaNotificacao()) {
					if(arqProcss.getIdeArquivoProcesso()==null){
						dto.getListaDeDocumentosDaNotificacao().remove(arqProcss);
					} else {
						arquivoProcessoService.deletarArquivo(arqProcss);
						dto.getListaDeDocumentosDaNotificacao().remove(arqProcss);
					}	
				}
			}
		}
		catch(Exception e){
			dto.setListaDeDocumentosDaNotificacao(new ArrayList<ArquivoProcesso>());
		}
	}
	
	public Collection<ProcessoAto> listarAtosExistentes() {
		return dto.getListaProcessoAtoParaReenquadramento();
	}
	
	public Notificacao getNotificacaoFinal() {
		return dto.getNotificacaoFinal();
	}

	public void setNotificacaoFinal(Notificacao notificacaoFinal) {
		this.dto.setNotificacaoFinal(notificacaoFinal);
	}
	
	public Boolean getNotificacaoComArquivosApensados() {
		return dto.getNotificacaoComArquivosApensados();
	}

	public void setNotificacaoComArquivosApensados(Boolean notificacaoComArquivosApensados) {
		dto.setNotificacaoComArquivosApensados(notificacaoComArquivosApensados);
	}

	public NotificacaoFinalDTO getDto() {
		return dto;
	}

	public void setDto(NotificacaoFinalDTO dto) {
		this.dto = dto;
	}

	public MetodoUtil getMetodoSelecionarAtoAmbiental() {
		return new MetodoUtil(this, "selecionarAtoAmbiental", AtoAmbiental.class);
	}

	public MetodoUtil getMetodoSelecionarReenquadramentoProcessoAto() {
		return new MetodoUtil(this, "selecionarReenquadramentoProcessoAto", ReenquadramentoProcessoAto.class);
	}

	public MetodoUtil getMetodoAdicionarReenquadramentoProcessoAto() {
		return new MetodoUtil(this, "adicionarReenquadramentoProcessoAto");
	}
	
	public MetodoUtil getMetodoAdicionarTipologia() {
		return new MetodoUtil(this, "adicionarTipologia", Tipologia.class);
	}
	
	public MetodoUtil getMetodoListarAtosExistentes() {
		return new MetodoUtil(this, "listarAtosExistentes");
	}
	
	public void insereReenquadramentoTipologiaEmpreendimento(AtoAmbiental atoAmbiental) {
		if (atoAmbiental.getListaReenquadramentoTipologiaEmpreendimento() != null && atoAmbiental.getListaReenquadramentoTipologiaEmpreendimento().size() > 0) {
			dto.getReenquadramentoProcessoAtoSelecionado().setReenquadramentoTipologiaEmpreendimentoCollection(new ArrayList<ReenquadramentoTipologiaEmpreendimento>());
			for (Tipologia tipologia : atoAmbiental.getListaReenquadramentoTipologiaEmpreendimento()) {
				ReenquadramentoTipologiaEmpreendimento rte = new ReenquadramentoTipologiaEmpreendimento();
				rte.setIdeReenquadramentoProcessoAto(dto.getReenquadramentoProcessoAtoSelecionado());
				rte.setIdeTipologia(tipologia);
				dto.getReenquadramentoProcessoAtoSelecionado().getReenquadramentoTipologiaEmpreendimentoCollection().add(rte);
			}
		}

	}
	
	public void insereReenquadramentoTipoFinalidadeUsoAgua(AtoAmbiental atoAmbiental) {
		dto.getReenquadramentoProcessoAtoSelecionado().setReenquadramentoTipoFinalidadeUsoAguaCollection(new ArrayList<ReenquadramentoTipoFinalidadeUsoAgua>());
		if (!Util.isNullOuVazio(atoAmbiental.getListaTipoFinalidadeUsoAgua())) {
			for (TipoFinalidadeUsoAgua tipologia : atoAmbiental.getListaTipoFinalidadeUsoAgua()) {
				ReenquadramentoTipoFinalidadeUsoAgua rtfua = new ReenquadramentoTipoFinalidadeUsoAgua();
				rtfua.setIdeReenquadramentoProcessoAto(dto.getReenquadramentoProcessoAtoSelecionado());
				rtfua.setIdeTipoFinalidadeUsoAgua(tipologia);
				dto.getReenquadramentoProcessoAtoSelecionado().getReenquadramentoTipoFinalidadeUsoAguaCollection().add(rtfua);
			}
		}
	}
	
	public void insereReenquadramentoProcessoAtoObjetivoAtividadeManejo(AtoAmbiental atoAmbiental) {
		dto.getReenquadramentoProcessoAtoSelecionado().setReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection(new ArrayList<ReenquadramentoProcessoAtoObjetivoAtividadeManejo>());
		if (!Util.isNullOuVazio(atoAmbiental.getListaObjetivoAtividadeManejo())) {
			for (ObjetivoAtividadeManejo oam : atoAmbiental.getListaObjetivoAtividadeManejo()) {
				ReenquadramentoProcessoAtoObjetivoAtividadeManejo rpaoam = new ReenquadramentoProcessoAtoObjetivoAtividadeManejo(); 
				rpaoam.setIdeReenquadramentoProcessoAto(dto.getReenquadramentoProcessoAtoSelecionado());
				rpaoam.setIdeObjetivoAtividadeManejo(oam);
				dto.getReenquadramentoProcessoAtoSelecionado().getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection().add(rpaoam);
			}
		}
	}
	
	public void insereReenquadramentoProcessoAtoTipoAtividadeFauna(AtoAmbiental atoAmbiental) {
		dto.getReenquadramentoProcessoAtoSelecionado().setReenquadramentoProcessoAtoTipoAtividadeFaunaCollection(new ArrayList<ReenquadramentoProcessoAtoTipoAtividadeFauna>());
		if (!Util.isNullOuVazio(atoAmbiental.getListaObjetivoAtividadeManejo())) {
			for (TipoAtividadeFauna taf : atoAmbiental.getListaTipoAtividadeFauna()) {
				ReenquadramentoProcessoAtoTipoAtividadeFauna rpataf = new ReenquadramentoProcessoAtoTipoAtividadeFauna(); 
				rpataf.setIdeReenquadramentoProcessoAto(dto.getReenquadramentoProcessoAtoSelecionado());
				rpataf.setIdeTipoAtividadeFauna(taf);
				dto.getReenquadramentoProcessoAtoSelecionado().getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection().add(rpataf);
			}
		}
	}

}