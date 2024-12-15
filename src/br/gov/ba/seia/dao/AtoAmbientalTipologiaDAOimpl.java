package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.AtoAmbientalTipologia;
import br.gov.ba.seia.entity.AtoAmbientalTipologiaFinalidade;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
public class AtoAmbientalTipologiaDAOimpl {
	
	@Inject
	IDAO<AtoAmbientalTipologia> atoAmbientalTipologiaDAO;

	@Inject 
	IDAO<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaDAO; 
	
	public List<AtoAmbientalTipologia> listarAtoAmbientalTipologiaPorDemanda(int ideAtoAmbiental,int first, int pageSize) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
			.createAlias("AtoAmbientalTipologia","atoAmbientalTipologia",JoinType.LEFT_OUTER_JOIN)
			.createAlias("Tipologia","tipoligia",JoinType.LEFT_OUTER_JOIN)
			.createAlias("AtoAmbientalTipologiaFinalidade","atoAmbientalTipologiaFinalidade",JoinType.LEFT_OUTER_JOIN )
			.createAlias("TipoFinalidadeUsoAgua","tipoFinalidadeUsoAgua",JoinType.LEFT_OUTER_JOIN )
			.add(Restrictions.eq("ideAtoAmbiental",ideAtoAmbiental));
		return atoAmbientalTipologiaDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
	}
	
	public Integer countAtoAmbientalTipologia(Integer ideAtoAmbiental){
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
			.createAlias("AtoAmbientalTipologia","atoAmbientalTipologia",JoinType.LEFT_OUTER_JOIN )
			.createAlias("Tipologia","tipoligia",JoinType.LEFT_OUTER_JOIN)
			.createAlias("AtoAmbientalTipologiaFinalidade","atoAmbientalTipologiaFinalidade", JoinType.LEFT_OUTER_JOIN )
			.createAlias("TipoFinalidadeUsoAgua","tipoFinalidadeUsoAgua",JoinType.LEFT_OUTER_JOIN )
			.add(Restrictions.eq("ideAtoAmbiental",ideAtoAmbiental));
		return atoAmbientalTipologiaDAO.count(criteria);
	}
	
	public Collection<TipoFinalidadeUsoAgua> buscarFinalidadePorTipologia(Integer ideTipologia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbientalTipologiaFinalidade.class) 
			.createAlias("ideAtoAmbientalTipologia", "AtoAmbientalTipologia", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoFinalidadeUsoAgua", "tipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN)
		    
			.add(Restrictions.eq("AtoAmbientalTipologia.ideTipologia.ideTipologia", ideTipologia))
		    .setProjection(Projections.projectionList()
		    		.add(Projections.property("tipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua")
		    		.add(Projections.property("tipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"),"nomTipoFinalidadeUsoAgua")
		    ).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class));

		return this.tipoFinalidadeUsoAguaDAO.listarPorCriteria(criteria, Order.asc("ideTipoFinalidadeUsoAgua"));
		}
}
