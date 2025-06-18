package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.entity.UnidadeTipologiaAtividade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UnidadeTipologiaAtividadeService {

	@Inject
	private IDAO<UnidadeTipologiaAtividade> unidadeTipologiaAtividadeIDAO;

	/**
	 * @param listaUnidade
	 * @throws Exception
	 * @INFO Salva uma UnidadeTipologiaAtividade
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaUnidadeTipologiaAtividade (List<UnidadeTipologiaAtividade> listaUnidade) throws Exception {
		for (UnidadeTipologiaAtividade unidadeTipologiaAtividade : listaUnidade) {
			unidadeTipologiaAtividadeIDAO.salvarOuAtualizar(unidadeTipologiaAtividade);
		}
	}
	/**
	 * @param listaUnidade
	 * @throws Exception
	 * @INFO Salva uma lista de UnidadeTipologiaAtividade
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUnidadeTipologiaAtividade (UnidadeTipologiaAtividade unidadeTipologiaAtividade) throws Exception {
		unidadeTipologiaAtividadeIDAO.salvarOuAtualizar(unidadeTipologiaAtividade);
	}
	
	/**
	 * @param listaUnidade
	 * @throws Exception
	 * @INFO Salva uma lista de UnidadeTipologiaAtividade
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UnidadeTipologiaAtividade buscarByIdeUnidadeETipologiaAtividade (UnidadeMedida unidadeMedida,TipologiaAtividade tipologiaAtividade) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeTipologiaAtividade.class);
		criteria.add(Restrictions.eq("ideTipologiaAtividade.ideTipologiaAtividade", tipologiaAtividade.getIdeTipologiaAtividade()));
		criteria.add(Restrictions.eq("ideUnidadeMedida.ideUnidadeMedida", unidadeMedida.getIdeUnidadeMedida()));
		UnidadeTipologiaAtividade unidadeTipologiaAtividade = unidadeTipologiaAtividadeIDAO.buscarPorCriteria(criteria);
		unidadeTipologiaAtividade.setIdeTipologiaAtividade(tipologiaAtividade);
		unidadeTipologiaAtividade.setIdeUnidadeMedida(unidadeMedida);
		return unidadeTipologiaAtividade;
	}

}
