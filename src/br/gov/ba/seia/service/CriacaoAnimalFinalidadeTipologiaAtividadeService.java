package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CriacaoAnimalFinalidadeTipologiaAtividade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CriacaoAnimalFinalidadeTipologiaAtividadeService {

	@Inject
	private IDAO<CriacaoAnimalFinalidadeTipologiaAtividade> criacaoAnimalFinalidadeTipologiaAtividadeIDAO;


	/**
	 * @param listaUnidade
	 * @
	 * @INFO Salva uma lista de CriacaoAnimalFinalidadeTipoAtividade
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaCriacaoAnimalFinalidadeTipologiaAtividade (List<CriacaoAnimalFinalidadeTipologiaAtividade> listaCriacao)  {
		for (CriacaoAnimalFinalidadeTipologiaAtividade criacao : listaCriacao) {
			criacaoAnimalFinalidadeTipologiaAtividadeIDAO.salvarOuAtualizar(criacao);
		}
		
	}
	/**
	 * @param listaUnidade
	 * @
	 * @INFO Salva uma CriacaoAnimalFinalidadeTipoAtividade
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCriacaoAnimalFinalidadeTipologiaAtividade (CriacaoAnimalFinalidadeTipologiaAtividade criacao)  {
		criacaoAnimalFinalidadeTipologiaAtividadeIDAO.salvarOuAtualizar(criacao);
	}

}
