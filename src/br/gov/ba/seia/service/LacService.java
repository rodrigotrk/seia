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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.LacEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LacService {

	@Inject
	IDAO<Lac> lacDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Lac buscarLacPorProcesso(Integer ideProcesso)  {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select lac ");
		sql.append("from Processo p ");
		sql.append("	 inner join p.ideRequerimento req ");
		sql.append("	 inner join req.lac lac ");
		sql.append("	 inner join fetch lac.ideDocumentoObrigatorio doc ");
		sql.append("where p.ideProcesso = :ideProcesso ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", ideProcesso);
		
		return lacDAO.buscarPorQuery(sql.toString(), params);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Lac buscarLacPorRequerimento(Integer ideRequerimento)  {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select lac ");
		sql.append("from Requerimento req ");
		sql.append("	 inner join req.lac lac ");
		sql.append("	 inner join fetch lac.ideDocumentoObrigatorio doc ");
		sql.append("where req.ideRequerimento = :ideRequerimento ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", ideRequerimento);
		
		return lacDAO.buscarPorQuery(sql.toString(), params);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Lac> listarLacByIdeRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Lac.class)
				.createAlias("ideDocumentoObrigatorio", "doc")
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideLac"), "ideLac")
						.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(Lac.class));
		return lacDAO.listarPorCriteria(criteria);
	}
	
	
	
	
	public boolean isLacPosto(int ideRequerimento) {
		boolean isLacPosto = false;
		DetachedCriteria criteria = DetachedCriteria.forClass(Lac.class)
				.createAlias("ideDocumentoObrigatorio", "doc")
				.createAlias("ideRequerimento", "req")
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("doc.ideDocumentoObrigatorio",LacEnum.LAC_POSTO.getId()));
		List<Lac> lacList = lacDAO.listarPorCriteria(criteria);
				
			if(Util.isNullOuVazio(lacList)){
				isLacPosto= false;
			}
			else{
				isLacPosto= true;
			}
			
		return isLacPosto;
	}
	
	public boolean isLacErb(int ideRequerimento) {
		boolean isLacErb = false;
		
		
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Lac.class)
				.createAlias("ideDocumentoObrigatorio", "doc")
				.createAlias("ideRequerimento", "req")
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("doc.ideDocumentoObrigatorio",LacEnum.LAC_ERB.getId()));
		List<Lac> lacList = lacDAO.listarPorCriteria(criteria);
				
			if(Util.isNullOuVazio(lacList)){
				isLacErb= false;
			}
			else{
				isLacErb= true;
			}
			
		return isLacErb;


	}
	public boolean isLacTransporte(int ideRequerimento) {
		boolean isLacTransporte = false;
		

		
		DetachedCriteria criteria = DetachedCriteria.forClass(Lac.class)
				.createAlias("ideDocumentoObrigatorio", "doc")
				.createAlias("ideRequerimento", "req")
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("doc.ideDocumentoObrigatorio",LacEnum.LAC_TRANSPORTADORA.getId()));
		List<Lac> lacList = lacDAO.listarPorCriteria(criteria);
				
			if(Util.isNullOuVazio(lacList)){
				isLacTransporte= false;
			}
			else{
				isLacTransporte= true;
			}
			
		return isLacTransporte;


	}


	

	public Collection<? extends Object> listarLacPorProcessoReenquadramento(ProcessoReenquadramento reenquadramento) {
		
		return null;
	}	
}