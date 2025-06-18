package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.VwDocumentoIdentificacaoObrigatorio;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
public class VwDocumentoIdentificacaoObrigatorioService {

	@SuppressWarnings("unused")
	private static final String DOCUMENTO_OBRIGATORIO_REQUERIMENTO = "DOCUMENTO_OBRIGATORIO_REQUERIMENTO";
	@Inject
	private IDAO<VwDocumentoIdentificacaoObrigatorio> vwDocumentoIdentificacaoObrigatorioDAO;
	@Inject
	private IDAO<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwDocumentoIdentificacaoObrigatorio> buscarDocsObrigatorioReqPorRequerimentoUnico(RequerimentoUnico requerimentoUnico) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(VwDocumentoIdentificacaoObrigatorio.class);
		criteria.add(Restrictions.eq("ideRequerimentoDto", requerimentoUnico.getIdeRequerimentoUnico()));
		return Util.sigletonList(vwDocumentoIdentificacaoObrigatorioDAO.listarPorCriteria(criteria, Order.asc("nomDocumentoDto")));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoObrigatorioRequerimento> buscarDocsFormulariosObrigatorio(RequerimentoUnico requerimentoUnico) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioRequerimento.class)
				.createAlias("ideRequerimento", "req")
				.createAlias("req.requerimentoUnico", "unico")
				.createAlias("unico.enquadramento", "enq")
				.createAlias("enq.atoAmbientalCollection", "ato")
				.createAlias("ideDocumentoObrigatorio", "doc")
				.createAlias("doc.documentoAtoCollection", "da",JoinType.INNER_JOIN);
				
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideDocumentoObrigatorioRequerimento"),"ideDocumentoObrigatorioRequerimento")				
					.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")				
					.add(Projections.property("indDocumentoValidado"),"indDocumentoValidado")				
					.add(Projections.property("dtcValidacao"),"dtcValidacao")
					
					.add(Projections.property("req.ideRequerimento"),"ideRequerimento.ideRequerimento")
					
					.add(Projections.property("doc.ideDocumentoObrigatorio"),"ideDocumentoObrigatorio.ideDocumentoObrigatorio")				
					.add(Projections.property("doc.nomDocumentoObrigatorio"),"ideDocumentoObrigatorio.nomDocumentoObrigatorio")				
					.add(Projections.property("doc.numTamanho"),"ideDocumentoObrigatorio.numTamanho")				
					.add(Projections.property("doc.indFormulario"),"ideDocumentoObrigatorio.indFormulario")				
					.add(Projections.property("doc.dscCaminhoArquivo"),"ideDocumentoObrigatorio.dscCaminhoArquivo")
					
					.add(Projections.property("ato.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
					.add(Projections.property("ato.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
										
		).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioRequerimento.class));
		
		criteria.add(Restrictions.eq("req.ideRequerimento", requerimentoUnico.getIdeRequerimentoUnico()));
		criteria.add(Restrictions.eqProperty("da.atoAmbiental.ideAtoAmbiental", "ato.ideAtoAmbiental"));
		
		 return documentoObrigatorioRequerimentoDAO.listarPorCriteria(criteria, Order.asc("doc.nomDocumentoObrigatorio"));
	}
}
