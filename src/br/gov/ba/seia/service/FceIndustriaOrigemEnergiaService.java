/**
 * 		14/05/14
 * @author eduardo.fernandes
 */
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
import br.gov.ba.seia.entity.FceIndustria;
import br.gov.ba.seia.entity.FceIndustriaOrigemEnergia;
import br.gov.ba.seia.entity.TipoOrigemEnergia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaOrigemEnergiaService {

	@Inject
	private IDAO<FceIndustriaOrigemEnergia> fceIndustriaOrigemEnergiaIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaOrigemEnergia(FceIndustria fceIndustria, List<TipoOrigemEnergia> listaOrigemEnergia) {
		for(TipoOrigemEnergia origemEnergia : listaOrigemEnergia){
			FceIndustriaOrigemEnergia fceIndustriaOrigemEnergia = new FceIndustriaOrigemEnergia(fceIndustria, origemEnergia);
			fceIndustriaOrigemEnergia.setIdeFceIndustria(fceIndustria);
			fceIndustriaOrigemEnergia.setIdeTipoOrigemEnergia(origemEnergia);
			salvarFceIndustriaOrigemEnergia(fceIndustriaOrigemEnergia);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaOrigemEnergia(FceIndustriaOrigemEnergia fceIndustriaOrigemEnergia) {
		fceIndustriaOrigemEnergiaIDAO.salvarOuAtualizar(fceIndustriaOrigemEnergia);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaOrigemEnergia> buscarFceIndustriaOrigemEnergiaByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaOrigemEnergia.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoOrigemEnergia", FetchMode.JOIN);
		return fceIndustriaOrigemEnergiaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaOrigemEnergiaIDAO.executarNamedQuery("FceIndustriaOrigemEnergia.removeByIdeFceIndustria",parameters);
	}
}