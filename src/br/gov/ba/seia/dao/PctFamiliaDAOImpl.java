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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

//import br.gov.ba.seia.entity.Parentesco;
import br.gov.ba.seia.entity.PctFamilia;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.PessoaFisica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PctFamiliaDAOImpl {

	@Inject
	private IDAO<PctFamilia> pctFamiliaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(PctFamilia pctFamilia) throws Exception {
		pctFamiliaDAO.sessionFlush();
		pctFamiliaDAO.sessionClear();
		pctFamiliaDAO.salvarOuAtualizar(pctFamilia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(PctFamilia pctFamilia) throws Exception {
		pctFamiliaDAO.sessionFlush();
		pctFamiliaDAO.sessionClear();		
		pctFamiliaDAO.atualizar(pctFamilia);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PctFamilia> listarPctFamilia(PctImovelRural pctImovelRural) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctFamilia.class, "pfa");
		criteria.createAlias("pfa.idePessoa", "pes", JoinType.INNER_JOIN);
		criteria.createAlias("pes.pessoaFisica", "psi", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePctImovelRural", "pct", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePessoaAssociada", "pa", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("pct.idePctImovelRural", pctImovelRural.getIdePctImovelRural()));
		criteria.add(Restrictions.eq("pfa.indExcluido", false));
		criteria.add(Restrictions.isNotNull("pfa.idePessoaAssociada"));
		
		criteria.addOrder(Order.asc("pct.idePctImovelRural"));
		
		return pctFamiliaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PctFamilia obterPctFamiliaPorCpf(PctImovelRural pctImovelRural, PessoaFisica pessoaFisica) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctFamilia.class, "pfa");
		criteria.createAlias("pfa.idePessoa", "pes", JoinType.INNER_JOIN);
		criteria.createAlias("pes.pessoaFisica", "psi", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePctImovelRural", "pct", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("pct.idePctImovelRural", pctImovelRural.getIdePctImovelRural()));
		criteria.add(Restrictions.eq("psi.numCpf", pessoaFisica.getNumCpf()));
		criteria.add(Restrictions.eq("pfa.indExcluido", false));
		
		criteria.addOrder(Order.asc("pct.idePctImovelRural"));
		
		return pctFamiliaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PctFamilia> listarMembrosFamiliaPorRepresentante(PctFamilia pctFamilia) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctFamilia.class, "pfa");
		criteria.createAlias("pfa.idePessoa", "pes", JoinType.INNER_JOIN);
		criteria.createAlias("pes.pessoaFisica", "psi", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePctImovelRural", "pct", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePessoaAssociada", "pa", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("pfa.idePessoaAssociada", pctFamilia.getIdePessoa()));
		criteria.add(Restrictions.eq("pfa.indExcluido", false));
		
		return pctFamiliaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PctFamilia> listarPctRepresentanteFamilia(PctImovelRural pctImovelRural) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctFamilia.class, "pfa");
		criteria.createAlias("pfa.idePessoa", "pes", JoinType.INNER_JOIN);
		criteria.createAlias("pes.pessoaFisica", "psi", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePctImovelRural", "pct", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("pct.idePctImovelRural", pctImovelRural.getIdePctImovelRural()));
		criteria.add(Restrictions.eq("pfa.indExcluido", false));
		criteria.add(Restrictions.isNull("pfa.idePessoaAssociada"));
		
		criteria.addOrder(Order.asc("pct.idePctImovelRural"));
		
		return pctFamiliaDAO.listarPorCriteria(criteria);
	}
	
	public boolean existePessoaPctFamilia(String cpf) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctFamilia.class, "pfa");
		criteria.createAlias("pfa.idePessoa", "pes", JoinType.INNER_JOIN);
		criteria.createAlias("pes.pessoaFisica", "psi", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePctImovelRural", "pct", JoinType.INNER_JOIN);
		criteria.createAlias("pct.ideImovelRural", "ir", JoinType.INNER_JOIN);
		criteria.createAlias("ir.imovel", "i");
		
		criteria.add(Restrictions.eq("i.indExcluido", false));
		criteria.add(Restrictions.eq("pfa.indExcluido", false));
		criteria.add(Restrictions.eq("psi.numCpf", cpf));

		
		boolean retorno = pctFamiliaDAO.listarPorCriteria(criteria).size() > 0;
		
		return retorno;
	}

	public PctFamilia obterPessoaPctFamilia(String cpf) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctFamilia.class, "pfa");
		criteria.createAlias("pfa.idePessoa", "pes", JoinType.INNER_JOIN);
		criteria.createAlias("pes.pessoaFisica", "psi", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePctImovelRural", "pct", JoinType.INNER_JOIN);
		criteria.createAlias("pct.ideImovelRural", "ir", JoinType.INNER_JOIN);
		criteria.createAlias("ir.imovel", "i");
		
		criteria.add(Restrictions.eq("i.indExcluido", false));
		criteria.add(Restrictions.eq("pfa.indExcluido", false));
		criteria.add(Restrictions.eq("psi.numCpf", cpf));
		
		
		return pctFamiliaDAO.buscarPorCriteria(criteria);
		
	}
	
	public PctFamilia obterPessoaPctFamiliaPctImovelRural(String cpf, PctImovelRural pctImovelRural) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctFamilia.class, "pfa");
		criteria.createAlias("pfa.idePessoa", "pes", JoinType.INNER_JOIN);
		criteria.createAlias("pes.pessoaFisica", "psi", JoinType.INNER_JOIN);
		criteria.createAlias("pfa.idePctImovelRural", "pct", JoinType.INNER_JOIN);
		criteria.createAlias("pct.ideImovelRural", "ir", JoinType.INNER_JOIN);
		criteria.createAlias("ir.imovel", "i");
		
		criteria.add(Restrictions.eq("i.indExcluido", false));
		criteria.add(Restrictions.eq("pfa.indExcluido", false));
		criteria.add(Restrictions.eq("psi.numCpf", cpf));
		criteria.add(Restrictions.eq("pct.idePctImovelRural", pctImovelRural.getIdePctImovelRural()));
		
		return pctFamiliaDAO.buscarPorCriteria(criteria);
		
	}
	
}
