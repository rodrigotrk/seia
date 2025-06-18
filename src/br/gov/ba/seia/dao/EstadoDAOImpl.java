package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Estado;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EstadoDAOImpl extends AbstractDAO<Estado>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<Estado> dao;
	
	@Override
	protected IDAO<Estado> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Estado> listar(){
		return super.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEstado(Estado estado)  {
		super.salvar(estado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Estado> listarEstado()  {
		return super.listar();
	}
}
