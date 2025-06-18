package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class DocumentoIdentificacaoDAOImpl {
	
	@Inject
	private IDAO<DocumentoIdentificacao> documentoIdentificacaoDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoIdentificacao> listar(Pessoa pessoa) {
	    return 
    		documentoIdentificacaoDAO
    			.listarPorCriteria(
					DetachedCriteria.forClass(DocumentoIdentificacao.class)
					.createAlias("idePessoa", "idePessoa", JoinType.INNER_JOIN)
    					.createAlias("ideOrgaoExpedidor", "ideOrgaoExpedidor", JoinType.INNER_JOIN)
    					.createAlias("ideTipoIdentificacao", "ideTipoIdentificacao", JoinType.INNER_JOIN)
    					.createAlias("ideEstado", "ideEstado", JoinType.INNER_JOIN)
						
    					.add(Restrictions.eq("idePessoa.idePessoa", pessoa.getIdePessoa()))
    					.add(Restrictions.eq("indExcluido", false))
    					
    					.setProjection(Projections.projectionList()
							.add(Projections.property("ideDocumentoIdentificacao"),"ideDocumentoIdentificacao")
							.add(Projections.property("numDocumento"),"numDocumento")
							.add(Projections.property("numSerie"),"numSerie")
							.add(Projections.property("dtcCriacao"),"dtcCriacao")
							.add(Projections.property("indExcluido"),"indExcluido")
							.add(Projections.property("dtcEmissao"),"dtcEmissao")
							.add(Projections.property("dtcValidade"),"dtcValidade")
							.add(Projections.property("dtcExclusao"),"dtcExclusao")
							.add(Projections.property("orgExpedidorOutros"),"orgExpedidorOutros")
							.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")

							.add(Projections.property("idePessoa.idePessoa"),"idePessoa.idePessoa")
								
							.add(Projections.property("ideTipoIdentificacao.ideTipoIdentificacao"),"ideTipoIdentificacao.ideTipoIdentificacao")
							.add(Projections.property("ideTipoIdentificacao.nomTipoIdentificacao"),"ideTipoIdentificacao.nomTipoIdentificacao")
							.add(Projections.property("ideOrgaoExpedidor.ideOrgaoExpedidor"),"ideOrgaoExpedidor.ideOrgaoExpedidor")
							.add(Projections.property("ideEstado.ideEstado"),"ideEstado.ideEstado")

						).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoIdentificacao.class)));	
	}
	
	

	public List<DocumentoIdentificacao> listarDocumentosIdentificacaoPorPessoa(Pessoa pessoa)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoIdentificacao.class)
			.setFetchMode("ideOrgaoExpedidor", FetchMode.JOIN)
			.setFetchMode("ideTipoIdentificacao", FetchMode.JOIN)
	        .setFetchMode("ideEstado", FetchMode.JOIN)
	        .createAlias("idePessoa", "pessoa")
	        .add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()))
	        .add(Restrictions.eq("indExcluido", false));
	        			       
	    return documentoIdentificacaoDAO.listarPorCriteria(criteria);
	}
	
	public List<DocumentoIdentificacao> listarDocumentosIdentificacaoRequerente(Integer ideRequerimento)  {
		DetachedCriteria subCriteria = DetachedCriteria.forClass(DocumentoIdentificacao.class)
				.createAlias("ideOrgaoExpedidor", "ideOrgaoExpedidor",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoIdentificacao", "ideTipoIdentificacao")
				.createAlias("ideEstado", "ideEstado",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoa", "pessoa")
				.createAlias("pessoa.requerimentoPessoaCollection", "rp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rp.ideTipoPessoaRequerimento", "tp", JoinType.INNER_JOIN)
				.createAlias("rp.requerimento", "req")
				
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("indExcluido", false))
				.add(Restrictions.ne("tp.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.ATENDENTE.getId()))
				
				.setProjection(Projections.projectionList()
						.add(Projections.distinct(Projections.groupProperty("ideTipoIdentificacao.nomTipoIdentificacao")))
						.add(Projections.groupProperty("ideTipoIdentificacao.ideTipoIdentificacao"),"ideTipoIdentificacao.ideTipoIdentificacao")
						.add(Projections.max("ideDocumentoIdentificacao"),"ideDocumentoIdentificacao")
				)
				
				.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoIdentificacao.class))
			;
		
		
		List<Integer> documentoId = new ArrayList<Integer>();
		
		for(DocumentoIdentificacao doc:documentoIdentificacaoDAO.listarPorCriteria(subCriteria)) {
			documentoId.add(doc.getIdeDocumentoIdentificacao());
		}
 
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoIdentificacao.class)
				.createAlias("ideOrgaoExpedidor", "ideOrgaoExpedidor",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoIdentificacao", "ideTipoIdentificacao")
				.createAlias("ideEstado", "ideEstado",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoa", "pessoa")
				.createAlias("pessoa.requerimentoPessoaCollection", "rp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rp.ideTipoPessoaRequerimento", "tp", JoinType.INNER_JOIN)
				.createAlias("rp.requerimento", "req");
				
				if(!Util.isNullOuVazio(documentoId)) {
					criteria.add(Restrictions.in("ideDocumentoIdentificacao", documentoId));			
				}
				
					criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				  	.add(Restrictions.eq("indExcluido", false))
				    .add(Restrictions.ne("tp.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.ATENDENTE.getId()))
					
				.setProjection(Projections.projectionList()
						.add(Projections.distinct(Projections.property("ideDocumentoIdentificacao")))
						.add(Projections.property("ideDocumentoIdentificacao"),"ideDocumentoIdentificacao")
						.add(Projections.property("numDocumento"),"numDocumento")
						.add(Projections.property("numSerie"),"numSerie")
						.add(Projections.property("dtcCriacao"),"dtcCriacao")
						.add(Projections.property("indExcluido"),"indExcluido")
						.add(Projections.property("dtcEmissao"),"dtcEmissao")
						.add(Projections.property("dtcValidade"),"dtcValidade")
						.add(Projections.property("dtcExclusao"),"dtcExclusao")
						.add(Projections.property("orgExpedidorOutros"),"orgExpedidorOutros")
						.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
						.add(Projections.property("ideTipoIdentificacao.ideTipoIdentificacao"),"ideTipoIdentificacao.ideTipoIdentificacao")
						.add(Projections.property("ideTipoIdentificacao.nomTipoIdentificacao"),"ideTipoIdentificacao.nomTipoIdentificacao")
						.add(Projections.property("pessoa.idePessoa"),"idePessoa.idePessoa")
						.add(Projections.property("ideOrgaoExpedidor.ideOrgaoExpedidor"),"ideOrgaoExpedidor.ideOrgaoExpedidor")
						.add(Projections.property("ideEstado.ideEstado"),"ideEstado.ideEstado")
				)
				
				.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoIdentificacao.class))
			;
		
 
		
		return new ArrayList<DocumentoIdentificacao>();
	}
	
	public void salvarDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		documentoIdentificacaoDAO.salvarOuAtualizar(documentoIdentificacao);
	}	
	
	public void excluirDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		documentoIdentificacaoDAO.atualizar(documentoIdentificacao);
	}
	
	public DocumentoIdentificacao recuperarDocumentoById(DocumentoIdentificacao documentoIdentificacao)  {
		Map<String, Object> paramDocumentoIdentificacao = new HashMap<String, Object>();
		paramDocumentoIdentificacao.put("ideDocumentoIdentificacao", documentoIdentificacao.getIdeDocumentoIdentificacao());
		paramDocumentoIdentificacao.put("indExcluido", false);
		return documentoIdentificacaoDAO.buscarEntidadePorNamedQuery("DocumentoIdentificacao.findByIdeDocumentoIdentificacao", paramDocumentoIdentificacao);
	}

}
