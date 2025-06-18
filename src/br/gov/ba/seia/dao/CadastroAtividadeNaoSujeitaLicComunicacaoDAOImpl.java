package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicComunicacao;

/**
 * @author eduardo.fernandes
 * @since 02/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8193">#8193</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicComunicacaoDAOImpl {

	@Inject
	private IDAO<CadastroAtividadeNaoSujeitaLicComunicacao> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicComunicacao> listar(CadastroAtividadeNaoSujeitaLic cadastro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicComunicacao.class)
				.add(Restrictions.eq("cadastroAtividadeNaoSujeitaLic.ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicComunicacao comunicacao) {
		dao.salvarOuAtualizar(comunicacao);
	}
}
