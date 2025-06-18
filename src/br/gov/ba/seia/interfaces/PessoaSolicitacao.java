package br.gov.ba.seia.interfaces;

import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.dto.AbaDTO;
import br.gov.ba.seia.dto.identificarPapel.SolicitacaoDTO;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.enumerator.IdentificarPessoaFisicaAbaEnum;

public interface PessoaSolicitacao {

	public void load(SolicitacaoDTO solicitacao);
	public void buscarRequerente();
	
	public boolean validarBuscarRequerente();
	public boolean isCadastroCompleto();
	public boolean isPossuiRequerente();
	
	public boolean validarDocumentoConsulta();
	public boolean validarPreencimentoAba();
	
	public void onChange(TabChangeEvent e);
	public AbaDTO aba(IdentificarPessoaFisicaAbaEnum pessoaFisica);
	
	public void avancarAba();		
	public int getAbaAtiva();
	public void setAbaAtiva();

	public void prepararDialog();
	public DocumentoIdentificacao getDocumentoIdentificacao();
	public void setDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao);
	
	
}
