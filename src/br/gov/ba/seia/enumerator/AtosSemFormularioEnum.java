package br.gov.ba.seia.enumerator;

public enum AtosSemFormularioEnum {
	RC(9, "RC", "Revisão de condicionante"),
	MNP(10,"MNP", "Manifestação prévia (MNP)"),
	ARLSF(13, "ARL/SF", "Autorização de Reserva Legal / Servidão ambiental"),
	ARTA(14, "ARTA", "Autorização de resgate e transporte de animais"),
	APMF(16,"APMF","Aprovação de Plano de Manejo Florestal sustentável (APMF)"),
	EPMF(17,"EPMF","Aprovação da Execução das Etapas do Plano de Manejo Florestal (EPMF)"),
	ACFP(20, "ACFP", "Aprovação de corte de floresta plantada"),
	AML(21, "AML", "Autorização para aproveitamento de material lenhoso (AML)"),
	RVFR(22, "RVFR", "Reconhecimento de Volume Florestal Remanescente"),
	PPV(23, "PPV", "Prorrogação de prazo de validade de licenças ambientais (PPV)"),
	RLA(40, "RLA", "Registro de Limpeza de Àrea"),
	LOA(5, "LOA", "Licença de Operação da Alteração"),
	LO(6, "LO", "Licença de Operação"),
	RLO(7, "RLO", "Renovação de Licença de Operação");
	
	private Integer ideAtoAmbiental;
	
	private String sglAtoAmbiental;
	
	private String nomAtoAmbiental;
	
	AtosSemFormularioEnum(Integer ideAtoAmbiental, String sglAtoAmbiental, String nomAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.sglAtoAmbiental = sglAtoAmbiental;
		this.nomAtoAmbiental = nomAtoAmbiental;
	}
	
	public Integer getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(Integer ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public String getSglAtoAmbiental() {
		return sglAtoAmbiental;
	}

	public void setSglAtoAmbiental(String sglAtoAmbiental) {
		this.sglAtoAmbiental = sglAtoAmbiental;
	}

	public String getNomAtoAmbiental() {
		return nomAtoAmbiental;
	}

	public void setNomAtoAmbiental(String nomAtoAmbiental) {
		this.nomAtoAmbiental = nomAtoAmbiental;
	}

}
