package br.gov.ba.seia.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao;
import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao.NumIdentificacaoEmitente;
import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao.Referencia;
import br.gov.ba.seia.dto.EmitirDocumentoArrecadacaoResponseDTO.RetEmissaoDocumentoArrecadacaoDTO;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCobranca;
import br.gov.ba.seia.entity.CerhDaeTipoUso;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhParcelasCobranca;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;

public final class CobrancaCerhUtil {


	private static final double TAXA_ATRASO = 0.02; //2%
	
	private static final double TAXA_SELIC_DEFAULT = 0.01; //1%
	
	private static final double TAR = 79.87;
	
	private static final double P = 0.75;
			
	private static final double VAZAO_ISENCAO_ABAST_HUMAN = 129.6d;

	private static final double VAZAO_ISENCAO = 43.2d;

	private static final int PRAZO_CORREIOS = 20;
	

	
	/**
	 * Verificar se cai em um feriado
	 * @param date
	 * @return boolean
	 */
	private boolean isFeriado(Calendar date){
		return false; // Que código é esse????
	}
	
	
	/**
	 * Para usuários Declarados (que não possuam processo vinculado ou que possuam processo com situação “Em análise” ou “Indeferido” 
	 * Para pontos onde a finalidade seja exclusivamente “Abastecimento Humano”, o usuário será isento de cobrança quando o somatório das vazões (vazão máxima instantânea) for menor ou igual à 129,6m³/dia 
	 * @param cerh
	 * @return
	 */
	public boolean isIsento(CerhCaptacaoCaracterizacao caracterizacao, double vazaoMaximaInstantanea){
		return isIsentoAbastecimentoHumano(caracterizacao, vazaoMaximaInstantanea)
				|| isIsentoProcesso(caracterizacao);
	}
	/**
	 * Para usuários Declarados (que não possuam processo vinculado ou que possuam processo com situação “Em análise” ou “Indeferido” 
	 * @param caracterizacao
	 * @param vazaoMaximaInstantanea
	 * @return
	 */
	public boolean isIsentoProcesso(CerhCaptacaoCaracterizacao caracterizacao){
		return false;
	}
	
