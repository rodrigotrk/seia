package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.AtividadeNaoSujeitaLicenciamentoDocumento;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;

/**
 * @author eduardo.fernandes 
 * @since 24/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>  ADICIONAR TICKET
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtividadeNaoSujeitaLicenciamentoDocumentoDAOImpl {

	@Inject
	private IDAO<AtividadeNaoSujeitaLicenciamentoDocumento> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtividadeNaoSujeitaLicenciamentoDocumento> listarByAtividade(TipoAtividadeNaoSujeitaLicenciamentoEnum atividadeNaoSujeitaLicenciamentoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtividadeNaoSujeitaLicenciamentoDocumento.class)
				.createAlias("ideDocumentoObrigatorio", "doc")
				.add(Restrictions.eq("tipoAtividadeNaoSujeitaLicenciamento.ideTipoAtividadeNaoSujeitaLicenciamento", atividadeNaoSujeitaLicenciamentoEnum.getIde()));
		return dao.listarPorCriteria(criteria, Order.asc("ideAtividadeNaoSujeitaLicenciamentoDocumento"));
	}
}
