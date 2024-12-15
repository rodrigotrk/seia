package br.gov.ba.seia.entity.auditoria.imovel;

import javax.persistence.Table;

import br.gov.ba.seia.entity.ImovelRural;
/**
 * Classe da tabela Imovel Rural
 * @author 
 *
 */
@Table(name="imovel_rural")
public class ImovelRuralQuestionario {
	private String numItr;
	
	private String desDenominacao;

	private Double valArea;

	private String desCartorio;

	private String desComarca;

	private String desLivro;

	private String numFolha;
	
	private String numMatricula;

	private String numCcir;
	
	private ImovelRural ideImovelRural;

	public String getNumItr() {
		return numItr;
	}

	public void setNumItr(String numItr) {
		this.numItr = numItr;
	}

	public String getDesDenominacao() {
		return desDenominacao;
	}

	public void setDesDenominacao(String desDenominacao) {
		this.desDenominacao = desDenominacao;
	}

	public String getDesCartorio() {
		return desCartorio;
	}

	public void setDesCartorio(String desCartorio) {
		this.desCartorio = desCartorio;
	}

	public String getDesComarca() {
		return desComarca;
	}

	public void setDesComarca(String desComarca) {
		this.desComarca = desComarca;
	}

	public String getDesLivro() {
		return desLivro;
	}

	public void setDesLivro(String desLivro) {
		this.desLivro = desLivro;
	}

	public String getNumFolha() {
		return numFolha;
	}

	public void setNumFolha(String numFolha) {
		this.numFolha = numFolha;
	}

	public String getNumMatricula() {
		return numMatricula;
	}

	public void setNumMatricula(String numMatricula) {
		this.numMatricula = numMatricula;
	}

	public String getNumCcir() {
		return numCcir;
	}

	public void setNumCcir(String numCcir) {
		this.numCcir = numCcir;
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}
}
