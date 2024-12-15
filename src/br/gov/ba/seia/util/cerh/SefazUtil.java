package br.gov.ba.seia.util.cerh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO;
import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao;
import br.gov.ba.seia.dto.EmitirDocumentoArrecadacaoResponseDTO.RetEmissaoDocumentoArrecadacaoDTO;
import br.gov.ba.seia.dto.EntEmissaoDocumentoArrecadacaoDTO;

/**
 * Classe utilitária da sefaz
 * @author lesantos
 *
 */
public final class SefazUtil {

	private static final String STRING_VAZIA = "";
	// URL SEFAZ
	private static final String END_POINT = "https://hwebservices.sefaz.ba.gov.br/webservices/WARR/EmissaoDocumentoArrecadacao/warrEmissaoDocumentoArrecadacao.asmx";
	private static final String VERSAO_APP_SEFAZ = "WARR_SEFAZBA_v1.00";
	private static final String NAMESPACE = "xmlns=\"http://www.sefaz.ba.gov.br/warr\"";
	private static final String TAG_EMITIR_DOCUMENTO_ARRECADACAO_RESULT = "<emitir_documento_arrecadacaoResult>";
	private static final String TAG_FECHADA_EMITIR_DOCUMENTO_ARRECADACAO_RESULT = "</emitir_documento_arrecadacaoResult>";
	private static final String EMITIR_DOC = "emitir_documento_arrecadacao";
	private static final String OBTER_CAMPOS = "obter_campos_obrigatorios";
	private static final String TP_AMB = "2";
	private static final int COD_TIPO_PROCESSAMENTO = 2;

	public static final int SEQ_DOC_ARREACADACAO = 2;
	public static final int RET_COD_BARRA = 1;
	public static final int TIPO_DAE = 2;

	private SefazUtil() {
	}

	/**
	 * lê o certificado gerado pela SEFAZ
	 */
	private static void carregarCertificado() {
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		String jksCertify = SefazUtil.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath()
				+ "/" + SefazUtil.class.getCanonicalName().replace(".", "/");
		jksCertify = jksCertify.replace(SefazUtil.class.getSimpleName(),
				"certificado/sefaz.jks");
		System.setProperty("javax.net.ssl.trustStore", jksCertify);
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
	}

	/**
	 * @param request
	 * @return result
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static RetEmissaoDocumentoArrecadacaoDTO emitirDae(
			DocumentoArrecadacao documentoArrecadacao) throws IOException,
			JAXBException {
		String xmlResult = getResult(EMITIR_DOC,
				getXMLRequest(documentoArrecadacao));
		System.out.println(xmlResult);
		return unmarshal(xmlResult);
	}
	/**
	 * 
	 * @param documentoArrecadacao
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static String obterCampos(DocumentoArrecadacao documentoArrecadacao)
			throws IOException, JAXBException {
		return getResult(OBTER_CAMPOS, getXMLRequest(documentoArrecadacao));
	}
	/**
	 * 
	 * @param metod
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private static String getResult(String metod, String request)
			throws IOException {
		System.out.println(request);
		carregarCertificado();
		URL URL = new URL(END_POINT);
		HttpURLConnection con = (HttpURLConnection) URL.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
		con.setRequestProperty("SOAPAction", "http://www.sefaz.ba.gov.br/warr/"
				+ metod);
		OutputStream reqStream = con.getOutputStream();
		reqStream.write(request.trim().getBytes());
		InputStream resStream = con.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(resStream, "utf-8"));
		StringBuilder sb = new StringBuilder();
		String l = null;
		while ((l = bufferedReader.readLine()) != null) {
			sb.append(l.trim() + "\n");
		}
		bufferedReader.close();

		return sb.toString();
	}
	/**
	 * 
	 * @param documentoArrecadacao
	 * @return
	 * @throws JAXBException
	 */
	private static String getXMLRequest(
			DocumentoArrecadacao documentoArrecadacao) throws JAXBException {
		StringWriter stringWriterRequest = marshal(getEntEmissaoDocumentoArrecadacaoDTO(documentoArrecadacao));
		return getXML(stringWriterRequest).toString();
	}


