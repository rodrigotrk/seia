package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.LacLegislacao;


/***
 * 
 * @author luis
 *
 */
public class LacLegislacaoDAOImpl {

	@Inject
	IDAO<LacLegislacao> daoLacLegislacao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(LacLegislacao pLacLegislacao)  {
		daoLacLegislacao.salvarOuAtualizar(pLacLegislacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(LacLegislacao pLacLegislacao)  {
		daoLacLegislacao.atualizar(pLacLegislacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(LacLegislacao pLacLegislacao)  {
		daoLacLegislacao.remover(pLacLegislacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacLegislacao> carregarByIdeLac(int pIdeLac) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideLac", pIdeLac);
		return daoLacLegislacao.buscarPorNamedQuery("LacLegislacao.findByIdeLac", map);
	}

	public Collection<LacLegislacao> carregarByIdeRequerimentoWithLac(int pIdeRequerimento) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideRequerimento", pIdeRequerimento);
		return daoLacLegislacao.buscarPorNamedQuery("LacLegislacao.findByIdeRequerimento", map);
	}

}
