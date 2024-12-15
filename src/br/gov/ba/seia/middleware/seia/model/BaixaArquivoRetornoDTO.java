package br.gov.ba.seia.middleware.seia.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe modelo para baixar arquivo de retorno
 * @author 
 *
 */
public class BaixaArquivoRetornoDTO {

	private List<BaixaBoletoDTO> listaBaixaBoleto;
	private List<BaixaBoletoDTO> listaBaixaBoletoErro;

	public BaixaArquivoRetornoDTO() {
		this.listaBaixaBoleto = new ArrayList<BaixaBoletoDTO>();
		this.listaBaixaBoletoErro = new ArrayList<BaixaBoletoDTO>();
	}

	public List<BaixaBoletoDTO> getListaBaixaBoleto() {
		return listaBaixaBoleto;
	}

	public void setListaBaixaBoleto(List<BaixaBoletoDTO> listaBaixaBoleto) {
		this.listaBaixaBoleto = listaBaixaBoleto;
	}

	public List<BaixaBoletoDTO> getListaBaixaBoletoErro() {
		return listaBaixaBoletoErro;
	}

	public void setListaBaixaBoletoErro(List<BaixaBoletoDTO> listaBaixaBoletoErro) {
		this.listaBaixaBoletoErro = listaBaixaBoletoErro;
	}

}
