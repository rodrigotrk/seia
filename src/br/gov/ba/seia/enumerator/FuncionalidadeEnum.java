package br.gov.ba.seia.enumerator;

public enum FuncionalidadeEnum {
	
	ACOES(1),
	AREAS(2),
	SECOES(3),
	FUNCIONALIDADES(4),
	ORGOES(5),
	RELACAO_DE_ORGAO_E_MUNICIPIO(6),
	PERFIS(7),
	USUARIO_INTERNO(8),
	USUARIO_EXTERNO(9),
	CONTROLE_DE_PERMISSOES(10),
	ANEXO_III(11),
	PESSOA_FISICA(12),
	PESSOA_JURIDICA(13),
	EMPREENDIMENTO(14),
	CONSULTA_DE_REQUERIMENTOS(15),
	LICENCIAMENTO_AUTORIZACAO(16),
	PAUTA_DA_AREA(17),
	PAUTA_DO_GESTOR(18),
	PAUTA_DO_TECNICO(19),
	CONSULTA_DE_PROCESSO(20),
	CONSULTA_DAS_NOTIFICACOES(21),
	DOCUMENTO_OBRIGATORIO(22),
	ATO_AMBIENTAL(23),
	EFETUAR_ENQUADRAMENTO(24),
	ENVIAR_DOCUMENTACAO_OBRIGATORIA(25),
	EFETUAR_VALIDACAO_PREVIA(26),
	GRAVAR_TAXA_BOLETO(27),
	ENVIAR_COMPROVANTE_DE_PAGAMENTO(28),
	VALIDAR_COMPROVANTE_DE_PAGAMENTO(29),
	REATIVACAO_DE_USUARIO(30),
	TIPOLOGIA(31),
	RELATORIO_QUANTITATIVO(32),
	IMOVEL_RURAL(33),
	RESUMO_REQUERIMENTO(34),
	RELATORIO_ATO_DECLARATORIO(35),
	RELATORIO_DLA(36),
	LAC_DE_ERB(37),
	LAC_DE_POSTO(38),
	RELATORIO_FINAL_LAC_ERB(39),
	RELATORIO_FINAL_LAC_POSTO(40),
	CERTIFICADO_LAC_DE_ERB(41),
	CERTIFICADO_LAC(42),
	CERTIFICADO_LAC_POSTO(43),
	CERTIFICADO_CORTE_FLORESTA(44),
	CERTIFICADO_FLORESTA_PRODUCAO_PLANTADA(45),
	HISTORICO_COMUNICACAO_REQUERENTE(46),
	CONSULTA_STATUS(47),
	AUDITORIA(48),
	RELATORIO_QUANTITATIVO_IMOVEL(49),
	RELATORIO_FINANCEIRO(50),
	GEOBAHIA(51),
	CONTROLE_DAS_PAUTAS(52),
	PERMISSAO_DE_ACESSO_A_PAUTA(53),
	CERTIFICADO_LAC_TRANSPORTADORA(54),
	RELATORIO_FINAL_LAC_TRANSPORTADORA(55),
	BOLETO_COMPLEMENTAR_INTERNO(56),
	BOLETO_COMPLEMENTAR_EXTERNO(57),
	CONSULTA_PUBLICA(58),
	RETORNO_DE_STATUS(59),
	REATIVAR_PESSOA(60),
	SINCRONIA_SICAR(61),
	CERTIFICADO_RLAC(62),
	CONTRATO_CONVENIO(63),
	RELATORIO_IMOVEL_RURAL(64),
	IMPORTACAO_IMOVEIS_CDA(65),
	ATIVIDADES_DE_EXPLORACAO_E_PRODUCAO_DE_OLEO_E_GAS(66),
	TERMO_DE_COMPROMISSO_AMBIENTAL_TCCA(67),
	CERTIFICADO_AUTORIZACAO_POR_PROCEDIMENTO_ESPECIAL(68),
	ATIVIDADES_NAO_SUJEITAS_A_LICENCIAMENTO_AMBIENTAL(69),
	IMPRIMIR(70),
	IDENTIFICAR_TIPO_DE_SOLICITACAO(71),
	DECLARACAO_DE_TRANSPORTE_DE_RESIDUOS_PERIGOSOS(72),
	CERH(73),
//	CONTRATO/_CONVENIO(74),
//	IMPORTACAO_IMOVEIS_CDA(75),
//	ATIVIDADE_DE_EXPLORACAO_E_PRODUCAO_DE_OLEO_E_GAS(76),
//	ATIVIDADE_NAO_SUJEITAS_A_LICENCIAMENTO_AMBIENTAL(77),
//	IMPRIMIR(78),
//	CERH(79),
//	IMOVEL_RURAL(80),
//	IMOVEL_RURAL(81),
	CONSULTAR_LOTES_DE_BOLETO(82),
	CONSULTAS_E_CADASTRO_DE_CONDICIONANTE(83),
	CONSULTAS_E_CADASTRO_DE_SEGMENTO(84),
	REENQUADRAMENTO_DE_PROCESSO(85),
	DECLARACAO_DE_INEXIGIBILIDADE(86),
	SUSPENSAO_DE_CADASTRO(87),
	SHAPES_CEFIR(88),
	CONTROLE_DE_MUNICIPIOS(89),
	ATIVIDADE_INEXIGIVEL(90),
	RESIDUOS_PERIGOSOS(91),
	SINAFLOR(92),
	CONVEIO_CERH(95)
	;
	
	private int id;
	
	private FuncionalidadeEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}
}