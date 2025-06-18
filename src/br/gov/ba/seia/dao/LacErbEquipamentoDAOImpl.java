package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.LacErbEquipamento;
import br.gov.ba.seia.entity.LacErbEquipamentoPK;


/***
 * 
 * @author luis
 *
 */
public class LacErbEquipamentoDAOImpl {

	@Inject
	IDAO<LacErbEquipamento> lacErbEquipamentoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(LacErbEquipamento pLacErbEquipamento)  {
		this.lacErbEquipamentoDAO.salvar(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(LacErbEquipamento pLacErbEquipamento)  {
		lacErbEquipamentoDAO.atualizar(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(LacErbEquipamento pLacErbEquipamento)  {
		lacErbEquipamentoDAO.remover(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LacErbEquipamento carregarByIde(LacErbEquipamentoPK pLacErbEquipamentoPK)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideLacErb", pLacErbEquipamentoPK.getIdeLacErb());
		map.put("ideErbEquipamento", pLacErbEquipamentoPK.getIdeErbEquipamento());
		map.put("ideTipoCanalErb", pLacErbEquipamentoPK.getIdeTipoCanalErb());
		return (LacErbEquipamento) lacErbEquipamentoDAO.buscarPorNamedQuery("LacErbEquipamento.findByIdeErbEquipamentoAndIdeLacErbAndIdeTipoCanalErb", map);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacErbEquipamento> carregarByIde(int pIde)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideLacErb", pIde);
		return lacErbEquipamentoDAO.buscarPorNamedQuery("LacErbEquipamento.findByIdeLacErb", map);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(LacErbEquipamento pLacErbEquipamento)  {
		lacErbEquipamentoDAO.salvarOuAtualizar(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(LacErbEquipamentoPK pLacErbEquipamentoPK)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideLacErb", pLacErbEquipamentoPK.getIdeLacErb());
		map.put("ideErbEquipamento", pLacErbEquipamentoPK.getIdeErbEquipamento());
		map.put("ideTipoCanalErb", pLacErbEquipamentoPK.getIdeTipoCanalErb());
		this.lacErbEquipamentoDAO.executarNamedQuery("LacErbEquipamento.removerLacErbEquipamento",map);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Collection<LacErbEquipamento> pLacErbEquipamentos)  {
		for (LacErbEquipamento lacErbEquipamento : pLacErbEquipamentos) {
			this.lacErbEquipamentoDAO.remover(lacErbEquipamento);
		}
	}
}
