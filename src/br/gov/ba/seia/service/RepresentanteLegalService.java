package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.RepresentanteLegalDAOImpl;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@SuppressWarnings("all")
public class RepresentanteLegalService {
	
	@Inject
	private RepresentanteLegalDAOImpl representanteLegalDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isRepresentanteLegalPessoaJuridica(Pessoa solicitante, Pessoa requerente)  {
		return 
			representanteLegalDAOImpl
				.isRepresentanteLegalPessoaJuridica(solicitante, requerente);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RepresentanteLegal> listarRepresentantesLegais(Collection<Pessoa> proprietarios)  {
		
		Collection<Integer> ids = new ArrayList<Integer>();
		
		for (Pessoa pessoa : proprietarios) {
			ids.add(pessoa.getIdePessoa());
		}
		
		return representanteLegalDAOImpl.listarRepresentantesLegais(ids);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RepresentanteLegal> listarRepresentantesLegaisPf(Collection<Pessoa> proprietarios)  {
		
		Collection<Integer> ids = new ArrayList<Integer>();
		
		for (Pessoa pessoa : proprietarios) {
			ids.add(pessoa.getIdePessoa());
		}
		
		return representanteLegalDAOImpl.listarRepresentantesLegaisPessoaFisica(ids);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> listarRepresentanteLegalPorPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		List<RepresentanteLegal> lista = representanteLegalDAOImpl.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
		
		if(!Util.isNullOuVazio(lista)){
			return lista;
			
		} else return new ArrayList<RepresentanteLegal>();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> listarRepresentanteLegalPorPessoaJuridicaComProjection(PessoaJuridica pessoaJuridica)  {
		List<RepresentanteLegal> lista = representanteLegalDAOImpl.listarRepresentanteLegalPorPessoaJuridicaComProjection(pessoaJuridica);
		
		if(!Util.isNullOuVazio(lista)){
			return lista;
			
		} else return new ArrayList<RepresentanteLegal>();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> getListaRepresentanteLegalPorRequerimento(Integer ideRequerimento)  {
		return representanteLegalDAOImpl.getListaRepresentanteLegalPorRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isRepresentanteLegalInativado(int idePessoaFisica, int idePessoaJuridica) {
		return representanteLegalDAOImpl.isRepresentanteLegalInativado(idePessoaFisica, idePessoaJuridica);

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRepresentanteLegal(RepresentanteLegal representanteLegal)  {
		representanteLegalDAOImpl.salvarRepresentanteLegal(representanteLegal);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRepresentanteLegal(RepresentanteLegal representanteLegal)  {
		representanteLegalDAOImpl.excluirRepresentanteLegal(representanteLegal);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RepresentanteLegal buscarPorIdViaCriteria(RepresentanteLegal representante) {
		return representanteLegalDAOImpl.buscarPorIdViaCriteria(representante);
	}
	
	/**
	 * Método que verifica se o usuário logado é o representante legal do CNPJ que está cadastrando 
	 * <li> <b>return</b> 1  ::: caso ele seja um dos representantes legais </li>
	 * <li> <b>return</b> 0  ::: caso o CNPJ ainda não tenha representantes cadastrados </li>
	 * <li> <b>return</b> -1 :: caso ele não seja o representante </li>
	 * <li> <b>return </b>null  :: caso algum dos parâmetros esteja nulo</li>
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer verificaRepresentanteLegal(PessoaFisica pessoaValidar, PessoaJuridica pessoaJuridica) {		
		if(!Util.isNull(pessoaValidar) && !Util.isNull(pessoaJuridica) && !Util.isNullOuVazio(pessoaJuridica.getIdePessoaJuridica())){
			List<RepresentanteLegal> listaRepresentantesLegais = this.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
			if(!Util.isNullOuVazio(listaRepresentantesLegais)){
				for (RepresentanteLegal representanteLegal : listaRepresentantesLegais) {
					if (pessoaValidar.getIdePessoaFisica().equals(representanteLegal.getIdePessoaFisica().getIdePessoaFisica())) {
							return 1;
						}
				}
				return -1;
			} else {
				return 0;
			}
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> buscarRepresentanteLegalByPessoaAndRequerente(Integer ideRepresentante,Integer idePessoaJuridica)  {
		return this.representanteLegalDAOImpl.buscarRepresentanteLegalByPessoa(ideRepresentante,idePessoaJuridica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> getListaRepresentanteLegalByPessoa(PessoaJuridica pessoaJuridica)  {
		List<RepresentanteLegal>lista = representanteLegalDAOImpl.getListaRepresentanteLegalByPessoa(pessoaJuridica);
		if(!Util.isNullOuVazio(lista)){
			return lista;
		}
		else {
			return new ArrayList<RepresentanteLegal>();
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> buscarRepresentanteLegal(Integer idePessoa)  {
		return this.representanteLegalDAOImpl.buscarRepresentanteLegal(idePessoa);
	}
		
}
