package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.primefaces.context.RequestContext;

import br.gov.ba.seia.controller.ComprovanteBoletoController;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.service.BoletoDaeHistoricoService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoHistoricoService;
import br.gov.ba.seia.service.BoletoService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComprovanteBoletoServiceFacade {
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private BoletoService boletoService;
	@EJB
	private BoletoDaeRequerimentoService boletoDaeService;
	@EJB
	private BoletoPagamentoHistoricoService boletoPagamentoHistoricoService;
	@EJB
	private BoletoDaeHistoricoService boletoDaeHistoricoService;
	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComprovantes(ComprovanteBoletoController ctrl) throws Exception  {
		
		if(isRequerimentoFoiTramitadoPorOutroUsuario(ctrl)) {
			JsfUtil.addErrorMessage(Util.getString("requerimento_msg_ja_tramitado"));
			return;
		}

		if (!isTodosComprovantesEnviados(ctrl)) {
			return;
		}
		
		if (!Util.isNull(ctrl.getBoleto()) && !Util.isNull(ctrl.getBoleto().getPathComprovante())) {
			if (Util.isNull(ctrl.getComprovanteBoleto())) {
				ctrl.setComprovanteBoleto(ctrl.gerarComprovantePagamentoBoleto());
			} else {
				Pessoa pessoaUpload = new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
				ctrl.getComprovanteBoleto().setDscCaminhoArquivo(ctrl.getBoleto().getPathComprovante());
				ctrl.getComprovanteBoleto().setIdeBoletoPagamentoRequerimento(ctrl.getBoleto());
				ctrl.getComprovanteBoleto().setIdePessoaUpload(pessoaUpload);
			}
			comprovantePagamentoService.salvarAtualizar(ctrl.getComprovanteBoleto());
		}

		if (!Util.isNull(ctrl.getCertificado()) && !Util.isNull(ctrl.getCertificado().getPathComprovante())) {
			if (Util.isNull(ctrl.getComprovanteCertificado())) {
				ctrl.setComprovanteCertificado(ctrl.gerarComprovanteDAE(ctrl.getCertificado()));
			} else {
				ctrl.getComprovanteCertificado().setDscCaminhoArquivo(ctrl.getCertificado().getPathComprovante());
			}
			comprovantePagamentoDaeService.salvarOuAtualizar(ctrl.getComprovanteCertificado());
		}

		if (!Util.isNull(ctrl.getVistoria()) && !Util.isNull(ctrl.getVistoria().getPathComprovante())) {
			if (Util.isNull(ctrl.getComprovanteVistoria())) {
				ctrl.setComprovanteVistoria(ctrl.gerarComprovanteDAE(ctrl.getVistoria()));
			} else {
				ctrl.getComprovanteVistoria().setDscCaminhoArquivo(ctrl.getVistoria().getPathComprovante());
			}
			comprovantePagamentoDaeService.salvarOuAtualizar(ctrl.getComprovanteVistoria());
		}

		tramitarRequerimento(ctrl);
		
		JsfUtil.addSuccessMessage("Comprovante enviado com sucesso!");
		RequestContext.getCurrentInstance().execute("dialogComprovante.hide()");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarRequerimento(ComprovanteBoletoController ctrl)
			throws Exception {
		tramitacaoRequerimentoService.tramitar(ctrl.getRequerimento(), StatusRequerimentoEnum.COMPROVANTE_ENVIADO, this.getOperador());
		if(isRequerimentoFoiTramitadoPorOutroUsuario(ctrl)) {
			throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado"));
		}
	}
	
	private boolean isTodosComprovantesEnviados(ComprovanteBoletoController ctrl) {

		boolean valido = true;
//		#boleto_registrado: A baixa dos boletos é feita por meio do arquivo de retorno emitido pelo banco.
		if (!Util.isNull(ctrl.getBoleto()) && Util.isNullOuVazio(ctrl.getBoleto().getPathComprovante()) && !ctrl.getBoleto().getIndBoletoRegistrado()) {
			JsfUtil.addErrorMessage("O comprovante do Boleto é obrigatório.");
			valido = false;
		}

		if (!Util.isNull(ctrl.getCertificado()) && Util.isNullOuVazio(ctrl.getCertificado().getPathComprovante())) {
			JsfUtil.addErrorMessage("O comprovante do DAE de Certificado é obrigatório.");
			valido = false;
		}

		if (!Util.isNull(ctrl.getVistoria()) && !ctrl.getVistoria().isIndIsento() && Util.isNullOuVazio(ctrl.getVistoria().getPathComprovante())) {
			JsfUtil.addErrorMessage("O comprovante do DAE de Vistoria é obrigatório.");
			valido = false;
		}

		return valido;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isRequerimentoFoiTramitadoPorOutroUsuario(ComprovanteBoletoController ctrl)  {
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(ctrl.getRequerimento().getIdeRequerimento(), ctrl.getTramitacaoRequerimento());
	}
	
	private Pessoa getOperador()  {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}
}