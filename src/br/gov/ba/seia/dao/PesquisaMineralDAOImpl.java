package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.PesquisaMineral;

/**
 * 
 * @author eduardo.fernandes 
 * @since 01/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a> 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralDAOImpl {

	@Inject
	private IDAO<PesquisaMineral> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineral pesquisaMineral)  {
		dao.salvarOuAtualizar(pesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PesquisaMineral buscar(CadastroAtividadeNaoSujeitaLic cadastro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PesquisaMineral.class)
				.createAlias("cadastroAtividadeNaoSujeitaLic", "cad")
				.add(Restrictions.eq("cad.ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
		return dao.buscarPorCriteria(criteria);
	}

}
