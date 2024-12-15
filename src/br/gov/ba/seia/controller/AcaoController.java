package br.gov.ba.seia.controller;

import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import javax.faces.component.UIForm;
import javax.faces.model.DataModel;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.service.AcaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class AcaoController extends SeiaControllerAb {

	private String descricaoAcao;
	private Acao acao;
	private DataModel<Acao> modelAcoes;

	private UIForm formularioASerLimpo;

	@EJB
	private AcaoService acaoService;

	public AcaoController() {}

	public void pesquisarAcao() {

		try {

			modelAcoes = Util.castListToDataModel(acaoService.filtrarLista(!Util.isNullOuVazio(getDescricaoAcao()) ? new Acao(getDescricaoAcao()) : new Acao()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void novaAcao() {

		setAcao(new Acao());

		limparTela();
	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}

	public void salvarAtualizarAcao() {

		try {

			if (Util.isNull(getAcao().getIdeAcao())) {

				acaoService.salvar(getAcao());
			}
			else {

				acaoService.atualizar(getAcao());
			}

			this.setModelAcoes(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	public void excluirAcao() {

		try {

			acaoService.excluir(getAcao());

			this.setModelAcoes(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}	

	public DataModel<Acao> getModelAcoes() {

		if (Util.isNull(modelAcoes)) {

			try {

				modelAcoes = Util.castListToDataModel(acaoService.filtrarLista(!Util.isNullOuVazio(getDescricaoAcao()) ? new Acao(getDescricaoAcao()) : new Acao()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelAcoes;
	}
	public void setModelAcoes(DataModel<Acao> modelAcoes) {
		this.modelAcoes = modelAcoes;
	}

	public Acao getAcao() {

		if (Util.isNull(acao)) acao = new Acao();

		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public String getDescricaoAcao() {
		return descricaoAcao;
	}
	public void setDescricaoAcao(String descricaoAcao) {
		this.descricaoAcao = descricaoAcao;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
}