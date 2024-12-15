package br.gov.ba.seia.service;

import java.util.Collection;
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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Enquadramento;

/**
 * @author mario.junior
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoImovelRuralService {

	@Inject
	private IDAO<DocumentoImovelRural> documentoObrigatorioDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarDocumentoObrigatorio(DocumentoImovelRural docObrigatorio) {
		documentoObrigatorioDAO.salvarOuAtualizar(docObrigatorio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoObrigatorio(DocumentoImovelRural docObrigatorio) {
		documentoObrigatorioDAO.salvar(docObrigatorio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarDocumentoObrigatorio(DocumentoImovelRural docObrigatorio)  {
		documentoObrigatorioDAO.atualizar(docObrigatorio);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoImovelRural obterDocumentoObrigatorio(DocumentoImovelRural docObrigatorio) {
		return documentoObrigatorioDAO.carregarLoad(docObrigatorio.getIdeDocumentoObrigatorio());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoImovelRural carregarDocumentoObrigatorio(Integer idDocObrigatorio){
		return documentoObrigatorioDAO.carregarGet(idDocObrigatorio);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoImovelRural obterDocumentoObrigatorioCriteria(DocumentoObrigatorio docObrigatorio)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorio.class);
		criteria.add(Restrictions.eq("ideDocumentoObrigatorio", docObrigatorio.getIdeDocumentoObrigatorio()));
		return documentoObrigatorioDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoObrigatorioEmLotes(List<DocumentoObrigatorio> newFomularios, Enquadramento enquadramento)  {
		for (DocumentoObrigatorio documentoObrigatorio : newFomularios) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideDocumentoObrigatorio", documentoObrigatorio.getIdeDocumentoObrigatorio());
			params.put("ideEnquadramento", enquadramento.getIdeEnquadramento());
			documentoObrigatorioDAO.executarNamedQuery("DocumentoObrigatorio.inserirEnquadramentoDocumento", params);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoObrigatorio(DocumentoImovelRural documentoObrigatorio)  {
		documentoObrigatorio.setImovelRural(null);
		documentoObrigatorioDAO.remover(documentoObrigatorio);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoImovelRural> listarDocumentosObrigatorios(Map<String, Object> params)  {
		String sql;
		if (params.isEmpty()) {
			sql = "select distinct d " + "from DocumentoObrigatorio d ";
		} else {
			sql = "select distinct d " + "from DocumentoObrigatorio d where upper(d.nomDocumentoObrigatorio) like upper(:nomeDocumento)";
		}
		return 	documentoObrigatorioDAO.listarPorQuery(sql, params);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoImovelRural> listarTodosDocumentosObrigatorios()  {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select d ");
		sql.append("from DocumentoObrigatorio d ");
		sql.append("order by d.nomDocumentoObrigatorio asc");
		
		return documentoObrigatorioDAO.listarPorQuery(sql.toString(), null);
	}
}
