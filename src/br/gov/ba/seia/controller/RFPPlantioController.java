package br.gov.ba.seia.controller;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.enumerator.SituacaoAtualFlorestaProducaoEnum;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.rfp.Componente;
import br.gov.ba.seia.util.rfp.Dialog;

public class RFPPlantioController {
	
	public void exibir(RFPController rfp){
		
		if(rfp.getSituacaoAtualFlorestaProducaoList()==null){
			rfp.setSituacaoAtualFlorestaProducaoList(rfp.getFacade().listarSituacaoAtualFlorestaProducao());
		}

		rfp.setIdeRegistroFlorestaProducaoImovelPlantio(new RegistroFlorestaProducaoImovelPlantio());
		rfp.getIdeRegistroFlorestaProducaoImovel().setIdeImovel(new Imovel());

		Html.getCurrency()
			.update(Componente.formPlantio)
			.show(Dialog.dialogRfpPlantio);
	}
	
	public void adicionar(RFPController rfp){
		
		if(validar(rfp.getIdeRegistroFlorestaProducaoImovelPlantio())){
			
			if(!rfp.getIdeRegistroFlorestaProducaoImovelPlantio().isEditar()){
				rfp.getRegistroFlorestaProducaoImovelPlantioList().add(rfp.getIdeRegistroFlorestaProducaoImovelPlantio());
			}

			rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
			
			MensagemUtil.msg0010();
			
			Html.getCurrency()
				.hide(Dialog.dialogRfpPlantio)
				.update(Componente.panelPlantio);
		}
		
	}
	
	private boolean validar(RegistroFlorestaProducaoImovelPlantio rfp){
		
		if(rfp.getIdeSituacaoAtualFlorestaProducao()==null){
			MensagemUtil.msg0003("Situação atual da floresta");
			return false;
		}else{
			if(rfp.getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao().equals(SituacaoAtualFlorestaProducaoEnum.TOTALMENTE_IMPLANTADA.getId())){
				if(rfp.getDtInicioPeriodoImplantacao()==null || rfp.getDtFimPeriodoImplantacao()==null){
					MensagemUtil.msg0003("Período da implantação");
					return false;
				}
				else if(rfp.getDtInicioPeriodoImplantacao()!=null && rfp.getDtFimPeriodoImplantacao()!=null){
					if(rfp.getDtInicioPeriodoImplantacao().after(rfp.getDtFimPeriodoImplantacao())){
						MensagemUtil.info0041();
						return false;
					}
				}
			}
			
			else if(
				rfp.getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao().equals(SituacaoAtualFlorestaProducaoEnum.EM_IMPLANTACAO.getId()) ||
				rfp.getIdeSituacaoAtualFlorestaProducao().getIdeSituacaoAtualFlorestaProducao().equals(SituacaoAtualFlorestaProducaoEnum.EM_PROJETO.getId())){

				if(rfp.getDtInicioPrevistaImplantacao()==null || rfp.getDtFimPrevistaImplantacao()==null){
					MensagemUtil.msg0003("Período previsto da implantanção");
					return false;
				}
				else if (rfp.getDtInicioPrevistaImplantacao()!=null || rfp.getDtFimPrevistaImplantacao()!=null){
					if(rfp.getDtInicioPrevistaImplantacao().after(rfp.getDtFimPrevistaImplantacao())){
						MensagemUtil.info0041();
						return false;
					}
				}
			}
		}
		
		if(rfp.getIdeLocalizacaoGeografica()==null || rfp.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()==null){
			MensagemUtil.msg0003("Localização geografica");
			return false;
		}
		
		
		return true;
	}

	public void excluir(RFPController rfp) {
		if(rfp.getIdeRegistroFlorestaProducaoImovelPlantio().getIdeRegistroFlorestaImovelPlantio()!=null){
			rfp.getFacade().excluirIdeRegistroFlorestaProducaoImovelPlantio(rfp.getIdeRegistroFlorestaProducaoImovelPlantio());
		}
		
		rfp.getRegistroFlorestaProducaoImovelPlantioList().remove(rfp.getIdeRegistroFlorestaProducaoImovelPlantio());
		rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
		
		for (RegistroFlorestaProducaoImovel imovelList : rfp.getRegistroFlorestaProducaoImovelList()) {
			imovelList.atualizarAreasPlantio();
		}
		
		for (RegistroFlorestaProducaoImovel fpi : rfp.getRegistroFlorestaProducaoImovelList()) {
			fpi.setEditar(false);
		}			
		
		rfp.getIdeRegistroFlorestaProducao().setEditar(false);
		
		if(rfp.getIdeRegistroFlorestaProducaoImovelPlantio()!=null){
			rfp.setIdeRegistroFlorestaProducaoImovelPlantio(new RegistroFlorestaProducaoImovelPlantio());
		}
		
		Html.getCurrency()
			.update(
				Componente.panelPlantio,
				"tabRegistroFlorestaPlantada:formAbaRegistroFlorestaPlantada",
				"tabRegistroFlorestaPlantada:formAbaRegistroFlorestaPlantada:pnlRegistroFlorestaProducaoImovelPlantio")
			.hide(Dialog.confirmDialogExcluirPlantio);
		
		MensagemUtil.msg0005();
	}
	
}
