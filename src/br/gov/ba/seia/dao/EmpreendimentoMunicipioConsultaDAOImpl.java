package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.VwConsultaEmpreendimento;
import br.gov.ba.seia.util.Util;

public class EmpreendimentoMunicipioConsultaDAOImpl {

	public List<EnderecoEmpreendimento> listaEmpreendimentosComParametro(Municipio pMunicipio, Pessoa pRequerente, String pNome, List<Integer> idesPessoa) {

		StringBuilder lSql = new StringBuilder();

		lSql.append(" select vwConsultaEmpreendimento from VwConsultaEmpreendimento vwConsultaEmpreendimento where 1 = 1 ");

        if (!Util.isNullOuVazio(pMunicipio) && !Util.isNullOuVazio(pMunicipio.getIdeMunicipio())) {

        	lSql.append(" and vwConsultaEmpreendimento.ideMunicipio =  :ideMunicipio ");	
        }

        if (!Util.isNullOuVazio(pNome)) {

        	lSql.append(" and lower(vwConsultaEmpreendimento.nomEmpreendimento) like lower(:nomEmpreendimento) ");
        }

        if (!Util.isNullOuVazio(pRequerente) && !Util.isNullOuVazio(pRequerente.getIdePessoa())) {

        	lSql.append(" and vwConsultaEmpreendimento.idePessoa = :idePessoa ");
        }
        
        if (!Util.isNullOuVazio(idesPessoa)) {

        	lSql.append(" and vwConsultaEmpreendimento.idePessoa in (:idesPessoa) ");
        }

		EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lSql.toString());

    	if (!Util.isNullOuVazio(pMunicipio) && !Util.isNullOuVazio(pMunicipio.getIdeMunicipio())) {

    		lQuery.setParameter("ideMunicipio", pMunicipio.getIdeMunicipio());
    	}

    	if (!Util.isNullOuVazio(pNome)) {

    		lQuery.setParameter("nomEmpreendimento", pNome + '%');
    	}

    	if (!Util.isNullOuVazio(pRequerente) && !Util.isNullOuVazio(pRequerente.getIdePessoa())) {

    		lQuery.setParameter("idePessoa", pRequerente.getIdePessoa());
    	}
    	
    	if (!Util.isNullOuVazio(idesPessoa)) {

    		lQuery.setParameter("idesPessoa", idesPessoa);
    	}
    	@SuppressWarnings("unchecked")
    	Collection<VwConsultaEmpreendimento> lColecaoVwConsultaEmpreendimento = lQuery.getResultList();
    	List<EnderecoEmpreendimento> lColecaoEnderecoEmpreendimento = new ArrayList<EnderecoEmpreendimento>();

    	for (VwConsultaEmpreendimento lVwConsultaEmpreendimento : lColecaoVwConsultaEmpreendimento) {

    		lColecaoEnderecoEmpreendimento.add(new EnderecoEmpreendimento(
    				new Endereco(new Logradouro(new Municipio(lVwConsultaEmpreendimento.getIdeMunicipio(), lVwConsultaEmpreendimento.getNomMunicipio()))), 
    				new Empreendimento(lVwConsultaEmpreendimento.getIdeEmpreendimento(), lVwConsultaEmpreendimento.getNomEmpreendimento(), !Util.isNullOuVazio(lVwConsultaEmpreendimento.getNomPessoa()) 
    						? new Pessoa(lVwConsultaEmpreendimento.getIdePessoa(), new PessoaFisica(lVwConsultaEmpreendimento.getIdePessoa(), lVwConsultaEmpreendimento.getNomPessoa())): 
    							!Util.isNullOuVazio(lVwConsultaEmpreendimento.getNomRazaoSocial()) ? new Pessoa(lVwConsultaEmpreendimento.getIdePessoa(), new PessoaJuridica(lVwConsultaEmpreendimento.getIdePessoa(), lVwConsultaEmpreendimento.getNomRazaoSocial())): null)));
    	}

    	return lColecaoEnderecoEmpreendimento;
	}

}