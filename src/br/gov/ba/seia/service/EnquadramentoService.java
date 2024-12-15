package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.controller.EnquadramentoController;
import br.gov.ba.seia.dao.EnquadramentoFinalidadeUsoAguaDaoImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.TramitacaoReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.EnquadramentoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ProcessoReenquadramentoHistAto;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.facade.ProcessoReenquadramentoHistAtoServiceFacade;
import br.gov.ba.seia.facade.ReaberturaProcessoFacade;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.service.facade.ComunicacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.service.facade.ComunicacaoServiceFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.AtoDeclaratorioUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnquadramentoService {

	@Inject
	private IDAO<Enquadramento> enquadramentoDAO;
	
	@Inject
	private IDAO<EnquadramentoDocumentoAto> enquadramentoDocumentoDAO;
	
	@Inject
	private IDAO<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalDAO;
	
	@Inject
	private IDAO<DocumentoObrigatorioInterface> documentoObrigatorioDAO;
	
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private ComunicacaoServiceFacade comunicacaoFacade;
	
	@EJB
	private OutorgaService outorgaService;
	
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	
	@EJB
	private ProcessoReenquadramentoHistAtoServiceFacade processoReenquadramentoHistAtoServiceFacade;
	
	@EJB
	private ProcessoAtoService processoAtoService;
	
	@EJB
	private ReenquadramentoProcessoService reenquadramentoProcessoService;
	
	@EJB
	private TramitacaoReenquadramentoProcessoDAOImpl tramitacaoReenquadramentoDAOImpl;
	
	@EJB
	private ReenquadramentoProcessoAtoDAOImpl reenquadramentoProcessoAtoDAOImpl;
	
	@EJB
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	
	@EJB
	private EnquadramentoFinalidadeUsoAguaDaoImpl enquadramentoFinalidadeUsoAguaDAOImpl;
	
	@EJB
	private ReaberturaProcessoFacade reaberturaProcessoFacade;
	
	@EJB
	private ComunicacaoReenquadramentoProcessoServiceFacade comunicacaoReenquadramentoProcessoServiceFacade;
	

	
	private List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Enquadramento enquadramento) {
		if (enquadramento.getIndEnquadramentoAprovado()) {
			salvarEnquadramentoCompleto(enquadramento);
			tramitacaoRequerimentoService.tramitar(enquadramento.getIdeRequerimento(), StatusRequerimentoEnum.ENQUADRADO, enquadramento.getIdePessoa());
		}
		else {
			tramitacaoRequerimentoService.tramitar(enquadramento.getIdeRequerimento(), StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO, enquadramento.getIdePessoa());	
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTramitandoSemFlush(Enquadramento enquadramento, boolean isReenquadramento)  {
		
		if (enquadramento.getIndEnquadramentoAprovado()) {
			salvarEnquadramentoCompleto(enquadramento);
		}
		
		if(isReenquadramento){

		
			tramitarReenquadramento(enquadramento);
		} 
		else {
			tramitarRequerimento(enquadramento);
		}
		
	}

	private void salvarEnquadramentoCompleto(Enquadramento enquadramento) {
		salvarOuAtualizarEnquadramento(enquadramento);
		gerarPkEnquadramentoAmbiental(enquadramento);
		gerarPkEnquadramentoDocumentoAto(enquadramento);
		salvarEnquadramentoAtoAmbiental(enquadramento.getEnquadramentoAtoAmbientalCollection());
		salvarEnquadramentoDocumentoAto(enquadramento.getEnquadramentoDocumentoAtoCollection());
	}
	
	private void tramitarRequerimento(Enquadramento enquadramento)  {
		if(enquadramento.getIndEnquadramentoAprovado()) {
			tramitacaoRequerimentoService.tramitarSemFlush(enquadramento.getIdeRequerimento(), StatusRequerimentoEnum.ENQUADRADO, enquadramento.getIdePessoa());
		}
		else {
			tramitacaoRequerimentoService.tramitarSemFlush(enquadramento.getIdeRequerimento(), StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO, enquadramento.getIdePessoa());
		}
	}

	private void tramitarReenquadramento(Enquadramento enquadramento)  {
		StatusReenquadramentoEnum statusReenquadramentoEnum = null;
		if(enquadramento.getIndEnquadramentoAprovado()) {
			if(isTramitarValidado(enquadramento)){
				statusReenquadramentoEnum = StatusReenquadramentoEnum.VALIDADO;
			} 
			else {
				statusReenquadramentoEnum = StatusReenquadramentoEnum.AGUARDANDO_ENVIO_DOCUMENTACAO;
			}
		}
		else {
			statusReenquadramentoEnum = StatusReenquadramentoEnum.PENDENCIA_DE_REENQUADRAMENTO;
		}
		
		enquadramento.getProcessoReenquadramentoDTO().setStatusTramitado(statusReenquadramentoEnum);
		
		reaberturaProcessoFacade
				.atualizarTramitacaoAnterior(enquadramento.getIdeProcessoReenquadramento().getIdeProcesso().getId());
		reaberturaProcessoFacade.salvarControleTramitacao(
				enquadramento.getIdeProcessoReenquadramento().getIdeProcesso().getId(), true, null, null,
				StatusFluxoEnum.REENQUADRAMENTO_EM_TRAMITE.getStatus(), AreaEnum.DIRRE.getId());
		tramitacaoReenquadramentoDAOImpl.salvar(new TramitacaoReenquadramentoProcesso(enquadramento.getIdeProcessoReenquadramento(), statusReenquadramentoEnum));
	}

	private boolean isTramitarValidado(Enquadramento enquadramento) {
		if(!Util.isNullOuVazio(enquadramento.getEnquadramentoAtoAmbientalCollection())) {
			for (EnquadramentoAtoAmbiental eaa : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
				for (DocumentoAto da : eaa.getListaDocumentosAtos()) {
					if(da.isCheckedReenquadramento()) {
						return false;
					}
				}
			}
		}
		return true;
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComunicacao(Enquadramento enquadramento)  {
		Object obj = null;
		Map<String, String> mapEmail = new HashMap<String, String>(); 
		if(!Util.isNull(enquadramento.getIdeRequerimento())){
			obj = enquadramento.getIdeRequerimento();
			mapEmail.put("assunto", "SEIA - Comunicado - Requerimento número " + enquadramento.getIdeRequerimento().getNumRequerimento() + "");
		} 
		else if(!Util.isNull(enquadramento.getIdeProcessoReenquadramento())){
			obj = enquadramento.getIdeProcessoReenquadramento();
			mapEmail.put("assunto", "SEIA - Comunicado - Processo número " + enquadramento.getIdeProcessoReenquadramento().getIdeProcesso().getNumProcesso() + "");
		}
		mapEmail.put("mensagem", getTextoEmail(enquadramento));
		this.comunicacaoFacade.gerarComunicacao(obj, mapEmail);
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarEnquadramento(Enquadramento enquadramento) {
		enquadramentoDAO.salvarOuAtualizar(enquadramento);
	}

	private void gerarPkEnquadramentoAmbiental(Enquadramento enquadramento) {
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
			enquadramentoAtoAmbiental.setEnquadramento(enquadramento);
		}
	}

	private void gerarPkEnquadramentoDocumentoAto(Enquadramento enquadramento) {

		if(!Util.isNullOuVazio(enquadramento.getEnquadramentoDocumentoAtoCollection())){

			for (EnquadramentoDocumentoAto enquadramentoDocumentoAto : enquadramento.getEnquadramentoDocumentoAtoCollection()) {
				enquadramentoDocumentoAto.gerarPK();
			}

			return;
		}

		enquadramento.setEnquadramentoDocumentoAtoCollection(new ArrayList<EnquadramentoDocumentoAto>());
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramento.getEnquadramentoAtoAmbientalCollection()) {

			if(Util.isNullOuVazio(enquadramentoAtoAmbiental.getListaDocumentosAtos())){
				continue;
			}

			for (DocumentoAto documentoAto : enquadramentoAtoAmbiental.getListaDocumentosAtos()) {
				if ((Util.isNull(enquadramento.getIdeProcessoReenquadramento()) && documentoAto.isChecked()) 
						|| (!Util.isNull(enquadramento.getIdeProcessoReenquadramento()) && documentoAto.isCheckedReenquadramento())) {
					EnquadramentoDocumentoAto enquadramentoDocumentoAto = new EnquadramentoDocumentoAto(enquadramento, documentoAto);
					enquadramentoDocumentoAto.gerarPK();
					enquadramento.getEnquadramentoDocumentoAtoCollection().add(enquadramentoDocumentoAto);
				} 
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnquadramentoAtoAmbiental(Collection<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalCollection) {
		
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoAtoAmbientalCollection) {

			
			if(enquadramentoAtoAmbiental.getAtoAmbiental().isOutorga() || enquadramentoAtoAmbiental.getAtoAmbiental().isOutorgaPreventiva()){

				enquadramentoAtoAmbiental.setEnquadramentoFinalidadeUsoAguaCollection(new ArrayList<EnquadramentoFinalidadeUsoAgua>());
				
				if (!Util.isNullOuVazio(enquadramentoAtoAmbiental.getEnquadramento().getIdeRequerimento())){
				
					List<Outorga> listOutorga = outorgaService.getOutorgaByIdeRequerimentoByNovaOutorga(enquadramentoAtoAmbiental.getEnquadramento().getIdeRequerimento());
	
					for (Outorga outorga : listOutorga) {
	
						if (outorga.getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.INTERVENCAO.getIdModalidade())&&
								enquadramentoAtoAmbiental.getTipologia().getIdeTipologia().equals(305) ) {
							
							addEnquadramentoAtoAmbietal(enquadramentoAtoAmbiental, null, outorga);
						}
						else if(outorga.getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES.getIdModalidade())&&
								enquadramentoAtoAmbiental.getTipologia().getIdeTipologia().equals(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES.getIdTipologia())){
							
							addEnquadramentoAtoAmbietal(enquadramentoAtoAmbiental, null, outorga);
							
						} else if(outorga.getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.CAPTACAO_SUBTERRANEA.getIdModalidade())&&
								enquadramentoAtoAmbiental.getTipologia().getIdeTipologia().equals(ModalidadeOutorgaEnum.CAPTACAO_SUBTERRANEA.getIdTipologia())){
	
							addEnquadramentoAtoAmbietal(enquadramentoAtoAmbiental, enquadramentoAtoAmbiental.getTipologia(), outorga);
							
						} else if(outorga.getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.CAPTACAO_SUPERFICIAL.getIdModalidade())&&
								enquadramentoAtoAmbiental.getTipologia().getIdeTipologia().equals(ModalidadeOutorgaEnum.CAPTACAO_SUPERFICIAL.getIdTipologia())){
	
							addEnquadramentoAtoAmbietal(enquadramentoAtoAmbiental, enquadramentoAtoAmbiental.getTipologia(), outorga);
						}
					}
				}
			} 
			else if(enquadramentoAtoAmbiental.getAtoAmbiental().isOutorgaPreventiva()){
				enquadramentoAtoAmbiental.setEnquadramentoFinalidadeUsoAguaCollection(new ArrayList<EnquadramentoFinalidadeUsoAgua>());
			}
			
			this.enquadramentoAtoAmbientalDAO.salvarOuAtualizar(enquadramentoAtoAmbiental);
			
			if (!Util.isNullOuVazio(enquadramentoAtoAmbiental.getProcessoReenquadramentoHistAtoTransient())){
				this.processoReenquadramentoHistAtoServiceFacade.salvarOuAtualizar(enquadramentoAtoAmbiental.getProcessoReenquadramentoHistAtoTransient());
			} 
		}
	}

	public void addEnquadramentoAtoAmbietal(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental, Tipologia tipologia, Outorga outorga) {

		List<TipoFinalidadeUsoAgua> listaFinalidades = 	tipoFinalidadeUsoAguaService.listarFinalidadesByRequerimentoByModalidadeByTipologia(outorga.getIdeRequerimento(), outorga.getIdeModalidadeOutorga(), tipologia);

		for (TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : listaFinalidades) {
			EnquadramentoFinalidadeUsoAgua enquadramentoFinalidadeUsoAgua = new EnquadramentoFinalidadeUsoAgua();

			enquadramentoFinalidadeUsoAgua.setIdeTipoFinalidadeUsoAgua(tipoFinalidadeUsoAgua);
			enquadramentoFinalidadeUsoAgua.setIdeEnquadramentoAtoAmbiental(enquadramentoAtoAmbiental);
			if(!enquadramentoAtoAmbiental.getEnquadramentoFinalidadeUsoAguaCollection().contains(enquadramentoFinalidadeUsoAgua)){
				enquadramentoAtoAmbiental.getEnquadramentoFinalidadeUsoAguaCollection().add(enquadramentoFinalidadeUsoAgua);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnquadramentoDocumentoAto(Collection<EnquadramentoDocumentoAto> enquadramentoDocumentoAtoCollection) {
		for (EnquadramentoDocumentoAto enquadramentoDocumentoAto : enquadramentoDocumentoAtoCollection) {
			this.enquadramentoDocumentoDAO.merge(enquadramentoDocumentoAto);
			this.salvarDocumentoObrigatorioParaEnvio(enquadramentoDocumentoAto);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarDocumentoObrigatorioParaEnvio(EnquadramentoDocumentoAto enquadramentoDocumentoAto) {
		Integer ideDocumentoAto = enquadramentoDocumentoAto.getDocumentoAto().getIdeDocumentoAto();
		DocumentoObrigatorio documentoObrigatorio = documentoObrigatorioService.buscarDocumentoObrigatorioPorDocumentoAto(ideDocumentoAto);
		
		if(documentoIsNotFce(documentoObrigatorio) && documentoIsNotAtoDeclaratorio(documentoObrigatorio) ) {
			DocumentoObrigatorioInterface documento; 
			Boolean isEnquadramento = Util.isNull(enquadramentoDocumentoAto.getEnquadramento().getIdeProcessoReenquadramento());
			if(isEnquadramento){
				documento = new DocumentoObrigatorioRequerimento();
				((DocumentoObrigatorioRequerimento) documento).setIdeRequerimento(enquadramentoDocumentoAto.getEnquadramento().getIdeRequerimento());
			} 
			else {
				documento = new DocumentoObrigatorioReenquadramento();
				((DocumentoObrigatorioReenquadramento) documento).setIdeProcessoReenquadramento(enquadramentoDocumentoAto.getEnquadramento().getIdeProcessoReenquadramento());
			}
			documento.setIdeDocumentoAto(enquadramentoDocumentoAto.getDocumentoAto());
			documento.setIdeEnquadramentoAtoAmbiental(enquadramentoDocumentoAto.getDocumentoAto().getEnquadramentoAtoAmbiental());
			documento.setIndDocumentoValidado(false);
			documento.setIndSigiloso(false);
			documento.setIdeDocumentoObrigatorio(documentoObrigatorio);
			this.documentoObrigatorioDAO.salvar(documento);
			
		}
		
	}
	
	private boolean documentoIsNotFce(DocumentoObrigatorio documentoObrigatorio) {
		return !FceUtil.isFce(documentoObrigatorio);
	}

	private boolean documentoIsNotAtoDeclaratorio(DocumentoObrigatorio documentoObrigatorio){
		return !AtoDeclaratorioUtil.isFormularioAtoDeclaratorio(documentoObrigatorio);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento buscarEnquadramentoPorRequerimento(Enquadramento enquadramento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class);
		criteria.add(Restrictions.eq("ideRequerimentoUnico", enquadramento.getIdeRequerimento()));
		criteria.add(Restrictions.eq("indExcluido", false));
		List<Enquadramento> listaEnquadramentos = enquadramentoDAO.listarPorCriteria(criteria);
		Enquadramento enquadramentoTemp = null;
		if(!Util.isNullOuVazio(listaEnquadramentos) && listaEnquadramentos.isEmpty()) {
			enquadramentoTemp = listaEnquadramentos.get(listaEnquadramentos.size()-1);
		}
		return enquadramentoTemp;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnquadramentoAtoAmbiental> buscarEnquadramentoAtoAmbientalByRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req");
		criteria
		.createAlias("req.enquadramentoCollection", "enq", JoinType.INNER_JOIN)
		.createAlias("enq.enquadramentoAtoAmbientalCollection", "enqAto", JoinType.INNER_JOIN)
		.setProjection(Projections.projectionList()
				.add(Projections.property("enqAto.ideEnquadramentoAtoAmbiental"),"ideEnquadramentoAtoAmbiental")
				.add(Projections.property("enqAto.atoAmbiental.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
				.add(Projections.groupProperty("enqAto.ideEnquadramentoAtoAmbiental"))
				.add(Projections.groupProperty("enqAto.atoAmbiental.ideAtoAmbiental"))
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class))
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				;
		return enquadramentoAtoAmbientalDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento buscarEnquadramentoCompletoRequerimento(RequerimentoUnico requerimentoUnico)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class)
				.createAlias("ideRequerimentoUnico", "ru", JoinType.LEFT_OUTER_JOIN)
				.createAlias("atoAmbientalCollection", "aac")
				.createAlias("documentoObrigatorioCollection", "do")
				.createAlias("do.documentoAtoCollection", "da")
				.createAlias("idePessoa", "p", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ru.requerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoPessoaCollection", "reqP");
		criteria.add(Restrictions.eq("ru.ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico()));
		criteria.add(Restrictions.eq("indEnquadramentoAprovado", Boolean.TRUE));
		criteria.add(Restrictions.eqProperty("do.ideDocumentoObrigatorio", "da.documentoObrigatorio.ideDocumentoObrigatorio"));
		Enquadramento enqTemp = new Enquadramento();
		enqTemp = enquadramentoDAO.buscarPorCriteria(criteria);
		if(!Util.isNullOuVazio(enqTemp)){
			if(!Hibernate.isInitialized(enqTemp.getAtoAmbientalCollection())) {
				Hibernate.initialize(enqTemp.getAtoAmbientalCollection());
			}
			if(!Hibernate.isInitialized(enqTemp.getDocumentoObrigatorioCollection())) {
				Hibernate.initialize(enqTemp.getDocumentoObrigatorioCollection());
			}
		}
		return enqTemp;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento buscarEnquadramentoComAtos(RequerimentoUnico requerimentoUnico)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class)
				.createAlias("ideRequerimentoUnico", "ru", JoinType.LEFT_OUTER_JOIN)
				.createAlias("atoAmbientalCollection", "aac");
		criteria.add(Restrictions.eq("ru.ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico()));
		criteria.add(Restrictions.eq("indEnquadramentoAprovado", Boolean.TRUE));
		Enquadramento enqTemp = enquadramentoDAO.buscarPorCriteria(criteria);
		if(!Util.isNullOuVazio(enqTemp)){
			if(!Hibernate.isInitialized(enqTemp.getAtoAmbientalCollection())) {
				Hibernate.initialize(enqTemp.getAtoAmbientalCollection());
			}
		}
		return enqTemp;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento buscarEnquadramentoInicializadoPeloUsuario(Enquadramento enquadramento) throws Exception {
		
		StatusRequerimento statusRequerimento = statusRequerimentoService.getMaxStatusTramitacaoRequerimantoPorData(enquadramento);
		StatusRequerimento[] statusRequerimentos = {statusRequerimento};
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class, "enq")
				.createAlias("ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.tramitacaoRequerimentoCollection", "tramReq")
				
				.add(Restrictions.eq("enq.ideEnquadramento", buscarUltimoEnquadramentoRequerimento(enquadramento.getIdeRequerimento()).getIdeEnquadramento()))
				.add(Restrictions.eq("enq.ideRequerimento.ideRequerimento", enquadramento.getIdeRequerimento().getIdeRequerimento()))
				.add(Restrictions.eq("enq.idePessoa", enquadramento.getIdePessoa()))
				.add(Restrictions.or(
								Restrictions.eq("enq.indEnquadramentoAprovado", Boolean.FALSE),
								Restrictions.in("tramReq.ideStatusRequerimento", statusRequerimentos)
							)
				);
		
		List<Enquadramento> enquadramentos = enquadramentoDAO.listarPorCriteria(criteria);
		return (!enquadramentos.isEmpty() ? enquadramentos.get(enquadramentos.size() - 1) : null);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento buscarUltimoEnquadramentoRequerimento(Requerimento pRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class);
		criteria
		.add(Restrictions.eq("ideRequerimento.ideRequerimento", pRequerimento.getIdeRequerimento()))
		.setFetchMode("ideRequerimento", FetchMode.JOIN)
		.addOrder(Order.asc("ideEnquadramento"));
		List<Enquadramento> listaEnquadramentos = enquadramentoDAO.listarPorCriteria(criteria);
		Enquadramento enquadramento = null;
		if(listaEnquadramentos.size() > 0) {
			enquadramento = listaEnquadramentos.get(listaEnquadramentos.size()-1);
		}
		if (!Util.isNull(enquadramento)) {
			Hibernate.initialize(enquadramento.getAtoAmbientalCollection());
		}
		return enquadramento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento carregar(Enquadramento pEnquadramento) {
		Enquadramento enquadramento = enquadramentoDAO.carregarGet(pEnquadramento.getIdeEnquadramento());
		if(!Util.isNullOuVazio(enquadramento.getAtoAmbientalCollection())) {
			Hibernate.initialize(enquadramento.getAtoAmbientalCollection());
		}
		return enquadramento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Enquadramento> carregarEnquadramentosByAtoAmbiental(Integer ideAtoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class)
				.createAlias("atoAmbientalCollection", "ato");
		criteria.add(Restrictions.eq("ato.ideAtoAmbiental",ideAtoAmbiental));
		return enquadramentoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Enquadramento> buscarEnquadramentoPorRequerimentoUnico(Requerimento requerimento) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", requerimento.getIdeRequerimento());
		return enquadramentoDAO.buscarPorNamedQuery("Enquadramento.findByIdeRequerimento", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void iniciarEnquadramento(Requerimento requerimento,Pessoa pessoa) throws Exception {
		TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(requerimento.getIdeRequerimento());
		if (tramitacaoRequerimento.getIdeStatusRequerimento().getIdeStatusRequerimento().compareTo(StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus()) < 0) {
			this.tramitacaoRequerimentoService.tramitarSemFlush(requerimento, StatusRequerimentoEnum.SENDO_ENQUADRADO,pessoa);
			if(isRequerimentoFoiTramitadoPorOutroUsuario(requerimento, tramitacaoRequerimento)) {
				throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado",requerimento.getNumRequerimento()));
			}
		}
		else{
			if(!tramitacaoRequerimento.getIdePessoa().equals(pessoa)) {
				throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado",requerimento.getNumRequerimento()));
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isRequerimentoFoiTramitadoPorOutroUsuario(Requerimento requerimento, TramitacaoRequerimento tramitacaoRequerimento) throws Exception {
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(requerimento.getIdeRequerimento(), tramitacaoRequerimento);
	}
	
	private String getTextoEmail(Enquadramento enquadramento) {
		
		if(enquadramento.getIndInexigibilidadeIsenta()){
			return getTextoEmailInexibilidadeIsento(enquadramento);
		}
		else if (!enquadramento.getIndEnquadramentoAprovado()) {
			return enquadramento.getDscJustificativa();
		} 
		else {
			
			if(!Util.isNull(enquadramento.getProcessoReenquadramentoDTO()) && StatusReenquadramentoEnum.VALIDADO.equals(enquadramento.getProcessoReenquadramentoDTO().getStatusTramitado())) {
				return comunicacaoReenquadramentoProcessoServiceFacade.gerarEmailDocumentoValidado(enquadramento.getProcessoReenquadramentoDTO()); 
			}
			
			StringBuilder buffer = new StringBuilder();
			boolean isInexigibilidade = false;
			boolean isCRF = false;
			
			List<EnquadramentoAtoAmbiental> enquadamentoAtos = (List<EnquadramentoAtoAmbiental>) enquadramento.getEnquadramentoAtoAmbientalCollection();
			
			if(enquadamentoAtos.size() == 1){
				if(enquadamentoAtos.get(0).getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.CRF.getId())){
					isCRF = true;
				}
			}
			
			if(!isCRF){
				buffer.append("Prezado(a),\n\n\n ");
				if(!Util.isNull(enquadramento.getIdeRequerimento())){
					buffer.append("O Requerimento de n° " + enquadramento.getIdeRequerimento().getNumRequerimento()
							+ "  foi enquadrado com sucesso.\n");
					
					buffer.append("Para regularização do empreendimento, os seguintes atos são necessários:\n");
					
					enquadramento.setEnquadramentoAtoAmbientalCollection(enquadramentoAtoAmbientalDAO.listarPorCriteria(getCriteriaCarregarEnquadramentoAtoAmbiental(enquadramento)));
					List<EnquadramentoAtoAmbiental> atos = (List<EnquadramentoAtoAmbiental>) enquadramento.getEnquadramentoAtoAmbientalCollection();
					
					for (int i = 0; i < atos.size(); i++) {
						buffer.append(atos.get(i).getAtoAmbiental().getNomAtoAmbiental() + (i < (atos.size() - 1) ? ", " : "."));
						if(atos.get(i).getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.INEXIGIBILIDADE.getId())){
							isInexigibilidade = true;
						}
					}
				} 
				else if(!Util.isNull(enquadramento.getIdeProcessoReenquadramento())){
				buffer.append("Foi iniciado o processo de reenquadramento do processo de n°"
							+ enquadramento.getIdeProcessoReenquadramento().getIdeProcesso().getNumProcesso());
				}

				buffer.append("\n\n");
				if(!isInexigibilidade){
					
					buffer.append("Para dar continuidade, os seguintes documentos são obrigatórios:\n\n");
					
					
					for (EnquadramentoDocumentoAto documentoAto : enquadramento.getEnquadramentoDocumentoAtoCollection()) {
						if(!Util.isNullOuVazio(documentoAto.getDocumentoAto().getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio())) {
							//for (DocumentoAto documentoAto : enquadramentoAto.getListaDocumentosAtos()) {
								//if(documentoAto.isCheckedReenquadramento()) {
									buffer.append("\t\u25CF " + documentoAto.getDocumentoAto().getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio()+ "\n\n");
								//}
							//}
						}
					} 
					
					if(!Util.isNull(enquadramento.getIdeProcessoReenquadramento())){
						buffer.append("Para enviá-los, acesse o SEIA, no menu lateral à esquerda acione a aba "+"\"PROCESSO"+"\"  em seguida "+"\"REENQUADRAMENTO"+"\" e siga os seguintes passos: \n\n");
						buffer.append(" Passo 1: Informe o número do processo mencionado acima no campo "+"\"Nº do processo:"+"\" e acione o botão "+"\"Consultar"+"\";\n\n");
						buffer.append(" Passo 2: Na coluna "+"\"Ações"+"\" acione o ícone  "+"\"Enviar documentação obrigatória"+"\";\n\n");
						buffer.append(" Passo 3: Na tela "+"\"ENVIAR DOCUMENTO OBRIGATÓRIO"+"\" preencha o Formulário de Caracterização do Empreendimento - FCE, caso tenho sido solicitado;\n\n");
						buffer.append(" Passo 4: Na coluna "+"\"Ações"+"\" acione o ícone "+"\"Upload de arquivo"+"\" e carregue o arquivo correspondente ao documento solicitado para cada ato ambiental;\n\n" );
						buffer.append(" Passo 5: Acione o botão "+"\"Salvar"+"\" e aguarde novas orientações.\n\n");
					}
					
					
				}
				
			}else{
				buffer.append("Prezado(a),\n ");
				if(!Util.isNull(enquadramento.getIdeRequerimento())){
					buffer.append("O Requerimento de n° " + enquadramento.getIdeRequerimento().getNumRequerimento() + " foi recebido com sucesso.");
					
					buffer.append("\n Para dar continuidade acesse o SEIA e efetue o envio da documentação obrigatória, a qual será avaliada pela Central de Atendimento - ATEND. "
							+ "Após a validação você receberá por email novas orientações para geração do Documento Arrecadação Estadual - DAE");
					
					enquadramento.setEnquadramentoAtoAmbientalCollection(enquadramentoAtoAmbientalDAO.listarPorCriteria(getCriteriaCarregarEnquadramentoAtoAmbiental(enquadramento)));
					

					
			}
			}
			
			buffer.append("\n");
			
			if(!Util.isNull(enquadramento.getIdeRequerimento())) {
				TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(enquadramento.getIdeRequerimento().getIdeRequerimento());
	
				if(tramitacaoRequerimento.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus() ||
						tramitacaoRequerimento.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.ENQUADRADO.getStatus()) {
					buffer.append("ATENÇÃO! De acordo com a Portaria INEMA 21.605/2020, os requerimentos abertos no sistema SEIA deverão formar seus respectivos processos em um prazo máximo de 120 dias. Caso esse prazo não seja atendido, os requerimentos serão cancelados, o que se aplica também a requerimentos de renovação de licença e/ou outorga.");
					buffer.append("\n");
				}
			}
			
			buffer.append("\n Atenciosamente,");
			buffer.append("\n Central de Atendimento/INEMA");
			
			return buffer.toString();
		}
	}
	
	
	
	private String getTextoEmailInexibilidadeIsento(Enquadramento enquadramento)  {
		TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(enquadramento.getIdeRequerimento().getIdeRequerimento());
		StringBuilder buffer = new StringBuilder();
		buffer.append("Prezado(a),\n ");
		Requerimento requerimento = enquadramento.getIdeRequerimento();
		buffer.append("O Requerimento de n° " + requerimento.getNumRequerimento()
				+ "  foi enquadrado com sucesso.\n");
		
		buffer.append("Favor acessar o SEIA para emitir a sua declaração de Inexigibilidade.");
		if(tramitacaoRequerimento.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus() ||
				tramitacaoRequerimento.getIdeStatusRequerimento().getIdeStatusRequerimento() == StatusRequerimentoEnum.ENQUADRADO.getStatus()) {
			buffer.append("ATENÇÃO! De acordo com a Portaria INEMA 21.605/2020, os requerimentos abertos no sistema SEIA deverão formar seus respectivos processos em um prazo máximo de 120 dias. Caso esse prazo não seja atendido, os requerimentos serão cancelados, o que se aplica também a requerimentos de renovação de licença e/ou outorga.");
			buffer.append("\n");
		}
		buffer.append("\n");
		buffer.append("\n Atenciosamente,");
		buffer.append("\n Central de Atendimento/INEMA");
		return buffer.toString();
	}

	private DetachedCriteria getCriteriaCarregarEnquadramentoAtoAmbiental(Enquadramento enquadramento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class);
		
		criteria
			.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
			.createAlias("enquadramento", "enq", JoinType.INNER_JOIN)
			.createAlias("tipologia", "tip", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("enq.ideEnquadramento", enquadramento.getIdeEnquadramento()))
			.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideEnquadramentoAtoAmbiental"),"ideEnquadramentoAtoAmbiental")
						.add(Projections.property("tip.ideTipologia"),"tipologia.ideTipologia")
						.add(Projections.property("tip.desTipologia"),"tipologia.desTipologia")
						.add(Projections.property("enq.ideEnquadramento"),"enquadramento.ideEnquadramento")
						.add(Projections.property("ato.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
						.add(Projections.property("ato.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
				)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class));
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnquadramentoAtoAmbiental> listarEnquadramentoAtoAmbientalByEnquadramento (Enquadramento enquadramento)  {
		return enquadramentoAtoAmbientalDAO.listarPorCriteria(getCriteriaCarregarEnquadramentoAtoAmbiental(enquadramento));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<EnquadramentoDocumentoAto> listarAtosEnquadradosByRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoDocumentoAto.class)
				.createAlias("documentoAto", "da")
				.createAlias("da.ideAtoAmbiental", "ato")
				.createAlias("da.ideDocumentoObrigatorio", "doc")
				.createAlias("da.ideTipologia", "tipologia",JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipologia.tipologiaGrupo", "tg",JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tg.indExcluido", false))
				.createAlias("enquadramento", "enquadramento")
				.createAlias("enquadramento.ideRequerimento", "req")

				
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));

		return enquadramentoDocumentoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<EnquadramentoAtoAmbiental> listarEnquadramentoAtoByRequerimento(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class,"eaac")
			.createAlias("eaac.enquadramento", "enq")
			.createAlias("enq.ideRequerimento", "req")
			.createAlias("eaac.atoAmbiental", "aa")
			.createAlias("aa.ideTipoAto", "ta")
			.createAlias("eaac.tipologia", "ti",JoinType.LEFT_OUTER_JOIN)

			.setProjection(Projections.projectionList()
				.add(Projections.property("eaac.ideEnquadramentoAtoAmbiental"),"ideEnquadramentoAtoAmbiental")

				.add(Projections.property("aa.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("aa.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("aa.sglAtoAmbiental"),"atoAmbiental.sglAtoAmbiental")
				.add(Projections.property("aa.indDeclaratorio"),"atoAmbiental.indDeclaratorio")
				.add(Projections.property("aa.numDiasValidade"),"atoAmbiental.numDiasValidade")
				.add(Projections.property("aa.indAtivo"),"atoAmbiental.indAtivo")
				.add(Projections.property("aa.indVisivelSolicitacaoTla"),"atoAmbiental.indVisivelSolicitacaoTla")
				
				.add(Projections.property("ta.ideTipoAto"),"atoAmbiental.ideTipoAto.ideTipoAto")
				.add(Projections.property("ta.nomTipoAto"),"atoAmbiental.ideTipoAto.nomTipoAto")
				
				.add(Projections.property("ti.ideTipologia"),"tipologia.ideTipologia")
				.add(Projections.property("ti.desTipologia"),"tipologia.desTipologia")
				
				.add(Projections.property("enq.ideEnquadramento"),"enquadramento.ideEnquadramento")

			).setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class))

			.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));

		Collection<EnquadramentoAtoAmbiental> lista = this.enquadramentoAtoAmbientalDAO.listarPorCriteria(criteria, Order.desc("aa.ideAtoAmbiental"));
		
		ProcessoReenquadramento processoReenquadramento = processoReenquadramentoDAOImpl.processoReenquadramentoPorRequerimento(new Requerimento(ideRequerimento));
		
		if (!Util.isNull(processoReenquadramento)){
			Collection<EnquadramentoAtoAmbiental> listaReenquadramento 
				= listarEnquadramentoAtoByProcessoReenquadramento(processoReenquadramento.getIdeProcesso().getIdeProcesso(), processoReenquadramento.getIdeProcessoReenquadramento());
			
			lista.addAll(listaReenquadramento);
		}
		
		for(EnquadramentoAtoAmbiental eaa : lista){
			eaa.setEnquadramentoFinalidadeUsoAguaCollection(
					enquadramentoFinalidadeUsoAguaDAOImpl.listarPorEnquadramentoAtoAmbiental(eaa.getIdeEnquadramentoAtoAmbiental()));
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<EnquadramentoAtoAmbiental> listarEnquadramentoAtoByProcessoReenquadramento(Integer ideProcesso, Integer ideProcessoReenquadramento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class,"eaac")
				.createAlias("eaac.enquadramento", "enq")
				.createAlias("enq.ideProcessoReenquadramento", "prr")
				.createAlias("prr.ideProcesso", "pro")
				.createAlias("pro.ideRequerimento", "req")
				.createAlias("eaac.atoAmbiental", "aa")
				.createAlias("aa.ideTipoAto", "ta")
				.createAlias("eaac.tipologia", "ti",JoinType.LEFT_OUTER_JOIN)

				.setProjection(Projections.projectionList()
						.add(Projections.property("eaac.ideEnquadramentoAtoAmbiental"),"ideEnquadramentoAtoAmbiental")
						.add(Projections.property("aa.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
						.add(Projections.property("aa.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
						.add(Projections.property("aa.sglAtoAmbiental"),"atoAmbiental.sglAtoAmbiental")
						.add(Projections.property("aa.indDeclaratorio"),"atoAmbiental.indDeclaratorio")
						.add(Projections.property("aa.numDiasValidade"),"atoAmbiental.numDiasValidade")
						.add(Projections.property("aa.indAtivo"),"atoAmbiental.indAtivo")
						.add(Projections.property("aa.indVisivelSolicitacaoTla"),"atoAmbiental.indVisivelSolicitacaoTla")
						.add(Projections.property("ta.ideTipoAto"),"atoAmbiental.ideTipoAto.ideTipoAto")
						.add(Projections.property("ta.nomTipoAto"),"atoAmbiental.ideTipoAto.nomTipoAto")
						.add(Projections.property("ti.ideTipologia"),"tipologia.ideTipologia")
						.add(Projections.property("ti.desTipologia"),"tipologia.desTipologia")

						).setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class))

						.add(Restrictions.eq("pro.ideProcesso", ideProcesso))
						.add(Restrictions.le("prr.ideProcessoReenquadramento", ideProcessoReenquadramento));

		return this.enquadramentoAtoAmbientalDAO.listarPorCriteria(criteria, Order.desc("aa.ideAtoAmbiental"));
	}
	
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnquadramentoDocumentoAto buscarEnquadramentoDocumentoAtoByIdeEnquadramentoAndIdeDocumento(Integer ideEnquadramento, Integer ideDocumentoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoDocumentoAto.class);
		criteria.add(Restrictions.eq("enquadramento.ideEnquadramento", ideEnquadramento));
		criteria.add(Restrictions.eq("documentoAto.ideDocumentoAto", ideDocumentoAto));
		return enquadramentoDocumentoDAO.buscarPorCriteria(criteria);
	}

	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEnquadramentoDocumentoAto(Integer ideEnquadramento, Integer ideDocumentoAto) {
		EnquadramentoDocumentoAto enquadramentoDocumentoAto = buscarEnquadramentoDocumentoAtoByIdeEnquadramentoAndIdeDocumento(ideEnquadramento, ideDocumentoAto);
		enquadramentoDocumentoDAO.remover(enquadramentoDocumentoAto);
	}

	public Collection<EnquadramentoAtoAmbiental> buscarTipologiasDoEnquadramento(Requerimento ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class)
			.createAlias("enquadramento", "enquadramento", JoinType.INNER_JOIN)
			.createAlias("tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("enquadramento.ideRequerimento", ideRequerimento))
			.setProjection(
					Projections.projectionList()
					.add(Projections.property("ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental")
					.add(Projections.property("tipologia.ideTipologia"), "tipologia.ideTipologia")
					.add(Projections.property("tipologia.desTipologia"), "tipologia.desTipologia")
					.add(Projections.property("enquadramento.ideEnquadramento"), "enquadramento.ideEnquadramento")
					.add(Projections.property("atoAmbiental.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class))
			;
		return enquadramentoAtoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento carregarEnquadramentoBy(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class);
		Integer ideRequerimento = requerimento.getIdeRequerimento();
		criteria
			.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideEnquadramento"),"ideEnquadramento")
					.add(Projections.property("indEnquadramentoAprovado"),"indEnquadramentoAprovado")
					.add(Projections.property("dscJustificativa"),"dscJustificativa")
					.add(Projections.property("indPassivelEiarima"),"indPassivelEiarima")
					.add(Projections.property("dscCaminhoArquivoRima"),"dscCaminhoArquivoRima")
					.add(Projections.property("idePessoa.idePessoa"),"idePessoa.idePessoa")
					.add(Projections.property("ideRequerimento.ideRequerimento"),"ideRequerimento.ideRequerimento")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Enquadramento.class));
		
		Enquadramento enq = enquadramentoDAO.buscarPorCriteria(criteria);
		enq.setEnquadramentoAtoAmbientalCollection(listarEnquadramentoAtoByRequerimento(ideRequerimento));
		return enq;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Enquadramento buscarUltimoEnquadramentoPorProcessoReenquadramento(ProcessoReenquadramento processoReenquadramento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class);
		criteria
		.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideEnquadramento"),"ideEnquadramento")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Enquadramento.class))
		
		.add(Restrictions.eq("ideProcessoReenquadramento.ideProcessoReenquadramento", processoReenquadramento.getIdeProcessoReenquadramento()))
		.addOrder(Order.asc("ideEnquadramento"));
		List<Enquadramento> listaEnquadramentos = enquadramentoDAO.listarPorCriteria(criteria);
		Enquadramento enquadramento = null;
		if(!listaEnquadramentos.isEmpty()) {
			enquadramento = listaEnquadramentos.get(listaEnquadramentos.size()-1);
		}

		return enquadramento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReenquadramentoProcessoAto isAlteracaoTipoAto(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental, Integer ideProcesso) throws Exception{
		
		EnquadramentoAtoAmbiental enquadramentoAtoAmbientalVerificacao = enquadramentoAtoAmbiental.clone();
		if (!Util.isNullOuVazio(enquadramentoAtoAmbiental.getProcessoReenquadramentoHistAtoTransient()) && !Util.isNullOuVazio(enquadramentoAtoAmbiental.getProcessoReenquadramentoHistAtoTransient().getIdeAtoAmbientalOriginal())){
			enquadramentoAtoAmbientalVerificacao.setAtoAmbiental(enquadramentoAtoAmbiental.getProcessoReenquadramentoHistAtoTransient().getIdeAtoAmbientalOriginal());
			enquadramentoAtoAmbientalVerificacao.setTipologia(enquadramentoAtoAmbiental.getProcessoReenquadramentoHistAtoTransient().getIdeTipologiaOriginal());
		}
		
		List<Object[]> items = processoAtoService.listarFinalidadeReenquadramentoProcessoPorProceso(ideProcesso);
		
		for (Object[] objects : items) {
			Integer ideReenquadramentoProcesso = (Integer)objects[4];
			
			Collection<ReenquadramentoProcessoAto> listaReenquadramentoProcessoAto = reenquadramentoProcessoService.listarReenquadramentoProcessoAto(new ReenquadramentoProcesso(ideReenquadramentoProcesso));
			
			for (ReenquadramentoProcessoAto reenquadramentoProcessoAto : listaReenquadramentoProcessoAto) {
				
				if (!Util.isNullOuVazio(reenquadramentoProcessoAto.getIdeProcessoAto())){
					
					
					if (!Util.isNullOuVazio(reenquadramentoProcessoAto.getIdeProcessoAto())){
						if (!Util.isNullOuVazio(reenquadramentoProcessoAto.getIdeProcessoAto().getTipologia())){
							if (reenquadramentoProcessoAto.getIdeProcessoAto().getTipologia().equals(enquadramentoAtoAmbientalVerificacao.getTipologia()) &&
								reenquadramentoProcessoAto.getIdeProcessoAto().getAtoAmbiental().equals(enquadramentoAtoAmbientalVerificacao.getAtoAmbiental())){
								return reenquadramentoProcessoAto;
							}
						} else {
							
							if (Util.isNullOuVazio(enquadramentoAtoAmbientalVerificacao.getTipologia())){
								if (reenquadramentoProcessoAto.getIdeProcessoAto().getAtoAmbiental().equals(enquadramentoAtoAmbientalVerificacao.getAtoAmbiental())){
									return reenquadramentoProcessoAto;
								}
							}
						}
					}
				}
			}
		}
		
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void selecionarAtoAmbiental(EnquadramentoController enquadramentoController,	AtoAmbiental atoAmbiental)  {
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoController.getEnquadramentoAtoAmbientalCollection()) {
			if (enquadramentoAtoAmbiental.equals(enquadramentoController.getEnquadramentoAtoAmbientalSelecionado())){
				ProcessoReenquadramentoHistAto processoReenquadramentoHistAto = new ProcessoReenquadramentoHistAto();
				processoReenquadramentoHistAto.setIdeProcessoReenquadramento(enquadramentoController.getProcessoReenquadramentoDTO().getProcessoReenquadramento());
				processoReenquadramentoHistAto.setDtcReenquadramento(new Date());
				processoReenquadramentoHistAto.setIdeAtoAmbientalOriginal(enquadramentoAtoAmbiental.getAtoAmbiental());
				processoReenquadramentoHistAto.setIdeTipologiaOriginal(enquadramentoAtoAmbiental.getTipologia());
				processoReenquadramentoHistAto.setIdeEnquadramentoAtoAmbientalReenquadrado(enquadramentoAtoAmbiental);
				
				enquadramentoAtoAmbiental.setProcessoReenquadramentoHistAtoTransient(processoReenquadramentoHistAto);
				enquadramentoAtoAmbiental.setAtoAmbiental(atoAmbiental);
				enquadramentoAtoAmbiental.setTipologia(atoAmbiental.getTipologia());
				enquadramentoAtoAmbiental.setListaDocumentosAtos(null);
			}
		}
		
		Collection<EnquadramentoDocumentoAto> enqDocumentoAtoCollection = listarAtosEnquadradosByRequerimento(enquadramentoController.getEnquadramento().getIdeRequerimento().getIdeRequerimento());
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoController.getEnquadramentoAtoAmbientalCollection()) {
			enquadramentoController.carregarDocs(enquadramentoAtoAmbiental);
			for (DocumentoAto documentoAto : enquadramentoAtoAmbiental.getListaDocumentosAtos()) {
				for (EnquadramentoDocumentoAto enquadramentoDocumentoAto : enqDocumentoAtoCollection) {
					if(enquadramentoDocumentoAto.getDocumentoAto().equals(documentoAto)){
						documentoAto.setChecked(true);
						documentoAto.setEnquadramentoAtoAmbiental(enquadramentoController.getEnquadramentoAtoAmbiental());
					}
				}
			}
		}
		enquadramentoController.setEnquadramentoAtoAmbientalCollection(enquadramentoController.getEnquadramento().getEnquadramentoAtoAmbientalCollection());
		enquadramentoController.voltar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarAletaracaoDeEnquadramentoAtoAmbiental(EnquadramentoController enquadramentoController) {
		
		Processo processo = enquadramentoController.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getIdeProcesso();
		
		List<Object[]> items = processoAtoService.listarFinalidadeReenquadramentoProcessoPorProceso(processo.getIdeProcesso());
		
		Boolean alterado = false;
		
		for (Object[] objects : items) {
			String motivoReenquadramento = (String)objects[1];
			Integer ideReenquadramentoProcesso = (Integer)objects[4];
			
			if (motivoReenquadramento.equalsIgnoreCase("ALTERACAO_ATOS_AUTORIZATIVOS")){
				Collection<ReenquadramentoProcessoAto> atos = reenquadramentoProcessoAtoDAOImpl.listarReenquadramentoProcessoAto(new ReenquadramentoProcesso(ideReenquadramentoProcesso));
				
				for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoController.getEnquadramentoAtoAmbientalCollection()) {
					if (enquadramentoAtoAmbiental.isAlteracao()){
						for (ReenquadramentoProcessoAto reenquadramentoProcessoAto : atos) {
							if (!Util.isNull(reenquadramentoProcessoAto.getIdeProcessoAto())){
								if (!Util.isNull(enquadramentoAtoAmbiental.getTipologia())){
									if (enquadramentoAtoAmbiental.getAtoAmbiental().equals(reenquadramentoProcessoAto.getIdeProcessoAto().getAtoAmbiental()) &&
											enquadramentoAtoAmbiental.getTipologia().equals(reenquadramentoProcessoAto.getIdeProcessoAto().getTipologia())){
										alterado = true;
										break;
									}
								} else {
									if (enquadramentoAtoAmbiental.getAtoAmbiental().equals(reenquadramentoProcessoAto.getIdeProcessoAto().getAtoAmbiental())){
										alterado = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		return alterado;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramento obterPenultimoProcessoReenquadramentoPorProcesso(Processo ideProcesso)  {
		return processoReenquadramentoDAOImpl.obterPenultimoProcessoReenquadramentoPorProcesso(ideProcesso);
	}

	public List<DocumentoObrigatorioEnquadramentoDTO> getListaDocumentoObrigatorioEnquadramento() {
		return listaDocumentoObrigatorioEnquadramento;
	}

	public void setListaDocumentoObrigatorioEnquadramento(List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento) {
		this.listaDocumentoObrigatorioEnquadramento = listaDocumentoObrigatorioEnquadramento;
	}


}