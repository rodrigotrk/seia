package br.gov.ba.seia.controller;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.util.ExpressaoRegularUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.rfp.Componente;
import br.gov.ba.seia.util.rfp.Dialog;

public class RFPImovelController {
	
	public void exibir(RFPController rfp){
		rfp.setImovelPesquisa(null);
		
		Html.getCurrency()
			.update(Componente.formIncluirImovel)
			.show(Dialog.dialogImovel);
	}
	
	public void carregar(RFPController rfp) {
		
		if(rfp.getImovelPesquisa().getImovelRural().getImovelRuralSicar().getNumSicar()==null || rfp.getImovelPesquisa().getImovelRural().getImovelRuralSicar().getNumSicar().isEmpty()){
			MensagemUtil.msg0003("Número Car");
			return;
		}
		
		else if (!rfp.getImovelPesquisa().getImovelRural().getImovelRuralSicar().getNumSicar().matches(ExpressaoRegularUtil.getNumSicar())){
			MensagemUtil.info0033();
			return;
		}
		
		Imovel imovel = rfp.getFacade().buscarImovelPorNumeroCar(rfp.getImovelPesquisa().getImovelRural());
		
		if(imovel!=null ){
			imovel = rfp.getFacade().carregarNomeImovel(imovel, rfp.getIdeRequerimento());
			if(rfp.getImovelList().contains(imovel)){
				MensagemUtil.info0040();
				rfp.setNumSicar("");
				Html.atualizar(Componente.formIncluirImovel);
			}else{
				imovel.setIndArrendado(true);
				rfp.setImovelPesquisa(imovel);
			}
		}
	}

	public void adicionar(RFPController rfp){
		try{
			
			if(rfp.getImovelList().contains(rfp.getImovelPesquisa())){
				MensagemUtil.info0040();
				return;
			}else{
				
				rfp.getImovelPesquisa().setIndArrendado(true);
				rfp.getImovelList().add(rfp.getImovelPesquisa());
				rfp.getIdeRegistroFlorestaProducaoImovel().setIdeImovel(rfp.getImovelPesquisa());
				
				MensagemUtil.msg0010();
				Html.atualizar(Componente.formFlorestaProducaoPlantada);
					
			}
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			 throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		
	}

}

