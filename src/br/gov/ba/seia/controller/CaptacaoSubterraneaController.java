package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("captacaoSubterraneaController")
@ViewScoped
public class CaptacaoSubterraneaController extends BaseDialogOutorgaController {

	private PerguntaRequerimento perguntaNR_A4_DCAPSUB_P11;
	private PerguntaRequerimento perguntaNR_A4_DCAPSUB_P12;
	private PerguntaRequerimento perguntaNR_A4_DCAPSUB_P14;

	@Override
	public void load() {

		try {

			limpar();

			carregarPerguntas();
			calcularVazaoSugeridaParaCaptacao();
			carregarValoresCaminhaoPipa();

			Outorga outorgaCaptacaoSubterranea = outorgaService.buscarOutorgaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA,super.getRequerimento());
			/**
			 * Correção #6785
			 * Só deve existir UMA Ourorga de Modalidade Captação para UM Requerimento
			 * Outorga outorgaCaptacaoSubterranea = this.outorgaService.buscarOutorgaByModalidadeAndRequerimento(ModalidadeOutorgaEnum.CAPTACAO, super.getRequerimento());
			 */
			if(!Util.isNullOuVazio(outorgaCaptacaoSubterranea)){
				outorga = outorgaCaptacaoSubterranea;
				outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(outorga);
			}

			editMode = false;

			super.removerImoveisCadastrados(abaOutorgaController.getCaptacoesSubterraneas());

			carregarImoveisParaOutorga();

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void fecharTela() {
		LocalizacaoGeograficaGenericController localizacaoGeograficaGenericController = (LocalizacaoGeograficaGenericController) SessaoUtil.recuperarManagedBean("#{localizacaoGeograficaGenericController}", LocalizacaoGeograficaGenericController.class);
		localizacaoGeograficaGenericController.fecharDialog();
	}

	public void visualizar(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		try {
			this.outorgaLocalizacaoGeografica =  outorgaLocalizacaoGeografica;
			outorga = outorgaLocalizacaoGeografica.getIdeOutorga();

			calcularVazaoSugeridaParaCaptacao();
			carregarPerguntas();
			perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(super.listaPerguntasRequerimento, getRequerimento(),(Imovel)null, outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			carregarValoresCaminhaoPipa();

			carregarFinalidadesUsoAgua();

			carregarOutorgaLocalizacaoGeograficaImovel();

			carregarImoveisParaOutorga();

			editMode = false;
			novoRequerimentoController.verificaImovelNaLista();
			if(novoRequerimentoController.isImovelRural()) {
				setImoveis(novoRequerimentoController.listarImovel());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public <T> void editar(T objeto) {
		try {
			outorgaLocalizacaoGeografica = (OutorgaLocalizacaoGeografica) objeto;
			outorga = outorgaLocalizacaoGeografica.getIdeOutorga();

			carregarPerguntas();
			calcularVazaoSugeridaParaCaptacao();
			perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(super.listaPerguntasRequerimento, getRequerimento(),(Imovel)null, outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			carregarValoresCaminhaoPipa();

			carregarFinalidadesUsoAgua();

			carregarOutorgaLocalizacaoGeograficaImovel();

			carregarImoveisParaOutorga();

			editMode = true;
			novoRequerimentoController.verificaImovelNaLista();
			if(novoRequerimentoController.isImovelRural()) {
				setImoveis(novoRequerimentoController.listarImovel());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarPerguntas() {
		super.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();

		this.perguntaNR_A4_DCAPSUB_P11 = super.carregarPerguntaByCod("NR_A4_DCAPSUB_P11");
		this.perguntaNR_A4_DCAPSUB_P12 = super.carregarPerguntaByCod("NR_A4_DCAPSUB_P12");
		this.perguntaNR_A4_DCAPSUB_P14 = super.carregarPerguntaByCod("NR_A4_DCAPSUB_P14");

		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUB_P11);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUB_P12);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCAPSUB_P14);
	}

	private void carregarValoresCaminhaoPipa() {
		this.outorga.setValorVolumeCaminhaoPipa(this.abaOutorgaController.getValorVolumeCaminhaoPipa());
		this.outorga.setQtdTransporteCaminhaoPipa(this.abaOutorgaController.getQtdTransporteCaminhaoPipa());
	}

	public void valueChangePerguntaNR_A4_DCAPSUB_P12(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			PerguntaRequerimento perguntaNR_A4_P11 = this.abaOutorgaController.getPerguntaNR_A4_P11();
			if(!Util.isNullOuVazio(perguntaNR_A4_P11) && !Util.isNullOuVazio(perguntaNR_A4_P11.getIndResposta())){
			
				if(perguntaNR_A4_P11.getIndResposta() && !Util.isNullOuVazio(outorga.getValorVolumeCaminhaoPipa()) && !Util.isNullOuVazio(outorga.getQtdTransporteCaminhaoPipa())){
					outorgaLocalizacaoGeografica.setNumVazao(calcularVazaoSugerida(outorga));
				} else {
					outorgaLocalizacaoGeografica.setNumVazao(null);
				}
				
				outorgaLocalizacaoGeografica.setDtcPerfuracaoPoco(null);
				perguntaNR_A4_DCAPSUB_P14.setIndResposta(null);
			}
		}
	}

	public void valueChangePerguntaNR_A4_DCAPSUB_P14(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			outorgaLocalizacaoGeografica.setNumOficio(null);
			outorgaLocalizacaoGeografica.setDtcEmissaoOficio(null);
		}
	}

	public void calcularVazaoSugeridaParaCaptacao(){
		
		PerguntaRequerimento perguntaNR_A4_P1411 = abaOutorgaController.getPerguntaNR_A4_P1411();
		
		if(!Util.isNullOuVazio(perguntaNR_A4_P1411.getIndResposta()) && perguntaNR_A4_P1411.getIndResposta() && 
		   !Util.isNullOuVazio(abaOutorgaController.getValorVolumeCaminhaoPipa()) && 
		   !Util.isNullOuVazio(abaOutorgaController.getQtdTransporteCaminhaoPipa())){

			if(outorgaLocalizacaoGeografica.getNumVazao() == null 
					|| outorgaLocalizacaoGeografica.getNumVazao().equals("") 
					|| outorgaLocalizacaoGeografica.getNumVazao().compareTo(new BigDecimal("0"))==0) {
				
				outorgaLocalizacaoGeografica.setNumVazao(calcularVazaoSugerida(outorga));
			}
		}
	}

	private BigDecimal calcularVazaoSugerida(Outorga outorga) {
		return abaOutorgaController.getValorVolumeCaminhaoPipa().multiply(new BigDecimal(abaOutorgaController.getQtdTransporteCaminhaoPipa().intValue()));
	}

	@Override
	public void salvar() {
		try {
			if(this.validar()){

				this.salvarOutorga(ModalidadeOutorgaEnum.CAPTACAO_SUBTERRANEA);

				this.salvarOutorgaTipoCaptacao();

				this.outorgaLocalizacaoGeograficaService.salvarAtualizar(this.outorgaLocalizacaoGeografica);

				this.salvarPerguntasRequerimento();

				this.salvarOutorgaLocalizacaoGeograficaFinalidade(this.outorgaLocalizacaoGeografica);

				this.salvarOutorgaLocalizacaoGeograficaImovel(this.outorgaLocalizacaoGeografica);

				if(this.editMode) {
					JsfUtil.addSuccessMessage("Captação Subterrânea atualizada com sucesso.");
				} else {
					JsfUtil.addSuccessMessage("Captação Subterrânea salva com sucesso.");
				}

				this.abaOutorgaController.adicionarOuAtualizarCaptacaoSubterranea(this.outorgaLocalizacaoGeografica);

				RequestContext.getCurrentInstance().execute("dialogCapSub.hide()");

			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarOutorgaTipoCaptacao()  {
		OutorgaTipoCaptacao outorgaTipoCaptacao = new OutorgaTipoCaptacao(this.outorgaLocalizacaoGeografica.getIdeOutorga(),new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId()));
		this.outorgaService.salvarAtualizarOutorgaTipoCaptacao(outorgaTipoCaptacao);
	}

	@Override
	boolean validar() {
		boolean valido = true;

		if(!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica)){
			if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica()) || !super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, inclua os pontos.");
				valido = false;
			}
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaImovel())){
				JsfUtil.addWarnMessage("Por favor, selecione um Imóvel.");
				valido = false;
			}
		}
		
		if(!Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUB_P11) && Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUB_P11.getIndResposta())){
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.1.");
			valido = false;
		}
		
