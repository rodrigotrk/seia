package br.gov.ba.seia.middleware.sinaflor.enumerator;
/**
 * Enum para tipo de atividade
 * @author 
 *
 */
public enum TipoAtividadeEnum {
	
	PMFS_AMAZONIA_BAIXA_INTENSIDADE(364),
	PMFS_AMAZONIA_PLENO(365),
	POA_AMAZONIA_BAIXA_INTENSIDADE(366),
	POA_AMAZONIA_PLENO(367),
	USO_ALTERNATIVO_DO_SOLO(370),
	AUTORIZACAO_DE_SUPRESSAO_DE_VEGETACAO_ASV(371),
	CORTE_DE_ARVORE_ISOLADA(374),
	EXPLORACAO_DE_FLORESTA_PLANTADA(929),
	AUMPF(1497),
	PMFS_OUTROS_BIOMAS_BAIXA_INTENSIDADE(1548),
	PMFS_OUTROS_BIOMAS_PLENO(1549),
	POA_OUTROS_BIOMAS_BAIXA_INTENSIDADE(1550),
	POA_OUTROS_BIOMAS_PLENO(1551)
	;
	
	private Integer id;
	
	private TipoAtividadeEnum(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
}
