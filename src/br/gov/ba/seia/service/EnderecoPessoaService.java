package br.gov.ba.seia.service;

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
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoPessoaService {
	
	@Inject
	private IDAO<EnderecoPessoa> enderecoPessoaDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoPessoa(EnderecoPessoa enderecoPessoa)  {
		enderecoPessoaDAO.salvarOuAtualizar(enderecoPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoPessoa buscarEnderecoPorPessoa(Pessoa pessoa)  {
		
		EnderecoPessoa enderecoPessoa = null;
		
		if (!Util.isNull(pessoa) && !Util.isNull(pessoa.getIdePessoa())) {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoPessoa.class);
			criteria.createAlias("ideEndereco", "endereco");
			criteria.createAlias("endereco.ideLogradouro", "logradouro",JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("logradouro.ideMunicipio", "municipio",JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("logradouro.ideBairro", "bairro",JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("logradouro.ideTipoLogradouro", "tipoLogradouro",JoinType.LEFT_OUTER_JOIN);
			criteria.setFetchMode("endereco.ideLogradouro", FetchMode.JOIN);
			criteria.add(Restrictions.eq("idePessoa.idePessoa", pessoa.getIdePessoa()));
			
			List<EnderecoPessoa> listEndPessoa = null;
			
			listEndPessoa = enderecoPessoaDAO.listarPorCriteria(criteria, Order.desc("ideEnderecoPessoa"));
			
			if(!Util.isNullOuVazio(listEndPessoa)){
				enderecoPessoa = listEndPessoa.get(0);
			}
		}
		
		return enderecoPessoa;
	}
}
