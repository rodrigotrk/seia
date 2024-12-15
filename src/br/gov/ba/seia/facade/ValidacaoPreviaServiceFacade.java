package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.controller.ValidacaoPreviaController;
import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoRequerimentoEmpreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.service.DocumentoIdentificacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoImovelRuralRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoRepresentacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoRequerimentoEmpreendimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.facade.ComunicacaoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ValidacaoPreviaServiceFacade {
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private DocumentoRepresentacaoRequerimentoService documentoRepresentacaoRequerimentoService;
	@EJB
	private DocumentoIdentificacaoRequerimentoService documentoIdentificacaoRequerimentoService;
	@EJB
	private DocumentoImovelRuralRequerimentoService documentoImovelRuralRequerimentoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	@EJB
	private DocumentoRequerimentoEmpreendimentoService documentoRequerimentoEmpreendimentoService;
	@EJB
	private ComunicacaoServiceFacade comunicacaoServiceFacade;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarValidacaoPrevia(ValidacaoPreviaController ctrl) throws Exception {
		
		Pessoa operador = getPessoaLogada();
		String msgSucesso = "";
		
		if (ctrl.isReenquadramento()){
			msgSucesso = tramitarTramitacaoReenquadramentoProcesso(ctrl, operador);
		} else {
			msgSucesso = tramitarTramitacaoRequerimento(ctrl, operador);
			requerimentoService.atualizarIndEmergencia(ctrl.getRequerimento());
			if(!ctrl.isCRF()){
				requerimentoService.atualizarArea(ctrl.getRequerimento());
			}
		}

		atualizarStatusArquivos(ctrl);
		
		RequestContext.getCurrentInstance().execute("dialogEfetuarValidacaoPrevia.hide()");
		
		JsfUtil.addSuccessMessage(msgSucesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private String tramitarTramitacaoReenquadramentoProcesso(ValidacaoPreviaController ctrl, Pessoa operador){
		
		if(ctrl.arquivosValidados()) {
			tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcessoSemFlush(ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento(), new StatusReenquadramento(StatusReenquadramentoEnum.VALIDADO), getOperador());
			ctrl.gerarEmailDocumentoValidado();
			RequestContext.getCurrentInstance().execute("confirmarEnvioEmailDocumentoValidado.show()");
			return Util.getString("enquadramento_msg_sucesso_validacao");
		} else {
			tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcessoSemFlush(ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento(), new StatusReenquadramento(StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_DOCUMENTO), getOperador());
			ctrl.gerarEmailPadraoPendenciaValidacao();
	
			RequestContext.getCurrentInstance().execute("confirmarEnvioEmailPendenciaValidacao.show()");
			return Util.getString("enquadramento_msg_sucesso_pendencia_validacao");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private String tramitarTramitacaoRequerimento(ValidacaoPreviaController ctrl, Pessoa operador) {
		if (ctrl.arquivosValidados()) {
			tramitacaoRequerimentoService.tramitarSemFlush(ctrl.getRequerimento(), StatusRequerimentoEnum.VALIDADO, operador);
			if(isRequerimentoFoiTramitado(ctrl)) {
				throw new SeiaRuntimeException(Util.getString("requerimento_msg_ja_tramitado",ctrl.getRequerimento().getNumRequerimento()));
			}
			return Util.getString("enquadramento_msg_sucesso_validacao");
			
		} else {
			tramitacaoRequerimentoService.tramitarSemFlush(ctrl.getRequerimento(), StatusRequerimentoEnum.PENDENCIA_VALIDACAO, operador);
			if(isRequerimentoFoiTramitado(ctrl)) {
				throw new SeiaRuntimeException(Util.getString("requerimento_msg_ja_tramitado",ctrl.getRequerimento().getNumRequerimento()));
			}
			
			RequestContext.getCurrentInstance().execute("confirmarEnvioEmailPendenciaValidacao.show()");
			return Util.getString("enquadramento_msg_sucesso_pendencia_validacao");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarProcesso(ValidacaoPreviaController ctrl) {
		try{
			Area area = ctrl.getRequerimento().getIdeArea();
			Pessoa operador = getOperador();
			tramitacaoRequerimentoService.tramitar(ctrl.getRequerimento(), StatusRequerimentoEnum.FORMADO, operador);
			processoService.gerarProcesso(ctrl.getRequerimento(), operador, area.getIdeArea(), false);
		}
		catch(Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Pessoa getOperador(){
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isRequerimentoFoiTramitado(ValidacaoPreviaController ctrl) {
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(ctrl.getRequerimento().getIdeRequerimento(), ctrl.getTramitacaoRequerimento());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarStatusArquivos(ValidacaoPreviaController ctrl) throws Exception {
		
		if (!ctrl.getReqAntigo()) {
			for(DocumentoObrigatorioEnquadramentoDTO documentoObrigatorioEnquadramentoDTO : ctrl.getListaDocumentoObrigatorioEnquadramento()) {
				documentoObrigatorioRequerimentoService.atualizar(documentoObrigatorioEnquadramentoDTO.getListaDocumentoObrigatorio());
			}
		} 
		else {
			this.documentoObrigatorioRequerimentoService.atualizar(ctrl.getListaDocumentoObrigatorioEnquadramentoAntigo());
		}
		
		if (!Util.isNullOuVazio(ctrl.getDocumentosRepresentacao())) {
			documentoRepresentacaoRequerimentoService.salvar(ctrl.getDocumentosRepresentacao());
		}
		
		if (!ctrl.isReenquadramento()){
			documentoIdentificacaoRequerimentoService.salvar(ctrl.getDocumentosIdentificacao());
			documentoImovelRuralRequerimentoService.salvarListaDocumentoImovelRuralRequerimento(ctrl.getDocumentosImovelRuralReq());
		}
		
		if(!Util.isNullOuVazio(ctrl.getListaDocumentoRequerimentoEmpreendimentos())){
			
			for(DocumentoRequerimentoEmpreendimento docReqEmp: ctrl.getListaDocumentoRequerimentoEmpreendimentos()){
				if(!Util.isNullOuVazio(docReqEmp.getIdeDocumentoRequerimentoEmpreendimento())){
					documentoRequerimentoEmpreendimentoService.salvarDocumentoRequerimentoEmpreendimento(docReqEmp);
				}
			}
			documentoRequerimentoEmpreendimentoService.salvarDocumentoRequerimentoEmpreendimentoEmLote(ctrl.getListaDocumentoRequerimentoEmpreendimentos());
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void replicarArquivos(ValidacaoPreviaController ctrl) throws SeiaException {
		try{
			for (DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento : ctrl.getDocumentosIdentificacao()) {
				
				if (!Util.isNullOuVazio(documentoIdentificacaoRequerimento.getDscCaminhoArquivo())
						&& documentoIdentificacaoRequerimento.getDscCaminhoArquivo().contains(DiretorioArquivoEnum.DOCUMENTO_IDENTIFICACAO_REQUERIMENTO.toString())) {
					break;
				}
				
				String dscCaminhoArquivo = documentoIdentificacaoRequerimento.getIdeDocumentoIdentificacao().getDscCaminhoArquivo();
				String fileName = FileUploadUtil.getFileName(dscCaminhoArquivo);
				String novoCaminho = gerenciaArquivoService.transferirArquivo(dscCaminhoArquivo, fileName,	
						documentoIdentificacaoRequerimento.getIdeDocumentoIdentificacaoRequerimento(),
						DiretorioArquivoEnum.DOCUMENTO_IDENTIFICACAO_REQUERIMENTO.toString());
				
				documentoIdentificacaoRequerimento.setDscCaminhoArquivo(novoCaminho);
				this.documentoIdentificacaoRequerimentoService.salvar(documentoIdentificacaoRequerimento);
			}
			
			if (!Util.isNullOuVazio(ctrl.getDocumentosRepresentacao())) {
				for (DocumentoRepresentacaoRequerimento documentoRepresentacao : ctrl.getDocumentosRepresentacao()) {
					
					if (!Util.isNullOuVazio(documentoRepresentacao.getCaminhoArquivoTransient())
							&& documentoRepresentacao.getCaminhoArquivoTransient().contains(
									DiretorioArquivoEnum.DOCUMENTO_REPRESENTACAO_REQUERIMENTO.toString())) {
						break;
					}
					String dscCaminhoArquivo = documentoRepresentacao.getCaminhoArquivoTransient();
					String fileName = FileUploadUtil.getFileName(dscCaminhoArquivo);
					String novoCaminho = gerenciaArquivoService.transferirArquivo(dscCaminhoArquivo, fileName,
							documentoRepresentacao.getIdeDocumentoRepresentacaoRequerimento(),
							DiretorioArquivoEnum.DOCUMENTO_REPRESENTACAO_REQUERIMENTO.toString());
					
					documentoRepresentacao.setDscCaminhoArquivo(novoCaminho);
					this.documentoRepresentacaoRequerimentoService.salvarOuAtualizar(documentoRepresentacao);
				}
			}			
		}
		catch(Exception e) {
			throw new SeiaException(e);
		}
	}
	
	private Pessoa getPessoaLogada() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}	
}
