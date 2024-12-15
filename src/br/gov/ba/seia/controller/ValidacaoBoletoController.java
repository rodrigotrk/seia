package br.gov.ba.seia.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.jrimum.bopepo.view.BoletoViewer;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.DadoBancario;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.BancoEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.facade.ValidacaoBoletoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DadoBancarioService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.BoletoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("validacaoBoletoController")
@ViewScoped
public class ValidacaoBoletoController extends BaseValidacaoController{
	
	@EJB
	private ValidacaoBoletoServiceFacade validacaoBoletoServiceFacade;
	
	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;
	
	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
	
	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentRequerimentoService;
	
	@EJB
	private DadoBancarioService dadoBancarioService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoservice;

	@EJB
	private DeclaracaoInexigibilidadeService declaracaoInexigibilidadeService;
	
	private Requerimento requerimento;
	
	private Empreendimento empreendimento;
	
	private Collection<ComprovantePagamento> comprovantesPagamento;

	private Collection<ComprovantePagamentoDae> comprovantesDAE;

	private String email;
	
	private String emailValidacao;

	private String tituloEmail;
	
	private StatusRequerimento statusAtual;

	private TramitacaoRequerimento tramitacaoRequerimento;
	
	private List<BoletoPagamentoRequerimento> boletosPagamentoRequerimento;

	private boolean existeDeclaracaoInexigibilidade;
	
	@PostConstruct
	public void init() {
	}

