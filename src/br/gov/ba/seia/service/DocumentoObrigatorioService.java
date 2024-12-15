package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.controller.EnvioDocumentoController;
import br.gov.ba.seia.controller.ValidacaoPreviaController;
import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.TipoDocumentoObrigatorio;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.CrudOperationEnum;
import br.gov.ba.seia.enumerator.TipoDocumentoObrigatorioEnum;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

/**
 * @author mario.junior
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoObrigatorioService {

	@Inject
	private IDAO<DocumentoObrigatorio> documentoObrigatorioDAO;
	
	@Inject
	private IDAO<DocumentoObrigatorioInterface> documentoObrigatorioRequerimento;

	@Inject
	private IDAO<DocumentoAto> documentoAtoDAO;
	
	@Inject
	private IDAO<EnquadramentoAtoAmbiental> enquadramentoAtoAmbiental;
	
	@Inject
	private IDAO<TipoDocumentoObrigatorio> tipoDocumentoObrigatorioDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarDocumentoObrigatorio(DocumentoObrigatorio docObrigatorio)  {
		documentoObrigatorioDAO.salvarOuAtualizar(docObrigatorio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoObrigatorio(DocumentoObrigatorio docObrigatorio)  {
		documentoObrigatorioDAO.persistir(docObrigatorio, CrudOperationEnum.INSERT);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarDocumentoObrigatorio(DocumentoObrigatorio docObrigatorio)  {
		documentoObrigatorioDAO.persistir(docObrigatorio, CrudOperationEnum.UPDATE);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoObrigatorio obterDocumentoObrigatorio(DocumentoObrigatorio docObrigatorio){
		return documentoObrigatorioDAO.carregarLoad(docObrigatorio.getIdeDocumentoObrigatorio());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramentoDTO(Integer ideRequerimento, boolean isValidacao) {
		
		DetachedCriteria criteria = getCriteriaEnquadramentoAtoAmbientalPorRequerimento(ideRequerimento);
		List<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental = enquadramentoAtoAmbiental.listarPorCriteria(criteria);
		
		List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramentoDTO = new ArrayList<DocumentoObrigatorioEnquadramentoDTO>();
		
		for(EnquadramentoAtoAmbiental enqAto : listaEnquadramentoAtoAmbiental){
			
			DocumentoObrigatorioEnquadramentoDTO doc = new DocumentoObrigatorioEnquadramentoDTO();
			criteria = null;
			criteria = getCriteriaDocumentoObrigatorio(enqAto.getIdeEnquadramentoAtoAmbiental());
			if(!isValidacao){ // Melhoria #6643
				adicionarRestrictionInTipoDocumentoAdicional(criteria);
			}
			List<DocumentoObrigatorioInterface> listaDocumentoObrigatorio = documentoObrigatorioRequerimento.listarPorCriteria(criteria);
			
			doc.setEnquadramentoAtoAmbiental(enqAto);
			doc.setListaDocumentoObrigatorio(listaDocumentoObrigatorio);
			listaDocumentoObrigatorioEnquadramentoDTO.add(doc);
		}
		
		return listaDocumentoObrigatorioEnquadramentoDTO;
	}

	/**
	 * O método <b>listaDocumentoObrigatorioEnquadramentoDTO</b> é chamado em {@link EnvioDocumentoController} e {@link ValidacaoPreviaController}
	 * para atender respectivamente as telas de "Enviar Documentação Obrigatória" e "Validação Prévia".
	 * Com a melhoria <b>#6643</b>, o {@link TipoDocumentoObrigatorio} ADICIONAL não deve ser exibido na primeira tela mas deve ser exibido na segunda tela.
	 * Portanto, foi criado esse método para se adicionar uma Restriction no criteria e modificar a busca quando necessário.
	 * @param criteria
	 * @see Melhoria #6643
	 */
	private void adicionarRestrictionInTipoDocumentoAdicional(DetachedCriteria criteria) {
		criteria.add(Restrictions.ne("tipoDoc.ideTipoDocumentoObrigatorio", TipoDocumentoObrigatorioEnum.DOCUMENTO_ADICIONAL.getId()));
	}

	private DetachedCriteria getCriteriaEnquadramentoAtoAmbientalPorRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria  = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class);		
		
		criteria
			.createAlias("atoAmbiental",        "ato", JoinType.INNER_JOIN)
			.createAlias("tipologia",           "tip", JoinType.LEFT_OUTER_JOIN)
			.createAlias("enquadramento",       "enq", JoinType.INNER_JOIN)
			.createAlias("enq.ideRequerimento", "req", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental")
				.add(Projections.property("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("tip.ideTipologia"), "tipologia.ideTipologia")
				.add(Projections.property("tip.desTipologia"), "tipologia.desTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class));		
		return criteria;
	}
	
	private DetachedCriteria getCriteriaDocumentoObrigatorio(Integer ideEnquadramentoAtoAmbiental) {
		
		DetachedCriteria criteria  = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class,"docObrigReq");		
		
		criteria
			.createAlias("docObrigReq.ideEnquadramentoAtoAmbiental", "enqAto", JoinType.INNER_JOIN)
			.createAlias("docObrigReq.ideDocumentoAto", "docAto", JoinType.INNER_JOIN)
			.createAlias("docObrigReq.ideRequerimento", "req", JoinType.INNER_JOIN)
		.createAlias("docObrigReq.ideDocumentoObrigatorio", "docAdicional", JoinType.LEFT_OUTER_JOIN)
			.createAlias("docAto.ideDocumentoObrigatorio", "docObrig", JoinType.INNER_JOIN)
		.createAlias("docObrig.ideTipoDocumentoObrigatorio", "tipoDoc", JoinType.INNER_JOIN)
						
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("docObrigReq.ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento")
					.add(Projections.property("docObrigReq.dscCaminhoArquivo"), "dscCaminhoArquivo")
					.add(Projections.property("docObrigReq.indDocumentoValidado"), "indDocumentoValidado")
					.add(Projections.property("docObrigReq.indSigiloso"), "indSigiloso")
					.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
					.add(Projections.property("docAto.ideDocumentoAto"), "ideDocumentoAto.ideDocumentoAto")
					.add(Projections.property("docObrig.nomDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("tipoDoc.ideTipoDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.ideTipoDocumentoObrigatorio.ideTipoDocumentoObrigatorio")
					.add(Projections.property("enqAto.ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental.ideEnquadramentoAtoAmbiental")

				.add(Projections.property("docAdicional.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("docAdicional.indAtivo"), "ideDocumentoObrigatorio.indAtivo")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class))
			.add(Restrictions.eq("enqAto.ideEnquadramentoAtoAmbiental", ideEnquadramentoAtoAmbiental))
			.add(Restrictions.eq("docAdicional.indAtivo", true));
		return criteria;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoObrigatorio carregarDocumentoObrigatorio(Integer idDocObrigatorio){
		return documentoObrigatorioDAO.carregarGet(idDocObrigatorio);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoObrigatorio obterDocumentoObrigatorioCriteria(DocumentoObrigatorio docObrigatorio)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class);
		criteria.add(Restrictions.eq("ideDocumentoObrigatorio", docObrigatorio.getIdeDocumentoObrigatorio()));
		return documentoObrigatorioDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoObrigatorioEmLotes(List<DocumentoObrigatorio> newFomularios, Enquadramento enquadramento)  {
		for (DocumentoObrigatorio documentoObrigatorio : newFomularios) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideDocumentoObrigatorio", documentoObrigatorio.getIdeDocumentoObrigatorio());
			params.put("ideEnquadramento", enquadramento.getIdeEnquadramento());
			documentoObrigatorioDAO.executarNamedQuery("DocumentoObrigatorio.inserirEnquadramentoDocumento", params);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoObrigatorio(DocumentoObrigatorio documentoObrigatorio)  {
		documentoObrigatorioDAO.persistir(documentoObrigatorio, CrudOperationEnum.DELETE);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorio> listarDocumentosObrigatorios(Map<String, Object> params)  {
		String sql;
		if (params.isEmpty()) {
			sql = "select distinct d " + "from DocumentoObrigatorio d ";
		} else {
			sql = "select distinct d " + "from DocumentoObrigatorio d where upper(d.nomDocumentoObrigatorio) like upper(:nomeDocumento)";
		}
		return documentoObrigatorioDAO.listarPorQuery(sql, params);
		
	}

	
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoObrigatorio> listarTodosDocumentosObrigatorios()  {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select d ");
		sql.append("from DocumentoObrigatorio d ");
		sql.append("order by d.nomDocumentoObrigatorio asc");
		
		return documentoObrigatorioDAO.listarPorQuery(sql.toString(), null);
	}

	public List<TipoDocumentoObrigatorio> listarTipoDocumentoObrigatorio() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TipoDocumentoObrigatorio.class);
		return tipoDocumentoObrigatorioDAO.listarPorCriteria(detachedCriteria, Order.asc("nomTipoDocumentoObrigatorio"));
	}
	
	
	public Collection<DocumentoAto> listarDocumentosObrigatoriosByAtoAndTipologia(AtoAmbiental ideAtoAmbiental,Collection<Integer> listTipologia){
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class, "docAto")
				.createAlias("docAto.ideAtoAmbiental", "ato",JoinType.INNER_JOIN)
				.createAlias("docAto.ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
				.createAlias("doc.ideTipoDocumentoObrigatorio","tipoDoc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("docAto.ideTipologia", "tipologia",JoinType.LEFT_OUTER_JOIN)
				.createAlias("docAto.ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua",JoinType.LEFT_OUTER_JOIN);
		
		
		if(!Util.isNull(listTipologia) && !listTipologia.isEmpty())	{
			
			Disjunction disjunction = Restrictions.disjunction();
			disjunction
				.add(Restrictions.and(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental.getIdeAtoAmbiental()), Restrictions.in("tipologia.ideTipologia", listTipologia)))
				.add(Restrictions.and(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental.getIdeAtoAmbiental()), Restrictions.isNull("tipologia.ideTipologia")))
			;
			
			criteria
				.add(disjunction)
				.add(Restrictions.eq("docAto.indAtivo", true))
			;
		}
		else {
			//docs com atos e tipologia nula 
			criteria
				.add(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental.getIdeAtoAmbiental()))
				.add(Restrictions.isNull("tipologia.ideTipologia"))
				.add(Restrictions.eq("docAto.indAtivo", true));
		}
		
		criteria
		.add(Restrictions.ne("tipoDoc.ideTipoDocumentoObrigatorio", TipoDocumentoObrigatorioEnum.DOCUMENTO_ADICIONAL.getId()))
			.addOrder(Order.asc("doc.indFormulario"))
			.addOrder(Order.desc("doc.nomDocumentoObrigatorio"))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("docAto.ideDocumentoAto"),"ideDocumentoAto")
					.add(Projections.property("docAto.indAtivo"),"indAtivo")
					.add(Projections.property("doc.ideDocumentoObrigatorio"),"ideDocumentoObrigatorio.ideDocumentoObrigatorio")
					.add(Projections.property("doc.nomDocumentoObrigatorio"),"ideDocumentoObrigatorio.nomDocumentoObrigatorio")
					.add(Projections.property("doc.dscCaminhoArquivo"),"ideDocumentoObrigatorio.dscCaminhoArquivo")
					.add(Projections.property("doc.indFormulario"),"ideDocumentoObrigatorio.indFormulario")
					.add(Projections.property("doc.indFormularioDigital"),"ideDocumentoObrigatorio.indFormularioDigital")
					.add(Projections.property("doc.indAtivo"),"ideDocumentoObrigatorio.indAtivo")
					.add(Projections.property("tipologia.ideTipologia"),"ideTipologia.ideTipologia")
					.add(Projections.property("tipologia.desTipologia"),"ideTipologia.desTipologia")
					.add(Projections.property("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoAto.class));
		
		List<DocumentoAto> documentos = this.documentoAtoDAO.listarPorCriteria(criteria);
		
		// Ordena com Formulários e Documentos Gerais Primeiro
		this.ordernar(documentos); 
		
		return documentos;
	}

	private void ordernar(List<DocumentoAto> documentos) {
		Collections.sort(documentos); 
		Collections.reverse(documentos);
	}
	

	public List<DocumentoObrigatorio> carregarFormulariosByRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class)
				.createAlias("documentoAtoCollection", "dac")
				.createAlias("dac.enquadramentoDocumentoAtoCollection", "edac")
				.createAlias("edac.enquadramento", "enq")
				.createAlias("enq.ideRequerimento", "req")
				
		.setProjection(Projections.projectionList()
				.add(Projections.property("ideDocumentoObrigatorio"),"ideDocumentoObrigatorio")
				.add(Projections.property("nomDocumentoObrigatorio"),"nomDocumentoObrigatorio")
				.add(Projections.property("indFormulario"),"indFormulario")
				.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorio.class))
				
		.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))	
		.add(Restrictions.eq("indFormulario", true));	
		
		return documentoObrigatorioDAO.listarPorCriteria(criteria);		
	}
	
	public List<DocumentoObrigatorio> carregarFormulariosByProcessoReenquadramento(Integer ideProcessoReenquadramento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class)
				.createAlias("documentoAtoCollection", "dac")
				.createAlias("dac.enquadramentoDocumentoAtoCollection", "edac")
				.createAlias("edac.enquadramento", "enq")
				.createAlias("enq.ideProcessoReenquadramento", "pr")
				
		.setProjection(Projections.projectionList()
				.add(Projections.property("ideDocumentoObrigatorio"),"ideDocumentoObrigatorio")
				.add(Projections.property("nomDocumentoObrigatorio"),"nomDocumentoObrigatorio")
				.add(Projections.property("indFormulario"),"indFormulario")
				.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorio.class))
				
		.add(Restrictions.eq("pr.ideProcessoReenquadramento", ideProcessoReenquadramento))	
		.add(Restrictions.eq("indFormulario", true))
		.add(Restrictions.eq("dac.indAtivo", true));
		
		
		return documentoObrigatorioDAO.listarPorCriteria(criteria);		
	}

	public Collection<DocumentoAto> listarDocumentosOutorga(AtoAmbiental atoAmbiental)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DocumentoAto.class)
				.createAlias("ideAtoAmbiental", "ato")
