package br.gov.ba.seia.controller;

import java.math.BigDecimal;

import br.gov.ba.seia.entity.EspecieFloresta;
import br.gov.ba.seia.entity.NaturezaEspecieFloresta;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelEspecieProducao;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.rfp.Componente;
import br.gov.ba.seia.util.rfp.Dialog;

public class RFPEspecieProducaoController {
		
	public void exibir(RFPController rfp){
		
		if(rfp.getNaturezaEspecieFlorestaList()==null){
			rfp.setNaturezaEspecieFlorestaList(rfp.getFacade().listarNaturezaEspecieFloresta());
		}
		
		if(rfp.getEspecieFlorestaList()==null){
			rfp.setEspecieFlorestaList(rfp.getFacade().listarEspecieFloresta());
		}
		
		rfp.setIdeRegistroFlorestaProducaoImovelEspecieProducao(new RegistroFlorestaProducaoImovelEspecieProducao());
		rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().setIdeEspecieFloresta(new EspecieFloresta());
		rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getIdeEspecieFloresta().setIdeNaturezaEspecieFloresta(new NaturezaEspecieFloresta());
		
		Html.getCurrency()
			.update(Componente.formEspecieProducao)
			.show(Dialog.dialogRfpEspecieProducao);
	}
	
	public void adicionar(RFPController rfp){
		
		if(validar(rfp)){
						
			if(!rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().isEditar()){
				rfp.getRegistroFlorestaProducaoImovelEspecieProducaoList().add(rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao());
			}	
			rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
			
			MensagemUtil.msg0010();
			
			Html.getCurrency().hide(Dialog.dialogRfpEspecieProducao).update(Componente.panelEspecieProducao);
		}
	}
		
	public boolean validar(RFPController rfp){
		
		if(rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getIdeEspecieFloresta()==null){
			MensagemUtil.msg0003("Nome cientifico / Vulgar");
			return false;
		}else
		 if (rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getIdeEspecieFloresta().getIdeNaturezaEspecieFloresta()==null){
			MensagemUtil.msg0003("Natureza das espécies utilizadas");
			return false;
		 }
		
		else if (rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValIma()==null ||	rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValIma().compareTo(BigDecimal.ZERO)==0){
			MensagemUtil.msg0003("Incremento médio anual - IMA");
			return false;
		}
		
		else if (rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValCicloCorte()==null || rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValCicloCorte()== 0 ){
			MensagemUtil.msg0003("Ciclo do corte");
			return false;
		}
		
		else if (rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValEspacamento()==null || rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValEspacamento().compareTo(BigDecimal.ZERO)==0) {
			MensagemUtil.msg0003("Espaçamento");
			return false;
		}
		
		else if (rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValEstimativaVolumeTotalFinal()==null || rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getValEstimativaVolumeTotalFinal().compareTo(BigDecimal.ZERO)==0){
			MensagemUtil.msg0003("Estimativa do volume de produção total final no primeiro corte");
			return false;
		}
		
		return true;
	}

	public void excluir(RFPController rfp){
		
		if(rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao().getIdeRegistroFlorestaProducaoImovelEspecieProducao()!=null){
			rfp.getFacade().excluirIdeRegistroFlorestaProducaoImovelEspecieProducao(rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao());
		}
		
		rfp.getRegistroFlorestaProducaoImovelEspecieProducaoList().remove(rfp.getIdeRegistroFlorestaProducaoImovelEspecieProducao());
		
		rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
		
		Html.getCurrency()
			.update(Componente.panelEspecieProducao)
			.hide(Dialog.confirmDialogExcluirEspecieProducao);
		
		MensagemUtil.msg0005();
	}
	
	
}

