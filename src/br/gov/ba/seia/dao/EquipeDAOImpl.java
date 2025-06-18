package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.IntegranteEquipe;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EquipeDAOImpl {
	
	@Inject
	private IDAO<Equipe> equipeDAO;
	
	@EJB
	private IntegranteEquipeDAOImpl integranteEquipeDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Equipe salvarEquipe(Equipe equipe)  {
		equipeDAO.salvar(equipe);
		return equipe;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Equipe buscarEquipe(Integer ideProcesso, Integer ideArea)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Equipe.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			
			.add(Property.forName("ideEquipe").eq(
				DetachedCriteria.forClass(Equipe.class)
					.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
					.createAlias("ideArea", "a", JoinType.INNER_JOIN)
					.add(Restrictions.eq("p.ideProcesso", ideProcesso))
					.add(Restrictions.eq("a.ideArea", ideArea))
					.setProjection(Projections.max("ideEquipe"))
				)
			)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEquipe"),"ideEquipe")
				.add(Projections.property("dtcExclusaoEquipe"),"dtcExclusaoEquipe")
				.add(Projections.property("dtcFormacaoEquipe"),"dtcFormacaoEquipe")
				.add(Projections.property("indAtivo"),"indAtivo")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Equipe.class))
		;

		return equipeDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Equipe buscarEquipeAtual(Integer ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Equipe.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("a.ideAreaPai", "ap", JoinType.INNER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.INNER_JOIN)
			
			.add(Property.forName("ideEquipe").eq(
				DetachedCriteria.forClass(Equipe.class)
					.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
					.add(Restrictions.eq("p.ideProcesso", ideProcesso))
					.setProjection(Projections.max("ideEquipe"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEquipe"),"ideEquipe")
				.add(Projections.property("dtcExclusaoEquipe"),"dtcExclusaoEquipe")
				.add(Projections.property("dtcFormacaoEquipe"),"dtcFormacaoEquipe")
				.add(Projections.property("indAtivo"),"indAtivo")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("ap.ideArea"),"ideArea.ideAreaPai.ideArea")
				.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Equipe.class))
		;
		
		return equipeDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Equipe> listarEquipe(Integer ideProcesso) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Equipe.class);
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEquipe"),"ideEquipe")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Equipe.class))
		;
		
		Collection<Equipe> listaEquipe = equipeDAO.listarPorCriteria(criteria);
		for(Equipe e : listaEquipe) {
			Collection<IntegranteEquipe> listaIntegranteEquipe = integranteEquipeDAOImpl.listarIntegranteEquipeComListaAtos(e.getIdeEquipe());
			e.setIntegranteEquipeCollection(listaIntegranteEquipe);
		}
		
		return listaEquipe;
	}
}