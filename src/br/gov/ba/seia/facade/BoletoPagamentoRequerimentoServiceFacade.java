package br.gov.ba.seia.facade;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dao.CampoLivreBopepoImpl;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.NumeroDocumentoFormatterUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoPagamentoRequerimentoServiceFacade {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	private static final String CONVENIO = "2294949";
	private static final Integer CONTA = 992637;
	private static final String DIGITO_CONTA = "2";
	private static final Integer AGENCIA = 3832;
	private static final String DIGITO_AGENCIA = "6";
	private static final String CARTEIRA = "17";
	private static final String CEDENTE = "Instituto do Meio Ambiente e Recursos Hídricos - INEMA";
	private static final String LOCAL_PAGAMENTO = "Pagavel em qualquer banco até o vencimento.";
	private static final String INSTRUCAO_1 = "Este documento só terá validade de quitação após compensação bancária.";
	private static final String INSTRUCAO_2 = "Qualquer irregularidade implicará no cancelamento do respectivo documento.";
	private static final String INSTRUCAO_3 = "ATENÇÃO: Após pagamento, solicitamos a remessa de cópia autenticada mecanicamente";
	private static final String INSTRUCAO_4 = "ou cópia do boleto quitado através depósito, por meio do fax (71)3117-1244, a fim de pos-";
	private static final String INSTRUCAO_5 = "sibilitar o arquivamento do processo administrativo ou solicitar baixa do processo judicial.";
	private static final String INSTRUCAO_6 = "O boleto de pagamento é referente apenas aos atos de Licenciamento e Outorga de água.";
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final String CNPJ = "13.700.575/0001-69";
	@Inject
	private EmailUtil sendEmail;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	@EJB
	private EnderecoFacade enderecoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarBoletoEnviandoEmail(BoletoPagamentoRequerimento pBoletoPagamentoRequerimento, Requerimento pRequerimento, Boolean showEmissaoDae) throws Exception {
		Requerimento requerimentoPassadoNoMetodo = pRequerimento;
		pRequerimento = requerimentoService.buscarEntidadePorExemplo(pRequerimento);
		//se nao conseguir recuperar por exemplo, tenta recuperar pelo ID.
		if (Util.isNull(pRequerimento)) {
			pRequerimento = requerimentoService.buscarEntidadePorId(requerimentoPassadoNoMetodo);
		}
		// Associa o requerimento ao boleto
		pBoletoPagamentoRequerimento.setIdeRequerimento(pRequerimento);
		// Configura data de emissão
		pBoletoPagamentoRequerimento.setDtcEmissao(new Date());
		boletoPagamentoRequerimentoService.salvar(pBoletoPagamentoRequerimento);
		//numBoleto
		pBoletoPagamentoRequerimento.setNumBoleto(gerarNumeroBoleto(pBoletoPagamentoRequerimento));
		boletoPagamentoRequerimentoService.atualizar(pBoletoPagamentoRequerimento);
		// Inserir nova tramitação com status 6 pagto liberado
		salvarTramitacaoPagamentoLiberado(pRequerimento,pBoletoPagamentoRequerimento.getIdePessoa());
		this.enviaEmailPessoaEmpreendimento(pRequerimento);
		
	}

	public void salvarTramitacaoPagamentoLiberado(Requerimento pRequerimento,Pessoa pessoa) throws Exception {
		StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus());
		tramitacaoRequerimentoService.criarTramitacaoRequerimento(pRequerimento, status,pessoa);
	}

	private String gerarNumeroBoleto(BoletoPagamentoRequerimento pBoletoPagamentoRequerimento) {
		StringBuilder numeroBoleto = new StringBuilder();
		numeroBoleto.append(CONVENIO);
		String sequencia = pBoletoPagamentoRequerimento.getIdeBoletoPagamentoRequerimento().toString();
		sequencia = Util.lpad(sequencia, '0', 10);
		numeroBoleto.append(sequencia);
		// ZCR/RN0029
		// O Nosso Número (campo do boleto de pagamento) deve possuir o formato
		// 2294949XXXXXXXXXX onde o �2294949� � o n�mero atual do conv�nio e o
		// �XXXXXXXXXX� � um n�mero sequencial (10 digitos) iniciando em 10001
		// (dez mil e um). O primeiro boleto a ser gerado pelo SEIA ter� o
		// n�mero �22949490000010001�, o segundo �22949490000010002�, o terceiro
		// �22949490000010003� e assim por diante.
		return numeroBoleto.toString();
	}

	private void enviaEmailPessoaEmpreendimento(Requerimento pRequerimento) throws Exception {
		Requerimento req = requerimentoService.carregar(pRequerimento);
		for (RequerimentoPessoa lPessoa : req.getRequerimentoPessoaCollection()) {
 
			
			if (Util.isNull(req.getBoletoDaeRequerimento())) {
				if (!Util.isNull(lPessoa.getPessoa()) && lPessoa.getIdeTipoPessoaRequerimento() != null && !lPessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.ATENDENTE.getId())) {
					this.enviarEmailBoletoLiberado(lPessoa.getPessoa(), lPessoa.getPessoa().getDesEmail(), req);
				}
			} else {
				enviarEmailBoletoDaeLiberado(req, lPessoa);
			}
		}
	}

	public void enviarEmailBoletoDaeLiberado(Requerimento req) throws Exception {
		Requerimento pRequerimento = requerimentoService.carregar(req);
		if(!Util.isNullOuVazio(pRequerimento.getRequerimentoPessoaCollection())){
			for (RequerimentoPessoa lPessoa : pRequerimento.getRequerimentoPessoaCollection()) {
				this.enviarEmailBoletoDaeLiberado(pRequerimento, lPessoa);
			}
		}
	}
	
	
	public void enviarEmailBoletoDaeLiberado(Requerimento req, RequerimentoPessoa lPessoa) throws Exception {
		if (!Util.isNull(lPessoa.getPessoa()) && lPessoa.getIdeTipoPessoaRequerimento() != null && !lPessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.ATENDENTE.getId())) {
			this.enviarEmailBoletoLiberadoDae(lPessoa.getPessoa(), lPessoa.getPessoa().getDesEmail(), req, req.getBoletoDaeRequerimento().getVlrTotalCertificado(),
					req.getBoletoDaeRequerimento().getVlrTotalVistoria());
		}
	}

	private void enviarEmailBoletoLiberado(Pessoa pessoa, String pEmail, Requerimento requerimento) throws Exception {
		final String lAssunto = "[SEIA] - Boleto de pagamento do Requerimento de nº " + requerimento.getNumRequerimento();
		final StringBuilder lMsg = new StringBuilder();
		lMsg.append("Prezado(a) ");
		if(!Util.isNullOuVazio(pessoa.getPessoaFisica()))
			lMsg.append(pessoa.getPessoaFisica().getNomPessoa());
		else
			lMsg.append(pessoa.getPessoaJuridica().getNomeFantasia());
		lMsg.append(", \nO boleto de pagamento do Requerimento de nº '");
		lMsg.append(requerimento.getNumRequerimento());
		lMsg.append("' já está disponível.\n");
		lMsg.append("Favor acessar o SEIA para efetuar o download do boleto.\n");
		lMsg.append("Atte.,\n");
		lMsg.append("Central de Atendimento/INEMA.\n");
		ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
		comunicacaoRequerimento.setDtcComunicacao(new Date());
		comunicacaoRequerimento.setDesMensagem(lMsg.toString());
		comunicacaoRequerimento.setIdeRequerimento(requerimento);
		comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		try{
			sendEmail.enviarEmail(pEmail, lAssunto, lMsg.toString());
		}catch (Exception e) {
			JsfUtil.addSuccessMessage(BUNDLE.getString("requerimento_msg_boleto_sucesso"));
			RequestContext.getCurrentInstance().execute("dialogBoletoIncluir.hide()");
			RequestContext.getCurrentInstance().execute("confirmacaoBoleto.hide()");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	private void enviarEmailBoletoLiberadoDae(Pessoa pessoa, String pEmail, Requerimento requerimento, BigDecimal vlrCertificado, BigDecimal vlrVistoria) throws Exception {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		String vlrCerificadoFormatado = df.format(vlrCertificado);
		String vlrVistoriaFormatado = df.format(vlrVistoria);
		final String lAssunto = "[SEIA] - Boleto de pagamento do Requerimento de nº " + requerimento.getNumRequerimento();
		final StringBuilder lMsg = new StringBuilder();
		lMsg.append("Prezado(a) ");
		if(!Util.isNullOuVazio(pessoa.getPessoaFisica()))
			lMsg.append(pessoa.getPessoaFisica().getNomPessoa());
		else
			lMsg.append(pessoa.getPessoaJuridica().getNomRazaoSocial());
		lMsg.append(", \nO boleto de pagamento do Requerimento de nº '");
		lMsg.append(requerimento.getNumRequerimento());
		lMsg.append("' já está disponível.\n");
		lMsg.append("Favor acessar o SEIA para efetuar o download do boleto e o envio do comprovante de pagamento do DAE.\n");
		lMsg.append("É necessário também o pagamento de DAE (Documento de Arrecadação Estadual) nos seguintes valores e códigos:\n");
		lMsg.append("R$ ");
		lMsg.append(vlrCerificadoFormatado);
		lMsg.append(" no código 2214 \n");
		if (!Util.isNullOuVazio(vlrVistoria) && vlrVistoria.compareTo(new BigDecimal("0.00")) != 0) {
			lMsg.append("R$ ");
			lMsg.append(vlrVistoriaFormatado);
			lMsg.append(" no código 2345 \n");
		}
		lMsg.append("A geração de DAE é feita a partir do site da SEFAZ (www.sefaz.ba.gov.br) no seguinte caminho: \"Inspetoria eletrônica - Pagamentos - Cálculo e geração DAE - Taxas. \n");
		lMsg.append("Atte.,\n");
		lMsg.append("Central de Atendimento/INEMA.\n");
		ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
		comunicacaoRequerimento.setDtcComunicacao(new Date());
		comunicacaoRequerimento.setDesMensagem(lMsg.toString());
		comunicacaoRequerimento.setIdeRequerimento(requerimento);
		comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		try{
			sendEmail.enviarEmail(pEmail, lAssunto, lMsg.toString());
		}catch (Exception e) {
			JsfUtil.addSuccessMessage(BUNDLE.getString("requerimento_msg_boleto_sucesso"));
			RequestContext.getCurrentInstance().execute("dialogBoletoIncluir.hide()");
			RequestContext.getCurrentInstance().execute("confirmacaoBoleto.hide()");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoViewer gerarBoleto(BoletoPagamentoRequerimento pBoletoPagamento) throws Exception {
		TipoPessoaRequerimento lTipoPessoa = new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId());
		RequerimentoPessoa lReqPessoa = requerimentoPessoaService.obterPorRequerimentoETipoPessoa(pBoletoPagamento.getIdeRequerimento(), lTipoPessoa);
		Sacado sacado = null;
		if (!Util.isNull(lReqPessoa.getPessoa().getPessoaFisica())) {
			sacado = new Sacado(lReqPessoa.getPessoa().getPessoaFisica().getNomPessoa(), NumeroDocumentoFormatterUtil.formatarCpf(lReqPessoa.getPessoa().getPessoaFisica().getNumCpf()));
		} else if (!Util.isNull(lReqPessoa.getPessoa().getPessoaJuridica())) {
			sacado = new Sacado(lReqPessoa.getPessoa().getPessoaJuridica().getNomRazaoSocial(), NumeroDocumentoFormatterUtil.formatarCnpj(lReqPessoa.getPessoa().getPessoaJuridica().getNumCnpj()));
		}
		Cedente cedente = new Cedente(CEDENTE, CNPJ);
		// Informando o endereço do sacador.
		EnderecoPessoa enderecoPessoa = enderecoService.filtrarEnderecoByPessoa(lReqPessoa.getPessoa());
		if (!Util.isNull(enderecoPessoa)) {
			Endereco endereco = enderecoPessoa.getIdeEndereco();
			Logradouro logradouro = enderecoService.filtrarLogradouroById(endereco.getIdeLogradouro());
			org.jrimum.domkee.comum.pessoa.endereco.Endereco enderecoSac = new org.jrimum.domkee.comum.pessoa.endereco.Endereco();
			enderecoSac.setUF(UnidadeFederativa.valueOf(logradouro.getIdeMunicipio().getIdeEstado().getDesSigla()));
			enderecoSac.setLocalidade(logradouro.getIdeMunicipio().getNomMunicipio());
			enderecoSac.setCep(new CEP(logradouro.getNumCepFormatado()));
			enderecoSac.setBairro(logradouro.getIdeBairro().getNomBairro());
			enderecoSac.setLogradouro(logradouro.getIdeTipoLogradouro().getNomTipoLogradouro() + " " + logradouro.getNomLogradouro());
			enderecoSac.setNumero(endereco.getNumEndereco());
			sacado.addEndereco(enderecoSac);
		}
		SacadorAvalista sacadorAvalista = new SacadorAvalista("");
		Collection<org.jrimum.domkee.comum.pessoa.endereco.Endereco> enderecos = new ArrayList<org.jrimum.domkee.comum.pessoa.endereco.Endereco>();
		enderecos.add(new org.jrimum.domkee.comum.pessoa.endereco.Endereco());
		sacadorAvalista.setEnderecos(enderecos);
		// Informando dados sobre a conta bancária do título.
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_DO_BRASIL.create());
		contaBancaria.setNumeroDaConta(new NumeroDaConta(CONTA, DIGITO_CONTA));
		Integer carteiraInt = Integer.valueOf(CARTEIRA);
		contaBancaria.setCarteira(new Carteira(carteiraInt));
		contaBancaria.setAgencia(new Agencia(AGENCIA, DIGITO_AGENCIA));
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
		titulo.setNumeroDoDocumento(Util.zeroLTrim(pBoletoPagamento.getNumBoleto().substring(pBoletoPagamento.getNumBoleto().length() - 10)));
		titulo.setNossoNumero(pBoletoPagamento.getNumBoleto());
		pBoletoPagamento.setValBoletoOutorga(Util.isNullOuVazio(pBoletoPagamento.getValBoletoOutorga()) ? new BigDecimal(0) : pBoletoPagamento.getValBoletoOutorga());
		titulo.setDataDoDocumento(pBoletoPagamento.getDtcEmissao());
		titulo.setDataDoVencimento(pBoletoPagamento.getDtcVencimento());
		titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
		titulo.setAceite(Titulo.Aceite.N);
		CampoLivreBopepoImpl campoLivre = new CampoLivreBopepoImpl(CONTA, titulo.getNossoNumero(), CARTEIRA);
		Boleto boleto = new Boleto(titulo, campoLivre);
		boleto.setInstrucao1(INSTRUCAO_1);
		boleto.setInstrucao2(INSTRUCAO_2);
		boleto.setInstrucao3(INSTRUCAO_3);
		boleto.setInstrucao4(INSTRUCAO_4);
		boleto.setInstrucao5(INSTRUCAO_5);
		boleto.setInstrucao6(INSTRUCAO_6);
		boleto.setInstrucao7("O valor referente aos Atos de Licenciamento R$ " + Util.getDecimalFormatPtBr().format(pBoletoPagamento.getValBoleto()) + " e Outorga de água R$ " + Util.getDecimalFormatPtBr().format(pBoletoPagamento.getValBoletoOutorga()) + ".");
		boleto.setLocalPagamento(LOCAL_PAGAMENTO);
		boleto.setDataDeProcessamento(pBoletoPagamento.getDtcEmissao());
		File boletoPDF = new File(new RelatorioUtil().retornaCaminhoRelatorio() + "BoletoTemplateSemSacadorAvalista.pdf");
		BoletoViewer boletoViewer = new BoletoViewer(boleto, boletoPDF);
		return boletoViewer;
	}
	
	public BoletoPagamentoRequerimento carregarByRequerimento(Integer ideRequerimento) throws Exception {
		List<BoletoPagamentoRequerimento> listaBolPagReq = this.boletoPagamentoRequerimentoService.carregarByRequerimento(ideRequerimento,false);
		if(!Util.isNullOuVazio(listaBolPagReq)){
			return listaBolPagReq.get(listaBolPagReq.size()-1);
		}else
			return null;
	}
	
}