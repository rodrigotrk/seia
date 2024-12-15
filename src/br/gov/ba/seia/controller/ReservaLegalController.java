package br.gov.ba.seia.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.TipoArl;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.entity.TipoOrigemCertificado;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("reservaLegalController")
@ViewScoped
public class ReservaLegalController extends ImovelRuralControllerNew {
	
	private static final long serialVersionUID = 1L;

	private ReservaLegal reservaLegalSelecionada;	
	private List<DocumentoImovelRural> listaArquivosReserva;
	private Boolean indAprovacao;
	private Integer ideImovelRural;
	private boolean indAverbadaDesabilitada;
	private boolean indAprovadaDesabilitada;
	private boolean renderedBotaoSalvar;
	private TipoEstadoConservacao tipoEstadoConservacaoInicial;
		
	@PostConstruct
	public void init() {
		if(Util.isNullOuVazio(super.imovelRural) && !Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural())) {
			super.imovelRural = ContextoUtil.getContexto().getImovelRural();
		} 
		
		if(!Util.isNullOuVazio(super.imovelRural)) {
			carregarTipoEstadoConservacao();
			carregarDatums();
			carregarListaPradImportado(1);
			carregarSecaoGeometrica();

			if (Util.isNull(getReservaLegal())) {
				if (!Util.isNull(super.imovelRural.getIndDesejaCompletarInformacoes()) && super.imovelRural.getIndDesejaCompletarInformacoes()){
					super.imovelRural.setReservaLegal(new ReservaLegal());
					getReservaLegal().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					getReservaLegal().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
					getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
					getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());				
					getReservaLegal().setAceiteCondicoesRecuperacaoRl(false);
					getReservaLegal().setIdeTipoArl(new TipoArl());					
				}
			}
			
			if (!Util.isNullOuVazio(listaTipoEstadoConservacao)){
				tipoEstadoConservacaoInicial = getReservaLegal().getIdeTipoEstadoConservacao();
			}
			
			if (!Util.isNull(super.imovelRural.getIndReservaLegal())){
				setIndAprovadaDesabilitada(true);
			}
			
