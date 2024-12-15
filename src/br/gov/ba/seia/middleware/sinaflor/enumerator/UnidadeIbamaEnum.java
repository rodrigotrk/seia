package br.gov.ba.seia.middleware.sinaflor.enumerator;
/**
 * Enum do ibama
 * @author 
 *
 */
public enum UnidadeIbamaEnum {
	
	INEMA(40394);
	
	private Integer id;
	
	private UnidadeIbamaEnum(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
}
