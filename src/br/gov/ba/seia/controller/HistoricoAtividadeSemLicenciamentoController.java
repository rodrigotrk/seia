package br.gov.ba.seia.controller;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.facade.CadastroAtividadeFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 13/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8193">#8193</a>
 */
@Named("historicoAtividadeSemLicenciamentoController")
@ViewScoped
public class HistoricoAtividadeSemLicenciamentoController {
	
	@EJB
	private CadastroAtividadeFacade facade;

	private CadastroAtividadeNaoSujeitaLic cadastro;

	public void carregarDialog() {
		try {
			
			cadastro = facade.buscar(cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
			cadastro.setCadastroAtividadeNaoSujeitaLicStatus(facade.listarCadastroStatus(cadastro));
			cadastro.setCadastroAtividadeNaoSujeitaLicComunicacaos(facade.listarComunicacao(cadastro));
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

}
