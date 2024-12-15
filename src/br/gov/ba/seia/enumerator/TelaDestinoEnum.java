package br.gov.ba.seia.enumerator;


@Deprecated /*Use Pagina Enum*/
public enum TelaDestinoEnum {
	
	/*
	 * dialogSelecionarSolicitante.xhtml 
	 */
	CONSULTAR_REQUERIMENTO(1, ""),
	IDENTIFICAR_PAPEL(2, "/paginas/identificar-papel/identificar-papel.xhtml"),
	CONSULTAR_CEFIR(3, ""),
	CONSULTAR_EMPREENDIMENTO(4, ""),
	CONSULTAR_BOLETO(5, ""),
	CONSULTAR_PROCESSO(6, ""),
	RELATORIO_FINANCEIRO(7, ""),
	
	/*
	 * dialogSelecionarEmpreendimento.xhtml
	 */
	CADASTRO_OLEO_GAS(8, "/paginas/manter-oleo-gas/cadastroCaepog.xhtml"),
	CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO(9, "/paginas/manter-atividade-nao-sujeita-licenciamento/consulta.xhtml"),
	IDENTIFICACAO_ATIVIDADE_SEM_LICENCIAMENTO(10, "/paginas/manter-atividade-nao-sujeita-licenciamento/identificarAtividade.xhtml"),
	PESQUISA_MINERAL_SEM_GUIA(11, "/paginas/manter-atividade-nao-sujeita-licenciamento/pesquisa-mineral/pesquisaMineralSemGuia.xhtml"),
	CADASTRO_EMPREENDIMENTO(12, "/paginas/manter-empreendimento/empreendimento.xhtml"),
	CADASTRO_CERH(13, "/paginas/manter-cerh/cadastro.xhtml"),
	CONSULTA_CERH(14, "/paginas/manter-cerh/consulta.xhtml"),

	
	/*
	 * Outras coisas
	 */
	
	LISTAR_EMPREENDIMENTO(15,"/paginas/manter-empreendimento/empreendimentoLst.xhtml"),
	TORRES_ANEMOMETRICCAS(16,"/paginas/manter-atividade-nao-sujeita-licenciamento/torres-anenometricas/cadastro.xhtml"),
	SILOS_ARMAZENS(17,"/paginas/manter-atividade-nao-sujeita-licenciamento/silos_armazens/silos_armazens_abas.xhtml");
	
	
	
	
	private String urlTela;
	private Integer ideTela;
	
	TelaDestinoEnum(Integer id, String url){
		this.ideTela = id;
		this.urlTela = url;
	}

	public String getUrlTela() {
		return urlTela;
	}

	public Integer getIdeTela() {
		return ideTela;
	}
	
	public static TelaDestinoEnum getEnum(Integer id) {
		TelaDestinoEnum[] enums = TelaDestinoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getIdeTela() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Tela de destino não encontrada!");
	}

}
