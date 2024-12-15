package br.gov.ba.seia.util;

import java.io.File;

public class Uri {

	public static final String LOGO_INEMA = 
			"logoInemaCabecalho.png";

	public static final String LOGO_SEFAZ = 
			"logo_sefaz_ba.jpg";
	
	public static final String LOGO_SEMA  = 
			"sema_vertical.jpg";
	
	public static final String MARCA_DAGUA = 
			"marca.png";
	
	public static final String URL_RELATORIO_PADRAO = 
			"WEB-INF" + File.separator + "classes" + File.separator + "br"	+ File.separator + "gov" + File.separator + "ba" + File.separator
			+ "seia" + File.separator + "reports" + File.separator ; 
	
	public static final String URL_RELATORIOS = 
			"WEB-INF" + File.separator + "classes" + File.separator + "br"	+ File.separator + "gov" + File.separator + "ba" + File.separator
			+ "seia" + File.separator + "reports" + File.separator + "sources"	+ File.separator;
	
	public static final String URL_RELATORIOS_NOVO_REQUERIMENTO = 
			URL_RELATORIOS + "novoRequerimento" + File.separator;

	private static final String URL_RELATORIOS_CADASTRO_SEM_LICENCIAMENTO = 
			URL_RELATORIOS + "cadastroAtividadeSemLicenca" + File.separator;
	
	public static final String URL_RELATORIOS_CAEPOG = 
			URL_RELATORIOS_CADASTRO_SEM_LICENCIAMENTO + "caepog" + File.separator;
	
	public static final String URL_RELATORIOS_PESQUISA_MINERAL = 
			URL_RELATORIOS_CADASTRO_SEM_LICENCIAMENTO + "pesquisaMineral" + File.separator;
	
	public static final String URL_RELATORIOS_SILOS_ARMAZENS = 
			URL_RELATORIOS_CADASTRO_SEM_LICENCIAMENTO + "silosArmazens" + File.separator;

	public static final String URL_RELATORIOS_INSTALACAO_DE_TORRES = 
			URL_RELATORIOS_CADASTRO_SEM_LICENCIAMENTO + "instalacaoDeTorres" + File.separator;
		
	public static final String RESOURCES  = 
			"resources" + File.separator;
	
	public static final String URL_IMAGEM =
			RESOURCES + "imagens";
	
	private static final String URL_RELATORIOS_ATO_DECLARATORIO = 
			URL_RELATORIOS + "atoDeclaratorio" + File.separator;
	
	public static final String URL_RELATORIOS_ATO_DIAP = 
			URL_RELATORIOS_ATO_DECLARATORIO + "diap" + File.separator;
	
	public static final String URL_RELATORIOS_ATO_RFP = 
			URL_RELATORIOS_ATO_DECLARATORIO + "rfp" + File.separator;
	
	public static final String URL_RELATORIOS_ATO_DQC = 
			URL_RELATORIOS_ATO_DECLARATORIO + "dqc" + File.separator;
	
	public static final String URL_RELATORIOS_ATO_DTRP = 
			URL_RELATORIOS_ATO_DECLARATORIO + "dtrp" + File.separator;
	
	public static final String URL_RELATORIOS_CADASTRO_ESTADUAL_USUARIO_RECURSO_HIDRICO = 
			URL_RELATORIOS + "cerh" + File.separator;
	
	public static final String URL_RELATORIOS_INEXIGIBILIDADE = 
			URL_RELATORIOS + "inexigibilidade" + File.separator;
	
	public static final String URL_DECLARACAO_REPOSICAO_FLORESTAL = 
			URL_RELATORIOS + "reposicaoFlorestal" + File.separator;
}
