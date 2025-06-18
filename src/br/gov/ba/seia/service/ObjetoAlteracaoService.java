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
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.LicencaObjetoAlteracao;
import br.gov.ba.seia.entity.ObjetoAlteracao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ObjetoAlteracaoService {

	@Inject
	private IDAO<ObjetoAlteracao> objetoAlteracaoIDAO;

	@Inject
	private IDAO<LicencaObjetoAlteracao> licencaOjetoAlteracaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetoAlteracao> listaTodosAtivos() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetoAlteracao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return objetoAlteracaoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLicencaObjetoAlteracao(LicencaObjetoAlteracao objeto)  {
		licencaOjetoAlteracaoIDAO.salvarOuAtualizar(objeto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLicencaObjetoAlteracaoByLicenca(Licenca licenca)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideLicenca", licenca);
		licencaOjetoAlteracaoIDAO.executarNamedQuery("LicencaObjetoAlteracao.removedByIdeLicenca", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ObjetoAlteracao> ListarObjetoAlteracaoByLicenca(Licenca licenca)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetoAlteracao.class);
		criteria.createAlias("licencaCollection", "licenca");
		criteria.add(Restrictions.eq("licenca.ideLicenca", licenca.getIdeLicenca()));
		return objetoAlteracaoIDAO.listarPorCriteria(criteria);
	}
}