	public void load(Requerimento requerimento) {

		try {

			Integer ideRequerimento = requerimento.getIdeRequerimento();
			this.requerimento = this.requerimentoService.carregarDadosBasicos(ideRequerimento);
			
			this.comprovantesPagamento = this.comprovantePagamentoService.listarByIdeRequerimentoHistoricoNaoCancelado(ideRequerimento);
			this.comprovantesDAE = this.comprovantePagamentoDaeService.obterPorIdRequerimento(ideRequerimento);
			this.boletosPagamentoRequerimento = boletoPagamentRequerimentoService.listarBoletosPorIdeRequerimento(ideRequerimento);

//			#6117
//			this.comprovantesDAE = this.comprovantePagamentoDaeService.listarByIdeRequerimento(ideRequerimento);
			
			removeComprovantesBoletoComplementar();//TICKET #5858
			
			setEmpreendimentoRequerimento(empreendimentoService.buscarEmpreendimentoRequerimento(requerimento));
			
			setAtosAmbientais(atoAmbientalService.listarAtosEnquadradosByRequerimento(ideRequerimento));
			consultarAreas(this.requerimento);
			
			if(!Util.isNullOuVazio(empreendimentoRequerimento)) {//CASO SEJA UMA ALTERAÇAO DE RAZAO SOCIAL POR EXEMPLO QUE NAO TEM EMPREENDIMENTO
				empreendimento = empreendimentoRequerimento.getIdeEmpreendimento();
				
				Collection<Tipologia> tipolologiasDoEnquadramento= null;
				
				boolean isPossuiTipologiaAutorizacao = false;
				
				for (AtoAmbiental atoAmbiental : this.atosAmbientais) {
					if(atoAmbiental.isAutorizacao()){
						isPossuiTipologiaAutorizacao = true;
					}
				}
				
				if(isPossuiTipologiaAutorizacao){
					
					 Collection<EnquadramentoAtoAmbiental> enquadramentos = enquadramentoService.buscarTipologiasDoEnquadramento(empreendimentoRequerimento.getIdeRequerimento());
					 if(!Util.isNullOuVazio(enquadramentos)){
						 tipolologiasDoEnquadramento = new ArrayList<Tipologia>();
						 
						 for (EnquadramentoAtoAmbiental enquadramento : enquadramentos) {
							 tipolologiasDoEnquadramento.add(enquadramento.getTipologia());
						 }
					 }
					 
				}else{
					tipolologiasDoEnquadramento = empreendimentoService.buscarTipologias(empreendimentoRequerimento.getIdeEmpreendimento(), false, true);
				}
								
				empreendimento.setTipologias(tipolologiasDoEnquadramento);
			}
			//verificaStatusAtual();
			setTramitacaoRequerimento(tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(ideRequerimento));
			
			this.existeDeclaracaoInexigibilidade = verificarDeclaracaoInexigibilidade(requerimento);
			
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void removeComprovantesBoletoComplementar() {
		
		Collection<ComprovantePagamento> listCP = new ArrayList<ComprovantePagamento>();
		
		for (ComprovantePagamento cp : comprovantesPagamento) {
			if(!cp.getIdeBoletoPagamentoRequerimento().getIdeTipoBoletoPagamento().getIdeTipoBoletoPagamento().equals(2)) {
				listCP.add(cp);
			}
		}
		
		comprovantesPagamento.clear();
		comprovantesPagamento.addAll(listCP);
	}
	
	public void salvar() {
		try{
			if(validar()) {
				if (Util.isNull(requerimento.getIdeArea()) && !this.existeDeclaracaoInexigibilidade) {
					JsfUtil.addWarnMessage("Favor selecionar a área onde o processo será formado.");
					return;
				}
				validacaoBoletoServiceFacade.salvar(this);
				RequestContext.getCurrentInstance().execute("dialogValidacaoComprovante.hide()");
			}
			else {
				email = "Favor acessar o SEIA para verificar pendências no comprovante enviado. \n\n Atte. \n Central de Atendimento/INEMA.";
				RequestContext.getCurrentInstance().execute("confirmarEnvioEmail.show()");
			}
			
		}
		catch(SeiaTramitacaoRequerimentoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível efetuar a validação.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean validar() {
		return existeComprovantesPagamento() && todosComprovantesForamValidados();
	}

	private boolean existeComprovantesPagamento() {
		return !Util.isNullOuVazio(comprovantesPagamento) || !Util.isNullOuVazio(comprovantesDAE);
	}
	
	private boolean todosComprovantesForamValidados() {
		return validaComprovanteApresentado() && validaComprovanteApresentadoDae();
	}

	private boolean validaComprovanteApresentado() {
		
		if(Util.isNullOuVazio(comprovantesPagamento)){
			return true;
		}
		else{
			for (ComprovantePagamento comprovantePagament : comprovantesPagamento) {
				if (!comprovantePagament.getIndComprovanteValidado()) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validaComprovanteApresentadoDae() {
		
		if(Util.isNullOuVazio(comprovantesDAE)){
			return true;
		}
		else{
			for (ComprovantePagamentoDae comprovantePagDAE : comprovantesDAE) {
				if (!comprovantePagDAE.getIndComprovanteValidado()) {
					return false;
				}
			}
		}
		return true;
	}
	
	

	public void enviarEmailPendenciaValidacao() {
		try {
			validacaoBoletoServiceFacade.enviarEmailPendenciaValidacao(this);
		} 
		catch (SeiaTramitacaoRequerimentoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void enviarEmailRequerimentoUnico() {
		try {
			validacaoBoletoServiceFacade.enviarEmailRequerimentoUnico(this);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isPendenteDeBoletoComplementar() {
		try {
			if(Util.isNull(this.requerimento)) {
				return false;
			}
			return boletoPagamentRequerimentoService.temBoletoPendente(this.requerimento.getIdeRequerimento());
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean renderedDownload(String caminhoArquivo) {
		if(!Util.isNullOuVazio(caminhoArquivo)) {
			return Util.verificaExistenciaArquivo(caminhoArquivo);
		} else {
			return false;
		}
	}

	public StreamedContent getFileDownload(String caminhoArquivo) {
		try {
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			return new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		}
		catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}		
	}
	
	public StreamedContent getFileDownloadBoleto(BoletoPagamentoRequerimento boleto) {
		try {
			if(boleto != null && boleto.getIndBoletoGeradoManualmente() != null && boleto.getIndBoletoGeradoManualmente()){
				return gerenciaArquivoservice.getContentFile(boleto.getDesCaminhoBoleto());
			}
			DadoBancario dadoBancario = dadoBancarioService.carregarBancoBoleto(BancoEnum.BANCO_DO_BRASIL);
			Pessoa requerente = pessoaService.carregarDadosRequerente(this.requerimento.getIdeRequerimento(), null);
			BigDecimal valorLicencas = new BigDecimal(0);
			BigDecimal valorOutorgas = new BigDecimal(0);
			
			List<AtoAmbiental> listAtosDeOutorga = (List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalByTipoAtoByAtivo(new TipoAto(TipoAtoEnum.AUTORIZACAO.getId()));
			
			if(Util.isLazyInitExcepOuNull(boleto.getDetalhamentoBoletoCollection())){
				boleto.setDetalhamentoBoletoCollection(boletoPagamentRequerimentoService.carregarDetalhamentosDoBoleto(boleto.getIdeBoletoPagamentoRequerimento()));
			}
			
			for (DetalhamentoBoleto detalheBoleto : boleto.getDetalhamentoBoletoCollection()) {
				if (listAtosDeOutorga.contains(detalheBoleto.getIdeAtoAmbiental())){
					valorOutorgas = valorOutorgas.add(detalheBoleto.getValor()); 
				} else {
					valorLicencas = valorLicencas.add(detalheBoleto.getValor());
				}
			}
			
			if(valorOutorgas.add(valorLicencas) == new BigDecimal(0)) {
				if(requerimentoService.isRequerimentoAntigo(this.requerimento.getIdeRequerimento())) {
					if (!Util.isNull(boleto.getValBoletoOutorga())) {
						valorOutorgas = boleto.getValBoletoOutorga();
					}
					
					valorLicencas = boleto.getValBoleto();
				}
			}
			
			dadoBancario.setDscInstrucao3(
					"Valor referente a atos de licenciamento (R$" 
					+ Util.getDecimalFormatPtBr().format(valorLicencas)+")"
					+ " e outorga (R$"
					+ Util.getDecimalFormatPtBr().format(valorOutorgas)+")");
			
			dadoBancario.setDscInstrucao4("solicitados através do requerimento "+ requerimento.getNumRequerimento() +"." );
			
			BoletoViewer boletoViewer = BoletoUtil.gerarBoleto(boleto, requerente, dadoBancario);
			return new DefaultStreamedContent(new ByteArrayInputStream(boletoViewer.getPdfAsByteArray()), "", "boleto" + new Date().getTime());
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isRequerimentoAntigo() {
		if(this.requerimento==null){
			return false;
		}
		return Util.isNullOuVazio(this.requerimento.getIdeArea());
	}
	
	public boolean verificarDeclaracaoInexigibilidade(Requerimento requerimento) throws Exception{
		DeclaracaoInexigibilidade declaracaoInexigibilidade = this.declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(requerimento);
		if(declaracaoInexigibilidade != null){
			return true;
		}
		return false;
	}
	
	public EmpreendimentoRequerimento getEmpreendimentoRequerimento() {
		return empreendimentoRequerimento;		
	}
	
	public void setEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {
		this.empreendimentoRequerimento = empreendimentoRequerimento;		
	}
	
	public Collection<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;		
	}
	
	public void setAtosAmbientais(Collection<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;		
	}
	
	public void consultarAreas(Requerimento requerimento) {
		super.consultarAreas(requerimento);		
	}
	
	public boolean isDirucSecundaria(){
		return super.isDirucSecundaria();
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Collection<ComprovantePagamento> getComprovantesPagamento() {
		return comprovantesPagamento;
	}

	public void setComprovantesPagamento(
			Collection<ComprovantePagamento> comprovantesPagamento) {
		this.comprovantesPagamento = comprovantesPagamento;
	}

	public Collection<ComprovantePagamentoDae> getComprovantesDAE() {
		return comprovantesDAE;
	}

	public void setComprovantesDAE(
			Collection<ComprovantePagamentoDae> comprovantesDAE) {
		this.comprovantesDAE = comprovantesDAE;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailValidacao() {
		return emailValidacao;
	}

	public void setEmailValidacao(String emailValidacao) {
		this.emailValidacao = emailValidacao;
	}

	public String getTituloEmail() {
		return tituloEmail;
	}

	public void setTituloEmail(String tituloEmail) {
		this.tituloEmail = tituloEmail;
	}

	public StatusRequerimento getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(StatusRequerimento statusAtual) {
		this.statusAtual = statusAtual;
	}

	public TramitacaoRequerimento getTramitacaoRequerimento() {
		return tramitacaoRequerimento;
	}

	public void setTramitacaoRequerimento(TramitacaoRequerimento tramitacaoRequerimento) {
		this.tramitacaoRequerimento = tramitacaoRequerimento;
	}

	public List<BoletoPagamentoRequerimento> getBoletosPagamentoRequerimento() {
		return boletosPagamentoRequerimento;
	}

	public void setBoletosPagamentoRequerimento(List<BoletoPagamentoRequerimento> boletosPagamentoRequerimento) {
		this.boletosPagamentoRequerimento = boletosPagamentoRequerimento;
	}
	
	public boolean isExisteDeclaracaoInexigibilidade() {
		return existeDeclaracaoInexigibilidade;
	}

	public void setExisteDeclaracaoInexigibilidade(boolean existeDeclaracaoInexigibilidade) {
		this.existeDeclaracaoInexigibilidade = existeDeclaracaoInexigibilidade;
	}
}