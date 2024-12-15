package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.controller.BoletoComplementarController;
import br.gov.ba.seia.dao.BoletoComplementarDAO;
import br.gov.ba.seia.dao.ComprovantePagamentoDaeImpl;
import br.gov.ba.seia.dao.DAEComplementarDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.dto.BoletoComplementarDTO;
import br.gov.ba.seia.dto.BoletoComplementarFilter;
import br.gov.ba.seia.entity.BoletoDaeHistorico;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.MotivoCancelamentoBoleto;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusBoletoPagamento;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.facade.BoletoComplementarServiceFacade;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Classe de negocio do boleto complementar
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 18/11/2013
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoComplementarService {

	@Inject
	BoletoComplementarDAO boletoComplementarDAO;
	
	@Inject
	private IDAO<MotivoCancelamentoBoleto> motivoCancelamentoBoletoDAO;

	@Inject
	private DAEComplementarDAOImpl daeComplementarDAO;
	
	@Inject
	private ComprovantePagamentoDaeImpl comprovantePagamentoDaeImpl;
	
	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;
	
	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
			
	@EJB
	private BoletoDaeRequerimentoService boletoDaeRequerimentoService;
	
	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;
	
	@EJB
	private BoletoPagamentoHistoricoService boletoPagamentoHistoricoService;
	
	@EJB
	private BoletoDaeHistoricoService boletoDaeHistoricoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoservice;
	
	@EJB
	private BoletoComplementarService boletoComplementarServicee;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private BoletoComplementarServiceFacade boletoComplementarServiceFacade;
	
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	
	@EJB 
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	/**
	 * Consulta que retorna os boletos cadastradas com seus respectivos historicos de datas, status e valores.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param filter - Filtro da tela de consulta de boleto complementar
	 * @param first - Usado pela paginacao para definir a pagina atual
	 * @param pageSize - Usado pela paginacao para definir a quantida maxima de registros por pagina
	 * @param usuarioExterno - Modifica o filtro de requerente na query quando o usuario e externo
	 * @return List de array de {@link Object} que deve ser convertida em uma List<{@link BoletoComplementarDTO}>
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoComplementarDTO> consultarBoletoComplementar(BoletoComplementarFilter filter, int first, int pageSize, Boolean usuarioExterno) {

		List<Object[]> listaBPR = boletoComplementarDAO.consultarBoletoComplementar(filter, first, pageSize, usuarioExterno);
		
		List<BoletoComplementarDTO> boletos = new ArrayList<BoletoComplementarDTO>();
		
		for (Object[] obj : listaBPR) {
			
			boletos.add(new BoletoComplementarDTO(obj));
		}

		return boletos;
	}
	
	/**
	 * Consulta que informa a quantidade total de registros retornados pela query do metodo {@link BoletoComplementarDAO#consultarBoletoComplementar(BoletoComplementarFilter, int, int)}
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param filter - Filtro da tela de consulta de boleto complementar
	 * @param usuarioExterno - Modifica o filtro de requerente na query quando o usuario e externo
	 * @return Quantidade total de boletos para aquela consulta 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer consultarBoletoComplementarCount(BoletoComplementarFilter filter, Boolean usuarioExterno) {
		return boletoComplementarDAO.consultarBoletoComplementarCount(filter, usuarioExterno);
	}
	
	/**
	 *
	 * @author micael.coutinho
	 * @return A lista de todos os motivos, ativos, de cancelamento de boletos 
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MotivoCancelamentoBoleto> listarMotivosCancelamentoBoleto() {
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoCancelamentoBoleto.class, "boleto").add(Restrictions.eq("indAtivo", true));
		return motivoCancelamentoBoletoDAO.listarPorCriteria(criteria, Order.asc("nomMotivoCancelamentoBoleto"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoComplementarDTO consultarDAEPorProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		return daeComplementarDAO.consultarDAEPorProcessoReenquadramento(ideProcessoReenquadramento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComprovantePagamentoDae preencherCaminhoArquivo(BoletoDaeRequerimento certificado, Boolean vistoria){
		List<ComprovantePagamentoDae> listaComprovantePagamentoDae = comprovantePagamentoDaeImpl.consultarPorIdBoletoDaeRequerimento(certificado.getIdeBoletoDaeRequerimento());
		
		if (!Util.isNullOuVazio(listaComprovantePagamentoDae)){
			certificado.setPathComprovante(null);
			
			for (ComprovantePagamentoDae comprovantePagamentoDae : listaComprovantePagamentoDae) {
				
				if (comprovantePagamentoDae.getDscCaminhoArquivo().indexOf("vistoria") != -1 && vistoria){
					certificado.setPathComprovante(comprovantePagamentoDae.getDscCaminhoArquivo());
					certificado.setIndComprovanteValidado(comprovantePagamentoDae.getIndComprovanteValidado());
					return comprovantePagamentoDae;
				}
				
				if (comprovantePagamentoDae.getDscCaminhoArquivo().indexOf("certificado") != -1 && !vistoria){
					certificado.setPathComprovante(comprovantePagamentoDae.getDscCaminhoArquivo());
					certificado.setIndComprovanteValidado(comprovantePagamentoDae.getIndComprovanteValidado());
					return comprovantePagamentoDae;
				}
			}
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoPagamentoRequerimento carragerBoleto(BoletoComplementarController ctrl, Integer ideProcessoReenquadramento) {
		BoletoPagamentoRequerimento boletoPagamentoRequerimento = boletoPagamentoRequerimentoService.consultarPorIdeProcessoReenquadramento(ideProcessoReenquadramento);
		
		if (!Util.isNullOuVazio(boletoPagamentoRequerimento)){
			ComprovantePagamento comprovantePagamento = comprovantePagamentoService.consultarPorIdBoletoPagamentoRequerimento(boletoPagamentoRequerimento.getIdeBoletoPagamentoRequerimento());
			if (!Util.isNullOuVazio(comprovantePagamento)){
				boletoPagamentoRequerimento.setPathComprovante(comprovantePagamento.getDscCaminhoArquivo());
				boletoPagamentoRequerimento.setIndComprovanteValidado(comprovantePagamento.getIndComprovanteValidado());
				ctrl.setComprovanteBoleto(comprovantePagamento);
				return boletoPagamentoRequerimento;
			}
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoDaeRequerimento carregarCertificadoDae(BoletoComplementarController ctrl, Integer ideProcessoReenquadramento) {
		BoletoDaeRequerimento boletoDaeRequerimento = boletoDaeRequerimentoService.carregarCertificadoByProcessoRequerimento(ideProcessoReenquadramento);
		
		if (!Util.isNullOuVazio(boletoDaeRequerimento)){
			ctrl.setComprovanteCertificado(preencherCaminhoArquivo(boletoDaeRequerimento, false));
			return boletoDaeRequerimento;
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoDaeRequerimento carregarVistoriaDae(BoletoComplementarController ctrl, Integer ideProcessoReenquadramento) {
		BoletoDaeRequerimento boletoDaeRequerimento = boletoDaeRequerimentoService.carregarCertificadoByProcessoRequerimento(ideProcessoReenquadramento);
		
		if (!Util.isNullOuVazio(boletoDaeRequerimento)){
			ctrl.setComprovanteVistoria(preencherCaminhoArquivo(boletoDaeRequerimento, true));
			
			return boletoDaeRequerimento;
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarEnviarComprovanteBoleto(BoletoComplementarController ctrl) {
		
		Integer ideProcessoReenquadramento = ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getIdeProcessoReenquadramento();
		ctrl.getEnviarComprovantePagamentoDTO().setBoleto(carragerBoleto(ctrl, ideProcessoReenquadramento));
		ctrl.getEnviarComprovantePagamentoDTO().setCertificado(carregarCertificadoDae(ctrl, ideProcessoReenquadramento));
		ctrl.getEnviarComprovantePagamentoDTO().setVistoria(carregarVistoriaDae(ctrl, ideProcessoReenquadramento));
		
		ctrl.getFilter().setIdeProcessoReenquadramento(ideProcessoReenquadramento);
		List<BoletoComplementarDTO> listaBoletoComplementarReequerimento = this.consultarBoletoComplementar(ctrl.getFilter(), 0, 2, ctrl.isUsuarioExterno());
		if (!listaBoletoComplementarReequerimento.isEmpty()){
			ctrl.setEnviarComprovante_boleto(listaBoletoComplementarReequerimento.get(0));
			
			for (BoletoComplementarDTO boletoComplementarDTO : listaBoletoComplementarReequerimento) {
				if (!Util.isNullOuVazio(boletoComplementarDTO.getValor())){
					ctrl.setBoletoReequerimento(boletoComplementarDTO);
					ctrl.getEnviarComprovante_boleto().setNumBoleto(boletoComplementarDTO.getNumBoleto());
					ctrl.getEnviarComprovante_boleto().setValor(boletoComplementarDTO.getValor());
				}
				
				if (!Util.isNullOuVazio(boletoComplementarDTO.getVlrTotalCertificado())){
					ctrl.setDaeReequerimento(boletoComplementarDTO);
					ctrl.getEnviarComprovante_boleto().setVlrTotalCertificado(boletoComplementarDTO.getVlrTotalCertificado());
				}
				
				if (!Util.isNullOuVazio(boletoComplementarDTO.getVlrTotalVistoria())){
					ctrl.setDaeReequerimento(boletoComplementarDTO);
					ctrl.getEnviarComprovante_boleto().setVlrTotalVistoria(boletoComplementarDTO.getVlrTotalVistoria());
				}
			}
		}
	}
	
	private void adicionarFiltroPorStatus(BoletoComplementarFilter filter, String status) {
		if (StatusBoletoEnum.COMPROVANTE.getValue().equals(status)){
			filter.setComprovante(Boolean.TRUE);
			filter.setPendenciaValidacaoComprovante(Boolean.TRUE);
			filter.setPago(Boolean.TRUE);
		} else if (StatusBoletoEnum.PENDENCIA_VALIDACAO.getValue().equals(status)){
			filter.setPendenciaValidacaoComprovante(Boolean.TRUE);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void validaComprovantePagamento(BoletoComplementarController ctrl) throws Exception{
		
		if (!Util.isNullOuVazio(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento())){
			
			Integer ideProcesso = ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getIdeProcesso().getIdeProcesso();
			
			adicionarFiltroPorStatus(ctrl.getFilter(), ctrl.getValidarComprovante_boletoComplementar().getStatus());
			
			ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento());
			Boolean primeiraValidacao = ctrl.mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum.BOLETO_VALIDADO, false, true);
			
			if (!primeiraValidacao){
				ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento());
				ctrl.mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum.REENQUADRADO, false, true);
				ctrl.tramitarProcesso(ideProcesso, StatusFluxoEnum.REENQUADRADO_AGUARDANDO_DISTRIBUICAO.getStatus());
			}
		}
		
		BoletoPagamentoHistorico bph = new BoletoPagamentoHistorico();
		bph.setIdeBoletoPagamento(new BoletoPagamentoRequerimento(ctrl.getValidarComprovante_boletoComplementar().getId()));
		bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.PAGO.getId()));
		bph.setDtcTramitacao(new Date());
		bph.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());

		boletoPagamentoHistoricoService.salvar(bph);
		
		ComprovantePagamento comprovantePagamento = comprovantePagamentoService.consultarPorIdBoletoPagamentoRequerimento(ctrl.getValidarComprovante_boletoComplementar().getId());
		
		if (!Util.isNullOuVazio(comprovantePagamento)){
			comprovantePagamento.setIndComprovanteValidado(Boolean.TRUE);
			comprovantePagamento.setDtcValidacao(new Date());
			comprovantePagamentoService.salvarAtualizar(comprovantePagamento);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void validaComprovantePagamentoDae(BoletoComplementarController ctrl) throws Exception{
		
		if (!Util.isNullOuVazio(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento())){
			adicionarFiltroPorStatus(ctrl.getFilter(), ctrl.getValidarComprovante_boletoComplementar().getStatus());
			ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento());
			if(isTodosBoletosPagos(ctrl.getValidarComprovante_boletoComplementar().getProcesso().getIdeProcesso())){
				ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento());
				ctrl.mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum.REENQUADRADO, false, true);
				ctrl.tramitarProcesso(ctrl.getValidarComprovante_boletoComplementar().getProcesso().getIdeProcesso(), StatusFluxoEnum.REENQUADRADO_AGUARDANDO_DISTRIBUICAO.getStatus());
			}
		}
	
		BoletoDaeHistorico bph = new BoletoDaeHistorico();
		bph.setIdeBoletoDaeRequerimento(new BoletoDaeRequerimento(ctrl.getValidarComprovante_boletoComplementar().getId()));
		bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.PAGO.getId()));
		bph.setDtcTramitacao(new Date());
		bph.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());

		boletoDaeHistoricoService.salvar(bph);
		
		List<ComprovantePagamentoDae> listaComprovantePagamentoDae = comprovantePagamentoDaeImpl.consultarPorIdBoletoDaeRequerimento(ctrl.getValidarComprovante_boletoComplementar().getId());
		
		if (!Util.isNullOuVazio(listaComprovantePagamentoDae)){
			for (ComprovantePagamentoDae comprovantePagamentoDae : listaComprovantePagamentoDae) {
				comprovantePagamentoDae.setIndComprovanteValidado(Boolean.TRUE);
				comprovantePagamentoDae.setDtcValidacao(new Date());
				
				comprovantePagamentoDaeService.salvarAtualizar(comprovantePagamentoDae);
			}
			ProcessoReenquadramento processoReenquadramento = processoReenquadramentoDAOImpl.obterProcessoReenquadramentoPorProcesso(ctrl.getValidarComprovante_boletoComplementar().getProcesso().getIdeProcesso());
			if (!Util.isNullOuVazio(processoReenquadramento)){
				
				tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcessoSemFlush(processoReenquadramento, new StatusReenquadramento(StatusReenquadramentoEnum.REENQUADRADO), ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
			}
		}
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void invalidaComprovantePagamentoDae(BoletoComplementarController ctrl) throws Exception {
		if (!Util.isNullOuVazio(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento())){
			
			adicionarFiltroPorStatus(ctrl.getFilter(), ctrl.getValidarComprovante_boletoComplementar().getStatus());
			
			ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento());
			ctrl.mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE, false, true);
		}

		BoletoDaeHistorico bph = new BoletoDaeHistorico();
		bph.setIdeBoletoDaeRequerimento(new BoletoDaeRequerimento(ctrl.getValidarComprovante_boletoComplementar().getId()));
		bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.PENDENCIA_VALIDACAO.getId()));
		bph.setDtcTramitacao(new Date());
		bph.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());

		boletoDaeHistoricoService.salvar(bph);

		Requerimento req = null;
		Processo proc = null;
		
		if(Util.isNullOuVazio(ctrl.getValidarComprovante_boletoDaeRequerimento().getIdeRequerimento()))
			req = requerimentoService.buscarEntidadePorId(ctrl.getValidarComprovante_boletoDaeRequerimento().getIdeProcesso().getIdeRequerimento());
		else
			req = requerimentoService.buscarEntidadePorId(ctrl.getValidarComprovante_boletoDaeRequerimento().getIdeRequerimento());
		
		ctrl.enviarEmails(req, proc, true);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void invalidaComprovantePagamento(BoletoComplementarController ctrl) throws Exception{
		if (!Util.isNullOuVazio(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento())){
			
			adicionarFiltroPorStatus(ctrl.getFilter(), ctrl.getValidarComprovante_boletoComplementar().getStatus());
			
			ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getValidarComprovante_boletoComplementar().getIdeProcessoReenquadramento());
			ctrl.mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE, false, true);
		}
		
		BoletoPagamentoHistorico bph = new BoletoPagamentoHistorico();
		bph.setIdeBoletoPagamento(new BoletoPagamentoRequerimento(ctrl.getValidarComprovante_boletoComplementar().getId()));
		bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.PENDENCIA_VALIDACAO.getId()));
		bph.setDtcTramitacao(new Date());
		bph.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());

		boletoPagamentoHistoricoService.salvar(bph);
		
		Requerimento req = null;
		Processo proc = null;
		
		if(!Util.isNullOuVazio(ctrl.getValidarComprovante_boletoPagamentoRequerimento().getIdeRequerimento())) {
			req = requerimentoService.buscarEntidadePorId(ctrl.getValidarComprovante_boletoPagamentoRequerimento().getIdeRequerimento());
		} else {
			proc = processoService.buscarProcessoPorCriteria(ctrl.getValidarComprovante_boletoPagamentoRequerimento().getIdeProcesso().getNumProcesso());
		}
		
		ctrl.enviarEmails(req, proc, true);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirComprovanteDAE(BoletoComplementarController ctrl, String tipo) {
		if ("certificado".equals(tipo)){
			gerenciaArquivoservice.deletarArquivo(ctrl.getEnviarComprovantePagamentoDTO().getCertificado().getPathComprovante());
			ctrl.getEnviarComprovantePagamentoDTO().getCertificado().setPathComprovante(null);
	
			
			ComprovantePagamentoDae comprovantePagamentoDae = boletoComplementarServicee.preencherCaminhoArquivo(ctrl.getEnviarComprovantePagamentoDTO().getCertificado(), false);
			if (!Util.isNullOuVazio(comprovantePagamentoDae)){
				comprovantePagamentoDaeService.remover(comprovantePagamentoDae);
			}
			ctrl.getEnviarComprovantePagamentoDTO().setCertificado(null);
			ctrl.setComprovanteCertificado(null);
			
		} else {
			gerenciaArquivoservice.deletarArquivo(ctrl.getEnviarComprovantePagamentoDTO().getVistoria().getPathComprovante());
			ctrl.getEnviarComprovantePagamentoDTO().getVistoria().setPathComprovante(null);

			
			ComprovantePagamentoDae comprovantePagamentoDae = boletoComplementarServicee.preencherCaminhoArquivo(ctrl.getEnviarComprovantePagamentoDTO().getVistoria(), true);
			if (!Util.isNullOuVazio(comprovantePagamentoDae)){
				comprovantePagamentoDaeService.remover(comprovantePagamentoDae);
			}
			ctrl.getEnviarComprovantePagamentoDTO().setVistoria(null);
			ctrl.setComprovanteVistoria(null);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirComprovanteBoleto(BoletoComplementarController ctrl) {
		gerenciaArquivoservice.deletarArquivo(ctrl.getEnviarComprovantePagamentoDTO().getBoleto().getPathComprovante());
		
		ComprovantePagamento comprovantePagamento = comprovantePagamentoService.consultarPorIdBoletoPagamentoRequerimento(ctrl.getEnviarComprovantePagamentoDTO().getBoleto().getIdeBoletoPagamentoRequerimento());
		
		if (!Util.isNullOuVazio(comprovantePagamento)){
			comprovantePagamentoService.remover(comprovantePagamento);
		}
		ctrl.setComprovanteBoleto(null);
		ctrl.getEnviarComprovantePagamentoDTO().setBoleto(null);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComprovanteDae(BoletoComplementarController ctrl) throws Exception{
		
		if (!Util.isNullOuVazio(ctrl.getEnviarComprovante_boleto().getIdeProcessoReenquadramento())){
			if (ctrl.isReenquadramento()){
				ctrl.atualizarEnviarComprovanteBoleto();
			} else {
				if (StatusBoletoEnum.PENDENCIA_VALIDACAO.getValue().equals(ctrl.getEnviarComprovante_boleto().getStatus())){
					ctrl.getFilter().setPendenciaValidacaoComprovante(true);
				} else {
					ctrl.getFilter().setEmitido(true);
				}
			}
			ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getEnviarComprovante_boleto().getIdeProcessoReenquadramento());
			ctrl.mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum.COMPORVANTE_ENVIADO, true, false);
		}
		
		//pega a tramitação do requerimento
		TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(ctrl.getEnviarComprovante_boleto().getRequerimento().getIdeRequerimento());
		
		if (!Util.isNullOuVazio(ctrl.getEnviarComprovante_boleto().getRequerimento().getIdeRequerimento())){
			ctrl.getEnviarComprovantePagamentoDTO().setRequerimento(ctrl.getEnviarComprovante_boleto().getRequerimento());
		} else {
			ctrl.getEnviarComprovantePagamentoDTO().setRequerimento(ctrl.getEnviarComprovante_boleto().getProcesso().getIdeRequerimento());
		}
		ctrl.getEnviarComprovantePagamentoDTO().setTramitacaoRequerimento(tramitacaoRequerimento);
		ctrl.getEnviarComprovantePagamentoDTO().setBoletoComplementarDTO(ctrl.getEnviarComprovante_boleto());
		ctrl.getEnviarComprovantePagamentoDTO().setComprovanteBoleto(ctrl.getComprovanteBoleto());
		ctrl.getEnviarComprovantePagamentoDTO().setComprovanteCertificado(ctrl.getComprovanteCertificado());
		ctrl.getEnviarComprovantePagamentoDTO().setComprovanteVistoria(ctrl.getComprovanteVistoria());
		boletoComplementarServiceFacade.salvarComprovantes(ctrl.getEnviarComprovantePagamentoDTO());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComprovanteBoleto(BoletoComplementarController ctrl) throws Exception{
		ComprovantePagamento comprovante = new ComprovantePagamento();
		BoletoPagamentoRequerimento boletoPagamentoRequerimento;
		if (ctrl.isReenquadramento()){
			boletoPagamentoRequerimento = new BoletoPagamentoRequerimento(ctrl.getBoletoReequerimento().getId());
			
		} else {
			boletoPagamentoRequerimento = new BoletoPagamentoRequerimento(ctrl.getEnviarComprovante_boleto().getId());
		}
		
		if (!Util.isNullOuVazio(ctrl.getEnviarComprovante_boleto().getIdeProcessoReenquadramento())){
			
			if (ctrl.isReenquadramento()){
				ctrl.atualizarEnviarComprovanteBoleto();
			} else {
				if (StatusBoletoEnum.PENDENCIA_VALIDACAO.getValue().equals(ctrl.getEnviarComprovante_boleto().getStatus())){
					ctrl.getFilter().setPendenciaValidacaoComprovante(true);
				} else {
					ctrl.getFilter().setEmitido(true);
				}
			}
			
			ctrl.getFilter().setIdeProcessoReenquadramento(ctrl.getEnviarComprovante_boleto().getIdeProcessoReenquadramento());
			ctrl.mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum.COMPORVANTE_ENVIADO, false, false);
		}
		
		comprovante.setIdeBoletoPagamentoRequerimento(boletoPagamentoRequerimento);
		comprovante.setDscCaminhoArquivo(ctrl.getEnviarComprovantePagamentoDTO().getBoleto().getPathComprovante());
		comprovante.setIndComprovanteValidado(Boolean.FALSE);
		comprovante.setIdePessoaUpload(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());

		comprovantePagamentoService.salvarAtualizar(comprovante);
		ctrl.setValidarComprovante_comprovantePagamento(comprovante);

		BoletoPagamentoHistorico bph = new BoletoPagamentoHistorico();
		bph.setIdeBoletoPagamento(boletoPagamentoRequerimento);
		bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.COMPROVANTE.getId()));
		bph.setDtcTramitacao(new Date());
		bph.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());

		boletoPagamentoHistoricoService.salvar(bph);
	}
	
	private boolean isTodosBoletosPagos(Integer ideProcesso){
		
		List<BoletoPagamentoRequerimento> boletos = boletoPagamentoRequerimentoService.listarPorIdeProcesso(ideProcesso);
		for(BoletoPagamentoRequerimento boleto : boletos){
			if(boleto.getIdeLoteRetornoBoleto() == null){
				return false;
			}
		}
		
		return true;
	}
}