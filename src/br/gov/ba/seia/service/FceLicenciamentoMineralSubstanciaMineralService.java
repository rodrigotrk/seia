package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralSubstanciaMineralDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralSubstanciaMineralTipologia;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;

/**
 * @author eduardo.fernandes
 * @since 13/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralSubstanciaMineralService {

	@Inject
	private FceLicenciamentoMineralSubstanciaMineralDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralSubstanciaMineral(List<FceLicenciamentoMineralSubstanciaMineralTipologia> lista)  {
		for(FceLicenciamentoMineralSubstanciaMineralTipologia substanciaMineral : lista){
			iDAO.salvarFceLicenciamentoMineralSubstanciaMineral(substanciaMineral);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralSubstanciaMineralTipologia> listarFceLicenciamentoMineralSubstanciaMineralBy(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		List<FceLicenciamentoMineralSubstanciaMineralTipologia> lista = iDAO.listarFceLicenciamentoMineralSubstanciaMineralBy(fceLicenciamentoMineral);
		for(FceLicenciamentoMineralSubstanciaMineralTipologia substanciaMineral : lista){
			substanciaMineral.setEdicao();
			substanciaMineral.setConfirmado(true);
		}
		return lista;
	}

	/**
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		iDAO.excluirListaFceLicenciamentoMineralSubstanciaMineral(fceLicenciamentoMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineral fceLicenciamentoMineral, SubstanciaMineralTipologia substanciaMineralTipologia)  {
		iDAO.excluirFceLicenciamentoMineralSubstanciaMineral(fceLicenciamentoMineral, substanciaMineralTipologia);
	}
	
	
}
