package br.gov.ba.seia.service;

import java.util.List;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeOrganismo;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;
import br.gov.ba.seia.entity.Organismo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * Service para retornar as informações cadastradas de {@link Organismo} no banco de dados.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 03/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrganismoService {
	@Inject
	private IDAO<Organismo> organismoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Organismo> listarOrganismo(){
		DetachedCriteria criteria = DetachedCriteria.forClass(Organismo.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return organismoIDAO.listarPorCriteria(criteria, Order.asc("ideOrganismo"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Organismo> listarOrganismoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeOrganismo.class)
				.createAlias("organismo", "org")
				.add(Restrictions.eq("fceAquiculturaLicencaTipoAtividadeTipoProducao.ideFceAquiculturaLicencaTipoAtividadeTipoProducao", 
						fceAquiculturaLicencaTipoAtividadeTipoProducao.getIdeFceAquiculturaLicencaTipoAtividadeTipoProducao()))
						.setProjection(Projections.projectionList()
								.add(Projections.property("org.ideOrganismo"), "ideOrganismo")
								.add(Projections.property("org.nomOrganismo"), "nomOrganismo")
								.add(Projections.property("org.indAtivo"), "indAtivo")
								)
								.setResultTransformer(new AliasToNestedBeanResultTransformer(Organismo.class));
		return organismoIDAO.listarPorCriteria(criteria);
	}
}