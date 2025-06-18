package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Sistema;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SistemaService {

	@Inject
	private IDAO<Sistema> sistemaDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void adicionarSistema(Sistema sistema) throws Exception {
		sistemaDAO.salvarOuAtualizar(sistema);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Sistema> carregaListaSistema() {
		return sistemaDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Sistema buscarSistemaByNom(String nome) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Sistema.class);
		criteria.add(Restrictions.eq("nomSistema", nome));
		return sistemaDAO.buscarPorCriteria(criteria);
	}

}
