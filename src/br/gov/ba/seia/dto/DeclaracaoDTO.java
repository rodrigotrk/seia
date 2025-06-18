package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.DeclaracaoParcialDae;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes 
 * @since 25/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8447">#8447</a>
 *
 */
public class DeclaracaoDTO {

	private DeclaracaoParcialDae declaracao;

	private String numero;
	private String nomRequerente;
	private String numCpfCnpj;
	private String endereco;
	private boolean podeImprimir;
	
	public DeclaracaoDTO(DeclaracaoParcialDae DeclaracaoParcialDae){
		this.declaracao = DeclaracaoParcialDae;
		this.podeImprimir = true;
	}
	
	public DeclaracaoDTO(DeclaracaoParcialDae DeclaracaoParcialDae, Requerimento requerimento) {
		this(DeclaracaoParcialDae);
		this.numero = requerimento.getNumRequerimento();
		this.nomRequerente = requerimento.getRequerente().getNomeRazao();
		this.numCpfCnpj = requerimento.getRequerente().getCpfCnpjFormatado();
	}
		
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getNomRequerente() {
		return nomRequerente;
	}
	
	public void setNomRequerente(String nomRequerente) {
		this.nomRequerente = nomRequerente;
	}
	
	public String getNumCpfCnpj() {
		return numCpfCnpj;
	}
	
	public void setNumCpfCnpj(String numCpfCnpj) {
		this.numCpfCnpj = numCpfCnpj;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public DeclaracaoParcialDae getDeclaracao() {
		return declaracao;
	}
	
	public boolean isExisteDeclaracaoParcialDae(){
		return !Util.isNull(declaracao) && !Util.isNull(declaracao.getIdeDeclaracaoParcialDae()); 
	}
	
	public boolean isExisteTipoDeclaracaoParcialDae(){
		return !Util.isNull(declaracao.getTipoCertificado());
	}
		
	public boolean isPodeImprimir() {
		return podeImprimir;
	}
	
}