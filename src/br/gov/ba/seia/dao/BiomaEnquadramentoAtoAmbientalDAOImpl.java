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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.BiomaEnquadramentoAtoAmbiental;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BiomaEnquadramentoAtoAmbientalDAOImpl {

	@Inject
	private IDAO<BiomaEnquadramentoAtoAmbiental> biomaEnquadramentoAtoAmbientalDAO;
	
	public List<BiomaEnquadramentoAtoAmbiental> biomaEnquadramentoAtoAmbientalListarByIdeRequerimento(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(BiomaEnquadramentoAtoAmbiental.class, "beaa");
		criteria.createAlias("beaa.ideBiomaRequerimento", "biomaReq", JoinType.INNER_JOIN)
				.createAlias("biomaReq.ideRequerimento", "req", JoinType.INNER_JOIN)
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		
		return biomaEnquadramentoAtoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPorBiomaRequerimento(Integer ideBiomaRequerimento) {
		String sql = "delete from BiomaEnquadramentoAtoAmbiental beaa where beaa.ideBiomaRequerimento.ideBiomaRequerimento =:ideBiomaRequerimento";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideBiomaRequerimento", ideBiomaRequerimento);
		
		biomaEnquadramentoAtoAmbientalDAO.executarQuery(sql, params);
	}
}
