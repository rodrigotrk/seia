package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceOutorgaSemDocumentoAdicionalController;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceDessedentacaoAnimal;
import br.gov.ba.seia.entity.FceDessedentacaoAnimalAtividade;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.FceDessedentacaoAnimalAtividadeService;
import br.gov.ba.seia.service.FceDessedentacaoAnimalService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.TipoPeriodoDerivacaoService;
import br.gov.ba.seia.service.TipologiaAtividadeService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("fceDessedentacaoAnimalController")
@ViewScoped
public class FceDessedentacaoAnimalController extends BaseFceOutorgaSemDocumentoAdicionalController implements FceNavegacaoInterface {

	@EJB
	private FceDessedentacaoAnimalService fceDessedentacaoAnimalService;
	@EJB
	private FceDessedentacaoAnimalAtividadeService fceDessedentacaoAnimalAtividadeService;
	@EJB
	private TipoPeriodoDerivacaoService tipoPeriodoDerivacaoService;
	@EJB
	private TipologiaAtividadeService tipologiaAtividadeService;
	@EJB
	private FceOutorgaLocalizacaoGeograficaService fceOutorgaLocGeoService;

	private static final TipologiaAtividade TIPOLOGIA_ATIVIDADE_OUTROS = new TipologiaAtividade("Outros");

	private int activeTab;

	private FceDessedentacaoAnimal fceDessedentacaoAnimal;

	private List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao;

	private FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionada;

	private List<TipologiaAtividade> listaTipologiaAtividade;
	private List<TipologiaAtividade> listaTipologiaAtividadeAll;
	private TipologiaAtividade tipologiaAtividade;
	private List<FceDessedentacaoAnimalAtividade> listaFceDessedentacaoAnimalAtividadeExcluidos;
	private List<FceDessedentacaoAnimalAtividade> listaFceDessedentacaoAnimalAtividadeSelecionada;
	private FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividade;

	private Integer totalNumeroCabecas;
	private BigDecimal totalConsumoDiarioEspecies;
	private BigDecimal vazaoSugerida;
	private String nomeEspecie;

