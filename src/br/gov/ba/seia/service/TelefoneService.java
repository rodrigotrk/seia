package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.PessoaDAOImpl;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TelefoneService {
	
	@Inject
	private IDAO<Telefone> daoTelefone;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Telefone> filtrarTelefonesNaoExcluidosPorPessoa(Pessoa pPessoa) throws Exception {
		
		List<Telefone> lColecaoTelefoneNaoExcluidos = new ArrayList<Telefone>();
		
		pPessoa = new PessoaDAOImpl().getPessoa(pPessoa); //Já carrega alguns dados da Pesosa
		
		for (Telefone lTelefone : pPessoa.getTelefoneCollection()) {
			
			if (!lTelefone.getIndExcluido()) {
				lColecaoTelefoneNaoExcluidos.add(lTelefone);
			}
		}
		
		return lColecaoTelefoneNaoExcluidos;
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Telefone> buscarTelefonesPorPessoa(Pessoa pessoa) throws Exception {
		
		if (!Util.isNull(pessoa) && !Util.isNull(pessoa.getIdePessoa())) {
			
			DetachedCriteria criteria= DetachedCriteria.forClass(Telefone.class)
					.setFetchMode("ideTipoTelefone", FetchMode.JOIN)
					.createAlias("pessoaCollection", "pessoas", JoinType.INNER_JOIN)
					
					.add(Restrictions.eq("pessoas.idePessoa", pessoa.getIdePessoa()))
					.add(Restrictions.eq("indExcluido", Boolean.FALSE));
			
			return daoTelefone.listarPorCriteria(criteria);
		} else {
			return new ArrayList<Telefone>();
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Telefone filtrarTelefonePorID(Integer ideTelefone) throws Exception {
		
			DetachedCriteria criteria= DetachedCriteria.forClass(Telefone.class);
			criteria.setFetchMode("ideTipoTelefone", FetchMode.JOIN);
			criteria.createAlias("pessoaCollection", "pessoas", JoinType.INNER_JOIN);
			
			criteria.add(Restrictions.eq("ideTelefone", ideTelefone));
			criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
			
			return daoTelefone.buscarPorCriteria(criteria);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTelefones(Collection<Telefone> telefones) throws Exception {
		for(Telefone telefone : telefones){
			salvarTelefone(telefone);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTelefone(Telefone telefone) throws Exception {
		
		telefone.setIndExcluido(Boolean.FALSE);
		telefone.setDtcCriacao(new Date());
		
		daoTelefone.salvarOuAtualizar(telefone);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTelefone(Telefone telefone) throws Exception {
		
		Map<String, Object> paramTelefone = new HashMap<String, Object>();
		
		paramTelefone.put("ideTelefone", telefone.getIdeTelefone());
		paramTelefone.put("dtcExclusao", new Date());
		
		daoTelefone.executarNamedQuery("Telefone.remove", paramTelefone);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Telefone> listarTelefone(Pessoa pessoa) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(Telefone.class)
				.createAlias("ideTipoTelefone", "tt")
				.createAlias("pessoaCollection", "p")
				.add(Restrictions.eq("p.idePessoa", pessoa.getIdePessoa()))
				.add(Restrictions.eq("indExcluido", false))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideTelefone"), "ideTelefone")
						.add(Projections.property("numTelefone"), "numTelefone")
						.add(Projections.property("tt.ideTipoTelefone"), "ideTipoTelefone.ideTipoTelefone")
						.add(Projections.property("tt.nomTipoTelefone"), "ideTipoTelefone.nomTipoTelefone")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(Telefone.class))
						;
		return daoTelefone.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Telefone> filtraTelefonePorPessoa(Pessoa pessoa) throws Exception {
		if (!Util.isNull(pessoa) && !Util.isNull(pessoa.getIdePessoa())) {
			DetachedCriteria criteria= DetachedCriteria.forClass(Telefone.class);
			criteria.setFetchMode("ideTipoTelefone", FetchMode.JOIN);
			criteria.createAlias("pessoaCollection", "pessoas", JoinType.INNER_JOIN);
			criteria.add(Restrictions.eq("pessoas.idePessoa", pessoa.getIdePessoa()));
			criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
			List<Telefone> telefones = daoTelefone.listarPorCriteria(criteria);
			return telefones;
		} else {
			return new ArrayList<Telefone>();
		}
	}
}