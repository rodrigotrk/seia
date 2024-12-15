package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 01/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnquadramentoDocumentoAtoService {

	@Inject
	private IDAO<EnquadramentoDocumentoAto> enquadramentoDocumentoAtoDAO;

	/**
	 * Método que retorna a lista de {@link EnquadramentoDocumentoAto} referente ao {@link Enquadramento} daquele {@link Requerimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimento
	 * @return
	 * @
	 * @since 01/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnquadramentoDocumentoAto> listarEnquadramentoDocAtoByRequerimentoToFceOutorgaAquicultura(Requerimento requerimento) {
		return enquadramentoDocumentoAtoDAO.listarPorCriteria(getCriteriaComProjection(requerimento, DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnquadramentoDocumentoAto> listarEnquadramentoDocAtoByRequerimentoToFceDispensaOutorga(Requerimento requerimento, DocumentoObrigatorioEnum docObrigatorioEnum) {
		DetachedCriteria criteria = getCriteria(requerimento, docObrigatorioEnum)
				.createAlias("da.ideAtoAmbiental", "aa")
				.add(Restrictions.eq("aa.ideAtoAmbiental", AtoAmbientalEnum.DOUT.getId()))
				.setProjection(projection())
				.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoDocumentoAto.class))
				;
		return enquadramentoDocumentoAtoDAO.listarPorCriteria(criteria);
	}	

	private DetachedCriteria getCriteriaComProjection(Requerimento requerimento, DocumentoObrigatorioEnum documentoObrigatorioEnum) {
		return getCriteria(requerimento, documentoObrigatorioEnum)
				.setProjection(projection())
				.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoDocumentoAto.class));
		 
	}

	private DetachedCriteria getCriteria(Requerimento requerimento, DocumentoObrigatorioEnum documentoObrigatorioEnum) {
		return DetachedCriteria.forClass(EnquadramentoDocumentoAto.class)
				.createAlias("enquadramento", "enq")
				.createAlias("enq.ideRequerimento", "req")
				.createAlias("documentoAto", "da")
				.createAlias("da.ideDocumentoObrigatorio", "dor")
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.eq("dor.ideDocumentoObrigatorio", documentoObrigatorioEnum.getId()))
				.add(Restrictions.eq("da.indAtivo", true));
	}

	private ProjectionList projection() {
		return Projections.projectionList()
				.add(Projections.property("enquadramento"), "ideEnquadramento")
				.add(Projections.property("documentoAto"), "ideDocumentoAto");
	
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoAto buscarDocAtoByEnquadramentoAndDocumentoObrigatorio(List<Enquadramento> enquadramentos, Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoDocumentoAto.class)
				.createAlias("documentoAto", "a", JoinType.LEFT_OUTER_JOIN)
				.createAlias("a.ideTipologia", "b", JoinType.LEFT_OUTER_JOIN)
				.createAlias("a.ideTipoFinalidadeUsoAgua", "c", JoinType.LEFT_OUTER_JOIN)
				.createAlias("a.ideAtoAmbiental", "d", JoinType.LEFT_OUTER_JOIN)
				.createAlias("d.ideTipoAto", "e", JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
						.add(Projections.property("enquadramentoDocumentoAtoPK.ideEnquadramento"), "enquadramentoDocumentoAtoPK.ideEnquadramento")
						.add(Projections.property("enquadramento.ideEnquadramento"), "enquadramento.ideEnquadramento")
						.add(Projections.property("a.indAtivo"), "documentoAto.indAtivo")
						.add(Projections.property("c.ideTipoFinalidadeUsoAgua"), "documentoAto.ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
						.add(Projections.property("d.ideAtoAmbiental"), "documentoAto.ideAtoAmbiental.ideAtoAmbiental")
						.add(Projections.property("e.ideTipoAto"), "documentoAto.ideAtoAmbiental.ideTipoAto.ideTipoAto")
						.add(Projections.property("e.nomTipoAto"), "documentoAto.ideAtoAmbiental.ideTipoAto.nomTipoAto")
						.add(Projections.groupProperty("enquadramento.ideEnquadramento"))
						.add(Projections.groupProperty("a.indAtivo"))
						.add(Projections.groupProperty("c.ideTipoFinalidadeUsoAgua"))
						.add(Projections.groupProperty("d.ideAtoAmbiental"))
						.add(Projections.groupProperty("e.ideTipoAto"))
						.add(Projections.groupProperty("e.nomTipoAto"))
						)
				.add(Restrictions.in("enquadramento", enquadramentos))
				.add(Restrictions.eq("a.ideDocumentoObrigatorio", fce.getIdeDocumentoObrigatorio()));
		if(fce.getIdeDocumentoObrigatorio().getIndAtivo()){
				criteria.add(Restrictions.eq("a.indAtivo", true))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoDocumentoAto.class));
				
				List<EnquadramentoDocumentoAto> listEnquadramentoDocumentoAto = enquadramentoDocumentoAtoDAO.listarPorCriteria(criteria);
				
				if(listEnquadramentoDocumentoAto.size() > 1) {
					for(EnquadramentoDocumentoAto eda : listEnquadramentoDocumentoAto) {
						if(!Util.isNullOuVazio(eda.getEnquadramento().getIdeProcessoReenquadramento())){
							return  eda.getDocumentoAto();
						}
					}
				}
				
				if(!Util.isNullOuVazio(listEnquadramentoDocumentoAto)) {
					return listEnquadramentoDocumentoAto.get(0).getDocumentoAto();
				}
				
				DocumentoAto documentoAto = new DocumentoAto();
				/**
				 * retornar objeto vazio
				 */
				return documentoAto;
		} else {
		
			criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoDocumentoAto.class));
			
			List<EnquadramentoDocumentoAto>	documentoAtos =	enquadramentoDocumentoAtoDAO.listarPorCriteria(criteria); /**buscarPorCriteria(criteria).getDocumentoAto();*/
			
			DocumentoAto documentoAto = null;
			
			if(documentoAtos.size() == 1){
				documentoAto = documentoAtos.iterator().next().getDocumentoAto();
			} else if (documentoAtos.size() > 1){
				for (EnquadramentoDocumentoAto enquadramentoDocumentoAto : documentoAtos) {
					if(enquadramentoDocumentoAto.getDocumentoAto().getIndAtivo()){
						documentoAto = enquadramentoDocumentoAto.getDocumentoAto();
					}
				}
			}
			return documentoAto;
		}			
	}	
}