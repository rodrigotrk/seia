package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ServidaoMinerariaDAOImpl;
import br.gov.ba.seia.entity.ServidaoMineraria;

/**
 * Service de {@link ServidaoMineraria}
 * 
 * @author eduardo.fernandes
 * @since 13/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServidaoMinerariaService {

	@Inject
	private ServidaoMinerariaDAOImpl servidaoMinerariaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ServidaoMineraria> listarServidaoMineraria() throws Exception {
		return servidaoMinerariaIDAO.listarServidaoMineraria();
	}
}
