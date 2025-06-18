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
import br.gov.ba.seia.entity.FceIndustriaCaptacao;
import br.gov.ba.seia.entity.FceIndustriaCaptacaoPK;
import br.gov.ba.seia.entity.TipoCaptacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaCaptacaoService {

	@Inject
	private IDAO<FceIndustriaCaptacao> fceIndustriaCaptacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceIndustriaCaptacao(FceIndustria fceIndustria, List<TipoCaptacao> listaTipoCaptacao) {
		for(TipoCaptacao tipoCaptacao : listaTipoCaptacao){
			FceIndustriaCaptacao fceIndustriaCaptacao = new FceIndustriaCaptacao(new FceIndustriaCaptacaoPK(fceIndustria, tipoCaptacao));
			fceIndustriaCaptacao.setIdeFceIndustria(fceIndustria);
			fceIndustriaCaptacao.setIdeTipoCaptacao(tipoCaptacao);
			salvarFceIndustriaCaptacao(fceIndustriaCaptacao);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceIndustriaCaptacao(FceIndustriaCaptacao fceIndustriaCaptacao) {
		fceIndustriaCaptacaoIDAO.salvarOuAtualizar(fceIndustriaCaptacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIndustriaCaptacao> buscarFceIndustriaCaptacaoByFceIndustria(FceIndustria fceIndustria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustriaCaptacao.class);
		criteria.add(Restrictions.eq("ideFceIndustria", fceIndustria));
		criteria.setFetchMode("ideFceIndustria", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoCaptacao", FetchMode.JOIN);
		return fceIndustriaCaptacaoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativaByIdeFceIndustria(FceIndustria fceIndustria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceIndustria", fceIndustria.getIdeFceIndustria());
		fceIndustriaCaptacaoIDAO.executarNamedQuery("FceIndustriaCaptacao.removeByIdeFceIndustria",parameters);
	}
}