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

import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoTipologiaEmpreendimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoTipologiaEmpreendimentoDAOImpl {
	
	@Inject
	private IDAO<ReenquadramentoTipologiaEmpreendimento> dao;
	
	@Inject
	private IDAO<ReenquadramentoProcessoAto> reenquadramentoProcessoAtoDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReenquadramentoTipologiaEmpreendimento> listar(ReenquadramentoProcessoAto reenquadramentoProcessoAto)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoTipologiaEmpreendimento.class);
		
		criteria
			.createAlias("ideTipologia", "t", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto", reenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto"), "ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto")
				.add(Projections.property("t.ideTipologia"), "ideTipologia.ideTipologia")
				.add(Projections.property("t.desTipologia"), "ideTipologia.desTipologia")
				.add(Projections.property("t.ideTipologia"), "reenquadramentoTipologiaEmpreendimentoPK.ideTipologia")
				.add(Projections.property("ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto"), "reenquadramentoTipologiaEmpreendimentoPK.ideReenquadramentoProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoTipologiaEmpreendimento.class))
		;

		return dao.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoTipologiaEmpreendimento reenquadramentoTipologiaEmpreendimento)  {
		dao.salvar(reenquadramentoTipologiaEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoProcessoAto.class, "rpa")

				.createAlias("rpa.ideReenquadramentoProcesso", "rp", JoinType.INNER_JOIN)
				.add(Restrictions.eq("rp.ideReenquadramentoProcesso",
						reenquadramentoProcesso.getIdeReenquadramentoProcesso()))
				.setProjection(
						Projections.projectionList().add(Projections.property("rpa.ideReenquadramentoProcessoAto"),
								"ideReenquadramentoProcessoAto"))
				.setResultTransformer(
						new AliasToNestedBeanResultTransformer(ReenquadramentoProcessoAto.class));

		Collection<ReenquadramentoProcessoAto> rpaCollection = reenquadramentoProcessoAtoDAO.listarPorCriteria(criteria);
		for (ReenquadramentoProcessoAto rpa : rpaCollection) {

			StringBuilder sql = new StringBuilder();

			sql.append("delete from ReenquadramentoTipologiaEmpreendimento rtf"
					+ " where rtf.ideReenquadramentoProcessoAto" + " = :ideReenquadramentoProcessoAto");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideReenquadramentoProcessoAto", rpa);
			reenquadramentoProcessoAtoDAO.executarQuery(sql.toString(), params);
		}
	}
	
}