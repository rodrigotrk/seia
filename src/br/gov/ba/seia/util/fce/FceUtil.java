package br.gov.ba.seia.util.fce;

import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.AGROSSILVOPASTORIL;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_ABASTECIMENTO_INDUSTRIAL;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_ASV;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_AUTORIZACAO_MINERACAO;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_CARACTERIZACAO_SES;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_CONSTRUCAO_BARRAGEM;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_DESSEDENTACAO_ANIMAL;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_GERACAO_ENERGIA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_INDUSTRIA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_INFRAESTRUTURA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_INTERVENCAO_BARRAGEM;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_INTERVENCAO_BARRAGEM_SEM_PONTO;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_IRRIGACAO;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_LANCAMENTO_EFLUENTES;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_LICENCIAMENTO_AQUICULTURA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_LICENCIAMENTO_MINERACAO;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_LOCALIZACAO_GEOGRAFICA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUBTERRANEA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUPERFICIAL;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_OUTORGA_INTERVENCAO_ABASTECIMENTO_HUMANO;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_PULVERIZACAO;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_SISTEMA_ABASTECIMENTO_AGUA;
import static br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum.FCE_TURISMO;
import br.gov.ba.seia.controller.AsvDadosGeraisController;
import br.gov.ba.seia.controller.FceAbastecimentoIndustrialController;
import br.gov.ba.seia.controller.FceAutorizacaoMineracaoController;
import br.gov.ba.seia.controller.FceBarragemController;
import br.gov.ba.seia.controller.FceCanalController;
import br.gov.ba.seia.controller.FceCaptacaoSubterraneaController;
import br.gov.ba.seia.controller.FceCaptacaoSuperficialController;
import br.gov.ba.seia.controller.FceController;
import br.gov.ba.seia.controller.FceDessedentacaoAnimalController;
import br.gov.ba.seia.controller.FceFlorestalAbstractController;
import br.gov.ba.seia.controller.FceFlorestalController;
import br.gov.ba.seia.controller.FceGeracaoEnergiaController;
import br.gov.ba.seia.controller.FceIndustriaController;
import br.gov.ba.seia.controller.FceInfraestruturaController;
import br.gov.ba.seia.controller.FceIntervencaoAbastecimentoHumanoController;
import br.gov.ba.seia.controller.FceIntervencaoBarragemController;
import br.gov.ba.seia.controller.FceIntervencaoBarragemControllerSemPonto;
import br.gov.ba.seia.controller.FceIntervencaoMineracaoController;
import br.gov.ba.seia.controller.FceIrrigacaoController;
import br.gov.ba.seia.controller.FceLancamentoEfluentesController;
import br.gov.ba.seia.controller.FceLicenciamentoAquiculturaController;
import br.gov.ba.seia.controller.FceLicenciamentoAquiculturaDadosGeraisController;
import br.gov.ba.seia.controller.FceLicenciamentoAquiculturaDadosRequerimentoController;
import br.gov.ba.seia.controller.FceLicenciamentoMineracaoController;
import br.gov.ba.seia.controller.FceLinhaTransmissaoDistribuicaoController;
import br.gov.ba.seia.controller.FceLocalizacaoGeograficaController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaBarragemController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaCaptacaoController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaLancamentoController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaNavegacaoController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaRioController;
import br.gov.ba.seia.controller.FcePulverizacaoController;
import br.gov.ba.seia.controller.FceSaaController;
import br.gov.ba.seia.controller.FceSesController;
import br.gov.ba.seia.controller.FceTurismoController;
import br.gov.ba.seia.controller.TanqueRedeController;
import br.gov.ba.seia.controller.ViveiroEscavadoController;
import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

public class FceUtil {
	
