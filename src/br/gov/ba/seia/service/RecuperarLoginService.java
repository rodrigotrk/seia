package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RecuperarLoginService {
	
	@Inject
	IDAO<PessoaFisica> daoPessoaF;
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica recuperarLoginService(String cpf, String email) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class);
		
		criteria.createAlias("pessoa", "p", JoinType.INNER_JOIN);
		criteria.createAlias("usuario", "u", JoinType.INNER_JOIN);
		
		
		criteria.add(Restrictions.eq("numCpf", cpf))
		.add(Restrictions.eq("p.desEmail", email))
		
		.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("idePessoaFisica"),"idePessoaFisica")
				.add(Projections.groupProperty("p.idePessoa"),"pessoa.idePessoa")
				.add(Projections.groupProperty("p.desEmail"),"pessoa.desEmail")
				.add(Projections.groupProperty("u.dscLogin"),"usuario.dscLogin")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaFisica.class));
		
		
		return daoPessoaF.buscarPorCriteria(criteria);
	}

	
	

}
