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
import br.gov.ba.seia.entity.ReenquadramentoTipoFinalidadeUsoAgua;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoTipoFinalidadeUsoAguaDAOImpl {
	
	
	@Inject
	private IDAO<ReenquadramentoTipoFinalidadeUsoAgua> reenquadramentoTipoFinalidadeUsoAguaDAO;
	
	@Inject
	private IDAO<ReenquadramentoProcessoAto> reenquadramentoProcessoAtoDAO;
	
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

			sql.append("delete from ReenquadramentoTipoFinalidadeUsoAgua rtf"
					+ " where rtf.ideReenquadramentoProcessoAto" + " = :ideReenquadramentoProcessoAto");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideReenquadramentoProcessoAto", rpa);
			reenquadramentoTipoFinalidadeUsoAguaDAO.executarQuery(sql.toString(), params);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReenquadramentoTipoFinalidadeUsoAgua> listar(ReenquadramentoProcessoAto reenquadramentoProcessoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoTipoFinalidadeUsoAgua.class);

		criteria
			.createAlias("ideTipoFinalidadeUsoAgua", "tf", JoinType.INNER_JOIN)
			.createAlias("ideReenquadramentoProcessoAto", "rpa", JoinType.INNER_JOIN)
			.add(Restrictions.eq("rpa.ideReenquadramentoProcessoAto", reenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("rpa.ideReenquadramentoProcessoAto"), "ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto")
				.add(Projections.property("tf.nomTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
				.add(Projections.property("tf.ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
				.add(Projections.property("tf.ideTipoFinalidadeUsoAgua"), "reenquadramentoTipoFinalidadeUsoAguaPK.ideTipoFinalidadeUsoAgua")
				.add(Projections.property("rpa.ideReenquadramentoProcessoAto"), "reenquadramentoTipoFinalidadeUsoAguaPK.ideReenquadramentoProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoTipoFinalidadeUsoAgua.class));

		return reenquadramentoTipoFinalidadeUsoAguaDAO.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoTipoFinalidadeUsoAgua rtfua)  {
		reenquadramentoTipoFinalidadeUsoAguaDAO.salvar(rtfua);
	}
	
	
	
}