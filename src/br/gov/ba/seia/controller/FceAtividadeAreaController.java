package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAtividadeArea;
import br.gov.ba.seia.entity.MetodoIrrigacao;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.FceAtividadeAreaService;
import br.gov.ba.seia.service.TipologiaAtividadeService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * 05/02/14
 * @author eduardo.fernandes
 */
@Named("fceAtividadeAreaController")
@ViewScoped
public class FceAtividadeAreaController {

	private static TipologiaAtividade outros = new TipologiaAtividade("Outros");

	@EJB
	private TipologiaAtividadeService tipologiaAtividadeService;
	@EJB
	private FceAtividadeAreaService fceAtividadeAreaService;

	private List<FceAtividadeArea> listaFceAtividadeArea;

	private List<TipologiaAtividade> listaTipologiaAtividade;
	/**
	 * Lista completa de Culturas Pulverizadas
	 */
	private List<TipologiaAtividade> listaTipologiaAtividadeFULL;
	/**
	 * Lista de Culturas Pulverizadas já adicionadas
	 */
	private List<TipologiaAtividade> listaTipologiaAtividadeAdicionadas;

	private String culturaPesquisada;
	private BigDecimal areaTotalCulturaDoNR;

	private StringBuilder stringAreaTotalCulturaDoNR;
	private StringBuilder somatorioAreaTotalCultura;

	private boolean irrigacao;
	
	/**
	 * Metodo que deve ser implementado após definição de onde vem a área do NR
	 */
	public void calcularAreaDaCultura(BigDecimal somatorioDasAreasDoRequerimento){
		areaTotalCulturaDoNR = somatorioDasAreasDoRequerimento;
		stringAreaTotalCulturaDoNR = new StringBuilder();
		formatarAreaToString(stringAreaTotalCulturaDoNR, areaTotalCulturaDoNR);
	}

	/**
	 * Transforma o valor da área numa string ##,###,###.##
	 * @param stringBuilder
	 * @param bigDecimal
	 */
	public void formatarAreaToString(StringBuilder stringBuilder, BigDecimal bigDecimal){
		DecimalFormat format = new DecimalFormat("##,###,###.##");
		format.setMinimumFractionDigits(2);
		stringBuilder.append(format.format(bigDecimal));
	}

