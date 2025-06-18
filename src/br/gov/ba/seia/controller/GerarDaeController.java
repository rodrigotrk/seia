package br.gov.ba.seia.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.DetalhamentoDaeDTO;
import br.gov.ba.seia.dto.GerarDaeDTO;
import br.gov.ba.seia.dto.ParcelaDaeDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ConsumidorReposicaoFlorestal;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.DeclaracaoParcialDae;
import br.gov.ba.seia.entity.DetentorReposicaoFlorestal;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.IndiceCobrancaEnum;
import br.gov.ba.seia.enumerator.PagamentoReposicaoFlorestalEnum;
import br.gov.ba.seia.enumerator.SituacaoDaeEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ConsumidorReposicaoFlorestalService;
import br.gov.ba.seia.service.CumprimentoReposicaoFlorestalService;
import br.gov.ba.seia.service.DeclaracaoParcialDaeService;
import br.gov.ba.seia.service.DetentorReposicaoFlorestalService;
import br.gov.ba.seia.service.DevedorReposicaoFlorestalService;
import br.gov.ba.seia.service.GerarDaeService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TaxaIndiceCobrancaService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.DaeUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SefazUtil;
import br.gov.ba.seia.util.Uri;
import br.gov.ba.seia.util.Util;

@Named("gerarDaeController")
@ViewScoped
public class GerarDaeController {

	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private GerarDaeService gerarDaeService;	
	
	private Requerimento requerimento;
	
	private GerarDaeDTO gerarDaeDTO;
	
	@EJB
	private TaxaIndiceCobrancaService taxaIndiceCobrancaService;

	@EJB
	private DetentorReposicaoFlorestalService detentorReposicaoFlorestalService;
	
	@EJB
	private ConsumidorReposicaoFlorestalService consumidorReposicaoFlorestalService;
	
	@EJB
	private DevedorReposicaoFlorestalService devedorReposicaoFlorestalService;
	
	@EJB
	private DeclaracaoParcialDaeService declaracaoParcialDaeService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private CumprimentoReposicaoFlorestalService cumprimentoReposicaoFlorestalService;
	
	@Inject
	private CertificadoUtil certificadoUtil;
	
	private boolean exibirMsgNovoDae;
	
	@PostConstruct
	public void init() {
		limpar();
	}
	
