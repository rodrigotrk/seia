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
import br.gov.ba.seia.entity.TipoSolicitacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSolicitacaoService {

	@Inject
	private IDAO<TipoSolicitacao> tipoSolicitacaolDAO;

	/**
	 * Usado na abaRenovacaoAlteracaoProrrogacao
	 * 
	 * @return listaTipoSolicitacaoLicenca
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSolicitacao> listarTipoSolicitacaoLicenca() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoSolicitacao.class);
		criteria.add(Restrictions.between("ideTipoSolicitacao", 2, 3));
		return tipoSolicitacaolDAO.listarPorCriteria(criteria);
	}

	/**
	 * Usado na abaRenovacaoAlteracaoProrrogacao
	 * 
	 * @return listaTipoSolicitacaoOutorga
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSolicitacao> listarTipoSolicitacaoOutorga() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoSolicitacao.class);
		criteria.add(Restrictions.between("ideTipoSolicitacao", 5, 7));
		return tipoSolicitacaolDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoSolicitacao getTipoSolicitacaoByIde(int ideTipoSolicitacaoSelecionado) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoSolicitacao.class);
		criteria.add(Restrictions.eq("ideTipoSolicitacao", ideTipoSolicitacaoSelecionado));
		return tipoSolicitacaolDAO.buscarPorCriteria(criteria);
	}

}