	/**
	 * Buscar a lista de tipologia_atividade by TipologiaEnum passada no param
	 * @param enums
	 * @author eduardo.fernandes
	 */
	public void carregarListaTipologiaAtividade(List<TipologiaEnum> enums){
		try {
			if(Util.isNullOuVazio(listaTipologiaAtividade)){
				listaTipologiaAtividade = new ArrayList<TipologiaAtividade>();
			}
			listaTipologiaAtividade = tipologiaAtividadeService.buscarTipologiaAtividadeByListTipologiaEnum(enums);
			listaTipologiaAtividade.add(outros);
			armazenarListaCompleta();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista das Culturas Pulverizadas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Realiza um backup com a lista completa que vem do banco
	 * @author eduardo.fernandes
	 */
	private void armazenarListaCompleta(){
		if(Util.isNullOuVazio(listaTipologiaAtividadeFULL)){
			listaTipologiaAtividadeFULL = new ArrayList<TipologiaAtividade>();
		}
		listaTipologiaAtividadeFULL.addAll(listaTipologiaAtividade);
	}

	/**
	 * Carrega os FceAtividadeArea salvos para aquele FCE
	 * @param fce
	 */
	public void carregarListaFceAtividadeArea(Fce fce){
		try {
			listaFceAtividadeArea = fceAtividadeAreaService.buscarListaFceAtividadeAreaByIdeFce(fce);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações das Culturas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void tratarListaAtividadesAdicionadas(String string){
		removerCulturasEscolhidasDaListaPrincipal();
		listaTipologiaAtividadeAdicionadas = new ArrayList<TipologiaAtividade>();
		for(FceAtividadeArea atividadeArea : listaFceAtividadeArea){
			if(string.compareTo("Irrigação") == 0){
				atividadeArea.setToIrrigacao(true);
				if(!Util.isNullOuVazio(atividadeArea.getIndOutrosMetodo()) && atividadeArea.getIndOutrosMetodo()){
					simularOutrosMetodosNaCultura(atividadeArea);
				}
			} else if(string.compareTo("Pulverização") == 0){
				atividadeArea.setToPulverizacao(true);
			}
			listaTipologiaAtividadeAdicionadas.add(atividadeArea.getIdeTipologiaAtividade());
		}
		calcularAreaTotal();
	}

	public void simularOutrosNaLista(boolean isEdicao){
		TipologiaAtividade outrosLocal = new TipologiaAtividade("Outros");
		adicionarCultura(outrosLocal, isEdicao);
	}

	public void simularOutrosMetodosNaCultura(FceAtividadeArea fceAtividadeArea){
		fceAtividadeArea.setIdeMetodoIrrigacao(new MetodoIrrigacao(-1, "Outros"));
	}

	/**
	 * Adiciona a cultura selecionada na listaTipologiaAtividadeAdicionadas e informa se é ou não edição
	 * @author eduardo.fernandes
	 */
	public void adicionarCultura(TipologiaAtividade tipologiaAtividade, boolean isEdicao){
		if(Util.isNullOuVazio(listaTipologiaAtividadeAdicionadas)){
			listaTipologiaAtividadeAdicionadas = new ArrayList<TipologiaAtividade>();
		}
		listaTipologiaAtividadeAdicionadas.add(tipologiaAtividade);
		if(isTipologiaAtividadeOutros(tipologiaAtividade) && !isEdicao){
			JsfUtil.addWarnMessage(Util.getString("fce_pulverizacao_outros"));
		}
		criarFceAtividadeArea(tipologiaAtividade);
		somatorioAreaTotalCultura = new StringBuilder();
		culturaPesquisada = null;
		atualizarListaTipologiaAtividade();
	}

	/**
	 * Limpa a lista principal de Culturas, adiciona todos os itens do banco e remove as culturas que já foram adicionadas.
	 * @author eduardo.fernandes
	 */
	private void atualizarListaTipologiaAtividade(){
		limparListaTipologiaAtividade();
		listaTipologiaAtividade.addAll(listaTipologiaAtividadeFULL);
		removerCulturasEscolhidasDaListaPrincipal();
	}

	/**
	 * Cria a FceAtividade e seta-se o IdeTipologiaAtividade de acordo com o param.
	 * @param tipologiaAtividade
	 * @author eduardo.fernandes
	 */
	public void criarFceAtividadeArea(TipologiaAtividade tipologiaAtividade){
		FceAtividadeArea fceAtividadeArea = new FceAtividadeArea(tipologiaAtividade);
		adicionarFceAtividadeArea(fceAtividadeArea);
	}

	private void adicionarFceAtividadeArea(FceAtividadeArea fceAtividadeArea){
		if(Util.isNullOuVazio(listaFceAtividadeArea)){
			listaFceAtividadeArea = new ArrayList<FceAtividadeArea>();
		}
		listaFceAtividadeArea.add(fceAtividadeArea);
	}

	/**
	 * Busca as culturas na lista principal, originária do banco, de acordo com a string inserida pelo usuário.
	 * @author eduardo.fernandes
	 */
	public void pesquisarCultura(){
		atualizarListaTipologiaAtividade();
		List<TipologiaAtividade> listaTemp = new ArrayList<TipologiaAtividade>();
		listaTemp.addAll(listaTipologiaAtividade);
		limparListaTipologiaAtividade();
		for(TipologiaAtividade temp : listaTemp){
			if(temp.getDscTipologiaAtividade().toLowerCase().indexOf(culturaPesquisada.toLowerCase()) != -1){
				listaTipologiaAtividade.add(temp);
			}
		}
		if(Util.isNullOuVazio(listaTipologiaAtividade) && !existeOutros()){
			listaTipologiaAtividade.add(outros);
		}
	}

	/**
	 * Limpa listaTipologiaAtividade
	 */
	private void limparListaTipologiaAtividade(){
		if(!Util.isNullOuVazio(listaTipologiaAtividade)){
			listaTipologiaAtividade.clear();
		}
	}

	/**
	 * Ao adicionar uma cultura, o usuário dever removê-la da lista completa (originária do banco).
	 * @author eduardo.fernandes
	 */
	private void removerCulturasEscolhidasDaListaPrincipal(){
		if(!irrigacao){
			if(isCulturaAdicionada()){
				List<TipologiaAtividade> listaTemp = new ArrayList<TipologiaAtividade>();
				listaTemp.addAll(listaTipologiaAtividade);
				for(FceAtividadeArea fceAtividadeArea : listaFceAtividadeArea){
					if(listaTemp.contains(fceAtividadeArea.getIdeTipologiaAtividade())){
						listaTipologiaAtividade.remove(fceAtividadeArea.getIdeTipologiaAtividade());
					}
				}
			}
		}
	}

	/**
	 * Confirma a fceAtividadeArea (cultura selecionada) e a adiciona na listaFceAtividadeArea.
	 * Necessário realizar validação.
	 */
	public void confirmarCultura(FceAtividadeArea fceAtividadeArea, String string){
		if(validarCultura(fceAtividadeArea, string)){
			fceAtividadeArea.setConfirmado(true);
			calcularAreaTotal();
			if(fceAtividadeArea.isEdicao()){
				JsfUtil.addSuccessMessage(Util.getString("msg_generica_alteracao_efetuada"));
			}
		}
	}

	/**
	 * Permite edição do campo Área
	 */
	public void editarCultura(FceAtividadeArea fceAtividadeArea){
		fceAtividadeArea.setConfirmado(false);
		fceAtividadeArea.setEdicao(true);
		somatorioAreaTotalCultura = new StringBuilder();
	}

	/**
	 * Exclui as culturas que o usuário já havia selecionado.
	 */
	public void excluirCultura(FceAtividadeArea fceAtividadeArea){
		List<TipologiaAtividade> listaTemp = new ArrayList<TipologiaAtividade>();
		listaTemp.addAll(listaTipologiaAtividadeAdicionadas);
		if(listaTemp.contains(fceAtividadeArea.getIdeTipologiaAtividade())){
			listaTipologiaAtividadeAdicionadas.remove(fceAtividadeArea.getIdeTipologiaAtividade());
			listaFceAtividadeArea.remove(fceAtividadeArea);
			JsfUtil.addSuccessMessage(Util.getString("msg_generica_exclusao_efetuada"));
		}
		listaTemp = null;
		culturaPesquisada = null;
		atualizarListaTipologiaAtividade();
		calcularAreaTotal();
	}

	public void calcularAreaTotal(){
		boolean todosConfirmados = true;
		BigDecimal areaTotalDasCulturas = new BigDecimal(0);
		somatorioAreaTotalCultura = new StringBuilder();
		boolean isAnaliseTecnica = false;
		for(FceAtividadeArea fceAtividadeArea : listaFceAtividadeArea){
			if(!fceAtividadeArea.isConfirmado()){
				todosConfirmados = false;
				areaTotalDasCulturas = new BigDecimal(0);
				break;
			}
			if(todosConfirmados){
				areaTotalDasCulturas = areaTotalDasCulturas.add(fceAtividadeArea.getNumArea());
			}
			if(!isAnaliseTecnica){
				isAnaliseTecnica = isFceTecnico(fceAtividadeArea);
			}
		}
		if(!Util.isNullOuVazio(areaTotalDasCulturas)){
			formatarAreaToString(somatorioAreaTotalCultura, areaTotalDasCulturas);
			if(!existeOutros() && !isAnaliseTecnica && !isAreaTotalDoNRequalsSomatorioArea(stringAreaTotalCulturaDoNR, somatorioAreaTotalCultura)){
				boolean toIrrigacao = listaFceAtividadeArea.get(0).isToIrrigacao();
				boolean toPulverizacao = listaFceAtividadeArea.get(0).isToPulverizacao();
				if(toPulverizacao){
					JsfUtil.addWarnMessage("A área total a ser pulverizada tem que ser igual a "+ stringAreaTotalCulturaDoNR +" (ha), conforme informado no requerimento. Favor corrigir.");
				} else if(toIrrigacao){
					JsfUtil.addWarnMessage("A área total a ser irrigada tem que ser igual a "+ stringAreaTotalCulturaDoNR +" (ha), conforme informado no requerimento. Favor corrigir.");
				}
			}
		}
	}

	/**
	 * Salva a lista de Culturas Adicionadas
	 * @param fce
	 * @author eduardo.fernandes
	 */
	public void salvarFceAtividadeArea(Fce fce){
		try {
			excluirListaSalva(fce);
			fceAtividadeAreaService.salvarListaFceAtividadeArea(fce, listaFceAtividadeArea);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " as informações das Culturas Pulverizadas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Quando edição, excluir os FceAtividadeArea e anular os Id's dos objetos que estão carregados na lista.
	 * @see anularListaFceAtividadeArea()
	 * @param fce
	 * @author eduardo.fernandes
	 */
	public void excluirListaSalva(Fce fce){
		try {
			fceAtividadeAreaService.excluirFceAtividadeAreaByIdeFce(fce);
			anularListaFceAtividadeArea();
		}catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " as informações das Culturas Pulverizadas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Quando edição, anular o Id's dos FceAtividadeArea salvos no banco para que não haja erro ao salvar a lista;
	 * @author eduardo.fernandes
	 */
	private void anularListaFceAtividadeArea(){
		for(FceAtividadeArea atividadeArea : listaFceAtividadeArea){
			atividadeArea.setIdeFceAtividadeArea(null);
		}
	}

	public boolean verificarConfirmado(String string){
		boolean valido = true;
		for(FceAtividadeArea fceAtividadeArea : listaFceAtividadeArea){
			if(!fceAtividadeArea.isConfirmado() && !fceAtividadeArea.getIdeTipologiaAtividade().equals(outros)){
				JsfUtil.addErrorMessage(Util.getString("fce_confirmar_cultura") + string);
				valido = false;
				break;
			}
		}
		return valido;
	}

	public boolean isCulturaAdicionada(){
		return !Util.isNullOuVazio(listaFceAtividadeArea);
	}

	public boolean existeOutros(){
		return !Util.isNullOuVazio(listaTipologiaAtividadeAdicionadas) && listaTipologiaAtividadeAdicionadas.contains(outros);
	}

	public boolean existeOutrosMetodoParaCultura(){
		for(FceAtividadeArea fceAtividadeArea : listaFceAtividadeArea){
			if(Util.isNullOuVazio(fceAtividadeArea.getIdeMetodoIrrigacao())){
				return true;
			}
		}
		return false;
	}

	public boolean isTipologiaAtividadeOutros(TipologiaAtividade tipologiaAtividade){
		return !Util.isNullOuVazio(tipologiaAtividade) && tipologiaAtividade.getDscTipologiaAtividade().compareTo(outros.getDscTipologiaAtividade()) == 0;
	}

	public boolean isAreaTotalCalculada(){
		return !Util.isNullOuVazio(somatorioAreaTotalCultura);
	}

	public boolean isAreaTotalDoNRequalsSomatorioArea(StringBuilder areaTotalNR, StringBuilder somatorioArea){
		if(areaTotalNR.toString().compareTo(somatorioArea.toString()) == 0){
			return true;
		} else {
			return false;
		}
	}

	private Boolean validarCultura(FceAtividadeArea fceAtividadeArea, String string){
		if(isFceTecnico(fceAtividadeArea)){
			return true;
		}
		else if(string.compareTo("Irrigação") == 0){
			fceAtividadeArea.setToIrrigacao(true);
			return validarCulturaIrrigada(fceAtividadeArea);
		} else if(string.compareTo("Pulverização") == 0){
			fceAtividadeArea.setToPulverizacao(true);
			return validarCulturaPulverizada(fceAtividadeArea);
		}
		return null;
	}

	/**
	 * Remoção da regra de validação solicitado na Homologação da Entrega 3.2 para Análise Técnica dos FCE's. 
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param fceAtividadeArea
	 * @return
	 * @since 16/03/2016
	 */
	private boolean isFceTecnico(FceAtividadeArea fceAtividadeArea) {
		return !Util.isNullOuVazio(fceAtividadeArea.getIdeFce()) && !Util.isNull(fceAtividadeArea.getIdeFce().getIdeAnaliseTecnica()) && fceAtividadeArea.getIdeFce().getIdeDadoOrigem().getIdeDadoOrigem().equals(DadoOrigemEnum.TECNICO.getId());
	}

	private boolean validarCulturaIrrigada(FceAtividadeArea fceAtividadeArea){
		boolean valido = true;
		if(Util.isNullOuVazio(fceAtividadeArea.getIdeMetodoIrrigacao())){
			JsfUtil.addErrorMessage("O método de irrigação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(valido && !verificarCulturaAndMetodoDuplicado(fceAtividadeArea)){
			valido = false;
		}
		if(Util.isNullOuVazio(fceAtividadeArea.getNumArea())){
			JsfUtil.addErrorMessage("A área da cultura irrigada " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAtividadeArea.getNumVolumeDerivar())){
			JsfUtil.addErrorMessage("O volume da cultura irrigada " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceAtividadeArea.getIdeTipoPeriodoDerivacao())){
			JsfUtil.addErrorMessage("O período de derivação da cultura irrigada " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		} else if(fceAtividadeArea.getIdeTipoPeriodoDerivacao().isIntermitente()){
			if(Util.isNullOuVazio(fceAtividadeArea.getNumTempoCaptacao())){
				JsfUtil.addErrorMessage("O tempo de captação da cultura irrigada" + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			} else if(fceAtividadeArea.getNumTempoCaptacao() > 24){
				JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_tempo_captacao_info_0034"));
				valido = false;
			}
		}
		return valido;
	}

	private boolean verificarCulturaAndMetodoDuplicado(FceAtividadeArea fceAtividadeArea){
		boolean valido = true;
		if(isCulturaAdicionada() && listaFceAtividadeArea.size() > 1){
			for(FceAtividadeArea atividadeArea : listaFceAtividadeArea){
				if(atividadeArea.isConfirmado()){
					if((atividadeArea.getIdeTipologiaAtividade().equals(fceAtividadeArea.getIdeTipologiaAtividade()))
							&& (atividadeArea.getIdeMetodoIrrigacao().getIdeMetodoIrrigacao().intValue() == fceAtividadeArea.getIdeMetodoIrrigacao().getIdeMetodoIrrigacao().intValue())){
						JsfUtil.addErrorMessage("O método de irrigação já foi inserido para a cultura "+ atividadeArea.getIdeTipologiaAtividade().getDscTipologiaAtividade() + ". Favor escolher outra opção.");
						valido = false;
					}
				}
			}
		}
		return valido;
	}

	private boolean validarCulturaPulverizada(FceAtividadeArea fceAtividadeArea){
		boolean valido = true;
		if(Util.isNullOuVazio(fceAtividadeArea.getNumArea())){
			JsfUtil.addErrorMessage("A área da cultura pulverizada " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		return valido;
	}

	public void limpar(){
		irrigacao = false;
		areaTotalCulturaDoNR = null;
		stringAreaTotalCulturaDoNR = null;
		somatorioAreaTotalCultura = null;
		culturaPesquisada = null;
		listaFceAtividadeArea = null;
		listaTipologiaAtividade = null;
		listaTipologiaAtividadeAdicionadas = null;
		listaTipologiaAtividadeFULL = null;
	}

	public List<FceAtividadeArea> getListaFceAtividadeArea() {
		return listaFceAtividadeArea;
	}
	public void setListaFceAtividadeArea(List<FceAtividadeArea> listaFceAtividadeArea) {
		this.listaFceAtividadeArea = listaFceAtividadeArea;
	}
	public List<TipologiaAtividade> getListaTipologiaAtividade() {
		return listaTipologiaAtividade;
	}
	public void setListaTipologiaAtividade(List<TipologiaAtividade> listaTipologiaAtividade) {
		this.listaTipologiaAtividade = listaTipologiaAtividade;
	}
	public List<TipologiaAtividade> getListaTipologiaAtividadeFULL() {
		return listaTipologiaAtividadeFULL;
	}
	public void setListaTipologiaAtividadeFULL(List<TipologiaAtividade> listaTipologiaAtividadeFULL) {
		this.listaTipologiaAtividadeFULL = listaTipologiaAtividadeFULL;
	}
	public List<TipologiaAtividade> getListaTipologiaAtividadeAdicionadas() {
		return listaTipologiaAtividadeAdicionadas;
	}
	public void setListaTipologiaAtividadeAdicionadas(List<TipologiaAtividade> listaTipologiaAtividadeAdicionadas) {
		this.listaTipologiaAtividadeAdicionadas = listaTipologiaAtividadeAdicionadas;
	}
	public String getCulturaPesquisada() {
		return culturaPesquisada;
	}
	public void setCulturaPesquisada(String culturaPesquisada) {
		this.culturaPesquisada = culturaPesquisada;
	}
	public BigDecimal getAreaTotalCulturaDoNR() {
		return areaTotalCulturaDoNR;
	}
	public void setAreaTotalCulturaDoNR(BigDecimal areaTotalCulturaDoNR) {
		this.areaTotalCulturaDoNR = areaTotalCulturaDoNR;
	}
	public StringBuilder getStringAreaTotalCulturaDoNR() {
		return stringAreaTotalCulturaDoNR;
	}
	public void setStringAreaTotalCulturaDoNR(StringBuilder stringAreaTotalCulturaDoNR) {
		this.stringAreaTotalCulturaDoNR = stringAreaTotalCulturaDoNR;
	}
	public StringBuilder getSomatorioAreaTotalCultura() {
		return somatorioAreaTotalCultura;
	}
	public void setSomatorioAreaTotalCultura(StringBuilder somatorioAreaTotalCultura) {
		this.somatorioAreaTotalCultura = somatorioAreaTotalCultura;
	}

	public boolean isIrrigacao() {
		return irrigacao;
	}

	public void setIrrigacao(boolean irrigacao) {
		this.irrigacao = irrigacao;
	}
}