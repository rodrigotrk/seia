package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CronogramaDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleCronograma;
import br.gov.ba.seia.entity.Cronograma;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CronogramaService {

	@Inject
	private IDAO<Cronograma> daoCronograma;
	
	@Inject
	private ControleCronogramaService controlCronoService;
	
	@Inject
	private CronogramaDAOImpl  cronogramaDAOImpl; 
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Cronograma cronograma)  {
		
	}
	
	/**
	 * Adcionar uma atividade ao cronograma do processo. Ou seja...
	 * Adiciona um crotroleCronograma ao cronograma, verificando se o cronograma já tem conclusão de parecer e se já tiver verifica se o itemCronograma do 
	 * @param controleCronograma
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void adcionarControleCronograma(Cronograma cronograma, ControleCronograma controleCronograma) throws Exception {
		controlCronoService.adcionarControleCronograma(cronograma, controleCronograma);
	}
	
	/**
	 * Busca o cronograma do processo, mas, se ainda não houver cronograma para o processo, manda o DAO criar o cronograma e então retorna nulo.
	 * @param processo
	 * @return null ou Cronograma
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Cronograma buscarCronogramaDoProcesso(Processo processo, Area area) throws Exception {
		List<Cronograma> lista = cronogramaDAOImpl.filtrarCronogramaByProcesso(processo, area);
		if(Util.isNullOuVazio(lista) && lista.isEmpty()){
			cronogramaDAOImpl.criarCronogramaDoProcesso(processo,area);
			lista = cronogramaDAOImpl.filtrarCronogramaByProcesso(processo,area);
			if(Util.isNullOuVazio(lista)){
				throw(new Exception("Erro ao criar controle de tramitação"));
			}
		}
		return lista.get(0);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cronograma obterCronogramaPorProcesso(Processo processo) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideProcesso", processo.getIdeProcesso());
		return daoCronograma.buscarEntidadePorNamedQuery("Cronograma.findByIdeProcesso", parametros);
	}
	
	/**
	 * Remove uma atividade do cronograma e retorna exceção caso o controle cronograma seja do tipo Conclusao de Parecer.
	 * @author micael.coutinho
	 * @param delControlCrono
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAtividadeCronograma(ControleCronograma delControlCrono) {
		controlCronoService.excluirControleCronograma(delControlCrono);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarControleCronograma(ControleCronograma controlCrono)  {
		controlCronoService.atualizarControleCronograma(controlCrono);
	}

}