/**
 * 
 */
package br.gov.ba.ws.enumerator;

import br.gov.ba.seia.enumerator.TipoPessoaEnum;

/**
 * @author lesantos
 *
 */
public enum PapelSolicitanteEnum {

	PROPRIO_REQUERENTE(1, "O próprio Requerente", null),
	REPRESENTANTE_LEGAL_PESSOA_JURIDICA(2, "Representanate Legal de Pessoa Jurídica", TipoPessoaEnum.JURIDICA),
	PROCURADOR_PESSOA_FISICA(3, "O procurador de Pessoa Fisica", TipoPessoaEnum.FISICA),
	PROCURADOR_PESSOA_JURIDICA(4, "O procurador de Pessoa Jurídica", TipoPessoaEnum.JURIDICA);
	
	private int id;
	
	private String desc;
	
	private TipoPessoaEnum tipoPessoaEnum;
	
	private PapelSolicitanteEnum(int id, String desc, TipoPessoaEnum tipoPessoaEnum){
		this.id = id;
		this.desc = desc;
		this.tipoPessoaEnum = tipoPessoaEnum;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	
	@Override
	public String toString() {
		return this.desc;
	}

	/**
	 * @return the tipoPessoaEnum
	 */
	public TipoPessoaEnum getTipoPessoaEnum() {
		return tipoPessoaEnum;
	}

}
