package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralTecnicaLavraDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTecnicaLavra;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralTecnicaLavraService {

	@Inject
	private FceLicenciamentoMineralTecnicaLavraDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralTecnicaLavra(List<FceLicenciamentoMineralTecnicaLavra> lista)  {
		if(!Util.isNullOuVazio(lista)){
			for(FceLicenciamentoMineralTecnicaLavra tecnicaLavra : lista){
				iDAO.salvarFceLicenciamentoMineralTecnicaLavra(tecnicaLavra);
			}
		}
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceLicenciamentoMineralTecnicaLavra(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		iDAO.excluirListaFceLicenciamentoMineralTecnicaLavra(fceLicenciamentoMineral);
	}

}
