package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;

/**
 *
 * Classe abstrata utilizada pelos FCE's para realizar o Upload/Download dos Documentos Adicionais.
 *
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 06/03/2015R
 */
public abstract class FceComDocumentoAdicionalController extends FceController {

	@Inject
	protected GerenciadorDocumentoAdicional gerenciadorDocumentoAdicional;

	public StreamedContent getDadosAdicionais(String nomeDoArquivo, String nomeDeSaida) throws FileNotFoundException{
		return gerenciadorDocumentoAdicional.getDadosAdicionais(nomeDoArquivo, nomeDeSaida);
	}

	protected void excluirDocumentoUpadoAnteriormente(String caminho){
		gerenciadorDocumentoAdicional.excluirDocumentoUpadoAnteriormente(caminho);
	}

	public StreamedContent getDocumentoAdicionalUpado(String string) {
		return gerenciadorDocumentoAdicional.getDocumentoAdicionalUpado(string);
	}

	public void salvarDocumentoAdicional(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio) throws Exception {
		gerenciadorDocumentoAdicional.salvarDocumentoAdicional(requerimento, documentoObrigatorio);
	}

	protected void carregarDocumentoAdicionalByDocumentoObrigatorio(DocumentoObrigatorioEnum documentoObrigatorio) throws Exception {
		gerenciadorDocumentoAdicional.carregarDocumentoAdicionalByDocumentoObrigatorio(super.requerimento, documentoObrigatorio);
	}

	public void carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) throws Exception{
		gerenciadorDocumentoAdicional.carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(documentoObrigatorioRequerimento);
	}

	protected void limparDocumentoUpado(){
		gerenciadorDocumentoAdicional.limparDocumentoUpado();
	}

	public DocumentoObrigatorioRequerimento getDocumentoUpado(){
		return gerenciadorDocumentoAdicional.getDocumentoUpado();
	}

	protected void inicarDocumentoAdicional(){
		gerenciadorDocumentoAdicional.inicarDocumentoAdicional(super.requerimento);
	}

	public boolean isArquivoUpado(){
		return gerenciadorDocumentoAdicional.isArquivoUpado();
	}

	public void setDesabilitarDocumentoAdicional(){
		gerenciadorDocumentoAdicional.setDesabilitarDocumentoAdicional(super.isDesabilitarTudo());
	}

	@Override
	public void limparFce(){
		limparDocumentoUpado();
		super.limparFce();
		setDesabilitarDocumentoAdicional();
	}
}