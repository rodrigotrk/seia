package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.ContratoConvenioDAOImpl;
import br.gov.ba.seia.entity.ContratoConvenio;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContratoConvenioServiceImpl{

	@EJB
	private ContratoConvenioDAOImpl contratoConvenioDAOImpl;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ContratoConvenio contratoConvenio) throws Exception  {
		validarNumeroUnico(contratoConvenio);
		contratoConvenioDAOImpl.salvar(contratoConvenio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ContratoConvenio contratoConvenio)  {
		contratoConvenioDAOImpl.salvar(contratoConvenio);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void validarNumeroUnico(ContratoConvenio contratoConvenioAvaliado) throws Exception  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenio.class)
			.setProjection(Projections.projectionList()
			.add(Projections.property("ideContratoConvenio"),"ideContratoConvenio"))
			.add(Restrictions.eq("numContrato", contratoConvenioAvaliado.getNumContrato()));

		if(contratoConvenioDAOImpl.isExiste(criteria)){
			throw new Exception("Já existe o número de contrato/convênio cadastrado.");
		}

	}

}
