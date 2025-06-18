package br.gov.ba.seia.interfaces;

import java.math.BigDecimal;

import br.gov.ba.seia.entity.Mes;

public interface CerhVazaoSazonalidadeInterface extends Comparable<CerhVazaoSazonalidadeInterface>{

	public Integer getIde();

	public BigDecimal getValVazao();
	public void setValVazao(BigDecimal vazao);
	
	public BigDecimal getVazaoDia();
	public void setVazaoDia(BigDecimal vazao);
	
	public BigDecimal getVazaoMes();
	public void setVazaoMes(BigDecimal vazao);
	
	public Integer getValDiaMes();
	public void setValDiaMes(Integer valDiaMes);
	
	public Integer getValTempo();
	public void setValTempo(Integer valTempo);
	
	public Mes getIdeMes();
	
}
