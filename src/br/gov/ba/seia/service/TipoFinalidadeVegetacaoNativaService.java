package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoFinalidadeVegetacaoNativa;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoFinalidadeVegetacaoNativaService {

	@Inject
	IDAO<TipoFinalidadeVegetacaoNativa> tipoFinalidadeVegetacaoNativaDAO;

	public Collection<TipoFinalidadeVegetacaoNativa> listarTipoFinalidadeVegetacaoNativa() {
		return tipoFinalidadeVegetacaoNativaDAO.listarTodos();
	}	
}
