package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.EspecieFloresta;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.NaturezaEspecieFloresta;
import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelEspecieProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SituacaoAtualFlorestaProducao;
import br.gov.ba.seia.enumerator.AbaRegistroFlorestaPlantadaEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.SituacaoAtualFlorestaProducaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.rfp.Dialog;

@ViewScoped
@Named("registroFlorestaPlantadaController")
public class RFPController extends RFPBaseController implements Serializable {
	private static final long serialVersionUID = 1L;

	private AbaRegistroFlorestaPlantadaEnum abaAtiva;

	private Calendar dataUltimoCorte;
	private List<Imovel> imovelList; 
	private Imovel imovelPesquisa;
	private Requerimento requerimento;
	
	private EspecieFloresta  especieFlorestaAuxiliar;
	private List<SituacaoAtualFlorestaProducao> situacaoAtualFlorestaProducaoList;
	private List<NaturezaEspecieFloresta> naturezaEspecieFlorestaList;
	private List<EspecieFloresta> especieFlorestaList;
	private LocalizacaoGeografica localizacaoGeografica;
	private boolean disableEtapaDois;
	private boolean disableEtapaTres;
	
	public void init(){
		inicializarVariaveis();
		carregarListas();
		carregar();
		carregarImoveisArrendados();
	}
	
	public void inicializarVariaveis(){
		setIdeRegistroFlorestaProducao(new RegistroFlorestaProducao());
		getIdeRegistroFlorestaProducao().setIdeAtoDeclaratorio(new AtoDeclaratorio(requerimento,DocumentoObrigatorioEnum.FORMULARIO_RFP));
		this.especieFlorestaAuxiliar = new EspecieFloresta();
		super.ideRegistroFlorestaProducaoImovelEspecieProducao = new RegistroFlorestaProducaoImovelEspecieProducao();
		super.ideRegistroFlorestaProducaoImovelEspecieProducao.setIdeEspecieFloresta(new EspecieFloresta());
		super.ideRegistroFlorestaProducaoImovelEspecieProducao.getIdeEspecieFloresta().setIdeNaturezaEspecieFloresta(new NaturezaEspecieFloresta());
		
		abaAtiva = AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE;
		disableEtapaDois = true;
		disableEtapaTres = true;
		Html.atualizar("tabRegistroFlorestaPlantada", "btnAuxiliares");
	}

	public void carregarListas(){
		if(getNaturezaEspecieFlorestaList()==null){
			setNaturezaEspecieFlorestaList(getFacade().listarNaturezaEspecieFloresta());
		}
		if(getEspecieFlorestaList()==null){
			setEspecieFlorestaList(getFacade().listarEspecieFloresta());
		}
		if(getSituacaoAtualFlorestaProducaoList()==null){
			setSituacaoAtualFlorestaProducaoList(getFacade().listarSituacaoAtualFlorestaProducao());
		}
	}
	
	public void carregar(){
		RegistroFlorestaProducao rfp = facade.carregarRegistroFlorestaProducao(getIdeRegistroFlorestaProducao());
		if(!Util.isNullOuVazio(rfp.getRegistroFlorestaProducaoImovelList())) {
			disableEtapaDois = false;
		}
		if(!Util.isNullOuVazio(rfp.getIndAceiteResponsabilidade()) && rfp.getIndAceiteResponsabilidade()) {
			disableEtapaTres = false;
		}
		setIdeRegistroFlorestaProducao(rfp);
	}
	
	public void concluir(){
		facade.salvarRegistroFlorestaProducao(ideRegistroFlorestaProducao, abaAtiva);
	}

	/**
	 * Controle da tela 
	 *
	 **/
	public void avancar(){
		if(validarAba(abaAtiva)){
			facade.salvarRegistroFlorestaProducao(getIdeRegistroFlorestaProducao(), abaAtiva);
			
			if(abaAtiva==AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE || abaAtiva==AbaRegistroFlorestaPlantadaEnum.ABA_REGISTRO_FLORESTA_PLANTADA){
				avancarAbas();
			}
			else if (abaAtiva==AbaRegistroFlorestaPlantadaEnum.ABA_RESPONSABILIDADE){
				Html.esconder(Dialog.dialogRfpFlorestaProducaoPlantada);
			}
		}
	}
	