		if(!Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUB_P12)){
			if(!Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUB_P12) && Util.isNullOuVazio(this.perguntaNR_A4_DCAPSUB_P12.getIndResposta())){
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.2.");
				valido = false;
			}else{
				if(this.perguntaNR_A4_DCAPSUB_P12.getIndResposta()){
					if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVazao()) || this.outorgaLocalizacaoGeografica.getNumVazao().compareTo(BigDecimal.ZERO)==0
							|| !Util.validaTamanhoString(outorgaLocalizacaoGeografica.getNumVazao().toString(),11)){
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.1.");
						valido = false;
					}
					if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcPerfuracaoPoco()) || Util.validarDuasDatas(new Date(),this.outorgaLocalizacaoGeografica.getDtcPerfuracaoPoco())){
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.2.2. Lembre-se a data informada não pode ser posterior a data de hoje.");
						valido = false;
					}
				}
			}
		}
		
		if(!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica)){
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
					
				}
				
				if(this.outorgaLocalizacaoGeografica.isPulverizacaoCaptacao()){
					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaPulverizada()) 
							|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaPulverizada().toString(),11)){
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.2.");
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
			if(this.outorgaLocalizacaoGeografica.isPulverizacaoCaptacao()){
				if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaPulverizada()) || !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaPulverizada().toString(),11)){
					JsfUtil.addWarnMessage("Por favor, preencha o campo 1.3.2.");
					valido = false;
				}
			}
			
			//Tentei usar o método after do próprio Date mas não foi suficiente para minha função
			Calendar gCal = new GregorianCalendar();
			if(!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcPerfuracaoPoco())){
				gCal.setTime(this.outorgaLocalizacaoGeografica.getDtcPerfuracaoPoco());
			}
			if(!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcPerfuracaoPoco()) && gCal.after(new GregorianCalendar(2009,8,9))){
				if(Util.isNullOuVazio(perguntaNR_A4_DCAPSUB_P14.getIndResposta())){
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.4.");
					valido = false;
				} else if(perguntaNR_A4_DCAPSUB_P14.getIndResposta()){
					if(this.outorgaLocalizacaoGeografica.getNumOficio().trim().isEmpty() || !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumOficio(), 50)){
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.4.1.");
						valido = false;
					}else if(!Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumOficio(), 50)){
						JsfUtil.addWarnMessage("O campo 1.4.1. (Número de oficio) não pode exceder 50 caracteres.");
						valido = false;
					}
					if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcEmissaoOficio()) || Util.validarDuasDatas(new Date(), this.outorgaLocalizacaoGeografica.getDtcEmissaoOficio())){
						JsfUtil.addWarnMessage("Por favor, preencha o campo 1.4.2. Lembre-se a data informada não pode ser posterior a data de hoje.");
						valido = false;
					}
				}
			}
		}
		
		return valido;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUB_P11() {
		return perguntaNR_A4_DCAPSUB_P11;
	}

	public void setPerguntaNR_A4_DCAPSUB_P11(PerguntaRequerimento perguntaNR_A4_DCAPSUB_P11) {
		this.perguntaNR_A4_DCAPSUB_P11 = perguntaNR_A4_DCAPSUB_P11;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUB_P12() {
		return perguntaNR_A4_DCAPSUB_P12;
	}

	public void setPerguntaNR_A4_DCAPSUB_P12(PerguntaRequerimento perguntaNR_A4_DCAPSUB_P12) {
		this.perguntaNR_A4_DCAPSUB_P12 = perguntaNR_A4_DCAPSUB_P12;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DCAPSUB_P14() {
		return perguntaNR_A4_DCAPSUB_P14;
	}

	public void setPerguntaNR_A4_DCAPSUB_P14(PerguntaRequerimento perguntaNR_A4_DCAPSUB_P14) {
		this.perguntaNR_A4_DCAPSUB_P14 = perguntaNR_A4_DCAPSUB_P14;
	}

	
	public void visualizar() {
		try {
			carregarPerguntas();
			perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(super.listaPerguntasRequerimento, getRequerimento(),(Imovel)null, outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());


			carregarFinalidadesUsoAgua();

			carregarOutorgaLocalizacaoGeograficaImovel();

			carregarImoveisParaOutorga();

			editMode = false;
			novoRequerimentoController.verificaImovelNaLista();
			if(novoRequerimentoController.isImovelRural()) {
				setImoveis(novoRequerimentoController.listarImovel());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
}
