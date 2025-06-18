package br.gov.ba.seia.enumerator;


public enum TipoLocalizacaoGeograficaPerguntaEnum {
	SEM_LOCALIZACAO(1),
	SOMENTE_SHAPE(2),
	SOMENTE_PONTO(3),
	COM_LOCALIZACAO_QUALQUER(4),
	SOMENTE_SHAPE_SE_SIM(5),
	SOMENTE_PONTO_SE_NAO(6),
	SE_SIM_SHAPE_SE_NAO_PONTOS(7),
	SHAPE_E_DESENEHO(8);
	
	private Integer ide;
	
	private TipoLocalizacaoGeograficaPerguntaEnum(Integer i) {
		ide = i;
	}
	
	public Integer getIde() {
		return ide;
	}
	
	public String getNomeTipoTipoLocalizacaoGeograficaPerguntaEnum() {
		if(this.ide.equals(1))
			return "Sem Localização";
		else if(this.ide.equals(2))
			return "Somente Shape";	
		else if(this.ide.equals(3))
			return "Somente Ponto";
		else if(this.ide.equals(4))
			return "Com Localização Qualquer";
		else if(this.ide.equals(5))
			return "Somente Shape se Sim";
		else if(this.ide.equals(6))
			return "Somente Shape se Não";
		else if(this.ide.equals(7))
			return "Se sim Shape e não Pontos";
		else if(this.ide.equals(8))
			return "Shape e Desenho";
		else
			return "ItemInvalido";
	}
	
	
}
