package br.gov.ba.seia.interfaces;

import br.gov.ba.seia.dto.identificarPapel.SolicitacaoDTO;


public interface PessoaFisicaAbaSolicitacao {

	public void load(SolicitacaoDTO socitacao);
	public void prepararDialog();
	
	
}
