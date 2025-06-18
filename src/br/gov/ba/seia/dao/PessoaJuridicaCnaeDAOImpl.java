package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;


public class PessoaJuridicaCnaeDAOImpl {
	
	@Inject
	IDAO<PessoaJuridicaCnae> pessoaJuridicaCnaeDAO;
	
	
	public List<Cnae> listarCnaePorPessoaJuridica(PessoaJuridica pessoaJuridica) {		
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePessoaJuridica", pessoaJuridica);
		Collection<PessoaJuridicaCnae> listaPessoaJuridicaCnae = pessoaJuridicaCnaeDAO.buscarPorNamedQuery("PessoaJuridicaCnae.findByPessoaJuridica", parametros);			
		List<Cnae> listaCnae = new ArrayList<Cnae>();
		
		for (PessoaJuridicaCnae pessoaJuridicaCnae : listaPessoaJuridicaCnae) {
			listaCnae.add(pessoaJuridicaCnae.getIdeCnae());
		}		
		return listaCnae;
	}
	
	
}
