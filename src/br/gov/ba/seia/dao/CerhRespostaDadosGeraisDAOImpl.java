package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

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

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhRespostaDadosGerais;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhRespostaDadosGeraisDAOImpl extends AbstractDAO<CerhRespostaDadosGerais>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<CerhRespostaDadosGerais> getDAO() {
		return cerhRespostaDadosGeraisDAO;
	}
	
	@Inject
	private IDAO<CerhRespostaDadosGerais> cerhRespostaDadosGeraisDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhRespostaDadosGerais cerhRespostaDadosGerais) {
		cerhRespostaDadosGeraisDAO.salvarOuAtualizar(cerhRespostaDadosGerais);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(Collection<CerhRespostaDadosGerais> listaCerhRespostaDadosGerais) {
		cerhRespostaDadosGeraisDAO.salvarEmLote((List<CerhRespostaDadosGerais>) listaCerhRespostaDadosGerais);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhRespostaDadosGerais> listarCerhRespostaDadosGeraisPor(Cerh cerh) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhRespostaDadosGerais.class)
			.createAlias("ideCerhPerguntaDadosGerais", "pg", JoinType.INNER_JOIN)
			.createAlias("ideCerh", "c", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideCerh", cerh))
			
			.addOrder(Order.asc("pg.codPergunta"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerhRespostaDadosGerais"), "ideCerhRespostaDadosGerais")
				.add(Projections.property("ideCerhRespostaDadosGerais"), "ideCerhRespostaDadosGerais")
				.add(Projections.property("indResposta"), "indResposta")
				.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
				.add(Projections.property("c.ideCerh"), "ideCerh.ideCerh")
				.add(Projections.property("pg.ideCerhPerguntaDadosGerais"), "ideCerhPerguntaDadosGerais.ideCerhPerguntaDadosGerais")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhRespostaDadosGerais.class))
		;
		
		return cerhRespostaDadosGeraisDAO.listarPorCriteria(criteria);
		
	}

	
}
