package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.EspecieFlorestalAutorizacao;
import br.gov.ba.seia.entity.FceFlorestal;
import br.gov.ba.seia.enumerator.TipoEspecieFlorestalEnum;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieFlorestalAutorizacaoDAOImpl {
	
	@EJB
	private EspecieFlorestalAutDestinoSocioEconomicoDAOImpl especieFlorestalAutDestinoSocioEconomicoDAO;
	@EJB
	private EspecieFlorestalAutDestinoSocioEconomicoDAOImpl especieFlorestalAutDestinoSocioEconomicoDAOImpl;
	
	@Inject
	private IDAO<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(EspecieFlorestalAutorizacao especieFlorestalAutorizacao) {
		especieFlorestalAutorizacaoDAO.remover(especieFlorestalAutorizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EspecieFlorestalAutorizacao especieFlorestalAutorizacao)  {
		especieFlorestalAutorizacaoDAO.salvarOuAtualizar(especieFlorestalAutorizacao);
	}
	
	public List<EspecieFlorestalAutorizacao> buscarEspecieFlorestalAutorizacaoNativa(FceFlorestal fceFlorestal, TipoEspecieFlorestalEnum ideTipoEspecieFlorestal) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieFlorestalAutorizacao.class, "eFA");	
		
		criteria.createAlias("eFA.ideProduto", "p", JoinType.INNER_JOIN);
		
		criteria.createAlias("eFA.ideEspecieFlorestal", "eF", JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("eFA.ideNomePopularEspecie", "nPE", JoinType.LEFT_OUTER_JOIN);
		
		criteria.createCriteria("eF.ideTipoEspecieFlorestal", "tEF", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("eFA.fceFlorestal.ideFceFlorestal", fceFlorestal.getIdeFceFlorestal()));
		
		criteria.add( Restrictions.or(Restrictions.eq("tEF.ideTipoEspecieFlorestal", ideTipoEspecieFlorestal.getId()), Restrictions.isNull("tEF.ideTipoEspecieFlorestal")) );
		
		List<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoes = especieFlorestalAutorizacaoDAO.listarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(especieFlorestalAutorizacaoes)) {
			for(EspecieFlorestalAutorizacao especieFlorestalAutorizacao : especieFlorestalAutorizacaoes){
				especieFlorestalAutorizacao.setEspecieFlorestalAutDestinoSocioEconomicoCollection(especieFlorestalAutDestinoSocioEconomicoDAOImpl.listarEspSuprAutDestSocioEconPorEspSuprAut(especieFlorestalAutorizacao));
			}
		}
		
		return especieFlorestalAutorizacaoes;
	}
}