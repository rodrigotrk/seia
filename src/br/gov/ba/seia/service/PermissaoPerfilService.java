package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProcuradorPessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.ProcuradorRepresentanteDAOImpl;
import br.gov.ba.seia.dao.RepresentanteLegalDAOImpl;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.util.ContextoUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PermissaoPerfilService {

	@EJB
	ProcuradorPessoaFisicaDAOImpl procuradorPessoaFisicaDAOImpl;
	
	@Inject
	ProcuradorRepresentanteDAOImpl procuradorRepresentanteDAOImpl;
	
	@Inject
	RepresentanteLegalDAOImpl representanteLegalDAOImpl;
	
	public List<Integer> listarIdesPessoas()  {
		List<Integer> idesPessoas = new ArrayList<Integer>();
		idesPessoas.addAll(listarIdesPessoaFisicaAptos());
		idesPessoas.addAll(listarIdesPessoaJuridicaAptos());
		
		return idesPessoas;
	}
	
	public List<Integer> listarIdesPessoaFisicaAptos()  {
		List<Integer> idesPessoaFisica = new ArrayList<Integer>(0);
		PessoaFisica pessoaFisicaUsuarioLogado =  ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		
		//Se o perfil for de usuário interno listam-se todas as Pessoas Físicas, caso contrário, aplicam-se os filtros.
		if(ContextoUtil.getContexto().isUsuarioExterno()){
			for (ProcuradorPessoaFisica procuradorPessoaFisica : procuradorPessoaFisicaDAOImpl.pessoasFisicasDoProcurador(pessoaFisicaUsuarioLogado)) {
				idesPessoaFisica.add(procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica());
			}
			
			idesPessoaFisica.add(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
			
			if(idesPessoaFisica.isEmpty()){
				idesPessoaFisica.add(-1);
			}
		}
		
		return idesPessoaFisica;
	}
	
	public List<Integer> listarIdesPessoaJuridicaAptos()  {
		List<Integer> idesPessoasJuridicas = new ArrayList<Integer>(0);
		PessoaFisica pessoaFisicaUsuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		
		//Se o perfil for de usuário interno listam-se todas as Pessoas Jurídicas, caso contrário, aplicam-se os filtros.
		if(ContextoUtil.getContexto().isUsuarioExterno()){
			
			for(ProcuradorRepresentante procuradorRepresentante: procuradorRepresentanteDAOImpl.pessoasJuridicasDoProcurador(pessoaFisicaUsuarioLogado)){
				idesPessoasJuridicas.add(procuradorRepresentante.getIdePessoaJuridica().getIdePessoaJuridica());
			}
			
			for(RepresentanteLegal representanteLegal : representanteLegalDAOImpl.pessoasJuridicasDoRepresentanteLegal(pessoaFisicaUsuarioLogado)) {
				idesPessoasJuridicas.add(representanteLegal.getIdePessoaJuridica().getIdePessoaJuridica());
			}
			
			if(idesPessoasJuridicas.isEmpty()){
				idesPessoasJuridicas.add(-1);
			}
		}
		
		return idesPessoasJuridicas;
	}
	
	public List<Integer> listarIdesPessoasAptas(PessoaFisica usuarioLogado)  {
		List<Integer> idesPessoa = new ArrayList<Integer>();
			for (ProcuradorPessoaFisica procuradorPessoaFisica : procuradorPessoaFisicaDAOImpl.pessoasFisicasDoProcurador(usuarioLogado)) {
				idesPessoa.add(procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica());
			}
			
			for(ProcuradorRepresentante procuradorRepresentante: procuradorRepresentanteDAOImpl.pessoasJuridicasDoProcurador(usuarioLogado)){
				idesPessoa.add(procuradorRepresentante.getIdePessoaJuridica().getIdePessoaJuridica());
			}
			
			for (RepresentanteLegal representanteLegal : representanteLegalDAOImpl.pessoasJuridicasDoRepresentanteLegal(usuarioLogado)) {
				idesPessoa.add(representanteLegal.getIdePessoaJuridica().getIdePessoaJuridica());
			}
			
			idesPessoa.add(usuarioLogado.getIdePessoaFisica());
		
		return idesPessoa;
	}
	
	public List<String> listarCpfCnpJPessoasAptas(PessoaFisica usuarioLogado) {
		List<String> cpfCnpjPessoa = new ArrayList<String>();
		
		for(ProcuradorPessoaFisica procuradorPessoaFisica: procuradorPessoaFisicaDAOImpl.cpfPessoasFisicasDoProcurador(usuarioLogado)){
			cpfCnpjPessoa.add(procuradorPessoaFisica.getIdePessoaFisica().getNumCpf());
		}
		
		for (ProcuradorRepresentante procuradorRepresentante : procuradorRepresentanteDAOImpl.cnpjPessoasJuridicasDoProcurador(usuarioLogado)) {
			cpfCnpjPessoa.add(procuradorRepresentante.getIdePessoaJuridica().getNumCnpj());
		}
		
		for (RepresentanteLegal representanteLegal : representanteLegalDAOImpl.cnpjPessoasJuridicasDoRepresentanteLegal(usuarioLogado)) {
			cpfCnpjPessoa.add(representanteLegal.getIdePessoaJuridica().getNumCnpj());
		}
		
		return cpfCnpjPessoa;
	}
}
