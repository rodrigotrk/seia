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
import br.gov.ba.seia.entity.FceIndustriaSistemaTratamento;
import br.gov.ba.seia.entity.FceIndustriaSistemaTratamentoPK;
import br.gov.ba.seia.entity.TipoSistemaTratamento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaSistemaTratamentoService {

	@Inject
	private IDAO<FceIndustriaSistemaTratamento> fceIndustriaSistemaTratamentoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaSistemaTratamento(FceIndustria fceIndustria, List<TipoSistemaTratamento> listaTipoSistemaTratamentos) {
		for(TipoSistemaTratamento tipoSistemaTratamento: listaTipoSistemaTratamentos){
			FceIndustriaSistemaTratamento fceIndustriaSistemaTratamento = new FceIndustriaSistemaTratamento(new FceIndustriaSistemaTratamentoPK(fceIndustria, tipoSistemaTratamento));
			fceIndustriaSistemaTratamento.setIdeFceIndustria(fceIndustria);
			fceIndustriaSistemaTratamento.setIdeTipoSistemaTratamento(tipoSistemaTratamento);
			salvarFceIndustriaSistemaTratamento(fceIndustriaSistemaTratamento);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaSistemaTratamento(FceIndustriaSistemaTratamento fceIndustriaCaptacao) {
		fceIndustriaSistemaTratamentoIDAO.salvarOuAtualizar(fceIndustriaCaptacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaSistemaTratamento> buscarFceIndustriaideSistemaTratamentoByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaSistemaTratamento.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoSistemaTratamento", FetchMode.JOIN);
		return fceIndustriaSistemaTratamentoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaSistemaTratamentoIDAO.executarNamedQuery("FceIndustriaSistemaTratamento.removeByIdeFceIndustria",parameters);
	}
}