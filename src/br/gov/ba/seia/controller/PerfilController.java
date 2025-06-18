package br.gov.ba.seia.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.FuncionalidadeService;
import br.gov.ba.seia.service.PerfilService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("perfilController")
@ViewScoped
public class PerfilController extends SeiaControllerAb {

	private String descricaoPerfil;
	private Perfil perfil;
	private DataModel<Perfil> modelPerfis;
	private List<Funcionalidade> funcionalidades;
	
	private UIForm formularioASerLimpo;

	@EJB
	private PerfilService perfilService;
	@EJB
	private FuncionalidadeService funcionalidadeService;

	public PerfilController() {}

	public void pesquisarPerfil() {

		try {

			modelPerfis = Util.castListToDataModel(perfilService.filtrarListaPerfis(!Util.isNullOuVazio(getDescricaoPerfil()) ? new Perfil(getDescricaoPerfil()) : new Perfil()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void novoPerfil() {

		setPerfil(new Perfil());

		limparTela();
	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
		carregarFuncionalidades();
	}
	
	private void carregarFuncionalidades() {
		try {
			funcionalidades = funcionalidadeService.obterTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void salvarAtualizarPerfil() {

		try {

			if (Util.isNull(getPerfil().getIdePerfil())) {

				perfilService.salvarPerfil(getPerfil());
			}
			else {

				perfilService.atualizarPerfil(getPerfil());
			}

			this.setModelPerfis(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	public void excluirPerfil() {

		try {

			perfilService.excluirPerfil(getPerfil());

			this.setModelPerfis(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}	

	public DataModel<Perfil> getModelPerfis() {

		if (Util.isNull(modelPerfis)) {

			try {

				modelPerfis = Util.castListToDataModel(perfilService.filtrarListaPerfis(!Util.isNullOuVazio(getDescricaoPerfil()) ? new Perfil(getDescricaoPerfil()) : new Perfil()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelPerfis;
	}
	public void setModelPerfis(DataModel<Perfil> modelPerfis) {
		this.modelPerfis = modelPerfis;
	}

	public Perfil getPerfil() {

		if (Util.isNull(perfil)) perfil = new Perfil();

		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getDescricaoPerfil() {
		return descricaoPerfil;
	}
	public void setDescricaoPerfil(String descricaoPerfil) {
		this.descricaoPerfil = descricaoPerfil;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}
	public void setFuncionalidades(List<Funcionalidade> pFuncionalidades) {
		funcionalidades = pFuncionalidades;
	}
}