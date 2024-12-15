package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtoAmbientalDAOImpl {

	@Inject
	IDAO<AtoAmbiental> atoAmbientalDAO;

	public Collection<AtoAmbiental> listarPorTipoAto(TipoAto tipoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		criteria
			.add(Restrictions.eq("ideTipoAto", tipoAto))
			.add(Restrictions.not(Restrictions.in("ideAtoAmbiental", new Integer[]{
					AtoAmbientalEnum.LAC.getId(),
					AtoAmbientalEnum.LOA.getId(),
					AtoAmbientalEnum.RLAC.getId(),
					AtoAmbientalEnum.ARLS.getId(),
					AtoAmbientalEnum.MNP.getId(),
					AtoAmbientalEnum.TLA.getId(),
					AtoAmbientalEnum.APE.getId(),
					AtoAmbientalEnum.COUT.getId(),
					AtoAmbientalEnum.CRF.getId(),
					AtoAmbientalEnum.INEXIGIBILIDADE.getId(),
					AtoAmbientalEnum.DIAP.getId(),
					AtoAmbientalEnum.DQC.getId(),
					AtoAmbientalEnum.DTRP.getId(),
					AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId(),
					AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId(),
					AtoAmbientalEnum.LIMPEZA_AREA.getId(),
			
			})))
			.addOrder(Order.asc("nomAtoAmbiental"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class));
		
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}

	public List<AtoAmbiental> listarAtoAmbientalPorDemanda(String nomAtoAmbiental, int first, int pageSize)	 {

		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);

		if (!"".equals(nomAtoAmbiental)) {
			criteria.add(Restrictions.ilike("nomAtoAmbiental", nomAtoAmbiental, MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc("nomAtoAmbiental"));
		return atoAmbientalDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
	}

	public Integer countAtoAmbiental(String nomAtoAmbiental) {

		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);

		if (!"".equals(nomAtoAmbiental)) {
			criteria.add(Restrictions.ilike("nomAtoAmbiental", nomAtoAmbiental, MatchMode.ANYWHERE));
		}
		if (nomAtoAmbiental == null)
			return 1;

		return atoAmbientalDAO.count(criteria);
	}

}