package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.inject.Inject;

import br.gov.ba.seia.entity.FormaManejo;

public class FormaManejoDAOImpl {

	@Inject
	private IDAO<FormaManejo> formaManejoDAO;

	public void salvarFormaManejo(FormaManejo formaManejo)  {
		formaManejoDAO.salvarOuAtualizar(formaManejo);
	}

	public Collection<FormaManejo> listaFormaManejo() {
		return formaManejoDAO.listarTodos();
	}

	public FormaManejo carregarFormaManejo(Integer id){
		return formaManejoDAO.carregarGet(id);
	}

}