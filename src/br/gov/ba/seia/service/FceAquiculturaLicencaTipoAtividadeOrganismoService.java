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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeOrganismo;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;

/**
 * Service de {@link FceAquiculturaLicencaTipoAtividadeOrganismo}.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 10/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaTipoAtividadeOrganismoService {
	@Inject
	private IDAO<FceAquiculturaLicencaTipoAtividadeOrganismo> fceAquiculturaLicencaTipoAtividadeOrganismoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividadeOrganismo> listarFceAquiculturaLicencaTipoAtividadeOrganismoBy(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeOrganismo.class)
				.createAlias("fceAquiculturaLicencaTipoAtividade", "fceAquicLicTipoAtividade")
				.createAlias("organismo", "org")
				.add(Restrictions.eq("fceAquicLicTipoAtividade.ideFceAquiculturaLicencaTipoAtividade", fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicencaTipoAtividade()));
		return fceAquiculturaLicencaTipoAtividadeOrganismoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLicencaTipoAtividadeOrganismo(List<FceAquiculturaLicencaTipoAtividadeOrganismo> listaToSalvar) {
		fceAquiculturaLicencaTipoAtividadeOrganismoIDAO.salvarEmLote(listaToSalvar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaTipoAtividadeOrganismoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaAtividadeTipoProducao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicencaTipoAtividadeTipoProducao", fceAquiculturaLicencaAtividadeTipoProducao.getIdeFceAquiculturaLicencaTipoAtividadeTipoProducao());
		fceAquiculturaLicencaTipoAtividadeOrganismoIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividadeOrganismo.removeByIdeFceAquiculturaLicencaTipoAtividadeTipoProducao", parameters);
	}
}