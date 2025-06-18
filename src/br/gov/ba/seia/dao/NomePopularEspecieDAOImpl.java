package br.gov.ba.seia.dao;

import java.util.List;

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

import br.gov.ba.seia.entity.EspecieFlorestal;
import br.gov.ba.seia.entity.EspecieSupressao;
import br.gov.ba.seia.entity.NomePopularEspecie;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NomePopularEspecieDAOImpl {
	
	@Inject
	private IDAO<NomePopularEspecie> nomePopularEspecieDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NomePopularEspecie> listarNomePopularEspecie(EspecieSupressao especieSupressao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NomePopularEspecie.class);
		
		criteria.createAlias("especieSupressaoNomePopularEspecieCollection", "esnp", JoinType.INNER_JOIN)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideNomePopularEspecie"), "ideNomePopularEspecie")
				.add(Projections.property("nomPopularEspecie"), "nomPopularEspecie"))
			.add(Restrictions.eq("esnp.especieSupressaoNomePopularEspeciePK.ideEspecieSupressao.ideEspecieSupressao", especieSupressao.getIdeEspecieSupressao()))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NomePopularEspecie.class));
		
		return nomePopularEspecieDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NomePopularEspecie> listarNomePopularEspecie(EspecieFlorestal especieFlorestal)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NomePopularEspecie.class);
		
		criteria.createAlias("especieFlorestalNomePopularEspecieCollection", "efnp", JoinType.INNER_JOIN)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideNomePopularEspecie"), "ideNomePopularEspecie")
				.add(Projections.property("nomPopularEspecie"), "nomPopularEspecie"))
			.add(Restrictions.eq("efnp.especieFlorestalNomePopularEspeciePK.ideEspecieFlorestal.ideEspecieFlorestal", especieFlorestal.getIdeEspecieFlorestal()))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NomePopularEspecie.class));
		
		return nomePopularEspecieDAO.listarPorCriteria(criteria);
	}
}