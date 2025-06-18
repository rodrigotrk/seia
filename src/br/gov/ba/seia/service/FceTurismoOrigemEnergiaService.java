package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceTurismo;
import br.gov.ba.seia.entity.FceTurismoOrigemEnergia;
import br.gov.ba.seia.entity.TipoOrigemEnergia;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 04/12/2014
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceTurismoOrigemEnergiaService {

	@Inject
	private IDAO<FceTurismoOrigemEnergia> fceTurismoOrigemEnergiaIDAO;

	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceTurismoOrigemEnergia(FceTurismo fceTurismo, List<TipoOrigemEnergia> listaOrigemEnergia) {
		for(TipoOrigemEnergia origemEnergia : listaOrigemEnergia){
			FceTurismoOrigemEnergia fceTurismoOrigemEnergia = new FceTurismoOrigemEnergia(fceTurismo, origemEnergia);
			fceTurismoOrigemEnergia.setIdeFceTurismo(fceTurismo);
			fceTurismoOrigemEnergia.setIdeTipoOrigemEnergia(origemEnergia);
			salvarFceTurismoOrigemEnergia(fceTurismoOrigemEnergia);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceTurismoOrigemEnergia(FceTurismoOrigemEnergia fceIndustriaOrigemEnergia) {
		fceTurismoOrigemEnergiaIDAO.salvarOuAtualizar(fceIndustriaOrigemEnergia);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceTurismoOrigemEnergia> listarFceTurismoOrigemEnergiaByFceTurismo(FceTurismo fceTurismo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceTurismoOrigemEnergia.class);
		criteria.add(Restrictions.eq("ideFceTurismo", fceTurismo));
		criteria.setFetchMode("ideFceTurismo", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoOrigemEnergia", FetchMode.JOIN);
		return fceTurismoOrigemEnergiaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceTurismoOrigemEnergiaByIdeFceTurismo(FceTurismo fceTurismo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceTurismo", fceTurismo.getIdeFceTurismo());
		fceTurismoOrigemEnergiaIDAO.executarNamedQuery("FceTurismoOrigemEnergia.removeByIdeFceTurismo",parameters);
	}
}