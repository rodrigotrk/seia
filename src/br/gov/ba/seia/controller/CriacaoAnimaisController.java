package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.CriacaoAnimal;
import br.gov.ba.seia.entity.CriacaoAnimalFinalidadeTipologiaAtividade;
import br.gov.ba.seia.entity.CriacaoAnimalFinalidadeTipologiaAtividadePK;
import br.gov.ba.seia.entity.Finalidade;
import br.gov.ba.seia.entity.FinalidadeTipologiaAtividade;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.entity.UnidadeTipologiaAtividade;
import br.gov.ba.seia.enumerator.FinalidadeEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CriacaoAnimalService;
import br.gov.ba.seia.service.TipologiaAtividadeService;
import br.gov.ba.seia.service.UnidadeTipologiaAtividadeService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("criacaoAnimaisController")
@ViewScoped
public class CriacaoAnimaisController {
	
	@EJB
	private TipologiaAtividadeService tipologiaAtividadeService;
	@EJB
	private CriacaoAnimalService criacaoAnimalService;
	@EJB
	private UnidadeTipologiaAtividadeService unidadeTipologiaAtividadeService;
	
	@Inject
	private AgrossilvopastorilController agrossilvopastorilController;
	
	private List<TipologiaAtividade> listaTipologiaAtividade;
	private List<TipologiaAtividade> listaTipologiaAtividadeAll;
	private TipologiaAtividade tipologiaAtividade;
	private List<UnidadeMedida> listaUnidadeMedida;
	private List<CriacaoAnimal> listaCriacaoAnimal;
	private CriacaoAnimal criacaoAnimal;
	private String nomTipologiaAtividade;
	private final Finalidade finalidadeOutros = new Finalidade(FinalidadeEnum.OUTROS.getId(), "Outros");
	
	private Boolean regularizacaoAtivCefir;
	
	public void init() {
		carregarTipologiaAtividade();
		if(agrossilvopastorilController.isFceSalvo()){
			carregarAbaCriacaoAnimais();
		}
		
	}
	
