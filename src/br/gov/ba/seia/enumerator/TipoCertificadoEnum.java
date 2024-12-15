package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.TipoCertificado;

public enum TipoCertificadoEnum {

	DLA(1, "DLA"),
	CEFIR(2, "CEFIR"),
	TERMO_DE_COMPROMISSO(3, "TC"),
	AVISO_BNDES(4, "AVBNDES"),
	CAEPOG(5, "CAEPOG"),
	PESQUISA_MINERAL(6, "CPM"),
	CERH(7, "CERH"),
	CRF(8, "CRF"),
	SILOS_ARMAZEM(9, "CSA"),
	TORRES_ANEMOMETRICAS(10, "TORRES");

	private Integer id;
	private String sigla;

	private TipoCertificadoEnum(Integer id, String sigla) {
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

	public static TipoCertificadoEnum getEnum(Integer id) {
		TipoCertificadoEnum[] enums = TipoCertificadoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Tela de certificado não encontrado!");
	}
	
	public static String getDscTipoCertificado(TipoCertificado tipoCertificado) {
		return getEnum(tipoCertificado.getIdeTipoCertificado()).getSigla();
	}
}
