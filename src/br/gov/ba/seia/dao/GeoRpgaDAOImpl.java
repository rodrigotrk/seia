package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;

import br.gov.ba.seia.entity.GeoRpga;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GeoRpgaDAOImpl {
	
	@Inject
	IDAO<GeoRpga> geoRpgaDao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<GeoRpga> listarRpgaComPrecoPublicoUnitario() {
	
		DetachedCriteria criteria = DetachedCriteria.forClass(GeoRpga.class, "gR");
		
		criteria.createAlias("cerhPrecoPubUnitarioCollection", "cPPU", JoinType.INNER_JOIN);
		
		ProjectionList proj = Projections.projectionList()
				.add(Projections.property("gR.gid"),"gid")
				.add(Projections.property("gR.nomRpga"),"nomRpga");
		
		criteria.setProjection(Projections.distinct(proj))
		.setResultTransformer(Transformers.aliasToBean(GeoRpga.class));
		
		return geoRpgaDao.listarPorCriteria(criteria);
	}
}
