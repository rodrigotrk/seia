package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieSupressaoAutorizacaoDAOImpl {

	@Inject
	private IDAO<EspecieSupressaoAutorizacao> especieSupressaoAutorizacaoDAO;
	
	public List<EspecieSupressaoAutorizacao> listarEspecieSupressaoAutorizacaoPor(ProcessoAtoConcedido processoAtoConcedido) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieSupressaoAutorizacao.class);
		
		criteria
			.createAlias("ideEspecieSupressao","es", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNomePopularEspecie","npe", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoAtoConcedido","pac", JoinType.INNER_JOIN)
			.createAlias("ideProduto","prd", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("pac.ideProcessoAtoConcedido", processoAtoConcedido.getIdeProcessoAtoConcedido()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEspecieSupressaoAutorizacao"), "ideEspecieSupressaoAutorizacao")
				.add(Projections.property("volumeTotalForaApp"), "volumeTotalForaApp")
				.add(Projections.property("volumeTotalEmApp"), "volumeTotalEmApp")
				.add(Projections.property("es.ideEspecieSupressao"), "ideEspecieSupressao.ideEspecieSupressao")
				.add(Projections.property("es.nomEspecieSupressao"), "ideEspecieSupressao.nomEspecieSupressao")
				.add(Projections.property("npe.ideNomePopularEspecie"), "ideNomePopularEspecie.ideNomePopularEspecie")
				.add(Projections.property("npe.nomPopularEspecie"), "ideNomePopularEspecie.nomPopularEspecie")
				.add(Projections.property("prd.ideProduto"), "ideProduto.ideProduto")
				.add(Projections.property("prd.dscProduto"), "ideProduto.dscProduto")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieSupressaoAutorizacao.class))
		;
		
		return especieSupressaoAutorizacaoDAO.listarPorCriteria(criteria);
	}
	
	public List<EspecieSupressaoAutorizacao> listarPorProcessoAto(ProcessoAto processoAto) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieSupressaoAutorizacao.class)
			.createAlias("ideEspecieSupressao","es", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNomePopularEspecie","npe", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoAto","pa", JoinType.INNER_JOIN)
			.createAlias("ideProduto","prd", JoinType.INNER_JOIN)
		
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEspecieSupressaoAutorizacao"), "ideEspecieSupressaoAutorizacao")
				.add(Projections.property("volumeTotalForaApp"), "volumeTotalForaApp")
				.add(Projections.property("volumeTotalEmApp"), "volumeTotalEmApp")
				.add(Projections.property("es.ideEspecieSupressao"), "ideEspecieSupressao.ideEspecieSupressao")
				.add(Projections.property("es.nomEspecieSupressao"), "ideEspecieSupressao.nomEspecieSupressao")
				.add(Projections.property("npe.ideNomePopularEspecie"), "ideNomePopularEspecie.ideNomePopularEspecie")
				.add(Projections.property("npe.nomPopularEspecie"), "ideNomePopularEspecie.nomPopularEspecie")
				.add(Projections.property("prd.ideProduto"), "ideProduto.ideProduto")
				.add(Projections.property("prd.dscProduto"), "ideProduto.dscProduto")
				.add(Projections.property("pa.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieSupressaoAutorizacao.class))
		
			.add(Restrictions.eq("pa.ideProcessoAto", processoAto.getIdeProcessoAto()));
		
		return especieSupressaoAutorizacaoDAO.listarPorCriteria(criteria);
	}
}
