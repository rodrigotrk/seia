package br.gov.ba.seia.dao;

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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnquadramentoAtoAmbientalDAOImpl {

	@Inject
	private IDAO<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnquadramentoAtoAmbiental> buscarEnquadramentoAtoAmbientalPorEnquadramento(Enquadramento enquadramento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class);
		
		criteria
			.createAlias("tipologia", "tl", JoinType.LEFT_OUTER_JOIN)
			.createAlias("enquadramento", "en", JoinType.LEFT_OUTER_JOIN)
			.createAlias("atoAmbiental", "aa", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("enquadramento.ideEnquadramento", enquadramento.getIdeEnquadramento()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEnquadramentoAtoAmbiental"),"ideEnquadramentoAtoAmbiental")
				.add(Projections.property("tipologia.ideTipologia"),"tipologia.ideTipologia")
				.add(Projections.property("enquadramento.ideEnquadramento"),"enquadramento.ideEnquadramento")
				.add(Projections.property("atoAmbiental.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoAtoAmbiental.class))
		;
		return enquadramentoAtoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumento(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) throws Exception {
		enquadramentoAtoAmbientalDAO.remover(enquadramentoAtoAmbiental);
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumentos(List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento) throws Exception {
		for(DocumentoObrigatorioEnquadramentoDTO documentoObrigatorioEnquadramento :listaDocumentoObrigatorioEnquadramento) {
			EnquadramentoAtoAmbiental enquadramentoAto = enquadramentoAtoAmbientalDAO.carregarGet(documentoObrigatorioEnquadramento.getEnquadramentoAtoAmbiental().getIdeEnquadramentoAtoAmbiental());
			enquadramentoAtoAmbientalDAO.remover(enquadramentoAto);
		}
	}
}
