package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import javax.faces.component.UIForm;
import javax.faces.model.DataModel;

import br.gov.ba.seia.entity.NivelCompetencia;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.service.OrgaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class OrgaoController extends SeiaControllerAb {

	private String descricaoOrgao;
	private Orgao orgao;
	private DataModel<Orgao> modelOrgaos;

	private UIForm formularioASerLimpo;

	@EJB
	private OrgaoService orgaoService;

	public OrgaoController() {}

	public void pesquisarOrgao() {

		try {

			modelOrgaos = Util.castListToDataModel(orgaoService.filtrarListaOrgaos(!Util.isNullOuVazio(getDescricaoOrgao()) ? new Orgao(getDescricaoOrgao()) : new Orgao()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void novoOrgao() {

		setOrgao(new Orgao());

		limparTela();
	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}

	public void salvarAtualizarOrgao() {

		try {

			if (Util.isNull(getOrgao().getIdeOrgao())) {

				orgaoService.salvarOrgao(getOrgao());
			}
			else {

				orgaoService.atualizarOrgao(getOrgao());
			}

			this.setModelOrgaos(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void excluirOrgao() {

		try {

			orgaoService.excluirOrgao(getOrgao());

			this.setModelOrgaos(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

    public Collection<Orgao> getColOrgaos() {

    	return orgaoService.filtrarListaOrgaos(new Orgao());
    }
    public Collection<NivelCompetencia> getColNiveisCompetencia() {

    	return orgaoService.filtrarListaNiveisCompetencia(null);
    }

	public DataModel<Orgao> getModelOrgaos() {

		if (Util.isNull(modelOrgaos)) {

			try {

				modelOrgaos = Util.castListToDataModel(orgaoService.filtrarListaOrgaos(!Util.isNullOuVazio(getDescricaoOrgao()) ? new Orgao(getDescricaoOrgao()) : new Orgao()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelOrgaos;
	}
	public void setModelOrgaos(DataModel<Orgao> modelOrgaos) {
		this.modelOrgaos = modelOrgaos;
	}

	public Orgao getOrgao() {

		if (Util.isNull(orgao)) orgao = new Orgao();

		return orgao;
	}
	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}

	public String getDescricaoOrgao() {
		return descricaoOrgao;
	}
	public void setDescricaoOrgao(String descricaoOrgao) {
		this.descricaoOrgao = descricaoOrgao;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
}