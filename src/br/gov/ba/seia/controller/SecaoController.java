package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import javax.faces.component.UIForm;
import javax.faces.model.DataModel;

import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.entity.TipoSecao;
import br.gov.ba.seia.service.SecaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class SecaoController extends SeiaControllerAb {

	private String descricaoSecao;
	private Secao secao;
	private DataModel<Secao> modelSecoes;

	private UIForm formularioASerLimpo;

	@EJB
	private SecaoService secaoService;

	public SecaoController() {}

	public void pesquisarSecao() {

		try {

			modelSecoes = Util.castListToDataModel(secaoService.filtrarListaSecoes(!Util.isNullOuVazio(getDescricaoSecao()) ? new Secao(getDescricaoSecao()) : new Secao()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void novaSecao() {

		setSecao(new Secao());

		limparTela();
	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}

	public void salvarAtualizarSecao() {

		try {

			if (Util.isNull(getSecao().getIdeSecao())) {

				secaoService.salvarSecao(getSecao());
			}
			else {

				secaoService.atualizarSecao(getSecao());
			}

			this.setModelSecoes(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	public void excluirSecao() {

		try {

			secaoService.excluirSecao(getSecao());

			this.setModelSecoes(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

    public Collection<Secao> getColSecoes() {

    	return secaoService.filtrarListaSecoes(new Secao());
    }
    public Collection<TipoSecao> getColTiposSecao() {

    	return secaoService.filtrarListaTiposSecao(new TipoSecao());
    }

	public DataModel<Secao> getModelSecoes() {

		if (Util.isNull(modelSecoes)) {

			try {

				modelSecoes = Util.castListToDataModel(secaoService.filtrarListaSecoes(!Util.isNullOuVazio(getDescricaoSecao()) ? new Secao(getDescricaoSecao()) : new Secao()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelSecoes;
	}
	public void setModelSecoes(DataModel<Secao> modelSecoes) {
		this.modelSecoes = modelSecoes;
	}

	public Secao getSecao() {

		if (Util.isNull(secao)) secao = new Secao();

		return secao;
	}
	public void setSecao(Secao secao) {
		this.secao = secao;
	}

	public String getDescricaoSecao() {
		return descricaoSecao;
	}
	public void setDescricaoSecao(String descricaoSecao) {
		this.descricaoSecao = descricaoSecao;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
}