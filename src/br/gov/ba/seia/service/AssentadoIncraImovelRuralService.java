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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AssentadoIncra;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.AssociacaoIncraImovelRural;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AssentadoIncraImovelRuralService {
	
	@Inject
	private IDAO<AssentadoIncraImovelRural> assentadoIncraImovelRuralDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AssentadoIncraImovelRural assentadoIncraImovelRural) {
		assentadoIncraImovelRuralDAO.salvarOuAtualizar(assentadoIncraImovelRural);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AssentadoIncraImovelRural> listarAssentadoIncraImovelRuralPorImovelRural(ImovelRural ideImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssentadoIncraImovelRural.class);
		criteria.createAlias("ideAssentadoIncra", "assentadoIncra", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural.ideImovelRural", ideImovelRural.getIdeImovelRural()));
		return assentadoIncraImovelRuralDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AssentadoIncraImovelRural buscarAssentadoIncraImovelRural(AssentadoIncra ideAssentadoIncra, ImovelRural ideImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssentadoIncraImovelRural.class);
		criteria.add(Restrictions.eq("ideAssentadoIncra.ideAssentadoIncra", ideAssentadoIncra.getIdeAssentadoIncra()));
		criteria.add(Restrictions.eq("ideImovelRural.ideImovelRural", ideImovelRural.getIdeImovelRural()));
		return assentadoIncraImovelRuralDAO.buscarPorCriteria(criteria); 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AssentadoIncraImovelRural assentadoIncraImovelRural) {
		String deleteSQL = "DELETE FROM assentado_incra_imovel_rural WHERE ide_assentado_incra_imovel_rural = :ideAssentadoIncraImovelRural";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAssentadoIncraImovelRural", assentadoIncraImovelRural.getIdeAssentadoIncraImovelRural());
		assentadoIncraImovelRuralDAO.executarNativeQuery(deleteSQL, params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AssentadoIncraImovelRural> buscarAssociacaoIncraImovelRural(AssentadoIncra ideAssentadoIncra) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssociacaoIncraImovelRural.class);
		criteria.add(Restrictions.eq("ideAssentadoIncra.ideAssentadoIncra", ideAssentadoIncra.getIdeAssentadoIncra()));
		return assentadoIncraImovelRuralDAO.listarPorCriteria(criteria); 
	}
	
	public void validaAssentadosIncraParaFinalizacao(ImovelRural imovelRural) throws Exception {
		boolean possuiAssentado = false;
		if(imovelRural.isFaseAssentamentoImovelRuralImplantado()) {
			if(!Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())) {
				for (AssentadoIncraImovelRural a : imovelRural.getAssentadoIncraImovelRuralCollection()) {
					if(!a.isIndExcluido()) {
						possuiAssentado = true;
						break;
					}
				}
			}
			if(!possuiAssentado) {
				throw new SeiaException("Por favor cadastre pelo menos um Assentado.");				
			}
		}
		
	}
}
