package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.LogradouroDAOImpl;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Logradouro;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LogradouroService {
	
	@Inject
	private LogradouroDAOImpl logradouroDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLogradouro(Logradouro logradouro)  {
		logradouroDAOImpl.salvarLogradouro(logradouro);
	}
		
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Logradouro> filtrarLogradouroByCep(Logradouro logradouro)  {
		return logradouroDAOImpl.filtrarLogradouroByCep(logradouro);		
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Logradouro> filtrarLogradouroByCep(Integer numCep, Integer ideLogradouro)  {
		return logradouroDAOImpl.filtrarLogradouroByCep(numCep, ideLogradouro);		
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByCepSemIndCorreio(Logradouro logradouro)  {
		return logradouroDAOImpl.filtrarLogradouroByCepSemIndCorreio(logradouro);		
	}
		
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Logradouro filtrarLogradouroById(Logradouro logradouro)  {
		return logradouroDAOImpl.filtrarLogradouroById(logradouro);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Logradouro> filtrarLogradouroByBairro(Bairro bairro, Integer numCep)  {
		return logradouroDAOImpl.filtrarLogradouroByBairro(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Logradouro> filtrarLogradouroByBairroAndApi(Bairro bairro, Integer numCep)  {
		return logradouroDAOImpl.filtrarLogradouroByBairroAndApi(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Logradouro> filtrarLogradouroByBairroSemIndCorreio(Bairro bairro, Integer numCep)  {
		return logradouroDAOImpl.filtrarLogradouroByBairroSemIndCorreio(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Logradouro> filtrarLogradouroByNome(Bairro bairro, Integer numCep)  {
		return logradouroDAOImpl.filtrarLogradouroByNome(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Logradouro> filtrarLogradouroByNomeAndApi(Bairro bairro, Integer numCep)  {
		return logradouroDAOImpl.filtrarLogradouroByNomeAndApi(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Logradouro getLogradouroById(int ideLogradouro)  {
		return logradouroDAOImpl.getLogradouroById(ideLogradouro);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Logradouro carregarIdeByEndereco(Endereco endereco)  {
		return logradouroDAOImpl.getLogradouroBy(endereco);
	}
	
	public Logradouro carregarLogradouro(String nmLogradouro,Integer ideBairro,Integer ideCidade,Integer numCep,Integer ideTipoLogradouro)  {
		return logradouroDAOImpl.getLogradouroByEndereco(nmLogradouro,ideBairro, ideCidade, numCep, ideTipoLogradouro);
	}
}
