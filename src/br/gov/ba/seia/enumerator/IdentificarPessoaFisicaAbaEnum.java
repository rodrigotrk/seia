package br.gov.ba.seia.enumerator;

public enum IdentificarPessoaFisicaAbaEnum {
	
	DADOS_BASICOS(0, "Dados básicos","DADOS_BASICOS"),
	DOCUMENTOS(1,"Documentos", "DOCUMENTOS"),
	TELEFONES(2,"Telefones", "TELEFONES"),
	ENDERECOS(3,"Endereços", "ENDERECOS"),
	PROCURADORES(4,"Procuradores", "PROCURADORES");
	
	private int id;
	private String nome;
	private String enumName;
	
	private IdentificarPessoaFisicaAbaEnum(int id, String nome, String enumName){
		this.id = id;
		this.nome = nome;
		this.enumName = enumName;
		
	}
	
	public int getId(){
		return id;	
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	
	public String getEnumName() {
		return enumName;
	}

	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}

	public static IdentificarPessoaFisicaAbaEnum getEnum(Integer id) {
		for(IdentificarPessoaFisicaAbaEnum identificarPessoaFisicaAbaEnum: IdentificarPessoaFisicaAbaEnum.values()){
			if(identificarPessoaFisicaAbaEnum.getId() == id) {
				return identificarPessoaFisicaAbaEnum;
			}
		}
		throw new IllegalArgumentException("Não Encontrado!");
	}
	
	public static IdentificarPessoaFisicaAbaEnum getEnum(String nome) {
		for(IdentificarPessoaFisicaAbaEnum identificarPessoaFisicaAbaEnum: IdentificarPessoaFisicaAbaEnum.values()){
			if(identificarPessoaFisicaAbaEnum.getEnumName().equalsIgnoreCase(nome)) {
				return identificarPessoaFisicaAbaEnum;
			}
		}
		throw new IllegalArgumentException("Não Encontrado!");
	}
}
