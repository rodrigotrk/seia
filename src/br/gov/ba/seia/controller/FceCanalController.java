package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.CaracteristicaCanal;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceCanal;
import br.gov.ba.seia.entity.FceCanalTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.FceCanalTrecho;
import br.gov.ba.seia.entity.FceCanalTrechoSecaoGeometrica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.SecaoGeometrica;
import br.gov.ba.seia.entity.TipoCanal;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoRevestimento;
import br.gov.ba.seia.entity.TipoRio;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.SecaoGeometricaEnum;
import br.gov.ba.seia.enumerator.TipoRioEnum;
import br.gov.ba.seia.facade.FceCanalServiceFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

@Named("fceCanalController")
@ViewScoped
public class FceCanalController extends FceController implements FceNavegacaoInterface {

	private static final DocumentoObrigatorio DOCUMENTO_OBRIGATORIO = new DocumentoObrigatorio(
			DocumentoObrigatorioEnum.FCE_CANAIS.getId(), "Formulário de Caracterização do Empreendimento - Canais");

	@EJB
	private FceCanalServiceFacade facade;

	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeoServiceFacade;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	private int activeTab;

	private FceCanal fceCanal;

	private FceCanalTrecho trecho;
	
	private List<FceCanalTipoFinalidadeUsoAgua> fceCanalTiposFinalidadesUsoAguas;

	private List<TipoCanal> tiposCanal;
	
	private List<FceCanalTrechoSecaoGeometrica> fceCanalTrechosSecaoGeometrica;
	
	private List<TipoRevestimento> tipoRevestimentos;
	
	private List<TipoRio> tiposRios;
	
	private boolean editarTrecho;

	private boolean editarVazao;
	
	private List<FceCanalTrechoSecaoGeometrica> trechoSecaoGeometricaSelecionados;
	
	private List<TipoRevestimento> tipoRevestimentoSelecionados;
	
	private boolean permitirSalvar = true;
	
	private String cameFrom = StringUtils.EMPTY;
	
	private FceCanalTipoFinalidadeUsoAgua fceCanalTipoFinalidadeUsoAgua;
	