			if (!Util.isNull(getReservaLegal()) && !Util.isNull(getReservaLegal().getIndAverbada())){
				setIndAverbadaDesabilitada(true);
			}
		}		
		
		verificarTipoArl();
	}

	public void verificarTipoArl() {
		if(super.imovelRural != null){
			if(!Util.isNullOuVazio(getReservaLegal()) 
					&& !Util.isNullOuVazio(getReservaLegal().getIdeTipoArl()) 
					&& !Util.isNullOuVazio(getReservaLegal().getIdeTipoArl().getIdeTipoArl())){
				this.setRenderedBotaoSalvar(true);
				
				if (Util.isNullOuVazio(listaTipoEstadoConservacao)){
					carregarTipoEstadoConservacao();
				}
			}else {
				this.setRenderedBotaoSalvar(false);
			}
		}
	}

	public boolean isDisabledTipoARL() {
		if(Util.isNullOuVazio(super.imovelRural) && !Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural())) {
			super.imovelRural = ContextoUtil.getContexto().getImovelRural();
		} 
		
		if(!Util.isNullOuVazio(super.imovelRural)){
			return isReservaRelocada() || (super.imovelRural.getReservaLegalAprovadaOuAverbada() && !isEditavelParaUsuarioSemRestricao());
		}else{
			return true;
		}
		
	}
	
	public boolean isDisabledAreaAverbada() {
		if(!Util.isNullOuVazio(imovelRural)){
			return isReservaRelocada() || (imovelRural.getReservaLegalAprovadaOuAverbada() && !isEditavelParaUsuarioSemRestricao());
		}else{
			return true;
		}
	}
	public boolean isDisabledExcluirPoligonal() {
		if(!Util.isNullOuVazio(imovelRural)){
			return isReservaRelocada() || (imovelRural.getReservaLegalAprovadaOuAverbada() && !isEditavelParaUsuarioSemRestricao());
		}else{
			return true;
		}
	}

	private boolean isReservaRelocada() {
		if(!Util.isNullOuVazio(imovelRural)){
			return new DadoOrigem(DadoOrigemEnum.TECNICO.getId()).equals(imovelRural.getReservaLegal().getIdeDadoOrigem());
		}else{
			return false;
		}
	}

	public ReservaLegal getReservaLegal() {
		if(!Util.isNullOuVazio(imovelRural)){
			if(Util.isNullOuVazio(imovelRural.getIdeImovelRural())){
				super.imovelRural = ContextoUtil.getContexto().getImovelRural();
			}
		}else{
			super.imovelRural = ContextoUtil.getContexto().getImovelRural();
		}
		
		if(Util.isNullOuVazio(super.imovelRural.getReservaLegal())){
			super.imovelRural.setReservaLegal(new ReservaLegal());
		}
		
		if(!Util.isNullOuVazio(super.imovelRural.getReservaLegal().getIdeTipoArl())){
			if(Util.isNullOuVazio(super.imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl())){
				super.imovelRural.getReservaLegal().setIdeTipoArl(new TipoArl(0));
				super.imovelRural.getReservaLegal().getIdeTipoArl().setDscTipoArl("Selecione...");
				super.imovelRural.getReservaLegal().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
		}else{
			super.imovelRural.getReservaLegal().setIdeTipoArl(new TipoArl(0));
			super.imovelRural.getReservaLegal().getIdeTipoArl().setDscTipoArl("Selecione...");
			super.imovelRural.getReservaLegal().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		if(!Util.isNullOuVazio(super.imovelRural.getReservaLegal().getCronogramaRecuperacao())
				   && Util.isNull(super.imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica())){
				  super.imovelRural.getReservaLegal().getCronogramaRecuperacao().setLocalizacaoGeografica(new LocalizacaoGeografica());
				}
		
		return super.imovelRural.getReservaLegal();
	}
	
	/***
	 * Este método é responsável por controlar o evento change da pergunta 'tipo de reserva legal' no questionário
	 * Caso seja escolhido o 'No Próprio imóvel' e o tipo Estado de Conservação da RL é Degradada ou Parcialmente Degradada é atribuido null ao tipo estado conservação da RL. 
	 */
	public void changeTipoArl(ValueChangeEvent event){
		boolean escondeBotaoProximo = true;
		if(!Util.isNull(event)){
			TipoArl tipoarl = (TipoArl) event.getNewValue();
			if(Util.isNull(tipoarl) || tipoarl.getIdeTipoArl() == 0) {
				getReservaLegal().setIdeTipoEstadoConservacao(null);
				escondeBotaoProximo = false;
			} else {
				if((tipoarl.getIdeTipoArl().equals(TipoArlEnum.NPI.getId()) || tipoarl.getIdeTipoArl().equals(TipoArlEnum.ECIP.getId())) && !Util.isNullOuVazio(getReservaLegal().getIdeTipoEstadoConservacao()) && getReservaLegal().getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao() != PRESERVADA) {
					getReservaLegal().setIdeTipoEstadoConservacao(null);					
				}else {
					getReservaLegal().setIndDesejaCadPrad(null);
					getReservaLegal().setDtcRespDesejaCadPrad(null);
					getReservaLegal().setCronogramaRecuperacao(null);
					getReservaLegal().setAceiteCondicoesRecuperacaoRl(false);
				}
			}
		}
		if(escondeBotaoProximo){
			RequestContext.getCurrentInstance().execute("$(\"[id*='_next']\").hide()");
		} else {
			RequestContext.getCurrentInstance().execute("$(\"[id*='_next']\").show()");
		}
	}
	
	public boolean getExibeSelecioneTipoArl(){
		
		if(!Util.isNullOuVazio(super.imovelRural) 
				&& !Util.isNullOuVazio(getReservaLegal())
				&& !Util.isNullOuVazio(getReservaLegal().getIdeTipoArl()) 
				&& super.imovelRural.getStatusCadastro() != null 
				&& super.imovelRural.isCadastrado()
				&& (getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.NPI.getId()) 
						|| getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId()))){
			
				return false;
		}
		return true;
	}
	
	public List<TipoArl> getListTipoArl() {
		try {
			List<TipoArl> listaTipoArls = new ArrayList<TipoArl>(super.imovelRuralServiceFacade.listarTipoArls());
			
			if(!Util.isNullOuVazio(super.imovelRural) && !Util.isNullOuVazio(getReservaLegal()) && !Util.isNullOuVazio(getReservaLegal().getIdeTipoArl()) 
					&& super.imovelRural.getStatusCadastro() != null && super.imovelRural.isCadastrado()
					&& (getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.NPI.getId()) 
							|| getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId()))
					){ 
					
					List<Integer> list = new ArrayList<Integer>();
					list.add(39817);
					list.add(39828);
					list.add(39832);
					list.add(39830);
					
					if(!list.contains(super.imovelRural.getIdeImovelRural())) {
						listaTipoArls.remove(new TipoArl(TipoArlEnum.ECOND.getId()));
						listaTipoArls.remove(new TipoArl(TipoArlEnum.CDAUC.getId()));
					}
					
					listaTipoArls.remove(new TipoArl(TipoArlEnum.ECSF.getId()));
			}
			
			return listaTipoArls;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new ArrayList<TipoArl>();
	}
	
	public String carregarReservaLegalParaValidacao(){
		try{
			indAprovacao = null;
			listaArquivosReserva = new ArrayList<DocumentoImovelRural>();			
			if (!Util.isNullOuVazio(reservaLegalSelecionada)){
				reservaLegalSelecionada = imovelRuralServiceFacade.carregarTudo(reservaLegalSelecionada);
				reservaLegalSelecionada.setIdeReservaLegalAverbada(super.imovelRuralServiceFacade.carregarReservaLegalAverbada(reservaLegalSelecionada));
				if(!Util.isNullOuVazio(reservaLegalSelecionada.getIdeReservaLegalAverbada()) && !Util.isNullOuVazio(reservaLegalSelecionada.getIdeReservaLegalAverbada().getIdeDocumentoAverbacao())) {
					this.listaArquivosReserva.add(reservaLegalSelecionada.getIdeReservaLegalAverbada().getIdeDocumentoAverbacao());					
				}
				if(!Util.isNullOuVazio(reservaLegalSelecionada.getIdeDocumentoAprovacao())) {
					this.listaArquivosReserva.add(reservaLegalSelecionada.getIdeDocumentoAprovacao());					
				}
			}			
		} catch (Exception e) {			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}	
		
		return null;
	}
	
	public void salvarValidacaoDocumento(){
		try{
			if(!Util.isNullOuVazio(reservaLegalSelecionada)){		
				if(!Util.isNullOuVazio(indAprovacao)){
					ReservaLegal objAntigo = imovelRuralServiceFacade.carregarTudo(reservaLegalSelecionada);
					reservaLegalSelecionada.setDtcAprovacao(new Date());				
					reservaLegalSelecionada.setIdeUsuarioAprovacao(new Usuario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
					validarAprovacao(objAntigo);
					reservaLegalSelecionada = null;
					RequestContext context = RequestContext.getCurrentInstance();
					context.addCallbackParam("closeModal", true);
				} else {
					JsfUtil.addErrorMessage("O preenchimento do campo " + getPerguntaValidacao() + " é obigatório!");
				}
			}
			
			
		} catch (Exception e) {			
			JsfUtil.addErrorMessage(e.getMessage());
		}		
	}

	private void validarAprovacao(ReservaLegal objAntigo) throws Exception {
		if(indAprovacao){
			if(reservaLegalSelecionada.getIdeStatus().getIdeStatusReservaLegal().equals(4)){
				reservaLegalSelecionada.setIdeStatus(new StatusReservaLegal(2));
				imovelRuralServiceFacade.atualizarReservaLegal(reservaLegalSelecionada);
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S007"));
			} else if(reservaLegalSelecionada.getIdeStatus().getIdeStatusReservaLegal().equals(8)){
				reservaLegalSelecionada.setIdeStatus(new StatusReservaLegal(1));
				imovelRuralServiceFacade.atualizarReservaLegal(reservaLegalSelecionada);
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S008"));
			}
			auditoria.atualizar(objAntigo, reservaLegalSelecionada);
		}else{
			if(!Util.isNullOuVazio(reservaLegalSelecionada.getObservacao())){
				reservaLegalSelecionada.setIdeStatus(new StatusReservaLegal(9));
				imovelRuralServiceFacade.atualizarReservaLegal(reservaLegalSelecionada);
				auditoria.atualizar(objAntigo, reservaLegalSelecionada);
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S012"));
			}else{
				throw new Exception("O preenchimento do campo Observações é obrigatório!");
			}
		}
	}
	
	public void changeProcessoTramite(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();
		if(!valor){
			getReservaLegal().setNumProcesso(null);
		}
	}
	
	public List<TipoOrigemCertificado> getListTipoOrigemCertificado() {
		try {
			return (List<TipoOrigemCertificado>) imovelRuralServiceFacade.listarTipoOrigemCertificado();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new ArrayList<TipoOrigemCertificado>();
	}
	
	public List<DocumentoImovelRural> getListDocumentoAprovacaoReserva() {
		List<DocumentoImovelRural> list = new ArrayList<DocumentoImovelRural>();
		if(!Util.isLazyInitExcepOuNull(getReservaLegal().getIdeDocumentoAprovacao())) {
			list.add(getReservaLegal().getIdeDocumentoAprovacao());
		}
		return list;
	}
	
	public void uploadReservalLegalDocumentoAprovacao(FileUploadEvent event) {
		String tipoDocumento = "COMPROVANTE_APROVACAO";
		String caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString()+
						 FileUploadUtil.getFileSeparator()+super.imovelRural.getIdeImovelRural().toString(),
						 super.imovelRural.getIdeImovelRural().toString()+"_"+tipoDocumento);
		if(!Util.isNullOuVazio(getReservaLegal().getIdeDocumentoAprovacao())) {
			getReservaLegal().getIdeDocumentoAprovacao().setDscCaminhoArquivo(caminhoArquivo);
			getReservaLegal().getIdeDocumentoAprovacao().setNomDocumentoObrigatorio(tipoDocumento);			
				
		}else{
			getReservaLegal().setIdeDocumentoAprovacao(new DocumentoImovelRural(null, tipoDocumento, caminhoArquivo));
			File file = new File(caminhoArquivo.trim());
			getReservaLegal().getIdeDocumentoAprovacao().setFileSize(file.length());
		}			
		if(!Util.isNullOuVazio(getReservaLegal().getIdeStatus()) && getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(9)) {
			getReservaLegal().setAlterarStatusRl(true);
		} else {
			getReservaLegal().setAlterarStatusRl(false);
		}	
		JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
	}
	
	public void excluirDocumentoAprovacaoReserva() {
		try {
			if (!imovelRural.getIsFinalizado() && !Util.isNullOuVazio(getReservaLegal().getIdeReservaLegal())) {
				imovelRuralServiceFacade.removerDocumentoAprovacaoReserva(getReservaLegal());
			}
			getReservaLegal().setIdeDocumentoAprovacao(null);
			
			JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
			return;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003"));
	}
	
	public void changeReservaLegalAprovada(ValueChangeEvent event) {
        Boolean valor = (Boolean) event.getNewValue();
        if(valor){
                getReservaLegal().setIndProcessoTramite(null);
                getReservaLegal().setNumProcesso(null);
        }
	}
	
	public Boolean getMostraDesejaCadastrarCronogramaRl() {
		if(ContextoUtil.getContexto().isPCT() && !Util.isNull(getReservaLegal())) {
			return super.getMostraCronograma(getReservaLegal().getIdeTipoEstadoConservacao());
		}else {
			return false;
		}
	}
	
	public void excluirCronogramaEtapaRl() {		
		getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection().remove(cronogramaEtapaSelecionado);
		if(getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection().isEmpty()) {
			getReservaLegal().setAceiteCondicoesRecuperacaoRl(false);
		}else {
			getReservaLegal().setAceiteCondicoesRecuperacaoRl(true);
		}
		JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S003"));
	}
	
	public void salvarReservaLegal() {
		try {
			
			Boolean isFinalizadoRL = false;
			
			if(!Util.isNull(imovelRural.getReservaLegal())
					&& !Util.isNull(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())
					&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoArl()) 
					&& (imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.NPI.getId()) 
							|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId()))) {
				if (imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isShapeFile()
						&& !super.habilitaSalvarShape(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())
						&& !getExisteTheGeomRl()) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()
						&& !temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId())
						&& !getExisteTheGeomRl()) {
					JsfUtil.addErrorMessage(Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}
			
			if(!Util.isNull(imovelRural.getReservaLegal().getCronogramaRecuperacao())
					&& !Util.isNull(imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica())) {
				if (imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao().isShapeFile()
						&& !super.habilitaSalvarShape(imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica())
						&& !getExisteTheGeomPradRl()) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()
						&& !temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId())
						&& !getExisteTheGeomPradRl()) {
					JsfUtil.addErrorMessage(Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}
			
			if(!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
				if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeReservaLegal())) {
					isFinalizadoRL = true;
				}
			}
			
			if(!super.isUsuarioSemRestricao()){
				imovelRuralServiceFacade.validarReservaLegal(super.imovelRural);
			}
			
			validarTipoRL();
			
			if(!Util.isNullOuVazio(getReservaLegal().getIdeReservaLegalAverbada())) {
				getReservaLegal().getIdeReservaLegalAverbada().setIdeReservaLegal(getReservaLegal());
			}
			
			// Se for um novo cadastro
			if(!super.imovelRural.getIsFinalizado()) {
				ReservaLegal objAntigoReservaLegal = null;
				CronogramaRecuperacao objAntigoCronogramaRecuperacaoRl = null;
				
				if(!Util.isNullOuVazio(getReservaLegal().getIdeLocalizacaoGeografica())) {
					getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
					getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
				}

				if(!Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao()) 
						&& !Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao().getIndPradImportada()) 
						&& getReservaLegal().getCronogramaRecuperacao().getIndPradImportada()){
					getReservaLegal().getCronogramaRecuperacao().setIdeDocumentoObrigatorio(retornarDocumentoPradRlSelecionado(getReservaLegal().getCronogramaRecuperacao().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
				}
				
				objAntigoReservaLegal = imovelRuralServiceFacade.carregarTudo(getReservaLegal());
				
				if (!Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao()) 
						&& !Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao().getIdeCronogramaRecuperacao())) {
					objAntigoCronogramaRecuperacaoRl = imovelRuralServiceFacade.filtrarCronogramaRecuperacaoById(getReservaLegal().getCronogramaRecuperacao());
				}
				
				imovelRuralServiceFacade.persistirReservaLegal(super.imovelRural, objAntigoReservaLegal, objAntigoCronogramaRecuperacaoRl);
				
				if(!Util.isNull(getReservaLegal().getIdeLocalizacaoGeografica()) 
						&& !Util.isNull(getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) 
						&& getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(getReservaLegal().getIdeLocalizacaoGeografica(), TemaGeoseiaEnum.RESERVA_LEGAL.getId().toString(), imovelRural.getIdeImovelRural().toString());
					imovelRuralServiceFacade.persistirShapes(getReservaLegal().getIdeLocalizacaoGeografica(), null);
					getReservaLegal().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
				}
					
				if(!Util.isNull(getReservaLegal().getCronogramaRecuperacao()) 
						&& !Util.isNull(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica())
						&& !Util.isNull(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getNovosArquivosShapeImportados())
						&& getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica(), TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId().toString(), imovelRural.getIdeImovelRural().toString());
					imovelRuralServiceFacade.persistirShapes(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica(), null);
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
				}
			} else {
				if(super.getMostraCronograma(getReservaLegal().getIdeTipoEstadoConservacao()) && !super.naoExibeEstadoConservacaoRecuperada(getReservaLegal().getCronogramaRecuperacao())){
					JsfUtil.addWarnMessage(Util.getString("cefir_msg_A057"));
				}
			}
			
			ContextoUtil.getContexto().setImovelRural(super.imovelRural);
			setIndAprovadaDesabilitada(true);
			setIndAverbadaDesabilitada(true);
			if(isFinalizadoRL) {
				JsfUtil.addSuccessMessage("Alteração efetuada com sucesso.");
			}else {
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			}
		} catch (Exception exception) {
			if(!Util.isNullOuVazio(exception.getMessage())){
				JsfUtil.addErrorMessage(exception.getMessage());
			}else{
				JsfUtil.addErrorMessage("Ocorreu erro ao salvar");
			}
		}
	}

	private void validarTipoRL() throws Exception {
		if(!Util.isNullOuVazio(getReservaLegal().getIdeTipoArl()) 
				&& (getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.NPI.getId()) 
						|| getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId()))){
			getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
			getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
			
			ReservaLegal reservaLegalTemp = imovelRuralServiceFacade.carregarTudo(getReservaLegal());
			
			if (!Util.isNull(getReservaLegal().getCronogramaRecuperacao())) {
				getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
				getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
				
				getReservaLegal().getCronogramaRecuperacao().setIdeReservaLegal(getReservaLegal());
				getReservaLegal().getCronogramaRecuperacao().setDtcCriacao(new Date());
				
				if(!Util.isNull(getReservaLegal().getCronogramaRecuperacao().getIndPradImportada()) 
						&& getReservaLegal().getCronogramaRecuperacao().getIndPradImportada()){
					getReservaLegal().getCronogramaRecuperacao().setIdeDocumentoObrigatorio(
							retornarDocumentoPradRlSelecionado(getReservaLegal().getCronogramaRecuperacao().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));					
				}
				
				if(Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica())) {
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setDtcCriacao(new Date());
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setDtcExclusao(null);
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIndExcluido(Boolean.FALSE); 
				}
				
				if(temShapeTemporario(super.imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId())){
					persistirShapesPradRl();
				}
			}
				
			if (!Util.isNullOuVazio(reservaLegalTemp) && !Util.isNullOuVazio(reservaLegalTemp.getIdeTipoEstadoConservacao()) 
					&& !reservaLegalTemp.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId())
					&& !Util.isNullOuVazio(reservaLegalTemp.getCronogramaRecuperacao())) {
		         this.listaPradImportadosRl = new ArrayList<DocumentoImovelRural>();
			}
			
			if (Util.isNullOuVazio(getReservaLegal().getIdeLocalizacaoGeografica())) {
				getReservaLegal().getIdeLocalizacaoGeografica().setDtcCriacao(new Date());
				getReservaLegal().getIdeLocalizacaoGeografica().setDtcExclusao(null);
				getReservaLegal().getIdeLocalizacaoGeografica().setIndExcluido(Boolean.FALSE); 
			}
			
			if(temShapeTemporario(super.imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId())){
				persistirShapesRl();
			}
		}
	}
		
	private void persistirShapesRl() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() 
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId() + FileUploadUtil.getFileSeparator();
		String nome = "shapeReservaLegal";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_" + getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();
		
		try {
			getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
							
			getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(
					retornaSistemaCordenadaSelecionado(getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
			
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);
			
			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(), 
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId(), 
					getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(), 
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
			
			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da Reserva Legal");
			} else if(retorno[0].equalsIgnoreCase("ERRO")){
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}
			
			String geometriaRl = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(super.imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);
			
			if(!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaRl)){
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}
			
			getReservaLegal().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
			getReservaLegal().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			
			if(!Util.isNullOuVazio(getReservaLegal().getIdeStatus()) && getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(9)){
				getReservaLegal().setAlterarStatusRl(true);
			}else{
				getReservaLegal().setAlterarStatusRl(false);
			}
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}
	
	private void persistirShapesPradRl() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() 
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId()	+ FileUploadUtil.getFileSeparator();
		String nome = "shapePradReservaLegal";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_" + getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();
		
		try {
			getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
							
			getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeSistemaCoordenada(
					retornaSistemaCordenadaSelecionado(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
			
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);
			
			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					super.imovelRural.getIdeImovelRural(), 
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId(), 
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(), 
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
			
			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da Área Degradada da Reserva Legal");
			} else if(retorno[0].equalsIgnoreCase("ERRO")){
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}
			
			String geometriaPradRl = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(super.imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId(), null);
			
			if(!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaPradRl)){
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}
			
			getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
			getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			
			if(!Util.isNullOuVazio(getReservaLegal().getIdeStatus()) && getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(9)){
				getReservaLegal().setAlterarStatusRl(true);
			}else{
				getReservaLegal().setAlterarStatusRl(false);
			}
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}
	
	public void uploadPradRl(FileUploadEvent event) {
		try {
			Boolean primeiroArquivo = true;
			String tipoDocumento = "PRA";
			String nmArquivo = imovelRural.toString()+"_"+tipoDocumento+"_RL";
			String caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString() 
					+ FileUploadUtil.getFileSeparator() + imovelRural.getIdeImovelRural().toString() 
					+ FileUploadUtil.getFileSeparator() + "PRA_Rl", nmArquivo);
			ReservaLegal reservaLegalTemp = imovelRuralServiceFacade.carregarTudo(getReservaLegal());
			
			if(!Util.isNullOuVazio(reservaLegalTemp) && !Util.isNullOuVazio(reservaLegalTemp.getCronogramaRecuperacao()) && !Util.isNullOuVazio(reservaLegalTemp.getCronogramaRecuperacao().getIdeDocumentoObrigatorio())){
				List<CronogramaRecuperacao> lCronogramasCadastrados = obterCronogramaPorDocumento(reservaLegalTemp.getCronogramaRecuperacao());
				
				if(cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
					imovelRuralServiceFacade.removerDocumentoPrad(reservaLegalTemp.getCronogramaRecuperacao());
				} else {
					tratarDocumentoPRAAssociado(lCronogramasCadastrados, reservaLegalTemp.getCronogramaRecuperacao());
				}
				
				primeiroArquivo = false;
			}
			
			getReservaLegal().getCronogramaRecuperacao().setIdeDocumentoObrigatorio(new DocumentoImovelRural(null, nmArquivo, caminhoArquivo));
			File file = new File(caminhoArquivo.trim());
			getReservaLegal().getCronogramaRecuperacao().getIdeDocumentoObrigatorio().setFileSize(file.length());
			
			if(primeiroArquivo){
				RequestContext.getCurrentInstance().execute("dlgUploadPradRl.hide()");
			}else{
				String hora = new SimpleDateFormat("HH:mm:ss").format(new Date()); 
				System.out.println(hora+"- PRA da Reserva Legal "+getReservaLegal().getIdeReservaLegal().toString()+" foi sobrescrito somente na pasta, pelo usuario de CPF= "+ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getNumCpf());
				RequestContext.getCurrentInstance().execute("dlgUploadPradRl.hide()");
			}
			
			carregarListaPradImportado(1);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}				
	}
	
