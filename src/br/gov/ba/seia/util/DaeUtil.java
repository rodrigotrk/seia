package br.gov.ba.seia.util;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao;
import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao.NumIdentificacaoEmitente;
import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao.Referencia;
import br.gov.ba.seia.dto.EmitirDocumentoArrecadacaoResponseDTO.RetEmissaoDocumentoArrecadacaoDTO;
import br.gov.ba.seia.dto.ParcelaDaeDTO;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DaeUtil {

	/**
	 * verificar atributos que devem ser enviados PARA o webservice
	 * @param ParcelaDaeDTO
	 * @return DocumentoArrecadacao
	 * @throws Exception
	 */
	private DocumentoArrecadacao getDocumentoArrecadacao(ParcelaDaeDTO parcelasDaeDTO){
		DocumentoArrecadacao documento_arrecadacao = new DocumentoArrecadacao();
		
		documento_arrecadacao.setSeq_documento_arrecadacao(SefazUtil.SEQ_DOC_ARREACADACAO);//2
		documento_arrecadacao.setCod_receita(parcelasDaeDTO.getDae().getCerhCodigoReceita().getNumCodigoCeceita());
		
		// 0 PJ e 1 PF
		String razaoSocial = "";
		if(parcelasDaeDTO.getPessoa().isPF()){
			razaoSocial = parcelasDaeDTO.getPessoa().getPessoaFisica().getNomPessoa();
			documento_arrecadacao.setCod_identificacao_emitente(1);
		}else{
			documento_arrecadacao.setCod_identificacao_emitente(0);
			razaoSocial = parcelasDaeDTO.getPessoa().getPessoaJuridica().getNomRazaoSocial();
		}
		
		documento_arrecadacao.setNum_identificacao_emitente(new NumIdentificacaoEmitente(parcelasDaeDTO.getPessoa().getCpfCnpj()));
//		Número do Tipo de Documento de Origem
//		Campo depende da criação do código de Receita, pois poderá ou não exigir um formato especificado pela SEFAZ. A sugestão da SEMA é informar o número do Cadastro do CERH, porém é necessário aguardar a criação do código de Receita.
		documento_arrecadacao.setCod_tipo_documento_origem(2);// 2
		//documento_arrecadacao.setNum_documento_origem("123456789"); //remover
//		Definição de qual documento de origem será usado depende da criação do código de Receita.
		documento_arrecadacao.setDtc_vencimento(FormaterUtil.formatarData(parcelasDaeDTO.getDae().getDtVencimento(), "yyyy-MM-dd"));
		documento_arrecadacao.setDtc_max_pagamento(FormaterUtil.formatarData(parcelasDaeDTO.getDae().getDtcMaxPagamento(), "yyyy-MM-dd"));
//		Código do Tipo de Referência – aguardando criação dos codigos
//		1 – Mês / Ano de Referência
//		 – Parcela / Total de Parcelas
//		3 – Ano Exercício
//		Campo depende da criação do código de Receita, pois poderá ou não exigir um formato especificado pela SEFAZ. A sugestão da SEMA é informar o ano base da cobrança, porém é necessário aguardar a criação do código de Receita.
//		De acordo com o tipo teremos a referencia:
//		Mês, Ano, Num parcela
//		Campo depende da criação do código de Receita, pois poderá ou não exigir um formato especificado pela SEFAZ. 
		documento_arrecadacao.setReferencia(new Referencia(1, parcelasDaeDTO.getDae().getNumMesReferencia(), parcelasDaeDTO.getDae().getNumAnoReferencia()));//1,10,2016 
		
		documento_arrecadacao.setVal_principal(parcelasDaeDTO.getValorDae());
		documento_arrecadacao.setVal_acrescimo(parcelasDaeDTO.getValorAcrescimo());

		documento_arrecadacao.setNom_razao_social(razaoSocial);
		documento_arrecadacao.setDes_tipo_logradouro(parcelasDaeDTO.getEnderecoPessoa().getIdeEndereco().getIdeLogradouro().getIdeTipoLogradouro().getIdeTipoLogradouro().toString());// "-"
		documento_arrecadacao.setDes_endereco(Util.substituirCaracterEspecial(parcelasDaeDTO.getEnderecoPessoa().getIdeEndereco().getEnderecoBasicoFormatado().toUpperCase()));//
		documento_arrecadacao.setDes_bairro(parcelasDaeDTO.getEnderecoPessoa().getIdeEndereco().getIdeLogradouro().getIdeBairro().getNomBairro().trim());//
		documento_arrecadacao.setNum_cep(parcelasDaeDTO.getEnderecoPessoa().getIdeEndereco().getIdeLogradouro().getNumCepString());//"41600115"
		documento_arrecadacao.setCod_municipio_ibge(parcelasDaeDTO.getEnderecoPessoa().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio().intValue() + "");//2927408
//		Tipo dod DAE  (tipo_dae) 2 - DAE COMPLEMENTAR
		documento_arrecadacao.setTipo_dae(SefazUtil.TIPO_DAE);//2
		
//		------------------------------------------START 
//		após oberte o código da receita esses campos não serão enviados pelo webservice
		documento_arrecadacao.setCod_unidade_orcamentaria_origem(10301);// 10301
		documento_arrecadacao.setCod_unidade_gestora_origem(1);//
		documento_arrecadacao.setCod_unidade_gestora_destino(1);//
		documento_arrecadacao.setCod_unidade_orcamentaria_destino(10301);// 10301

//		---------------------------------------------- END
		documento_arrecadacao.setCod_tipo_retorno_documento(1);//
		documento_arrecadacao.setSts_retornar_barra(SefazUtil.RET_COD_BARRA);//1
//		Informações Complementares (des_informacoe_complementares)
//		Número do Cadastro <Nº do cadastro do empreendimento no CERH>
//		Parcelas <parcelas que compõe o DAE, podendo ser mais de uma / Total de Parcelas>
//		<Tipo de Uso da água dos pontos que compõe o DAE, podendo ser abreviado>
		// Campo de informações complementares no xml de envio
		return documento_arrecadacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarDaeSefaz(ParcelaDaeDTO parcelasDaeDTO) throws Exception{
				
		RetEmissaoDocumentoArrecadacaoDTO retorno = SefazUtil.emitirDae(getDocumentoArrecadacao(parcelasDaeDTO));
		if(!Util.isNullOuVazio(retorno) && retorno.getEmissao_documento_arrecadacao() != null){
			if(!Util.isNullOuVazio(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_dae_emitido())){
				parcelasDaeDTO.getDae().setDscNossoNumero(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_dae_emitido().toString());
			}
			parcelasDaeDTO.getDae().setCodDocumentoOrigem(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_documento_arrecadacao());
			parcelasDaeDTO.getDae().setNumDocumentoOrigem(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_documento_arrecadacao().toString());
			parcelasDaeDTO.getDae().setCodReferencia(0);// onde obter ?
			parcelasDaeDTO.getDae().setUrlDae(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getDes_endereco_doc_arrec());
			parcelasDaeDTO.getDae().setCodbarras(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getDes_codigo_barra());
		}else{
			throw new Exception(retorno.getxMotivo());
		}
	}
	
	
	public static String gerarLinkDownloadDae(String url) {
		StringBuilder lReturn = new StringBuilder();
		lReturn.append("window.open('");
		lReturn.append(url);
		lReturn.append("');");
		lReturn.append("return true;");
		return lReturn.toString();
	}
}
