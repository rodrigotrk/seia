package br.gov.ba.seia.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.util.Log4jUtil;

public class ComprovantePagamentoDaeDTO {

	private ComprovantePagamentoDae comprovantePagamentoDae;

	private String size;
	
	private String nome;
	
	public ComprovantePagamentoDaeDTO(ComprovantePagamentoDae pComprovantePagamento) {
		comprovantePagamentoDae = pComprovantePagamento;
		File arquivo = new File(pComprovantePagamento.getDscCaminhoArquivo());
		if(arquivo.exists() && arquivo.isFile() && arquivo.length() > 0) {
			size = Long.toString(arquivo.length()/1000);
			nome = arquivo.getName();
		}
	}

	public ComprovantePagamentoDae getComprovantePagamentoDae() {
		return comprovantePagamentoDae;
	}

	public void setComprovantePagamentoDae(
			ComprovantePagamentoDae comprovantePagamento) {
		this.comprovantePagamentoDae = comprovantePagamento;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public StreamedContent getDownload() {
		
		String lNomeReal = comprovantePagamentoDae.getDscCaminhoArquivo();
		String lNomeArquivo = lNomeReal.substring(lNomeReal.indexOf(File.separator));
		String lTipoArquivo = lNomeArquivo.substring(lNomeArquivo.indexOf("."));
		
		try {
			return new DefaultStreamedContent(new FileInputStream(new File(comprovantePagamentoDae.getDscCaminhoArquivo())), "application/"+ lTipoArquivo, lNomeArquivo);
		} catch (FileNotFoundException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
		return null;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}