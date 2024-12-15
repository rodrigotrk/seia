package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;

/**
 * ENUM para realizar a associação entre a {@link TipoFinalidadeUsoAgua} e o {@link DocumentoObrigatorio}.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 26/02/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
 */
public enum FceFinalidadeUsoAguaEnum {
	ABASTECIMENTO_HUMANO(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO, DocumentoObrigatorioEnum.FCE_OUTORGA_INTERVENCAO_ABASTECIMENTO_HUMANO),
	ABASTECIMENTO_INDUSTRIAL(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL, DocumentoObrigatorioEnum.FCE_ABASTECIMENTO_INDUSTRIAL),
	IRRIGACAO(TipoFinalidadeUsoAguaEnum.IRRIGACAO, DocumentoObrigatorioEnum.FCE_IRRIGACAO),
	PULVERIZACAO(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA, DocumentoObrigatorioEnum.FCE_PULVERIZACAO),
	DESSEDENTACAO_ANIMAL(TipoFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL, DocumentoObrigatorioEnum.FCE_DESSEDENTACAO_ANIMAL),
	AQUICULTURA_VIVEIRO_ESCAVADO(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO, DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA),
	AQUICULTURA_TANQUE_REDE(TipoFinalidadeUsoAguaEnum.AQUICULTURA_TANQUE_REDE, DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA),
	MINERACAO(TipoFinalidadeUsoAguaEnum.MINERACAO, DocumentoObrigatorioEnum.FCE_AUTORIZACAO_MINERACAO)
	;

	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
	private DocumentoObrigatorio ideDocumentoObrigatorio;

	private FceFinalidadeUsoAguaEnum(TipoFinalidadeUsoAguaEnum tipoFinalidadeUsoAguaEnum, DocumentoObrigatorioEnum docEnum){
		this.ideTipoFinalidadeUsoAgua = new TipoFinalidadeUsoAgua(tipoFinalidadeUsoAguaEnum.getId());
		this.ideDocumentoObrigatorio = new DocumentoObrigatorio(docEnum.getId());
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

}
