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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fauna;
import br.gov.ba.seia.entity.ObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAtividadeFauna;
import br.gov.ba.seia.entity.TipoCriadouroFauna;
import br.gov.ba.seia.enumerator.ObjetivoAtividadeManejoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FaunaService {

	@Inject
	private IDAO<Fauna> faunaDAO;

	@Inject
	private IDAO<ObjetivoAtividadeManejo> objetivoAtividadeManejoDAO;

	@Inject
	private IDAO<TipoAtividadeFauna> tipoAtividadeFaunaDAO;

	@Inject
	private IDAO<TipoCriadouroFauna> tipoCriadouroFaunaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarFauna(Fauna fauna)  {
		faunaDAO.salvarOuAtualizar(fauna);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Fauna getFaunaByIdeRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fauna.class);
		criteria.createAlias("ideRequerimento", "ideRequerimento", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideRequerimento", requerimento));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		return faunaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Fauna fauna)  {
		faunaDAO.remover(fauna);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoAtividadeManejo> getListObjetivoAtividadeManejoByFauna(Fauna fauna) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideFauna", fauna.getIdeFauna());
		return objetivoAtividadeManejoDAO.buscarPorNamedQuery("ObjetivoAtividadeManejo.findByIdeFauna", map);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAtividadeFauna> getListTipoAtividadeFaunaByFauna(Fauna fauna) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideFauna", fauna.getIdeFauna());
		return tipoAtividadeFaunaDAO.buscarPorNamedQuery("TipoAtividadeFauna.findByIdeFauna", map);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCriadouroFauna> getListTipoCriadouroFaunaByFauna(Fauna fauna) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideFauna", fauna.getIdeFauna());
		return tipoCriadouroFaunaDAO.buscarPorNamedQuery("TipoCriadouroFauna.findByIdeFauna", map);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAtividadeFauna> carregaListaTipoAtividadeFauna() {
		return tipoAtividadeFaunaDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCriadouroFauna> carregaListaTipoCriadouroFauna() {
		return tipoCriadouroFaunaDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoAtividadeManejo> carregaListaObjetivoAtividadeManejo() {
		return objetivoAtividadeManejoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoAtividadeManejo> listarObjetivoAtividadeManejoParaReenquadramento()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoAtividadeManejo.class);
		criteria
			.add(Restrictions.in("ideObjetivoAtividadeManejo", new Integer[] {
					ObjetivoAtividadeManejoEnum.ESTUDO.getId(),
					ObjetivoAtividadeManejoEnum.PESQUISA_ACADEMICA.getId(),
					ObjetivoAtividadeManejoEnum.OBRAS_DO_EMPREENDIMENTO.getId()
			}))
			.addOrder(Order.asc("nomObjetivoAtividadeManejo"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideObjetivoAtividadeManejo"),"ideObjetivoAtividadeManejo")	
				.add(Projections.property("nomObjetivoAtividadeManejo"),"nomObjetivoAtividadeManejo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ObjetivoAtividadeManejo.class))
		;
		return objetivoAtividadeManejoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAtividadeFauna> listarTipoAtividadeFaunaParaReenquadramento()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAtividadeFauna.class);
		criteria
			.addOrder(Order.asc("nomTipoAtividadeFauna"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipoAtividadeFauna"),"ideTipoAtividadeFauna")	
				.add(Projections.property("nomTipoAtividadeFauna"),"nomTipoAtividadeFauna")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoAtividadeFauna.class))
		;
		return tipoAtividadeFaunaDAO.listarPorCriteria(criteria);
	}
}
