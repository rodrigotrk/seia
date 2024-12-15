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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CategoriaTurismo;
import br.gov.ba.seia.entity.FceCategoriaTurismo;
import br.gov.ba.seia.entity.FceCategoriaTurismoPK;
import br.gov.ba.seia.entity.FceTurismo;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 15/12/2014
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCategoriaTurismoService {

	@Inject
	private IDAO<FceCategoriaTurismo> fceCategoriaTurismoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCategoriaTurismo> listarFceCategoriaTurismoByFceTurismoAndTipologiaEnum(FceTurismo fceTurismo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCategoriaTurismo.class)
				.createAlias("ideCategoriaTurismo", "ct")
				.createAlias("ct.ideTipologia", "t")
				.add(Restrictions.eq("ideFceTurismo", fceTurismo));
		return fceCategoriaTurismoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceCategoriaTurismo(FceTurismo fceTurismo, List<CategoriaTurismo> listaCategoriaTurismo) {
		for(CategoriaTurismo categoriaTurismo : listaCategoriaTurismo){
			montarAndSalvarFceCategoriaTurismo(fceTurismo, categoriaTurismo);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void montarAndSalvarFceCategoriaTurismo(FceTurismo fceTurismo, CategoriaTurismo categoriaTurismo)  {
		FceCategoriaTurismo fceCategoriaTurismo = new FceCategoriaTurismo(new FceCategoriaTurismoPK(fceTurismo, categoriaTurismo));
		fceCategoriaTurismo.setIdeFceTurismo(fceTurismo);
		fceCategoriaTurismo.setIdeCategoriaTurismo(categoriaTurismo);
		salvarFceCategoriaTurismo(fceCategoriaTurismo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceCategoriaTurismo(FceCategoriaTurismo fceCategoriaTurismo) {
		fceCategoriaTurismoIDAO.salvarOuAtualizar(fceCategoriaTurismo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceCategoriaTurismoByIdeFceTurismo(FceTurismo fceTurismo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceTurismo", fceTurismo.getIdeFceTurismo());
		fceCategoriaTurismoIDAO.executarNamedQuery("FceCategoriaTurismo.removeByIdeFceTurismo",parameters);
	}
}