package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaJuridicaCnaeService {

	private static final Long QTD_CNAE_PRINCIPAL = 1l;
	@Inject
	IDAO<PessoaJuridicaCnae> daoPessoaJuridicaCnae;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean salvarPessoaJuridicaCnae(PessoaJuridicaCnae pessoaJuridicaCnae) {
		Boolean retorno = Boolean.FALSE;
		if (validateCnaePrincipal(pessoaJuridicaCnae)) {
			daoPessoaJuridicaCnae.salvar(pessoaJuridicaCnae);
			retorno = Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean salvarOuAtualizarPessoaJuridicaCnae(PessoaJuridicaCnae pessoaJuridicaCnae){
		Boolean retorno = Boolean.FALSE;
		if (validateCnaePrincipal(pessoaJuridicaCnae)) {
			daoPessoaJuridicaCnae.salvarOuAtualizar(pessoaJuridicaCnae);
			retorno = Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Boolean validateCnaePrincipal(PessoaJuridicaCnae pessoaJuridicaCnae) {
		Boolean retorno = Boolean.TRUE;
		if (pessoaJuridicaCnae.getIndCnaePrincipal()) {
			DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridicaCnae.class);
			criteria.setProjection(Projections.count("indCnaePrincipal"))
			.add(Restrictions.ne("idePessoaJuridicaCnae", pessoaJuridicaCnae.getIdePessoaJuridicaCnae() != null ? pessoaJuridicaCnae.getIdePessoaJuridicaCnae():-1 )) //pra caso de estar editando
			        .add(Restrictions.and(
			        		Restrictions.eq("idePessoaJuridica", pessoaJuridicaCnae.getIdePessoaJuridica()),			        		
			        		Restrictions.eq("indCnaePrincipal", Boolean.TRUE)));
			retorno = (((Long)daoPessoaJuridicaCnae.executarCriteria(criteria)) >= QTD_CNAE_PRINCIPAL ) ? Boolean.FALSE : Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaJuridicaCnae> buscaPessoaJuridicaCnaePorPessoaJuridica(PessoaJuridica pessoajuridica) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePessoaJuridica", pessoajuridica);
		return daoPessoaJuridicaCnae.buscarPorNamedQuery("PessoaJuridicaCnae.findByPessoaJuridica", parametros);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaJuridicaCnae(PessoaJuridicaCnae pessoaJuridicaCnae){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idePessoaJuridicaCnae", pessoaJuridicaCnae.getIdePessoaJuridicaCnae());
		daoPessoaJuridicaCnae.executarNamedQuery("PessoaJuridicaCnae.removeByIdePessoaJuridicaCnae", param);
	}

}
