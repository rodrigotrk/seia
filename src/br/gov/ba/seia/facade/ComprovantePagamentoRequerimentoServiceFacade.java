package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.ComprovantePagamentoDTO;
import br.gov.ba.seia.dto.ComprovantePagamentoDaeDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComprovantePagamentoRequerimentoServiceFacade {
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private ProcessoRequerimentoServiceFacade processoRequerimentoServiceFacade;
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	
	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private EmailService emailService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registraComprovante(ComprovantePagamento pComprovantePagamento, Requerimento pRequerimento) throws Exception {
		pComprovantePagamento.setIdeBoletoPagamentoRequerimento(pRequerimento.getBoletoPagamentoRequerimento());
		ComprovantePagamentoDTO comprovante = obterPorRequerimento(pRequerimento);
		if(!Util.isNullOuVazio(comprovante)){
			pComprovantePagamento.setIdeComprovantePagamento(comprovante.getComprovantePagamento().getIdeComprovantePagamento());
		}
		// Registra o comprovante
		comprovantePagamentoService.salvarAtualizar(pComprovantePagamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registraComprovanteDae(ComprovantePagamentoDae comprovantePagamentoDae, Requerimento requerimento) throws Exception {

		comprovantePagamentoDae.setIdeBoletoDaeRequerimento(requerimento.getBoletoDaeRequerimento());
		
		List<ComprovantePagamentoDaeDTO> comprovantes = obterPorRequerimentoDae(requerimento);
		if(!Util.isNullOuVazio(comprovantes)){
			for(ComprovantePagamentoDaeDTO comprovante : comprovantes){
				if (!comprovante.getComprovantePagamentoDae().getIndComprovanteValidado() &&(
						(comprovante.getComprovantePagamentoDae().getDscCaminhoArquivo().contains("certificado") && comprovantePagamentoDae.getDscCaminhoArquivo().contains("certificado"))||
						comprovante.getComprovantePagamentoDae().getDscCaminhoArquivo().contains("vistoria") && comprovantePagamentoDae.getDscCaminhoArquivo().contains("vistoria")) ){
					comprovantePagamentoDae.setIdeComprovantePagamentoDae(comprovante.getComprovantePagamentoDae().getIdeComprovantePagamentoDae());
				}
			}
		}
		// Registra o comprovante
		comprovantePagamentoDaeService.salvarOuAtualizar(comprovantePagamentoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComprovantePagamentoDTO obterPorRequerimento(Requerimento pRequerimento)  {
		ComprovantePagamento comprovante = comprovantePagamentoService.obterPorRequerimento(pRequerimento);
		if(Util.isNull(comprovante)){
			return null;
		}else{
			return new ComprovantePagamentoDTO(comprovante);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ComprovantePagamentoDaeDTO> obterPorRequerimentoDae(Requerimento pRequerimento) {
		List<ComprovantePagamentoDaeDTO> lista = new ArrayList<ComprovantePagamentoDaeDTO>();
		for (ComprovantePagamentoDae comprovantePagamentoDae : comprovantePagamentoDaeService.obterPorRequerimento(pRequerimento)) {
			lista.add(new ComprovantePagamentoDaeDTO(comprovantePagamentoDae));
		}
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void validarComprovanteGerarProcesso(ComprovantePagamento pComprovantePagamento, List<ComprovantePagamentoDaeDTO> comprovantePagamentosDae, RequerimentoUnico pRequerimento, Area pArea,Pessoa requerente) throws Exception {
		
		pComprovantePagamento.setIndComprovanteValidado(true);
		pComprovantePagamento.setDtcValidacao(new Date());
		
		// Registra o comprovante
		comprovantePagamentoService.salvarAtualizar(pComprovantePagamento);
		
		salvarComprovantePagamentoDAE(pComprovantePagamento.getIdePessoaValidacao(), comprovantePagamentosDae);
		
		gerarProcesso(pComprovantePagamento.getIdePessoaValidacao(), pRequerimento, pArea,requerente);
		
		
		
	}

	public void salvarComprovantePagamentoDAE(Pessoa pessoa, List<ComprovantePagamentoDaeDTO> comprovantePagamentosDae)
			throws Exception {
		if (!Util.isNull(comprovantePagamentosDae)) {
			for (ComprovantePagamentoDaeDTO comprovantePagamentoDae : comprovantePagamentosDae) {
				comprovantePagamentoDae.getComprovantePagamentoDae().setIdePessoaValidacao(pessoa);
				validarComprovanteDae(comprovantePagamentoDae.getComprovantePagamentoDae());
			}
		}
	}

	public void gerarProcesso(Pessoa pessoaValidacao, RequerimentoUnico pRequerimento, Area pArea,Pessoa requerente) throws Exception {
		// Inserir nova tramita��o com status 8 pago
		StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.FORMADO.getStatus());
		tramitacaoRequerimentoService.criarTramitacaoRequerimento(pRequerimento.getRequerimento(), status, pessoaValidacao);
		
		// gerar processo
		if(!this.processoRequerimentoServiceFacade.hasProcesso(pRequerimento.getIdeRequerimentoUnico())){
			processoRequerimentoServiceFacade.gerarProcesso(pRequerimento.getRequerimento(), pArea);
		}
		Exception erro =null;
		try {
			
			if(requerimentoComAtoDeclaratorio(pRequerimento)){
				this.enviarEmailGeracaoCertificado(requerente, pRequerimento.getRequerimento(), getAtoDeclaratorio(pRequerimento));
			}else{
				this.enviarEmailFormacaoProcesso(requerente,pRequerimento.getRequerimento());
			}
			
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		
	}
	
	private AtoAmbiental getAtoDeclaratorio(RequerimentoUnico requerimentoUnico){
		for (AtoAmbiental atoAmbiental : requerimentoUnico.getAtosAmbientais()) {
			if(atoAmbiental.getIndDeclaratorio()){
				return atoAmbiental;
			}
		}
		return null;
	}
	
	private boolean requerimentoComAtoDeclaratorio(RequerimentoUnico requerimentoUnico){
		for (AtoAmbiental atoAmbiental : requerimentoUnico.getAtosAmbientais()) {
			if(atoAmbiental.getIndDeclaratorio()){
				return true;
			}
		}
		return false;
	}
	
	private void enviarEmailFormacaoProcesso(Pessoa requerente, Requerimento requerimento) throws Exception {
		String nome = consultarNomeRequerente(requerente);	
		String numProcesso = consultarNumeroProcesso(requerimento);
		String msg = gerarMensagemEmailFormacaoProcesso(requerimento, nome, numProcesso);
		try{
			requerimento = requerimentoService.buscarEntidadePorId(requerimento);
			salvarComunicacaoComRequerente(requerimento, msg);
			emailService.enviarEmailsAoRequerente(requerimento, "Processo formado"+numProcesso, msg);
			//email.enviarEmail(null,null, email.carregarListaEmails(requerimento), " Processo formado "+numProcesso, msg);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao enviar o email.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public void enviarEmailPendenciaValidacao(Requerimento requerimento,String textoEmail){
		try {
			salvarComunicacaoComRequerente(requerimento, textoEmail);
			emailService.enviarEmailsAoRequerente(requerimento, "Pendência Validação do Comprovante de Pagamento do Requerimento "+requerimento.getNumRequerimento(), textoEmail);
		} catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void salvarComunicacaoComRequerente(Requerimento requerimento, String msg) throws Exception {
		try {
			ComunicacaoRequerimento comunicacao = gerarComunicacaoRequerente(requerimento, msg);		
			comunicacaoRequerimentoService.salvar(comunicacao);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao enviar o email.");
			throw e;
		}
	}

	private String gerarMensagemEmailFormacaoProcesso(Requerimento requerimento, String nome, String numProcesso) {
		String msg = "Prezado(a) "+ nome  +", \n"+
		             "O Processo "+ numProcesso +" solicitado por meio do requerimento nº "+requerimento.getNumRequerimento()+"  foi formado no dia "+ Util.formatData(new Date()) +" \n"+
				     "Para acompanhar o seu andamento acesse o SEIA.\n\n"+
		             "Atte., \n"+
				     "Central de Atendimento/INEMA.";
		return msg;
	}

	private String consultarNumeroProcesso(Requerimento requerimento) {
		Exception erro =null;
		try {
			return processoService.buscarPorRequerimento(requerimento.getIdeRequerimento()).getNumProcesso();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao recuperar processo para envio de e-mail.");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return "";
	}

	private String consultarNomeRequerente(Pessoa requerente) {
		String nome;
		if(!Util.isNullOuVazio(requerente.getPessoaFisica())){
			nome = requerente.getPessoaFisica().getNomPessoa();
		}else{
			nome = requerente.getPessoaJuridica().getNomRazaoSocial();
		}
		return nome;
	}

	private void enviarEmailGeracaoCertificado(Pessoa requerente, Requerimento requerimento,AtoAmbiental atoAmbiental) throws Exception {
		String nome = consultarNomeRequerente(requerente);	
		String msg = gerarMensagemEmailGeracaoCertificado(requerimento, nome,atoAmbiental);
		try{
			requerimento = requerimentoService.buscarEntidadePorId(requerimento);
			salvarComunicacaoComRequerente(requerimento, msg);
			emailService.enviarEmailsAoRequerente(requerimento, "Certificado Gerado "+requerimento.getNumRequerimento(), msg);
			//email.enviarEmail(null,null, email.carregarListaEmails(requerimento), " Certificado Gerado "+requerimento.getNumRequerimento(), msg);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao enviar o email.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}
	
	private String gerarMensagemEmailGeracaoCertificado(Requerimento requerimento, String nome,AtoAmbiental atoAmbiental) {
		String msg = "Prezado(a) "+ nome  +", \n\n"+
				"Informamos que o certificado de "+atoAmbiental.getNomAtoAmbiental()+" referente ao requerimento de número " 
				 +requerimento.getNumRequerimento()+" está disponível no sistema SEIA.\nAcesse o sistema e consulte-o.\n\n"+
	             "Atte., \n"+
			     "Central de Atendimento/INEMA.";
		return msg;
	}
	
	private ComunicacaoRequerimento gerarComunicacaoRequerente(Requerimento requerimento, String msg) {
		ComunicacaoRequerimento comunicacao = new ComunicacaoRequerimento();
		comunicacao.setDesMensagem(msg);
		comunicacao.setDtcComunicacao(new Date());
		comunicacao.setIdeRequerimento(requerimento);
		return comunicacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void validarComprovanteDae(ComprovantePagamentoDae pComprovantePagamento)  {
		pComprovantePagamento.setIndComprovanteValidado(true);
		pComprovantePagamento.setDtcValidacao(new Date());
		
		// Registra o comprovante
		comprovantePagamentoDaeService.salvarOuAtualizar(pComprovantePagamento);
	}
}