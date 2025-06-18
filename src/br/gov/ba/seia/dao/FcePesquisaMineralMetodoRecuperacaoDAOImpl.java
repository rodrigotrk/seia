package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralMetodoRecuperacao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class FcePesquisaMineralMetodoRecuperacaoDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineralMetodoRecuperacao> fcePesquisaMineralMetodoRecuperacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralMetodoRecuperacao(List<FcePesquisaMineralMetodoRecuperacao> listaFcePesquisaMineralMetodoRecuperacao){
		fcePesquisaMineralMetodoRecuperacaoDAO.salvarEmLote(listaFcePesquisaMineralMetodoRecuperacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralMetodoRecuperacaoBy(FcePesquisaMineral ideFcePesquisaMineral){		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral());
		fcePesquisaMineralMetodoRecuperacaoDAO.executarNamedQuery("FcePesquisaMineralMetodoRecuperacao.removeByIdeFcePesquisaMineral", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralMetodoRecuperacao> listarFcePesquisaMineralMetodoRecuperacao(FcePesquisaMineral ideFcePesquisaMineral) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralMetodoRecuperacao.class)
			.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideFcePesquisaMineral.ideFcePesquisaMineral"), "ideFcePesquisaMineral.ideFcePesquisaMineral")
				.add(Projections.property("ideMetodoRecuperacaoIntervencao.ideMetodoRecuperacaoIntervencao"), "ideMetodoRecuperacaoIntervencao.ideMetodoRecuperacaoIntervencao")
				.add(Projections.property("ideFcePesquisaMineral.ideFcePesquisaMineral"), "ideFcePesquisaMineralMetodoRecuperacaoPK.ideFcePesquisaMineral")
				.add(Projections.property("ideMetodoRecuperacaoIntervencao.ideMetodoRecuperacaoIntervencao"), "ideFcePesquisaMineralMetodoRecuperacaoPK.ideMetodoRecuperacaoIntervencao")		
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralMetodoRecuperacao.class));
		return  fcePesquisaMineralMetodoRecuperacaoDAO.listarPorCriteria(detachedCriteria);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralMetodoRecuperacaoByNativeQuery(List<FcePesquisaMineralMetodoRecuperacao> listaFcePesquisaMineralMetodoRecuperacao) throws Exception{
		
		for(FcePesquisaMineralMetodoRecuperacao fcePesquisaMineralMetodoRecuperacao: listaFcePesquisaMineralMetodoRecuperacao) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ideFcePesquisaMineral", fcePesquisaMineralMetodoRecuperacao.getIdeFcePesquisaMineralMetodoRecuperacaoPK().getIdeFcePesquisaMineral());
			parametros.put("ideMetodoRecuperacaoIntervencao", fcePesquisaMineralMetodoRecuperacao.getIdeFcePesquisaMineralMetodoRecuperacaoPK().getIdeMetodoRecuperacaoIntervencao());
			
			fcePesquisaMineralMetodoRecuperacaoDAO.executarNativeQuery("INSERT INTO fce_pesquisa_mineral_metodo_recuperacao values(:ideFcePesquisaMineral,:ideMetodoRecuperacaoIntervencao)", parametros);
		}
		
	}
	
}
