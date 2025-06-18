package br.gov.ba.seia.enumerator;

public enum PerfilEnum {

	ADMINISTRADOR(1),
	USUARIO_EXTERNO(2),
	TECNICO(3),
	COORDENADOR(4),
    ATENDENTE(5),
    FINANCEIRO(6),
    ESTAGIARIO(11),
    USUARIO_MP(15),
    DIRETOR(9),    
    TEC_COAES(12),
	COORD_CTGA(13),
	TEC_CTGA(14),
	TECNICO_CONVENIADO(15), 
	ASSESSOR_CEFIR(29), 
	ASSESSOR(10),
	ATENDENTE_CEFIR(34),
	TEC_CEFIR(41),
	COORDENADOR_E_TECNICO(72),
	TEC_COGEC(36);

	private Integer id;

	private PerfilEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public static PerfilEnum getEnum(Integer id) {
		for (PerfilEnum  perfilEnum : PerfilEnum.values()) {
			if(perfilEnum.getId().equals(id)){
				return perfilEnum;
			}
		}
		
		throw new IllegalArgumentException("");
	}

}
