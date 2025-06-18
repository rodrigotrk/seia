package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhPerguntaDadosGerais;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhPerguntaDadosGeraisDAOImpl extends AbstractDAO<CerhPerguntaDadosGerais>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<CerhPerguntaDadosGerais> getDAO() {
		return dao;
	}
	
	@Inject
	private IDAO<CerhPerguntaDadosGerais> dao;
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhPerguntaDadosGerais> listarCerhPerguntaDadosGerais() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhPerguntaDadosGerais.class)
			.addOrder(Order.asc("codPergunta"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerhPerguntaDadosGerais"), "ideCerhPerguntaDadosGerais")
				.add(Projections.property("codPergunta"), "codPergunta")
				.add(Projections.property("dscPergunta"), "dscPergunta")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("dtcExclusao"), "dtcExclusao")
				.add(Projections.property("indAtivo"), "indAtivo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhPerguntaDadosGerais.class))
		;
		
		return dao.listarPorCriteria(criteria);
		
	}
	
}