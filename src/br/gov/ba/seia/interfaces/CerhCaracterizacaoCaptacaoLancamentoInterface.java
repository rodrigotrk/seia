package br.gov.ba.seia.interfaces;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public interface CerhCaracterizacaoCaptacaoLancamentoInterface {

	public Date getData();
	public void setData(Date data);
	
	public BigDecimal getValVazaoMaximaInstantanea();
	public void setValVazaoMaximaInstantanea(BigDecimal vazao);
	
	public Collection<CerhVazaoSazonalidadeInterface> getVazaoSazonalidadeCollection();
	public void setVazaoSazonalidadeCollection(Collection<CerhVazaoSazonalidadeInterface> coll);
	
}
