package br.gov.ba.seia.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.FinalidadeReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.FinalidadeReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoPotencialPoluicaoDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoTipoFinalidadeUsoAguaDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoTipologiaDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoTipologiaEmpreendimentoDAOImpl;
import br.gov.ba.seia.dto.NotificacaoAprovacaoDTO;
import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.entity.FinalidadeReenquadramentoProcesso;
import br.gov.ba.seia.entity.ObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ReenquadramentoPotencialPoluicao;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAtoObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAtoTipoAtividadeFauna;
import br.gov.ba.seia.entity.ReenquadramentoTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.ReenquadramentoTipologia;
import br.gov.ba.seia.entity.ReenquadramentoTipologiaEmpreendimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.FinalidadeReenquadramentoEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoProcessoService {

	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private ReenquadramentoProcessoDAOImpl reenquadramentoProcessoDAOImpl;
	@EJB
	private FinalidadeReenquadramentoDAOImpl finalidadeReenquadramentoDAOImpl;
	@EJB
	private FinalidadeReenquadramentoProcessoDAOImpl finalidadeReenquadramentoProcessoDAOImpl;
	@EJB
	private ReenquadramentoProcessoAtoDAOImpl reenquadramentoProcessoAtoDAOImpl;
	@EJB
	private ReenquadramentoPotencialPoluicaoDAOImpl reenquadramentoPotencialPoluicaoDAOImpl;
	@EJB
	private ReenquadramentoTipologiaDAOImpl reenquadramentoTipologiaDAOImpl;
	@EJB
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	@EJB
	private ReenquadramentoTipologiaEmpreendimentoDAOImpl reenquadramentoTipologiaEmpreendimentoDAOImpl;
	@EJB
	private ReenquadramentoTipoFinalidadeUsoAguaDAOImpl reenquadramentoTipoFinalidadeUsoAguaDAOImpl;
	@EJB
	private ReenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl reenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl;
	@EJB
	private ReenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl reenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarReenquadramento(NotificacaoFinalDTO dto) {
		try {
			if (Util.isNull(dto.getReenquadramentoProcesso()) == false) {
				removerDependencias(dto.getReenquadramentoProcesso());
				salvarReenquadramentoProcesso(dto);
				atualizarNotificacao(dto);
			}
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarReenquadramentoProcesso(NotificacaoFinalDTO dto)  {
		
		prepararParaSalvar(dto);
		
		Collection<FinalidadeReenquadramentoProcesso> reequadramentoProcessoCollection = new ArrayList<FinalidadeReenquadramentoProcesso>();
		
		if(dto.getReenquadramentoProcesso() != null && !Util.isNullOuVazio(dto.getReenquadramentoProcesso().getFinalidadeReequadramentoProcessoCollection())){
			
			reequadramentoProcessoCollection.addAll(dto.getReenquadramentoProcesso().getFinalidadeReequadramentoProcessoCollection());
			dto.getReenquadramentoProcesso().setFinalidadeReequadramentoProcessoCollection(null);
			
			reenquadramentoProcessoDAOImpl.salvar(dto.getReenquadramentoProcesso());
			
			salvarReenquadramentoProcessoAto(dto);
			
			dto.getReenquadramentoProcesso().setFinalidadeReequadramentoProcessoCollection(reequadramentoProcessoCollection);

			for(FinalidadeReenquadramentoProcesso frp : dto.getReenquadramentoProcesso().getFinalidadeReequadramentoProcessoCollection()) {
				frp.gerarPK();
				finalidadeReenquadramentoProcessoDAOImpl.salvar(frp);
			}
			
		}
		
	}

	private void salvarReenquadramentoProcessoAto(NotificacaoFinalDTO dto)  {
		for(ReenquadramentoProcessoAto rpa : dto.getReenquadramentoProcesso().getReenquadramentoProcessoAtoCollection()) {
			
			reenquadramentoProcessoAtoDAOImpl.salvar(rpa);
			
			if(!Util.isNullOuVazio(rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection())) {
				for(ReenquadramentoTipoFinalidadeUsoAgua rtfua : rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection()) {
					rtfua.setIdeReenquadramentoProcessoAto(rpa);
					rtfua.gerarPK();
					reenquadramentoTipoFinalidadeUsoAguaDAOImpl.salvar(rtfua);
				}
			}
			
			if(!Util.isNullOuVazio(rpa.getReenquadramentoTipologiaEmpreendimentoCollection())) {
				for(ReenquadramentoTipologiaEmpreendimento rte : rpa.getReenquadramentoTipologiaEmpreendimentoCollection()) {
					rte.setIdeReenquadramentoProcessoAto(rpa);
					rte.gerarPK();
					reenquadramentoTipologiaEmpreendimentoDAOImpl.salvar(rte);
				}
			}
			
			if(!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection())) {
				for(ReenquadramentoProcessoAtoTipoAtividadeFauna rpataf : rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection()) {
					rpataf.setIdeReenquadramentoProcessoAto(rpa);
					rpataf.gerarPK();
					reenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl.salvar(rpataf);
				}
			}
			
			if(!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection())) {
				for(ReenquadramentoProcessoAtoObjetivoAtividadeManejo rpaoam : rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection()) {
					rpaoam.setIdeReenquadramentoProcessoAto(rpa);
					rpaoam.gerarPK();
					reenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl.salvar(rpaoam);
				}
			}				
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void prepararParaSalvar(NotificacaoFinalDTO dto) {
		
		ReenquadramentoProcesso reenquadramentoProcesso = dto.getReenquadramentoProcesso();
		reenquadramentoProcesso.setReenquadramentoProcessoAtoCollection(new ArrayList<ReenquadramentoProcessoAto>());
		
		if(!Util.isNullOuVazio(dto.getListaAlteracaoReenquadramentoProcessoAto())) {
			for (ReenquadramentoProcessoAto rpa : dto.getListaAlteracaoReenquadramentoProcessoAto()) {
				limparIdeAnterior(reenquadramentoProcesso, rpa);				
			}
		}
		
		if(!Util.isNullOuVazio(dto.getListaInclusaoReenquadramentoProcessoAto())) {
			for (ReenquadramentoProcessoAto rpa : dto.getListaInclusaoReenquadramentoProcessoAto()) {
				limparIdeAnterior(reenquadramentoProcesso, rpa);
			}
		}
		
		reenquadramentoProcesso.setReenquadramentoPotencialPoluicaoCollection(removerIdeReenquadramentoPotencialPoluicao(dto.getListaReenquadramentoPotencialPoluicao()));
		reenquadramentoProcesso.setReenquadramentoTipologiaCollection(dto.getListaReenquadramentoTipologia());
	}

	private void limparIdeAnterior(ReenquadramentoProcesso reenquadramentoProcesso, ReenquadramentoProcessoAto rpa) {
		
		rpa.setIdeReenquadramentoProcessoAto(null);
		reenquadramentoProcesso.getReenquadramentoProcessoAtoCollection().add(rpa);
		
		if(!Util.isNullOuVazio(rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection())) {
			for(ReenquadramentoTipoFinalidadeUsoAgua rtfua : rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection()) {
				rtfua.setIdeReenquadramentoProcessoAto(null);
				rtfua.setReenquadramentoTipoFinalidadeUsoAguaPK(null);
			}
		}
		
		if(!Util.isNullOuVazio(rpa.getReenquadramentoTipologiaEmpreendimentoCollection())) {
			for(ReenquadramentoTipologiaEmpreendimento rte : rpa.getReenquadramentoTipologiaEmpreendimentoCollection()) {
				rte.setIdeReenquadramentoProcessoAto(null);
				rte.setReenquadramentoTipologiaEmpreendimentoPK(null);
			}
		}
		
		if(!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection())) {
			for(ReenquadramentoProcessoAtoTipoAtividadeFauna rpataf : rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection()) {
				rpataf.setIdeReenquadramentoProcessoAto(null);
				rpataf.setReenquadramentoProcessoAtoTipoAtividadeFaunaPK(null);
			}
		}
		
		if(!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection())) {
			for(ReenquadramentoProcessoAtoObjetivoAtividadeManejo rpaoam : rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection()) {
				rpaoam.setIdeReenquadramentoProcessoAto(null);
				rpaoam.setReenquadramentoProcessoAtoObjetivoAtividadeManejoPK(null);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<ReenquadramentoPotencialPoluicao> removerIdeReenquadramentoPotencialPoluicao(Collection<ReenquadramentoPotencialPoluicao> listaReenquadramentoPotencialPoluicao){
		if (!Util.isNullOuVazio(listaReenquadramentoPotencialPoluicao)){
			Iterator<ReenquadramentoPotencialPoluicao> i = listaReenquadramentoPotencialPoluicao.iterator();
			while (i.hasNext()){
				ReenquadramentoPotencialPoluicao reenquadramentoPotencialPoluicao = i.next();
				reenquadramentoPotencialPoluicao.setIdeReenquadramentoPotencialPoluicao(null);
			}
		}
		return listaReenquadramentoPotencialPoluicao;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarNotificacao(NotificacaoFinalDTO dto) {
		try {
			dto.getNotificacaoFinal().setIdeReenquadramentoProcesso(dto.getReenquadramentoProcesso());
			notificacaoDAOImpl.salvarOuAtualizar(dto.getNotificacaoFinal());
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void removerDependencias(ReenquadramentoProcesso reenquadramentoProcesso)  {
		if (!Util.isNull(reenquadramentoProcesso.getIdeReenquadramentoProcesso())) {
			reenquadramentoTipologiaEmpreendimentoDAOImpl.removerPor(reenquadramentoProcesso);
			reenquadramentoTipoFinalidadeUsoAguaDAOImpl.removerPor(reenquadramentoProcesso);
			reenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl.removerPor(reenquadramentoProcesso);
			reenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl.removerPor(reenquadramentoProcesso);
			finalidadeReenquadramentoProcessoDAOImpl.removerPor(reenquadramentoProcesso);
			reenquadramentoProcessoAtoDAOImpl.removerPor(reenquadramentoProcesso);
			reenquadramentoPotencialPoluicaoDAOImpl.removerPor(reenquadramentoProcesso);
			reenquadramentoTipologiaDAOImpl.removerPor(reenquadramentoProcesso);
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarReenquadramentoProcesso(NotificacaoFinalDTO dto)  {
		
		ReenquadramentoProcesso reenquadramentoProcesso = reenquadramentoProcessoDAOImpl.buscarReenquadramentoProcesso(dto.getNotificacaoFinal());
		
		reenquadramentoProcesso.setFinalidadeReequadramentoProcessoCollection(finalidadeReenquadramentoProcessoDAOImpl.listarFinalidadeReenquadramentoProcesso(reenquadramentoProcesso));
		reenquadramentoProcesso.setReenquadramentoProcessoAtoCollection(reenquadramentoProcessoAtoDAOImpl.listarReenquadramentoProcessoAto(reenquadramentoProcesso));
		reenquadramentoProcesso.setReenquadramentoPotencialPoluicaoCollection(reenquadramentoPotencialPoluicaoDAOImpl.listarReenquadramentoPotencialPoluicao(reenquadramentoProcesso));
		reenquadramentoProcesso.setReenquadramentoTipologiaCollection(reenquadramentoTipologiaDAOImpl.listarReenquadramentoTipologia(reenquadramentoProcesso));
		
		if(!Util.isNullOuVazio(reenquadramentoProcesso.getReenquadramentoProcessoAtoCollection())) {
			dto.setListaProcessoAtoParaReenquadramento(new ArrayList<ProcessoAto>());
			dto.setListaInclusaoReenquadramentoProcessoAto(new ArrayList<ReenquadramentoProcessoAto>());
			dto.setListaAlteracaoReenquadramentoProcessoAto(new ArrayList<ReenquadramentoProcessoAto>());
			for (ReenquadramentoProcessoAto rpa : reenquadramentoProcesso.getReenquadramentoProcessoAtoCollection()) {
				if(Util.isNull(rpa.getIdeProcessoAto())) {
					dto.getListaInclusaoReenquadramentoProcessoAto().add(rpa);
				}
				else{
					dto.getListaAlteracaoReenquadramentoProcessoAto().add(rpa);
					dto.getListaProcessoAtoParaReenquadramento().add(rpa.getIdeProcessoAto());
				}
			}
		}
		
		if(!Util.isNullOuVazio(reenquadramentoProcesso.getReenquadramentoPotencialPoluicaoCollection())) {
			dto.setListaReenquadramentoPotencialPoluicao(reenquadramentoProcesso.getReenquadramentoPotencialPoluicaoCollection());
			dto.setListaRequerimentoTipologiaParaReenquadramento(new ArrayList<RequerimentoTipologia>());
			for(ReenquadramentoPotencialPoluicao rpp :dto.getListaReenquadramentoPotencialPoluicao()) {
				dto.getListaRequerimentoTipologiaParaReenquadramento().add(rpp.getIdeRequerimentoTipologia());
			}
		}
		
		dto.setListaReenquadramentoTipologia(reenquadramentoProcesso.getReenquadramentoTipologiaCollection());
		dto.setReenquadramentoProcesso(reenquadramentoProcesso);
		dto.carregarFinalidadeReenquadramento(finalidadeReenquadramentoDAOImpl.listarFinalidadeReenquadramento(reenquadramentoProcesso));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarReenquadramentoProcesso(NotificacaoAprovacaoDTO dto)  {
		
		if (!Util.isNullOuVazio(dto.getNotificacao()) && !Util.isNullOuVazio(dto.getNotificacao().getIdeNotificacao())){
			ReenquadramentoProcesso reenquadramentoProcesso = reenquadramentoProcessoDAOImpl.buscarReenquadramentoProcesso(dto.getNotificacao());
			
			if (!Util.isNullOuVazio(reenquadramentoProcesso)){
				reenquadramentoProcesso.setFinalidadeReequadramentoProcessoCollection(finalidadeReenquadramentoProcessoDAOImpl.listarFinalidadeReenquadramentoProcesso(reenquadramentoProcesso));
				reenquadramentoProcesso.setReenquadramentoProcessoAtoCollection(reenquadramentoProcessoAtoDAOImpl.listarReenquadramentoProcessoAto(reenquadramentoProcesso));
				reenquadramentoProcesso.setReenquadramentoPotencialPoluicaoCollection(reenquadramentoPotencialPoluicaoDAOImpl.listarReenquadramentoPotencialPoluicao(reenquadramentoProcesso));
				reenquadramentoProcesso.setReenquadramentoTipologiaCollection(reenquadramentoTipologiaDAOImpl.listarReenquadramentoTipologia(reenquadramentoProcesso));
				
				if(!Util.isNullOuVazio(reenquadramentoProcesso.getReenquadramentoProcessoAtoCollection())) {
					dto.setListaInclusaoReenquadramentoProcessoAto(new ArrayList<ReenquadramentoProcessoAto>());
					dto.setListaAlteracaoReenquadramentoProcessoAto(new ArrayList<ReenquadramentoProcessoAto>());
					for (ReenquadramentoProcessoAto rpa : reenquadramentoProcesso.getReenquadramentoProcessoAtoCollection()) {
						if(Util.isNull(rpa.getIdeProcessoAto())) {
							dto.getListaInclusaoReenquadramentoProcessoAto().add(rpa);
						}
						else{
							dto.getListaAlteracaoReenquadramentoProcessoAto().add(rpa);
						}
					}
				}
				dto.setListaReenquadramentoPotencialPoluicao(reenquadramentoProcesso.getReenquadramentoPotencialPoluicaoCollection());
				dto.setListaReenquadramentoTipologia(reenquadramentoProcesso.getReenquadramentoTipologiaCollection());
				dto.setReenquadramentoProcesso(reenquadramentoProcesso);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void gerarNumeroProcessoReenquadramento(ProcessoReenquadramento processoReenquadramento)  {
		StringBuilder numero = new StringBuilder();

		String ultimoNumero; 
		
		ProcessoReenquadramento ultimo = processoReenquadramentoDAOImpl.buscarUltimo();

		if (Util.isNullOuVazio(ultimo)) {
			ultimoNumero = String.valueOf(1);
		}
		else {
			ultimoNumero = String.valueOf(ultimo.getIdeProcessoReenquadramento() + 1);
		}

		numero
				.append(new SimpleDateFormat("yyyy").format(new Date()))
				.append('.');

		numero
				.append("001")
				.append('.');

		numero
				.append(Util.lpad(ultimoNumero, '0', 6))
				.append('/');

		numero 
				.append("INEMA/REENQUADRAMENTO");

		processoReenquadramento.setNumProcesso(numero.toString());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String montarTextoNotificacao(NotificacaoFinalDTO dto) {
		StringBuilder texto = new StringBuilder();
		if(dto.getReenquadramentoProcesso().possuiFinalidades()) {
			
			texto.append("Prezado(a),\n\n\n");
			texto.append("Para a continuidade da análise do processo nº "+ dto.getVwProcesso().getNumProcesso()+", será necessário o seu reenquadramento para:\n");
			
			adicionarTextoAtoAmbientalAlterado(texto, dto);
			adicionarTextoAtoAmbientalIncluido(texto, dto);
			adicionarTextoPorte(texto, dto);
			adicionarTextoClasse(texto, dto);
			adicionarTextoPotenciaPoluidor(texto, dto);
			adicionarTextoTipologia(texto, dto);
			
			texto.append("\n\nPara tanto, é necessário a edição do requerimento vinculado ao processo.\n"); 
			texto.append("Para isso, acesse o SEIA, no menu lateral à esquerda acione a aba \"PROCESSO\", em seguida \"REENQUADRAMENTO\" e siga os seguintes passos:\n\n"); 
			texto.append("Passo 1: Informe o número do processo mencionado acima no campo \"Nº do processo:\" e acione o botão \"Consultar\"\n"); 
			texto.append("Passo 2: Na coluna \"Ações\" acione o ícone \"Editar requerimento\"\n"); 
			texto.append("Passo 3: Realize as alterações necessárias conforme orientações acima;\n"); 
			texto.append("Passo 4: Acione o botão \"Finalizar requerimento\" e aguarde novas orientações.\n\n\n"); 
			texto.append("NOTA 1: Caso for necessário a inclusão de uma nova tipologia (atividade) ao empreendimento que não esteja cadastrada é necessário editar o empreendimento e incluí-la antes de editar o requerimento. Para isso, acesse o SEIA-e, no menu lateral à esquerda acione a aba \"Consultas\", em seguida \"Empreendimento\" e siga os seguintes passos:\n\n"); 
			texto.append("Passo 1: Informe o nome do empreendimento e acione o botão consultar;\n"); 
			texto.append("Passo 2: Na coluna \"Ações\" acione o ícone “Editar”;\n"); 
			texto.append("Passo 3: Na aba \"*Identificação\", no campo \"TIPOLOGIAS DA(S) ATIVIDADE(S)\" acione a funcionalidade \"Selecionar uma ou mais tipologias\";\n"); 
			texto.append("Passo 4: Selecione a(s) tipologia(s) informada(s) nessa notificação;\n"); 
			texto.append("Passo 5: Conclua a edição do empreendimento e execute os passos para edição do requerimento.\n\n\n"); 
			texto.append("NOTA 2: Ressaltamos que a modificação a ser realizada pode acarretar na alteração da taxa paga e solicitação de novos documentos e/ou estudos.\n\n\n"); 
			texto.append("NOTA 3: As modificações solicitadas devem ser realizadas em um prazo máximo de até 180 dias. Passado este período sem que elas tem sido realizadas, o processo estará passível ao arquivamento.\n\n");
			texto.append("Atenciosamente,\n"); 
			texto.append("Central de Atendimento/INEMA");
			
		}
		return texto.toString();
	}
	
	private void adicionarTextoAtoAmbientalAlterado(StringBuilder texto, NotificacaoFinalDTO dto) {
		if(dto.getReenquadramentoProcesso().possuiFinalidade(FinalidadeReenquadramentoEnum.ALTERACAO_ATOS_AUTORIZATIVOS)
		&& !Util.isNullOuVazio(dto.getListaAlteracaoReenquadramentoProcessoAto())) {
			alterarTextoAtoAmbiental(texto, dto.getListaAlteracaoReenquadramentoProcessoAto(),dto);
		}
	}

	private void adicionarTextoAtoAmbientalIncluido(StringBuilder texto, NotificacaoFinalDTO dto) {
		if(dto.getReenquadramentoProcesso().possuiFinalidade(FinalidadeReenquadramentoEnum.INCLUSAO_NOVOS_ATOS_AUTORIZATIVOS)
		&& !Util.isNullOuVazio(dto.getListaInclusaoReenquadramentoProcessoAto())) {
			incluirTextoAtoAmbiental(texto, dto.getListaInclusaoReenquadramentoProcessoAto(), dto);
		}
	}
	
	private void alterarTextoAtoAmbiental(StringBuilder texto, Collection<ReenquadramentoProcessoAto> lista,NotificacaoFinalDTO dto) {
		for (Iterator<ReenquadramentoProcessoAto> iterator = lista.iterator(); iterator.hasNext();) {
			ReenquadramentoProcessoAto rpa = (ReenquadramentoProcessoAto) iterator.next();
			if (!Util.isNullOuVazio(rpa.getIdeNovoAtoAmbiental())){
				
				texto.append("\n- Substituir o ato ".concat(rpa.getIdeProcessoAto().getAtoAmbiental().getNomAtoAmbiental()));
				
				if (!Util.isNullOuVazio(rpa.getIdeProcessoAto().getTipologia())) {
					texto.append(", ".concat(rpa.getIdeProcessoAto().getTipologia().getDesTipologia()));

				}
				
				if (!Util.isNullOuVazio(rpa.getIdeProcessoAto().getTipoFinalidadeUsoAgua())) {
					texto.append(", ".concat(rpa.getIdeProcessoAto().getTipoFinalidadeUsoAgua().getNomTipoFinalidadeUsoAgua()));
				}
				
				texto.append(" pelo ato ".concat(rpa.getIdeNovoAtoAmbiental().getNomAtoAmbiental()));
				
				if (!Util.isNullOuVazio(rpa.getIdeNovaTipologia())) {
					texto.append(", ".concat(rpa.getIdeNovaTipologia().getDesTipologia()));
				}
				
				if (!Util.isNullOuVazio(rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection())) {
					for(ReenquadramentoTipoFinalidadeUsoAgua tp : rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection()) {
						texto.append(", ".concat(tp.getIdeTipoFinalidadeUsoAgua().getNomTipoFinalidadeUsoAgua()));
					}
				}
				
				if (!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection())) {
					for(ReenquadramentoProcessoAtoObjetivoAtividadeManejo rpaoam : rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection()) {
						texto.append(", ".concat(rpaoam.getIdeObjetivoAtividadeManejo().getNomObjetivoAtividadeManejo()));
					}
					
				}
				
				if (!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection())) {
					for(ReenquadramentoProcessoAtoTipoAtividadeFauna rpataf : rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection()) {
						texto.append(", ".concat(rpataf.getIdeTipoAtividadeFauna().getNomTipoAtividadeFauna()));
					}
					
				}
				
				texto.append(";\n");
				
				if (!Util.isNullOuVazio(rpa.getDscJustificativa())) {
					texto.append("\nJustificativa para reenquadramento em Outorga:\n\n".concat(rpa.getDscJustificativa()));
					texto.append("\n");
				}
				
				incluirTextoEmpreendimento(texto, rpa, dto.getVwProcesso().getNomEmpreendimento());
			}
		}
	}
	

	private void incluirTextoAtoAmbiental(StringBuilder texto, Collection<ReenquadramentoProcessoAto> lista, NotificacaoFinalDTO dto ) {
		for (Iterator<ReenquadramentoProcessoAto> iterator = lista.iterator(); iterator.hasNext();) {
			ReenquadramentoProcessoAto rpa = (ReenquadramentoProcessoAto) iterator.next();
			if (!Util.isNullOuVazio(rpa.getIdeNovoAtoAmbiental())){
				
				texto.append("\n- Incluir o ato "+rpa.getIdeNovoAtoAmbiental().getNomAtoAmbiental());
				
				if (!Util.isNullOuVazio(rpa.getIdeNovaTipologia())) {
					texto.append(", ".concat(rpa.getIdeNovaTipologia().getDesTipologia()));
				}
				
				if (!Util.isNullOuVazio(rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection())) {
					for(ReenquadramentoTipoFinalidadeUsoAgua tp:rpa.getReenquadramentoTipoFinalidadeUsoAguaCollection()) {
						texto.append(", ".concat(tp.getIdeTipoFinalidadeUsoAgua().getNomTipoFinalidadeUsoAgua()));
					}
					
				}
				
				if (!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection())) {
					for(ReenquadramentoProcessoAtoObjetivoAtividadeManejo rpaoam : rpa.getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection()) {
						texto.append(", ".concat(rpaoam.getIdeObjetivoAtividadeManejo().getNomObjetivoAtividadeManejo()));
					}
					
				}
				
				if (!Util.isNullOuVazio(rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection())) {
					for(ReenquadramentoProcessoAtoTipoAtividadeFauna rpataf : rpa.getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection()) {
						texto.append(", ".concat(rpataf.getIdeTipoAtividadeFauna().getNomTipoAtividadeFauna()));
					}
					
				}
				
				
				texto.append(";\n");
				
				if (!Util.isNullOuVazio(rpa.getDscJustificativa())) {
					texto.append("\nJustificativa para reenquadramento em Outorga:\n\n".concat(rpa.getDscJustificativa()));
					texto.append("\n");
				}
				
				incluirTextoEmpreendimento(texto, rpa, dto.getVwProcesso().getNomEmpreendimento());
			}
		}
	}
	
	
	
	private void incluirTextoEmpreendimento(StringBuilder texto, ReenquadramentoProcessoAto rpa, String nomEmpreendimento) {
		
		StringBuilder textoEmpreendimento = new StringBuilder();
			
		if (!Util.isNullOuVazio(rpa.getIdeNovoAtoAmbiental())
				&& TipoAtoEnum.LICENCA.getId().equals(rpa.getIdeNovoAtoAmbiental().getIdeTipoAto().getIdeTipoAto())
				&& !Util.isNullOuVazio(rpa.getReenquadramentoTipologiaEmpreendimento())) {
			
			for (ReenquadramentoTipologiaEmpreendimento rte : rpa.getReenquadramentoTipologiaEmpreendimento()) {
				try {
					
					rte.getIdeTipologia().setTextoCompleto(retornarTipologiaCompleta(rte.getIdeTipologia(),""));
					
					textoEmpreendimento.append("\nPara a atividade de: "+ rte.getIdeTipologia().getTextoCompleto() + ": ");
					textoEmpreendimento.append("\n\n- Deverá ser solicitada licença ambiental para o empreendimento.");
					textoEmpreendimento.append(" Desta forma, é necessário consultar o empreendimento vinculado a este processo, ");
					textoEmpreendimento.append("denominado "+ nomEmpreendimento);
					textoEmpreendimento.append(" e editar a sua tipologia de forma a inserir a(s) seguinte(s) atividade(s) " + rte.getIdeTipologia().getTextoCompleto() + ", ");
					textoEmpreendimento.append("Apenas após a edição do empreendimento, deve ser editado o requerimento na pauta de consulta de processos."); 
					
					textoEmpreendimento.replace(textoEmpreendimento.length()-2,textoEmpreendimento.length()-1,"");
					
				}
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
		
		if(!textoEmpreendimento.toString().isEmpty()) {
			texto.append(textoEmpreendimento);
			texto.append("\n");
		}	 
	}		
	
	/**<p>Retorna a árvore de tipologia</p>
	 * @param Tipologia
	 * @param String
	 * @return String
	 * @throws Exception 
	 * **/
	protected String retornarTipologiaCompleta(Tipologia tipologia, String textoTipologiaEmpreendimento) throws Exception  {
		tipologia = tipologiaService.carregarTipologiaPorIde(tipologia.getId());
		if(!Util.isNullOuVazio(tipologia.getIdeTipologiaPai())) {
			textoTipologiaEmpreendimento = "".concat(" > ").concat(tipologia.getDesTipologia()).concat(textoTipologiaEmpreendimento);
			return retornarTipologiaCompleta(tipologia.getIdeTipologiaPai(),textoTipologiaEmpreendimento);
		}
		return tipologia.getDesTipologia().concat(textoTipologiaEmpreendimento);
	}

	
	private void adicionarTextoPorte(StringBuilder texto, NotificacaoFinalDTO dto) {
		if(dto.getReenquadramentoProcesso().possuiFinalidade(FinalidadeReenquadramentoEnum.CORRECAO_PORTE_EMPREENDIMENTO)) {
			texto.append("- Será necessária a correção do porte do empreendimento;\n\n");
		}
	}
	
	private void adicionarTextoClasse(StringBuilder texto, NotificacaoFinalDTO dto) {
		if(dto.getReenquadramentoProcesso().possuiFinalidade(FinalidadeReenquadramentoEnum.ALTERACAO_CLASSE_EMPREENDIMENTO)
		&& !Util.isNull(dto.getReenquadramentoProcesso().getIdeNovaClasseEmpreendimento())) {
			texto.append("- Foi identificada a necessidade de alteração na classe do empreendimento que passará a ser enquadrada na "+dto.getReenquadramentoProcesso().getIdeNovaClasseEmpreendimento().getNomClasse()+";\n\n");
		}
	}
	
	private void adicionarTextoPotenciaPoluidor(StringBuilder texto, NotificacaoFinalDTO dto) {
		if(dto.getReenquadramentoProcesso().possuiFinalidade(FinalidadeReenquadramentoEnum.ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE)
		&& !Util.isNullOuVazio(dto.getListaReenquadramentoPotencialPoluicao())) {
			texto.append("- Foi identificada a necessidade de alteração do potencial poluidor do empreendimento. A partir da análise técnica, determinou-se que: ");
			for (Iterator<ReenquadramentoPotencialPoluicao> iterator = dto.getListaReenquadramentoPotencialPoluicao().iterator(); iterator.hasNext();) {
				ReenquadramentoPotencialPoluicao rpp = (ReenquadramentoPotencialPoluicao) iterator.next();
				String tipologia = rpp.getIdeRequerimentoTipologia().getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getDesTipologia();
				texto.append("Atividade: " + tipologia + " - Potencial poluidor: " + rpp.getIdePotencialPoluicaoNovo().getNomPotencialPoluicao());
				texto.append(iterator.hasNext() ? ", " : ";\n\n");
			}
		}
	}
	
	private void adicionarTextoTipologia(StringBuilder texto, NotificacaoFinalDTO dto) {
		if(dto.getReenquadramentoProcesso().possuiFinalidade(FinalidadeReenquadramentoEnum.ALTERACAO_TIPOLOGIA)
		&& !Util.isNullOuVazio(dto.getListaReenquadramentoTipologia())) {
			texto.append("- Será necessária a alteração da tipologia do empreendimento, que passará a ser caracterizado pelas seguinte(s) tipologia(s): ");
			for (Iterator<ReenquadramentoTipologia> iterator = dto.getListaReenquadramentoTipologia().iterator(); iterator.hasNext();) {
				ReenquadramentoTipologia rt = iterator.next();
				texto.append(rt.getIdeTipologia().getDesTipologia());
				texto.append(iterator.hasNext() ? ", " : ";\n\n");
			}
		}
	}
	
	public Collection<ReenquadramentoProcessoAto> listarReenquadramentoProcessoAto(ReenquadramentoProcesso reenquadramentoProcesso) {
		return reenquadramentoProcessoAtoDAOImpl.listarReenquadramentoProcessoAto(reenquadramentoProcesso);
	}
}