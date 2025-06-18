package br.gov.ba.seia.service;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.dao.AnaliseTecnicaDAOImpl;
import br.gov.ba.seia.dao.EmpreendimentoRequerimentoDAOImpl;
import br.gov.ba.seia.dao.EquipeDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.RequerimentoTipologiaDAOImpl;
import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.ControleFluxo;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ProcessoReenquadramentoHistAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.OperacaoProcessoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.facade.ProcessoReenquadramentoHistAtoServiceFacade;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PermissaoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VwConsultaProcessoService {

	@Inject
	private IDAO<VwConsultaProcesso> vwProcessoDAO;
	@Inject
	private IDAO<ControleFluxo> controleFluxoDAO;
	@EJB
	private PautaService pautaService;
	@EJB
	private AreaService areaService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private DocumentoIdentificacaoRequerimentoService documentoIdentificacaoRequerimentoService;
	@EJB
	private DocumentoRepresentacaoRequerimentoService documentoRepresentacaoRequerimentoService;
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private NotificacaoServiceFacade notificacaoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private ProcessoAtoService processoAtoService;
	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;
	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
	@EJB
	private StatusFluxoService statusFluxoService;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private AnaliseTecnicaDAOImpl analiseTecnicaDAOImpl;
	@EJB
	private EquipeDAOImpl equipeDAOImpl;
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	@EJB
	private ReservaAguaService reservaAguaService;
	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;
	@EJB
	private RequerimentoTipologiaDAOImpl requerimentoTipologiaDAOImpl;
	@EJB
	private EmpreendimentoRequerimentoDAOImpl empreendimentoRequerimentoDAOImpl;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	@EJB
	private ProcessoReenquadramentoHistAtoServiceFacade processoReenquadramentoHistAtoServiceFacade;
	@EJB
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwConsultaProcesso> listarPorCriteriaDemanda(Map<String, Object> params, OperacaoProcessoEnum operacaoProcessoEnum, Integer idePauta, Integer startPage, Integer maxPage) throws Exception {
		
		try {
			Collection<VwConsultaProcesso> listaProcessos = new ArrayList<VwConsultaProcesso>();
			listaProcessos = consultarProcessos(params, operacaoProcessoEnum, idePauta, startPage, maxPage);
			listaProcessos = getListaProcessoComAcoesVinculadas((List<VwConsultaProcesso>) listaProcessos);
			listaProcessos = getProcessoReenquadramento((List<VwConsultaProcesso>) listaProcessos);
			
			return listaProcessos;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwConsultaProcesso> listarPorCriteriaDemanda(Map<String, Object> params, OperacaoProcessoEnum operacaoProcessoEnum, Integer idePauta) throws Exception {
		
		try {
			Collection<VwConsultaProcesso> listaProcessos = new ArrayList<VwConsultaProcesso>();
			listaProcessos = consultarProcessos(params, operacaoProcessoEnum, idePauta, null,null);
			
			return listaProcessos;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwConsultaProcesso> listarProcessoSincronizacaoSinaflor() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
			.createAlias("ideRequerimento","req", JoinType.INNER_JOIN)
			.createAlias("req.requerimentoUnico","ru", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.empreendimentoRequerimentoCollection","er", JoinType.LEFT_OUTER_JOIN)
			.createAlias("er.ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.requerimentoPessoaCollection", "rp", JoinType.INNER_JOIN)
			.createAlias("rp.ideTipoPessoaRequerimento", "tpr", JoinType.INNER_JOIN)
			.createAlias("rp.pessoa", "p", JoinType.INNER_JOIN)
			
			.createAlias("processoAtoCollection", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.atoAmbiental", "aa", JoinType.INNER_JOIN)
			.createAlias("pa.ideProcessoAtoConcedido", "pac", JoinType.INNER_JOIN)
			.createAlias("controleTramitacaoCollection", "ct", JoinType.INNER_JOIN)
			.createAlias("processoSinaflorCollection", "ps", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ct.ideStatusFluxo", "sf", JoinType.INNER_JOIN)
			.createAlias("pa.controleProcessoAtoCollection", "cpa", JoinType.INNER_JOIN)
			.createAlias("cpa.ideStatusProcessoAto", "spa", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("ct.indFimDaFila", true))
			.add(Restrictions.in("aa.ideAtoAmbiental", Arrays.asList(new Integer[] {
					AtoAmbientalEnum.ASV.getId()
			})))
			.add(Restrictions.or(
					Restrictions.eq("spa.ideStatusProcessoAto", StatusProcessoAtoEnum.DEFERIDO.getId()),
					Restrictions.eq("spa.ideStatusProcessoAto", StatusProcessoAtoEnum.TRANSFERIDO.getId())))
			.add(Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
			.add(Restrictions.eq("sf.ideStatusFluxo", StatusFluxoEnum.CONCLUIDO.getStatus()))
			.add(Restrictions.isNull("ps.ideProcessoSinaflor"))
			
			//Só deve sincronizar processos cujo requerimento foi finalizado a partir de 2 de maio de 2018 Ps.: O GregronCakender comeca a contar do 0(janeiro).
			.add(Restrictions.ge("req.dtcFinalizacao", new Date(new GregorianCalendar(2018, 4, 2).getTimeInMillis())))
			
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideProcesso"),"ideProcesso")
				.add(Projections.groupProperty("emp.ideEmpreendimento"),"ideEmpreendimento")
				.add(Projections.groupProperty("req.ideRequerimento"),"ideRequerimento")
				.add(Projections.groupProperty("p.idePessoa"),"idePessoaRequerente")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(VwConsultaProcesso.class));
		
		return vwProcessoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso buscarVwConsultaProcessoPorIdeProcesso(Integer ideProcesso, boolean isLai) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", ideProcesso);
		params.put("isLai", isLai);
		DetachedCriteria criteria = getCriteriaConsultarProcessos(params, OperacaoProcessoEnum.CONSULTAR, null);
		List<VwConsultaProcesso> lista = vwProcessoDAO.listarPorCriteria(criteria);
		if (!Util.isNullOuVazio(lista)){
			return lista.get(0);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<VwConsultaProcesso> getProcessoReenquadramento(List<VwConsultaProcesso> listaProcessos) throws Exception{
		for (VwConsultaProcesso vwConsultaProcesso : listaProcessos) {
			ProcessoReenquadramento processoReenquadramento = processoReenquadramentoDAOImpl.obterProcessoReenquadramentoPorProcesso(vwConsultaProcesso.getIdeProcesso());
			vwConsultaProcesso.setProcessoReenquadramento(processoReenquadramento);
		}
		return listaProcessos;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComQtdDiasProcessoFormado(VwConsultaProcesso processo) throws Exception {
		calcularQtdDiasFormacaoDoProcesso(processo);
		return processo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void calcularQtdDiasFormacaoDoProcesso(VwConsultaProcesso processo) {
		
		Long qtdDiasFormado = null;
	
		if (!Util.isNull(processo) && !Util.isNullOuVazio(processo.getControleTramitacaoAllList())) {
			
			for (ControleTramitacao ct : processo.getControleTramitacaoAllList()) {
				
				if (!Util.isNull(ct.getIdeStatusFluxo()) && !Util.isNull(ct.getIdeStatusFluxo().getIdeStatusFluxo())) {
					
					if ((ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.CONCLUIDO.getStatus()) 
							|| ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.ARQUIVADO.getStatus()))) {
						
						qtdDiasFormado = ct.getDtcTramitacao().getTime() - processo.getDtcFormacao().getTime();
						processo.setQtdDiasFormado(qtdDiasFormado / 86400000L);
					}
				}
			}
		}
		
		if(qtdDiasFormado == null) {
			qtdDiasFormado = new Date().getTime() - processo.getDtcFormacao().getTime();
			processo.setQtdDiasFormado(qtdDiasFormado / 86400000L);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComListControleTramitacaoAll(VwConsultaProcesso processo) throws Exception {
		processo.setControleTramitacaoAllList(controleTramitacaoService.listarControleTramitacaoPorIdeProcesso(processo.getIdeProcesso()));
		return processo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComListaAtos(VwConsultaProcesso processo) throws Exception {
		carregarAtosProcesso(processo);
		carregarAtosProcessoReequadramento(processo);
		return processo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarAtosProcesso(VwConsultaProcesso vwProcesso) throws Exception {
		List<ProcessoAto> listProcessoAto = processoAtoService.listarAtosPorProcesso(vwProcesso.getIdeProcesso());
		
		vwProcesso.setListProcessoAto(listProcessoAto);
		if (!listProcessoAto.isEmpty()) {
			vwProcesso.setGrupoProcesso(listProcessoAto.get(0).getAtoAmbiental().getIdeTipoAto().getIdeGrupoProcesso());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarAtosProcessoReequadramento(VwConsultaProcesso vwProcesso) throws Exception {
		List<ProcessoAto> listProcessoAto = processoAtoService.listarAtosPorProcessoReequadramento(vwProcesso.getIdeProcesso());
		vwProcesso.setListProcessoAtoReequadramento(listProcessoAto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> getAtosDosProcessos(List<VwConsultaProcesso> listaConsultaProcesso) throws Exception {
		return atoAmbientalService.filtrarListaAtosAmbientalPorProcessos(listaConsultaProcesso);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComDocumentoObrigatorio(VwConsultaProcesso processo) throws Exception {
	
		List<DocumentoObrigatorioRequerimento> listaDocumentoObrigatorioRequerimento = (List<DocumentoObrigatorioRequerimento>) documentoObrigatorioRequerimentoService.buscarDocumentosObrigatoriosRequerimentoEnquadramentoByIdeRequerimento(processo.getIdeRequerimento());
		for (DocumentoObrigatorioRequerimento dor : listaDocumentoObrigatorioRequerimento) {
			String caminhoArquivo = dor.getDscCaminhoArquivo().trim();
			File file = new File(caminhoArquivo);
			dor.setFileSize(file.length());
		}
		
		processo.setDocumentoObrigatorioRequerimentoList(listaDocumentoObrigatorioRequerimento);
		return processo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComDocumentoIdentificacao(VwConsultaProcesso processo) throws Exception {
		
		List<DocumentoIdentificacaoRequerimento> listaDocumentoIdentificacaoRequerimento = documentoIdentificacaoRequerimentoService.buscarDocsIdentificacao(processo.getIdeRequerimento(), null);
		for (DocumentoIdentificacaoRequerimento dir : listaDocumentoIdentificacaoRequerimento) {
			String caminhoArquivo = dir.getDscCaminhoArquivo().trim();
			File file = new File(caminhoArquivo);
			dir.setFileSize(file.length());
		}
		
		processo.setDocumentoIdentificacaoRequerimentoList(listaDocumentoIdentificacaoRequerimento);
		return processo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComDocumentoRepresentacao(VwConsultaProcesso processo) throws Exception {
		
		Collection<DocumentoRepresentacaoRequerimento> listaDocumentoRepresentacaoRequerimento = documentoRepresentacaoRequerimentoService.buscarDocsRepresentacaoByRequerimento(processo.getIdeRequerimento());
		for (DocumentoRepresentacaoRequerimento drr : listaDocumentoRepresentacaoRequerimento) {
			String caminhoArquivo = drr.getDscCaminhoArquivo().trim();
			File file = new File(caminhoArquivo);
			drr.setFileSize(file.length());
		}
		
		processo.setDocumentoRepresentacaoRequerimentoList(listaDocumentoRepresentacaoRequerimento);
		return processo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComComprovante(VwConsultaProcesso processo) throws Exception {
		ArrayList<ComprovantePagamento> listComprovantePagamentosReq =  new ArrayList<ComprovantePagamento>();
		ComprovantePagamento comprovante = comprovantePagamentoService.obterPorIdRequerimento(processo.getIdeRequerimento());
		if(!Util.isNull(comprovante)){
			listComprovantePagamentosReq.add(comprovante);
		}
		/**
		 * Melhoria que carregar a grid (COMPROVANTE DE PAMENTO)independente de ter registro em "comprovante_pagamento". 
		 * @redmine: http://10.105.17.77/redmine/issues/9765
		 * @author danilo.santos
		 */
		else{
			List<BoletoPagamentoRequerimento> listBoletoPagamentoReq  = boletoPagamentoRequerimentoService.carregarByRequerimento(processo.getIdeRequerimento(), false);
			for (BoletoPagamentoRequerimento boletoPagamentoReq : listBoletoPagamentoReq) {
				comprovante = new ComprovantePagamento(); 
				boletoPagamentoReq.setIdeRequerimento(new Requerimento(processo.getIdeRequerimento()));
				comprovante.setIdeBoletoPagamentoRequerimento(boletoPagamentoReq);
				listComprovantePagamentosReq.add(comprovante);
			}
		}
		
		processo.setComprovantePagamentoList(listComprovantePagamentosReq);
		return processo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComComprovanteBoletoComplementar(VwConsultaProcesso processo) throws Exception {
			ComprovantePagamento comprovante = new ComprovantePagamento();
			ArrayList<ComprovantePagamento> listComprovantePagamentosReq =  new ArrayList<ComprovantePagamento>();
			List<BoletoPagamentoRequerimento> listBoletoPagamentoReq  = boletoPagamentoRequerimentoService.carregarByRequerimento(processo.getIdeRequerimento(),true);
			for (BoletoPagamentoRequerimento boletoPagamentoReq : listBoletoPagamentoReq) {
				comprovante = new ComprovantePagamento(); 
				boletoPagamentoReq.setIdeRequerimento(new Requerimento(processo.getIdeRequerimento()));
				comprovante.setIdeBoletoPagamentoRequerimento(boletoPagamentoReq);
				listComprovantePagamentosReq.add(comprovante);
			}
		
		processo.setComprovantePagamentoComplementarList(listComprovantePagamentosReq);
		return processo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private VwConsultaProcesso getProcessoComComprovanteDae(VwConsultaProcesso processo) throws Exception {
		List<ComprovantePagamentoDae> listComprovantePagamentosDaeReq = comprovantePagamentoDaeService.obterPorIdRequerimento(processo.getIdeRequerimento());
		processo.setComprovantePagamentoDaeList(listComprovantePagamentosDaeReq);
		return processo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComArquivos(VwConsultaProcesso processo) throws Exception {
		carregarArquivosApensados(processo);
		carregarArquivosApensadosNaNoticacao(processo);
		return processo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarArquivosApensadosComCategoria(VwConsultaProcesso processo) throws Exception {
		List<ArquivoProcesso> listArquivoProcesso = (List<ArquivoProcesso>) arquivoProcessoService.listarArquivosApensosPublicosPorIdeProcesso(processo.getIdeProcesso());
		for (ArquivoProcesso ap : listArquivoProcesso) {
			if(!Util.isNull(ap.getDscCaminhoArquivo())){
				String caminhoArquivo = ap.getDscCaminhoArquivo();
				File file = getFile(caminhoArquivo);
				ap.setFileSize(file.length());
				ap.setFileName(file.getName());
			}
		}
		processo.setArquivoProcessoList(listArquivoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarArquivosApensadosNaNoticacao(VwConsultaProcesso processo) throws Exception {
		List<ArquivoProcesso> listArquivoProcesso = (List<ArquivoProcesso>) arquivoProcessoService.listaArquivoProcessoPorIdeProcessoPorNotificacao(processo.getIdeProcesso());
		for (ArquivoProcesso ap : listArquivoProcesso) {
			if(!Util.isNull(ap.getDscCaminhoArquivo())){
				String caminhoArquivo = ap.getDscCaminhoArquivo();
				File file = getFile(caminhoArquivo);
				ap.setFileSize(file.length());
				ap.setFileName(file.getName());
			}
		}
		processo.setArquivoProcessoNotificacaoList(listArquivoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private File getFile(String arquivoProcesso) {
		byte[] arquivoProcessoBytes = arquivoProcesso.getBytes();
		File file = new File("");
		try {
			file = new File(arquivoProcesso);
			if(file.exists()) {
				return file;
			}
			file = new File(arquivoProcesso.trim());
			if(file.exists()) {
				return file;
			}
			Util.removerAcentos(arquivoProcesso);
			file = new File(arquivoProcesso);
			if(file.exists()) {
				return file;
			}
			String encodedString = new String(arquivoProcessoBytes,"UTF8");
			file = new File(encodedString);
			if(file.exists()) {
				return file;
			}
			encodedString = new String(arquivoProcessoBytes,"ISO_8859_1");
			file = new File(encodedString);
			if(file.exists()) {
				return file;
			}	
			encodedString = new String(arquivoProcessoBytes,"US-ASCII");
			file = new File(encodedString);
			if(file.exists()) {
				return file;
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File(arquivoProcesso);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarArquivosApensados(VwConsultaProcesso processo) throws Exception {
		List<ArquivoProcesso> listArquivoProcesso = (List<ArquivoProcesso>) arquivoProcessoService.listaArquivoProcessoPorIdeProcesso(processo.getIdeProcesso());
		for (ArquivoProcesso ap : listArquivoProcesso) {
			if(!Util.isNull(ap.getDscCaminhoArquivo())){
				String caminhoArquivo = ap.getDscCaminhoArquivo();
				File file = getFile(caminhoArquivo);
				ap.setFileSize(file.length());
				ap.setFileName(file.getName());
			}
		}
		processo.setArquivoProcessoList(listArquivoProcesso);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoComTodasNotificacoes(VwConsultaProcesso processo) throws Exception {
		carregarNotificacoes(processo);
		return processo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarNotificacoes(VwConsultaProcesso processo) throws Exception {
		processo.setNotificacaoEnviadaList(notificacaoDAOImpl.listarNotificacoesAprovadasByProcesso(new Processo(processo.getIdeProcesso())));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ControleFluxo> getListaControleFuxoDoProcesso(VwConsultaProcesso vwConsultaProcesso) throws Exception {
		getProcessoComListaAtos(vwConsultaProcesso);
		getTipologiaVwProcesso(vwConsultaProcesso);
		if (vwConsultaProcesso.getGrupoProcesso() != null && vwConsultaProcesso.getStatusFluxo() != null) {
			DetachedCriteria criteriaControleFluxo = DetachedCriteria.forClass(ControleFluxo.class);
			criteriaControleFluxo.add(Restrictions.eq("ideGrupoProcesso.ideGrupoProcesso", vwConsultaProcesso.getGrupoProcesso().getIdeGrupoProcesso()));
			criteriaControleFluxo.add(Restrictions.eq("ideStatusAnterior.ideStatusFluxo", vwConsultaProcesso.getStatusFluxo().getIdeStatusFluxo()));
			return  controleFluxoDAO.listarPorCriteria(criteriaControleFluxo);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void getTipologiaVwProcesso(VwConsultaProcesso vwConsultaProcesso) throws Exception{
		List<Tipologia> lTipologia = tipologiaService.listarTipologiaPorProcesso(vwConsultaProcesso);
		if(lTipologia != null && !lTipologia.isEmpty()){
			vwConsultaProcesso.setTipologiaList(lTipologia);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void getProcessoComRequerimentoTipologia(VwConsultaProcesso vwConsultaProcesso) throws Exception{
		Collection<RequerimentoTipologia> listaRequerimentoTipologia = requerimentoTipologiaDAOImpl.listarRequerimentoTipologiaPor(vwConsultaProcesso);
		vwConsultaProcesso.setRequerimentoTipologiaList(listaRequerimentoTipologia);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void getProcessoComEmpreendimentoRequerimento(VwConsultaProcesso vwConsultaProcesso) throws Exception{
		Collection<EmpreendimentoRequerimento> listaEmpreendimentoRequerimento = empreendimentoRequerimentoDAOImpl.listarEmpreendimentoRequerimento(vwConsultaProcesso);
		vwConsultaProcesso.setEmpreendimentoRequerimentoList(listaEmpreendimentoRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<VwConsultaProcesso> getListaProcessoComAcoesVinculadas(List<VwConsultaProcesso> listaConsultaProcesso) {
		try {
			for (VwConsultaProcesso vwConsultaProcesso : listaConsultaProcesso) {
				
				
				List<ControleFluxo> listaControleFluxo = getListaControleFuxoDoProcesso(vwConsultaProcesso);
				
				if (!Util.isNull(listaControleFluxo) && !listaControleFluxo.isEmpty()) {					
					for (ControleFluxo cf : listaControleFluxo) {
						vwConsultaProcesso.setPodeEncaminhar(isPodeEncaminhar(vwConsultaProcesso));
						vwConsultaProcesso.setPodeNotificar(isPodeNotificar(cf));
						vwConsultaProcesso.setPodeAprovarNotificacao(isPodeAprovarNotificacao(cf));
						vwConsultaProcesso.setPodeApensarDocumento(isPodeApensarDocumento(cf));
					}
				} 
				else{
					vwConsultaProcesso.setPodeEncaminhar(true);
				}
				vwConsultaProcesso.setPodeFormarEquipe(isPodeFormarEquipe(vwConsultaProcesso));
				vwConsultaProcesso.setPodeReabrirProcesso(isPodeReabrirProcesso(vwConsultaProcesso));
				vwConsultaProcesso.setPodeAprovarAnaliseTecnina(isPodeAprovarAnaliseTecnica(vwConsultaProcesso));
				vwConsultaProcesso.setPodeAtualizarPassivo(isPodeAtualizarPassivo(vwConsultaProcesso));
				vwConsultaProcesso.setRenderedNotificacaoPrazo(isRenderedNotificacaoPrazo(vwConsultaProcesso));
				vwConsultaProcesso.setRenderedNotificacaoComunicacao(isRenderedNotificacaoComunicacao(vwConsultaProcesso));
				vwConsultaProcesso.setStatusAguardandoAprovacao(isStatusAguardandoAprovacao(vwConsultaProcesso));
				vwConsultaProcesso.setPodeExibirAnaliseTecnicaImovelRural(isPodeExibirAnaliseTecnicaImovelRural(vwConsultaProcesso));
				vwConsultaProcesso.setRenderedReservaAgua(isPodeCancelarReservaAgua(vwConsultaProcesso));
				vwConsultaProcesso.setRenderedRfp(isRenderedRfp(vwConsultaProcesso));
				vwConsultaProcesso.setRenderedRcfp(isRenderedRcfp(vwConsultaProcesso));
				vwConsultaProcesso.setRenderedDqc(isRenderedDcq(vwConsultaProcesso));				
				vwConsultaProcesso.setRenderedRlac(isRenderedRlac(vwConsultaProcesso));				
				vwConsultaProcesso.setRenderedLacErb(isRenderedLacErb(vwConsultaProcesso));				
				vwConsultaProcesso.setRenderedLacPosto(isRenderedLacPosto(vwConsultaProcesso));				
				vwConsultaProcesso.setRenderedLacTransportadora(isRenderedLacTransportadora(vwConsultaProcesso));				
				vwConsultaProcesso.setRenderedAPE(isRenderedAPE(vwConsultaProcesso));				
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		return listaConsultaProcesso;
	}
	
	private boolean isRenderedLacErb(VwConsultaProcesso vwConsultaProcesso) {
		if(!Util.isNull(vwConsultaProcesso.getLac())) {
			return vwConsultaProcesso.getLac().getIdeDocumentoObrigatorio().isErb();
		}
		return false;
	}
	
	private boolean isRenderedLacPosto(VwConsultaProcesso vwConsultaProcesso) {
		if(!Util.isNull(vwConsultaProcesso.getLac())) {
			return vwConsultaProcesso.getLac().getIdeDocumentoObrigatorio().isPosto();
		}
		return false;
	}
	
	private boolean isRenderedLacTransportadora(VwConsultaProcesso vwConsultaProcesso) {
		if(!Util.isNull(vwConsultaProcesso.getLac())) {
			return vwConsultaProcesso.getLac().getIdeDocumentoObrigatorio().isTransportadora();
		}
		return false;
	}
	
	private boolean isRenderedRfp(VwConsultaProcesso vwConsultaProcesso) {
		if(isProcessoConcluido(vwConsultaProcesso)) {
			for (ProcessoAto pa : vwConsultaProcesso.getListProcessoAto()) {
				if (pa.getAtoAmbiental().isRfp()) {
					return true;
				}
			}			
		}
		return false;
	}
	
	private boolean isRenderedRcfp(VwConsultaProcesso vwConsultaProcesso) {
		if(isProcessoConcluido(vwConsultaProcesso)) {
			for (ProcessoAto pa : vwConsultaProcesso.getListProcessoAto()) {
				if (pa.getAtoAmbiental().isRcfp()) {
					return true;
				}
			}			
		}
		return false;
	}
	
	private boolean isRenderedDcq(VwConsultaProcesso vwConsultaProcesso) {
		if(isProcessoConcluido(vwConsultaProcesso)) {
			for (ProcessoAto pa : vwConsultaProcesso.getListProcessoAto()) {
				if (pa.getAtoAmbiental().isDqc()) {
					return true;
				}
			}			
		}
		return false;
	}
	
	private boolean isRenderedAPE(VwConsultaProcesso vwConsultaProcesso) {
		
		try {
			Collection<AtoAmbiental> atosAmbientais = atoAmbientalService.listarAtosEnquadradosByRequerimento(vwConsultaProcesso.getIdeRequerimento());
			
			if (!Util.isNullOuVazio(atosAmbientais)) {
				for (AtoAmbiental atoAmbiental : atosAmbientais) {
					
					if (atoAmbiental.getIdeAtoAmbiental().equals(AtoAmbientalEnum.APE.getId())
						&& isProcessoConcluido(vwConsultaProcesso)) {
						
						return true;
					}
				}
			}
			
			return false;
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isRenderedRlac(VwConsultaProcesso vwConsultaProcesso) {
		if(isProcessoConcluido(vwConsultaProcesso)) {
			for (ProcessoAto pa : vwConsultaProcesso.getListProcessoAto()) {
				if (pa.getAtoAmbiental().isRlac()) {
					return true;
				}
			}			
		}
		return false;
	}

	private boolean isPodeFormarEquipe(VwConsultaProcesso vwConsultaProcesso) {
		return 
				!(new StatusFluxo(StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus()).equals(vwConsultaProcesso.getStatusFluxo()))
				
				&&
				
				!(new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus())
				.equals(vwConsultaProcesso.getStatusFluxo()))
		;
	}
	
	private boolean isRenderedNotificacaoPrazo(VwConsultaProcesso vwp) {
		try {
			return !existeNotificacao(vwp) && existeEquipe(vwp);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isRenderedNotificacaoComunicacao(VwConsultaProcesso vwp) {
		try {
			return !existeNotificacao(vwp);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isPodeExibirAnaliseTecnicaImovelRural(VwConsultaProcesso vwConsultaProcesso) {
		try{
			return imovelRuralService.verificarReservaLegal(vwConsultaProcesso.getIdeEmpreendimento());
		}
		catch(Exception e){
			return false;
		}
	}
	
	private boolean isStatusAguardandoAprovacao(VwConsultaProcesso vwConsultaProcesso){
		
		int ideStatusFluxo = vwConsultaProcesso.getStatusFluxo().getIdeStatusFluxo();
		 
		if(StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus()==ideStatusFluxo
		    ||StatusFluxoEnum.NOTIFICACAO_ALTERADA.getStatus()==ideStatusFluxo
		    ||StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus()==ideStatusFluxo){
			 return true;
		 }
		 
		 return false;
	 }
	
	private boolean isPodeAtualizarPassivo(VwConsultaProcesso vwConsultaProcesso) {
		return vwConsultaProcesso.getStatusFluxo() != null && isProcessoConcluido(vwConsultaProcesso);
	}

	private boolean isPodeAprovarAnaliseTecnica(VwConsultaProcesso vwConsultaProcesso) {
		try {
			AnaliseTecnica analiseTecnica = analiseTecnicaDAOImpl.buscarAnaliseTecnica(vwConsultaProcesso.getIdeProcesso());
			return	!Util.isNull(analiseTecnica) 
					&& vwConsultaProcesso.getStatusFluxo() != null
					&& (vwConsultaProcesso.getStatusFluxo().getIdeStatusFluxo() == StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA.getStatus()
					)
			;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isPodeCancelarReservaAgua(VwConsultaProcesso vwConsultaProcesso) {
		try{
			return false;
		}
		catch(Exception e){
			return false;
		}
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isAreaDoUsuarioLogadoNOUT(Integer ideUsuarioLogado){
		Area area = buscarAreaByPessoaFisica(ideUsuarioLogado);
		if(!Util.isNull(area)){
			return area.getIdeArea().equals(AreaEnum.NOUT.getId());
		} else {
			return false;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isAreaDoUsuarioLogadoDIRRE(Integer ideUsuarioLogado){
		Area area = buscarAreaByPessoaFisica(ideUsuarioLogado);
		if(!Util.isNull(area)){
			return area.getIdeArea().equals(AreaEnum.DIRRE.getId());
		} else {
			return false;
		}
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param ideUsuarioLogado
	 * @return
	 * @throws Exception
	 * @since 24/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Area buscarAreaByPessoaFisica(Integer ideUsuarioLogado){
		try {
			return areaService.buscarAreaPorPessoaFisica(ideUsuarioLogado);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a área do funcionário logado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isPodeReabrirProcesso(VwConsultaProcesso vwConsultaProcesso) {
		return PermissaoUtil.temPermissaoParaReabrirProcesso() && processoPodeSerReaberto(vwConsultaProcesso);
	}

	/**
	 * Método que verifica se o {@link Processo} pode ser reaberto de acordo com seu {@link StatusFluxo}.
	 * @param vwConsultaProcesso
	 * @return
	 */
	private boolean processoPodeSerReaberto(VwConsultaProcesso vwConsultaProcesso) {
		return !Util.isNull(vwConsultaProcesso.getStatusFluxo()) && 
				(isProcessoCancelado(vwConsultaProcesso) || isProcessoConcluido(vwConsultaProcesso) || isProcessoArquivado(vwConsultaProcesso));
	}

	private boolean isProcessoArquivado(VwConsultaProcesso vwConsultaProcesso) {
		
		return new StatusFluxo(StatusFluxoEnum.ARQUIVADO.getStatus()).equals(vwConsultaProcesso.getStatusFluxo());
	}

	private boolean isProcessoConcluido(VwConsultaProcesso vwConsultaProcesso) {
		return new StatusFluxo(StatusFluxoEnum.CONCLUIDO.getStatus()).equals(vwConsultaProcesso.getStatusFluxo());
	}

	private boolean isProcessoCancelado(VwConsultaProcesso vwConsultaProcesso) {
		return new StatusFluxo(StatusFluxoEnum.CANCELADO.getStatus()).equals(vwConsultaProcesso.getStatusFluxo());
	}

	private boolean isPodeApensarDocumento(ControleFluxo cf) {
		return cf.getIdeAcaoFluxo().getIdeAcaoFluxo().intValue() == 4;
	}

	private boolean isPodeAprovarNotificacao(ControleFluxo cf) {
		return cf.getIdeAcaoFluxo().getIdeAcaoFluxo().intValue() == 3;
	}

	private boolean isPodeNotificar(ControleFluxo cf) {
		return cf.getIdeAcaoFluxo().getIdeAcaoFluxo().intValue() == 2;
	}

	private boolean isPodeEncaminhar(VwConsultaProcesso vwConsultaProcesso) {
		return vwConsultaProcesso.getStatusFluxo().getIdeStatusFluxo().intValue() != StatusFluxoEnum.NOTIFICACAO_EXPEDIDA.getStatus();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoLai(VwConsultaProcesso vwProcesso) throws Exception {
		VwConsultaProcesso vwConsultaProcessoRetorno = buscarVwConsultaProcessoPorIdeProcesso(vwProcesso.getIdeProcesso(), true);
		this.getProcessoComListaAtos(vwConsultaProcessoRetorno);		
		return vwConsultaProcessoRetorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private VwConsultaProcesso getDocumentoObrigatorioEnquadramento(VwConsultaProcesso processo) throws Exception {
		List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento = null;
		List<Integer> ideProcessoReenquadramentoList=new ArrayList<Integer>();
		if (!Util.isNullOuVazio(processo.getProcessoReenquadramento())){
			Collection<ProcessoAto> listaProcessoAto = processo.getListProcessoAtoReequadramento();
			for (ProcessoAto processoAto : listaProcessoAto) {
				if (!Util.isNullOuVazio(processoAto.getIdeProcessoReenquadramento())){
					ideProcessoReenquadramentoList.add( processoAto.getIdeProcessoReenquadramento().getIdeProcessoReenquadramento());
				}
			}
			listaDocumentoObrigatorioEnquadramento = documentoObrigatorioService.listaDocumentoObrigatorioEnquadramentoDTOReenquadramento(ideProcessoReenquadramentoList, true);	

			
		} else {
			Collection<ProcessoAto> listaProcessoAto = processo.getListProcessoAtoReequadramento();
			
			for (ProcessoAto processoAto : listaProcessoAto) {
				if (!Util.isNullOuVazio(processoAto.getIdeProcessoReenquadramento())){
					ideProcessoReenquadramentoList.add( processoAto.getIdeProcessoReenquadramento().getIdeProcessoReenquadramento());
					break;
				}
			}
			listaDocumentoObrigatorioEnquadramento = documentoObrigatorioService.listaDocumentoObrigatorioEnquadramentoDTOReenquadramento(ideProcessoReenquadramentoList, true);
		}
		
		if (!Util.isNullOuVazio(listaDocumentoObrigatorioEnquadramento)){
			List<DocumentoObrigatorioReenquadramento> listaDocumentoObrigatorioReenquadramento = new ArrayList<DocumentoObrigatorioReenquadramento>();
			
			for (DocumentoObrigatorioEnquadramentoDTO documentoObrigatorioEnquadramentoDTO : listaDocumentoObrigatorioEnquadramento) {
				for (DocumentoObrigatorioInterface documentoObrigatorioReenquadramento : documentoObrigatorioEnquadramentoDTO.getListaDocumentoObrigatorio()) {
					listaDocumentoObrigatorioReenquadramento.add((DocumentoObrigatorioReenquadramento)documentoObrigatorioReenquadramento);
				}
			}
			
			processo.setDocumentoObrigatorioReenquadramentoList(listaDocumentoObrigatorioReenquadramento);
		}
		return processo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getHistoricoAtoProcesso(VwConsultaProcesso vwProcesso) throws Exception {
		List<ProcessoReenquadramentoHistAto> lista = processoReenquadramentoHistAtoServiceFacade.obterProReenHistAtoPorProcessoReenquadramento(vwProcesso.getProcessoReenquadramento());
		
		if (!Util.isNullOuVazio(lista)){
			List<ProcessoAto> listaProcessoAto = processoAtoService.listarAtosPorProcesso(vwProcesso.getIdeProcesso());
			
			for (Iterator<ProcessoReenquadramentoHistAto> iterator = lista.iterator(); iterator.hasNext();) {
				ProcessoReenquadramentoHistAto processoReenquadramentoHistAto = (ProcessoReenquadramentoHistAto) iterator.next();
				
				for (ProcessoAto processoAto : listaProcessoAto) {
					if (!Util.isNullOuVazio(processoReenquadramentoHistAto.getIdeAtoAmbientalOriginal()) && processoReenquadramentoHistAto.getIdeAtoAmbientalOriginal().equals(processoAto.getAtoAmbiental())){
						iterator.remove();
						break;
					}
				}
			}
		}
		
		vwProcesso.setProcessoReenquadramentoHistAtoList(lista);
		return vwProcesso;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoCompleto(VwConsultaProcesso processo) throws Exception {
		
		VwConsultaProcesso vwConsultaProcessoRetorno = new VwConsultaProcesso();
		
		vwConsultaProcessoRetorno = getProcessoComListControleTramitacaoAll(processo);
		vwConsultaProcessoRetorno = getProcessoComQtdDiasProcessoFormado(processo);
		vwConsultaProcessoRetorno = getProcessoComListaAtos(processo);
		vwConsultaProcessoRetorno = getProcessoComDocumentoObrigatorio(processo);
		vwConsultaProcessoRetorno = getProcessoComDocumentoIdentificacao(processo);
		vwConsultaProcessoRetorno = getProcessoComDocumentoRepresentacao(processo);
		vwConsultaProcessoRetorno = getProcessoComArquivos(processo);
		vwConsultaProcessoRetorno = getProcessoComTodasNotificacoes(processo);
		vwConsultaProcessoRetorno = getProcessoComComprovante(processo);
		vwConsultaProcessoRetorno = getProcessoComComprovanteBoletoComplementar(processo);
		vwConsultaProcessoRetorno = getProcessoComComprovanteDae(processo);
		vwConsultaProcessoRetorno = getDocumentoObrigatorioEnquadramento(processo);
		vwConsultaProcessoRetorno = getHistoricoAtoProcesso(processo);
		vwConsultaProcessoRetorno.setPerguntas(this.perguntaRequerimentoService.carregarByIdeProcesso(processo.getIdeProcesso()));
		
		return vwConsultaProcessoRetorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso getProcessoCompletoLAI(Integer idProcesso) throws Exception {
		VwConsultaProcesso vwProcesso = buscarVwConsultaProcessoPorIdeProcesso(idProcesso, true);
		
		this.carregarArquivosApensadosComCategoria(vwProcesso);
		this.calcularQtdDiasFormacaoDoProcesso(vwProcesso);
		this.carregarAtosProcesso(vwProcesso);
		this.carregarDocFormacaoPublico(vwProcesso);
		this.carregarPerguntas(idProcesso, vwProcesso);
		this.carregarNotificacoes(vwProcesso);
		
		return vwProcesso;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarPerguntas(Integer idProcesso, VwConsultaProcesso vwProcesso) throws Exception {
		vwProcesso.setPerguntas(this.perguntaRequerimentoService.carregarByIdeProcesso(idProcesso));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarDocFormacaoPublico(VwConsultaProcesso processo) throws Exception {
		List<DocumentoObrigatorioRequerimento> listDocObrigatorioReq = (List<DocumentoObrigatorioRequerimento>) documentoObrigatorioRequerimentoService.buscarDocumentosDeFormacaoPublicos(processo.getIdeRequerimento());
		for (DocumentoObrigatorioRequerimento dor : listDocObrigatorioReq) {
			String caminhoArquivo = dor.getDscCaminhoArquivo().trim();
			File file = new File(caminhoArquivo);
			dor.setFileSize(file.length());
		}
		processo.setDocumentoObrigatorioRequerimentoList(listDocObrigatorioReq);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer consultarProcessosCount(Map<String, Object> params, OperacaoProcessoEnum operacaoProcessoEnum, Integer idePauta) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
			
		criteria
			.add(Property.forName("ideProcesso").in(
					getCriteriaConsultarProcessos(params, operacaoProcessoEnum, idePauta)
						.setProjection(Projections.projectionList()
							.add(Projections.sqlGroupProjection("{alias}.ide_processo",
									"{alias}.ide_processo, st16_.ide_status_fluxo", 
									new String[] {"ide_processo", "ide_status_fluxo"}, 
									new Type[] {StandardBasicTypes.INTEGER,StandardBasicTypes.INTEGER}
							)
						)
					)
				)
			)
			.setProjection(Projections.rowCount())
		;
		
		Object o = vwProcessoDAO.buscarPorCriteria(criteria);
		
		return Integer.valueOf(o.toString());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<VwConsultaProcesso> consultarProcessos(Map<String, Object> params, OperacaoProcessoEnum operacaoProcessoEnum, Integer idePauta, Integer startPage, Integer maxPage) throws Exception {
		
		DetachedCriteria criteria = getCriteriaConsultarProcessos(params, operacaoProcessoEnum, idePauta);
		
		if(!OperacaoProcessoEnum.CONSULTAR.equals(operacaoProcessoEnum)) {
			criteria.addOrder(Order.desc("ct.dtcTramitacao"));
		}
		
		criteria
			.addOrder(Order.desc("dtcFormacao"))
			.addOrder(Order.desc("numProcesso"))
		;
		
		if(startPage==null && maxPage==null) {
			return vwProcessoDAO.listarPorCriteria(criteria);
		}		
 		return vwProcessoDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage); 
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getCriteriaConsultarProcessos(Map<String, Object> params, OperacaoProcessoEnum operacaoProcessoEnum, Integer idePauta) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
			.createAlias("ideRequerimento","req", JoinType.INNER_JOIN)
			.createAlias("req.requerimentoUnico","ru", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.empreendimentoRequerimentoCollection","er", JoinType.LEFT_OUTER_JOIN)
			.createAlias("er.ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("emp.enderecoEmpreendimentoCollection", "eemp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("eemp.ideTipoEndereco", "tend", JoinType.LEFT_OUTER_JOIN)
			.createAlias("eemp.ideEndereco", "end", JoinType.LEFT_OUTER_JOIN)
			.createAlias("end.ideLogradouro","log", JoinType.LEFT_OUTER_JOIN)
			.createAlias("log.ideMunicipio", "mun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.requerimentoPessoaCollection", "rp", JoinType.INNER_JOIN)
			.createAlias("rp.ideTipoPessoaRequerimento", "tpr", JoinType.INNER_JOIN)
			.createAlias("rp.pessoa", "p", JoinType.INNER_JOIN)
			.createAlias("p.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("p.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
			.createAlias("controleTramitacaoCollection", "ct", JoinType.INNER_JOIN)
			.createAlias("ct.ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.createAlias("er.idePorte", "pt", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ru.idePorte", "ptru", JoinType.LEFT_OUTER_JOIN)
			.createAlias("processoAtoCollection", "pa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.atoAmbiental", "aa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("aa.ideTipoAto", "ta", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ct.ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("ct.idePauta", "pta", JoinType.INNER_JOIN)
			.createAlias("pta.idePessoaFisica","fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("notificacaoCollection","n", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.outorgaCollection","o", JoinType.LEFT_OUTER_JOIN)
			.createAlias("o.outorgaLocalizacaoGeograficaCollection","olg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("olg.outorgaLocalizacaoGeograficaFinalidadeCollection","olgf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("olgf.ideTipoFinalidadeUsoAgua", "tf", JoinType.LEFT_OUTER_JOIN)			
			
			//tla
			.createAlias("transferenciaAmbientalCollection", "tra", JoinType.LEFT_OUTER_JOIN)			
			.createAlias("tra.ideProcessoAto", "patr", JoinType.LEFT_OUTER_JOIN)			
			.createAlias("patr.processo", "ptla", JoinType.LEFT_OUTER_JOIN)			
			.createAlias("ptla.ideRequerimento","reqTla", JoinType.LEFT_OUTER_JOIN)			
			.createAlias("reqTla.requerimentoPessoaCollection", "rpTla", JoinType.LEFT_OUTER_JOIN)			
			.createAlias("req.lac", "lc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("lc.ideDocumentoObrigatorio", "do", JoinType.LEFT_OUTER_JOIN)
			
			.add(
				Restrictions.disjunction()
					.add(Restrictions.eq("aa.ideAtoAmbiental", AtoAmbientalEnum.DTRP.getId()))
					.add(Restrictions.eq("tend.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
					.add(Restrictions.eq("aa.ideAtoAmbiental", AtoAmbientalEnum.ARLS.getId()))
					.add(Restrictions.eq("aa.ideAtoAmbiental", AtoAmbientalEnum.CRF.getId())))
			
			.add(Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
			.add(Restrictions.eq("ct.indFimDaFila", true));
		
		adicionarFiltrosConsultaPadrao(params, operacaoProcessoEnum, criteria);
		adicionarFiltrosConsultaPauta(params, operacaoProcessoEnum, idePauta, criteria);
		
		adicionarProjection(criteria, operacaoProcessoEnum);
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(VwConsultaProcesso.class));
		
		return criteria;
	}

	private void adicionarProjection(DetachedCriteria criteria, OperacaoProcessoEnum operacaoProcessoEnum) {
		
		ProjectionList projectionList = Projections.projectionList();
		
		projectionList
			.add(Projections.groupProperty("ideProcesso"),"ideProcesso")
			.add(Projections.groupProperty("numProcesso"),"numProcesso")
			.add(Projections.groupProperty("dtcFormacao"),"dtcFormacao")
			.add(Projections.groupProperty("mun.nomMunicipio"),"nomMunicipio")
			.add(Projections.groupProperty("emp.ideEmpreendimento"),"ideEmpreendimento")
			.add(Projections.groupProperty("emp.nomEmpreendimento"),"nomEmpreendimento")
			.add(Projections.groupProperty("emp.indUnidadeConservacao"),"indUnidadeConservacao")
			.add(Projections.groupProperty("emp.desEmail"),"desEmail")
			.add(Projections.groupProperty("tpr.ideTipoPessoaRequerimento"),"ideTipoPessoaRequerimento")
			.add(Projections.groupProperty("req.ideRequerimento"),"ideRequerimento")
			.add(Projections.groupProperty("req.numRequerimento"),"numRequerimento")
			.add(Projections.groupProperty("req.desEmail"),"desEmailRequerimento")
			.add(Projections.groupProperty("req.dtcCriacao"),"dtcCriacaoRequerimento")
			.add(Projections.groupProperty("log.nomLogradouro"),"nomLogradouro")
			.add(Projections.groupProperty("p.idePessoa"),"idePessoaRequerente")
			.add(Projections.groupProperty("lc.ideLac"),"lac.ideLac")
			.add(Projections.groupProperty("lc.ideRequerimento.ideRequerimento"),"lac.ideRequerimento.ideRequerimento")
			.add(Projections.groupProperty("do.ideDocumentoObrigatorio"),"lac.ideDocumentoObrigatorio.ideDocumentoObrigatorio")
		;
		
		if(!OperacaoProcessoEnum.CONSULTAR.equals(operacaoProcessoEnum)) {
			projectionList
				.add(Projections.groupProperty("ct.dtcTramitacao"),"dtcTramitacao")
				.add(Projections.groupProperty("ct.indResponsavel"),"indLiderEquipe")
				.add(Projections.groupProperty("pta.idePauta"),"idePautaTramitacao")
				.add(Projections.groupProperty("a.ideArea"),"ideArea")
			;
		}
		
		projectionList
			.add(Projections.groupProperty("st.ideStatusFluxo"),"statusFluxo.ideStatusFluxo")
			.add(Projections.groupProperty("st.dscStatusFluxo"),"statusFluxo.dscStatusFluxo")
			.add(Projections.groupProperty("st.dscStatusFluxoExterno"),"statusFluxo.dscStatusFluxoExterno")
			.add(Projections.sqlGroupProjection("coalesce(pt17_.ide_porte,ptru18_.ide_porte) as ide_porte_", "ide_porte_", new String[] {"ide_porte_"}, new Type[] {StandardBasicTypes.INTEGER}),"idePorte")
			.add(Projections.sqlGroupProjection("coalesce(pt17_.nom_porte,ptru18_.nom_porte) as nom_porte_", "nom_porte_", new String[] {"nom_porte_"}, new Type[] {StandardBasicTypes.STRING}),"nomPorte")
			.add(Projections.sqlGroupProjection("coalesce(pf13_.nom_pessoa,pj14_.nom_razao_social) as nom_requerente_", "nom_requerente_", new String[] {"nom_requerente_"}, new Type[] {StandardBasicTypes.STRING}),"nomRequerente")
			.add(Projections.sqlGroupProjection("coalesce(pf13_.num_cpf,pj14_.num_cnpj) as numCpfcnpjRequerente_", "numCpfcnpjRequerente_", new String[] {"numCpfcnpjRequerente_"}, new Type[] {StandardBasicTypes.STRING}),"numCpfcnpjRequerente")
		;
		
		if(OperacaoProcessoEnum.CONSULTAR.equals(operacaoProcessoEnum)) {
			criteria.setProjection(Projections.distinct(projectionList));
		}
		else {
			criteria.setProjection(projectionList);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void adicionarFiltrosConsultaPadrao(Map<String, Object> params, OperacaoProcessoEnum operacaoProcessoEnum, DetachedCriteria criteria) throws Exception {
		if (params.get("area") != null) {
			criteria.add(Restrictions.eq("a.ideArea", params.get("area")));
		}
		
		if (params.get("ideProcesso") != null) {
			criteria.add(Restrictions.eq("ideProcesso", (Integer) params.get("ideProcesso")));
		}
		
		if (params.get("numProcesso") != null) {
			criteria.add(Restrictions.ilike("numProcesso", (String) params.get("numProcesso"), MatchMode.ANYWHERE));
		}
		
		if (params.get("periodoInicio") != null && params.get("periodoFim") != null) {
			criteria.add(Restrictions.between("dtcFormacao", params.get("periodoInicio"), params.get("periodoFim")));
		}
		
		if (params.get("statusFluxo") != null) {
			@SuppressWarnings("unchecked")
			List<Integer> list = (List<Integer>) params.get("statusFluxo");
			criteria.add(Restrictions.in("st.ideStatusFluxo", list));
		}
		
		if(params.get("tipoAto") != null) {
			criteria.add(Restrictions.eq("ta.ideTipoAto", params.get("tipoAto")));
		}
		
		if(params.get("atoAmbiental")!= null) {
			criteria.add(Restrictions.eq("aa.ideAtoAmbiental", params.get("atoAmbiental")));
		}
		
		if(params.get("tipologia")!= null) {
			criteria.add(Restrictions.eq("t.ideTipologia", params.get("tipologia")));
		}
		
		if(params.get("finalidade")!= null) {
			criteria.add(Restrictions.eq("tf.ideTipoFinalidadeUsoAgua", params.get("finalidade")));
		}
		
		if (params.get("idemunicipio") != null) {
			criteria.add(Restrictions.eq("mun.ideMunicipio", params.get("idemunicipio")));
		}
		
		if (params.get("funcionario") != null) {
			criteria.add(Restrictions.eq("fun.idePessoaFisica", params.get("funcionario")));
		}
		
		if(params.get("cpfcnpj") != null){
			criteria.add(Restrictions.or(
					Restrictions.eq("pf.numCpf", params.get("cpfcnpj")), 
					Restrictions.eq("pj.numCnpj", params.get("cpfcnpj"))
				)
			);
		}
		
		if(params.get("empreendimento") != null) {
			Empreendimento empreendimento = (Empreendimento) params.get("empreendimento");
			criteria.add(Restrictions.eq("emp.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		}
		
		if(OperacaoProcessoEnum.CONSULTAR.equals(operacaoProcessoEnum)) {
			
			if(ContextoUtil.getContexto().isUsuarioExterno() && !isLai(params)) {
				
				List<Integer>listaPessoas = permissaoPerfilService.listarIdesPessoas();
				List<Pessoa> lp = new ArrayList<Pessoa>();
				
				for(Integer i : listaPessoas) {
					if(!Util.isNullOuVazio(i)) {
						lp.add(new Pessoa(i));
					}
				}
				
				Disjunction or = Restrictions.disjunction();
				
				or
					.add(Restrictions.in("rp.pessoa", lp))
					.add(Restrictions.and(
							Restrictions.in("rpTla.pessoa", lp),
							Restrictions.eq("st.ideStatusFluxo", StatusFluxoEnum.CONCLUIDO.getStatus())
						)
					)
				;
				
				criteria.add(or);
			}
			
			criteria.add(Restrictions.or(
				Restrictions.isNull("ct.indAreaSecundaria"),
				Restrictions.eq("ct.indAreaSecundaria", false)
			));
		}
		
		filtrarPorTipologiaAtividade(criteria, params);
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void adicionarFiltrosConsultaPauta(Map<String, Object> params, OperacaoProcessoEnum operacaoProcessoEnum, Integer idePauta, DetachedCriteria criteria) throws Exception {
		switch (operacaoProcessoEnum) {
			case DISTRIBUIR:
				criteria.add(Restrictions.eq("pta.idePauta", idePauta));
				break;
			case PAUTA_GESTOR:
				criteria.add(Restrictions.eq("pta.idePauta", idePauta));
				Disjunction ors = Restrictions.disjunction();
				@SuppressWarnings("unchecked")
				List<Integer> list = (List<Integer>) params.get("statusFluxo");
				for(Integer ideStatusFluxo : list) {
					if(StatusFluxoEnum.NOTIFICACAO_EXPEDIDA.getStatus()==ideStatusFluxo) {
						ors.add(
								Restrictions.and(
									Restrictions.eq("st.ideStatusFluxo", ideStatusFluxo), 
									Restrictions.eq("n.tipo", TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO.getTipo())
							)
						);					
					}
					else{
						ors.add(Restrictions.eq("st.ideStatusFluxo", ideStatusFluxo));
					}
				}
				criteria.add(ors);
				break;
			case PAUTA_TECNICO:
				criteria.add(Restrictions.eq("pta.idePauta", idePauta));
				break;
			default:
				break;
		}
	}
	
	private boolean isLai(Map<String, Object> params) {
		return Util.isNull(params.get("isLai")) ? false : ((Boolean) params.get("isLai"));
	}
	
	public boolean existeEquipe(VwConsultaProcesso vwConsultaProcesso) throws Exception{
		Equipe equipe = equipeDAOImpl.buscarEquipe(vwConsultaProcesso.getIdeProcesso(), vwConsultaProcesso.getIdeArea());
		return equipe != null;
	}
	
	public boolean existeNotificacao(VwConsultaProcesso vwConsultaProcesso) throws Exception{ 
		Integer ideProcesso = vwConsultaProcesso.getIdeProcesso();
		Integer idePauta = vwConsultaProcesso.getIdePautaTramitacao();
		if(Util.isNull(idePauta)) {
			return true;
		}
		else {
			Pauta pautaTramitacao = new Pauta(idePauta);
			return notificacaoDAOImpl.verificarExistenciaNotificacao(ideProcesso, pautaTramitacao);
		}
	}
	
	private void filtrarPorTipologiaAtividade(DetachedCriteria criteria, Map<String, Object> params) {
		
		Tipologia tipologiaAtividade = (Tipologia) params.get("tipologiaAtividade");
		@SuppressWarnings("unchecked")
		List<Integer> listaIdeTipologiaAtividade = (List<Integer>) params.get("listaIdeTipologiaAtividade");
		
		if (!Util.isNull(tipologiaAtividade) || !Util.isNullOuVazio(listaIdeTipologiaAtividade)) {
			criteria
				.createAlias("req.requerimentoTipologiaCollection","rt", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rt.ideUnidadeMedidaTipologiaGrupo","umtg", JoinType.LEFT_OUTER_JOIN)
				.createAlias("umtg.ideTipologiaGrupo","tg", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tg.ideTipologia","tipo", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("tg.indExcluido", false))
				.add(Restrictions.eq("tipo.indExcluido", false))
			;
			
			if(!Util.isNull(tipologiaAtividade)) {
				criteria.add(Restrictions.eq("tipo.ideTipologia", tipologiaAtividade.getIdeTipologia()));
			} else{
				criteria.add(Restrictions.in("tipo.ideTipologia", listaIdeTipologiaAtividade));
			}
		}
	}
}