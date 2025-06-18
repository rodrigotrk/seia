package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.AssociacaoAssentadoImovelRuralIncra;
import br.gov.ba.seia.entity.AssociacaoIncraImovelRural;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AssociacaoAssentadoImovelRuralIncraService {
	
	@Inject
	private IDAO<AssociacaoAssentadoImovelRuralIncra> associacaoAssentadoImovelRuralDAO;
	
	public List<AssociacaoAssentadoImovelRuralIncra> listarPorIdeAssociacaoIncraImovelRural(AssociacaoIncraImovelRural ideAssociacaoIncraImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssociacaoAssentadoImovelRuralIncra.class);
		criteria.add(Restrictions.eq("ideAssociacaoIncraImovelRural.ideAssociacaoIncraImovelRural", ideAssociacaoIncraImovelRural.getIdeAssociacaoIncraImovelRural()));
		return associacaoAssentadoImovelRuralDAO.listarPorCriteria(criteria);
	}
	
	public List<AssociacaoAssentadoImovelRuralIncra> listarPorIdeAssentadoIncraImovelRural(AssentadoIncraImovelRural ideAssentadoIncraImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssociacaoAssentadoImovelRuralIncra.class);
		criteria.add(Restrictions.eq("ideAssentadoIncraImovelRural.ideAssentadoIncraImovelRural", ideAssentadoIncraImovelRural.getIdeAssentadoIncraImovelRural()));
		return associacaoAssentadoImovelRuralDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncra) {
		associacaoAssentadoImovelRuralDAO.remover(associacaoAssentadoImovelRuralIncra);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAllByAssentadoIncraImovelRural(AssentadoIncraImovelRural assentadoIncraImovelRural) {
		String deleteSQL = "DELETE FROM associacao_assentado_imovel_rural_incra WHERE ide_assentado_incra_imovel_rural = :ideAssentadoIncraImovelRural";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAssentadoIncraImovelRural", assentadoIncraImovelRural.getIdeAssentadoIncraImovelRural());
		associacaoAssentadoImovelRuralDAO.executarNativeQuery(deleteSQL, params);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncra)  {
		associacaoAssentadoImovelRuralDAO.salvar(associacaoAssentadoImovelRuralIncra);
	}
}
