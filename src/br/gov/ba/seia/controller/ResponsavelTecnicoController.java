package br.gov.ba.seia.controller;


import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.facade.ResponsavelTecnicoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("responsavelTecnicoController")
@ViewScoped
public class ResponsavelTecnicoController {

	@EJB
	private ResponsavelTecnicoServiceFacade serviceFacade;
	
	private Pessoa pessoa;
	private PessoaFisica pessoaSelecionada = new PessoaFisica();
	
	private DataModel<ResponsavelEmpreendimento> modelResponsavelEmpreendimentos = new ListDataModel<ResponsavelEmpreendimento>();
	
	public String consultarPessoaFisica() {
		try {
			
			this.pessoaSelecionada = serviceFacade.filtrarPessoaFisicaByCpf(this.pessoaSelecionada);
			
			this.pessoa = this.pessoaSelecionada.getPessoa();
//			carregarTelefones();
//			carregarDocumentosIdentificacao();
			
			
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
		return null;
	}

	
	public DataModel<ResponsavelEmpreendimento> getModelResponsavelEmpreendimentos() {
		@SuppressWarnings("unchecked")
		ListDataModel<ResponsavelEmpreendimento> listaResponsavelSession = (ListDataModel<ResponsavelEmpreendimento>)Util.getAttributeSession(Util.LISTA_RESPONSAVEL_TECNICO_SESSION);
		if (Util.isNull(listaResponsavelSession)) {
			atualizarListaResponsavelEmpreendimento();
			Util.updateAttributeSession(Util.LISTA_PESSOA_JURIDICA_SESSION, modelResponsavelEmpreendimentos);
		} else {
			modelResponsavelEmpreendimentos = listaResponsavelSession;
		}
		return modelResponsavelEmpreendimentos;
	}
	

	
	private void atualizarListaResponsavelEmpreendimento() {
		try {
			modelResponsavelEmpreendimentos = Util.castListToDataModel(serviceFacade.listarResponsavelEmpreendimento());
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	/**
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return pessoa;
	}


	/**
	 * @param pessoa the pessoa to set
	 */
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
	
}
