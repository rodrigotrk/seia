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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaepogDefinicaoCampo;
import br.gov.ba.seia.entity.CaepogLocacao;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogLocacaoService {
	
	@Inject
	IDAO<CaepogLocacao> caepogLocacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogLocacao> listarPorCaepogDefinicaoCampo(CaepogDefinicaoCampo cdc)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogLocacao.class, "loc")
				.createAlias("loc.ideCaepogDefinicaoCampo", "cdc", JoinType.INNER_JOIN)
				.createAlias("loc.ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN);
		
		if(!Util.isNullOuVazio(cdc)) {
			criteria.add(Restrictions.eq("loc.ideCaepogDefinicaoCampo.ideCaepogDefinicaoCampo", cdc.getIdeCaepogDefinicaoCampo()));
		}
		
		return caepogLocacaoDAO.listarPorCriteria(criteria, Order.asc("loc.ideCaepogLocacao"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CaepogLocacao cl)  {
		caepogLocacaoDAO.salvarOuAtualizar(cl);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CaepogLocacao cl)  {
		caepogLocacaoDAO.remover(cl);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorCaepogDefinicaoCampo(CaepogDefinicaoCampo cdc)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCaepogDefinicaoCampo", cdc.getIdeCaepogDefinicaoCampo());
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from CaepogLocacao cl where cl.ideCaepogDefinicaoCampo.ideCaepogDefinicaoCampo = :ideCaepogDefinicaoCampo");
		caepogLocacaoDAO.executarQuery(sql.toString(), params);
	}
}