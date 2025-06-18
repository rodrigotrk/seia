package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collections;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.FuncionalidadeAcao;
import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionalidadeAcaoService {

	@Inject
	IDAO<FuncionalidadeAcao> daoFuncionalidadeAcao;

	@Inject
	IDAO<Funcionalidade> daoFuncionalidade;
	
	/**
	 * Retorna todas os objetos da entidade {@link FuncionalidadeAcao}
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return List<{@link FuncionalidadeAcao}>
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FuncionalidadeAcao> obterTodos() {
		
		return daoFuncionalidadeAcao.listarTodos();
	}
	
	/**
	 * Retorna as funcionalidades encontradas na entidade {@link FuncionalidadeAcao}
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return List<{@link Funcionalidade}>
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Funcionalidade> obterFuncionalidades()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FuncionalidadeAcao.class);
		criteria.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("funcionalidade")),"funcionalidade"));

		List<Funcionalidade> funcionalidades = daoFuncionalidade.listarPorCriteria(criteria);
		Collections.sort(funcionalidades);
		return funcionalidades;
	}
	
	/**
	 * Retorna as acoes associadas a determinada funcionalidade.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param funcionalidade
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Acao> obterAcoes(Funcionalidade funcionalidade)  {
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFuncionalidade", funcionalidade.getIdeFuncionalidade());
		
		List<FuncionalidadeAcao> listaFuncionalidadeAcao = daoFuncionalidadeAcao.buscarPorNamedQuery("FuncionalidadeAcao.findByFuncionalidade", params);
		List<Acao> acoes = new ArrayList<Acao>();
		
		for (FuncionalidadeAcao fa : listaFuncionalidadeAcao) {
			acoes.add(fa.getAcao());
		}
		Collections.sort(acoes);
		return acoes;
	}
	
	/**
	 * Retorna as funcionalidades encontradas na entidade {@link FuncionalidadeAcao} de acordo com o ID da {@link Secao}
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param secao
	 * @return
	 * @
	 */
	public List<Funcionalidade> obterFuncionalidadesPorSecao(Secao secao)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionalidade.class);
		criteria.add(Restrictions.eq("ideSecao.ideSecao", secao.getIdeSecao()));
		criteria.add(Restrictions.eq("indExcluido", false));
		return daoFuncionalidade.listarPorCriteria(criteria);
	}
}