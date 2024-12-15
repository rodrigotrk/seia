package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AreaMunicipio;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoAreaMunicipio;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaMunicipioService;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.TipoAreaMunicipioService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class AreaMunicipioController extends SeiaControllerAb {

	private AreaMunicipio areaMunicipio;

	private DataModel<AreaMunicipio> modelAreasMunicipios;

	private UIForm formularioASerLimpoPermissao;

	@EJB
	private AreaService areaService;
	
	@EJB
	private MunicipioService municipioService;
	
	
	@EJB
	private AreaMunicipioService areaMunicipioService;
	
	@EJB
	private TipoAreaMunicipioService tipoAreaMunicipioService;

	private String acao = "ALTERAR";

	public AreaMunicipioController() {}

	public void pesquisarAreaMunicipio() {

		try {

			modelAreasMunicipios = Util.castListToDataModel(areaMunicipioService.filtrarListaAreasMunicipios(getAreaMunicipio()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void limparTelaAreaMunicipio() {

		limparComponentesFormulario(formularioASerLimpoPermissao);
	}

	public void novaAreaMunicipio() {

		setAcao("CADASTRAR");
		setAreaMunicipio(new AreaMunicipio());

		limparTelaAreaMunicipio();
	}

	public void prepararAlterar() {

		setAcao("ALTERAR");
		limparTelaAreaMunicipio();
	}

	public void salvarAtualizarAreaMunicipio() {

		try {

			if ("ALTERAR".equals(getAcao())) {
				areaMunicipioService.atualizarAreaMunicipio(getAreaMunicipio());
				JsfUtil.addSuccessMessage("Alteração realizada com sucesso.");
			}
			else {

				areaMunicipioService.salvarAreaMunicipio(getAreaMunicipio());
				JsfUtil.addSuccessMessage("Cadastro realizado com sucesso.");
			}

			this.setAreaMunicipio(null);
			this.setModelAreasMunicipios(null);
		}
		catch (Exception exception) {

			this.addMensagemErro("Chave Violada: A relação entre área e Localidade já se encontra cadastrada.", null);
			getRequestContext().addCallbackParam("validationFailed", true);
		}
	}
	public void excluirAreaMunicipio() {

		try {

			areaMunicipioService.excluirAreaMunicipio(getAreaMunicipio());
			JsfUtil.addSuccessMessage("Exclusão realizada com sucesso.");
			this.setAreaMunicipio(null);
			this.setModelAreasMunicipios(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

    public Collection<Area> getColAreas() {
    	
    	Exception erro =null;
    	try {
			return areaService.listarAreas();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return null;
    }
    public Collection<Municipio> getColMunicipios() {
    	
    	Exception erro =null;
    	
    	try {
			return municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
    }
    
    
    public Collection<TipoAreaMunicipio> getColTipoAreaMunicipio() {
    	
    	Exception erro = null;
    	try {
			return tipoAreaMunicipioService.listarTipoAreaMunicipio();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
    }

    public boolean isDesabilitaCampos() {

    	if ("ALTERAR".equals(getAcao())) {

    		return true;
    	}
    	else {

    		return false;
    	}
    }

	public DataModel<AreaMunicipio> getModelAreasMunicipios() {

		if (Util.isNull(modelAreasMunicipios)) {

			try {

				modelAreasMunicipios = Util.castListToDataModel(areaMunicipioService.filtrarListaAreasMunicipios(getAreaMunicipio()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelAreasMunicipios;
	}
	public void setModelAreasMunicipios(DataModel<AreaMunicipio> modelAreasMunicipios) {
		this.modelAreasMunicipios = modelAreasMunicipios;
	}

	public AreaMunicipio getAreaMunicipio() {

		if (Util.isNullOuVazio(areaMunicipio)) areaMunicipio = new AreaMunicipio();
		return areaMunicipio;
	}
	public void setAreaMunicipio(AreaMunicipio areaMunicipio) {
		this.areaMunicipio = areaMunicipio;
	}

	public UIForm getFormularioASerLimpoPermissao() {
		return formularioASerLimpoPermissao;
	}
	public void setFormularioASerLimpoPermissao(UIForm formularioASerLimpoPermissao) {
		this.formularioASerLimpoPermissao = formularioASerLimpoPermissao;
	}

	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
}