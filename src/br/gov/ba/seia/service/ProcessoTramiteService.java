package br.gov.ba.seia.service;

/**
 * @author eduardo.fernandes
 */
import java.util.List;

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
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoTramiteService {

	@Inject
	private IDAO<ProcessoTramite> processoTramiteDAO;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ProcessoTramite processoTramite)  {
		this.processoTramiteDAO.salvarOuAtualizar(processoTramite);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoTramite> listarProcessoTramiteByRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoTramite.class)
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideProcessoTramite"), "ideProcessoTramite")
						.add(Projections.property("numProcessoTramite"), "numProcessoTramite")
						.add(Projections.property("dscTipoProcessoTramite"), "dscTipoProcessoTramite")
						.add(Projections.property("ideRequerimento.ideRequerimento"), "ideRequerimento.ideRequerimento")
						.add(Projections.property("ideSistema.ideSistema"), "ideSistema.ideSistema")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoTramite.class));
		return processoTramiteDAO.listarPorCriteria(criteria);
	}
}