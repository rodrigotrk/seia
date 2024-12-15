package br.gov.ba.seia.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.DadoConcedidoDTO;
import br.gov.ba.seia.entity.DestinoSocioeconomico;
import br.gov.ba.seia.entity.EspecieSupressao;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.NomePopularEspecie;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("especieSupressaoController")
@ViewScoped
public class EspecieSupressaoController {

	// Constantes para conversão de medidas do volume total.
	private static final Double MDC = 0.5;
	private static final Double ESTEREO = 1.5;
	
	@EJB
	private AsvSupressaoService asvSupressaoService;
	
	
	private List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao;
	private String nomEspecieSupressao;
	private List<EspecieSupressao> listaEspecieSupressao;
	private List<EspecieSupressao> listaEspecieSupressaoAll;
	private EspecieSupressao especieSupressaoSelecionada;
	private NomePopularEspecie nomePopularEspecieSelecionada;
	private List<DestinoSocioeconomico> listaDestinoSocioeconomico;
	private EspecieSupressaoAutorizacao especieSupressaoAutorizacaoSelecionado;

	private String idAtualizacao;
	
	public void init() {
		limpar();
		carregarEspecie();
	}
	
	public void carregarEspecie(){
		try {
			setListaEspecieSupressao(asvSupressaoService.listarEspecieSupressao());
			setListaEspecieSupressaoAll(new ArrayList<EspecieSupressao>(getListaEspecieSupressao()));
			carregarDestinoSocioeconomico();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void pesquisarEspecieSupressao(){
		listaEspecieSupressao = asvSupressaoService.pesquisarEspecieSupressao(listaEspecieSupressao, listaEspecieSupressaoAll, nomEspecieSupressao);
	}
	
	public void carregarDestinoSocioeconomico()  {
		listaDestinoSocioeconomico = asvSupressaoService.listarDestinoSocioeconomico();
	}
	
	public void obterNovoNomePopular() {
		nomePopularEspecieSelecionada = new NomePopularEspecie();
	}
	
	public void limpar() {
		listaEspecieSupressaoAutorizacao = null;
		nomEspecieSupressao = null;
		listaEspecieSupressao = null;
		listaEspecieSupressaoAll = null;
		especieSupressaoSelecionada = null;
		nomePopularEspecieSelecionada = null;
		listaDestinoSocioeconomico = null;
		especieSupressaoAutorizacaoSelecionado = null;
		idAtualizacao = null;
	}
	
	public void removerEspecie() {
		asvSupressaoService.removerEspecie(especieSupressaoAutorizacaoSelecionado, listaEspecieSupressaoAutorizacao);
		asvSupressaoService.adicionarEspecie(especieSupressaoAutorizacaoSelecionado, listaEspecieSupressao, listaEspecieSupressaoAll);
		
		if (!Util.isNullOuVazio(idAtualizacao)){
			Html.atualizar(idAtualizacao);
		}
	}
	
	public Integer obterPosicaoNaLista(List<DadoConcedidoDTO> listaDTO, DadoConcedidoDTO dto){
		Integer posicao = listaDTO.indexOf(dto);
		if (posicao != -1){
			return posicao;
		}
		
		return 0;
	}
	
	public void validarEspecie(ActionEvent evt) {
		
		String clientId = evt.getComponent().getParent().getParent().getParent().getParent().getClientId();
		
		EspecieSupressaoAutorizacao especieSupressao = (EspecieSupressaoAutorizacao) evt.getComponent().getAttributes().get("especieSupressao"); 
		if (Util.isNullOuVazio(especieSupressao.getVolumeTotalForaApp())) {
			MensagemUtil.msg0003("Volume total de APP");
			return;
		}
		
		if (Util.isNullOuVazio(especieSupressao.getListaDestinoSocioeconomicoSelecionado())){
			MensagemUtil.msg0003("Destino socioeconêmico do Produto");
			return;
		}
		
		especieSupressao.setEdicao(false);
		
		Html.atualizar(clientId);
	}
	
	public void editarEspecie(ActionEvent evt) {
		String clientId = evt.getComponent().getParent().getParent().getParent().getParent().getClientId();
		Html.atualizar(clientId);
	}
	
	private Double calcularVolumeTotalForaApp () {
		Double somaVolumeTotalForaForaApp = 0.0;
		if (!Util.isNullOuVazio(listaEspecieSupressaoAutorizacao)){
			for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : listaEspecieSupressaoAutorizacao) {
				if (!Util.isNullOuVazio(especieSupressaoAutorizacao.getVolumeTotalForaApp())){
					especieSupressaoAutorizacao.setVolumeTotalForaEstereo(converterEstereo(especieSupressaoAutorizacao.getVolumeTotalForaApp()));
					especieSupressaoAutorizacao.setVolumeTotalForaMDC(converterMDC(especieSupressaoAutorizacao.getVolumeTotalForaApp()));
					somaVolumeTotalForaForaApp += especieSupressaoAutorizacao.getVolumeTotalForaApp();
				}
			}
		}
		
		return somaVolumeTotalForaForaApp;
	}
	
	private Double calcularVolumeTotalEmApp () {
		Double somaVolumeTotalForaEmApp = 0.0;
		if (!Util.isNullOuVazio(listaEspecieSupressaoAutorizacao)){
			for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : listaEspecieSupressaoAutorizacao) {
				if (!Util.isNullOuVazio(especieSupressaoAutorizacao.getVolumeTotalEmApp())){
					especieSupressaoAutorizacao.setVolumeTotalEmEstereo(converterEstereo(especieSupressaoAutorizacao.getVolumeTotalEmApp()));
					especieSupressaoAutorizacao.setVolumeTotalEmMDC(converterMDC(especieSupressaoAutorizacao.getVolumeTotalEmApp()));
					somaVolumeTotalForaEmApp += especieSupressaoAutorizacao.getVolumeTotalEmApp();
				}
			}
		}
		
		return somaVolumeTotalForaEmApp;
	}
	
