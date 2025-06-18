package br.gov.ba.seia.controller;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.jrimum.bopepo.view.BoletoViewer;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BiomaRequerimento;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.DadoBancario;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.BancoEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.facade.BiomaRequerimentoServiceFacade;
import br.gov.ba.seia.facade.ComprovanteBoletoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoHistoricoService;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.BoletoService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DadoBancarioService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.BoletoUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("comprovanteBoletoController")
@ViewScoped
public class ComprovanteBoletoController {


	@EJB
	private BoletoService boletoService;

	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;

	@EJB
	private BoletoDaeRequerimentoService boletoDaeRequerimentoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;

	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;

	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;

	@EJB
	private GerenciaArquivoService gerenciaArquivoservice;

	@EJB
	private DadoBancarioService dadoBancarioService;

	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private ComprovanteBoletoServiceFacade comprovanteBoletoServiceFacade;
	
	@EJB
	private BiomaRequerimentoServiceFacade biomaRequerimentoServiceFacade;
	
	@EJB
	private BoletoPagamentoHistoricoService boletoPagamentoHistoricoService;
	
	@EJB
	private DeclaracaoInexigibilidadeService declaracaoInexigibilidadeService;
	
	private ComprovantePagamento comprovanteBoleto;

	private ComprovantePagamentoDae comprovanteVistoria;
	
	private ComprovantePagamentoDae comprovanteCertificado;

	private BoletoPagamentoRequerimento boleto;

	private BoletoDaeRequerimento vistoria;
	
	private BoletoDaeRequerimento certificado;
	
	private Requerimento requerimento;

	private TramitacaoRequerimento tramitacaoRequerimento;
	
	private Collection<BiomaRequerimento> listaBiomaRequerimentoDetalhamento;
	
	private Double valorTotalAreaBioma;
	
	private boolean isApe;

	
	@PostConstruct
	public void init() {
	}

