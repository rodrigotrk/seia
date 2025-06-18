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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.OrgaoEmissorAuto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrgaoEmissorAutoService {

	@Inject
	private IDAO<OrgaoEmissorAuto> orgaoEmissorAutoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OrgaoEmissorAuto> listarTodosOrgaoEmissorAuto() {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrgaoEmissorAuto.class);
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideOrgaoEmissorAuto"), "ideOrgaoEmissorAuto")
					.add(Projections.property("nomOrgaoEmissorAuto"), "nomOrgaoEmissorAuto")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(OrgaoEmissorAuto.class))
				;
		return orgaoEmissorAutoDAO.listarPorCriteria(criteria);
	}
	
	
}
