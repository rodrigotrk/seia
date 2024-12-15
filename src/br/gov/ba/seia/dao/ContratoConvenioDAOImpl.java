package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaContratoConvenio;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContratoConvenioDAOImpl extends AbstractDAO<ContratoConvenio>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<ContratoConvenio> getDAO() {
		return contratoConvenioDAOI;
	}

	@Inject
	private IDAO<ContratoConvenio> contratoConvenioDAOI;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isPessoaComContratoConvenio(Integer idePessoaFisica)  {
		return contratoConvenioDAOI.isExiste(getCriteriaListarContratoConvenio(idePessoaFisica));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ContratoConvenio> listarConveniosPorPessoa(Integer idePessoaFisica)  {
		return contratoConvenioDAOI.listarPorCriteria(getCriteriaListarContratoConvenio(idePessoaFisica));
	}

	public DetachedCriteria getCriteriaListarContratoConvenio(Integer idePessoaFisica){
		return
			DetachedCriteria.forClass(PessoaFisicaContratoConvenio.class)
				.createAlias("idePessoaFisica" , "idePessoaFisica")
				.createAlias("ideContratoConvenio" , "ideContratoConvenio")
				.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", idePessoaFisica))
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideContratoConvenio.ideContratoConvenio"),"ideContratoConvenio")
				)

				.setResultTransformer(new AliasToNestedBeanResultTransformer(ContratoConvenio.class));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ContratoConvenio> listaContratoConvenio(PessoaJuridica pj)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenio.class);

			criteria.createAlias("ideGestorFinanceiro", "gf")
			.createAlias("idePessoaJuridica", "pj")
			.createAlias("ideTipoProjeto", "tp");

			criteria.add(Restrictions.eq("pj.idePessoaJuridica", pj.getIdePessoaJuridica()));
			criteria.add(Restrictions.eq("indExcluido", false));


		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideContratoConvenio"),"ideContratoConvenio")
				.add(Projections.property("nomContratoConvenio"),"nomContratoConvenio")
				.add(Projections.property("numContrato"),"numContrato")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("gf.ideGestorFinanceiro"),"ideGestorFinanceiro.ideGestorFinanceiro")
				.add(Projections.property("pj.idePessoaJuridica"),"idePessoaJuridica.idePessoaJuridica")
				.add(Projections.property("pj.nomRazaoSocial"),"idePessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pj.numCnpj"),"idePessoaJuridica.numCnpj")
				.add(Projections.property("tp.ideTipoProjeto"),"ideTipoProjeto.ideTipoProjeto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ContratoConvenio.class))
		;

		return contratoConvenioDAOI.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ContratoConvenio> listaContratoConvenio() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenio.class)
			.createAlias("ideGestorFinanceiro", "gf")
			.createAlias("idePessoaJuridica", "pj")
			.createAlias("ideTipoProjeto", "tp")
			.add(Restrictions.eq("indExcluido", false))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideContratoConvenio"),"ideContratoConvenio")
				.add(Projections.property("nomContratoConvenio"),"nomContratoConvenio")
				.add(Projections.property("numContrato"),"numContrato")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("gf.ideGestorFinanceiro"),"ideGestorFinanceiro.ideGestorFinanceiro")
				.add(Projections.property("pj.idePessoaJuridica"),"idePessoaJuridica.idePessoaJuridica")
				.add(Projections.property("tp.ideTipoProjeto"),"ideTipoProjeto.ideTipoProjeto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ContratoConvenio.class))
		;

		return contratoConvenioDAOI.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isCompartilhaContratoConvenio(PessoaFisica pessoaValidar,PessoaFisica pessoaFisica) {

		DetachedCriteria subcriteria = DetachedCriteria.forClass(PessoaFisicaContratoConvenio.class)
			.createAlias("idePessoaFisica", "idePessoaFisica", JoinType.INNER_JOIN)
			.createAlias("ideContratoConvenio", "ideContratoConvenio", JoinType.INNER_JOIN)
			.add(Restrictions.eq("idePessoaFisica.idePessoaFisica",pessoaFisica.getId()))

			.setProjection(Projections.projectionList()
					.add(Projections.property("ideContratoConvenio"),"ideContratoConvenio"))
			;

		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisicaContratoConvenio.class)
			.createAlias("idePessoaFisica", "idePessoaFisica", JoinType.INNER_JOIN)
			.createAlias("ideContratoConvenio", "ideContratoConvenio", JoinType.INNER_JOIN)
			.add(Restrictions.eq("idePessoaFisica.idePessoaFisica",pessoaValidar.getId()))

			.setProjection(Projections.projectionList()
					.add(Projections.property("ideContratoConvenio"),"ideContratoConvenio"))
			.add(Subqueries.propertyEq("ideContratoConvenio",subcriteria));
			

		return contratoConvenioDAOI.isExiste(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isExiste(DetachedCriteria criteria) {
		return contratoConvenioDAOI.isExiste(criteria);
	}
	
	public Collection<ContratoConvenio> listarContratoConvenioPorFiltro(ContratoConvenio contratoConvenio, Integer first, Integer pageSize) throws Exception{
		
		Collection<ContratoConvenio> contratoConvenioCollection = new ArrayList<ContratoConvenio>();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenio.class);

		criteria.createAlias("ideGestorFinanceiro", "gf")
		.createAlias("idePessoaJuridica", "pj")
		.createAlias("ideTipoProjeto", "tp");
		
		
		if(!Util.isNullOuVazio(contratoConvenio.getIdePessoaJuridica())){
			
			criteria.add(Restrictions.eq("idePessoaJuridica", contratoConvenio.getIdePessoaJuridica()));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getNomContratoConvenio())){
			
			criteria.add(Restrictions.ilike("nomContratoConvenio" , contratoConvenio.getNomContratoConvenio(),MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getNumContrato())){
			
			criteria.add(Restrictions.ilike("numContrato", contratoConvenio.getNumContrato(), MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getIdeGestorFinanceiro())){
			
			criteria.add(Restrictions.eq("ideGestorFinanceiro", contratoConvenio.getIdeGestorFinanceiro()));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getIdeTipoProjeto())){
			
			criteria.add(Restrictions.eq("ideTipoProjeto", contratoConvenio.getIdeTipoProjeto()));
		}
		
		criteria.add(Restrictions.eq("indExcluido", false));
		
		contratoConvenioCollection = contratoConvenioDAOI.listarPorCriteriaDemanda(criteria, first, pageSize);
		
		return contratoConvenioCollection;
	}
	
	public int listarContratoConvenioPorFiltroCount(ContratoConvenio contratoConvenio) throws Exception{
	
		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenio.class);

		criteria.createAlias("ideGestorFinanceiro", "gf")
		.createAlias("idePessoaJuridica", "pj")
		.createAlias("ideTipoProjeto", "tp");
		
		if(!Util.isNullOuVazio(contratoConvenio.getIdePessoaJuridica())){
			
			criteria.add(Restrictions.eq("idePessoaJuridica", contratoConvenio.getIdePessoaJuridica()));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getNomContratoConvenio())){
			
			criteria.add(Restrictions.ilike("nomContratoConvenio" , contratoConvenio.getNomContratoConvenio(),MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getNumContrato())){
			
			criteria.add(Restrictions.ilike("numContrato", contratoConvenio.getNumContrato(), MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getIdeGestorFinanceiro())){
			
			criteria.add(Restrictions.eq("ideGestorFinanceiro", contratoConvenio.getIdeGestorFinanceiro()));
		}
		
		if(!Util.isNullOuVazio(contratoConvenio.getIdeTipoProjeto())){
			
			criteria.add(Restrictions.eq("ideTipoProjeto", contratoConvenio.getIdeTipoProjeto()));
		}
		
		criteria.add(Restrictions.eq("indExcluido", false));
		
		return contratoConvenioDAOI.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isPessoaComContratoConvenioPCT(Integer idePessoaFisica) throws Exception {
		
		return contratoConvenioDAOI.isExiste(getCriteriaListarContratoConvenioPCT(idePessoaFisica));
	}

	public DetachedCriteria getCriteriaListarContratoConvenioPCT(Integer idePessoaFisica){
		return
			DetachedCriteria.forClass(PessoaFisicaContratoConvenio.class)
				.createAlias("idePessoaFisica" , "idePessoaFisica")
				.createAlias("ideContratoConvenio" , "ideContratoConvenio")
				.createAlias("ideContratoConvenio.ideTipoProjeto" , "ideTipoProjeto")
				.add(Restrictions.eq("indAtivo", true))
				.add(Restrictions.eq("ideContratoConvenio.indExcluido", false))
				.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", idePessoaFisica))
				.add(Restrictions.eq("ideTipoProjeto.ideTipoProjeto", Integer.valueOf(2)))
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideContratoConvenio.ideContratoConvenio"),"ideContratoConvenio")
					.add(Projections.property("ideContratoConvenio.nomContratoConvenio"),"nomContratoConvenio")
				)

				.setResultTransformer(new AliasToNestedBeanResultTransformer(ContratoConvenio.class));
	}
	
	public List<ContratoConvenio> listarContratoConvencioPctByPessoaFisica(Integer idePessoaFisica) throws Exception{
		
		return contratoConvenioDAOI.listarPorCriteria(getCriteriaListarContratoConvenioPCT(idePessoaFisica));
	}
}