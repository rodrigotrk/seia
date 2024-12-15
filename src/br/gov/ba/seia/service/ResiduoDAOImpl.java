package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Residuo;
import br.gov.ba.seia.util.Util;

public class ResiduoDAOImpl extends BaseDAO<Residuo>{

	@Inject
	IDAO<Residuo> residuoDAO;

	@Override
	protected IDAO<Residuo> getDao() {
		return this.residuoDAO;
	}
	
	/**
	 * @author eduardo.fernandes
	 * @return lista de resíduos cadastrados no banco
	 * @
	 */
	public List<Residuo> listarResiduos() {
		return residuoDAO.listarTodos();
	}

	/**
	 * @Comentários Método que usa a nome do Residuo para retornar uma lista filtrada do banco.
	 * @param residuo
	 * @return List<Residuo>
	 * @author eduardo.fernandes
	 */
	@SuppressWarnings("unchecked")
	public List<Residuo> filtrarResiduos(Residuo residuo) {
		StringBuilder lEjbql = new StringBuilder("SELECT r FROM Residuo r ");
		if (!Util.isNull(residuo)) {
			residuo.setIndAtivo(true);
			lEjbql.append("WHERE r.indAtivo = :indAtivo ");
			if (!Util.isNull(residuo.getIdeResiduo())){
				lEjbql.append(" AND r.ideResiduo = :ideResiduo");
			}
			if (!Util.isNull(residuo.getNomResiduo())) {
				lEjbql.append(" AND (LOWER(r.nomResiduo) LIKE LOWER(:nomResiduo) OR LOWER(r.codResiduo) LIKE LOWER(:nomResiduo))");
			}
		}
		lEjbql.append(" ORDER BY r.codResiduo");
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lEjbql.toString());
		if (!Util.isNull(residuo)) {
			lQuery.setParameter("indAtivo", residuo.isIndAtivo());
			if (!Util.isNull(residuo.getIdeResiduo())) {
				lQuery.setParameter("ideResiduo", residuo.getIdeResiduo());
			}
			if (!Util.isNull(residuo.getNomResiduo())) {
				lQuery.setParameter("nomResiduo", "%" + residuo.getNomResiduo() + "%");
			}
		}
		return lQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Residuo> filtrarResiduos(String textFilter)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Residuo.class);
		criteria.add(Restrictions.or(Restrictions.ilike("codResiduo", "%" + textFilter + "%"), Restrictions.ilike("nomResiduo","%" + textFilter + "%")));
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.addOrder(Order.asc("codResiduo"));
		List<Residuo> listResiduo = residuoDAO.listarPorCriteria(criteria);
		
		return listResiduo;
	}

	/**
	 * @Comentários Método que retorna um Residuo pelo seu Id no banco.
	 * @param ide
	 * @return Residuo
	 * @
	 * @author eduardo.fernandes
	 */
	public Residuo obterResiduoByIdeResiduo(Integer ide)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideResiduo", ide);
		return residuoDAO.buscarEntidadePorNamedQuery("Residuo.findByIdeResiduo", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Residuo obterResiduoByCodigoResiduo(String codigo)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("codResiduo", codigo);
		return residuoDAO.buscarEntidadePorNamedQuery("Residuo.findByCodResiduo", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Residuo obterResiduoByNomeResiduo(String nome)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("nomResiduo", nome);
		return residuoDAO.buscarEntidadePorNamedQuery("Residuo.findByNomeResiduo", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Residuo obterResiduoByComposicaoResiduo(String composicao)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dscComposicaoResiduo", composicao);
		return residuoDAO.buscarEntidadePorNamedQuery("Residuo.findByComposicaoResiduo", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Residuo pResiduo)  {

		residuoDAO.salvar(pResiduo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Residuo pResiduo)  {
		residuoDAO.atualizar(pResiduo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Residuo pResiduo)  {

		residuoDAO.atualizar(pResiduo);
	}	
}