//Reserva Legal Averbada
	
	public void changeReservaLegalAverbada(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();
		if(valor && Util.isNullOuVazio(getReservaLegal().getIdeReservaLegalAverbada())) {
			getReservaLegal().setIdeReservaLegalAverbada(new ReservaLegalAverbada());
		}
	}
	
	public void anulaValorReservaLegalAverbada(){

	}
	
	public List<DocumentoImovelRural> getListDocumentoAverbacaoReserva() {
		List<DocumentoImovelRural> list = new ArrayList<DocumentoImovelRural>();
		if(!Util.isNullOuVazio(getReservaLegal().getIdeReservaLegalAverbada()) && !Util.isNullOuVazio(getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao())) {
			list.add(getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao());
		}
		return list;
	}
	
	public void excluirDocumentoAverbacaoReserva() {
		try {
			if (!imovelRural.getIsFinalizado()) {
				imovelRuralServiceFacade.removerDocumentoAverbacaoReserva(getReservaLegal()
						.getIdeReservaLegalAverbada());
			}
			getReservaLegal().getIdeReservaLegalAverbada().setIdeDocumentoAverbacao(null);
			
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_msg005"));
			return;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003"));
	}
	
	public void uploadDocumentoAverbacao(FileUploadEvent event) {
		String tipoDocumento = "COMPROVANTE_AVERBACAO";
		String caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString()+
						 FileUploadUtil.getFileSeparator()+super.imovelRural.getIdeImovelRural().toString(),
						 super.imovelRural.getIdeImovelRural().toString()+"_"+tipoDocumento);
		if(!Util.isNullOuVazio(getReservaLegal().getIdeReservaLegalAverbada()) && !Util.isNullOuVazio(getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao())) {
			getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao().setDscCaminhoArquivo(caminhoArquivo);
			getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao().setNomDocumentoObrigatorio(tipoDocumento);
		}else{
			if(Util.isNullOuVazio(getReservaLegal().getIdeReservaLegalAverbada())) {
				getReservaLegal().setIdeReservaLegalAverbada(new ReservaLegalAverbada());
			}
			getReservaLegal().getIdeReservaLegalAverbada().setIdeDocumentoAverbacao(new DocumentoImovelRural(null, tipoDocumento, caminhoArquivo));
			File file = new File(caminhoArquivo.trim());
			getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao().setFileSize(file.length());
		}
		
		if(!Util.isNullOuVazio(getReservaLegal().getIdeStatus()) && getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(9)) {
			getReservaLegal().setAlterarStatusRl(true);
		} else {
			getReservaLegal().setAlterarStatusRl(false);
		}
		
		JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
	}
	
	public boolean getExisteTheGeomRl() {
		try {
			if (!Util.isNullOuVazio(getReservaLegal()) && getReservaLegal().getIdeLocalizacaoGeografica() != null) {
				return imovelRuralServiceFacade.existeTheGeom(getReservaLegal().getIdeLocalizacaoGeografica());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}
	
	public boolean getExisteTheGeomPradRl() {
		try {
			if (!Util.isNullOuVazio(getReservaLegal()) && getReservaLegal().getCronogramaRecuperacao() != null
					&& getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica() != null) {
				return imovelRuralServiceFacade.existeTheGeom(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}
	
	public Boolean getMostrarBotaoUploadShapeRl(){
		return !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()) 
				&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao())
				&& imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())
				&& (Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape()) 
					|| imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().size() < 4);
	}
	
	public Boolean getMostrarBotaoUploadShapePradRl(){
		return !Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao()) 
				&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao())
				&& imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())
				&& (Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape()) 
					|| imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape().size() < 4);
	}
	
	public void importarShapeRl(FileUploadEvent event) {
		String caminhoArquivo;
		try{
			if(Util.isNullOuVazio(getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape())){
				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			}
			
			if(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().size() > 3){
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			}else{
				caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
						+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId(), "shapeReservaLegal");
						
				if(getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				}else{
					if(caminhoArquivo.contains(".prj")){
						imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));
						
						if(Util.isNull(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())){
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().remove(FileUploadUtil.getFileName(caminhoArquivo));
							return;
						}
					}
					
					imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
					
					if(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().size() == 4){
						RequestContext.getCurrentInstance().execute("dlgUploadShapeRl.hide()");
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}
	
	public void importarShapePradRl(FileUploadEvent event) {	
		try{
			String caminhoArquivo;
			
			if(Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape())){
				getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			}
			
			if(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape().size() > 3){
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			}else{
				caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
						+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId(), "shapePradReservaLegal");
						
				if(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				}else{
					if(caminhoArquivo.contains(".prj")){
						getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));
						
						if(Util.isNull(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeSistemaCoordenada())){
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							return;
						}
					}
					
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
					
					if(getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape().size() == 4){
						RequestContext.getCurrentInstance().execute("dlgUploadShapePradRl.hide()");
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}
	
	private boolean isEstadoConversacaoDegradado(TipoEstadoConservacao tec) {
		if(!Util.isNullOuVazio(tec)) {
			return (TipoEstadoConservacaoEnum.DEGRADADA.getId().equals(tec.getId()) || TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId().equals(tec.getId()));
		}else {
			return false;
		}		
	}	
	
	private boolean isEstadoConservacaoPreservada(TipoEstadoConservacao tec) {
		if(!Util.isNullOuVazio(tec)) {
			return (TipoEstadoConservacaoEnum.PRESERVADA.getId().equals(tec.getId()));
		}else {
			return false;
		}		
	}
		
	@Override
	public void changeTipoEstadoConservacao(ValueChangeEvent event){	
		if(event != null && event.getNewValue() != null && event.getNewValue() instanceof TipoEstadoConservacao) {
			TipoEstadoConservacao tec = (TipoEstadoConservacao) event.getNewValue();
			
			if(ContextoUtil.getContexto().isPCT()) {
				if(!isEstadoConversacaoDegradado(tec) || !isEstadoConversacaoDegradado(tipoEstadoConservacaoInicial)) {
					getReservaLegal().setIndDesejaCadPrad(null);
					getReservaLegal().setDtcRespDesejaCadPrad(null);
					getReservaLegal().setCronogramaRecuperacao(null);
					getReservaLegal().setAceiteCondicoesRecuperacaoRl(false);
				}			
			}else {
				if(isEstadoConversacaoDegradado(tec) && Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao())) {
					getReservaLegal().setCronogramaRecuperacao(new CronogramaRecuperacao());
					getReservaLegal().getCronogramaRecuperacao().setLocalizacaoGeografica(new LocalizacaoGeografica());
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
					getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
				}else if((!Util.isNull(tec) && isEstadoConservacaoPreservada(tec)) ||
							Util.isNull(tec)) {
						getReservaLegal().setIndDesejaCadPrad(null);
						getReservaLegal().setDtcRespDesejaCadPrad(null);
						getReservaLegal().setCronogramaRecuperacao(null);
						getReservaLegal().setAceiteCondicoesRecuperacaoRl(false);
					}		
			}	
		} else {
			getReservaLegal().setIndDesejaCadPrad(null);
			getReservaLegal().setDtcRespDesejaCadPrad(null);
			getReservaLegal().setCronogramaRecuperacao(null);
			getReservaLegal().setAceiteCondicoesRecuperacaoRl(false);
		}
	}
	
	public void changeDesejaCadastrarCronograma() {
		if(getReservaLegal().getIndDesejaCadPrad()) {
			getReservaLegal().setCronogramaRecuperacao(new CronogramaRecuperacao());
			getReservaLegal().getCronogramaRecuperacao().setLocalizacaoGeografica(new LocalizacaoGeografica());
			getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
			getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
		}else {
			getReservaLegal().setCronogramaRecuperacao(null);
		}
	}	
	
	public boolean getCadastrarCronogramaRl() {
		if(ContextoUtil.getContexto().isPCT()) {
			Boolean desejaCadPrad = null;
			
			if(!Util.isNullOuVazio(getReservaLegal())){
				desejaCadPrad = getReservaLegal().getIndDesejaCadPrad();
			}
			
			if(Util.isNullOuVazio(desejaCadPrad)) {
				if(isEstadoConversacaoDegradado(tipoEstadoConservacaoInicial) && !Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao())){
					return super.getMostraCronograma(getReservaLegal().getIdeTipoEstadoConservacao());
				}else {
					return false;
				}			
			}else {			
				return desejaCadPrad;
			}			
		} else {
			return super.getMostraCronograma(getReservaLegal().getIdeTipoEstadoConservacao()); 
		}
	}
	
