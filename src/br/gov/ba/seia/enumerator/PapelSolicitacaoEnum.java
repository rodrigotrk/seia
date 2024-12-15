package br.gov.ba.seia.enumerator;

public enum PapelSolicitacaoEnum {

	REQUERENTE(1, "O próprio usuário", "Selecione essa opção caso queira que o cadastro seja emitido em nome da pessoa física logada no sistema", true),
	REPRESENTANTE_LEGAL_PJ(2,"Representante legal de pessoa jurídica", "Selecione essa opção caso você seja um representante legal de uma referida empresa e queira que o cadastro seja emitido em nome deste CNPJ" , true),
	PROCURADOR_PF(3,"Procurador de pessoa física","Selecione essa opção caso você seja um procurador de pessoa física e queira que o cadastro seja emitido em nome desta pessoa física", true),
	PROCURADOR_PJ(4,"Procurador de pessoa jurídica","Selecione essa opção caso você seja um procurador de um CNPJ e queira que o cadastro seja emitido em nome do referido CNPJ", true),
	CONVENIO_PESSOA_FISICA(5,"Convênio de pessoa física", "Selecione esse opção caso voce estaja vinculado a um convênio e queira cadastrar para uma pessoa física" , false),
	CONVENIO_PESSOA_JURIDICA(6, "Convênio de pessoa juridica", "Selecione esse opção caso voce estaja vinculado a um convênio e queira cadastrar para uma pessoa jurídica", false);

	private int id;
	private String nome;
	private String dica;
	private boolean obrigatorio;

	private PapelSolicitacaoEnum(int id, String nome, String dica, boolean obrigatorio){
		this.id = id;
		this.nome = nome;
		this.dica = dica;
		this.obrigatorio = obrigatorio;

	}

	public int getId(){
		return this.id;
	}

	public String getNome(){
		return nome;
	}

	public String getDica() {
		return dica;
	}

	public boolean isObrigatorio() {
		return obrigatorio;
	}
}