//				.createAlias("ideDocumentoObrigatorio", "doc")
				.createAlias("ideTipologia", "tipologia",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua",JoinType.LEFT_OUTER_JOIN)
		.add(Restrictions.eq("ato.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()))
		.add(Restrictions.eq("ato.indAtivo", true));
		
		return this.documentoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	public Collection<DocumentoObrigatorio> listarDocumentoObrigatorioSemRelacionamentos() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideDocumentoObrigatorio"),"ideDocumentoObrigatorio")
						.add(Projections.property("nomDocumentoObrigatorio"),"nomDocumentoObrigatorio")
						.add(Projections.property("indFormulario"),"indFormulario")
						
				
				).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorio.class));
		
	
		
		criteria.addOrder(Order.asc("indFormulario"));
		criteria.addOrder(Order.asc("nomDocumentoObrigatorio"));
		
		
			
		return documentoObrigatorioDAO.listarPorCriteria(criteria);		
	}

	public Collection<DocumentoObrigatorio> listarDocumentoObrigatorioIndFormualario(boolean b) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideDocumentoObrigatorio"),"ideDocumentoObrigatorio")
						.add(Projections.property("nomDocumentoObrigatorio"),"nomDocumentoObrigatorio")
						.add(Projections.property("indFormulario"),"indFormulario")
				
				).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorio.class));
				
				criteria.add(Restrictions.eq("indFormulario", b));
				
		return documentoObrigatorioDAO.listarPorCriteria(criteria);		
	}
	
	public DocumentoObrigatorio buscarDocumentoObrigatorioPorDocumentoAto(Integer ideDocumentoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class);

		criteria
		.createAlias("documentoAtoCollection", "documentoAto")
		.setProjection(Projections.projectionList()
				.add(Projections.property("ideDocumentoObrigatorio"),"ideDocumentoObrigatorio")
				.add(Projections.property("nomDocumentoObrigatorio"),"nomDocumentoObrigatorio")
				.add(Projections.property("indFormulario"),"indFormulario")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorio.class));

		criteria.add(Restrictions.eq("documentoAto.ideDocumentoAto", ideDocumentoAto));

		return documentoObrigatorioDAO.buscarPorCriteria(criteria);
	}


	/**
	 * Método que vai listar apenas os {@link DocumentoObrigatorio} que estão vinculados ao {@link AtoAmbiental} do {@link ProcessoAto} para {@link AnaliseTecnica} daquele {@link Funcionario}.
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param tecnico
	 * @param listaProcessoAto
	 * @return 
	 * @throws Exception 
	 * @since 14/03/2016
	 */
	public List<DocumentoObrigatorio> listarDocumentoObrigatoriosByTecnicoProcessoAto(Funcionario tecnico, List<Integer> listaProcessoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class, "doc")
				.createAlias("doc.documentoAtoCollection", "da", JoinType.INNER_JOIN)
				.createAlias("da.enquadramentoDocumentoAtoCollection", "eda", JoinType.INNER_JOIN)
				.createAlias("eda.enquadramento", "e", JoinType.INNER_JOIN)
				.createAlias("e.ideRequerimento", "r", JoinType.INNER_JOIN)
				.createAlias("r.processoCollection", "p", JoinType.INNER_JOIN)
				.createAlias("p.processoAtoCollection", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.processoAtoIntegranteEquipeCollection", "paie", JoinType.INNER_JOIN)
				.createAlias("paie.ideIntegranteEquipe", "ie", JoinType.INNER_JOIN)
				.createAlias("ie.idePessoaFisica", "f", JoinType.INNER_JOIN)
				.add(Property.forName("da.ideAtoAmbiental").eqProperty("pa.atoAmbiental"))
				.add(Restrictions.or(Property.forName("da.ideTipologia").eqProperty("pa.tipologia"), Property.forName("pa.tipologia").isNull()))
				.add(Restrictions.in("pa.ideProcessoAto", listaProcessoAto))
				.add(Restrictions.eq("f.idePessoaFisica", tecnico.getIdePessoaFisica()))
					.setProjection(Projections.projectionList()
					.add(Projections.property("ideDocumentoObrigatorio"),"ideDocumentoObrigatorio"))
					.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorio.class))
					;
		return documentoObrigatorioDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramentoDTOReenquadramento(List<Integer> ideProcessoReenquadramentoList, boolean isValidacao) {
		
		List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramentoDTO = new ArrayList<DocumentoObrigatorioEnquadramentoDTO>();
		
		if(!Util.isNullOuVazio(ideProcessoReenquadramentoList)) {
			
			DetachedCriteria criteria = getCriteriaEnquadramentoAtoAmbientalPorProcessoReenquadramento(ideProcessoReenquadramentoList);
			List<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental = enquadramentoAtoAmbiental.listarPorCriteria(criteria);
			
			
			for(EnquadramentoAtoAmbiental enqAto : listaEnquadramentoAtoAmbiental){
				
				DocumentoObrigatorioEnquadramentoDTO doc = new DocumentoObrigatorioEnquadramentoDTO();
				criteria = getCriteriaDocumentoObrigatorioReenquadramento(enqAto.getIdeEnquadramentoAtoAmbiental());
				if(!isValidacao){ // Melhoria #6643
					adicionarRestrictionInTipoDocumentoAdicional(criteria);
				}
				List<DocumentoObrigatorioInterface> listaDocumentoObrigatorio = documentoObrigatorioRequerimento.listarPorCriteria(criteria);
				
				doc.setEnquadramentoAtoAmbiental(enqAto);
				doc.setListaDocumentoObrigatorio(listaDocumentoObrigatorio);
				listaDocumentoObrigatorioEnquadramentoDTO.add(doc);
			}
		}
		
		
		return listaDocumentoObrigatorioEnquadramentoDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramentoDTOReenquadramento(Integer ideProcessoReenquadramento, boolean isValidacao)  {
		
		DetachedCriteria criteria = getCriteriaEnquadramentoAtoAmbientalPorProcessoReenquadramento(ideProcessoReenquadramento);
		List<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental = enquadramentoAtoAmbiental.listarPorCriteria(criteria);
		
		List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramentoDTO = new ArrayList<DocumentoObrigatorioEnquadramentoDTO>();
		
		for(EnquadramentoAtoAmbiental enqAto : listaEnquadramentoAtoAmbiental){
			
			DocumentoObrigatorioEnquadramentoDTO doc = new DocumentoObrigatorioEnquadramentoDTO();
			criteria = getCriteriaDocumentoObrigatorioReenquadramento(enqAto.getIdeEnquadramentoAtoAmbiental());
			if(!isValidacao){ // Melhoria #6643
				adicionarRestrictionInTipoDocumentoAdicional(criteria);
			}
			List<DocumentoObrigatorioInterface> listaDocumentoObrigatorio = documentoObrigatorioRequerimento.listarPorCriteria(criteria);
			
			doc.setEnquadramentoAtoAmbiental(enqAto);
			doc.setListaDocumentoObrigatorio(listaDocumentoObrigatorio);
			listaDocumentoObrigatorioEnquadramentoDTO.add(doc);
		}
		
		return listaDocumentoObrigatorioEnquadramentoDTO;
	}
	
	private DetachedCriteria getCriteriaEnquadramentoAtoAmbientalPorProcessoReenquadramento(List<Integer> ideProcessoReenquadramento) {
		DetachedCriteria criteria  = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class);		
		
		criteria
			.createAlias("atoAmbiental",        "ato", JoinType.INNER_JOIN)
			.createAlias("tipologia",           "tip", JoinType.LEFT_OUTER_JOIN)
			.createAlias("enquadramento",       "enq", JoinType.INNER_JOIN)
			.createAlias("enq.ideProcessoReenquadramento", "req", JoinType.INNER_JOIN)
			
			.add(Restrictions.in("req.ideProcessoReenquadramento", ideProcessoReenquadramento))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental")
				.add(Projections.property("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("tip.ideTipologia"), "tipologia.ideTipologia")
				.add(Projections.property("tip.desTipologia"), "tipologia.desTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class));		
		return criteria;
	}
	
	private DetachedCriteria getCriteriaEnquadramentoAtoAmbientalPorProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		DetachedCriteria criteria  = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class);		
		
		criteria
			.createAlias("atoAmbiental",        "ato", JoinType.INNER_JOIN)
			.createAlias("tipologia",           "tip", JoinType.LEFT_OUTER_JOIN)
			.createAlias("enquadramento",       "enq", JoinType.INNER_JOIN)
			.createAlias("enq.ideProcessoReenquadramento", "req", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("req.ideProcessoReenquadramento", ideProcessoReenquadramento))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental")
				.add(Projections.property("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("tip.ideTipologia"), "tipologia.ideTipologia")
				.add(Projections.property("tip.desTipologia"), "tipologia.desTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class));		
		return criteria;
	}
	
	private DetachedCriteria getCriteriaDocumentoObrigatorioReenquadramento(Integer ideEnquadramentoAtoAmbiental) {
		
		DetachedCriteria criteria  = DetachedCriteria.forClass(DocumentoObrigatorioReenquadramento.class,"docObrigReq");		
		
		criteria
			.createAlias("docObrigReq.ideEnquadramentoAtoAmbiental", "enqAto", JoinType.INNER_JOIN)
			.createAlias("docObrigReq.ideDocumentoAto", "docAto", JoinType.INNER_JOIN)
			.createAlias("docObrigReq.ideProcessoReenquadramento", "req", JoinType.INNER_JOIN)
			.createAlias("docObrigReq.ideDocumentoObrigatorio", "docAdicional", JoinType.LEFT_OUTER_JOIN)
			.createAlias("docAto.ideDocumentoObrigatorio", "docObrig", JoinType.INNER_JOIN)
			.createAlias("docObrig.ideTipoDocumentoObrigatorio", "tipoDoc", JoinType.INNER_JOIN)
			
			.createAlias("docObrigReq.idePessoaValidacao", "pessoaValidacao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoaValidacao.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
						
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("docObrigReq.ideDocumentoObrigatorioReenquadramento"), "ideDocumentoObrigatorioReenquadramento")
					.add(Projections.property("docObrigReq.dscCaminhoArquivo"), "dscCaminhoArquivo")
					.add(Projections.property("docObrigReq.indDocumentoValidado"), "indDocumentoValidado")
					.add(Projections.property("docObrigReq.indSigiloso"), "indSigiloso")
					.add(Projections.property("req.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento")
					.add(Projections.property("docAto.ideDocumentoAto"), "ideDocumentoAto.ideDocumentoAto")
					.add(Projections.property("docObrig.nomDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.nomDocumentoObrigatorio")
					.add(Projections.property("tipoDoc.ideTipoDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.ideTipoDocumentoObrigatorio.ideTipoDocumentoObrigatorio")
					.add(Projections.property("enqAto.ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental.ideEnquadramentoAtoAmbiental")
					.add(Projections.property("docAdicional.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
					.add(Projections.property("docObrigReq.dtcValidacao"), "dtcValidacao")
					.add(Projections.property("pessoaFisica.nomPessoa"), "idePessoaValidacao.pessoaFisica.nomPessoa")
					.add(Projections.property("pessoaValidacao.idePessoa"), "idePessoaValidacao.idePessoa")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioReenquadramento.class))
			.add(Restrictions.eq("enqAto.ideEnquadramentoAtoAmbiental", ideEnquadramentoAtoAmbiental));
		return criteria;
	}
	
	public Boolean isDocumentoObrigatorioAto(DocumentoObrigatorio docObrigatorio, AtoAmbiental atoAmbiental, 
			 Tipologia ideTipologia){
					DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class, "docAto")
					.createAlias("docAto.ideAtoAmbiental", "ato",JoinType.INNER_JOIN)
					.createAlias("docAto.ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
					.createAlias("doc.ideTipoDocumentoObrigatorio","tipoDoc", JoinType.LEFT_OUTER_JOIN)
					.createAlias("docAto.ideTipologia", "tipologia",JoinType.LEFT_OUTER_JOIN);
					
					criteria
						.add(Restrictions.eq("doc.ideDocumentoObrigatorio", docObrigatorio.getIdeDocumentoObrigatorio()))
						.add(Restrictions.eq("ato.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()))
						.add(Restrictions.eq("docAto.indAtivo", true));
					
					if(!Util.isNullOuVazio(ideTipologia)) {
						criteria.add(Restrictions.eq("tipologia.ideTipologia", ideTipologia.getIdeTipologia()));
					}
					
					criteria
						.setProjection(
							Projections.projectionList()
								.add(Projections.property("docAto.ideDocumentoAto"),"ideDocumentoAto")
								.add(Projections.property("docAto.indAtivo"),"indAtivo")
								.add(Projections.property("doc.ideDocumentoObrigatorio"),"ideDocumentoObrigatorio.ideDocumentoObrigatorio")
								.add(Projections.property("doc.nomDocumentoObrigatorio"),"ideDocumentoObrigatorio.nomDocumentoObrigatorio")
								.add(Projections.property("doc.dscCaminhoArquivo"),"ideDocumentoObrigatorio.dscCaminhoArquivo")
								.add(Projections.property("doc.indAtivo"),"ideDocumentoObrigatorio.indAtivo")
								.add(Projections.property("tipologia.ideTipologia"),"ideTipologia.ideTipologia")
								.add(Projections.property("tipologia.desTipologia"),"ideTipologia.desTipologia")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoAto.class));
				
				return  this.documentoAtoDAO.listarPorCriteria(criteria).isEmpty() ? false : true;
	}
	
	public Boolean isAtivo(DocumentoObrigatorio docObrigatorio) {
		StringBuilder lSql = new StringBuilder();
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		
		lSql.append(" SELECT doc.indAtivo FROM DocumentoObrigatorio doc ");
		lSql.append(" WHERE doc.ideDocumentoObrigatorio = " + docObrigatorio.getIdeDocumentoObrigatorio());

		Query lQuery = lEntityManager.createQuery(lSql.toString());
		
		return (Boolean) lQuery.getSingleResult();
	}
}
