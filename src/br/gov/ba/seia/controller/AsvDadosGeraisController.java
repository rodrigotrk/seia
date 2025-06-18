package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ClassificacaoVegetacao;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.FceAsv;
import br.gov.ba.seia.entity.FceAsvClassiVegetacao;
import br.gov.ba.seia.entity.FceAsvClassiVegetacaoPK;
import br.gov.ba.seia.entity.ProdutoSupressao;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @date 15/08/2013
 */
@Named("asvDadosGeraisController")
@ViewScoped
public class AsvDadosGeraisController extends FceController implements FceNavegacaoInterface{

	@Inject
	private AsvSupressaoController asvSupressaoController;

	@EJB
	private AsvSupressaoService asvSupressaoService;

	private static final int IDE_CLASSIFICACAO_VEGETACAO_OUTROS = 17;
	protected static final DocumentoObrigatorio DOC_FCE_ASV = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_ASV.getId());

	private FceAsv fceAsv;

	private int activeTab;
	private boolean desabilitaAba;
	private ClassificacaoVegetacao tipoClassificacao;
	private String nomClassificacao;

	private List<ClassificacaoVegetacao> listaClassificacao;
	private List<ClassificacaoVegetacao> listaClassificacaoAll;
	private List<ClassificacaoVegetacao> listaClassificacaoSelecionada;
	private List<ClassificacaoVegetacao> listaClassificacaoTemp;
	
	private boolean isNotFceASV;

	@Override
	public void init() {
		carregarAba();
		if(isFceSalvo()){
			desabilitaAba = false;
			prepararFceAsv();
		} else {
			iniciarFce(DOC_FCE_ASV);
			fceAsv = new FceAsv(super.fce);
		}
		asvSupressaoController.init();
	}

	public void carregarClassificacao (){
		listaClassificacao = new ArrayList<ClassificacaoVegetacao>();
		listaClassificacaoAll = new ArrayList<ClassificacaoVegetacao>();
		try {
			listaClassificacao = asvSupressaoService.listarClassificacaoVegetal();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

		listaClassificacaoAll.addAll(listaClassificacao);
		ordenaListaClassificacaoVegetacao();
	}

	private void ordenaListaClassificacaoVegetacao() {
		if(listaClassificacao.contains(new ClassificacaoVegetacao(IDE_CLASSIFICACAO_VEGETACAO_OUTROS))){
			int x = listaClassificacao.indexOf(new ClassificacaoVegetacao(IDE_CLASSIFICACAO_VEGETACAO_OUTROS));
			ClassificacaoVegetacao classif = listaClassificacao.get(x);
			int y = listaClassificacaoAll.indexOf(new ClassificacaoVegetacao(IDE_CLASSIFICACAO_VEGETACAO_OUTROS));
			ClassificacaoVegetacao classifAll = listaClassificacaoAll.get(y);
			listaClassificacao.remove(x);
			listaClassificacaoAll.remove(y);
			listaClassificacao.add(classif);
			listaClassificacaoAll.add(classifAll);
		}
	}

	public void prepararFceAsv(){
		carregarFceAsv();
		if (!Util.isNullOuVazio(listaClassificacaoSelecionada)){
			for (ClassificacaoVegetacao classificacaoVegetacao : listaClassificacaoSelecionada) {
				if(listaClassificacao.contains(classificacaoVegetacao)){
					listaClassificacao.remove(classificacaoVegetacao);
				}
			}
		}
	}

	public void carregarFceAsv(){
		try {
			fceAsv = asvSupressaoService.buscarFceAsvByIdeFce(super.fce);
			if(!Util.isNull(fceAsv)){
				buscarFceAsvExistente();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void buscarFceAsvExistente(){
		try {
			listaClassificacaoSelecionada = asvSupressaoService.listarClassificacaoVegetacaoByIdeFce(fceAsv);
			listaClassificacaoTemp = new ArrayList<ClassificacaoVegetacao>();
			listaClassificacaoTemp.addAll(listaClassificacaoSelecionada);
			for (ClassificacaoVegetacao classificacaoVegetacao : listaClassificacaoSelecionada) {
				listaClassificacao.remove(classificacaoVegetacao);
				listaClassificacaoAll.remove(classificacaoVegetacao);
			}
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void pesquisarClassificacao(){
		listaClassificacao = new ArrayList<ClassificacaoVegetacao>();
		for (ClassificacaoVegetacao tipoClass : listaClassificacaoAll) {
			if(tipoClass.getDscClassificacaoVegetacao().toLowerCase().indexOf(nomClassificacao.toLowerCase())!= -1){
				listaClassificacao.add(tipoClass);
			}
		}
		if(Util.isNullOuVazio(listaClassificacao) && !existeOutros()){
			try{
				listaClassificacao.add(asvSupressaoService.buscarClassificacaoVegetacaoByIde(IDE_CLASSIFICACAO_VEGETACAO_OUTROS));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * verifica se já existe uma FCE
	 */
	@Override
	public void verificarEdicao(){
		if(!Util.isNullOuVazio(requerimento)){
			try {
				carregarFceDoRequerente(DOC_FCE_ASV);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	public boolean existeOutros(){
		return !Util.isNullOuVazio(listaClassificacaoSelecionada) && listaClassificacaoSelecionada.contains(new ClassificacaoVegetacao(IDE_CLASSIFICACAO_VEGETACAO_OUTROS));
	}

	public void adicionarClassificacao(){
		if(Util.isNullOuVazio(listaClassificacaoSelecionada)){
			listaClassificacaoSelecionada = new ArrayList<ClassificacaoVegetacao>();
		}

		listaClassificacaoSelecionada.add(tipoClassificacao);
		listaClassificacao.remove(tipoClassificacao);
		listaClassificacaoAll.remove(tipoClassificacao);

		if(tipoClassificacao.equals(new ClassificacaoVegetacao(IDE_CLASSIFICACAO_VEGETACAO_OUTROS))){
			JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info001"));
		}
		tipoClassificacao = new ClassificacaoVegetacao();
		ordenaListaClassificacaoVegetacao();
	}

	public void excluirClassificacao(){
		listaClassificacaoSelecionada.remove(tipoClassificacao);
		listaClassificacao.add(tipoClassificacao);
		listaClassificacaoAll.add(tipoClassificacao);
		Collections.sort(listaClassificacaoAll);
		Collections.sort(listaClassificacao);
		ordenaListaClassificacaoVegetacao();
		JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
		tipoClassificacao = new ClassificacaoVegetacao();
	}

	@Override
	public boolean validarAba() {
		if(Util.isNullOuVazio(listaClassificacaoSelecionada)){
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_msg010"));
			return false;
		}
		return true;
	}

	@Override
	public void finalizar(){
		try {
			asvSupressaoService.finalizar(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - ASV.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.concluirFce();
		salvarDadosGerais();
		asvSupressaoController.finalizarAbaSupressao();
		
		if (asvSupressaoController.getFceAsv().getIdeFce().isFceTecnico()){
			 Html.executarJS("atualizarDadoConcedido();");
		}
	}

	private void excluirListaClassificacao() throws Exception{
		asvSupressaoService.excluirFceAsvClassiVegetacao(fceAsv);
	}

	private void salvarFceAsv() {
		if(Util.isNullOuVazio(fceAsv)) {
		
			fceAsv = new FceAsv(super.fce);
			
		}
		
		asvSupressaoService.salvarFceAsv(fceAsv);
	}

	public void salvarListaClassificacaoVegetacaoSelecionada(){
		if(!Util.isNullOuVazio(listaClassificacaoSelecionada)){
			asvSupressaoService.excluirFceAsvClassiVegetacao(fceAsv);
			for (ClassificacaoVegetacao classificacao : listaClassificacaoSelecionada){
				FceAsvClassiVegetacao fceAsvClassiVegetacao = new FceAsvClassiVegetacao(new FceAsvClassiVegetacaoPK(fceAsv.getIdeFceAsv(), classificacao.getIdeClassificacaoVegetacao()));
				fceAsvClassiVegetacao.setIdeFceAsv(fceAsv);
				fceAsvClassiVegetacao.setIdeClassificacaoVegetacao(classificacao);
				asvSupressaoService.salvarFceAsvClassiVegetacao(fceAsvClassiVegetacao);
				if(!Util.isNullOuVazio(listaClassificacaoTemp)){
					listaClassificacaoTemp.clear();
				} else {
					listaClassificacaoTemp = new ArrayList<ClassificacaoVegetacao>();
				}
				listaClassificacaoTemp.addAll(listaClassificacaoSelecionada);
			}
		}
	}

	public boolean houveAlteracaoNaLista(){
		return !Util.isNullOuVazio(listaClassificacaoTemp) && !listaClassificacaoTemp.equals(listaClassificacaoSelecionada);
	}

	public boolean isTemClassificacao(){
		return !Util.isNullOuVazio(listaClassificacaoSelecionada);
	}

	/**
	 * @param event
	 * @author eduardo.fernandes
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if("asvAbaDadosGerais".equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if("asvAbaSupressao".equals(event.getTab().getId())){
			activeTab = 1;
		}
	}

	/**
	 * @Comentários O usuário só irá conseguir avançar para a [abaSupressao] após salvar a [abaDadosGerais]
	 * @see controlarAbas(TabChangeEvent event)
	 * @author eduardo.fernandes
	 */
	@Override
	public void avancarAba(){
		if(validarAba()){
			salvarDadosGerais();
			activeTab++;
		}
	}
	
	public void salvarDadosGerais() {
		try {
			if (!super.isFceSalvo()){
				super.salvarFce();
			}
			
			salvarFceAsv();
			salvarListaClassificacaoVegetacaoSelecionada();
			
			if (activeTab == 0){
				JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg001"));
				desabilitaAba = false;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - ASV.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @Comentários Método chamado na [abaSupressao] para voltar a [abaDadosGerais]
	 * @see controlarAbas(TabChangeEvent event)
	 * @author eduardo.fernandes
	 */
	@Override
	public void voltarAba(){
		activeTab--;
	}

	// Getters and Setters
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public List<ClassificacaoVegetacao> getListaClassificacao() {
		return listaClassificacao;
	}

	public void setListaClassificacao(List<ClassificacaoVegetacao> listaClassificacao) {
		this.listaClassificacao = listaClassificacao;
	}

	public List<ClassificacaoVegetacao> getListaClassificacaoAll() {
		return listaClassificacaoAll;
	}

	public void setListaClassificacaoAll(List<ClassificacaoVegetacao> listaClassificacaoAll) {
		this.listaClassificacaoAll = listaClassificacaoAll;
	}

	public List<ClassificacaoVegetacao> getListaClassificacaoSelecionada() {
		return listaClassificacaoSelecionada;
	}

	public void setListaClassificacaoSelecionada(List<ClassificacaoVegetacao> listaClassificacaoSelecionada) {
		this.listaClassificacaoSelecionada = listaClassificacaoSelecionada;
	}

	public FceAsv getFceAsv() {
		return fceAsv;
	}

	public void setFceAsv(FceAsv fceAsv) {
		this.fceAsv = fceAsv;
	}

	public AsvSupressaoService getAsvSupressaoService() {
		return asvSupressaoService;
	}

	public void setAsvSupressaoService(AsvSupressaoService asvSupressaoService) {
		this.asvSupressaoService = asvSupressaoService;
	}

	public ClassificacaoVegetacao getTipoClassificacao() {
		return tipoClassificacao;
	}

	public void setTipoClassificacao(ClassificacaoVegetacao tipoClassificacao) {
		this.tipoClassificacao = tipoClassificacao;
	}

	public String getNomClassificacao() {
		return nomClassificacao;
	}

	public void setNomClassificacao(String nomClassificacao) {
		this.nomClassificacao = nomClassificacao;
	}

	public boolean isDesabilitaAba() {
		return desabilitaAba;
	}

	public void setDesabilitaAba(boolean desabilitaAba) {
		this.desabilitaAba = desabilitaAba;
	}

	@Override
	public void carregarAba() {
		activeTab = 0;
		desabilitaAba = true;
		tipoClassificacao = new ClassificacaoVegetacao();
		nomClassificacao = "";
		carregarClassificacao();
		verificarEdicao();
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.fce.FceInterface#limpar()
	 */
	@Override
	public void limpar() {
		asvSupressaoController.limparAbaAsvSupressao();
		super.limparFce();
	}

	public StreamedContent getImprimirRelatorio() throws Exception {
		return getImprimirRelatorio(super.fce, DOC_FCE_ASV);
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceAsv");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioASV");
		
		if(!isNotFceASV || !super.isFceTecnico()){
			
			RequestContext.getCurrentInstance().execute("fce_asv.show();");
		}
	}

	@Override
	protected void prepararDuplicacao() {
		fceAsv.setIdeFceAsv(null);
		fceAsv.setIdeFce(super.fce);
		if (!Util.isNullOuVazio(asvSupressaoController.getListProdutoSupressaoSelecionados())){
			for(ProdutoSupressao produtoSupressao : asvSupressaoController.getListProdutoSupressaoSelecionados()){
				produtoSupressao.setIdeProdutoSupressao(null);
				produtoSupressao.setFceAsv(fceAsv);
			}
		}
		
		if (!Util.isNullOuVazio(asvSupressaoController.getListaEspecieSupressaoAutorizacao())){
			for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : asvSupressaoController.getListaEspecieSupressaoAutorizacao()) {
				especieSupressaoAutorizacao.setIdeEspecieSupressaoAutorizacao(null);
				especieSupressaoAutorizacao.setFceAsv(fceAsv);
			}
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceAsv();
			salvarListaClassificacaoVegetacaoSelecionada();
			
			if(fceAsv.getIndApp() != null && fceAsv.getIndApp() 
					&& (!asvSupressaoController.isSemAreaAppNoEmpreendimento() || !asvSupressaoController.blockPorcentagemApp)){
				
				asvSupressaoController.salvarFceAsvJustificativaSupressao();
			}
			
			asvSupressaoController.salvarFceAsvObjetivoSupressao();
			asvSupressaoController.salvarFceAsvOcorrenciaArea();
			asvSupressaoController.salvarFceProdutoSupressao();
			asvSupressaoController.salvarFceEspecie();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - ASV.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void carregarFceTecnico() {
		activeTab = 0;
		desabilitaAba = true;
		tipoClassificacao = new ClassificacaoVegetacao();
		nomClassificacao ="";
		carregarClassificacao();
		super.carregarFceDoTecnico(DOC_FCE_ASV);
		desabilitaAba = false;
		prepararFceAsv();
		asvSupressaoController.init();
	}

	public boolean getIsNotFceASV() {
		return isNotFceASV;
	}

	public void setIsNotFceASV(boolean isNotFceASV) {
		this.isNotFceASV = isNotFceASV;
	}
}