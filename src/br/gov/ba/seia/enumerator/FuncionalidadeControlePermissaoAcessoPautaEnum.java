package br.gov.ba.seia.enumerator;

public enum FuncionalidadeControlePermissaoAcessoPautaEnum {
	
	PERMISSAO_DE_ACESSO_A_PAUTA(53),
	PAUTA_DA_AREA(17),
	PAUTA_DO_GESTOR(18);
	
	private int id;
	
	private FuncionalidadeControlePermissaoAcessoPautaEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

}