	public void carregarEspecieProducao(){
		if(!Util.isNullOuVazio(this.especieFlorestaAuxiliar.getIdeNaturezaEspecieFloresta())){
			this.especieFlorestaList = facade.listarEspecieFloresta(this.especieFlorestaAuxiliar.getIdeNaturezaEspecieFloresta());
		}
	}
	
	public void carregarListaEspecie(){
	
		if(!Util.isNullOuVazio(super.ideRegistroFlorestaProducaoImovelEspecieProducao.getIdeEspecieFloresta().getIdeNaturezaEspecieFloresta())){
			setEspecieFlorestaList(getFacade().listarEspecieFloresta(super.ideRegistroFlorestaProducaoImovelEspecieProducao.getIdeEspecieFloresta().getIdeNaturezaEspecieFloresta()));
		}else{
			super.ideRegistroFlorestaProducaoImovelEspecieProducao.setIdeEspecieFloresta(new EspecieFloresta());
			super.ideRegistroFlorestaProducaoImovelEspecieProducao.getIdeEspecieFloresta().setIdeNaturezaEspecieFloresta(new NaturezaEspecieFloresta());
			setEspecieFlorestaList(getFacade().listarEspecieFloresta());
		}	
		
		Html.atualizar("formEspecieProducao:listaEspecie");
	
	}
	
	private boolean validarAba(AbaRegistroFlorestaPlantadaEnum abaRegistroFlorestaPlantadaEnum){
		if(abaRegistroFlorestaPlantadaEnum == AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE){
			if((getIdeRegistroFlorestaProducao()==null) ||  
			   (getIdeRegistroFlorestaProducao().getIndCienteTermoCompromisso()==null) || !getIdeRegistroFlorestaProducao().getIndCienteTermoCompromisso()){
				MensagemUtil.msg0003("Ciente");
				return false;
			}
		}
		
		else if (abaRegistroFlorestaPlantadaEnum == AbaRegistroFlorestaPlantadaEnum.ABA_REGISTRO_FLORESTA_PLANTADA){
			if(!florestaProducao.validar(getIdeRegistroFlorestaProducao().getRegistroFlorestaProducaoImovelList())){
				return false;
			}
			if(getIdeRegistroFlorestaProducao().getDtPrevistaUltimoCorte()==null){
				MensagemUtil.msg0003("Data prevista para o Último corte");
				return false;
			}
			if(!responsavelTecnico.validar(getIdeRegistroFlorestaProducao().getRegistroFlorestaProducaoResponsavelTecnicoList())){
				return false;
			}
		}
		
		else if (abaRegistroFlorestaPlantadaEnum == AbaRegistroFlorestaPlantadaEnum.ABA_RESPONSABILIDADE){
			if(getIdeRegistroFlorestaProducao().getIndAceiteResponsabilidade()==null || !getIdeRegistroFlorestaProducao().getIndAceiteResponsabilidade()){
				MensagemUtil.msg0003("Ciente");
				return false;
			}
		}
		
		return true;
	}

	public void atualizarCamposTela(){
		for (RegistroFlorestaProducaoImovel imovel : getRegistroFlorestaProducaoImovelList()) {
			imovel.atualizarAreasPlantio();
		}
		Html.atualizar("tabRegistroFlorestaPlantada:formAbaRegistroFlorestaPlantada");
	}
	
	public void onTabChange(TabChangeEvent event){ 
		abaAtiva = AbaRegistroFlorestaPlantadaEnum.getEnum(event.getTab().getId());
		Html.atualizar("tabRegistroFlorestaPlantada","btnAuxiliares");
	}

	public void avancarAbas(){
		if(abaAtiva==AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE){
			disableEtapaDois = false;
		}if(abaAtiva==AbaRegistroFlorestaPlantadaEnum.ABA_REGISTRO_FLORESTA_PLANTADA){
			disableEtapaTres = false;
		}		
		abaAtiva = AbaRegistroFlorestaPlantadaEnum.avancar(abaAtiva);
		Html.atualizar("tabRegistroFlorestaPlantada","btnAuxiliares");	
		MensagemUtil.msg0010();
	}
	
