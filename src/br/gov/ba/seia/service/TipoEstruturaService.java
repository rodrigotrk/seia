package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoEstruturaDAOImpl;
import br.gov.ba.seia.entity.TipoEstrutura;

/**
 * Service de {@link TipoEstrutura}
 * 
 * @author eduardo.fernandes
 * @since 13/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoEstruturaService {

	@Inject
	private TipoEstruturaDAOImpl tipoEstruturaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoEstrutura> listarTipoEstrutura() throws Exception {
		return tipoEstruturaIDAO.listarTipoEstrutura();
	}
}
