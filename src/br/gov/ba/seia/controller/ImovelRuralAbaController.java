package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

import br.gov.ba.seia.entity.AreaRuralConsolidada;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.ContratoConvenioCefir;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoImovelRuralPosse;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.OutrosPassivosAmbientais;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.TipoArl;
import br.gov.ba.seia.entity.TipoDocumentoImovelRural;
import br.gov.ba.seia.entity.TipoFinalidadeVegetacaoNativa;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.VegetacaoNativa;
import br.gov.ba.seia.entity.VegetacaoNativaFinalidade;
import br.gov.ba.seia.enumerator.ImovelRuralAbaEnum;
import br.gov.ba.seia.enumerator.MunicipioEnum;
import br.gov.ba.seia.enumerator.TipoDocumentoImovelRuralEnum;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;


public class ImovelRuralAbaController {
	private static final int OUTRO = -1;

	public String onFlow(ImovelRuralControllerNew ctl, FlowEvent event){
		ctl.setImovelRural(ContextoUtil.getContexto().getImovelRural());

		String abaAnterior = event.getOldStep();
		String abaProxima = event.getNewStep();

		if(abaProxima.equals(ImovelRuralAbaEnum.DADOS_BASICOS.getAba())){
			avancarParaAbaDadosBasicos(ctl);
		}

		if(abaProxima.equals(ImovelRuralAbaEnum.DOCUMENTACAO.getAba())){
			avancarParaAbaDocumentacao(ctl);
		}

		if(abaAnterior.equals(ImovelRuralAbaEnum.DADOS_BASICOS.getAba())){
			if (ctl.getImovelRural().getIdeImovelRural()==null) {
				MensagemUtil.erro("Favor salvar os dados básicos do imóvel.");
				ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
				return ImovelRuralAbaEnum.DADOS_BASICOS.getAba();
			}

			try {
				ctl.getImovelRuralServiceFacade().atualizarModuloFiscal(ctl.getImovelRural());
				ctl.getImovelRuralServiceFacade().validarQtdModulosFiscaisImovelBndes(ctl.getImovelRural());
				ctl.carregarSecaoGeometrica();

				if (Util.isNullOuVazio(ctl.getImovelRural().getDocumentoImovelRuralPosse())) {
					ctl.getImovelRural().setDocumentoImovelRuralPosse(new DocumentoImovelRuralPosse());
					
				} else if (ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural() != null 
						&& ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getNumGrupoDocumento() != null
						&& ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getNumGrupoDocumento() == 4){
					carregarPropriedadesEnderecoDeclarante(ctl);
				}
				
				if(!ctl.getShowPCT()){
					atualizarLabelsPossePropriedade(ctl);
				}

				ctl.setRenderPollProprietarios(true);
			} catch (ValidationException e) {
				MensagemUtil.erro(e.getMessage());
				return ImovelRuralAbaEnum.DADOS_BASICOS.getAba();
			} catch (Exception e) {
				return ImovelRuralAbaEnum.DADOS_BASICOS.getAba();
			}
		}

		if(abaAnterior.equals(ImovelRuralAbaEnum.DOCUMENTACAO.getAba())){
			/* 
			if (abaProxima.equals(ImovelRuralAbaEnum.DADOS_BASICOS.getAba())) {
				if (!ctl.getImovelRural().getIsFinalizado() 
						&& ctl.getImovelRural().getDocumentoImovelRuralPosse()!=null 
						&& ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeDocumentoImovelRuralPosse()!=null) {
					
					ctl.getImovelRural().setDocumentoImovelRuralPosse(null);
				}
			}
			*/
			if(abaProxima.equals(ImovelRuralAbaEnum.LOCALIZACAO_GEOGRAFICA.getAba())){
				List<String> mensagens = ctl.validaCamposTelaDocumentacao();
				if (!mensagens.isEmpty()) {
					MensagemUtil.erro(mensagens);
					ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
					return ImovelRuralAbaEnum.DOCUMENTACAO.getAba();
				}

				if (ctl.getImovelRural().getIdeLocalizacaoGeografica()==null) {
					ctl.getImovelRural().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					ctl.getImovelRural().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
					ctl.getImovelRural().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
					ctl.getImovelRural().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
				}

				if (ctl.getImovelRural().isImovelINCRA() && ctl.getImovelRural().getIdeLocalizacaoGeograficaLote()==null) {
					ctl.getImovelRural().setIdeLocalizacaoGeograficaLote(new LocalizacaoGeografica());
					ctl.getImovelRural().getIdeLocalizacaoGeograficaLote().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
					ctl.getImovelRural().getIdeLocalizacaoGeograficaLote().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
					ctl.getImovelRural().getIdeLocalizacaoGeograficaLote().setIdeSistemaCoordenada(new SistemaCoordenada());
				}
				
				if(ctl.getShowPCT() && Util.isNullOuVazio(ctl.getImovelRural().getIdeLocalizacaoGeograficaPctLimiteTerritorio())){
					ctl.getImovelRural().setIdeLocalizacaoGeograficaPctLimiteTerritorio(new LocalizacaoGeografica());
				}
			}
		}

		if(abaAnterior.equals(ImovelRuralAbaEnum.LOCALIZACAO_GEOGRAFICA.getAba())){
			if(abaProxima.equals(ImovelRuralAbaEnum.DOCUMENTACAO.getAba())){
				if (Util.isNullOuVazio(ctl.getImovelRural().getIdeLocalizacaoGeografica())) {
					ctl.getImovelRural().setIdeLocalizacaoGeografica(null);
				}
				
				if (Util.isNullOuVazio(ctl.getImovelRural().getIdeLocalizacaoGeograficaLote())) {
					ctl.getImovelRural().setIdeLocalizacaoGeograficaLote(null);
				}
				
				if (Util.isNullOuVazio(ctl.getImovelRural().getIdeLocalizacaoGeograficaPct())) {
					ctl.getImovelRural().setIdeLocalizacaoGeograficaPct(null);
				}

				ctl.setRenderPollProprietarios(true);
				atualizarLabelsPossePropriedade(ctl);
			}

			if(abaProxima.equals(ImovelRuralAbaEnum.QUESTIONARIO.getAba())){
				if (ctl.getImovelRural().getIdeLocalizacaoGeografica()==null) {
					MensagemUtil.erro("Localização geográfica obrigatória");
					ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
					return ImovelRuralAbaEnum.LOCALIZACAO_GEOGRAFICA.getAba();
				}
				
				if(ctl.getShowPCT()){
					if(Util.isNullOuVazio(ctl.getImovelRural().getIdeLocalizacaoGeograficaPctLimiteTerritorio())){
						JsfUtil.addErrorMessage("A poligonal da comunidade inserida é obrigatória");
						ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
						return "localizacaoGeograficaTab";
					}
					
					if(Util.isNullOuVazio(Util.isNullOuVazio(ctl.getImovelRural().getIdeLocalizacaoGeograficaPct()))){
						JsfUtil.addErrorMessage("A coordenada do ponto/sede é obrigatória");
						ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
						return "localizacaoGeograficaTab";
					}
				}
				else {
					if (ctl.getImovelRural().isImovelBNDES()) {
						if(Util.isNull(ctl.getImovelRural().getIdeContratoConvenioCefir()) || Util.isNull(ctl.getImovelRural().getIdeContratoConvenioCefir().getIdeContratoConvenioCefir())) {
							ctl.getImovelRural().setIndContratoConvenio(true);
							ctl.getImovelRural().setIdeContratoConvenioCefir(new ContratoConvenioCefir());
						}
					} else {
						if(Util.isNull(ctl.getImovelRural().getIdeContratoConvenioCefir()) || Util.isNull(ctl.getImovelRural().getIdeContratoConvenioCefir().getIdeContratoConvenioCefir())) {
							ctl.getImovelRural().setIndContratoConvenio(false);
							ctl.getImovelRural().setIdeContratoConvenioCefir(null);
						}
					}
					ctl.setIndLancamentoResiduosLiquidos((ctl.getImovelRural().getIndLancamentoConcessionaria()!=null && ctl.getImovelRural().getIndLancamentoConcessionaria()) || (ctl.getImovelRural().getIndLancamentoManancial()!=null && ctl.getImovelRural().getIndLancamentoManancial()));
				}

				if (!ctl.getImovelRural().isMenorQueQuatroModulosFiscais() || ctl.getImovelRural().isImovelBNDES() || ctl.getImovelRural().isImovelINCRA() || ctl.getImovelRural().isImovelPCT()) {
					ctl.getImovelRural().setIndDesejaCompletarInformacoes(true);
				}
				
				if (ctl.getImovelRural().isImovelINCRA() && !ctl.getImovelRural().getIdeLocalizacaoGeograficaLote().getNovosArquivosShapeImportados()	&& Util.isNullOuVazio(ctl.getImovelRural().getIdeLocalizacaoGeograficaLote())) {
					ctl.getImovelRural().setIdeLocalizacaoGeograficaLote(null);
				}
			}
		}

		if(abaAnterior.equals(ImovelRuralAbaEnum.QUESTIONARIO.getAba())){
			if(abaProxima.equals(ImovelRuralAbaEnum.LOCALIZACAO_GEOGRAFICA.getAba())){
				if (ctl.getImovelRural().isImovelINCRA() && ctl.getImovelRural().getIdeLocalizacaoGeograficaLote()==null) {
					ctl.getImovelRural().setIdeLocalizacaoGeograficaLote(new LocalizacaoGeografica());
					ctl.getImovelRural().getIdeLocalizacaoGeograficaLote().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
					ctl.getImovelRural().getIdeLocalizacaoGeograficaLote().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
					ctl.getImovelRural().getIdeLocalizacaoGeograficaLote().setIdeSistemaCoordenada(new SistemaCoordenada());
				}
				
				if (!ctl.getShowPCT()) {
					if(ctl.getImovelRural().getIdeContratoConvenioCefir()==null || ctl.getImovelRural().getIdeContratoConvenioCefir().getIdeContratoConvenioCefir()==null) {
						ctl.getImovelRural().setIndContratoConvenio(false);
						ctl.getImovelRural().setIdeContratoConvenioCefir(null);
					}
				}
			}

			if (abaProxima.equals(ImovelRuralAbaEnum.DADOS_ESPECIFICOS.getAba())) {
				if (ctl.validaQuestionario()) {
					// RESERVA LEGAL
					if (Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal())) {
						ctl.getImovelRural().setReservaLegal(new ReservaLegal());
						ctl.getImovelRural().getReservaLegal().setIdeTipoArl(new TipoArl());
					}

					if (ctl.getImovelRural().getReservaLegal().getIndAverbada()!=null
						&& ctl.getImovelRural().getReservaLegal().getIndAverbada()
						&& Util.isNull(ctl.getImovelRural().getReservaLegal().getIdeReservaLegalAverbada()==null)){
						ctl.getImovelRural().getReservaLegal().setIdeReservaLegalAverbada(new ReservaLegalAverbada());
					}

					if (Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica())) {
						ctl.getImovelRural().getReservaLegal().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
						ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
						ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
						ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
					}

					// VEGETAÇÃO NATIVA
					if (!Util.isNull(ctl.getImovelRural().getIndVegetacaoNativa()) && ctl.getImovelRural().getIndVegetacaoNativa() 
							&& Util.isNull(ctl.getImovelRural().getVegetacaoNativa())) {
						ctl.getImovelRural().setVegetacaoNativa(new VegetacaoNativa());
					}
					
					if (!Util.isNull(ctl.getImovelRural().getIndVegetacaoNativa()) && ctl.getImovelRural().getIndVegetacaoNativa() 
							&& Util.isNullOuVazio(ctl.getImovelRural().getVegetacaoNativa().getVegetacaoNativaFinalidadeCollection())) {
						ctl.getImovelRural().getVegetacaoNativa().setVegetacaoNativaFinalidadeCollection(new ArrayList<VegetacaoNativaFinalidade>());
						ctl.getImovelRural().getVegetacaoNativa().setTipoFinalidadeVegetacaoNativaCollection(new ArrayList<TipoFinalidadeVegetacaoNativa>());
					}
					
					if (!Util.isNull(ctl.getImovelRural().getIndVegetacaoNativa()) && ctl.getImovelRural().getIndVegetacaoNativa() 
							&& Util.isNullOuVazio(ctl.getImovelRural().getVegetacaoNativa().getIdeLocalizacaoGeografica())) {
						ctl.getImovelRural().getVegetacaoNativa().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
						ctl.getImovelRural().getVegetacaoNativa().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
						ctl.getImovelRural().getVegetacaoNativa().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
						ctl.getImovelRural().getVegetacaoNativa().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
					}
					
					// RPPN
					if (!Util.isNull(ctl.getImovelRural().getIndRppn()) && ctl.getImovelRural().getIndRppn() 
							&& Util.isNullOuVazio(ctl.getImovelRural().getIdeImovelRuralRppn())) {
						ctl.getImovelRural().setIdeImovelRuralRppn(new ImovelRuralRppn(ctl.getImovelRural()));
					}
					
					if (!Util.isNull(ctl.getImovelRural().getIndRppn()) && ctl.getImovelRural().getIndRppn() 
							&& Util.isNullOuVazio(ctl.getImovelRural().getIdeImovelRuralRppn().getIdeLocalizacaoGeografica())) {
						ctl.getImovelRural().getIdeImovelRuralRppn().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
						ctl.getImovelRural().getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
						ctl.getImovelRural().getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
						ctl.getImovelRural().getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
					}

					// ÁREA RURAL CONSOLIDADA
					if ((ctl.getImovelRural().isImovelINCRA()|| ctl.getShowPCT()) 
							&& ctl.getImovelRural().getIndAreaRuralConsolidada()
							&& Util.isNullOuVazio(ctl.getImovelRural().getIdeAreaRuralConsolidada())) {
						ctl.getImovelRural().setIdeAreaRuralConsolidada(new AreaRuralConsolidada());
					}
					
					if (!Util.isNull(ctl.getImovelRural().getIndAreaRuralConsolidada()) && ctl.getImovelRural().getIndAreaRuralConsolidada() 
							&& Util.isNullOuVazio(ctl.getImovelRural().getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica())) {
						ctl.getImovelRural().getIdeAreaRuralConsolidada().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
						ctl.getImovelRural().getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
						ctl.getImovelRural().getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
						ctl.getImovelRural().getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
					}
					
					// OUTROS PASSIVOS
					if (!Util.isNull(ctl.getImovelRural().getIndOutrosPassivos()) && ctl.getImovelRural().getIndOutrosPassivos()) {
						if (Util.isNullOuVazio(ctl.getImovelRural().getOutrosPassivosAmbientais())) {
							ctl.getImovelRural().setOutrosPassivosAmbientais(new OutrosPassivosAmbientais());
						}
						
						if (Util.isNullOuVazio(ctl.getImovelRural().getOutrosPassivosAmbientais().getCronogramaRecuperacao())) {
							ctl.getImovelRural().getOutrosPassivosAmbientais().setCronogramaRecuperacao(new CronogramaRecuperacao());
						}
					}
				} else {
					if(!Util.isNullOuVazio(ctl.getMsgDataInvalida())){
						SessaoUtil.adicionarObjetoSessao("MENSAGEM_ERRO_VALIDACAO_IMOVEL_RURAL",ctl.getMsgDataInvalida());
						Html.executarJS("imovelRuralPoll.start();");
					} else {
						SessaoUtil.adicionarObjetoSessao("MENSAGEM_ERRO_VALIDACAO_IMOVEL_RURAL",ctl.getMsgDataInvalida());
					}
					
					ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
					
					if(ctl.getImovelRural().isImovelBNDES()) {
						return ImovelRuralAbaEnum.DADOS_ESPECIFICOS.getAba();
					}
					
					SessaoUtil.adicionarObjetoSessao("MENSAGEM_PREENCHIMENTO_QUESTIONARIO_OBRIGATORIO","O preenchimento do questionário é obrigatório!");
					Html.executarJS("msgQuestionario();");
					return ImovelRuralAbaEnum.QUESTIONARIO.getAba();
				}
			}
		}

		if(abaAnterior.equals(ImovelRuralAbaEnum.DADOS_ESPECIFICOS.getAba())){
			if(abaProxima.equals(ImovelRuralAbaEnum.CONFIRMACAO.getAba())){
				if (!Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal())&& !Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeTipoArl())	&& !Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeTipoArl().getIdeTipoArl())) {
					if (!Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIndAverbada())&& ctl.getImovelRural().getReservaLegal().getIndAverbada()) {
						if (Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeReservaLegalAverbada())	|| Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao()) && (!ctl.getImovelRural().isImovelCDA() && !ctl.getImovelRural().isImovelBNDES())	&& !ctl.isUsuarioSemRestricao()) {
							JsfUtil.addErrorMessage(Util.getString("cefir_lbl_documento_comprova_averbacao"));
							ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());

							return ImovelRuralAbaEnum.DADOS_ESPECIFICOS.getAba();
						}
					}

					if (!Util.isNullOuVazio(ctl.getImovelRural().getIndReservaLegal()) && ctl.getImovelRural().getIndReservaLegal()	&& !(!Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeStatus())
									&& !Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeStatus().getdscStatus())
									&& ctl.getImovelRural().getReservaLegal().getIdeStatus().getdscStatus().equals("Aprovada")
									&& Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getDtcAprovacaoDeclarada()))) {

						if (!Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIndAverbada())
								&& !ctl.getImovelRural().getReservaLegal().getIndAverbada()
								&& (Util.isLazyInitExcepOuNull(ctl.getImovelRural().getReservaLegal().getIdeDocumentoAprovacao()) || Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeDocumentoAprovacao()))
								&& !ctl.isUsuarioSemRestricao()) {

							if(!ctl.getImovelRural().isImovelCDA() && !ctl.getImovelRural().isImovelBNDES()) {
								JsfUtil.addErrorMessage(Util.getString("cefir_lbl_documento_comprova_aprovacao"));
								ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
								return ImovelRuralAbaEnum.DADOS_ESPECIFICOS.getAba();
							}
						}
					}

					if(!Util.isNullOuVazio(ctl.getImovelRural().getIndAreaProdutiva()) && ctl.getImovelRural().getIndAreaProdutiva()){
						try {
							if(!ctl.getImovelRuralServiceFacade().validaCampoLicencaAreaProdutiva(ctl.getImovelRural())){
								ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
								RequestContext.getCurrentInstance().execute("dlgAtividaDesenvolvidaIncompleta.show()");
								return ImovelRuralAbaEnum.DADOS_ESPECIFICOS.getAba();
							}
						} catch (Exception e) {
							Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
							JsfUtil.addErrorMessage(Util.getString("Erro ao validar Atividades Desenvolvidas"));
						}
					}
				} else {
					ctl.getImovelRural().setReservaLegal(null);
				}

				if (!Util.isNullOuVazio(ctl.getListCapSuperficial()) || !Util.isNullOuVazio(ctl.getListCapSubterranea())	|| !Util.isNullOuVazio(ctl.getListLancamentoResiduos()) || !Util.isNullOuVazio(ctl.getListPontoIntervencao())) {
					if(!Util.isNullOuVazio(ctl.getImovelRural().getImovelRuralUsoAguaCollection())) {
						ctl.getImovelRural().getImovelRuralUsoAguaCollection().clear();
					}else {
						ctl.getImovelRural().setImovelRuralUsoAguaCollection(new ArrayList<ImovelRuralUsoAgua>());
					}
				}

				if (!Util.isNullOuVazio(ctl.getListCapSuperficial())) {
					ctl.getImovelRural().getImovelRuralUsoAguaCollection().addAll(ctl.getListCapSuperficial());
				}

				if (!Util.isNullOuVazio(ctl.getListCapSubterranea())) {
					ctl.getImovelRural().getImovelRuralUsoAguaCollection().addAll(ctl.getListCapSubterranea());
				}

				if (!Util.isNullOuVazio(ctl.getListLancamentoResiduos())) {
					ctl.getImovelRural().getImovelRuralUsoAguaCollection().addAll(ctl.getListLancamentoResiduos());
				}

				if (!Util.isNullOuVazio(ctl.getListPontoIntervencao())) {
					ctl.getImovelRural().getImovelRuralUsoAguaCollection().addAll(ctl.getListPontoIntervencao());
				}

				if (ctl.getImovelRural().getIsFinalizado()) {
					ctl.montarEnderecoDadosBasicos();
					if(!ctl.getShowPCT()){
						ctl.montarEnderecoDocumentacao();
					}
				}
			}

			if (abaProxima.equals(ImovelRuralAbaEnum.QUESTIONARIO.getAba())) {
				if (!ctl.getImovelRural().getIsFinalizado()) {
					if (!Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal()) && Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIdeReservaLegal())) {
						ctl.getImovelRural().setReservaLegal(null);
					}

					if (!Util.isNullOuVazio(ctl.getImovelRural().getVegetacaoNativa()) && Util.isNullOuVazio(ctl.getImovelRural().getVegetacaoNativa().getIdeVegetacaoNativa())) {
						ctl.getImovelRural().setVegetacaoNativa(null);
					}

					if (!Util.isNullOuVazio(ctl.getImovelRural().getIdeAreaRuralConsolidada()) && Util.isNullOuVazio(ctl.getImovelRural().getIdeAreaRuralConsolidada().getIdeAreaRuralConsolidada())) {
						ctl.getImovelRural().setIdeAreaRuralConsolidada(null);
					}

					if (ctl.isImovelRuralRppnInicializado() && !ctl.isEdicaoImoveRuralRppn(ctl.getImovelRural())) {
						ctl.getImovelRural().setIdeImovelRuralRppn(null);
					}

					if (!Util.isNullOuVazio(ctl.getImovelRural().getOutrosPassivosAmbientais()) && Util.isNullOuVazio(ctl.getImovelRural().getOutrosPassivosAmbientais().getIdeOutrosPassivosAmbientais())) {
						ctl.getImovelRural().setOutrosPassivosAmbientais(null);
					}
				}
			}
		}

		if (abaAnterior.equals(ImovelRuralAbaEnum.CONFIRMACAO.getAba())) {
			if (abaProxima.equals(ImovelRuralAbaEnum.DADOS_ESPECIFICOS.getAba())) {
				if (Util.isNull(ctl.getImovelRural().getReservaLegal())) {
					ctl.getImovelRural().setReservaLegal(new ReservaLegal());
				}

				if (!Util.isNullOuVazio(ctl.getImovelRural().getReservaLegal().getIndAverbada()) && ctl.getImovelRural().getReservaLegal().getIndAverbada()	&& Util.isNull(ctl.getImovelRural().getReservaLegal().getIdeReservaLegalAverbada())) {
					ctl.getImovelRural().getReservaLegal().setIdeReservaLegalAverbada(new ReservaLegalAverbada());
				}

				if (ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica() == null) {
					ctl.getImovelRural().getReservaLegal().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					ctl.getImovelRural().getReservaLegal().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
					ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
					ctl.getImovelRural().getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
				}
			}
		}

		if(abaProxima.equals(ImovelRuralAbaEnum.CONFIRMACAO.getAba())){
			if (ctl.getTipoVinculoImovel().getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
				ctl.setTituloProprietariosJustoPossuidores(Util.getString("cefir_lbl_justos_possidores_imovel"));
				ctl.setLblListaProprietariosJustoPossuidores(Util.getString("cefir_lbl_justo_possuidores"));
				ctl.setLblDocumentoPossePropriedade(Util.getString("cefir_lbl_documento_posse"));
			} else {
				ctl.setTituloProprietariosJustoPossuidores(Util.getString("cefir_lbl_proprietarios_imovel"));
				ctl.setLblListaProprietariosJustoPossuidores(Util.getString("cefir_lbl_proprietarios_imovel"));
				ctl.setLblDocumentoPossePropriedade(Util.getString("cefir_lbl_documento_propriedade"));
			}
			
			if(ctl.getShowPCT()){
				try {
					if(!Util.isNullOuVazio(ctl.getPctImovelRural().getIdeTipoTerritorioPct()) && ctl.getPctImovelRural().getIdeTipoTerritorioPct().getIdeTipoTerritorioPct() == 1){
						if(!Util.isNullOuVazio(ctl.getImovelRural().getIdeMunicipioCartorio())){
							ctl.getImovelRural().getIdeMunicipioCartorio().setIdeEstado(ctl.getEstadoSelecionado());
							ctl.getImovelRural().getIdeMunicipioCartorio().setNomMunicipio(ctl.getImovelRuralServiceFacade().filtrarMunicipioById(ctl.getImovelRural().getIdeMunicipioCartorio()).getNomMunicipio());
						}
					}
					
					if(!Util.isNullOuVazio(ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())){
						if(Util.isNullOuVazio(ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getDscTipoDocumentoImovelRural())){
							ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().setDscTipoDocumentoImovelRural(ctl.getImovelRuralServiceFacade().findByIdeTipoDocumentoImovelRural(ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getIdeTipoDocumentoImovelRural()).getDscTipoDocumentoImovelRural());
						}
					}
					
					ctl.getImovelRural().setIdePctImovelRural(ctl.getPctImovelRural());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			ctl.setFinalizarRendered(true);
		} else {
			ctl.setFinalizarRendered(false);
		}

		RequestContext.getCurrentInstance().addCallbackParam("exibirBotaoFinalizar", true);
		ContextoUtil.getContexto().setImovelRural(ctl.getImovelRural());
		return event.getNewStep();
	}

	private void avancarParaAbaDadosBasicos(ImovelRuralControllerNew controller) {
		if(controller.getImovelRural().getImovel().getIdeEndereco()!=null) {
			try {
				if(!Util.isNotNullAndTrue(controller.getImovelRural().getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro().getIndOrigemCorreio()) &&
						!Util.isNotNullAndTrue(controller.getImovelRural().getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro().isIndOrigemApi())) {
					controller.setBairroSelecionado((Bairro) controller.getImovelRural().getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro().clone());
					controller.getBairroSelecionado().setIdeBairro(OUTRO);
				}

				if(!Util.isNotNullAndTrue(controller.getImovelRural().getImovel().getIdeEndereco().getIdeLogradouro().getIndOrigemCorreio()) && 
						!Util.isNotNullAndTrue(controller.getImovelRural().getImovel().getIdeEndereco().getIdeLogradouro().getIndOrigemApi()) ) {
					controller.setLogradouroSelecionado((Logradouro) controller.getImovelRural().getImovel().getIdeEndereco().getIdeLogradouro().clone());
					controller.getLogradouroSelecionado().setIdeLogradouro(OUTRO);
				}
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}

	private void avancarParaAbaDocumentacao(ImovelRuralControllerNew controller) {
		try{
			DocumentoImovelRuralPosse documentoImovelRuralPosse = controller.getImovelRural().getDocumentoImovelRuralPosse();
			TipoDocumentoImovelRural tipoDocumentoImovelRural = new TipoDocumentoImovelRural(TipoDocumentoImovelRuralEnum.DECLARACAO_DOS_CONFRONTANTES_COM_ANUENCIA_DO_SINDICATO_DOS_TRABALHADORES_RURAIS);
			
			if(!controller.isNotRenderCamposPct() && !Util.isNullOuVazio(documentoImovelRuralPosse.getIdeEnderecoDeclarante())){
				if(documentoImovelRuralPosse.getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro().getIndOrigemCorreio()==false && 
						documentoImovelRuralPosse.getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro().isIndOrigemApi()==false) {
					
					controller.setBairroDeclarante((Bairro) documentoImovelRuralPosse.getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro().clone());
					controller.getBairroDeclarante().setIdeBairro(OUTRO);
					controller.setShowInputs(true);
				}
				
				if(documentoImovelRuralPosse.getIdeEnderecoDeclarante().getIdeLogradouro().getIndOrigemCorreio()==false &&
					  Util.isNotNullAndTrue(documentoImovelRuralPosse.getIdeEnderecoDeclarante().getIdeLogradouro().getIndOrigemApi())==false ) {
					controller.setLogradouroDeclarante((Logradouro) documentoImovelRuralPosse.getIdeEnderecoDeclarante().getIdeLogradouro().clone());
					controller.getLogradouroDeclarante().setIdeLogradouro(OUTRO);
					controller.setShowInputLogradouro(true);
				}
			} else {
				if(documentoImovelRuralPosse!=null && tipoDocumentoImovelRural.equals(documentoImovelRuralPosse.getIdeTipoDocumentoImovelRural())) {
					if(!Util.isNotNullAndTrue(controller.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro().getIndOrigemCorreio()) &&
							!Util.isNotNullAndTrue(controller.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro().isIndOrigemApi())) {
						controller.setBairroDeclarante((Bairro) controller.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro().clone());
						controller.getBairroDeclarante().setIdeBairro(OUTRO);
						controller.setShowInputs(true);
					}
					
					if(!Util.isNotNullAndTrue(controller.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIndOrigemCorreio()) && 
							!Util.isNotNullAndTrue(controller.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIndOrigemApi())) {
						controller.setLogradouroDeclarante((Logradouro) controller.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().clone());
						controller.getLogradouroDeclarante().setIdeLogradouro(OUTRO);
						controller.setShowInputLogradouro(true);
					}
				}
			}
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void carregarPropriedadesEnderecoDeclarante(ImovelRuralControllerNew ctl) {
		if (ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante()!=null) {
			ctl.setEnderecoDeclarante(ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante());
			ctl.setLogradouroPesquisa(ctl.getEnderecoDeclarante().getIdeLogradouro() );
			ctl.montaListaBairrosDeclarante();
			ctl.setLogradouroDeclarante(ctl.getEnderecoDeclarante().getIdeLogradouro());

			if (!Util.isNullOuVazio(ctl.getLogradouroDeclarante()) && !Util.isNullOuVazio(ctl.getLogradouroDeclarante().getIdeBairro())) {
				for (Bairro bairro : ctl.getListaBairroDeclarante()) {
					if (bairro.getNomBairro().trim().toLowerCase().equals(ctl.getLogradouroDeclarante().getIdeBairro().getNomBairro().trim().toLowerCase())) {
						ctl.setBairroDeclarante(bairro);
						ctl.setBairroDeclaranteCombo(bairro);
						break;
					}//
				}
				if((ctl.getBairroDeclarante()==null)  && ctl.getEnderecoDeclarante().getIdeLogradouro().getIdeBairro()!=null) {
					ctl.setBairroDeclarante(ctl.getEnderecoDeclarante().getIdeLogradouro().getIdeBairro());
					if(ctl.getListaBairroDeclarante().contains(ctl.getEnderecoDeclarante().getIdeLogradouro().getIdeBairro())==false) {
						ctl.getListaBairroDeclarante().add(ctl.getEnderecoDeclarante().getIdeLogradouro().getIdeBairro());
						ctl.setBairroDeclaranteCombo(ctl.getEnderecoDeclarante().getIdeLogradouro().getIdeBairro());
					}else {
						ctl.setBairroDeclaranteCombo(new Bairro(OUTRO, "Outro", false));
						
					}
				}
			}
			
			try {
				if (!Util.isNullOuVazio(ctl.getBairroDeclarante())){
					ctl.setListaLogradouroDeclarante(ctl.getImovelRuralServiceFacade().filtrarLogradouroByNome(ctl.getBairroDeclarante(),Integer.valueOf(ctl.getLogradouroPesquisa().getNumCep())));
				}
				else {
					ctl.setBairroDeclarante(ctl.getImovelRural().getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro());
				}

				if (!Util.isNullOuVazio(ctl.getLogradouroDeclarante()) && !ctl.getLogradouroDeclarante().getIdeLogradouro().equals(0) && (Util.isNotNullAndTrue(ctl.getLogradouroDeclarante().getIndOrigemCorreio()) || Util.isNotNullAndTrue(ctl.getLogradouroDeclarante().getIndOrigemApi()))) {
					if (!ctl.getListaLogradouroDeclarante().contains(ctl.getLogradouroDeclarante())) {
						for (Logradouro logradouro : ctl.getListaLogradouroDeclarante()) {
							if (logradouro.getNomLogradouro().trim().toLowerCase().equals(ctl.getLogradouroDeclarante().getNomLogradouro().trim().toLowerCase())) {
								ctl.setLogradouroDeclarante(logradouro);
								break;
							}
						}
					}
				} else if (Util.isNullOuVazio(ctl.getLogradouroDeclarante().getNomLogradouro())){
					ctl.setLogradouroDeclarante(new Logradouro(0));
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}

			ctl.getLogradouroDeclarante().setIdeBairro(ctl.getBairroDeclarante());

			if (!Util.isNullOuVazio(ctl.getLogradouroDeclarante()) && !Util.isNullOuVazio(ctl.getLogradouroDeclarante().getIdeBairro())) {
				ctl.setTipoLogradouroDeclarante(ctl.getLogradouroDeclarante().getIdeTipoLogradouro());

				if (!Util.isNullOuVazio(ctl.getLogradouroDeclarante().getIdeMunicipio())){
					ctl.setMunicipioDeclarante(ctl.getLogradouroDeclarante().getIdeMunicipio());
				}else {
					ctl.setMunicipioDeclarante(ctl.getLogradouroPesquisa().getIdeMunicipio());
				}

				if (!Util.isNullOuVazio(ctl.getMunicipioDeclarante()) && !Util.isNullOuVazio(ctl.getMunicipioDeclarante().getIdeMunicipio())){
					ctl.setIsMunicipioSalvador((ctl.getMunicipioDeclarante().getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId())));
				}
				else{
					ctl.setIsMunicipioSalvador(null);
				}
			}

			if (!Util.isNullOuVazio(ctl.getMunicipioDeclarante()) && !Util.isNullOuVazio(ctl.getMunicipioDeclarante().getIdeEstado())) {
				ctl.setEstadoDeclarante(ctl.getMunicipioDeclarante().getIdeEstado());
			}
			
			ctl.setEnableEnderecoDeclarante(true);
			ctl.setEnableChangeBairroDeclarante(true);
		}
	}

	private void atualizarLabelsPossePropriedade(ImovelRuralControllerNew ctl) {
		if (ctl.getTipoVinculoImovel().getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
			if (!ctl.getImovelRuralServiceFacade().verificarExistenciaProprietarios(ctl.getListProprietariosImovel())) {
				ctl.setTituloProprietariosJustoPossuidores(Util.getString("cefir_lbl_justos_possidores_atuais_imovel"));
				ctl.setLblListaProprietariosJustoPossuidores(Util.getString("cefir_lbl_justo_possuidores"));
			} else {
				ctl.setTituloProprietariosJustoPossuidores(Util.getString("cefir_lbl_proprietarios_atuais_imovel"));
				ctl.setLblListaProprietariosJustoPossuidores(Util.getString("cefir_lbl_proprietarios_imovel"));
			}

			ctl.setLblBotaoIncluirProprietariosJustoPossuidores(Util.getString("cefir_lbl_incluir_justo_possuidor"));
			ctl.setLblDocumentoPossePropriedade(Util.getString("cefir_lbl_documento_posse"));
		} else {
			ctl.setTituloProprietariosJustoPossuidores(Util.getString("cefir_lbl_proprietarios_atuais_imovel"));
			ctl.setLblListaProprietariosJustoPossuidores(Util.getString("cefir_lbl_proprietarios_imovel"));
			ctl.setLblBotaoIncluirProprietariosJustoPossuidores(Util.getString("cefir_lbl_incluir_proprietario"));
			ctl.setLblDocumentoPossePropriedade(Util.getString("cefir_lbl_documento_propriedade"));
		}
	}
}
