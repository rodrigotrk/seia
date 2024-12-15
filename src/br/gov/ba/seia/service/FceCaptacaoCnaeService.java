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
import br.gov.ba.seia.entity.FceCaptacaoSuperficial;
import br.gov.ba.seia.entity.FceCaptacaoSuperficialCnae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCaptacaoCnaeService {

	private static final Long QTD_CNAE_PRINCIPAL = 1l;
	@Inject
	IDAO<FceCaptacaoSuperficialCnae> daoFceCaptacaoSuperficialCnae;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean salvarFceCaptacaoSuperficialCnae(FceCaptacaoSuperficialCnae fceCaptacaoSuperficialCnae) {
		Boolean retorno = Boolean.FALSE;
		if (validateCnaePrincipal(fceCaptacaoSuperficialCnae)) {
			daoFceCaptacaoSuperficialCnae.salvar(fceCaptacaoSuperficialCnae);
			retorno = Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean salvarOuAtualizarCaptacaoSuperficialCnae(FceCaptacaoSuperficialCnae fceCapacitacaoSuperficialCnae) {
		Boolean retorno = Boolean.FALSE;
		if (validateCnaePrincipal(fceCapacitacaoSuperficialCnae)) {
			daoFceCaptacaoSuperficialCnae.salvarOuAtualizar(fceCapacitacaoSuperficialCnae);
			retorno = Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Boolean validateCnaePrincipal(FceCaptacaoSuperficialCnae fceCapacitacaoSuperficialCnae)  {
		Boolean retorno = Boolean.TRUE;
		if (fceCapacitacaoSuperficialCnae.isIndCnaePrincipal()) {
			DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSuperficialCnae.class);
			criteria.setProjection(Projections.count("indCnaePrincipal"))
			.add(Restrictions.ne("ideFceCaptacaoSuperficialCnae", fceCapacitacaoSuperficialCnae.getIdeFceCaptacaoSuperficialCnae() != null ? fceCapacitacaoSuperficialCnae.getIdeFceCaptacaoSuperficialCnae():-1 )) //pra caso de estar editando
			        .add(Restrictions.and(
			        		Restrictions.eq("ideFceCaptacaoSupercial", fceCapacitacaoSuperficialCnae.getIdeFceCaptacaoSupercial()),			        		
			        		Restrictions.eq("indCnaePrincipal", Boolean.TRUE)));
			retorno = (((Long)daoFceCaptacaoSuperficialCnae.executarCriteria(criteria)) >= QTD_CNAE_PRINCIPAL ) ? Boolean.FALSE : Boolean.TRUE;
		}
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<FceCaptacaoSuperficialCnae> buscaFceCaptacaoSuperficialCnaePorFceCaptacaoSupericial(FceCaptacaoSuperficial fceCaptacaoSuperficial) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideFceCaptacaoSuperficial", fceCaptacaoSuperficial);
		Collection<FceCaptacaoSuperficialCnae> coll = daoFceCaptacaoSuperficialCnae.buscarPorNamedQuery("FceCaptacaoSuperficialCnae.findByFceCaptacaoSuperifical", parametros);
		return coll;
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceCaptacaoSuperficialCnae(FceCaptacaoSuperficialCnae fceCaptacaoSuperficialCnae) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ideFceCaptacaoSuperficialCnae", fceCaptacaoSuperficialCnae.getIdeFceCaptacaoSuperficialCnae());
		daoFceCaptacaoSuperficialCnae.executarNamedQuery("FceCaptacaoSuperficialCnae.removeByIdeFceCaptacaoSuperficialCnae", param);
	}
	
	public Collection<FceCaptacaoSuperficialCnae> listarCnaeByFce (FceCaptacaoSuperficial fceCapSuperficial) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSuperficialCnae.class)
				.createAlias("ideFceCaptacaoSupercial", "fcs")
				.add(Restrictions.eq("fcs.ideFceCaptacaoSuperficial", fceCapSuperficial.getIdeFceCaptacaoSuperficial()));
		return daoFceCaptacaoSuperficialCnae.listarPorCriteria(criteria);
	}

}
