package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.CaracteristcaAtividadeIntervencaoAppDTO;
import br.gov.ba.seia.entity.AtividadeIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoAppCaracteristca;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.facade.DeclaracaoIntervencaoAppFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * @author eduardo.fernandes 
 * @since 10/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Named("declaracaoIntervencaoAppController")
@ViewScoped
public class DeclaracaoIntervencaoAppController {

	@EJB
	private DeclaracaoIntervencaoAppFacade facade;
	
	private static final int ETAPA_UM = 0;
	private static final int ETAPA_DOIS = 1;
	private static final int ETAPA_TRES = 2;
	
	private int activeTab;
	private boolean portariaLida;
	private DeclaracaoIntervencaoApp declaracaoIntervencaoApp;
	private Requerimento requerimento;
	private List<LocalizacaoGeografica> listaLocGeo;
	private List<CaracteristcaAtividadeIntervencaoAppDTO> listaCaracteristicaDTO;
	private CaracteristcaAtividadeIntervencaoAppDTO dto;
	
	public void init() {
		limpar();
		
		declaracaoIntervencaoApp = carregarDiap();
		
		montarDTO();
		
		if(isEdicao()){
			setPortariaLida();
		}
	}
	
	public DeclaracaoIntervencaoApp carregarDiap(){
		try {
			return facade.buscarDiap(requerimento);
		} 
		catch (Exception e) {
			MensagemUtil.erro008();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * 
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 */
	private void montarDTO() {
		listaCaracteristicaDTO = facade.prepararDTO(declaracaoIntervencaoApp);
		if(isEdicao()){
			for (DeclaracaoIntervencaoAppCaracteristca diac : declaracaoIntervencaoApp.getDeclaracaoIntervencaoAppCaracteristcas()) {
				CaracteristcaAtividadeIntervencaoAppDTO dto = obterDeclaracaoIntervencaoAppCaracteristcaFromDTO(diac);
				dto.setSelecionado(true);
				dto.setDesCaminhoArquivoDecreto(diac.getDesCaminhoArquivoDecreto());
				for(AtividadeIntervencaoApp aia : dto.getListaAtividade()){
					if(diac.getCaracteristicaAtividadeIntervencaoApp().getAtividadeIntervencaoApp().equals(aia)){
						aia.setSelecionado(true);
						dto.changetAtividade(aia);
					}
				}
			}
			declaracaoIntervencaoApp.setDeclaracaoIntervencaoAppCaracteristcas(new ArrayList<DeclaracaoIntervencaoAppCaracteristca>());
		}
	}
	
	
	
	private CaracteristcaAtividadeIntervencaoAppDTO obterDeclaracaoIntervencaoAppCaracteristcaFromDTO(DeclaracaoIntervencaoAppCaracteristca diac){
		for (CaracteristcaAtividadeIntervencaoAppDTO dto : listaCaracteristicaDTO) {
			if(dto.getCaracteristica().equals(diac.getCaracteristicaAtividadeIntervencaoApp().getCaracteristicaIntervencaoApp())){
				return dto;
			}
		}
		return null;
	}

	public void valueChangeCiente(ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			
		}
	}
	
	public void limpar(){
		activeTab = 0;
		portariaLida = false;
		declaracaoIntervencaoApp = null;
		listaLocGeo = null;
		listaCaracteristicaDTO = null;
		dto = null;
	}

	public void controlarAbas(TabChangeEvent event){
		if("tabEtapaUm".equals(event.getTab().getId())){
			activeTab = ETAPA_UM;
		} 
		else if("tabEtapaDois".equals(event.getTab().getId())){
			activeTab = ETAPA_DOIS;
		} 
		else if("tabEtapaTres".equals(event.getTab().getId())){
			activeTab = ETAPA_TRES;
		}
	}
	
	public void avancar(){
		if(isEtapaUm() && (Util.isNull(declaracaoIntervencaoApp.getIndCientePortaria()) || !declaracaoIntervencaoApp.getIndCientePortaria())){
			MensagemUtil.msg0003("O ciente ");
		} 
		else {
			activeTab++;
		}
	}
	
	public void voltar(){
		activeTab--;
	}
	
	public void concluir(){
		if(Util.isNull(declaracaoIntervencaoApp.getIndAceiteResponsabilidade()) || !declaracaoIntervencaoApp.getIndAceiteResponsabilidade()){
			MensagemUtil.msg0003("O ciente ");
		} 
		else if(validar()){
			facade.concluir(this);
			MensagemUtil.msg0010();
			Html.esconder("dialogDiap");
			Html.exibir("relatorioDiap");
		} 
		else {
			activeTab = ETAPA_DOIS;
			Html.atualizar("tabDiap");
		}
	}
	
	public void salvar(){
		MensagemUtil.msg0010();
		declaracaoIntervencaoApp.setIndAceiteResponsabilidade(null);
		if(Util.isNullOuVazio(declaracaoIntervencaoApp.getIdeLocalizacaoGeografica())){
			declaracaoIntervencaoApp.setIdeLocalizacaoGeografica(null);
		}
		facade.salvar(this);
		if(Util.isNull(declaracaoIntervencaoApp.getIdeLocalizacaoGeografica())){
			declaracaoIntervencaoApp.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
	}

	private boolean validar(){
		boolean valido = true;
		
		if(!isLocalizacaoGeograficaSalva()){
			valido = false;
			MensagemUtil.msg0003("A localização geográfica da intervenção");
		}
	
		if(!validarDTO()){
			valido = false;
		}
		
		if(Util.isNullOuVazio(declaracaoIntervencaoApp.getDesObjetivoIntervencaoApp())){
			valido = false;
			MensagemUtil.msg0003("O objetivo da intervenção");
		}
		
		return valido;
	}

	private boolean validarDTO() {
		boolean isIntervencaoSelecionada = false;
		boolean isAtividadeSelecionada = false;
		boolean isUploadRealizado = true;
		for (CaracteristcaAtividadeIntervencaoAppDTO dto : listaCaracteristicaDTO) {		
			if(dto.isSelecionado()){
				isIntervencaoSelecionada = true;
				for(AtividadeIntervencaoApp intervencaoApp : dto.getListaAtividade()){
					if(intervencaoApp.isSelecionado()){
						isAtividadeSelecionada = true;
						if(intervencaoApp.isOutrasAtividades() && Util.isNullOuVazio(dto.getArquivo())){
							isUploadRealizado = false;
							break;
						}
					}
				}
			}
		}
		if(!isIntervencaoSelecionada){
			MensagemUtil.msg0003("A característica da intervenção");
		} 
		else if(!isAtividadeSelecionada){
			MensagemUtil.msg0003("A atividade da intervenção");
		}
		if(!isUploadRealizado){
			MensagemUtil.msg0003("O upload do Decreto");
		}
		
		if(!isIntervencaoSelecionada || !isAtividadeSelecionada || !isUploadRealizado){
			return false;
		} 
		else {
			return true;
		}
	}
	
	public void excluirLocalizacaoGeografica(){
		try {
			LocalizacaoGeografica lg = declaracaoIntervencaoApp.getIdeLocalizacaoGeografica();
			
			atualizarLocalizacaoGeograficaDiap();
			
			facade.excluirLocalizacaoGeografica(lg);
			
			listaLocGeo = null;
			MensagemUtil.msg0005();
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a localização geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void atualizarLocalizacaoGeograficaDiap() throws Exception {
		declaracaoIntervencaoApp.setIdeLocalizacaoGeografica(null);
		if(isEdicao()){
			facade.salvarDIAP(declaracaoIntervencaoApp);
			declaracaoIntervencaoApp.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
	}
	
	public void deletarDecreto(){
		facade.deletarArquivo(dto.getDesCaminhoArquivoDecreto());
		dto.limparUploadDecreto();
	}

	
	public StreamedContent getDecreto(String dscCaminho){
		try {
			return facade.getDocumentoAdicionalUpado(dscCaminho);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); /** ADICIONAR MSG DE ERRO*/
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	public StreamedContent getImprimirRelatorio(){
		try {
			return new ImpressoraAtoDeclaratorio().imprimirRelatorio(declaracaoIntervencaoApp.getIdeAtoDeclaratorio());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível gerar o relatório.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	/*
	 * flags
	 */
	public boolean isEtapaUm(){
		return activeTab == ETAPA_UM;
	}
	
	public boolean isEtapaDois(){
		return activeTab == ETAPA_DOIS;
	}
	
	public boolean isEtapaTres(){
		return activeTab == ETAPA_TRES;
	}
	
	public boolean isPortariaLida(){
		return this.portariaLida;
	}
	
	public boolean isLocalizacaoGeograficaSalva(){
		try {
			return facade.isLocalizacaoGeograficaSalva(declaracaoIntervencaoApp.getIdeLocalizacaoGeografica());
		} catch (Exception e) {
			//  verificar com DANILO a MSG
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da localização geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	/**
	 * 
	 * 
	 * @author eduardo.fernandes 
	 * @since 12/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @return
	 */
	private boolean isEdicao() {
		return !Util.isNullOuVazio(declaracaoIntervencaoApp);
	}
	
	public boolean uploadRealizado(CaracteristcaAtividadeIntervencaoAppDTO caracteristicaDto) {
		return caracteristicaDto.isUploadRealizado();
	}
	
	/*
	 * getters & setters
	 */
	public int getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	public String getVisualizarLocalizacao() {
		if(isLocalizacaoGeograficaSalva()){
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(declaracaoIntervencaoApp.getIdeLocalizacaoGeografica());
		}
		return "";
	}
	
	public DeclaracaoIntervencaoApp getDeclaracaoIntervencaoApp() {
		return declaracaoIntervencaoApp;
	}

	public void setDeclaracaoIntervencaoApp(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		this.declaracaoIntervencaoApp = declaracaoIntervencaoApp;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public void setPortariaLida() {
		this.portariaLida = true;
	}

	public List<LocalizacaoGeografica> getListaLocGeo() {
		if(!Util.isNullOuVazio(declaracaoIntervencaoApp.getIdeLocalizacaoGeografica())){
			if(Util.isNull(listaLocGeo)){
				listaLocGeo = new ArrayList<LocalizacaoGeografica>();
				listaLocGeo.add(declaracaoIntervencaoApp.getIdeLocalizacaoGeografica());
			}
		} 
		else {
			listaLocGeo = null;
		}
		return listaLocGeo;
	}

	public List<CaracteristcaAtividadeIntervencaoAppDTO> getListaCaracteristicaDTO() {
		return listaCaracteristicaDTO;
	}

	public CaracteristcaAtividadeIntervencaoAppDTO getDto() {
		return dto;
	}

	public void setDto(CaracteristcaAtividadeIntervencaoAppDTO dto) {
		this.dto = dto;
	}
	
}