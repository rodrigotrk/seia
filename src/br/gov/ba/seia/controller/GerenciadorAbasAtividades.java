package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.util.Util;

/**
 * Controller responsável pela navegação entre as abas das Atividades.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 04/09/2015
 */
public class GerenciadorAbasAtividades{

	private static final int ABA_PISCICULTURA = 0;
	private static final int ABA_CARCINICULTURA = 1;
	private static final int ABA_RANICULTURA = 2;
	private static final int ABA_ALGICULTURA = 3;
	private static final int ABA_MALOCOCULTURA = 4;

	private int activeTab;

	private List<AquiculturaAtividadeDTO> listaAquiculturaAtividadeDTO;

	public void atualizarTipoProducao(AjaxBehaviorEvent event){
		String id = event.getComponent().getId();
		String clientId = event.getComponent().getClientId();
		RequestContext.getCurrentInstance().addPartialUpdateTarget(clientId.replace(id, "group"));
	}
	
	public void atualizarTipoInstalacao(AjaxBehaviorEvent event){
		String id = event.getComponent().getId();
		String clientId = event.getComponent().getClientId();
		int inicio = clientId.indexOf("tableTipoInstalacao");
		int fim = clientId.indexOf(id);
		String toRemove = clientId.substring(inicio, fim);
		RequestContext.getCurrentInstance().addPartialUpdateTarget(clientId.replace(":"+toRemove+id, ":group"));
	}

	/**
	 * Método que controla as abas do FCE de acordo com o clique dado pelo usuário na tela.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/05/2015
	 * @param event
	 */
	public void controlarAbas(TabChangeEvent event){
		if ("tabPiscicultura".equals(event.getTab().getId())) {
			activeTab = ABA_PISCICULTURA;
		}
		else if ("tabCarcinicultura".equals(event.getTab().getId())) {
			activeTab = ABA_CARCINICULTURA;
		}
		else if ("tabRanicultura".equals(event.getTab().getId())) {
			activeTab = ABA_RANICULTURA;
		}
		else if ("tabAlgicultura".equals(event.getTab().getId())) {
			activeTab = ABA_ALGICULTURA;
		}
		else if ("tabMalococultura".equals(event.getTab().getId())) {
			activeTab = ABA_MALOCOCULTURA;
		}
	}

	public boolean isAbaPisciculturaAtiva(){
		return activeTab == ABA_PISCICULTURA;
	}

	public boolean isAbaCarciniculturaAtiva(){
		return activeTab == ABA_CARCINICULTURA;
	}

	public boolean isAbaRAniculturaAtiva(){
		return activeTab == ABA_RANICULTURA;
	}

	public boolean isAbaAlgiculturaAtiva(){
		return activeTab == ABA_ALGICULTURA;
	}

	public boolean isAbaMalococulturaAtiva(){
		return activeTab == ABA_MALOCOCULTURA;
	}

	/**
	 * Método que realiza o cálculo do somatório da área de cultivo informada por espécie ({@link FceAquiculturaLicencaTipoAtividade}) na Caracterização do Cultivo para uma determinada {@link AquiculturaAtividadeDTO}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	public void obterSomatorioDaAtividade(AquiculturaAtividadeDTO dto){
		obterSomatorioAreaCultivo(dto);
		obterSomatorioVolumeCultivo(dto);
	}
	
	private void obterSomatorioAreaCultivo(AquiculturaAtividadeDTO dto){
		if(Util.isNull(dto)){
			dto = getAtividadeDTO(); 
		}
		dto.setSomatorioAreaCultivo(new BigDecimal(0));
		BigDecimal somatorioAreaCultivo = dto.getSomatorioAreaCultivo();

		for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : dto.getListaCaracterizacaoCultivo()){
			if(caracterizacaoCultivo.isProducaoFormaJovem()){
				somatorioAreaCultivo = somatorioAreaCultivo.add(caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumAreaCultivo());
			}
			if(caracterizacaoCultivo.isProducaoEngorda()){
				somatorioAreaCultivo = somatorioAreaCultivo.add(caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumAreaCultivo());
			}
		}
		if(!Util.isNull(somatorioAreaCultivo)){
			dto.setSomatorioAreaCultivo(somatorioAreaCultivo);
		}
		else {
			dto.setSomatorioAreaCultivo(null);
		}
	}
	
	public void obterSomatorioVolumeCultivo(AquiculturaAtividadeDTO dto){
		if(Util.isNull(dto)){
			dto = getAtividadeDTO(); 
		}
		dto.setSomatorioVolume(new BigDecimal(0));
		BigDecimal somatorioVolumeCultivo = dto.getSomatorioVolume();

		for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : dto.getListaCaracterizacaoCultivo()){
			if(caracterizacaoCultivo.isProducaoFormaJovem()){
				if(!Util.isNull(caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo())){
					somatorioVolumeCultivo = somatorioVolumeCultivo.add(caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo());
				}
			}
			if(caracterizacaoCultivo.isProducaoEngorda()){
				if(!Util.isNull(caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo())){
					somatorioVolumeCultivo = somatorioVolumeCultivo.add(caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo());
				}
			}
		}
		if(!Util.isNullOuVazio(somatorioVolumeCultivo)){
			dto.setSomatorioVolume(somatorioVolumeCultivo);
		}
		else {
			dto.setSomatorioVolume(null);
		}		
	}
	

	/*
	 * [INI] - getters && setters
	 */
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}
	/*
	 * [FIM] - getters && setters
	 */

	public AquiculturaAtividadeDTO getAtividadeDTO() {
		return listaAquiculturaAtividadeDTO.get(getActiveTab());
	}

	public List<AquiculturaAtividadeDTO> getListaAquiculturaAtividadeDTO() {
		return listaAquiculturaAtividadeDTO;
	}

	public void setListaAquiculturaAtividadeDTO(List<AquiculturaAtividadeDTO> listaAquiculturaAtividadeDTO) {
		this.listaAquiculturaAtividadeDTO = listaAquiculturaAtividadeDTO;
	}
}