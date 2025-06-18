package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.AtoAmbiental;

public enum TipoStatusProcessoAtoEnum {

	NULO(0, "Nulo"),
	AGUARDANDO_ANALISE(1,"Aguardando análise"),
	EM_ANALISE(2,"Em análise"),
	DEFERIDO_PELO_TECNICO(3,"Deferido pelo técnico"),
	INDEFERIDO_PELO_TECNICO(4,"Indeferido pelo técnico"),
	DEFERIDO(5,"Deferido"),
	INDEFERIDO(6,"Indeferido"),
	TRANSFERIDO(7,"Transferido"),
	EMITIDO(8,"Emitido"),
	ALTERADO(9,"Alterado");

	private Integer id;
	private String sigla;

	private TipoStatusProcessoAtoEnum(Integer id, String sigla) {
		this.id = id;
		this.sigla = sigla;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public static TipoStatusProcessoAtoEnum getEnum(AtoAmbiental atoAmbiental) {
		TipoStatusProcessoAtoEnum[] enums = TipoStatusProcessoAtoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == atoAmbiental.getIdeAtoAmbiental()) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Ato Ambiental não encontrado!");
	}

}
