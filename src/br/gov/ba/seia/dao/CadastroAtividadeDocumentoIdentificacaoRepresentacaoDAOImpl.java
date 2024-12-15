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

import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicStatus;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 19/12/2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeDocumentoIdentificacaoRepresentacaoDAOImpl {

	@Inject
	private IDAO<CadastroAtividadeDocumentoIdentificacaoRepresentacao> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listar(CadastroAtividadeNaoSujeitaLic cadastro) {
		return dao.listarPorCriteria(getCriteria(cadastro));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listarDocumentoIdentificacao(CadastroAtividadeNaoSujeitaLic cadastro) {
		DetachedCriteria criteria = getCriteria(cadastro)
				.add(Restrictions.isNotNull("docIden"));
		return dao.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listarDocumentoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro) {
		DetachedCriteria criteria = getCriteria(cadastro)
				.add(Restrictions.or(Restrictions.isNotNull("ideRepresentanteLegal"), Restrictions.isNotNull("ideProcuradorRepEmpreendimento")));
		return dao.listarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteria(CadastroAtividadeNaoSujeitaLic cadastro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeDocumentoIdentificacaoRepresentacao.class)
				.createAlias("ideCadastroAtividadeNaoSujeitaLic", "cadastro")
				.createAlias("ideDocumentoIdentificacao", "docIden", JoinType.LEFT_OUTER_JOIN)
				.createAlias("docIden.ideTipoIdentificacao", "tipoIden", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideProcuradorRepEmpreendimento", "docProcurador", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideRepresentanteLegal", "docRepresentante", JoinType.LEFT_OUTER_JOIN);
				CadastroAtividadeNaoSujeitaLicStatus ultimoStatus = cadastro.getCadastroUltimoStatus();
				if(!Util.isNull(ultimoStatus) && !Util.isNull(ultimoStatus.getCadastroAtividadeNaoSujeitaLicTipoStatus())){
					if(!ultimoStatus.getCadastroAtividadeNaoSujeitaLicTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO.getIde())){
						criteria.add(Restrictions.or(Restrictions.isNull("docIden.ideDocumentoIdentificacao"), Restrictions.eq("docIden.indExcluido", false)));
					} 
					else {
						criteria.add(Restrictions.eq("indDocumentoValidado", true));
					}
				}
				criteria.add(Restrictions.eq("cadastro.ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
		return criteria;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc) {
		dao.salvarOuAtualizar(doc);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> lista) {
		dao.salvarEmLote(lista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Object object) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String namedQuery = "CadastroAtividadeDocumentoIdentificacaoRepresentacao.removeByIde";
		if (object instanceof CadastroAtividadeNaoSujeitaLic) {
			parameters.put("ideCadastroAtividadeNaoSujeitaLic", ((CadastroAtividadeNaoSujeitaLic) object).getIdeCadastroAtividadeNaoSujeitaLic());
			namedQuery += "CadastroAtividade";
		}
		else if (object instanceof CadastroAtividadeNaoSujeitaLicDocApensado) {
			parameters.put("ideCadastroAtividadeDocumentoIdentificacaoRepresentacao", ((CadastroAtividadeNaoSujeitaLicDocApensado) object).getIdeCadastroAtividadeNaoSujeitaLicDocApensado());
		}
		dao.executarNamedQuery(namedQuery, parameters);
	}
	
	public void excluirDocumentoIdentificacaoExcluido(CadastroAtividadeNaoSujeitaLic cadastro) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
		parameters.put("indExcluido", true);
		dao.executarNamedQuery("CadastroAtividadeDocumentoIdentificacaoRepresentacao.removeByIdeCadastroAtividadeDocIdentificacaoExcluido", parameters);
	}
}