	public static Fce duplicarFce(Fce fce, AnaliseTecnica analiseTecnica){
		if(isFceAgrossilvoPastoril(fce.getIdeDocumentoObrigatorio())) {
			
		}
		else if(isFceAsv(fce.getIdeDocumentoObrigatorio())){
// 			8592 - DadoConcedido ASV			
			AsvDadosGeraisController asvDadosGeraisController = (AsvDadosGeraisController) SessaoUtil.recuperarManagedBean("#{asvDadosGeraisController}", AsvDadosGeraisController.class);
			return duplicarFceAnaliseTecnica(fce, asvDadosGeraisController, analiseTecnica);
		}
		else if(isFceBarragem(fce.getIdeDocumentoObrigatorio())) {
			if(isFceBarragemComPonto(fce.getIdeDocumentoObrigatorio())){
				FceIntervencaoBarragemController fceIntervencaoBarragemController = (FceIntervencaoBarragemController) SessaoUtil.recuperarManagedBean("#{fceIntervencaoBarragemController}", FceIntervencaoBarragemController.class);
				return duplicarFceAnaliseTecnica(fce, fceIntervencaoBarragemController, analiseTecnica);
			}
			else if(isFceConstrucaoBarragem(fce.getIdeDocumentoObrigatorio())){
				FceBarragemController fceBarragemController = (FceBarragemController) SessaoUtil.recuperarManagedBean("#{fceBarragemController}", FceBarragemController.class);
				return duplicarFceAnaliseTecnica(fce, fceBarragemController, analiseTecnica);
			}
			else if(isFceBarragemSemPonto(fce.getIdeDocumentoObrigatorio())){
				FceIntervencaoBarragemControllerSemPonto fceIntervencaoBarragemController = (FceIntervencaoBarragemControllerSemPonto) SessaoUtil.recuperarManagedBean("#{fceIntervencaoBarragemControllerSemPonto}", FceIntervencaoBarragemControllerSemPonto.class);
				return duplicarFceAnaliseTecnica(fce, fceIntervencaoBarragemController, analiseTecnica);
			}
			
		}
		else if(isFceOutorgaAbastecimentoIndustrial(fce.getIdeDocumentoObrigatorio())) {
			FceAbastecimentoIndustrialController fceAbastecimentoIndustrialController = (FceAbastecimentoIndustrialController) SessaoUtil.recuperarManagedBean("#{fceAbastecimentoIndustrialController}", FceAbastecimentoIndustrialController.class);
			return duplicarFceAnaliseTecnica(fce, fceAbastecimentoIndustrialController, analiseTecnica);
		}
		else if(isFceOutorgaCaptacaoSubterranea(fce.getIdeDocumentoObrigatorio())) {
			FceCaptacaoSubterraneaController fceCaptacaoSubterraneaController = (FceCaptacaoSubterraneaController) SessaoUtil.recuperarManagedBean("#{fceCaptacaoSubterraneaController}", FceCaptacaoSubterraneaController.class);
			return duplicarFceAnaliseTecnica(fce, fceCaptacaoSubterraneaController, analiseTecnica);
		}
		else if(isFceOutorgaCaptacaoSuperficial(fce.getIdeDocumentoObrigatorio())) {
			FceCaptacaoSuperficialController fceCaptacaoSuperficialController = (FceCaptacaoSuperficialController) SessaoUtil.recuperarManagedBean("#{fceCaptacaoSuperficialController}", FceCaptacaoSuperficialController.class);
			return duplicarFceAnaliseTecnica(fce, fceCaptacaoSuperficialController, analiseTecnica);
		}
		else if(isFceOutorgaDessedentacaoAnimal(fce.getIdeDocumentoObrigatorio())) {
			FceDessedentacaoAnimalController fceDessedentacaoAnimalController = (FceDessedentacaoAnimalController) SessaoUtil.recuperarManagedBean("#{fceDessedentacaoAnimalController}", FceDessedentacaoAnimalController.class);
			return duplicarFceAnaliseTecnica(fce, fceDessedentacaoAnimalController, analiseTecnica);
		}
		else if(isFceOutorgaIrrigacao(fce.getIdeDocumentoObrigatorio())){
			FceIrrigacaoController fceIrrigacaoController = (FceIrrigacaoController) SessaoUtil.recuperarManagedBean("#{fceIrrigacaoController}", FceIrrigacaoController.class);
			return duplicarFceAnaliseTecnica(fce, fceIrrigacaoController, analiseTecnica);
		}
		else if(isFceOutorgaLancamentoEfluentes(fce.getIdeDocumentoObrigatorio())) {
			FceLancamentoEfluentesController fceLancamentoEfluentesController = (FceLancamentoEfluentesController) SessaoUtil.recuperarManagedBean("#{fceLancamentoEfluentesController}", FceLancamentoEfluentesController.class);
			return duplicarFceAnaliseTecnica(fce, fceLancamentoEfluentesController, analiseTecnica);
		}
		else if(isFceOutorgaPulverizacao(fce.getIdeDocumentoObrigatorio())) {
			FcePulverizacaoController fcePulverizacaoController = (FcePulverizacaoController) SessaoUtil.recuperarManagedBean("#{fcePulverizacaoController}", FcePulverizacaoController.class);
			return duplicarFceAnaliseTecnica(fce, fcePulverizacaoController, analiseTecnica);
		}
		else if(isFceIndustria(fce.getIdeDocumentoObrigatorio())) {
			FceIndustriaController fceIndustriaController = (FceIndustriaController) SessaoUtil.recuperarManagedBean("#{fceIndustriaController}", FceIndustriaController.class);
			return duplicarFceAnaliseTecnica(fce, fceIndustriaController, analiseTecnica);
		}
		else if(isFcePerfuracaoPoco(fce.getIdeDocumentoObrigatorio())) {
			FceLocalizacaoGeograficaController fceLocalizacaoGeograficaController = (FceLocalizacaoGeograficaController) SessaoUtil.recuperarManagedBean("#{fceLocalizacaoGeograficaController}", FceLocalizacaoGeograficaController.class);
			return duplicarFceAnaliseTecnica(fce, fceLocalizacaoGeograficaController, analiseTecnica);
		}
		else if(isFceAbastecimentoHumano(fce.getIdeDocumentoObrigatorio())) {
			FceIntervencaoAbastecimentoHumanoController fceIntervencaoAbastecimentoHumanoController = (FceIntervencaoAbastecimentoHumanoController) SessaoUtil.recuperarManagedBean("#{fceIntervencaoAbastecimentoHumanoController}", FceIntervencaoAbastecimentoHumanoController.class);
			return duplicarFceAnaliseTecnica(fce, fceIntervencaoAbastecimentoHumanoController, analiseTecnica);
		}
		else if(isFceOutorgaAquicultura(fce.getIdeDocumentoObrigatorio())) {
			FceOutorgaAquiculturaNavegacaoController navegacaoController = (FceOutorgaAquiculturaNavegacaoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaNavegacaoController}", FceOutorgaAquiculturaNavegacaoController.class);
			navegacaoController.setRequerimento(fce.getIdeRequerimento());
			
			FceOutorgaAquiculturaCaptacaoController captacaoController = (FceOutorgaAquiculturaCaptacaoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaCaptacaoController}", FceOutorgaAquiculturaCaptacaoController.class);
			captacaoController.prepararAnaliseTecnica(fce, analiseTecnica);
			navegacaoController.setAnaliseTecnica(false);
			
			FceOutorgaAquiculturaLancamentoController lancamentoController = (FceOutorgaAquiculturaLancamentoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaLancamentoController}", FceOutorgaAquiculturaLancamentoController.class);
			lancamentoController.prepararAnaliseTecnica(fce, analiseTecnica);
			navegacaoController.setAnaliseTecnica(false);

			FceOutorgaAquiculturaBarragemController barragemController = (FceOutorgaAquiculturaBarragemController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaBarragemController}", FceOutorgaAquiculturaBarragemController.class);
			barragemController.prepararAnaliseTecnica(fce, analiseTecnica);
			navegacaoController.setAnaliseTecnica(false);

			FceOutorgaAquiculturaRioController rioController = (FceOutorgaAquiculturaRioController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaRioController}", FceOutorgaAquiculturaRioController.class);
			rioController.prepararAnaliseTecnica(fce, analiseTecnica);
			
			return navegacaoController.getFce();
		}
		else if(isFceTurismo(fce.getIdeDocumentoObrigatorio())) {
			FceTurismoController fceTurismoController = (FceTurismoController) SessaoUtil.recuperarManagedBean("#{fceTurismoController}", FceTurismoController.class);
			return duplicarFceAnaliseTecnica(fce, fceTurismoController, analiseTecnica);
		}
		else if(isFceLicenciamentoAquicultura(fce.getIdeDocumentoObrigatorio())){
			FceLicenciamentoAquiculturaController controller = (FceLicenciamentoAquiculturaController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoAquiculturaController}", FceLicenciamentoAquiculturaController.class);
			controller.setRequerimento(fce.getIdeRequerimento());
			controller.setEmAnaliseTecnica(true);
			controller.setEmDuplicacao(true);
			controller.setEdicao(true);
			controller.setAnaliseTecnica(analiseTecnica);
			controller.carregarAba();
			controller.verificarEdicao();
			controller.prepararEdicao();
			
			FceLicenciamentoAquiculturaDadosRequerimentoController reqController = (FceLicenciamentoAquiculturaDadosRequerimentoController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoAquiculturaDadosRequerimentoController}", FceLicenciamentoAquiculturaDadosRequerimentoController.class);
			reqController.init();
			
			FceLicenciamentoAquiculturaDadosGeraisController dadosGerais = (FceLicenciamentoAquiculturaDadosGeraisController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoAquiculturaDadosGeraisController}", FceLicenciamentoAquiculturaDadosGeraisController.class);
			dadosGerais.init();
			dadosGerais.prepararAnaliseTecnica();
			
			ViveiroEscavadoController viveiroEscavado = (ViveiroEscavadoController) SessaoUtil.recuperarManagedBean("#{viveiroEscavadoController}", ViveiroEscavadoController.class);
			viveiroEscavado.init();
			viveiroEscavado.prepararDuplicacao();
			
			TanqueRedeController tanqueRede = (TanqueRedeController) SessaoUtil.recuperarManagedBean("#{tanqueRedeController}", TanqueRedeController.class);
			tanqueRede.init();
			tanqueRede.prepararDuplicacao();
			
			controller.prepararAnaliseTecnica(fce, analiseTecnica);
			
			return controller.getFce();
		}
		else if(isFceLicenciamentoMineracao(fce.getIdeDocumentoObrigatorio())){
			FceLicenciamentoMineracaoController fceLicMineracaoController = (FceLicenciamentoMineracaoController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoMineracaoController}", FceLicenciamentoMineracaoController.class);
			return duplicarFceAnaliseTecnica(fce, fceLicMineracaoController, analiseTecnica);
		}
		else if(isFceAutorizacaoMineracao(fce.getIdeDocumentoObrigatorio())){
			FceAutorizacaoMineracaoController fceAutMineracaoController = (FceAutorizacaoMineracaoController) SessaoUtil.recuperarManagedBean("#{fceAutorizacaoMineracaoController}", FceAutorizacaoMineracaoController.class);
			return duplicarFceAnaliseTecnica(fce, fceAutMineracaoController, analiseTecnica);
		}
		else if(isFceInfraestrutura(fce.getIdeDocumentoObrigatorio())){
			FceInfraestruturaController fceInfraestruturaController = (FceInfraestruturaController) 
					SessaoUtil.recuperarManagedBean("#{fceInfraestruturaController}", FceInfraestruturaController.class);
			
			return duplicarFceAnaliseTecnica(fce, fceInfraestruturaController, analiseTecnica);
		}
		else if(isFceCanais(fce.getIdeDocumentoObrigatorio())){
			FceCanalController fceCanalController = (FceCanalController) SessaoUtil.recuperarManagedBean("#{fceCanalController}", FceCanalController.class);
			return duplicarFceAnaliseTecnica(fce, fceCanalController, analiseTecnica);
		}
		else if(isFceIntervencaoMineracao(fce.getIdeDocumentoObrigatorio())){
			FceIntervencaoMineracaoController fceIntervencaoMineracaoController = (FceIntervencaoMineracaoController) SessaoUtil.recuperarManagedBean("#{fceIntervencaoMineracaoController}", FceIntervencaoMineracaoController.class);
			return duplicarFceAnaliseTecnica(fce, fceIntervencaoMineracaoController, analiseTecnica);
		}
		else if(isFceSistemaAbastecimentoAgua(fce.getIdeDocumentoObrigatorio())){
			FceSaaController fceSaaController = (FceSaaController) SessaoUtil.recuperarManagedBean("#{fceSaaController}", FceSaaController.class);
			return duplicarFceAnaliseTecnica(fce, fceSaaController, analiseTecnica);
		}
		else if(isFceSES(fce.getIdeDocumentoObrigatorio())){
			FceSesController fceSesController = (FceSesController) SessaoUtil.recuperarManagedBean("#{fceSesController}", FceSesController.class);
			return duplicarFceAnaliseTecnica(fce, fceSesController, analiseTecnica);
		}
		else if(isFceLinhaEnergiaTrasmissaoEnergia(fce.getIdeDocumentoObrigatorio())){
			FceLinhaTransmissaoDistribuicaoController fceLinTransmissaoDistribruicaoController = (FceLinhaTransmissaoDistribuicaoController) SessaoUtil.recuperarManagedBean("#{fceLinhaTransmissaoDistribruicaoController}", FceLinhaTransmissaoDistribuicaoController.class);
			return duplicarFceAnaliseTecnica(fce, fceLinTransmissaoDistribruicaoController, analiseTecnica);
		}
		else if(isFceGeracaoEnergia(fce.getIdeDocumentoObrigatorio())){
			FceGeracaoEnergiaController fceGeracaoEnergiaController = (FceGeracaoEnergiaController) SessaoUtil.recuperarManagedBean("#{fceGeracaoEnergiaController}", FceGeracaoEnergiaController.class);
			return duplicarFceAnaliseTecnica(fce, fceGeracaoEnergiaController, analiseTecnica);
		}
		else if (isFceFlorestal(fce.getIdeDocumentoObrigatorio())) {
			FceFlorestalController fceFlorestalController = (FceFlorestalController) SessaoUtil.recuperarManagedBean("#{fceFlorestalController}", FceFlorestalController.class);
			fceFlorestalController.init(fce);
			return duplicarFceAnaliseTecnica(fce, fceFlorestalController.getImpl(), analiseTecnica);
		}
		return null;
	}
	
