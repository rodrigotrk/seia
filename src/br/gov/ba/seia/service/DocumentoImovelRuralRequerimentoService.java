package br.gov.ba.seia.service;

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
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.DocumentoImovelRuralRequerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
public class DocumentoImovelRuralRequerimentoService {

	@Inject
	private IDAO<DocumentoImovelRural> documentoImovelRuralDAO;
	
	@Inject
	private IDAO<DocumentoImovelRuralRequerimento> documentoImovelRuralRequerimentoDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoImovelRural> listarDocumentoImovelRuralByEmpreendimento(Integer ideEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoImovelRural.class)
			.createAlias("imovelRural", "irProc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("irProc.imovel", "iProc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("iProc.empreendimentoCollection", "eProc", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("documentoImovelRuralPosse", "dPosse", JoinType.LEFT_OUTER_JOIN)
			.createAlias("dPosse.ideImovelRural", "irPosse", JoinType.LEFT_OUTER_JOIN)
			.createAlias("irPosse.imovel", "iPosse", JoinType.LEFT_OUTER_JOIN)
			.createAlias("iPosse.empreendimentoCollection", "ePosse", JoinType.LEFT_OUTER_JOIN)
			
			
			.add(Restrictions.or(
					Restrictions.eq("ePosse.ideEmpreendimento", ideEmpreendimento),
					Restrictions.eq("eProc.ideEmpreendimento", ideEmpreendimento)
				)
			)
			
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideDocumentoImovelRural"),"ideDocumentoImovelRural")
				.add(Projections.groupProperty("nomDocumentoObrigatorio"),"nomDocumentoObrigatorio")
				.add(Projections.groupProperty("dscCaminhoArquivo"),"dscCaminhoArquivo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoImovelRural.class))
		;
		
		return documentoImovelRuralDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoImovelRuralRequerimento> listarDocumentoImovelRuralReqByRequerimento(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoImovelRuralRequerimento.class)
				.createAlias("ideRequerimento", "requerimento")
				.createAlias("ideDocumentoImovelRural", "documentoImovelRural")
				.createAlias("documentoImovelRural.imovelRural", "imovelRural")
				.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));

		return documentoImovelRuralRequerimentoDAO.listarPorCriteria(criteria, Order.asc("ideDocumentoImovelRuralRequerimento"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarListaDocumentoImovelRuralRequerimento(List<DocumentoImovelRuralRequerimento> listDocImovelRuralReq)  {
		documentoImovelRuralRequerimentoDAO.salvarEmLote(listDocImovelRuralReq);
	}

}
