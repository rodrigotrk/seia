package br.gov.ba.seia.dao;

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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.IntegranteEquipe;
import br.gov.ba.seia.entity.ProcessoAtoIntegranteEquipe;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class IntegranteEquipeDAOImpl {
	
	@Inject
	private IDAO<IntegranteEquipe> integranteEquipeDAO;
	
	@Inject
	private IDAO<ProcessoAtoIntegranteEquipe> processoAtoIntegranteEquipeDAO; 
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(IntegranteEquipe integranteEquipe){
		
		StringBuilder sql = null;
		Map<String, Object> params = null;
		
		sql = new StringBuilder()
			.append("delete from ProcessoAtoIntegranteEquipe p1 ")
			.append("where p1.processoAtoIntegranteEquipePK in (select p2.processoAtoIntegranteEquipePK ")
			.append("	   									    from ProcessoAtoIntegranteEquipe p2 ")
			.append("	   									    inner join p2.ideIntegranteEquipe ie ")
			.append("											where ie.ideIntegranteEquipe = :ideIntegranteEquipe)")
		;
		
		params = new HashMap<String, Object>();
		params.put("ideIntegranteEquipe", integranteEquipe.getIdeIntegranteEquipe());
		
		integranteEquipeDAO.executarQuery(sql.toString(), params);
		
		sql = new StringBuilder()
			.append("delete from IntegranteEquipe ie ")
			.append("where ie.ideIntegranteEquipe = :ideIntegranteEquipe")
		;
		
		params = new HashMap<String, Object>();
		params.put("ideIntegranteEquipe", integranteEquipe.getIdeIntegranteEquipe());
		
		integranteEquipeDAO.executarQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<IntegranteEquipe> listarIntegranteEquipe(Integer ideEquipe) {
    	DetachedCriteria criteria = criteriaIntegranteEquipe(ideEquipe);
    	return integranteEquipeDAO.listarPorCriteria(criteria);
    }
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<IntegranteEquipe> listarIntegranteEquipeComListaAtos(Integer ideEquipe)  {
		DetachedCriteria criteria = criteriaIntegranteEquipe(ideEquipe);
		List<IntegranteEquipe> listaIntegranteEquipe = integranteEquipeDAO.listarPorCriteria(criteria);
		
		for(IntegranteEquipe ie : listaIntegranteEquipe) {
			ie.setProcessoAtoIntegranteEquipeCollection(listarProcessoAtoIntegranteEquipe(ie));
			ie.setListaAto(listarAto(ie.getProcessoAtoIntegranteEquipeCollection()));
		}
		return listaIntegranteEquipe;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<String> listarAto(Collection<ProcessoAtoIntegranteEquipe> listaProcessoAtoIntegranteEquipe)  {
		Collection<String> listaAto = new ArrayList<String>();
		for(ProcessoAtoIntegranteEquipe processoAtoIntegranteEquipe : listaProcessoAtoIntegranteEquipe) {
			listaAto.add(processoAtoIntegranteEquipe.getIdeProcessoAto().getNomAtoAmbientalTipologia());
		}
		return listaAto;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<ProcessoAtoIntegranteEquipe> listarProcessoAtoIntegranteEquipe(IntegranteEquipe integranteEquipe)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(IntegranteEquipe.class);
		criteria
			.createAlias("processoAtoIntegranteEquipeCollection", "paiep", JoinType.INNER_JOIN)
			.createAlias("paiep.ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.atoAmbiental", "at", JoinType.INNER_JOIN)
			.createAlias("pa.tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideIntegranteEquipe", integranteEquipe.getIdeIntegranteEquipe()))
			
			.setProjection(Projections.projectionList()
				
				.add(Projections.property("ideIntegranteEquipe"),"ideIntegranteEquipe.ideIntegranteEquipe")
				
				.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
				.add(Projections.property("t.ideTipologia"),"ideProcessoAto.tipologia.ideTipologia")
				.add(Projections.property("t.desTipologia"),"ideProcessoAto.tipologia.desTipologia")
				.add(Projections.property("at.ideAtoAmbiental"),"ideProcessoAto.atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("at.nomAtoAmbiental"),"ideProcessoAto.atoAmbiental.nomAtoAmbiental")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAtoIntegranteEquipe.class))
		;
		
		return processoAtoIntegranteEquipeDAO.listarPorCriteria(criteria);
	}

	private DetachedCriteria criteriaIntegranteEquipe(Integer ideEquipe) {
		DetachedCriteria criteria = DetachedCriteria.forClass(IntegranteEquipe.class);
    	criteria
	    	.createAlias("ideArea", "a", JoinType.LEFT_OUTER_JOIN)
	    	.createAlias("idePessoaFisica", "fun", JoinType.INNER_JOIN)
	    	.createAlias("fun.ideArea", "afun", JoinType.INNER_JOIN)
	    	.createAlias("fun.pessoaFisica", "pf", JoinType.INNER_JOIN)
	    	.createAlias("ideEquipe", "eq", JoinType.INNER_JOIN)
	    	
	    	.createAlias("eq.ideArea", "aeq", JoinType.INNER_JOIN)
	    	.createAlias("eq.ideAreaResponsavel", "ar", JoinType.LEFT_OUTER_JOIN)
	    	.createAlias("eq.ideProcesso", "p", JoinType.INNER_JOIN)
	    	
	    	.add(Restrictions.eq("eq.ideEquipe", ideEquipe))
	    	
	    	.setProjection(Projections.projectionList()
	    		.add(Projections.property("ideIntegranteEquipe"),"ideIntegranteEquipe")
	    		.add(Projections.property("a.ideArea"),"ideArea.ideArea")
	    		.add(Projections.property("indLiderEquipe"),"indLiderEquipe")
	    		.add(Projections.property("eq.ideEquipe"),"ideEquipe.ideEquipe")
	    		.add(Projections.property("aeq.ideArea"),"ideEquipe.ideArea.ideArea")
	    		.add(Projections.property("ar.ideArea"),"ideEquipe.ideAreaResponsavel.ideArea")
	    		.add(Projections.property("p.ideProcesso"),"ideEquipe.ideProcesso.ideProcesso")
	    		.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
	    		.add(Projections.property("afun.ideArea"),"idePessoaFisica.ideArea.ideArea")
	    		.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.pessoaFisica.idePessoaFisica")
	    		.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.pessoaFisica.nomPessoa")
	    	)
	    	
	    	.setResultTransformer(new AliasToNestedBeanResultTransformer(IntegranteEquipe.class))
    	;
		return criteria;
	}
   
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public IntegranteEquipe buscarLiderProcesso(Integer ideEquipe) {
    	DetachedCriteria criteria = criteriaIntegranteEquipe(ideEquipe);
    	criteria.add(Restrictions.eq("indLiderEquipe", Boolean.TRUE));
    	return integranteEquipeDAO.buscarPorCriteria(criteria);
    	
    }
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(IntegranteEquipe integranteEquipe) {
		integranteEquipeDAO.salvar(integranteEquipe);
	}
    
}