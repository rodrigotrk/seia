package br.gov.ba.seia.middleware.seia.model;
/**
 * Classe modelo para processo
 * @author 
 *
 */
public class Processo {
	
    private Integer ideProcesso;
    
    private String numProcesso;
    
    public Processo() {}

	public Integer getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Integer ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}
    
}
