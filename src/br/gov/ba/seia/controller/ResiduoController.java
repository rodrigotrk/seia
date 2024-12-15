package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Residuo;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ResiduoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class ResiduoController extends SeiaControllerAb {

	private String descricaoResiduo;
	private Residuo residuo;
	private DataModel<Residuo> modelResiduos;

	private UIForm formularioASerLimpo;
	private boolean visualizar;
	private Residuo residuoTemp;
	private String textFilter = "";
	
	List<String> listPericulosidade;
	
	String[] auxPericulosidade;
	List<String> listPericulosidadeSelecionados;

	@EJB
	private ResiduoService residuoService;

	public ResiduoController() {}
	
	@PostConstruct
	public void init(){
		residuo = new Residuo();
		residuoTemp = new Residuo();
		visualizar = false;
		
		listPericulosidadeSelecionados = new ArrayList<String>();
		preencherListaPericulosidade();
		
	}

	public void pesquisarResiduo() {

		try {

			modelResiduos = Util.castListToDataModel(residuoService.pesquisarResiduos(!Util.isNullOuVazio(getDescricaoResiduo()) ? new Residuo(getDescricaoResiduo()) : new Residuo()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void novoResiduo() {
		visualizar = false;
		setResiduo(new Residuo());
		listPericulosidadeSelecionados = new ArrayList<String>();

		limparTela();
	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}
	
	public void editar(){
		
		listPericulosidadeSelecionados = new ArrayList<String>();
		
		residuoTemp.setIdeResiduo(residuo.getIdeResiduo());
		residuoTemp.setCodResiduo(residuo.getCodResiduo());
		residuoTemp.setNomResiduo(residuo.getNomResiduo());
		residuoTemp.setDscComposicaoResiduo(residuo.getDscComposicaoResiduo());
		residuoTemp.setDscPericulosidade(residuo.getDscPericulosidade());
		
		String aux = residuoTemp.getDscPericulosidade();
		
		auxPericulosidade = aux.split(", ");
		
		for(String item : auxPericulosidade){
			listPericulosidadeSelecionados.add(item);
		}
		
		visualizar = false;
		
		limparTela();
	}

	public void salvarAtualizarResiduo() {

		try {

		//	Residuo residuoTempCodigo = residuoService.obterResiduoByCodigo(getResiduo().getCodResiduo());
			Residuo residuoTempNome = residuoService.obterResiduoByNome(getResiduo().getNomResiduo());
			
			String auxiliar = "";
			
			for(int i =0; i < listPericulosidadeSelecionados.size();i++){
				if(i != listPericulosidadeSelecionados.size()-1){
					auxiliar += listPericulosidadeSelecionados.get(i) + ", ";
				}else{
					auxiliar += listPericulosidadeSelecionados.get(i);
				}
			}
			
			residuo.setDscPericulosidade(auxiliar);
			
				
					if (Util.isNull(getResiduo().getIdeResiduo())) {
						
					//	if(Util.isNull(residuoTempCodigo)){
							
						if(Util.isNull(residuoTempNome)){
							residuo = getResiduo();
							residuo.setIndAtivo(true);
							residuoService.salvar(residuo);
							JsfUtil.addSuccessMessage("Salvo com sucesso!");
							Html.esconder("dlgResiduo");
						}else{
							JsfUtil.addErrorMessage("O nome informado já está cadastrado");
						}
						
				//		}else{
				//			JsfUtil.addErrorMessage("O código informado já está cadastrado");
				//		}
						
					}
					else {
						if(residuoTemp.getCodResiduo() != residuo.getCodResiduo() || residuoTemp.getNomResiduo() != residuo.getNomResiduo()
								|| residuoTemp.getDscComposicaoResiduo() != residuo.getDscComposicaoResiduo()  || residuoTemp.getDscPericulosidade() != residuo.getDscPericulosidade()){
							
							residuoTemp.setIndAtivo(false);
							residuoTemp.setDtcExclusao(new Date());
							residuoService.excluir(residuoTemp);
							
							residuo.setIdeResiduo(null);
							residuo.setDtcExclusao(null);
							residuo.setIndAtivo(true);
							residuoService.salvar(residuo);
						}else{
							residuoService.atualizar(getResiduo());
						}
						
						JsfUtil.addSuccessMessage("Resíduo atualizado com sucesso!");
						Html.esconder("dlgResiduo");
					}
	
					this.setModelResiduos(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	public void excluirResiduo() {

		try {
	
			residuo.setIndAtivo(false);
			residuo.setDtcExclusao(new Date());
			residuoService.excluir(residuo);
			
			this.setModelResiduos(null);
			JsfUtil.addSuccessMessage("Exclusão realizada com sucesso!");
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}	
	
	public void filtrarDados() {
		try {

			List<Residuo> lista = null;
			
			if(descricaoResiduo.length() >= 3){
				lista = residuoService.filtrarResiduos(descricaoResiduo);
				if(!Util.isNullOuVazio(lista)){
					modelResiduos = Util.castListToDataModel(lista);
					lista = null;
				}
			}else{
				modelResiduos = Util.castListToDataModel(residuoService.filtrarResiduos(descricaoResiduo));
			}
			
			Html.atualizar("formListaResiduos:dataTableResiduos");
			
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void visualizarDados(){
		
		listPericulosidadeSelecionados = new ArrayList<String>();
		
		String aux = residuo.getDscPericulosidade();
		
		if(!Util.isNullOuVazio(aux)){
			auxPericulosidade = aux.split(", ");
			for(String item : auxPericulosidade){
				listPericulosidadeSelecionados.add(item);
			}
		}
		
		visualizar = true;
	}

	public DataModel<Residuo> getModelAcoes() {

		if (Util.isNull(modelResiduos)) {

			try {

				modelResiduos = Util.castListToDataModel(residuoService.pesquisarResiduos(!Util.isNullOuVazio(getDescricaoResiduo()) ? new Residuo(getDescricaoResiduo()) : new Residuo()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelResiduos;
	}
	
	private void preencherListaPericulosidade() {
		listPericulosidade = new ArrayList<String>();
		listPericulosidade.add("Tóxico");
		listPericulosidade.add("Altamente tóxico");
		listPericulosidade.add("Inflamável");
		listPericulosidade.add("Infeccioso");
		listPericulosidade.add("Patogênico");
		listPericulosidade.add("Reativo");
		listPericulosidade.add("Cortante");
		listPericulosidade.add("Perfuro");
		listPericulosidade.add("Corrosivo");
		listPericulosidade.add("Explosivo");
		listPericulosidade.add("Radioativo");
	}
	
	public void setModelResiduos(DataModel<Residuo> modelResiduos) {
		this.modelResiduos = modelResiduos;
	}

	public Residuo getResiduo() {

		if (Util.isNull(residuo)) residuo = new Residuo();

		return residuo;
	}
	public void setResiduo(Residuo residuo) {
		this.residuo = residuo;
	}

	public String getDescricaoResiduo() {
		return descricaoResiduo;
	}

	public void setDescricaoResiduo(String descricaoResiduo) {
		this.descricaoResiduo = descricaoResiduo;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

	public String getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}

	public List<String> getListPericulosidade() {
		return listPericulosidade;
	}

	public void setListPericulosidade(List<String> listPericulosidade) {
		this.listPericulosidade = listPericulosidade;
	}

	public String[] getAuxPericulosidade() {
		return auxPericulosidade;
	}

	public void setAuxPericulosidade(String[] auxPericulosidade) {
		this.auxPericulosidade = auxPericulosidade;
	}

	public List<String> getListPericulosidadeSelecionados() {
		return listPericulosidadeSelecionados;
	}

	public void setListPericulosidadeSelecionados(
			List<String> listPericulosidadeSelecionados) {
		this.listPericulosidadeSelecionados = listPericulosidadeSelecionados;
	}

	public DataModel<Residuo> getModelResiduos() {
		return modelResiduos;
	}
	
}