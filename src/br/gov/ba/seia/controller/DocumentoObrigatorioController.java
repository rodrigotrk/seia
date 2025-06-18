package br.gov.ba.seia.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.TipoDocumentoObrigatorio;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
@Named("documentoObrigatorioController")
@ViewScoped
public class DocumentoObrigatorioController {

	private Collection<DocumentoObrigatorio> listaDocumentoObrigatorio;
	private Collection<DocumentoObrigatorio> listaDocumentoObrigatorioACadastrar;
	private String nomeDocumento;
	private Boolean indFormulario,indPublico;
	private String caminhoArquivo;

	private DocumentoObrigatorio documentoObrigatorio;

	private DocumentoObrigatorio documentoObrigatorioSelecionado;

	private boolean modoEdicao = false;
	private Integer ideDocumento;

	private TipoDocumentoObrigatorio TipoDocumentoObrigatorioSelecionado;
	private List<TipoDocumentoObrigatorio> TipoDocumentoObrigatorio; 
	
	
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;

	@PostConstruct
	public void init(){
		consultarDocumentoObrigatorio();
		listaDocumentoObrigatorioACadastrar = new ArrayList<DocumentoObrigatorio>();

		final Object temp = ContextoUtil.getContexto().getObject();
		indFormulario=false;
		if(temp != null && temp instanceof DocumentoObrigatorio) {
			documentoObrigatorio = (DocumentoObrigatorio) temp;
			modoEdicao = true;
			ContextoUtil.getContexto().setObject(null);

			ideDocumento = documentoObrigatorio.getIdeDocumentoObrigatorio();
			nomeDocumento = documentoObrigatorio.getNomDocumentoObrigatorio();
			indFormulario = documentoObrigatorio.getIndFormulario();
			indPublico = documentoObrigatorio.getIndPublico();

			listaDocumentoObrigatorioACadastrar.add(documentoObrigatorio);
			
		}
	}

	public void consultarDocumentoObrigatorio(){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!Util.isNullOuVazio(nomeDocumento)){
				params.put("nomeDocumento", "%"+nomeDocumento+"%");
			}

			listaDocumentoObrigatorio = documentoObrigatorioService.listarDocumentosObrigatorios(params);
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void limparFormulario(){
		nomeDocumento = null;
		listaDocumentoObrigatorioACadastrar = null;
		modoEdicao = false;
		indFormulario=false;
		indPublico=false;
	}

	public void trataArquivo(FileUploadEvent event) {
		String caminho = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
		this.setCaminhoArquivo(caminho);
		documentoObrigatorio = new DocumentoObrigatorio();
		File file = new File(this.getCaminhoArquivo().trim());
		documentoObrigatorio.setFileSize(file.length());
		documentoObrigatorio.setDscCaminhoArquivo(this.getCaminhoArquivo());
		if(Util.isNullOuVazio(listaDocumentoObrigatorioACadastrar)){
			listaDocumentoObrigatorioACadastrar = new ArrayList<DocumentoObrigatorio>();
		}
		listaDocumentoObrigatorioACadastrar.add(documentoObrigatorio);
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");		
	}

	public String salvarDocumento(){
		try {
			
			if((modoEdicao == false) && (listaDocumentoObrigatorioACadastrar != null) && (!listaDocumentoObrigatorioACadastrar.isEmpty())){
				documentoObrigatorio.setIdeDocumentoObrigatorio(null);
				documentoObrigatorio.setNumTamanho(new BigDecimal(documentoObrigatorio.getFileSize()));
			}
			
			else if(modoEdicao == true && listaDocumentoObrigatorioACadastrar != null && !listaDocumentoObrigatorioACadastrar.isEmpty()){
				documentoObrigatorio.setIdeDocumentoObrigatorio(ideDocumento);

				documentoObrigatorio.setDscCaminhoArquivo(caminhoArquivo);
				if(!Util.isNull(caminhoArquivo)){
					File file = new File(this.getCaminhoArquivo().trim());				
					documentoObrigatorio.setNumTamanho(new BigDecimal(file.length()));
				}
	
			}
			else if(modoEdicao == false){
				documentoObrigatorio = new DocumentoObrigatorio();
				documentoObrigatorio.setIdeDocumentoObrigatorio(null);
				documentoObrigatorio.setNumTamanho(new BigDecimal(0));
				documentoObrigatorio.setDscCaminhoArquivo(null);
	
			}
			else if(modoEdicao == true){
				documentoObrigatorio = new DocumentoObrigatorio();
				documentoObrigatorio.setIdeDocumentoObrigatorio(ideDocumento);
				documentoObrigatorio.setNumTamanho(new BigDecimal(0));
				documentoObrigatorio.setDscCaminhoArquivo(null);
			}
			
			documentoObrigatorio.setIdeTipoDocumentoObrigatorio(TipoDocumentoObrigatorioSelecionado);
			documentoObrigatorio.setNomDocumentoObrigatorio(nomeDocumento);
			documentoObrigatorio.setIndFormulario(indFormulario);
			documentoObrigatorio.setIndPublico(indPublico);
			
			documentoObrigatorioService.salvarAtualizarDocumentoObrigatorio(documentoObrigatorio);
			JsfUtil.addSuccessMessage("Documento salvo com Sucesso.");
			limparFormulario();
			
			return prepararParaConsultar();
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage("Erro: Não foi possível Salvar");
			return null;
		}
	}

