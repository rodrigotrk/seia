package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceBarragemController;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.FceCaptacaoSuperficialService;
import br.gov.ba.seia.service.FceIntervencaoBarragemService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceOutorgaLocalizacaoGeograficaBuilder;

/**
 * 04/10/13
 * @author eduardo.fernandes
 */
@Named("fceIntervencaoBarragemController")
@ViewScoped
public class FceIntervencaoBarragemController extends BaseFceBarragemController {

	@EJB
	private FceIntervencaoBarragemService barragemService;
	@EJB
	private FceCaptacaoSuperficialService fceCaptacaoSuperficialService;
	@EJB
	private FceOutorgaLocalizacaoGeograficaService fceOutorgaLocalizacaoGeograficaService;
	
	private static final DocumentoObrigatorio DOC_OBRIGATORIO_BARRAGEM = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_INTERVENCAO_BARRAGEM.getId());
	private static final DocumentoObrigatorio DOC_OBRIGATORIO_UPLOAD = new DocumentoObrigatorio(DocumentoObrigatorioEnum.ESTUDO_HIDROLOGICO.getId());

	private List<FceIntervencaoBarragem> listaFceIntervencaoBarragem;
	private FceIntervencaoBarragem intervencaoBarragem;

	private List<String> listaAno;
	private List<TipoBarragem> listaTipoBarragem;
	
	private Fce fceExterno;
	private Requerimento requerimentoExterno;
	
	private boolean desabilitarTudoExterno;

	@Override
	public void init(){
		veridicarDesabilitarTudoExterno();
		verificarRequerimentoExterno();
		verificarFceExterno();
		verificarEdicao();
		if(!isFceSalvo()){
			iniciarFce(DOC_OBRIGATORIO_BARRAGEM);
			listaFceIntervencaoBarragem = new ArrayList<FceIntervencaoBarragem>();
		} else {
			listarFceIntervencao();
		}
		carregarAba();
	}

	public void veridicarDesabilitarTudoExterno(){
		this.desabilitarTudo = desabilitarTudoExterno;
	}
	
	public void verificarFceExterno(){
		if (!Util.isNullOuVazio(fceExterno)){
			fce = fceExterno;
			informarCaracterizacaoBarragem();
		}
	}
	
	public void verificarRequerimentoExterno(){
		if (!Util.isNullOuVazio(requerimentoExterno)){
			requerimento = requerimentoExterno;
		}
	}
	
	@Override
	public void carregarAba() {
		listarAno();
		listarTipoBarragem();
		carregarDadosDoRequerimento();
		if(!isFceSalvo()){
			criarListaFceIntervencaoBarragem();
		}
	}

	/**
	 * Método que verifica se já existe uma FCE cadastrada no BD para aquele Requerimento.
	 * @return !Util.isNullOuVazio(fce)
	 * @author eduardo.fernandes
	 */
	@Override
	public void verificarEdicao(){
		if(!Util.isNullOuVazio(requerimento)){
			try {
				carregarFceDoRequerente(DOC_OBRIGATORIO_BARRAGEM);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Quando existir uma FCE salva no banco, deve-se buscar o FceIntervencaoBarragem que contenha ela.
	 * @author eduardo.fernandes
	 */
	private void listarFceIntervencao(){
		try {
			listaFceIntervencaoBarragem = barragemService.listarFceIntervencaoBarragemByIdeFce(super.fce);
			super.tratarPontos(listaFceIntervencaoBarragem);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void informarCaracterizacaoBarragem(){
		try {
			if(!Util.isNullOuVazio(intervencaoBarragem)){
				intervencaoBarragem.setEdicao(true);
				super.carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(intervencaoBarragem.getIdeDocumentoObrigatorioRequerimento());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Documento Adicional do FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDadosDoRequerimento(){
		try {
			super.listarOutorgaLocalizacaoGeografica();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 29/01/2016
	 */
	private void criarListaFceIntervencaoBarragem() {
		if(!Util.isNullOuVazio(super.listaOutorgaLocalizacaoGeografica)){
			for(OutorgaLocalizacaoGeografica olg : super.listaOutorgaLocalizacaoGeografica){
				listaFceIntervencaoBarragem.add(new FceIntervencaoBarragem(super.fce, olg));
			}
		}
	}

	private void listarAno() {
		Integer anoInicial = Calendar.getInstance().get(Calendar.YEAR) - 20;
		Integer anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		listaAno = new ArrayList<String>();
		anoAtual = (anoAtual - anoInicial) + 1;
		for (int x = 0; x < anoAtual; x++){
			listaAno.add(new Integer(anoInicial + x).toString());
		}
		Collections.reverse(listaAno);
	}

	private void listarTipoBarragem(){
		try{
			listaTipoBarragem = fceOutorgaServiceFacade.listarTipoBarragem();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipo Barragem");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método chamado no clique do botão Salvar no dialogFceBarragemIntervencao.
	 * @author eduardo.fernandes
	 */
	public void salvarDialogFceBarragemRegularizacao(){
		try {
			if(validarIntervencaoBarragemRegularizacao()){
				FceBarragemController fceBarragemController = (FceBarragemController) SessaoUtil.recuperarManagedBean("#{fceBarragemController}", FceBarragemController.class);
				barragemService.salvarDialogFceBarragemRegularizacao(this, fceBarragemController);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que verifica se o usuário preencheu devidamente o dialog de FCE INTERVENCAO.
	 * @return TRUE or FALSE
	 * @author eduardo.fernandes
	 */
	private boolean validarIntervencaoBarragemRegularizacao(){
		boolean valido = true;

		boolean volMaximoNull = true;
		boolean volMinimoNull = true;
		boolean volMaximorumNull = true;

		if(Util.isNullOuVazio(intervencaoBarragem.getNumBaciaHidrografica())){
			valido = false;
			JsfUtil.addErrorMessage("A Área de Drenagem da Bacia Hidrográfica " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumAlturaMaxima())){
			valido = false;
			JsfUtil.addErrorMessage("A Altura Máxima(da base à crista) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumComprimentoTotal())){
			valido = false;
			JsfUtil.addErrorMessage("O Comprimento Total " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumLarguraBase())){
			valido = false;
			JsfUtil.addErrorMessage("A Largura da Base " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumLarguraCoroamento())){
			valido = false;
			JsfUtil.addErrorMessage("A Largura do Coroamento " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumNivelAguaMaxima())){
			valido = false;
			JsfUtil.addErrorMessage("O Nível d'água máximo operacional " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else {
			volMaximoNull = false;
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumNivelAguaMinima())){
			valido = false;
			JsfUtil.addErrorMessage("O Nível d'água mínimo operacional " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else {
			volMinimoNull = false;
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumNivelAguaMaximorum())){
			valido = false;
			JsfUtil.addErrorMessage("O Nível d'água máximo maximorum " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else {
			volMaximorumNull = false;
		}
		if(!volMaximoNull && !volMaximorumNull){
			if(intervencaoBarragem.getNumNivelAguaMaxima() >= intervencaoBarragem.getNumNivelAguaMaximorum()){
				valido = false;
				JsfUtil.addErrorMessage(Util.getString("fce_intervencao_barragem_inf0029"));
			}
			if(!volMinimoNull){
				if(intervencaoBarragem.getNumNivelAguaMinima() > intervencaoBarragem.getNumNivelAguaMaximorum() || intervencaoBarragem.getNumNivelAguaMinima() > intervencaoBarragem.getNumNivelAguaMaxima()){
					valido = false;
					JsfUtil.addErrorMessage(Util.getString("fce_intervencao_barragem_inf0028"));
				}
			}
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumVolumeMaximoAcumulado())){
			valido = false;
			JsfUtil.addErrorMessage("O Volume máximo acumulado " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumVazaoRegularizada())){
			valido = false;
			JsfUtil.addErrorMessage("A Vazão regularizada com 90% de garantia " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(!validarDatas()){
			valido = false;
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumGarantiaAtendimentoVazao())){
			valido = false;
			JsfUtil.addErrorMessage("A Garantia de atendimento da vazão regularizadora " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else if(intervencaoBarragem.getNumGarantiaAtendimentoVazao() > 100){
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_intervencao_barragem_vazao_regularizada"));
		}  else if(intervencaoBarragem.getNumGarantiaAtendimentoVazao() < 80.0){
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_intervencao_barragem_inf0030"));
		}
	
		if(!isArquivoUpado()){
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_intervencao_barragem_upload_obrigatorio"));
		}
		return valido;
	}

	/**
	 * Método que verifica se as datas foram informadas e se a data inicial vem antes da data final.
	 * @return valido
	 * @author eduardo.fernandes
	 */
	public boolean validarDatas(){
		boolean valido = true;
		if(Util.isNullOuVazio(intervencaoBarragem.getDtcIniVazaoRegularizada())){
			valido = false;
			JsfUtil.addErrorMessage("O Início do período utilizado para definir a vazão " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getDtcFimVazaoRegularizada())){
			valido = false;
			JsfUtil.addErrorMessage("O Fim do período utilizado para definir a vazão " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(valido){
			if(intervencaoBarragem.getDtcFimVazaoRegularizada() < intervencaoBarragem.getDtcIniVazaoRegularizada()){
				valido = false;
				JsfUtil.addErrorMessage(Util.getString("fce_intervencao_barragem_validacao_datas"));
			}
		}
		return valido;
	}

	/**
	 * Método chamado para pelo dialog Barragem de Regularização.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 29/01/2016
	 */
	public void salvarCaracterizacaoBarragem(){
		try {
			salvarDocumentoAdicional();
			intervencaoBarragem.setIdeDocumentoObrigatorioRequerimento(getDocumentoUpado());
			salvarFceIntervencaoBarragem(intervencaoBarragem);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 17/12/2014
	 * @throws Exception
	 */
	private void salvarDocumentoAdicional() {
		try {
			super.salvarDocumentoAdicional(requerimento, DOC_OBRIGATORIO_UPLOAD);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o extrato do estudo hidrológico");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @param fceIntervencaoBarragem
	 * @author eduardo.fernandes
	 * @throws Exception 
	 */
	private void salvarFceIntervencaoBarragem(FceIntervencaoBarragem fceIntervencaoBarragem) throws Exception{
		if(!super.isFceSalvo()){
			super.salvarFce();
		}
		barragemService.salvarFceIntervencaoBarragem(fceIntervencaoBarragem);
	}

	@Override
	public void finalizar(){
		try {
			barragemService.finalizar(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_finalizar") + " o FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		if(validarAba()){
			for(FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem){
				salvarFceIntervencaoBarragem(fceIntervencaoBarragem);
			}
			super.concluirFce();
			fecharDialogFceBarragem();
		}
	}

	@Override
	public boolean validarAba(){
		for(FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem){
			if(fceIntervencaoBarragem.isTipoBarragemRegularizacao() && Util.isNullOuVazio(fceIntervencaoBarragem)){
				JsfUtil.addErrorMessage(Util.getString("fce_intervencao_barragem_erro"));
				return true;
			}
		}
		return true;
	}

	private void fecharDialogFceBarragem(){
		JsfUtil.addSuccessMessage(Util.getString("msg_generica_sucesso_finalizar"));
		RequestContext.getCurrentInstance().execute("tipoBarragem.hide()");
		limpar();
		RequestContext.getCurrentInstance().execute("rel_barragem.show()");
	}
	
	public void fecharDialogBarragemRegularizacao(){
		if(!intervencaoBarragem.isEdicao()){
			super.exibirMensagem001();
		}
		else{
			super.exibirMensagem002();
		}
		RequestContext.getCurrentInstance().execute("barragemRegularizacao.hide()");
	}

	public void limparDialog(){
		intervencaoBarragem = null;
		super.limparDocumentoUpado();		
	}
	
	@Override
	public void limpar(){
		limparDialog();
		listaFceIntervencaoBarragem.clear();
	}

	/**
	 * Método baixar o arquivo .doc que o usuário deve preencher e depois enviar.
	 * @return FILE
	 * @throws FileNotFoundException2017.001.208613/INEMA/REQ
	 * @author eduardo.fernandes
	 */
	public StreamedContent getEstudoHidrologico() {
		try {
			return getDadosAdicionais("Extrato_Estudo_Hidrologico.doc", "Estudo Hidrológico.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_extrato_hidrologico"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_OBRIGATORIO_BARRAGEM);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	// Getters && Setters
	public FceIntervencaoBarragem getIntervencaoBarragem() {
		return intervencaoBarragem;
	}

	public void setIntervencaoBarragem(FceIntervencaoBarragem intervencaoBarragem) {
		this.intervencaoBarragem = intervencaoBarragem;
	}

	public List<String> getListaAno() {
		return listaAno;
	}

	public void setListaAno(List<String> listaAno) {
		this.listaAno = listaAno;
	}

	public List<FceIntervencaoBarragem> getListaFceIntervencaoBarragem() {
		return listaFceIntervencaoBarragem;
	}

	public void setListaFceIntervencaoBarragem(List<FceIntervencaoBarragem> listaFceIntervencaoBarragem) {
		this.listaFceIntervencaoBarragem = listaFceIntervencaoBarragem;
	}

	public List<TipoBarragem> getListaTipoBarragem() {
		return listaTipoBarragem;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("panelBarragem");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioIntervencaoBarragem");
		RequestContext.getCurrentInstance().execute("tipoBarragem.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		listaOutorgaConcedida = new ArrayList<OutorgaConcedida>();
		for(FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem){
			try {
				fceIntervencaoBarragem.setIdeFce(super.fce);
				fceIntervencaoBarragem.setIdeFceIntervencaoBarragem(null);
				OutorgaLocalizacaoGeografica outorgaLocGeoOriginal = fceIntervencaoBarragem.getIdeOutorgaLocalizacaoGeografica();
				LocalizacaoGeografica locGeoDuplicada = super.fceOutorgaServiceFacade.duplicarLocalizacaoGeografica(outorgaLocGeoOriginal.getIdeLocalizacaoGeografica());

				FceOutorgaLocalizacaoGeograficaBuilder builder = new FceOutorgaLocalizacaoGeograficaBuilder()
				.comFce(super.fce)
				.comTipologia(TipologiaEnum.INTERVENCAO_CORPO_HIDRICO)
				.comLocalizacaoGeografica(locGeoDuplicada)
				.comOutorgaLocalizacaoGeografica(outorgaLocGeoOriginal);
				fceIntervencaoBarragem.setIdeTipoBarragem(outorgaLocGeoOriginal.getIdeTipoBarragem());
				fceIntervencaoBarragem.setIdeFceOutorgaLocalizacaoGeografica(builder.construirFceOutorgaLocalizacaoGeografica());
				
				listaOutorgaConcedida.add(new OutorgaConcedida(fceIntervencaoBarragem.getIdeFceOutorgaLocalizacaoGeografica(), outorgaLocGeoOriginal));
				
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " a Localização Geográfica.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			for(FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem){
				salvarFceOutorgaLocalizacaoGeografica(fceIntervencaoBarragem);
				salvarFceIntervencaoBarragem(fceIntervencaoBarragem);
			}
			super.salvarListaOutorgaConcedida(listaOutorgaConcedida);
			super.tratarPontos(listaFceIntervencaoBarragem);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}		
	}

	@Override
	protected void carregarFceTecnico() {
		listarAno();
		listarTipoBarragem();
		listarFceIntervencao();
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceIntervencaoBarragem
	 * @throws Exception
	 * @since 29/01/2016
	 */
	private void salvarFceOutorgaLocalizacaoGeografica(FceIntervencaoBarragem fceIntervencaoBarragem) throws Exception {
		if(!super.isFceSalvo()){
			super.salvarFce();
		}
		fceOutorgaLocalizacaoGeograficaService.salvarFceOutorgaLocGeo(fceIntervencaoBarragem.getIdeFceOutorgaLocalizacaoGeografica());
	}

	public Fce getFceExterno() {
		return fceExterno;
	}

	public void setFceExterno(Fce fceExterno) {
		this.fceExterno = fceExterno;
	}

	public Requerimento getRequerimentoExterno() {
		return requerimentoExterno;
	}

	public void setRequerimentoExterno(Requerimento requerimentoExterno) {
		this.requerimentoExterno = requerimentoExterno;
	}

	public boolean isDesabilitarTudoExterno() {
		return desabilitarTudoExterno;
	}

	public void setDesabilitarTudoExterno(boolean desabilitarTudoExterno) {
		this.desabilitarTudoExterno = desabilitarTudoExterno;
	}
	
}