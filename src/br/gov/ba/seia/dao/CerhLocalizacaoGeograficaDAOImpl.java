package br.gov.ba.seia.dao;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 * @since 27/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLocalizacaoGeograficaDAOImpl extends AbstractDAO<CerhLocalizacaoGeografica>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhLocalizacaoGeografica> dao;
	
	@Inject
	private IDAO<Integer> ideDAO;

	@Override
	protected IDAO<CerhLocalizacaoGeografica> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CerhLocalizacaoGeografica> listarParaHistorico(Cerh c,TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhLocalizacaoGeografica.class)
			.createAlias("ideCerhTipoUso", "ideCerhTipoUso")
			.createAlias("gid", "ideGeoRpga", JoinType.LEFT_OUTER_JOIN)
		
			.createAlias("ideCerhTipoUso.ideCerh", "ideCerh")	
			.createAlias("ideLocalizacaoGeografica", "ideLocalizacaoGeografica")
			.createAlias("ideLocalizacaoGeografica.dadoGeograficoCollection", "dadoGeografico")
			
			.createAlias("ideCerhBarragemCaracterizacao", "ideCerhBarragemCaracterizacao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoCaracterizacao", "ideCerhCaptacaoCaracterizacao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhIntervencaoCaracterizacao", "ideCerhIntervencaoCaracterizacao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhLancamentoEfluenteCaracterizacao", "ideCerhLancamentoEfluenteCaracterizacao", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerh.ideCerh",c.getId()))
			.add(Restrictions.eq("ideCerhTipoUso.ideTipoUsoRecursoHidrico", new TipoUsoRecursoHidrico(tipoUsoRecursoHidricoEnum.getId())))
			
			.setProjection(
				Projections.projectionList()
					.add(Projections.alias(Projections.property("ideObjetoPai"), "ideObjetoPai"))	
					.add(Projections.alias(Projections.property("ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica"))
					.add(Projections.alias(Projections.property("ideLocalizacaoGeografica.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica"))
					.add(Projections.alias(Projections.property("ideCerhTipoUso.ideCerhTipoUso"), "ideCerhTipoUso.ideCerhTipoUso"))
					.add(Projections.alias(Projections.property("ideCerhTipoUso.ideTipoUsoRecursoHidrico"), "ideCerhTipoUso.ideTipoUsoRecursoHidrico"))
					.add(Projections.alias(Projections.property("ideCerhTipoUso.ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico"), "ideCerhTipoUso.ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico"))
					.add(Projections.alias(Projections.property("ideCerhBarragemCaracterizacao.ideCerhBarragemCaracterizacao"), "ideCerhBarragemCaracterizacao.ideCerhBarragemCaracterizacao"))
					.add(Projections.alias(Projections.property("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao"))
					.add(Projections.alias(Projections.property("ideCerhIntervencaoCaracterizacao.ideCerhIntervencaoCaracterizacao"), "ideCerhIntervencaoCaracterizacao.ideCerhIntervencaoCaracterizacao"))
					.add(Projections.alias(Projections.property("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao"), "ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao"))
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLocalizacaoGeografica.class));
		return	dao.listarPorCriteria(criteria, Order.asc("ideCerhLocalizacaoGeografica"));
	}

	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLocalizacaoGeografica> listar(CerhProcesso cerhProcesso,
			TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhLocalizacaoGeografica.class)
				.createAlias("ideCerhProcesso", "cp")
				.createAlias("ideCerhTipoUso", "ctu")
				.createAlias("ctu.ideTipoUsoRecursoHidrico", "tuh", JoinType.INNER_JOIN)
				.createAlias("ctu.ideCerh", "c");

		if (tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL)
				|| tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA)) {
			criteria.createAlias("ideCerhCaptacaoCaracterizacao", "captacao");
		}

		else if (tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.INTERVENCAO)) {
			criteria.createAlias("ideCerhIntervencaoCaracterizacao", "intervencao");
		}

		else if (tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.BARRAGEM)) {
			criteria.createAlias("ideCerhBarragemCaracterizacao", "barragem");
		}

		else if (tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE)) {
			criteria.createAlias("ideCerhLancamentoEfluenteCaracterizacao", "lancamento");
		}

		
		  criteria.add(Restrictions.eq("c.ideCerh", cerhProcesso.getIdeCerh().getIdeCerh()))
		  .add(Restrictions.eq("tuh.ideTipoUsoRecursoHidrico", tipoUsoRecursoHidricoEnum.getId()))
		  .add(Restrictions.eq("cp.ideCerhProcesso", cerhProcesso.getIdeCerhProcesso()));
		 

		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLocalizacaoGeografica> listar(CerhTipoUso cerhTipoUso)  {
		return 
			dao.listarPorCriteria(
				DetachedCriteria.forClass(CerhLocalizacaoGeografica.class)
					.createAlias("ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
					.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
		
					.createAlias("ideCerhProcesso", "cerhProcesso", JoinType.LEFT_OUTER_JOIN)
					.createAlias("cerhProcesso.ideCerhSituacaoRegularizacao", "cerhSituacaoRegularizacao", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ideCerhTipoUso.ideCerhTipoUso", cerhTipoUso.getIdeCerhTipoUso()))
					.setProjection(
						Projections.projectionList()
							.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
							.add(Projections.property("ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica")
							.add(Projections.property("ideCerhProcesso.ideCerhProcesso"), "ideCerhProcesso.ideCerhProcesso")
							.add(Projections.property("ideCerhTipoUso.ideCerhTipoUso"), "ideCerhTipoUso.ideCerhTipoUso")
							.add(Projections.property("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
							.add(Projections.property("lg.dtcCriacao"), "ideLocalizacaoGeografica.dtcCriacao")
							.add(Projections.property("lg.indExcluido"), "ideLocalizacaoGeografica.indExcluido")
							.add(Projections.property("lg.dtcExclusao"), "ideLocalizacaoGeografica.dtcExclusao")
							.add(Projections.property("lg.fonteCoordenada"), "ideLocalizacaoGeografica.fonteCoordenada")
							.add(Projections.property("lg.desLocalizacaoGeografica"), "ideLocalizacaoGeografica.desLocalizacaoGeografica")
							
							.add(Projections.property("sc.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
							.add(Projections.property("sc.srid"), "ideLocalizacaoGeografica.ideSistemaCoordenada.srid")
							
							.add(Projections.property("cerhProcesso.ideCerhProcesso"), "ideCerhProcesso.ideCerhProcesso")
							
							.add(Projections.property("cerhSituacaoRegularizacao.ideCerhSituacaoRegularizacao"), "ideCerhProcesso.ideCerhSituacaoRegularizacao.ideCerhSituacaoRegularizacao")
							.add(Projections.property("cerhSituacaoRegularizacao.ideObjetoPai"), "ideCerhProcesso.ideCerhSituacaoRegularizacao.ideObjetoPai")
							.add(Projections.property("cerhSituacaoRegularizacao.dscSituacaoRegularizacao"),"ideCerhProcesso.ideCerhSituacaoRegularizacao.dscSituacaoRegularizacao")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLocalizacaoGeografica.class)));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Integer ideCerhLocalizacaoGeografica)  {
		StringBuilder hql = new StringBuilder();
		
		hql.append("DELETE FROM CerhLocalizacaoGeografica c WHERE c.ideCerhLocalizacaoGeografica = :ideCerhLocalizacaoGeografica");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCerhLocalizacaoGeografica", ideCerhLocalizacaoGeografica);
		
		dao.executarQuery(hql.toString(), params);		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Integer> listarCerhLocalizacaoGeografica(Collection<Integer> ideCerhTipoUsoCollection) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhLocalizacaoGeografica.class)
			.createAlias("ideCerhTipoUso", "ctu")
			.add(Restrictions.in("ctu.ideCerhTipoUso", ideCerhTipoUsoCollection))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica"))
			;
		return ideDAO.listarPorCriteria(criteria);
	}

}