	public void load(Requerimento requerimento) {
		try {
			Integer ideRequerimento = requerimento.getIdeRequerimento();
			this.requerimento = requerimentoService.carregarDadosBasicos(ideRequerimento);
			carregarListaBiomaRequerimentoDetalhamento(requerimento);
			carregarBoleto(ideRequerimento);
			carregarVistoria(ideRequerimento);
			carregarCertificado(ideRequerimento);
			
			for (AtoAmbiental atoAmbiental : requerimento.getAtosAmbientais()) {
				if(atoAmbiental.isAPE()){
					setApe(atoAmbiental.isAPE());
					break;
				}
				
			} 
			
			tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(ideRequerimento);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	private void carregarListaBiomaRequerimentoDetalhamento(Requerimento requerimento) throws Exception {
		listaBiomaRequerimentoDetalhamento = biomaRequerimentoServiceFacade.carregarListaBiomaRequerimentoDetalhamento(requerimento);
		if(!Util.isNullOuVazio(listaBiomaRequerimentoDetalhamento)) {
			valorTotalAreaBioma = biomaRequerimentoServiceFacade.calcularValorTotalAreaBioma(listaBiomaRequerimentoDetalhamento);
		}
	}
	
	public boolean isRenderedPnlDetalhamentoBioma() {
		return !Util.isNullOuVazio(listaBiomaRequerimentoDetalhamento) && !Util.isNullOuVazio(vistoria) && !Util.isNullOuVazio(certificado);
	}
	
	public BiomaRequerimento getBiomaPredominante() {
		
		if(!Util.isNullOuVazio(listaBiomaRequerimentoDetalhamento)) {
			
			BiomaRequerimento predominante = null;
			
			for(BiomaRequerimento br : listaBiomaRequerimentoDetalhamento) {
				if(predominante == null || (predominante != null && predominante.getValArea() < br.getValArea())) {
					predominante = br;
				}
			}
			
			return predominante;
		}
		
		return null;
	}

	private void carregarBoleto(Integer ideRequerimento) {
		
		this.boleto = this.boletoService.carregarByRequerimento(ideRequerimento);
		if(boleto != null && boleto.getIdeBoletoPagamentoRequerimento() != null){
			this.setComprovanteBoleto(comprovantePagamentoService.consultarPorIdBoletoPagamentoRequerimento(boleto.getIdeBoletoPagamentoRequerimento()));
		}
		
		if (!Util.isNull(getComprovanteBoleto()) && getComprovanteBoleto().getIdeComprovantePagamento()!=null && this.boleto != null) {
			this.boleto.setPathComprovante(getComprovanteBoleto().getDscCaminhoArquivo());
		}
	}

	private void carregarVistoria(Integer ideRequerimento) {
		
		this.vistoria = this.boletoDaeRequerimentoService.carregarVistoriaByRequerimento(ideRequerimento);
		
		if (!Util.isNull(vistoria)) {
			this.setComprovanteVistoria(this.comprovantePagamentoDaeService.carregarVistoriaByIdeRequerimento(ideRequerimento, vistoria.getIdeBoletoDaeRequerimento()));
		
			if (!Util.isNull(getComprovanteVistoria())) {
				this.vistoria.setPathComprovante(getComprovanteVistoria().getDscCaminhoArquivo());
			}
		}
	}

	private void carregarCertificado(Integer ideRequerimento) {
		
		this.certificado = this.boletoDaeRequerimentoService.carregarCertificadoByRequerimento(ideRequerimento);
		
		if (!Util.isNull(certificado)) {
			this.setComprovanteCertificado(this.comprovantePagamentoDaeService.carregarCertificadoByIdeRequerimento(ideRequerimento, certificado.getIdeBoletoDaeRequerimento()));
		
			if (!Util.isNull(getComprovanteCertificado())) {
				this.certificado.setPathComprovante(getComprovanteCertificado().getDscCaminhoArquivo());
			}
		}
	}

	public void uploadBoleto(FileUploadEvent event) {
		try {
			String path = this.gerenciaArquivoservice.uploadArquivoComprovanteBoleto(event.getFile(), this.boleto);
			boleto.setPathComprovante(path);
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void uploadCertificado(FileUploadEvent event) {
		try {
			String path = this.gerenciaArquivoservice.uploadArquivoComprovanteBoletoDae(event.getFile(), this.certificado, "certificado_dae");
			this.certificado.setPathComprovante(path);
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void uploadVistoria(FileUploadEvent event) {
		
		try {
			String path = this.gerenciaArquivoservice.uploadArquivoComprovanteBoletoDae(event.getFile(), this.vistoria, "vistoria_dae");
			this.vistoria.setPathComprovante(path);
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void salvarComprovantes() {
		try{
			comprovanteBoletoServiceFacade.salvarComprovantes(this);
		}
		catch(SeiaTramitacaoRequerimentoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public ComprovantePagamento gerarComprovantePagamentoBoleto()  {
		ComprovantePagamento comprovantePagamento = new ComprovantePagamento();
		comprovantePagamento.setDtcValidacao(new Date());
		comprovantePagamento.setIdePessoaUpload(this.getOperador());
		comprovantePagamento.setIdeBoletoPagamentoRequerimento(this.boleto);
		comprovantePagamento.setDscCaminhoArquivo(this.boleto.getPathComprovante());
		comprovantePagamento.setIndComprovanteValidado(false);
		return comprovantePagamento;
	}

	public ComprovantePagamentoDae gerarComprovanteDAE(BoletoDaeRequerimento boletoDaeRequerimento) {
		ComprovantePagamentoDae comprovantePagamentoDae = new ComprovantePagamentoDae();
		comprovantePagamentoDae.setIdeBoletoDaeRequerimento(boletoDaeRequerimento);
		comprovantePagamentoDae.setDscCaminhoArquivo(boletoDaeRequerimento.getPathComprovante());
		comprovantePagamentoDae.setIdePessoaUpload(getOperador());
		comprovantePagamentoDae.setDtcValidacao(new Date());
		return comprovantePagamentoDae;
	}

	private Pessoa getOperador() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}

	public StreamedContent getFileDownloadBoleto() {
		
		if(this.boleto.getIndBoletoGeradoManualmente() != null && this.boleto.getIndBoletoGeradoManualmente()){
			return getFileDownload(this.boleto.getDesCaminhoBoleto()); //#9243
		}
		
		try {
			
			BigDecimal valorLicencas = new BigDecimal(0);
			BigDecimal valorOutorgas = new BigDecimal(0);

			DadoBancario dadoBancario = this.dadoBancarioService.carregarBancoBoleto(BancoEnum.BANCO_DO_BRASIL);
			Pessoa requerente = this.pessoaService.carregarDadosRequerente(this.requerimento.getIdeRequerimento(), null);
			
			List<AtoAmbiental> listAtosDeOutorga = (List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalPorTipoAto(new TipoAto(TipoAtoEnum.AUTORIZACAO.getId()), true);
			listAtosDeOutorga.addAll((List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalPorTipoAto(new TipoAto(TipoAtoEnum.OUTORGA.getId()), false));
			
			if(Util.isLazyInitExcepOuNull(this.boleto.getDetalhamentoBoletoCollection())){
				this.boleto.setDetalhamentoBoletoCollection(boletoPagamentoRequerimentoService.carregarDetalhamentosDoBoleto(this.boleto.getIdeBoletoPagamentoRequerimento()));
			}
			
			for (DetalhamentoBoleto detalheBoleto : this.boleto.getDetalhamentoBoletoCollection()) {
				if (listAtosDeOutorga.contains(detalheBoleto.getIdeAtoAmbiental())){
					valorOutorgas = valorOutorgas.add(detalheBoleto.getValor()); 
				} else {
					valorLicencas = valorLicencas.add(detalheBoleto.getValor());
				}
			}
			
			if(valorOutorgas.add(valorLicencas) == new BigDecimal(0)) {
				if(requerimentoService.isRequerimentoAntigo(this.requerimento.getIdeRequerimento())) {
					if (!Util.isNull(this.boleto.getValBoletoOutorga())) {
						valorOutorgas = this.boleto.getValBoletoOutorga();
					}
					
					valorLicencas = this.boleto.getValBoleto();
				}
			}
			
			DeclaracaoInexigibilidade declaracaoInexigibilidade = this.declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(requerimento);
			if(declaracaoInexigibilidade != null){
				
				dadoBancario.setDscInstrucao3(
						" Valor referente a declaração de inexigibilidade (R$" 
								+ Util.getDecimalFormatPtBr().format(valorLicencas)+")");
				
				dadoBancario.setDscInstrucao4("feita através do requerimento "+ requerimento.getNumRequerimento() +"." );
				
			}else{
				
				dadoBancario.setDscInstrucao3(
						" Valor referente a atos de licenciamento (R$" 
								+ Util.getDecimalFormatPtBr().format(valorLicencas)+")"
								+ " e outorga (R$" 
								+ Util.getDecimalFormatPtBr().format(valorOutorgas)+")");
				
				dadoBancario.setDscInstrucao4("solicitados através do requerimento "+ requerimento.getNumRequerimento() +"." );
			}
			
//			BoletoViewer boletoViewer = BoletoUtil.gerarBoleto(this.boleto, requerente, dadoBancario);
//			return new DefaultStreamedContent(new ByteArrayInputStream(boletoViewer.getPdfAsByteArray()), "", "boleto" + new Date().getTime()+".pdf");
			
			GeradorDeBoleto geradorBoleto = BoletoUtil.geradorDeBoleto(this.boleto, requerente, dadoBancario);
			return new DefaultStreamedContent(new ByteArrayInputStream(geradorBoleto.geraPDF()), "", "boleto" + new Date().getTime()+".pdf");

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Boolean isPerfuracaoPocoEnquadramentoAto(){
		try {
			Collection<EnquadramentoAtoAmbiental> enquadramentoAtosAmbientaisDoReq = this.enquadramentoService.listarEnquadramentoAtoByRequerimento(requerimento.getIdeRequerimento());
			for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoAtosAmbientaisDoReq) {
				if(enquadramentoAtoAmbiental.getAtoAmbiental().isPerfuracaoDePoco()){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}

	public StreamedContent getFileDownload(String caminhoArquivo) {
		try {
			return gerenciaArquivoservice.getContentFile(caminhoArquivo);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean isRenderedDownloadComprovante() {
		return !Util.isNullOuVazio(boleto.getPathComprovante()) && (boleto.getIndBoletoGeradoManualmente() || !boleto.getIndBoletoRegistrado());
	}
	
	public boolean isRenderedUploadBoleto() {
		return Util.isNullOuVazio(boleto.getPathComprovante()) && (boleto.getIndBoletoGeradoManualmente() || !boleto.getIndBoletoRegistrado());
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public BoletoPagamentoRequerimento getBoleto() {
		return boleto;
	}

	public void setBoleto(BoletoPagamentoRequerimento boleto) {
		this.boleto = boleto;
	}

	public BoletoDaeRequerimento getVistoria() {
		return vistoria;
	}

	public void setVistoria(BoletoDaeRequerimento vistoria) {
		this.vistoria = vistoria;
	}

	public BoletoDaeRequerimento getCertificado() {
		return certificado;
	}

	public void setCertificado(BoletoDaeRequerimento certificado) {
		this.certificado = certificado;
	}

	public TramitacaoRequerimento getTramitacaoRequerimento() {
		return tramitacaoRequerimento;
	}

	public void setTramitacaoRequerimento(TramitacaoRequerimento tramitacaoRequerimento) {
		this.tramitacaoRequerimento = tramitacaoRequerimento;
	}

	public ComprovantePagamento getComprovanteBoleto() {
		return comprovanteBoleto;
	}

	public void setComprovanteBoleto(ComprovantePagamento comprovanteBoleto) {
		this.comprovanteBoleto = comprovanteBoleto;
	}

	public ComprovantePagamentoDae getComprovanteCertificado() {
		return comprovanteCertificado;
	}

	public void setComprovanteCertificado(ComprovantePagamentoDae comprovanteCertificado) {
		this.comprovanteCertificado = comprovanteCertificado;
	}

	public ComprovantePagamentoDae getComprovanteVistoria() {
		return comprovanteVistoria;
	}

	public void setComprovanteVistoria(ComprovantePagamentoDae comprovanteVistoria) {
		this.comprovanteVistoria = comprovanteVistoria;
	}

	public Collection<BiomaRequerimento> getListaBiomaRequerimentoDetalhamento() {
		return listaBiomaRequerimentoDetalhamento;
	}

	public void setListaBiomaRequerimentoDetalhamento(
			Collection<BiomaRequerimento> listaBiomaRequerimentoDetalhamento) {
		this.listaBiomaRequerimentoDetalhamento = listaBiomaRequerimentoDetalhamento;
	}

	public Double getValorTotalAreaBioma() {
		return valorTotalAreaBioma;
	}

	public void setValorTotalAreaBioma(Double valorTotalAreaBioma) {
		this.valorTotalAreaBioma = valorTotalAreaBioma;
	}

	public boolean isApe() {
		return isApe;
	}

	public void setApe(boolean isApe) {
		this.isApe = isApe;
	}

}