	public void voltar(){
		abaAtiva = AbaRegistroFlorestaPlantadaEnum.voltar(abaAtiva);
		Html.atualizar("tabRegistroFlorestaPlantada","btnAuxiliares");
	}
	
	public void aceiteTermoCompromisso(ValueChangeEvent event){
		ideRegistroFlorestaProducao.setIndCienteTermoCompromisso((Boolean) event.getNewValue());
	}
	
	public boolean getBloquearAbas(){
		if(Util.isNullOuVazio(ideRegistroFlorestaProducao) || 
		  (Util.isNullOuVazio(ideRegistroFlorestaProducao.getIndCienteTermoCompromisso()) || !ideRegistroFlorestaProducao.getIndCienteTermoCompromisso())){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Registro floresta de produção 
	 *
	 **/
	public BigDecimal getSomaAreas(){

		BigDecimal valTotalAreas = BigDecimal.ZERO;
		
		for(RegistroFlorestaProducaoImovel imovel: getRegistroFlorestaProducaoImovelList()){
			if(imovel.getRegistroFlorestaProducaoImovelPlantioList()!=null){
				for (RegistroFlorestaProducaoImovelPlantio plantio : imovel.getRegistroFlorestaProducaoImovelPlantioList()) {
					if(plantio.getValAreaPlantio()!=null){
						valTotalAreas = valTotalAreas.add(plantio.getValAreaPlantio());
					}
				}
			}
			
		}
		
		
		return valTotalAreas;
	}
	
	public Date getDataAtual(){
		 return new Date();
	}
	
	public void carregarImoveisArrendados(){
		if(imovelList==null){
			imovelList = new ArrayList<Imovel>();
		}

		for (RegistroFlorestaProducaoImovel registroFlorestaProducaoImovel : getRegistroFlorestaProducaoImovelList()) {
			if(registroFlorestaProducaoImovel.getIdeImovel()!=null){
				registroFlorestaProducaoImovel.setIdeImovel(facade.carregarNomeImovel(registroFlorestaProducaoImovel.getIdeImovel(), requerimento));

				if(!imovelList.contains(registroFlorestaProducaoImovel.getIdeImovel())){
					imovelList.add(registroFlorestaProducaoImovel.getIdeImovel());
				}
				
			}
			
		}
	}
	
	/**
	 *
	 * Floresta de produção
	 **/
	
	public void adicionarFlorestaProducao(){
		florestaProducao.adicionar(this);
	}

	public void exibirFlorestaProducao(){
		florestaProducao.exibir(this);
	}
	
	public void excluirFlorestaProducao(){
		florestaProducao.excluir(this);
		atualizarCamposTela();
	}
	
	/**
	 *
	 * Imovel
	 */
	
	public void exibirImovel(){
		imovel.exibir(this);
	}
	
	public void carregarImovel(){
		imovel.carregar(this);
	}
	
	public void adicionarImovel(){
		imovel.adicionar(this);
	}
	
	
		
	/**
	 * 
	 * Plantio
	 * */
	public void exibirPlantio(){
		dadosPlantio.exibir(this);
	}
	
	public void adicionarPlantio(){
		dadosPlantio.adicionar(this);
	}

	public void excluirPlantio(){
		dadosPlantio.excluir(this);
	}
	
	public String situacaoAtual(RegistroFlorestaProducaoImovel rfpi){
		
		if(rfpi!=null &&
		   rfpi.getIdeRegistroFlorestaProducaoImovelPlantio()!=null && 
		   rfpi.getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao()!=null &&
		   rfpi.getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getDesSituacaoFlorestaProducao()!=null){
			return rfpi.getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getDesSituacaoFlorestaProducao();
		}
		
		if(rfpi.getRegistroFlorestaProducaoImovelPlantioList()!=null){
			for (RegistroFlorestaProducaoImovelPlantio lista : rfpi.getRegistroFlorestaProducaoImovelPlantioList()) {
				if(lista.getIdeSituacaoAtualFlorestaProducao()!=null && lista.getIdeSituacaoAtualFlorestaProducao().getDesSituacaoFlorestaProducao()!=null){
					return lista.getIdeSituacaoAtualFlorestaProducao().getDesSituacaoFlorestaProducao();
				}
			}
		}
		
		return " - ";
	}
	
	/**
	 * Especie producão
	 * */
	public void exibirEspecieProducao(){
		this.especieFlorestaAuxiliar = new EspecieFloresta();
		especieProducao.exibir(this);	
	}
	
	public void adicionarEspecieProducao(){
		
		this.especieFlorestaAuxiliar.setIdeNaturezaEspecieFloresta(new NaturezaEspecieFloresta());
		this.especieFlorestaAuxiliar.setIdeNaturezaEspecieFloresta(this.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getIdeEspecieFloresta().getIdeNaturezaEspecieFloresta());
		this.getIdeRegistroFlorestaProducaoImovelEspecieProducao().setIdeEspecieFloresta(this.especieFlorestaAuxiliar);
		especieProducao.adicionar(this);
	}
	
	public void excluirEspecieProducao(){
		especieProducao.excluir(this);
	}	
	
	public boolean isDesabiltarEnvioSituacao(){

		if(getIdeRegistroFlorestaProducao().getIdeRegistroFlorestaProducaoImovel().isVisualizar()){
			return true;
		}
		
		if(getIdeRegistroFlorestaProducao().getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList()==null){
			return false;
		}
		
		
		if(getIdeRegistroFlorestaProducao().getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList().size()>0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * ResponsavelTecnico
	 **/
	public void exibirResponsavelTecnico(){
		responsavelTecnico.exibir(this);
	}
	
	public void carregarResponsavelTecnico(){
		responsavelTecnico.carregar(this);
	}
	
	public void adicionarResponsavelTecnico(){
		responsavelTecnico.adicionar(this);
	}
	
	public void excluirResponsavelTecnico(){
		responsavelTecnico.excluir(this);
	}
	
	public void valueChangeSituacao(ValueChangeEvent event){	
		
		getIdeRegistroFlorestaProducaoImovelPlantio().setIdeSituacaoAtualFlorestaProducao((SituacaoAtualFlorestaProducao) event.getNewValue());
		
		if(getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao()==SituacaoAtualFlorestaProducaoEnum.EM_IMPLANTACAO.getId() ||
		   getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao()==SituacaoAtualFlorestaProducaoEnum.EM_PROJETO.getId()){
		
			getIdeRegistroFlorestaProducaoImovelPlantio().setDtInicioPrevistaImplantacao(null);
			getIdeRegistroFlorestaProducaoImovelPlantio().setDtFimPrevistaImplantacao(null);
		}
		
		else if (getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao()==SituacaoAtualFlorestaProducaoEnum.TOTALMENTE_IMPLANTADA.getId()){
			getIdeRegistroFlorestaProducaoImovelPlantio().setDtInicioPeriodoImplantacao(null);
			getIdeRegistroFlorestaProducaoImovelPlantio().setDtFimPeriodoImplantacao(null);
		}
	}
	
	
	/**
	 * Renders 
	 * */
	public boolean isVisivelAvancar(){
		return !(abaAtiva == AbaRegistroFlorestaPlantadaEnum.ABA_RESPONSABILIDADE);
	}

	public boolean isVisivelVoltar(){
		return!(abaAtiva == AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE);
	}
	
	public boolean isVisivelConcluir(){
		return (abaAtiva == AbaRegistroFlorestaPlantadaEnum.ABA_RESPONSABILIDADE);
	}
	
	public boolean isVisivelPeriodoPrevisto(){
		
		if(getIdeRegistroFlorestaProducaoImovelPlantio() == null || getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao() == null){
			return false;
		}

		return 
			getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao().equals(SituacaoAtualFlorestaProducaoEnum.EM_PROJETO.getId()) ||
			getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao().equals(SituacaoAtualFlorestaProducaoEnum.EM_IMPLANTACAO.getId());
	}
	
	public boolean isVisivelPeriodoImplantacao(){
		
		if(getIdeRegistroFlorestaProducaoImovelPlantio() == null || getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao() == null){
		   return false;
		}
				
		return 
			getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao().equals(SituacaoAtualFlorestaProducaoEnum.TOTALMENTE_IMPLANTADA.getId());
	}
	
	public Imovel getImovelPesquisa(){
		if(imovelPesquisa==null){
			imovelPesquisa = new Imovel();
		}
		
		return imovelPesquisa;
	}

	public List<Imovel> getImovelList() {
		if(imovelList == null){
			imovelList = new ArrayList<Imovel>();
		}
		return imovelList;
	}
	
	public boolean isImovelListPossuiRuralOuUrbano() {
		if(!Util.isNullOuVazio(imovelList)) {
			for (Imovel imv : imovelList) {
				if(!Util.isNullOuVazio(imv.getImovelRural())) return true;
				if(!Util.isNullOuVazio(imv.getImovelUrbano())) return true;
			}
		}
		
		return false;
	}

	
	public String getNumSicar(){
		
		if(getImovelPesquisa().getImovelRural()==null){
			getImovelPesquisa().setImovelRural(new ImovelRural());
		}
		
		if(getImovelPesquisa().getImovelRural().getImovelRuralSicar()==null){
			getImovelPesquisa().getImovelRural().setImovelRuralSicar(new ImovelRuralSicar());
		}
		
		return getImovelPesquisa().getImovelRural().getImovelRuralSicar().getNumSicar();
	}
	
	public void setNumSicar(String numCar){
		getImovelPesquisa().getImovelRural().getImovelRuralSicar().setNumSicar(numCar);
	}
	
	public void setImovelRural(ImovelRural imovelRural){
		getImovelPesquisa().setImovelRural(imovelRural);
	}
	
	
	public AbaRegistroFlorestaPlantadaEnum getAbaAtiva() {
		if(abaAtiva==null){
			return (AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE);
		}
		
		return abaAtiva;
	}
	
	public StreamedContent getImprimirRelatorio(){
		try {
			return new ImpressoraAtoDeclaratorio().imprimirRelatorio(getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	/**
	 * Gets e Sets
	 * */
	
	public void setImovelList(List<Imovel> imovelList) {
		this.imovelList = imovelList;
	}

	public List<SituacaoAtualFlorestaProducao> getSituacaoAtualFlorestaProducaoList() {
		return situacaoAtualFlorestaProducaoList;
	}

	public void setSituacaoAtualFlorestaProducaoList(List<SituacaoAtualFlorestaProducao> situacaoAtualFlorestaProducaoList) {
		this.situacaoAtualFlorestaProducaoList = situacaoAtualFlorestaProducaoList;
	}

	public Calendar getDataUltimoCorte() {
		return dataUltimoCorte;
	}

	public void setDataUltimoCorte(Calendar dataUltimoCorte) {
		this.dataUltimoCorte = dataUltimoCorte;
	}

	public RegistroFlorestaProducao getRegistroFlorestaProducao() {
		return ideRegistroFlorestaProducao;
	}

	public void setRegistroFlorestaProducao(RegistroFlorestaProducao registroFlorestaProducao) {
		this.ideRegistroFlorestaProducao = registroFlorestaProducao;
	}

	public List<NaturezaEspecieFloresta> getNaturezaEspecieFlorestaList() {
		return naturezaEspecieFlorestaList;
	}

	public void setNaturezaEspecieFlorestaList(List<NaturezaEspecieFloresta> naturezaEspecieFlorestaList) {
		this.naturezaEspecieFlorestaList = naturezaEspecieFlorestaList;
	}

	public List<EspecieFloresta> getEspecieFlorestaList() {
		return especieFlorestaList;
	}

	public void setEspecieFlorestaList(List<EspecieFloresta> especieFlorestaList) {
		this.especieFlorestaList = especieFlorestaList;
	}

	public void setImovelPesquisa(Imovel imovelPesquisa) {
		this.imovelPesquisa = imovelPesquisa;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public EspecieFloresta getEspecieFlorestaAuxiliar() {
		return especieFlorestaAuxiliar;
	}

	public void setEspecieFlorestaAuxiliar(EspecieFloresta especieFlorestaAuxiliar) {
		this.especieFlorestaAuxiliar = especieFlorestaAuxiliar;
	}

	public boolean isDisableEtapaDois() {
		return disableEtapaDois;
	}

	public void setDisableEtapaDois(boolean disableEtapaDois) {
		this.disableEtapaDois = disableEtapaDois;
	}

	public boolean isDisableEtapaTres() {
		return disableEtapaTres;
	}

	public void setDisableEtapaTres(boolean disableEtapaTres) {
		this.disableEtapaTres = disableEtapaTres;
	}
	
}