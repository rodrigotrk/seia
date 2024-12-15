package br.gov.ba.seia.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.AtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoAppCaracteristca;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
public class CaracteristcaAtividadeIntervencaoAppDTO {
	
	private boolean selecionado;
	private DeclaracaoIntervencaoApp diap;
	private List<DeclaracaoIntervencaoAppCaracteristca> listaDeclaracaoIntervencaoAppCaracteristca; 
	
	private CaracteristicaIntervencaoApp caracteristica;
	private List<AtividadeIntervencaoApp> listaAtividade;
	
	private List<String> arquivo;
	private String desCaminhoArquivoDecreto;
	
	public void prepararUpload(FileUploadEvent event) {
		String caminho = DiretorioArquivoEnum.DIAP.toString() + File.separator +  diap.getIdeAtoDeclaratorio().getIdeRequerimento().getIdeRequerimento();
		desCaminhoArquivoDecreto = FileUploadUtil.Enviar(event, caminho);
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
	}
	
	public void changeIntervencao(ValueChangeEvent event){
		if (!(Boolean) event.getNewValue()) {
			for(AtividadeIntervencaoApp atividade : listaAtividade){
				atividade.setSelecionado(false);
			}
			listaDeclaracaoIntervencaoAppCaracteristca.clear();
		}
	}
	
	public void changetAtividade(AjaxBehaviorEvent event){
		AtividadeIntervencaoApp atividade = (AtividadeIntervencaoApp) event.getComponent().getAttributes().get("atividade");			
		changetAtividade(atividade);
		atualizarDecreto(event);
	}

	public void changetAtividade(AtividadeIntervencaoApp atividade) {
		if(atividade.isSelecionado()){
			listaDeclaracaoIntervencaoAppCaracteristca.add(new DeclaracaoIntervencaoAppCaracteristca(diap, caracteristica, atividade));
		} 
		else {
			if(atividade.isOutrasAtividades()) {
				limparUploadDecreto();
			}
			listaDeclaracaoIntervencaoAppCaracteristca.remove(obterDeclaracaoIntervencaoAppCaracteristcaBy(atividade));
		}
	}
	// tabDiap:formDiapEtapaDois:j_idt204:1:gridUpDecreto
	private void atualizarDecreto(AjaxBehaviorEvent event){
		String id = event.getComponent().getId();
		String clientId = event.getComponent().getClientId();
		int inicio = clientId.indexOf("tableDtoAtividade");
		int fim = clientId.indexOf(id);
		String toRemove = clientId.substring(inicio, fim);
		RequestContext.getCurrentInstance().addPartialUpdateTarget(clientId.replace(":"+toRemove+id, ":gridUpDecreto"));
	}
	
	public String getLabel() {
		return Util.getString("diap_lbl_realize_upload", caracteristica.getNomCaracteristicaIntervencao());
	}
	
	private DeclaracaoIntervencaoAppCaracteristca obterDeclaracaoIntervencaoAppCaracteristcaBy(AtividadeIntervencaoApp atividade){
		for(DeclaracaoIntervencaoAppCaracteristca diapc : listaDeclaracaoIntervencaoAppCaracteristca){
			if(diapc.getCaracteristicaAtividadeIntervencaoApp().getCaracteristicaIntervencaoApp().equals(caracteristica) 
					&& diapc.getCaracteristicaAtividadeIntervencaoApp().getAtividadeIntervencaoApp().equals(atividade)){
				return diapc;
			}
		}
		return new DeclaracaoIntervencaoAppCaracteristca();
	}

	public void limparUploadDecreto() {
		desCaminhoArquivoDecreto = null;	
		arquivo = null;
	}
	
	public CaracteristcaAtividadeIntervencaoAppDTO(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		this.diap = declaracaoIntervencaoApp;
		this.listaDeclaracaoIntervencaoAppCaracteristca = new ArrayList<DeclaracaoIntervencaoAppCaracteristca>();
	}
	
	public boolean isSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public CaracteristicaIntervencaoApp getCaracteristica() {
		return caracteristica;
	}
	
	public void setCaracteristica(CaracteristicaIntervencaoApp caracteristica) {
		this.caracteristica = caracteristica;
	}
	
	public List<AtividadeIntervencaoApp> getListaAtividade() {
		return listaAtividade;
	}
	
	public void setListaAtividade(List<AtividadeIntervencaoApp> listaAtividade) {
		this.listaAtividade = listaAtividade;
	}

	public boolean isContemOutrasAtividade(){
		if(!Util.isNullOuVazio(listaDeclaracaoIntervencaoAppCaracteristca)){
			for(DeclaracaoIntervencaoAppCaracteristca diac : listaDeclaracaoIntervencaoAppCaracteristca){
				if(diac.getCaracteristicaAtividadeIntervencaoApp().getAtividadeIntervencaoApp().isSelecionado() && diac.getCaracteristicaAtividadeIntervencaoApp().isOutrasAtividades()){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isUploadRealizado(){
		return !Util.isNullOuVazio(getArquivo());
	}
	
	public List<String> getArquivo() {
		if(Util.isNull(arquivo) && !Util.isNullOuVazio(desCaminhoArquivoDecreto)){
			arquivo = new ArrayList<String>();
			arquivo.add(desCaminhoArquivoDecreto);
		}
		return arquivo;
	}

	public List<DeclaracaoIntervencaoAppCaracteristca> getListaDeclaracaoIntervencaoAppCaracteristca() {
		return listaDeclaracaoIntervencaoAppCaracteristca;
	}

	public void setListaDeclaracaoIntervencaoAppCaracteristca(List<DeclaracaoIntervencaoAppCaracteristca> listaDeclaracaoIntervencaoAppCaracteristca) {
		this.listaDeclaracaoIntervencaoAppCaracteristca = listaDeclaracaoIntervencaoAppCaracteristca;
	}

	public String getFileName(){
		return FileUploadUtil.getFileName(desCaminhoArquivoDecreto);
	}

	public String getDesCaminhoArquivoDecreto() {
		return desCaminhoArquivoDecreto;
	}

	public void setDesCaminhoArquivoDecreto(String desCaminhoArquivoDecreto) {
		this.desCaminhoArquivoDecreto = desCaminhoArquivoDecreto;
	}
}