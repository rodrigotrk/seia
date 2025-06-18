package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;

@Stateless
public class ResponsavelTecnicoService extends PessoaService {
	@Inject
	IDAO<ResponsavelEmpreendimento> daoResponsavelEmpreendimento;

	public void excluirResponsavelEmpreendimento(ResponsavelEmpreendimento responsavelEmpreendimento)  {
		daoResponsavelEmpreendimento.remover(responsavelEmpreendimento);
	}

	public Collection<ResponsavelEmpreendimento> listarResponsavelEmpreendimento() {
		return daoResponsavelEmpreendimento.buscarPorNamedQuery("ResponsavelEmpreendimento.findAll");
	}

	public Collection<ResponsavelEmpreendimento> filtrarListaResponsavelEmpreendimento(ResponsavelEmpreendimento responsavelEmpreendimento) {
		return daoResponsavelEmpreendimento.listarPorExemplo(responsavelEmpreendimento);
	}

}
