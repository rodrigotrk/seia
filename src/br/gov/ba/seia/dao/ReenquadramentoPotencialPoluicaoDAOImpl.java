package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

import br.gov.ba.seia.entity.ReenquadramentoPotencialPoluicao;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoPotencialPoluicaoDAOImpl {
	
	@Inject
	private IDAO<ReenquadramentoPotencialPoluicao> reenquadramentoPotencialPoluicaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoPotencialPoluicao reenquadramentoPotencialPoluicao)  {
		reenquadramentoPotencialPoluicaoDAO.salvar(reenquadramentoPotencialPoluicao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ReenquadramentoPotencialPoluicao reenquadramentoPotencialPoluicao)  {
		reenquadramentoPotencialPoluicaoDAO.remover(reenquadramentoPotencialPoluicao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ReenquadramentoPotencialPoluicao rpp where rpp.ideReenquadramentoProcesso = :ideReenquadramentoProcesso");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideReenquadramentoProcesso", reenquadramentoProcesso);
		
		reenquadramentoPotencialPoluicaoDAO.executarQuery(sql.toString(),params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<ReenquadramentoPotencialPoluicao> listarReenquadramentoPotencialPoluicao(ReenquadramentoProcesso reenquadramentoProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoPotencialPoluicao.class);
		criteria
			.createAlias("idePotencialPoluicaoInicial", "ppInicial", JoinType.INNER_JOIN)
			.createAlias("idePotencialPoluicaoNovo", "ppNovo", JoinType.INNER_JOIN)
			.createAlias("ideRequerimentoTipologia", "rt", JoinType.INNER_JOIN)
			.createAlias("rt.ideUnidadeMedidaTipologiaGrupo", "umtg", JoinType.INNER_JOIN)
			.createAlias("umtg.ideTipologiaGrupo", "tg", JoinType.INNER_JOIN)
			.createAlias("tg.ideTipologia", "t", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideReenquadramentoProcesso", reenquadramentoProcesso))
			.setProjection(Projections.projectionList()
				.add(Projections.property("rt.ideRequerimentoTipologia"),"ideRequerimentoTipologia.ideRequerimentoTipologia")
				.add(Projections.property("umtg.ideUnidadeMedidaTipologiaGrupo"),"ideRequerimentoTipologia.ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
				.add(Projections.property("tg.ideTipologiaGrupo"),"ideRequerimentoTipologia.ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologiaGrupo")
				.add(Projections.property("t.ideTipologia"),"ideRequerimentoTipologia.ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.ideTipologia")
				.add(Projections.property("t.desTipologia"),"ideRequerimentoTipologia.ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.desTipologia")
				
				.add(Projections.property("ppInicial.idePotencialPoluicao"),"idePotencialPoluicaoInicial.idePotencialPoluicao")
				.add(Projections.property("ppInicial.sglPotencialPoluicao"),"idePotencialPoluicaoInicial.sglPotencialPoluicao")
				.add(Projections.property("ppInicial.nomPotencialPoluicao"),"idePotencialPoluicaoInicial.nomPotencialPoluicao")
				.add(Projections.property("ppNovo.idePotencialPoluicao"),"idePotencialPoluicaoNovo.idePotencialPoluicao")
				.add(Projections.property("ppNovo.sglPotencialPoluicao"),"idePotencialPoluicaoNovo.sglPotencialPoluicao")
				.add(Projections.property("ppNovo.nomPotencialPoluicao"),"idePotencialPoluicaoNovo.nomPotencialPoluicao")
				.add(Projections.property("ideReenquadramentoProcesso.ideReenquadramentoProcesso"),"ideReenquadramentoProcesso.ideReenquadramentoProcesso")
				
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoPotencialPoluicao.class))
		;
		
		return reenquadramentoPotencialPoluicaoDAO.listarPorCriteria(criteria);
	}
}