	public static void visualizarFce(Fce fce){
		abrirFce(null,fce, true);
	}
	
	public static void abrirFce(Fce fce){
		abrirFce(null, fce, false);
	}
	
	public static void visualizarFce(DadoConcedidoFceImpl dadoConcedidoFceImpl){
		abrirFce(dadoConcedidoFceImpl, dadoConcedidoFceImpl.getFce(), true);
	}
	
	public static void abrirFce(DadoConcedidoFceImpl dadoConcedidoFceImpl){
		abrirFce(dadoConcedidoFceImpl, dadoConcedidoFceImpl.getFce(), false);
	}
	
	private static void abrirFce(DadoConcedidoFceImpl dadoConcedidoFceImpl, Fce fce, boolean desabilitarTudo) {
		
		DocumentoObrigatorio documentoObrigatorio = fce.getIdeDocumentoObrigatorio();
		if(isFceAgrossilvoPastoril(documentoObrigatorio)) {
			
		}
		else if(isFceAsv(documentoObrigatorio)) {
			AsvDadosGeraisController asvDadosGeraisController = (AsvDadosGeraisController) SessaoUtil.recuperarManagedBean("#{asvDadosGeraisController}", AsvDadosGeraisController.class);
			if(desabilitarTudo){
				asvDadosGeraisController.setDesabilitarTudo();
			}
			if(asvDadosGeraisController.getIsNotFceASV() && Util.isNullOuVazio(fce.getIdeAnaliseTecnica())){
				asvDadosGeraisController.setIsNotFceASV(false);
			}
			asvDadosGeraisController.abrirFce(fce);
			
//			desabilita a abertura do dialog de fce_asv.
		}
		else if(isFceBarragem(documentoObrigatorio)) {
			if(isFceConstrucaoBarragem(documentoObrigatorio)){
				FceBarragemController fceBarragemController = (FceBarragemController) SessaoUtil.recuperarManagedBean("#{fceBarragemController}", FceBarragemController.class);
				if(desabilitarTudo){
					fceBarragemController.setDesabilitarTudo();
					fceBarragemController.setDesabilitarDocumentoAdicional();
				}
				fceBarragemController.abrirFce(fce);
			}
			else if(isFceBarragemComPonto(documentoObrigatorio)){
				FceIntervencaoBarragemController fceIntervencaoBarragemController = (FceIntervencaoBarragemController) SessaoUtil.recuperarManagedBean("#{fceIntervencaoBarragemController}", FceIntervencaoBarragemController.class);
				if(desabilitarTudo){
					fceIntervencaoBarragemController.setDesabilitarTudo();
					fceIntervencaoBarragemController.setDesabilitarDocumentoAdicional();
				}
				fceIntervencaoBarragemController.abrirFce(fce);
			}
			else if(isFceBarragemSemPonto(documentoObrigatorio)){
				FceIntervencaoBarragemControllerSemPonto fceIntervencaoBarragemController = (FceIntervencaoBarragemControllerSemPonto) SessaoUtil.recuperarManagedBean("#{fceIntervencaoBarragemControllerSemPonto}", FceIntervencaoBarragemControllerSemPonto.class);
				if(desabilitarTudo){
					fceIntervencaoBarragemController.setDesabilitarTudo();
					fceIntervencaoBarragemController.setDesabilitarDocumentoAdicional();
				}
				fceIntervencaoBarragemController.abrirFce(fce);
			}
		}
		else if(isFceOutorgaAbastecimentoIndustrial(documentoObrigatorio)) {
			FceAbastecimentoIndustrialController fceAbastecimentoIndustrialController = (FceAbastecimentoIndustrialController) SessaoUtil.recuperarManagedBean("#{fceAbastecimentoIndustrialController}", FceAbastecimentoIndustrialController.class);
			if(desabilitarTudo){
				fceAbastecimentoIndustrialController.setDesabilitarTudo();
				fceAbastecimentoIndustrialController.setDesabilitarDocumentoAdicional();
			}
			fceAbastecimentoIndustrialController.abrirFce(fce);
		}
		else if(isFceOutorgaCaptacaoSubterranea(documentoObrigatorio)) {
			FceCaptacaoSubterraneaController fceCaptacaoSubterraneaController = (FceCaptacaoSubterraneaController) SessaoUtil.recuperarManagedBean("#{fceCaptacaoSubterraneaController}", FceCaptacaoSubterraneaController.class);
			if(desabilitarTudo){
				fceCaptacaoSubterraneaController.setDesabilitarTudo();
				fceCaptacaoSubterraneaController.setDesabilitarDocumentoAdicional();
			}
			fceCaptacaoSubterraneaController.abrirFce(fce);
		}
		else if(isFceOutorgaCaptacaoSuperficial(documentoObrigatorio)) {
			FceCaptacaoSuperficialController fceCaptacaoSuperficialController = (FceCaptacaoSuperficialController) SessaoUtil.recuperarManagedBean("#{fceCaptacaoSuperficialController}", FceCaptacaoSuperficialController.class);
			if(desabilitarTudo){
				fceCaptacaoSuperficialController.setDesabilitarTudo();
				fceCaptacaoSuperficialController.setDesabilitarDocumentoAdicional();
			}
			fceCaptacaoSuperficialController.abrirFce(fce);
		}
		else if(isFceOutorgaDessedentacaoAnimal(documentoObrigatorio)) {
			FceDessedentacaoAnimalController fceDessedentacaoAnimalController = (FceDessedentacaoAnimalController) SessaoUtil.recuperarManagedBean("#{fceDessedentacaoAnimalController}", FceDessedentacaoAnimalController.class);
			if(desabilitarTudo){
				fceDessedentacaoAnimalController.setDesabilitarTudo();
			}
			fceDessedentacaoAnimalController.abrirFce(fce);
		}
		else if(isFceOutorgaIrrigacao(documentoObrigatorio)) {
			FceIrrigacaoController fceIrrigacaoController = (FceIrrigacaoController) SessaoUtil.recuperarManagedBean("#{fceIrrigacaoController}", FceIrrigacaoController.class);
			if(desabilitarTudo){
				fceIrrigacaoController.setDesabilitarTudo();
			}
			fceIrrigacaoController.abrirFce(fce);
		}
		else if(isFceOutorgaLancamentoEfluentes(documentoObrigatorio)) {
			FceLancamentoEfluentesController fceLancamentoEfluentesController = (FceLancamentoEfluentesController) SessaoUtil.recuperarManagedBean("#{fceLancamentoEfluentesController}", FceLancamentoEfluentesController.class);
			if(desabilitarTudo){
				fceLancamentoEfluentesController.setDesabilitarTudo();
				fceLancamentoEfluentesController.setDesabilitarDocumentoAdicional();
			}
			fceLancamentoEfluentesController.abrirFce(fce);
		}
		else if(isFceOutorgaPulverizacao(documentoObrigatorio)) {
			FcePulverizacaoController fcePulverizacaoController = (FcePulverizacaoController) SessaoUtil.recuperarManagedBean("#{fcePulverizacaoController}", FcePulverizacaoController.class);
			if(desabilitarTudo){
				fcePulverizacaoController.setDesabilitarTudo();
			}
			fcePulverizacaoController.abrirFce(fce);
		}
		else if(isFceIndustria(documentoObrigatorio)) {
			FceIndustriaController fceIndustriaController = (FceIndustriaController) SessaoUtil.recuperarManagedBean("#{fceIndustriaController}", FceIndustriaController.class);
			if(desabilitarTudo){
				fceIndustriaController.setDesabilitarTudo();
			}
			fceIndustriaController.abrirFce(fce);
		}
		else if(isFcePerfuracaoPoco(documentoObrigatorio)) {
			FceLocalizacaoGeograficaController fceLocalizacaoGeograficaController = (FceLocalizacaoGeograficaController) SessaoUtil.recuperarManagedBean("#{fceLocalizacaoGeograficaController}", FceLocalizacaoGeograficaController.class);
			if(desabilitarTudo){
				fceLocalizacaoGeograficaController.setDesabilitarTudo();
				fceLocalizacaoGeograficaController.setDesabilitarDocumentoAdicional();
			}
			fceLocalizacaoGeograficaController.abrirFce(fce);
		}
		else if(isFceAbastecimentoHumano(documentoObrigatorio)) {
			FceIntervencaoAbastecimentoHumanoController fceIntervencaoAbastecimentoHumanoController = (FceIntervencaoAbastecimentoHumanoController) SessaoUtil.recuperarManagedBean("#{fceIntervencaoAbastecimentoHumanoController}", FceIntervencaoAbastecimentoHumanoController.class);
			if(desabilitarTudo){
				fceIntervencaoAbastecimentoHumanoController.setDesabilitarTudo();
			}
			fceIntervencaoAbastecimentoHumanoController.abrirFce(fce);
		}
		else if(isFceOutorgaAquicultura(documentoObrigatorio)) {
			FceOutorgaAquiculturaNavegacaoController navegacaoController = (FceOutorgaAquiculturaNavegacaoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaNavegacaoController}", FceOutorgaAquiculturaNavegacaoController.class);
			navegacaoController.limpar();
			navegacaoController.setFce(fce);
			if(!Util.isNull(fce.getIdeAnaliseTecnica())){
				navegacaoController.setAnaliseTecnica(true);
			}
			navegacaoController.setRequerimento(fce.getIdeRequerimento());
			if(desabilitarTudo){
				navegacaoController.setDesabilitarTudo();
			}
			navegacaoController.abrirDialog();
		}
		else if(isFceTurismo(documentoObrigatorio)) {
			FceTurismoController fceTurismoController = (FceTurismoController) SessaoUtil.recuperarManagedBean("#{fceTurismoController}", FceTurismoController.class);
			if(desabilitarTudo){
				fceTurismoController.setDesabilitarTudo();
				fceTurismoController.setDesabilitarDocumentoAdicional();
			}
			fceTurismoController.abrirFce(fce);
		}
		else if(FceUtil.isFceLicenciamentoAquicultura(documentoObrigatorio)) {
			FceLicenciamentoAquiculturaController aquiculturaController = (FceLicenciamentoAquiculturaController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoAquiculturaController}", FceLicenciamentoAquiculturaController.class);
			aquiculturaController.setRequerimento(fce.getIdeRequerimento());
			aquiculturaController.limpar();
			if(desabilitarTudo){
				aquiculturaController.setDesabilitarTudo();
			}
			if(fce.getIdeDadoOrigem().getIdeDadoOrigem().equals(DadoOrigemEnum.REQUERIMENTO.getId())){
				aquiculturaController.setEmDuplicacao(true);
			}
			else{
				aquiculturaController.setEmAnaliseTecnica(true);
			}
			aquiculturaController.abrirFceTecnico();
			aquiculturaController.abrirDialog();
		}
		else if(isFceLicenciamentoMineracao(documentoObrigatorio)){
			FceLicenciamentoMineracaoController fceLicMineracaoController = (FceLicenciamentoMineracaoController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoMineracaoController}", FceLicenciamentoMineracaoController.class);
			if(desabilitarTudo){
				fceLicMineracaoController.setDesabilitarTudo();
			}
			fceLicMineracaoController.abrirFce(fce);
		}
		else if(isFceAutorizacaoMineracao(documentoObrigatorio)){
			FceAutorizacaoMineracaoController fceAutMineracaoController = (FceAutorizacaoMineracaoController) SessaoUtil.recuperarManagedBean("#{fceAutorizacaoMineracaoController}", FceAutorizacaoMineracaoController.class);
			if(desabilitarTudo){
				fceAutMineracaoController.setDesabilitarTudo();
			}else {
				fceAutMineracaoController.setDesabilitarTudo(false);
			}
			fceAutMineracaoController.abrirFce(fce);
		}
		else if(isFceInfraestrutura(documentoObrigatorio)){
			FceInfraestruturaController fceInfraestruturaController = (FceInfraestruturaController) 
					SessaoUtil.recuperarManagedBean("#{fceInfraestruturaController}", FceInfraestruturaController.class);
			
			if(desabilitarTudo){
				fceInfraestruturaController.setDesabilitarTudo();
			}
			
			fceInfraestruturaController.abrirFce(fce);
		}
		else if(isFceCanais(documentoObrigatorio)){
			FceCanalController fceCanalController = (FceCanalController) SessaoUtil.recuperarManagedBean("#{fceCanalController}", FceCanalController.class);
			fceCanalController.limpar();
			if(desabilitarTudo){
				fceCanalController.setDesabilitarTudo();
			}
			fceCanalController.abrirFce(fce);
		}
		else if(isFceSistemaAbastecimentoAgua(documentoObrigatorio)){
			FceSaaController fceSaaController = (FceSaaController) SessaoUtil.recuperarManagedBean("#{fceSaaController}", FceSaaController.class);
			if(desabilitarTudo){
				fceSaaController.setDesabilitarTudo();
			}
			fceSaaController.abrirFce(fce);
		}
		else if(isFceSES(documentoObrigatorio)){
			FceSesController fceSesController = (FceSesController) SessaoUtil.recuperarManagedBean("#{fceSesController}", FceSesController.class);
			if(desabilitarTudo){
				fceSesController.setDesabilitarTudo();
			}
			fceSesController.abrirFce(fce);
		}
		else if(isFceLinhaEnergiaTrasmissaoEnergia(fce.getIdeDocumentoObrigatorio())){
			FceLinhaTransmissaoDistribuicaoController fceLinTransmissaoDistribruicaoController = (FceLinhaTransmissaoDistribuicaoController) SessaoUtil.recuperarManagedBean("#{fceLinhaTransmissaoDistribruicaoController}", FceLinhaTransmissaoDistribuicaoController.class);
			if(desabilitarTudo){
				fceLinTransmissaoDistribruicaoController.setDesabilitarTudo();
			}else{
				fceLinTransmissaoDistribruicaoController.setDesabilitado(false);
			}
			fceLinTransmissaoDistribruicaoController.abrirFce(fce);
		}
		else if(isFceGeracaoEnergia(fce.getIdeDocumentoObrigatorio())){
			FceGeracaoEnergiaController fceGeracaoEnergiaController = (FceGeracaoEnergiaController) SessaoUtil.recuperarManagedBean("#{fceGeracaoEnergiaController}", FceGeracaoEnergiaController.class);
			if(desabilitarTudo){
				fceGeracaoEnergiaController.setDesabilitarTudo();
			}
			fceGeracaoEnergiaController.abrirFce(fce);
		}
		else if(isFceIntervencaoMineracao(documentoObrigatorio)){
			FceIntervencaoMineracaoController fceIntervencaoMineracaoController = (FceIntervencaoMineracaoController) SessaoUtil.recuperarManagedBean("#{fceIntervencaoMineracaoController}", FceIntervencaoMineracaoController.class);
			fceIntervencaoMineracaoController.setDesabilitarTudo(desabilitarTudo);
			fceIntervencaoMineracaoController.abrirFce(fce);
		}
		else if (isFceFlorestal(documentoObrigatorio)) {
			FceFlorestalController fceFlorestalController = (FceFlorestalController) SessaoUtil.recuperarManagedBean("#{fceFlorestalController}", FceFlorestalController.class);
			if(Util.isNull(dadoConcedidoFceImpl)) {
				fceFlorestalController.init(fce);
			}
			else{
				fceFlorestalController.init(dadoConcedidoFceImpl);
			}
			FceFlorestalAbstractController fceFlorestalAbstractController = fceFlorestalController.getImpl();
			fceFlorestalAbstractController.setDesabilitarTudo(desabilitarTudo);
			fceFlorestalAbstractController.abrirFce(fce);
		}
	}
	

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2015
	 */
	private static Fce duplicarFceAnaliseTecnica(Fce fce, FceController fceController, AnaliseTecnica analiseTecnica) {
		fceController.duplicarFceAnaliseTecnica(fce, analiseTecnica);
		return fceController.getFce();
	}

