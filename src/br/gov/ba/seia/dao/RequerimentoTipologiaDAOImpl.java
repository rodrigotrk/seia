package br.gov.ba.seia.dao;

import java.util.Collection;

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

import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RequerimentoTipologiaDAOImpl {

	@Inject
	private IDAO<RequerimentoTipologia> requerimentoTipologiaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RequerimentoTipologia> listarRequerimentoTipologiaPor(VwConsultaProcesso vwProcesso)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoTipologia.class);
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("ideUnidadeMedidaTipologiaGrupo", "umtg", JoinType.INNER_JOIN)
			.createAlias("umtg.ideTipologiaGrupo", "tg", JoinType.INNER_JOIN)
			.createAlias("tg.idePotencialPoluicao", "pp", JoinType.INNER_JOIN)
			.createAlias("tg.ideTipologia", "t", JoinType.INNER_JOIN)
			.add(Restrictions.eq("r.ideRequerimento", vwProcesso.getIdeRequerimento()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideRequerimentoTipologia"),"ideRequerimentoTipologia")
				.add(Projections.property("umtg.ideUnidadeMedidaTipologiaGrupo"),"ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
				.add(Projections.property("tg.ideTipologiaGrupo"),"ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologiaGrupo")
				.add(Projections.property("pp.idePotencialPoluicao"),"ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.idePotencialPoluicao")
				.add(Projections.property("pp.sglPotencialPoluicao"),"ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.sglPotencialPoluicao")
				.add(Projections.property("pp.nomPotencialPoluicao"),"ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.nomPotencialPoluicao")
				.add(Projections.property("t.ideTipologia"),"ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.ideTipologia")
				.add(Projections.property("t.codTipologia"),"ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.codTipologia")
				.add(Projections.property("t.desTipologia"),"ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.desTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoTipologia.class))
		;
		
		return requerimentoTipologiaDAO.listarPorCriteria(criteria);
	}
	

}