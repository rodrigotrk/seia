package br.gov.ba.seia.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.DadoBancario;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.BancoEnum;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoService {

	@Inject
	private IDAO<BoletoPagamentoHistorico> boletoPagamentoHistoricoDAO;
	
	@Inject
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;

	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;

	@EJB
	private DadoBancarioService dadoBancarioService;
	
	@EJB
	private EmailService emailService;

	public void salvarBoleto(BoletoPagamentoRequerimento boleto){
		this.boletoPagamentoRequerimentoService.salvar(boleto);
		
		if(boleto.getDetalhamentoBoletoCollection() != null) {
			this.boletoPagamentoRequerimentoService.salvar(boleto.getDetalhamentoBoletoCollection());
		}

		if(!boleto.getIndIsento()) {
			String numeroBoleto = this.gerarNumeroBoleto(boleto);
			boleto.setNumBoleto(numeroBoleto);
		}
		
		this.boletoPagamentoRequerimentoService.atualizar(boleto);
	}
	
	public void salvarBoletoPagHistorico(BoletoPagamentoHistorico boletoPagHist)  {
		this.boletoPagamentoHistoricoDAO.salvarOuAtualizar(boletoPagHist);
	}

	private String gerarNumeroBoleto(BoletoPagamentoRequerimento pBoletoPagamentoRequerimento) {
		StringBuilder numeroBoleto = new StringBuilder();
		DadoBancario dadoBancario = this.dadoBancarioService.carregarBancoBoleto(BancoEnum.BANCO_DO_BRASIL);
		numeroBoleto.append(dadoBancario.getConvenio());
		String sequencia = pBoletoPagamentoRequerimento.getIdeBoletoPagamentoRequerimento().toString();
		sequencia = Util.lpad(sequencia, '0', 10);
		numeroBoleto.append(sequencia);
		return numeroBoleto.toString();
	}

	public BoletoPagamentoRequerimento carregarByRequerimento(Integer ideRequerimento)  {
		List<BoletoPagamentoRequerimento> listaBolPagReq = this.boletoPagamentoRequerimentoService.carregarByRequerimento(ideRequerimento,false);
		if(!Util.isNullOuVazio(listaBolPagReq)){
			return listaBolPagReq.get(listaBolPagReq.size()-1);
		}
			return null;
	}

	public void atualizarComunicacaoRequerimento(BoletoPagamentoRequerimento boleto) {

		Requerimento requerimento = boleto.getIdeRequerimento();

		final String lAssunto = "[SEIA] - Boleto de pagamento do Requerimento de nº " + requerimento.getNumRequerimento();
		
		final StringBuilder mensagem = new StringBuilder();
		
		if(boleto.getIndIsento()) {
			mensagem.append("Prezado(a) " + requerimento.getRequerente().getNomeRazao());
			mensagem.append(", \nO Requerimento de nº '");
			mensagem.append(requerimento.getNumRequerimento());
			mensagem.append("' foi insento do valor referente as taxas das solicitações realizadas no SEIA.\n");
			mensagem.append("Atte.,\n");
			mensagem.append("Central de Atendimento/INEMA.\n");
		}
		else {
			mensagem.append("Prezado(a) " + requerimento.getRequerente().getNomeRazao());
			mensagem.append(", \nO boleto de pagamento do Requerimento de nº '");
			mensagem.append(requerimento.getNumRequerimento());
			mensagem.append("' já está disponível.\n");
			mensagem.append("Favor acessar o SEIA para efetuar o download do boleto.\n");
			mensagem.append("Atte.,\n");
			mensagem.append("Central de Atendimento/INEMA.\n");
		}
		

		ComunicacaoRequerimento comunicacaoRequerimento = gerarComunicacao(requerimento, lAssunto, mensagem);
		this.comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		
		emailService.enviarEmailsAoRequerente(requerimento, lAssunto, mensagem.toString());
	}

	public void atualizarPendenciaComunicacaoRequerimento(Requerimento requerimento) {
		
		final String lAssunto = "[SEIA] - Pendência de envio de comprovante de pagamento do requerimento nº " + requerimento.getNumRequerimento();
		
		final StringBuilder mensagem = new StringBuilder();
		mensagem.append("Prezado(a) ");
		mensagem.append(", \n Favor acessar o SEIA para verificar pendências no comprovante de pagamento enviado do requerimento nº " + requerimento.getNumRequerimento());
		mensagem.append("Atte.,\n");
		mensagem.append("Central de Atendimento/INEMA.\n");

		ComunicacaoRequerimento comunicacaoRequerimento = gerarComunicacao(requerimento, lAssunto, mensagem);
		this.comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		
		emailService.enviarEmailsAoRequerente(requerimento, lAssunto, mensagem.toString());
	}

	private ComunicacaoRequerimento gerarComunicacao(Requerimento requerimento, final String lAssunto, final StringBuilder mensagem) {
		ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
		comunicacaoRequerimento.setDtcComunicacao(new Date());
		comunicacaoRequerimento.setDesMensagem(mensagem.toString());
		comunicacaoRequerimento.setIdeRequerimento(requerimento);
		comunicacaoRequerimento.setAssunto(lAssunto);
		return comunicacaoRequerimento;
	}
	
	public void salvarBoletoManualmente(BoletoPagamentoRequerimento boleto)  {
		this.boletoPagamentoRequerimentoService.salvar(boleto);
		
		if(boleto.getDetalhamentoBoletoCollection() != null) {
			this.boletoPagamentoRequerimentoService.salvar(boleto.getDetalhamentoBoletoCollection());
		}
	}
}