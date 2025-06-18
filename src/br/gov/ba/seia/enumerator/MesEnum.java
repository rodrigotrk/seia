package br.gov.ba.seia.enumerator;

import java.util.Calendar;

public enum MesEnum {
	JANEIRO(1, "Janeiro", 31),
	FEVEREIRO(2, "Fevereiro", 28),
	MARCO(3, "Março", 31),
	ABRIL(4, "Abril", 30),
	MAIO(5, "Maio", 31),
	JUNHO(6, "Junho", 30),
	JULHO(7, "Julho", 31),
	AGOSTO(8, "Agosto", 31),
	SETEMBRO(9, "Setembro", 30),
	OUTUBRO(10, "Outubro", 31),
	NOVEMBRO(11, "Novembro", 30),
	DEZEMBRO(12, "Dezembro", 31);
	
	private String desc;
	private Integer id;
	private Integer qtdDias;
	
	MesEnum(Integer id){		
		this.id = id;
	}
	
	MesEnum(Integer id, String dsc){		
		this.id = id;
		this.desc = dsc;
	}
	
	MesEnum(Integer id, String dsc, Integer qtdDias){		
		this.id = id;
		this.desc = dsc;
		this.qtdDias = qtdDias;
	}
			
	@Override
	public String toString(){
		return this.desc;
	}
	
	public Integer getValue(){
		return id;
	}

	public Integer getQtdDias() {
		return qtdDias;
	}
	
	public String getNomMes(){
		return desc;
	}
	
	public static MesEnum getMesEnum(Integer ide) {
		MesEnum[] enums = MesEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getValue() == ide) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Mês não encontrado!");
	}
	
	public static MesEnum getMesEnum(Calendar calendar) {
		return getMesEnum(calendar.get(Calendar.MONTH) + 1);
	}
	
}
