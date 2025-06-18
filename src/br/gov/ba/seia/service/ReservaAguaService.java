package br.gov.ba.seia.service;

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
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceReservaAgua;
import br.gov.ba.seia.entity.ReservaAgua;
import br.gov.ba.seia.enumerator.StatusReservaAguaEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservaAguaService {

	@Inject
	private IDAO<ReservaAgua> reservaAguaIDAO;

	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ReservaAgua buscarUltimaReservaAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica)  {
		DetachedCriteria criteria = obterCriteria(fceOutorgaLocalizacaoGeografica)
				.setProjection(Projections.max("dtcStatusReservaAgua"))
				;
		return reservaAguaIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ReservaAgua> listarReservaAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica)  {
		DetachedCriteria criteria = obterCriteria(fceOutorgaLocalizacaoGeografica);
		return reservaAguaIDAO.listarPorCriteria(criteria);
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceOutorgaLocalizacaoGeografica
	 * @return
	 * @since 22/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a>
	 */
	private DetachedCriteria obterCriteria(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		return DetachedCriteria.forClass(ReservaAgua.class)
				.createAlias("ideFceReservaAgua", "fra")
				.createAlias("ideStatusReservaAgua", "sra")
				.createAlias("idePessoaStatusReservaAgua", "func")
				.createAlias("ideMotivoReservaAgua", "mrs", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideFceOutorgaLocalizacaoGeografica.ideFceOutorgaLocalizacaoGeografica", fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica()));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ReservaAgua> listarReservaAguaBy(Integer ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReservaAgua.class)
				.createAlias("ideFceOutorgaLocalizacaoGeografica", "folg")
				.createAlias("ideFceReservaAgua", "fra")
				.createAlias("ideStatusReservaAgua", "sra")
				.createAlias("folg.ideFce", "f")
				.createAlias("f.ideRequerimento", "r")
				.createAlias("r.processoCollection", "p");

		DetachedCriteria subCriteria = DetachedCriteria.forClass(FceReservaAgua.class,"fra2")
				.createAlias("fra2.reservaAguaCollection", "ra2")
				.createAlias("fra2.ideFce", "f2")
				.createAlias("f2.ideRequerimento", "r2")
				.createAlias("r2.processoCollection", "p2")
				.add(Restrictions.eq("p2.ideProcesso", ideProcesso))
				.setProjection(
						Projections.projectionList()
						.add(Projections.max("fra2.ideFceReservaAgua"))
						);
		criteria.add(Subqueries.propertyEq("fra.ideFceReservaAgua", subCriteria))
		;

		criteria.add(Restrictions.eq("sra.ideStatusReservaAgua", StatusReservaAguaEnum.RESERVADO.getIde()))
		.add(Restrictions.eq("p.ideProcesso", ideProcesso))
		;
		return reservaAguaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarReservaAgua(ReservaAgua reservaAgua)  {
		reservaAguaIDAO.salvarOuAtualizar(reservaAgua);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirReservaAgua(ReservaAgua reservaAgua)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceReservaAgua", reservaAgua.getIdeFceReservaAgua().getIdeFceReservaAgua());
		reservaAguaIDAO.executarNamedQuery("ReservaAgua.excluirByIdeFceReservaAgua", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirReservaAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceOutorgaLocalizacaoGeografica", fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica());
		reservaAguaIDAO.executarNamedQuery("ReservaAgua.excluirByIdeFceOutorgaLocalizacaoGeografica", params);
	}
}