package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
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

import br.gov.ba.seia.entity.CerhCaptacaoAbastecimentoIndustrial;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 13/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoAbastecimentoIndustrialDAOImpl  {

	@Inject
	private IDAO<CerhCaptacaoAbastecimentoIndustrial> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoAbastecimentoIndustrial> listar(Integer ideCerhCaptacaoCaracterizacaoFinalidade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoAbastecimentoIndustrial.class)
				.createAlias("ideUnidadeMedida", "um", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideCerhCaptacaoCaracterizacaoFinalidade", "cccf")
				.createAlias("cccf.ideTipoFinalidadeUsoAgua", "tfua")
				.add(Restrictions.eq("cccf.ideCerhCaptacaoCaracterizacaoFinalidade", ideCerhCaptacaoCaracterizacaoFinalidade))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						
						.add(Projections.property("ideCerhCaptacaoAbastecimentoIndustrial"), "ideCerhCaptacaoAbastecimentoIndustrial")
						.add(Projections.property("nomProduto"), "nomProduto")
						.add(Projections.property("valConsumoAgua"), "valConsumoAgua")
						.add(Projections.property("valProducaoAnual"), "valProducaoAnual")
						.add(Projections.property("valProducaoDiaria"), "valProducaoDiaria")
						.add(Projections.property("um.ideUnidadeMedida"), "ideUnidadeMedida.ideUnidadeMedida")
						.add(Projections.property("um.codUnidadeMedida"), "ideUnidadeMedida.codUnidadeMedida")
						.add(Projections.property("um.nomUnidadadeMedida"), "ideUnidadeMedida.nomUnidadadeMedida")
						.add(Projections.property("cccf.ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade")
						.add(Projections.property("tfua.ideTipoFinalidadeUsoAgua"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoAbastecimentoIndustrial.class))
				;
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("DELETE FROM CerhCaptacaoAbastecimentoIndustrial c WHERE c.ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade = :ide");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide", cerhCaptacaoCaracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
		
		
		dao.executarQuery(hql.toString(), params);
	}


}
