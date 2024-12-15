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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceTurismo;
import br.gov.ba.seia.entity.FceTurismoLocalizacaoGeografica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceTurismoLocalizacaoGeograficaService {

	@Inject
	private IDAO<FceTurismoLocalizacaoGeografica> fceTurismoLocalizacaoGeograficaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceTurismoLocalizacaoGeografica> listarFceTurismoLocalizacaoGeograficaByIdeFceTurismo(FceTurismo fceTurismo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceTurismoLocalizacaoGeografica.class)
				.createAlias("ideLocalizacaoGeografica", "loc")
				.createAlias("ideFceTurismo", "fce")
				.createAlias("ideTipoApp", "app")
				.createAlias("ideImovel", "imo")
				.createAlias("imo.imovelRural", "ir", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.eq("ideFceTurismo.ideFceTurismo", fceTurismo.getIdeFceTurismo()));
		return fceTurismoLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceTurismoLocalizacaoGeografica(List<FceTurismoLocalizacaoGeografica> lista)  {
		fceTurismoLocalizacaoGeograficaIDAO.salvarEmLote(lista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceTurismoLocalizacaoGeografica(FceTurismoLocalizacaoGeografica fceTurismoLocGeo)  {
		fceTurismoLocalizacaoGeograficaIDAO.salvarOuAtualizar(fceTurismoLocGeo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceCategoriaTurismoByIdeFceTurismo(FceTurismo fceTurismo)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceTurismo", fceTurismo.getIdeFceTurismo());
		fceTurismoLocalizacaoGeograficaIDAO.executarNamedQuery("FceTurismoLocalizacaoGeografica.removeByIdeFceTurismo",parameters);
	}

}