	public void load(Requerimento req) {
		try {
			limpar();
			gerarDaeDTO.setRequerimento(this.requerimentoService.carregarDadosBasicos(req.getIdeRequerimento()));
			carregarFormulario();
			carregarDados(); 
			verificarGeracaoDae(gerarDaeDTO.getParcelaDaeDTO());
			verificarGeracaoDae(gerarDaeDTO.getParcelaUnica());
			verificarParcelaDaeVencido();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void limpar() {
		gerarDaeDTO = new GerarDaeDTO();
	}
	
	private void carregarFormulario() throws Exception {
		gerarDaeService.carregarFormulario(gerarDaeDTO);
	}
	
	private void carregarDados() throws Exception {
		gerarDaeService.carregarDados(gerarDaeDTO);
	}
	
	public void gerarParcelas() {
		try {
			
			if(gerarDaeDTO.isIndAlterarVolume() && validarAlterarVolume() && podeGerarDae()){
				prepararAlterarVolumeReferencia();
				prepararGerarDae();
				gerarDaeDTO.setGerarDae(false);
			}else if(!gerarDaeDTO.isIndAlterarVolume()){
				prepararGerarDae();
			}
			
		} catch (AppExceptionError aee) {
			MensagemUtil.erro(aee.getMessage());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void gerarParcelaDae(ParcelaDaeDTO parcelaDaeDTO) {
		try {
			gerarDaeService.gerarParcelaDae(gerarDaeDTO, parcelaDaeDTO);
			verificarGeracaoDae(gerarDaeDTO.getParcelaDaeDTO());
			verificarGeracaoDae(gerarDaeDTO.getParcelaUnica());
			load(gerarDaeDTO.getRequerimento());
			Html.atualizar("fromDae");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isIGMPMesAnterior() throws Exception {
		return !Util.isNullOuVazio(gerarDaeService.obterIGPMMesAnterior(new Date()));
	}
	
	public void verificarParcelaDaeVencido() throws Exception {
		
		ParcelaDaeDTO vencido = gerarDaeService.verificarParcelaDaeVencido(gerarDaeDTO.getParcelaDaeDTO());
		
		if(Util.isNullOuVazio(vencido) || !Util.isNullOuVazio(gerarDaeDTO.getParcelaUnica())) {
			vencido = gerarDaeService.verificarParcelaDaeVencido(gerarDaeDTO.getParcelaUnica());
		}/* else {
			vencido = gerarDaeService.verificarParcelaDaeVencido(gerarDaeDTO.getParcelaUnica());
		}*/

		gerarDaeDTO.setDaeVencido(vencido);
	}
	
	public void verificarGeracaoDae(List<ParcelaDaeDTO> listparcelaDaeDto){
		try {	
			if(!Util.isNullOuVazio(listparcelaDaeDto)){
					
				for(ParcelaDaeDTO parcelaDaeDtoAtual : listparcelaDaeDto){
					
					Integer posicaoItem = listparcelaDaeDto.indexOf(parcelaDaeDtoAtual);
					
					if(posicaoItem > 0){
			
						ParcelaDaeDTO parcelaAnterior = listparcelaDaeDto.get(posicaoItem-1);
						
									
						if(!Util.isNullOuVazio(parcelaAnterior.getSituacaoDae())){
							
							
						    if(parcelaAnterior.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.PAGO.getId()) && Util.isNullOuVazio(parcelaDaeDtoAtual.getSituacaoDae()) ||
						    		parcelaAnterior.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.VENCIDO.getId()) && Util.isNullOuVazio(parcelaDaeDtoAtual.getSituacaoDae())){
								
							
								
								Integer mesReferencia = parcelaAnterior.getVencimento().getMonth();
								SimpleDateFormat ano = new SimpleDateFormat("yyyy");
								Integer anoReferencia = Integer.parseInt(ano.format(parcelaAnterior.getVencimento()));
								if(mesReferencia == 0){
									mesReferencia = 12;
									anoReferencia = anoReferencia -1;
								}
								Date vencimentoProximoDate = Util.adicionarMeses(parcelaAnterior.getVencimento(), 1);
								//verifica se a segunda data é superior a primeira
								if(Util.validarDuasDatasReposicao(vencimentoProximoDate, new Date())) {
									parcelaDaeDtoAtual.setSituacaoDae(new SituacaoDae(SituacaoDaeEnum.VENCIDO.getId()));
									parcelaDaeDtoAtual.setDataDaeVencido(vencimentoProximoDate);
									parcelaDaeDtoAtual.setDataDaeAnteriorPago(parcelaAnterior.getVencimento());
								}
								if(!Util.isNullOuVazio(taxaIndiceCobrancaService.obterTaxaporIndiceCobrancaEMes(IndiceCobrancaEnum.IGPM.getId(), mesReferencia, anoReferencia))){
									parcelaDaeDtoAtual.setGerarDae(true);
									parcelaDaeDtoAtual.setBoleto(false);
									parcelaDaeDtoAtual.setCalendario(false);
									parcelaDaeDtoAtual.setPago(false);
								}else{
									parcelaDaeDtoAtual.setGerarDae(false);
									parcelaDaeDtoAtual.setBoleto(false);
									parcelaDaeDtoAtual.setCalendario(true);
									parcelaDaeDtoAtual.setPago(false);
								}
							}else if(parcelaAnterior.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.PAGO.getId()) && parcelaDaeDtoAtual.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.PAGO.getId())){
								parcelaDaeDtoAtual.setGerarDae(false);
								parcelaDaeDtoAtual.setBoleto(false);
								parcelaDaeDtoAtual.setCalendario(false);
								parcelaDaeDtoAtual.setPago(true);
							}else if(parcelaAnterior.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.PAGO.getId()) && parcelaDaeDtoAtual.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.EM_ABERTO.getId())){
								parcelaDaeDtoAtual.setGerarDae(false);
								parcelaDaeDtoAtual.setBoleto(true);
								parcelaDaeDtoAtual.setCalendario(false);
								parcelaDaeDtoAtual.setPago(false);
							}else if(parcelaAnterior.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.EM_ABERTO.getId()) && Util.isNullOuVazio(parcelaDaeDtoAtual.getSituacaoDae())){
								parcelaDaeDtoAtual.setGerarDae(false);
								parcelaDaeDtoAtual.setBoleto(false);
								parcelaDaeDtoAtual.setCalendario(true);
								parcelaDaeDtoAtual.setPago(false);
							}else if(parcelaAnterior.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.VENCIDO.getId()) && (!Util.isNullOuVazio(parcelaDaeDtoAtual.getSituacaoDae()) && parcelaDaeDtoAtual.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.EM_ABERTO.getId()))) {
								parcelaDaeDtoAtual.setGerarDae(false);
								parcelaDaeDtoAtual.setBoleto(true);
								parcelaDaeDtoAtual.setCalendario(false);
								parcelaDaeDtoAtual.setPago(false);
							}else if(parcelaAnterior.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.VENCIDO.getId()) && Util.isNullOuVazio(parcelaDaeDtoAtual.getSituacaoDae())) {
								parcelaDaeDtoAtual.setGerarDae(false);
								parcelaDaeDtoAtual.setBoleto(false);
								parcelaDaeDtoAtual.setCalendario(true);
								parcelaDaeDtoAtual.setPago(false);
							}	
						}else if(Util.isNullOuVazio(parcelaAnterior.getSituacaoDae()) && Util.isNullOuVazio(parcelaDaeDtoAtual.getSituacaoDae())){
							parcelaDaeDtoAtual.setGerarDae(false);
							parcelaDaeDtoAtual.setBoleto(false);
							parcelaDaeDtoAtual.setCalendario(true);
							parcelaDaeDtoAtual.setPago(false);
						}
					}else if(parcelaDaeDtoAtual.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.PAGO.getId())){
						parcelaDaeDtoAtual.setGerarDae(false);
						parcelaDaeDtoAtual.setBoleto(false);
						parcelaDaeDtoAtual.setCalendario(false);
						parcelaDaeDtoAtual.setPago(true);
					}else if(parcelaDaeDtoAtual.getSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.EM_ABERTO.getId())){
						parcelaDaeDtoAtual.setGerarDae(false);
						parcelaDaeDtoAtual.setBoleto(true);
						parcelaDaeDtoAtual.setCalendario(false);
						parcelaDaeDtoAtual.setPago(false);
					}
				}	
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	
	public StreamedContent getImprimirDeclaracaoParcial(ParcelaDaeDTO parcelaDto) {
		Exception  erro = null;
		try {
			MemoriaCalculoDaeParcela parcela = new MemoriaCalculoDaeParcela();
			parcela.setIdeMemoriaCalculoDaeParcela(parcelaDto.getIdeMemoriaCalculoDaeParcela());
			if (!this.declaracaoParcialDaeService.exists(gerarDaeDTO.getRequerimento().getIdeRequerimento())) {
				DeclaracaoParcialDae certificado =  this.gerarCertificadoDeclaracao(gerarDaeDTO.getRequerimento().getIdeRequerimento());
				//String numeroCertificado = this.declaracaoParcialDaeService.gerarNumeroCertificado(certificado);
				//certificado.setNumDeclaracaoParcialDae(numeroCertificado);
				certificado.setIdeMemoriaCalculoDaeParcela(parcela);
				this.declaracaoParcialDaeService.salvarCertificado(certificado);
			}if(!this.declaracaoParcialDaeService.hasToken(gerarDaeDTO.getRequerimento().getIdeRequerimento(), AtoAmbientalEnum.CRF)){
				DeclaracaoParcialDae certificado = this.declaracaoParcialDaeService.carregarCertificado(gerarDaeDTO.getRequerimento().getIdeRequerimento(), AtoAmbientalEnum.CRF);
				certificado.setIdeMemoriaCalculoDaeParcela(parcela);
				this.declaracaoParcialDaeService.atualizarTokenCertificado(certificado);
			}

			return this.imprimirQuitacao(parcelaDto.getNumParcela(), parcelaDto.getValorDae(), parcelaDto.getIdeMemoriaCalculoDaeParcela());
		} catch (Exception e) {
			erro =null;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	private DeclaracaoParcialDae gerarCertificadoDeclaracao(Integer ideRequerimento) throws Exception {
		AtoAmbiental atoAmbiental = getAtoAmbientalCRF();
		Requerimento requerimento = requerimentoService.buscarEntidadePorId(new Requerimento(ideRequerimento));
		
		return gerarCertificado(atoAmbiental, requerimento);
	}
	
	
	private DeclaracaoParcialDae gerarCertificado(AtoAmbiental atoAmbiental, Requerimento requerimento) {
		try {
			return certificadoUtil.gerarDeclaracaoParcialDae(atoAmbiental, requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	protected AtoAmbiental getAtoAmbientalCRF() {
		AtoAmbiental atoAmbiental = new AtoAmbiental(AtoAmbientalEnum.CRF.getId(), AtoAmbientalEnum.CRF.name());
		return atoAmbiental;
	}
	
	private StreamedContent imprimirQuitacao(Integer parcelaReferencia, Double valorDae, Integer ideMemoriaCalculoParcela) throws Exception {
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("SUBREPORT_DIR", retornaCaminhoDeclaracaoReposicaoFlorestal());
		lParametros.put("NOME_RELATORIO", obterModeloDeclaracao(gerarDaeDTO.getRequerimento()));
		lParametros.put("ide_requerimento", gerarDaeDTO.getRequerimento().getIdeRequerimento());
		lParametros.put("parcela_referencia", parcelaReferencia);
		lParametros.put("valor_parcela", valorDae);
		lParametros.put("valor_parcela_extenso", Util.valorEmReaisParaExtenso(new BigDecimal(valorDae)));
		lParametros.put("ide_memoria_calculo_parcela", ideMemoriaCalculoParcela);
		
		return new RelatorioUtil(lParametros).gerar();
	}
	
	public static String retornaCaminhoDeclaracaoReposicaoFlorestal() {	
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		return sessao.getServletContext().getRealPath(File.separator + Uri.URL_DECLARACAO_REPOSICAO_FLORESTAL) + File.separator;
	}
	
	public String obterModeloDeclaracao(Requerimento requerimento){
		String modeloDeclaracao = "";
		try {
			DetentorReposicaoFlorestal detentor = detentorReposicaoFlorestalService.obterDetentorReposicaoFlorestalPorRequerimento(requerimento.getIdeRequerimento());
			ConsumidorReposicaoFlorestal consumidor = consumidorReposicaoFlorestalService.obterConsumidorReposicaoFlorestalPorRequerimento(requerimento.getIdeRequerimento());
			DevedorReposicaoFlorestal devedor = devedorReposicaoFlorestalService.obterDevedorPorRequerimento(requerimento.getIdeRequerimento());
			
			if(!Util.isNullOuVazio(detentor) && Util.isNullOuVazio(consumidor) && Util.isNullOuVazio(devedor)){
				modeloDeclaracao = "declaracao_parcial_quitacao_modelo01.jasper";
			}else if(Util.isNullOuVazio(detentor) && !Util.isNullOuVazio(consumidor) && Util.isNullOuVazio(devedor)){
				modeloDeclaracao = "declaracao_parcial_quitacao_modelo02.jasper";
			}else if(Util.isNullOuVazio(detentor) && Util.isNullOuVazio(consumidor) && !Util.isNullOuVazio(devedor)){
				modeloDeclaracao = "declaracao_parcial_quitacao_modelo03.jasper";
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return modeloDeclaracao;
	}
	
	public void recalcular(){
		if(validarAlterarVolume()){
			
			Html.exibir("recalcular");
		}	
		
	}
	
	private void adicionarParametros(List<DetalhamentoDaeDTO> listaDetalhamentoDaeDTO) throws Exception{
			
		cumprimentoReposicaoFlorestalService.recalcular(listaDetalhamentoDaeDTO, gerarDaeDTO);
		Html.atualizar("fromDae");
	}
	
	public boolean isRenderedAlterarVolume(){
		
		if(!Util.isNullOuVazio(gerarDaeDTO.getListaDetalhamentoDaeDTO())){
			for(DetalhamentoDaeDTO detalhamentoDaeDTO: gerarDaeDTO.getListaDetalhamentoDaeDTO()){
				if(!Util.isNullOuVazio(detalhamentoDaeDTO.getPagamentoReposicaoFlorestal()) && PagamentoReposicaoFlorestalEnum.DEVEDOR.getId().equals(detalhamentoDaeDTO.getPagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal())){
					return true;
				}
			}
		}
		return false;
	}
	
	public void uploadBoleto(FileUploadEvent event) {
		try {
			String path = this.gerenciaArquivoService.uploadParecerTecnico(event.getFile(), gerarDaeDTO.getRequerimento());
			gerarDaeDTO.setDscCaminhoAqruivoParecerTecnico(path);
			
			String fileName = event.getFile().getFileName();
			
			if(fileName.length() > 250){
				fileName = fileName.substring(0, 250); 	
			}
			
			gerarDaeDTO.setFileName(fileName);
			
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void excluirArquivo(){
		gerenciaArquivoService.deletarArquivo(gerarDaeDTO.getDscCaminhoAqruivoParecerTecnico());
		
		gerarDaeDTO.setFileName(null);
		gerarDaeDTO.setDscCaminhoAqruivoParecerTecnico(null);
		
		JsfUtil.addSuccessMessage("Exclusão realizada com sucesso!");
	}
	
	public StreamedContent getFileDownload(String caminhoArquivo) {
		try {
			return gerenciaArquivoService.getContentFile(caminhoArquivo);
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean validarAlterarVolume(){
		boolean retorno = true;
			
		if(Util.isNullOuVazio(gerarDaeDTO.getValVolumeReferencia()) || gerarDaeDTO.getValVolumeReferencia().compareTo(BigDecimal.ZERO) == 0){
			JsfUtil.addWarnMessage("O campo Deseja alterar o volume de referência utilizado no cálculo é de preenchimento obrigatório e deve possuir valor maior que 0,00!");
			retorno = false;
		}
		
		if(Util.isNullOuVazio(gerarDaeDTO.getDscCaminhoAqruivoParecerTecnico())){
			JsfUtil.addWarnMessage("O campo Upload do parecer técnico é de preenchimento obrigatório!");
			retorno = false;
		}
		
		return retorno;
	}
	
	public void prepararGerarDae() throws Exception{
		
		gerarDaeService.gerarParcelas(gerarDaeDTO);
		verificarGeracaoDae(gerarDaeDTO.getParcelaDaeDTO());
		verificarGeracaoDae(gerarDaeDTO.getParcelaUnica());
		verificarParcelaDaeVencido();
		Html.atualizar("fromDae");
	}
	
	public void prepararAlterarVolumeReferencia() throws Exception{
		DevedorReposicaoFlorestal devedorReposicaoFlorestal = devedorReposicaoFlorestalService.obterDevedorPorRequerimento(gerarDaeDTO.getRequerimento().getIdeRequerimento());
		devedorReposicaoFlorestal.setValVolumeReferencia(gerarDaeDTO.getValVolumeReferencia());
		devedorReposicaoFlorestal.setDscCaminhoParecerTecnico(gerarDaeDTO.getDscCaminhoAqruivoParecerTecnico());
		devedorReposicaoFlorestal.setDtcGravado(new Date());
		
		CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestalService.obterCumprimentoReposicaoFlorestalPorRequerimento(gerarDaeDTO.getRequerimento());
		cumprimentoReposicaoFlorestal.setVlrPecuniario(new BigDecimal(gerarDaeDTO.getValorPecuniario()));
		devedorReposicaoFlorestal.setIdeCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal);
		cumprimentoReposicaoFlorestalService.salvarOuAtualizar(cumprimentoReposicaoFlorestal);
		devedorReposicaoFlorestalService.salvarDevedorReposicaoFlorestal(devedorReposicaoFlorestal);
	}
	
	public boolean isDesabilitarBotaoRecalcular(){
		
		if(!Util.isNullOuVazio(gerarDaeDTO.getValVolumeReferencia()) && !Util.isNullOuVazio(gerarDaeDTO.getDscCaminhoAqruivoParecerTecnico())){
			return false;
		}
		
		return true;
	}
	
	public void gerarNovoCalculo(){
		List<DetalhamentoDaeDTO> listaDetalhamentoDaeDTO  = gerarDaeDTO.getListaDetalhamentoDaeDTO();
		
		try {
			adicionarParametros(listaDetalhamentoDaeDTO);
			gerarDaeDTO.setGerarDae(true);
			JsfUtil.addSuccessMessage("Cálculo realizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			JsfUtil.addErrorMessage("Erro ao gerar calculo!");
		}
	}
	
	private boolean podeGerarDae(){
		
		if(!gerarDaeDTO.isGerarDae()){
			JsfUtil.addWarnMessage("É necessário recalcular antes de gerar o DAE!");
			return false;
		}
		
		return true;
	}
	
	public void resetGerarDae(){
		
		gerarDaeDTO.setGerarDae(false);
	}
	
	public void limparCamposVolumeReferencia(){
		
		if(!gerarDaeDTO.isIndAlterarVolume()){
			gerarDaeDTO.setValVolumeReferencia(null);
			gerarDaeDTO.setDscCaminhoAqruivoParecerTecnico(null);
			gerarDaeDTO.setFileName(null);
		}
	}
	
	public String gerarLinkDownloadDae(String url) {
		return DaeUtil.gerarLinkDownloadDae(url);
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public GerarDaeDTO getGerarDaeDTO() {
		return gerarDaeDTO;
	}

	public void setGerarDaeDTO(GerarDaeDTO gerarDaeDTO) {
		this.gerarDaeDTO = gerarDaeDTO;
	}

	/**
	 * Verifica se a geração de DAE deve ser desabilitada
	 * @return true se deve desabilitar
	 */
	public boolean isDesabilitarGerarDAE() {
		
		if (gerarDaeDTO != null 
				&& (gerarDaeDTO.getParcelaUnica() != null && !Util.isNullOuVazio(gerarDaeDTO.getParcelaUnica()))
				|| (gerarDaeDTO.getParcelaDaeDTO() != null && !Util.isNullOuVazio(gerarDaeDTO.getParcelaDaeDTO()))) {
			return true;
			
		} else {
			return false;
		} 
	}

	public boolean isExibirMsgNovoDae() {
		return exibirMsgNovoDae;

	}

	public void setExibirMsgNovoDae(boolean exibirMsgNovoDae) {
		this.exibirMsgNovoDae = exibirMsgNovoDae;
	}
	
	
}
