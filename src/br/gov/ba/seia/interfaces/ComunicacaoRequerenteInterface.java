package br.gov.ba.seia.interfaces;

import java.util.Date;

public interface ComunicacaoRequerenteInterface {
	
	public String getDesMensagem();
	public void setDesMensagem(String msg);
	
	public Date getDtcComunicacao();
	public void setDtcComunicacao(Date data);
	
}