	public static String retornarJasperFce(DocumentoObrigatorio documentoObrigatorio, FceFlorestalAbstractController ... args) {
		if(isFceAgrossilvoPastoril(documentoObrigatorio)) {
			return "fce_agrossilvopastoril.jasper";
		}
		else if(isFceAsv(documentoObrigatorio)) {
			return "fce_asv.jasper";
		}
		else if(isFceBarragem(documentoObrigatorio)) {
			if (documentoObrigatorio.getIdeDocumentoObrigatorio().equals(FCE_INTERVENCAO_BARRAGEM.getId())) {
				return "fce_intervencao_barragem.jasper";
			}else{
				return "fce_barragem.jasper";
			}
		}
		else if(isFceOutorgaAbastecimentoIndustrial(documentoObrigatorio)) {
			return "fce_abastecimento_industrial.jasper";
		}
		else if(isFceOutorgaCaptacaoSubterranea(documentoObrigatorio)) {
			return "fce_captacao_subterranea.jasper";
		}
		else if(isFceOutorgaCaptacaoSuperficial(documentoObrigatorio)) {
			return "fce_captacao_superficial.jasper";
		}
		else if(isFceOutorgaDessedentacaoAnimal(documentoObrigatorio)) {
			return "fce_dessedentacao_animal.jasper";
		}
		else if(isFceOutorgaIrrigacao(documentoObrigatorio)) {
			return "fce_irrigacao.jasper";
		}
		else if(isFceOutorgaLancamentoEfluentes(documentoObrigatorio)) {
			return "fce_lancamento_efluentes.jasper";
		}
		else if(isFceOutorgaPulverizacao(documentoObrigatorio)) {
			return "fce_pulverizacao.jasper";
		}
		else if(isFceIndustria(documentoObrigatorio)) {
			return "fce_industria.jasper";
		}
		else if(isFcePerfuracaoPoco(documentoObrigatorio)) {
			return "fce_loc_geo_perfuracao_poco.jasper";
		}
		else if(isFceAbastecimentoHumano(documentoObrigatorio)) {
			return "fce_abastecimento_humano.jasper";
		}
		else if(isFceOutorgaAquicultura(documentoObrigatorio)) {
			return "fce_outorga_aquicultura.jasper";
		}
		else if(isFceTurismo(documentoObrigatorio)) {
			return "fce_turismo.jasper";
		}
		else if(isFceLicenciamentoAquicultura(documentoObrigatorio)) {
			return "fce_licenca_aquicultura.jasper";
		}
		else if(isFceLicenciamentoMineracao(documentoObrigatorio)){
			return "fce_licenciamento_mineracao.jasper";
		}
		else if(isFceGeracaoEnergia(documentoObrigatorio)){
			return "fce_geracao_energia.jasper";
		}
		else if(isFceAutorizacaoMineracao(documentoObrigatorio)){
			return "fce_autorizacao_mineracao.jasper";
		}else if(isFceSistemaAbastecimentoAgua(documentoObrigatorio)){
			return "fce_sistema_abastecimento_agua.jasper";
		}else if(isFceSES(documentoObrigatorio)){
			return "fce_sistema_esgotamento_sanitario.jasper";
		}
		else if(isFceLinhaEnergiaTrasmissaoEnergia(documentoObrigatorio)){
			return "fce_linha_energia.jasper";
		}
		else if(isFceIntervencaoMineracao(documentoObrigatorio)){
			return "fce_intervencao_mineracao.jasper";
		}
		else if(isFceInfraestrutura(documentoObrigatorio)){
			return "fce_infraestrutura.jasper";
		}
		else if(isFceCanais(documentoObrigatorio)){
			return "fce_canais.jasper";
		} 
		else if (isFceFlorestal(documentoObrigatorio)) {
			if (!Util.isNullOuVazio(args)) {
				FceFlorestalAbstractController fceFlorestalAbstractController = args[0];
				return fceFlorestalAbstractController.getNomeRelatorio();
			}
			throw new SeiaRuntimeException("O controller florestal não foi informado.");
		}
		throw new SeiaRuntimeException("Não há FCE para o documento obrigatório passado.");
	}


