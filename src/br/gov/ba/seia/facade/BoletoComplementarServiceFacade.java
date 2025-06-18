package br.gov.ba.seia.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.BoletoComplementarController;
import br.gov.ba.seia.dto.EnviarComprovantePagamentoDTO;
import br.gov.ba.seia.entity.BoletoDaeHistorico;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.MotivoIsencaoBoleto;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.StatusBoletoPagamento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.service.BoletoComplementarService;
import br.gov.ba.seia.service.BoletoDaeHistoricoService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoHistoricoService;
import br.gov.ba.seia.service.BoletoService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.MotivoIsencaoBoletoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoComplementarServiceFacade {

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
	@EJB
	private BoletoComplementarService boletoComplementarService;
	@EJB
	private MotivoIsencaoBoletoService motivoIsencaoBoletoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean salvarComprovantes(EnviarComprovantePagamentoDTO ctrl) throws Exception {

		isTodosComprovantesEnviados(ctrl);

		if (!Util.isNull(ctrl.getBoleto())/* && !Util.isNull(ctrl.getBoleto().getPathComprovante())*/) {
			if (Util.isNull(ctrl.getComprovanteBoleto())) {
				ctrl.setComprovanteBoleto(gerarComprovantePagamentoBoleto(ctrl.getBoleto()));
			} else {
				Pessoa pessoaUpload = new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
				ctrl.getComprovanteBoleto().setDscCaminhoArquivo(ctrl.getBoleto().getPathComprovante());
				ctrl.getComprovanteBoleto().setIdeBoletoPagamentoRequerimento(ctrl.getBoleto());
				ctrl.getComprovanteBoleto().setIdePessoaUpload(pessoaUpload);
			}
			
			if (!ctrl.getComprovanteBoleto().getIndComprovanteValidado()){
				comprovantePagamentoService.salvarAtualizar(ctrl.getComprovanteBoleto());
				salvarHistoricoPagamento(ctrl);
			}
		}
		
		Boolean comprovanteDae = Boolean.FALSE;
		if (!Util.isNull(ctrl.getCertificado())/* && !Util.isNull(ctrl.getCertificado().getPathComprovante())*/) {
			if (Util.isNull(ctrl.getComprovanteCertificado())) {
				ctrl.setComprovanteCertificado(gerarComprovanteDAE(ctrl.getCertificado()));
			} else {
				ctrl.getComprovanteCertificado().setDscCaminhoArquivo(ctrl.getCertificado().getPathComprovante());
			}
			
			if (!ctrl.getComprovanteCertificado().getIndComprovanteValidado() && !Util.isNull(ctrl.getComprovanteCertificado().getDscCaminhoArquivo())){
				comprovantePagamentoDaeService.salvarOuAtualizar(ctrl.getComprovanteCertificado());
				comprovanteDae = Boolean.TRUE;
			}
		}

		if (!Util.isNull(ctrl.getVistoria())/* && !Util.isNull(ctrl.getVistoria().getPathComprovante())*/) {
			if (Util.isNull(ctrl.getComprovanteVistoria())) {
				ctrl.setComprovanteVistoria(gerarComprovanteDAE(ctrl.getVistoria()));
			} else {
				ctrl.getComprovanteVistoria().setDscCaminhoArquivo(ctrl.getVistoria().getPathComprovante());
			}
			
			if (!ctrl.getComprovanteVistoria().getIndComprovanteValidado() && !Util.isNull(ctrl.getComprovanteVistoria().getDscCaminhoArquivo())){
				comprovantePagamentoDaeService.salvarOuAtualizar(ctrl.getComprovanteVistoria());
				comprovanteDae = Boolean.TRUE;
			}
		}
		
		if (comprovanteDae){
			salvarHistoricoDae(ctrl);
		}
		
		return true;
	}
	
	public ComprovantePagamento gerarComprovantePagamentoBoleto(BoletoPagamentoRequerimento boletoPagamentoRequerimento) throws Exception {
		ComprovantePagamento comprovantePagamento = new ComprovantePagamento();
		comprovantePagamento.setDtcValidacao(new Date());
		comprovantePagamento.setIdePessoaUpload(this.getOperador());
		comprovantePagamento.setIdeBoletoPagamentoRequerimento(boletoPagamentoRequerimento);
		comprovantePagamento.setDscCaminhoArquivo(boletoPagamentoRequerimento.getPathComprovante());
		comprovantePagamento.setIndComprovanteValidado(false);
		return comprovantePagamento;
	}
	
	private void isTodosComprovantesEnviados(EnviarComprovantePagamentoDTO ctrl) throws SeiaValidacaoRuntimeException {
		if (!Util.isNullOuVazio(ctrl.getBoletoComplementarDTO().getValor()) 
			&& ctrl.getBoletoComplementarDTO().getIndBoletoRegistrado() != null && !ctrl.getBoletoComplementarDTO().getIndBoletoRegistrado()){
				if (Util.isNull(ctrl.getBoleto())) {
					throw new SeiaValidacaoRuntimeException("O comprovante do boleto é obrigatório.");
				} else {
					if (Util.isNull(ctrl.getBoleto().getPathComprovante())){
						throw new SeiaValidacaoRuntimeException("O comprovante do boleto é obrigatório.");
					}
				}
		}
		
		if (!Util.isNullOuVazio(ctrl.getBoletoComplementarDTO().getVlrTotalCertificado())){
			if (Util.isNull(ctrl.getCertificado())){
				throw new SeiaValidacaoRuntimeException("O comprovante do DAE de Certificado é obrigatório.");
			} else {
				if (Util.isNull(ctrl.getCertificado().getPathComprovante())){
					throw new SeiaValidacaoRuntimeException("O comprovante do DAE de Certificado é obrigatório.");
				 }
			}
		}
		
		if (!Util.isNullOuVazio(ctrl.getBoletoComplementarDTO().getVlrTotalVistoria())){
			if (Util.isNull(ctrl.getVistoria())) {
				throw new SeiaValidacaoRuntimeException("O comprovante do DAE de Vistoria é obrigatório.");
			} else {
				 if (Util.isNull(ctrl.getVistoria().getPathComprovante())){
					 throw new SeiaValidacaoRuntimeException("O comprovante do DAE de Vistoria é obrigatório.");
				 }
			}
		}
	}
	
	private Pessoa getOperador() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}

	private ComprovantePagamentoDae gerarComprovanteDAE(BoletoDaeRequerimento boletoDaeRequerimento) throws Exception {
		ComprovantePagamentoDae comprovantePagamentoDae = new ComprovantePagamentoDae();
		comprovantePagamentoDae.setIdeBoletoDaeRequerimento(boletoDaeRequerimento);
		comprovantePagamentoDae.setDscCaminhoArquivo(boletoDaeRequerimento.getPathComprovante());
		comprovantePagamentoDae.setIdePessoaUpload(getOperador());
		comprovantePagamentoDae.setIndComprovanteValidado(Boolean.FALSE);
		return comprovantePagamentoDae;
	}
	
	private BoletoDaeHistorico gerarHistoricoDAE(EnviarComprovantePagamentoDTO ctrl) throws Exception{
		BoletoDaeHistorico bph = new BoletoDaeHistorico();
		Integer ideBoletoDae =  
				ctrl.getComprovanteCertificado() != null ?  ctrl.getComprovanteCertificado().getIdeBoletoDaeRequerimento().getIdeBoletoDaeRequerimento()
				: ctrl.getComprovanteVistoria().getIdeBoletoDaeRequerimento().getIdeBoletoDaeRequerimento();
		bph.setIdeBoletoDaeRequerimento(new BoletoDaeRequerimento(ideBoletoDae));
		bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.COMPROVANTE.getId()));
		bph.setDtcTramitacao(new Date());
		bph.setIdePessoa(getOperador());
		return bph;
	}
	
	private BoletoPagamentoHistorico gerarHistoricoPagamento(EnviarComprovantePagamentoDTO ctrl) throws Exception{
		BoletoPagamentoHistorico bph = new BoletoPagamentoHistorico();
		bph.setIdeBoletoPagamento(new BoletoPagamentoRequerimento(ctrl.getBoleto().getIdeBoletoPagamentoRequerimento()));
		bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.COMPROVANTE.getId()));
		bph.setDtcTramitacao(new Date());
		bph.setIdePessoa(getOperador());
		return bph;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarHistoricoDae(EnviarComprovantePagamentoDTO ctrl) throws Exception{
		BoletoDaeHistorico boletoDaeHistorico = gerarHistoricoDAE(ctrl);
		boletoDaeHistoricoService.salvar(boletoDaeHistorico);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarHistoricoPagamento(EnviarComprovantePagamentoDTO ctrl) throws Exception{
		BoletoPagamentoHistorico boletoDaeHistorico = gerarHistoricoPagamento(ctrl);
		boletoPagamentoHistoricoService.salvar(boletoDaeHistorico);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MotivoIsencaoBoleto> listarTodosMotivoIsencaoBoletoParaReenquadramento() {
		try {
			return motivoIsencaoBoletoService.listarMotivosInsencaoDoReenquadramentoAtivos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarBoletoComplementar(BoletoComplementarController ctrl) {
		try {
			ctrl.salvar();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
