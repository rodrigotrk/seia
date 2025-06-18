package br.gov.ba.seia.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoProcessoDAOImpl {
	
	@EJB
	private FinalidadeReenquadramentoProcessoDAOImpl finalidadeReenquadramentoProcessoDAOImpl;
	@EJB
	private ReenquadramentoProcessoAtoDAOImpl reenquadramentoProcessoAtoDAOImpl;
	@EJB
	private ReenquadramentoPotencialPoluicaoDAOImpl reenquadramentoPotencialPoluicaoDAOImpl;
	@EJB
	private ReenquadramentoTipologiaDAOImpl reenquadramentoTipologiaDAOImpl;
	
	@Inject
	private IDAO<ReenquadramentoProcesso> reenquadramentoProcessoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoProcesso reenquadramentoProcesso)  {
		if(Util.isNull(reenquadramentoProcesso.getIdeReenquadramentoProcesso())){
			reenquadramentoProcessoDAO.salvar(reenquadramentoProcesso);
		} 
		else {
			reenquadramentoProcessoDAO.merge(reenquadramentoProcesso);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ReenquadramentoProcesso reenquadramentoProcesso)  {
		reenquadramentoProcessoDAO.remover(reenquadramentoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReenquadramentoProcesso buscarReenquadramentoProcesso(Notificacao notificacao)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoProcesso.class);
		criteria
			.createAlias("ideNotificacao", "n", JoinType.INNER_JOIN)
			.createAlias("ideClasseEmpreendimentoInicial", "cInicial", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNovaClasseEmpreendimento", "cNova", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePorteEmpreendimentoInicial", "pInicial", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNovoPorteEmpreendimento", "pNovo", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideNotificacao", notificacao))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideReenquadramentoProcesso"),"ideReenquadramentoProcesso")
				.add(Projections.property("n.ideNotificacao"),"ideNotificacao.ideNotificacao")
				.add(Projections.property("cInicial.ideClasse"),"ideClasseEmpreendimentoInicial.ideClasse")
				.add(Projections.property("cInicial.nomClasse"),"ideClasseEmpreendimentoInicial.nomClasse")
				.add(Projections.property("cNova.ideClasse"),"ideNovaClasseEmpreendimento.ideClasse")
				.add(Projections.property("cNova.nomClasse"),"ideNovaClasseEmpreendimento.nomClasse")
				.add(Projections.property("pInicial.idePorte"),"idePorteEmpreendimentoInicial.idePorte")
				.add(Projections.property("pNovo.idePorte"),"ideNovoPorteEmpreendimento.idePorte")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoProcesso.class))
		;
		
		return reenquadramentoProcessoDAO.buscarPorCriteria(criteria);
	}
}