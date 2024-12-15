package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaService;

@Stateless
public class ResponsavelTecnicoServiceFacade  extends PessoaService{	
	
	@Inject
	IDAO<ResponsavelEmpreendimento> daoResponsavelEmpreendimento;		
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	public PessoaFisica filtrarPessoaFisicaByCpf(PessoaFisica pessoaFisica) throws Exception{
		return   pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaFisica);
	}
	
	public void excluirResponsavelEmpreendimento(ResponsavelEmpreendimento responsavelEmpreendimento) throws Exception{			
		daoResponsavelEmpreendimento.remover(responsavelEmpreendimento);
	}
	
	
	public Collection<ResponsavelEmpreendimento> listarResponsavelEmpreendimento() {
		return daoResponsavelEmpreendimento.buscarPorNamedQuery("ResponsavelEmpreendimento.findAll");
	}	
	
	public Collection<ResponsavelEmpreendimento> filtrarListaResponsavelEmpreendimento(ResponsavelEmpreendimento responsavelEmpreendimento){
		return daoResponsavelEmpreendimento.listarPorExemplo(responsavelEmpreendimento);
	}	
	
}
