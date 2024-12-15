package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.MemoriaCalculoDae;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MemoriaCalculoDaeService {

	@Inject
	private IDAO<MemoriaCalculoDae> memoriaCalculoDaeDAO;
	
	@Inject
	private MemoriaCalculoDaeParcelaService memoriaCalculoDaeParcelaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MemoriaCalculoDae obterMemoriaCalculoDaePorRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDae.class);
		
		criteria.createAlias("ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("ideParametroCalculo", "pca", JoinType.INNER_JOIN);
		criteria.createAlias("crf.requerimento", "req", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		
		criteria.setProjection(
			Projections.projectionList()
				.add(Projections.property("ideMemoriaCalculoDae"), "ideMemoriaCalculoDae")
				.add(Projections.property("valorTotal"), "valorTotal")
				.add(Projections.property("qtdParcelas"), "qtdParcelas")
				.add(Projections.property("indParcelado"), "indParcelado")
				.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
				.add(Projections.property("pca.ideParametroCalculo"), "ideParametroCalculo.ideParametroCalculo")
				.add(Projections.property("req.ideRequerimento"), "ideCumprimentoReposicaoFlorestal.requerimento.ideRequerimento")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDae.class));
		List<MemoriaCalculoDae> list = memoriaCalculoDaeDAO.listarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(list)) {
			//: Melhorar esse código depois
			MemoriaCalculoDae memoriaCalculoDae = list.get(list.size()-1);
			memoriaCalculoDae.setMemoriaCalculoDaeCollection(memoriaCalculoDaeParcelaService.listarMemoriaCalculoDaeParcelaPorMCD(memoriaCalculoDae.getIdeMemoriaCalculoDae()));
			
			return memoriaCalculoDae;
			
		} else return null;
	}
}
