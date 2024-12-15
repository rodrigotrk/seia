package br.gov.ba.seia.service;

/**
 * @author eduardo.fernandes
 */
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
import br.gov.ba.seia.entity.FaseEmpreendimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FaseEmpreendimentoService {

	@Inject
	private IDAO<FaseEmpreendimento> faseEmpreendimentoDAO;

	/**
	 * Lista Fase de Empreendimento, chamado em NovoRequerimentoController
	 * 
	 * @return listaFaseEmpreendimento by ide 1 a 3
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FaseEmpreendimento> listarFaseEmpreendimento()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FaseEmpreendimento.class);
		criteria.add(Restrictions.between("ideFaseEmpreendimento", 1, 3));
		return faseEmpreendimentoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FaseEmpreendimento getTipoSolicitacaoByIde(int ideFaseEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FaseEmpreendimento.class);
		criteria.add(Restrictions.eq("ideFaseEmpreendimento", ideFaseEmpreendimento));
		return faseEmpreendimentoDAO.buscarPorCriteria(criteria);
	}

}