// Fim Reserva Legal Averbada
	
	public ArrayList<DocumentoImovelRural> getListaPradRl() {
		ArrayList<DocumentoImovelRural> listPrad = new ArrayList<DocumentoImovelRural>();
		if (!Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao().getIdeDocumentoObrigatorio()) && !Util.isNullOuVazio(getReservaLegal().getCronogramaRecuperacao().getIdeDocumentoObrigatorio().getDscCaminhoArquivo()))
			listPrad.add(getReservaLegal().getCronogramaRecuperacao().getIdeDocumentoObrigatorio());
		return listPrad;			
	}
	
	public ReservaLegal getReservaLegalSelecionada() {
		return reservaLegalSelecionada;
	}

	public void setReservaLegalSelecionada(ReservaLegal reservaLegalSelecionada) {
		this.reservaLegalSelecionada = reservaLegalSelecionada;
	}
	
	public Boolean getIndAprovacao() {
		return indAprovacao;
	}

	public void setIndAprovacao(Boolean indAprovacao) {
		this.indAprovacao = indAprovacao;
	}	
	
	public List<DocumentoImovelRural> getListaArquivosReserva() {
		return listaArquivosReserva;
	}

	public void setListaArquivosReserva(List<DocumentoImovelRural> listaArquivosReserva) {
		this.listaArquivosReserva = listaArquivosReserva;
	}
	
	public String getPerguntaValidacao(){
		String pergunta = "";
		if(!Util.isNullOuVazio(reservaLegalSelecionada)){
			if(reservaLegalSelecionada.getIdeStatus().getIdeStatusReservaLegal().equals(4) || reservaLegalSelecionada.getIdeStatus().getIdeStatusReservaLegal().equals(6)){
				pergunta = "O documento que comprova a averbação da reserva legal é válido?";
			} else if(reservaLegalSelecionada.getIdeStatus().getIdeStatusReservaLegal().equals(8) || reservaLegalSelecionada.getIdeStatus().getIdeStatusReservaLegal().equals(9)){
				pergunta = "O documento que comprova a aprovação da reserva legal é válido?";
			}
		}
		return pergunta;		
	}

	public Integer getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(Integer ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}
	
	public boolean isAverbacao(){
		boolean isAverbacao = false;
		if(!Util.isNullOuVazio(reservaLegalSelecionada) && reservaLegalSelecionada.getIdeStatus().getIdeStatusReservaLegal().equals(4)){
				isAverbacao = true;
		}
		return isAverbacao;
	}

	public boolean getIndAverbadaDesabilitada() {
		return indAverbadaDesabilitada;
	}

	public void setIndAverbadaDesabilitada(boolean indAverbadaDesabilitada) {
		this.indAverbadaDesabilitada = indAverbadaDesabilitada;
	}

	public Boolean getIndAprovadaDesabilitada() {
		return indAprovadaDesabilitada;
	}

	public void setIndAprovadaDesabilitada(Boolean indAprovadaDesabilitada) {
		this.indAprovadaDesabilitada = indAprovadaDesabilitada;
	}
	
	public boolean isRlCdaOuBndes() {
		return (super.imovelRural.isImovelBNDES() || super.imovelRural.isImovelCDA());
	}
	
	public List<ClassificacaoSecaoGeometrica> getListaSecaoGeometricaRl() {
		List<ClassificacaoSecaoGeometrica> listaSecaoGeometricaRl = new ArrayList<ClassificacaoSecaoGeometrica>();
		try {		
			listaSecaoGeometricaRl = imovelRuralServiceFacade.listarClassificacaoSecaoGeometrica();
			// removendo a opção de ponto
			listaSecaoGeometricaRl.remove(0);
			if (!Util.isNullOuVazio(super.imovelRural) && !super.imovelRural.isMenorQueQuatroModulosFiscais()) {
				// removendo a opção de desenho
				listaSecaoGeometricaRl.remove(1);
			}			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return listaSecaoGeometricaRl;
	}
	
	public boolean isExibePerguntasRLAprovada() {
		if (Util.isNullOuVazio(imovelRural)) {
			return false;
		}
		if (Util.isNullOuVazio(imovelRural.getIdeImovelRural())) {
			return false;
		}
		if (!Util.isNullOuVazio(imovelRural.getIndReservaLegal()) && imovelRural.getIndReservaLegal()) {
			if (!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeStatus())
					&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeStatus().getdscStatus())
					&& imovelRural.getReservaLegal().getIdeStatus().getdscStatus().equals("Aprovada")
					&& Util.isNullOuVazio(imovelRural.getReservaLegal().getDtcAprovacaoDeclarada())) {
				return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * 
	 * Método que verifica se o {@link TipoEstadoConservacao} RECUPERADA pode ser exibida para a {@link ReservaLegal} ou {@link App}.
	 *
	 * @return <code>false - os imóveis rurais que tiverem o PRA de RL ou de APP e que tiverem com a(s) atividade(s) do cronograma de recuperação concluída(s) (100%)</code> 
	 *
	 * @author eduardo.fernandes
	 * @since 26/09/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/7823">#7823</a> 
	 */
	protected boolean naoExibeEstadoConservacaoRecuperada() {
		return super.naoExibeEstadoConservacaoRecuperada(getReservaLegal().getCronogramaRecuperacao());
	}
	
	@Override
	public List<TipoEstadoConservacao> getListaTipoEstadoConservacao() {
		if(!Util.isNull(super.getListaTipoEstadoConservacao())){
			TipoEstadoConservacao recuperada = super.obterEstadoConservacaoRecuperada();
			if(!Util.isNull(getReservaLegal()) && naoExibeEstadoConservacaoRecuperada(getReservaLegal().getCronogramaRecuperacao())){
				super.listaTipoEstadoConservacao.remove(recuperada);
			} 
			else {
				if(!super.getListaTipoEstadoConservacao().contains(recuperada)){
					super.listaTipoEstadoConservacao.add(recuperada);
				}
			}
		}
		return super.getListaTipoEstadoConservacao();
	}
	
	public String getCaminhoGeoBahia() {
		return URLEnum.CAMINHO_GEOBAHIA_CEFIR.toString();
	}
	
	public String getCaminhoNovoGeoBahia() {
		return URLEnum.CAMINHO_NOVO_GEOBAHIA.toString();
	}

	public boolean isRenderedBotaoSalvar() {
		return renderedBotaoSalvar;
	}

	public void setRenderedBotaoSalvar(boolean renderedBotaoSalvar) {
		this.renderedBotaoSalvar = renderedBotaoSalvar;
	}
}
