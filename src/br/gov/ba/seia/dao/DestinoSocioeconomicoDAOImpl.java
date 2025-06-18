package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;

import br.gov.ba.seia.entity.DestinoSocioeconomico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DestinoSocioeconomicoDAOImpl {
	
	@Inject
	private IDAO<DestinoSocioeconomico> destinoSocioeconomicoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoSocioeconomico> listarDestinoSocioeconomico()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DestinoSocioeconomico.class);
		return destinoSocioeconomicoDAO.listarPorCriteria(criteria);
	}
	
}