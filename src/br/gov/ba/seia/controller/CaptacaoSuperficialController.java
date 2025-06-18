package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovelPK;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("captacaoSuperficialController")
@ViewScoped
public class CaptacaoSuperficialController extends BaseDialogOutorgaController {

	private Collection<TipoBarragem> tiposBarragem;

	private PerguntaRequerimento perguntaNR_A4_DCAPSUP_P11;
	private PerguntaRequerimento perguntaNR_A4_DCAPSUP_P12;
	private PerguntaRequerimento perguntaNR_A4_DCAPSUP_P121;
	private PerguntaRequerimento perguntaNR_A4_DCAPSUP_P1211;
	private PerguntaRequerimento perguntaNR_A4_DCAPSUP_P12112;

	private Boolean dispensa;

	@Override
	public void load() {

		try {
			this.limpar();

			this.carregarPerguntas();
			this.calcularVazaoSugeridaParaCaptacao();
			this.carregarValoresCaminhaoPipa();

			Outorga outorgaCaptacaoSuperficial = this.outorgaService.buscarOutorgaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL,super.getRequerimento());
			/**
			 * Correção #6785
			 * Só deve existir UMA Ourorga de Modalidade Captação para UM Requerimento
			 * Outorga outorgaCaptacaoSuperficial = this.outorgaService.buscarOutorgaByModalidadeAndRequerimento(ModalidadeOutorgaEnum.CAPTACAO, super.getRequerimento());
			 */
			if(!Util.isNullOuVazio(outorgaCaptacaoSuperficial)){
				this.outorga = outorgaCaptacaoSuperficial;
				this.outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(this.outorga);
			}

			this.tiposBarragem = this.outorgaService.carregaListaTipoBarragem();
			this.editMode = false;

			super.removerImoveisCadastrados(this.abaOutorgaController.getCaptacaoesSuperficiais());

			this.carregarImoveisParaOutorga();

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public <T> void editar(T objeto) {
		try {
			this.outorgaLocalizacaoGeografica = (OutorgaLocalizacaoGeografica) objeto;
				
			carregarDispensa();
				
			this.outorga = this.outorgaLocalizacaoGeografica.getIdeOutorga();
			this.calcularVazaoSugeridaParaCaptacao();
			this.carregarPerguntas();
			this.perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(super.listaPerguntasRequerimento, this.getRequerimento(),(Imovel)null,this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			this.carregarValoresCaminhaoPipa();
			this.carregarOutorgaLocalizacaoGeograficaImovel();
			this.carregarFinalidadesUsoAgua();
			this.tiposBarragem = this.outorgaService.carregaListaTipoBarragem();
			this.editMode = true;
			this.carregarImoveisParaOutorga();
			this.novoRequerimentoController.verificaImovelNaLista();
			if(this.novoRequerimentoController.isImovelRural()) {
				this.setImoveis(this.novoRequerimentoController.listarImovel());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDispensa() {
		if(Util.isNullOuVazio(outorgaLocalizacaoGeografica.getDtcPublicacaoPortariaBarragem()) && Util.isNullOuVazio(outorgaLocalizacaoGeografica.getNumPortariaBarragem())){
			dispensa = true;
		}
		else if (Util.isNullOuVazio(outorgaLocalizacaoGeografica.getDtcEmissaoOficio()) && Util.isNullOuVazio(outorgaLocalizacaoGeografica.getNumOficio())){
			dispensa = false;
		}
	}

	public void fecharTela() {
		LocalizacaoGeograficaGenericController localizacaoGeograficaGenericController = (LocalizacaoGeograficaGenericController) SessaoUtil.recuperarManagedBean("#{localizacaoGeograficaGenericController}", LocalizacaoGeograficaGenericController.class);
		localizacaoGeograficaGenericController.fecharDialog();

	}

	public void visualizar(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		try {
			this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
			this.outorga = this.outorgaLocalizacaoGeografica.getIdeOutorga();

			this.carregarDispensa();
			this.calcularVazaoSugeridaParaCaptacao();
			this.carregarPerguntas();
			this.perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(super.listaPerguntasRequerimento, this.getRequerimento(),(Imovel)null,this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			this.carregarValoresCaminhaoPipa();
			this.carregarOutorgaLocalizacaoGeograficaImovel();
			this.carregarFinalidadesUsoAgua();
			this.tiposBarragem = this.outorgaService.carregaListaTipoBarragem();
			this.editMode = false;
			this.carregarImoveisParaOutorga();
			this.novoRequerimentoController.verificaImovelNaLista();
			if(this.novoRequerimentoController.isImovelRural()) {
				this.setImoveis(this.novoRequerimentoController.listarImovel());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarPerguntas() {
		super.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();

		this.perguntaNR_A4_DCAPSUP_P11 = super.carregarPerguntaByCod("NR_A4_DCAPSUP_P11");
		this.perguntaNR_A4_DCAPSUP_P12 = super.carregarPerguntaByCod("NR_A4_DCAPSUP_P12");
		this.perguntaNR_A4_DCAPSUP_P121 = super.carregarPerguntaByCod("NR_A4_DCAPSUP_P121");
		this.perguntaNR_A4_DCAPSUP_P1211 = super.carregarPerguntaByCod("NR_A4_DCAPSUP_P1211");
		this.perguntaNR_A4_DCAPSUP_P12112 = super.carregarPerguntaByCod("NR_A4_DCAPSUP_P12112");

		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUP_P11);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUP_P12);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUP_P121);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUP_P1211);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUP_P12112);

	}

	private void carregarValoresCaminhaoPipa() {
		this.outorga.setValorVolumeCaminhaoPipa(this.abaOutorgaController.getValorVolumeCaminhaoPipa());
		this.outorga.setQtdTransporteCaminhaoPipa(this.abaOutorgaController.getQtdTransporteCaminhaoPipa());
	}

	public void calcularVazaoSugeridaParaCaptacao(){
		PerguntaRequerimento perguntaNR_A4_P1411 = this.abaOutorgaController.getPerguntaNR_A4_P1411();
		
		if(!Util.isNullOuVazio(perguntaNR_A4_P1411.getIndResposta()) && perguntaNR_A4_P1411.getIndResposta() && 
		   !Util.isNullOuVazio(this.abaOutorgaController.getValorVolumeCaminhaoPipa()) && !Util.isNullOuVazio(this.abaOutorgaController.getQtdTransporteCaminhaoPipa())){
			
			if(outorgaLocalizacaoGeografica.getNumVazao()==null){
				outorgaLocalizacaoGeografica.setNumVazao(calcularVazaoSugerida(outorga));				
			} 
		}
	}

	private BigDecimal calcularVazaoSugerida(Outorga outorga) {
		BigDecimal temp = new BigDecimal(this.abaOutorgaController.getQtdTransporteCaminhaoPipa().intValue());
		return this.abaOutorgaController.getValorVolumeCaminhaoPipa().multiply(temp);
	}

	public void valueChangeOutorgaDispensa(ValueChangeEvent event){
		Boolean isDispensa = (Boolean) event.getNewValue();
		if(isDispensa){	
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
		}else{
			this.outorgaLocalizacaoGeografica.setDtcEmissaoOficio(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
		}
		
		dispensa = isDispensa;
	}


	public void valueChangePerguntaNR_A4_P12(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.perguntaNR_A4_DCAPSUP_P121.setIndResposta(null);
			this.perguntaNR_A4_DCAPSUP_P1211.setIndResposta(null);
			this.outorgaLocalizacaoGeografica.setIdeTipoBarragem(null);
			this.perguntaNR_A4_DCAPSUP_P12112.setIndResposta(null);
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumProcessoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
		}
	}

	public void valueChangePerguntaNR_A4_DCAPSUP_P121(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.outorgaLocalizacaoGeografica.setIdeTipoBarragem(null);
			this.perguntaNR_A4_DCAPSUP_P12112.setIndResposta(null);
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
			this.outorgaLocalizacaoGeografica.setNumAreaInundadaReservatorio(null);
		}
	}

	public void valueChangePerguntaNR_A4_DCAPSUP_P1211(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.outorgaLocalizacaoGeografica.setIdeTipoBarragem(null);
			this.perguntaNR_A4_DCAPSUP_P12112.setIndResposta(null);
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumProcessoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
		}
	}

	public void valueChangePerguntaNR_A4_DCAPSUP_P12112(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumAreaIrrigada(null);
			this.outorgaLocalizacaoGeografica.setNumAreaIrrigadaCaptacao(null);
			this.outorgaLocalizacaoGeografica.setNumProcessoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
			this.outorgaLocalizacaoGeografica.setNumAreaInundadaReservatorio(null);
		}
	}

	@Override
	public void salvar() {
		try {
			if(this.validar()){

				this.salvarOutorga(ModalidadeOutorgaEnum.CAPTACAO_SUPERFICIAL);

				this.salvarOutorgaTipoCaptacao();

				this.outorgaLocalizacaoGeograficaService.salvarAtualizar(this.outorgaLocalizacaoGeografica);

				this.salvarPerguntasRequerimento();

				this.salvarOutorgaLocalizacaoGeograficaFinalidade(this.outorgaLocalizacaoGeografica);

				this.salvarOutorgaLocalizacaoGeograficaImovel(this.outorgaLocalizacaoGeografica);

				if(this.editMode) {
					JsfUtil.addSuccessMessage("Captação Superficial atualizada com sucesso.");
				} else {
					JsfUtil.addSuccessMessage("Captação Superficial salva com sucesso.");
				}

				this.abaOutorgaController.adicionarOuAtualizarCaptacaoSuperficial(this.outorgaLocalizacaoGeografica);

				RequestContext.getCurrentInstance().execute("dialogCapSuper.hide()");

			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	boolean validar() {
		boolean valido = true;
		if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaImovel())){
			JsfUtil.addWarnMessage("Você precisa selecionar o(s) Imovel(is) antes de salvar.");
			valido = false;
		}
		if (!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
			if (!super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, inclua os pontos da captação superficial.");
				valido = false;
			}
			if(Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUP_P11.getIndResposta())){
				JsfUtil.addWarnMessage("Você precisa responder a questão 1.1");
				valido = false;
			}
			if(Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUP_P12.getIndResposta())){
				JsfUtil.addWarnMessage("Você precisa responder a questão 1.2");
				valido = false;
			} else if(this.perguntaNR_A4_DCAPSUP_P12.getIndResposta() ){
				if(Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUP_P121.getIndResposta())){
					JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1");
					valido = false;
				} else if(this.perguntaNR_A4_DCAPSUP_P121.getIndResposta() ){
					if(Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUP_P1211.getIndResposta())){
						JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1");
						valido = false;
					} else if(this.perguntaNR_A4_DCAPSUP_P1211.getIndResposta() ){
						if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeTipoBarragem())){
							JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.1");
							valido = false;
						}
						if(Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUP_P12112.getIndResposta())){
							JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2");
							valido = false;
						} else if(this.perguntaNR_A4_DCAPSUP_P12112.getIndResposta() ){
							if(Util.isNullOuVazio(this.dispensa)){
								JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1");
								valido = false;
							} else {
								if((!this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumPortariaBarragem()) || this.outorgaLocalizacaoGeografica.getNumPortariaBarragem().trim().equals("0") ||
										!Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumPortariaBarragem(), 50))){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.1");
									valido = false;
								}
								if((!this.dispensa) && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcPublicacaoPortariaBarragem())){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.2");
									valido = false;
								}
								if((this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumOficio())  || this.outorgaLocalizacaoGeografica.getNumOficio().trim().equals("0")
										|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumOficio(), 50))){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.1");
									valido = false;
								}
								if((this.dispensa) && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcEmissaoOficio())){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.2");
									valido = false;
								}
								if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumProcessoBarragem()) ||  this.outorgaLocalizacaoGeografica.getNumProcessoBarragem().trim().equals("0")
										|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumProcessoBarragem(), 50))){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.3.");
									valido = false;
								}
								if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVolumeAcumulacaoBarragem()) || this.outorgaLocalizacaoGeografica.getNumVolumeAcumulacaoBarragem().compareTo(BigDecimal.ZERO) == 0)
										|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumVolumeAcumulacaoBarragem().toString(), 11)){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.4");
									valido = false;
								}
								if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaInundadaReservatorio()) || this.outorgaLocalizacaoGeografica.getNumAreaInundadaReservatorio().compareTo(BigDecimal.ZERO)==0)
										|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaInundadaReservatorio().toString(), 11)){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.5");
									valido = false;
								}
								if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumDescargaFundo()) || this.outorgaLocalizacaoGeografica.getNumDescargaFundo().compareTo(BigDecimal.ZERO)==0)
										|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumDescargaFundo().toString(), 11)){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.6");
									valido = false;
								}
								if(!Util.isNullOuVazio(this.dispensa) && this.outorgaLocalizacaoGeografica.isIrrigacao()  && ((Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaIrrigada()) || BigDecimal.ZERO.equals(this.outorgaLocalizacaoGeografica.getNumAreaIrrigada()))
										|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaIrrigada().toString(), 11))){
									JsfUtil.addWarnMessage("Você precisa responder a questão 1.2.1.1.2.1.7.1.");
									valido = false;
								}


							}
						}
					}
				}
			}
			
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAguaCaptacao())){
				JsfUtil.addWarnMessage("Por favor, selecione uma ou mais finalidade para o campo 1.3.");
				valido = false;
				
			} else {
				if(this.outorgaLocalizacaoGeografica.isIrrigacaoCaptacao()){
					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaIrrigadaCaptacao()) 
							|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaIrrigadaCaptacao().toString(),11)){
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.1.");
						valido = false;
					}

					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaEmpreendimento()) 
							|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaEmpreendimento().toString(),11)){
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.5.");
						valido = false;
					}
				}
				
				if(this.outorgaLocalizacaoGeografica.isPulverizacaoCaptacao()){
					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaPulverizada()) 
							|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaPulverizada().toString(),11)){
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.2.");
						valido = false;
					}

					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaEmpreendimento()) 
							|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaEmpreendimento().toString(),11)){
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.5.");
						valido = false;
					}
				}
				
				if(this.outorgaLocalizacaoGeografica.isDessedentacaoAnimal()){
					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaDessedentacaoAnimal()) 
							|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaDessedentacaoAnimal().toString(),11)){
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.3.");
						valido = false;
					}
				}
				
				if(this.outorgaLocalizacaoGeografica.isAbastecimentoIndustrial()){
					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIndAbastecimentoEmDistritoIndustrial())){ 
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.4.");
						valido = false;
					}
				}
			}

			if((Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVazao()) || this.outorgaLocalizacaoGeografica.getNumVazao().compareTo(BigDecimal.ZERO)==0)
					|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumVazao().toString(), 11)){
				JsfUtil.addWarnMessage("Você precisa responder a questão 1.4.");
				valido = false;
			}
		}
		else{
			JsfUtil.addWarnMessage("Você precisa adicionar Ponto Geográfico");
			valido = false;
		}
		return valido;
	}

	private void salvarOutorgaTipoCaptacao() throws Exception {
		OutorgaTipoCaptacao outorgaTipoCaptacao = new OutorgaTipoCaptacao(this.outorgaLocalizacaoGeografica.getIdeOutorga(),new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId()));
		this.outorgaService.salvarAtualizarOutorgaTipoCaptacao(outorgaTipoCaptacao);
	}


	@Override
	public void salvarOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		try {
			OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel = new OutorgaLocalizacaoGeograficaImovel();
			OutorgaLocalizacaoGeograficaImovelPK outorgaLocalizacaoGeograficaImovelPK = new OutorgaLocalizacaoGeograficaImovelPK();
			for (Imovel imovel : this.outorgaLocalizacaoGeografica.getListaImovel()) {
				outorgaLocalizacaoGeograficaImovelPK.setIdeImovel(imovel.getIdeImovel());
				outorgaLocalizacaoGeograficaImovelPK.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
				outorgaLocalizacaoGeograficaImovel.setIdeImovel(imovel);
				outorgaLocalizacaoGeograficaImovel.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
				outorgaLocalizacaoGeograficaImovel.setOutorgaLocalizacaoGeograficaImovelPK(outorgaLocalizacaoGeograficaImovelPK);
				boolean registroNovo = true;
				for(OutorgaLocalizacaoGeograficaImovelPK outorgaPK:super.getListaOutorgaLocGeoImovelPKCadastradas()) {
					if((outorgaPK.getIdeImovel() == outorgaLocalizacaoGeograficaImovelPK.getIdeImovel()) && 
							(outorgaPK.getIdeOutorgaLocalizacaoGeografica() == outorgaLocalizacaoGeograficaImovelPK.getIdeOutorgaLocalizacaoGeografica())) {
						registroNovo = false;
					}
				}
				if(registroNovo) {
					this.outorgaLocalizacaoGeograficaService.salvarAtualizarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaImovel);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public Collection<TipoBarragem> getTiposBarragem() {
		return tiposBarragem;
	}

	public void setTiposBarragem(Collection<TipoBarragem> tiposBarragem) {
		this.tiposBarragem = tiposBarragem;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUP_P11() {
		return perguntaNR_A4_DCAPSUP_P11;
	}

	public void setPerguntaNR_A4_DCAPSUP_P11(PerguntaRequerimento perguntaNR_A4_DCAPSUP_P11) {
		this.perguntaNR_A4_DCAPSUP_P11 = perguntaNR_A4_DCAPSUP_P11;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUP_P12() {
		return perguntaNR_A4_DCAPSUP_P12;
	}

	public void setPerguntaNR_A4_DCAPSUP_P12(PerguntaRequerimento perguntaNR_A4_DCAPSUP_P12) {
		this.perguntaNR_A4_DCAPSUP_P12 = perguntaNR_A4_DCAPSUP_P12;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUP_P121() {
		return perguntaNR_A4_DCAPSUP_P121;
	}

	public void setPerguntaNR_A4_DCAPSUP_P121(PerguntaRequerimento perguntaNR_A4_DCAPSUP_P121) {
		this.perguntaNR_A4_DCAPSUP_P121 = perguntaNR_A4_DCAPSUP_P121;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUP_P1211() {
		return perguntaNR_A4_DCAPSUP_P1211;
	}

	public void setPerguntaNR_A4_DCAPSUP_P1211(PerguntaRequerimento perguntaNR_A4_DCAPSUP_P1211) {
		this.perguntaNR_A4_DCAPSUP_P1211 = perguntaNR_A4_DCAPSUP_P1211;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUP_P12112() {
		return perguntaNR_A4_DCAPSUP_P12112;
	}

	public void setPerguntaNR_A4_DCAPSUP_P12112(PerguntaRequerimento perguntaNR_A4_DCAPSUP_P12112) {
		this.perguntaNR_A4_DCAPSUP_P12112 = perguntaNR_A4_DCAPSUP_P12112;
	}

	public Boolean getDispensa() {
		return dispensa;
	}

	public void setDispensa(Boolean dispensa) {
		this.dispensa = dispensa;
	}

}
