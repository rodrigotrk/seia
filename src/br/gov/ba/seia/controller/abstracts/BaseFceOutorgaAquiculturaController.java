package br.gov.ba.seia.controller.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.controller.FceOutorgaAquiculturaNavegacaoController;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.EspecieAquiculturaTipoAtividade;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceAquiculturaEspecie;
import br.gov.ba.seia.entity.FceAquiculturaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoAquicultura;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceOutorgaAquiculturaServiceFacade;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * Abstração da Base do FCE - Outorga Aquicultura
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 15/04/2015
 */
public abstract class BaseFceOutorgaAquiculturaController extends BaseFceOutorgaComDocumentoAdicionalController {

	@Inject
	protected FceOutorgaAquiculturaNavegacaoController navegacaoController;

	@EJB
	protected  FceOutorgaAquiculturaServiceFacade aquiculturaServiceFacade;

	protected final static DocumentoObrigatorio DOCUMENTO_OBRIGATORIO_ADICIONAL = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA_ADICIONAIS.getId());

	protected FceAquicultura fceAquicultura;

	private List<String> arquivosAdicionaisToDelete;

	protected List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividadeSelecionado;

	protected AquiculturaTipoAtividade tipoAtividadeSelecionado;

	private List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividade;

	protected List<AquiculturaTipoAtividadeEnum> listaAtividadeEnum;

	protected List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeografica;

	protected FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionado;

	protected List<FceOutorgaLocalizacaoAquicultura> listaFceOutorgaLocalizacaoAquicultura;

	protected List<FceAquiculturaEspecie> listaFceAquiculturaEspecie;

	protected boolean outrasEspecies;

	public abstract void avancarAba();

	public abstract void voltarAba();

	protected abstract void carregarDadosRequerimento();

	protected abstract void iniciarFceAquicultura();

	public abstract void prepararAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica);
	
	/**
	 * Método chamado para adicionar a {@link AquiculturaTipoAtividade} selecionada pelo usuário na lista de Tipo de Atividade Selecionadas de suas respectivas abas.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2014
	 * @see Melhoria #6590 <br> Força-se a adição das {@link AquiculturaTipoAtividade} de acordo com as abas existentes no <i>FCE - Outorga para Aquicultura</i> nas suas respectivas listas
	 */
	protected void adicionarTipoAtividade(AquiculturaTipoAtividade aquiculturaTipoAtividadeParaAdicionar){
		if(Util.isNull(listaAquiculturaTipoAtividadeSelecionado)){
			listaAquiculturaTipoAtividadeSelecionado = new ArrayList<AquiculturaTipoAtividade>();
		}
		aquiculturaTipoAtividadeParaAdicionar.setListaEspecieAquiculturaTipoAtividades(listarEspecieAquiculturaTivoAtividadeByTipoAtividade(aquiculturaTipoAtividadeParaAdicionar));
		if(!listaAquiculturaTipoAtividadeSelecionado.contains(aquiculturaTipoAtividadeParaAdicionar)){
			listaAquiculturaTipoAtividade.remove(aquiculturaTipoAtividadeParaAdicionar);
			listaAquiculturaTipoAtividadeSelecionado.add(aquiculturaTipoAtividadeParaAdicionar);
		}
	}

	private void adicionarTipoAtividadesFromRequerimento(){
		List<AquiculturaTipoAtividade> listaTemp = new ArrayList<AquiculturaTipoAtividade>();
		listaTemp.addAll(listaAquiculturaTipoAtividade);
		for(AquiculturaTipoAtividade aquiculturaTipoAtividade : listaTemp){
			adicionarTipoAtividade(aquiculturaTipoAtividade);
		}
	}

	protected void carregarFce(){
		if(navegacaoController.isAnaliseTecnica()){
			if(!Util.isNullOuVazio(navegacaoController.getFce()) && navegacaoController.getFce().isFceTecnico() == false){
				super.carregarFceDoRequerente(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
			}else{
				
				super.carregarFceDoTecnico(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
			}
		} else {
			super.carregarFceDoRequerente(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
		}
	}

	protected void excluirTipoAtividade(AquiculturaTipoAtividade aquiculturaTipoAtividade){
		listaAquiculturaTipoAtividade.add(aquiculturaTipoAtividade);
		Collections.sort(listaAquiculturaTipoAtividade);
		listaAquiculturaTipoAtividadeSelecionado.remove(aquiculturaTipoAtividade);
	}

	/*
	 * changes
	 */
	/**
	 * Método que verifica se a {@link AquiculturaTipoAtividade} selecionada é <i>Outros</i> e envia a mensagem de alerta para tela.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2014
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void changeEspecies(ValueChangeEvent event){
		List<EspecieAquiculturaTipoAtividade> antigasEspeciesSelecionadas = (List<EspecieAquiculturaTipoAtividade>) event.getOldValue();
		List<EspecieAquiculturaTipoAtividade> novasEspeciesSelecionadas = (List<EspecieAquiculturaTipoAtividade>) event.getNewValue();
		if(!Util.isNullOuVazio(antigasEspeciesSelecionadas)) {
			for(EspecieAquiculturaTipoAtividade especie : novasEspeciesSelecionadas) {
				if(especie.getIdeEspecie().isOutros() && !antigasEspeciesSelecionadas.contains(especie)){
					super.exibirInformacao001();
					outrasEspecies = true;
					break;
				}
			}
		}
		else if(!Util.isNullOuVazio(novasEspeciesSelecionadas)){
			int count = 0;
			for(EspecieAquiculturaTipoAtividade aquiculturaTipoAtividade : novasEspeciesSelecionadas){
				if(!aquiculturaTipoAtividade.getIdeEspecie().isOutros()){
					count++;
				}
				else {
					super.exibirInformacao001();
					outrasEspecies = true;
					break;
				}
			}
			if(novasEspeciesSelecionadas.size() == count){
				outrasEspecies = false;
			}
		}
	}

	public void confirmarCoordenada(){
		if(validarCoordenada(fceOutorgaLocalizacaoGeograficaSelecionado)){
			fceOutorgaLocalizacaoGeograficaSelecionado.setConfirmado(true);
			if(fceOutorgaLocalizacaoGeograficaSelecionado.isEdicao()){
				super.exibirMensagem002();
			}
			else {
				super.exibirMensagem001();
			}
			fceOutorgaLocalizacaoGeograficaSelecionado.setEdicao(true);
		}
	}

	protected void criarListaFceOutorgaLocalizacaoGeograficaBy(List<OutorgaLocalizacaoGeografica> listaOutorgaLocGeo, FceAquicultura fceAquicultura){
		listaFceOutorgaLocalizacaoGeografica = new ArrayList<FceOutorgaLocalizacaoGeografica>();
		if(!super.isFceSalvo()){
			for(OutorgaLocalizacaoGeografica olg : listaOutorgaLocGeo){
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = new FceOutorgaLocalizacaoGeografica(fceAquicultura.getIdeFce(), olg);
				listaFceOutorgaLocalizacaoGeografica.add(fceOutorgaLocalizacaoGeografica);
				criarListaFceOutorgaLocalizacaoGeograficaAquiculturaBy(fceOutorgaLocalizacaoGeografica, fceAquicultura);
			}
		} else {
			montarListaFceOutorgaLocalizacaoGeografica(listaOutorgaLocGeo);
		}
	}

	protected void criarListaFceOutorgaLocalizacaoGeograficaAquiculturaBy(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, FceAquicultura fceAquicultura){
		if(Util.isNullOuVazio(listaFceOutorgaLocalizacaoAquicultura)){
			listaFceOutorgaLocalizacaoAquicultura = new ArrayList<FceOutorgaLocalizacaoAquicultura>();
		}
		listaFceOutorgaLocalizacaoAquicultura.add(new FceOutorgaLocalizacaoAquicultura(fceOutorgaLocalizacaoGeografica, fceAquicultura));
	}

	public void editarCoordenada() {
		fceOutorgaLocalizacaoGeograficaSelecionado.setConfirmado(false);
		fceOutorgaLocalizacaoGeograficaSelecionado.setEdicao(true);
	}

	protected void finalizar(FceAquicultura fceAquicultura, List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocalizacaoGeografica) throws Exception {
		if(isFceSalvo()){
			excluirListFceAquiculturaEspecie(fceAquicultura);
		}
		montarListaFceAquiculturaEspecie(fceAquicultura);
		finalizarFceOutorgaAquicultura(fceAquicultura, listaFceAquiculturaLocalizacaoGeografica, listaAquiculturaTipoAtividadeSelecionado);
		salvarDocumentoAdicional(fceAquicultura);
		if(!Util.isNull(fceAquicultura.getIdeFce())){
			super.fce = fceAquicultura.getIdeFce();
		} 
		else {
			super.fce = navegacaoController.getFce();
		}
		super.concluirFce();
		
		if(!Util.isNullOuVazio(super.fce)){
			if(!Util.isNullOuVazio(super.fce.getIdeAnaliseTecnica())){
				
				Html.executarJS("atualizarDadoConcedido();");
			}
			
		}

	}
	
	protected void duplicar(FceAquicultura fceAquicultura, List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocalizacaoGeografica) throws Exception {
		if(isFceSalvo() && !Util.isNullOuVazio(fceAquicultura) && !Util.isNullOuVazio(fceAquicultura.getIdeFceAquicultura())){
			excluirListFceAquiculturaEspecie(fceAquicultura);
		}
		montarListaFceAquiculturaEspecie(fceAquicultura);
		finalizarFceOutorgaAquicultura(fceAquicultura, listaFceAquiculturaLocalizacaoGeografica, listaAquiculturaTipoAtividadeSelecionado);
		super.fce = fceAquicultura.getIdeFce();
		super.salvarFce();
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @param listaFceAquiculturaLocalizacaoGeografica
	 * @throws Exception
	 * @since 06/07/2015
	 */
	private void finalizarFceOutorgaAquicultura(FceAquicultura fceAquicultura, List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocalizacaoGeografica, List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividade) throws Exception {
		aquiculturaServiceFacade.finalizarFceAquicultura(montarMapComParametros(fceAquicultura, listaFceAquiculturaLocalizacaoGeografica));
		aquiculturaServiceFacade.verificarAndRemoverEspeciesDeFceLicencaAquicultura(fceAquicultura.getIdeTipoAquicultura(), listaFceAquiculturaEspecie, super.requerimento, listaAquiculturaTipoAtividadeSelecionado);
	}

	/**
	 * Método para tratar a lista de {@link AquiculturaTipoAtividadeEnum} de acordo com as {@link Tipologia} do {@link RequerimentoTipologia}, cadastradas na <i>Etapa 7</i>
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 09/02/2015
	 * @return listaAtividadeEnum
	 * @see Melhoria #6590
	 */
	private AquiculturaTipoAtividadeEnum getAtividadeEnum(Tipologia tipologia){
		if(tipologia.getCodTipologia().compareTo("A2.3.1") == 0 || tipologia.getCodTipologia().compareTo("A2.3.2") == 0 || tipologia.getCodTipologia().compareTo("A2.3.3") == 0){
			return AquiculturaTipoAtividadeEnum.PSICULTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.4.1") == 0 || tipologia.getCodTipologia().compareTo("A2.4.2") == 0){
			return AquiculturaTipoAtividadeEnum.CARCINICULTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.5") == 0){
			return AquiculturaTipoAtividadeEnum.RANICULTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.6") == 0){
			return AquiculturaTipoAtividadeEnum.ALGICUTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.7") == 0){
			return AquiculturaTipoAtividadeEnum.MALOCOCULTURA;
		}
		else {
			return null;
		}
	}

	/**
	 * Método que verifica se a lista de {@link Tipologia} está carregada.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 10/02/2015
	 * @return true se <i>listaTipologia</i> estiver carregada
	 * @see Melhoria #6590
	 */
	private boolean isEmpreendimentoSemTipologia(){
		return Util.isNullOuVazio(getListaTipologia());
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 24/04/2015
	 */
	public boolean isExisteTipologiaFromEtapa7() {
		return !Util.isNullOuVazio(listaAtividadeEnum);
	}


	protected boolean isListaOutorgaLocalizacaoGeograficaNotNull(){
		return !Util.isNullOuVazio(super.listaOutorgaLocalizacaoGeografica);
	}

	public boolean isPontoCaptacaoSuperficialPreenchido(){
		return !Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeografica);
	}

	/**
	 * Método que verifica se as {@link Tipologia} do {@link Requerimento} são relacionadas à <b>Aquicultura</b>, <b>Criação de Animais</b> e/ou <b>Carcinicultura</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 10/02/2015
	 * @return true or false
	 * @see Melhoria #6590 <br> Mesmo sem o preenchimento da <i>Etapa 7</i> o sistema está salvando {@link RequerimentoTipologia}, portanto faz-se necessário checar o preenchimento de <i>tipologia.getValAtividade()</i>
	 */
	private boolean isRequerimentoComAtividadesOutorga(){
		if(!isEmpreendimentoSemTipologia()){
			listaAtividadeEnum = new ArrayList<AquiculturaTipoAtividadeEnum>();
			for(Tipologia tipologia : getListaTipologia()){
				if(!Util.isNullOuVazio(tipologia.getValAtividade()) && isTipologiaPaiEqualsToAquiculturaOrCriacaoAnimaisOrCarcinicultura(tipologia)){
					AquiculturaTipoAtividadeEnum enumTemp = getAtividadeEnum(tipologia);
					if(!Util.isNull(enumTemp)){
						listaAtividadeEnum.add(enumTemp);
					}
				}
			}
		}
		return isExisteTipologiaFromEtapa7();
	}

	/**
	 * Método que verifica se as {@link Tipologia}Pai da {@link Tipologia} é <b>Aquicultura</b> ou <b>Criação de Animais</b> ou <b>Carcinicultura</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 10/02/2015
	 * @param tipologia
	 * @return <i>true</i> se a {@link Tipologia}Pai for <b>Aquicultura</b> ou <b>Criação de Animais</b>
	 */
	private boolean isTipologiaPaiEqualsToAquiculturaOrCriacaoAnimaisOrCarcinicultura(Tipologia tipologia) {
		return tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.AQUICULTURA.getId())
				|| tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.CRIACAO_DE_ANIMAIS.getId())
				|| tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.CARCINICULTURA.getId())
				&& !tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.CRIACAO_CONFINADA.getId());
	}

	protected boolean isOutrosSelecionado(){
		return outrasEspecies;
	}

	/*
	 * flags
	 */
	public boolean isTodasTipoAtividadesAdiciondas(){
		return Util.isNullOuVazio(getListaAquiculturaTipoAtividade());
	}

	public boolean isTipoAtividadeAdicionado(){
		return !Util.isNullOuVazio(listaAquiculturaTipoAtividadeSelecionado);
	}

	public boolean isExisteRequerimento() {
		super.requerimento = navegacaoController.getRequerimento();
		return !Util.isNull(super.requerimento);
	}
	
	@Override
	public void limpar(){
		super.limparDadosOutorga();
		navegacaoController.limparNavegacao();
		listaAquiculturaTipoAtividadeSelecionado = null;
		listaAquiculturaTipoAtividade = null;
		listaAtividadeEnum = null;
		listaFceAquiculturaEspecie = null;
		listaFceOutorgaLocalizacaoAquicultura = null;
		listaFceOutorgaLocalizacaoGeografica = null;
	}

	/**
	 * Método para carregar a lista de {@link AquiculturaTipoAtividade} presente nas Abas: <pre>Viveiro Escavado - Captação / Tanque Rede - Rio / Tanque Rede - Barragem</pre>
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2014
	 * @see Melhoria #6590 <br> Caso o usuário tenha solicitado uma <b>Licença</b>, na Etapa 3, e preenchido a Etapa 7 do Requerimento, o <i>FCE - Outorga para Aquicultura</i> deve listar apenas as {@link AquiculturaTipoAtividade} vinculadas as {@link Tipologia} cadastradas.
	 */
	protected void listarAquiculturaTipoAtividade(){
		try {
			if(!isRequerimentoComAtividadesOutorga()){
				listaAquiculturaTipoAtividade = aquiculturaServiceFacade.listarAquiculturaTipoAtividade();
				atualizarListaAquiculturaTipoAtividade();
			}
			else {
				listaAquiculturaTipoAtividade = aquiculturaServiceFacade.listarAquiculturatipoAtividadeBy(listaAtividadeEnum);
				adicionarTipoAtividadesFromRequerimento();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista do período de Tipo Atividade.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/04/2015
	 */
	private void atualizarListaAquiculturaTipoAtividade() {
		if(isFceSalvo() && !Util.isNull(listaAquiculturaTipoAtividadeSelecionado)){
			for(AquiculturaTipoAtividade aquiculturaTipoAtividade : listaAquiculturaTipoAtividadeSelecionado){
				listaAquiculturaTipoAtividade.remove(aquiculturaTipoAtividade);
			}
		}
	}

	protected List<EspecieAquiculturaTipoAtividade> listarEspecieAquiculturaTivoAtividadeByTipoAtividade(AquiculturaTipoAtividade tipoAtividade) {
		try {
			return aquiculturaServiceFacade.listarEspecieAquiculturaTivoAtividadeBy(tipoAtividade);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações das espécies de acordo com o tipo de atividade escolhido.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void preparEdicao(FceAquicultura fceAquicultura){
		try {
			listarFceOutorgalocalizcaoAquiculturaBy(fceAquicultura);
			listarFceAquiculturaEspecieBy(fceAquicultura);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @throws Exception
	 * @since 28/04/2015
	 */
	protected void listarFceAquiculturaEspecieBy(FceAquicultura fceAquicultura) throws Exception {
		listaFceAquiculturaEspecie = aquiculturaServiceFacade.listarFceAquiculturaEspecieBy(fceAquicultura);
		prepararEdicaoFceAquiculturaEspecie(listaFceAquiculturaEspecie);
	}


	protected void prepararEdicaoFceAquiculturaEspecie(List<FceAquiculturaEspecie> list) {
		listaAquiculturaTipoAtividadeSelecionado = new ArrayList<AquiculturaTipoAtividade>();
		for(FceAquiculturaEspecie aquiculturaEspecie : list) {
			AquiculturaTipoAtividade tipoAtividade = aquiculturaEspecie.getIdeEspecieAquiculturaTipoAtividade().getIdeAquiculturaTipoAtividade();
			if(!listaAquiculturaTipoAtividadeSelecionado.contains(tipoAtividade)) {
				tipoAtividade.setSelecionado(true);
				listaAquiculturaTipoAtividadeSelecionado.add(tipoAtividade);
				tipoAtividade.setListaEspecieAquiculturaTipoAtividades(listarEspecieAquiculturaTivoAtividadeByTipoAtividade(tipoAtividade));
				tipoAtividade.setListaEspecieAquiculturaTipoAtividadesSelected(new ArrayList<EspecieAquiculturaTipoAtividade>());
			}
			tipoAtividade.getListaEspecieAquiculturaTipoAtividadesSelected().add(aquiculturaEspecie.getIdeEspecieAquiculturaTipoAtividade());
		}
	}


	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/04/2015
	 */
	protected void montarListaFceOutorgaLocalizacaoGeografica(List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica) {
		if(!Util.isNullOuVazio(listaFceOutorgaLocalizacaoAquicultura)){
			for(FceOutorgaLocalizacaoAquicultura fceOutorgaLocalizacaoAquicultura : listaFceOutorgaLocalizacaoAquicultura){
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoAquicultura.getIdeFceOutorgaLocalizacaoGeografica();
				fceOutorgaLocalizacaoGeografica.setIdeFceLancamentoEfluente(null);
				listaFceOutorgaLocalizacaoGeografica.add(fceOutorgaLocalizacaoGeografica);
				for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgaLocalizacaoGeografica){
					if(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
						fceOutorgaLocalizacaoGeografica.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
						fceOutorgaLocalizacaoGeografica.setConfirmado(true);
						fceOutorgaLocalizacaoGeografica.setEdicao(true);
					}
				}
			}
		} 
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @throws Exception
	 * @since 28/04/2015
	 */
	private void listarFceOutorgalocalizcaoAquiculturaBy(FceAquicultura fceAquicultura) throws Exception {
		listaFceOutorgaLocalizacaoAquicultura = aquiculturaServiceFacade.listarFceOutorgaLocalizacaoAquiculturaBy(fceAquicultura);
	}

	/**
	 * Método que carrega a lista de {@link Tipologia} do {@link RequerimentoTipologia}, preenchidas na <b>Etapa 7</b> do requerimento.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 09/02/2015
	 * @param requerimento
	 * @see Melhoria #6590
	 */
	protected void listarTipologiaDoRequerimento(){
		try {
			if(Util.isNull(getListaTipologia())){
				navegacaoController.setListaTipologia(aquiculturaServiceFacade.listarTipologiasDoRequerimento(super.requerimento));
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipologias do Requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	protected void listarTipoPeriodoDerivacao(){
		try {
			if(Util.isNull(getListaTipoPeriodoDerivacao())){
				navegacaoController.setListaTipoPeriodoDerivacao(aquiculturaServiceFacade.listarTipoPeriodoDerivacao());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista do período de derivação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/*
	 * [INI] Edição
	 */
	protected FceAquicultura buscarFceAquiculturaBy(Fce fce, TipoAquiculturaEnum aquiculturaEnum) {
		try {
			return aquiculturaServiceFacade.buscarFceAquiculturaBy(fce, aquiculturaEnum);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	protected FceAquicultura buscarFceAquiculturaRequerenteBy(Requerimento requerimento, TipoAquiculturaEnum aquiculturaEnum) {
		try {
			return aquiculturaServiceFacade.buscarFceAquiculturaRequerenteBy(requerimento, aquiculturaEnum);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void excluirListFceAquiculturaEspecie(FceAquicultura fceAquicultura) throws Exception {
		aquiculturaServiceFacade.excluirListFceAquiculturaEspecieBy(fceAquicultura);
	}
	/*
	 * [FIM] Edição
	 */

	protected void montarListaFceAquiculturaEspecie(FceAquicultura fceAquicultura)  {
		listaFceAquiculturaEspecie = new ArrayList<FceAquiculturaEspecie>();
		for(AquiculturaTipoAtividade aquiculturaTipoAtividade : listaAquiculturaTipoAtividadeSelecionado) {
			if(aquiculturaTipoAtividade.isSelecionado()){
				for(EspecieAquiculturaTipoAtividade especieAquiculturaTipoAtividade : aquiculturaTipoAtividade.getListaEspecieAquiculturaTipoAtividadesSelected()) {
					FceAquiculturaEspecie fceAquiculturaEspecie = new FceAquiculturaEspecie(especieAquiculturaTipoAtividade, fceAquicultura);
					listaFceAquiculturaEspecie.add(fceAquiculturaEspecie);
				}
			}
		}
	}

	protected Map<String, Object> montarMapComParametros(FceAquicultura fceAquicultura, List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocalizacaoGeografica) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fce", fceAquicultura.getIdeFce());
		parametros.put("fceAquicultura", fceAquicultura);
		parametros.put("listaFceOutorgaLocalizacaoGeografica", this.listaFceOutorgaLocalizacaoGeografica);
		parametros.put("listaFceOutorgaLocalizacaoAquicultura", this.listaFceOutorgaLocalizacaoAquicultura);
		parametros.put("listaFceAquiculturaLocalizacaoGeografica", listaFceAquiculturaLocalizacaoGeografica);
		parametros.put("listaFceAquiculturaEspecie", this.listaFceAquiculturaEspecie);
		return parametros;
	}

	/**
	 * Método genérico que vai guardar o evento do Upload e o caminho gerado pelo {@link #getCaminhoArquivoUpado(event)} para o seu {@link FceAquicultura}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @param event
	 * @param fceAquicultura
	 */
	protected void tratarUpload(FileUploadEvent event, FceAquicultura fceAquicultura) {
		fceAquicultura.setFileUploadEvent(event);
		fceAquicultura.setUploadCaminhoArquivo(new ArrayList<String>());
		fceAquicultura.getUploadCaminhoArquivo().add(getCaminhoArquivoUpado(event));
	}

	private void adicionarArquivoToDelete(String caminho) {
		if(Util.isNull(arquivosAdicionaisToDelete)) {
			arquivosAdicionaisToDelete = new ArrayList<String>();
		}
		arquivosAdicionaisToDelete.add(caminho);
	}

	public void limparCaminhoDocumentoAdicional() {
		adicionarArquivoToDelete(fceAquicultura.getUploadCaminhoArquivo().get(0));
		fceAquicultura.getUploadCaminhoArquivo().clear();
		fceAquicultura.setFileUploadEvent(null);
		fceAquicultura = null;
	}

	protected void atualizarFceAquicultura(FceAquicultura fceAquicultura) throws Exception {
		aquiculturaServiceFacade.salvarFceAquicultura(fceAquicultura);
	}

	protected void salvarDocumentoAdicional(FceAquicultura fceAquicultura) throws Exception {
		if(validarDocumentoAdicional(fceAquicultura)){
			super.inicarDocumentoAdicional();
			super.getDocumentoUpado().setDscCaminhoArquivo(fceAquicultura.getUploadCaminhoArquivo().get(0));
			super.getDocumentoUpado().setFileUpload(fceAquicultura.getFileUploadEvent());
			super.salvarDocumentoAdicional(super.requerimento, DOCUMENTO_OBRIGATORIO_ADICIONAL);
			fceAquicultura.setIdeDocumentoObrigatorioRequerimento(super.getDocumentoUpado());
			atualizarFceAquicultura(fceAquicultura);
		}
	}

	@Override
	public void abrirDialog() {
		navegacaoController.abrirDialog();
	}

	protected void duplicarFceOutorgaAquicultura(){
		prepararDuplicacao();
		duplicarFce();
	}
	
	protected void prepararDuplicacaoFce(FceAquicultura fceAquicultura) throws Exception{
		fceAquicultura.setIdeFceAquicultura(null);
		fceAquicultura.setIdeFce(navegacaoController.getFce());
		if(!Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeografica)){
		for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocalizacaoGeografica){
			FceOutorgaLocalizacaoGeografica temp = fceOutorgaLocalizacaoGeografica.clone();
			temp.setIdeFceOutorgaLocalizacaoGeografica(null);
			fceOutorgaLocalizacaoGeografica = temp;
			fceOutorgaLocalizacaoGeografica.setIdeFceLancamentoEfluente(null);
			fceOutorgaLocalizacaoGeografica.setIdeFce(fceAquicultura.getIdeFce());
		}
		}
		for(FceOutorgaLocalizacaoAquicultura fceOutorgaLocalizacaoAquicultura : listaFceOutorgaLocalizacaoAquicultura){
			FceOutorgaLocalizacaoAquicultura temp = fceOutorgaLocalizacaoAquicultura.clone();
			temp.setIdeFceOutorgaLocalizacaoAquicultura(null);
			fceOutorgaLocalizacaoAquicultura = temp;
			fceOutorgaLocalizacaoAquicultura.setFceAquicultura(fceAquicultura);
		}
		for(FceAquiculturaEspecie fceAquiculturaEspecie : listaFceAquiculturaEspecie){
			FceAquiculturaEspecie temp = fceAquiculturaEspecie.clone();
			temp.setIdeFceAquiculturaEspecie(null);
			fceAquiculturaEspecie = temp;
			fceAquiculturaEspecie.setIdeFceAquicultura(fceAquicultura);
		}
	}
	
	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 01/07/2015
	 */
	protected void verificarFceLicenciamentoAquicultura() {
		try {
			if(Util.isNull(navegacaoController.getFceLicenciamentoAquicolaPreenchido())){
				navegacaoController.setFceLicenciamentoAquicolaPreenchido(aquiculturaServiceFacade.isFceLicenciamentoAquiculturaPreenchido(super.requerimento));
				if(navegacaoController.getFceLicenciamentoAquicolaPreenchido()){
					JsfUtil.addWarnMessage(Util.getString("fce_out_aqui_com_fce_lic_aqui"));
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean validarDocumentoAdicional(FceAquicultura fceAquicultura){
		return !Util.isNull(fceAquicultura.getFileUploadEvent()) && !Util.isNull(fceAquicultura.getUploadCaminhoArquivo());
	}

	/**
	 * Método que retornar o caminho da pasta que o {@link DocumentoObrigatorioRequerimento} vai ser salvo.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @param event
	 * @return String
	 */
	protected String getCaminhoArquivoUpado(FileUploadEvent event) {
		return FileUploadUtil.Enviar(event, DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
	}

	private boolean validarCoordenada(FceOutorgaLocalizacaoGeografica coordenada){
		if(Util.isNullOuVazio(coordenada.getNomRio())){
			JsfUtil.addErrorMessage("O nome do rio " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		return true;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @param valido
	 * @return
	 * @since 22/04/2015
	 */
	protected boolean validarDadosSobreCultivo(FceAquicultura fceAquicultura) {
		boolean valido = true;
		if(Util.isNullOuVazio(fceAquicultura.getNumViveiro())){
			JsfUtil.addErrorMessage("O nº de viveiros " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getNumAreaTotalOcupada())){
			JsfUtil.addErrorMessage("A área total ocupada pelo cultivo " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getNumVolumeViveiro())){
			JsfUtil.addErrorMessage("O volume dos viveiros " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getNumProducaoCultivoAnual())){
			JsfUtil.addErrorMessage("A produção anual do cultivo " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		
		if(navegacaoController.getActiveTab() == 1){
		if(Util.isNullOuVazio(fceAquicultura.getProfundidadeMedia())){
			JsfUtil.addErrorMessage("A profundidade média " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getTaxaRenovacaoDiariaAgua())){
			JsfUtil.addErrorMessage("A taxa de renovacão diária de água " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getNumDiasRenovacaoAgua())){
			JsfUtil.addErrorMessage("O n° de dias com renovação de água " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getVolumeRecirculadoDiariamenteMetros())){
			JsfUtil.addErrorMessage("O volume recirculado diarimente " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getVolumeRecirculadoDiariamentePorcentagem())){
			JsfUtil.addErrorMessage("O volume recirculado diarimente " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getEsvaziamentoViveiros())){
			JsfUtil.addErrorMessage("O esvaziamento dos viveiros " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getAreaTotalEspelhoAgua())){
			JsfUtil.addErrorMessage("A área total de espelho d'água " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquicultura.getVolumeTotalArmazenado())){
			JsfUtil.addErrorMessage("O volume total armazenado " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		}
		return valido;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @param valido
	 * @return
	 * @since 22/04/2015
	 */
	protected boolean validarListaFceOutorgaLocalizacaoGeografica(String string) {
		for(FceOutorgaLocalizacaoGeografica pontos : listaFceOutorgaLocalizacaoGeografica) {
			if(!pontos.isConfirmado()) {
				if(string.compareTo("poligonal") == 0){
					JsfUtil.addErrorMessage(Util.getString("msg_generica_confirmar_poligonal_intervencao"));
				}
				else if(string.compareTo("captacao") == 0){
					JsfUtil.addErrorMessage(Util.getString("msg_generica_confirmar_ponto_captaco"));
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaAquiculturaTipoAtividade
	 * @param valido
	 * @return
	 * @since 22/04/2015
	 */
	protected boolean validarTipoAtividade() {
		boolean valido = true;
		if(Util.isNullOuVazio(listaAquiculturaTipoAtividadeSelecionado)) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um tipo de atividade." );
			valido = false;
		}
		else {
			int count = 0;
			for(AquiculturaTipoAtividade aquiculturaTipoAtividade : listaAquiculturaTipoAtividadeSelecionado) {
				if(aquiculturaTipoAtividade.isSelecionado() && Util.isNullOuVazio(aquiculturaTipoAtividade.getListaEspecieAquiculturaTipoAtividadesSelected())) {
					JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma espécie cultivada." );
					valido = false;
				} 
				else if(!aquiculturaTipoAtividade.isSelecionado()){
					count++;
				}
			}
			if(count == listaAquiculturaTipoAtividadeSelecionado.size()){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um tipo de atividade." );
				valido = false;
			}
		}
		return valido;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @param valido
	 * @return
	 * @since 22/04/2015
	 */
	protected boolean validarTipoPeriodoDerivacao(FceAquicultura fceAquicultura) {
		boolean valido = true;
		if(Util.isNullOuVazio(fceAquicultura.getIdeTipoPeriodoDerivacao())){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um período de derivação.");
			valido = false;
		}
		else if(fceAquicultura.getIdeTipoPeriodoDerivacao().isIntermitente()){
			if(Util.isNullOuVazio(fceAquicultura.getNumTempoCaptacao())){
				JsfUtil.addErrorMessage("O tempo de derivação "	+ Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			}
			else if(fceAquicultura.getNumTempoCaptacao() > 24) {
				JsfUtil.addErrorMessage(Util.getString("fce_out_aqui_cp_inf0034"));
				valido = false;
			}
		}
		return valido;
	}

	public String getUrlToVisualizar(LocalizacaoGeografica localizacaoGeografica){
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(localizacaoGeografica);
	}
	
	/*
	 * getters & setters
	 */
	public FceOutorgaLocalizacaoGeografica getFceOutorgaLocalizacaoGeograficaSelecionado() {
		return fceOutorgaLocalizacaoGeograficaSelecionado;
	}

	public List<AquiculturaTipoAtividade> getListaAquiculturaTipoAtividade() {
		return listaAquiculturaTipoAtividade;
	}

	public List<AquiculturaTipoAtividade> getListaAquiculturaTipoAtividadeSelecionado() {
		return listaAquiculturaTipoAtividadeSelecionado;
	}

	public List<FceAquiculturaEspecie> getListaFceAquiculturaEspecie() {
		return listaFceAquiculturaEspecie;
	}

	public List<FceOutorgaLocalizacaoAquicultura> getListaFceOutorgaLocalizacaoAquicultura() {
		return listaFceOutorgaLocalizacaoAquicultura;
	}

	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaLocalizacaoGeografica() {
		return listaFceOutorgaLocalizacaoGeografica;
	}

	public List<Tipologia> getListaTipologia() {
		return navegacaoController.getListaTipologia();
	}

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return navegacaoController.getListaTipoPeriodoDerivacao();
	}

	public AquiculturaTipoAtividade getTipoAtividadeSelecionado() {
		return tipoAtividadeSelecionado;
	}

	public void setFceOutorgaLocalizacaoGeograficaSelecionado(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionado) {
		this.fceOutorgaLocalizacaoGeograficaSelecionado = fceOutorgaLocalizacaoGeograficaSelecionado;
	}

	public void setListaAquiculturaTipoAtividadeSelecionado(List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividadeSelecionado) {
		this.listaAquiculturaTipoAtividadeSelecionado = listaAquiculturaTipoAtividadeSelecionado;
	}

	public void setTipoAtividadeSelecionado(AquiculturaTipoAtividade tipoAtividadeSelecionado) {
		this.tipoAtividadeSelecionado = tipoAtividadeSelecionado;
	}

	public FceAquicultura getFceAquicultura() {
		return fceAquicultura;
	}

	public void setFceAquicultura(FceAquicultura fceAquicultura) {
		this.fceAquicultura = fceAquicultura;
	}
	
	@Override
	public boolean isDesabilitarTudo(){
		return navegacaoController.isDesabilitarTudo();
	}
	
	public void desabilitarTudo() {
		navegacaoController.setDesabilitarTudo();
	}
}