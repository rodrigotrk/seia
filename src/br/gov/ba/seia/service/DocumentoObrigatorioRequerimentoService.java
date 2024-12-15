package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@SuppressWarnings("all")
public class DocumentoObrigatorioRequerimentoService {

	@Inject
	private IDAO<DocumentoObrigatorioRequerimento> docObrigatorioRequerimentoDAO;
	
	@Inject
	private IDAO<DocumentoObrigatorioInterface> documentoObrigatorioInterfaceDAO;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private DocumentoObrigatorioService docObrigatorioService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private RequerimentoService requerimentoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(DocumentoObrigatorioRequerimento doc) throws Exception {
		if (doc.getArquivoChanged() != null && doc.getArquivoChanged()) {
			String diretorio = "";
			if (doc.getFileUpload() != null) {
				diretorio = gerenciaArquivoService.uploadArquivoDocumentoObrigatorioRequerimentoExperimental(doc);
			} else {
				diretorio = gerenciaArquivoService.transferArquivoDocumentoObrigatorioRequerimentoExperimental(doc);
			}
			doc.setDscCaminhoArquivo(diretorio);
			doc.setArquivoChanged(Boolean.FALSE);
		}
		//docObrigatorioRequerimentoDAO.atualizar(doc);
		docObrigatorioRequerimentoDAO.salvarOuAtualizar(doc);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorioInterface> buscarDocsObrigatorioReqPorRequerimentoUnico(RequerimentoUnico requerimentoUnico) throws Exception {

		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class, "docObgReq");

		criteria.createAlias("docObgReq.ideRequerimento", "req", JoinType.INNER_JOIN)
		.createAlias("docObgReq.ideDocumentoAto", "da", JoinType.INNER_JOIN)
		.createAlias("da.ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
		.createAlias("da.ideAtoAmbiental", "aa", JoinType.INNER_JOIN)
		.createAlias("da.ideTipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
		.createAlias("docObgReq.idePessoaValidacao", "pessoaValidacao", JoinType.LEFT_OUTER_JOIN)
		.createAlias("docObgReq.idePessoaUpload", "pessoaUpload", JoinType.LEFT_OUTER_JOIN)
		
		.setProjection(Projections.projectionList()
				.add(Projections.property("docObgReq.ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
				.add(Projections.property("docObgReq.dscCaminhoArquivo"), "dscCaminhoArquivo")
				.add(Projections.property("docObgReq.indDocumentoValidado"), "indDocumentoValidado")
				.add(Projections.property("docObgReq.indSigiloso"), "indSigiloso")
				.add(Projections.property("docObgReq.dtcValidacao"), "dtcValidacao")
				.add(Projections.property("docObgReq.ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental")

				.add(Projections.property("pessoaUpload.idePessoa"), "idePessoaUpload.idePessoa")
				.add(Projections.property("pessoaValidacao.idePessoa"), "idePessoaValidacao.idePessoa")
				
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
				
				.add(Projections.property("da.ideDocumentoAto"), "ideDocumentoAto.ideDocumentoAto")
				.add(Projections.property("da.indAtivo"), "ideDocumentoAto.indAtivo")
				.add(Projections.property("aa.ideAtoAmbiental"), "ideDocumentoAto.ideAtoAmbiental.ideAtoAmbiental")
				
				.add(Projections.property("tipologia.ideTipologia"), "ideDocumentoAto.ideTipologia.ideTipologia")
				.add(Projections.property("tipologia.desTipologia"), "ideDocumentoAto.ideTipologia.desTipologia")
				
				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("doc.numTamanho"), "ideDocumentoAto.ideDocumentoObrigatorio.numTamanho")
				.add(Projections.property("doc.indFormulario"), "ideDocumentoAto.ideDocumentoObrigatorio.indFormulario")

				).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class))
		
		.add(Restrictions.eq("req.ideRequerimento", requerimentoUnico.getIdeRequerimentoUnico()));

		return documentoObrigatorioInterfaceDAO.listarPorCriteria(criteria, Order.asc("ideDocumentoObrigatorio.ideDocumentoObrigatorio"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorioRequerimento> buscarDocumentosObrigatoriosRequerimentoPorIdeRequerimento(Integer ideRequerimento) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class)
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento))
				.setFetchMode("ideDocumentoObrigatorio", FetchMode.JOIN);
		Collection<DocumentoObrigatorioRequerimento> docsRequerimento = docObrigatorioRequerimentoDAO.listarPorCriteria(criteria, Order.asc("ideDocumentoObrigatorio.ideDocumentoObrigatorio"));
		for (DocumentoObrigatorioRequerimento dor : docsRequerimento) {
			Hibernate.initialize(dor.getIdePessoaUpload());
			Hibernate.initialize(dor.getIdeDocumentoAto());
		}
		return docsRequerimento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorioRequerimento> buscarDocumentosDeFormacaoPublicos(Integer ideRequerimento) throws Exception {
		DetachedCriteria criteria = criteriaDocumentoFormacao(ideRequerimento)
				//.add(Restrictions.eq("doc.indPublico",true))
				//.add(Restrictions.eq("this.indSigiloso",false));
				.add(Restrictions.or(Restrictions.eq("doc.indPublico", true), Restrictions.isNull("doc.indPublico")))
				.add(Restrictions.or(Restrictions.eq("this.indSigiloso", false), Restrictions.isNull("this.indSigiloso")));
		return docObrigatorioRequerimentoDAO.listarPorCriteria(criteria, Order.asc("doc.ideDocumentoObrigatorio"));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorioRequerimento> buscarDocumentosObrigatoriosRequerimentoEnquadramentoByIdeRequerimento(Integer ideRequerimento) throws Exception {
		DetachedCriteria criteria = criteriaDocumentoFormacao(ideRequerimento);
		return docObrigatorioRequerimentoDAO.listarPorCriteria(criteria, Order.asc("doc.ideDocumentoObrigatorio"));
	}

	private DetachedCriteria criteriaDocumentoFormacao(Integer ideRequerimento) {

		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class)
				.createAlias("ideRequerimento", "requerimento")
				.createAlias("requerimento.requerimentoUnico", "unico",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaUpload", "pessoa", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao", "idePessoaValidacao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideDocumentoObrigatorio", "doc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideDocumentoAto", "da", JoinType.LEFT_OUTER_JOIN)
				.createAlias("da.ideAtoAmbiental", "aa", JoinType.INNER_JOIN)
				.createAlias("da.ideDocumentoObrigatorio", "docObgDocAto", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
				.add(Projections.property("dscCaminhoArquivo"), "dscCaminhoArquivo").add(Projections.property("indDocumentoValidado"), "indDocumentoValidado")
				.add(Projections.property("dtcValidacao"), "dtcValidacao")
				.add(Projections.property("ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
				.add(Projections.property("pessoa.idePessoa"), "idePessoaUpload.idePessoa")
				.add(Projections.property("idePessoaValidacao.idePessoa"), "idePessoaValidacao.idePessoa")
				.add(Projections.property("idePessoaValidacao.pessoaFisica.idePessoaFisica"), "idePessoaValidacao.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pessoaFisica.nomPessoa"), "idePessoaValidacao.pessoaFisica.nomPessoa")
				.add(Projections.property("requerimento.ideRequerimento"), "ideRequerimento.ideRequerimento")

				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("doc.numTamanho"), "ideDocumentoObrigatorio.numTamanho")
				.add(Projections.property("doc.indFormulario"), "ideDocumentoObrigatorio.indFormulario")
				.add(Projections.property("doc.indAtivo"), "ideDocumentoObrigatorio.indAtivo")

				.add(Projections.property("da.ideDocumentoAto"), "ideDocumentoAto.ideDocumentoAto")
				.add(Projections.property("da.indAtivo"), "ideDocumentoAto.indAtivo")
				.add(Projections.property("aa.ideAtoAmbiental"), "ideDocumentoAto.ideAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("doc.indAtivo"), "ideDocumentoAto.ideDocumentoObrigatorio.indAtivo")
				
				.add(Projections.property("docObgDocAto.ideDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("docObgDocAto.nomDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("docObgDocAto.numTamanho"), "ideDocumentoAto.ideDocumentoObrigatorio.numTamanho")
				.add(Projections.property("docObgDocAto.indFormulario"), "ideDocumentoAto.ideDocumentoObrigatorio.indFormulario"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class));

		criteria.add(Restrictions.eq("this.indDocumentoValidado", Boolean.TRUE));
		criteria.add(Restrictions.or(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento),Restrictions.eq("unico.ideRequerimentoUnico", ideRequerimento)));

		return criteria;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarDocumentosObrigatorio(List<DocumentoObrigatorioRequerimento> listaDocumentoObrigatorioRequerimento) throws Exception {
		for (DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento : listaDocumentoObrigatorioRequerimento) {
			if (!Util.isNullOuVazio(documentoObrigatorioRequerimento.getIdeDocumentoObrigatorioRequerimento())) {
				String path = documentoObrigatorioRequerimento.getDscCaminhoArquivo();
				this.docObrigatorioRequerimentoDAO.remover(documentoObrigatorioRequerimento);
				this.gerenciaArquivoService.deletarArquivo(path);
			}
		}
	}

	public Collection<DocumentoObrigatorioRequerimento> listarFormulariosEnviados(Requerimento requerimento) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class)
				.createAlias("ideDocumentoObrigatorio", "docObr", JoinType.INNER_JOIN)
				.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
						.add(Projections.property("dscCaminhoArquivo"), "dscCaminhoArquivo")
						.add(Projections.property("indDocumentoValidado"), "indDocumentoValidado")
						.add(Projections.property("dtcValidacao"), "dtcValidacao")
						.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
						.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
						.add(Projections.property("docObr.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
						.add(Projections.property("docObr.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
						.add(Projections.property("docObr.indFormulario"), "ideDocumentoObrigatorio.indFormulario"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class));
		criteria.add(Restrictions.eq("docObr.indFormulario", Boolean.TRUE));
		criteria.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		return docObrigatorioRequerimentoDAO.listarPorCriteria(criteria);
	}

	public Collection<DocumentoObrigatorioRequerimento> listarDocumentosEnviados(Enquadramento enquadramento) throws Exception {
		Collection<DocumentoObrigatorioRequerimento> tDocumentoObrigatorioRequerimentos = new ArrayList<DocumentoObrigatorioRequerimento>();
		for (AtoAmbiental lAtoAmbiental : enquadramento.getAtoAmbientalCollection()) {
			DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class)
					.createAlias("ideDocumentoObrigatorio", "docObr", JoinType.INNER_JOIN)
					.createAlias("docObr.documentoAtoCollection", "docAto")
					.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
					.add(Restrictions.eq("docObr.indFormulario", Boolean.FALSE))
					.add(Restrictions.eq("req.ideRequerimento", enquadramento.getIdeRequerimento().getIdeRequerimento()))
					.add(Restrictions.eq("docAto.atoAmbiental", lAtoAmbiental))
					.addOrder(Order.asc("docObr.nomDocumentoObrigatorio"))
					.addOrder(Order.asc("docAto.atoAmbiental"))
					.setProjection(Projections.projectionList()
							.add(Projections.property("ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
							.add(Projections.property("dscCaminhoArquivo"), "dscCaminhoArquivo")
							.add(Projections.property("indDocumentoValidado"), "indDocumentoValidado")
							.add(Projections.property("dtcValidacao"), "dtcValidacao")
							.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
							.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
							.add(Projections.property("docObr.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
							.add(Projections.property("docObr.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
							.add(Projections.property("docObr.indFormulario"), "ideDocumentoObrigatorio.indFormulario")
							.add(Projections.property("docAto.atoAmbiental"), "ideDocumentoObrigatorio.documentoAtoCollection.atoAmbiental"))
							.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class));
			for (DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento : docObrigatorioRequerimentoDAO.listarPorCriteria(criteria)) {
				if (!tDocumentoObrigatorioRequerimentos.contains(documentoObrigatorioRequerimento)) {
					tDocumentoObrigatorioRequerimentos.add(documentoObrigatorioRequerimento);
				}
			}
		}
		return tDocumentoObrigatorioRequerimentos;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(List<DocumentoObrigatorioRequerimento> docsObrigatorioRequerimento) throws Exception {
		docObrigatorioRequerimentoDAO.salvarEmLote(docsObrigatorioRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorio> listarFormulariosAtoWhereIn(RequerimentoUnico pDocObrigatorioReq, Collection<AtoAmbiental> pAtosAmbientais) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class)
				.createAlias("ideDocumentoObrigatorio", "docObr")
				.createAlias("ideRequerimento", "req")
				.createAlias("docObr.documentoAtoCollection", "docAto")
				.add(Restrictions.eq("docObr.indFormulario", Boolean.TRUE))
				.add(Restrictions.eq("req.ideRequerimento", pDocObrigatorioReq.getRequerimento().getIdeRequerimento()))
				.add(Restrictions.in("docAto.atoAmbiental", pAtosAmbientais))
				.addOrder(Order.asc("docObr.nomDocumentoObrigatorio"))
				.addOrder(Order.asc("docAto.atoAmbiental"))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
						.add(Projections.property("docObr.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
						.add(Projections.property("docObr.dscCaminhoArquivo"), "ideDocumentoObrigatorio.dscCaminhoArquivo")
						.add(Projections.property("docObr.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
						.add(Projections.property("docAto.atoAmbiental"), "ideDocumentoObrigatorio.documentoAtoCollection.atoAmbiental"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class));
		Collection<DocumentoObrigatorio> docs = new ArrayList<DocumentoObrigatorio>();
		return docs;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorioRequerimento> buscarDocumentosObrigatoriosByAto(Integer ideRequerimento, Integer ideAtoAmbiental, Tipologia tipologia) throws Exception {

		Integer ideTipologia = null;
		boolean isTipologiaNull = Util.isNullOuVazio(tipologia);
		if (!isTipologiaNull) {
			ideTipologia = tipologia.getIdeTipologia();
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class);

		criteria.createAlias("ideRequerimento","ideRequerimento")
		.createAlias("ideDocumentoAto","ideDocumentoAto")
		.createAlias("ideDocumentoAto.ideAtoAmbiental","ato")
		.createAlias("ideDocumentoAto.ideTipologia","tipo",JoinType.LEFT_OUTER_JOIN)
		.createAlias("ideDocumentoAto.ideTipoFinalidadeUsoAgua","finalidade",JoinType.LEFT_OUTER_JOIN)

		.addOrder(Order.asc("ideDocumentoAto"))

		.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento));
		//				.add(Restrictions.eq("ideDocumentoAto.indAtivo", true));

		if (requerimentoService.isRequerimentoAntigo(ideRequerimento) || isTipologiaNull) {
			criteria.add(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental));
		} else {
			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental), Restrictions.isNull("tipo.ideTipologia")),
					Restrictions.and(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental), Restrictions.eq("tipo.ideTipologia", ideTipologia))
					));
		}

		return this.docObrigatorioRequerimentoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento, UploadedFile arquivo) throws Exception {
		String diretorioArquivo = this.gerenciaArquivoService.uploadArquivoDocumentoObrigatorioRequerimentoExperimental(documentoObrigatorioRequerimento);
		
		if(diretorioArquivo != null) {
			documentoObrigatorioRequerimento.setDscCaminhoArquivo(diretorioArquivo);
			this.docObrigatorioRequerimentoDAO.salvarOuAtualizar(documentoObrigatorioRequerimento);
		} else {
			throw new Exception("Erro ao carregar o caminho do arquivo.");
		}
	}

	/*@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitar(Requerimento requerimento, Pessoa pessoa, boolean todosArquivosEnviados) throws Exception {

		StatusRequerimento statusRequerimento = statusRequerimentoService.getMaxStatusTramitacaoRequerimantoPorData(requerimento);

		if(todosArquivosEnviados) {
			this.tramitarValidacaoPrevia(requerimento, pessoa);
		} else if(statusRequerimento.isEnquadrado()){
			this.tramitarPendenciaEnvioDocumentacao(requerimento, pessoa);
		}
	}*/

	/*@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarPendenciaEnvioDocumentacao(Requerimento requerimento,Pessoa pessoa) throws Exception {
		StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus());
		tramitacaoRequerimentoService.criarTramitacaoRequerimento(requerimento, status, pessoa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarValidacaoPrevia(Requerimento requerimento,Pessoa pessoa) throws Exception {
		StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus());
		tramitacaoRequerimentoService.criarTramitacaoRequerimento(requerimento, status, pessoa);
	}*/

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Collection<DocumentoObrigatorioInterface> listaDocumentos) throws Exception {
		for (DocumentoObrigatorioInterface documentoObrigatorioRequerimento : listaDocumentos) {
			this.documentoObrigatorioInterfaceDAO.atualizar(documentoObrigatorioRequerimento);
		}
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoObrigatorioRequerimento buscarDocumentoObrigatorioRequerimentoByIde(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) throws Exception{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideDocumentoObrigatorioRequerimento", ideDocumentoObrigatorioRequerimento.getIdeDocumentoObrigatorioRequerimento());
		return docObrigatorioRequerimentoDAO.buscarEntidadePorNamedQuery("DocumentoObrigatorioRequerimento.findByIdeDocumentoObrigatorioRequerimento", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarEnquadramentoDosDocumentosByRequerimento(Requerimento ideRequerimento, EnquadramentoAtoAmbiental ideEnquadramentoAto) throws Exception{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimento", ideRequerimento);
		parameters.put("ideEnquadramentoAtoAmbiental", ideEnquadramentoAto);
		docObrigatorioRequerimentoDAO.executarNamedQuery("DocumentoObrigatorioRequerimento.atualizarEnquadramentoByRequerimento", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoObrigatorioRequerimento buscarDocumentoObrigatorioRequerimentoByCaminhoArquivo(String caminho) throws Exception{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dscCaminhoArquivo", caminho);
		return docObrigatorioRequerimentoDAO.buscarEntidadePorNamedQuery("DocumentoObrigatorioRequerimento.findByDscCaminhoArquivo", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoObrigatorioRequerimentoByIdeDocObrReq(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) throws Exception{
		this.docObrigatorioRequerimentoDAO.remover(documentoObrigatorioRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoObrigatorioRequerimento buscarDocObrigatorioRequerimentoByRequerimentoAndIdeDocumentoObrigatorioEnum(Requerimento requerimento, Integer documentoObrigatorioEnum) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class)
				.createAlias("ideDocumentoObrigatorio", "docObr")
				.createAlias("ideRequerimento", "req")
				.add(Restrictions.eq("docObr.ideDocumentoObrigatorio", documentoObrigatorioEnum))
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()))
				.addOrder(Order.desc("ideDocumentoObrigatorio"))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
						.add(Projections.property("dscCaminhoArquivo"), "dscCaminhoArquivo")
						.add(Projections.property("indDocumentoValidado"), "indDocumentoValidado"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class));
		return docObrigatorioRequerimentoDAO.buscarPorCriteriaMaxResult(criteria);
	}
}