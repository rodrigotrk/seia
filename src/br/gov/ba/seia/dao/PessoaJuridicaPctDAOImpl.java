package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.PessoaJuridicaPct;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaJuridicaPctDAOImpl {

	@Inject
	private IDAO<PessoaJuridicaPct> idao;
	
	
	public boolean existePessoaJuridicaAssociadaComunidade(PctImovelRural pctImovelRural, PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridicaPct.class, "pJP");
		
		criteria.createAlias("pJP.idePctImovelRural", "pIR", JoinType.INNER_JOIN);
		
		criteria.createAlias("pJP.idePessoaJuridica", "pJ", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("pJP.idePctImovelRural.idePctImovelRural", pctImovelRural.getIdePctImovelRural()));
		
		criteria.add(Restrictions.eq("pJ.numCnpj", pessoaJuridicaPct.getIdePessoaJuridica().getNumCnpj()));
		
		criteria.add(Restrictions.eq("pJP.indExcluido", Boolean.FALSE));
		
		PessoaJuridicaPct pessoa= idao.buscarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(pessoa)){
			return true;
		}
		
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaJuridicarPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		
		idao.salvarOuAtualizar(pessoaJuridicaPct);
	}
	
	
	public List<PessoaJuridicaPct> listarPessoaJuridicaByPct(PctImovelRural pctImovelRural) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridicaPct.class, "pJP");
		
		criteria.setFetchMode("pJP.idePessoaJuridica.representanteLegalCollection", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("pJP.idePctImovelRural.idePctImovelRural", pctImovelRural.getIdePctImovelRural()));
		
		criteria.add(Restrictions.eq("pJP.indExcluido", Boolean.FALSE));
		
		return idao.listarPorCriteria(criteria, Order.asc("pJP.idePessoaJuridicaPct"));
	}

	public PessoaJuridicaPct obterPessoaJuridicaPct(Integer idePessoaJuridicaPct) throws Exception {
		return idao.carregarGet(idePessoaJuridicaPct);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updatePessoaJuridicarPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception {
		idao.atualizar(pessoaJuridicaPct);
		
	}
}
