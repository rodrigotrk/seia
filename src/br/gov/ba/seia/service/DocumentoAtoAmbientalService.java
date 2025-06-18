package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoAtoAmbientalService {

	@Inject
	private IDAO<DocumentoAto> documentoAtoDAO;
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorio> listarFormulariosAtoWhereIn(Collection<AtoAmbiental> pAtoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class)
			.createAlias("ideDocumentoObrigatorio", "docObr", JoinType.FULL_JOIN)
			.setProjection(Projections.groupProperty("ideDocumentoObrigatorio"))
			.add(Restrictions.in("ideAtoAmbiental", pAtoAmbiental))
			.add(Restrictions.eq("docObr.indFormulario", Boolean.TRUE));
		return (ArrayList<DocumentoObrigatorio>) (Collection<?>) documentoAtoDAO.listarPorCriteria(criteria);
	}

	public List<DocumentoAto> buscarByAtoAmbientalAndDocumentoObrigatorio(Integer ideDocumentoObrigatorio, Integer ideAtoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class)
				.createAlias("ideAtoAmbiental", "ato", JoinType.INNER_JOIN)
				.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)

				.setProjection(Projections.projectionList()
					.add(Projections.property("ideDocumentoAto"),"ideDocumentoAto")
					.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoAto.class))

				.add(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental))
				.add(Restrictions.eq("doc.ideDocumentoObrigatorio", ideDocumentoObrigatorio))
				.add(Restrictions.eq("indAtivo", true));

		return documentoAtoDAO.listarPorCriteria(criteria);
	}


	public DocumentoAto carregarDocumentoAto(DocumentoAto documentoAto)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class)
			.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
			.createAlias("ideAtoAmbiental", "ato", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideDocumentoAto", documentoAto.getIdeDocumentoAto()));
		return documentoAtoDAO.buscarPorCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoObrigatorio> listarDocumentoObrigatorioWhereIn(Collection<AtoAmbiental> pAtoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class)
				.setProjection(Projections.groupProperty("documentoObrigatorio"))
				.add(Restrictions.in("ideAtoAmbiental", pAtoAmbiental))
				.createAlias("documentoObrigatorio", "docObr", JoinType.FULL_JOIN)
				.add(Restrictions.eq("docObr.indFormulario", Boolean.FALSE))
				.addOrder(Order.asc("documentoObrigatorio"));
		return (ArrayList<DocumentoObrigatorio>) (Collection<?>) documentoAtoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoAto> listarDocumentoAtoWhereInAto(AtoAmbiental pAtoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class)
				.createAlias("ideDocumentoObrigatorio", "docObr", JoinType.INNER_JOIN)

				.add(Restrictions.eq("ideAtoAmbiental", pAtoAmbiental))
				.add(Restrictions.ne("indAtivo", Boolean.FALSE))
				.add(Restrictions.isNull("ideTipologia"))
				.add(Restrictions.ne("docObr.indAtivo", Boolean.FALSE))
				.add(Restrictions.eq("docObr.indFormulario", Boolean.FALSE))

				.addOrder(Order.asc("docObr.nomDocumentoObrigatorio"))

				.setProjection(Projections.projectionList()
						.add(Projections.property("ideDocumentoAto"), "ideDocumentoAto")
						.add(Projections.property("ideAtoAmbiental"), "atoAmbiental")
						.add(Projections.property("indAtivo"), "indAtivo")
						.add(Projections.property("ideDocumentoAto"), "documentoAtoPK")
						.add(Projections.property("ideTipologia.ideTipologia"), "ideTipologia.ideTipologia")
						.add(Projections.property("ideDocumentoObrigatorio"), "documentoObrigatorio")
						.add(Projections.property("docObr.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
						.add(Projections.property("docObr.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoAto.class));
		return documentoAtoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarDocumentoAto(DocumentoAto docAto)  {
		documentoAtoDAO.salvarOuAtualizar(docAto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarDocumentoAto(DocumentoAto docAto)  {
		documentoAtoDAO.atualizar(docAto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoAto> listarDocumentoAtoByIdeAtoAmbiental(Integer ideAtoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class);
		criteria.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", ideAtoAmbiental));
		criteria.add(Restrictions.eq("indAtivo", true));
		return documentoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoAto> listarDocumentoAtoPorDocumentoObrigatorio(Collection<DocumentoObrigatorio> listaDocumentoObrigatorio)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class);
		criteria.add(Restrictions.in("ideDocumentoObrigatorio", listaDocumentoObrigatorio));
		criteria.add(Restrictions.eq("indAtivo", true));
		return documentoAtoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoAtoPorIdeAtoAmbiental(Integer ideAtoAmbiental)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideAtoAmbiental", ideAtoAmbiental);
		documentoAtoDAO.executarNamedQuery("DocumentoAto.deleteByIdeAtoAmbiental", parameters);
	}

	public void excluirDocumentoAto (DocumentoAto documentoAto) {
		documentoAtoDAO.remover(documentoAto);

	}
	
	private DetachedCriteria getCriteriaDocumentoAto(int ideAtoAmbiental) {
		return DetachedCriteria.forClass(DocumentoAto.class)
			.createAlias("ideAtoAmbiental", "ideAtoAmbiental", JoinType.INNER_JOIN)
			.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipologia", "ideTipologia", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideDocumentoObrigatorio", "ideDocumentoObrigatorio", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", ideAtoAmbiental))
			
			.setProjection(Projections.projectionList()		
				   .add(Projections.property("indAtivo"), "indAtivo")
				   .add(Projections.property("ideAtoAmbiental"), "ideAtoAmbiental")
				   .add(Projections.property("ideDocumentoAto"), "ideDocumentoAto")
				   .add(Projections.property("ideDocumentoObrigatorio.ideDocumentoObrigatorio"),"ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				   .add(Projections.property("ideDocumentoObrigatorio.nomDocumentoObrigatorio"),"ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				   .add(Projections.property("ideTipologia.ideTipologia"),"ideTipologia.ideTipologia")
				   .add(Projections.property("ideTipologia.desTipologia"),"ideTipologia.desTipologia")
				   .add(Projections.property("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
				   .add(Projections.property("ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
				  
			).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoAto.class))
					
			.addOrder(Order.asc("ideDocumentoObrigatorio.nomDocumentoObrigatorio"))
			.addOrder(Order.asc("ideTipologia.desTipologia"))
			.addOrder(Order.asc("ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoAto> listarDocumentoAtoByDemanda(int ideAtoAmbiental, int startPage, int maxPage){	
		return documentoAtoDAO.listarPorCriteriaDemanda(getCriteriaDocumentoAto(ideAtoAmbiental), startPage, maxPage);
	}

	public Integer countDocumentoAtosById(Integer ideAtoAmbiental){	
		StringBuilder lSql = new StringBuilder();
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		
		lSql.append(" SELECT COUNT(*) FROM DocumentoAto doc ");
		lSql.append(" WHERE doc.ideAtoAmbiental = " + ideAtoAmbiental);

		Query lQuery = lEntityManager.createQuery(lSql.toString());
		
		String retorno = lQuery.getSingleResult().toString();
		return Integer.parseInt(retorno);
		
	}
	
	/*
			Enquadramento
			EnquadramentoDocumentoAto
			DocumentoAto
			(Requerimento,AtoAmbiental)
	 */
	
	public boolean isExisteDocumentoObrigatorio(DocumentoAto doc)  {	
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class)
			.add(Restrictions.eq("ideDocumentoObrigatorio", doc.getIdeDocumentoObrigatorio()))
			.add(Restrictions.eq("ideAtoAmbiental", doc.getIdeAtoAmbiental()));
		
			if(!Util.isNullOuVazio(doc.getIdeTipologia())){
				criteria.add(Restrictions.eq("ideTipologia", doc.getIdeTipologia()));
			}else{
				criteria.add(Restrictions.isNull("ideTipologia"));
			}
	
			if(!Util.isNullOuVazio(doc.getIdeTipoFinalidadeUsoAgua())){
				criteria.add(Restrictions.eq("ideTipoFinalidadeUsoAgua", doc.getIdeTipoFinalidadeUsoAgua()));
			}else{
				criteria.add(Restrictions.isNull("ideTipoFinalidadeUsoAgua"));
			}
	
		return documentoAtoDAO.count(criteria)>0;
	}

}