	public static boolean isFce(DocumentoObrigatorio documentoObrigatorio){
		return isFceAgrossilvoPastoril(documentoObrigatorio)
				|| isFceAsv(documentoObrigatorio)
				|| isFceBarragem(documentoObrigatorio)
				|| isFceOutorgaAbastecimentoIndustrial(documentoObrigatorio)
				|| isFceOutorgaCaptacaoSubterranea(documentoObrigatorio)
				|| isFceOutorgaCaptacaoSuperficial(documentoObrigatorio)
				|| isFceOutorgaDessedentacaoAnimal(documentoObrigatorio)
				|| isFceOutorgaIrrigacao(documentoObrigatorio)
				|| isFceOutorgaLancamentoEfluentes(documentoObrigatorio)
				|| isFceOutorgaPulverizacao(documentoObrigatorio)
				|| isFceIndustria(documentoObrigatorio)
				|| isFcePerfuracaoPoco(documentoObrigatorio)
				|| isFceAbastecimentoHumano(documentoObrigatorio)
				|| isFceOutorgaAquicultura(documentoObrigatorio)
				|| isFceTurismo(documentoObrigatorio)
				|| isFceLicenciamentoAquicultura(documentoObrigatorio)
				|| isFceLicenciamentoMineracao(documentoObrigatorio) 
				|| isFceAutorizacaoMineracao(documentoObrigatorio)
				|| isFceInfraestrutura(documentoObrigatorio)
				|| isFceCanais(documentoObrigatorio)
				|| isFceSistemaAbastecimentoAgua(documentoObrigatorio)
				|| isFceSES(documentoObrigatorio)
				|| isFceLinhaEnergiaTrasmissaoEnergia(documentoObrigatorio)
				|| isFceGeracaoEnergia(documentoObrigatorio)
				|| isFceIntervencaoMineracao(documentoObrigatorio) 
				|| isFceFlorestal(documentoObrigatorio);
	}

