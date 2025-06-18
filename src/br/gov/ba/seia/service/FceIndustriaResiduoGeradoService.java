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
import br.gov.ba.seia.entity.FceIndustriaResiduoGerado;
import br.gov.ba.seia.entity.FceIndustriaResiduoGeradoPK;
import br.gov.ba.seia.entity.TipoResiduoGerado;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaResiduoGeradoService {

	@Inject
	private IDAO<FceIndustriaResiduoGerado> fceIndustriaResiduoGeradoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaResiduoGerado(FceIndustria fceIndustria, List<TipoResiduoGerado> listaTipoResiduoGerado) {
		for(TipoResiduoGerado tipoResiduoGerado: listaTipoResiduoGerado){
			FceIndustriaResiduoGerado fceIndustriaResiduoGerado = new FceIndustriaResiduoGerado(new FceIndustriaResiduoGeradoPK(fceIndustria, tipoResiduoGerado));
			fceIndustriaResiduoGerado.setIdeFceIndustria(fceIndustria);
			fceIndustriaResiduoGerado.setIdeTipoResiduoGerado(tipoResiduoGerado);
			salvarFceIndustriaResiduoGerado(fceIndustriaResiduoGerado);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaResiduoGerado(FceIndustriaResiduoGerado fceIndustriaCaptacao) {
		fceIndustriaResiduoGeradoIDAO.salvarOuAtualizar(fceIndustriaCaptacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaResiduoGerado> buscarFceIndustriaResiduoGeradoByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaResiduoGerado.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoResiduoGerado", FetchMode.JOIN);
		return fceIndustriaResiduoGeradoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaResiduoGeradoIDAO.executarNamedQuery("FceIndustriaResiduoGerado.removeByIdeFceIndustria",parameters);
	}
}