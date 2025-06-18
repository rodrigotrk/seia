package br.gov.ba.seia.service;

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
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;

/**
 *
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 09/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaService {

	@Inject
	private IDAO<FceAquiculturaLicenca> fceAquiculturaLicencaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAquiculturaLicenca buscarFceAquiculturaByFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicenca.class)
				.createAlias("ideFce", "fce")
				.createAlias("fce.ideAnaliseTecnica", "at", JoinType.LEFT_OUTER_JOIN)
				.createAlias("at.ideProcesso", "proc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.eq("fce.ideFce", fce.getIdeFce()));
		
		return fceAquiculturaLicencaIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAquiculturaLicenca(FceAquiculturaLicenca fceAquiculturaLicenca) {
		fceAquiculturaLicencaIDAO.salvarOuAtualizar(fceAquiculturaLicenca);
	}
}