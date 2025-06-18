package br.gov.ba.seia.util.schedule.jobservice;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Comunicacao;
import br.gov.ba.seia.enumerator.ComunicacaoStatusEnum;
import br.gov.ba.seia.service.ComunicacaoService;
import br.gov.ba.seia.service.ComunicacaoStatusService;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoJobService {

	@EJB
	ComunicacaoService comunicacaoService;

	@EJB
	ComunicacaoStatusService comunicacaoStatusService;

	public void processaComunicacoesDoDia() {
		try {
		
			List<Comunicacao> comunicacoes = comunicacaoService.buscarComunicacoesParaAtualizacaoDiaria();

			// Atualiza as notificacoes
			for (Comunicacao comunicacao : comunicacoes) {
				// Ativa as comunicacões do dia

				if ((DataUtil.getDataAtual().getDate() == comunicacao.getDtcPeriodoInicio().getDate() 
						&& comunicacao.getDtcPeriodoInicio().getYear()==DataUtil.getDataAtual().getYear()
						&& comunicacao.getDtcPeriodoInicio().getMonth()==DataUtil.getDataAtual().getMonth()) && comunicacao
						.getIdeComunicacaoStatus().equals(ComunicacaoStatusEnum.NOVO.getIdComunicacaoStatus())
						&& comunicacao.isIndAtiva()) {
					comunicacao.setIdeComunicacaoStatus(ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus());
					comunicacaoService.atualizarComunicacao(comunicacao);

					// Arquiva as comunicacoes passadas
				}
	
				if ((comunicacao.getDtcPeriodoFim().getDate() == DataUtil.getDataOntem().getDate()
						&& comunicacao.getDtcPeriodoFim().getYear()==DataUtil.getDataOntem().getYear()
						&& comunicacao.getDtcPeriodoFim().getMonth()==DataUtil.getDataOntem().getMonth())
						&& comunicacao.isIndAtiva()) {
					comunicacao.setIndAtiva(false);
					comunicacao.setIdeComunicacaoStatus(ComunicacaoStatusEnum.ARQUIVADO.getIdComunicacaoStatus());
					comunicacaoService.atualizarComunicacao(comunicacao);
				}

			}
		} catch (Exception e) {
			Log4jUtil.log(Comunicacao.class.getSimpleName(), Level.ERROR, e);
		}

	}

}
