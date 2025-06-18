package br.gov.ba.seia.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.ImovelRural;

public class CronogramaEtapaDAOImpl implements CronogramaEtapaDAOIf{

	@Inject
	IDAO<CronogramaEtapa> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CronogramaEtapa filtrarById(CronogramaEtapa pCronogramaEtapa) {
		return dao.buscarEntidadePorExemplo(pCronogramaEtapa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CronogramaEtapa pCronogramaEtapa)  {
		dao.salvar(pCronogramaEtapa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(CronogramaEtapa pCronogramaEtapa) {
		dao.atualizar(pCronogramaEtapa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAllByIdeCronogramaRecuperacao(Integer pIdeCronogramaRecuperacao) {
		String deleteSQL = "DELETE FROM cronograma_etapa WHERE ide_cronograma_recuperacao = :ideCronogramaRecuperacao";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCronogramaRecuperacao", pIdeCronogramaRecuperacao);
		dao.executarNativeQuery(deleteSQL, params);
	}


	@Override
	public List<CronogramaEtapa> getCronogramaEtapa(CronogramaEtapa pCronogramaEtapa) {
		
		return null;
	}

	@Override
	public List<CronogramaEtapa> filtrarCronogramaByImovelRural(ImovelRural imovel) {

		return null;
	}
	
	
}