	public void carregarTipologiaAtividade(){
		listaTipologiaAtividadeAll = new ArrayList<TipologiaAtividade>();
		Exception erro = null;
		try {
			listaTipologiaAtividade = tipologiaAtividadeService.buscarTipologiaAtividadeByIdeTipologia(new Tipologia(7));//Criacao Animais
			listaTipologiaAtividadeAll.addAll(listaTipologiaAtividade);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void pesquisarTipologiaAtividade(){
		listaTipologiaAtividade = new ArrayList<TipologiaAtividade>();
		boolean achou = false;
		for (TipologiaAtividade tipologiaAtividade : listaTipologiaAtividadeAll) {
			if(tipologiaAtividade.getDscTipologiaAtividade().toLowerCase().indexOf(nomTipologiaAtividade.toLowerCase())!= -1){
				listaTipologiaAtividade.add(tipologiaAtividade);
				achou = true;
			}
		}
		if(!achou && nomTipologiaAtividade.equals("")){
			listaTipologiaAtividade.addAll(listaTipologiaAtividadeAll);
		}
	}
	
	public void adicionarTipologiaAtividade(){
		CriacaoAnimal cricaoAnimal = new CriacaoAnimal(new UnidadeTipologiaAtividade(tipologiaAtividade),false);
		Exception erro = null;
		try {
			cricaoAnimal.setListaFinalidade(tipologiaAtividadeService.buscarFinalidadeTipologiaAtividadeByIdeTipologia(tipologiaAtividade));
			cricaoAnimal.getListaFinalidade().remove(finalidadeOutros);
			cricaoAnimal.getListaFinalidade().add(finalidadeOutros);//Fez necessário para que a finalidade "OUTROS" permaneça no final da lista
			tipologiaAtividade.setListaUnidade(tipologiaAtividadeService.buscarUnidadeMedidaByTipologiaAtividade(tipologiaAtividade));
			if(tipologiaAtividade.getListaUnidade().size() == 1){
				cricaoAnimal.getIdeUnidadeTipologiaAtividade().setIdeUnidadeMedida(tipologiaAtividade.getListaUnidade().iterator().next());
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		if(Util.isNull(listaCriacaoAnimal)){
			listaCriacaoAnimal = new ArrayList<CriacaoAnimal>();
		}
		listaCriacaoAnimal.add(cricaoAnimal);
		listaTipologiaAtividade.remove(tipologiaAtividade);
		listaTipologiaAtividadeAll.remove(tipologiaAtividade);
		Collections.sort(listaTipologiaAtividade);
		Collections.sort(listaTipologiaAtividadeAll);
	}
	
	public void excluirTipologiaAtividade(){
		Exception erro =null;
		try {
			if(!Util.isNull(criacaoAnimal.getIdeCriacaoAnimal())){
				criacaoAnimalService.excluirCriacaoAFTAtividadeByIdeCriacaoAnimal(criacaoAnimal);
				criacaoAnimalService.excluirCriacaoAnimal(criacaoAnimal);
			}
			listaCriacaoAnimal.remove(criacaoAnimal);
			listaTipologiaAtividade.add(criacaoAnimal.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade());
			listaTipologiaAtividadeAll.add(criacaoAnimal.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade());
			Collections.sort(listaTipologiaAtividade);
			Collections.sort(listaTipologiaAtividadeAll);
			AgrossilvopastorilController.msgExclusao();
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Erro ao excluir criacao de animal");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean salvarAbaCriacaoAnimais(Boolean isSalvarFinal){
		if(validarAbaCriacaoAnimais()){
			Exception erro = null;
			try {
				for (CriacaoAnimal criacaoAnimal : listaCriacaoAnimal) {
					UnidadeTipologiaAtividade unidadeTipologiaAtividade;
					criacaoAnimalService.excluirCriacaoAFTAtividadeByIdeCriacaoAnimal(criacaoAnimal);
					unidadeTipologiaAtividade = unidadeTipologiaAtividadeService.buscarByIdeUnidadeETipologiaAtividade(criacaoAnimal.getIdeUnidadeTipologiaAtividade().getIdeUnidadeMedida(), criacaoAnimal.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade());
					criacaoAnimal.setIdeUnidadeTipologiaAtividade(unidadeTipologiaAtividade);
					criacaoAnimal.setIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
					criacaoAnimalService.salvarCriacaoAnimal(criacaoAnimal);
					salvarCriacaoAFTAtividade(criacaoAnimal);
				}
				if(!isSalvarFinal){
					JsfUtil.addSuccessMessage("Salvo com sucesso.");
					agrossilvopastorilController.avancarAba();
				}
				return true;
			} catch (Exception e) {
				erro =e;
				JsfUtil.addErrorMessage("Erro ao salvar Criação de Animais.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				return false;
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return false;
	}
	/**
	 * @throws Exception
	 * @INFO Salva a lista de Finalidades selecionadas na tabela CriacaoAnimalFinalidadeTipologiaAtividade
	 */
	public void salvarCriacaoAFTAtividade(CriacaoAnimal criacaoAnimal) {
		List<FinalidadeTipologiaAtividade> listaFinalidadeTipologiaAtividade = new ArrayList<FinalidadeTipologiaAtividade>();
		CriacaoAnimalFinalidadeTipologiaAtividade criacaoAnimalTipo = new CriacaoAnimalFinalidadeTipologiaAtividade();
		for (Finalidade finalidade : criacaoAnimal.getListaFinalidadeSelecionadas()) {
			listaFinalidadeTipologiaAtividade.add(tipologiaAtividadeService.buscarFinaTipoAtivByTipologiaAtivEFinali(criacaoAnimal.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade(),finalidade));
		}
		for (FinalidadeTipologiaAtividade finalidadeTipologiaAtividade : listaFinalidadeTipologiaAtividade) {
			criacaoAnimalTipo.setId(new CriacaoAnimalFinalidadeTipologiaAtividadePK(criacaoAnimal.getIdeCriacaoAnimal(), finalidadeTipologiaAtividade.getIdeFinalidadeTipologiaAtividade()));
			criacaoAnimalTipo.setIdeCriacaoAnimal(criacaoAnimal);
			criacaoAnimalTipo.setIdeFinalidadeTipologiaAtividade(finalidadeTipologiaAtividade);
			criacaoAnimalService.salvarCriacaoAnimalFinalidadeTipologiaAtividade(criacaoAnimalTipo);
		}
	}
	
	public boolean validarAbaCriacaoAnimais(){
		boolean valido = true;
		if(Util.isNullOuVazio(listaCriacaoAnimal)){
			valido = false;
			AgrossilvopastorilController.campoObrigatorio("Tipos de Cultura");
		}else{
			for (CriacaoAnimal criacaoAnimalObj : listaCriacaoAnimal) {
				String tipoCriacaoAnimal = criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade().getDscTipologiaAtividade();
				if(!criacaoAnimalObj.isDesabilitado()){
					valido = false;
					JsfUtil.addErrorMessage("Favor confirmar os dados da Espécie animal  "+tipoCriacaoAnimal + ".");
				}
				if(Util.isNull(criacaoAnimalObj.getNumCapacidade()) || criacaoAnimalObj.getNumCapacidade() == 0){
					valido = false;
					AgrossilvopastorilController.campoObrigatorio("Área a ser Ocupada de "+tipoCriacaoAnimal);
				}
				if(Util.isNull(criacaoAnimalObj.getNumCabecas()) || criacaoAnimalObj.getNumCabecas() == 0){
					valido = false;
					AgrossilvopastorilController.campoObrigatorio("O Número de cabeças de "+tipoCriacaoAnimal);
				}
				if(Util.isNull(criacaoAnimalObj.getNumProducao()) || criacaoAnimalObj.getNumProducao().compareTo(BigDecimal.ZERO) == 0){
					valido = false;
					AgrossilvopastorilController.campoObrigatorio("A produção/und de "+tipoCriacaoAnimal);
				}
				if(Util.isNull(criacaoAnimalObj.getNumAnimais()) || criacaoAnimalObj.getNumAnimais() == 0){
					valido = false;
					AgrossilvopastorilController.campoObrigatorio("O Número de animais de "+tipoCriacaoAnimal);
				}
				if(Util.isNull(criacaoAnimalObj.getListaFinalidadeSelecionadas()) || criacaoAnimalObj.getListaFinalidadeSelecionadas().isEmpty()){
					valido = false;
					AgrossilvopastorilController.campoObrigatorio("Finalidades de "+tipoCriacaoAnimal);
				}
			}
		}
		return valido;
	}
	
	public void carregarAbaCriacaoAnimais(){
		Exception erro =null;
		try {
			listaCriacaoAnimal = criacaoAnimalService.buscarCriacaoAnimalByIdeFceAgrossilvo(agrossilvopastorilController.getFceAgrossilvopastoril());
			for (CriacaoAnimal criacaoAnimalObj : listaCriacaoAnimal) {
				criacaoAnimalObj.setDesabilitado(true);
				listaTipologiaAtividade.remove(criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade());
				listaTipologiaAtividadeAll.remove(criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade());
				criacaoAnimalObj.setIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				criacaoAnimalObj.setNumProducaoInt(criacaoAnimalObj.getNumProducao().intValue());
				criacaoAnimalObj.setListaFinalidade(tipologiaAtividadeService.buscarFinalidadeTipologiaAtividadeByIdeTipologia(criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade()));
				criacaoAnimalObj.setListaFinalidadeSelecionadas(criacaoAnimalService.buscarFinalidadeByIdeCriacaoAnimal(criacaoAnimalObj));
				criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade().setListaUnidade((tipologiaAtividadeService.buscarUnidadeMedidaByTipologiaAtividade(criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade())));
				if(criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade().getListaUnidade().size() == 1){
					criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().setIdeUnidadeMedida(criacaoAnimalObj.getIdeUnidadeTipologiaAtividade().getIdeTipologiaAtividade().getListaUnidade().iterator().next());
				}
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	@SuppressWarnings("unchecked")
	public void changeCheckBox(ValueChangeEvent ev){
		List<Finalidade> finalidadesNew = new ArrayList<Finalidade>();
		finalidadesNew.addAll((List<Finalidade>) ev.getNewValue());
		if(!Util.isNull(ev.getOldValue())){
			finalidadesNew.removeAll((List<Finalidade>) ev.getOldValue());
		}
		for (Finalidade finalidade : finalidadesNew) {
			if(finalidade.equals(new Finalidade(FinalidadeEnum.OUTROS.getId()))){
				RequestContext.getCurrentInstance().execute("infoOutros.show()");
				return;
			}
		}
		
	}
	/**
	 * @return
	 * @INFO Retorna a finalidade do tipo outros
	 */
	public Finalidade getFinalidadeOutros(){
		Exception erro =null;
		try {
			return tipologiaAtividadeService.getFinalidadeOutros();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return new Finalidade(FinalidadeEnum.OUTROS.getId(), "Outros");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	/**
	 * @return
	 * @INFO verifica se a 2a grid pode ou não ser renderizada
	 */
	public boolean isExibirTblTipoCriacaoSelecionados() {
		return !Util.isNullOuVazio(listaCriacaoAnimal);
	}
	
	public boolean isUnidade(ValueChangeEvent ev) {
		UnidadeMedida um = (UnidadeMedida) ev.getNewValue();
		if(um.getCodUnidadeMedida().equals("Un"))
			return true;
		else
			return false;
	}
	
	public List<UnidadeMedida> getListaUnidadeMedida() {
		return listaUnidadeMedida;
	}

	public void setListaUnidadeMedida(List<UnidadeMedida> listaUnidadeMedida) {
		this.listaUnidadeMedida = listaUnidadeMedida;
	}

	public List<TipologiaAtividade> getListaTipologiaAtividade() {
		return listaTipologiaAtividade;
	}

	public void setListaTipologiaAtividade(List<TipologiaAtividade> listaTipologiaAtividade) {
		this.listaTipologiaAtividade = listaTipologiaAtividade;
	}

	public String getNomTipologiaAtividade() {
		return nomTipologiaAtividade;
	}

	public void setNomTipologiaAtividade(String nomTipologiaAtividade) {
		this.nomTipologiaAtividade = nomTipologiaAtividade;
	}

	public List<CriacaoAnimal> getListaCriacaoAnimal() {
		return listaCriacaoAnimal;
	}

	public void setListaCriacaoAnimal(List<CriacaoAnimal> listaCriacaoAnimal) {
		this.listaCriacaoAnimal = listaCriacaoAnimal;
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public CriacaoAnimal getCriacaoAnimal() {
		return criacaoAnimal;
	}

	public void setCriacaoAnimal(CriacaoAnimal criacaoAnimal) {
		this.criacaoAnimal = criacaoAnimal;
	}

	public Boolean getRegularizacaoAtivCefir() {
		return regularizacaoAtivCefir;
	}

	public void setRegularizacaoAtivCefir(Boolean regularizacaoAtivCefir) {
		this.regularizacaoAtivCefir = regularizacaoAtivCefir;
	}
}