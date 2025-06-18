/**
 * 		14/08/14
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
import br.gov.ba.seia.entity.FceIndustriaTipoApp;
import br.gov.ba.seia.entity.TipoApp;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaTipoAppService {

	@Inject
	private IDAO<FceIndustriaTipoApp> fceIndustriaTipoAppIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaOrigemEnergia(FceIndustria fceIndustria, List<TipoApp> listaTipoApp) {
		for(TipoApp tipoApp : listaTipoApp){
			FceIndustriaTipoApp fceIndustriaTipoApp = new FceIndustriaTipoApp(fceIndustria, tipoApp);
			fceIndustriaTipoApp.setIdeFceIndustria(fceIndustria);
			fceIndustriaTipoApp.setIdeTipoApp(tipoApp);
			salvarFceIndustriaTipoApp(fceIndustriaTipoApp);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaTipoApp(FceIndustriaTipoApp fceIndustriaTipoApp) {
		fceIndustriaTipoAppIDAO.salvarOuAtualizar(fceIndustriaTipoApp);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaTipoApp> buscarFceIndustriaTipoAppByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaTipoApp.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoApp", FetchMode.JOIN);
		return fceIndustriaTipoAppIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaTipoAppIDAO.executarNamedQuery("FceIndustriaTipoApp.removeByIdeFceIndustria",parameters);
	}
}