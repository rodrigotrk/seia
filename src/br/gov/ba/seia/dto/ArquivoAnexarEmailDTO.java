package br.gov.ba.seia.dto;

import java.io.InputStream;

/**
 * Classe de integração
 * @author 
 *
 */
public class ArquivoAnexarEmailDTO {
	
	private String type;
	private String nomeArquivo;
	private String dscArquivo;
	private InputStream inputStream;
	
	/**
	 * Construtor
	 */
	public ArquivoAnexarEmailDTO () {
		
	}
	/**
	 * Construtor
	 * @param type
	 * @param nomeArquivo
	 * @param dscArquivo
	 * @param inputStream
	 */
	public ArquivoAnexarEmailDTO (String type, String nomeArquivo, String dscArquivo, InputStream inputStream) {
		this.setType(type);
		this.setNomeArquivo(nomeArquivo);
		this.setDscArquivo(dscArquivo);
		this.setInputStream(inputStream);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getDscArquivo() {
		return dscArquivo;
	}

	public void setDscArquivo(String dscArquivo) {
		this.dscArquivo = dscArquivo;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
