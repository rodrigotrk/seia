package br.gov.ba.seia.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;

import org.primefaces.context.RequestContext;

import br.gov.ba.seia.controller.ValidacaoBoletoController;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.BoletoService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DadoBancarioService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Dependent 
public class ValidacaoBoletoServiceFacade {

	@EJB
	private BoletoService boletoService;
	
	@EJB
	private CertificadoService certificadoService;

	@EJB
	private BoletoDaeRequerimentoService boletoDaeRequerimentoService;

	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentRequerimentoService;
	
	@EJB
	private DadoBancarioService dadoBancarioService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;

	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;

	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;

	@EJB
	private ProcessoService processoService;

	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	
	@EJB
	private AreaService areaService;

	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private EnquadramentoService enquadramentoService;

	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private PessoaService pessoaService;

	@EJB
	private EmailService emailService;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	
	@EJB
	private ComprovantePagamentoRequerimentoServiceFacade comprovantePagamentoServiceFacade;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ValidacaoBoletoController ctrl) throws Exception {
		
		if(isRequerimentoFoiTramitadoPorOutroUsuario(ctrl)) {
			throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado"));
		}
		
		String msg = null;
		Requerimento requerimento = ctrl.getRequerimento();
		
		if (isTodosValidados(ctrl)) {
			tramitarRequerimento(ctrl,StatusRequerimentoEnum.BOLETO_VALIDADO);
			
			//se for inexigibilidade apenas tramitar para DECLARACAO EMITIDA e não gerar processo
			if(isRequerimentoInexigibilidade(requerimento)) {
				tramitarRequerimento(ctrl,StatusRequerimentoEnum.DECLARACAO_EMITIDA);
			}else{
				requerimentoService.finalizar(requerimento, getOperador(), false);
			}
			
			msg="Validação efetuada com sucesso!";
			
			if(ctrl.isDirucSecundaria()) requerimentoService.duplicarControleTramitacaoParaAreaSecundaria(requerimento, getOperador(), true);
		}
		else {
			tramitarRequerimento(ctrl, StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE);
			this.boletoService.atualizarPendenciaComunicacaoRequerimento(requerimento);
			msg="Pendência de validação foi efetuado com sucesso!";
		}
		
		if (!Util.isNull(requerimento.getIdeArea())) requerimentoService.atualizarArea(requerimento);
		
		this.atualizarComprovantes(ctrl);
		
		if(!Util.isNullOuVazio(msg)) JsfUtil.addSuccessMessage(msg);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarRequerimento(ValidacaoBoletoController ctrl, StatusRequerimentoEnum statusRequerimentoEnum) throws Exception {
		this.tramitacaoRequerimentoService.tramitarSemFlush(ctrl.getRequerimento(), statusRequerimentoEnum, getOperador());
		if(isRequerimentoFoiTramitadoPorOutroUsuario(ctrl)) {
			throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado"));
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isRequerimentoFoiTramitadoPorOutroUsuario(ValidacaoBoletoController ctrl){
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(ctrl.getRequerimento().getIdeRequerimento(), ctrl.getTramitacaoRequerimento());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarComprovantes(ValidacaoBoletoController ctrl)  {
		if (!Util.isNullOuVazio(ctrl.getComprovantesPagamento()))
			this.comprovantePagamentoService.atualizar(ctrl.getComprovantesPagamento());

		if (!Util.isNullOuVazio(ctrl.getComprovantesDAE()))
			this.comprovantePagamentoDaeService.atualizar(ctrl.getComprovantesDAE());
	}
	
	private boolean isTodosValidados(ValidacaoBoletoController ctrl) throws Exception {
		for (ComprovantePagamento comprovantePagamento : ctrl.getComprovantesPagamento()) {
			if (comprovantePagamento.getIndComprovanteValidado()) {
				comprovantePagamento.setIdePessoaValidacao(getOperador());
				comprovantePagamento.setDtcValidacao(new Date());
			} else {
				return false;
			}
		}
		for (ComprovantePagamentoDae comprovantePagamentoDae : ctrl.getComprovantesDAE()) {
			if (comprovantePagamentoDae.getIndComprovanteValidado()) {
				comprovantePagamentoDae.setIdePessoaValidacao(getOperador());
				comprovantePagamentoDae.setDtcValidacao(new Date());
			} else {
				return false;
			}
		}
		if(Util.isNullOuVazio(ctrl.getComprovantesPagamento())){
			for(BoletoPagamentoRequerimento boleto : ctrl.getBoletosPagamentoRequerimento()){
				if(boleto.getIdeLoteRetornoBoleto() == null){
					return false;
				}
			}
		}
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarEmailPendenciaValidacao(ValidacaoBoletoController ctrl)  throws Exception {
		Requerimento requerimento = ctrl.getRequerimento();
		
		ctrl.setTituloEmail("Pendências no comprovante enviado Requerimento: "+requerimento.getNumRequerimento());
		
		if(Util.isNullOuVazio(ctrl.getRequerimento().getRequerimentoPessoaCollection())) {
			requerimento.setRequerimentoPessoaCollection(requerimentoPessoaService.listarEnvolvidosRequerimento(requerimento.getIdeRequerimento()));
		}
		
		salvar(ctrl);
		
		requerimentoService.salvarComunicacao(requerimento, ctrl.getEmail());
		
		emailService.enviarEmailsAoRequerente(requerimento, ctrl.getTituloEmail(), ctrl.getEmail());
		
		RequestContext.getCurrentInstance().execute("dialogValidacaoComprovante.hide()");
		JsfUtil.addSuccessMessage("Email enviado com sucesso.");
	}

	private Pessoa getOperador() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}	
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarEmailRequerimentoUnico(ValidacaoBoletoController ctrl) throws Exception {
		RequerimentoUnico requerimentoUnico = requerimentoUnicoService.buscarRequerimentoUnico(ctrl.getRequerimento().getRequerimentoUnico());
		StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus());
		gerarTramitacaoPendenciaValidacao(requerimentoUnico, status);
		
		comprovantePagamentoServiceFacade.enviarEmailPendenciaValidacao(requerimentoUnico.getRequerimento(), ctrl.getEmailValidacao());
		
		RequestContext.getCurrentInstance().execute("emailValidacao.hide()");
		RequestContext.getCurrentInstance().execute("dialogBoletoValidarComprovante.hide()");
		
		JsfUtil.addSuccessMessage("E-mail de pendência enviado com sucesso.");
		
		ctrl.setEmailValidacao(null);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarTramitacaoPendenciaValidacao(RequerimentoUnico requerimentoUnico, StatusRequerimento status) {
		Pessoa usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
		tramitacaoRequerimentoService.criarTramitacaoRequerimento(requerimentoUnico.getRequerimento(), status, usuarioLogado);
	}

	private boolean isRequerimentoInexigibilidade(Requerimento requerimento)  {
		boolean isInexigibilidade = false;
		
		List<EnquadramentoAtoAmbiental> lista = enquadramentoService.buscarEnquadramentoAtoAmbientalByRequerimento(requerimento.getIdeRequerimento());
		
		if(!Util.isNullOuVazio(lista)) {
			for(EnquadramentoAtoAmbiental ato : lista) {
				if(ato.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.INEXIGIBILIDADE.getId())) {
					isInexigibilidade = true;
				}
			}
		}
			
		return isInexigibilidade;
	}
}