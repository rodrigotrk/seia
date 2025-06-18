package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.List;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.rfp.Componente;
import br.gov.ba.seia.util.rfp.Dialog;

public class RFPFlorestaProducaoController {
	
	public void exibir(RFPController rfp){
		for (Imovel imovel : rfp.getFacade().listarRequerimentoImovelPor(rfp.getIdeRequerimento())) {
			imovel = rfp.getFacade().carregarNomeImovel(imovel, rfp.getRequerimento());
			if(!rfp.getImovelList().contains(imovel)){
				rfp.getImovelList().add(imovel);
			}
		}
		
		rfp.getRegistroFlorestaProducao().setIdeRegistroFlorestaProducaoImovel(new RegistroFlorestaProducaoImovel());
		rfp.getIdeRegistroFlorestaProducaoImovel().setIdeImovel(new Imovel());
		rfp.getIdeRegistroFlorestaProducaoImovel().setEditar(false);
	
		Html.exibir(Dialog.dialogRfpFlorestaProducaoPlantada);
	}
	
	public void adicionar(RFPController rfp){
		
		if(validar(rfp)){
			
			rfp.getIdeRegistroFlorestaProducaoImovel().setAreaTotalPlantio(somarAreasPlantio(rfp.getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList()));
			
			if(!rfp.getIdeRegistroFlorestaProducaoImovel().isEditar()){
				rfp.getRegistroFlorestaProducaoImovelList().add(rfp.getIdeRegistroFlorestaProducaoImovel());
			}
		
			rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
			
			MensagemUtil.msg0010();

			Html.getCurrency()
				.hide(Dialog.dialogRfpFlorestaProducaoPlantada)
				.update(Componente.panelFlorestaProducaoImovelPlantio)
				.update("tabRegistroFlorestaPlantada:formAbaRegistroFlorestaPlantada:pnlRegistroFlorestaProducaoImovelPlantio")
				.update("tabRegistroFlorestaPlantada:formAbaRegistroFlorestaPlantada:somaAreas");
		}
	}
	
	private boolean validar(RFPController rfp){
		
		if(rfp.getIdeRegistroFlorestaProducaoImovel().getIdeImovel()==null){
			MensagemUtil.msg0003("Selecionar Imóvel");
			return false;
		}
		
		else if(rfp.getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList() == null || rfp.getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList().isEmpty()){
			MensagemUtil.msg0003("Adicionar Plantio");
			return false;
		}
		
		else if(rfp.getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelEspecieProducaoList() == null ||rfp.getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelEspecieProducaoList().isEmpty()){
			MensagemUtil.msg0003("Espécie de produção");
			return false;
		}

		for (RegistroFlorestaProducaoImovel fpi : rfp.getRegistroFlorestaProducaoImovelList()) {
			
			if(!fpi.isEditar()){
				if(fpi.getIdeImovel().getIdeImovel().equals((rfp.getIdeRegistroFlorestaProducaoImovel().getIdeImovel().getIdeImovel()))){
					for (RegistroFlorestaProducaoImovelPlantio rfpip : fpi.getRegistroFlorestaProducaoImovelPlantioList()) {
						if(rfpip.getIdeSituacaoAtualFlorestaProducao().equals(rfp.getIdeRegistroFlorestaProducaoImovelPlantio().getIdeSituacaoAtualFlorestaProducao())){
							
							Html.atualizar("formFlorestaProducaoPlantada:pnlDadosPlantio");
							JsfUtil.addErrorMessage("Situação atual da floresta já cadastrada");
							return false;
						}
					}
				}
			}
			
		}
		
		for (RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio : rfp.getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList()) {
			if(registroFlorestaProducaoImovelPlantio.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() == null){
				MensagemUtil.msg0003("Localização gegráfica do plantio");
				return false;
			}
		}
		
		return true;
	}
	
	public boolean validar(List<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovel){
		if(registroFlorestaProducaoImovel== null || registroFlorestaProducaoImovel.isEmpty()){
			MensagemUtil.msg0003("Dados da floresta de produção");
			return false;
		}
		
		
		for (RegistroFlorestaProducaoImovel rfpi : registroFlorestaProducaoImovel) {
			for (RegistroFlorestaProducaoImovelPlantio plantio : rfpi.getRegistroFlorestaProducaoImovelPlantioList()) {
				if(plantio.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()==null){
					MensagemUtil.msg0003("Localização geográfia do plantio");
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void excluir(RFPController rfp) {
		
		rfp.getFacade().excluirRegistroFlorestaProducao(rfp);
		
		rfp.getRegistroFlorestaProducaoImovelList().remove(rfp.getIdeRegistroFlorestaProducaoImovel());
		rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());

		rfp.getImovelList().clear();	
		
		Html.getCurrency()
			.update(Componente.panelFlorestaProducaoImovelPlantio)
			.hide(Dialog.confirmDialogExcluirFlorestaProducao);
		
		MensagemUtil.msg0005();
	}
	
	private BigDecimal somarAreasPlantio(List<RegistroFlorestaProducaoImovelPlantio> imovelPlantio){
		BigDecimal areaPlantioTotal = BigDecimal.ZERO;
		for (RegistroFlorestaProducaoImovelPlantio plantio : imovelPlantio) {
			if(plantio.getValAreaPlantio()!=null){
				areaPlantioTotal = areaPlantioTotal.add(plantio.getValAreaPlantio());
			}
		}
		
		return areaPlantioTotal;
	}
	
}
