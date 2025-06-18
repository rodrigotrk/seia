package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.MotivoReservaAgua;
import br.gov.ba.seia.entity.MotivoStatusReservaAgua;
import br.gov.ba.seia.entity.ReservaAgua;
import br.gov.ba.seia.enumerator.StatusReservaAguaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoReservaAguaService {

	@Inject
	private IDAO<MotivoReservaAgua> motivoReservaAguaIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MotivoReservaAgua buscarMotivoReservaAgua(ReservaAgua reservaAgua) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoReservaAgua.class);
		criteria.add(Restrictions.eq("ideMotivoReservaAgua", reservaAgua.getIdeMotivoReservaAgua()));
		return motivoReservaAguaIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MotivoReservaAgua> listarMotivoReservaAgua(StatusReservaAguaEnum statusReservaAguaEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoStatusReservaAgua.class)
				.createAlias("ideMotivoReservaAgua", "mra")
				.createAlias("ideStatusReservaAgua", "sra")
				.add(Restrictions.eq("sra.ideStatusReservaAgua", statusReservaAguaEnum.getIde()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("mra.ideMotivoReservaAgua"), "ideMotivoReservaAgua")
						.add(Projections.property("mra.nomMotivoReservaAgua"), "nomMotivoReservaAgua")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(MotivoReservaAgua.class));
		return motivoReservaAguaIDAO.listarPorCriteria(criteria);
	}
	
}