	@Override
	public void init(){
		carregarAba();
		verificarEdicao();
		if(!isFceSalvo()){
			iniciarFce(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_DESSEDENTACAO_ANIMAL.getId()));
			fceDessedentacaoAnimal = new FceDessedentacaoAnimal(super.getFce());
			prepararListaFceOutorgaLocalizacaoGeografica();
		}
		else {
			carregarFceDessedentacaoAnimal();
			listarFceDessedentacaoAnimalAtividade();
			realizarCalculos();
			listarFceOutorgaLocalizacaoGeoGeografica();
			removerTipologiaContidaNaListaDeEspecieSelecionada();
		}
	}

	@Override
	public void carregarAba() {
		carregarAbaDadosRequerimento();
		carregarAbaDessedentacaoAnimal();
	}

	/**
	 * Método que lista os FceOutorgaLocalizacaoGeografica daquele FCE, tratando os pontos geográficos e setando os booleanos que desabilitam o campo Rio
	 * da aba Dados do Requerimento
	 * @return List<FceOutorgaLocalizacaoGeografica>
	 * @author marcelo.deus
	 * @author eduardo.fernandes
	 */
	private void listarFceOutorgaLocalizacaoGeoGeografica(){
		try {
			super.listaFceOutorgaLocalizacaoGeograficaCapSup = fceOutorgaLocGeoService.listarFceOutorgaLocTipoCaptacaoGeoByIdeFce(super.fce);
			if(!Util.isNull(super.listaFceOutorgaLocalizacaoGeograficaCapSup)){
				if(isListaOutorgaLocGeoMaiorQueFceLocGeo()){
					prepararListaFceOutorgaLocalizacaoGeografica();
				}
				for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
					for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaCaptacaoSuperficial){
						if(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
							fceOutorgaLocalizacaoGeografica.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
						}
					}
				}
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " os dados do requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que verifica se a quantidade de objetos na lista listaOutorgaLocalizacaoGeografica é maior que o da lista listaFceOutorgaLocalizacaoGeografica
	 * @return boolean
	 * @author marcelo.deus
	 */
	private boolean isListaOutorgaLocGeoMaiorQueFceLocGeo(){
		return super.listaCaptacaoSuperficial.size() > super.listaFceOutorgaLocalizacaoGeograficaCapSup.size();
	}

	/**
	 * Método que verifica se já existe um FCE para aquele requerimento
	 * @param requerimento
	 * @param DOC_OBRIGATORIO_ABS_INDUSTRIAL
	 * @author marcelo.deus
	 *
	 */
	@Override
	public void verificarEdicao(){
		carregarFceDoRequerente(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_DESSEDENTACAO_ANIMAL.getId()));
	}

	/**
	 * Método que carrega a aba Dados do Requerimento do FCE
	 * @author marcelo.deus
	 */
	private void carregarAbaDadosRequerimento(){
		super.carregarListaOutorgaLocGeoByTipoFinalidadeUsoAgua(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL.getId()));
		super.separarCaptacoes();
	}

	/**
	 * Método para criar as {@link FceOutorgaLocalizacaoGeografica}
	 * @param outorgaLocalizacaoGeografica
	 * @author marcelo.deus
	 * @author eduardo.fernandes
	 */
	private void prepararListaFceOutorgaLocalizacaoGeografica() {
		if(!Util.isNullOuVazio(super.listaCaptacaoSuperficial)){
			super.listaFceOutorgaLocalizacaoGeograficaCapSup = new ArrayList<FceOutorgaLocalizacaoGeografica>();
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaCaptacaoSuperficial){
				if(!super.listaFceOutorgaLocalizacaoGeograficaCapSup.contains(outorgaLocalizacaoGeografica)){
					super.listaFceOutorgaLocalizacaoGeograficaCapSup.add(new FceOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica));
				}
			}
		}
	}

	/**
	 * Método que carrega os elementos da aba Dessedentação Animal
	 * @author marcelo.deus
	 */
	private void carregarAbaDessedentacaoAnimal(){
		carregarTipoPeriodoDerivacao();
		carregarListaTipologia();
	}

	/**
	 * Método que busca um FCE existente no banco caso seja Edição
	 * @param fce
	 * @author marcelo.deus
	 *
	 */
	private void carregarFceDessedentacaoAnimal(){
		try {
			fceDessedentacaoAnimal = fceDessedentacaoAnimalService.buscarFceDesAnimalByIdeFce(super.fce);
			
			if (Util.isNullOuVazio(fceDessedentacaoAnimal)){
				fceDessedentacaoAnimal = new FceDessedentacaoAnimal(super.getFce());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista da tabela associativa FceDessedentacaoAnimalAtividade
	 * @param FceDessedentacaoAnimal
	 * @author marcelo.deus
	 */
	private void listarFceDessedentacaoAnimalAtividade(){
		try {
			if (!Util.isNullOuVazio(fceDessedentacaoAnimal.getIdeFceDessedentacaoAnimal())){
				listaFceDessedentacaoAnimalAtividadeSelecionada = fceDessedentacaoAnimalAtividadeService.buscarFceDessedentacaoAnimalAtividadeByIdeFceDesAnimal(fceDessedentacaoAnimal);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		if(!Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			for (FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividadeObj : listaFceDessedentacaoAnimalAtividadeSelecionada){
				fceDessedentacaoAnimalAtividadeObj.setConfirmado(true);
			}
			
			if(!Util.isNullOuVazio(fceDessedentacaoAnimal) && fceDessedentacaoAnimal.getIndOutros()){
				listaFceDessedentacaoAnimalAtividadeSelecionada.add(new FceDessedentacaoAnimalAtividade(TIPOLOGIA_ATIVIDADE_OUTROS));
			}
		}
	}

	/**
	 * Método que busca a lista de TipoPeriodoDerivacao
	 * @author marcelo.deus
	 */
	private void carregarTipoPeriodoDerivacao(){
		try {
			listaTipoPeriodoDerivacao = tipoPeriodoDerivacaoService.listarTipoPeriodoDerivacao();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipo Período Derivação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de TipologiaAtividade
	 * @param TipologiaEnum
	 * @author marcelo.deus
	 */
	private void carregarListaTipologia(){
		try {
			listaTipologiaAtividade = tipologiaAtividadeService.buscarTipologiaAtividadeByIdeTipologia(new Tipologia(TipologiaEnum.CRIACAO_DE_ANIMAIS.getId()));
			listaTipologiaAtividade.add(TIPOLOGIA_ATIVIDADE_OUTROS);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Espécies.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		if(!Util.isNullOuVazio(listaTipologiaAtividade) && Util.isNullOuVazio(listaTipologiaAtividadeAll)){
			listaTipologiaAtividadeAll = new ArrayList<TipologiaAtividade>();
			listaTipologiaAtividadeAll.addAll(listaTipologiaAtividade);
		}
	}

	/**
	 * Método que atualiza a lista de TipologiaAtividade
	 * @author marcelo.deus
	 */
	private void atualizarListaTipologia(){
		listaTipologiaAtividade.clear();
		listaTipologiaAtividade.addAll(listaTipologiaAtividadeAll);
		removerTipologiaContidaNaListaDeEspecieSelecionada();
	}

	/**
	 * Método que salva o FceLocalizacaoGeografica quando o Rio for confirmado na aba Dados do Requerimento
	 * @author marcelo.deus
	 */
	public void confirmarNomePopularRio(){
		if(validarNomePopularRio()){
			salvarFceLocalizacaoGeografica();
		}
	}

	/**
	 * Método que valida o campo Rio na aba Dados do Requerimento
	 * @return boolean
	 * @author marcelo.deus
	 */
	public boolean validarNomePopularRio(){
		if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada.getNomRio())){
			fceOutorgaLocalizacaoGeograficaSelecionada.setConfirmado(true);
			if(fceOutorgaLocalizacaoGeograficaSelecionada.isRioPreenchido()){
				super.exibirMensagem002();
			} 
			else {
				super.exibirMensagem001();
			}
			return true;
		}
		JsfUtil.addErrorMessage("O Rio " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		return false;
	}

	/**
	 * Método chamado quando o botão Editar for clicado para o campo Rio na aba Dados do Requerimento
	 * @author marcelo.deus
	 */
	public void editarNomePopularRio(){
		fceOutorgaLocalizacaoGeograficaSelecionada.setConfirmado(false);
		fceOutorgaLocalizacaoGeograficaSelecionada.setRioPreenchido(true);
	}

	/**
	 * Método que valida os campos de um objeto fceDessedentacaoAnimalAtividade na tabela listaFceDessedentacaoAnimalAtividadeSelecionada
	 * @return boolean
	 * @author marcelo.deus
	 */
	public boolean validarEspecieNaLista(){
		boolean valido=true;
		if(Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getNumCabecas())){
			JsfUtil.addErrorMessage("O número de cabeças " + Util.getString("msg_generica_null_ou_vazio"));
			valido=false;
		}

		if(Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getNumConsumoDiarioCabeca())){
			JsfUtil.addErrorMessage("O campo consumo diário por cabeça " + Util.getString("msg_generica_null_ou_vazio"));
			valido=false;
		}

		if(Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getNumConsumoDiarioEspecie())){
			JsfUtil.addErrorMessage("O campo consumo diário por espécie " + Util.getString("msg_generica_null_ou_vazio"));
			valido=false;
		}
		return valido;
	}

	/**
	 * Método que valida o FCE Dessedentação Animal
	 * @return boolean
	 * @author marcelo.deus
	 */
	@Override
	public boolean validarAba(){
		boolean valido=true;
		boolean especieConfirmada = true;

		if(isExtensivoNaoMarcado() && isIntensivoNaoMarcado() && isSemiIntensivoNaoMarcado()){
			JsfUtil.addErrorMessage("O sistema de produção "+ Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido=false;
		}
		if(Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			JsfUtil.addErrorMessage(Util.getString("fce_dessedentacao_animal_especie_obrigatoria"));
			valido=false;
		} else{
			especieConfirmada = verificarConfirmado();
		}

		if(Util.isNullOuVazio(fceDessedentacaoAnimal.getIdeTipoPeriodoDerivacao())){
			JsfUtil.addErrorMessage("O período de derivação "+ Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido=false;
		} else if(fceDessedentacaoAnimal.getIdeTipoPeriodoDerivacao().equals(new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId()))){
			if(Util.isNullOuVazio(fceDessedentacaoAnimal.getNumTempoCaptacao())){
				JsfUtil.addErrorMessage("O tempo de captação "+ Util.getString("msg_generica_null_ou_vazio"));
				valido=false;
			} else if (fceDessedentacaoAnimal.getNumTempoCaptacao() > 24){
				JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_tempo_captacao_info_0034"));
				valido=false;
			}
		}

		if(!especieConfirmada){
			valido = especieConfirmada;
		}
		return valido;
	}

	/**
	 * Método que verifica se todos os objetos da lista listaFceDessedentacaoAnimalAtividadeSelecionada estão confirmados
	 * @return boolan
	 * @author marcelo.deus
	 */
	private boolean verificarConfirmado(){
		boolean valido = true;
		for(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividadeObj : listaFceDessedentacaoAnimalAtividadeSelecionada){
			if(!fceDessedentacaoAnimalAtividadeObj.isConfirmado() && !fceDessedentacaoAnimalAtividadeObj.getIdeTipologiaAtividade().getDscTipologiaAtividade().equals(TIPOLOGIA_ATIVIDADE_OUTROS.getDscTipologiaAtividade())){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040")+ " a Espécie.");
				valido = false;
				break;
			}
		}
		return valido;
	}

	/**
	 * Método que verifica se o campo Rio da aba Dados do Requerimento foi confirmado
	 * @return boolean
	 * @author marcelo.deus
	 */
	private boolean verificaRioConfirmado(){
		boolean valido = true;
		if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSup)){
			for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
				if(!fceOutorgaLocalizacaoGeografica.isConfirmado()){
					JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040")+ " o Rio.");
					valido = false;
					setActiveTab(0);
					break;
				}
			}
		}
		return valido;
	}

	/**
	 * Método chamado quando o botão Editar for clicado para os objetos da lista listaFceDessedentacaoAnimalAtividadeSelecionada
	 * @author marcelo.deus
	 */
	public void editarEspecieNaLista(){
		fceDessedentacaoAnimalAtividade.setConfirmado(false);
		fceDessedentacaoAnimalAtividade.setEdicao(true);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 10/04/2015
	 */
	private void realizarCalculos() {
		calculaTotalCabecas();
		calculaTotalConsumoPorEspecie();
	}

	/**
	 * Método que calcula o campo footer do número total de cabeças da lista listaFceDessedentacaoAnimalAtividadeSelecionada caso todos os objetos estejam confirmados
	 * @author marcelo.deus
	 */
	public void calculaTotalCabecas(){
		boolean todosConfirmados = true;
		totalNumeroCabecas = 0;
		if(Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			totalNumeroCabecas = null;
		}else{
			for(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividadeObj : listaFceDessedentacaoAnimalAtividadeSelecionada){
				if(!fceDessedentacaoAnimalAtividadeObj.isConfirmado()){
					todosConfirmados = false;
					totalNumeroCabecas = null;
					break;
				}
			}
			if(todosConfirmados){
				for (FceDessedentacaoAnimalAtividade fceAnimalAtividade : listaFceDessedentacaoAnimalAtividadeSelecionada){
					totalNumeroCabecas += fceAnimalAtividade.getNumCabecas();
				}
			}
		}
		fceDessedentacaoAnimal.setNumTotalCabecas(totalNumeroCabecas);
	}

	/**
	 * Método que calcula o campo footer do número total de consumo por espécie da lista listaFceDessedentacaoAnimalAtividadeSelecionada caso todos os objetos estejam confirmados
	 * @author marcelo.deus
	 */
	public void calculaTotalConsumoPorEspecie(){
		boolean todosConfirmados = true;
		totalConsumoDiarioEspecies = new BigDecimal(0);
		if(Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			totalConsumoDiarioEspecies = null;
		}else{
			for(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividadeObj : listaFceDessedentacaoAnimalAtividadeSelecionada){
				if(!fceDessedentacaoAnimalAtividadeObj.isConfirmado()){
					todosConfirmados = false;

					break;
				}
			}
			if(todosConfirmados){
				for (FceDessedentacaoAnimalAtividade fceAnimalAtividade : listaFceDessedentacaoAnimalAtividadeSelecionada){
					totalConsumoDiarioEspecies = totalConsumoDiarioEspecies.add(fceAnimalAtividade.getNumConsumoDiarioEspecie());
				}
			}
		}
		fceDessedentacaoAnimal.setNumTotalConsumoEspecie(totalConsumoDiarioEspecies);
		this.vazaoSugerida = BigDecimal.ZERO;
		if (!Util.isNullOuVazio(fceDessedentacaoAnimal.getNumTotalConsumoEspecie())){
			this.vazaoSugerida=transformaTotalConsumoEspecieEmMCubico(fceDessedentacaoAnimal.getNumTotalConsumoEspecie().setScale(3)).setScale(3);
		}
		
		fceDessedentacaoAnimal.setNumVolumeDerivar(this.vazaoSugerida);
	}

	/**
	 * Método que calcula o valor do consumo por espécie do objeto fceDessedentacaoAnimalAtividade
	 * @param fceDessedentacaoAnimalAtividade
	 * @author marcelo.deus
	 */
	public void calculaConsumoPorEspecie (FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividade){
		if(!Util.isNullOuVazio(fceDessedentacaoAnimalAtividade)){
			if(!Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getNumCabecas()) && !Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getNumConsumoDiarioCabeca())){
				BigDecimal numCabecas = new BigDecimal(fceDessedentacaoAnimalAtividade.getNumCabecas());
				fceDessedentacaoAnimalAtividade.setNumConsumoDiarioEspecie(numCabecas.multiply(fceDessedentacaoAnimalAtividade.getNumConsumoDiarioCabeca()));
			}
			if(Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getNumCabecas()) || Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getNumConsumoDiarioCabeca())){
				fceDessedentacaoAnimalAtividade.setNumConsumoDiarioEspecie(null);
			}
		}
	}

	/**
	 * Método que faz a conversão do total de consumo por espécie para metro cúbico, arredondando em três casas decimais
	 * @param totalConsumoEspecie
	 * @return BigDecimal
	 * @author marcelo.deus
	 */
	public BigDecimal transformaTotalConsumoEspecieEmMCubico(BigDecimal totalConsumoEspecie){
		BigDecimal mil = new BigDecimal(1000);
		return totalConsumoEspecie.divide(mil, RoundingMode.HALF_UP).setScale(3);
	}

	/**
	 * Método utilizado para fazer a pesquisa do nome da espécie na lista de TipologiaAtividade
	 * @author marcelo.deus
	 */
	public void pesquisarEspecie() {
		atualizarListaTipologia();
		List<TipologiaAtividade> listaTemp = new ArrayList<TipologiaAtividade>();
		listaTemp.addAll(listaTipologiaAtividade);
		listaTipologiaAtividade.clear();
		for(TipologiaAtividade temp : listaTemp){
			if(temp.getDscTipologiaAtividade().toLowerCase().indexOf(nomeEspecie.toLowerCase()) != -1){
				listaTipologiaAtividade.add(temp);
			}
		}
		if(Util.isNullOuVazio(listaTipologiaAtividade) && !isExisteOutros()){
			listaTipologiaAtividade.add(TIPOLOGIA_ATIVIDADE_OUTROS);
		}
	}

	/**
	 * Método que confirma o objeto fceDessedentacaoAnimalAtividade caso esteja dentro das regras estabelecidas
	 * @author marcelo.deus
	 */
	public void confirmarEspecieNaLista(){
		if(validarEspecieNaLista()){
			fceDessedentacaoAnimalAtividade.setConfirmado(true);
			realizarCalculos();
			if(fceDessedentacaoAnimalAtividade.isEdicao()){
				super.exibirMensagem002();
			}
			tipologiaAtividade = new TipologiaAtividade();
			fceDessedentacaoAnimalAtividade = new FceDessedentacaoAnimalAtividade();
		}
	}

	/**
	 * Método que exclui um objeto fceDessedentacaoAnimalAtividade da lista listaFceDessedentacaoAnimalAtividadeSelecionada
	 * @author marcelo.deus
	 */
	public void excluirEspecieNaListaSelecionada(){
		if(!Util.isNullOuVazio(fceDessedentacaoAnimalAtividade.getIdeFceDessedentacaoAnimalAtividade())){
			if(Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeExcluidos)){
				listaFceDessedentacaoAnimalAtividadeExcluidos = new ArrayList<FceDessedentacaoAnimalAtividade>();
			}
			listaFceDessedentacaoAnimalAtividadeExcluidos.add(fceDessedentacaoAnimalAtividade);
		}
		listaFceDessedentacaoAnimalAtividadeSelecionada.remove(fceDessedentacaoAnimalAtividade);
		nomeEspecie = null;
		atualizarListaTipologia();
		super.exibirMensagem005();
		realizarCalculos();
		fceDessedentacaoAnimalAtividade = new FceDessedentacaoAnimalAtividade();
	}

	/**
	 * Método que adiciona uma espécia na tabela listaFceDessedentacaoAnimalAtividadeSelecionada
	 * @author marcelo.deus
	 */
	public void adicionarEspecieNaLista(){
		if(Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			listaFceDessedentacaoAnimalAtividadeSelecionada = new ArrayList<FceDessedentacaoAnimalAtividade>();
		}
		fceDessedentacaoAnimalAtividade = new FceDessedentacaoAnimalAtividade(fceDessedentacaoAnimal, tipologiaAtividade);
		if(tipologiaAtividade.getDscTipologiaAtividade().equals(TIPOLOGIA_ATIVIDADE_OUTROS.getDscTipologiaAtividade())){
			zerarFootersDaTabelaEspecie();
			JsfUtil.addWarnMessage(Util.getString("fce_pulverizacao_outros"));
		}
		listaFceDessedentacaoAnimalAtividadeSelecionada.add(fceDessedentacaoAnimalAtividade);
		zerarFootersDaTabelaEspecie();
		nomeEspecie = null;
		tipologiaAtividade = new TipologiaAtividade();
		atualizarListaTipologia();
	}

	/**
	 * Método que seta como nulo os valores dos footers da tabela de espécies selecionadas
	 * @author marcelo.deus
	 */
	public void zerarFootersDaTabelaEspecie(){
		fceDessedentacaoAnimal.setNumTotalCabecas(null);
		fceDessedentacaoAnimal.setNumTotalConsumoEspecie(null);
	}

	/**
	 * Método que salva o FCE Dessedentação Animal no banco
	 * @author marcelo.deus
	 */
	public void salvarFceDessedentacaoAnimal(){
		fceDessedentacaoAnimal.setNumTotalCabecas(totalNumeroCabecas);
		fceDessedentacaoAnimal.setNumTotalConsumoEspecie(totalConsumoDiarioEspecies);
		fceDessedentacaoAnimal.setIndOutros(isExisteOutros());
		if(Util.isNullOuVazio(fceDessedentacaoAnimal.getIndSistemaExtensivo())){
			fceDessedentacaoAnimal.setIndSistemaExtensivo(false);
		}
		if(Util.isNullOuVazio(fceDessedentacaoAnimal.getIndSistemaIntesivo())){
			fceDessedentacaoAnimal.setIndSistemaIntesivo(false);
		}
		if(Util.isNullOuVazio(fceDessedentacaoAnimal.getIndSistemaSemiIntensivo())){
			fceDessedentacaoAnimal.setIndSistemaSemiIntensivo(false);
		}

		if(isExisteOutros()){
			fceDessedentacaoAnimal.setNumVolumeDerivar(new BigDecimal(0));
		}

		try {
			fceDessedentacaoAnimalService.salvarFceDessedentacaoAnimal(fceDessedentacaoAnimal);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que salva a lista listaFceDessedentacaoAnimalAtividadeSelecionada na tabela associativa de FceDessedentacaoAnimalAtividade
	 * @author marcelo.deus
	 */
	public void salvarListaFceDessedentacaoAnimalAtividade(){
		if(isTemEspecieEscolhida()){
			for(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividadeObj : listaFceDessedentacaoAnimalAtividadeSelecionada){
				if(fceDessedentacaoAnimalAtividadeObj.getIdeTipologiaAtividade().getDscTipologiaAtividade().equals(TIPOLOGIA_ATIVIDADE_OUTROS.getDscTipologiaAtividade())){
					listaFceDessedentacaoAnimalAtividadeSelecionada.remove(fceDessedentacaoAnimalAtividadeObj);
					break;
				}
			}
			try {
				fceDessedentacaoAnimalAtividadeService.salvarListaFceDessedentacaoAnimalAtividade(listaFceDessedentacaoAnimalAtividadeSelecionada);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Método para salvar o FceOutorgaLocalizacaoGeografica do FCE Dessedentação Animal
	 * @param fceOutorgaLocalizacaoGeografica
	 * @author marcelo.deus
	 */
	private void salvarFceDessedentacaoAnimalLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		fceOutorgaLocGeoService.salvarFceOutorgaLocGeo(fceOutorgaLocalizacaoGeografica);
	}

	/**
	 * Método para salvar o FceOutorgaLocalizacaoGeografica do FCE Dessedentação Animal
	 * @author marcelo.deus
	 */
	private void salvarFceLocalizacaoGeografica(){
		try {
			if(!isFceSalvo()){
				salvarFce();
			}
			fceOutorgaLocalizacaoGeograficaSelecionada.setIdeFce(super.fce);
			salvarFceDessedentacaoAnimalLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaSelecionada);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " as informações da Dessedentação Animal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método chamado ao clicar no botão Finalizar do FCE Dessedentação Animal
	 * @author marcelo.deus
	 */
	@Override
	public void finalizar(){
		try{
			fceDessedentacaoAnimalService.finalizar(this);
		}catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Dessedentação Animal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		if(verificaRioConfirmado() && validarAba()){
			super.concluirFce();
			salvarFceDessedentacaoAnimal();
			if(!Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeExcluidos)){
				excluirFceDessedentacaoAnimalAtividadeAntigo();
				listaFceDessedentacaoAnimalAtividadeExcluidos.clear();
			}
			salvarListaFceDessedentacaoAnimalAtividade();
			if(isFceSalvo()){
				super.exibirMensagem002();
			} else {
				super.exibirMensagem001();
			}
			RequestContext.getCurrentInstance().execute("abs_animal.hide()");
			if(fceDessedentacaoAnimal.getIndOutros()){
				JsfUtil.addWarnMessage(Util.getString("fce_dessedentacao_animal_finalizar_outros"));
			} else{
				RequestContext.getCurrentInstance().execute("rel_fce_abs_animal.show()");
			}
			setActiveTab(0);
			limpar();
			
		}
	}

	/**
	 * Método auxiliar criado para remover os objetos antigos da lista listaFceDessedentacaoAnimalAtividadeSelecionada que foram carregados junto com o FCE no modo edição
	 * @author marcelo.deus
	 */
	public void excluirFceDessedentacaoAnimalAtividadeAntigo(){
		try {
			fceDessedentacaoAnimalAtividadeService.removerFceDessedentacaoAnimalAtividadeAntigo(listaFceDessedentacaoAnimalAtividadeExcluidos);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que remove as TipologiaAtividade da lista que estão contidas na listaFceDessedentacaoAnimalAtividadeSelecionada
	 * @author marcelo.deus
	 */
	public void removerTipologiaContidaNaListaDeEspecieSelecionada(){
		if(!Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			List<TipologiaAtividade> list = new ArrayList<TipologiaAtividade>();
			list.addAll(listaTipologiaAtividade);
			for(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividadeObj : listaFceDessedentacaoAnimalAtividadeSelecionada){
				if(list.contains(fceDessedentacaoAnimalAtividadeObj.getIdeTipologiaAtividade())){
					listaTipologiaAtividade.remove(fceDessedentacaoAnimalAtividadeObj.getIdeTipologiaAtividade());
				}
			}
		}
	}

	/**
	 * Método que avança a aba caso o campo Rio esteja confirmado
	 * @author marcelo.deus
	 */
	@Override
	public void avancarAba(){
		if(verificaRioConfirmado()){
			setActiveTab(1);
		}
	}

	/**
	 * Método que verifica se existe "Outros" na listaFceDessedentacaoAnimalAtividadeSelecionada
	 * @return boolean
	 * @author marcelo.deus
	 */
	public boolean isExisteOutros(){
		if(!Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			for (FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividadeObj : listaFceDessedentacaoAnimalAtividadeSelecionada){
				if(isTipologiaAtividadeOutros(fceDessedentacaoAnimalAtividadeObj.getIdeTipologiaAtividade())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Método que anula todas as variáveis presentes
	 * @author marcelo.deus
	 */
	@Override
	public void limpar(){
		if(!Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeExcluidos)){
			listaFceDessedentacaoAnimalAtividadeExcluidos.clear();
		}
		if(!Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada)){
			listaFceDessedentacaoAnimalAtividadeSelecionada.clear();
		}
		if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSup)){
			super.listaFceOutorgaLocalizacaoGeograficaCapSup.clear();
		}
		if(!Util.isNullOuVazio(listaTipologiaAtividade)){
			listaTipologiaAtividade.clear();
		}
		if(!Util.isNullOuVazio(listaTipologiaAtividadeAll)){
			listaTipologiaAtividadeAll.clear();
		}
		if(!Util.isNullOuVazio(listaTipoPeriodoDerivacao)){
			listaTipoPeriodoDerivacao.clear();
		}
		fceDessedentacaoAnimal = null;
		super.limparDadosOutorga();
		totalConsumoDiarioEspecies = null;
		totalNumeroCabecas = null;
		activeTab = 0;
	}

	//Métodos utilizados nos rendereds da tela
	public boolean isTipologiaAtividadeOutros(TipologiaAtividade tipologiaAtividade){
		return !Util.isNullOuVazio(tipologiaAtividade) && (tipologiaAtividade.getDscTipologiaAtividade().compareTo("Outros") == 0);
	}

	public boolean isTemEspecieEscolhida(){
		return !Util.isNullOuVazio(listaFceDessedentacaoAnimalAtividadeSelecionada);
	}

	public boolean isContemDerivacaoIntermitente(){
		return !Util.isNullOuVazio(fceDessedentacaoAnimal.getIdeTipoPeriodoDerivacao()) && (fceDessedentacaoAnimal.getIdeTipoPeriodoDerivacao()).equals(new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId()));
	}

	private boolean isIntensivoNaoMarcado(){
		return Util.isNullOuVazio(fceDessedentacaoAnimal.getIndSistemaIntesivo()) || !fceDessedentacaoAnimal.getIndSistemaIntesivo();
	}

	private boolean isExtensivoNaoMarcado(){
		return Util.isNullOuVazio(fceDessedentacaoAnimal.getIndSistemaExtensivo()) || !fceDessedentacaoAnimal.getIndSistemaExtensivo();
	}

	private boolean isSemiIntensivoNaoMarcado(){
		return Util.isNullOuVazio(fceDessedentacaoAnimal.getIndSistemaSemiIntensivo()) || !fceDessedentacaoAnimal.getIndSistemaSemiIntensivo();
	}

	public void changeDerivacao(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event.getOldValue())) {
			if(event.getOldValue().equals(new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId()))){
				fceDessedentacaoAnimal.setNumTempoCaptacao(null);
			}
		}
	}
	//Fim dos métodos rendereds da tela

	/**
	 * Método que seta a Aba ativa de acordo com o clique do usuário na tela
	 * @param activeTab
	 * @param event
	 * @author marcelo.deus
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if("dadosRequerimento".equals(event.getTab().getId())){
			setActiveTab(0);
		}
		else if("abastecimento".equals(event.getTab().getId())){
			setActiveTab(1);
		}
		else if("dadosAdicionais".equals(event.getTab().getId())){
			setActiveTab(2);
		}
	}

	/**
	 * Método que retorna uma aba através do clique do botão "Voltar"
	 * @param activeTab
	 * @author marcelo.deus
	 */
	@Override
	public void voltarAba(){
		activeTab--;
	}

	/**
	 * Método que imprime o relatório
	 * @param fce
	 * @param requerimento
	 * @param DocumentoObrigatorioEnum
	 * @param String
	 * @return StreamedContent
	 * @throws Exception
	 * @author marcelo.deus
	 */
	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_DESSEDENTACAO_ANIMAL.getId()));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Dessedentação Animal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	//GETS & SETS
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public BigDecimal getTotalConsumoDiarioEspecies() {
		return totalConsumoDiarioEspecies;
	}

	public void setTotalConsumoDiarioEspecies(BigDecimal totalConsumoDiarioEspecies) {
		this.totalConsumoDiarioEspecies = totalConsumoDiarioEspecies;
	}

	public String getNomeEspecie() {
		return nomeEspecie;
	}

	public void setNomeEspecie(String nomeEspecie) {
		this.nomeEspecie = nomeEspecie;
	}

	public Integer getTotalNumeroCabecas() {
		return totalNumeroCabecas;
	}

	public void setTotalNumeroCabecas(Integer totalNumeroCabecas) {
		this.totalNumeroCabecas = totalNumeroCabecas;
	}

	public FceDessedentacaoAnimal getFceDessedentacaoAnimal() {
		return fceDessedentacaoAnimal;
	}

	public void setFceDessedentacaoAnimal(FceDessedentacaoAnimal fceDessedentacaoAnimal) {
		this.fceDessedentacaoAnimal = fceDessedentacaoAnimal;
	}

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public void setListaTipoPeriodoDerivacao(List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao) {
		this.listaTipoPeriodoDerivacao = listaTipoPeriodoDerivacao;
	}

	public List<TipologiaAtividade> getListaTipologiaAtividade() {
		return listaTipologiaAtividade;
	}

	public void setListaTipologiaAtividade(List<TipologiaAtividade> listaTipologiaAtividade) {
		this.listaTipologiaAtividade = listaTipologiaAtividade;
	}

	public List<TipologiaAtividade> getListaTipologiaAtividadeSelecionada() {
		return listaTipologiaAtividadeAll;
	}

	public void setListaTipologiaAtividadeSelecionada(List<TipologiaAtividade> listaTipologiaAtividadeAll) {
		this.listaTipologiaAtividadeAll = listaTipologiaAtividadeAll;
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public List<FceDessedentacaoAnimalAtividade> getListaFceDessedentacaoAnimalAtividade() {
		return listaFceDessedentacaoAnimalAtividadeExcluidos;
	}

	public void setListaFceDessedentacaoAnimalAtividade(List<FceDessedentacaoAnimalAtividade> listaFceDessedentacaoAnimalAtividade) {
		this.listaFceDessedentacaoAnimalAtividadeExcluidos = listaFceDessedentacaoAnimalAtividade;
	}

	public List<FceDessedentacaoAnimalAtividade> getListaFceDessedentacaoAnimalAtividadeSelecionada() {
		return listaFceDessedentacaoAnimalAtividadeSelecionada;
	}

	public void setListaFceDessedentacaoAnimalAtividadeSelecionada(List<FceDessedentacaoAnimalAtividade> listaFceDessedentacaoAnimalAtividadeSelecionada) {
		this.listaFceDessedentacaoAnimalAtividadeSelecionada = listaFceDessedentacaoAnimalAtividadeSelecionada;
	}

	public FceDessedentacaoAnimalAtividade getFceDessedentacaoAnimalAtividade() {
		return fceDessedentacaoAnimalAtividade;
	}

	public void setFceDessedentacaoAnimalAtividade(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividade) {
		this.fceDessedentacaoAnimalAtividade = fceDessedentacaoAnimalAtividade;
	}

	public FceOutorgaLocalizacaoGeografica getFceOutorgaLocalizacaoGeograficaSelecionada() {
		return fceOutorgaLocalizacaoGeograficaSelecionada;
	}

	public void setFceOutorgaLocalizacaoGeograficaSelecionada(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionada) {
		this.fceOutorgaLocalizacaoGeograficaSelecionada = fceOutorgaLocalizacaoGeograficaSelecionada;
	}

	public BigDecimal getVazaoSugerida() {
		return vazaoSugerida;
	}

	public void setVazaoSugerida(BigDecimal vazaoSugerida) {
		this.vazaoSugerida = vazaoSugerida;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSubterranea(){
		return super.listaCaptacaoSubterranea;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSuperficial(){
		return super.listaCaptacaoSuperficial;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceAbastecimentoAnimal");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioDessedentacaoAnimal");
		RequestContext.getCurrentInstance().execute("abs_animal.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		fceDessedentacaoAnimal.setIdeFceDessedentacaoAnimal(null);
		fceDessedentacaoAnimal.setIdeFce(super.fce);
		for(FceDessedentacaoAnimalAtividade dessedentacaoAnimalAtividade : listaFceDessedentacaoAnimalAtividadeSelecionada){
			dessedentacaoAnimalAtividade.setIdeFceDessedentacaoAnimalAtividade(null);
			dessedentacaoAnimalAtividade.setFceDessedentacaoAnimal(fceDessedentacaoAnimal);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			salvarFce();
			salvarFceDessedentacaoAnimal();
			salvarListaFceDessedentacaoAnimalAtividade();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Dessedentação Animal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			carregarAbaDessedentacaoAnimal();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL);
			carregarFceDessedentacaoAnimal();
			listarFceDessedentacaoAnimalAtividade();
			realizarCalculos();
			removerTipologiaContidaNaListaDeEspecieSelecionada();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Dessedentação Animal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
}