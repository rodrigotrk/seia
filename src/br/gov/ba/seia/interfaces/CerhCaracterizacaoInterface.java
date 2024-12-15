package br.gov.ba.seia.interfaces;

import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhSituacaoTipoUso;

public interface CerhCaracterizacaoInterface {

	public Integer getId();
	public void setIde(Integer ide);
	
	public String getNomCorpoHidrico();
	public void setNomCorpoHidrico(String nome);
	
	public String getDscObservacao();
	public void setDscObservacao(String obs);
	
	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeografica();
	public void setIdeCerhLocalizacaoGeografica(CerhLocalizacaoGeografica cerhLocalizacaoGeografica);
	
	public CerhSituacaoTipoUso getIdeCerhSituacaoTipoUso();
	public void setIdeCerhSituacaoTipoUso(CerhSituacaoTipoUso cerhSituacaoTipoUso);
	
}
