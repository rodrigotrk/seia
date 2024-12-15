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

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoDadosIrrigacao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 13/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoDadosIrrigacaoDAOImpl extends AbstractDAO<CerhCaptacaoDadosIrrigacao>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhCaptacaoDadosIrrigacao> dao;

	@Override
	protected IDAO<CerhCaptacaoDadosIrrigacao> getDAO() {
		return dao;
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoDadosIrrigacao> listar(Integer ideCerhCaptacaoCaracterizacaoFinalidade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoDadosIrrigacao.class)
				.createAlias("ideMetodoIrrigacao", "mi")
				.createAlias("ideTipologiaAtividade", "ta")
				.createAlias("ideCerhCaptacaoCaracterizacaoFinalidade", "cccf")
				.createAlias("cccf.ideTipoFinalidadeUsoAgua", "tfua")
				.add(Restrictions.eq("cccf.ideCerhCaptacaoCaracterizacaoFinalidade", ideCerhCaptacaoCaracterizacaoFinalidade))
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhCaptacaoDadosIrrigacao"), "ideCerhCaptacaoDadosIrrigacao")
						.add(Projections.property("valAreaIrrigada"), "valAreaIrrigada")

						.add(Projections.property("mi.ideMetodoIrrigacao"), "ideMetodoIrrigacao.ideMetodoIrrigacao")
						.add(Projections.property("mi.dscMetodoIrrigacao"), "ideMetodoIrrigacao.dscMetodoIrrigacao")

						.add(Projections.property("ta.ideTipologiaAtividade"), "ideTipologiaAtividade.ideTipologiaAtividade")
						.add(Projections.property("ta.dscTipologiaAtividade"), "ideTipologiaAtividade.dscTipologiaAtividade")
						.add(Projections.property("ta.indExcluido"), "ideTipologiaAtividade.indExcluido")
						.add(Projections.property("ta.dtcCriacao"), "ideTipologiaAtividade.dtcCriacao")
						.add(Projections.property("ta.dtcExclusao"), "ideTipologiaAtividade.dtcExclusao")
						.add(Projections.property("ta.numConsumoDiario"), "ideTipologiaAtividade.numConsumoDiario")
						.add(Projections.property("ta.ideTipologia.ideTipologia"), "ideTipologiaAtividade.ideTipologia.ideTipologia")

						.add(Projections.property("cccf.ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade")
						.add(Projections.property("tfua.ideTipoFinalidadeUsoAgua"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoDadosIrrigacao.class))
				;
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade) {
		StringBuilder hql = new StringBuilder();

		hql.append("DELETE FROM CerhCaptacaoDadosIrrigacao c WHERE c.ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade = :ide");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide", cerhCaptacaoCaracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());


		dao.executarQuery(hql.toString(), params);
	}



}
