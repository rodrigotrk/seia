package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.Geofisica;

public class GeofisicaDAOImpl {

	@Inject
	private IDAO<Geofisica> geofisicaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Geofisica> listarGeofisica()  {
		return geofisicaDAO.listarPorCriteria(DetachedCriteria.forClass(Geofisica.class), Order.asc("ideGeofisica"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Geofisica> listarGeofisicaBy(FcePesquisaMineral idefcePesquisaMineral)  {	
	 DetachedCriteria  detachedCriteria= DetachedCriteria.forClass(Geofisica.class)
		.createAlias("listaFcePesquisaMineralProspeccaoGeofisica", "fcePesquisaMineralProspeccaoGeofisica",JoinType.INNER_JOIN)
		.createAlias("fcePesquisaMineralProspeccaoGeofisica.ideFcePesquisaMineralProspeccao", "ideFcePesquisaMineralProspeccao",JoinType.INNER_JOIN)
		.createAlias("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineral", "ideFcePesquisaMineral",JoinType.INNER_JOIN)
		.add(Restrictions.eq("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineral.ideFcePesquisaMineral", idefcePesquisaMineral.getIdeFcePesquisaMineral()));
	 return geofisicaDAO.listarPorCriteria(detachedCriteria);
	}
}
