package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Finalidade;
import br.gov.ba.seia.entity.FinalidadeTipologiaAtividade;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.FinalidadeEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaAtividadeService {

	@Inject
	private IDAO<TipologiaAtividade> tipologiaAtividadeIDAO;
	@Inject
	private IDAO<FinalidadeTipologiaAtividade> finalidadeTipologiaAtividadeIDAO;
	@Inject
	private IDAO<UnidadeMedida> unidadeMedidaIDAO;
	@Inject
	private IDAO<Finalidade> finalidadeIDAO;


	/**
	 * @param tipologia
	 * @return
	 * @
	 * @INFO Retorna uma lista de tipologia atividade a partir da tipologia informada
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipologiaAtividade> buscarTipologiaAtividadeByIdeTipologia(Tipologia tipologia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaAtividade.class);
		criteria.add(Restrictions.eq("ideTipologia.ideTipologia", tipologia.getIdeTipologia()));//Criação de Animal
		return tipologiaAtividadeIDAO.listarPorCriteria(criteria,Order.asc("dscTipologiaAtividade"));
	}
	/**
	 * @param tipologiaAtividade
	 * @return
	 * @
	 * @INFO Busca todas as unidades de medida da TipologiaAtividade informada
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeMedida> buscarUnidadeMedidaByTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("ideTipologiaAtividade", tipologiaAtividade.getIdeTipologiaAtividade());
		return unidadeMedidaIDAO.buscarPorNamedQuery("UnidadeMedida.findByTipologiaAtividade",maps);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Finalidade> buscarFinalidadeTipologiaAtividadeByIdeTipologia(TipologiaAtividade tipologiaAtividade) {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("ideTipologiaAtividade", tipologiaAtividade.getIdeTipologiaAtividade());
		return finalidadeIDAO.buscarPorNamedQuery("Finalidade.findByTipologiaAtividade",maps);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FinalidadeTipologiaAtividade buscarFinaTipoAtivByTipologiaAtivEFinali(TipologiaAtividade tipologiaAtividade, Finalidade finalidade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FinalidadeTipologiaAtividade.class);
		criteria.add(Restrictions.eq("ideTipologiaAtividade.ideTipologiaAtividade", tipologiaAtividade.getIdeTipologiaAtividade()));
		criteria.add(Restrictions.eq("ideFinalidade.ideFinalidade", finalidade.getIdeFinalidade()));
		return finalidadeTipologiaAtividadeIDAO.buscarPorCriteria(criteria);
	}

	public Finalidade getFinalidadeOutros() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Finalidade.class);
		criteria.add(Restrictions.eq("ideFinalidade", FinalidadeEnum.OUTROS.getId()));
		return finalidadeIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipologiaAtividade> buscarTipologiaAtividadeByListTipologiaEnum(List<TipologiaEnum> listTipologia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaAtividade.class);
		Disjunction disjunction = Restrictions.disjunction();
		for(TipologiaEnum tipologia : listTipologia){
			disjunction.add(Restrictions.eq("ideTipologia.ideTipologia",tipologia.getId()));
		}
		criteria.add(disjunction);
		return tipologiaAtividadeIDAO.listarPorCriteria(criteria,Order.asc("dscTipologiaAtividade"));
	}
}
