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
import br.gov.ba.seia.entity.FceIndustriaResiduoCivil;
import br.gov.ba.seia.entity.FceIndustriaResiduoCivilPK;
import br.gov.ba.seia.entity.TipoResiduoConsCivil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaResiduoCivilService {

	@Inject
	private IDAO<FceIndustriaResiduoCivil> fceIndustriaResiduoCivilIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaResiduoCivil(FceIndustria fceIndustria, List<TipoResiduoConsCivil> listaTipoResiduoConsCivils) {
		for(TipoResiduoConsCivil tipoResiduoConsCivil : listaTipoResiduoConsCivils){
			FceIndustriaResiduoCivil fceIndustriaResiduoCivil = new FceIndustriaResiduoCivil(new FceIndustriaResiduoCivilPK(fceIndustria, tipoResiduoConsCivil));
			fceIndustriaResiduoCivil.setIdeFceIndustria(fceIndustria);
			fceIndustriaResiduoCivil.setIdeTipoResiduoConsCivil(tipoResiduoConsCivil);
			salvarFceIndustriaResiduoCivil(fceIndustriaResiduoCivil);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaResiduoCivil(FceIndustriaResiduoCivil fceIndustriaCaptacao) {
		fceIndustriaResiduoCivilIDAO.salvarOuAtualizar(fceIndustriaCaptacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaResiduoCivil> buscarFceIndustriaResiduoCivilByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaResiduoCivil.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoResiduoConsCivil", FetchMode.JOIN);
		return fceIndustriaResiduoCivilIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaResiduoCivilIDAO.executarNamedQuery("FceIndustriaResiduoCivil.removeByIdeFceIndustria",parameters);
	}
}