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
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceAquiculturaLocalizacaoGeografica;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 21/11/2014
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLocalizacaoGeograficaService {

	@Inject
	private IDAO<FceAquiculturaLocalizacaoGeografica> fceAquiculturaLocGeoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLocalizacaoGeografica(List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocGeo) {
		fceAquiculturaLocGeoIDAO.salvarEmLote(listaFceAquiculturaLocGeo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLocalizacaoGeografica> listarFceAquiculturaLocalizacaoGeograficaByIdeFceAquicultura(FceAquicultura fceAquicultura) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLocalizacaoGeografica.class);
		criteria.createAlias("ideLocalizacaoGeografica", "ideLocalizacaoGeografica", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideFceAquicultura.ideFceAquicultura", fceAquicultura.getIdeFceAquicultura()));
		return fceAquiculturaLocGeoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLocalizacaoGeograficaByIdeFceAquicultura(FceAquicultura fceAquicultura) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquicultura", fceAquicultura.getIdeFceAquicultura());
		fceAquiculturaLocGeoIDAO.executarNamedQuery("FceAquiculturaLocalizacaoGeografica.removeByIdeFceAquicultura",parameters);
	}
}