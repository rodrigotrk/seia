package br.gov.ba.seia.middleware.sinaflor.model;

import java.util.List;
/**
 * Classe modelo de retorno paginado
 * @author 
 *
 */
public class RetornoPaginado<T> {
	
	private Integer paginaAtual;
	private	List<T> retorno;
	private Integer totalPaginas;
	
	public Integer getPaginaAtual() {
		return paginaAtual;
	}
	
	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}
	
	public Integer getTotalPaginas() {
		return totalPaginas;
	}
	
	public void setTotalPaginas(Integer totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public List<T> getRetorno() {
		return retorno;
	}

	public void setRetorno(List<T> retorno) {
		this.retorno = retorno;
	}

}
