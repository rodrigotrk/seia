package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.DisposicaoFinalResiduoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DisposicaoFinalResiduo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DisposicaoFinalResiduoService {
	
	@Inject
	DisposicaoFinalResiduoDAOImpl disposicaoFinalResiduoDAOImpl;
	
	@Inject
	private IDAO<DisposicaoFinalResiduo> disposicaoFinalResiduoDAO;
	
	public Collection<DisposicaoFinalResiduo> listarTodos()  {
		return this.disposicaoFinalResiduoDAOImpl.listarTodos();
	}

	public DisposicaoFinalResiduo carregar(Integer ideDisposicaoFinalResiduo){
		return this.disposicaoFinalResiduoDAO.carregarGet(ideDisposicaoFinalResiduo);
	}
	
}
