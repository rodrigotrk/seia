
package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ControleCronogramaDAOImpl;
import br.gov.ba.seia.dao.CronogramaDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleCronograma;
import br.gov.ba.seia.entity.Cronograma;
import br.gov.ba.seia.enumerator.ItemCronogramaEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleCronogramaService {

	@Inject
	private IDAO<ControleCronograma> daoControleCronograma;
	
	@Inject
	private ControleCronogramaDAOImpl controleCronogramaDAOImpl;
	
	@Inject
	private CronogramaDAOImpl cronogramaDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ControleCronograma> listarPorCronograma(Cronograma cronograma) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideCronograma", cronograma.getIdeCronograma());
		return daoControleCronograma.buscarPorNamedQuery("ControleCronograma.findByIdeCronograma", parametros);
	}
	
	/**
	 * Retorna o controleCronograma de itemCronograma igual a Conclusão de Parecer que faça pertence ao cronograma informado no pametro.
	 * @param cronograma
	 * @return ControleCronograma
	 * @throws Exception
	 * @author micael.coutinho
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleCronograma getControleCronogramaConclusaoParecerByCronograma(Cronograma cronograma, Area area) {
		ControleCronograma cCrono = new ControleCronograma();
		List<Cronograma> listaCronogramas= cronogramaDAOImpl.filtrarCronogramaByProcesso(cronograma.getIdeProcesso(),area);
		
		if(Util.isNullOuVazio(listaCronogramas)){
			throw new SeiaValidacaoRuntimeException("Favor informar no Cronograma a data de conclusão do Parecer.");
		}
		
		Collection<ControleCronograma> listCronog = listaCronogramas.get(0).getControleCronogramaCollection();
		for (Iterator<ControleCronograma> iterator = listCronog.iterator(); iterator.hasNext();) {
			ControleCronograma ctrlCronos = iterator.next();
			if(ctrlCronos.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde())){
				cCrono = ctrlCronos;
				break;
			}
		}
		return cCrono;
	}
	
	/**
	 * Adcionar uma atividade ao cronograma do processo. Ou seja...
	 * Adiciona um crotroleCronograma ao cronograma, verificando se o cronograma já tem conclusão de parecer e se já tiver verifica se o itemCronograma do 
	 * @param controleCronograma
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void adcionarControleCronograma(Cronograma cronograma, ControleCronograma controleCronograma) throws Exception {
		controleCronogramaDAOImpl.adcionarControleCronogramaAoCronograma(cronograma, controleCronograma);
	}
	
	/**
	 * Remove uma atividade do cronograma e retorna exceção caso o controle cronograma seja do tipo Conclusao de Parecer.
	 * @author micael.coutinho
	 * @param delControlCrono
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirControleCronograma(ControleCronograma delControlCrono)  {
		controleCronogramaDAOImpl.excluirControleCronograma(delControlCrono);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarControleCronograma(ControleCronograma controlCrono) {
		controleCronogramaDAOImpl.atualizarControleCronograma(controlCrono);
	}
	
	public List<ControleCronograma> listarControleCronogramaByIdeProcesso(Integer ideProcesso) {
		return controleCronogramaDAOImpl.getControleCronogramaByIdeProcesso(ideProcesso);
	}

}