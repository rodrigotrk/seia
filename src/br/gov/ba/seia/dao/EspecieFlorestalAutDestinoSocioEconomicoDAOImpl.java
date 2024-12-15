package br.gov.ba.seia.dao;

import java.util.List;

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

import br.gov.ba.seia.entity.EspecieFlorestalAutDestinoSocioEconomico;
import br.gov.ba.seia.entity.EspecieFlorestalAutorizacao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieFlorestalAutDestinoSocioEconomicoDAOImpl {
	
	@Inject
	private IDAO<EspecieFlorestalAutDestinoSocioEconomico> especieFlorestalAutDestinoSocioEconomicoDAO;
	
	public void salvar(EspecieFlorestalAutDestinoSocioEconomico especieFlorestalAutDestinoSocioEconomico)  {
		especieFlorestalAutDestinoSocioEconomicoDAO.salvarOuAtualizar(especieFlorestalAutDestinoSocioEconomico);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieFlorestalAutDestinoSocioEconomico> listarEspSuprAutDestSocioEconPorEspSuprAut(EspecieFlorestalAutorizacao especieFlorestalAutorizacao)  {
		DetachedCriteria criteriaEsadse = DetachedCriteria.forClass(EspecieFlorestalAutDestinoSocioEconomico.class)
				.createAlias("ideDestinoSocioeconomico", "ideDestinoSocioeconomico", JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideEspecieFlorestalAutDestinoSocioEconomico"), "ideEspecieFlorestalAutDestinoSocioEconomico")
					.add(Projections.property("ideDestinoSocioeconomico.ideDestinoSocioeconomico"), "ideDestinoSocioeconomico.ideDestinoSocioeconomico")
					.add(Projections.property("ideDestinoSocioeconomico.dscDestinoSocioeconomico"), "ideDestinoSocioeconomico.dscDestinoSocioeconomico"))
				.add(Restrictions.eq("ideEspecieFlorestalAutorizacao.ideEspecieFlorestalAutorizacao", especieFlorestalAutorizacao.getIdeEspecieFlorestalAutorizacao()))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieFlorestalAutDestinoSocioEconomico.class));
		
		return especieFlorestalAutDestinoSocioEconomicoDAO.listarPorCriteria(criteriaEsadse);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerEspecieFlorestalAutDestinoSocioEconomico(EspecieFlorestalAutorizacao especieFlorestalAutorizacao){
		List<EspecieFlorestalAutDestinoSocioEconomico> lista = listarEspSuprAutDestSocioEconPorEspSuprAut(especieFlorestalAutorizacao);
		if(!Util.isNullOuVazio(lista)) {
			for (EspecieFlorestalAutDestinoSocioEconomico especieFlorestalAutDestinoSocioEconomico : lista) {
				especieFlorestalAutDestinoSocioEconomicoDAO.remover(especieFlorestalAutDestinoSocioEconomico);
			}
		}
	}
	
}