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
import br.gov.ba.seia.entity.FceIndustriaEmissaoAtmosferica;
import br.gov.ba.seia.entity.FceIndustriaEmissaoAtmosfericaPK;
import br.gov.ba.seia.entity.TipoEmissaoAtmosferica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaEmissaoAtmosfericaService {

	@Inject
	private IDAO<FceIndustriaEmissaoAtmosferica> fceIndustriaEmissaoAtmosfericaIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaEmissaoAtmosferica(FceIndustria fceIndustria, List<TipoEmissaoAtmosferica> listaTipoEmissaoAtmosfericas) {
		for(TipoEmissaoAtmosferica tipoEmissaoAtmosferica : listaTipoEmissaoAtmosfericas){
			FceIndustriaEmissaoAtmosferica fceIndustriaEmissaoAtmosferica = new FceIndustriaEmissaoAtmosferica(new FceIndustriaEmissaoAtmosfericaPK(fceIndustria, tipoEmissaoAtmosferica));
			fceIndustriaEmissaoAtmosferica.setIdeFceIndustria(fceIndustria);
			fceIndustriaEmissaoAtmosferica.setIdeTipoEmissaoAtmosferica(tipoEmissaoAtmosferica);
			salvarFceIndustriaEmissaoAtmosferica(fceIndustriaEmissaoAtmosferica);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaEmissaoAtmosferica(FceIndustriaEmissaoAtmosferica fceIndustriaEmissaoAtmosferica) {
		fceIndustriaEmissaoAtmosfericaIDAO.salvarOuAtualizar(fceIndustriaEmissaoAtmosferica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaEmissaoAtmosferica> buscarFceIndustriaEmissaoAtmosfericaByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaEmissaoAtmosferica.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoEmissaoAtmosferica", FetchMode.JOIN);
		return fceIndustriaEmissaoAtmosfericaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaEmissaoAtmosfericaIDAO.executarNamedQuery("FceIndustriaEmissaoAtmosferica.removeByIdeFceIndustria",parameters);
	}
}