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

import br.gov.ba.seia.entity.DocumentoRequerimentoEmpreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoRequerimentoEmpreendimentoDAOImpl {

	@Inject
	private IDAO<DocumentoRequerimentoEmpreendimento> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoRequerimentoEmpreendimento(DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento) {
		
		idao.salvarOuAtualizar(documentoRequerimentoEmpreendimento);
	}
	
	public List<DocumentoRequerimentoEmpreendimento> listarDocumentoRequerimentoEmpreendimento(EmpreendimentoRequerimento ideEmpreendimentoRequerimento){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoRequerimentoEmpreendimento.class, "dRE");
		
		criteria.add(Restrictions.eq("dRE.ideEmpreendimentoRequerimento.ideEmpreendimentoRequerimento", ideEmpreendimentoRequerimento.getIdeEmpreendimentoRequerimento()));
		
		return idao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoRequerimentoEmpreendimentoEmLote(List<DocumentoRequerimentoEmpreendimento> documentoRequerimentoEmpreendimentos){
		
		idao.salvarEmLote(documentoRequerimentoEmpreendimentos);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumentoRequerimentoEmpreendimento(DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideDocumentoRequerimentoEmpreendimento", documentoRequerimentoEmpreendimento.getIdeDocumentoRequerimentoEmpreendimento());
		idao.executarNamedQuery("DocumentoRequerimentoEmpreendimento.removeByIde", parameters);
	}
	
	public List<DocumentoRequerimentoEmpreendimento> listarDocumentoRequerimentoPorRequerimento(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoRequerimentoEmpreendimento.class, "dRE");
		
		criteria.createAlias("dRE.ideEmpreendimentoRequerimento", "eR", JoinType.INNER_JOIN);
		
		criteria.createAlias("eR.ideRequerimento", "r", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		
		return 	idao.listarPorCriteria(criteria);
	}
}
