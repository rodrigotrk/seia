package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

import br.gov.ba.seia.entity.PotencialPoluicao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PotencialPoluicaoDAOImpl {
	
	@Inject
	private IDAO<PotencialPoluicao> potencialPoluicaoDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarPotencialPoluicao(PotencialPoluicao potencialPoluicao) {
		potencialPoluicaoDAO.salvarOuAtualizar(potencialPoluicao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PotencialPoluicao> listarPotencialPoluicao() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PotencialPoluicao.class);
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePotencialPoluicao"),"idePotencialPoluicao")
				.add(Projections.property("sglPotencialPoluicao"),"sglPotencialPoluicao")
				.add(Projections.property("nomPotencialPoluicao"),"nomPotencialPoluicao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(PotencialPoluicao.class))
		;
		
		return potencialPoluicaoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PotencialPoluicao> listaPotencialPoluicao() {
		return potencialPoluicaoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PotencialPoluicao carregarPotencialPoluicao(Integer id){
		return potencialPoluicaoDAO.carregarGet(id);
	}


}