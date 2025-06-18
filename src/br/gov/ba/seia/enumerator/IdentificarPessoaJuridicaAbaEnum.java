package br.gov.ba.seia.enumerator;

public enum IdentificarPessoaJuridicaAbaEnum {
	
	PESSOA_JURIDICA(0, "Pessoa Jurídica"),
	CNAE(1,"CNAE"),
	COMPOSICAO_ACIONARIA(2,"Comp. Acionária"),
	REPRESENTANTE_LEGAL(3,"Representante Legal"),
	TELEFONES(4,"Telefone"),
	ENDERECOS(5,"Endereço"),
	PROCURADORES(6,"Procuradores");
	
	private int id;
	private String nome;
	
	private IdentificarPessoaJuridicaAbaEnum(int id, String nome){
		this.id = id;
		this.nome = nome;
	}
	
	public int getId(){
		return id;	
	}
	
	public String getNome() {
		return nome;
	}

	public static IdentificarPessoaJuridicaAbaEnum getEnum(Integer id) {
		for(IdentificarPessoaJuridicaAbaEnum identificarPessoaJuridicaAbaEnum: IdentificarPessoaJuridicaAbaEnum.values()){
			if(identificarPessoaJuridicaAbaEnum.getId() == id){
				return identificarPessoaJuridicaAbaEnum;
			}
		}
		
		throw new IllegalArgumentException("Não Encontrado!");
	}
}