	public Double converterEstereo(Double volumeTotal){
		if (!Util.isNullOuVazio(volumeTotal)){
			return (volumeTotal * ESTEREO);
		}
		return 0.0;
	}
	
	public Double converterMDC(Double volumeTotal){
		if (!Util.isNullOuVazio(volumeTotal)){
			return (volumeTotal * MDC);
		}
		return 0.0;
	}
	
	public String getRetornarSomaVolumeForaTotal() {
		Double valor = calcularVolumeTotalForaApp();
		return retornarTextoVolumeTotal(valor);
	}
	
	public String getRetornarSomaVolumeEmTotal() {
		Double valor = calcularVolumeTotalEmApp();
		return retornarTextoVolumeTotal(valor);
	}
	
	private String formatarMoeda(Double valor) {
		DecimalFormat formatoDois = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols((new Locale("pt", "BR"))));
		formatoDois.setMinimumFractionDigits(2);
		formatoDois.setParseBigDecimal (true);
		return formatoDois.format(valor);
	}
	//Adaptativa - Ticket 29763
	private String formatarMoedaQuatroCasasDecimais(Double valor) {
		DecimalFormat formatoDois = new DecimalFormat("##,###,###,##0.0000", new DecimalFormatSymbols((new Locale("pt", "BR"))));
		formatoDois.setMinimumFractionDigits(4);
		formatoDois.setParseBigDecimal (true);
		return formatoDois.format(valor);
	}
	
	private String retornarTextoVolumeTotal(Double valor) {
		// e aqui em "ValorMoeda"
		String valorMoeda = formatarMoedaQuatroCasasDecimais(valor);
		String valorMoedaEstereo = formatarMoeda(converterEstereo(valor));
		String valorMoedaMDC = formatarMoeda(converterMDC(valor));
		
		return "m³ ".concat(valorMoeda)
				.concat("&#10;st ")
				.concat(valorMoedaEstereo)
				.concat("&#10;MDC ")
				.concat(valorMoedaMDC);
	}
	
	public void calcularVolumeForaAPP(EspecieSupressaoAutorizacao especieSupressaoAutorizacao) {
		if (!Util.isNullOuVazio(especieSupressaoAutorizacao.getVolumeTotalForaApp())){
			especieSupressaoAutorizacao.setVolumeTotalForaEstereo(converterEstereo(especieSupressaoAutorizacao.getVolumeTotalForaApp()));
			especieSupressaoAutorizacao.setVolumeTotalForaMDC(converterMDC(especieSupressaoAutorizacao.getVolumeTotalForaApp()));
		} else {
			especieSupressaoAutorizacao.setVolumeTotalForaEstereo(0.0);
			especieSupressaoAutorizacao.setVolumeTotalForaMDC(0.0);
		}
	}
	
	public void calcularVolumeEmAPP(EspecieSupressaoAutorizacao especieSupressaoAutorizacao) {
		if (!Util.isNullOuVazio(especieSupressaoAutorizacao.getVolumeTotalEmApp())){
			especieSupressaoAutorizacao.setVolumeTotalEmEstereo(converterEstereo(especieSupressaoAutorizacao.getVolumeTotalEmApp()));
			especieSupressaoAutorizacao.setVolumeTotalEmMDC(converterMDC(especieSupressaoAutorizacao.getVolumeTotalEmApp()));
		} else {
			especieSupressaoAutorizacao.setVolumeTotalEmEstereo(0.0);
			especieSupressaoAutorizacao.setVolumeTotalEmMDC(0.0);
		}
	}

	public List<EspecieSupressaoAutorizacao> getListaEspecieSupressaoAutorizacao() {
		return listaEspecieSupressaoAutorizacao;
	}

	public void setListaEspecieSupressaoAutorizacao(
			List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao) {
		this.listaEspecieSupressaoAutorizacao = listaEspecieSupressaoAutorizacao;
	}

	public String getNomEspecieSupressao() {
		return nomEspecieSupressao;
	}

	public void setNomEspecieSupressao(String nomEspecieSupressao) {
		this.nomEspecieSupressao = nomEspecieSupressao;
	}

	public List<EspecieSupressao> getListaEspecieSupressao() {
		return listaEspecieSupressao;
	}

	public void setListaEspecieSupressao(
			List<EspecieSupressao> listaEspecieSupressao) {
		this.listaEspecieSupressao = listaEspecieSupressao;
	}

	public List<EspecieSupressao> getListaEspecieSupressaoAll() {
		return listaEspecieSupressaoAll;
	}

	public void setListaEspecieSupressaoAll(
			List<EspecieSupressao> listaEspecieSupressaoAll) {
		this.listaEspecieSupressaoAll = listaEspecieSupressaoAll;
	}

	public EspecieSupressao getEspecieSupressaoSelecionada() {
		return especieSupressaoSelecionada;
	}

	public void setEspecieSupressaoSelecionada(
			EspecieSupressao especieSupressaoSelecionada) {
		this.especieSupressaoSelecionada = especieSupressaoSelecionada;
	}

	public NomePopularEspecie getNomePopularEspecieSelecionada() {
		return nomePopularEspecieSelecionada;
	}

	public void setNomePopularEspecieSelecionada(
			NomePopularEspecie nomePopularEspecieSelecionada) {
		this.nomePopularEspecieSelecionada = nomePopularEspecieSelecionada;
	}

	public List<DestinoSocioeconomico> getListaDestinoSocioeconomico() {
		return listaDestinoSocioeconomico;
	}

	public void setListaDestinoSocioeconomico(
			List<DestinoSocioeconomico> listaDestinoSocioeconomico) {
		this.listaDestinoSocioeconomico = listaDestinoSocioeconomico;
	}

	public EspecieSupressaoAutorizacao getEspecieSupressaoAutorizacaoSelecionado() {
		return especieSupressaoAutorizacaoSelecionado;
	}

	public void setEspecieSupressaoAutorizacaoSelecionado(
			EspecieSupressaoAutorizacao especieSupressaoAutorizacaoSelecionado) {
		this.especieSupressaoAutorizacaoSelecionado = especieSupressaoAutorizacaoSelecionado;
	}

	public String getIdAtualizacao() {
		return idAtualizacao;
	}

	public void setIdAtualizacao(String idAtualizacao) {
		this.idAtualizacao = idAtualizacao;
	}

}