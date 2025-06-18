package br.gov.ba.seia.util;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;

public class AtoDeclaratorioUtil {

	
	public static boolean isFormularioAtoDeclaratorio(DocumentoObrigatorio documentoObrigatorio){
		return isDocumentoRFP(documentoObrigatorio) ||  isDocumentoDQC(documentoObrigatorio) || isDocumentoDIAP(documentoObrigatorio);
	}
	
	public static boolean isDocumentoRFP(DocumentoObrigatorio documentoObrigatorio){
		return documentoObrigatorio.equals(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FORMULARIO_RFP.getId()));
	}
	
	public static boolean isDocumentoDQC(DocumentoObrigatorio documentoObrigatorio){
		return documentoObrigatorio.equals(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FORMULARIO_DQC.getId()));
	}
	
	public static boolean isDocumentoDIAP(DocumentoObrigatorio documentoObrigatorio){
		return documentoObrigatorio.equals(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FORMULARIO_DIAP.getId()));
	}
	
	public static String retornarCaminhoJasper(DocumentoObrigatorioEnum docObrigatorioEnum){
		if(docObrigatorioEnum.equals(DocumentoObrigatorioEnum.FORMULARIO_DIAP)){
			return Uri.URL_RELATORIOS_ATO_DIAP;
		} 
		else if(docObrigatorioEnum.equals(DocumentoObrigatorioEnum.FORMULARIO_DQC)){
			return Uri.URL_RELATORIOS_ATO_DQC;
		}
		else if(docObrigatorioEnum.equals(DocumentoObrigatorioEnum.FORMULARIO_RFP)){
			return Uri.URL_RELATORIOS_ATO_RFP;
		}
		else if(docObrigatorioEnum.equals(DocumentoObrigatorioEnum.DTRP_DOCUMENTO_1)
				|| docObrigatorioEnum.equals(DocumentoObrigatorioEnum.DTRP_DOCUMENTO_2)
				){
			return Uri.URL_RELATORIOS_ATO_DTRP;
		}
		return null;
	}
	
}
