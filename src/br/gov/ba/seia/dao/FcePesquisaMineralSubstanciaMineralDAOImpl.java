package br.gov.ba.seia.dao;

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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralSubstanciaMineralTipologia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralSubstanciaMineralDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineralSubstanciaMineralTipologia> fcePesquisaMineralSubstanciaMineralDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralSubstanciaMineral(List<FcePesquisaMineralSubstanciaMineralTipologia> ideFcePesquisaMineralSubstanciaMineral) {
		fcePesquisaMineralSubstanciaMineralDAO.salvarEmLote(ideFcePesquisaMineralSubstanciaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralSubstanciaMineralTipologia> listarFcePesquisaMineralSubstanciaMineral(FcePesquisaMineral ideFcePesquisaMineral) {	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralSubstanciaMineralTipologia.class)
			.createAlias("ideFcePesquisaMineral", "fpm")
			.createAlias("substanciaMineralTipologia", "smt")
			.createAlias("smt.ideSubstanciaMineral", "sub")
			.createAlias("smt.ideTipologia", "tip")
			.createAlias("tip.ideTipologiaPai", "pai")
			.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral())
			).setProjection(Projections.projectionList()
					.add(Projections.property("fpm.ideFcePesquisaMineral"), "ideFcePesquisaMineral.ideFcePesquisaMineral")
					.add(Projections.property("smt.ideSubstanciaMineralTipologia"), "substanciaMineralTipologia.ideSubstanciaMineralTipologia")
					.add(Projections.property("sub.ideSubstanciaMineral"), "substanciaMineralTipologia.ideSubstanciaMineral.ideSubstanciaMineral")
					.add(Projections.property("sub.nomSubstanciaMineral"), "substanciaMineralTipologia.ideSubstanciaMineral.nomSubstanciaMineral")
					.add(Projections.property("tip.ideTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologia")
					.add(Projections.property("tip.desTipologia"), "substanciaMineralTipologia.ideTipologia.desTipologia")
					.add(Projections.property("tip.codTipologia"), "substanciaMineralTipologia.ideTipologia.codTipologia")
					.add(Projections.property("pai.ideTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologiaPai.ideTipologia")
					.add(Projections.property("pai.desTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologiaPai.desTipologia")
					.add(Projections.property("pai.codTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologiaPai.codTipologia")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralSubstanciaMineralTipologia.class)
					);
		return fcePesquisaMineralSubstanciaMineralDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral());
		fcePesquisaMineralSubstanciaMineralDAO.executarNamedQuery("FcePesquisaMineralSubstanciaMineralTipologia.removeByIdeFcePesquisaMineral", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralSubstanciaMineralByNativeQuery(List<FcePesquisaMineralSubstanciaMineralTipologia> ideFcePesquisaMineralSubstanciaMineral) throws Exception {
		
		for(FcePesquisaMineralSubstanciaMineralTipologia fcePesquisaMineralSubstanciaMineralTipologia: ideFcePesquisaMineralSubstanciaMineral) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ideFcePesquisaMineral", fcePesquisaMineralSubstanciaMineralTipologia.getIdeFcePesquisaMineralSubstanciaMineralPK().getIdeFcePesquisaMineral());
			parametros.put("ideSubstanciaMineralTipologia", fcePesquisaMineralSubstanciaMineralTipologia.getIdeFcePesquisaMineralSubstanciaMineralPK().getIdeSubstanciaMineralTipologia());
			
			fcePesquisaMineralSubstanciaMineralDAO.executarNativeQuery("INSERT INTO fce_pesquisa_mineral_substancia_mineral_tipologia values(:ideSubstanciaMineralTipologia,:ideFcePesquisaMineral)", parametros);
		}
		
	}

}
