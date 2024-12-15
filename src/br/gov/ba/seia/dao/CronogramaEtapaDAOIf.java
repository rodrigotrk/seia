package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.ImovelRural;

public interface CronogramaEtapaDAOIf {

	public List<CronogramaEtapa> getCronogramaEtapa(CronogramaEtapa pCronogramaEtapa);

	List<CronogramaEtapa> filtrarCronogramaByImovelRural(ImovelRural imovel);
}
