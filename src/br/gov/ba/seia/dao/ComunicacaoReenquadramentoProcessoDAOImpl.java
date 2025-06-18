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
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.ComunicacaoReenquadramentoProcesso;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoReenquadramentoProcessoDAOImpl {

	@Inject
	private IDAO<ComunicacaoReenquadramentoProcesso> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComunicacaoReenquadramentoProcesso> listarComunicacaoDemanda(Integer ideProcessoReenquadramento, int startPage, int maxPage)  {
		DetachedCriteria criteria = criteriaCarregaComunicacao(ideProcessoReenquadramento)
			.addOrder(Order.desc("ideComunicacaoReenquadramentoProcesso"))
		;
		return dao.listarPorCriteriaDemanda(criteria, startPage, maxPage);
	}
	
	private DetachedCriteria criteriaCarregaComunicacao(Integer ideProcessoReenquadramento){
		return  DetachedCriteria.forClass(ComunicacaoReenquadramentoProcesso.class)
				.createAlias("ideProcessoReenquadramento", "pr")
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("ideComunicacaoReenquadramentoProcesso"), "ideComunicacaoReenquadramentoProcesso")
								.add(Projections.property("desMensagem"), "desMensagem")
								.add(Projections.property("dtcComunicacao"), "dtcComunicacao")
								.add(Projections.property("pr.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ComunicacaoReenquadramentoProcesso.class))
				.add(Restrictions.eq("pr.ideProcessoReenquadramento", ideProcessoReenquadramento));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComunicacaoReenquadramento(ComunicacaoReenquadramentoProcesso comunicacaoReenquadramentoProcesso) {
		dao.salvarOuAtualizar(comunicacaoReenquadramentoProcesso);
	}
}
