package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.PessoaFisicaContratoConvenio;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaFisicaContratoConvenioService {

	@Inject
	private IDAO<PessoaFisicaContratoConvenio> pessoaFisicaContratoConvenioIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisicaContratoConvenio> listarPessoaFisicaContratoConvenioByConvenio(ContratoConvenio contratoConvenio) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisicaContratoConvenio.class);
		criteria
		.createAlias("idePessoaFisica", "pf",JoinType.INNER_JOIN)
		.createAlias("ideContratoConvenio", "cc",JoinType.INNER_JOIN)
		
		.add(Restrictions.and(Restrictions.eq("cc.ideContratoConvenio", contratoConvenio.getIdeContratoConvenio()), Restrictions.eq("indAtivo", true)))
		.setProjection(Projections.projectionList()
			.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
			.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.nomPessoa")
			.add(Projections.property("pf.numCpf"),"idePessoaFisica.numCpf")
			.add(Projections.property("indAtivo"),"indAtivo")
			.add(Projections.property("dtcCriacao"),"dtcCriacao")
			.add(Projections.property("cc.ideContratoConvenio"),"ideContratoConvenio.ideContratoConvenio")
		)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaFisicaContratoConvenio.class));		
		
		return pessoaFisicaContratoConvenioIDAO.listarPorCriteria(criteria, Order.asc("cc.ideContratoConvenio"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaFisicaContratoConvenio(PessoaFisicaContratoConvenio pfcc) {
		pessoaFisicaContratoConvenioIDAO.salvar(pfcc);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirVinculo(PessoaFisicaContratoConvenio pfcc) {
		pessoaFisicaContratoConvenioIDAO.remover(pfcc);
	}
}