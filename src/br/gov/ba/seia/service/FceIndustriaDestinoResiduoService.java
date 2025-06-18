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
import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.FceIndustria;
import br.gov.ba.seia.entity.FceIndustriaDestinoResiduo;
import br.gov.ba.seia.entity.FceIndustriaDestinoResiduoPK;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaDestinoResiduoService {

	@Inject
	private IDAO<FceIndustriaDestinoResiduo> fceIndustriaDestinoResiduoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaDestinoResiduo(FceIndustria fceIndustria, List<DestinoResiduo> listaDestinoResiduo) {
		for(DestinoResiduo destinoResiduo : listaDestinoResiduo){
			FceIndustriaDestinoResiduo fceIndustriaDestinoResiduo = new FceIndustriaDestinoResiduo(new FceIndustriaDestinoResiduoPK(fceIndustria, destinoResiduo));
			fceIndustriaDestinoResiduo.setIdeFceIndustria(fceIndustria);
			fceIndustriaDestinoResiduo.setIdeDestinoResiduo(destinoResiduo);
			salvarFceIndustriaDestinoResiduo(fceIndustriaDestinoResiduo);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaDestinoResiduo(FceIndustriaDestinoResiduo fceIndustriaDestinoResiduo) {
		fceIndustriaDestinoResiduoIDAO.salvarOuAtualizar(fceIndustriaDestinoResiduo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaDestinoResiduo> buscarFceIndustriaDestinoResiduoByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaDestinoResiduo.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideDestinoResiduo", FetchMode.JOIN);
		return fceIndustriaDestinoResiduoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaDestinoResiduoIDAO.executarNamedQuery("FceIndustriaDestinoResiduo.removeByIdeFceIndustria",parameters);
	}
}