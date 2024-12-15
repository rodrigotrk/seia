package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Portaria;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PortariaDAOimpl {

	@Inject
	private IDAO<Portaria> portariaDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Portaria portaria) {
		portariaDAO.salvarOuAtualizar(portaria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public Portaria buscarPortariaByProcesso(Processo processo) {
		DetachedCriteria detachedcriteria = DetachedCriteria.forClass(Portaria.class)
			.createAlias("idePessoaFisica", "func")
			.createAlias("func.pessoaFisica", "pessoa")
			
			.add(Restrictions.eq("ideProcesso.ideProcesso", processo.getIdeProcesso()))
		
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePortaria"),"idePortaria")
				.add(Projections.property("dtcPortaria"),"dtcPortaria")
				.add(Projections.property("dtcPublicacaoPortaria"),"dtcPublicacaoPortaria")
				.add(Projections.property("numPortaria"),"numPortaria")
				.add(Projections.property("textoPortaria"),"textoPortaria")
				.add(Projections.property("idePessoaFisica"),"idePessoaFisica")
				.add(Projections.property("ideProcesso"),"ideProcesso")
				.add(Projections.property("ideTipoPortaria"),"ideTipoPortaria")
				
				.add(Projections.property("func.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pessoa.idePessoaFisica"),"idePessoaFisica.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pessoa.nomPessoa"),"idePessoaFisica.pessoaFisica.nomPessoa")
				
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Portaria.class)) ; 

		return portariaDAO.buscarPorCriteria(detachedcriteria);
	}
}
