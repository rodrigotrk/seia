package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.VwConsultaProcesso;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpreendimentoRequerimentoDAOImpl {

	@Inject
	IDAO<EmpreendimentoRequerimento> empreendimentoRequerimentoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpreendimentoRequerimento buscarEmpreendimentoRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = getCriteria(requerimento.getIdeRequerimento());
		return empreendimentoRequerimentoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<EmpreendimentoRequerimento> listarEmpreendimentoRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = getCriteria(requerimento.getIdeRequerimento());
		return empreendimentoRequerimentoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<EmpreendimentoRequerimento> listarEmpreendimentoRequerimento(VwConsultaProcesso vwConsultaProcesso)  {
		DetachedCriteria criteria = getCriteria(vwConsultaProcesso.getIdeRequerimento());
		return empreendimentoRequerimentoDAO.listarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteria(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class);
		criteria.createAlias("ideRequerimento", "requerimento").createAlias("ideEmpreendimento", "empreendimento")
				.createAlias("ideClasse", "classe", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideFaseEmpreendimento", "faseEmpreendimento", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePorte", "porte", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		return criteria;
	}
}