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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;

/**
 * 
 * 
 * @author eduardo.fernandes
 * @since 02/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a> ADICIONAR TICKET
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicDocApensadoDAOImpl {

	@Inject
	private IDAO<CadastroAtividadeNaoSujeitaLicDocApensado> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicDocApensado> listar(CadastroAtividadeNaoSujeitaLic cadastro)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicDocApensado.class)
				.createAlias("ideDocumentoObrigatorio", "doc")
				.createAlias("cadastroAtividadeNaoSujeitaLic", "cadastro")
				.createAlias("cadastro.idePessoaFisicaCadastro", "pf")
				.add(Restrictions.eq("cadastro.ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
		return dao.listarPorCriteria(criteria, Order.asc("doc.ideDocumentoObrigatorio"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicDocApensado doc)  {
		dao.salvarOuAtualizar(doc);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<CadastroAtividadeNaoSujeitaLicDocApensado> lista)  {
		dao.salvarEmLote(lista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Object object) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String namedQuery = "CadastroAtividadeNaoSujeitaLicDocApensado.removeByIde";
		if (object instanceof CadastroAtividadeNaoSujeitaLic) {
			parameters.put("ideCadastroAtividadeNaoSujeitaLic", ((CadastroAtividadeNaoSujeitaLic) object).getIdeCadastroAtividadeNaoSujeitaLic());
			namedQuery += "CadastroAtividade";
		}
		else if (object instanceof CadastroAtividadeNaoSujeitaLicDocApensado) {
			parameters.put("ideCadastroAtividadeNaoSujeitaLicDocApensado", ((CadastroAtividadeNaoSujeitaLicDocApensado) object).getIdeCadastroAtividadeNaoSujeitaLicDocApensado());
		}
		dao.executarNamedQuery(namedQuery, parameters);
	}

}
