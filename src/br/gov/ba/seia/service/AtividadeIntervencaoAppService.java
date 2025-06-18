package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.AtividadeIntervencaoAppDAOImpl;
import br.gov.ba.seia.entity.AtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;
import br.gov.ba.seia.entity.Requerimento;

/**
 * Service da classe {@link AtividadeIntervencaoApp}.
 * 
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtividadeIntervencaoAppService {
	
	@Inject
	private AtividadeIntervencaoAppDAOImpl dao;

	/**
	 * Método que retorna uma lista de {@link AtividadeIntervencaoApp} com <b>ind_excluido = false</b> associados à {@link CaracteristicaIntervencaoApp}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param caracteristicaIntervencaoApp [1 - 'Utilidade pública', 2 - 'Interesse social', 3 - 'Baixo impacto ambiental']
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtividadeIntervencaoApp> listarBy(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp) {
		return dao.listarBy(caracteristicaIntervencaoApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtividadeIntervencaoApp> listarPor(Requerimento requerimento) {
		return dao.listarAtividadePor(requerimento);
	}
}
