package br.gov.ba.seia.service;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dao.BoletoDaeRequerimentoImpl;
import br.gov.ba.seia.dao.BoletoPagamentoRequerimentoDAOImpl;
import br.gov.ba.seia.dao.ComprovantePagamentoDaeImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.LoteBoletoDAOImpl;
import br.gov.ba.seia.dao.LoteRemessaBoletoDAOImpl;
import br.gov.ba.seia.dao.PessoaDAOImpl;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.ComunicacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.DadoBancario;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.LoteBoleto;
import br.gov.ba.seia.entity.LoteRemessaBoleto;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusBoletoPagamento;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.TipoLoteBoleto;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.BancoEnum;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoBoletoPagamentoEnum;
import br.gov.ba.seia.enumerator.TipoLoteBoletoEnum;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.exception.SeiaLoteBoletoException;
import br.gov.ba.seia.facade.BoletoServiceFacade;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.middleware.seia.model.BaixaBoletoDTO;
import br.gov.ba.seia.middleware.seia.model.LeituraArquivoRetornoDTO;
import br.gov.ba.seia.service.facade.ComunicacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.service.facade.ComunicacaoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LoteBoletoService {

	private static final String ZERO = "0";
	private static final String TRES = "3";
	private static final String NOVE = "9";
	
	@Inject
	private LoteBoletoDAOImpl loteBoletoDAOImpl;
	@Inject
	private BoletoPagamentoRequerimentoDAOImpl boletoPagamentoRequerimentoDAOImpl;
	@Inject
	private PessoaDAOImpl pessoaDAOImpl;
	@Inject
	private IDAO<LoteBoleto> loteBoletoDAO;
	@Inject
	private IDAO<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoDAO;
	@Inject
	private LoteRemessaBoletoDAOImpl loteRemessaBoletoDAOImpl;
	@Inject
	private BoletoDaeRequerimentoImpl boletoDaeRequerimentoDAOImpl;
	@Inject
	private ComprovantePagamentoDaeImpl comprovantePagamentoDaeImpl;
	@Inject 
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl; 
	@Inject
	private EnderecoPessoaService enderecoPessoaService;

	
	@EJB
	private DadoBancarioService dadoBancarioService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private BoletoServiceFacade boletoServiceFacade;
	@EJB
	private BoletoPagamentoHistoricoService boletoPagamentoHistoricoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	@EJB
	private ComunicacaoReenquadramentoProcessoServiceFacade comunicacaoReenquadramentoProcessoServiceFacade;
	@EJB
	private EmailService emailService;
	@EJB
	private ComunicacaoServiceFacade comunicacaoServiceFacade;

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LoteBoleto> listarLoteBoleto(Map<String, Object> params)  {
		return loteBoletoDAOImpl.listarLoteBoleto(params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countListarLoteBoleto(Map<String, Object> params)  {
		return loteBoletoDAOImpl.countListarLoteBoleto(params);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StreamedContent gerarLoteRemessaBoleto() throws Exception  {

		List<BoletoPagamentoRequerimento> boletos = boletoPagamentoRequerimentoDAOImpl.listarBoletosRegistradosNaoRemetidos();
		if(Util.isNullOuVazio(boletos)){
			throw new Exception("Não há boleto a ser remetido.");
		}
		
		LoteBoleto lote = new LoteBoleto(new TipoLoteBoleto(TipoLoteBoletoEnum.REMESSA.getId()), new Date(), getNumeroLote());
		LoteRemessaBoleto remessa = new LoteRemessaBoleto();
		remessa.setIdeLoteBoleto(lote);
		remessa.setIdePessoaGeracao(new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		
		ByteArrayOutputStream baos = getRemessa(lote, boletos, remessa);
		
		loteBoletoDAO.salvar(lote);
		loteRemessaBoletoDAOImpl.salvar(remessa);
		boletoPagamentoRequerimentoDAO.salvarEmLote(boletos);
		
		String nomeArquivo = "remessa_"+lote.getNumLoteBoleto()+".REM";
		
		return new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()), "", nomeArquivo);
	}
	
	private String getNumeroLote() {
		LoteBoleto ultimoLoteBoletoGerado = loteBoletoDAOImpl.carregarUltimoLoteBoleto();
		
		String numLote = "";
		if (ultimoLoteBoletoGerado != null) {
			int i = Integer.parseInt(ultimoLoteBoletoGerado.getNumLoteBoleto()) + 1;
			numLote = "" + i;
		} else{
			numLote = "1";
		}

		return Util.lpad(numLote, '0', 6);
	}
	
	private ByteArrayOutputStream getRemessa(LoteBoleto lote, List<BoletoPagamentoRequerimento> boletos,
			LoteRemessaBoleto remessa) throws IOException {
		DadoBancario dadoBancario = this.dadoBancarioService.carregarBancoBoleto(BancoEnum.BANCO_DO_BRASIL);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));
		writer.write(getHeaderArquivo(dadoBancario, lote.getNumLoteBoleto()));
		writer.write(getHeaderLote(dadoBancario, lote.getNumLoteBoleto()));

		int qtdDetalhes = 1;
		for (BoletoPagamentoRequerimento b : boletos) {
			Integer ideRequerimento;
			if (b.getIdeRequerimento() != null) {
				ideRequerimento = b.getIdeRequerimento().getIdeRequerimento();
			} else {
				Processo processo = processoService.carregarProcesso(b.getIdeProcesso().getIdeProcesso());
				ideRequerimento = processo.getIdeRequerimento().getIdeRequerimento();
			}

			Pessoa requerente = pessoaDAOImpl.carregarDadosRequerenteRefatorado(ideRequerimento);

			if (requerente != null) {
				EnderecoPessoa enderecoPessoa = enderecoPessoaService.buscarEnderecoPorPessoa(requerente);
				if (enderecoPessoa != null && enderecoPessoa.getIdeEndereco() != null) {
					requerente.setEndereco(enderecoPessoa.getIdeEndereco());
				}
			}

			writer.write(getSegmentoP(dadoBancario, b, qtdDetalhes));
			qtdDetalhes++;
			writer.write(getSegmentoQ(dadoBancario, b, qtdDetalhes, requerente));
			qtdDetalhes++;

			if (remessa != null) {
				b.setIdeLoteRemessaBoleto(remessa);
				b.setIndBoletoGeradoManualmente(false);
			}
		}

		writer.write(getTrailerLote(boletos.size() * 2 + 2));
		writer.write(getTrailerArquivo(boletos.size() * 2 + 4));
		writer.close();

		return baos;
	}
	
	private String getHeaderArquivo(DadoBancario dadoBancario, String numLote){
		StringBuilder ha = new StringBuilder();
		
		ha.append("001")
		.append("0000")
		.append("0")
		.append(getRightPad("", 9))
		
		.append("2")
		.append(getLeftPad(dadoBancario.getNumCnpj().replaceAll("[^0-9]", ""), 14))
		.append(getLeftPad(dadoBancario.getConvenio(), 9)).append("0014").append(dadoBancario.getNumCarteira().replaceAll("[^0-9]", "")).append(getRightPad("", 2))
		.append(getLeftPad(dadoBancario.getNumAgencia().toString(), 5))
		.append(dadoBancario.getNumDigitoAgencia())
		.append(getLeftPad(dadoBancario.getNumConta().toString(), 12))
		.append(dadoBancario.getNumDigitoConta())
		.append(" ")
		.append(getRightPad(Util.removerAcentos(dadoBancario.getNomCedente()).toUpperCase(), 30))
		.append(getRightPad("BANCO DO BRASIL S.A.", 30))
		.append(getRightPad("", 10))
		
		.append("1")
		.append(new SimpleDateFormat("ddMMyyyy").format(new Date()))
		.append(new SimpleDateFormat("HHmmss").format(new Date()))
		.append(getLeftPad(numLote, 6))
		.append("000")
		.append(getLeftPad("", 5))
		.append(getRightPad("", 20))
		.append(getRightPad("", 20))
		.append(getRightPad("", 29))
		.append("\n");
		;
		
		return ha.toString();
	}
	
	private String getHeaderLote(DadoBancario dadoBancario, String numLote){
		StringBuilder hl = new StringBuilder();

		hl.append("001")
		.append("0001")
		.append("1")
		.append("R")
		.append("01")
		.append(getRightPad("", 2))
		.append("000")
		.append(getRightPad("", 1))
		
		.append("2")
		.append(getLeftPad(dadoBancario.getNumCnpj().replaceAll("[^0-9]", ""), 15))
		.append(getLeftPad(dadoBancario.getConvenio(), 9)).append("0014").append(dadoBancario.getNumCarteira().replaceAll("[^0-9]", "")).append(getRightPad("", 2))
		.append(getLeftPad(dadoBancario.getNumAgencia().toString(), 5))
		.append(dadoBancario.getNumDigitoAgencia())
		.append(getLeftPad(dadoBancario.getNumConta().toString(), 12))
		.append(dadoBancario.getNumDigitoConta())
		.append(" ")
		.append(getRightPad(Util.removerAcentos(dadoBancario.getNomCedente()).toUpperCase(), 30))
		
		.append(getRightPad("", 40))
		.append(getRightPad("", 40))
		.append(getLeftPad(numLote, 8))
		.append(new SimpleDateFormat("ddMMyyyy").format(new Date()))
		.append(getLeftPad("", 8))
		.append(getRightPad("", 33))
		.append("\n")
		;
		
		return hl.toString();
	}
	
	private String getSegmentoP(DadoBancario dadoBancario, BoletoPagamentoRequerimento boleto, Integer qtdDetalhes){
		StringBuilder p = new StringBuilder();
		
		
		BigDecimal valBoleto = boleto.getValTotalBoleto().setScale(2,BigDecimal.ROUND_HALF_EVEN);
		
		p.append("001")						
		.append("0001")
		.append("3")
		.append(getLeftPad(qtdDetalhes.toString(), 5))
		.append("P")
		.append(getRightPad("", 1))
		.append("01")
		
		.append(getLeftPad(dadoBancario.getNumAgencia().toString(), 5))
		.append(dadoBancario.getNumDigitoAgencia())
		.append(getLeftPad(dadoBancario.getNumConta().toString(), 12))
		.append(dadoBancario.getNumDigitoConta())
		.append(getRightPad("", 1))
		.append(getRightPad(boleto.getNumBoleto(), 20))
		.append("1")
		.append("1")
		.append("1")
		.append("2")
		.append("2")
		
		.append(getRightPad(boleto.getIdeBoletoPagamentoRequerimento().toString(), 15))
		.append(new SimpleDateFormat("ddMMyyyy").format(boleto.getDtcVencimento()))
		.append(getLeftPad(valBoleto.toString().replaceAll("[^0-9]", ""), 15))
		.append(getLeftPad("", 5))
		.append(getRightPad("", 1))
		.append("04")
		.append("N")
		.append(new SimpleDateFormat("ddMMyyyy").format(boleto.getDtcEmissao()))
		.append("3")
		.append(getLeftPad("", 8))
		.append(getLeftPad("", 15))
		.append(getLeftPad("", 1))
		.append(getLeftPad("", 8))
		.append(getLeftPad("", 15))
		.append(getLeftPad("", 15))
		.append(getLeftPad("", 15))
		.append(getRightPad("", 25))
		.append("3")
		.append(getLeftPad("", 2))
		.append(getLeftPad("", 1))
		.append(getLeftPad("", 3))
		.append("09")
		.append(getLeftPad("", 10))
		.append(getRightPad("", 1))
		.append("\n")
		;
		
		return p.toString();
	}
	
	private String getSegmentoQ(DadoBancario dadoBancario, BoletoPagamentoRequerimento boleto, Integer qtdDetalhes, Pessoa requerente){
		StringBuilder q = new StringBuilder();

		String tipoInscricaoPagador;
		String numeroInscricaoPagador;
		String nomePagador;
		
		if (requerente.isPF()) {
			tipoInscricaoPagador = "1";
			numeroInscricaoPagador = requerente.getPessoaFisica().getNumCpf() != null ? (requerente.getPessoaFisica().getNumCpf()).replaceAll("[^\\d]", "") : "";
			nomePagador = Util.removerAcentos(requerente.getPessoaFisica().getNomPessoa());
		} else {
			tipoInscricaoPagador = "2";
			numeroInscricaoPagador = requerente.getPessoaJuridica().getNumCnpj() != null ? (requerente.getPessoaJuridica().getNumCnpj()).replaceAll("[^\\d]", "") : "";
			nomePagador = Util.removerAcentos(requerente.getPessoaJuridica().getNomRazaoSocial());
		}
		
		String endereco = "";
		String bairro = "";
		Integer cep = 0;
		String cidade = "";
		String uf= "";
		if(requerente.getEndereco() != null){
			Logradouro logradouro = requerente.getEndereco().getIdeLogradouro();
			if(logradouro != null){
				endereco = Util.removerAcentos(logradouro.getIdeTipoLogradouro().getNomTipoLogradouro()) + " "+ Util.removerAcentos(logradouro.getNomLogradouro())
						+ ", N.: " + Util.removerAcentos(requerente.getEndereco().getNumEndereco());
				cep = logradouro.getNumCep();
				if(logradouro.getIdeBairro() != null){
					bairro = Util.removerAcentos(logradouro.getIdeBairro().getNomBairro());
				}
				if(logradouro.getMunicipio() != null){
					cidade = Util.removerAcentos(logradouro.getMunicipio().getNomMunicipio());
					uf= Util.removerAcentos(logradouro.getMunicipio().getIdeEstado().getDesSigla());
				}
			}
		}
		
		q.append("001")
		.append("0001")
		.append("3")
		.append(getLeftPad(qtdDetalhes.toString(), 5))
		.append("Q")
		.append(getRightPad("", 1))
		.append("01")
		
		.append(tipoInscricaoPagador)
		.append(getLeftPad(numeroInscricaoPagador, 15))
		.append(getRightPad(nomePagador.toUpperCase(), 40))
		.append(getRightPad(endereco.toUpperCase(), 40))
		.append(getRightPad(bairro.toUpperCase(), 15))
		.append(getLeftPad(cep.toString(), 8))
		.append(getRightPad(cidade.toUpperCase(), 15))
		.append(getRightPad(uf.toUpperCase(), 2))
		
		.append(getLeftPad("", 1))
		.append(getLeftPad("", 15))
		.append(getRightPad("", 40))
		
		.append(getLeftPad("", 3))
		.append(getRightPad("", 20))
		.append(getRightPad("", 8))
		.append("\n")
		;
		
		return q.toString();
	}
	
	private String getTrailerLote(Integer qtdRegistrosLotes){
		StringBuilder tl = new StringBuilder();
		
		tl.append("001")
		.append("0001")
		.append("5")
		.append(getRightPad("", 9))
		.append(getLeftPad(qtdRegistrosLotes.toString(), 6))
		.append(getLeftPad("", 6))
		.append(getLeftPad("", 17))
		.append(getLeftPad("", 6))
		.append(getLeftPad("", 17))
		.append(getLeftPad("", 6))
		.append(getLeftPad("", 17))
		.append(getLeftPad("", 6))
		.append(getLeftPad("", 17))
		.append(getRightPad("", 8))
		.append(getRightPad("", 117))
		.append("\n")
		;
		
		return tl.toString();
	}
	
	private String getTrailerArquivo(Integer qtdRegistrosArquivo){
		StringBuilder ta = new StringBuilder();
		
		ta.append("001")
		.append("9999")
		.append("9")
		.append(getRightPad("", 9))
		.append(getLeftPad("1", 6))
		.append(getLeftPad(qtdRegistrosArquivo.toString(), 6))
		.append(getLeftPad("", 6))
		.append(getRightPad("", 205))
		.append("\n")
		;
				
		return ta.toString();
	}
	
	private String getLeftPad(String texto, int tamanho){
		if(StringUtils.isEmpty(texto)){
			texto = StringUtils.EMPTY;
		}
		
		return StringUtils.right(StringUtils.leftPad(texto, tamanho, '0'), tamanho);
	}
	
	private String getRightPad(String texto, int tamanho){
		if(StringUtils.isEmpty(texto)){
			texto = StringUtils.EMPTY;
		}
		
		return StringUtils.left(StringUtils.rightPad(texto, tamanho, ' '), tamanho);
	}
	
	//Download do arquivo de remessa previamente gerado.
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent getLoteRemessa(LoteBoleto lote, List<BoletoPagamentoRequerimento> boletos) throws Exception  {

		if(Util.isNullOuVazio(boletos)){
			throw new Exception("Não há boletos nessa remessa.");
		}
		
		if(lote == null){
			throw new Exception("Lote inválido.");
		}

		ByteArrayOutputStream baos = getRemessa(lote, boletos, null);
		String nomeArquivo = "remessa_"+lote.getNumLoteBoleto()+".REM";
		
		return new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()), "", nomeArquivo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String uploadArquivoRetorno(UploadedFile event) throws Exception  {
		return gerenciaArquivoService.uploadRetornoBoleto(event, getNumeroLote());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void lerArquivoRetorno(LeituraArquivoRetornoDTO leituraArquivoRetornoDTO) throws Exception {
		
		File arquivoRetorno = new File(leituraArquivoRetornoDTO.getCaminhoArquivoRetorno());
		
		Scanner scnr = new Scanner(new FileInputStream(arquivoRetorno));
		int qtdLinhas = 0;
		while (scnr.hasNextLine()) {
			qtdLinhas++;
			scnr.nextLine();
		}
		scnr.close();
 		if(qtdLinhas <= 4){
        	throw new SeiaLoteBoletoException("Arquivo de retorno sem boletos processados.");
        }
 		
 		scnr = new Scanner(new FileInputStream(arquivoRetorno));
		
		DadoBancario dadoBancario = this.dadoBancarioService.carregarBancoBoleto(BancoEnum.BANCO_DO_BRASIL);
		
		String linhaT = "";
		String convenio = dadoBancario.retornarConvenio();
		while (scnr.hasNextLine()) {
			
			String linha = scnr.nextLine();
            validarLayout(linha, convenio, dadoBancario.getNumCnpj());
            
            if(TRES.equals(StringUtils.substring(linha, 7, 8)) && "T".equalsIgnoreCase(StringUtils.substring(linha, 13, 14))){
            	linhaT = linha;
    		}
            
            if(TRES.equals(StringUtils.substring(linha, 7, 8)) && "U".equalsIgnoreCase(StringUtils.substring(linha, 13, 14))){
            	processarBoleto(linhaT, linha, leituraArquivoRetornoDTO);
    		}
        }
		scnr.close();
		
		if(Util.isNullOuVazio(leituraArquivoRetornoDTO.getListaBoleto())){
			throw new SeiaLoteBoletoException("Arquivo de retorno contém apenas boletos recusados.");
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarEmailLoteBoleto(List<BaixaBoletoDTO> listaBaixaBoleto) {
		try {
			for (BaixaBoletoDTO baixaBoletoDTO : listaBaixaBoleto) {
				
				List<BoletoPagamentoRequerimento> listaBoletoPagamentoRequerimento = new  ArrayList<BoletoPagamentoRequerimento>();
				for (Object obj : baixaBoletoDTO.getBoletos()) {
					listaBoletoPagamentoRequerimento.add(Util.objectTransform(obj, BoletoPagamentoRequerimento.class));
				}
				
				for (BoletoPagamentoRequerimento boletoPagamentoRequerimento : listaBoletoPagamentoRequerimento) {
					Processo processo = null;
					
					if(!Util.isNullOuVazio(boletoPagamentoRequerimento.getIdeProcesso())) {
						processo = processoService.buscarPorIdeProcesso(boletoPagamentoRequerimento.getIdeProcesso());
					}else {
						processo = processoService.buscarPorRequerimento(boletoPagamentoRequerimento.getIdeRequerimento().getIdeRequerimento());
					}
					
					if(!Util.isNullOuVazio(processo)){
						if(!Util.isNullOuVazio(boletoPagamentoRequerimento.getIdeRequerimento())){
							ComunicacaoRequerimento comunicacaoRequerimento = comunicacaoRequerimentoService.buscarUltimaComunicacaoRequerimento(boletoPagamentoRequerimento.getIdeRequerimento().getIdeRequerimento());
							emailService.enviarEmailsAoRequerente(boletoPagamentoRequerimento.getIdeRequerimento(), comunicacaoRequerimento.getAssunto(), comunicacaoRequerimento.getDesMensagem());
							comunicacaoRequerimentoService.atualizarStatusEnvioComunicacao(comunicacaoRequerimento);
						}else if(!Util.isNullOuVazio(boletoPagamentoRequerimento.getIdeProcessoReenquadramento())){
							boletoPagamentoRequerimento.setIdeProcesso(processoService.buscarPorIdeProcesso(processo));
							ComunicacaoReenquadramentoProcesso comunicacaoReenquadramentoProcesso = new ComunicacaoReenquadramentoProcesso();
							comunicacaoReenquadramentoProcesso.setIdeProcessoReenquadramento(boletoPagamentoRequerimento.getIdeProcessoReenquadramento());
							comunicacaoReenquadramentoProcesso.setDtcComunicacao(new Date());
							comunicacaoReenquadramentoProcesso.setDesMensagem(carregarMensagemReenquadramento(boletoPagamentoRequerimento.getIdeProcesso().getNumProcesso()));
							emailService.enviarEmailsAoRequerente(processo.getIdeRequerimento(), "SEIA - Status do boleto", comunicacaoReenquadramentoProcesso.getDesMensagem());
							comunicacaoReenquadramentoProcessoServiceFacade.salvarComunicacaoReenquadramentoProcesso(comunicacaoReenquadramentoProcesso);
						}
						
						
					} else {
						TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(boletoPagamentoRequerimento.getIdeRequerimento().getIdeRequerimento());
						if(!tramitacaoRequerimento.getIdeStatusRequerimento().isDeclaracaoEmitida()) {
							if(tramitacaoRequerimento.getIdeStatusRequerimento().isProcessoFormado()) {
								boletoPagamentoRequerimento.setIdeLoteRetornoBoleto(null);
								boletoPagamentoRequerimento.setDtcPagamento(null);
								boletoPagamentoRequerimentoDAO.atualizar(boletoPagamentoRequerimento);
								tramitacaoRequerimentoService.removerTramitacao(tramitacaoRequerimento);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private String carregarMensagemReenquadramento(String numProcesso) {
		StringBuilder texto = new StringBuilder();
		texto
			.append("Prezado(a), \n")
			.append("O boleto  do reenquadramento do processo nº " + numProcesso + "foi validado. \n")
			.append("Favor acessar o SEIA para verificar o andamento do processo. \n")
			.append("Atenciosamente, \n")			
			.append("Central de Atendimento - INEMA");
		
		return texto.toString();

	}

	private void salvarBoletoPagamentoHistorico(BoletoPagamentoRequerimento bpr, StatusBoletoEnum sbe) {
		try {
			BoletoPagamentoHistorico bph = new BoletoPagamentoHistorico();
			bph.setIdeBoletoPagamento(new BoletoPagamentoRequerimento(bpr.getIdeBoletoPagamentoRequerimento()));
			bph.setDtcTramitacao(new Date());
			bph.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
			bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(sbe.getId()));
			boletoPagamentoHistoricoService.salvar(bph);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void validarPossibilidadeGerarProcesso(Requerimento requerimento) throws Exception {
		boletoServiceFacade.validarPossibilidadeGerarProcesso(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void confirmarLoteRemessa(LoteBoleto lote, List<BoletoPagamentoRequerimento> boletos) {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		Pessoa operador = new Pessoa(usuario.getIdePessoaFisica());
		
		
		for(BoletoPagamentoRequerimento boleto : boletos) {
			
			if(Util.isNull(boleto.getIdeTipoBoletoPagamento())) {
				throw new RuntimeException("O campo ideTipoBoletoPagamento não foi carregado.");
			}
			else {
				
				TipoBoletoPagamentoEnum tipoBoletoPagamentoEnum =  TipoBoletoPagamentoEnum.getEnum(boleto.getIdeTipoBoletoPagamento());
				
				List<TipoBoletoPagamentoEnum> listaTipoBoletoProcesso = Arrays.asList(
					TipoBoletoPagamentoEnum.EIA_RIMA_VINCULADO_PROCESSO,
					TipoBoletoPagamentoEnum.SEGUNDA_VIA_CERTIFICADO,
					TipoBoletoPagamentoEnum.ERRATA_PORTARIA,
					TipoBoletoPagamentoEnum.INCLUSAO_NOVO_ATO
				);
				
				if(TipoBoletoPagamentoEnum.REQUERIMENTO.equals(tipoBoletoPagamentoEnum)) {
					TramitacaoRequerimento tramitacao = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(boleto.getIdeRequerimento().getIdeRequerimento());
					boletoServiceFacade.tramitarRequerimento(boleto.getIdeRequerimento(), StatusRequerimentoEnum.PAGAMENTO_LIBERADO, tramitacao);
					
					BoletoDaeRequerimento vistoria = boletoDaeRequerimentoDAOImpl.carregarVistoriaByRequerimento(boleto.getIdeRequerimento().getIdeRequerimento());
					BoletoDaeRequerimento certificado = boletoDaeRequerimentoDAOImpl.carregarCertificadoByRequerimento(boleto.getIdeRequerimento().getIdeRequerimento());
					boletoServiceFacade.gerarComunicacaoRequerimento(boleto.getIdeRequerimento(), boleto, vistoria, certificado);
					
				}
				else if(TipoBoletoPagamentoEnum.REQUERIMENTO_BOLETO_COMPLEMENTAR.equals(tipoBoletoPagamentoEnum)) {
					enviarEmailRequerimentoBoletoPagamentoLiberado(boleto.getIdeRequerimento());
					salvarBoletoPagamentoHistorico(boleto, StatusBoletoEnum.EMITIDO);
				}			
				else if(listaTipoBoletoProcesso.contains(tipoBoletoPagamentoEnum)) {
					enviarEmailProcessoBoletoPagamentoLiberado(boleto.getIdeProcesso());
					salvarBoletoPagamentoHistorico(boleto, StatusBoletoEnum.EMITIDO);
				}			
				else if(TipoBoletoPagamentoEnum.REENQUADRAMENTO_PROCESSO.equals(tipoBoletoPagamentoEnum)) {
					ProcessoReenquadramento reenquadramento = processoReenquadramentoDAOImpl.obterProcessoReenquadramentoPorProcesso(boleto.getIdeProcesso().getIdeProcesso());
					if(reenquadramento != null){
						tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcesso(reenquadramento, new StatusReenquadramento(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO), operador);
						enviarEmailReenquadramentoBoletoPagamentoLiberado(reenquadramento);					
					}
					salvarBoletoPagamentoHistorico(boleto, StatusBoletoEnum.EMITIDO);
				}
			}
		}
		
		for(LoteRemessaBoleto remessa : loteRemessaBoletoDAOImpl.listarRemessasaPorLote(lote)){
			remessa.setIdePessoaConfirmacao(new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			remessa.setDtcEnvioRemessa(new Date());
			loteRemessaBoletoDAOImpl.atualizar(remessa);
		}
	}

	private void enviarEmailReenquadramentoBoletoPagamentoLiberado(ProcessoReenquadramento reenquadramento) {
		StringBuilder msg = new StringBuilder();
		msg.append("Prezado(a),\n\n");
		msg.append("O boleto complementar referente ao reenquadramento do processo nº "+reenquadramento.getIdeProcesso().getNumProcesso()+" está disponível para pagamento.\n\n"); 
		msg.append("Para dar continuidade, acesse o SEIA no menu lateral à esquerda acione a aba \"PROCESSO\", em seguida \"REENQUADRAMENTO\" e siga os seguintes passos:\n"); 
		msg.append("Passo 1: Informe o número do processo mencionado acima no campo \"Nº do processo:\" e acione o botão \"Consultar\";\n\n"); 
		msg.append("Passo 2: Na coluna \"Ações\" acione o ícone  \"Imprimir boleto / Enviar comprovante\";\n\n");  
		msg.append("Passo 3: Na tela \"Imprimir boleto / Enviar comprovante\" acesse o link (números em azul e sublinhado) a direita de \"Nº do Boleto\" para visualizar o boleto.\n\n"); 
		msg.append("Passo 4: Realize o pagamento conforme orientações no boleto e aguarde novas orientações.\n\n"); 
		msg.append("Atenciosamente,\n");
		msg.append("Central de Atendimento/INEMA");
		
		Map<String,String> mapEmail = new HashMap<String,String>();
		mapEmail.put("assunto", "SEIA - Comunicado - Boleto disponível para pagamento");
		mapEmail.put("mensagem", msg.toString());
		comunicacaoServiceFacade.gerarComunicacao(reenquadramento, mapEmail);
	}
	
	private void enviarEmailProcessoBoletoPagamentoLiberado(Processo processo) {
		StringBuilder msg = new StringBuilder();
		msg.append("Prezado(a),\n\n");
		msg.append("O boleto complementar referente ao processo nº "+processo.getNumProcesso()+" está disponível para pagamento.\n\n"); 
		msg.append("Atenciosamente,\n");
		msg.append("Central de Atendimento/INEMA");
		
		Map<String,String> mapEmail = new HashMap<String,String>();
		mapEmail.put("assunto", "SEIA - Boleto de pagamento do processo de nº "+processo.getNumProcesso());
		mapEmail.put("mensagem", msg.toString());
		comunicacaoServiceFacade.gerarComunicacao(processo, mapEmail);
	}
	
	private void enviarEmailRequerimentoBoletoPagamentoLiberado(Requerimento requerimento) {
		StringBuilder msg = new StringBuilder();
		msg.append("Prezado(a),\n\n");
		msg.append("O boleto complementar referente ao requerimento nº "+requerimento.getNumRequerimento()+" está disponível para pagamento.\n\n"); 
		msg.append("Atenciosamente,\n");
		msg.append("Central de Atendimento/INEMA");
		
		Map<String,String> mapEmail = new HashMap<String,String>();
		mapEmail.put("assunto", "SEIA - Boleto de pagamento do requerimento de nº "+requerimento.getNumRequerimento());
		mapEmail.put("mensagem", msg.toString());
		comunicacaoServiceFacade.gerarComunicacao(requerimento, mapEmail);
	}
	
	private void validarLayout(String linha, String convenio, String cnpj) throws SeiaException {
		if(StringUtils.isEmpty(linha)){
			throw new SeiaException("Arquivo sem informações.");
		}
		if(linha.length() != 240){
			throw new SeiaException("Arquivo contém linhas fora do padrão CNAB240.");
		}
		
		String tipoRegistro = StringUtils.substring(linha, 7, 8);
		
		if(ZERO.equals(tipoRegistro)){
			validarLayoutHeaderArquivo(linha, convenio, cnpj);
		}
		else if(TRES.equals(tipoRegistro)){
			validarLayoutDetalhamentoT(linha);
		} 
		else if(NOVE.equals(tipoRegistro)){
			validarLayoutTrailer(linha);
		}
	}
	
	private void validarLayoutHeaderArquivo(String linha, String convenio, String cnpj) throws SeiaException {
		if (StringUtils.isBlank(linha.substring(0, 3))) {
			throw new SeiaException("Header sem codigo do banco.");
		}
		if (!StringUtils.isNumeric(linha.substring(0, 3))) {
			throw new SeiaException("Código do banco inválido.");
		}
		if (StringUtils.isBlank(linha.substring(18, 32))) {
			throw new SeiaException("Header sem cnpj da empresa.");
		}
		if (!cnpj.replaceAll("[^0-9]", "").equalsIgnoreCase(linha.substring(18,32))) { 
	      throw new SeiaException("Header com código cnpj da empresa divergente.");
	    }
		if (StringUtils.isBlank(linha.substring(32, 52))) { 
	      throw new SeiaException("Header sem código do convênio.");
	    }
		if (!convenio.equalsIgnoreCase(linha.substring(32, 52))) { 
	      throw new SeiaException("Header com código do convênio divergente.");
	    }
	    if (StringUtils.isBlank(linha.substring(72, 102))) {
	      throw new SeiaException("Header sem nome da empresa.");
	    }
	    if (StringUtils.isBlank(linha.substring(102, 132))) {
	      throw new SeiaException("Header sem nome do banco.");
	    }
	    if (StringUtils.isBlank(linha.substring(143, 151))) {
	      throw new SeiaException("Header sem data de geração.");
	    }
	    if (StringUtils.isBlank(linha.substring(163, 166))) {
	    	throw new SeiaException("Header sem versão do layout.");
	    }
	    
	}
	
	private void validarLayoutDetalhamentoT(String linha) throws SeiaException {
		if (StringUtils.isBlank(linha.substring(0, 3))) {
			throw new SeiaException("Registro sem codigo do banco.");
		}
		if (!StringUtils.isNumeric(linha.substring(0, 3))) {
			throw new SeiaException("Registro com código do banco inválido.");
		}
		if (StringUtils.isBlank(linha.substring(37, 57))) {
	      throw new SeiaException("Registro sem número de boleto.");
	    }
		if (!StringUtils.isNumeric(linha.substring(37, 57).trim())) {
			throw new SeiaException("Registro com 'nosso número' inválido.");
		}
	}
	
	private void validarLayoutTrailer(String linha) throws SeiaException {
		if (StringUtils.isBlank(linha.substring(0, 3))) {
			throw new SeiaException("Trailer sem codigo do banco.");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void processarBoleto(String linhaT, String linhaU, LeituraArquivoRetornoDTO leituraArquivoRetornoDTO) throws Exception {
		
		String numeroBoleto = linhaT.substring(37, 57);
		
		//campo 07.3T - codigo de movimento de retorno
		// '02', '03', '26' e '30' são relacionados a descricao C047-A: codigos de rejeicoes
		// '06', '09' e '17' são relacionados com a descrição C047-C: Códigos de liquidação / baixa 
		List<String> listaCodigosLiquidacao = new ArrayList<String>();
		listaCodigosLiquidacao.add("06");
		listaCodigosLiquidacao.add("09");
		listaCodigosLiquidacao.add("17");
		String codigoMovimentoRetorno = linhaT.substring(15, 17); 
		
		String linhasValidas = linhaU.substring(77, 92); //campo 12.3U - indica o valor pago pelo inema. 
		String zeros = getLeftPad("", 15);
		
		String dataPagamento = linhaU.substring(137, 145); //campo 16.3U - DDMMYYYY
		
		if(!zeros.equalsIgnoreCase(linhasValidas) && listaCodigosLiquidacao.contains(codigoMovimentoRetorno)) {
			
			BoletoPagamentoRequerimento boleto = boletoPagamentoRequerimentoDAOImpl.obterBoletoPorNumeroBoleto(numeroBoleto.trim());
			
			if(boleto != null){
				
				if (!Util.isNullOuVazio(dataPagamento)){
					SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
					Date dtPagamento = (Date)formatter.parse(dataPagamento);
					boleto.setDtcPagamento(dtPagamento);
				}
				
				if(boleto.getIdeLoteRetornoBoleto() != null){ //para que um boleto de outro arquivo de retorno nao seja alterado
					return;
				}				
												
				if(boleto.getIdeRequerimento() != null) {
					
					Requerimento requerimento = requerimentoService.carregarDadosBasicos(boleto.getIdeRequerimento().getIdeRequerimento());
					leituraArquivoRetornoDTO.addListaRequerimento(requerimento);
					
					if(boleto.getIdeTipoBoletoPagamento().getIdeTipoBoletoPagamento().equals(TipoBoletoPagamentoEnum.REQUERIMENTO.getId())) {
						
						boolean todosBoletosComplementaresPagos = verificarPagamentoBoletoComplementarRequerimento(requerimento);
						boolean todosDaePagos = verificarPagamentoBoletoDAERequerimento(requerimento);
						
						if(todosBoletosComplementaresPagos && todosDaePagos) {
							leituraArquivoRetornoDTO.addListaRequerimentoGerarProcesso(requerimento);
						}
					}
					else if(boleto.getIdeTipoBoletoPagamento().getIdeTipoBoletoPagamento().equals(TipoBoletoPagamentoEnum.REQUERIMENTO_BOLETO_COMPLEMENTAR.getId())) {
						
						boolean pagamentoBoletoRequerimento = verificarPagamentoBoletoRequerimento(requerimento);
						boolean processoNaoEncontrado = processoService.isProcessoNaoEncontrado(requerimento);
						
						if(pagamentoBoletoRequerimento && processoNaoEncontrado) {
							leituraArquivoRetornoDTO.addListaRequerimentoGerarProcesso(requerimento);
						}
					}
					
				}
				else if(boleto.getIdeProcessoReenquadramento() != null){
					
					ProcessoReenquadramento reenquadramento = processoReenquadramentoDAOImpl.obterProcessoReenquadramentoPorProcesso(boleto.getIdeProcesso().getIdeProcesso());
					leituraArquivoRetornoDTO.addListaProcessoReenquadramento(reenquadramento);
					
					if(verificarPagamentoBoletoDAEProcessoReenquadramento(reenquadramento)){
						leituraArquivoRetornoDTO.addListaProcessoReenquadramentoTramitar(reenquadramento);
					}
					
				}
				else if(boleto.getIdeProcesso() != null){
					leituraArquivoRetornoDTO.addListaProcesso(boleto.getIdeProcesso());
				}
				
				leituraArquivoRetornoDTO.addListaBoleto(boleto);
			}	
		}
	}

	private boolean verificarPagamentoBoletoDAEProcessoReenquadramento(
			ProcessoReenquadramento reenquadramento)  {
		boolean todosDaePagos = true;
		List<BoletoDaeRequerimento> boletosDae = boletoDaeRequerimentoDAOImpl.getBoletosPorIdeProcessoReenquadramento(reenquadramento.getIdeProcesso().getIdeProcesso());
		if(!Util.isNullOuVazio(boletosDae)){ 
			List<ComprovantePagamentoDae> comprovantesDae = comprovantePagamentoDaeImpl.obterPorIdeProcessoReenquadramento(reenquadramento.getIdeProcessoReenquadramento());
			if(boletosDae.size() > comprovantesDae.size()) {
				todosDaePagos = false;
			}
			else{
				for(ComprovantePagamentoDae comp : comprovantesDae) {
					if(!comp.getIndComprovanteValidado()){
						todosDaePagos = false;
						break;
					}
				}
			}
		}
		return todosDaePagos;
	}

	private boolean verificarPagamentoBoletoDAERequerimento(Requerimento requerimento)  {
		boolean todosDaePagos = true;
		List<BoletoDaeRequerimento> boletosDae = boletoDaeRequerimentoDAOImpl.getBoletosPorIdeRequerimento(requerimento.getIdeRequerimento());
		if(!Util.isNullOuVazio(boletosDae)) {
			List<ComprovantePagamentoDae> comprovantesDae = comprovantePagamentoDaeImpl.obterPorIdRequerimento(requerimento.getIdeRequerimento());
			if(boletosDae.size() != comprovantesDae.size()) {
				todosDaePagos = false;
			}
			else{
				for(ComprovantePagamentoDae comp : comprovantesDae) { 
					if(!comp.getIndComprovanteValidado()){
						todosDaePagos = false;
						break;
					}
				}
			}
		}
		return todosDaePagos;
	}

	private boolean verificarPagamentoBoletoComplementarRequerimento(Requerimento requerimento)  {
		boolean todosBoletosComplementaresPagos = true;
		List<BoletoPagamentoRequerimento> boletosComplementar = boletoPagamentoRequerimentoDAOImpl.listarBoletosComplementarPorIdeRequerimento(requerimento.getIdeRequerimento());
		if(!Util.isNullOuVazio(boletosComplementar)) {
			for(BoletoPagamentoRequerimento bpr : boletosComplementar) {
				if(Util.isNull(bpr.getDtcPagamento())) {
					todosBoletosComplementaresPagos = false;
					break;
				}
			}
		}
		return todosBoletosComplementaresPagos;
	}
	
	private boolean verificarPagamentoBoletoRequerimento(Requerimento requerimento)  {
		boolean boletoRequerimentoPago = true;
		List<BoletoPagamentoRequerimento> boletoRequerimento = boletoPagamentoRequerimentoDAOImpl.listarBoletosPorIdeRequerimento(requerimento.getIdeRequerimento());
		if(!Util.isNullOuVazio(boletoRequerimento)) {
			for(BoletoPagamentoRequerimento bpr : boletoRequerimento) {
				if(Util.isNull(bpr.getDtcPagamento())) {
					boletoRequerimentoPago = false;
					break;
				}
			}
		}
		return boletoRequerimentoPago;
	}

}
