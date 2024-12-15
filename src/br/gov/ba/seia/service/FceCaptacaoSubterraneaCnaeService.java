package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceCaptacaoSubterranea;
import br.gov.ba.seia.entity.FceCaptacaoSubterraneaCnae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCaptacaoSubterraneaCnaeService {

	private static final Long QTD_CNAE_PRINCIPAL = 1l;
	@Inject
	IDAO<FceCaptacaoSubterraneaCnae> daoFceCaptacaoSubterraneaCnae;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean salvarFceCaptacaoSubterraneaCnae(FceCaptacaoSubterraneaCnae fceCaptacaoSubterraneaCnae) {
		Boolean retorno = Boolean.FALSE;
		if (validateCnaePrincipal(fceCaptacaoSubterraneaCnae)) {
			daoFceCaptacaoSubterraneaCnae.salvar(fceCaptacaoSubterraneaCnae);
			retorno = Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean salvarOuAtualizarCaptacaoSubterraneaCnae(FceCaptacaoSubterraneaCnae fceCapacitacaoSubterraneaCnae) {
		Boolean retorno = Boolean.FALSE;
		if (validateCnaePrincipal(fceCapacitacaoSubterraneaCnae)) {
			daoFceCaptacaoSubterraneaCnae.salvarOuAtualizar(fceCapacitacaoSubterraneaCnae);
			retorno = Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Boolean validateCnaePrincipal(FceCaptacaoSubterraneaCnae fceCapacitacaoSubterraneaCnae)  {
		Boolean retorno = Boolean.TRUE;
		if (fceCapacitacaoSubterraneaCnae.isIndCnaePrincipal()) {
			DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSubterraneaCnae.class);
			criteria.setProjection(Projections.count("indCnaePrincipal"))
			.add(Restrictions.ne("ideFceCaptacaoSubterraneaCnae", fceCapacitacaoSubterraneaCnae.getIdeFceCaptacaoSubterraneaCnae() != null ? fceCapacitacaoSubterraneaCnae.getIdeFceCaptacaoSubterraneaCnae():-1 )) //pra caso de estar editando
			        .add(Restrictions.and(
			        		Restrictions.eq("ideFceCaptacaoSubterranea", fceCapacitacaoSubterraneaCnae.getIdeFceCaptacaoSubterranea()),			        		
			        		Restrictions.eq("indCnaePrincipal", Boolean.TRUE)));
			retorno = (((Long)daoFceCaptacaoSubterraneaCnae.executarCriteria(criteria)) >= QTD_CNAE_PRINCIPAL ) ? Boolean.FALSE : Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<FceCaptacaoSubterraneaCnae> buscaFceCaptacaoSubterraneaCnaePorFceCaptacaoSupericial(FceCaptacaoSubterranea fceCaptacaoSubterranea) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideFceCaptacaoSubterranea", fceCaptacaoSubterranea);
		return daoFceCaptacaoSubterraneaCnae.buscarPorNamedQuery("FceCaptacaoSubterraneaCnae.findByFceCaptacaoSubterranea", parametros);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceCaptacaoSubterraneaCnae(FceCaptacaoSubterraneaCnae fceCaptacaoSubterraneaCnae) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ideFceCaptacaoSubterraneaCnae", fceCaptacaoSubterraneaCnae.getIdeFceCaptacaoSubterraneaCnae());
		daoFceCaptacaoSubterraneaCnae.executarNamedQuery("FceCaptacaoSubterraneaCnae.removeByIdeFceCaptacaoSubterraneaCnae", param);
	}
	
	public Collection<FceCaptacaoSubterraneaCnae> listarCnaeByFce (FceCaptacaoSubterranea fceCapSubterranea) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSubterraneaCnae.class);
		criteria.add(Restrictions.eq("ideFceCaptacaoSubterranea.ideFceCaptacaoSubterranea", fceCapSubterranea.getIdeFceCaptacaoSubterranea()));
		return daoFceCaptacaoSubterraneaCnae.listarPorCriteria(criteria);
	}

}
