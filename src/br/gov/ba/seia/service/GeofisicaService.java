package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.GeofisicaDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.Geofisica;
import br.gov.ba.seia.entity.SubstanciaMineral;

/**
 * Service de {@link SubstanciaMineral}
 * 
 * @author alexandre.queiroz
 * @since 10/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GeofisicaService {

	@Inject
	private GeofisicaDAOImpl geofisicaDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Geofisica> listarGeofisica()  {
		return geofisicaDAO.listarGeofisica();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Geofisica> listarGeofisicaBy(FcePesquisaMineral idefcePesquisaMineral) {
		return geofisicaDAO.listarGeofisicaBy(idefcePesquisaMineral);
	}
	
}