package br.gov.ba.seia.controller.abstracts;

import java.io.FileNotFoundException;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.GerenciadorDocumentoAdicional;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;

public abstract class BaseFceOutorgaComDocumentoAdicionalController extends BaseFceOutorgaController {

	@Inject
	private GerenciadorDocumentoAdicional gerenciadorDocumentoAdicional;

	/**
	 * 
	 * @param nomeDoArquivo
	 * @param nomeDeSaida
	 * @return
	 * @throws FileNotFoundException
	 */
	public StreamedContent getDadosAdicionais(String nomeDoArquivo, String nomeDeSaida) throws FileNotFoundException {
		return gerenciadorDocumentoAdicional.getDadosAdicionais(nomeDoArquivo, nomeDeSaida);
	}

	/**
	 * Exclur documento anexado
	 * 
	 * @param caminho
	 */
	protected void excluirDocumentoUpadoAnteriormente(String caminho) {
		gerenciadorDocumentoAdicional.excluirDocumentoUpadoAnteriormente(caminho);
	}

	public StreamedContent getDocumentoAdicionalUpado(String string) {
		return gerenciadorDocumentoAdicional.getDocumentoAdicionalUpado(string);
	}

	/**
	 * Salvar documento adicional
	 * 
	 * @param requerimento
	 * @param documentoObrigatorio
	 * @throws Exception
	 */
	public void salvarDocumentoAdicional(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio)
			throws Exception {
		gerenciadorDocumentoAdicional.salvarDocumentoAdicional(requerimento, documentoObrigatorio);
	}

	/**
	 * Método que persiste a lista {@link OutorgaConcedida} no Banco de Dados.
	 * 
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @see <a href="http://10.105.12.26/redmine/issues/7550">#7550</a>
	 */
	public void salvarListaOutorgaConcedida(List<OutorgaConcedida> listaOutorgaConcedida) throws Exception {
		super.fceOutorgaServiceFacade.salvarListaOutorgaConcedida(listaOutorgaConcedida);
	}

	/**
	 * Carregar documento adicional
	 * @param documentoObrigatorio
	 * @throws Exception
	 */
	protected void carregarDocumentoAdicionalByDocumentoObrigatorio(DocumentoObrigatorioEnum documentoObrigatorio)
			throws Exception {
		gerenciadorDocumentoAdicional.carregarDocumentoAdicionalByDocumentoObrigatorio(super.requerimento,
				documentoObrigatorio);
	}
	/**
	 * Carregar documento adicional do documento obrigatorio do requerimento
	 * @param documentoObrigatorioRequerimento
	 * @throws Exception
	 */
	public void carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(
			DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) throws Exception {
		gerenciadorDocumentoAdicional
				.carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(documentoObrigatorioRequerimento);
	}
	/**
	 * Limpar documento carregado
	 */
	protected void limparDocumentoUpado() {
		gerenciadorDocumentoAdicional.limparDocumentoUpado();
	}

	public DocumentoObrigatorioRequerimento getDocumentoUpado() {
		return gerenciadorDocumentoAdicional.getDocumentoUpado();
	}
	/**
	 * Iniciar documento adicional
	 */
	protected void inicarDocumentoAdicional() {
		gerenciadorDocumentoAdicional.inicarDocumentoAdicional(super.requerimento);
	}
	/**
	 * 
	 * @return arquivo recarregado
	 */
	public boolean isArquivoUpado() {
		return gerenciadorDocumentoAdicional.isArquivoUpado();
	}

	public void setDesabilitarDocumentoAdicional() {
		gerenciadorDocumentoAdicional.setDesabilitarDocumentoAdicional(super.isDesabilitarTudo());
	}
}