package br.gov.ba.seia.middleware.sinaflor.enumerator;
/**
 * Enum do Sinaflor Api
 * @author 
 *
 */
public enum SinaflorApiEnum {
	
	AUTENTICACAO("/public/autenticar/sistema-integrado"),
	EMPREENDIMENTOS("/empreendimentos"),
	MUNICIPIOS("/municipios"),
	NOMES_POPULARES("/nomespopulares"),
	PESSOAS("/pessoas"),
	PRODUTOS_FLORESTAIS("/produtosflorestais"),
	PROJETOS("/projetos"),
	TAXONOMIAS("/taxonomias"),
	TOKEN("/token"),
	UNIDADES_IBAMA("/unidadesibama")
	;
	
	private String servico;
	
	private SinaflorApiEnum(String servico) {
		this.servico = servico;
	}
	
	public String toString() {
		return this.servico;
	}
}
