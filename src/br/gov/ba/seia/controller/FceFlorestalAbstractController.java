package br.gov.ba.seia.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.DadoConcedidoDTO;
import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.dto.DadoConcedidoPoligonalDTO;
import br.gov.ba.seia.entity.DestinoSocioeconomico;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EspecieFlorestal;
import br.gov.ba.seia.entity.EspecieFlorestalAutDestinoSocioEconomico;
import br.gov.ba.seia.entity.EspecieFlorestalAutorizacao;
import br.gov.ba.seia.entity.EspecieSupressao;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.FceFlorestal;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.NomePopularEspecie;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.ProdutoSupressao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoEspecieFlorestalEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.facade.FceFlorestalAbstractService;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;


public abstract class FceFlorestalAbstractController extends FceController {
	
	protected FceFlorestalAbstractService fceFlorestalService;
	
	protected DocumentoObrigatorio documentoObrigatorio;
	
	protected DadoConcedidoFceImpl dadoConcedido;
	
	protected List<Produto> listProduto;
	
	protected Produto produtoNativoSelecionado;
	
	protected List<ProdutoSupressao> produtoFlorestalSelecionado;
	
	protected Boolean isLenha;
	
	protected List<EspecieFlorestal> especieFlorestalList;
	
	protected List<EspecieFlorestal> especieFlorestalListAll;
	
	protected String nomEspecieSupressao;
	
	protected NomePopularEspecie nomePopularEspecieSelecionada;
	
	protected EspecieFlorestal especieFlorestalSelecionada;
	
	protected List<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoList;
	
	protected MetodoUtil metodoRetornoEspecie;
	
	protected List<DestinoSocioeconomico> listaDestinoSocioeconomico;
	
	private List<DadoConcedidoDTO> listaDTO;
	
	private EspecieFlorestalAutorizacao especieFlorestalAutorizacaoSelecionado;
	
	private String labelAto;
	
	private String labelAreaTotal;
	
	private boolean isRenderedAreaUnidadeProducao;
	        
	private static final int IDE_TIPO_PRODUTO_SUPRESSAO = 3;
	
	private static final Double MDC = 0.5;
	
	private static final Double ESTEREO = 1.5;

	protected FceFlorestal fceFlorestal = new FceFlorestal();
	
	protected abstract MotivoNotificacaoEnum getMotivoNotificacao();
	
	protected abstract PerguntaEnum getPergunta();
	
	public abstract String getLabelArea();
	
	public FceFlorestalAbstractController(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio, DadoConcedidoFceImpl dadoConcedidoImpl, FceServiceFacade fceServiceFacade, FceFlorestalAbstractService fceFlorestalService) {
		super.requerimento = requerimento;
		super.fceServiceFacade = fceServiceFacade;
		this.fceFlorestalService = fceFlorestalService;
		this.documentoObrigatorio = documentoObrigatorio;
		this.dadoConcedido=dadoConcedidoImpl;
	}

	@Override
	public void init() {
		metodoRetornoEspecie = new MetodoUtil(this, "retornoEspecie");
		carregarAba();
	}
	
	@Override
	public void abrirDialog() {
		verificarEdicao();
		Html.atualizar("frmFceFlorestal");
		Html.exibir("fce_florestal");
	}
	
	@Override
	public void carregarAba() {
		carregarFceDoRequerente(getDocumentoObrigatorio());
		carregarProdutos();
		carregarEspecieFlorestal();
	}
	
	@Override
	public void limpar() {
		
	}

