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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceIntervencaoCaracteristicaExtracao;
import br.gov.ba.seia.entity.FceIntervencaoTipoCaractExtracao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIntervencaoTipoCaractExtracaoService {

	@Inject
	private IDAO<FceIntervencaoTipoCaractExtracao> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FceIntervencaoTipoCaractExtracao tipoCaractExtracao) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideFceIntervencaoTipoCaractExtracao", tipoCaractExtracao.getIdeFceIntervencaoTipoCaractExtracao());
		idao.executarNamedQuery("FceIntervencaoTipoCaractExtracao.deleteByIde", map);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIntervencaoTipoCaractExtracao> listarByFceIntervencaoCaracteristicaExtracao(FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIntervencaoTipoCaractExtracao.class)
				.add(Restrictions.eq("ideFceIntervencaoCaracteristicaExtracao", fceIntervencaoCaracteristicaExtracao));
		return idao.listarPorCriteria(criteria);
	}
}
