package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.EnvioDocumentoController;
import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.AtoDeclaratorioUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnvioDocumentoServiceFacade {
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;

	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnvioDocumentoController ctrl) throws Exception {
		if(isRequerimentoFoiTramitadoPorOutroUsuario(ctrl)) {
			JsfUtil.addErrorMessage(Util.getString("requerimento_msg_ja_tramitado", ctrl.gerarNomeOperacaoTela()));
		}
		else {
			boolean todosArquivosEnviados = todosArquivosEnviados(ctrl);				
			atualizarDocumentoObrigatorioRequerimento(ctrl);				
			tramitar(ctrl, todosArquivosEnviados);
			if(!todosArquivosEnviados) {
				JsfUtil.addSuccessMessage(Util.getString("documento_obrigatorio_msg_envio_pendencia"));
			} 
			else {
				JsfUtil.addSuccessMessage(Util.getString("documento_obrigatorio_msg_envio_documentacao_efetuado_sucesso"));
			}
		}
	}
	
	private Pessoa getOperador() {
		return new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isRequerimentoFoiTramitadoPorOutroUsuario(EnvioDocumentoController ctrl) throws Exception {
		if (ctrl.isReenquadramento()){
			return !tramitacaoReenquadramentoProcessoServiceFacade.isTamitacaoRequerimentoAtual(ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getIdeProcessoReenquadramento(), ctrl.getTramitacaoReenquadramentoProcesso());
		}
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(ctrl.getRequerimento().getIdeRequerimento(), ctrl.getTramitacaoRequerimento());
	}
	
	private boolean todosArquivosEnviados(EnvioDocumentoController ctrl) {
		boolean todosArquivosEnviados = true;
		
		if(!Util.isNull(ctrl.getFormularios())){
			for(DocumentoObrigatorio documentoObrigatorio : ctrl.getFormularios()){
				if(ctrl.isFormularioDigital(documentoObrigatorio) && todosArquivosEnviados && documentoObrigatorioService.isAtivo(documentoObrigatorio)){
					if(FceUtil.isFce(documentoObrigatorio)){
						todosArquivosEnviados = ctrl.existeFcePreenchido(documentoObrigatorio);
					} 
					else if(AtoDeclaratorioUtil.isFormularioAtoDeclaratorio(documentoObrigatorio)){
						todosArquivosEnviados = ctrl.existeAtoDeclaratorioPreenchido(documentoObrigatorio);
					}
				}
			}
		}

		if(!ctrl.isRequerimentoAntigoEnquadradoNaV1()) {
			for (DocumentoObrigatorioEnquadramentoDTO docObrigEnq : ctrl.getListaDocumentoObrigatorioEnquadramento()) {
				for (DocumentoObrigatorioInterface documentoObrigatorioRequerimento : docObrigEnq.getListaDocumentoObrigatorio()) {
					if (Util.isNullOuVazio(documentoObrigatorioRequerimento.getDscCaminhoArquivo())) {
						return false;
					}
				}
			}
		} else {
			for (DocumentoObrigatorioInterface docObrigReq : ctrl.getListaDocumentoObrigatorioEnquadramentoAntigo()) {
				if (Util.isNullOuVazio(docObrigReq.getDscCaminhoArquivo())) {
					return false;
				}
			}
		}

		if (!Util.isNullOuVazio(ctrl.getDocumentosIdentificacao())) {
			for (DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento : ctrl.getDocumentosIdentificacao()) {
				if (Util.isNullOuVazio(documentoIdentificacaoRequerimento.getIdeDocumentoIdentificacao().getDscCaminhoArquivo())) {
					return false;
				}
			}
		}
		
		return todosArquivosEnviados;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitar(EnvioDocumentoController ctrl, boolean todosArquivosEnviados) throws Exception {
		
		if (ctrl.isReenquadramento()){
			tramitarTramitacaoReenquadramentoProcesso(ctrl, todosArquivosEnviados);
		} else {
			tramitarTramitacaoRequerimento(ctrl, todosArquivosEnviados);
		}
		
		if(isRequerimentoFoiTramitadoPorOutroUsuario(ctrl)) {
			throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado", ctrl.gerarNomeOperacaoTela()));
		}
		
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarTramitacaoReenquadramentoProcesso(EnvioDocumentoController ctrl, boolean todosArquivosEnviados) throws Exception{
		if(todosArquivosEnviados) {
			tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcessoSemFlush(ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento(), new StatusReenquadramento(StatusReenquadramentoEnum.EM_VALIDACAO_PREVIA), getOperador());
		} else {
			tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcessoSemFlush(ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento(), new StatusReenquadramento(StatusReenquadramentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO), getOperador());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarTramitacaoRequerimento(EnvioDocumentoController ctrl, boolean todosArquivosEnviados) throws Exception{
		StatusRequerimento statusRequerimento = statusRequerimentoService.getMaxStatusTramitacaoRequerimantoPorData(ctrl.getRequerimento());
		
		if(todosArquivosEnviados) {
			tramitacaoRequerimentoService.criarTramitacaoRequerimentoSemFlush(ctrl.getRequerimento(), new StatusRequerimento(StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus()), getOperador());
		} else if(statusRequerimento.isEnquadrado()){
			tramitacaoRequerimentoService.criarTramitacaoRequerimentoSemFlush(ctrl.getRequerimento(), new StatusRequerimento(StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus()), getOperador());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarDocumentoObrigatorioRequerimento(EnvioDocumentoController ctrl) throws Exception {
		if(!ctrl.isRequerimentoAntigoEnquadradoNaV1()) {
			for (DocumentoObrigatorioEnquadramentoDTO docObrigEnq : ctrl.getListaDocumentoObrigatorioEnquadramento()) {
				documentoObrigatorioRequerimentoService.atualizar(docObrigEnq.getListaDocumentoObrigatorio());
			}
		} else {
			documentoObrigatorioRequerimentoService.atualizar(ctrl.getListaDocumentoObrigatorioEnquadramentoAntigo());
		}
	}
	
}