	@Override
	public void finalizar() {
		try {
			prepararParaFinalizar();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	protected void carregarFceTecnico() {
		try {
			super.carregarFceDoTecnico(documentoObrigatorio);
			montarDto(getPergunta(), getMotivoNotificacao());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	@Override
	public void prepararParaFinalizar() throws Exception {
		if(validarAba() && validarDadosConcedidoImoveis()) {
			super.concluirFce();
			montarObjetos(false);
			fceFlorestalService.salvarFceFlorestal(fceFlorestal);
			
			if (fce.isFceTecnico()){
				fceFlorestalService.salvarProcessoAtoConcedido(getListaDTO(), dadoConcedido);
				Html.executarJS("atualizarDadoConcedido();");
				MensagemUtil.sucesso(Util.getString("MSG-010"));
			} else {
				MensagemUtil.sucesso("Fce salvo com sucesso!");
			}
			
			abrirDialogoImprimirRelatorio();
		}
	}
	
	public boolean validarEspecieFlorestalAutorizacaoList(){
		 List<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoList = getEspecieFlorestalAutorizacaoList();
		 
		 boolean retorno = true;
		 
		 if(Util.isNullOuVazio(especieFlorestalAutorizacaoList)){
			 retorno = false;
			 MensagemUtil.alerta("O campo Selecione o produto é de preenchimento obrigatório!");
		 }else{
			 
			 for(EspecieFlorestalAutorizacao especieFlorestalAutorizacao : especieFlorestalAutorizacaoList){
				 
				 if(Util.isNullOuVazio(especieFlorestalAutorizacao.getVolumeTotalForaApp())){
					 retorno = false;
					 MensagemUtil.alerta("O campo Volume total fora de APP é de preenchimento obrigatório!");
				 }
				 
				 if(Util.isNullOuVazio(especieFlorestalAutorizacao.getListaDestinoSocioeconomicoSelecionado())){
					 retorno = false;
					 MensagemUtil.alerta("O campo Destino socioeconêmico do Produto é de preenchimento obrigatório!");
				 }
				 
				 if(Util.isNullOuVazio(especieFlorestalAutorizacao.getEdicao()) || especieFlorestalAutorizacao.getEdicao()){
					 retorno = false;
					 MensagemUtil.alerta("A confirmação do produto Florestal é de preenchimento obrigatório!"); 
				 }
			 }
		 }
		 
		 return retorno;
	}
	
	/**
	 * carrega a lista do combo selecioneProduto.
	 */
	private void carregarProdutos() {
		try {
			listProduto = fceFlorestalService.carregarListaProdutosByTipoProduto(IDE_TIPO_PRODUTO_SUPRESSAO);
			
			//MODIFICAÇÃO NECESSÁRIA PARA SINAFLOR
			if(!Util.isNullOuVazio(listProduto)) {
				List<Produto> tmp = new ArrayList<Produto>();
				
				for (Produto p : listProduto) {
					if(!p.getDscProduto().equals("Palmito in natura(estirpe)") && !p.getDscProduto().equals("Raízes")) {
						tmp.add(p);
					}
				}
				
				listProduto = tmp;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "Produtos da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeProduto(){
		if(!Util.isNullOuVazio(produtoNativoSelecionado) && produtoNativoSelecionado.getDscProduto().equalsIgnoreCase("Lenha")){
			isLenha = true;
		}else{
			isLenha = false;
		}
	}
	
	public boolean isPodeAdicionar(){
		return !this.listProduto.isEmpty() && !Util.isNullOuVazio(produtoNativoSelecionado);
	}
	
	public void adicionarProdutoSupressao(){
		if(!Util.isNullOuVazio(produtoNativoSelecionado)){
			if(Util.isNullOuVazio(produtoFlorestalSelecionado)){
				produtoFlorestalSelecionado = new ArrayList<ProdutoSupressao>();
			}
		}
		else {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_msg010"));
		}
	}
	
	public void carregarEspecieFlorestal(){
		
		try {
			this.especieFlorestalList = fceFlorestalService.listarEspecieFlorestal(TipoEspecieFlorestalEnum.NATIVA.getId());
			this.especieFlorestalListAll = new ArrayList<EspecieFlorestal>(this.especieFlorestalList);
			carregarDestinoSocioeconomico();
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarDestinoSocioeconomico() throws Exception {
		listaDestinoSocioeconomico = fceFlorestalService.listarDestinoSocioeconomico();
	}
	
	public void pesquisarEspecieSupressao(){
		try {
			this.especieFlorestalList = fceFlorestalService.pesquisarEspecieFlorestal(especieFlorestalList, especieFlorestalListAll, nomEspecieSupressao);
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void adicionarEspecie(EspecieSupressaoAutorizacao especieSupressaoAutorizacao, List<EspecieSupressao> listaEspecieSupressao,	List<EspecieSupressao> listaEspecieSupressaoAll) {
		if (!listaEspecieSupressao.contains(especieSupressaoAutorizacao.getIdeEspecieSupressao())) {
			listaEspecieSupressao.add(especieSupressaoAutorizacao.getIdeEspecieSupressao());
			listaEspecieSupressaoAll.add(especieSupressaoAutorizacao.getIdeEspecieSupressao());
			Collections.sort(listaEspecieSupressao);
			Collections.sort(listaEspecieSupressaoAll);
		}
	}
	
	public void retornoEspecie() throws Exception {
		if(verificarSeExisteEspeficacaoLenha() && !Util.isNullOuVazio(produtoNativoSelecionado) 
				&& produtoNativoSelecionado.getDscProduto().equalsIgnoreCase("Lenha")){
			
			JsfUtil.addWarnMessage("O produto selecionado já foi adicionado!");
		}
		if(!verificarSeExisteEspeficacaoLenha() && !Util.isNullOuVazio(produtoNativoSelecionado) 
				&& produtoNativoSelecionado.getDscProduto().equalsIgnoreCase("Lenha")){
			
			if (fceFlorestalService.ajustarRetornoEspecie(this)) {
				Html.atualizar(new String[]{ "frmFceFlorestal:groupdProdutosFlorestal"});
			}
		}else if(!Util.isNullOuVazio(getProdutoNativoSelecionado()) 
				&& !getProdutoNativoSelecionado().getDscProduto().equalsIgnoreCase("Lenha")){
			if (fceFlorestalService.ajustarRetornoEspecie(this)) {
				Html.atualizar(new String[]{ "frmFceFlorestal:groupdProdutosFlorestal"});
			}
		}
		
	}
	
	public boolean verificarSeExisteEspeficacaoLenha(){
			
			if(!Util.isNullOuVazio(especieFlorestalAutorizacaoList)){
				
				for(EspecieFlorestalAutorizacao espSupAut :  especieFlorestalAutorizacaoList){
					
					if(!Util.isNullOuVazio(espSupAut.getIdeProduto())){
						
						if(espSupAut.getIdeProduto().getDscProduto().equalsIgnoreCase("Lenha")){
							return true;
						}
					}
				}
			}
			
			return false;
	}
	
	public void calcularVolumeEmAPP(AjaxBehaviorEvent evt) {
		EspecieFlorestalAutorizacao especieFlorestalAutorizacao = (EspecieFlorestalAutorizacao) evt.getComponent().getAttributes().get("especieFlorestal");
		if (!Util.isNullOuVazio(especieFlorestalAutorizacao.getVolumeTotalEmApp())){
			especieFlorestalAutorizacao.setVolumeTotalEmEstereo(converterEstereo(especieFlorestalAutorizacao.getVolumeTotalEmApp()));
			especieFlorestalAutorizacao.setVolumeTotalEmMDC(converterMDC(especieFlorestalAutorizacao.getVolumeTotalEmApp()));
		} else {
			especieFlorestalAutorizacao.setVolumeTotalEmEstereo(0.0);
			especieFlorestalAutorizacao.setVolumeTotalEmMDC(0.0);
		}
	}
	
	public void calcularVolumeForaAPP(AjaxBehaviorEvent evt) {
		EspecieFlorestalAutorizacao especieFlorestalAutorizacao = (EspecieFlorestalAutorizacao) evt.getComponent().getAttributes().get("especieFlorestal");
		if (!Util.isNullOuVazio(especieFlorestalAutorizacao.getVolumeTotalForaApp())){
			especieFlorestalAutorizacao.setVolumeTotalForaEstereo(converterEstereo(especieFlorestalAutorizacao.getVolumeTotalForaApp()));
			especieFlorestalAutorizacao.setVolumeTotalForaMDC(converterMDC(especieFlorestalAutorizacao.getVolumeTotalForaApp()));
		} else {
			especieFlorestalAutorizacao.setVolumeTotalForaEstereo(0.0);
			especieFlorestalAutorizacao.setVolumeTotalForaMDC(0.0);
		}
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
	
	private Double calcularVolumeTotalEmApp () {
		Double somaVolumeTotalForaEmApp = 0.0;
		if (!Util.isNullOuVazio(especieFlorestalAutorizacaoList)){
			for (EspecieFlorestalAutorizacao especieFlorestalAutorizacao : especieFlorestalAutorizacaoList) {
				if (!Util.isNullOuVazio(especieFlorestalAutorizacao.getVolumeTotalEmApp())){
					especieFlorestalAutorizacao.setVolumeTotalEmEstereo(converterEstereo(especieFlorestalAutorizacao.getVolumeTotalEmApp()));
					especieFlorestalAutorizacao.setVolumeTotalEmMDC(converterMDC(especieFlorestalAutorizacao.getVolumeTotalEmApp()));
					somaVolumeTotalForaEmApp += especieFlorestalAutorizacao.getVolumeTotalEmApp();
				}
			}
		}
		
		return somaVolumeTotalForaEmApp;
	}
	
	private Double calcularVolumeTotalForaApp () {
		Double somaVolumeTotalForaForaApp = 0.0;
		if (!Util.isNullOuVazio(especieFlorestalAutorizacaoList)){
			for (EspecieFlorestalAutorizacao especieFlorestalAutorizacao : especieFlorestalAutorizacaoList) {
				if (!Util.isNullOuVazio(especieFlorestalAutorizacao.getVolumeTotalForaApp())){
					especieFlorestalAutorizacao.setVolumeTotalForaEstereo(converterEstereo(especieFlorestalAutorizacao.getVolumeTotalForaApp()));
					especieFlorestalAutorizacao.setVolumeTotalForaMDC(converterMDC(especieFlorestalAutorizacao.getVolumeTotalForaApp()));
					somaVolumeTotalForaForaApp += especieFlorestalAutorizacao.getVolumeTotalForaApp();
				}
			}
		}
		
		return somaVolumeTotalForaForaApp;
	}
	
	private String retornarTextoVolumeTotal(Double valor) {
		String valorMoeda = formatarMoeda(valor);
		String valorMoedaEstereo = formatarMoeda(converterEstereo(valor));
		String valorMoedaMDC = formatarMoeda(converterMDC(valor));
		
		return "m³ ".concat(valorMoeda)
				.concat("&#10;st ")
				.concat(valorMoedaEstereo)
				.concat("&#10;MDC ")
				.concat(valorMoedaMDC);
	}
	
	private String formatarMoeda(Double valor) {
		DecimalFormat formatoDois = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols((new Locale("pt", "BR"))));
		formatoDois.setMinimumFractionDigits(2); 
		formatoDois.setParseBigDecimal (true);
		return formatoDois.format(valor);
	}
	
	public void validarEspecie(ActionEvent evt) {
		EspecieFlorestalAutorizacao especieFlorestal = (EspecieFlorestalAutorizacao) evt.getComponent().getAttributes().get("especieFlorestal");
		if (Util.isNullOuVazio(especieFlorestal.getListaDestinoSocioeconomicoSelecionado())){
			MensagemUtil.msg0003("Destino socioeconêmico do Produto");
			return;
		}
		
		especieFlorestal.setEdicao(false);
	}
	
	public void limparEspeciesSelecionadas(AjaxBehaviorEvent evt) {
		if(!fceFlorestal.getIndMaterialLenhoso()) {
			if(!Util.isNullOuVazio(especieFlorestalAutorizacaoList)) {
				for(int i=especieFlorestalAutorizacaoList.size()-1; i>=0; i--) {
					especieFlorestalAutorizacaoSelecionado = especieFlorestalAutorizacaoList.get(i);
					exibirEspecie();
					exibirNomePopular();
					removerEspecieSelecionada();
				}
			}
		}
	}
	
	public void removerEspecieNativa() throws Exception {
		exibirEspecie();
		exibirNomePopular();
		removerEspecieSelecionada();
		Html.atualizar("frmFceFlorestal:groupdProdutosFlorestal");
	}

	private void removerEspecieSelecionada() {
		
		if(Util.isNullOuVazio(fceFlorestal.getListaEspecieFlorestalAutorizacaoNativaRemocao())) {
			fceFlorestal.setListaEspecieFlorestalAutorizacaoNativaRemocao(new ArrayList<EspecieFlorestalAutorizacao>());
		}
		fceFlorestal.getListaEspecieFlorestalAutorizacaoNativaRemocao().add(especieFlorestalAutorizacaoSelecionado);
		
		if (!Util.isNullOuVazio(especieFlorestalAutorizacaoSelecionado)) { 
			especieFlorestalAutorizacaoList.remove(especieFlorestalAutorizacaoSelecionado);
		}
		else {
			
			for (int i = especieFlorestalAutorizacaoList.size()-1; i >= 0 ; i--) {
				if(especieFlorestalAutorizacaoSelecionado == especieFlorestalAutorizacaoList.get(i)) {
					especieFlorestalAutorizacaoList.remove(i);
					break;
				}
			}
		}
	}

	private void exibirNomePopular() {
		List<NomePopularEspecie> removidosNomePopularEspecie = new ArrayList<NomePopularEspecie>();
		
		if(!Util.isNullOuVazio(especieFlorestalAutorizacaoSelecionado.getIdeEspecieFlorestal())){
			removidosNomePopularEspecie = especieFlorestalAutorizacaoSelecionado.getIdeEspecieFlorestal().getRemovidosNomePopularEspecie();
		}
		
		if (!Util.isNullOuVazio(removidosNomePopularEspecie)){
			if (removidosNomePopularEspecie.contains(especieFlorestalAutorizacaoSelecionado.getIdeNomePopularEspecie())){
				removidosNomePopularEspecie.remove(especieFlorestalAutorizacaoSelecionado.getIdeNomePopularEspecie());
			}
		}
	}
	
	public void exibirEspecie() {
		if (!especieFlorestalList.contains(especieFlorestalAutorizacaoSelecionado.getIdeEspecieFlorestal())) {
			
			if(!Util.isNullOuVazio(especieFlorestalAutorizacaoSelecionado.getIdeEspecieFlorestal())) {
				especieFlorestalList.add(especieFlorestalAutorizacaoSelecionado.getIdeEspecieFlorestal());
				especieFlorestalListAll.add(especieFlorestalAutorizacaoSelecionado.getIdeEspecieFlorestal());
				Collections.sort(especieFlorestalList);
				Collections.sort(especieFlorestalListAll);
			}
		}
	}
	
	public String getMsgImprimirRelatorio() {
		return super.msgImprimirRelatorio(labelAto);
	}
	
	public void abrirDialogoImprimirRelatorio() {
		Html.atualizar("pnlImprimirRelatorioFceFlorestal");
		Html.exibir("dlgImprimirRelatorioFceFlorestal");
		Html.esconder("fce_florestal");
	}
	
	public StreamedContent getImprimirRelatorio() throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_fce", fce.getIdeFce());
		lParametros.put("ide_origem_fce", fce.getIdeDadoOrigem().getIdeDadoOrigem());
		lParametros.put("NOM_DOC", fceServiceFacade.retornarNomDocumento(fce));
		lParametros.put("TIPO_DOC", fce.isFceTecnico() ? "DADOS CONCEDIDOS" : "FORMULÁRIO DE CARACTERIZAÇÃO DO EMPREENDIMENTO – FCE");
		
		return new RelatorioUtil("fce_florestal.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public void montarDto(PerguntaEnum perguntaEnum, MotivoNotificacaoEnum motivoNotificacaoEnum) throws Exception {
		setListaDTO(new ArrayList<DadoConcedidoDTO>());

		Integer ideRequerimento = super.fce.getIdeRequerimento().getIdeRequerimento();
		Integer ideProcesso = dadoConcedido.getProcessoAto().getProcesso().getIdeProcesso();
		Collection<Imovel> imoveis = fceFlorestalService.listarImoveisBy(ideProcesso);
		
		if(!Util.isNullOuVazio(imoveis)) {
			for(Imovel i : imoveis) {
				String nome = "";
				if(Util.isNull(i.getImovelRural())) {
					nome = fceFlorestalService.obterNomeDoEmpreendimento(ideProcesso);
				} 
				else {
					nome = i.getNomeImovelRural();
					if(!Util.isNullOuVazio(i.getImovelRural().getNumMatricula())) {
						nome += " - MATRÍCULA: " + i.getImovelRural().getNumMatricula();
					}
				}
				DadoConcedidoDTO dadoConcedidoDTO = new DadoConcedidoDTO(i.getIdeImovel(), nome);
				LocalizacaoGeografica locGeo = fceFlorestalService.carregarLocalizacaoGeografica(perguntaEnum, ideRequerimento, i); 
				
				if (Util.isNullOuVazio(locGeo.getIdeSistemaCoordenada())) {
					DadoConcedidoPoligonalDTO poligonalDTO = new DadoConcedidoPoligonalDTO(locGeo, null);
					fceFlorestalService.calcularArea(poligonalDTO);
					dadoConcedidoDTO.getListaPoligonalDTO().add(poligonalDTO);
					fceFlorestalService.listarPoligonalNotificacao(dadoConcedidoDTO, ideProcesso, i, motivoNotificacaoEnum);
					getListaDTO().add(dadoConcedidoDTO);
				} else {
					DadoConcedidoPoligonalDTO poligonalDTO = new DadoConcedidoPoligonalDTO(locGeo, null);
					
					poligonalDTO.setValArea(fceFlorestalService.obterValorTotalArea(ideRequerimento).doubleValue());
					
					fceFlorestalService.tratarPonto(locGeo);
					dadoConcedidoDTO.getListaPoligonalDTO().add(poligonalDTO);
					dadoConcedidoDTO.setLatitudeInicial(locGeo.getLatitudeInicial());
					dadoConcedidoDTO.setLongitudeInicial(locGeo.getLongitudeInicial());
					fceFlorestalService.listarPoligonalNotificacao(dadoConcedidoDTO, ideProcesso, i, motivoNotificacaoEnum);
					getListaDTO().add(dadoConcedidoDTO);
				}	
			}
			
			verificarDadoConcedido();
		}
	}
	
	public void verificarDadoConcedido() throws Exception {
		Integer ideProcessoAto = dadoConcedido.getProcessoAto().getIdeProcessoAto();
		for (ProcessoAtoConcedido processoAtoConcedido : fceFlorestalService.listarProcessoAtoConcedido(ideProcessoAto)) {
			for (DadoConcedidoDTO dto : getListaDTO()) {
				if(dto.getIdeImovel().equals(processoAtoConcedido.getIdeImovel().getIdeImovel())) {
					
					DadoConcedidoPoligonalDTO poligonalConcedida = null;
					
					if(!Util.isNull(dto.getListaPoligonalNotificacaoDTO())) {
						poligonalConcedida = fceFlorestalService.localizacaoGeograficaConcedida(dto.getListaPoligonalNotificacaoDTO(), processoAtoConcedido.getIdeLocalizacaoGeografica());
					}
					
					if(Util.isNull(poligonalConcedida)) {
						poligonalConcedida = fceFlorestalService.localizacaoGeograficaConcedida(dto.getListaPoligonalDTO(), processoAtoConcedido.getIdeLocalizacaoGeografica());
					}
					poligonalConcedida.setConcedido(true);
					dto.setPoligonalConcedida(poligonalConcedida);
					break;
				}
			}
		}
	}
	
	public boolean isRenderedPoligonalRequerimento(DadoConcedidoDTO dto) {
		return !Util.isNullOuVazio(dto.getListaPoligonalDTO());
	}
	
	public boolean isDisabled(DadoConcedidoDTO dto, DadoConcedidoPoligonalDTO poligonalDTO) {
		if(desabilitarTudo) {
			return true;
		}
		else if(Util.isNull(dto.getPoligonalConcedida())) {
			return false;
		}
		return !dto.getPoligonalConcedida().getLocalizacaoGeografica().equals(poligonalDTO.getLocalizacaoGeografica());
	}
	
	public void add(AjaxBehaviorEvent evt) {
		DadoConcedidoDTO dadoConcedidoDTO = (DadoConcedidoDTO) evt.getComponent().getAttributes().get("dadoConcedidoDTO"); 
		DadoConcedidoPoligonalDTO poligonalDTO = (DadoConcedidoPoligonalDTO) evt.getComponent().getAttributes().get("poligonalDTO");
		if(poligonalDTO.isConcedido()) {
			dadoConcedidoDTO.setPoligonalConcedida(poligonalDTO);
		}
		else {
			dadoConcedidoDTO.setPoligonalConcedida(null);
		}
		
		Integer posicao = obterPosicaoNaLista(listaDTO, dadoConcedidoDTO);
		String idPoligonalRequerimento = "frmFceFlorestal:accDadoConcedidoImovel:".concat(posicao.toString()).concat(":poligonalRequerimento");
		String idPoligonalNotificacao = "frmFceFlorestal:accDadoConcedidoImovel:".concat(posicao.toString()).concat(":poligonalNotificacao");
		
		Html.atualizar(idPoligonalRequerimento, idPoligonalNotificacao);
	}
	
	public Integer obterPosicaoNaLista(List<DadoConcedidoDTO> listaDTO, DadoConcedidoDTO dto){
		Integer posicao = listaDTO.indexOf(dto);
		if (posicao != -1){
			return posicao;
		}
		
		return 0;
	}
	
	public Boolean validarDadosConcedidoImoveis() {
		if (isFceTecnico()) {
			Boolean retornoRequerimento = Boolean.FALSE;
			Boolean retornoNotificacao = Boolean.FALSE;
			
			for (DadoConcedidoDTO dadoConcedidoDTO : listaDTO) {
				retornoRequerimento = Boolean.FALSE;
				retornoNotificacao = Boolean.FALSE;
	
				if(!Util.isNull(dadoConcedidoDTO.getListaPoligonalDTO())){
					for(DadoConcedidoPoligonalDTO requerimento : dadoConcedidoDTO.getListaPoligonalDTO()){
						if(!requerimento.isConcedido()){
							retornoRequerimento = Boolean.FALSE;
							if(!Util.isNull(dadoConcedidoDTO.getListaPoligonalNotificacaoDTO())){
								
								for(DadoConcedidoPoligonalDTO notificacao : dadoConcedidoDTO.getListaPoligonalNotificacaoDTO()){
									if(!notificacao.isConcedido()){
										JsfUtil.addErrorMessage(Util.getString("MSG-erro-selecionar-areas"));
										return Boolean.FALSE;
									}else{
										retornoNotificacao = Boolean.TRUE;
									}
								}
							} else {
								JsfUtil.addErrorMessage(Util.getString("MSG-erro-selecionar-areas"));
								return Boolean.FALSE;
							}
						}else{
							retornoRequerimento = Boolean.TRUE;
						}
					}
				}
			}
			return (retornoRequerimento || retornoNotificacao);
		}
		return Boolean.TRUE;
	}
	
	public void montarObjetos(boolean isDuplicar) {
		
		fceFlorestal.setIdeFce(super.fce);
		
		if(!Util.isNullOuVazio(especieFlorestalAutorizacaoList)) {
			
			List<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoListTmp = Util.deepCloneList(especieFlorestalAutorizacaoList);
			for(EspecieFlorestalAutorizacao especieFlorestalAutorizacao : especieFlorestalAutorizacaoListTmp) {
				if (isDuplicar) {

					especieFlorestalAutorizacao.setIdeEspecieFlorestalAutorizacao(null);
					especieFlorestalAutorizacao.setFceFlorestal(fceFlorestal);
				}
				
				especieFlorestalAutorizacao.setEspecieFlorestalAutDestinoSocioEconomicoCollection(new ArrayList<EspecieFlorestalAutDestinoSocioEconomico>());
				for(DestinoSocioeconomico destinoSocioeconomico : especieFlorestalAutorizacao.getListaDestinoSocioeconomicoSelecionado()) {
					EspecieFlorestalAutDestinoSocioEconomico especieFlorestalAutDestinoSocioEconomico = new EspecieFlorestalAutDestinoSocioEconomico();
					especieFlorestalAutDestinoSocioEconomico.setIdeDestinoSocioeconomico(destinoSocioeconomico);
					especieFlorestalAutDestinoSocioEconomico.setIdeEspecieFlorestalAutorizacao(especieFlorestalAutorizacao);
					especieFlorestalAutorizacao.getEspecieFlorestalAutDestinoSocioEconomicoCollection().add(especieFlorestalAutDestinoSocioEconomico);
				}
			}
			fceFlorestal.setListaEspecieFlorestalAutorizacaoNativa(especieFlorestalAutorizacaoListTmp);
		}
	}

	@Override
	protected void prepararDuplicacao() {
		try {
			fceFlorestal = fceFlorestal.clone();
			fceFlorestal.setIdeFceFlorestal(null);
			fceFlorestal.setEspecieFlorestalAutorizacaoCollection(null);
			montarObjetos(true);
		}
		catch (CloneNotSupportedException e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			fceFlorestalService.salvarFceFlorestal(fceFlorestal);
			abrirDialogoImprimirRelatorio();
		}
		catch (Exception e) {
			throw new SeiaRuntimeException(Util.getString("msg_generica_erro_ao_duplicar") + " o ." + getLabelAto(),e);
		}
	}
	
	public boolean isRenderedPoligonalNotificacao(DadoConcedidoDTO dto) {
		return !Util.isNullOuVazio(dto.getListaPoligonalNotificacaoDTO());
	}
	
	public boolean isExibirPerguntaMaterialLenhoso() {
		return true;
	}
	
	public String getNomeRelatorio() {
		return "fce_florestal.jasper";
	}
	
	public void obterNovoNomePopular() {
		nomePopularEspecieSelecionada = new NomePopularEspecie();
	}
	
	public DocumentoObrigatorio getDocumentoObrigatorio() {
		return documentoObrigatorio;
	}

	public void setDocumentoObrigatorio(DocumentoObrigatorio documentoObrigatorio) {
		this.documentoObrigatorio = documentoObrigatorio;
	}

	public FceFlorestal getFceFlorestal() {
		return fceFlorestal;
	}

	public void setFceFlorestal(FceFlorestal fceFlorestal) {
		this.fceFlorestal = fceFlorestal;
	}

	public List<Produto> getListProduto() {
		return listProduto;
	}

	public void setListProduto(List<Produto> listProduto) {
		this.listProduto = listProduto;
	}

	public Produto getProdutoNativoSelecionado() {
		return produtoNativoSelecionado;
	}

	public void setProdutoNativoSelecionado(Produto produtoNativoSelecionado) {
		this.produtoNativoSelecionado = produtoNativoSelecionado;
	}

	public Boolean getIsLenha() {
		return isLenha;
	}

	public void setIsLenha(Boolean isLenha) {
		this.isLenha = isLenha;
	}

	public List<ProdutoSupressao> getProdutoFlorestalSelecionado() {
		return produtoFlorestalSelecionado;
	}

	public void setProdutoFlorestalSelecionado(
			List<ProdutoSupressao> produtoFlorestalSelecionado) {
		this.produtoFlorestalSelecionado = produtoFlorestalSelecionado;
	}

	public List<EspecieFlorestal> getEspecieFlorestalList() {
		return especieFlorestalList;
	}

	public void setEspecieFlorestalList(List<EspecieFlorestal> especieFlorestalList) {
		this.especieFlorestalList = especieFlorestalList;
	}

	public List<EspecieFlorestal> getEspecieFlorestalListAll() {
		return especieFlorestalListAll;
	}

	public void setEspecieFlorestalListAll(
			List<EspecieFlorestal> especieFlorestalListAll) {
		this.especieFlorestalListAll = especieFlorestalListAll;
	}

	public String getNomEspecieSupressao() {
		return nomEspecieSupressao;
	}

	public void setNomEspecieSupressao(String nomEspecieSupressao) {
		this.nomEspecieSupressao = nomEspecieSupressao;
	}

	public NomePopularEspecie getNomePopularEspecieSelecionada() {
		return nomePopularEspecieSelecionada;
	}

	public void setNomePopularEspecieSelecionada(
			NomePopularEspecie nomePopularEspecieSelecionada) {
		this.nomePopularEspecieSelecionada = nomePopularEspecieSelecionada;
	}

	public EspecieFlorestal getEspecieFlorestalSelecionada() {
		return especieFlorestalSelecionada;
	}

	public void setEspecieFlorestalSelecionada(
			EspecieFlorestal especieFlorestalSelecionada) {
		this.especieFlorestalSelecionada = especieFlorestalSelecionada;
	}

	public List<EspecieFlorestalAutorizacao> getEspecieFlorestalAutorizacaoList() {
		return especieFlorestalAutorizacaoList;
	}

	public void setEspecieFlorestalAutorizacaoList(
			List<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoList) {
		this.especieFlorestalAutorizacaoList = especieFlorestalAutorizacaoList;
	}

	public MetodoUtil getMetodoRetornoEspecie() {
		return metodoRetornoEspecie;
	}

	public void setMetodoRetornoEspecie(MetodoUtil metodoRetornoEspecie) {
		this.metodoRetornoEspecie = metodoRetornoEspecie;
	}

	public List<DestinoSocioeconomico> getListaDestinoSocioeconomico() {
		return listaDestinoSocioeconomico;
	}

	public void setListaDestinoSocioeconomico(
			List<DestinoSocioeconomico> listaDestinoSocioeconomico) {
		this.listaDestinoSocioeconomico = listaDestinoSocioeconomico;
	}

	public EspecieFlorestalAutorizacao getEspecieFlorestalAutorizacaoSelecionado() {
		return especieFlorestalAutorizacaoSelecionado;
	}

	public void setEspecieFlorestalAutorizacaoSelecionado(
			EspecieFlorestalAutorizacao especieFlorestalAutorizacaoSelecionado) {
		this.especieFlorestalAutorizacaoSelecionado = especieFlorestalAutorizacaoSelecionado;
	}

	public String getLabelAto() {
		return labelAto;
	}

	public void setLabelAto(String labelAto) {
		this.labelAto = labelAto;
	}

	public String getLabelAreaTotal() {
		return labelAreaTotal;
	}

	public void setLabelAreaTotal(String labelAreaTotal) {
		this.labelAreaTotal = labelAreaTotal;
	}

	public boolean isRenderedAreaUnidadeProducao() {
		return isRenderedAreaUnidadeProducao;
	}

	public void setRenderedAreaUnidadeProducao(boolean isRenderedAreaUnidadeProducao) {
		this.isRenderedAreaUnidadeProducao = isRenderedAreaUnidadeProducao;
	}

	public List<DadoConcedidoDTO> getListaDTO() {
		return listaDTO;
	}

	public void setListaDTO(List<DadoConcedidoDTO> listaDTO) {
		this.listaDTO = listaDTO;
	}
	
}