	@Override
	public void init() {
		activeTab = 0;
		verificarEdicao();
		try {
			if (!super.isFceSalvo()) {
				super.iniciarFce(DOCUMENTO_OBRIGATORIO);
				iniciarFceCanal();
			} else {
				this.setFceCanal(facade.getFceCanal(this.getFce()));
				if (Util.isNullOuVazio(this.getFceCanal())) {
					iniciarFceCanal();
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		carregarAba();
	}

	private void iniciarFceCanal() throws Exception{
		this.setFceCanal(new FceCanal());
		this.getFceCanal().setFce(this.getFce());
		this.setTrecho(new FceCanalTrecho());
		this.obterVazao();
	}
	
	@Override
	public void verificarEdicao() {
		super.carregarFceDoRequerente(DOCUMENTO_OBRIGATORIO);
	}

	/**
	 * Obtém a vazão informada no requerimento
	 * @throws Exception
	 */
	private void obterVazao() throws Exception{
		if(!Util.isNullOuVazio(this.getRequerimento()) && !Util.isNullOuVazio(this.getRequerimento().getRequerimentoUnico()) && !Util.isNullOuVazio(this.getRequerimento().getRequerimentoUnico().getNumVazaoCaptacao())){
			this.getFceCanal().setVazao(this.getRequerimento().getRequerimentoUnico().getNumVazaoCaptacao().doubleValue());
		}else{
			PerguntaRequerimento pR = this.facade.getPerguntaRequerimento(this.getRequerimento(), "NR_A3_P1"); //Licença
			if(pR != null && pR.getIndResposta()){
				RequerimentoTipologia requerimentoTipologia =  facade.getRequerimentoTipologia(this.getRequerimento());
				if(!Util.isNull(requerimentoTipologia) || !Util.isNull(requerimentoTipologia.getValAtividade())){
					fceCanal.setVazao(requerimentoTipologia.getValAtividade().doubleValue());
				}else {
					JsfUtil.addWarnMessage("Vazão não informada no requerimento");
				}							
			}
		}

		if(Util.isNullOuVazio(this.fceCanal.getVazao())){
			editarVazao = false;
		}else {
			editarVazao = true;
		}
	}
	
	@Override
	public void carregarAba() {
		try {
			buildFceCanalTiposFinalidadesUsoAgua();
			tiposRios = facade.listarTipoRio();
			tiposRios.remove(new TipoRio(TipoRioEnum.EFEMERO.getId()));
			tiposCanal = facade.listarTiposCanal();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void buildFceCanalTiposFinalidadesUsoAgua() throws Exception{
		List<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaCollection = facade.listarTipoFinalidadeUsoAgua();
		this.fceCanalTiposFinalidadesUsoAguas = new ArrayList<FceCanalTipoFinalidadeUsoAgua>();
		for(TipoFinalidadeUsoAgua t : tipoFinalidadeUsoAguaCollection){
			this.fceCanalTiposFinalidadesUsoAguas.add(new FceCanalTipoFinalidadeUsoAgua(this.fceCanal, t));
		}
		this.fceCanalTiposFinalidadesUsoAguas.removeAll(this.fceCanal.getFceCanalTiposFinalidadesUsoAgua());//remove os tipos de uso para não duplicar os itens na inserção
		this.fceCanalTiposFinalidadesUsoAguas.addAll(this.fceCanal.getFceCanalTiposFinalidadesUsoAgua());//add os tipos de uso do canal
		Collections.sort(this.fceCanalTiposFinalidadesUsoAguas);
	}
	
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("FceCanaisAbaDadosGerais".equals(event.getTab().getId())) {
			activeTab = 0;
		}else if("FceCanaisAbacaracterizacaoAtividade".equals(event.getTab().getId())){
			activeTab = 0;
			if(validarAba()){
				activeTab = 1;
			}
		}else if("FceCanaisResumo".equals(event.getTab().getId())){
			activeTab = 1;
			if(validarAba()){
				activeTab = 2;
			}
		}
	}
 
	@Override
	public void avancarAba() {
		if(validarAba()){
			try {
				if(this.getActiveTab() >= 2){
					if(validarTrechos()){
						concluirFce();
						facade.prepararParaSalvar(this);
						Html.exibir("confirmationSalvarFCECanais");
					}else{
						Html.exibir("dialogMsgSecaoGeo");
					}
					return;
				}
				facade.prepararParaSalvar(this);
				activeTab++;
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage(e.getMessage());
			}
		}
	}

	public void salvarFceCanais() throws Exception {
		if(Util.isNullOuVazio(fce)){
			salvarFce();
		}
		
		facade.salvarFceCanal(fceCanal);
		if(this.getActiveTab() >= 2){
			JsfUtil.addSuccessMessage("Inclusão realizada com sucesso!");
		}
	}
	
	private boolean validarTrechos() {
		permitirSalvar = true;
		for (FceCanalTrecho trechoObj : this.getFceCanal().getCanalTrechos()) {
			for (FceCanalTrechoSecaoGeometrica fceCanalTrechoSecaoGeometrica : trechoObj.getFceCanalTrechoSecaoGeometrica()) {
				if("Outros".equalsIgnoreCase(fceCanalTrechoSecaoGeometrica.getSecaoGeometrica().getDscSecaoGeometrica())){
					permitirSalvar = false;
					break;
				}
			} 
			
			if(permitirSalvar){
				for (TipoRevestimento fceCanalTrechoTipoRevestimento : trechoObj.getTiposRevestimentos()) {
					if("Outros".equalsIgnoreCase(fceCanalTrechoTipoRevestimento.getDsTiporevestimento())){
						permitirSalvar = false;
						break;
					}
				}
			}
		}
		
		return permitirSalvar;
	}
	
	public StreamedContent getImprimirRelatorio() {
		try {
			return super.getImprimirRelatorio(this.fce, DOCUMENTO_OBRIGATORIO);
		} catch (Exception e) {
			JsfUtil.addSuccessMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	}
	
	@Override
	public void voltarAba() {
		if(this.getActiveTab() == 0){
			Html.esconder("dialogFceCanais");
		}else{
			activeTab--;
		}
	}

	public String getComprimentoTotal(FceCanal fceCanal){
		double comprimentoTotal = 0d;
		if(!Util.isNullOuVazio(fceCanal)){
			if(!Util.isNullOuVazio(fceCanal.getCanalTrechos())){
				for (FceCanalTrecho trecho : this.getFceCanal().getCanalTrechos()) {
					comprimentoTotal = comprimentoTotal + (trecho.getComprimento() != null ? trecho.getComprimento() : 0d); 
				}
			}
		}
		DecimalFormat df = new DecimalFormat("##,###,###,##0.00");
		return df.format(comprimentoTotal);
	}


	public void removeMunicipio(Municipio municipio){
		this.getFceCanal().getMunicipios().remove(municipio);
		this.setActiveTab(1);
	}
	
	/**
	 * Para a inclusão do primeiro trecho do canal será necessário informar o ponto inicial e o ponto final do trecho.
	 * À partir do 2ª trecho, será necessário informar apenas o ponto final do trecho.
	 * O ponto inicial sempre virá preenchido com o ponto final do último trecho inserido, sem opção para edição.
	 * 
	 */
	public void addtrecho()  {
		this.setTrecho(new FceCanalTrecho());
		this.getTrecho().setLocalizacaoGeograficaInicio(new LocalizacaoGeografica());
		this.getTrecho().setLocalizacaoGeograficaFim(new LocalizacaoGeografica());
		if(!Util.isNullOuVazio(this.getFceCanal().getCanalTrechos())){
			if(!this.getFceCanal().getCanalTrechos().isEmpty()){
				int size = this.getFceCanal().getCanalTrechos().size();
				//É necessário duplicar a localização, caso contrário, a mesma localização estará associada a dois trechos diferentes, impossibilitando a exclusão de um deles
				LocalizacaoGeografica novaLoc = localizacaoGeoServiceFacade.duplicarLocalizacaoGeografica(this.getFceCanal().getCanalTrechos().get(size -1).getLocalizacaoGeograficaFim());
				this.getTrecho().setLocalizacaoGeograficaInicio(localizacaoGeoServiceFacade.carregarPorIdeLocalizacao(novaLoc.getIdeLocalizacaoGeografica()));
			}
		}
	}

	public void editarTrecho(FceCanalTrecho trecho) throws Exception {
		obterTrecho(trecho, false);
	}
	
	private void obterTrecho(FceCanalTrecho trecho, boolean editar) throws Exception{
		this.setTrecho(trecho);
		this.setFceCanalTrechosSecaoGeometrica(buildFceCanalTrechosSecaoGeometrica());
		this.setTipoRevestimentos(buildTipoRevestimentos());
		this.setEditarTrecho(editar);
		
		this.tipoRevestimentoSelecionados = new ArrayList<TipoRevestimento>();
		this.tipoRevestimentoSelecionados.addAll(this.getTrecho().getTiposRevestimentos());
		this.trechoSecaoGeometricaSelecionados = new ArrayList<FceCanalTrechoSecaoGeometrica>();
		this.trechoSecaoGeometricaSelecionados.addAll(this.getTrecho().getFceCanalTrechoSecaoGeometrica());
		Html.atualizar("formFceCanaisDadosTrecho");
	}
	
	public void visualizarTrecho(FceCanalTrecho trecho) throws Exception{
		this.obterTrecho(trecho, true);
	}
	
	public int getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}
	
	@Override
	public void finalizar() {
		if (validarAba()) {
			try {
				facade.prepararParaFinalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Canais.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	@Override
	public void prepararParaFinalizar() throws Exception {
		salvarFceCanais();
		super.concluirFce();
		if(!isFceSalvo()){
			super.exibirMensagem001();
		}
		else {
			super.exibirMensagem002();
		}
		RequestContext.getCurrentInstance().execute("dialogFceCanais.hide()");
		limpar();
	}
	
	public void addcaracteristica(CaracteristicaCanal caracteristicaCanal) {
		if(this.getFceCanal().getCaracteristicasCanal() == null){
			this.getFceCanal().setCaracteristicasCanal(new ArrayList<CaracteristicaCanal>());
		}
		
		if(this.getFceCanal().getCaracteristicasCanal().contains(caracteristicaCanal)){
			this.getFceCanal().getCaracteristicasCanal().remove(caracteristicaCanal);
		}else{
			this.getFceCanal().getCaracteristicasCanal().add(caracteristicaCanal);
		}
	}
	
	public void salvarTrecho(){
		if(this.getFceCanal().getCanalTrechos() == null){
			this.getFceCanal().setCanalTrechos(new ArrayList<FceCanalTrecho>());
		}
		
		if(Util.isNullOuVazio(this.getTrecho().getLocalizacaoGeograficaInicio())){
			MensagemUtil.msg0003("Localização geografica inicial");
			return;
		}
		if(Util.isNullOuVazio(this.getTrecho().getLocalizacaoGeograficaFim())){
			MensagemUtil.msg0003("Localização geografica fim");
			return;
		}
		
		//calcula o comprimento do trecho 
		try {
			BigDecimal comprimento = localizacaoGeoServiceFacade.getDistanciaEntrePontos(this.getTrecho().getLocalizacaoGeograficaInicio(), this.getTrecho().getLocalizacaoGeograficaFim());
			this.getTrecho().setComprimento(comprimento.doubleValue());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		this.getTrecho().setCanal(this.getFceCanal());
		this.getFceCanal().getCanalTrechos().add(this.getTrecho());
		this.setActiveTab(1);
		this.setTrecho(new FceCanalTrecho());
		
		try {
			//Obtém todos os municipios adcionados ao Canal e os que os trechos foram inseridos
			List<Municipio> municipios = this.getMunicipios();
			if(!Util.isNull(this.fceCanal.getMunicipios())){
				//Remove os municípios inseridos diretamento ao Canal para validar se os trechos cadastrados passam por mais de um município
				municipios.removeAll(this.fceCanal.getMunicipios());
			}
			if(municipios.size() > 1){
				JsfUtil.addWarnMessage("Os trechos cadastrados passam por mais de um município");
			}
			List<String> bacias = this.getBacias(this.fceCanal);
			if(bacias.size() > 1){
				JsfUtil.addWarnMessage("Os trechos cadastrados passam por mais de uma bacia hidrografica");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
		JsfUtil.addSuccessMessage("Trecho adicionado");
		Html.esconder("dialogAddtrecho");
		
	}
	
	public void salvarEdicaoTrecho(){
		if (validarDadosTrecho()) {
			this.trecho.setFceCanalTrechoSecaoGeometrica(this.trechoSecaoGeometricaSelecionados);
			this.trecho.setTiposRevestimentos(this.tipoRevestimentoSelecionados);
						
			Html.esconder("dialogDadostrecho");
		}
	}
	
	public boolean validarDadosTrecho(FceCanalTrecho trecho){
		boolean result = true;
		if(Util.isNullOuVazio(trecho.getTipoCanal())){
			result = false;
		}
		if(Util.isNullOuVazio(trecho.getFceCanalTrechoSecaoGeometrica())){
			result = false;
		}
		if(Util.isNullOuVazio(trecho.getTiposRevestimentos())){
			result = false;
		}
		return result;
	}
	
	private boolean validarDadosTrecho(){
		boolean result = true;
		if(Util.isNullOuVazio(this.getTrecho().getTipoCanal())){
			MensagemUtil.msg0003("Tipo do Canal");
			result = false;
		}
		
		if(Util.isNullOuVazio(this.trechoSecaoGeometricaSelecionados)){
			MensagemUtil.msg0003("Secao geométrica");
			result = false;
		}else{
			//#9642
			for (FceCanalTrechoSecaoGeometrica item : this.trechoSecaoGeometricaSelecionados) {
				if (SecaoGeometricaEnum.TRAPEZOIDAL.getId() == item.getSecaoGeometrica().getId().intValue()) {
					if(item.getBase_maior() == 0d){
						MensagemUtil.msg0003("Base maior");
						result = false;
					}
					if(item.getBase_menor() == 0d){
						MensagemUtil.msg0003("Base menor");
						result = false;
					}
					if(item.getAltura() == 0d){
						MensagemUtil.msg0003("Altura");
						result = false;
					}
				}
				if (SecaoGeometricaEnum.RETANGULAR.getId() == item.getSecaoGeometrica().getId().intValue()) {
					if(item.getLargura() == 0d){
						MensagemUtil.msg0003("Largura");
						result = false;
					}
					if(item.getAltura() == 0d){
						MensagemUtil.msg0003("Altura");
						result = false;
					}
				}
				if (SecaoGeometricaEnum.CIRCULAR.getId() == item.getSecaoGeometrica().getId().intValue() && item.getDiametro() == 0d) {
					MensagemUtil.msg0003("Diâmetro");
					result = false;
				}
			}
		}
		
		if(Util.isNullOuVazio(this.tipoRevestimentoSelecionados)){
			MensagemUtil.msg0003("Tipo de revestimento");
			result = false;
		}
		
		return result;
	}
	
	public String getVisualizarLocalizacao(LocalizacaoGeografica localizacaoGeografica) {
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(localizacaoGeografica);
	}
	
	@Override
	public void limpar() {
		super.limparFce();
		activeTab = 0;
	}

	
	@Override
	public boolean validarAba() {
		boolean result = true;
		//valida aba dados gerais
		if(getActiveTab() == 0){
			if(this.fceCanal.getVazao() == 0d){
				this.fceCanal.setVazao(0);
				MensagemUtil.msg0003("Vazão");
				result = false;
			}
			if(this.fceCanal.getAreaOcupada() == 0d){
				this.fceCanal.setAreaOcupada(0);
				MensagemUtil.msg0003("Área ocupada");
				result = false;
			}
			if(Util.isNullOuVazio(this.fceCanal.getCaracteristicasCanal())){
				MensagemUtil.msg0003("Característica");
				result = false;
			}
			if(Util.isNullOuVazio(this.fceCanal.getFceCanalTiposFinalidadesUsoAgua())){
				MensagemUtil.msg0003("Finalidade");
				result = false;
			}
		// caracteristicas de atividade
		}else if(this.getActiveTab() == 1){
			if(Util.isNullOuVazio(this.getFceCanal().getCanalTrechos())){
				JsfUtil.addWarnMessage("Formulário dados do trecho é de preenchimento obrigatório.");
				result = false;
			}
			if(Util.isNullOuVazio(this.getFceCanal().getNomeRio())){
				MensagemUtil.msg0003("nome do rio");
				result = false;
			}
			if(Util.isNullOuVazio(this.getFceCanal().getTipoRio())){
				MensagemUtil.msg0003("tipo do rio");
				result = false;
			}
			//Dados do trecho
			for (FceCanalTrecho trecho : this.getFceCanal().getCanalTrechos()) {
				if(Util.isNullOuVazio(trecho.getTipoCanal())){
					MensagemUtil.msg0003("tipo do canal do trecho");
					result = false;
				}
				if(Util.isNullOuVazio(trecho.getFceCanalTrechoSecaoGeometrica())){
					MensagemUtil.msg0003("seção geométrica do trecho");
					result = false;
				}
				if(Util.isNullOuVazio(trecho.getTiposRevestimentos())){
					MensagemUtil.msg0003("tipo revestimento do trecho");
					result = false;
				}
				
				if(Util.isNullOuVazio(trecho.getComprimento()) || trecho.getComprimento().equals(0d)){
					MensagemUtil.msg0003("Comprimento do trecho");
					result = false;
				}
			}
		}
		return result;
	}
	
	public boolean verificarEdicaoFceCnal(){
		if(this.fceCanal == null || this.fceCanal.getId() == null){
			return false;
		}
		return true;
	}

	@Override
	public void abrirDialog() {
		if(Util.isNullOuVazio(requerimento)){
			requerimento = fce.getIdeRequerimento();
		}
		
		if(Util.isNullOuVazio(requerimento.getNumRequerimento())){
			try {
				requerimento = this.requerimentoService.buscarEntidadeCarregadaPorId(requerimento);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		
		Html.exibir("dialogFceCanais");
		Html.atualizar("_dialogFceCanais");
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}
	
	private void salvarAbas() throws Exception {
		salvarFceCanais();
	}

	@Override
	protected void prepararDuplicacao() {
		fceCanal.setFce(super.fce);
		fceCanal.setIdeFceCanal(null);
		
		List<CaracteristicaCanal> canalCaracTecnico = new ArrayList<CaracteristicaCanal>();
		if(fceCanal.getCaracteristicasCanal() !=null && !fceCanal.getCaracteristicasCanal().isEmpty()){
			for (CaracteristicaCanal itemCanalCarac : fceCanal.getCaracteristicasCanal()) {
				CaracteristicaCanal itemCanalCaracTecnico = new CaracteristicaCanal();
				itemCanalCaracTecnico.setDsCaracteristicaCanal(itemCanalCarac.getDsCaracteristicaCanal());
				itemCanalCaracTecnico.setIdeCaracteristicaCanal(itemCanalCarac.getIdeCaracteristicaCanal());
				itemCanalCaracTecnico.setSelecionado(itemCanalCarac.isSelecionado());
				
				canalCaracTecnico.add(itemCanalCaracTecnico);
			}
		}
		
		fceCanal.getCaracteristicasCanal().clear();
		fceCanal.setCaracteristicasCanal(canalCaracTecnico);
		
		if(fceCanal.getCanalTrechos() != null && !fceCanal.getCanalTrechos().isEmpty()){
			for (FceCanalTrecho item : fceCanal.getCanalTrechos()) {
				item.setIdFceCanalTrecho(null);
				item.setCanal(fceCanal);
				for (FceCanalTrechoSecaoGeometrica itemSecGeom : item.getFceCanalTrechoSecaoGeometrica()) {
					itemSecGeom.setIdeFceCanalTrechoSecaoGeometrica(null);
				}
				
				try {
					item.setLocalizacaoGeograficaInicio(localizacaoGeoServiceFacade.duplicarLocalizacaoGeografica(item.getLocalizacaoGeograficaInicio()));
					item.setLocalizacaoGeograficaFim(localizacaoGeoServiceFacade.duplicarLocalizacaoGeografica(item.getLocalizacaoGeograficaFim()));
				} catch (Exception e) {
					MensagemUtil.erro("Erro ao duplicar Localização Geográfica do FCE de Canais. Contate o Administrador.");
				}
				
				
				
			}
		}
		
		if(!Util.isNullOuVazio(fceCanal.getFceCanalTiposFinalidadesUsoAgua())) {
			for(FceCanalTipoFinalidadeUsoAgua t : fceCanal.getFceCanalTiposFinalidadesUsoAgua()) {
				t.setIdeFceCanalTipoFinalidadeUsoAgua(null);
				t.setIdeFceCanal(fceCanal);
			}
		}
	}

	@Override
	protected void duplicarFce() {
		try{
			salvarAbas();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Canais.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}
	
	private void exibirDialogMsgSecaoGeo(String opcao) {
		if(opcao.equals("Outros")){
			Html.exibir("dialogMsgSecaoGeo");
		}
	}
	
	public void addFceCanalTrechoSecaoGeometrica(FceCanalTrechoSecaoGeometrica trechoSecaoGeometrica){
		if(!this.trechoSecaoGeometricaSelecionados.contains(trechoSecaoGeometrica)){
			trechoSecaoGeometrica.setFceCanalTrecho(this.getTrecho());
			this.trechoSecaoGeometricaSelecionados.add(trechoSecaoGeometrica);
			this.exibirDialogMsgSecaoGeo(trechoSecaoGeometrica.getSecaoGeometrica().getDscSecaoGeometrica());
		}else{
			for (FceCanalTrechoSecaoGeometrica item : trechoSecaoGeometricaSelecionados) {
				if(item.getSecaoGeometrica().getDscSecaoGeometrica().equalsIgnoreCase(trechoSecaoGeometrica.getSecaoGeometrica().getDscSecaoGeometrica())){
					this.trechoSecaoGeometricaSelecionados.remove(item);
					break;
				}
			}

		}
		Html.atualizar("formFceCanaisDadosTrecho:checkBoxesSecaoGeometrica");
	}
	
	public void addTipoRevestimento(TipoRevestimento tipoRevestimento) {
		if(!this.tipoRevestimentoSelecionados.contains(tipoRevestimento)){
			tipoRevestimento.setSelecionado(true);
			this.tipoRevestimentoSelecionados.add(tipoRevestimento);
			this.exibirDialogMsgSecaoGeo(tipoRevestimento.getDsTiporevestimento());
		} else {
			for (TipoRevestimento item : tipoRevestimentoSelecionados) {
				if(item.getDsTiporevestimento().equalsIgnoreCase(tipoRevestimento.getDsTiporevestimento())){
					tipoRevestimento.setSelecionado(false);
					this.tipoRevestimentoSelecionados.remove(tipoRevestimento);
					break;
				}
			}

		}

		Html.atualizar("formFceCanaisDadosTrecho:checkBoxesTipoRevestimento");
	}

	/**
	 * Deve exibir na tela os trechos com a seção geometrica que estão no objeto 
	 * @return
	 * @throws Exception 
	 */
	private List<FceCanalTrechoSecaoGeometrica> buildFceCanalTrechosSecaoGeometrica() throws Exception{
		List<FceCanalTrechoSecaoGeometrica> trechosSecao = new ArrayList<FceCanalTrechoSecaoGeometrica>();
		boolean incluido = false;
		for(SecaoGeometrica secaoGeometrica : facade.listarSecoesGeometricas()){
			incluido = false;
			//verificar se existe a seção geografica no trecho
			if(this.getTrecho().getFceCanalTrechoSecaoGeometrica() != null && !this.getTrecho().getFceCanalTrechoSecaoGeometrica().isEmpty()){
				for(FceCanalTrechoSecaoGeometrica cfTrechoSecaoGeo : this.getTrecho().getFceCanalTrechoSecaoGeometrica()){
					if(cfTrechoSecaoGeo.getSecaoGeometrica().equals(secaoGeometrica)){
						cfTrechoSecaoGeo.getSecaoGeometrica().setSelecionado(true);
						trechosSecao.add(cfTrechoSecaoGeo);
						incluido = true;
						break;
					}
				}
			}
			if(!incluido){
				FceCanalTrechoSecaoGeometrica trechoSecao = new FceCanalTrechoSecaoGeometrica();
				trechoSecao.setSecaoGeometrica(secaoGeometrica);
				trechosSecao.add(trechoSecao);
			}
		}
		
		return trechosSecao;
	}

	private List<TipoRevestimento> buildTipoRevestimentos() throws Exception{
		List<TipoRevestimento> tList = new ArrayList<TipoRevestimento>();
		for(TipoRevestimento t : facade.listarTipoRevestimento()){
			if(this.getTrecho().getTiposRevestimentos() != null && !this.getTrecho().getTiposRevestimentos().isEmpty()){
				if(this.getTrecho().getTiposRevestimentos().contains(t)){
					t.setSelecionado(true);
				}
			}
			tList.add(t);
		}
		Collections.sort(tList);
		
		return tList;
	}
	
	public List<CaracteristicaCanal> getCaracteristicasCanal(FceCanal fceCanal) throws Exception{
		List<CaracteristicaCanal> listCaracteristicasCanal = new ArrayList<CaracteristicaCanal>();
		for(CaracteristicaCanal c : facade.listarCaracteristicasCanal()){
			if(!Util.isNullOuVazio(fceCanal) && fceCanal.getCaracteristicasCanal().contains(c)){
				c.setSelecionado(true);
			}
			listCaracteristicasCanal.add(c);
		}
		return listCaracteristicasCanal;
	}
	
	public void excluirTrecho() throws Exception {
		//#10226 - exclusao apenas do trecho selecionado, independente de ser trecho intermediario.
		this.getFceCanal().getCanalTrechos().remove(this.trecho);
		if(this.trecho != null && this.getTrecho().getIdFceCanalTrecho() != null){
			facade.removerFceCanalTrecho(this.trecho);
		}
		
		this.getBacias(this.fceCanal);
	}
	

	@Override
	protected void carregarFceTecnico() {
		super.carregarFceDoTecnico(DOCUMENTO_OBRIGATORIO);
		if(this.getFce() != null){
			try {
				this.setFceCanal(facade.getFceCanal(this.getFce()));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		
		carregarAba();
	}



	/**
	 * @return the fceCanalTiposFinalidadesUsoAguas
	 */
	public List<FceCanalTipoFinalidadeUsoAgua> getFceCanalTiposFinalidadesUsoAguas() {
		return fceCanalTiposFinalidadesUsoAguas;
	}

	public FceCanal getFceCanal() {
		return fceCanal;
	}

	public void setFceCanal(FceCanal fceCanal) {
		this.fceCanal = fceCanal;
	}

	public FceCanalTrecho getTrecho() {
		return trecho;
	}

	public void setTrecho(FceCanalTrecho trecho) {
		this.trecho = trecho;
	}
	
	public List<LocalizacaoGeografica> getTableLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica){
		List<LocalizacaoGeografica> data =  new ArrayList<LocalizacaoGeografica>();
		data.add(localizacaoGeografica);
		return data;
	}
	
	public List<FceCanal> getTableFceCanals(FceCanal fceCanal){
		List<FceCanal> data =  new ArrayList<FceCanal>();
		data.add(fceCanal);
		return data;
	}

	public List<TipoCanal> getTiposCanal() {
		return tiposCanal;
	}

	public void setTiposCanal(List<TipoCanal> tiposCanal) {
		this.tiposCanal = tiposCanal;
	}

	public void setFceCanalTrechosSecaoGeometrica(
			List<FceCanalTrechoSecaoGeometrica> fceCanalTrechosSecaoGeometrica) {
		this.fceCanalTrechosSecaoGeometrica = fceCanalTrechosSecaoGeometrica;
	}

	public List<FceCanalTrechoSecaoGeometrica> getFceCanalTrechosSecaoGeometrica() {
		return fceCanalTrechosSecaoGeometrica;
	}

	public List<TipoRevestimento> getTipoRevestimentos() {
		return tipoRevestimentos;
	}

	public void setTipoRevestimentos(List<TipoRevestimento> tipoRevestimentos) {
		this.tipoRevestimentos = tipoRevestimentos;
	}

	public List<TipoRio> getTiposRios() {
		return tiposRios;
	}

	public void setTiposRios(List<TipoRio> tiposRios) {
		this.tiposRios = tiposRios;
	}


	public List<String> getBacias(FceCanal fceCanal) {
		List<String> bacias = new ArrayList<String>();
		if(!Util.isNullOuVazio(fceCanal) && !CollectionUtils.isEmpty(fceCanal.getCanalTrechos())){
			for(FceCanalTrecho trecho : fceCanal.getCanalTrechos()){
				String baciaIni = localizacaoGeoServiceFacade.getBacia(trecho.getLocalizacaoGeograficaInicio());
				if(!Util.isNullOuVazio(baciaIni) && !bacias.contains(baciaIni)){
					bacias.add(baciaIni);
				}
				
				String baciaFim = localizacaoGeoServiceFacade.getBacia(trecho.getLocalizacaoGeograficaFim());
				if(!Util.isNullOuVazio(baciaFim) && !bacias.contains(baciaFim)){
					bacias.add(baciaFim);
				}
			}
		}
		return bacias;
	}
	
	public List<String> getRpga(FceCanal fceCanal) {
		List<String> rpgas = new ArrayList<String>();
		if(!Util.isNullOuVazio(fceCanal))
			if(!Util.isNullOuVazio(fceCanal.getCanalTrechos())){
				int size = fceCanal.getCanalTrechos().size();
				String rpgaIni = localizacaoGeoServiceFacade.getRPGA(fceCanal.getCanalTrechos().get(0).getLocalizacaoGeograficaInicio());
				if(!Util.isNullOuVazio(rpgaIni) && !"--".equals(rpgaIni)){
					rpgas.add(rpgaIni);
				}
				String rpgaFim = localizacaoGeoServiceFacade.getRPGA(fceCanal.getCanalTrechos().get(size -1).getLocalizacaoGeograficaFim());
				if(!rpgas.contains(rpgaFim) && !Util.isNullOuVazio(rpgaFim) && !"--".equals(rpgaFim)){
					rpgas.add(rpgaFim);
				}
			}
		return rpgas;
	}
	
	public List<Municipio> getMunicipiosTrechos() throws Exception{
		List<Municipio> municipios = new ArrayList<Municipio>();
		if(!Util.isNullOuVazio(fceCanal)){
			//Obtém os municípios os os trechos foram inseridos. Esses municípios não podem ser removidos na ação excluir no dataTable "listaMunicipioEnvolvido"
			if(!Util.isNullOuVazio(fceCanal.getCanalTrechos())){
				for(FceCanalTrecho t: fceCanal.getCanalTrechos()){
					List<Municipio> listMuniTrecho = facade.listarMunicipio(t.getLocalizacaoGeograficaInicio());
					listMuniTrecho.addAll(facade.listarMunicipio(t.getLocalizacaoGeograficaFim()));
					for(Municipio m: listMuniTrecho){
						if(!municipios.contains(m)){
							m.setChecked(true);
							m.setSelectable(false);
							municipios.add(m);
						}
					}
				}
			}
		}
		return municipios;
	}
	/**
	 * obtém os municipios envolvidos no FCE Canal. setChecked para exibir no componenet de adcionar Municípios
	 * @return
	 * @throws Exception
	 */
	public List<Municipio> getMunicipios() throws Exception {
		List<Municipio> municipios = new ArrayList<Municipio>();
		if(!Util.isNullOuVazio(fceCanal)){
			//Obtém os municípios os os trechos foram inseridos. Esses municípios não podem ser removidos na ação excluir no dataTable "listaMunicipioEnvolvido"
			if(!Util.isNullOuVazio(fceCanal.getCanalTrechos())){
				municipios.addAll(getMunicipiosTrechos());
				//Adiciona os municípios adcionados pelo usuário. Esses municípios podem ser removidos do dataTable "listaMunicipioEnvolvido"
				if(!Util.isNullOuVazio(fceCanal.getMunicipios())){
					for(Municipio m : fceCanal.getMunicipios()){
						m.setChecked(true);
						m.setSelectable(true);
						municipios.add(m);
					}
				}
			}
		}
		return municipios;
	}

	/**
	 * @return the editarTrecho
	 */
	public boolean isEditarTrecho() {
		return editarTrecho;
	}

	/**
	 * @param editarTrecho the editarTrecho to set
	 */
	public void setEditarTrecho(boolean editarTrecho) {
		this.editarTrecho = editarTrecho;
	}

	public boolean isPermitirSalvar() {
		return permitirSalvar;
	}

	public void setPermitirSalvar(boolean permitirSalvar) {
		this.permitirSalvar = permitirSalvar;
	}

	public String getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(String cameFrom) {
		this.cameFrom = cameFrom;
	}

	/**
	 * @return the editarVazao
	 */
	public boolean isEditarVazao() {
		return editarVazao;
	}

	/**
	 * @return the fceCanalTipoFinalidadeUsoAgua
	 */
	public FceCanalTipoFinalidadeUsoAgua getFceCanalTipoFinalidadeUsoAgua() {
		return fceCanalTipoFinalidadeUsoAgua;
	}

	/**
	 * @param fceCanalTipoFinalidadeUsoAgua the fceCanalTipoFinalidadeUsoAgua to set
	 */
	public void setFceCanalTipoFinalidadeUsoAgua(
			FceCanalTipoFinalidadeUsoAgua fceCanalTipoFinalidadeUsoAgua) {
		this.fceCanalTipoFinalidadeUsoAgua = null;
	}

	public void selecionarTipoUsoAgua(ValueChangeEvent event){
		FceCanalTipoFinalidadeUsoAgua fceCanalTipoFinalidadeUsoAguaObj = (FceCanalTipoFinalidadeUsoAgua) event.getNewValue();
		if(this.fceCanal != null && fceCanalTipoFinalidadeUsoAguaObj != null && !this.fceCanal.getFceCanalTiposFinalidadesUsoAgua().contains(fceCanalTipoFinalidadeUsoAguaObj)){
			this.fceCanal.getFceCanalTiposFinalidadesUsoAgua().add(fceCanalTipoFinalidadeUsoAguaObj);
		}
		this.fceCanalTipoFinalidadeUsoAgua = null; 
	}
	
	public void excluirTipoUsoAgua(FceCanalTipoFinalidadeUsoAgua fceCanalTipoFinalidadeUsoAgua){
		this.fceCanal.getFceCanalTiposFinalidadesUsoAgua().remove(fceCanalTipoFinalidadeUsoAgua);
	}
	
}