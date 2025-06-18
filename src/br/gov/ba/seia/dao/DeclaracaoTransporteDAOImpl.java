package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.DeclaracaoTransporte;
import br.gov.ba.seia.entity.Requerimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoTransporteDAOImpl {

	@Inject
	private IDAO<DeclaracaoTransporte> declaracaoTransporteDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoTransporte obterDeclaracaoPorRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoTransporte.class)
				.createAlias("atoDeclaratorio", "atoDeclaratorio")
				.createAlias("atoDeclaratorio.requerimento", "requerimento")
				
				.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.eq("requerimento.indExcluido", false));
;
				
		
		return declaracaoTransporteDAO.buscarPorCriteria(criteria);
		
	}
	
}
