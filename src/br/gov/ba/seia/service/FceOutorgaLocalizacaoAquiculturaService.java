package br.gov.ba.seia.service;

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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoAquicultura;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 21/11/2014
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaLocalizacaoAquiculturaService {

	@Inject
	private IDAO<FceOutorgaLocalizacaoAquicultura> fceOutorgaLocalizacaoAquiculturaIDAO;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocalizacaoAquicultura(List<FceOutorgaLocalizacaoAquicultura> listaFceOutorgaLocalizacaoAquicultura) {
		fceOutorgaLocalizacaoAquiculturaIDAO.salvarEmLote(listaFceOutorgaLocalizacaoAquicultura);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoAquicultura> listarFceOutorgaLocalizacaoAquiculturaByIdeFceAquicultura(FceAquicultura fceAquicultura) throws CloneNotSupportedException {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoAquicultura.class);
		criteria.createAlias("ideFceOutorgaLocalizacaoGeografica", "folg", JoinType.INNER_JOIN);
		criteria.createAlias("folg.ideFce", "f", JoinType.INNER_JOIN);
		criteria.createAlias("f.ideDadoOrigem", "of", JoinType.INNER_JOIN);
		criteria.createAlias("folg.ideOutorgaLocalizacaoGeografica", "olg", JoinType.INNER_JOIN);
		criteria.createAlias("olg.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN);
		criteria.createAlias("lg.dadoGeograficoCollection", "dg");
		criteria.add(Restrictions.eq("fceAquicultura.ideFceAquicultura", fceAquicultura.getIdeFceAquicultura()));
		List<FceOutorgaLocalizacaoAquicultura> list = fceOutorgaLocalizacaoAquiculturaIDAO.listarPorCriteria(criteria);
		for(FceOutorgaLocalizacaoAquicultura fceOutorgaLocalizacaoGeografica : list) {
			FceOutorgaLocalizacaoGeografica clone = fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica().clone();
			if (Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica().getIdeFceLancamentoEfluente())) {
				fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica().setIdeFceLancamentoEfluente(new FceLancamentoEfluente());
			}
			fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica().getIdeFceLancamentoEfluente().setIdeFceOutorgaLocalizacaoGeografica(new FceOutorgaLocalizacaoGeografica(clone.getIdeFceOutorgaLocalizacaoGeografica()));
			fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica().getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
		}
		return list;
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceOutorgaLocalizacaoAquiculturaByIdeFceAquicultura(FceAquicultura fceAquicultura) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquicultura", fceAquicultura.getIdeFceAquicultura());
		fceOutorgaLocalizacaoAquiculturaIDAO.executarNamedQuery("FceOutorgaLocalizacaoAquicultura.removeByIdeFceAquicultura",parameters);
	}
}