	private static boolean compararDocumento(DocumentoObrigatorio documentoObrigatorio, DocumentoObrigatorioEnum docEnum) {
		if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
			return (documentoObrigatorio.getIdeDocumentoObrigatorio().equals(docEnum.getId()));
		}
		return false;
	}

	public static boolean isFceAsv(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_ASV);
	}

	public static boolean isFceBarragem(DocumentoObrigatorio documentoObrigatorio) {
		return isFceBarragemComPonto(documentoObrigatorio) || isFceBarragemSemPonto(documentoObrigatorio) || isFceConstrucaoBarragem(documentoObrigatorio);
	}

	public static boolean isFceBarragemSemPonto(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_INTERVENCAO_BARRAGEM_SEM_PONTO);
	}

	public static boolean isFceConstrucaoBarragem(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_CONSTRUCAO_BARRAGEM);
	}

	public static boolean isFceBarragemComPonto(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_INTERVENCAO_BARRAGEM);
	}

	public static boolean isFceOutorgaAbastecimentoIndustrial(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_ABASTECIMENTO_INDUSTRIAL);
	}

	public static boolean isFceOutorgaLancamentoEfluentes(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_LANCAMENTO_EFLUENTES);
	}

	public static boolean isFceIndustria(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_INDUSTRIA);
	}

	public static boolean isFcePerfuracaoPoco(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_LOCALIZACAO_GEOGRAFICA);
	}

	public static boolean isFceAbastecimentoHumano(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_OUTORGA_INTERVENCAO_ABASTECIMENTO_HUMANO);
	}

	public static boolean isFceOutorgaAquicultura(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_OUTORGA_AQUICULTURA);
	}

	public static boolean isFceLicenciamentoAquicultura(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_LICENCIAMENTO_AQUICULTURA);
	}

	public static boolean isFceAutorizacaoMineracao(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio, FCE_AUTORIZACAO_MINERACAO);
	}

	public static boolean isFceLicenciamentoMineracao(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio, FCE_LICENCIAMENTO_MINERACAO);
	}
	
	public static boolean isFceSistemaAbastecimentoAgua(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio, FCE_SISTEMA_ABASTECIMENTO_AGUA);
	}
	
	public static boolean isFceSES(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio, FCE_CARACTERIZACAO_SES);
	}

	public static boolean isFceTurismo(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_TURISMO);
	}

	public static boolean isFceOutorgaCaptacaoSubterranea(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_OUTORGA_CAPTACAO_SUBTERRANEA);
	}

	public static boolean isFceAgrossilvoPastoril(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,AGROSSILVOPASTORIL);
	}

	public static boolean isFceOutorgaCaptacaoSuperficial(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_OUTORGA_CAPTACAO_SUPERFICIAL);
	}
	
	public static boolean isFceInfraestrutura(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio, FCE_INFRAESTRUTURA);
	}

	public static boolean isFceOutorgaDessedentacaoAnimal(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_DESSEDENTACAO_ANIMAL);
	}

	public static boolean isFceOutorgaIrrigacao(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_IRRIGACAO);
	}

	public static boolean isFceOutorgaPulverizacao(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,FCE_PULVERIZACAO);
	}

	public static boolean isFceGeracaoEnergia(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio, FCE_GERACAO_ENERGIA);
	}
	
	public static boolean isFceLinhaEnergiaTrasmissaoEnergia(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,DocumentoObrigatorioEnum.FCE_LINHA_DE_TRASMISSAO_DISTRIBUICAO_ENERGIA);
	}
	public static boolean isFceIntervencaoMineracao(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,DocumentoObrigatorioEnum.FCE_INTERVENCAO_MINERACAO);
	}
	
	public static boolean isFceCanais(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,DocumentoObrigatorioEnum.FCE_CANAIS);
	}
	
	public static boolean isFceEPMF(DocumentoObrigatorio documentoObrigatorio) {
		return compararDocumento(documentoObrigatorio,DocumentoObrigatorioEnum.FCE_EPMF);
}
	
	public static boolean isFceFlorestal(DocumentoObrigatorio documentoObrigatorio) {
		if(isFceEPMF(documentoObrigatorio)){
			return true;
		}
		return false;
	}
	
	public static boolean isExibirParaAnaliseTecnica(Fce fce) {
		return !Util.isNull(fce) && fce.isFceTecnico();
	}

}