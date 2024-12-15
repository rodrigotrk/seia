package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtividadeInexigivel;
import br.gov.ba.seia.entity.AtividadeInexigivelModeloCertificadoInexigibilidade;
import br.gov.ba.seia.entity.ModeloCertificadoInexigibilidade;
import br.gov.ba.seia.entity.RecomendacaoAtividadeInexigivel;
import br.gov.ba.seia.entity.RecomendacaoInexigibilidade;
import br.gov.ba.seia.entity.TipoAtividadeInexigivel;
import br.gov.ba.seia.enumerator.TipoAtividadeInexigivelEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtividadeInexigivelService;
import br.gov.ba.seia.service.ModeloCertificadoInexigibilidadeService;
import br.gov.ba.seia.service.RecomendacaoAtividadeInexigivelService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class AtividadeInexigivelController extends SeiaControllerAb {

	private String descricaoAtividade;
	private String descricaoRecomendacao;
	private AtividadeInexigivel atividade;
	private DataModel<AtividadeInexigivel> modelAtividades;
	private List<RecomendacaoInexigibilidade> modelRecomendacoes;
	private List<RecomendacaoInexigibilidade> recomendacoesSelecionadas;
	private List<RecomendacaoInexigibilidade> recomendacoesSelecionadasTemp;
	private List<RecomendacaoAtividadeInexigivel> listRecomendacoesAtividades;
	private DataModel<ModeloCertificadoInexigibilidade> modelModeloCertificado;
	private UIForm formularioASerLimpo;
	private boolean visualizar;
	private String finalidadeSelecionada;
	private boolean inexigivelOutorga;
	private boolean inexigivelLicenca;
	private TipoAtividadeInexigivel tipoAtividadeInexigivel;
	private AtividadeInexigivel atividadeTemp;
	private boolean disableEndereco;
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	private int activeIndex;
	private ModeloCertificadoInexigibilidade modeloCertificadoInexigibilidade;
	private ModeloCertificadoInexigibilidade modeloCertificadoInexigibilidadeTemp;
	private AtividadeInexigivelModeloCertificadoInexigibilidade atividadeModelo;
	private boolean desabilarAbaCertificado;
	
	@EJB
	private AtividadeInexigivelService atividadeService;
	
	@EJB
	private RecomendacaoAtividadeInexigivelService recomendacaoAtividadeInexigivelService;
	
	@EJB
	private ModeloCertificadoInexigibilidadeService modeloCertificadoInexigibilidadeService;	
	

	public AtividadeInexigivelController() {}
	
	@PostConstruct
	public void init(){
		modeloCertificadoInexigibilidade = new ModeloCertificadoInexigibilidade();
		setAtividade(new AtividadeInexigivel());
		visualizar = false;
		finalidadeSelecionada = "";
		inexigivelOutorga = false;
		inexigivelLicenca = false;
		disableEndereco = false;
		recomendacoesSelecionadas = new ArrayList<RecomendacaoInexigibilidade>();
		desabilarAbaCertificado = true;
		activeIndex = 0;
	}

	public void editar(){
		try {
			atividadeTemp = (AtividadeInexigivel) atividade.clone();
			
			try {
				listRecomendacoesAtividades = recomendacaoAtividadeInexigivelService.obterRecomendacaoPor(atividadeTemp);
				atividadeModelo = modeloCertificadoInexigibilidadeService.obterModeloCertificadoPorAtividadeInexigivel(atividadeTemp);
				modeloCertificadoInexigibilidade =  atividadeModelo.getModeloCertificadoInexigibilidade();
				modeloCertificadoInexigibilidadeTemp = (ModeloCertificadoInexigibilidade) modeloCertificadoInexigibilidade.clone();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			recomendacoesSelecionadas = new ArrayList<RecomendacaoInexigibilidade>();
			for(RecomendacaoAtividadeInexigivel item : listRecomendacoesAtividades){
				recomendacoesSelecionadas.add(item.getRecomendacaoInexigibilidade());
			}
			recomendacoesSelecionadasTemp = new ArrayList<RecomendacaoInexigibilidade>();
			recomendacoesSelecionadasTemp.addAll(recomendacoesSelecionadas);
			
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		carregarDados();
		visualizar = false;
		desabilarAbaCertificado = false;
		activeIndex = 0;
		limparTela();
	}
	
	public void novaAtividade() {
			visualizar = false;
			setAtividade(new AtividadeInexigivel());
			modeloCertificadoInexigibilidade = new ModeloCertificadoInexigibilidade();
			modeloCertificadoInexigibilidade.setCaminhoArquivoModeloCertificadoInexigibilidade("/opt/ARQUIVOS/CONTROLE_ATIVIDADES_INEXIGIVEIS/Certificado_inexigibilidade_M1.pdf");
			modeloCertificadoInexigibilidade.setCodigo("M1");
			modeloCertificadoInexigibilidade.setDescricao("certificado_inexigibilidade_modelo02");
			modeloCertificadoInexigibilidade.setIdeModeloCertificadoInexigibilidade(1);
			modeloCertificadoInexigibilidade.setIndAtivo(true);
			recomendacoesSelecionadas = new ArrayList<RecomendacaoInexigibilidade>();
			inexigivelOutorga = false;
			inexigivelLicenca = false;
			finalidadeSelecionada = "";
			disableEndereco = false;
			activeIndex = 0;
			limparTela();

	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}

	public void salvarAtualizarAtividade() {

		try {
			
			if(validarCampos()){
				AtividadeInexigivel atividadeTempNome = atividadeService.buscarAtividadeInexigivelPorNome(getAtividade().getNomAtividadeInexigivel());
				
				getTipoFinalidade();
	
				if(inexigivelLicenca) {
					tipoAtividadeInexigivel = new TipoAtividadeInexigivel(TipoAtividadeInexigivelEnum.LICENCA.getIdeTipoAtividadeInexigivel());
				}else if (inexigivelOutorga){
					tipoAtividadeInexigivel = new TipoAtividadeInexigivel(TipoAtividadeInexigivelEnum.OUTORGA.getIdeTipoAtividadeInexigivel());
				}
				
				if (Util.isNull(getAtividade().getIdeAtividadeInexigivel())) {
					
					if(Util.isNull(atividadeTempNome)){
						
						atividade = getAtividade();
						atividade.setIndAtivo(true);
						atividade.setPermiteQtdBueiros(false);
						atividade.setTipoAtividadeInexigivel(tipoAtividadeInexigivel);
						if(atividade.getPermiteLocalRealizacao()){
							atividade.setPermiteEndereco(false);
						}
						atividadeService.salvar(atividade);
						
						salvarAbaRecomendacaoCertificado();
						
						
						if(inexigivelLicenca && inexigivelOutorga){
							atividade.setIdeAtividadeInexigivel(null);
							atividade.setTipoAtividadeInexigivel(new TipoAtividadeInexigivel(TipoAtividadeInexigivelEnum.OUTORGA.getIdeTipoAtividadeInexigivel()));
							if(atividade.getPermiteLocalRealizacao()){
								atividade.setPermiteEndereco(false);
							}
							atividadeService.salvar(atividade);
							salvarAbaRecomendacaoCertificado();
						}
						
						JsfUtil.addSuccessMessage("Salvo com sucesso!");
						Html.esconder("dlgAtividade");
					}else{
						JsfUtil.addErrorMessage("O nome informado já está cadastrado");
					}
				}
				else {
					
					if(!atividadeTemp.equals(atividade) || !recomendacoesSelecionadasTemp.equals(recomendacoesSelecionadas) || !modeloCertificadoInexigibilidadeTemp.equals(modelModeloCertificado)){
						
						atividadeTemp.setIndAtivo(false);
						atividadeTemp.setDtcExclusao(new Date());
						atividadeService.excluir(atividadeTemp);
					
						if(atividade.getPermiteLocalRealizacao()){
							atividade.setPermiteEndereco(false);
						}
						atividade.setIdeAtividadeInexigivel(null);
						atividade.setDtcExclusao(null);
						atividade.setTipoAtividadeInexigivel(tipoAtividadeInexigivel);
						atividade.setIndAtivo(true);
						atividadeService.salvar(atividade);
						
						salvarAbaRecomendacaoCertificado();
					}
									
					JsfUtil.addSuccessMessage("Atividade atualizada com sucesso!");
					Html.esconder("dlgAtividade");
				}
	
				this.setModelAtividades(null);
			}
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void salvarAbaRecomendacaoCertificado() {
		//Persistir Recomendação Atividade Inexigivel
		for(RecomendacaoInexigibilidade recomendacao : getRecomendacoesSelecionadas()){
			RecomendacaoAtividadeInexigivel rec = new RecomendacaoAtividadeInexigivel();
			rec.setRecomendacaoInexigibilidade(recomendacao);
			rec.setAtividadeInexigivel(atividade);
			rec.setIndAtivo(true);
			
			recomendacaoAtividadeInexigivelService.salvar(rec);
		}
		
		//persistir Modelo Certificado Exigivel
		AtividadeInexigivelModeloCertificadoInexigibilidade modelo = new AtividadeInexigivelModeloCertificadoInexigibilidade();
		modelo.setAtividadeInexigivel(atividade);
		modelo.setModeloCertificadoInexigibilidade(getModeloCertificadoInexigibilidade());
		modeloCertificadoInexigibilidadeService.salvar(modelo);
	}
	
	private boolean validarCampos() {
		
		if(Util.isNullOuVazio(atividade.getNomAtividadeInexigivel())){
			JsfUtil.addErrorMessage("O campo Nome da atividade é de preenchimento obrigatório");
			return false;
		}else if(!inexigivelLicenca && !inexigivelOutorga){
			JsfUtil.addErrorMessage("O campo Tipo de atividade é de preenchimento obrigatório");
			return false;
		}else if(finalidadeSelecionada == ""){
			JsfUtil.addErrorMessage("O campo Caracterização da Atividade é de preenchimento obrigatório");
			return false;
		}else if(Util.isNullOuVazio(atividade.getPermiteLuzParaTodos())){
			JsfUtil.addErrorMessage("O campo atividade faz parte do programa luz para todos é de preenchimento obrigatório");
			return false;
		}else if(Util.isNullOuVazio(atividade.getPermiteMunicipioEmergencial())){
			JsfUtil.addErrorMessage("O campo atividade poderá ser realizada em municípios em estado de emergência é de preenchimento obrigatório");
			return false;
		}else if(Util.isNullOuVazio(atividade.getPermiteLocalRealizacao())){
			JsfUtil.addErrorMessage("O campo Perguntar se a atividade é realizada em um local especifico ou em vários locais do estado da Bahia é de preenchimento obrigatório");
			return false;
		}else if(Util.isNullOuVazio(atividade.getPermiteEndereco())){
			JsfUtil.addErrorMessage("O campo É necessário informar endereço de realização da atividade é de preenchimento obrigatório");
			return false;
		}else if(Util.isNullOuVazio(recomendacoesSelecionadas)){
			JsfUtil.addErrorMessage("É necessário informar ao menos uma recomendação, é de preenchimento obrigatório");
			return false;
		}else if(Util.isNullOuVazio(modeloCertificadoInexigibilidade)){
			JsfUtil.addErrorMessage("É necessário informar qual modelo do certificado , é de preenchimento obrigatório");
			return false;
		}
		
		
		return true;
	}

	public void changeFinalidade(){
		
		if(finalidadeSelecionada.equals("sistema")){
			atividade.setPermiteEndereco(true);
			disableEndereco = true;
		}else if(finalidadeSelecionada.equals("projeto")){
			atividade.setPermiteEndereco(true);
			disableEndereco = true;
		}else if(finalidadeSelecionada.equals("unidade")){
			atividade.setPermiteEndereco(true);
			disableEndereco = true;
		}else if(finalidadeSelecionada.equals("ponte")){
			atividade.setPermiteEndereco(null);
			disableEndereco = false;
		}else if(finalidadeSelecionada.equals("nenhuma")){
			atividade.setPermiteEndereco(null);
			disableEndereco = false;
		}
		
		if(!Util.isNullOuVazio(atividade.getPermiteLocalRealizacao())){
			changeAtividadeLocaisEspecificos();
		}
		
	}
	
	public void changeAtividadeLocaisEspecificos(){
		if(atividade.getPermiteLocalRealizacao()){
			atividade.setPermiteEndereco(true);
			disableEndereco = true;
		}else{
			atividade.setPermiteEndereco(null);
			disableEndereco = false;
		}
		
		if(finalidadeSelecionada.equals("sistema")){
			atividade.setPermiteEndereco(true);
			disableEndereco = true;
		}else if(finalidadeSelecionada.equals("projeto")){
			atividade.setPermiteEndereco(true);
			disableEndereco = true;
		}else if(finalidadeSelecionada.equals("unidade")){
			atividade.setPermiteEndereco(true);
			disableEndereco = true;
		}
		
	}

	private void getTipoFinalidade() {
		if(finalidadeSelecionada.equals("ponte")){
			atividade.setPermitePonte(true);
			atividade.setPermiteAbastecimento(false);
			atividade.setPermiteProjeto(false);
			atividade.setPermiteUnidade(false);
		}else if(finalidadeSelecionada.equals("sistema")){
			atividade.setPermiteAbastecimento(true);
			atividade.setPermitePonte(false);
			atividade.setPermiteProjeto(false);
			atividade.setPermiteUnidade(false);
		}else if(finalidadeSelecionada.equals("projeto")){
			atividade.setPermiteProjeto(true);
			atividade.setPermiteAbastecimento(false);
			atividade.setPermitePonte(false);
			atividade.setPermiteUnidade(false);
		}else if(finalidadeSelecionada.equals("unidade")){
			atividade.setPermiteUnidade(true);
			atividade.setPermiteProjeto(false);
			atividade.setPermiteAbastecimento(false);
			atividade.setPermitePonte(false);
		}else if(finalidadeSelecionada.equals("nenhuma")){
			atividade.setPermiteUnidade(false);
			atividade.setPermiteProjeto(false);
			atividade.setPermiteAbastecimento(false);
			atividade.setPermitePonte(false);
		}
	}
	
	public void selecionarRecomendacao(RecomendacaoInexigibilidade recomendacao){
		modelRecomendacoes.remove(recomendacao);
		recomendacoesSelecionadas.add(recomendacao);
		Html.atualizar("tabViewDI:formLista");
	}
	
	public void removerRecomendacao(RecomendacaoInexigibilidade recomendacao){
		recomendacoesSelecionadas.remove(recomendacao);
		modelRecomendacoes.add(recomendacao);
		
		ordernarRecomendacoes();
		
		Html.atualizar("tabViewDI:formLista");
	}
	
	private void ordernarRecomendacoes() {
		Collections.sort(modelRecomendacoes, new Comparator<RecomendacaoInexigibilidade>(){
			@Override
			public int compare(RecomendacaoInexigibilidade o1, RecomendacaoInexigibilidade o2) {
				return o1.getDesRecomendacaoInexigibilidade().compareTo(o2.getDesRecomendacaoInexigibilidade());
			}
		});
	}
	
	public void excluirAtividade() {

		try {
	
			atividade.setIndAtivo(false);
			atividade.setDtcExclusao(new Date());
			atividadeService.excluir(atividade);
			
			this.setModelAtividades(null);
			JsfUtil.addSuccessMessage("Exclusão realizada com sucesso!");
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}	
	
	
	public void filtrarDados() {
		try {

			List<AtividadeInexigivel> lista = null;
			
			if(descricaoAtividade.length() >= 3){
				lista = atividadeService.filtrarAtividade(descricaoAtividade);
				if(!Util.isNullOuVazio(lista)){
					modelAtividades = Util.castListToDataModel(lista);
					lista = null;
				}
			}else{
				modelAtividades = Util.castListToDataModel(atividadeService.filtrarAtividade(descricaoAtividade));
			}
			
			Html.atualizar("formListaAtividades:dataTableAtividades");
			
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void filtrarRecomendacoes() {
		try {

			List<RecomendacaoInexigibilidade> lista = null;
			
			if(descricaoRecomendacao.length() >= 3){
				lista = recomendacaoAtividadeInexigivelService.filtrarRecomendacao(descricaoRecomendacao);
				if(!Util.isNullOuVazio(lista)){
					modelRecomendacoes = lista;
					lista = null;
				}
			}else{
				modelRecomendacoes = recomendacaoAtividadeInexigivelService.filtrarRecomendacao(descricaoRecomendacao);
			}
			
			Html.atualizar("tabViewDI:formLista:dataTableRecomendacoes");
			
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}	
		
	public void visualizarDados(){
		carregarDados();
		visualizar = true;
		desabilarAbaCertificado = false;
		
	}

	private void carregarDados() {
		try {
			if(atividade.getPermiteAbastecimento()){
				finalidadeSelecionada = "sistema";
			}else if(atividade.getPermitePonte()){
				finalidadeSelecionada = "ponte";
			}else if(atividade.getPermiteProjeto()){
				finalidadeSelecionada = "projeto";
			}else if(atividade.getPermiteUnidade()){
				finalidadeSelecionada = "unidade";
			}else{
				finalidadeSelecionada = "nenhuma";
			}
			
			if(atividade.getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel().equals(TipoAtividadeInexigivelEnum.LICENCA.getIdeTipoAtividadeInexigivel())){
				inexigivelLicenca = true;
				inexigivelOutorga = false;
			}else if(atividade.getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel().equals(TipoAtividadeInexigivelEnum.OUTORGA.getIdeTipoAtividadeInexigivel())){
				inexigivelOutorga = true;
				inexigivelLicenca = false;
			}
			
			if(atividade.getPermiteLocalRealizacao()){
				atividade.setPermiteEndereco(true);
				disableEndereco = true;
			}
			AtividadeInexigivelModeloCertificadoInexigibilidade atividadeInex = modeloCertificadoInexigibilidadeService.obterModeloCertificadoPorAtividadeInexigivel(atividade);
			modeloCertificadoInexigibilidade = atividadeInex.getModeloCertificadoInexigibilidade();
			
			recomendacoesSelecionadas = new ArrayList<RecomendacaoInexigibilidade>();
			List<RecomendacaoAtividadeInexigivel> listRecoAtivInex = recomendacaoAtividadeInexigivelService.obterRecomendacaoPor(atividade);
			for(RecomendacaoAtividadeInexigivel item : listRecoAtivInex){
				recomendacoesSelecionadas.add(item.getRecomendacaoInexigibilidade());
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		
	}

	public DataModel<AtividadeInexigivel> getModelAtividades() {

		if (Util.isNull(modelAtividades)) {

			try {

				modelAtividades = Util.castListToDataModel(atividadeService.buscarPorDescricaoTipoAtividade(!Util.isNullOuVazio(getDescricaoAtividade()) ? new AtividadeInexigivel(getDescricaoAtividade()) : new AtividadeInexigivel()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelAtividades;
	}
	
	public List<RecomendacaoInexigibilidade> getModelRecomendacoes() {

		if (Util.isNull(modelRecomendacoes)) {

			try {

				modelRecomendacoes = recomendacaoAtividadeInexigivelService.listarRecomendacoes();
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelRecomendacoes;
	}
	
	public DataModel<ModeloCertificadoInexigibilidade> getModelModeloCertificado() {

		if (Util.isNull(modelModeloCertificado)) {

			try {

				modelModeloCertificado = Util.castListToDataModel(modeloCertificadoInexigibilidadeService.listarModeloCertificado());
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelModeloCertificado;
	}	
	
	public StreamedContent getArquivoBaixar(ModeloCertificadoInexigibilidade modelo) {
		return modeloCertificadoInexigibilidadeService.baixarArquivo(modelo.getCaminhoArquivoModeloCertificadoInexigibilidade()); //tem que ser caminho do arquivo
	}
	
	public void avancar(){
		desabilarAbaCertificado = false;
		activeIndex =0;
		activeIndex++;
	}
	
	public void voltar(){
		if(activeIndex == 0) {
			Html.esconder("tabViewDI");
		}else {
			activeIndex--;
		}
	}
	
	public String getHintPonte() {
		return BUNDLE.getString("hint_finalidade_ponte");
	}
	
	public String getHintSistema() {
		return BUNDLE.getString("hint_finalidade_sistema");
	}
	
	public String getHintProjeto() {
		return BUNDLE.getString("hint_finalidade_projeto");
	}
	
	public String getHintUnidade() {
		return BUNDLE.getString("hint_finalidade_unidade");
	}
	
	public String getHintNenhuma() {
		return BUNDLE.getString("hint_finalidade_nenhuma");
	}	
	
	public void setModelAtividades(DataModel<AtividadeInexigivel> modelAtividades) {
		this.modelAtividades = modelAtividades;
	}

	public AtividadeInexigivel getAtividade() {

		if (Util.isNull(atividade)) atividade = new AtividadeInexigivel();

		return atividade;
	}
	public void setAtividade(AtividadeInexigivel atividade) {
		this.atividade = atividade;
	}

	public String getDescricaoAtividade() {
		return descricaoAtividade;
	}

	public void setDescricaoAtividade(String descricaoAtividade) {
		this.descricaoAtividade = descricaoAtividade;
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

	public String getFinalidadeSelecionada() {
		return finalidadeSelecionada;
	}

	public void setFinalidadeSelecionada(String finalidadeSelecionada) {
		this.finalidadeSelecionada = finalidadeSelecionada;
	}

	public boolean isInexigivelOutorga() {
		return inexigivelOutorga;
	}

	public void setInexigivelOutorga(boolean inexigivelOutorga) {
		this.inexigivelOutorga = inexigivelOutorga;
	}

	public boolean isInexigivelLicenca() {
		return inexigivelLicenca;
	}

	public void setInexigivelLicenca(boolean inexigivelLicenca) {
		this.inexigivelLicenca = inexigivelLicenca;
	}

	public boolean isDisableEndereco() {
		return disableEndereco;
	}

	public void setDisableEndereco(boolean disableEndereco) {
		this.disableEndereco = disableEndereco;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public String getDescricaoRecomendacao() {
		return descricaoRecomendacao;
	}

	public void setDescricaoRecomendacao(String descricaoRecomendacao) {
		this.descricaoRecomendacao = descricaoRecomendacao;
	}

	public ModeloCertificadoInexigibilidade getModeloCertificadoInexigibilidade() {
		return modeloCertificadoInexigibilidade;
	}

	public void setModeloCertificadoInexigibilidade(
			ModeloCertificadoInexigibilidade modeloCertificadoInexigibilidade) {
		this.modeloCertificadoInexigibilidade = modeloCertificadoInexigibilidade;
	}

	public List<RecomendacaoInexigibilidade> getRecomendacoesSelecionadas() {
		return recomendacoesSelecionadas;
	}

	public void setRecomendacoesSelecionadas(
			List<RecomendacaoInexigibilidade> recomendacoesSelecionadas) {
		this.recomendacoesSelecionadas = recomendacoesSelecionadas;
	}

	public boolean isDesabilarAbaCertificado() {
		return desabilarAbaCertificado;
	}

	public void setDesabilarAbaCertificado(boolean desabilarAbaCertificado) {
		this.desabilarAbaCertificado = desabilarAbaCertificado;
	}
	
}