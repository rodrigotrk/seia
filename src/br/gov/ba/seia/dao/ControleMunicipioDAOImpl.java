package br.gov.ba.seia.dao;

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

import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleMunicipioDAOImpl {

	@Inject
	private IDAO<Municipio> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listarMunicipiosDaBahia() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class);
		criteria
			.createAlias("ideTipoMunicipio", "ideTipoMunicipio")
			.createAlias("ideSituacaoMunicipio", "ideSituacaoMunicipio")
			.createAlias("ideEstado", "ideEstado")
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideMunicipio"), "ideMunicipio")
				.add(Projections.property("nomMunicipio"), "nomMunicipio")
				.add(Projections.property("indBloqueioDQC"),"indBloqueioDQC")
				.add(Projections.property("indEstadoEmergencia"),"indEstadoEmergencia")
				.add(Projections.property("numCep"),"numCep")
				.add(Projections.property("coordGeobahiaMunicipio"),"coordGeobahiaMunicipio")
				.add(Projections.property("ideTipoMunicipio.ideTipoMunicipio"),"ideTipoMunicipio.ideTipoMunicipio")
				.add(Projections.property("ideSituacaoMunicipio.ideSituacaoMunicipio"),"ideSituacaoMunicipio.ideSituacaoMunicipio")
				.add(Projections.property("ideEstado.ideEstado"),"ideEstado.ideEstado")
			)
			.add(Restrictions.eq("ideEstado.ideEstado", EstadoEnum.BAHIA.getId()))
			.add(Restrictions.eq("indExcluido", false))
			.addOrder(Order.asc("nomMunicipio"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class))
		;
		
		return dao.listarPorCriteria(criteria);
	}
}
