package br.gov.ba.seia.interfaces;


/**
 * Interface dos FCE's
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 06/11/2014
 */
public interface FceInterface {

	public void init();

	public void verificarEdicao();

	public void carregarAba();

	public void finalizar();

	public void limpar();

	public boolean validarAba();
	
	public void abrirDialog();
	
	public void prepararParaFinalizar() throws Exception;
	
}