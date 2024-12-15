package br.gov.ba.seia.service;

import java.util.HashMap;
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
import br.gov.ba.seia.entity.CriacaoAnimal;
import br.gov.ba.seia.entity.CriacaoAnimalFinalidadeTipologiaAtividade;
import br.gov.ba.seia.entity.FceAgrossilvopastoril;
import br.gov.ba.seia.entity.Finalidade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CriacaoAnimalService {
	
	@Inject
	private IDAO<CriacaoAnimal> criacaoAnimalIDAO;
	@Inject
	private IDAO<Finalidade> finalidadeIDAO;
	@Inject
	private IDAO<CriacaoAnimalFinalidadeTipologiaAtividade> criacaoAnimalFinalidadeTipoAtividadeIDAO;
	
	/**
	 * @param listaUnidade
	 * @
	 * @INFO Salva uma lista de criacaoAnimal
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaCriacaoAnimal (List<CriacaoAnimal> listaCriacao)  {
		for (CriacaoAnimal criacao : listaCriacao) {
			criacaoAnimalIDAO.salvarOuAtualizar(criacao);
		}
	}
	/**
	 * @param listaUnidade
	 * @
	 * @INFO Salva uma criacaoAnimal
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCriacaoAnimal (CriacaoAnimal criacaoAnimal)  {
		criacaoAnimalIDAO.salvarOuAtualizar(criacaoAnimal);
	}
	/**
	 * @param criacaoAnimal
	 * @
	 * @INFO Exclui a criacao de animal passada pro parametro
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCriacaoAnimal(CriacaoAnimal criacaoAnimal)  {
		criacaoAnimalIDAO.remover(criacaoAnimal);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CriacaoAnimal> buscarCriacaoAnimalByIdeFceAgrossilvo(FceAgrossilvopastoril fceAgrossilvopastoril) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CriacaoAnimal.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril.ideFceAgrossilvopastoril", fceAgrossilvopastoril.getIdeFceAgrossilvopastoril()));
		criteria.createAlias("ideUnidadeTipologiaAtividade", "uta");
		criteria.createAlias("uta.ideTipologiaAtividade", "ta");
		criteria.createAlias("uta.ideUnidadeMedida", "um");
		return criacaoAnimalIDAO.listarPorCriteria(criteria);
	}
	/**
	 * @param criacaoAnimalFinalidadeTipoAtividade
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCriacaoAnimalFinalidadeTipologiaAtividade(CriacaoAnimalFinalidadeTipologiaAtividade criacaoAnimalFinalidadeTipoAtividade) {
		criacaoAnimalFinalidadeTipoAtividadeIDAO.salvarOuAtualizar(criacaoAnimalFinalidadeTipoAtividade);
	}
	/**
	 * @category excluir CriacaoAnimalFinalidadeTipoAtividade pelo Ide da CriacaoAnimal
	 * @param criacaoAnimal
	 * @
	 * @INFO Resumindo irá excluir todas as Finalidades relacionadas a Criacao de Animal passada por parametro
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCriacaoAFTAtividadeByIdeCriacaoAnimal(CriacaoAnimal criacaoAnimal) {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("ideCriacaoAnimal", criacaoAnimal.getIdeCriacaoAnimal());
		criacaoAnimalFinalidadeTipoAtividadeIDAO.executarNamedQuery("CriacaoAnimalFinalidadeTipologiaAtividade.excluirByIdeCriacaoAnimal", maps);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Finalidade> buscarFinalidadeByIdeCriacaoAnimal(CriacaoAnimal criacaoAnimal){
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("ideCriacaoAnimal", criacaoAnimal.getIdeCriacaoAnimal());
		return finalidadeIDAO.buscarPorNamedQuery("Finalidade.findFinalidadesByIdeCriacaoAnimal",maps);
	}
}
