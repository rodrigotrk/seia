package br.gov.ba.seia.interfaces;


/**
 * @author italo.santos
 * interface utilizada sempre que uma tabela será mapeada para auditoria.
 */
public interface Auditoria {
	
	 public Integer getIdePessoaFisicaUsuario();
	 
	 public void setIdePessoaFisicaUsuario(Integer idePessoaFisicaUsuario);
	 
	 public String getEnderecoIp();
	 
	 public void setEnderecoIp(String enderecoIp);
	 
	 public String getCaminhoRequisicao();
	 
	 public void setCaminhoRequisicao(String caminhoRequisicao);
	 
	 /**
	 * @throws Exception
	 * método utilizado para preencher os atributos obrigatórios para auditoria.
	 */
	 public void capturarCamposAuditoria();
}
