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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceIndustria;
import br.gov.ba.seia.entity.FceIndustriaSubstancia;
import br.gov.ba.seia.entity.FceIndustriaSubstanciaPK;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaSubstanciaService {

	@Inject
	private IDAO<FceIndustriaSubstancia> fceIndustriaSubstanciaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaSubstancia> buscarListaFceIndustriaSubstanciaByIdeFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaSubstancia.class);
		criteria.add(Restrictions.eq("ideFceIndustria.ideFceIndustria", fceIndustria.getIdeFceIndustria()));
		criteria.createAlias("ideTipoSubstancia", "tipoSubstancia");
		List<FceIndustriaSubstancia> listaTemp = fceIndustriaSubstanciaIDAO.listarPorCriteria(criteria);
		for(FceIndustriaSubstancia fceIndustriaSubstancia : listaTemp){
			fceIndustriaSubstancia.setConfirmado(true);
		}
		return listaTemp;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceIndustriaSubstacia(FceIndustria fceIndustria, List<FceIndustriaSubstancia> listaFceIndustriaSubstancia) {
		for(FceIndustriaSubstancia fceIndustriaSubstancia : listaFceIndustriaSubstancia){
			fceIndustriaSubstancia.setIdeFceIndustriaSubstanciaPK(new FceIndustriaSubstanciaPK(fceIndustria, fceIndustriaSubstancia.getIdeTipoSubstancia()));
			fceIndustriaSubstancia.setIdeFceIndustria(fceIndustria);
			fceIndustriaSubstanciaIDAO.salvarOuAtualizar(fceIndustriaSubstancia);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaSubstanciaIDAO.executarNamedQuery("FceIndustriaSubstancia.removeByIdeFceIndustria",parameters);
	}
}