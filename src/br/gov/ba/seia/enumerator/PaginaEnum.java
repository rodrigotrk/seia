package br.gov.ba.seia.enumerator;

public enum PaginaEnum {


	CONSULTA_TCCA(-1,"/paginas/manter-tcca/consultaTcca.xhtml"),
	CONSULTA_PROJETO(-1,"/paginas/manter-tcca/consultaProjeto.xhtml"),
	CADASTRO_PROJETO(-1,"/paginas/manter-tcca/cadastroTcca.xhtml"),
	CADASTRO_TCCA(-1,"/paginas/manter-tcca/cadastroTcca.xhtml"),
	CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO(9, "/paginas/manter-atividade-nao-sujeita-licenciamento/consulta.xhtml"),
	CADASTRO_CERH(13, "/paginas/manter-cerh/cadastro.xhtml"),
	CADASTRO_EMPREENDIMENTO(12, "/paginas/manter-empreendimento/empreendimento.xhtml"),
	CADASTRO_OLEO_GAS(8, "/paginas/manter-oleo-gas/cadastroCaepog.xhtml"),

	CADASTRO_IMOVEL_RURAL(-1,"/paginas/manter-imoveis/cefir/cadastroImovelRural.xhtml"),

	CONSULTAR_BOLETO(5, ""),
	CONSULTAR_CERH(14, "/paginas/manter-cerh/consulta.xhtml"),
	CONSULTAR_CEFIR(3, ""),
	CONSULTAR_EMPREENDIMENTO(4, ""),
	CONSULTAR_PROCESSO(6, ""),
	CONSULTAR_REQUERIMENTO(1,""),

	IDENTIFICAR_PAPEL(2, "/paginas/identificar-papel/identificar-papel.xhtml"),
	IDENTIFICACAO_ATIVIDADE_SEM_LICENCIAMENTO(10, "/paginas/manter-atividade-nao-sujeita-licenciamento/identificarAtividade.xhtml"),
	IDENTIFICACAO_PAPEL_ATIVIDADE_SEM_LICENCIAMENTO(16, "/paginas/manter-atividade-nao-sujeita-licenciamento/identificarPapel.xhtml"),

	IDENTIFICAR_SOCILITACAO_PESSOA_DADOS_BASICOS(-1,"/paginas/identificar-papel-solicitacao/pessoa-fisica/abas/dados-basicos.xhtml"),
	IDENTIFICAR_SOCILITACAO_PESSOA_DOCUMENTOS(-1,"/paginas/identificar-papel-solicitacao/pessoa-fisica/abas/documentos.xhtml"),
	IDENTIFICAR_SOCILITACAO_PESSOA_TELEFONES(-1,"/paginas/identificar-papel-solicitacao/pessoa-fisica/abas/telefones.xhtml"),
	IDENTIFICAR_SOCILITACAO_PESSOA_ENDERECOS(-1,"/paginas/identificar-papel-solicitacao/pessoa-fisica/abas/enderecos.xhtml"),
	IDENTIFICAR_SOCILITACAO_PESSOA_PROCURADORES(-1,"/paginas/identificar-papel-solicitacao/pessoa-fisica/abas/procuradores.xhtml"),


	LISTAR_EMPREENDIMENTO(15,"/paginas/manter-empreendimento/empreendimentoLst.xhtml"),

	RELATORIO_FINANCEIRO(7, ""),

	PESQUISA_MINERAL_SEM_GUIA(11, "/paginas/manter-atividade-nao-sujeita-licenciamento/pesquisa-mineral/pesquisaMineralSemGuia.xhtml"),
	PAUTA_REENQUADRAMENTO(-1, "/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml"),
	EXIBIR_COMUNICACAO_USUARIO(-1,"/paginas/comunicacao/consulta-usuario.xhtml")
	;

	private Integer idePagina;
	private String url;

	PaginaEnum(Integer idePagina, String url){
		this.idePagina = idePagina;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public Integer getIdePagina() {
		return idePagina;
	}

	public void setIdePagina(Integer idePagina) {
		this.idePagina = idePagina;
	}

	public static PaginaEnum getEnum(Integer id) {
		for(int i = 0; i < PaginaEnum.values().length ; i++){
			if(PaginaEnum.values()[i].getIdePagina() == id) {
				return PaginaEnum.values()[i];
			}
		}
		throw new IllegalArgumentException("Não Encontrado!");
	}

}
