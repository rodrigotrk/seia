package br.gov.ba.seia.enumerator;


public enum ItemCronogramaEnum {
	REUNIAO(1),
	REALIZAR_INSPECAO(2),
	CONCLUSAO_DE_PARECER(3),
	AUDIENCIA_PUBLICA(4);
	
	private Integer ide;
	
	private ItemCronogramaEnum(Integer i) {
		ide = i;
	}
	
	public Integer getIde() {
		return ide;
	}
	
	public String getNomeItemCronograma() {
		if(ide.equals(1))
			return "Reunião";
		else if(ide.equals(2))
			return "Realizar Inspeção";
		else if(ide.equals(3))
			return "Conclusão de Parecer";
		else if(ide.equals(4))
			return "Audiência Pública";
		else
			return "ItemInvalido";
	}
	
	public String getNomeItemCronograma(Integer ide) {
		if(ide.equals(1))
			return "Reunião";
		else if(ide.equals(2))
			return "Realizar Inspeção";
		else if(ide.equals(3))
			return "Conclusão de Parecer";
		else if(ide.equals(4))
			return "Audiência Pública";
		else
			return "ItemInvalido";
	}
	
}
