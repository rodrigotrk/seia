package br.gov.ba.seia.service.facade;

import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ComunicacaoProcesso;
import br.gov.ba.seia.entity.ComunicacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.interfaces.ComunicacaoRequerenteInterface;
import br.gov.ba.seia.service.EmailService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoServiceFacade {

	@Inject
	private IDAO<ComunicacaoRequerenteInterface> daoComunicacao;
	
	@EJB
	private EmailService emailService;

	public void gerarComunicacao(Object obj, Map<String, String> mapEmail) {
		
		Requerimento requerimento = null;
		ComunicacaoRequerenteInterface comunicacao = new ComunicacaoRequerimento();
		
		if(obj instanceof Requerimento){
			requerimento = (Requerimento) obj;
			comunicacao = montarComunicacaoRequerente(requerimento, mapEmail);
		}
		else if(obj instanceof Processo){
			Processo processo = (Processo) obj;
			requerimento = processo.getIdeRequerimento();
			comunicacao = montarComunicacaoRequerente(processo);
		}
		else if(obj instanceof ProcessoReenquadramento){
			ProcessoReenquadramento processoReenquadramento = (ProcessoReenquadramento) obj;
			requerimento = processoReenquadramento.getIdeProcesso().getIdeRequerimento();
			comunicacao = montarComunicacaoRequerente(processoReenquadramento);
		}
		comunicacao.setDtcComunicacao(new Date());
		comunicacao.setDesMensagem(mapEmail.get("mensagem"));
		
		salvar(comunicacao);
		enviarEmail(requerimento, mapEmail);
	}
	
	private ComunicacaoRequerimento montarComunicacaoRequerente(Requerimento requerimento, Map<String, String> mapEmail) {
		ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
		comunicacaoRequerimento.setIdeRequerimento(requerimento);
		comunicacaoRequerimento.setAssunto(mapEmail.get("assunto"));
		comunicacaoRequerimento.setIndEnviado(false);
		return comunicacaoRequerimento;
	}

	private ComunicacaoReenquadramentoProcesso montarComunicacaoRequerente(ProcessoReenquadramento processoReenquadramento) {
		return new ComunicacaoReenquadramentoProcesso(processoReenquadramento);
	}
	private ComunicacaoProcesso montarComunicacaoRequerente(Processo processo) {
		return new ComunicacaoProcesso(processo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	private void salvar(ComunicacaoRequerenteInterface comunicacaoReq) {
		daoComunicacao.salvar(comunicacaoReq);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void enviarEmail(Requerimento requerimento, Map<String, String> mapEmail ) {
		emailService.enviarEmailsAoRequerente(requerimento, mapEmail.get("assunto"), mapEmail.get("mensagem"));
	}
}
