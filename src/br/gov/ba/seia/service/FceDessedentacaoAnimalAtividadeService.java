package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceDessedentacaoAnimal;
import br.gov.ba.seia.entity.FceDessedentacaoAnimalAtividade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceDessedentacaoAnimalAtividadeService {

	@Inject
	private IDAO<FceDessedentacaoAnimalAtividade> fceDessedentacaoAnimalAtividadeIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceDessedentacaoAnimalAtividade> buscarFceDessedentacaoAnimalAtividadeByIdeFceDesAnimal (FceDessedentacaoAnimal fceDessedentacaoAnimal) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceDessedentacaoAnimalAtividade.class);
		criteria.add(Restrictions.eq("ideFceDessedentacaoAnimal", fceDessedentacaoAnimal));
		criteria.setFetchMode("ideTipologiaAtividade", FetchMode.JOIN);
		return fceDessedentacaoAnimalAtividadeIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceDessedentacaoAnimalAtividade (List<FceDessedentacaoAnimalAtividade> listFceDessedentacaoAnimalAtividade) {
		for(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividade : listFceDessedentacaoAnimalAtividade){
			fceDessedentacaoAnimalAtividadeIDAO.salvarOuAtualizar(fceDessedentacaoAnimalAtividade);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceDessedentacaoAnimalAtividadeAntigo (List<FceDessedentacaoAnimalAtividade> listFceDessedentacaoAnimalAtividade)  {
		for(FceDessedentacaoAnimalAtividade fceDessedentacaoAnimalAtividade : listFceDessedentacaoAnimalAtividade){
			fceDessedentacaoAnimalAtividadeIDAO.remover(fceDessedentacaoAnimalAtividade);
		}
	}
}
