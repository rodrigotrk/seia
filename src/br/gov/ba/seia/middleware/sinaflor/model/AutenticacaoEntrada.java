package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo autenticacao de entrada
 * @author 
 *
 */
public class AutenticacaoEntrada {
	
	private	String chaveAcesso;
	private	Integer codigo;
	
	public AutenticacaoEntrada() {
		
	}
	
	public AutenticacaoEntrada(String chaveAcesso, Integer codigo) {
		this.chaveAcesso=chaveAcesso;
		this.codigo=codigo;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

}
