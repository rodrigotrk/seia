package br.gov.ba.seia.enumerator;

public enum AbaIdentificarPapelEnum {
	
	ABA_DADOS_BASICOS(		0,"ABA_DADOS_BASICOS",		PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_DADOS_BASICOS,"Dados básicos"),
	ABA_DOCUMENTOS(			1,"ABA_DOCUMENTOS",			PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_DOCUMENTOS,"Documentos"),
	ABA_CNAE(				2,"ABA_CNAE",				PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_DADOS_BASICOS,"CNAE"),
	ABA_COMP_ACIONARIA(		3,"ABA_COMP_ACIONARIA",		PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_DOCUMENTOS,"Composição Acionaria"),
	ABA_REPRESENTANTE_LEGAL(4,"ABA_REPRESENTANTE_LEGAL",PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_DOCUMENTOS,"Representante Legal"),
	ABA_TELEFONE(			5,"ABA_TELEFONE",			PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_TELEFONES,"Telefone"),
	ABA_ENDERECO(			6,"ABA_ENDERECO",			PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_ENDERECOS,"Endereço"),
	ABA_PROCURADOR(			7,"ABA_PROCURADOR",			PaginaEnum.IDENTIFICAR_SOCILITACAO_PESSOA_PROCURADORES,"Procurador");
	
	private Integer id;
	private String nome;
	private String nomeId;
	private PaginaEnum pagina;
	private boolean ativo;

	private AbaIdentificarPapelEnum(Integer id,String nomeId, PaginaEnum pagina,String nome) {
		this.id = id;
		this.nome = nome;
		this.nomeId = nomeId;
		this.pagina = pagina;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public PaginaEnum getPagina(){
		return pagina;
	}
	
	public String getNomeId(){
		return nomeId;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public AbaIdentificarPapelEnum getAbaEnum(Integer id){
		for (AbaIdentificarPapelEnum aba : AbaIdentificarPapelEnum.values()) {
			if(aba.getId().equals(id)){
				return aba;
			}
		}
		throw new IllegalArgumentException("Não encontrado");
	}
	
	public AbaIdentificarPapelEnum getAbaAtiva(){
		for (AbaIdentificarPapelEnum aba : AbaIdentificarPapelEnum.values()) {
			if(aba.isAtivo()){
				return aba;
			}
		}
		throw new IllegalArgumentException("Não encontrado");
	}
	
}