	/**
	 * Para pontos onde a finalidade seja exclusivamente “Abastecimento Humano”, o usuário será isento de cobrança quando o somatório das vazões (vazão máxima instantânea) for menor ou igual à 129,6m³/dia
	 * @param caracterizacao
	 * @param vazaoMaximaInstantanea
	 * @return isento
	 */
	public boolean isIsentoAbastecimentoHumano(CerhCaptacaoCaracterizacao caracterizacao, double vazaoMaximaInstantanea){
		if(caracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection().isEmpty()){
			if(caracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection().size() == 1){
				CerhBarragemCaracterizacaoFinalidade finalidade = (CerhBarragemCaracterizacaoFinalidade) caracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection().toArray()[0];
				if(finalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO.getId())
						&& vazaoMaximaInstantanea <= VAZAO_ISENCAO_ABAST_HUMAN){
					return true;
				}
			}else{
				if(vazaoMaximaInstantanea <= VAZAO_ISENCAO){
					return true;
				}
			}			
		}
		return false;
	}
	

	
	private class ValorTipoUso{
		private TipoUsoRecursoHidrico tipoUsoRecursoHidrico;
		
		private BigDecimal valorFinal;

		private String rpga;

		private CerhLocalizacaoGeografica cerhLocalizacaoGeografica;

		public ValorTipoUso(TipoUsoRecursoHidrico tipoUsoRecursoHidrico,
				BigDecimal valorFinal,
				String rpga, CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
			this.tipoUsoRecursoHidrico = tipoUsoRecursoHidrico;
			this.valorFinal = valorFinal;
			this.rpga = rpga;
			this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
		}

		/**
		 * @return the tipoUsoRecursoHidrico
		 */
		public TipoUsoRecursoHidrico getTipoUsoRecursoHidrico() {
			return tipoUsoRecursoHidrico;
		}

		/**
		 * @return the valorFInal
		 */
		public BigDecimal getValorFinal() {
			return valorFinal;
		}

		/**
		 * @param valorFInal the valorFInal to set
		 */
		public void setValorFinal(BigDecimal valorFinal) {
			this.valorFinal = valorFinal;
		}

		/**
		 * @return the cerhLocalizacaoGeografica
		 */
		public CerhLocalizacaoGeografica getCerhLocalizacaoGeografica() {
			return cerhLocalizacaoGeografica;
		}

		/**
		 * @return the rpga
		 */
		public String getRpga() {
			return rpga;
		}

	}
	
	private class GrupoCobranca{
		
		private List<ValorTipoUso> valorTipoUsos;

		public GrupoCobranca() {
			valorTipoUsos = new ArrayList<CobrancaCerhUtil.ValorTipoUso>();
		}

		/**
		 * @return the valorTipoUsos
		 */
		public List<ValorTipoUso> getValorTipoUsos() {
			return valorTipoUsos;
		}

	}
	
	/**
	 * verificar atributos que devem ser enviados PARA o webservice
	 * @param cerhDae
	 * @return DocumentoArrecadacao
	 * @throws Exception
	 */
	private DocumentoArrecadacao getDocumentoArrecadacao(Dae cerhDae) {
		Endereco endereco = null;
		DocumentoArrecadacao documento_arrecadacao = new DocumentoArrecadacao();
		documento_arrecadacao.setSeq_documento_arrecadacao(SefazUtil.SEQ_DOC_ARREACADACAO);//2
		documento_arrecadacao.setCod_receita(cerhDae.getCerhCodigoReceita().getNumCodigoCeceita());// 2248
		documento_arrecadacao.setNum_identificacao_emitente(new NumIdentificacaoEmitente("empreendimento.getIdePessoa().getCpfCnpj()"));// new NumIdentificacaoEmitente("16512975000139")
		documento_arrecadacao.setCod_tipo_documento_origem(2);// 2
		documento_arrecadacao.setDtc_vencimento(FormaterUtil.formatarData(cerhDae.getDtVencimento(), "yyyy-MM-dd"));//"2017-10-01"
		documento_arrecadacao.setDtc_max_pagamento(FormaterUtil.formatarData(cerhDae.getDtVencimento(), "yyyy-MM-dd"));//"2017-10-01" 
//		Código do Tipo de Referência – aguardando criação dos codigos
//		1 – Mês / Ano de Referência
//		 – Parcela / Total de Parcelas
//		3 – Ano Exercício
//		Campo depende da criação do código de Receita, pois poderá ou não exigir um formato especificado pela SEFAZ. A sugestão da SEMA é informar o ano base da cobrança, porém é necessário aguardar a criação do código de Receita.
//		De acordo com o tipo teremos a referencia:
//		Mês, Ano, Num parcela
		documento_arrecadacao.setReferencia(new Referencia(1, cerhDae.getNumMesReferencia(), cerhDae.getNumAnoReferencia()));//1,10,2016 
		documento_arrecadacao.setVal_principal(cerhDae.getValorDae().setScale(2, RoundingMode.CEILING).doubleValue());
		documento_arrecadacao.setVal_acrescimo(cerhDae.getValorAcrescimo().setScale(2, RoundingMode.CEILING).doubleValue());
		documento_arrecadacao.setNom_razao_social("Teste");// ver PJ "PJ__Teste_Juan_Final"
		documento_arrecadacao.setDes_tipo_logradouro(endereco.getIdeLogradouro().getIdeTipoLogradouro().getIdeTipoLogradouro().toString());// "-"
		documento_arrecadacao.setDes_endereco(Util.substituirCaracterEspecial(endereco.getEnderecoBasicoFormatado().toUpperCase()));//
		documento_arrecadacao.setDes_bairro(endereco.getIdeLogradouro().getIdeBairro().getNomBairro());//
		documento_arrecadacao.setNum_cep(endereco.getIdeLogradouro().getNumCepString());//"41600115"
		documento_arrecadacao.setCod_municipio_ibge(endereco.getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio().intValue() + "");//2927408
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
	
	public void gerarDaesSefaz(CerhCobranca cobranca) throws Exception{
		for(CerhParcelasCobranca parcela : cobranca.getCerhParcelasCobrancasCollection()){
			for(Dae dae : parcela.getCerhDaesCollection()){
				gerarDaeSefaz(dae);
			}
		}
	}
	
	private void gerarDaeSefaz(Dae dae) throws Exception{
				
		RetEmissaoDocumentoArrecadacaoDTO retorno = SefazUtil.emitirDae(getDocumentoArrecadacao(dae));
		
		if(retorno.getEmissao_documento_arrecadacao() != null){
			
			if(!Util.isNullOuVazio(retorno.getEmissao_documento_arrecadacao())
					&& !Util.isNullOuVazio(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao())
					&& !Util.isNullOuVazio(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_dae_emitido())){
				
				dae.setDscNossoNumero(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_dae_emitido().toString());
			}
			
			dae.setCodDocumentoOrigem(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_documento_arrecadacao());
			dae.setNumDocumentoOrigem(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_documento_arrecadacao().toString());
			dae.setCodReferencia(0);// onde obter ?
			dae.setUrlDae(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getDes_endereco_doc_arrec());
			dae.setCodbarras(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getDes_codigo_barra());
		}else{
			throw new Exception(retorno.getxMotivo());
		}
	}
	
	
	public Dae montar2Via(Dae dae) throws Exception{
		Dae dae2 = (Dae) Util.copiarEntity(dae);// copiar todos os campos do DAE original
		//nova Data de Emissão
		dae2.setDtEmissao(Calendar.getInstance().getTime());
		//novos Valores a serem calculados
		dae2.setCerhDaetipoUsos(new ArrayList<CerhDaeTipoUso>());
		for(CerhDaeTipoUso tU : dae.getCerhDaetipoUsos()){
			CerhDaeTipoUso cerhDaeTipoUso = (CerhDaeTipoUso) Util.copiarEntity(tU);
			//apaga o ID  para persistir como um novo registro
			cerhDaeTipoUso.setIdeCerhDaeTipoUso(null);
			dae2.getCerhDaetipoUsos().add(cerhDaeTipoUso);
		}
		//Recalcular os juros
		//DAE original
		dae2.setDaePai(dae);
		return dae2;
	}
	
	
}
