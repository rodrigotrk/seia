package br.gov.ba.seia.dao;

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

import br.gov.ba.seia.entity.PesquisaMineralDocumentoCaptacao;
import br.gov.ba.seia.entity.PesquisaMineralUsoDaAgua;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * {@link IDAO} de {@link PesquisaMineralDocumentoCaptacao}
 * 
 * @author eduardo.fernandes 
 * @since 18/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a> 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralDocumentoCaptacaoDAOImpl {

	@Inject
	private IDAO<PesquisaMineralDocumentoCaptacao> dao;
	
	/**
	 * Método que lista todas as {@link PesquisaMineralDocumentoCaptacao}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralDocumentoCaptacao> listarTodosDocumentoCaptacao() {
		return dao.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PesquisaMineralDocumentoCaptacao buscar(PesquisaMineralUsoDaAgua usoAgua) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PesquisaMineralUsoDaAgua.class)
			.createAlias("pesquisaMineralDocumentoCaptacao", "docCaptacao")	
			.add(Restrictions.eq("idePesquisaMineralUsoDaAgua", usoAgua.getIdePesquisaMineralUsoDaAgua()))
			.setProjection(Projections.projectionList()
					.add(Projections.property("docCaptacao.idePesquisaMineralDocumentoCaptacao"), "idePesquisaMineralDocumentoCaptacao")
					.add(Projections.property("docCaptacao.nomDocumento"), "nomDocumento")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(PesquisaMineralDocumentoCaptacao.class));
		return dao.buscarPorCriteria(criteria);
	}

	
}
