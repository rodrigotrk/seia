package br.gov.ba.seia.entity;

import java.util.Date;

public interface GeoReferenciavel {
	
	public Integer getIde();
	
	public void setIde(Integer ide);
	
	public LocalizacaoGeografica getIdeLocalizacaoGeografica();
	
	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica);
	
	public Integer getIdeRequerimento();
	
	public Processo getIdeProcesso();

	public void setDtcCriacao(Date data);
	
}
