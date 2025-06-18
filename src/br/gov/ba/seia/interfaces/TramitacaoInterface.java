package br.gov.ba.seia.interfaces;

import java.util.Date;

import br.gov.ba.seia.entity.Pessoa;

public interface TramitacaoInterface {
	
	public Date getDtcMovimentacao();
	public void setDtcMovimentacao(Date data);
	
	public Pessoa getIdePessoa();
	public void setIdePessoa(Pessoa pessoa);
}
