package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.ParametroReferencia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TipologiaTipoAto;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;



public class ParamentroReferenciaDAOImpl {
	
	@Inject
	IDAO<ParametroReferencia> parametroReferenciaDAO;	

	public ParametroReferencia filtrarTipologiaTipoAtoGrupoById(ParametroReferencia pParametroReferencia) {
		return parametroReferenciaDAO.buscarEntidadePorExemplo(pParametroReferencia);
	}
	

	public void salvarParametroReferencia(ParametroReferencia pParametroReferencia)  {
		parametroReferenciaDAO.salvarOuAtualizar(pParametroReferencia);
	}

	public void atualizarParametroReferencia(ParametroReferencia pParametroReferencia)  {
		parametroReferenciaDAO.atualizar(pParametroReferencia);
	}
	
	public  ParametroReferencia carregarParametroReferencia(Integer id){
		return 	parametroReferenciaDAO.carregarLoad(id);
	}
	
	
	public  Collection<ParametroReferencia> recuperarParametroReferencia(Collection<UnidadeMedidaTipologiaGrupo> unidadeMedidaTipologiaGrupo ) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ParametroReferencia.class);
		detachedCriteria.setFetchMode("ideUnidadeMedidaTipologiaGrupo", FetchMode.JOIN);
		detachedCriteria.add(Restrictions.in("ideUnidadeMedidaTipologiaGrupo", unidadeMedidaTipologiaGrupo));
		return parametroReferenciaDAO.listarPorCriteria(detachedCriteria);
	}
	
	public void deletarParametroReferencias(Collection<ParametroReferencia> pParametroReferencia)  {
		for (ParametroReferencia listparametroReferencia : pParametroReferencia) {
			parametroReferenciaDAO.remover(listparametroReferencia);
		}
	}
	
	
	public Collection<ParametroReferencia> listarParamentroReferenciaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ParametroReferencia.class);
		criteria.createAlias("ideTipologiaTipoAto", "tipologiaTipoAto");
		criteria.add(Restrictions.eq("tipologiaTipoAto.ideTipologiaGrupo.ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo()));		
		return parametroReferenciaDAO.listarPorCriteria(criteria);
	}
	
	
	public ParametroReferencia buscarParametroReferencia(TipologiaTipoAto ideTipologiaTipoAto,UnidadeMedidaTipologiaGrupo ideUnidadeMedidaTipologiaGrupo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ParametroReferencia.class);
		criteria.add(Restrictions.eq("ideTipologiaTipoAto", ideTipologiaTipoAto));
		criteria.add(Restrictions.eq("ideUnidadeMedidaTipologiaGrupo", ideUnidadeMedidaTipologiaGrupo));
		
		return parametroReferenciaDAO.buscarPorCriteria(criteria);
		
	}
	
	
	
}