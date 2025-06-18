package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralServidaoMinerariaDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralServidaoMineraria;
import br.gov.ba.seia.entity.FceLicenciamentoMineralServidaoMinerariaPK;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralServidaoMinerariaService {

	@Inject
	private FceLicenciamentoMineralServidaoMinerariaDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralServidaoMineraria(List<FceLicenciamentoMineralServidaoMineraria> lista)  {
		for(FceLicenciamentoMineralServidaoMineraria servidaoMineraria : lista){
			iDAO.salvarFceLicenciamentoMineralServidaoMineraria(servidaoMineraria);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralServidaoMineraria> listarFceLicenciamentoMineralServidaoMinerariaBy(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		List<FceLicenciamentoMineralServidaoMineraria> lista = iDAO.listarFceLicenciamentoMineralServidaoMinerariaBy(fceLicenciamentoMineral);
		for(FceLicenciamentoMineralServidaoMineraria servidao : lista){
			servidao.setEdicao();
			servidao.setConfirmado(true);
			servidao.setIdeFceLicenciamentoMineralServidaoMinerariaPK(new FceLicenciamentoMineralServidaoMinerariaPK(fceLicenciamentoMineral, servidao.getServidaoMineraria()));
		}
		return lista;
	}

}