	public String prepararParaIncluir(){
		return "/paginas/manter-documentoobrigatorio/cadastro.xhtml";
	}

	public String prepararParaConsultar(){
		return "/paginas/manter-documentoobrigatorio/consulta.xhtml";
	}

	public void excluirDocumentoObrigatorio(){
		try {
			documentoObrigatorioService.excluirDocumentoObrigatorio(this.documentoObrigatorioSelecionado);
			consultarDocumentoObrigatorio();
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso!");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	public String getNomeDocumento() {
		return nomeDocumento;
	}

	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	public Collection<DocumentoObrigatorio> getListaDocumentoObrigatorio() {
		return listaDocumentoObrigatorio;
	}

	public void setListaDocumentoObrigatorio(Collection<DocumentoObrigatorio> listaDocumentoObrigatorio) {
		this.listaDocumentoObrigatorio = listaDocumentoObrigatorio;
	}

	public DocumentoObrigatorio getDocumentoObrigatorioSelecionado() {
		return documentoObrigatorioSelecionado;
	}

	public void setDocumentoObrigatorioSelecionado(DocumentoObrigatorio documentoObrigatorioSelecionado) {
		this.documentoObrigatorioSelecionado = documentoObrigatorioSelecionado;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public Collection<DocumentoObrigatorio> getListaDocumentoObrigatorioCadastrado() {
		return listaDocumentoObrigatorioACadastrar;
	}

	public void setListaDocumentoObrigatorioCadastrado(Collection<DocumentoObrigatorio> listaDocumentoObrigatorioCadastrado) {
		this.listaDocumentoObrigatorioACadastrar = listaDocumentoObrigatorioCadastrado;
	}

	public Boolean getIndFormulario() {
		return indFormulario;
	}

	public void setIndFormulario(Boolean indFormulario) {
		this.indFormulario = indFormulario;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}

	public Integer getIdeDocumento() {
		return ideDocumento;
	}

	public void setIdeDocumento(Integer ideDocumento) {
		this.ideDocumento = ideDocumento;
	}

	public Boolean getIndPublico() {
		return indPublico;
	}

	public void setIndPublico(Boolean indPublico) {
		this.indPublico = indPublico;
	}

	public TipoDocumentoObrigatorio getTipoDocumentoObrigatorioSelecionado() {
		return TipoDocumentoObrigatorioSelecionado;
	}

	public void setTipoDocumentoObrigatorioSelecionado(	TipoDocumentoObrigatorio tipoDocumentoObrigatorioSelecionado) {
		TipoDocumentoObrigatorioSelecionado = tipoDocumentoObrigatorioSelecionado;
	}

	public List<TipoDocumentoObrigatorio> getTipoDocumentoObrigatorio() {
		
		if(Util.isNullOuVazio(TipoDocumentoObrigatorio)){
			try {
				TipoDocumentoObrigatorio = documentoObrigatorioService.listarTipoDocumentoObrigatorio();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		

		return TipoDocumentoObrigatorio;
	}

	public void setTipoDocumentoObrigatorio(List<TipoDocumentoObrigatorio> tipoDocumentoObrigatorio) {
		TipoDocumentoObrigatorio = tipoDocumentoObrigatorio;
	}
}
