package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import javax.faces.component.UIForm;
import javax.faces.model.DataModel;

import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.service.FuncionalidadeService;
import br.gov.ba.seia.service.SecaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class FuncionalidadeController extends SeiaControllerAb {

	private String descricaoFuncionalidade;
	private Funcionalidade funcionalidade;
	private DataModel<Funcionalidade> modelFuncionalidades;

	private UIForm formularioASerLimpo;

	@EJB
	private FuncionalidadeService funcionalidadeService;
	@EJB
	private SecaoService secaoService;

	public FuncionalidadeController() {}

	public void pesquisarFuncionalidade() {

		try {

			modelFuncionalidades = Util.castListToDataModel(funcionalidadeService.filtrarListaFuncionalidades(!Util.isNullOuVazio(getDescricaoFuncionalidade()) ? new Funcionalidade(getDescricaoFuncionalidade()) : new Funcionalidade()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void novaFuncionalidade() {

		setFuncionalidade(new Funcionalidade());

		limparTela();
	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}

	public void salvarAtualizarFuncionalidade() {

		try {

			if (Util.isNull(getFuncionalidade().getIdeFuncionalidade())) {

				funcionalidadeService.salvarFuncionalidade(getFuncionalidade());
			}
			else {

				funcionalidadeService.atualizarFuncionalidade(getFuncionalidade());
			}

			this.setModelFuncionalidades(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void excluirFuncionalidade() {

		try {

			funcionalidadeService.excluirFuncionalidade(getFuncionalidade());

			this.setModelFuncionalidades(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

    public Collection<Secao> getColSecoes() {

    	return secaoService.filtrarListaSecoes(new Secao());
    }

	public DataModel<Funcionalidade> getModelFuncionalidades() {

		if (Util.isNull(modelFuncionalidades)) {

			try {

				modelFuncionalidades = Util.castListToDataModel(funcionalidadeService.filtrarListaFuncionalidades(!Util.isNullOuVazio(getDescricaoFuncionalidade()) ? new Funcionalidade(getDescricaoFuncionalidade()) : new Funcionalidade()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelFuncionalidades;
	}
	public void setModelFuncionalidades(DataModel<Funcionalidade> modelFuncionalidades) {
		this.modelFuncionalidades = modelFuncionalidades;
	}

	public Funcionalidade getFuncionalidade() {

		if (Util.isNull(funcionalidade)) funcionalidade = new Funcionalidade();

		return funcionalidade;
	}
	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public String getDescricaoFuncionalidade() {
		return descricaoFuncionalidade;
	}
	public void setDescricaoFuncionalidade(String descricaoFuncionalidade) {
		this.descricaoFuncionalidade = descricaoFuncionalidade;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
}