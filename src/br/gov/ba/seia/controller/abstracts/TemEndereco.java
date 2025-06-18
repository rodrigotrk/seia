package br.gov.ba.seia.controller.abstracts;

import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.EnderecoGenericoController;
import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes 
 * @since 09/02/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
 *
 */
public abstract class TemEndereco {

	@Inject
	protected EnderecoGenericoController enderecoGenericoController;

	/**
	 * Método invocado pelo {@link EnderecoGenericoController} externamente.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @throws Exception
	 */
	public abstract void salvarEnderecoPessoaEndereco() throws Exception;
	
	/**
	 * Método criado para forçar a passagem do "idForm" para <b>super.enviarId(idForm);</b>
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/02/2017
	 */
	public abstract void enviarId();

	public void enviarId(String idForm){
		enderecoGenericoController.setIdForm(idForm);
		definirMetodoSalvarExterno();
	}
	
	private void definirMetodoSalvarExterno() {
		try {
			enderecoGenericoController.setMetodo(new MetodoUtil(this, getClass().getMethod("salvarEnderecoPessoaEndereco", null)));
		}
		catch (SecurityException e) {
			JsfUtil.addErrorMessage(Util.getString("")); /** ADICIONAR MSG DE ERRO*/
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		catch (NoSuchMethodException e) {
			JsfUtil.addErrorMessage(Util.getString("")); /** ADICIONAR MSG DE ERRO*/
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método criado para forçar a passagem do {@link Endereco} para <b>super.carregarEndereco(endereco);</b>
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/02/2017
	 */
	public abstract void prepararEnderecoGenericoController();
	
	/**
	 * Método que envia o {@link Endereco} para o {@link EnderecoGenericoController} e chama seu método de carregar.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
	 * @param endereco
	 */
	public void carregarEndereco(Endereco endereco){
		enderecoGenericoController.setEndereco(endereco);
		enderecoGenericoController.carregarEndereco(); 
	}
	
	public abstract void desabilitarParaVisualizacao();
	
	public void init(){
		enderecoGenericoController.setCepConsultado(null);
		enderecoGenericoController.setEndereco(null);
		enderecoGenericoController.init();
	}
}