	/**
	 * 
	 * @param retEmissaoDocumentoArrecadacaoDTO
	 */
	public static void validacaoResponse(
			RetEmissaoDocumentoArrecadacaoDTO retEmissaoDocumentoArrecadacaoDTO) {
		// Fazer.
	}
	/**
	 * 
	 * @param xmlResponse
	 * @return
	 * @throws JAXBException
	 */
	private static RetEmissaoDocumentoArrecadacaoDTO unmarshal(
			String xmlResponse) throws JAXBException {

		xmlResponse = xmlResponse
				.substring(
						xmlResponse
								.indexOf(TAG_EMITIR_DOCUMENTO_ARRECADACAO_RESULT)
								+ TAG_EMITIR_DOCUMENTO_ARRECADACAO_RESULT
										.length(),
						xmlResponse
								.indexOf(TAG_FECHADA_EMITIR_DOCUMENTO_ARRECADACAO_RESULT))
				.replace(NAMESPACE, STRING_VAZIA);

		JAXBContext jaxbContext = JAXBContext
				.newInstance(RetEmissaoDocumentoArrecadacaoDTO.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		return (RetEmissaoDocumentoArrecadacaoDTO) jaxbUnmarshaller
				.unmarshal(new StringReader(xmlResponse));
	}
	/**
	 * 
	 * @param request
	 * @return stringWriterRequest
	 * @throws JAXBException
	 */
	private static StringWriter marshal(
			EntEmissaoDocumentoArrecadacaoDTO request) throws JAXBException {
		StringWriter stringWriterRequest = new StringWriter();
		JAXBContext jaxbContext = JAXBContext
				.newInstance(EntEmissaoDocumentoArrecadacaoDTO.class);

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
		marshaller.marshal(request, stringWriterRequest);

		return stringWriterRequest;
	}
	/**
	 * 
	 * @param stringWriterRequest
	 * @return xml
	 */
	private static StringBuilder getXML(StringWriter stringWriterRequest) {
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xml.append("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:warr=\"http://www.sefaz.ba.gov.br/warr\">");
		xml.append("<soap:Header>");
		xml.append("<warr:CabecMsg>");
		xml.append("<warr:Projeto>SIGNT</warr:Projeto>");
		xml.append("<warr:VersaoXSD>1.00</warr:VersaoXSD>");
		xml.append("</warr:CabecMsg>");
		xml.append("</soap:Header>");
		xml.append("<soap:Body>");
		xml.append("<warr:emitir_documento_arrecadacao>");
		xml.append("<warr:Pxml_requisicao><![CDATA[");
		xml.append(stringWriterRequest.toString().replace("ns2:", STRING_VAZIA)
				.replace(":ns2", STRING_VAZIA));
		xml.append("]]>");
		xml.append("</warr:Pxml_requisicao>");
		xml.append("</warr:emitir_documento_arrecadacao>");
		xml.append("</soap:Body>");
		xml.append("</soap:Envelope>");
		return xml;
	}
	/**
	 * 
	 * @param documentoArrecadacao
	 * @return request
	 */
	private static EntEmissaoDocumentoArrecadacaoDTO getEntEmissaoDocumentoArrecadacaoDTO(
			DocumentoArrecadacao documentoArrecadacao) {
		EntEmissaoDocumentoArrecadacaoDTO request = new EntEmissaoDocumentoArrecadacaoDTO();
		request.setTpAmb(TP_AMB);
		request.setVerAplic(VERSAO_APP_SEFAZ);
		request.setCod_tipo_processamento(COD_TIPO_PROCESSAMENTO);
		EmissaoDocumentoArrecadacaoReqDTO emissaoDocumentoArrecadacao = new EmissaoDocumentoArrecadacaoReqDTO();
		emissaoDocumentoArrecadacao
				.setDocumento_arrecadacao(documentoArrecadacao);
		request.setEmissao_documento_arrecadacao(emissaoDocumentoArrecadacao);
		return request;
	}
	/**
	 * Metodo para remover caracter invalido
	 * @param value
	 * @return
	 */
	public static String removeCaracterInvalido(String value){
		return value.replace("º", "").replace("ª", "");
	}

}