/**
 * 		03/04/14
 * @author eduardo.fernandes
 */
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

import br.gov.ba.seia.controller.FceLancamentoEfluentesController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaracterizacaoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracterizacaoPK;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLancamentoEfluentesCaracterizacaoService {

	@Inject
	private IDAO<FceLancamentoEfluenteCaracterizacao> fceLancamentoEfluentesCaracterizacaoIDAO;

	/**
	 * Passa-se a lista de CaracterizaçãoEfluente escolhida pelo usuário e associa ao FceLancamentoEfluente
	 * @param fceLancamentoEfluente
	 * @param listaCaracterizacaoEfluentes
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararAndSalvarFceLancamentoEfluenteCaracterizacao(FceLancamentoEfluente fceLancamentoEfluente, List<CaracterizacaoEfluente> listaCaracterizacaoEfluentes) {
		for(CaracterizacaoEfluente caracterizacaoEfluente : listaCaracterizacaoEfluentes){
			FceLancamentoEfluenteCaracterizacao efluenteCaracterizacao = new FceLancamentoEfluenteCaracterizacao(new FceLancamentoEfluenteCaracterizacaoPK(fceLancamentoEfluente, caracterizacaoEfluente));
			efluenteCaracterizacao.setIdeFceLancamentoEfluente(fceLancamentoEfluente);
			efluenteCaracterizacao.setIdeCaracterizacaoEfluente(caracterizacaoEfluente);
			salvarFceLancamentoEfluenteCaracterizacao(efluenteCaracterizacao);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceLancamentoEfluenteCaracterizacao(FceLancamentoEfluenteCaracterizacao fceLancamentoEfluenteCaracterizacao) {
		fceLancamentoEfluentesCaracterizacaoIDAO.salvarOuAtualizar(fceLancamentoEfluenteCaracterizacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLancamentoEfluenteCaracterizacao> buscarFceLancamentoEfluenteCaracterizacaoByFceLancEflu(FceLancamentoEfluente fceLancamentoEfluente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLancamentoEfluenteCaracterizacao.class);
		criteria.add(Restrictions.eq("ideFceLancamentoEfluente", fceLancamentoEfluente));
		criteria.setFetchMode("ideFceLancamentoEfluente", FetchMode.JOIN);
		criteria.setFetchMode("ideCaracterizacaoEfluente", FetchMode.JOIN);
		return fceLancamentoEfluentesCaracterizacaoIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceLancamentoEfluentesController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}