package br.gov.ba.seia.util;

import javax.xml.bind.JAXBException;

import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao;
import br.gov.ba.seia.dto.EmitirDocumentoArrecadacaoResponseDTO.RetEmissaoDocumentoArrecadacaoDTO;
import br.gov.ba.seia.middleware.sefaz.service.SefazService;

import com.google.gson.Gson;

/**
 * @author lesantos
 *
 */
public final class SefazUtil {

	// URL SEFAZ
	public static final int SEQ_DOC_ARREACADACAO = 2;
	public static final int RET_COD_BARRA = 1;
	public static final int TIPO_DAE = 2;

	private SefazUtil() {
	}

	public static RetEmissaoDocumentoArrecadacaoDTO emitirDae(DocumentoArrecadacao documentoArrecadacao) throws Exception {
		String xmlResult = getResult(documentoArrecadacao);

		return unmarshal(xmlResult);
	}

	private static String getResult(DocumentoArrecadacao documentoArrecadacao) throws Exception {
		return SefazService.enviar(documentoArrecadacao);
	}
	
	private static RetEmissaoDocumentoArrecadacaoDTO unmarshal(String xmlResponse) throws JAXBException {
		return (RetEmissaoDocumentoArrecadacaoDTO) new Gson().fromJson(xmlResponse, RetEmissaoDocumentoArrecadacaoDTO.class);
	}

	public static String removeCaracterInvalido(String value){
		return value.replace("º", "").replace("ª", "");
	}
}
